package com.example.tutocrmapp.repositories;

import com.example.tutocrmapp.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    Optional<Customer> findByIdAndPhoneNumber(long customerId, String phoneNumber);
}
