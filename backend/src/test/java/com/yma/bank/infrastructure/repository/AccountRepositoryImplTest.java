package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.Account;
import com.yma.bank.domain.OperationTypeEnum;
import com.yma.bank.domain.services.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RepositoryTestConfiguration.class})
@DataJpaTest
@ActiveProfiles("test")
public class AccountRepositoryImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    LocalDateTime baseLineDate = LocalDateTime.now().minusDays(10);

    @Test
    public void getAccountTest() {
        // Given
        createData();

        // When
        Optional<Account> actual = accountRepository.getAccount(123456L, baseLineDate);

        // Then
        Assertions.assertEquals(BigDecimal.valueOf(200L), actual.get().calculateBalanceOperationsToDisplay());
        Assertions.assertEquals(BigDecimal.valueOf(700L), actual.get().getBaseLineBalance());
    }

    private void createData() {
        entityManager.persistAndFlush(new AccountEntity(null, 123456L));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.minusDays(2), BigDecimal.valueOf(800L), OperationTypeEnum.DEPOSIT.name()));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.minusDays(1), BigDecimal.valueOf(100L), OperationTypeEnum.WITHDRAWAL.name()));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.plusDays(2), BigDecimal.valueOf(200L), OperationTypeEnum.WITHDRAWAL.name()));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.plusDays(3), BigDecimal.valueOf(400L), OperationTypeEnum.DEPOSIT.name()));
    }
}
