package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.CustomerFeedback;

@Repository
public interface CustomerFeedbackRepository extends JpaRepository<CustomerFeedback, Long> {
}
