package com.salesmanager.catalog.business.integration.core.repository;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantStoreInfoRepository extends JpaRepository<MerchantStoreInfo, Integer> {

    @Query("SELECT m FROM MerchantStoreInfo m WHERE m.code = ?1")
    MerchantStoreInfo findByCode(String code);
}
