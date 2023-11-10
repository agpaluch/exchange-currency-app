package com.agpaluch.exchange.currency.persistence;

import com.agpaluch.exchange.currency.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
