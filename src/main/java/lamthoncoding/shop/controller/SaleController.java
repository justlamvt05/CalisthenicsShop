package lamthoncoding.shop.controller;

import jakarta.servlet.http.HttpSession;
import lamthoncoding.shop.dto.ProductDTO;
import lamthoncoding.shop.dto.UserDTO;
import lamthoncoding.shop.entity.*;
import lamthoncoding.shop.service.OrderService;
import lamthoncoding.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor

public class SaleController {
    private static final String UPLOAD_DIR = "C:\\New folder\\product-img";
    private final OrderService orderService;
    private final ProductService productService;
    @GetMapping("/sale")

    public String sale(@RequestParam(required = false) String searchText,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int pageSize,
                       @RequestParam(defaultValue = "id") String sortField,
                       @RequestParam(defaultValue = "asc") String sortDir,
                       Model model,
                       HttpSession session) {
        User user = (User) session.getAttribute("user");
        session.setAttribute("user", user);
        Page<ProductDTO> pageProducts = productService.getAllProductsPageable(searchText,page,pageSize,sortField,sortDir);
        model.addAttribute("searchText", searchText);
        model.addAttribute("pageProducts",  pageProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "product_list";
    }
    @GetMapping("/sale/{id}/toggle_status_product")
    public String toggleStatus(@PathVariable int id,
                               @RequestParam(required = false) String searchText,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(defaultValue = "id") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               RedirectAttributes redirectAttributes) {
        productService.toggleProductStatus(id);
        redirectAttributes.addFlashAttribute("toastNotification", "Status updated successfully");
        String redirectUrl = String.format("redirect:/sale?page=%d&pageSize=%d&sortField=%s&sortDir=%s",
                page, pageSize, sortField, sortDir);

        if (searchText != null) {
            redirectUrl += "&searchText=" + URLEncoder.encode(searchText, StandardCharsets.UTF_8);
        }
        return redirectUrl;

    }
    @GetMapping("/sale/{id}/view")
    public String viewProduct(@PathVariable Integer id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if(product.isPresent()) {
            model.addAttribute("product", product.get());
        }

        return "product_view";
    }
    @GetMapping("/sale/{id}/edit")
    public String editProductForm(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product_edit"; // Thymeleaf view
    }
    @PostMapping("/sale/edit")
    public String updateProduct(@ModelAttribute Product product,
                                @RequestParam("newImages") List<MultipartFile> newImages,
                                @RequestParam(value = "deleteImageIds", required = false) List<Integer> deleteImageIds,RedirectAttributes redirectAttributes) {

        productService.updateProduct(product, newImages, deleteImageIds);
        redirectAttributes.addFlashAttribute("toastNotification", "Product updated successfully");
        return "redirect:/sale/" + product.getId() + "/edit";
    }

    @GetMapping("/sale/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product_add";
    }

    @PostMapping("/sale/products/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("uploadImages") List<MultipartFile> images, RedirectAttributes redirectAttributes) {
        productService.addProduct(product, images);
        redirectAttributes.addFlashAttribute("toastNotification", "Product added successfully");

        return "redirect:/sale";
    }
    @GetMapping("/product-img/{filename}")
    @ResponseBody
    public ResponseEntity<byte[]> serveAvatar(@PathVariable String filename) throws IOException {
        Path file = Paths.get(UPLOAD_DIR).resolve(filename);
        String contentType = Files.probeContentType(file);
        if (contentType == null) {
            contentType = "application/octet-stream"; // fallback
        }

        byte[] imageBytes = Files.readAllBytes(file);
        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .body(imageBytes);
    }
    @GetMapping("/sale/orders")
    public String viewOrders(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 5;
        Page<Order> ordersPage = orderService.getAllOrdersPaged(page, pageSize);
        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "order-list";
    }

    @PostMapping("/sale/orders/{id}/status")
    public String updateOrderStatus(@PathVariable int id, @RequestParam String status) {
        orderService.changeStatus(id, status);
        return "redirect:/sale/orders";
    }

}
