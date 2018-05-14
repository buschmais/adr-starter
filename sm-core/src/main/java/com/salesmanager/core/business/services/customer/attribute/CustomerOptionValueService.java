package com.salesmanager.core.business.services.customer.attribute;

import java.util.List;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.core.model.customer.attribute.CustomerOptionValue;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;


public interface CustomerOptionValueService extends SalesManagerEntityService<Long, CustomerOptionValue> {



	List<CustomerOptionValue> listByStore(MerchantStore store, Language language)
			throws ServiceException;

	void saveOrUpdate(CustomerOptionValue entity) throws ServiceException;

	CustomerOptionValue getByCode(MerchantStore store, String optionValueCode);



}
