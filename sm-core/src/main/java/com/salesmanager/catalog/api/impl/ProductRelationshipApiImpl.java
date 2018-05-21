package com.salesmanager.catalog.api.impl;

import com.salesmanager.catalog.api.ProductRelationshipApi;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.service.product.relationship.ProductRelationshipService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.product.relationship.ProductRelationship;
import com.salesmanager.catalog.model.product.relationship.ProductRelationshipType;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.core.model.reference.language.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductRelationshipApiImpl implements ProductRelationshipApi {

    private MerchantStoreInfoService merchantStoreInfoService;

    private ProductRelationshipService productRelationshipService;

    @Autowired
    public ProductRelationshipApiImpl(MerchantStoreInfoService merchantStoreInfoService, ProductRelationshipService productRelationshipService) {
        this.merchantStoreInfoService = merchantStoreInfoService;
        this.productRelationshipService = productRelationshipService;
    }


    @Override
    public List<ProductRelationship> getGroups(MerchantStoreDTO store) {
        MerchantStoreInfo storeInfo = this.merchantStoreInfoService.findbyCode(store.getCode());
        return productRelationshipService.getGroups(storeInfo);
    }

    @Override
    public List<ProductRelationship> getByType(MerchantStoreDTO store, ProductRelationshipType type, Language language) throws ServiceException {
        MerchantStoreInfo storeInfo = this.merchantStoreInfoService.findbyCode(store.getCode());
        return this.productRelationshipService.getByType(storeInfo,type, language);
    }
}
