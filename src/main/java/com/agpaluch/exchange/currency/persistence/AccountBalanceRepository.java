package com.agpaluch.exchange.currency.persistence;

import com.agpaluch.exchange.currency.entity.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {
}
