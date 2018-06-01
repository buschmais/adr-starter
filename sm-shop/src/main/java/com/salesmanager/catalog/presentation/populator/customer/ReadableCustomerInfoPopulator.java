package com.salesmanager.catalog.presentation.populator.customer;

import com.salesmanager.catalog.model.integration.core.CustomerInfo;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.model.customer.ReadableCustomerInfo;
import com.salesmanager.catalog.presentation.populator.AbstractDataPopulator;
import com.salesmanager.core.business.exception.ConversionException;

public class ReadableCustomerInfoPopulator extends AbstractDataPopulator<CustomerInfo, ReadableCustomerInfo> {

    @Override
    public ReadableCustomerInfo populate(CustomerInfo customerInfo, ReadableCustomerInfo readableCustomerInfo, MerchantStoreInfo store, LanguageInfo language) throws ConversionException {
        readableCustomerInfo.setFirstName(customerInfo.getFirstName());
        readableCustomerInfo.setLastName(customerInfo.getLastName());
        return readableCustomerInfo;
    }

    @Override
    protected ReadableCustomerInfo createTarget() {
        return null;
    }
}
