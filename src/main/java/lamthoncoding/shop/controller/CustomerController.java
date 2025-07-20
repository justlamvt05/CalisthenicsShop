package lamthoncoding.shop.controller;

import jakarta.servlet.http.HttpSession;
import lamthoncoding.shop.entity.Product;
import lamthoncoding.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    @Autowired
    private ProductService productService;

    @GetMapping("/customer")
    public String customer(Model model, HttpSession session) {
        model.addAttribute("products", productService.getAllProducts());
        return "customer";
    }
}
