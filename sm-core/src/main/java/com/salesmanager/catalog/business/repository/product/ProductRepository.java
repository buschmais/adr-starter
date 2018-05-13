package com.salesmanager.catalog.business.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.core.model.catalog.product.Product;


public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

}
