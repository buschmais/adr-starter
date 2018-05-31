package com.salesmanager.catalog.presentation.controller.category.facade;

import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.model.category.PersistableCategory;
import com.salesmanager.catalog.presentation.model.category.ReadableCategory;

import java.util.List;

public interface CategoryFacade {
	
	/**
	 * Returns a list of ReadableCategory ordered and built according to a given depth
	 * @param store
	 * @param depth
	 * @param language
	 * @return
	 * @throws Exception
	 */
	List<ReadableCategory> getCategoryHierarchy(MerchantStoreInfo store, int depth, LanguageInfo language, String filter) throws Exception;
	
	void saveCategory(MerchantStoreInfo store, PersistableCategory category) throws Exception;
	
	ReadableCategory getById(MerchantStoreInfo store, Long id, LanguageInfo language) throws Exception;
	
	ReadableCategory getByCode(MerchantStoreInfo store, String code, LanguageInfo language) throws Exception;
	
	void deleteCategory(Category category) throws Exception;

}
