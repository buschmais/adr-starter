package com.salesmanager.catalog.business.service.product.relationship;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.relationship.ProductRelationship;
import com.salesmanager.catalog.model.product.relationship.ProductRelationshipType;
import com.salesmanager.core.model.reference.language.Language;

public interface ProductRelationshipService extends
		SalesManagerEntityService<Long, ProductRelationship> {

	void saveOrUpdate(ProductRelationship relationship) throws ServiceException;

	/**
	 * Get product relationship List for a given type (RELATED, FEATURED...) and language allows
	 * to return the product description in the appropriate language
	 * @param store
	 * @param product
	 * @param type
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	List<ProductRelationship> getByType(MerchantStoreInfo store, Product product,
										ProductRelationshipType type, Language language) throws ServiceException;

	/**
	 * Get product relationship List for a given type (RELATED, FEATURED...) and a given base product
	 * @param store
	 * @param product
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	List<ProductRelationship> getByType(MerchantStoreInfo store, Product product,
			ProductRelationshipType type)
			throws ServiceException;

	/**
	 * Get product relationship List for a given type (RELATED, FEATURED...) 
	 * @param store
	 * @param type
	 * @return
	 * @throws ServiceException
	 */
	List<ProductRelationship> getByType(MerchantStoreInfo store,
			ProductRelationshipType type) throws ServiceException;

	List<ProductRelationship> listByProduct(Product product)
			throws ServiceException;

	List<ProductRelationship> getByType(MerchantStoreInfo store,
			ProductRelationshipType type, Language language)
			throws ServiceException;

	/**
	 * Get a list of relationship acting as groups of products
	 * @param store
	 * @return
	 */
	List<ProductRelationship> getGroups(MerchantStoreInfo store);

	/**
	 * Creates a product group
	 * @param groupName
	 * @throws ServiceException
	 */
	void addGroup(MerchantStoreInfo store, String groupName) throws ServiceException;

	List<ProductRelationship> getByGroup(MerchantStoreInfo store, String groupName)
			throws ServiceException;

	void deleteGroup(MerchantStoreInfo store, String groupName)
			throws ServiceException;

	void deactivateGroup(MerchantStoreInfo store, String groupName)
			throws ServiceException;

	void activateGroup(MerchantStoreInfo store, String groupName)
			throws ServiceException;

	List<ProductRelationship> getByGroup(MerchantStoreInfo store, String groupName,
			Language language) throws ServiceException;

}
