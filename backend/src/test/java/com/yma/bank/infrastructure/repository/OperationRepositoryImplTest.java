package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.Account;
import com.yma.bank.domain.Operation;
import com.yma.bank.domain.services.OperationRepository;
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
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RepositoryTestConfiguration.class})
@DataJpaTest
@ActiveProfiles("test")
public class OperationRepositoryImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OperationRepository repositoryExtended;

    @Autowired
    private OperationEntityRepository operationRepository;

    LocalDateTime baseLineDate = LocalDateTime.now().minusDays(10);

    @Test
    public void saveOperationTest() {
        // When
        repositoryExtended.saveOperation(new Operation(200L, 123456L, baseLineDate, BigDecimal.valueOf(500L)));

        //Then
        List<OperationEntity> actual = operationRepository.findAll();
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BigDecimal.valueOf(500L), actual.get(0).getAmount());
        Assertions.assertEquals(baseLineDate, actual.get(0).getTimestamp());
        Assertions.assertEquals(123456L, actual.get(0).getAccountId());
    }

    @Test
    public void getAccountTest() {
        // Given
        createData();

        // When
        Account actual = repositoryExtended.getAccount(123456L, baseLineDate);

        // Then
        Assertions.assertEquals(BigDecimal.valueOf(200L), actual.calculateBalanceOperationsToDisplay());
        Assertions.assertEquals(BigDecimal.valueOf(900L), actual.getBaseLineBalance());
    }

    private void createData() {
        entityManager.persistAndFlush(new AccountEntity(null, 123456L));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.minusDays(2), BigDecimal.valueOf(800L)));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.minusDays(1), BigDecimal.valueOf(-100L)));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.plusDays(2), BigDecimal.valueOf(-200L)));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.plusDays(3), BigDecimal.valueOf(400L)));
    }
}
