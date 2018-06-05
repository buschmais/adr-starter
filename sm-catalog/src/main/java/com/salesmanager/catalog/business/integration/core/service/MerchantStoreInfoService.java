package com.salesmanager.catalog.business.integration.core.service;

import com.salesmanager.catalog.business.integration.core.repository.MerchantStoreInfoRepository;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantStoreInfoService {

    private MerchantStoreInfoRepository merchantStoreInfoRepository;

    @Autowired
    public MerchantStoreInfoService(MerchantStoreInfoRepository merchantStoreInfoRepository) {
        this.merchantStoreInfoRepository = merchantStoreInfoRepository;
    }

    public MerchantStoreInfo findbyCode(String code) {
        return this.merchantStoreInfoRepository.findByCode(code);
    }
}
