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
public class OperationEntity {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    /**
     * The account that owns this activity.
     */
    @Getter
    private Long accountId;

    /**
     * The timestamp of the activity.
     */
    @Getter
    private LocalDateTime timestamp;

    /**
     * The money that was transferred between the accounts.
     */
    @Getter
    private BigDecimal amount;
}