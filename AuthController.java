package bg.softuni.recipebook.controller;

import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.exception.ForbiddenActionException;
import bg.softuni.recipebook.exception.NotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorControllerAdvice {
    @ExceptionHandler({BusinessRuleException.class, ForbiddenActionException.class, NotFoundException.class})
    public String handleDomainErrors(RuntimeException ex, Model model) { model.addAttribute("message", ex.getMessage()); return "error/custom-error"; }
}
