package com.agpaluch.exchange.currency.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account extends AbstractEntity {

    private String accountNumber;

    @CreatedDate //TODO????
    private LocalDateTime createdOn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_balance_id", referencedColumnName = "id")
    private AccountBalance accountBalance;
}
