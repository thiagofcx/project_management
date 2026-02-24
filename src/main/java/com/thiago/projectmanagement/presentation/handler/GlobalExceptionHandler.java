package com.thiago.projectmanagement.presentation.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.thiago.projectmanagement.domain.exception.AllocationNotFoundException;
import com.thiago.projectmanagement.domain.exception.InvalidCredentialsException;
import com.thiago.projectmanagement.domain.exception.InvalidProjectDatesException;
import com.thiago.projectmanagement.domain.exception.ProjectNotFoundException;
import com.thiago.projectmanagement.domain.exception.ResourceAlreadyAllocatedException;
import com.thiago.projectmanagement.domain.exception.ResourceAlreadyExistsException;
import com.thiago.projectmanagement.domain.exception.ResourceNotFoundException;
import com.thiago.projectmanagement.domain.exception.UnauthorizedException;
import com.thiago.projectmanagement.domain.exception.UserAlreadyExistsException;
import com.thiago.projectmanagement.domain.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InvalidProjectDatesException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidProjectDatesException(InvalidProjectDatesException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProjectNotFoundException(ProjectNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResourceAlreadyAllocatedException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAlreadyAllocatedException(ResourceAlreadyAllocatedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AllocationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAllocationNotFoundException(AllocationNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getErrorCode());
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "VALIDATION_ERROR");
        body.put("message", "Request validation failed");

        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(v -> {
                    Map<String, String> entry = new HashMap<>();
                    String path = v.getPropertyPath().toString();
                    String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                    entry.put("field", field);
                    entry.put("message", v.getMessage());
                    return entry;
                })
                .collect(Collectors.toList());
        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "VALIDATION_ERROR");
        body.put("message", "Request validation failed");

        String field = ex.getName() != null ? ex.getName() : "";
        String message = "Invalid value";
        if (ex.getRequiredType() != null) {
            String type = ex.getRequiredType().getSimpleName();
            if (type.equals("int") || type.equals("Integer") || type.equals("Long") || type.equals("long")) {
                message = "Must be a valid integer";
            } else {
                message = "Must be a valid " + type.toLowerCase();
            }
        }

        body.put("errors", List.of(Map.of("field", field, "message", message)));
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "VALIDATION_ERROR");
        body.put("message", "Request validation failed");

        List<Map<String, String>> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    Map<String, String> entry = new HashMap<>();
                    String field = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
                    entry.put("field", field);
                    entry.put("message", error.getDefaultMessage());
                    return entry;
                })
                .collect(Collectors.toList());
        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "VALIDATION_ERROR");
        body.put("message", "Request validation failed");

        Throwable cause = ex.getCause();
        String fieldName = resolveFieldNameFromParseError(cause);
        String errorMessage = "Invalid request body";
        if (cause != null && cause.getMessage() != null) {
            String msg = cause.getMessage();
            if (msg.contains("DateTimeParseException") || msg.contains("Cannot deserialize value of type `java.time.LocalDate`")) {
                errorMessage = "Invalid date format. Use ISO-8601: yyyy-MM-dd (e.g. 2026-01-28)";
            } else {
                errorMessage = msg;
            }
        }

        body.put("errors", List.of(Map.of("field", fieldName != null ? fieldName : "", "message", errorMessage)));
        return ResponseEntity.badRequest().body(body);
    }

    /** Pattern to extract field name from Jackson "reference chain" in message: ...[\"fieldName\"] */
    private static final Pattern REFERENCE_CHAIN_FIELD = Pattern.compile("\\[\"([^\"]+)\"]");

    private String resolveFieldNameFromParseError(Throwable cause) {
        Throwable current = cause;
        while (current != null) {
            String field = extractFieldFromJacksonPath(current);
            if (!field.isEmpty()) return field;
            field = extractFieldFromMessage(current.getMessage());
            if (!field.isEmpty()) return field;
            current = current.getCause();
        }
        return "";
    }

    private String extractFieldFromJacksonPath(Throwable ex) {
        if (ex == null) return "";
        try {
            Class<?> exClass = ex.getClass();
            while (exClass != null && exClass != Throwable.class) {
                String name = exClass.getName();
                if ("com.fasterxml.jackson.databind.exc.InvalidFormatException".equals(name)
                        || "com.fasterxml.jackson.databind.exc.MismatchedInputException".equals(name)) {
                    Object path = exClass.getMethod("getPath").invoke(ex);
                    if (path instanceof java.util.List<?> pathList && !pathList.isEmpty()) {
                        Object ref = pathList.get(pathList.size() - 1);
                        if (ref != null) {
                            java.lang.reflect.Method getFieldName = ref.getClass().getMethod("getFieldName");
                            Object value = getFieldName.invoke(ref);
                            if (value != null) return value.toString();
                        }
                    }
                    return "";
                }
                exClass = exClass.getSuperclass();
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    /** Fallback: extract last field from message like (through reference chain: ...[\"startDate\"]) */
    private String extractFieldFromMessage(String message) {
        if (message == null) return "";
        Matcher m = REFERENCE_CHAIN_FIELD.matcher(message);
        String last = "";
        while (m.find()) last = m.group(1);
        return last;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        MethodArgumentNotValidException validationEx = unwrapMethodArgumentNotValid(ex);
        if (validationEx != null) {
            return handleMethodArgumentNotValid(validationEx);
        }
        MethodArgumentTypeMismatchException typeMismatch = unwrapMethodArgumentTypeMismatch(ex);
        if (typeMismatch != null) {
            return handleMethodArgumentTypeMismatch(typeMismatch);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Internal Error");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private MethodArgumentNotValidException unwrapMethodArgumentNotValid(Throwable ex) {
        Throwable current = ex;
        while (current != null) {
            if (current instanceof MethodArgumentNotValidException manv) {
                return manv;
            }
            current = current.getCause();
        }
        return null;
    }

    private MethodArgumentTypeMismatchException unwrapMethodArgumentTypeMismatch(Throwable ex) {
        Throwable current = ex;
        while (current != null) {
            if (current instanceof MethodArgumentTypeMismatchException matm) {
                return matm;
            }
            current = current.getCause();
        }
        return null;
    }
}
