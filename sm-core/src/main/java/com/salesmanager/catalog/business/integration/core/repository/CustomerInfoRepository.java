package com.salesmanager.catalog.business.integration.core.repository;

import com.salesmanager.catalog.model.integration.core.CustomerInfo;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long> {

    @Query("SELECT c FROM CustomerInfo c WHERE c.id = ?1")
    CustomerInfo findById(Long id);

    @Query("SELECT c FROM CustomerInfo c JOIN c.merchantStore m WHERE c.nick = ?1 AND m.id = ?2")
    CustomerInfo findByNick(String nick, Integer storeId);
}
