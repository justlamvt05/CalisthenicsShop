package lamthoncoding.shop.controller;

import jakarta.servlet.http.HttpSession;
import lamthoncoding.shop.entity.Cart;
import lamthoncoding.shop.entity.ProductSku;
import lamthoncoding.shop.entity.User;
import lamthoncoding.shop.service.CartService;
import lamthoncoding.shop.service.ProductService;
import lamthoncoding.shop.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductSkuService productSkuService;
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Cart cart = cartService.getCart(user);
        model.addAttribute("cart", cart);
        model.addAttribute("products", productService.findRandomProducts());
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer skuId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        cartService.addToCart(user, skuId, quantity);

        return "redirect:/cart";

    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam Integer itemId) {
        cartService.removeItem(itemId);
        return "redirect:/cart";
    }
}
