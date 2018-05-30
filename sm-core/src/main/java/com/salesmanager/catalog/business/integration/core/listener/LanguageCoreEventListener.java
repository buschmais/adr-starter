package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.catalog.business.integration.core.repository.LanguageInfoRepository;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.common.model.integration.CreatedEvent;
import com.salesmanager.common.model.integration.DeletedEvent;
import com.salesmanager.common.model.integration.UpdatedEvent;
import com.salesmanager.core.integration.language.LanguageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class LanguageCoreEventListener {

    private LanguageInfoRepository languageInfoRepository;

    @Autowired
    public LanguageCoreEventListener(LanguageInfoRepository languageInfoRepository) {
        this.languageInfoRepository = languageInfoRepository;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreCreateEvent(CreatedEvent<LanguageDTO> event) {
        LanguageDTO languageDTO = event.getDto();
        if (languageDTO != null) {
            LanguageInfo language = new LanguageInfo(
                    languageDTO.getId(),
                    languageDTO.getCode()
            );
            this.languageInfoRepository.save(language);
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreDeleteEvent(DeletedEvent<LanguageDTO> event) {
        LanguageDTO languageDTO = event.getDto();
        if (languageDTO != null) {
            this.languageInfoRepository.delete(languageDTO.getId());
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreUpdateEvent(UpdatedEvent<LanguageDTO> event) {
        LanguageDTO languageDTO = event.getDto();
        if (languageDTO != null) {
            LanguageInfo language = new LanguageInfo(
                    languageDTO.getId(),
                    languageDTO.getCode()
            );
            this.languageInfoRepository.save(language);
        }
    }

}
