package com.salesmanager.catalog.presentation.populator.manufacturer;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.model.product.manufacturer.Manufacturer;
import com.salesmanager.catalog.presentation.populator.AbstractDataPopulator;
import com.salesmanager.common.business.exception.ConversionException;
import com.salesmanager.catalog.model.product.manufacturer.ManufacturerDescription;
import com.salesmanager.catalog.presentation.model.manufacturer.ReadableManufacturer;

import java.util.Set;

public class ReadableManufacturerPopulator extends AbstractDataPopulator<Manufacturer,ReadableManufacturer>
{



	
	@Override
	public ReadableManufacturer populate(
			com.salesmanager.catalog.model.product.manufacturer.Manufacturer source,
			ReadableManufacturer target, MerchantStoreInfo store, LanguageInfo language) throws ConversionException {
		target.setId(source.getId());
		if(source.getDescriptions()!=null && source.getDescriptions().size()>0) {
			
				Set<ManufacturerDescription> descriptions = source.getDescriptions();
				ManufacturerDescription description = null;
				for(ManufacturerDescription desc : descriptions) {
					if(desc.getLanguage().getCode().equals(language.getCode())) {
						description = desc;
						break;
					}
				}
				
				target.setOrder(source.getOrder());
				target.setId(source.getId());
				target.setCode(source.getCode());
				
				if (description != null) {
					com.salesmanager.catalog.presentation.model.manufacturer.ManufacturerDescription d = new com.salesmanager.catalog.presentation.model.manufacturer.ManufacturerDescription();
					d.setName(description.getName());
					d.setDescription(description.getDescription());
					d.setId(description.getId());
					target.setDescription(d);
				}

		}

		return target;
	}

    @Override
    protected ReadableManufacturer createTarget()
    {
        return null;
    }
}
