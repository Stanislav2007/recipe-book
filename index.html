package bg.softuni.recipebook.service;

import bg.softuni.recipebook.dto.CategoryRequest;
import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.exception.NotFoundException;
import bg.softuni.recipebook.model.entity.Category;
import bg.softuni.recipebook.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) { this.categoryRepository = categoryRepository; }
    public void seedCategories() {
        List.of("Breakfast", "Lunch", "Dinner", "Dessert", "Vegetarian").forEach(name -> categoryRepository.findByName(name).orElseGet(() -> { Category c = new Category(); c.setName(name); c.setDescription(name + " recipes"); return categoryRepository.save(c); }));
    }
    public List<Category> findAll() { return categoryRepository.findAll(); }
    public Category findById(UUID id) { return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found.")); }
    public void create(CategoryRequest request) { if (categoryRepository.findByName(request.getName()).isPresent()) throw new BusinessRuleException("Category already exists."); Category c = new Category(); c.setName(request.getName()); c.setDescription(request.getDescription()); categoryRepository.save(c); }
}
