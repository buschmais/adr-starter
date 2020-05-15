package com.salesmanager.catalog.business.service.product.attribute;

import java.util.List;

import javax.inject.Inject;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import org.springframework.stereotype.Service;

import com.salesmanager.catalog.business.repository.product.attribute.ProductAttributeRepository;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.attribute.ProductAttribute;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;

@Service("productAttributeService")
public class ProductAttributeServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductAttribute> implements ProductAttributeService {
	
	private ProductAttributeRepository productAttributeRepository;

	@Inject
	public ProductAttributeServiceImpl(ProductAttributeRepository productAttributeRepository) {
		super(productAttributeRepository);
		this.productAttributeRepository = productAttributeRepository;
	}
	
	@Override
	public ProductAttribute getById(Long id) {
		
		return productAttributeRepository.findOne(id);
		
	}
	
	
	@Override
	public List<ProductAttribute> getByOptionId(MerchantStore store,
			Long id) throws ServiceException {
		
		return productAttributeRepository.findByOptionId(store.getId(), id);
		
	}
	
	@Override
	public List<ProductAttribute> getByAttributeIds(MerchantStore store,
			Product product, List<Long> ids) throws ServiceException {
		
		return productAttributeRepository.findByAttributeIds(store.getId(), product.getId(), ids);
		
	}
	
	@Override
	public List<ProductAttribute> getByOptionValueId(MerchantStore store,
			Long id) throws ServiceException {
		
		return productAttributeRepository.findByOptionValueId(store.getId(), id);
		
	}
	
	/**
	 * Returns all product attributes
	 */
	@Override
	public List<ProductAttribute> getByProductId(MerchantStore store,
			Product product, Language language) throws ServiceException {
		return productAttributeRepository.findByProductId(store.getId(), product.getId(), language.getId());
		
	}


	@Override
	public void saveOrUpdate(ProductAttribute productAttribute)
			throws ServiceException {
		//if(productAttribute.getId()!=null && productAttribute.getId()>0) {
		//	productAttributeRepository.update(productAttribute);
		//} else {
			productAttributeRepository.save(productAttribute);
		//}
		
	}
	
	@Override
	public void delete(ProductAttribute attribute) throws ServiceException {
		
		//override method, this allows the error that we try to remove a detached instance
		attribute = this.getById(attribute.getId());
		super.delete(attribute);
		
	}

}
