package bg.softuni.recipebook.service;

import bg.softuni.recipebook.dto.RecipeRequest;
import bg.softuni.recipebook.exception.ForbiddenActionException;
import bg.softuni.recipebook.exception.NotFoundException;
import bg.softuni.recipebook.model.entity.Recipe;
import bg.softuni.recipebook.model.entity.User;
import bg.softuni.recipebook.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CurrentUser currentUser;
    public RecipeService(RecipeRepository recipeRepository, CategoryService categoryService, UserService userService, CurrentUser currentUser) {
        this.recipeRepository = recipeRepository; this.categoryService = categoryService; this.userService = userService; this.currentUser = currentUser;
    }
    public List<Recipe> findAllRecipes() { return recipeRepository.findAll(); }
    public List<Recipe> findMyRecipes() { return recipeRepository.findAllByAuthor(userService.getCurrentUserEntity()); }
    public Recipe findById(UUID id) { return recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Recipe not found.")); }
    public void create(RecipeRequest request) {
        Recipe recipe = new Recipe(); fillRecipe(recipe, request); recipe.setAuthor(userService.getCurrentUserEntity()); recipeRepository.save(recipe);
    }
    public void update(UUID id, RecipeRequest request) { Recipe recipe = findById(id); verifyOwnerOrAdmin(recipe); fillRecipe(recipe, request); recipeRepository.save(recipe); }
    public void delete(UUID id) { Recipe recipe = findById(id); verifyOwnerOrAdmin(recipe); recipeRepository.delete(recipe); }
    public RecipeRequest mapToRequest(Recipe recipe) { RecipeRequest r = new RecipeRequest(); r.setTitle(recipe.getTitle()); r.setIngredients(recipe.getIngredients()); r.setInstructions(recipe.getInstructions()); r.setCookingMinutes(recipe.getCookingMinutes()); r.setImageUrl(recipe.getImageUrl()); r.setCategoryId(recipe.getCategory().getId()); return r; }
    private void fillRecipe(Recipe recipe, RecipeRequest request) { recipe.setTitle(request.getTitle()); recipe.setIngredients(request.getIngredients()); recipe.setInstructions(request.getInstructions()); recipe.setCookingMinutes(request.getCookingMinutes()); recipe.setImageUrl(request.getImageUrl()); recipe.setCategory(categoryService.findById(request.getCategoryId())); }
    private void verifyOwnerOrAdmin(Recipe recipe) { User user = userService.getCurrentUserEntity(); if (!recipe.getAuthor().getId().equals(user.getId()) && !currentUser.isAdmin()) throw new ForbiddenActionException("You can edit or delete only your own recipes."); }
}
