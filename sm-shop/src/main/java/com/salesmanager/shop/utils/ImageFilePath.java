package com.salesmanager.shop.utils;

import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.merchant.MerchantStore;

public interface ImageFilePath {
	
	/**
	 * Context path configured in shopizer-properties.xml
	 * @return
	 */
	public String getContextPath();
	
	
	public String getBasePath();

	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticImageUtils(MerchantStore store, String imageName);
	
	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image by specifying the image type
	 * @param store
	 * @param imageName
	 * @return
	 */
	public String buildStaticImageUtils(MerchantStore store, String type, String imageName);
	
	/**
	 * Builds a merchant store logo path
	 * @param store
	 * @return
	 */
	public String buildStoreLogoFilePath(MerchantStore store);
	
	
	/**
	 * Builds static file path
	 * @param store
	 * @param fileName
	 * @return
	 */
	public String buildStaticContentFilePath(MerchantStore store, String fileName);


}
