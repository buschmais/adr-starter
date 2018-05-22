package com.salesmanager.catalog.api.impl;

import com.salesmanager.catalog.api.CategoryApi;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.service.category.CategoryService;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.core.model.reference.language.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryApiImpl implements CategoryApi {

    private CategoryService categoryService;

    private MerchantStoreInfoService merchantStoreInfoService;

    @Autowired
    public CategoryApiImpl(CategoryService categoryService, MerchantStoreInfoService merchantStoreInfoService) {
        this.categoryService = categoryService;
        this.merchantStoreInfoService = merchantStoreInfoService;
    }

    @Override
    public List<Category> listByDepth(MerchantStoreDTO store, int depth, Language language) {
        MerchantStoreInfo storeInfo = this.merchantStoreInfoService.findbyCode(store.getCode());
        return this.categoryService.listByDepth(storeInfo, depth, language);
    }
}
