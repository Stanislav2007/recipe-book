package bg.softuni.mealplan.repository;

import bg.softuni.mealplan.entity.MealPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class MealPlanRepositoryIntegrationTest {
    @Autowired private MealPlanRepository repository;

    @Test
    void findsPlansByUserOrderedByDate() {
        UUID userId = UUID.randomUUID();
        repository.save(plan(userId, LocalDate.now().plusDays(2)));
        repository.save(plan(userId, LocalDate.now().plusDays(1)));
        assertEquals(2, repository.findAllByUserIdOrderByPlannedDateAsc(userId).size());
    }

    private MealPlan plan(UUID userId, LocalDate date) {
        MealPlan plan = new MealPlan();
        plan.setUserId(userId); plan.setRecipeId(UUID.randomUUID()); plan.setRecipeTitle("Soup");
        plan.setPlannedDate(date); plan.setMealType("Dinner");
        return plan;
    }
}
