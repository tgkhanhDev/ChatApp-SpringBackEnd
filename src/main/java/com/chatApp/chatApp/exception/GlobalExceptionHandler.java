package com.chatApp.chatApp.exception;

import com.chatApp.chatApp.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

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
}
