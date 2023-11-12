package com.agpaluch.exchange.currency.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Account extends AbstractEntity {

    private String accountNumber;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    @Singular("accountBalance")
    private List<AccountBalance> accountBalances = new ArrayList<>();

    public void addAccountBalance(AccountBalance accountBalance) {
        accountBalances.add(accountBalance);
    }

    public void removeBalance(AccountBalance accountBalance) {
        accountBalances.remove(accountBalance);
    }
}
