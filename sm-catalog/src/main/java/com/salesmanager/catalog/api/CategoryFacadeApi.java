package com.salesmanager.catalog.api;

import com.salesmanager.catalog.presentation.model.category.ReadableCategory;
import com.salesmanager.core.integration.language.LanguageDTO;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;

import java.util.List;

public interface CategoryFacadeApi {

    List<ReadableCategory> getCategoryHierarchy(MerchantStoreDTO store,
                                                int depth, LanguageDTO language, String filter) throws Exception;

}
