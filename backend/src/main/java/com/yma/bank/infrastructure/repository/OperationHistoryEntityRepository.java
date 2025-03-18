package com.yma.bank.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationHistoryEntityRepository extends JpaRepository<OperationHistoryEntity, Long> {
    List<OperationHistoryEntity> findByAccountId(Long accountId);
}
