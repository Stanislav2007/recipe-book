package bg.softuni.recipebook.controller;

import bg.softuni.recipebook.exception.BusinessRuleException;
import bg.softuni.recipebook.exception.ForbiddenActionException;
import bg.softuni.recipebook.exception.NotFoundException;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ErrorControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler(BusinessRuleException.class)
    public String handleBusinessRule(
            BusinessRuleException exception,
            Model model,
            HttpServletResponse response) {
        return renderError(exception.getMessage(), HttpStatus.BAD_REQUEST, model, response);
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public String handleForbidden(
            ForbiddenActionException exception,
            Model model,
            HttpServletResponse response) {
        return renderError(exception.getMessage(), HttpStatus.FORBIDDEN, model, response);
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(
            NotFoundException exception,
            Model model,
            HttpServletResponse response) {
        return renderError(exception.getMessage(), HttpStatus.NOT_FOUND, model, response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleInvalidParameter(
            MethodArgumentTypeMismatchException exception,
            Model model,
            HttpServletResponse response) {
        return renderError("Invalid request parameter.", HttpStatus.BAD_REQUEST, model, response);
    }

    @ExceptionHandler(FeignException.class)
    public String handleMicroserviceError(
            FeignException exception,
            Model model,
            HttpServletResponse response) {
        LOGGER.warn("Meal-plan service request failed with status {}", exception.status());
        return renderError(
                "The meal-plan service could not complete the request. Please try again.",
                HttpStatus.SERVICE_UNAVAILABLE,
                model,
                response);
    }

    @ExceptionHandler(Exception.class)
    public String handleUnexpectedError(
            Exception exception,
            Model model,
            HttpServletResponse response) {
        LOGGER.error("Unexpected application error", exception);
        return renderError(
                "An unexpected error occurred. Please try again.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                model,
                response);
    }

    private String renderError(
            String message,
            HttpStatus status,
            Model model,
            HttpServletResponse response) {
        response.setStatus(status.value());
        model.addAttribute("message", message);
        model.addAttribute("status", status.value());
        return "error/custom-error";
    }
}
