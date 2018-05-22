package com.salesmanager.catalog.presentation.api.impl;

import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.api.CategoryFacadeApi;
import com.salesmanager.catalog.presentation.controller.category.facade.CategoryFacade;
import com.salesmanager.catalog.presentation.model.category.ReadableCategory;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.core.model.reference.language.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryFacadeApiImpl implements CategoryFacadeApi {

    private CategoryFacade categoryFacade;

    private MerchantStoreInfoService merchantStoreInfoService;

    @Autowired
    public CategoryFacadeApiImpl(CategoryFacade categoryFacade, MerchantStoreInfoService merchantStoreInfoService) {
        this.categoryFacade = categoryFacade;
        this.merchantStoreInfoService = merchantStoreInfoService;
    }

    @Override
    public List<ReadableCategory> getCategoryHierarchy(MerchantStoreDTO store, int depth, Language language, String filter) throws Exception {
        MerchantStoreInfo storeInfo = this.merchantStoreInfoService.findbyCode(store.getCode());
        return this.categoryFacade.getCategoryHierarchy(storeInfo, depth, language, filter);
    }

}
