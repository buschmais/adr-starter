package com.salesmanager.test.configuration;

import com.salesmanager.catalog.CatalogConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
@Import(CatalogConfiguration.class)
@EntityScan(basePackages = {"com.salesmanager.core.model"})
@ComponentScan({"com.salesmanager.core.business.services","com.salesmanager.core.business.utils"})
@ImportResource("spring/test-shopizer-context.xml")
public class ConfigurationTest {

}
