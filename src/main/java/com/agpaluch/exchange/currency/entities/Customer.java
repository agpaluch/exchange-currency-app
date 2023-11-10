package com.agpaluch.exchange.currency.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String firstName;
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    //@JoinColumn needed so that Hibernate does not create third table with pairs of keys
    private List<Account> accounts = new ArrayList<>();

}
