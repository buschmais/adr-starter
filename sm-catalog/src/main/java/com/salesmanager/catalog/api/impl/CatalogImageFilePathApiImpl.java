package com.salesmanager.catalog.api.impl;

import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.api.CatalogImageFilePathApi;
import com.salesmanager.catalog.presentation.util.CatalogImageFilePathUtils;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CatalogImageFilePathApiImpl implements CatalogImageFilePathApi {

    private CatalogImageFilePathUtils catalogImageFilePathUtils;

    private MerchantStoreInfoService merchantStoreInfoService;

    @Autowired
    public CatalogImageFilePathApiImpl(CatalogImageFilePathUtils catalogImageFilePathUtils, MerchantStoreInfoService merchantStoreInfoService) {
        this.catalogImageFilePathUtils = catalogImageFilePathUtils;
        this.merchantStoreInfoService = merchantStoreInfoService;
    }

    @Override
    public String buildProductImageUtils(MerchantStoreDTO store, String sku, String imageName) {
        MerchantStoreInfo storeInfo = this.merchantStoreInfoService.findbyCode(store.getCode());
        return this.catalogImageFilePathUtils.buildProductImageUtils(storeInfo, sku, imageName);
    }
}
