package com.salesmanager.catalog.presentation.util;

import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.core.integration.language.LanguageDTO;
import com.salesmanager.shop.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RestUtils {

    @Autowired
    private LanguageInfoService languageInfoService;

    /**
     * Should be used by rest web services
     * @param request
     * @param store
     * @return
     * @throws Exception
     */
    public LanguageInfo getRESTLanguage(HttpServletRequest request, MerchantStoreInfo store) throws Exception {

        Validate.notNull(request,"HttpServletRequest must not be null");
        Validate.notNull(store,"MerchantStore must not be null");

        LanguageDTO languageDTO = null;
        LanguageInfo language = null;


        String lang = request.getParameter(Constants.LANG);

        if(StringUtils.isBlank(lang)) {
            //try with HttpSession
            languageDTO = (LanguageDTO) request.getSession().getAttribute(Constants.LANGUAGE_DTO);
            if(languageDTO==null) {
                language = languageInfoService.findbyCode(store.getDefaultLanguage());
            }

            if(language==null) {
                language = languageInfoService.defaultLanguage();
            }
        } else {
            language = languageInfoService.findbyCode(lang);
            if(language==null) {
                languageDTO = (LanguageDTO) request.getSession().getAttribute(Constants.LANGUAGE_DTO);
                if(languageDTO==null) {
                    language = languageInfoService.findbyCode(store.getDefaultLanguage());
                }

                if(language==null) {
                    language = languageInfoService.defaultLanguage();
                }
            }
        }

        return language;
    }

}
