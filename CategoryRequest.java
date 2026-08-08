package bg.softuni.recipebook.controller;

import bg.softuni.recipebook.service.MealPlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/meal-plans")
public class MealPlanController {
    private final MealPlanService mealPlanService;

    public MealPlanController(MealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("mealPlans", mealPlanService.findMine());
        return "meal-plan/list";
    }

    @PostMapping("/recipe/{recipeId}")
    public String schedule(@PathVariable UUID recipeId, @RequestParam LocalDate plannedDate,
                           @RequestParam String mealType, RedirectAttributes redirectAttributes) {
        mealPlanService.schedule(recipeId, plannedDate, mealType);
        redirectAttributes.addFlashAttribute("message", "Recipe added to your meal plan.");
        return "redirect:/meal-plans";
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        mealPlanService.complete(id);
        redirectAttributes.addFlashAttribute("message", "Meal marked as completed.");
        return "redirect:/meal-plans";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        mealPlanService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Meal removed from the plan.");
        return "redirect:/meal-plans";
    }
}
