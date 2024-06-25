package com.example.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	@Query(value = "SELECT * FROM customer_transaction " + "WHERE DATE(timestamp) BETWEEN :start AND :end "
			+ "AND transaction_type = :transactionType " + "AND account_number = :accountNumber", nativeQuery = true)
	List<Transaction> findFilteredTransaction(@Param("start") LocalDate startDate,
			@Param("end") LocalDate endDate, @Param("transactionType") String transactionType,
			@Param("accountNumber") Long accountNumber);

	@Query(value = "SELECT * FROM customer_transaction " + "WHERE DATE(timestamp) BETWEEN :startDate AND :endDate "
			+ "AND account_number = :accountNumber", nativeQuery = true)
	List<Transaction> findTransaction(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, @Param("accountNumber") Long accountNumber);

	
	@Query(value = "SELECT * FROM customer_transaction WHERE account_number = :accountNumber ORDER BY DATE(timestamp) DESC LIMIT 5", nativeQuery = true)
	List<Transaction> getFiveTransaction(@Param("accountNumber") Long accountNumber);

}
