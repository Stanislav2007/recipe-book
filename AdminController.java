package bg.softuni.recipebook.service;

import bg.softuni.recipebook.dto.RecipeRequest;
import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.exception.ForbiddenActionException;
import bg.softuni.recipebook.exception.NotFoundException;
import bg.softuni.recipebook.model.entity.Recipe;
import bg.softuni.recipebook.model.entity.User;
import bg.softuni.recipebook.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RecipeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);
    private final RecipeRepository recipeRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CurrentUser currentUser;

    public RecipeService(RecipeRepository recipeRepository, CategoryService categoryService,
                         UserService userService, CurrentUser currentUser) {
        this.recipeRepository = recipeRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @Cacheable("recipes")
    public List<Recipe> findAllRecipes() { return recipeRepository.findAll(); }

    public List<Recipe> findMyRecipes() { return recipeRepository.findAllByAuthor(userService.getCurrentUserEntity()); }

    public Recipe findById(UUID id) {
        return recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Recipe not found."));
    }

    @Transactional
    @CacheEvict(value = "recipes", allEntries = true)
    public Recipe create(RecipeRequest request) {
        validateTitle(request.getTitle(), null);
        Recipe recipe = new Recipe();
        fillRecipe(recipe, request);
        recipe.setAuthor(userService.getCurrentUserEntity());
        Recipe saved = recipeRepository.save(recipe);
        LOGGER.info("Created recipe {} by user {}", saved.getId(), saved.getAuthor().getId());
        return saved;
    }

    @Transactional
    @CacheEvict(value = "recipes", allEntries = true)
    public void update(UUID id, RecipeRequest request) {
        Recipe recipe = findById(id);
        verifyOwnerOrAdmin(recipe);
        validateTitle(request.getTitle(), id);
        fillRecipe(recipe, request);
        recipeRepository.save(recipe);
        LOGGER.info("Updated recipe {} by user {}", id, userService.getCurrentUserEntity().getId());
    }

    @Transactional
    @CacheEvict(value = "recipes", allEntries = true)
    public void delete(UUID id) {
        Recipe recipe = findById(id);
        verifyOwnerOrAdmin(recipe);
        recipeRepository.delete(recipe);
        LOGGER.info("Deleted recipe {} by user {}", id, userService.getCurrentUserEntity().getId());
    }

    public RecipeRequest mapToRequest(Recipe recipe) {
        RecipeRequest request = new RecipeRequest();
        request.setTitle(recipe.getTitle());
        request.setIngredients(recipe.getIngredients());
        request.setInstructions(recipe.getInstructions());
        request.setCookingMinutes(recipe.getCookingMinutes());
        request.setImageUrl(recipe.getImageUrl());
        request.setCategoryId(recipe.getCategory().getId());
        return request;
    }

    private void validateTitle(String title, UUID currentRecipeId) {
        recipeRepository.findByTitleIgnoreCase(title.trim())
                .filter(recipe -> currentRecipeId == null || !recipe.getId().equals(currentRecipeId))
                .ifPresent(recipe -> { throw new BusinessRuleException("A recipe with this title already exists."); });
    }

    private void fillRecipe(Recipe recipe, RecipeRequest request) {
        recipe.setTitle(request.getTitle().trim());
        recipe.setIngredients(request.getIngredients().trim());
        recipe.setInstructions(request.getInstructions().trim());
        recipe.setCookingMinutes(request.getCookingMinutes());
        recipe.setImageUrl(request.getImageUrl() == null ? "" : request.getImageUrl().trim());
        recipe.setCategory(categoryService.findById(request.getCategoryId()));
    }

    private void verifyOwnerOrAdmin(Recipe recipe) {
        User user = userService.getCurrentUserEntity();
        if (!recipe.getAuthor().getId().equals(user.getId()) && !currentUser.isAdmin()) {
            throw new ForbiddenActionException("You can edit or delete only your own recipes.");
        }
    }
}
