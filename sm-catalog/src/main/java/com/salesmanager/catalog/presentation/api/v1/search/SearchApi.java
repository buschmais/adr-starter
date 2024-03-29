package com.salesmanager.catalog.presentation.api.v1.search;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.util.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.catalog.presentation.model.SearchProductList;
import com.salesmanager.catalog.presentation.model.SearchProductRequest;
import com.salesmanager.catalog.presentation.controller.search.facade.SearchFacade;

/**
 * Api for searching shopizer catalog based on search term
 * when filtering products based on product attribute is required, see /api/v1/product
 * @author c.samson
 *
 */
@Controller
@RequestMapping("/api/v1")
public class SearchApi {
	
	@Inject
	private SearchFacade searchFacade;
	
	@Inject
	private RestUtils restUtils;

	@Inject
	private MerchantStoreInfoService merchantStoreInfoService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchApi.class);
	
	
	/**
	 * Search products from underlying elastic search
	 * @param searchRequest
	 * @param lang nothing or en or fr ...
	 * @param request
	 * @param response
	 * @return SearchProductList
	 * @throws Exception
	 */
    @ResponseStatus(HttpStatus.FOUND)
	@RequestMapping( value= "/search", method=RequestMethod.POST)
	public @ResponseBody SearchProductList search(@RequestBody SearchProductRequest searchRequest, @RequestParam(value = "lang", required=false) String lang, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try {
			
			MerchantStoreInfo merchantStore = merchantStoreInfoService.findbyCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			LanguageInfo language = restUtils.getRESTLanguage(request, merchantStore);
			SearchProductList productList = searchFacade.search(merchantStore, language, searchRequest);
			
			return productList;
			
		} catch (Exception e) {
			LOGGER.error("Error while searching products",e);
			try {
				response.sendError(503, "Error while searching products " + e.getMessage());
			} catch (Exception ignore) {
			}

		}
		return null;
	}

}
