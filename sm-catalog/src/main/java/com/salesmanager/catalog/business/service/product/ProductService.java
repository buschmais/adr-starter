package com.salesmanager.catalog.business.service.product;

import java.util.List;
import java.util.Locale;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.integration.core.TaxClassInfo;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.ProductCriteria;
import com.salesmanager.catalog.model.product.ProductList;
import com.salesmanager.catalog.model.product.description.ProductDescription;



public interface ProductService extends SalesManagerEntityService<Long, Product> {

	void addProductDescription(Product product, ProductDescription description) throws ServiceException;
	
	ProductDescription getProductDescription(Product product, LanguageInfo language);
	
	Product getProductForLocale(long productId, LanguageInfo language, Locale locale) throws ServiceException;
	
	List<Product> getProductsForLocale(Category category, LanguageInfo language, Locale locale) throws ServiceException;

	List<Product> getProducts(List<Long> categoryIds) throws ServiceException;



	ProductList listByStore(MerchantStoreInfo store, LanguageInfo language,
			ProductCriteria criteria);

	List<Product> listByStore(MerchantStoreInfo store);

	List<Product> listByTaxClass(TaxClassInfo taxClass);

	List<Product> getProducts(List<Long> categoryIds, LanguageInfo language)
			throws ServiceException;

	Product getBySeUrl(MerchantStoreInfo store, String seUrl, Locale locale);

	/**
	 * Get a product by sku (code) field  and the language
	 * @param productCode
	 * @param language
	 * @return
	 */
	Product getByCode(String productCode, LanguageInfo language);

	
}
	
