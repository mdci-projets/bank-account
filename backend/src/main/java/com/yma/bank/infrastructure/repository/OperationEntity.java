package com.yma.bank.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "operation")
@Getter
public class OperationEntity {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The account that owns this activity.
     */
    private Long accountId;

    /**
     * The timestamp of the activity.
     */
    private LocalDateTime timestamp;

    /**
     * The money that was transferred between the accounts.
     */
    private BigDecimal amount;

    private String operationType;
}