package com.salesmanager.catalog.business.integration.core.repository;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantStoreInfoRepository extends JpaRepository<MerchantStoreInfo, Integer> {
}
