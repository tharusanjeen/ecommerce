package com.project.ecommerce.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ecommerce.dto.order.OrderDetailsDto;
import com.project.ecommerce.dto.order.OrderItemResponseDto;
import com.project.ecommerce.dto.order.OrderRequestDto;
import com.project.ecommerce.dto.order.OrderResponseDto;
import com.project.ecommerce.exception.CustomExceptions;
import com.project.ecommerce.exception.CustomExceptions.CartNotFoundException;
import com.project.ecommerce.exception.CustomExceptions.NotEnoughStockException;
import com.project.ecommerce.model.Cart;
import com.project.ecommerce.model.CartItem;
import com.project.ecommerce.model.Order;
import com.project.ecommerce.model.OrderItem;
import com.project.ecommerce.model.Product;
import com.project.ecommerce.model.User;
import com.project.ecommerce.model.Enums.CartStatus;
import com.project.ecommerce.model.Enums.OrderStatus;
import com.project.ecommerce.model.Enums.PaymentMethod;
import com.project.ecommerce.model.Enums.PaymentStatus;
import com.project.ecommerce.repository.CartRepository;
import com.project.ecommerce.repository.OrderItemRepository;
import com.project.ecommerce.repository.OrderRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    private static final BigDecimal SHIPPING_COST = new BigDecimal("100.00");

    @Transactional
    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto, UUID userId) {

        // Fetch user
        User user = getUser(userId);

        // Fetch cart
        Cart cart = getCart(user);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Validate stock + calculate total
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new NotEnoughStockException("Not enough stock for product: " + product.getName());
            }

            BigDecimal itemTotal = product.getDiscountedPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);
        }

        // Create Order
        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .paymentMethod(PaymentMethod.CASH_ON_DELIVERY)
                .paymentStatus(PaymentStatus.UNPAID)
                .totalAmount(totalAmount)
                .shippingCost(SHIPPING_COST)
                .grandTotal(totalAmount.add(SHIPPING_COST))
                .addressId(orderRequestDto.getAddressId())
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        // Create OrderItems + deduct stock
        List<OrderItemResponseDto> itemResponses = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            BigDecimal itemTotal = product.getDiscountedPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(product.getId())
                    .productName(product.getName())
                    .productPrice(product.getDiscountedPrice())
                    .discountPercentage(cartItem.getDiscountAtAddition())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(itemTotal)
                    .build();

            orderItemRepository.save(orderItem);

            itemResponses.add(
                    OrderItemResponseDto.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .productPrice(product.getPrice())
                            .discountPercentage(cartItem.getDiscountAtAddition())
                            .quantity(cartItem.getQuantity())
                            .totalPrice(itemTotal)
                            .build()

            );
        }

        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .totalAmount(order.getTotalAmount())
                .shippingCost(order.getShippingCost())
                .grandTotal(order.getGrandTotal())
                .addressId(order.getAddressId())
                .createdAt(order.getCreatedAt())
                .items(itemResponses)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDetailsDto> getMyOrders(UUID userId) {

        User user = getUser(userId);

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream().map(order -> OrderDetailsDto.from(order)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderItemResponseDto> getOrderDetails(UUID userId, UUID orderId) {

        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

        return orderItems.stream()
                .map(item -> OrderItemResponseDto.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .productPrice(item.getProductPrice())
                        .discountPercentage(item.getDiscountPercentage())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void cancelOrder(UUID orderId, UUID userId) {

        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        if (order.getOrderStatus() != OrderStatus.PENDING &&
                order.getOrderStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Order cannot be cancelled at this stage.");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new CustomExceptions.ProductNotFoundException("Product not found!"));

            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    private Cart getCart(User user) {
        return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseThrow(() -> new CartNotFoundException("Cart not found!"));
    }

    // ADMIN
    @Transactional(readOnly = true)
    @Override
    public List<OrderDetailsDto> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderDetailsDto::from).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateOrderStatus(UUID orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        if (order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot change status of a completed or cancelled order.");
        }

        order.setOrderStatus(status);

        if(status == OrderStatus.DELIVERED) {
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setDeliveredAt(LocalDateTime.now());
        }

        if(status == OrderStatus.CANCELLED) {
            List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
            for(OrderItem item : orderItems) {
                Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found!"));
                
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        orderRepository.save(order);
    }
}