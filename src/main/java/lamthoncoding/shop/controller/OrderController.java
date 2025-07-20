package lamthoncoding.shop.controller;

import jakarta.servlet.http.HttpSession;
import lamthoncoding.shop.entity.Address;
import lamthoncoding.shop.entity.Cart;
import lamthoncoding.shop.entity.Order;
import lamthoncoding.shop.entity.User;
import lamthoncoding.shop.service.AddressService;
import lamthoncoding.shop.service.CartService;
import lamthoncoding.shop.service.OrderService;
import lamthoncoding.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private  OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Address> addressList = addressService.getAddressesByUserId(user.getId());
        Cart cart = cartService.getCart(user);
        model.addAttribute("cart", cart);
        model.addAttribute("addressList", addressList);
        model.addAttribute("order", new Order());

        return "order";  // order.html
    }
    @PostMapping("/checkout")
    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }


        Cart cart = cartService.getCart(user);
        if (cart == null || cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("toastNotification", "Your cart is empty!");
            return "redirect:/cart";
        }

        Order order = orderService.createOrderFromCart(cart, user);
        redirectAttributes.addFlashAttribute("toastNotification", "Your order has been created successfully!");
        return "redirect:/order/" + order.getId();
    }
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Address> addressList = addressService.getAddressesByUserId(user.getId());
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("addressList", addressList);
        return "order-detail";
    }
    @GetMapping("/my-orders")
    public String viewMyOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        model.addAttribute("orders", orders);
        return "my-orders"; // my-orders.html
    }
}

