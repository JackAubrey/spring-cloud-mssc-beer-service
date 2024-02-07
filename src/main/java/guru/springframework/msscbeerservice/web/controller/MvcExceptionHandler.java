package guru.springframework.msscbeerservice.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class MvcExceptionHandler {
    /**
     * taken initially from Baeldung validation tutorial.
     * In order to the GlobalException works with Spring, you must return a ResponseEntity
     * @param ex the exception found
     * @return a map with validation errors found
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach( error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundExceptions(
            ResourceNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .message("Resource Not Found")
                .errors( List.of(ex.getLocalizedMessage()) )
                .status( HttpStatus.NOT_FOUND )
                .build();

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
