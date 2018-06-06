package com.salesmanager.catalog.presentation.controller.items.facade;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.presentation.model.product.ReadableProductList;

public interface ProductItemsFacade {
	
	/**
	 * List items attached to a Manufacturer
	 * @param store
	 * @param language
	 * @return
	 */
	ReadableProductList listItemsByManufacturer(MerchantStoreInfo store, LanguageInfo language, Long manufacturerId, int startCount, int maxCount) throws Exception;

	/**
	 * List product items by id
	 * @param store
	 * @param language
	 * @param ids
	 * @param startCount
	 * @param maxCount
	 * @return
	 * @throws Exception
	 */
	ReadableProductList listItemsByIds(MerchantStoreInfo store, LanguageInfo language, List<Long> ids, int startCount, int maxCount) throws Exception;

	
	/**
	 * List products created in a group, for instance FEATURED group
	 * @param group
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProductList listItemsByGroup(String group, MerchantStoreInfo store, LanguageInfo language) throws Exception;

	/**
	 * Add product to a group
	 * @param product
	 * @param group
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProductList addItemToGroup(Product product, String group, MerchantStoreInfo store, LanguageInfo language) throws Exception;
	
	/**
	 * Removes a product from a group
	 * @param product
	 * @param group
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProductList removeItemFromGroup(Product product, String group, MerchantStoreInfo store, LanguageInfo language) throws Exception;
	
	

}
