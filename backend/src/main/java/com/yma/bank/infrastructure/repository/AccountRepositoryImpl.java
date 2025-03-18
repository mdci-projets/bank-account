package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.Account;
import com.yma.bank.domain.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRepositoryImpl.class);

    private final AccountEntityRepository accountEntityRepository;

    private final OperationEntityRepository operationEntityRepository;

    private final AccountMapper accountMapper;

    public AccountRepositoryImpl(AccountEntityRepository accountEntityRepository,
                                 OperationEntityRepository operationEntityRepository,
                                 AccountMapper accountMapper) {
        this.accountEntityRepository = accountEntityRepository;
        this.operationEntityRepository = operationEntityRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Optional<Account> getAccount(Long accountId, LocalDateTime baselineDate) {
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
        return Optional.of(accountMapper.mapToDomainEntity(account, operationEntityList, withdrawalBalance, depositBalance));
    }

    private static Long getSafeBalance(Long value) {
        return (value != null) ? value : 0L;
    }
}