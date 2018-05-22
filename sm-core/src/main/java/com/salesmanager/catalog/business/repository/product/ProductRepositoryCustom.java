package com.salesmanager.catalog.business.repository.product;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.ProductCriteria;
import com.salesmanager.catalog.model.product.ProductList;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.tax.taxclass.TaxClass;

public interface ProductRepositoryCustom {
	
	
	
	

		ProductList listByStore(MerchantStoreInfo store, Language language,
								ProductCriteria criteria);
		
		 Product getByFriendlyUrl(MerchantStoreInfo store,String seUrl, Locale locale);

		List<Product> getProductsListByCategories(@SuppressWarnings("rawtypes") Set categoryIds);

		List<Product> getProductsListByCategories(Set<Long> categoryIds,
				Language language);

		List<Product> listByTaxClass(TaxClass taxClass);

		List<Product> listByStore(MerchantStoreInfo store);

		Product getProductForLocale(long productId, Language language,
				Locale locale);

		Product getById(Long productId);

		Product getByCode(String productCode, Language language);

		List<Product> getProductsForLocale(MerchantStoreInfo store,
				Set<Long> categoryIds, Language language, Locale locale);

}
