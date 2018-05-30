package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.catalog.business.integration.core.repository.MerchantStoreInfoRepository;
import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.model.integration.CreatedEvent;
import com.salesmanager.common.model.integration.DeletedEvent;
import com.salesmanager.common.model.integration.UpdatedEvent;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
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

    private LanguageInfoService languageInfoService;

    @Autowired
    public MerchantStoreCoreEventListener(MerchantStoreInfoRepository merchantStoreInfoRepository, LanguageInfoService languageInfoService) {
        this.merchantStoreInfoRepository = merchantStoreInfoRepository;
        this.languageInfoService = languageInfoService;
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

    private List<LanguageInfo> getLanguages(MerchantStoreDTO storeDTO) {
        List<LanguageInfo> languages = storeDTO.getLanguages().stream()
                .map(code -> {
                    return languageInfoService.findbyCode(code);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return languages;
    }

}
