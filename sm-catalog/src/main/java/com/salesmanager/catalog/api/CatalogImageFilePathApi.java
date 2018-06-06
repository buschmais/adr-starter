package com.salesmanager.catalog.api;

import com.salesmanager.core.integration.merchant.MerchantStoreDTO;

public interface CatalogImageFilePathApi {

    String buildProductImageUtils(MerchantStoreDTO store, String sku, String imageName);

}
