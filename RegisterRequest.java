package bg.softuni.recipebook.controller;

import bg.softuni.recipebook.dto.ProfileRequest;
import bg.softuni.recipebook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profile(Model model) {
        if (!model.containsAttribute("profileRequest")) {
            model.addAttribute("profileRequest", userService.currentProfile());
        }
        model.addAttribute("user", userService.getCurrentUserEntity());
        return "user/profile";
    }

    @PostMapping
    public String update(@Valid ProfileRequest profileRequest, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.getCurrentUserEntity());
            return "user/profile";
        }
        userService.updateProfile(profileRequest);
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully.");
        return "redirect:/profile";
    }
}
