package com.yma.bank.infrastructure.repository;
import com.yma.bank.domain.OperationTypeEnum;
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
public class OperationEntityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OperationEntityRepository repository;

    LocalDateTime baseLineDate = LocalDateTime.now().minusDays(10);

    @Test
    public void findByAccountIdSinceTest() {
        // Given
        createData();

        // When
        List<OperationEntity> operationEntityList = repository.findByAccountIdSince(123456L, baseLineDate);

        // Then
        Assertions.assertEquals(4, operationEntityList.size());

    }

    @Test
    public void getDepositBalanceUntil() {

        // Given
        createData();

        // When
        Long depositBalance = repository.getDepositBalanceUntil(123456L, baseLineDate);

        // Then
        Assertions.assertEquals(800L, depositBalance);

    }

    @Test
    public void getWithdrawalBalanceUntil() {
        // Given
        createData();

        // When
        Long withdrawalBalance = repository.getWithdrawalBalanceUntil(123456L, baseLineDate);

        // Then
        Assertions.assertEquals(100L, withdrawalBalance);

    }

    private void createData() {
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.minusDays(2), BigDecimal.valueOf(800L), OperationTypeEnum.DEPOSIT.name()));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.minusDays(1), BigDecimal.valueOf(100L), OperationTypeEnum.WITHDRAWAL.name()));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.plusDays(2), BigDecimal.valueOf(200L), OperationTypeEnum.WITHDRAWAL.name()));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.plusDays(3), BigDecimal.valueOf(400L), OperationTypeEnum.WITHDRAWAL.name()));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.plusDays(4), BigDecimal.valueOf(300L), OperationTypeEnum.DEPOSIT.name()));
        entityManager.persistAndFlush(new OperationEntity(null, 123456L, baseLineDate.plusDays(5), BigDecimal.valueOf(100L), OperationTypeEnum.WITHDRAWAL.name()));
    }
}
