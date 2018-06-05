package com.salesmanager.catalog.api;

import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.file.DigitalProduct;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;

public interface DigitalProductApi {

    DigitalProduct getByProduct(MerchantStoreDTO store, Product product) throws ServiceException;

}
