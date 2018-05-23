package com.salesmanager.catalog.presentation.populator.catalog;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.populator.AbstractDataPopulator;
import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.catalog.model.product.attribute.ProductOptionValue;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.catalog.presentation.model.product.attribute.PersistableProductOptionValue;
import com.salesmanager.catalog.presentation.model.product.attribute.ProductOptionValueDescription;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.Validate;

import java.util.HashSet;
import java.util.Set;



/**
 * Converts a PersistableProductOptionValue to
 * a ProductOptionValue model object
 * @author Carl Samson
 *
 */
public class PersistableProductOptionValuePopulator extends
		AbstractDataPopulator<PersistableProductOptionValue, ProductOptionValue> {

	
	private LanguageService languageService;
	
	public LanguageService getLanguageService() {
		return languageService;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	@Override
	public ProductOptionValue populate(PersistableProductOptionValue source,
									   ProductOptionValue target, MerchantStoreInfo store, Language language)
			throws ConversionException {
		
		Validate.notNull(languageService, "Requires to set LanguageService");
		
		
		try {
			

			target.setMerchantStore(store);
			target.setProductOptionValueSortOrder(source.getOrder());
			target.setCode(source.getCode());
			
			if(!CollectionUtils.isEmpty(source.getDescriptions())) {
				Set<com.salesmanager.catalog.model.product.attribute.ProductOptionValueDescription> descriptions = new HashSet<com.salesmanager.catalog.model.product.attribute.ProductOptionValueDescription>();
				for(ProductOptionValueDescription desc  : source.getDescriptions()) {
					com.salesmanager.catalog.model.product.attribute.ProductOptionValueDescription description = new com.salesmanager.catalog.model.product.attribute.ProductOptionValueDescription();
					Language lang = languageService.getByCode(desc.getLanguage());
					if(lang==null) {
						throw new ConversionException("Language is null for code " + description.getLanguage() + " use language ISO code [en, fr ...]");
					}
					description.setLanguage(lang);
					description.setName(desc.getName());
					description.setTitle(desc.getTitle());
					description.setProductOptionValue(target);
					descriptions.add(description);
				}
				target.setDescriptions(descriptions);
			}
		
		} catch (Exception e) {
			throw new ConversionException(e);
		}
		
		
		return target;
	}

	@Override
	protected ProductOptionValue createTarget() {
		return null;
	}

}
