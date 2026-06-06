package bg.softuni.recipebook.repository;

import bg.softuni.recipebook.model.entity.Favorite;
import bg.softuni.recipebook.model.entity.Recipe;
import bg.softuni.recipebook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    Optional<Favorite> findByUserAndRecipe(User user, Recipe recipe);
    List<Favorite> findAllByUser(User user);
    boolean existsByUserAndRecipe(User user, Recipe recipe);
}
