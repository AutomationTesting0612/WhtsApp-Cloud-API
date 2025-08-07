package com.poc.whtsapp.message.service;

import com.poc.whtsapp.message.entity.UserSegment;
import com.poc.whtsapp.message.repository.UserSegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SegmentService {

    @Autowired
    private UserSegmentRepository segmentRepository;

    public UserSegment createSegment(UserSegment segment) {
        return segmentRepository.save(segment);
    }

    public List<UserSegment> getAllSegments() {
        return segmentRepository.findAll();
    }

    public Optional<UserSegment> getSegmentById(Long id) {
        return segmentRepository.findById(id);
    }

    public UserSegment updateSegment(Long id, UserSegment updatedSegment) {
        return segmentRepository.findById(id).map(segment -> {
            segment.setName(updatedSegment.getName());
            segment.setPhoneNumbers(updatedSegment.getPhoneNumbers());
            return segmentRepository.save(segment);
        }).orElse(null);
    }

    public void deleteSegment(Long id) {
        segmentRepository.deleteById(id);
    }
}
