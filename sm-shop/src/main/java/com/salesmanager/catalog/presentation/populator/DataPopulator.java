/**
 * 
 */
package com.salesmanager.catalog.presentation.populator;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.model.reference.language.Language;

/**
 * @author Umesh A
 *
 */
public interface DataPopulator<Source,Target>
{


    public Target populate(Source source, Target target, MerchantStoreInfo store, Language language) throws ConversionException;
    public Target populate(Source source, MerchantStoreInfo store, Language language) throws ConversionException;

   
}
