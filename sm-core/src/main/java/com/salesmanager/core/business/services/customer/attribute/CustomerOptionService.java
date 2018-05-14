package com.salesmanager.core.business.services.customer.attribute;

import java.util.List;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.core.model.customer.attribute.CustomerOption;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;


public interface CustomerOptionService extends SalesManagerEntityService<Long, CustomerOption> {

	List<CustomerOption> listByStore(MerchantStore store, Language language)
			throws ServiceException;



	void saveOrUpdate(CustomerOption entity) throws ServiceException;



	CustomerOption getByCode(MerchantStore store, String optionCode);




}
