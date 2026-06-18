<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
<head><title>Create Recipe</title><link rel="stylesheet" href="/css/style.css"></head>
<body>
<div th:replace="~{fragments :: nav}"></div>
<main class="container form-page">
    <section class="panel">
        <h1>Create Recipe</h1>
        <p class="hint">Добави заглавие, категория, снимка и инструкции. За снимка можеш да използваш пример: /images/lasagna-tray.jpg</p>
        <form method="post" th:action="@{/recipes/create}" th:object="${recipeRequest}" class="form-grid">
            <div class="field full"><label>Title</label><input th:field="*{title}" placeholder="Домашна лазаня"><div class="error" th:errors="*{title}"></div></div>
            <div><label>Category</label><select th:field="*{categoryId}"><option value="">Choose category</option><option th:each="category : ${categories}" th:value="${category.id}" th:text="${category.name}"></option></select><div class="error" th:errors="*{categoryId}"></div></div>
            <div><label>Cooking minutes</label><input type="number" th:field="*{cookingMinutes}" min="1" max="600"><div class="error" th:errors="*{cookingMinutes}"></div></div>
            <div class="field full"><label>Image path or URL</label><input th:field="*{imageUrl}" placeholder="/images/caprese.jpg"><p class="hint">Налични примерни снимки: /images/caprese.jpg, /images/lasagna-tray.jpg, /images/dessert.jpg, /images/shopska.jpg, /images/caesar.jpg, /images/burger.jpg, /images/rolls.jpg</p><div class="error" th:errors="*{imageUrl}"></div></div>
            <div class="field full"><label>Ingredients</label><textarea th:field="*{ingredients}" placeholder="домати, моцарела, босилек..."></textarea><div class="error" th:errors="*{ingredients}"></div></div>
            <div class="field full"><label>Instructions</label><textarea th:field="*{instructions}" placeholder="Опиши стъпките за приготвяне..."></textarea><div class="error" th:errors="*{instructions}"></div></div>
            <div class="actions"><button class="btn">Save recipe</button><a class="btn secondary" href="/recipes">Cancel</a></div>
        </form>
    </section>
</main>
<div th:replace="~{fragments :: footer}"></div>
</body>
</html>
