# Recipe Book — SoftUni Advanced Spring Project

Original two-application Spring Boot solution for managing recipes, favorites, categories and personal meal plans.

## Technology stack

- Java 17+
- Spring Boot 3.4.0
- Maven
- Spring MVC and Thymeleaf
- Spring Data JPA
- Spring Security with BCrypt, roles and enabled CSRF
- OpenFeign
- MySQL 8.4 — separate database per application
- Spring Cache and Spring Scheduling
- JUnit 5, Mockito, MockMvc, H2 (tests only) and JaCoCo
- Docker and Docker Compose

## Architecture

### Main application — port 8080

Domain entities: `Recipe`, `Category`, `Favorite`; `User` supports authentication and profiles. Every entity has exactly one JPA repository and at least one service. The application uses layered controllers, services and repositories.

### Meal Plan REST microservice — port 8081

Domain entity: `MealPlan`, with its own repository, service, REST controller, validation, exception handling and separate `meal_plan_db` database.

The main application consumes the service through OpenFeign:

- `GET /api/meal-plans/user/{userId}`
- `POST /api/meal-plans`
- `PUT /api/meal-plans/{id}/complete`
- `DELETE /api/meal-plans/{id}`

## State-changing domain functionalities

Main application:

1. Create a recipe.
2. Edit an owned recipe; administrators may edit all recipes.
3. Delete an owned recipe; administrators may delete all recipes.
4. Create a category as an administrator.
5. Add a recipe to favorites.
6. Remove a recipe from favorites.
7. Edit the authenticated user's profile.
8. Change another user's role as an administrator.

Microservice functionalities triggered from the main frontend:

1. Schedule a recipe for a date and meal type.
2. Mark a planned meal as completed.
3. Delete a planned meal.

Every required domain-changing service operation contains a log statement and gives visible feedback to the user.

## Security

- Public: home, registration, login, CSS and images.
- Authenticated: recipes, favorites, meal plans and own profile.
- ADMIN only: dashboard, category creation and role management.
- Passwords are BCrypt-hashed.
- CSRF remains enabled; Thymeleaf forms include the token automatically.
- An administrator cannot remove their own administrator role.

Demo administrator: `demo` / `demo123`.

## Dynamic pages

Home, registration, login, all recipes, recipe details, create recipe, edit recipe, my recipes, favorites, meal plan, profile and admin dashboard. The application therefore contains more than ten complete pages, with at least nine dynamic pages.

## Validation and errors

DTO, entity and service-level rules are present. Both applications have handlers for a built-in framework exception and custom application exceptions. Errors render a custom page or structured REST response; the Whitelabel page is not used for handled operations.

## Scheduling and caching

- Cron job at 03:00 clears and rebuilds the recipe cache from the database.
- Fixed-delay job clears application caches.
- Recipe listing uses Spring Cache and create/update/delete evict the cache.

## Tests and coverage

Both applications include unit, JPA integration and MockMvc API tests. JaCoCo runs on `mvn verify` and enforces a 70% line threshold on the configured application-code scope. The generated reports must be checked before submission.

```bash
./verify-project.sh
```

Reports are generated under `target/site/jacoco/index.html` in each application.

## Run with Docker

```bash
docker compose up --build
```

Open `http://localhost:8080`.

## Run locally

Start both required MySQL databases:

```bash
docker compose up -d recipe-db meal-plan-db
```

Start the microservice:

```bash
cd meal-plan-service
MEAL_DB_URL='jdbc:mysql://localhost:3307/meal_plan_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC' \
MEAL_DB_USERNAME=root MEAL_DB_PASSWORD=root mvn spring-boot:run
```

Start the main application:

```bash
DB_URL='jdbc:mysql://localhost:3306/recipe_book?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC' \
DB_USERNAME=root DB_PASSWORD=root MEAL_PLAN_SERVICE_URL=http://localhost:8081 mvn spring-boot:run
```

## Requirement traceability

| Requirement | Implementation |
|---|---|
| Java 17+, Spring Boot 3.4.0, Maven | Both `pom.xml` files |
| Two independent applications and ports | `RecipeBookApplication` 8080; `MealPlanApplication` 8081 |
| Separate relational databases | `recipe_book`; `meal_plan_db` |
| UUID identifiers | All entities |
| 3+ main domain entities | Recipe, Category, Favorite |
| 1+ microservice entity | MealPlan |
| 10+ pages, 9+ dynamic | 12 Thymeleaf pages |
| Feign GET and 2+ write endpoints | `MealPlanClient` and REST controller |
| 6+ main functionalities | Recipe CRUD, category create, favorite add/remove |
| 2+ microservice functionalities | Schedule, complete, delete |
| Authentication, authorization and roles | `SecurityConfig`, USER/ADMIN |
| Admin role management | Admin dashboard role form |
| Own profile view/edit | `/profile` |
| Validation on all layers | DTO annotations, entity annotations, service rules |
| Two error handlers per app | Controller advice classes |
| Cron and non-cron scheduling | `RecipeMaintenanceJobs` |
| Complete Spring caching | `RecipeService`, scheduled eviction |
| Unit, integration and API tests | `src/test` in both applications |
| 70% line coverage gate | JaCoCo `check` in both POM files |
| Logging per functionality | Service classes |
| README and Docker setup | This file and Docker files |

## Verification before submission

Run both applications independently and keep the generated JaCoCo reports:

```bash
mvn clean verify
mvn -f meal-plan-service/pom.xml clean verify
```

The reports are generated under `target/site/jacoco/index.html` in each application.
The main application uses `recipe_book`; the microservice uses `meal_plan_db` and port `8081`.

## Required domain functionalities

Main application state-changing operations:

1. Create a recipe.
2. Edit an owned recipe.
3. Delete an owned recipe.
4. Create a category as administrator.
5. Add a recipe to favorites.
6. Remove a recipe from favorites.

Microservice state-changing operations invoked through Feign:

1. Add a recipe to a meal plan.
2. Mark a meal-plan item as completed.
3. Delete a meal-plan item.

Authentication, registration, profile editing, and role management are implemented but are not counted among the six required main domain functionalities.
