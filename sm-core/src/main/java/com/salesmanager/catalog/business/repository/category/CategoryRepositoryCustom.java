package com.salesmanager.catalog.business.repository.category;

import java.util.List;

import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;

public interface CategoryRepositoryCustom {

	List<Object[]> countProductsByCategories(MerchantStoreInfo store,
			List<Long> categoryIds);

	List<Category> listByStoreAndParent(MerchantStoreInfo store, Category category);

}
