package com.salesmanager.catalog.presentation.controller.product.facade;

import java.util.List;

import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.ProductCriteria;
import com.salesmanager.catalog.model.product.manufacturer.Manufacturer;
import com.salesmanager.catalog.model.product.review.ProductReview;
import com.salesmanager.catalog.presentation.model.manufacturer.PersistableManufacturer;
import com.salesmanager.catalog.presentation.model.manufacturer.ReadableManufacturer;
import com.salesmanager.catalog.presentation.model.product.PersistableProduct;
import com.salesmanager.catalog.presentation.model.product.PersistableProductReview;
import com.salesmanager.catalog.presentation.model.product.ProductPriceEntity;
import com.salesmanager.catalog.presentation.model.product.ReadableProduct;
import com.salesmanager.catalog.presentation.model.product.ReadableProductList;
import com.salesmanager.catalog.presentation.model.product.ReadableProductReview;

public interface ProductFacade {
	
	PersistableProduct saveProduct(MerchantStoreInfo store, PersistableProduct product, LanguageInfo language) throws Exception;
	
	/**
	 * Get a Product by id and store
	 * @param store
	 * @param id
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct getProduct(MerchantStoreInfo store, Long id, LanguageInfo language) throws Exception;
	
	
	/**
	 * Reads a product by code
	 * @param store
	 * @param uniqueCode
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct getProductByCode(MerchantStoreInfo store, String uniqueCode, LanguageInfo language) throws Exception;
	
	/**
	 * Get a product by sku and store
	 * @param store
	 * @param sku
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct getProduct(MerchantStoreInfo store,String sku, LanguageInfo language) throws Exception;
	
	/**
	 * Sets a new price to an existing product
	 * @param product
	 * @param price
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct updateProductPrice(ReadableProduct product, ProductPriceEntity price, LanguageInfo language) throws Exception;

	/**
	 * Sets a new price to an existing product
	 * @param product
	 * @param quantity
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct updateProductQuantity(ReadableProduct product, int quantity, LanguageInfo language) throws Exception;

	/**
	 * Deletes a product for a given product id
	 * @param product
	 * @throws Exception
	 */
	void deleteProduct(Product product) throws Exception;
	
	
	/**
	 * Filters a list of product based on criteria
	 * @param store
	 * @param language
	 * @param criterias
	 * @return
	 * @throws Exception
	 */
	ReadableProductList getProductListsByCriterias(MerchantStoreInfo store, LanguageInfo language, ProductCriteria criterias) throws Exception;
	
	
	/**
	 * Adds a product to a category
	 * @param category
	 * @param product
	 * @return
	 * @throws Exception
	 */
	ReadableProduct addProductToCategory(Category category, Product product, LanguageInfo language) throws Exception;
	
	/**
	 * Removes item from a category
	 * @param category
	 * @param product
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct removeProductFromCategory(Category category, Product product, LanguageInfo language) throws Exception;

	
	/**
	 * Saves or updates a Product review
	 * @param review
	 * @param language
	 * @throws Exception
	 */
	void saveOrUpdateReview(PersistableProductReview review, MerchantStoreInfo store, LanguageInfo language) throws Exception;

	/**
	 * Deletes a product review
	 * @param review
	 * @param store
	 * @param language
	 * @throws Exception
	 */
	void deleteReview(ProductReview review, MerchantStoreInfo store, LanguageInfo language) throws Exception;

	/**
	 * Get reviews for a given product
	 * @param product
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	List<ReadableProductReview> getProductReviews(Product product, MerchantStoreInfo store, LanguageInfo language) throws Exception;
	
	/**
	 * Creates or saves a manufacturer
	 * @param manufacturer
	 * @param store
	 * @param language
	 * @throws Exception
	 */
	void saveOrUpdateManufacturer(PersistableManufacturer manufacturer, MerchantStoreInfo store, LanguageInfo language) throws Exception;
	
	/**
	 * Deletes a manufacturer
	 * @param manufacturer
	 * @param store
	 * @param language
	 * @throws Exception
	 */
	void deleteManufacturer(Manufacturer manufacturer, MerchantStoreInfo store, LanguageInfo language) throws Exception;
	
	/**
	 * Get a Manufacturer by id
	 * @param id
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableManufacturer getManufacturer(Long id, MerchantStoreInfo store, LanguageInfo language) throws Exception;
	
	/**
	 * Get all Manufacturer
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	List<ReadableManufacturer> getAllManufacturers(MerchantStoreInfo store, LanguageInfo language) throws Exception;
}
