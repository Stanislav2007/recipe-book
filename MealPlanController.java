package bg.softuni.mealplan.service;

import bg.softuni.mealplan.dto.MealPlanRequest;
import bg.softuni.mealplan.entity.MealPlan;
import bg.softuni.mealplan.exception.DuplicateMealPlanException;
import bg.softuni.mealplan.exception.MealPlanNotFoundException;
import bg.softuni.mealplan.repository.MealPlanRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MealPlanServiceTest {
    private final MealPlanRepository repository = mock(MealPlanRepository.class);
    private final MealPlanService service = new MealPlanService(repository);

    @Test
    void createSavesValidPlan() {
        MealPlanRequest request = request();
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        assertEquals("Dinner", service.create(request).mealType());
        verify(repository).save(any(MealPlan.class));
    }

    @Test
    void createRejectsDuplicate() {
        MealPlanRequest request = request();
        when(repository.existsByUserIdAndRecipeIdAndPlannedDateAndMealType(
                request.userId(), request.recipeId(), request.plannedDate(), request.mealType())).thenReturn(true);
        assertThrows(DuplicateMealPlanException.class, () -> service.create(request));
    }

    @Test
    void completeUpdatesOwnedPlan() {
        UUID id = UUID.randomUUID(); UUID userId = UUID.randomUUID();
        MealPlan plan = plan(userId);
        when(repository.findById(id)).thenReturn(Optional.of(plan));
        when(repository.save(plan)).thenReturn(plan);
        assertTrue(service.complete(id, userId).completed());
    }


    @Test
    void createNormalizesMealType() {
        MealPlanRequest request = new MealPlanRequest(UUID.randomUUID(), UUID.randomUUID(), "Soup",
                LocalDate.now().plusDays(1), "dinner");
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals("Dinner", service.create(request).mealType());
    }

    @Test
    void findByUserMapsPlans() {
        UUID userId = UUID.randomUUID();
        when(repository.findAllByUserIdOrderByPlannedDateAsc(userId)).thenReturn(List.of(plan(userId)));

        assertEquals(1, service.findByUser(userId).size());
    }

    @Test
    void deleteRemovesOwnedPlan() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        MealPlan plan = plan(userId);
        when(repository.findById(id)).thenReturn(Optional.of(plan));

        service.delete(id, userId);

        verify(repository).delete(plan);
    }

    @Test
    void deleteRejectsDifferentOwner() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(plan(UUID.randomUUID())));
        assertThrows(MealPlanNotFoundException.class, () -> service.delete(id, UUID.randomUUID()));
    }

    private MealPlanRequest request() {
        return new MealPlanRequest(UUID.randomUUID(), UUID.randomUUID(), "Soup",
                LocalDate.now().plusDays(1), "Dinner");
    }

    private MealPlan plan(UUID userId) {
        MealPlan plan = new MealPlan();
        plan.setUserId(userId); plan.setRecipeId(UUID.randomUUID()); plan.setRecipeTitle("Soup");
        plan.setPlannedDate(LocalDate.now().plusDays(1)); plan.setMealType("Dinner");
        return plan;
    }
}
