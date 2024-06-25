package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.CreditCard;

@Repository
public interface CardRepository extends JpaRepository<CreditCard, String> {
	Optional<CreditCard> findByCardType(String cardType);

}
