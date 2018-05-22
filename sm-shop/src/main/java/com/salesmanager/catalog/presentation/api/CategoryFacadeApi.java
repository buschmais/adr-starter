package com.salesmanager.catalog.presentation.api;

import com.salesmanager.catalog.presentation.model.category.ReadableCategory;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.core.model.reference.language.Language;

import java.util.List;

public interface CategoryFacadeApi {

    List<ReadableCategory> getCategoryHierarchy(MerchantStoreDTO store,
                                                int depth, Language language, String filter) throws Exception;

}
