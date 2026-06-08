package bg.softuni.recipebook.dto;

import jakarta.validation.constraints.*;

public class CategoryRequest {
    @NotBlank @Size(min = 3, max = 50)
    private String name;
    @NotBlank @Size(min = 5, max = 255)
    private String description;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
