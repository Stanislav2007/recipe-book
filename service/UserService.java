package bg.softuni.recipebook.service;

import bg.softuni.recipebook.dto.LoginRequest;
import bg.softuni.recipebook.dto.RegisterRequest;
import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.exception.NotFoundException;
import bg.softuni.recipebook.model.entity.User;
import bg.softuni.recipebook.model.enums.UserRole;
import bg.softuni.recipebook.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUser currentUser;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CurrentUser currentUser) {
        this.userRepository = userRepository; this.passwordEncoder = passwordEncoder; this.currentUser = currentUser;
    }
    public void register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) throw new BusinessRuleException("Passwords do not match.");
        if (userRepository.existsByUsername(request.getUsername())) throw new BusinessRuleException("Username already exists.");
        if (userRepository.existsByEmail(request.getEmail())) throw new BusinessRuleException("Email already exists.");
        User user = new User();
        user.setUsername(request.getUsername()); user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRepository.count() == 0 ? UserRole.ADMIN : UserRole.USER);
        userRepository.save(user);
    }
    public boolean login(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername()).filter(u -> passwordEncoder.matches(request.getPassword(), u.getPassword())).map(u -> { currentUser.login(u.getId(), u.getUsername(), u.getRole()); return true; }).orElse(false);
    }
    public void logout() { currentUser.logout(); }
    public User getCurrentUserEntity() { if (!currentUser.isLoggedIn()) throw new BusinessRuleException("You must be logged in."); return findById(currentUser.getId()); }
    public User findById(UUID id) { return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found.")); }
    public List<User> findAllUsers() { return userRepository.findAll(); }
}
