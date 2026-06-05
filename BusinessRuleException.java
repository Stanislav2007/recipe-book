package bg.softuni.recipebook.controller;

import bg.softuni.recipebook.dto.CategoryRequest;
import bg.softuni.recipebook.service.CategoryService;
import bg.softuni.recipebook.service.RecipeService;
import bg.softuni.recipebook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RecipeService recipeService;
    private final CategoryService categoryService;
    public AdminController(UserService userService, RecipeService recipeService, CategoryService categoryService) { this.userService = userService; this.recipeService = recipeService; this.categoryService = categoryService; }
    @GetMapping public String dashboard(Model model) { model.addAttribute("users", userService.findAllUsers()); model.addAttribute("recipes", recipeService.findAllRecipes()); model.addAttribute("categories", categoryService.findAll()); if (!model.containsAttribute("categoryRequest")) model.addAttribute("categoryRequest", new CategoryRequest()); return "admin/dashboard"; }
    @PostMapping("/categories") public String createCategory(@Valid CategoryRequest categoryRequest, BindingResult bindingResult, Model model) { if (bindingResult.hasErrors()) { model.addAttribute("users", userService.findAllUsers()); model.addAttribute("recipes", recipeService.findAllRecipes()); model.addAttribute("categories", categoryService.findAll()); return "admin/dashboard"; } categoryService.create(categoryRequest); return "redirect:/admin"; }
}
