package com.salesmanager.core.integration;

import com.salesmanager.core.integration.customer.CustomerUpdatedEvent;
import com.salesmanager.core.integration.language.LanguageUpdatedEvent;
import com.salesmanager.core.integration.merchant.MerchantStoreUpdatedEvent;
import com.salesmanager.core.integration.tax.TaxClassUpdatedEvent;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.tax.taxclass.TaxClass;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;


@Component
public class UpdatedEventListener implements PostUpdateEventListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (event.getEntity() instanceof MerchantStore) {
            MerchantStore store = ((MerchantStore) event.getEntity());
            applicationEventPublisher.publishEvent(new MerchantStoreUpdatedEvent(store.toDTO()));
        } else if (event.getEntity() instanceof Language) {
            Language language = ((Language) event.getEntity());
            applicationEventPublisher.publishEvent(new LanguageUpdatedEvent(language.toDTO()));
        } else if (event.getEntity() instanceof Customer) {
            Customer customer = ((Customer) event.getEntity());
            applicationEventPublisher.publishEvent(new CustomerUpdatedEvent(customer.toDTO()));
        } else if (event.getEntity() instanceof TaxClass) {
            TaxClass taxClass = ((TaxClass) event.getEntity());
            applicationEventPublisher.publishEvent(new TaxClassUpdatedEvent(taxClass.toDTO()));
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
