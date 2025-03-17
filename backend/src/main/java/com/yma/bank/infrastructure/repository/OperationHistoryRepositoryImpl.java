package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.OperationTypeEnum;
import com.yma.bank.domain.OperationHistory;
import com.yma.bank.domain.services.OperationHistoryRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OperationHistoryRepositoryImpl implements OperationHistoryRepository {

    private final OperationHistoryEntityRepository repository;

    public OperationHistoryRepositoryImpl(OperationHistoryEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(OperationHistory operationHistory) {
        OperationHistoryEntity entity = toEntity(operationHistory);
        repository.save(entity);
    }

    @Override
    public List<OperationHistory> findByAccountId(Long accountId) {
        return repository.findByAccountId(accountId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public OperationHistoryEntity toEntity(OperationHistory operation) {
        return new OperationHistoryEntity(
                operation.getId(),
                operation.getAccountId(),
                operation.getAmount(),
                operation.getOperationType().name(),
                operation.getTimestamp()
        );
    }

    public OperationHistory toDomain(OperationHistoryEntity entity) {
        return new OperationHistory(
                entity.getId(),
                entity.getAccountId(),
                entity.getAmount(),
                OperationTypeEnum.valueOf(entity.getOperationType()),
                entity.getTimestamp()
        );
    }
}
