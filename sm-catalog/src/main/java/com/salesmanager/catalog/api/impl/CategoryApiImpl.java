package com.salesmanager.catalog.api.impl;

import com.salesmanager.catalog.api.CategoryApi;
import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.service.category.CategoryService;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.core.integration.language.LanguageDTO;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryApiImpl implements CategoryApi {

    private CategoryService categoryService;

    private MerchantStoreInfoService merchantStoreInfoService;

    private LanguageInfoService languageInfoService;

    @Autowired
    public CategoryApiImpl(CategoryService categoryService, MerchantStoreInfoService merchantStoreInfoService, LanguageInfoService languageInfoService) {
        this.categoryService = categoryService;
        this.merchantStoreInfoService = merchantStoreInfoService;
        this.languageInfoService = languageInfoService;
    }

    @Override
    public List<Category> listByDepth(MerchantStoreDTO store, int depth, LanguageDTO language) {
        MerchantStoreInfo storeInfo = this.merchantStoreInfoService.findbyCode(store.getCode());
        LanguageInfo languageInfo = this.languageInfoService.findbyCode(language.getCode());
        return this.categoryService.listByDepth(storeInfo, depth, languageInfo);
    }

    @Override
    public Category getByLanguage(long categoryId, LanguageDTO language) {
        LanguageInfo languageInfo = this.languageInfoService.findbyCode(language.getCode());
        return this.categoryService.getByLanguage(categoryId, languageInfo);
    }
}
