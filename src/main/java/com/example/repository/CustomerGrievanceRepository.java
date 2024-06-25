package com.example.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.CustomerGrievance;

@Repository
public interface CustomerGrievanceRepository extends JpaRepository<CustomerGrievance, Long> {
	@Query(value = "SELECT * FROM customer_grievance WHERE account_number = ?1", nativeQuery = true)
	List<CustomerGrievance> findAllByAccountNumber(Long accountNumber);
}

