package lamthoncoding.shop.service.impl;

import lamthoncoding.shop.entity.Order;
import lamthoncoding.shop.entity.OrderItem;
import lamthoncoding.shop.entity.Cart;
import lamthoncoding.shop.entity.CartItem;
import lamthoncoding.shop.entity.User;
import lamthoncoding.shop.repository.OrderItemRepository;
import lamthoncoding.shop.repository.OrderRepository;
import lamthoncoding.shop.service.CartService;
import lamthoncoding.shop.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Override
    @Transactional
    public Order createOrderFromCart(Cart cart, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setSku(cartItem.getProductSku());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProductSku().getPrice().doubleValue());

            total += orderItem.getPrice() * orderItem.getQuantity();
            orderItems.add(orderItem);
        }

        order.setTotal(total);
        order.setItems(orderItems);

        // Save order (có cascade thì orderItems sẽ tự lưu theo)
        orderRepository.save(order);

        // Clear cart sau khi đặt hàng thành công
        cartService.clearCart(cart);

        return order;
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }
    @Override
    public Page<Order> getAllOrdersPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllOrders(pageable);
    }

    @Override
    public void changeStatus(int orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        orderRepository.save(order);
    }
    @Override
    public List<Order> getOrdersByUserId(int userId) {
        return orderRepository.findByUser_Id(userId);
    }

}
