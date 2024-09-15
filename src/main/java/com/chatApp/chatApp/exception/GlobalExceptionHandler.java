package com.chatApp.chatApp.exception;

import com.chatApp.chatApp.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //Some regular validators
    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max";

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {

        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
//        apiResponse.setData(null);

        return ResponseEntity.badRequest().body(apiResponse);

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(404);
        apiResponse.setMessage("Resource not found");
        return ResponseEntity.status(404).body(apiResponse);
    }

    // Xử lý ngoại lệ NoResourceFoundException cho tài nguyên tĩnh
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(404);
        apiResponse.setMessage("Static resource not found, please check your path carefully!");
        return ResponseEntity.status(404).body(apiResponse);
    }

//    @ExceptionHandler(value = RuntimeException.class)
//    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
//
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED.getCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED.getMessage()+ "TAU VO DAY");
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

    // Handling AccessDeniedException for authorization failures
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(403); // Forbidden
        apiResponse.setMessage("You do not have the required permissions to access this resource.");
        return ResponseEntity.status(403).body(apiResponse);
    }

    // Handling AuthenticationException for authentication failures
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(401); // Unauthorized
        apiResponse.setMessage("Authentication failed. Please login with valid credentials.");
        return ResponseEntity.status(401).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation = exception.getBindingResult()
                    .getAllErrors().stream().findFirst().orElseThrow().unwrap(ConstraintViolation.class);
//            log.info("ConstraintViolation: {}", constraintViolation);
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
            //Return min... message... payload... groups...

//            log.info("Attributes: {}", attributes);

        } catch (IllegalArgumentException e) {
//            errorCode = ErrorCode.INVALID_KEY;
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) ?
                mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage()
        );

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes){
        String minValue = String.valueOf( attributes.get(MIN_ATTRIBUTE) );
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
