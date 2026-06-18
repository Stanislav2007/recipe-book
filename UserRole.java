package bg.softuni.recipebook.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public class RecipeRequest {
    @NotBlank(message = "Title is required.")
    @Size(min = 3, max = 80, message = "Title must be between 3 and 80 characters.")
    private String title;

    @NotBlank(message = "Ingredients are required.")
    @Size(min = 10, max = 500, message = "Ingredients must be between 10 and 500 characters.")
    private String ingredients;

    @NotBlank(message = "Instructions are required.")
    @Size(min = 20, max = 1500, message = "Instructions must be between 20 and 1500 characters.")
    private String instructions;

    @Min(value = 1, message = "Cooking time must be at least 1 minute.")
    @Max(value = 600, message = "Cooking time cannot be more than 600 minutes.")
    private int cookingMinutes;

    @Size(max = 255, message = "Image path is too long.")
    private String imageUrl;

    @NotNull(message = "Category is required.")
    private UUID categoryId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public int getCookingMinutes() { return cookingMinutes; }
    public void setCookingMinutes(int cookingMinutes) { this.cookingMinutes = cookingMinutes; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
}
