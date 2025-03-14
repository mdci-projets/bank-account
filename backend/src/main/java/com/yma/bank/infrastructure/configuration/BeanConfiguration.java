package com.yma.bank.infrastructure.configuration;

import com.yma.bank.BankApplication;
import com.yma.bank.domain.services.DomainOperationService;
import com.yma.bank.domain.services.OperationRepositoryExtended;
import com.yma.bank.domain.services.OperationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = BankApplication.class)
public class BeanConfiguration {

    @Bean
    OperationService operationService(final OperationRepositoryExtended operationRepositoryExtended) {
        return new DomainOperationService(operationRepositoryExtended);
    }
}
