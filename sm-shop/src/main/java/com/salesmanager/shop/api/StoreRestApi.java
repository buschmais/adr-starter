package com.salesmanager.shop.api;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.FilePathUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/store")
public class StoreRestApi {

    @Inject
    FilePathUtils filePathUtils;

    @Inject
    MerchantStoreService merchantStoreService;

    @RequestMapping(value = "/shop/uri", method = POST)
    @ResponseBody
    public String getStoreUri(@RequestPart(name = "store", value = "store") MerchantStoreDTO storeDTO, @RequestParam("contextPath") String contextPath) throws ServiceException {
        MerchantStore store = merchantStoreService.getByCode(storeDTO.getCode());
        return filePathUtils.buildStoreUri(store, contextPath)  + Constants.SHOP_URI;
    }

}
