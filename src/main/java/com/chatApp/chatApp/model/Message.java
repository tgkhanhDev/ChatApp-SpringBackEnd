package com.chatApp.chatApp.model;


import com.chatApp.chatApp.enums.EStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    String senderName;
    String receiverName;
    String message;
    String date;
    EStatus status;
}
