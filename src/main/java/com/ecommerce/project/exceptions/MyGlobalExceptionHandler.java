package com.ecommerce.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName, message);
        });
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> myResourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(APIException.class)
    public ResponseEntity<String> myAPIException(APIException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }
}

/*

This is a **global exception handler** class that provides centralized error handling for a Spring MVC application.
 It specifically handles validation errors that occur when request data doesn't meet validation constraints.

## Code Breakdown

### Class Declaration
```java
@RestControllerAdvice
public class MyGlobalExceptionHandler {
```
- **`@RestControllerAdvice`**: This annotation makes this class a global exception handler for REST controllers
- It combines `@ControllerAdvice` and `@ResponseBody`, meaning responses will be serialized directly to JSON/XML
- Any exceptions thrown by controllers in the application can be caught and handled here

### Exception Handler Method
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public Map<String, String> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
```
- **`@ExceptionHandler`**: Specifies that this method handles `MethodArgumentNotValidException`
- This exception is thrown when validation fails on request body objects (typically when using `@Valid` or `@Validated`)
- The method returns a `Map<String, String>` which will be converted to JSON

### Error Processing Logic
```java
Map<String, String> response = new HashMap<>();
e.getBindingResult().getAllErrors().forEach(err -> {
    String fieldName = ((FieldError)err).getField();
    String message = err.getDefaultMessage();
    response.put(fieldName, message);
});
return response;
```
This section:
1. Creates an empty map to store field validation errors
2. Gets all validation errors from the binding result
3. For each error:
   - Casts it to `FieldError` to get the field name
   - Extracts the validation error message
   - Adds the field-message pair to the response map
4. Returns the map (which Spring will serialize to JSON)
 */