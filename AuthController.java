package bg.softuni.recipebook.config;

import bg.softuni.recipebook.service.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final CurrentUser currentUser;
    public AuthInterceptor(CurrentUser currentUser) { this.currentUser = currentUser; }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        boolean guestAllowed = uri.equals("/") || uri.equals("/login") || uri.equals("/register") || uri.startsWith("/css") || uri.startsWith("/images") || uri.startsWith("/h2-console") || uri.equals("/error");
        if (!currentUser.isLoggedIn() && !guestAllowed) { response.sendRedirect("/login"); return false; }
        if (uri.startsWith("/admin") && !currentUser.isAdmin()) { response.sendRedirect("/recipes"); return false; }
        return true;
    }
}
