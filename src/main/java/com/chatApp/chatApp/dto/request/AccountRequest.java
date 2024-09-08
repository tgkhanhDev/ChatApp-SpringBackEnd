package com.chatApp.chatApp.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {
    String username;
    String password;
    String firstName;
    String lastName;
    Date dob;
    Set<String> roles;
}
