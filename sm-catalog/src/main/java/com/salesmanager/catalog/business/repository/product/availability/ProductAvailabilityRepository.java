package com.salesmanager.catalog.business.repository.product.availability;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.catalog.model.product.availability.ProductAvailability;

public interface ProductAvailabilityRepository extends JpaRepository<ProductAvailability, Long> {

}
