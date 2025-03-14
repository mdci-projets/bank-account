package com.yma.bank.infrastructure.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@AutoConfigureDataJpa
@EntityScan("com.yma.bank")
@ComponentScan("com.yma.bank")
@EnableJpaRepositories("com.yma.bank")
public class RepositoryTestConfiguration {
}
