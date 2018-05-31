package com.salesmanager.catalog.presentation.populator.catalog;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.populator.AbstractDataPopulator;
import org.apache.commons.lang.Validate;

import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.catalog.business.service.product.PricingService;
import com.salesmanager.catalog.model.product.price.FinalPrice;
import com.salesmanager.catalog.model.product.price.ProductPrice;
import com.salesmanager.catalog.presentation.model.product.ReadableProductPrice;



public class ReadableProductPricePopulator extends
		AbstractDataPopulator<ProductPrice, ReadableProductPrice> {
	
	
	private PricingService pricingService;

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

	@Override
	public ReadableProductPrice populate(ProductPrice source,
										 ReadableProductPrice target, MerchantStoreInfo store, LanguageInfo language)
			throws ConversionException {
		Validate.notNull(pricingService,"pricingService must be set");
		Validate.notNull(source.getProductAvailability(),"productPrice.availability cannot be null");
		Validate.notNull(source.getProductAvailability().getProduct(),"productPrice.availability.product cannot be null");
		
		try {
			
			FinalPrice finalPrice = pricingService.calculateProductPrice(source.getProductAvailability().getProduct());
			
			target.setOriginalPrice(pricingService.getDisplayAmount(source.getProductPriceAmount(), store));
			if(finalPrice.isDiscounted()) {
				target.setDiscounted(true);
				target.setFinalPrice(pricingService.getDisplayAmount(source.getProductPriceSpecialAmount(), store));
			} else {
				target.setFinalPrice(pricingService.getDisplayAmount(finalPrice.getOriginalPrice(), store));
			}
			
		} catch(Exception e) {
			throw new ConversionException("Exception while converting to ReadableProductPrice",e);
		}
		
		
		
		return target;
	}

	@Override
	protected ReadableProductPrice createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
