package com.salesmanager.catalog.business.service.product.attribute;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.product.attribute.ProductOption;

public interface ProductOptionService extends SalesManagerEntityService<Long, ProductOption> {

	List<ProductOption> listByStore(MerchantStoreInfo store, LanguageInfo language)
			throws ServiceException;


	List<ProductOption> getByName(MerchantStoreInfo store, String name,
								  LanguageInfo language) throws ServiceException;

	void saveOrUpdate(ProductOption entity) throws ServiceException;


	List<ProductOption> listReadOnly(MerchantStoreInfo store, LanguageInfo language)
			throws ServiceException;


	ProductOption getByCode(MerchantStoreInfo store, String optionCode);
	
	ProductOption getById(MerchantStoreInfo store, Long optionId);
	



}
