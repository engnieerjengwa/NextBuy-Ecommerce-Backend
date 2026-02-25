package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendReceiptEmail(String to, String subject, String customerName, 
                               String orderTrackingNumber, double totalPrice, int totalQuantity) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            
            String formattedDate = new SimpleDateFormat("MMMM dd, yyyy").format(new Date());
            String formattedPrice = "%.2f".formatted(totalPrice);
            
            String htmlContent = 
                "<html>" +
                "<head>" +
                "  <style>" +
                "    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                "    .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "    .header { background-color: #4CAF50; color: white; padding: 10px; text-align: center; }" +
                "    .content { padding: 20px; border: 1px solid #ddd; }" +
                "    .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #777; }" +
                "    .order-details { margin: 20px 0; }" +
                "    .order-details table { width: 100%; border-collapse: collapse; }" +
                "    .order-details th, .order-details td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='header'>" +
                "      <h2>Order Confirmation</h2>" +
                "    </div>" +
                "    <div class='content'>" +
                "      <p>Dear " + customerName + ",</p>" +
                "      <p>Thank you for your purchase! Your order has been successfully processed.</p>" +
                "      <div class='order-details'>" +
                "        <h3>Order Details:</h3>" +
                "        <table>" +
                "          <tr><th>Order Date:</th><td>" + formattedDate + "</td></tr>" +
                "          <tr><th>Order Number:</th><td>" + orderTrackingNumber + "</td></tr>" +
                "          <tr><th>Total Items:</th><td>" + totalQuantity + "</td></tr>" +
                "          <tr><th>Total Amount:</th><td>$" + formattedPrice + "</td></tr>" +
                "        </table>" +
                "      </div>" +
                "      <p>You can track your order status using the order number above.</p>" +
                "      <p>If you have any questions about your order, please contact our customer service.</p>" +
                "      <p>Thank you for shopping with us!</p>" +
                "    </div>" +
                "    <div class='footer'>" +
                "      <p>This is an automated email. Please do not reply to this message.</p>" +
                "      <p>&copy; " + java.time.Year.now().getValue() + " NexBuy. All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
            
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            
        } catch (MessagingException e) {
            // Log the error but don't throw it to prevent disrupting the checkout process
            System.err.println("Failed to send receipt email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}