package bg.softuni.recipebook.service;

import bg.softuni.recipebook.dto.CategoryRequest;
import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.exception.NotFoundException;
import bg.softuni.recipebook.model.entity.Category;
import bg.softuni.recipebook.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) { this.categoryRepository = categoryRepository; }

    @Transactional
    public void seedCategories() {
        List.of("Breakfast", "Lunch", "Dinner", "Dessert", "Vegetarian").forEach(name ->
                categoryRepository.findByName(name).orElseGet(() -> {
                    Category category = new Category();
                    category.setName(name);
                    category.setDescription(name + " recipes");
                    return categoryRepository.save(category);
                }));
    }

    public List<Category> findAll() { return categoryRepository.findAll(); }

    public Category findById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found."));
    }

    @Transactional
    public void create(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName().trim()).isPresent()) {
            throw new BusinessRuleException("Category already exists.");
        }
        Category category = new Category();
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription().trim());
        Category saved = categoryRepository.save(category);
        LOGGER.info("Created category {}", saved.getId());
    }
}
