package lamthoncoding.shop.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lamthoncoding.shop.dto.UserProfileForm;
import lamthoncoding.shop.entity.Address;
import lamthoncoding.shop.entity.User;
import lamthoncoding.shop.service.AddressService;
import lamthoncoding.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class UserController {
    private static final String UPLOAD_DIR = "C:\\New folder\\avatars";
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @GetMapping("/user/profile")
    public String profile(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        Address address = addressService.getAddressesByUserId(currentUser.getId()).stream().findFirst().orElse(new Address());

        UserProfileForm profileForm = new UserProfileForm();
        profileForm.setUser(currentUser);
        profileForm.setAddress(address);

        model.addAttribute("profileForm", profileForm);
        return "user_profile";
    }
    @PostMapping("/user/update")
    public String updateProfile(Model model, @Valid @ModelAttribute("profileForm") UserProfileForm profileForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam("avatarFile") MultipartFile avatarFile, HttpSession session) {
        User user = profileForm.getUser();
        Address address = profileForm.getAddress();
        if(bindingResult.hasErrors()){
            return "user_profile";
        }
        Optional<User> sameUserName = userService.findByUsernameAndIdNot(user.getUsername(), user.getId());
        if (sameUserName.isPresent()) {
            bindingResult.rejectValue("username", "error.user", "Username is already in use");
        }
        Optional<User> phoneNumber = userService.findByPhoneNumberAndIdNot(user.getPhoneNumber(), user.getId());
        if (phoneNumber.isPresent()) {
            bindingResult.rejectValue("phone", "error.user", "Phone number is already in use");
        }
        if (bindingResult.hasErrors()) {
            return "user_profile";
        }
        if (!avatarFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);

                Files.createDirectories(uploadPath);
                avatarFile.transferTo(uploadPath.resolve(fileName).toFile());

                // lưu đường dẫn tương đối vào DB, ví dụ:
                String avatarPath = "/user-avatars/" + fileName;
                user.setAvatar(avatarPath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userService.updateById(user);

        address.setUser(user);
        addressService.addOrUpdateAddress(address);

        user = userService.findById(user.getId());
        session.setAttribute("user", user);

        redirectAttributes.addFlashAttribute("toastNotification", "Profile updated successfully");
        return "redirect:/user/profile";
    }

    @GetMapping("/user-avatars/{filename}")
    @ResponseBody
    public ResponseEntity<byte[]> serveAvatar(@PathVariable String filename) throws IOException {
        Path file = Paths.get(UPLOAD_DIR).resolve(filename);
        byte[] fileContent = Files.readAllBytes(file);
        return ResponseEntity.ok(fileContent);
    }
}
