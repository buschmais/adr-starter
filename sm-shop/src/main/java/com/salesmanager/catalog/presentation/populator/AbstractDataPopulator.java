/**
 * 
 */
package com.salesmanager.catalog.presentation.populator;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.model.reference.language.Language;

import java.util.Locale;


/**
 * @author Umesh A
 *
 */
public abstract class AbstractDataPopulator<Source,Target> implements DataPopulator<Source, Target>
{

 
   
    private Locale locale;

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public Locale getLocale() {
		return locale;
	}
	

	@Override
	public Target populate(Source source, MerchantStoreInfo store, Language language) throws ConversionException{
	   return populate(source,createTarget(), store, language);
	}
	
	protected abstract Target createTarget();

   

}
