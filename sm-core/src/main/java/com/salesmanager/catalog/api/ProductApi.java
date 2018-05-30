package com.salesmanager.catalog.api;

import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.integration.language.LanguageDTO;

import java.util.Locale;

public interface ProductApi {

    Product getByCode(String productCode, LanguageDTO language);

    Product getProductForLocale(long productId, LanguageDTO language, Locale locale) throws ServiceException;

}
