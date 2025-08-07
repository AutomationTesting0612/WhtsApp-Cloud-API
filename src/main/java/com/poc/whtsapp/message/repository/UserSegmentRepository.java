package com.poc.whtsapp.message.repository;

import com.poc.whtsapp.message.entity.UserSegment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSegmentRepository  extends JpaRepository<UserSegment, Long> {

}
