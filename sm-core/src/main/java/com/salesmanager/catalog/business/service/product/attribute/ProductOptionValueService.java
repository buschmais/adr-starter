package com.salesmanager.catalog.business.service.product.attribute;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.product.attribute.ProductOptionValue;
import com.salesmanager.core.model.reference.language.Language;

public interface ProductOptionValueService extends SalesManagerEntityService<Long, ProductOptionValue> {

	void saveOrUpdate(ProductOptionValue entity) throws ServiceException;

	List<ProductOptionValue> getByName(MerchantStoreInfo store, String name,
									   Language language) throws ServiceException;


	List<ProductOptionValue> listByStore(MerchantStoreInfo store, Language language)
			throws ServiceException;

	List<ProductOptionValue> listByStoreNoReadOnly(MerchantStoreInfo store,
			Language language) throws ServiceException;

	ProductOptionValue getByCode(MerchantStoreInfo store, String optionValueCode);
	
	ProductOptionValue getById(MerchantStoreInfo store, Long optionValueId);

}
