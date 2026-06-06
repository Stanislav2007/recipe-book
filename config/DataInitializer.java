package bg.softuni.recipebook.config;

import bg.softuni.recipebook.model.entity.Category;
import bg.softuni.recipebook.model.entity.Recipe;
import bg.softuni.recipebook.model.entity.User;
import bg.softuni.recipebook.model.enums.UserRole;
import bg.softuni.recipebook.repository.CategoryRepository;
import bg.softuni.recipebook.repository.RecipeRepository;
import bg.softuni.recipebook.repository.UserRepository;
import bg.softuni.recipebook.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CategoryService categoryService, CategoryRepository categoryRepository,
                           UserRepository userRepository, RecipeRepository recipeRepository,
                           PasswordEncoder passwordEncoder) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        categoryService.seedCategories();
        seedDemoRecipes();
    }

    private void seedDemoRecipes() {
        if (recipeRepository.count() > 0) {
            return;
        }

        User demoAuthor = userRepository.findByUsername("demo")
                .orElseGet(() -> {
                    User user = new User();
                    user.setUsername("demo");
                    user.setEmail("demo@recipebook.bg");
                    user.setPassword(passwordEncoder.encode("demo123"));
                    user.setRole(UserRole.ADMIN);
                    return userRepository.save(user);
                });

        Category dinner = categoryRepository.findByName("Dinner").orElseThrow();
        Category dessert = categoryRepository.findByName("Dessert").orElseThrow();
        Category vegetarian = categoryRepository.findByName("Vegetarian").orElseThrow();
        Category lunch = categoryRepository.findByName("Lunch").orElseThrow();
        Category breakfast = categoryRepository.findByName("Breakfast").orElseThrow();

        List<Recipe> recipes = List.of(
                recipe("Домашна лазаня", "кори за лазаня, кайма, доматен сос, бешамел, кашкавал, пармезан",
                        "Запържете каймата с доматения сос. Наредете кори, сос и бешамел на пластове. Завършете с кашкавал и запечете до златиста коричка.",
                        75, "/images/lasagna-tray.jpg", dinner, demoAuthor),
                recipe("Капрезе салата", "домати, моцарела, пресен босилек, зехтин, сол",
                        "Нарежете доматите и моцарелата. Подредете ги в чиния, добавете босилек, зехтин и щипка сол.",
                        10, "/images/caprese.jpg", vegetarian, demoAuthor),
                recipe("Шоколадов десерт с боровинки", "какао, бишкоти или блат, крем, боровинки, шоколад",
                        "Подредете основата, добавете крем и завършете с какао и пресни боровинки. Охладете преди сервиране.",
                        35, "/images/dessert.jpg", dessert, demoAuthor),
                recipe("Шопска салата", "домати, краставици, чушка, лук, сирене, магданоз",
                        "Нарежете зеленчуците, овкусете леко и поръсете обилно с настъргано сирене.",
                        15, "/images/shopska.jpg", vegetarian, demoAuthor),
                recipe("Цезар салата", "айсберг, пилешко, крутони, пармезан, цезар сос",
                        "Изпечете пилешкото, накъсайте салатата и смесете с крутони, пармезан и сос.",
                        25, "/images/caesar.jpg", lunch, demoAuthor),
                recipe("Домашен бургер", "питка, месо, салата, сос, картофи, кашкавал",
                        "Изпечете месото, затоплете питката и сглобете бургера със сос и свежа салата. Сервирайте с картофи.",
                        35, "/images/burger.jpg", lunch, demoAuthor),
                recipe("Пухкави солени рулца", "брашно, мая, яйца, сирене, масло, сусам",
                        "Замесете меко тесто, оформете рулца с плънка, намажете с яйце и изпечете до златисто.",
                        90, "/images/rolls.jpg", breakfast, demoAuthor)
        );

        recipeRepository.saveAll(recipes);
    }

    private Recipe recipe(String title, String ingredients, String instructions, int minutes,
                          String imageUrl, Category category, User author) {
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setIngredients(ingredients);
        recipe.setInstructions(instructions);
        recipe.setCookingMinutes(minutes);
        recipe.setImageUrl(imageUrl);
        recipe.setCategory(category);
        recipe.setAuthor(author);
        return recipe;
    }
}
