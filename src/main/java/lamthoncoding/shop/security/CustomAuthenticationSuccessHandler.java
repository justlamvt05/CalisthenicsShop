package lamthoncoding.shop.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lamthoncoding.shop.entity.User;
import lamthoncoding.shop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        session.setAttribute("user", user);
        String role = user.getRole().getName();
        String redirectURL = request.getContextPath();
        switch (role.toLowerCase()) {
            case "admin":
                redirectURL += "/admin";
                break;
            case "sale":
                redirectURL += "/sale";
                break;
            case "customer":
                redirectURL += "/customer";
                break;
            default:
                redirectURL += "/login?error=true";
                break;
        }

        response.sendRedirect(redirectURL);
    }
}
