package bg.softuni.recipebook.service;

import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.model.entity.Favorite;
import bg.softuni.recipebook.model.entity.Recipe;
import bg.softuni.recipebook.model.entity.User;
import bg.softuni.recipebook.repository.FavoriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FavoriteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteService.class);
    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final RecipeService recipeService;

    public FavoriteService(FavoriteRepository favoriteRepository, UserService userService, RecipeService recipeService) {
        this.favoriteRepository = favoriteRepository;
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @Transactional
    public void add(UUID recipeId) {
        User user = userService.getCurrentUserEntity();
        Recipe recipe = recipeService.findById(recipeId);
        if (favoriteRepository.existsByUserAndRecipe(user, recipe)) {
            throw new BusinessRuleException("Recipe is already in favorites.");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        favoriteRepository.save(favorite);
        LOGGER.info("User {} added recipe {} to favorites", user.getId(), recipeId);
    }

    @Transactional
    public void remove(UUID recipeId) {
        User user = userService.getCurrentUserEntity();
        Recipe recipe = recipeService.findById(recipeId);
        Favorite favorite = favoriteRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new BusinessRuleException("Recipe is not in favorites."));
        favoriteRepository.delete(favorite);
        LOGGER.info("User {} removed recipe {} from favorites", user.getId(), recipeId);
    }

    public List<Favorite> findMyFavorites() {
        return favoriteRepository.findAllByUser(userService.getCurrentUserEntity());
    }
}
