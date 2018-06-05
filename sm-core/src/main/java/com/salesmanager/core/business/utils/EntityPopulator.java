/**
 * 
 */
package com.salesmanager.core.business.utils;

import com.salesmanager.common.business.exception.ConversionException;
import com.salesmanager.core.model.merchant.MerchantStore;

/**
 * @author Umesh A
 *
 */
public interface EntityPopulator<Source,Target>
{

    public Target populateToEntity(Source source, Target target, MerchantStore store)  throws ConversionException;
    public Target populateToEntity(Source source) throws ConversionException;
}
