package com.salesmanager.catalog.presentation.populator.catalog;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.salesmanager.catalog.business.integration.core.service.CustomerInfoService;
import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.model.integration.core.CustomerInfo;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.populator.AbstractDataPopulator;
import org.apache.commons.lang3.Validate;

import com.salesmanager.common.business.exception.ConversionException;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.review.ProductReview;
import com.salesmanager.catalog.model.product.review.ProductReviewDescription;
import com.salesmanager.catalog.presentation.model.product.PersistableProductReview;
import com.salesmanager.common.presentation.util.DateUtil;



public class PersistableProductReviewPopulator extends
		AbstractDataPopulator<PersistableProductReview, ProductReview> {
	
	
	

	private CustomerInfoService customerInfoService;
	

	private ProductService productService;
	

	private LanguageInfoService languageInfoService;

	public LanguageInfoService getLanguageInfoService() {
		return languageInfoService;
	}

	public void setLanguageInfoService(LanguageInfoService languageInfoService) {
		this.languageInfoService = languageInfoService;
	}

	@Override
	public ProductReview populate(PersistableProductReview source,
								  ProductReview target, MerchantStoreInfo store, LanguageInfo language)
			throws ConversionException {
		
		
		Validate.notNull(customerInfoService,"customerInfoService cannot be null");
		Validate.notNull(productService,"productService cannot be null");
		Validate.notNull(languageInfoService,"languageInfoService cannot be null");
		Validate.notNull(source.getRating(),"Rating cannot bot be null");
		
		try {
			
			if(target==null) {
				target = new ProductReview();
			}
			
			CustomerInfo customer = customerInfoService.findById(source.getCustomerId());
			
			//check if customer belongs to store
			if(customer ==null || customer.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				throw new ConversionException("Invalid customer id for the given store");
			}
			
			if(source.getDate() == null) {
				String date = DateUtil.formatDate(new Date());
				source.setDate(date);
			}
			target.setReviewDate(DateUtil.getDate(source.getDate()));
			target.setCustomer(customer);
			target.setReviewRating(source.getRating());
			
			Product product = productService.getById(source.getProductId());
			
			//check if product belongs to store
			if(product ==null || product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				throw new ConversionException("Invalid product id for the given store");
			}
			
			target.setProduct(product);
			
			LanguageInfo lang = languageInfoService.findbyCode(language.getCode());
			if(lang ==null) {
				throw new ConversionException("Invalid language code, use iso codes (en, fr ...)");
			}
			
			ProductReviewDescription description = new ProductReviewDescription();
			description.setDescription(source.getDescription());
			description.setLanguage(lang);
			description.setName("-");
			description.setProductReview(target);
			
			Set<ProductReviewDescription> descriptions = new HashSet<ProductReviewDescription>();
			descriptions.add(description);
			
			target.setDescriptions(descriptions);
			
			

			
			
			return target;
			
		} catch (Exception e) {
			throw new ConversionException("Cannot populate ProductReview", e);
		}
		
	}

	@Override
	protected ProductReview createTarget() {
		return null;
	}

	public CustomerInfoService getCustomerInfoService() {
		return customerInfoService;
	}

	public void setCustomerInfoService(CustomerInfoService customerInfoService) {
		this.customerInfoService = customerInfoService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}


}
