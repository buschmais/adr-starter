package com.salesmanager.catalog.business.service.product.attribute;

import java.util.List;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.product.attribute.ProductOption;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;

public interface ProductOptionService extends SalesManagerEntityService<Long, ProductOption> {

	List<ProductOption> listByStore(MerchantStore store, Language language)
			throws ServiceException;


	List<ProductOption> getByName(MerchantStore store, String name,
			Language language) throws ServiceException;

	void saveOrUpdate(ProductOption entity) throws ServiceException;


	List<ProductOption> listReadOnly(MerchantStore store, Language language)
			throws ServiceException;


	ProductOption getByCode(MerchantStore store, String optionCode);
	
	ProductOption getById(MerchantStore store, Long optionId);
	



}
