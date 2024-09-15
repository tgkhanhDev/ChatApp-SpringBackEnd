package com.chatApp.chatApp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {

    @NotEmpty(message = "Vui lòng nhập đầy đủ username!")
    String username;

    @NotEmpty(message = "Vui lòng nhập đầy đủ password!")
    @Size(min = 3, message = "Độ dài password phải tối thiểu 3 ký tự!")
    String password;
}
