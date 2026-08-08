package bg.softuni.recipebook.service;

import bg.softuni.recipebook.client.MealPlanClient;
import bg.softuni.recipebook.dto.mealplan.MealPlanRequest;
import bg.softuni.recipebook.dto.mealplan.MealPlanResponse;
import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.model.entity.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class MealPlanService {
    private static final Set<String> SUPPORTED_MEAL_TYPES = Set.of("breakfast", "lunch", "dinner", "snack");
    private static final Logger LOGGER = LoggerFactory.getLogger(MealPlanService.class);
    private final MealPlanClient mealPlanClient;
    private final UserService userService;
    private final RecipeService recipeService;

    public MealPlanService(MealPlanClient mealPlanClient, UserService userService, RecipeService recipeService) {
        this.mealPlanClient = mealPlanClient;
        this.userService = userService;
        this.recipeService = recipeService;
    }

    public List<MealPlanResponse> findMine() {
        return mealPlanClient.findByUser(userService.getCurrentUserEntity().getId());
    }

    public void schedule(UUID recipeId, LocalDate plannedDate, String mealType) {
        validatePlannedDate(plannedDate);
        String normalizedMealType = normalizeMealType(mealType);
        Recipe recipe = recipeService.findById(recipeId);
        UUID userId = userService.getCurrentUserEntity().getId();
        mealPlanClient.create(new MealPlanRequest(userId, recipeId, recipe.getTitle(), plannedDate, normalizedMealType));
        LOGGER.info("Recipe {} scheduled for user {}", recipeId, userId);
    }

    public void complete(UUID id) {
        UUID userId = userService.getCurrentUserEntity().getId();
        mealPlanClient.complete(id, userId);
        LOGGER.info("Meal plan {} completed by user {}", id, userId);
    }

    private void validatePlannedDate(LocalDate plannedDate) {
        if (plannedDate == null || plannedDate.isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Planned date must be today or in the future.");
        }
    }

    private String normalizeMealType(String mealType) {
        if (mealType == null || mealType.isBlank()) {
            throw new BusinessRuleException("Meal type is required.");
        }
        String normalized = mealType.trim().toLowerCase(Locale.ROOT);
        if (!SUPPORTED_MEAL_TYPES.contains(normalized)) {
            throw new BusinessRuleException("Meal type must be Breakfast, Lunch, Dinner, or Snack.");
        }
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }

    public void delete(UUID id) {
        UUID userId = userService.getCurrentUserEntity().getId();
        mealPlanClient.delete(id, userId);
        LOGGER.info("Meal plan {} deleted by user {}", id, userId);
    }
}
