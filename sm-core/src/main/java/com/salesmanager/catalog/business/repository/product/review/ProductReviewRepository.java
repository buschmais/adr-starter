package com.salesmanager.catalog.business.repository.product.review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.catalog.model.product.review.ProductReview;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {


	@Query("select p from ProductReview p join fetch p.customer pc join fetch p.product pp join fetch pp.merchantStore ppm left join fetch p.descriptions pd where p.id = ?1")
	ProductReview findOne(Long id);
	
	@Query("select p from ProductReview p join fetch p.customer pc join fetch p.product pp join fetch pp.merchantStore ppm left join fetch p.descriptions pd where pc.id = ?1")
	List<ProductReview> findByCustomer(Long customerId);
	
	@Query("select p from ProductReview p " +
			"left join fetch p.descriptions pd " +
			"join fetch p.customer pc " +
			"join fetch pc.merchantStore pcm " +
			"join fetch p.product pp " +
			"join fetch pp.merchantStore ppm  " +
			"join fetch p.product pp " +
			"join fetch pp.merchantStore ppm " +
			"left join fetch p.descriptions pd " +
			"where pp.id = ?1")
	List<ProductReview> findByProduct(Long productId);
	
	@Query("select p from ProductReview p " +
			"join fetch p.product pp " +
			"join fetch pp.merchantStore ppm  " +
			"where pp.id = ?1")
	List<ProductReview> findByProductNoCustomers(Long productId);
	
	@Query("select p from ProductReview p " +
			"left join fetch p.descriptions pd " +
			"join fetch p.customer pc " +
			"join fetch pc.merchantStore pcm " +
			"join fetch p.product pp " +
			"join fetch pp.merchantStore ppm  " +
			"join fetch p.product pp " +
			"join fetch pp.merchantStore ppm " +
			"left join fetch p.descriptions pd " +
			"where pp.id = ?1 and pd.language.id =?2")
	List<ProductReview> findByProduct(Long productId, Integer languageId);
	
	@Query("select p from ProductReview p " +
			"left join fetch p.descriptions pd " +
			"join fetch p.customer pc " +
			"join fetch pc.merchantStore pcm " +
			"join fetch p.product pp " +
			"join fetch pp.merchantStore ppm  " +
			"join fetch p.product pp " +
			"join fetch pp.merchantStore ppm " +
			"where pp.id = ?1 and pc.id = ?2")
	ProductReview findByProductAndCustomer(Long productId, Long customerId);
	
	
}
