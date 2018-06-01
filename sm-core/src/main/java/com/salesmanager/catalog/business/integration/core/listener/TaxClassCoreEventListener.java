package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.common.model.integration.CreatedEvent;
import com.salesmanager.common.model.integration.DeletedEvent;
import com.salesmanager.common.model.integration.UpdatedEvent;
import com.salesmanager.core.integration.tax.TaxClassDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TaxClassCoreEventListener {

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleTaxClassCreateEvent(CreatedEvent<TaxClassDTO> event) {

    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleTaxClassDeleteEvent(DeletedEvent<TaxClassDTO> event) {

    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handletaxClassUpdateEvent(UpdatedEvent<TaxClassDTO> event) {

    }

}
