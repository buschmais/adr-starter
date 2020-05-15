package com.salesmanager.catalog.business.util;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.salesmanager.common.business.constants.Constants;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.attribute.ProductAttribute;
import com.salesmanager.catalog.model.product.attribute.ProductOption;
import com.salesmanager.catalog.model.product.attribute.ProductOptionDescription;
import com.salesmanager.catalog.model.product.attribute.ProductOptionValue;
import com.salesmanager.catalog.model.product.attribute.ProductOptionValueDescription;
import com.salesmanager.catalog.model.product.availability.ProductAvailability;


public class CatalogServiceHelper {
	
	/**
	 * Filters descriptions and set the appropriate language
	 * @param p
	 * @param language
	 */
	public static void setToLanguage(Product p, int language) {
		
		
	Set<ProductAttribute> attributes = p.getAttributes();
		if(attributes!=null) {
			
			for(ProductAttribute attribute : attributes) {

				ProductOption po = attribute.getProductOption();
				Set<ProductOptionDescription> spod = po.getDescriptions();
				if(spod!=null) {
					Set<ProductOptionDescription> podDescriptions = new HashSet<ProductOptionDescription>();
					for(ProductOptionDescription pod : spod) {
						//System.out.println("    ProductOptionDescription : " + pod.getProductOptionName());
						if(pod.getLanguage().getId()==language) {
							podDescriptions.add(pod);
						}
					}
					po.setDescriptions(podDescriptions);
				}
				
				ProductOptionValue pov = attribute.getProductOptionValue();
				
				
				Set<ProductOptionValueDescription> spovd = pov.getDescriptions();
				if(spovd!=null) {
					Set<ProductOptionValueDescription> povdDescriptions = new HashSet();
					for(ProductOptionValueDescription povd : spovd) {
						if(povd.getLanguage().getId()==language) {
							povdDescriptions.add(povd);
						}
					}
					pov.setDescriptions(povdDescriptions);
				}
					
			}
		}
		
	}
	
	/**
	 * Overwrites the availability in order to return 1 price / region
	 * @param product
	 * @param locale
	 */
	public static void setToAvailability(Product product, Locale locale) {
		
		Set<ProductAvailability> availabilities =  product.getAvailabilities();
		
		ProductAvailability defaultAvailability = null;
		ProductAvailability localeAvailability = null;
		
		for(ProductAvailability availability : availabilities) {
			
			if(availability.getRegion().equals(Constants.ALL_REGIONS)) {
				defaultAvailability = availability;
			} 
			if(availability.getRegion().equals(locale.getCountry())) {
				localeAvailability = availability;
			}
			
		}
		
		if(defaultAvailability!=null || localeAvailability!=null) {
			Set<ProductAvailability> productAvailabilities = new HashSet<ProductAvailability>();
			if(defaultAvailability!=null) {
				productAvailabilities.add(defaultAvailability);
			}
			if(localeAvailability!=null) {
				productAvailabilities.add(localeAvailability);
			}
			product.setAvailabilities(productAvailabilities);
		}
		
	}

}
