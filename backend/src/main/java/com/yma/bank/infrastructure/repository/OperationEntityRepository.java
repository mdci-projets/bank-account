package com.yma.bank.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationEntityRepository extends JpaRepository<OperationEntity, Long> {
    @Query("select oe from OperationEntity oe " +
            "where oe.accountId = :accountId " +
            "and oe.timestamp >= :since")
    List<OperationEntity> findByAccountIdSince(
            @Param("accountId") Long accountId,
            @Param("since") LocalDateTime since);

    @Query("select sum(oe.amount) from OperationEntity oe " +
            "where oe.accountId = :accountId " +
            "and oe.amount >=0 " +
            "and oe.timestamp < :until")
    Long getDepositBalanceUntil(
            @Param("accountId") Long accountId,
            @Param("until") LocalDateTime until);

    @Query("select sum(oe.amount) from OperationEntity oe " +
            "where oe.accountId = :accountId " +
            "and oe.amount <0 " +
            "and oe.timestamp < :until")
    Long getWithdrawalBalanceUntil(
            @Param("accountId") Long accountId,
            @Param("until") LocalDateTime until);
}