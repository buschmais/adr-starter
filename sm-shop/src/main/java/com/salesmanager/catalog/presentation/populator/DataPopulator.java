/**
 * 
 */
package com.salesmanager.catalog.presentation.populator;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.business.exception.ConversionException;

/**
 * @author Umesh A
 *
 */
public interface DataPopulator<Source,Target>
{


    public Target populate(Source source, Target target, MerchantStoreInfo store, LanguageInfo language) throws ConversionException;
    public Target populate(Source source, MerchantStoreInfo store, LanguageInfo language) throws ConversionException;

   
}
