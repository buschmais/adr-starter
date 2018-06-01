package com.salesmanager.catalog.business.service.product.review;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.CustomerInfo;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.review.ProductReview;

public interface ProductReviewService extends
		SalesManagerEntityService<Long, ProductReview> {
	
	
	List<ProductReview> getByCustomer(CustomerInfo customer);
	List<ProductReview> getByProduct(Product product);
	List<ProductReview> getByProduct(Product product, LanguageInfo language);
	ProductReview getByProductAndCustomer(Long productId, Long customerId);
	/**
	 * @param product
	 * @return
	 */
	List<ProductReview> getByProductNoCustomers(Product product);



}
