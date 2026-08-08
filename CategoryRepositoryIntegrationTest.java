<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments :: head('My Profile')}"></head>
<body>
<nav th:replace="~{fragments :: nav}"></nav>
<main class="container page-card narrow">
    <h1>My Profile</h1>
    <div class="alert success" th:if="${message}" th:text="${message}"></div>
    <p><strong>Role:</strong> <span th:text="${user.role}"></span></p>
    <form th:action="@{/profile}" th:object="${profileRequest}" method="post" class="form-grid">
        <label>Username<input th:field="*{username}" required minlength="3" maxlength="30"></label>
        <small class="field-error" th:errors="*{username}"></small>
        <label>Email<input type="email" th:field="*{email}" required maxlength="80"></label>
        <small class="field-error" th:errors="*{email}"></small>
        <button class="btn" type="submit">Save profile</button>
    </form>
</main>
</body>
</html>
