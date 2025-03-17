package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.OperationHistory;
import com.yma.bank.domain.OperationTypeEnum;
import com.yma.bank.domain.services.OperationHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OperationHistoryRepositoryImplTest {

    @Autowired
    private OperationHistoryRepository repository;

    @Test
    void shouldSaveOperationHistorySuccessfully() {
        OperationHistory operation = new OperationHistory(
                null, 1001L, new BigDecimal("200.00"), OperationTypeEnum.DEPOSIT, LocalDateTime.now()
        );

        repository.save(operation);
        List<OperationHistory> history = repository.findByAccountId(1001L);

        assertEquals(1, history.size());
        assertEquals(new BigDecimal("200.00"), history.get(0).getAmount());
        assertEquals(OperationTypeEnum.DEPOSIT, history.get(0).getOperationType());
    }

    @Test
    void shouldRetrieveMultipleOperationsForAccount() {
        repository.save(new OperationHistory(null, 1001L, new BigDecimal("150.00"), OperationTypeEnum.WITHDRAWAL, LocalDateTime.now().minusDays(2)));
        repository.save(new OperationHistory(null, 1001L, new BigDecimal("250.00"), OperationTypeEnum.DEPOSIT, LocalDateTime.now().minusDays(1)));

        List<OperationHistory> history = repository.findByAccountId(1001L);

        assertEquals(2, history.size());
        assertEquals(new BigDecimal("150.00"), history.get(0).getAmount());
        assertEquals(new BigDecimal("250.00"), history.get(1).getAmount());
    }
}
