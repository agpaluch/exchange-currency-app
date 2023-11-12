package com.agpaluch.exchange.currency.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.List;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account extends AbstractEntity {

    private String accountNumber;

    @CreatedDate //TODO????
    private Instant createdDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<AccountBalance> accountBalance;
}
