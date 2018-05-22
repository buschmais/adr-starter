package com.salesmanager.catalog.business.service.product.manufacturer;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.product.manufacturer.Manufacturer;
import com.salesmanager.catalog.model.product.manufacturer.ManufacturerDescription;
import com.salesmanager.core.model.reference.language.Language;

public interface ManufacturerService extends SalesManagerEntityService<Long, Manufacturer> {

	List<Manufacturer> listByStore(MerchantStoreInfo store, Language language)
			throws ServiceException;

	List<Manufacturer> listByStore(MerchantStoreInfo store) throws ServiceException;

	void saveOrUpdate(Manufacturer manufacturer) throws ServiceException;
	
	void addManufacturerDescription(Manufacturer manufacturer, ManufacturerDescription description) throws ServiceException;
	
	Long getCountManufAttachedProducts( Manufacturer manufacturer )  throws ServiceException;
	
	void delete(Manufacturer manufacturer) throws ServiceException;
	
	Manufacturer getByCode(MerchantStoreInfo store, String code);

	/**
	 * List manufacturers by products from a given list of categories
	 * @param store
	 * @param ids
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	List<Manufacturer> listByProductsByCategoriesId(MerchantStoreInfo store,
			List<Long> ids, Language language) throws ServiceException;
	
}
