package com.salesmanager.catalog.presentation.populator.catalog;

import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.populator.AbstractDataPopulator;
import com.salesmanager.common.business.exception.ConversionException;
import com.salesmanager.catalog.model.product.attribute.ProductOption;
import com.salesmanager.catalog.presentation.model.product.attribute.PersistableProductOption;
import com.salesmanager.catalog.presentation.model.product.attribute.ProductOptionDescription;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.Set;




public class PersistableProductOptionPopulator extends
		AbstractDataPopulator<PersistableProductOption, ProductOption> {
	
	private LanguageInfoService languageInfoService;

	public void setLanguageInfoService(LanguageInfoService languageInfoService) {
		this.languageInfoService = languageInfoService;
	}

	public LanguageInfoService getLanguageInfoService() {
		return languageInfoService;
	}

	@Override
	public ProductOption populate(PersistableProductOption source,
								  ProductOption target, MerchantStoreInfo store, LanguageInfo language)
			throws ConversionException {
		Validate.notNull(languageInfoService, "Requires to set LanguageInfoService");
		
		
		try {
			

			target.setMerchantStore(store);
			target.setProductOptionSortOrder(source.getOrder());
			target.setCode(source.getCode());
			
			if(!CollectionUtils.isEmpty(source.getDescriptions())) {
				Set<com.salesmanager.catalog.model.product.attribute.ProductOptionDescription> descriptions = new HashSet<com.salesmanager.catalog.model.product.attribute.ProductOptionDescription>();
				for(ProductOptionDescription desc  : source.getDescriptions()) {
					com.salesmanager.catalog.model.product.attribute.ProductOptionDescription description = new com.salesmanager.catalog.model.product.attribute.ProductOptionDescription();
					LanguageInfo lang = languageInfoService.findbyCode(desc.getLanguage());
					if(lang==null) {
						throw new ConversionException("Language is null for code " + description.getLanguage() + " use language ISO code [en, fr ...]");
					}
					description.setLanguage(lang);
					description.setName(desc.getName());
					description.setTitle(desc.getTitle());
					description.setProductOption(target);
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
	protected ProductOption createTarget() {
		return null;
	}

}
