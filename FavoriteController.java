package bg.softuni.recipebook.controller;

import bg.softuni.recipebook.dto.LoginRequest;
import bg.softuni.recipebook.dto.RegisterRequest;
import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) { this.userService = userService; }
    @GetMapping("/register") public String register(Model model) { if (!model.containsAttribute("registerRequest")) model.addAttribute("registerRequest", new RegisterRequest()); return "auth/register"; }
    @PostMapping("/register") public String register(@Valid RegisterRequest registerRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) { redirectAttributes.addFlashAttribute("registerRequest", registerRequest); redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerRequest", bindingResult); return "redirect:/register"; }
        try { userService.register(registerRequest); } catch (BusinessRuleException e) { redirectAttributes.addFlashAttribute("registerRequest", registerRequest); redirectAttributes.addFlashAttribute("error", e.getMessage()); return "redirect:/register"; }
        return "redirect:/login";
    }
    @GetMapping("/login") public String login(Model model) { if (!model.containsAttribute("loginRequest")) model.addAttribute("loginRequest", new LoginRequest()); return "auth/login"; }
    @PostMapping("/login") public String login(@Valid LoginRequest loginRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) { redirectAttributes.addFlashAttribute("loginRequest", loginRequest); redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginRequest", bindingResult); return "redirect:/login"; }
        if (!userService.login(loginRequest)) { redirectAttributes.addFlashAttribute("loginRequest", loginRequest); redirectAttributes.addFlashAttribute("error", "Invalid username or password."); return "redirect:/login"; }
        return "redirect:/recipes";
    }
    @PostMapping("/logout") public String logout() { userService.logout(); return "redirect:/"; }
}
