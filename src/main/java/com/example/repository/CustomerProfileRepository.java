package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.dto.GetEmailDTO;
import com.example.model.CustomerProfile;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
	Optional<CustomerProfile> findByCustomerIdAndPassword(Long customerId, String password);


    @Query(value = "SELECT * FROM customer_profile WHERE email = :email", nativeQuery = true)
    Optional<CustomerProfile> findByEmail(@Param("email") String email);
	
	@Query("SELECT new com.example.dto.GetEmailDTO(cp.email) " +
            "FROM CustomerCardAccount cca " +
            "JOIN cca.customerProfile cp " +
            "WHERE cca.accountNumber = :accountNumber")
    GetEmailDTO findEmail(@Param("accountNumber") Long accountNumber);

	@Query(value = "SELECT name FROM customer_profile WHERE customer_id = :customerId", nativeQuery = true)
	String getName(@Param("customerId") Long customerId);
}
