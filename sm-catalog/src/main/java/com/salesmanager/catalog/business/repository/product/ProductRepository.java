package com.salesmanager.catalog.business.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.catalog.model.product.Product;


public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

}
