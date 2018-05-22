package com.salesmanager.catalog.business.service.search;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.search.SearchKeywords;
import com.salesmanager.catalog.model.search.SearchResponse;

public interface SearchService {
	
	/**
	 * The indexing service for products. The index service must be invoked when a product is
	 * created or updated
	 * @param store
	 * @param product
	 * @throws ServiceException
	 */
	void index(MerchantStoreInfo store, Product product) throws ServiceException;

	/**
	 * Deletes an index in the appropriate language. Must be invoked when a product is deleted
	 * @param store
	 * @param product
	 * @throws ServiceException
	 */
	void deleteIndex(MerchantStoreInfo store, Product product)
			throws ServiceException;

	/**
	 * Similar keywords based on a a series of characters. Used in the auto-complete
	 * functionality
	 * @param collectionName
	 * @param jsonString
	 * @param entriesCount
	 * @return
	 * @throws ServiceException
	 */
	SearchKeywords searchForKeywords(String collectionName,
			String jsonString, int entriesCount) throws ServiceException;

	/**
	 * Search products based on user entry
	 * @param store
	 * @param languageCode
	 * @param jsonString
	 * @param entriesCount
	 * @param startIndex
	 * @throws ServiceException
	 */
	SearchResponse search(MerchantStoreInfo store, String languageCode, String jsonString,
						  int entriesCount, int startIndex) throws ServiceException;

	/**
	 * Initializes search service in order to avoid lazy initialization
	 */
	void initService();

}
