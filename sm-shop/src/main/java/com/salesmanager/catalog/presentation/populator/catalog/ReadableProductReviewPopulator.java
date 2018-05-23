package com.salesmanager.catalog.presentation.populator.catalog;

import java.util.Set;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.populator.AbstractDataPopulator;
import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.catalog.model.product.review.ProductReview;
import com.salesmanager.catalog.model.product.review.ProductReviewDescription;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.catalog.presentation.model.product.ReadableProductReview;
import com.salesmanager.shop.model.customer.ReadableCustomer;
import com.salesmanager.shop.populator.customer.ReadableCustomerPopulator;
import com.salesmanager.common.presentation.util.DateUtil;

public class ReadableProductReviewPopulator extends
		AbstractDataPopulator<ProductReview, ReadableProductReview> {

	@Override
	public ReadableProductReview populate(ProductReview source,
										  ReadableProductReview target, MerchantStoreInfo store, Language language)
			throws ConversionException {

		
		try {
			ReadableCustomerPopulator populator = new ReadableCustomerPopulator();
			ReadableCustomer customer = new ReadableCustomer();
			populator.populate(source.getCustomer(), customer, store, language);

			target.setId(source.getId());
			target.setDate(DateUtil.formatDate(source.getReviewDate()));
			target.setCustomer(customer);
			target.setRating(source.getReviewRating());
			target.setProductId(source.getProduct().getId());
			
			Set<ProductReviewDescription> descriptions = source.getDescriptions();
			if(descriptions!=null) {
				for(ProductReviewDescription description : descriptions) {
					target.setDescription(description.getDescription());
					target.setLanguage(description.getLanguage().getCode());
					break;
				}
			}

			return target;
			
		} catch (Exception e) {
			throw new ConversionException("Cannot populate ProductReview", e);
		}
		
		
		
	}

	@Override
	protected ReadableProductReview createTarget() {
		return null;
	}

}
