package com.salesmanager.catalog.api;

import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.attribute.ProductAttribute;
import com.salesmanager.catalog.model.product.price.FinalPrice;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.core.model.reference.currency.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public interface ProductPriceApi {

    String getAdminFormattedAmountWithCurrency(MerchantStoreDTO merchantStoreDTO, BigDecimal amount) throws Exception;

    String getAdminFormattedAmount(MerchantStoreDTO merchantStoreDTO, BigDecimal amount) throws Exception;

    String getStoreFormattedAmountWithCurrency(MerchantStoreDTO merchantStoreDTO, BigDecimal amount) throws Exception;

    BigDecimal getAmount(String formattedAmount) throws Exception;

    FinalPrice getFinalProductPrice(Product product, List<ProductAttribute> attributes);

    String getFormattedAmountWithCurrency(Currency currency, BigDecimal amount) throws Exception;

}
