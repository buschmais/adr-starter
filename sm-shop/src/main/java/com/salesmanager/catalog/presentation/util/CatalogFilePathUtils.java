package com.salesmanager.catalog.presentation.util;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.product.file.DigitalProduct;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.shop.constants.Constants;
import org.springframework.stereotype.Component;

@Component
public class CatalogFilePathUtils {

    private final static String DOWNLOADS = "/downloads/";

    public String buildAdminDownloadProductFilePath(MerchantStoreInfo store, DigitalProduct digitalProduct) {
        return new StringBuilder().append(Constants.ADMIN_URI).append(Constants.FILES_URI).append(DOWNLOADS).append(store.getCode()).append(Constants.SLASH).append(digitalProduct.getProductFileName()).toString();
    }

}
