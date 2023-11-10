package com.agpaluch.exchange.currency.persistence;

import com.agpaluch.exchange.currency.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
