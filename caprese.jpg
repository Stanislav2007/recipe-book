package bg.softuni.recipebook.service;

import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.model.entity.Favorite;
import bg.softuni.recipebook.model.entity.Recipe;
import bg.softuni.recipebook.model.entity.User;
import bg.softuni.recipebook.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final RecipeService recipeService;
    public FavoriteService(FavoriteRepository favoriteRepository, UserService userService, RecipeService recipeService) { this.favoriteRepository = favoriteRepository; this.userService = userService; this.recipeService = recipeService; }
    public void add(UUID recipeId) { User user = userService.getCurrentUserEntity(); Recipe recipe = recipeService.findById(recipeId); if (favoriteRepository.existsByUserAndRecipe(user, recipe)) throw new BusinessRuleException("Recipe is already in favorites."); Favorite f = new Favorite(); f.setUser(user); f.setRecipe(recipe); favoriteRepository.save(f); }
    public void remove(UUID recipeId) { User user = userService.getCurrentUserEntity(); Recipe recipe = recipeService.findById(recipeId); favoriteRepository.findByUserAndRecipe(user, recipe).ifPresent(favoriteRepository::delete); }
    public List<Favorite> findMyFavorites() { return favoriteRepository.findAllByUser(userService.getCurrentUserEntity()); }
}
