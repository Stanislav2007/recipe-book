package bg.softuni.recipebook.service;

import bg.softuni.recipebook.dto.ProfileRequest;
import bg.softuni.recipebook.dto.RegisterRequest;
import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.exception.ForbiddenActionException;
import bg.softuni.recipebook.exception.NotFoundException;
import bg.softuni.recipebook.model.entity.User;
import bg.softuni.recipebook.model.enums.UserRole;
import bg.softuni.recipebook.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUser currentUser;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CurrentUser currentUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUser = currentUser;
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessRuleException("Passwords do not match.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessRuleException("Username already exists.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Email already exists.");
        }
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRepository.count() == 0 ? UserRole.ADMIN : UserRole.USER);
        User saved = userRepository.save(user);
        LOGGER.info("Registered user {} with role {}", saved.getId(), saved.getRole());
    }

    public User getCurrentUserEntity() {
        if (!currentUser.isLoggedIn()) {
            throw new BusinessRuleException("You must be logged in.");
        }
        return findById(currentUser.getId());
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found."));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public ProfileRequest currentProfile() {
        User user = getCurrentUserEntity();
        ProfileRequest request = new ProfileRequest();
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());
        return request;
    }

    @Transactional
    public void updateProfile(ProfileRequest request) {
        User user = getCurrentUserEntity();
        userRepository.findByUsername(request.getUsername())
                .filter(other -> !other.getId().equals(user.getId()))
                .ifPresent(other -> { throw new BusinessRuleException("Username already exists."); });
        userRepository.findByEmail(request.getEmail())
                .filter(other -> !other.getId().equals(user.getId()))
                .ifPresent(other -> { throw new BusinessRuleException("Email already exists."); });
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        userRepository.save(user);
        refreshAuthentication(user.getUsername());
        LOGGER.info("Updated profile for user {}", user.getId());
    }

    private void refreshAuthentication(String username) {
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuthentication == null || !currentAuthentication.isAuthenticated()) {
            return;
        }
        UsernamePasswordAuthenticationToken refreshedAuthentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        username,
                        currentAuthentication.getCredentials(),
                        currentAuthentication.getAuthorities());
        refreshedAuthentication.setDetails(currentAuthentication.getDetails());
        SecurityContextHolder.getContext().setAuthentication(refreshedAuthentication);
    }

    @Transactional
    public void updateRole(UUID userId, UserRole role) {
        if (!currentUser.isAdmin()) {
            throw new ForbiddenActionException("Only administrators can manage roles.");
        }
        User user = findById(userId);
        if (user.getId().equals(currentUser.getId()) && role != UserRole.ADMIN) {
            throw new BusinessRuleException("You cannot remove your own administrator role.");
        }
        user.setRole(role);
        userRepository.save(user);
        LOGGER.info("Administrator {} changed role of user {} to {}", currentUser.getId(), userId, role);
    }
}
