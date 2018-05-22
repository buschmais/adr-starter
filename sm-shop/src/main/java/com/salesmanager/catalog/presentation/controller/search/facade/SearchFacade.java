package com.salesmanager.catalog.presentation.controller.search.facade;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.catalog.model.search.SearchResponse;
import com.salesmanager.catalog.presentation.model.SearchProductList;
import com.salesmanager.catalog.presentation.model.SearchProductRequest;

/**
 * Different services for searching and indexing data
 * @author c.samson
 *
 */
public interface SearchFacade {
	

	/**
	 * This utility method will re-index all products in the catalogue
	 * @param store
	 * @throws Exception
	 */
	public void indexAllData(MerchantStoreInfo store) throws Exception;
	
	/**
	 * Produces a search request against elastic search
	 * @param searchRequest
	 * @return
	 * @throws Exception
	 */
	public SearchProductList search(MerchantStoreInfo store, Language language, SearchProductRequest searchRequest) throws Exception;

	/**
	 * Copy sm-core search response to a simple readable format populated with corresponding products
	 * @param searchResponse
	 * @return
	 */
	public SearchProductList copySearchResponse(SearchResponse searchResponse, MerchantStoreInfo store, int start, int count, Language language) throws Exception;

}
