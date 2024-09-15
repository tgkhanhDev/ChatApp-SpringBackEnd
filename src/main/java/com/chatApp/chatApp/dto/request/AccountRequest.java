package com.chatApp.chatApp.dto.request;

import com.chatApp.chatApp.validator.DobConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {
    String username;

    @NotEmpty(message = "Vui lòng nhập đầy đủ password!")
    @Size(min = 3, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 8)
    LocalDate dob;

    Set<String> roles;
}
