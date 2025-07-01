package com.poc.whtsapp.message.repository;

import com.poc.whtsapp.message.entity.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageStatusRepository extends JpaRepository<MessageStatus, String> {
}
