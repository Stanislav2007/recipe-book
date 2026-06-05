package bg.softuni.recipebook.controller;

import bg.softuni.recipebook.service.FavoriteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    public FavoriteController(FavoriteService favoriteService) { this.favoriteService = favoriteService; }
    @GetMapping public String favorites(Model model) { model.addAttribute("favorites", favoriteService.findMyFavorites()); return "user/favorites"; }
    @PostMapping("/{recipeId}/add") public String add(@PathVariable UUID recipeId) { favoriteService.add(recipeId); return "redirect:/favorites"; }
    @PostMapping("/{recipeId}/remove") public String remove(@PathVariable UUID recipeId) { favoriteService.remove(recipeId); return "redirect:/favorites"; }
}
