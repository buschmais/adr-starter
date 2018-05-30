package com.salesmanager.catalog.api;

import com.salesmanager.catalog.model.product.relationship.ProductRelationship;
import com.salesmanager.catalog.model.product.relationship.ProductRelationshipType;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.integration.language.LanguageDTO;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;

import java.util.List;

public interface ProductRelationshipApi {

    List<ProductRelationship> getGroups(MerchantStoreDTO store);

    List<ProductRelationship> getByType(MerchantStoreDTO store,
                                        ProductRelationshipType type, LanguageDTO language)
            throws ServiceException;

}
