package com.yma.bank.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {
    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Column(unique = true)
    @Getter
    private Long accountId;
}

