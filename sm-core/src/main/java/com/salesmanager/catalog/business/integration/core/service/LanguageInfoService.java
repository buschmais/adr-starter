package com.salesmanager.catalog.business.integration.core.service;

import com.salesmanager.catalog.business.integration.core.repository.LanguageInfoRepository;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.common.business.constants.Constants;
import com.salesmanager.common.business.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    public LanguageInfo defaultLanguage() {
        LanguageInfo lang = getLanguagesMap().get(Locale.ENGLISH.getLanguage());

        if (lang == null) {
            lang = this.languageInfoRepository.findByCode(Constants.DEFAULT_LANGUAGE);
        }
        return lang;
    }

    public Map<String,LanguageInfo> getLanguagesMap() {

        List<LanguageInfo> langs = this.languageInfoRepository.findAll();
        Map<String,LanguageInfo> returnMap = new LinkedHashMap<>();

        for(LanguageInfo lang : langs) {
            returnMap.put(lang.getCode(), lang);
        }
        return returnMap;

    }

    public List<LanguageInfo> getLanguages() {
        return this.languageInfoRepository.findAll();
    }
}
