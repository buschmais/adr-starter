package com.salesmanager.catalog.business.integration.core.service;

import com.salesmanager.catalog.business.integration.core.repository.LanguageInfoRepository;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageInfoService {

    private LanguageInfoRepository languageInfoRepository;

    @Autowired
    public LanguageInfoService(LanguageInfoRepository languageInfoRepository) {
        this.languageInfoRepository = languageInfoRepository;
    }

    public LanguageInfo findbyCode(String code) {
        return this.languageInfoRepository.findByCode(code);
    }
}
