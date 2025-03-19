package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.Operation;
import com.yma.bank.domain.services.OperationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OperationRepositoryImpl implements OperationRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationRepositoryImpl.class);

    private final OperationEntityRepository operationEntityRepository;

    private final AccountMapper accountMapper;

    @Autowired
    public OperationRepositoryImpl(final OperationEntityRepository operationEntityRepository,
                                   AccountMapper accountMapper) {
        this.operationEntityRepository = operationEntityRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public void saveOperation(Operation operation) {
        LOGGER.info("Recording operation for account ID {}", operation.getAccountId());
        operationEntityRepository.save(accountMapper.mapToJpaEntity(operation));
        LOGGER.info("Operation successfully recorded for account ID {}", operation.getAccountId());
    }
}
