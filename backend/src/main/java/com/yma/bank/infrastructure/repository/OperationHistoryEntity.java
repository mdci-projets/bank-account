package com.yma.bank.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "operation_history")
public class OperationHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private Long accountId;

    @Getter
    private BigDecimal amount;

    @Getter
    private String operationType;

    @Getter
    private LocalDateTime timestamp;
}