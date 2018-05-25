package com.salesmanager.core.business.services.tax;

import java.util.List;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.tax.taxclass.TaxClass;

public interface TaxClassService extends SalesManagerEntityService<Long, TaxClass> {

	public List<TaxClass> listByStore(MerchantStore store) throws ServiceException;

	public List<TaxClass> listByStore(Integer storeId) throws ServiceException;

	TaxClass getByCode(String code) throws ServiceException;

	TaxClass getByCode(String code, MerchantStore store)
			throws ServiceException;

}
