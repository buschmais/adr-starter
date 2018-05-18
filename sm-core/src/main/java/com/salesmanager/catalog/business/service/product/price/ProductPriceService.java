package com.salesmanager.catalog.business.service.product.price;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.product.price.ProductPrice;
import com.salesmanager.catalog.model.product.price.ProductPriceDescription;

public interface ProductPriceService extends SalesManagerEntityService<Long, ProductPrice> {

	void addDescription(ProductPrice price, ProductPriceDescription description) throws ServiceException;

	void saveOrUpdate(ProductPrice price) throws ServiceException;
	

}
