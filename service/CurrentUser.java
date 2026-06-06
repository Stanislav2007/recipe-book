package bg.softuni.recipebook.service;

import bg.softuni.recipebook.model.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.util.UUID;

@Component
@SessionScope
public class CurrentUser {
    private UUID id;
    private String username;
    private UserRole role;
    public boolean isLoggedIn() { return id != null; }
    public boolean isAdmin() { return role == UserRole.ADMIN; }
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public UserRole getRole() { return role; }
    public void login(UUID id, String username, UserRole role) { this.id = id; this.username = username; this.role = role; }
    public void logout() { this.id = null; this.username = null; this.role = null; }
}
