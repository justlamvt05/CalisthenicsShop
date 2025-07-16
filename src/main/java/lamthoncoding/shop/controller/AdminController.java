package lamthoncoding.shop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lamthoncoding.shop.dto.UserDTO;
import lamthoncoding.shop.entity.Role;
import lamthoncoding.shop.entity.User;
import lamthoncoding.shop.service.RoleService;
import lamthoncoding.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/admin")
    public String listUsers(
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Role> roles = roleService.findAllRoles();
        Page<UserDTO> pageUsers = userService.listUsers(roleId, status, searchText, page, pageSize, sortField, sortDir);
        model.addAttribute("roles", roles);
        model.addAttribute("pageUsers", pageUsers);
        model.addAttribute("roleId", roleId);
        model.addAttribute("status", status);
        session.setAttribute("user", user);
        model.addAttribute("user", user);
        model.addAttribute("searchText", searchText);
        model.addAttribute("pageUsers", pageUsers);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "user_list";
    }

    @GetMapping("/admin/{id}/toggle_status")
    public String toggleStatus(@PathVariable int id, @RequestParam(required = false) Integer roleId,
                               @RequestParam(required = false) Boolean status,
                               @RequestParam(required = false) String searchText,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(defaultValue = "id") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               RedirectAttributes redirectAttributes) {
        userService.toggleStatus(id);
        redirectAttributes.addFlashAttribute("toastNotification", "Status updated successfully");
        String redirectUrl = String.format("redirect:/admin?page=%d&pageSize=%d&sortField=%s&sortDir=%s",
                page, pageSize, sortField, sortDir);

        if (searchText != null) {
            redirectUrl += "&searchText=" + URLEncoder.encode(searchText, StandardCharsets.UTF_8);
        }
        if (roleId != null) {
            redirectUrl += "&roleId=" + roleId;
        }
        if (status != null) {
            redirectUrl += "&status=" + status;
        }

        return redirectUrl;

    }
    @GetMapping("/admin/{id}/view")
    public String viewUser(@PathVariable int id, HttpSession session, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user_view";
    }
    @GetMapping("/admin/{id}/edit")
    public String editUser(@PathVariable int id, HttpSession session, Model model) {
        List<Role> roles = roleService.findAllRoles();
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "user_edit";
    }
    @PostMapping("/admin/edit")
    public String editUserInfo(RedirectAttributes redirectAttributes, Model model, @Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "user_edit";
        }
        Optional<User> sameUserName = userService.findByUsernameAndIdNot(user.getUsername(), user.getId());
        if (sameUserName.isPresent()) {
            bindingResult.rejectValue("username", "error.user", "Username is already in use");
        }
        Optional<User> sameEmail = userService.findByEmailAndIdNot(user.getEmail(), user.getId());
        if (sameEmail.isPresent()) {
            bindingResult.rejectValue("email", "error.user", "Email is already in use");
        }
        Optional<User> phoneNumber = userService.findByPhoneNumberAndIdNot(user.getPhoneNumber(), user.getId());
        if (phoneNumber.isPresent()) {
            bindingResult.rejectValue("phone", "error.user", "Phone number is already in use");
        }
        if (bindingResult.hasErrors()) {
            return "user_edit";
        }
        redirectAttributes.addFlashAttribute("toastNotification", "Status updated successfully");
        userService.updateById(user);
        return "redirect:/admin";
    }
    @GetMapping("/admin/add")
    public String addUser(Model model, HttpSession session) {
        model.addAttribute("user", new User());
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("roles", roles);
        return "user_add";
    }
    @PostMapping("/admin/add")
    public String addUser( @Valid @ModelAttribute("user") User user,
                          BindingResult result,
                          Model model, RedirectAttributes redirectAttributes) {

        User existUserByEmail = userService.findByEmail(user.getEmail());
        if (existUserByEmail != null) {
            result.rejectValue("email", "Duplicate.user.email", "There is already an account with this email");
        }


        User existUserByUsername = userService.findByUsername(user.getUsername());
        if (existUserByUsername != null) {
            result.rejectValue("username", null, "There is already an account with this username");
        }


        if (user.getPhoneNumber() != null && !user.getPhoneNumber().trim().isEmpty()) {
            User existUserByPhoneNumber = userService.findByPhoneNumber(user.getPhoneNumber());
            if (existUserByPhoneNumber != null) {
                result.rejectValue("phoneNumber", null, "There is already an account with this phone number");
            }
        }


        if (user.getBirthOfDate() != null &&
                user.getBirthOfDate().isAfter(LocalDate.now().minusYears(10))) {
            result.rejectValue("birthOfDate", null, "User must be older than 10 years old");
        }


        if (result.hasErrors()) {
            List<Role> roles = roleService.findAllRoles();
            model.addAttribute("roles", roles);
            return "user_add";
        }


        redirectAttributes.addFlashAttribute("toastNotification", "Status updated successfully");
        userService.saveUser(user);

        return "redirect:/admin";
    }


}
