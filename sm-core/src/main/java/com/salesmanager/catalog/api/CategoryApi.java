package com.salesmanager.catalog.api;

import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.core.model.reference.language.Language;

import java.util.List;

public interface CategoryApi {

    List<Category> listByDepth(MerchantStoreDTO store, int depth, Language language);

}
