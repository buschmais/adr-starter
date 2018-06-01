package com.salesmanager.catalog.business.service.product.review;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.salesmanager.catalog.model.integration.core.CustomerInfo;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.catalog.business.repository.product.review.ProductReviewRepository;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.common.business.service.SalesManagerEntityServiceImpl;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.review.ProductReview;

@Service("productReviewService")
public class ProductReviewServiceImpl extends
		SalesManagerEntityServiceImpl<Long, ProductReview> implements
		ProductReviewService {


	private ProductReviewRepository productReviewRepository;
	
	@Inject
	private ProductService productService;
	
	@Inject
	public ProductReviewServiceImpl(
			ProductReviewRepository productReviewRepository) {
			super(productReviewRepository);
			this.productReviewRepository = productReviewRepository;
	}

	@Override
	public List<ProductReview> getByCustomer(CustomerInfo customer) {
		return productReviewRepository.findByCustomer(customer.getId());
	}

	@Override
	public List<ProductReview> getByProduct(Product product) {
		return productReviewRepository.findByProduct(product.getId());
	}
	
	@Override
	public ProductReview getByProductAndCustomer(Long productId, Long customerId) {
		return productReviewRepository.findByProductAndCustomer(productId, customerId);
	}
	
	@Override
	public List<ProductReview> getByProduct(Product product, LanguageInfo language) {
		return productReviewRepository.findByProduct(product.getId(), language.getId());
	}
	
	private void saveOrUpdate(ProductReview review) throws ServiceException {
		

		Validate.notNull(review,"ProductReview cannot be null");
		Validate.notNull(review.getProduct(),"ProductReview.product cannot be null");
		Validate.notNull(review.getCustomer(),"ProductReview.customer cannot be null");
		
		
		//refresh product
		Product product = productService.getById(review.getProduct().getId());
		
		//ajust product rating
		Integer count = 0;
		if(product.getProductReviewCount()!=null) {
			count = product.getProductReviewCount();
		}
				
		
		

		BigDecimal averageRating = product.getProductReviewAvg();
		if(averageRating==null) {
			averageRating = new BigDecimal(0);
		}
		//get reviews

		
		BigDecimal totalRating = averageRating.multiply(new BigDecimal(count));
		totalRating = totalRating.add(new BigDecimal(review.getReviewRating()));
		
		count = count + 1;
		double avg = totalRating.doubleValue() / count.intValue();
		
		product.setProductReviewAvg(new BigDecimal(avg));
		product.setProductReviewCount(count);
		super.save(review);
		
		productService.update(product);
		
		review.setProduct(product);
		
	}
	
	public void update(ProductReview review) throws ServiceException {
		this.saveOrUpdate(review);
	}
	
	public void create(ProductReview review) throws ServiceException {
		this.saveOrUpdate(review);
	}

	/* (non-Javadoc)
	 * @see com.salesmanager.catalog.business.service.product.review.ProductReviewService#getByProductNoObjects(com.salesmanager.catalog.model.product.Product)
	 */
	@Override
	public List<ProductReview> getByProductNoCustomers(Product product) {
		return productReviewRepository.findByProductNoCustomers(product.getId());
	}


}
