package com.salesmanager.catalog.business.service.product.attribute;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.attribute.ProductAttribute;
import com.salesmanager.core.model.reference.language.Language;

public interface ProductAttributeService extends
		SalesManagerEntityService<Long, ProductAttribute> {

	void saveOrUpdate(ProductAttribute productAttribute)
			throws ServiceException;
	
	List<ProductAttribute> getByOptionId(MerchantStoreInfo store,
			Long id) throws ServiceException;

	List<ProductAttribute> getByOptionValueId(MerchantStoreInfo store,
			Long id) throws ServiceException;

	List<ProductAttribute> getByProductId(MerchantStoreInfo store, Product product, Language language)
			throws ServiceException;

	List<ProductAttribute> getByAttributeIds(MerchantStoreInfo store, Product product, List<Long> ids)
			throws ServiceException;
}
