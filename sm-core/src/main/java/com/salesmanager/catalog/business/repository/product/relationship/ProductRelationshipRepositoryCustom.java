package com.salesmanager.catalog.business.repository.product.relationship;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.relationship.ProductRelationship;
import com.salesmanager.core.model.reference.language.Language;


public interface ProductRelationshipRepositoryCustom {

	List<ProductRelationship> getByType(MerchantStoreInfo store, String type,
										Language language);

	List<ProductRelationship> getByType(MerchantStoreInfo store, String type,
			Product product, Language language);

	List<ProductRelationship> getByGroup(MerchantStoreInfo store, String group);

	List<ProductRelationship> getGroups(MerchantStoreInfo store);

	List<ProductRelationship> getByType(MerchantStoreInfo store, String type);

	List<ProductRelationship> listByProducts(Product product);

	List<ProductRelationship> getByType(MerchantStoreInfo store, String type,
			Product product);
	

}
