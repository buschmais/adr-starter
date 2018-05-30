package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.common.model.integration.CreatedEvent;
import com.salesmanager.common.model.integration.DeletedEvent;
import com.salesmanager.common.model.integration.UpdatedEvent;
import com.salesmanager.core.integration.language.LanguageDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class LanguageCoreEventListener {

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreCreateEvent(CreatedEvent<LanguageDTO> event) {
        LanguageDTO languageDTO = event.getDto();
        if (languageDTO != null) {

        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreDeleteEvent(DeletedEvent<LanguageDTO> event) {
        LanguageDTO languageDTO = event.getDto();
        if (languageDTO != null) {

        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreUpdateEvent(UpdatedEvent<LanguageDTO> event) {
        LanguageDTO languageDTO = event.getDto();
        if (languageDTO != null) {

        }
    }

}
