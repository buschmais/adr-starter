package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.catalog.business.integration.core.repository.MerchantStoreInfoRepository;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.model.integration.CreatedEvent;
import com.salesmanager.common.model.integration.DeletedEvent;
import com.salesmanager.common.model.integration.UpdatedEvent;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.core.model.reference.language.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MerchantStoreCoreEventListener {

    private MerchantStoreInfoRepository merchantStoreInfoRepository;

    private LanguageService languageService;

    @Autowired
    public MerchantStoreCoreEventListener(MerchantStoreInfoRepository merchantStoreInfoRepository, LanguageService languageService) {
        this.merchantStoreInfoRepository = merchantStoreInfoRepository;
        this.languageService = languageService;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreCreateEvent(CreatedEvent<MerchantStoreDTO> event) {
        MerchantStoreDTO storeDTO = event.getDto();
        if (storeDTO != null) {
            MerchantStoreInfo storeInfo = new MerchantStoreInfo(
                    storeDTO.getId(),
                    storeDTO.getCode(),
                    storeDTO.getCurrency(),
                    storeDTO.getDefaultLanguage(),
                    storeDTO.getCountryIsoCode(),
                    storeDTO.isCurrencyFormatNational(),
                    storeDTO.isUseCache(),
                    storeDTO.getStoreTemplate(),
                    storeDTO.getDomainName(),
                    getLanguages(storeDTO));
            merchantStoreInfoRepository.save(storeInfo);
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreDeleteEvent(DeletedEvent<MerchantStoreDTO> event) {
        MerchantStoreDTO storeDTO = event.getDto();
        if (storeDTO != null) {
            merchantStoreInfoRepository.delete(storeDTO.getId());
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMerchantStoreUpdateEvent(UpdatedEvent<MerchantStoreDTO> event) {
        MerchantStoreDTO storeDTO = event.getDto();
        if (storeDTO != null) {
            MerchantStoreInfo storeInfo = new MerchantStoreInfo(
                    storeDTO.getId(),
                    storeDTO.getCode(),
                    storeDTO.getCurrency(),
                    storeDTO.getDefaultLanguage(),
                    storeDTO.getCountryIsoCode(),
                    storeDTO.isCurrencyFormatNational(),
                    storeDTO.isUseCache(),
                    storeDTO.getStoreTemplate(),
                    storeDTO.getDomainName(),
                    getLanguages(storeDTO));
            merchantStoreInfoRepository.save(storeInfo);
        }
    }

    private List<Language> getLanguages(MerchantStoreDTO storeDTO) {
        List<Language> languages = storeDTO.getLanguages().stream()
                .map(code -> {
                    try {
                        return languageService.getByCode(code);
                    } catch (ServiceException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return languages;
    }

}
