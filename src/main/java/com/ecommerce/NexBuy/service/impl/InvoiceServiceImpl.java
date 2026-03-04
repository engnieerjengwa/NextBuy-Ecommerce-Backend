package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.entity.Address;
import com.ecommerce.NexBuy.entity.Order;
import com.ecommerce.NexBuy.entity.OrderItem;
import com.ecommerce.NexBuy.repo.OrderRepository;
import com.ecommerce.NexBuy.service.InvoiceService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final OrderRepository orderRepository;

    public InvoiceServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public byte[] generateInvoicePdf(Long orderId, String customerEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (!order.getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
            throw new IllegalArgumentException("You can only download invoices for your own orders");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fonts
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, new Color(44, 62, 80));
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font smallFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.GRAY);

            // Title
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Invoice details
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            addInfoCell(infoTable, "Invoice #:", "INV-" + order.getId(), headerFont, normalFont);
            addInfoCell(infoTable, "Date:", order.getDateCreated().format(DATE_FORMAT), headerFont, normalFont);
            addInfoCell(infoTable, "Order #:", order.getOrderTrackingNumber(), headerFont, normalFont);
            addInfoCell(infoTable, "Status:", order.getStatus(), headerFont, normalFont);
            document.add(infoTable);
            document.add(new Paragraph(" "));

            // Billing Address
            Address billing = order.getBillingAddress();
            if (billing != null) {
                document.add(new Paragraph("Bill To:", headerFont));
                document.add(new Paragraph(
                        billing.getStreet() + ", " + billing.getCity() + ", " +
                                billing.getState() + " " + billing.getZipCode() + ", " + billing.getCountry(),
                        normalFont));
                document.add(new Paragraph(" "));
            }

            // Items table
            PdfPTable itemTable = new PdfPTable(4);
            itemTable.setWidthPercentage(100);
            itemTable.setWidths(new float[]{1f, 4f, 1.5f, 2f});

            addTableHeader(itemTable, "#", headerFont);
            addTableHeader(itemTable, "Item", headerFont);
            addTableHeader(itemTable, "Qty", headerFont);
            addTableHeader(itemTable, "Amount", headerFont);

            int index = 1;
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    addTableCell(itemTable, String.valueOf(index++), normalFont);
                    addTableCell(itemTable, "Product #" + item.getProductId(), normalFont);
                    addTableCell(itemTable, String.valueOf(item.getQuantity()), normalFont);
                    BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    addTableCell(itemTable, "$" + lineTotal.toPlainString(), normalFont);
                }
            }

            document.add(itemTable);
            document.add(new Paragraph(" "));

            // Total
            Paragraph total = new Paragraph("Total: $" + order.getTotalPrice().toPlainString(), titleFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank you for shopping with NexBuy!", smallFont));

            document.close();
            logger.info("Invoice PDF generated for order {}", orderId);
            return baos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating invoice PDF for order {}", orderId, e);
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }

    private void addInfoCell(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(0);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A", valueFont));
        valueCell.setBorder(0);
        table.addCell(valueCell);
    }

    private void addTableHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(52, 73, 94));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Font whiteFont = new Font(font.getFamily(), font.getSize(), font.getStyle(), Color.WHITE);
        cell.setPhrase(new Phrase(text, whiteFont));
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
