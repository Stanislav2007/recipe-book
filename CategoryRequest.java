package bg.softuni.recipebook.controller;

import bg.softuni.recipebook.dto.RecipeRequest;
import bg.softuni.recipebook.service.CategoryService;
import bg.softuni.recipebook.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.UUID;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final CategoryService categoryService;
    public RecipeController(RecipeService recipeService, CategoryService categoryService) { this.recipeService = recipeService; this.categoryService = categoryService; }
    @GetMapping public String all(Model model) { model.addAttribute("recipes", recipeService.findAllRecipes()); return "recipe/list"; }
    @GetMapping("/my") public String my(Model model) { model.addAttribute("recipes", recipeService.findMyRecipes()); return "recipe/my"; }
    @GetMapping("/{id}") public String details(@PathVariable UUID id, Model model) { model.addAttribute("recipe", recipeService.findById(id)); return "recipe/details"; }
    @GetMapping("/create") public String create(Model model) { addFormAttributes(model, new RecipeRequest()); return "recipe/create"; }
    @PostMapping("/create") public String create(@Valid RecipeRequest recipeRequest, BindingResult bindingResult, Model model) { if (bindingResult.hasErrors()) { addFormAttributes(model, recipeRequest); return "recipe/create"; } recipeService.create(recipeRequest); return "redirect:/recipes"; }
    @GetMapping("/{id}/edit") public String edit(@PathVariable UUID id, Model model) { addFormAttributes(model, recipeService.mapToRequest(recipeService.findById(id))); model.addAttribute("recipeId", id); return "recipe/edit"; }
    @PostMapping("/{id}/edit") public String edit(@PathVariable UUID id, @Valid RecipeRequest recipeRequest, BindingResult bindingResult, Model model) { if (bindingResult.hasErrors()) { addFormAttributes(model, recipeRequest); model.addAttribute("recipeId", id); return "recipe/edit"; } recipeService.update(id, recipeRequest); return "redirect:/recipes/" + id; }
    @PostMapping("/{id}/delete") public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) { recipeService.delete(id); redirectAttributes.addFlashAttribute("message", "Recipe deleted successfully."); return "redirect:/recipes"; }
    private void addFormAttributes(Model model, RecipeRequest request) { model.addAttribute("recipeRequest", request); model.addAttribute("categories", categoryService.findAll()); }
}
