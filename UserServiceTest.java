<!doctype html>
<html xmlns:th="http://www.thymeleaf.org">
<head><title>Register</title><link rel="stylesheet" href="/css/style.css"></head>
<body>
<div th:replace="~{fragments :: nav}"></div>
<main class="container auth-card">
    <section class="panel">
        <div class="eyebrow">Join Recipe Book</div>
        <h1>Register</h1>
        <p class="error" th:if="${registerError}" th:text="${registerError}"></p>
        <form method="post" th:action="@{/register}" th:object="${registerRequest}">
            <label>Username</label><input th:field="*{username}"><div class="error" th:errors="*{username}"></div>
            <label>Email</label><input th:field="*{email}"><div class="error" th:errors="*{email}"></div>
            <label>Password</label><input type="password" th:field="*{password}"><div class="error" th:errors="*{password}"></div>
            <label>Confirm password</label><input type="password" th:field="*{confirmPassword}"><div class="error" th:errors="*{confirmPassword}"></div>
            <div class="actions"><button class="btn">Register</button><a class="btn secondary" href="/login">Already registered?</a></div>
        </form>
    </section>
</main>
<div th:replace="~{fragments :: footer}"></div>
</body>
</html>
