package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.OrderStatus;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.model.Book;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.repository.OrderRepository;
import com.example.demo.security.SecurityUtil;

@Service
public class OrderServiceImpl implements  OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
  private final OrderRepository orderRepository;
  private final BookService bookService;
  private final SecurityUtil securityUtil;
  private final PaymentService paymentService;

  @Autowired
  public OrderServiceImpl(OrderRepository orderRepository, BookService bookService, SecurityUtil securityUtil, PaymentService paymentService) {
      this.orderRepository = orderRepository;
      this.bookService = bookService;
      this.securityUtil = securityUtil;
      this.paymentService = paymentService;
  }

  @Override
  public Order placeOrder(List<OrderItem> items) {
    //Book book = bookService.findById("1").orElseThrow(() -> new RuntimeException("Order not found")); // if using Optional<> type always wrap it with an exceptional handler, then we can access the type returned directly instead of using var as below

    /*  Appraoch 1: Saving a single book order
     // Check if the book exists
      logger.info("Placing order for book ID: {}, quantity: {}, customer: {}", bookId, quantity);
      var bookOpt = bookService.findById(bookId); //Either contains a Book Or contains nothing, Optional is used to handle both the cases so it avoids the default exception thrown if the row or data doesn't exist in the db
      if (bookOpt.isEmpty()) {
          logger.error("Failed to place order - Book with ID {} does not exist", bookId);
          throw new IllegalArgumentException("Book with ID " + bookId + " does not exist.");
      }
      Book book = bookOpt
        .orElseThrow(() -> new RuntimeException("Book not found"));

      BigDecimal price = book.getPrice();
      BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(quantity));


      // Create and save the order
      Order order = new Order(bookId, quantity);
      String userId= securityUtil.getCurrentUserId();
      order.setUserId(userId);
      order.setTotalAmount(totalAmount);
      order.setStatus(OrderStatus.PENDING);
      Order savedOrder = orderRepository.save(order);
      logger.info("Order placed successfully with ID: {}", savedOrder.getId());
      return savedOrder; */

    /* Approach 2: If the books are sent as list with common quantity
    // Fetch all books
    List<Book> books = bookService.findAllByIds(bookIds);

    // Validation: some books not found
    if (books.size() != bookIds.size()) {
        logger.error("Some books not found. Requested: {}, Found: {}",
                  bookIds.size(), books.size());
        throw new IllegalArgumentException("One or more books do not exist");
    }

    // Calculate total amount
    BigDecimal totalAmount = books.stream()
            .map(Book::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .multiply(BigDecimal.valueOf(quantity));

    // Create order
    Order order = new Order();
    order.setBookIds(bookIds);
    order.setQuantity(quantity);
    order.setTotalAmount(totalAmount);
    order.setStatus(OrderStatus.PENDING);

    String userId = securityUtil.getCurrentUserId();
    order.setUserId(userId);

    Order savedOrder = orderRepository.save(order);

    logger.info("Order placed successfully with ID: {}", savedOrder.getId());
    return savedOrder; */

    //Approach 3
    logger.info("Placing order with items: {}", items);

    // Extract bookIds
    List<String> bookIds = items.stream()
            .map(OrderItem::getBookId)
            .toList();

    // Fetch books from DB
    List<Book> books = bookService.findAllByIds(bookIds);

    if (books.size() != bookIds.size()) {
        throw new IllegalArgumentException("One or more books do not exist");
    }

    // Map bookId -> Book : To get the price of each book
    Map<String, Book> bookMap = books.stream()
            .collect(Collectors.toMap(Book::getId, Function.identity())); //Collectors.toMap(keyMapper, valueMapper) --> (Book.getId, Book)

    // Calculate total
    BigDecimal totalAmount = BigDecimal.ZERO;

    for (OrderItem item : items) {
        Book book = bookMap.get(item.getBookId());
        item.setPrice(book.getPrice());

        BigDecimal itemTotal =
                book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        totalAmount = totalAmount.add(itemTotal);
    }

    // Create order
    Order order = new Order(items);
    //order.setItems(items);
    order.setTotalAmount(totalAmount);
    order.setStatus(OrderStatus.PENDING);
    order.setUserId(securityUtil.getCurrentUserId());

    Order savedOrder = orderRepository.save(order);

    logger.info("Order placed successfully with ID: {}", savedOrder.getId());
    return savedOrder;
}

    @Override
    public PaymentResponse initiatePayment(String orderId) {

      Order order = orderRepository.findById(orderId)
              .orElseThrow(() -> new RuntimeException("Order not found"));
      logger.debug("here");

      //order.setPaymentId(payment.getPaymentId()); TODO: Should add the payment ID
      order.setStatus(OrderStatus.PAYMENT_INITIATED);

      orderRepository.save(order);
      return paymentService.initiatePayment(order);
  }

  @Override
  public List<Order> getAllOrders() {
      logger.debug("Fetching all orders from repository");
      String userId = securityUtil.getCurrentUserId();
      List<Order> orders = orderRepository.findByUserId(userId);
      logger.debug("Found {} orders", orders.size());
      return orders;
  }

  @Override
  public Optional<Order> getOrderById(String id) {
      logger.debug("Fetching order with ID: {}", id);
      Optional<Order> order = orderRepository.findById(id);
      if (order.isPresent()) {
          logger.debug("Order found with ID: {}", id);
      } else {
          logger.debug("Order not found with ID: {}", id);
      }
      return order;
  }
  }
