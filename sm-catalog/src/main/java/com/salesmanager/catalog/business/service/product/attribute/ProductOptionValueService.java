package com.salesmanager.catalog.business.service.product.attribute;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.product.attribute.ProductOptionValue;

public interface ProductOptionValueService extends SalesManagerEntityService<Long, ProductOptionValue> {

	void saveOrUpdate(ProductOptionValue entity) throws ServiceException;

	List<ProductOptionValue> getByName(MerchantStoreInfo store, String name,
									   LanguageInfo language) throws ServiceException;


	List<ProductOptionValue> listByStore(MerchantStoreInfo store, LanguageInfo language)
			throws ServiceException;

	List<ProductOptionValue> listByStoreNoReadOnly(MerchantStoreInfo store,
												   LanguageInfo language) throws ServiceException;

	ProductOptionValue getByCode(MerchantStoreInfo store, String optionValueCode);
	
	ProductOptionValue getById(MerchantStoreInfo store, Long optionValueId);

}
