package lamthoncoding.shop.controller;

import jakarta.servlet.http.HttpSession;
import lamthoncoding.shop.entity.Product;
import lamthoncoding.shop.entity.User;
import lamthoncoding.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/product/{id}")
    public String product(@PathVariable int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("products", productService.findRandomProducts());
        return "product";
    }
    @GetMapping("/product/search")
    public String searchProduct(@RequestParam("name") String name, Model model) {
        List<Product> products = productService.searchProductsByName(name);
        model.addAttribute("products", products);
        model.addAttribute("searchKeyword", name);
        return "search-result";
    }
}
