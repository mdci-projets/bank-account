package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.Account;
import com.yma.bank.domain.DomainException;
import com.yma.bank.domain.Operation;
import com.yma.bank.domain.services.OperationRepositoryExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OperationRepositoryExtendedImpl implements OperationRepositoryExtended {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationRepositoryExtendedImpl.class);

    private final OperationEntityRepository operationEntityRepository;

    private final AccountEntityRepository accountEntityRepository;

    private final AccountMapper accountMapper;

    @Autowired
    public OperationRepositoryExtendedImpl(final OperationEntityRepository operationEntityRepository,
                                           final AccountEntityRepository accountEntityRepository,
                                           AccountMapper accountMapper) {
        this.operationEntityRepository = operationEntityRepository;
        this.accountEntityRepository = accountEntityRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Account getAccount(Long accountId, LocalDateTime baselineDate) {
        LOGGER.info("Searching for account ID {} in the database", accountId);

        AccountEntity account = accountEntityRepository.findByAccountId(accountId)
                .orElseThrow(() -> {
                    LOGGER.error("Account ID {} not found", accountId);
                    return new DomainException(String.format("Account with ID %s not found", accountId));
                });

        LOGGER.info("Account ID {} found, retrieving operations", accountId);
        List<OperationEntity> operationEntityList = operationEntityRepository.findByAccountIdSince(accountId, baselineDate);

        Long withdrawalBalance = getSafeBalance(operationEntityRepository.getWithdrawalBalanceUntil(accountId, baselineDate));
        Long depositBalance = getSafeBalance(operationEntityRepository.getDepositBalanceUntil(accountId, baselineDate));

        LOGGER.info("Balances retrieved for account ID {}: Withdrawals={}, Deposits={}", accountId, withdrawalBalance, depositBalance);
        return accountMapper.mapToDomainEntity(account, operationEntityList, withdrawalBalance, depositBalance);
    }

    private static Long getSafeBalance(Long value) {
        return (value != null) ? value : 0L;
    }


    @Override
    public void saveOperation(Operation operation) {
        LOGGER.info("Recording operation for account ID {}", operation.getAccountId());
        operationEntityRepository.save(accountMapper.mapToJpaEntity(operation));
        LOGGER.info("Operation successfully recorded for account ID {}", operation.getAccountId());
    }
}
