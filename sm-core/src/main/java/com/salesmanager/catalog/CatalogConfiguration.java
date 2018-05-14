package com.salesmanager.catalog;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityListeners;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.salesmanager.catalog.business.service", "com.salesmanager.catalog.business.util"})
@EnableJpaRepositories(basePackages = {"com.salesmanager.catalog.business.repository"})
@PropertySource("classpath:catalog.properties")
public class CatalogConfiguration {
}
