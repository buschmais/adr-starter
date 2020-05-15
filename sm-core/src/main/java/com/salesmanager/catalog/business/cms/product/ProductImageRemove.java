package com.salesmanager.catalog.business.cms.product;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.business.modules.cms.common.ImageRemove;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.image.ProductImage;


public interface ProductImageRemove {

	public void removeProductImage(ProductImage productImage) throws ServiceException;
	public void removeProductImages(Product product) throws ServiceException;
    public void removeImages(final String merchantStoreCode) throws ServiceException;

}
