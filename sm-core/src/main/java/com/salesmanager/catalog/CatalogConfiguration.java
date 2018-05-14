package com.salesmanager.catalog;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.infinispan.configuration.cache.SingleFileStoreConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.tree.TreeCache;
import org.infinispan.tree.TreeCacheFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityListeners;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.salesmanager.catalog.business.service", "com.salesmanager.catalog.business.util"})
@EnableJpaRepositories(basePackages = {"com.salesmanager.catalog.business.repository"})
@PropertySource("classpath:catalog.properties")
public class CatalogConfiguration {

    @Autowired
    private Environment environment;

    @Autowired
    private DefaultCacheManager defaultCacheManager;

    @Bean
    TreeCache catalogTreeCache() {
        final PersistenceConfigurationBuilder persistConfig = new ConfigurationBuilder().persistence();
        persistConfig.passivation(false);
        final SingleFileStoreConfigurationBuilder fileStore = new SingleFileStoreConfigurationBuilder(persistConfig).location(environment.getProperty("catalog.cms.location"));
        fileStore.invocationBatching().enable();
        fileStore.eviction().maxEntries(15);
        fileStore.eviction().strategy(EvictionStrategy.LRU);
        fileStore.jmxStatistics().disable();
        final org.infinispan.configuration.cache.Configuration config = persistConfig.addStore(fileStore).build();
        config.compatibility().enabled();
        defaultCacheManager.defineConfiguration("CatalogRepository", config);
        final Cache<String, String> cache = defaultCacheManager.getCache("CatalogRepository");
        TreeCacheFactory f = new TreeCacheFactory();
        TreeCache treeCache = f.createTreeCache(cache);
        cache.start();
        return treeCache;
    }
}
