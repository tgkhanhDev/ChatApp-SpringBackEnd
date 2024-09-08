package com.chatApp.chatApp.dto.request;

import com.chatApp.chatApp.model.Permission;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {

    String name;
    String description;
    Set<String> permissions;
}
