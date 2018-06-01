package com.salesmanager.core.integration;

import com.salesmanager.core.integration.customer.CustomerCreatedEvent;
import com.salesmanager.core.integration.language.LanguageCreatedEvent;
import com.salesmanager.core.integration.merchant.MerchantStoreCreatedEvent;
import com.salesmanager.core.integration.tax.TaxClassCreatedEvent;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.tax.taxclass.TaxClass;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;


@Component
public class CreatedEventListener implements PostInsertEventListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onPostInsert(PostInsertEvent event) {
        if (event.getEntity() instanceof MerchantStore) {
            MerchantStore store = ((MerchantStore) event.getEntity());
            applicationEventPublisher.publishEvent(new MerchantStoreCreatedEvent(store.toDTO()));
        } else if (event.getEntity() instanceof Language) {
            Language language = ((Language) event.getEntity());
            applicationEventPublisher.publishEvent(new LanguageCreatedEvent(language.toDTO()));
        } else if (event.getEntity() instanceof Customer) {
            Customer customer = ((Customer) event.getEntity());
            applicationEventPublisher.publishEvent(new CustomerCreatedEvent(customer.toDTO()));
        } else if (event.getEntity() instanceof TaxClass) {
            TaxClass taxClass = ((TaxClass) event.getEntity());
            applicationEventPublisher.publishEvent(new TaxClassCreatedEvent(taxClass.toDTO()));
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
