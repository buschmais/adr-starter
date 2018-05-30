package com.salesmanager.catalog.business.repository.product.relationship;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.relationship.ProductRelationship;


public interface ProductRelationshipRepositoryCustom {

	List<ProductRelationship> getByType(MerchantStoreInfo store, String type,
										LanguageInfo language);

	List<ProductRelationship> getByType(MerchantStoreInfo store, String type,
			Product product, LanguageInfo language);

	List<ProductRelationship> getByGroup(MerchantStoreInfo store, String group);

	List<ProductRelationship> getGroups(MerchantStoreInfo store);

	List<ProductRelationship> getByType(MerchantStoreInfo store, String type);

	List<ProductRelationship> listByProducts(Product product);

	List<ProductRelationship> getByType(MerchantStoreInfo store, String type,
			Product product);
	

}
