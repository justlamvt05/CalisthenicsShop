package lamthoncoding.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lamthoncoding.shop.entity.User;
import lamthoncoding.shop.service.ProductService;
import lamthoncoding.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller

public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("products", productService.getTop4Products());
        return "home";
    }


    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        User existUserByEmail = userService.findByEmail(user.getEmail());
        if (existUserByEmail != null) {
            result.rejectValue("email", "Duplicate.user.email", "There is already an account with this email");
            return "register";
        }
        User existUserByUsername = userService.findByUsername(user.getUsername());
        if (existUserByUsername != null) {
            result.rejectValue("username", null, "There is already an account with this username");
            return "register";

        }
        User existUserByPhoneNumber = userService.findByPhoneNumber(user.getPhoneNumber());
        if (existUserByPhoneNumber != null) {
            result.rejectValue("phoneNumber", null, "There is already an account with this phone number");
            return "register";
        }
        if(user.getBirthOfDate()!=null && user.getBirthOfDate().isAfter(LocalDate.now().minusYears(10))){
            result.rejectValue("birthOfDate", null, "You must be older than 10 years old to register");
            return "register";
        }

        userService.saveUser(user);
        return "redirect:/register?success";
    }


    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model, HttpSession session) {
        Object errorMessage = session.getAttribute("errorMessage");

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage.toString());
            session.removeAttribute("errorMessage");
        }

        return "login";
    }



}
