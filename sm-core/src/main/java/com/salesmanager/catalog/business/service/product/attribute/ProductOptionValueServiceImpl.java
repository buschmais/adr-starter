package com.salesmanager.catalog.business.service.product.attribute;

import java.util.List;

import javax.inject.Inject;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.springframework.stereotype.Service;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.catalog.business.repository.product.attribute.ProductOptionValueRepository;
import com.salesmanager.common.business.service.SalesManagerEntityServiceImpl;
import com.salesmanager.catalog.model.product.attribute.ProductAttribute;
import com.salesmanager.catalog.model.product.attribute.ProductOptionValue;
import com.salesmanager.core.model.reference.language.Language;

@Service("productOptionValueService")
public class ProductOptionValueServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductOptionValue> implements
		ProductOptionValueService {

	@Inject
	private ProductAttributeService productAttributeService;
	
	private ProductOptionValueRepository productOptionValueRepository;
	
	@Inject
	public ProductOptionValueServiceImpl(
			ProductOptionValueRepository productOptionValueRepository) {
			super(productOptionValueRepository);
			this.productOptionValueRepository = productOptionValueRepository;
	}
	
	
	@Override
	public List<ProductOptionValue> listByStore(MerchantStoreInfo store, Language language) throws ServiceException {
		
		return productOptionValueRepository.findByStoreId(store.getId(), language.getId());
	}
	
	@Override
	public List<ProductOptionValue> listByStoreNoReadOnly(MerchantStoreInfo store, Language language) throws ServiceException {
		
		return productOptionValueRepository.findByReadOnly(store.getId(), language.getId(), false);
	}

	@Override
	public List<ProductOptionValue> getByName(MerchantStoreInfo store, String name, Language language) throws ServiceException {
		
		try {
			return productOptionValueRepository.findByName(store.getId(), name, language.getId());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}
	
	@Override
	public void saveOrUpdate(ProductOptionValue entity) throws ServiceException {
		
		
		//save or update (persist and attach entities
		if(entity.getId()!=null && entity.getId()>0) {

			super.update(entity);
			
		} else {
			
			super.save(entity);
			
		}
		
	}
	
	
	public void delete(ProductOptionValue entity) throws ServiceException {
		
		//remove all attributes having this option
		List<ProductAttribute> attributes = productAttributeService.getByOptionValueId(entity.getMerchantStore(), entity.getId());
		
		for(ProductAttribute attribute : attributes) {
			productAttributeService.delete(attribute);
		}
		
		ProductOptionValue option = getById(entity.getId());
		
		//remove option
		super.delete(option);
		
	}
	
	@Override
	public ProductOptionValue getByCode(MerchantStoreInfo store, String optionValueCode) {
		return productOptionValueRepository.findByCode(store.getId(), optionValueCode);
	}


	@Override
	public ProductOptionValue getById(MerchantStoreInfo store, Long optionValueId) {
		return productOptionValueRepository.findOne(store.getId(), optionValueId);
	}



}
