package com.salesmanager.catalog.business.repository.product.type;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.catalog.model.product.type.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

	ProductType findByCode(String code);
}
