package com.salesmanager.catalog.business.repository.product.relationship;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.catalog.model.product.relationship.ProductRelationship;


public interface ProductRelationshipRepository extends JpaRepository<ProductRelationship, Long>, ProductRelationshipRepositoryCustom {

}
