package com.yma.bank.infrastructure.configuration;

import com.yma.bank.BankApplication;
import com.yma.bank.domain.services.*;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = BankApplication.class)
public class BeanConfiguration {

    @Bean
    OperationService operationService(final OperationRepository operationRepository) {
        return new OperationServiceImpl(operationRepository);
    }

    @Bean
    public StatementDomainService statementDomainService() {
        return new StatementDomainService();
    }

    @Bean
    StatementService statementService(final OperationHistoryRepository operationHistoryRepository, final StatementDomainService statementDomainService) {
        return new StatementServiceImpl(operationHistoryRepository, statementDomainService);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking Management API")
                        .version("1.0")
                        .description("API Documentation for Banking Operations Management")
                        .contact(new Contact()
                                .name("Support Dev")
                                .email("support@bankapi.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentation Complète")
                        .url("https://bankapi.com/docs"));
    }
}
