package com.salesmanager.catalog.business.service.product.type;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.catalog.business.repository.product.type.ProductTypeRepository;
import com.salesmanager.common.business.service.SalesManagerEntityServiceImpl;
import com.salesmanager.catalog.model.product.type.ProductType;

@Service("productTypeService")
public class ProductTypeServiceImpl extends SalesManagerEntityServiceImpl<Long, ProductType>
		implements ProductTypeService {

	private ProductTypeRepository productTypeRepository;
	
	@Inject
	public ProductTypeServiceImpl(
			ProductTypeRepository productTypeRepository) {
			super(productTypeRepository);
			this.productTypeRepository = productTypeRepository;
	}
	
	@Override
	public ProductType getProductType(String productTypeCode) throws ServiceException {
		
		return productTypeRepository.findByCode(productTypeCode);
		
	}



}
