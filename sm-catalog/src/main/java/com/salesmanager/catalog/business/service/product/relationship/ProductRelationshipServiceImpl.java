package com.salesmanager.catalog.business.service.product.relationship;

import java.util.List;

import javax.inject.Inject;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.springframework.stereotype.Service;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.catalog.business.repository.product.relationship.ProductRelationshipRepository;
import com.salesmanager.common.business.service.SalesManagerEntityServiceImpl;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.relationship.ProductRelationship;
import com.salesmanager.catalog.model.product.relationship.ProductRelationshipType;

@Service("productRelationshipService")
public class ProductRelationshipServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductRelationship> implements
		ProductRelationshipService {

	
	private ProductRelationshipRepository productRelationshipRepository;
	
	@Inject
	public ProductRelationshipServiceImpl(
			ProductRelationshipRepository productRelationshipRepository) {
			super(productRelationshipRepository);
			this.productRelationshipRepository = productRelationshipRepository;
	}
	
	@Override
	public void saveOrUpdate(ProductRelationship relationship) throws ServiceException {
		
		if(relationship.getId()!=null && relationship.getId()>0) {
			
			this.update(relationship);
			
		} else {
			this.create(relationship);
		}
		
	}
	
	
	@Override
	public void addGroup(MerchantStoreInfo store, String groupName) throws ServiceException {
		ProductRelationship relationship = new ProductRelationship();
		relationship.setCode(groupName);
		relationship.setStore(store);
		relationship.setActive(true);
		this.save(relationship);
	}
	
	@Override
	public List<ProductRelationship> getGroups(MerchantStoreInfo store) {
		return productRelationshipRepository.getGroups(store);
	}
	
	@Override
	public void deleteGroup(MerchantStoreInfo store, String groupName) throws ServiceException {
		List<ProductRelationship> entities = productRelationshipRepository.getByGroup(store, groupName);
		for(ProductRelationship relation : entities) {
			this.delete(relation);
		}
	}
	
	@Override
	public void deactivateGroup(MerchantStoreInfo store, String groupName) throws ServiceException {
		List<ProductRelationship> entities = productRelationshipRepository.getByGroup(store, groupName);
		for(ProductRelationship relation : entities) {
			relation.setActive(false);
			this.saveOrUpdate(relation);
		}
	}
	
	@Override
	public void activateGroup(MerchantStoreInfo store, String groupName) throws ServiceException {
		List<ProductRelationship> entities = this.getByGroup(store, groupName);
		for(ProductRelationship relation : entities) {
			relation.setActive(true);
			this.saveOrUpdate(relation);
		}
	}
	
	public void delete(ProductRelationship relationship) throws ServiceException {
		
		//throws detached exception so need to query first
		relationship = this.getById(relationship.getId());
		super.delete(relationship);
		
		
	}
	
	@Override
	public List<ProductRelationship> listByProduct(Product product) throws ServiceException {

		return productRelationshipRepository.listByProducts(product);

	}
	
	
	@Override
	public List<ProductRelationship> getByType(MerchantStoreInfo store, Product product, ProductRelationshipType type, LanguageInfo language) throws ServiceException {

		return productRelationshipRepository.getByType(store, type.name(), product, language);

	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStoreInfo store, ProductRelationshipType type, LanguageInfo language) throws ServiceException {
		return productRelationshipRepository.getByType(store, type.name(), language);
	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStoreInfo store, ProductRelationshipType type) throws ServiceException {

		return productRelationshipRepository.getByType(store, type.name());

	}
	
	@Override
	public List<ProductRelationship> getByGroup(MerchantStoreInfo store, String groupName) throws ServiceException {

		return productRelationshipRepository.getByType(store, groupName);

	}
	
	@Override
	public List<ProductRelationship> getByGroup(MerchantStoreInfo store, String groupName, LanguageInfo language) throws ServiceException {

		return productRelationshipRepository.getByType(store, groupName, language);

	}
	
	@Override
	public List<ProductRelationship> getByType(MerchantStoreInfo store, Product product, ProductRelationshipType type) throws ServiceException {
		

		return productRelationshipRepository.getByType(store, type.name(), product);
				
		
	}



}
