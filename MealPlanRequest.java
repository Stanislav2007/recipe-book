package bg.softuni.recipebook.client;

import bg.softuni.recipebook.dto.mealplan.MealPlanRequest;
import bg.softuni.recipebook.dto.mealplan.MealPlanResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "meal-plan-service", url = "${meal-plan.service.url:http://localhost:8081}")
public interface MealPlanClient {
    @GetMapping("/api/meal-plans/user/{userId}")
    List<MealPlanResponse> findByUser(@PathVariable UUID userId);

    @PostMapping("/api/meal-plans")
    MealPlanResponse create(@RequestBody MealPlanRequest request);

    @PutMapping("/api/meal-plans/{id}/complete")
    MealPlanResponse complete(@PathVariable UUID id, @RequestParam UUID userId);

    @DeleteMapping("/api/meal-plans/{id}")
    void delete(@PathVariable UUID id, @RequestParam UUID userId);
}
