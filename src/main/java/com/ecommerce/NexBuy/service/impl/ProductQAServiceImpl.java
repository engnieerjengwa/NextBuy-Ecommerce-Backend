package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.ProductAnswerRequestDto;
import com.ecommerce.NexBuy.dto.request.ProductQuestionRequestDto;
import com.ecommerce.NexBuy.dto.response.ProductAnswerResponseDto;
import com.ecommerce.NexBuy.dto.response.ProductQuestionResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.entity.ProductAnswer;
import com.ecommerce.NexBuy.entity.ProductQuestion;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.ProductAnswerRepository;
import com.ecommerce.NexBuy.repo.ProductQuestionRepository;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.service.ProductQAService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProductQAServiceImpl implements ProductQAService {

    private static final Logger logger = LoggerFactory.getLogger(ProductQAServiceImpl.class);

    private final ProductQuestionRepository questionRepository;
    private final ProductAnswerRepository answerRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public ProductQAServiceImpl(ProductQuestionRepository questionRepository,
                                ProductAnswerRepository answerRepository,
                                ProductRepository productRepository,
                                CustomerRepository customerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<ProductQuestionResponseDto> getQuestionsByProductId(Long productId, Pageable pageable) {
        return questionRepository.findByProductIdOrderByDateCreatedDesc(productId, pageable)
                .map(this::mapQuestionToDto);
    }

    @Override
    @Transactional
    public ProductQuestionResponseDto askQuestion(String customerEmail, ProductQuestionRequestDto requestDto) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + requestDto.getProductId()));

        ProductQuestion question = new ProductQuestion();
        question.setProduct(product);
        question.setCustomer(customer);
        question.setQuestion(requestDto.getQuestion());

        ProductQuestion saved = questionRepository.save(question);
        logger.info("Question asked for product {} by customer {}", product.getId(), customerEmail);
        return mapQuestionToDto(saved);
    }

    @Override
    @Transactional
    public ProductAnswerResponseDto answerQuestion(String customerEmail, ProductAnswerRequestDto requestDto) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        ProductQuestion question = questionRepository.findById(requestDto.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found with ID: " + requestDto.getQuestionId()));

        ProductAnswer answer = new ProductAnswer();
        answer.setQuestion(question);
        answer.setAnsweredByCustomerId(customer.getId());
        answer.setAnswer(requestDto.getAnswer());

        ProductAnswer saved = answerRepository.save(answer);

        // Mark question as answered
        question.setIsAnswered(true);
        questionRepository.save(question);

        logger.info("Question {} answered by customer {}", question.getId(), customerEmail);
        return mapAnswerToDto(saved, customer.getFirstName());
    }

    @Override
    @Transactional
    public void markAnswerHelpful(Long answerId) {
        ProductAnswer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found with ID: " + answerId));
        answer.setHelpfulCount(answer.getHelpfulCount() + 1);
        answerRepository.save(answer);
    }

    private ProductQuestionResponseDto mapQuestionToDto(ProductQuestion question) {
        ProductQuestionResponseDto dto = new ProductQuestionResponseDto();
        dto.setId(question.getId());
        dto.setProductId(question.getProduct().getId());
        dto.setCustomerId(question.getCustomer().getId());
        dto.setCustomerFirstName(question.getCustomer().getFirstName());
        dto.setQuestion(question.getQuestion());
        dto.setIsAnswered(question.getIsAnswered());
        dto.setDateCreated(question.getDateCreated());
        dto.setAnswers(question.getAnswers().stream()
                .map(a -> {
                    String name = "Customer";
                    if (a.getAnsweredByCustomerId() != null) {
                        Customer c = customerRepository.findById(a.getAnsweredByCustomerId()).orElse(null);
                        if (c != null) name = c.getFirstName();
                    }
                    return mapAnswerToDto(a, name);
                })
                .collect(Collectors.toList()));
        return dto;
    }

    private ProductAnswerResponseDto mapAnswerToDto(ProductAnswer answer, String name) {
        ProductAnswerResponseDto dto = new ProductAnswerResponseDto();
        dto.setId(answer.getId());
        dto.setAnsweredByCustomerId(answer.getAnsweredByCustomerId());
        dto.setAnsweredByName(name);
        dto.setAnsweredBySeller(answer.getAnsweredBySeller());
        dto.setAnswer(answer.getAnswer());
        dto.setHelpfulCount(answer.getHelpfulCount());
        dto.setDateCreated(answer.getDateCreated());
        return dto;
    }
}
