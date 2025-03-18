package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.OperationHistory;
import com.yma.bank.domain.services.OperationHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OperationHistoryRepositoryImpl implements OperationHistoryRepository {

    private final OperationHistoryEntityRepository repository;

    private final OperationHistoryMapper mapper;

    public OperationHistoryRepositoryImpl(OperationHistoryEntityRepository repository, OperationHistoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(OperationHistory operationHistory) {
        OperationHistoryEntity entity = mapper.toEntity(operationHistory);
        repository.save(entity);
    }

    @Override
    public List<OperationHistory> findByAccountId(Long accountId) {
        return repository.findByAccountId(accountId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
