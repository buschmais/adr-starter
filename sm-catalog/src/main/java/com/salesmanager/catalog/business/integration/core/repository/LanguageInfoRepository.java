package com.salesmanager.catalog.business.integration.core.repository;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageInfoRepository extends JpaRepository<LanguageInfo, Integer> {

    @Query("SELECT l FROM LanguageInfo l WHERE l.code = ?1")
    LanguageInfo findByCode(String code);

}
