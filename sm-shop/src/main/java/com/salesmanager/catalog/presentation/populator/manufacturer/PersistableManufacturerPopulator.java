
package com.salesmanager.catalog.presentation.populator.manufacturer;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.populator.AbstractDataPopulator;
import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.catalog.model.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.catalog.presentation.model.manufacturer.ManufacturerDescription;
import com.salesmanager.catalog.presentation.model.manufacturer.PersistableManufacturer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.Validate;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Carl Samson
 *
 */


public class PersistableManufacturerPopulator extends AbstractDataPopulator<PersistableManufacturer, Manufacturer>
{
	
	
	private LanguageService languageService;

	@Override
	public Manufacturer populate(PersistableManufacturer source,
								 Manufacturer target, MerchantStoreInfo store, Language language)
			throws ConversionException {
		
		Validate.notNull(languageService, "Requires to set LanguageService");
		
		try {
			
			target.setMerchantStore(store);
			target.setCode(source.getCode());
			

			if(!CollectionUtils.isEmpty(source.getDescriptions())) {
				Set<com.salesmanager.catalog.model.product.manufacturer.ManufacturerDescription> descriptions = new HashSet<com.salesmanager.catalog.model.product.manufacturer.ManufacturerDescription>();
				for(ManufacturerDescription description : source.getDescriptions()) {
					com.salesmanager.catalog.model.product.manufacturer.ManufacturerDescription desc = new com.salesmanager.catalog.model.product.manufacturer.ManufacturerDescription();
					desc.setManufacturer(target);
					if(desc.getId() != null && desc.getId().longValue()>0) {
						desc.setId(description.getId());
					}
					desc.setDescription(description.getDescription());
					desc.setName(description.getName());
					Language lang = languageService.getByCode(description.getLanguage());
					if(lang==null) {
						throw new ConversionException("Language is null for code " + description.getLanguage() + " use language ISO code [en, fr ...]");
					}
					desc.setLanguage(lang);
					descriptions.add(desc);
				}
				target.setDescriptions(descriptions);
			}
		
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	
		
		return target;
	}

	@Override
	protected Manufacturer createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	public LanguageService getLanguageService() {
		return languageService;
	}


}
