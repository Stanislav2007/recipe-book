package bg.softuni.mealplan.controller;

import bg.softuni.mealplan.dto.MealPlanRequest;
import bg.softuni.mealplan.dto.MealPlanResponse;
import bg.softuni.mealplan.service.MealPlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealPlanController.class)
class MealPlanControllerApiTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private MealPlanService service;

    @Test
    void getReturnsPlans() throws Exception {
        UUID userId = UUID.randomUUID();
        when(service.findByUser(userId)).thenReturn(List.of());
        mockMvc.perform(get("/api/meal-plans/user/{userId}", userId))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void invalidUserIdReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/meal-plans/user/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request parameter."));
    }

    @Test
    void postCreatesPlan() throws Exception {
        MealPlanRequest request = new MealPlanRequest(UUID.randomUUID(), UUID.randomUUID(), "Soup",
                LocalDate.now().plusDays(1), "Dinner");
        when(service.create(any())).thenReturn(new MealPlanResponse(UUID.randomUUID(), request.userId(),
                request.recipeId(), request.recipeTitle(), request.plannedDate(), request.mealType(), false));
        mockMvc.perform(post("/api/meal-plans").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.mealType").value("Dinner"));
    }


    @Test
    void completeReturnsUpdatedPlan() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(service.complete(id, userId)).thenReturn(new MealPlanResponse(id, userId, UUID.randomUUID(),
                "Soup", LocalDate.now().plusDays(1), "Dinner", true));

        mockMvc.perform(put("/api/meal-plans/{id}/complete", id).param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        doNothing().when(service).delete(id, userId);

        mockMvc.perform(delete("/api/meal-plans/{id}", id).param("userId", userId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void invalidMealTypeReturnsBadRequest() throws Exception {
        MealPlanRequest request = new MealPlanRequest(UUID.randomUUID(), UUID.randomUUID(), "Soup",
                LocalDate.now().plusDays(1), "Midnight feast");

        mockMvc.perform(post("/api/meal-plans").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void invalidPostReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/meal-plans").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").exists());
    }
}
