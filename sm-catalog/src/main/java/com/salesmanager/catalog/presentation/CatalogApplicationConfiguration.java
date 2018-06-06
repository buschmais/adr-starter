package com.salesmanager.catalog.presentation;

import com.salesmanager.catalog.CatalogConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CatalogConfiguration.class)
@ComponentScan(basePackages = {"com.salesmanager.catalog.presentation"})
public class CatalogApplicationConfiguration {
}
