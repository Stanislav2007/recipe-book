package bg.softuni.recipebook.controller;

import bg.softuni.recipebook.service.CurrentUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {
    private final CurrentUser currentUser;
    public GlobalModelAdvice(CurrentUser currentUser) { this.currentUser = currentUser; }
    @ModelAttribute("currentUser") public CurrentUser currentUser() { return currentUser; }
}
