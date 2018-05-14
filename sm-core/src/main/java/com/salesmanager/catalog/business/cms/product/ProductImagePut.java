package com.salesmanager.catalog.business.cms.product;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.content.ImageContentFile;


public interface ProductImagePut {
	
	
	public void addProductImage(ProductImage productImage, ImageContentFile contentImage) throws ServiceException;


}