package com.salesmanager.catalog.presentation.util;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class RestUtils {

    @Autowired
    private LanguageService languageService;

    /**
     * Should be used by rest web services
     * @param request
     * @param store
     * @return
     * @throws Exception
     */
    public Language getRESTLanguage(HttpServletRequest request, MerchantStoreInfo store) throws Exception {

        Validate.notNull(request,"HttpServletRequest must not be null");
        Validate.notNull(store,"MerchantStore must not be null");

        Language language = null;


        String lang = request.getParameter(Constants.LANG);

        if(StringUtils.isBlank(lang)) {
            //try with HttpSession
            language = (Language) request.getSession().getAttribute(Constants.LANGUAGE);
            if(language==null) {
                language = languageService.getByCode(store.getDefaultLanguage());
            }

            if(language==null) {
                language = languageService.defaultLanguage();
            }
        } else {
            language = languageService.getByCode(lang);
            if(language==null) {
                language = (Language) request.getSession().getAttribute(Constants.LANGUAGE);
                if(language==null) {
                    language = languageService.getByCode(store.getDefaultLanguage());
                }

                if(language==null) {
                    language = languageService.defaultLanguage();
                }
            }
        }

        return language;
    }

}
