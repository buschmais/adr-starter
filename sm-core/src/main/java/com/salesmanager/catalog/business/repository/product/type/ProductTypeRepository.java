package com.salesmanager.catalog.business.repository.product.type;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.core.model.catalog.product.type.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

	ProductType findByCode(String code);
}
