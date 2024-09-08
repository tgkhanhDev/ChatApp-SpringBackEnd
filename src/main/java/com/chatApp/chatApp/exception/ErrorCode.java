package com.chatApp.chatApp.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {

    UNCATEGORIZED(401, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(405, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(401, "User already existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(401, "Độ dài tên phải tối thiểu {min} ký tự!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(401, "Độ dài mật khẩu phải bằng {min} hoặc trên 20!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(405, "Unauthenticated, Your TOKEN may expired!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(401, "You does not have permission", HttpStatus.FORBIDDEN),
    ROLE_INVALID(405, "Role invalid", HttpStatus.BAD_REQUEST),
    PERMISSION_INVALID(401, "Permission invalid", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(401, "Permission not found", HttpStatus.NOT_FOUND),
    INVALID_DOB(401, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_FOUND(404, "Account not found", HttpStatus.NOT_FOUND);


    int code;
    String message;
    HttpStatusCode statusCode;
}
