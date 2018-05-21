package com.salesmanager.catalog.api.impl;

import com.salesmanager.catalog.api.DigitalProductApi;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.service.product.file.DigitalProductService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.file.DigitalProduct;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DigitalProductApiImpl implements DigitalProductApi {

    private DigitalProductService digitalProductService;

    private MerchantStoreInfoService merchantStoreInfoService;

    @Autowired
    public DigitalProductApiImpl(DigitalProductService digitalProductService, MerchantStoreInfoService merchantStoreInfoService) {
        this.digitalProductService = digitalProductService;
        this.merchantStoreInfoService = merchantStoreInfoService;
    }

    @Override
    public DigitalProduct getByProduct(MerchantStoreDTO store, Product product) {
        MerchantStoreInfo storeInfo = this.merchantStoreInfoService.findbyCode(store.getCode());
        return digitalProductService.getByProduct(storeInfo, product);
    }

}
