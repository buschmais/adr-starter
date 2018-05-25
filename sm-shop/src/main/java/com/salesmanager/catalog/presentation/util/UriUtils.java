package com.salesmanager.catalog.presentation.util;

import com.salesmanager.catalog.business.service.category.CategoryService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

@Component
public class UriUtils {

    @Value("#{catalogEhCacheManager.getCache('catalogCache')}")
    private Cache catalogCache;

    @Autowired
    @Qualifier("coreRestTemplate")
    private RestTemplate coreRestTemplate;

    private static final String STORE_URI_KEY = "STORE_URI";

    private static final String RELATIVE_STORE_URI_KEY = "RELATIVE_STORE_URI";

    public String getStoreUri(MerchantStoreInfo store, String contextPath) {
        MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<>();
        parameter.add("store", store.getCode());
        parameter.add("contextPath", contextPath);

        if (catalogCache.get(STORE_URI_KEY) == null) {
            String shopUrl = coreRestTemplate.postForObject("/store/uri", parameter, String.class);
            catalogCache.put(STORE_URI_KEY, shopUrl);
        }
        return (String) catalogCache.get(STORE_URI_KEY).get();
    }

    public String getRelativeStoreUri(MerchantStoreInfo store, String contextPath) {
        MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<>();
        parameter.add("store", store.getCode());
        parameter.add("contextPath", contextPath);

        if (catalogCache.get(RELATIVE_STORE_URI_KEY) == null) {
            String shopUrl = coreRestTemplate.postForObject("/store/uri/relative", parameter, String.class);
            catalogCache.put(RELATIVE_STORE_URI_KEY, shopUrl);
        }
        return (String) catalogCache.get(RELATIVE_STORE_URI_KEY).get();
    }

}
