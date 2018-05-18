package com.salesmanager.catalog.presentation.util;

import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.shop.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CatalogImageFilePathUtils {

    @Autowired
    private Environment environment;

    private String basePath = Constants.STATIC_URI;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String context) {
        this.basePath = context;
    }

    /**
     * Builds product property image url path
     * @param store
     * @param imageName
     * @return
     */
    public String buildProductPropertyImageUtils(MerchantStore store, String imageName) {
        return new StringBuilder().append(getBasePath()).append(Constants.FILES_URI).append(Constants.SLASH).append(store.getCode()).append("/").append(FileContentType.PROPERTY).append("/")
                .append(imageName).toString();
    }

    /**
     * Builds a manufacturer image file path that can be used by image servlet
     * utility for getting the physical image
     * @param store
     * @param manufacturer
     * @param imageName
     * @return
     */
    public String buildManufacturerImageUtils(MerchantStore store, com.salesmanager.catalog.presentation.model.manufacturer.Manufacturer manufacturer, String imageName) {
        return new StringBuilder().append(getBasePath()).append("/").append(store.getCode()).append("/").
                append(FileContentType.MANUFACTURER.name()).append("/")
                .append(manufacturer.getId()).append("/")
                .append(imageName).toString();
    }

    /**
     * Builds a product image file path that can be used by image servlet
     * utility for getting the physical image
     * @param store
     * @param product
     * @param imageName
     * @return
     */
    public String buildProductImageUtils(MerchantStore store, Product product, String imageName) {
        return new StringBuilder().append(getBasePath()).append("/products/").append(store.getCode()).append("/")
                .append(product.getSku()).append("/").append("LARGE").append("/").append(imageName).toString();
    }

    /**
     * Builds a default product image file path that can be used by image servlet
     * utility for getting the physical image
     * @param store
     * @param sku
     * @param imageName
     * @return
     */
    public String buildProductImageUtils(MerchantStore store, String sku, String imageName) {
        return new StringBuilder().append(getBasePath()).append("/products/").append(store.getCode()).append("/")
                .append(sku).append("/").append("LARGE").append("/").append(imageName).toString();
    }

    /**
     * Builds a large product image file path that can be used by the image servlet
     * @param store
     * @param sku
     * @param imageName
     * @return
     */
    public String buildLargeProductImageUtils(MerchantStore store, String sku, String imageName) {
        return new StringBuilder().append(getBasePath()).append("/products/").append(store.getCode()).append("/")
                .append(sku).append("/").append("LARGE").append("/").append(imageName).toString();
    }

    public String getContextPath() {
        return environment.getProperty("CONTEXT_PATH");
    }
}
