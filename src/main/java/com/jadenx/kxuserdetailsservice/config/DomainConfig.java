package com.jadenx.kxuserdetailsservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.jadenx.kxuserdetailsservice.domain")
@EnableJpaRepositories("com.jadenx.kxuserdetailsservice.repos")
@EnableTransactionManagement
public class DomainConfig {
}
