package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.Account;
import com.yma.bank.domain.Operation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccountMapper {

    Account mapToDomainEntity(
            AccountEntity account,
            List<OperationEntity> operationEntityList,
            Long withdrawalBalance,
            Long depositBalance) {

        BigDecimal baselineBalance = BigDecimal.valueOf(depositBalance).subtract(BigDecimal.valueOf(withdrawalBalance));
        return new Account(account.getAccountId(),
                baselineBalance,
                mapToOperation(operationEntityList));

    }

    List<Operation> mapToOperation(List<OperationEntity> operationEntityList) {
        List<Operation> mappedOperationList = new ArrayList<>();

        for (OperationEntity operationEntity : operationEntityList) {
            mappedOperationList.add(new Operation(
                    operationEntity.getId(),
                    operationEntity.getAccountId(),
                    operationEntity.getTimestamp(),
                    operationEntity.getAmount()));
        }

        return mappedOperationList;
    }

    OperationEntity mapToJpaEntity(Operation operation) {
        return new OperationEntity(
                operation.getId(),
                operation.getAccountId(),
                operation.getTimestamp(),
                operation.getAmount());
    }
}
