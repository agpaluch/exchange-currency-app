package com.agpaluch.exchange.currency.persistence;

import com.agpaluch.exchange.currency.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByLastName(String lastName);
}
