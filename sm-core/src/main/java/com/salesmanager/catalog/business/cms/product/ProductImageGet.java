package com.salesmanager.catalog.business.cms.product;

import java.util.List;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.business.modules.cms.common.ImageGet;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.file.ProductImageSize;
import com.salesmanager.catalog.model.product.image.ProductImage;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.content.OutputContentFile;

public interface ProductImageGet {
	
	/**
	 * Used for accessing the path directly
	 * @param merchantStoreCode
	 * @param product
	 * @param imageName
	 * @return
	 * @throws ServiceException
	 */
	public OutputContentFile getProductImage(final String merchantStoreCode, final String productCode, final String imageName) throws ServiceException;
	public OutputContentFile getProductImage(final String merchantStoreCode, final String productCode, final String imageName, final ProductImageSize size) throws ServiceException;
	public OutputContentFile getProductImage(ProductImage productImage) throws ServiceException;
	public List<OutputContentFile> getImages(Product product) throws ServiceException;
    public List<OutputContentFile> getImages( final String merchantStoreCode, FileContentType imageContentType ) throws ServiceException;


}
