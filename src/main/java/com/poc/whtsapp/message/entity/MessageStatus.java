package com.poc.whtsapp.message.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageStatus {

    @Id
    private String messageId;

    private String recipientId;
    private String status;
    private String timestamp;
    private String errorReason;
}
