package bg.softuni.recipebook.service.scheduling;

import bg.softuni.recipebook.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RecipeMaintenanceJobs {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeMaintenanceJobs.class);

    private final RecipeService recipeService;
    private final CacheManager cacheManager;

    public RecipeMaintenanceJobs(RecipeService recipeService, CacheManager cacheManager) {
        this.recipeService = recipeService;
        this.cacheManager = cacheManager;
    }

    @Scheduled(cron = "${recipes.cache.warmup.cron:0 0 3 * * *}")
    void rebuildRecipeCache() {
        clearCache("recipes");
        int recipeCount = recipeService.findAllRecipes().size();
        LOGGER.info("Recipe cache rebuilt with {} recipes", recipeCount);
    }

    @Scheduled(fixedDelayString = "${cache.eviction.delay:1800000}")
    void clearRecipeCaches() {
        cacheManager.getCacheNames().forEach(this::clearCache);
        LOGGER.info("Application caches cleared");
    }

    private void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
