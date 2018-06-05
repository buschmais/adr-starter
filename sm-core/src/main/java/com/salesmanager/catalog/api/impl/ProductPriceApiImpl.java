package com.salesmanager.catalog.api.impl;

import com.salesmanager.catalog.api.ProductPriceApi;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.util.ProductPriceUtils;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.attribute.ProductAttribute;
import com.salesmanager.catalog.model.product.price.FinalPrice;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class ProductPriceApiImpl implements ProductPriceApi {

    private ProductPriceUtils productPriceUtils;

    private MerchantStoreInfoService merchantStoreInfoService;

    @Autowired
    public ProductPriceApiImpl(ProductPriceUtils productPriceUtils, MerchantStoreInfoService merchantStoreInfoService) {
        this.productPriceUtils = productPriceUtils;
        this.merchantStoreInfoService = merchantStoreInfoService;
    }

    @Override
    public String getAdminFormattedAmountWithCurrency(MerchantStoreDTO merchantStoreDTO, BigDecimal amount) throws Exception {
        MerchantStoreInfo storeInfo = this.merchantStoreInfoService.findbyCode(merchantStoreDTO.getCode());
        return this.productPriceUtils.getAdminFormatedAmountWithCurrency(storeInfo, amount);
    }

    @Override
    public String getAdminFormattedAmount(MerchantStoreDTO merchantStoreDTO, BigDecimal amount) throws Exception {
        MerchantStoreInfo storeInfo = this.merchantStoreInfoService.findbyCode(merchantStoreDTO.getCode());
        return this.productPriceUtils.getAdminFormatedAmount(storeInfo, amount);
    }

    @Override
    public String getStoreFormattedAmountWithCurrency(MerchantStoreDTO merchantStoreDTO, BigDecimal amount) throws Exception {
        MerchantStoreInfo storeInfo = this.merchantStoreInfoService.findbyCode(merchantStoreDTO.getCode());
        return this.productPriceUtils.getStoreFormatedAmountWithCurrency(storeInfo, amount);
    }

    @Override
    public BigDecimal getAmount(String formattedAmount) throws Exception {
        return this.productPriceUtils.getAmount(formattedAmount);
    }

    @Override
    public FinalPrice getFinalProductPrice(Product product, List<ProductAttribute> attributes) {
        return this.productPriceUtils.getFinalProductPrice(product, attributes);
    }

}
