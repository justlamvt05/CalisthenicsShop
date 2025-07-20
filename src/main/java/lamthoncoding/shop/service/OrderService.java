package lamthoncoding.shop.service;

import lamthoncoding.shop.entity.Order;
import lamthoncoding.shop.entity.Cart;
import lamthoncoding.shop.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    Order createOrderFromCart(Cart cart, User user);
    Order getOrderById(Integer id);

    Page<Order> getAllOrdersPaged(int page, int size);

    void changeStatus(int orderId, String newStatus);

    List<Order> getOrdersByUserId(int userId);
}
