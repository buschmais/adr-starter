package com.salesmanager.catalog.presentation.populator.catalog;



import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.apache.commons.lang.Validate;

import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.category.CategoryDescription;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.catalog.presentation.model.category.ReadableCategory;

public class ReadableCategoryPopulator extends
		AbstractDataPopulator<Category, ReadableCategory> {

	@Override
	public ReadableCategory populate(Category source, ReadableCategory target,
									 MerchantStoreInfo store, Language language) throws ConversionException {
		
		
		Validate.notNull(source,"Category must not be null");
		
		
		target.setLineage(source.getLineage());
		if(source.getDescriptions()!=null && source.getDescriptions().size()>0) {
			
			CategoryDescription description = source.getDescription();
			if(source.getDescriptions().size()>1) {
				for(CategoryDescription desc : source.getDescriptions()) {
					if(desc.getLanguage().getCode().equals(language.getCode())) {
						description = desc;
						break;
					}
				}
			}
		
		
		
			if(description!=null) {
				com.salesmanager.catalog.presentation.model.category.CategoryDescription desc = new com.salesmanager.catalog.presentation.model.category.CategoryDescription();
				desc.setFriendlyUrl(description.getSeUrl());
				desc.setName(description.getName());
				desc.setId(source.getId());
				desc.setDescription(description.getName());
				desc.setKeyWords(description.getMetatagKeywords());
				desc.setHighlights(description.getCategoryHighlight());
				desc.setTitle(description.getMetatagTitle());
				desc.setMetaDescription(description.getMetatagDescription());
				
				target.setDescription(desc);
			}
		
		}
		
		if(source.getParent()!=null) {
			com.salesmanager.catalog.presentation.model.category.Category parent = new com.salesmanager.catalog.presentation.model.category.Category();
			parent.setCode(source.getParent().getCode());
			parent.setId(source.getParent().getId());
			target.setParent(parent);
		}
		
		
		target.setCode(source.getCode());
		target.setId(source.getId());
		target.setDepth(source.getDepth());
		target.setSortOrder(source.getSortOrder());
		target.setVisible(source.isVisible());
		target.setFeatured(source.isFeatured());

		return target;
		
	}

	@Override
	protected ReadableCategory createTarget() {
		return null;
	}

}
