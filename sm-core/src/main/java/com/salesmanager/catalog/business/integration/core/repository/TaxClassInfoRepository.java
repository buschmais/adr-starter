package com.salesmanager.catalog.business.integration.core.repository;

import com.salesmanager.catalog.model.integration.core.TaxClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxClassInfoRepository extends JpaRepository<TaxClassInfo, Long> {

    @Query("SELECT t FROM TaxClassInfo t WHERE t.id = ?1")
    TaxClassInfo findById(Long id);

}
