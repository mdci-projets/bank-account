package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.Account;
import com.yma.bank.domain.Operation;
import com.yma.bank.domain.services.OperationRepositoryExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OperationRepositoryExtendedImpl implements OperationRepositoryExtended {

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
        AccountEntity account =
                accountEntityRepository.findByAccountId(accountId)
                        .orElseThrow(EntityNotFoundException::new);

        List<OperationEntity> operationEntityList =
                operationEntityRepository.findByAccountIdSince(
                        accountId,
                        baselineDate);

        Long withdrawalBaseLineBalance = orZero(operationEntityRepository
                .getWithdrawalBalanceUntil(
                        accountId,
                        baselineDate));

        Long depositBaseLineBalance = orZero(operationEntityRepository
                .getDepositBalanceUntil(
                        accountId,
                        baselineDate));

        return accountMapper.mapToDomainEntity(
                account,
                operationEntityList,
                withdrawalBaseLineBalance,
                depositBaseLineBalance);
    }

    private Long orZero(Long value) {
        return value == null ? 0L : value;
    }

    @Override
    public void saveOperation(Operation operation) {
        operationEntityRepository.save(accountMapper.mapToJpaEntity(operation));
    }
}
