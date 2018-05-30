package com.salesmanager.catalog.business.service.product.attribute;

import java.util.List;

import javax.inject.Inject;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.springframework.stereotype.Service;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.catalog.business.repository.product.attribute.ProductOptionRepository;
import com.salesmanager.common.business.service.SalesManagerEntityServiceImpl;
import com.salesmanager.catalog.model.product.attribute.ProductAttribute;
import com.salesmanager.catalog.model.product.attribute.ProductOption;

@Service("productOptionService")
public class ProductOptionServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductOption> implements ProductOptionService {

	
	private ProductOptionRepository productOptionRepository;
	
	@Inject
	private ProductAttributeService productAttributeService;
	
	@Inject
	public ProductOptionServiceImpl(
			ProductOptionRepository productOptionRepository) {
			super(productOptionRepository);
			this.productOptionRepository = productOptionRepository;
	}
	
	@Override
	public List<ProductOption> listByStore(MerchantStoreInfo store, LanguageInfo language) throws ServiceException {
		
		
		return productOptionRepository.findByStoreId(store.getId(), language.getId());
		
		
	}
	
	@Override
	public List<ProductOption> listReadOnly(MerchantStoreInfo store, LanguageInfo language) throws ServiceException {

		return productOptionRepository.findByReadOnly(store.getId(), language.getId(), true);
		
		
	}
	

	
	@Override
	public List<ProductOption> getByName(MerchantStoreInfo store, String name, LanguageInfo language) throws ServiceException {
		
		try {
			return productOptionRepository.findByName(store.getId(), name, language.getId());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}
	
	@Override
	public void saveOrUpdate(ProductOption entity) throws ServiceException {
		
		
		//save or update (persist and attach entities
		if(entity.getId()!=null && entity.getId()>0) {
			super.update(entity);
		} else {
			super.save(entity);
		}
		
	}
	
	@Override
	public void delete(ProductOption entity) throws ServiceException {
		
		//remove all attributes having this option
		List<ProductAttribute> attributes = productAttributeService.getByOptionId(entity.getMerchantStore(), entity.getId());
		
		for(ProductAttribute attribute : attributes) {
			productAttributeService.delete(attribute);
		}
		
		ProductOption option = this.getById(entity.getId());
		
		//remove option
		super.delete(option);
		
	}
	
	@Override
	public ProductOption getByCode(MerchantStoreInfo store, String optionCode) {
		return productOptionRepository.findByCode(store.getId(), optionCode);
	}

	@Override
	public ProductOption getById(MerchantStoreInfo store, Long optionId) {
		return productOptionRepository.findOne(store.getId(), optionId);
	}
	

	




}
