package bg.softuni.recipebook.service;

import bg.softuni.recipebook.model.entity.User;
import bg.softuni.recipebook.model.enums.UserRole;
import bg.softuni.recipebook.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUser {
    private final UserRepository userRepository;

    public CurrentUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public boolean isAdmin() {
        return getRole() == UserRole.ADMIN;
    }

    public UUID getId() {
        return currentEntity().getId();
    }

    public String getUsername() {
        return currentEntity().getUsername();
    }

    public UserRole getRole() {
        return currentEntity().getRole();
    }

    private User currentEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !isLoggedIn()) {
            throw new IllegalStateException("No authenticated user.");
        }
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user no longer exists."));
    }
}
