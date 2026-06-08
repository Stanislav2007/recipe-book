<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
<head><title>Favorites</title><link rel="stylesheet" href="/css/style.css"></head>
<body>
<div th:replace="~{fragments :: nav}"></div>
<main class="container">
    <div class="section-title"><div><h1>Favorites</h1><p>Твоите запазени рецепти.</p></div></div>
    <div class="empty" th:if="${#lists.isEmpty(favorites)}">Все още нямаш любими рецепти.</div>
    <div class="grid">
        <article class="card" th:each="favorite : ${favorites}">
            <img class="recipe-img" th:src="${favorite.recipe.imageUrl != null and !#strings.isEmpty(favorite.recipe.imageUrl) ? favorite.recipe.imageUrl : '/images/lasagna-plate.jpg'}" th:alt="${favorite.recipe.title}">
            <div class="card-body"><span class="tag" th:text="${favorite.recipe.category.name}"></span><h3 th:text="${favorite.recipe.title}"></h3><div class="actions"><a class="btn secondary" th:href="@{/recipes/{id}(id=${favorite.recipe.id})}">Details</a><form class="inline" method="post" th:action="@{/favorites/{id}/remove(id=${favorite.recipe.id})}"><button class="danger">Remove</button></form></div></div>
        </article>
    </div>
</main>
<div th:replace="~{fragments :: footer}"></div>
</body>
</html>
