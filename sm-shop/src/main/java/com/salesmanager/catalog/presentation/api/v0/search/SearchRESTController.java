package com.salesmanager.catalog.presentation.api.v0.search;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.common.business.ajax.AjaxResponse;
import com.salesmanager.catalog.presentation.controller.search.facade.SearchFacade;


/**
 * Searching and indexing products
 * @author c.samson
 *
 */

@Controller
@RequestMapping("/services")
public class SearchRESTController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchRESTController.class);
	
	@Inject
	private MerchantStoreInfoService merchantStoreInfoService;
	
	@Inject
	private SearchFacade searchFacade;
	
	@RequestMapping( value="/private/{store}/search/index", method=RequestMethod.GET)
	@ResponseBody
	public AjaxResponse indexProducts(@PathVariable String store, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		AjaxResponse resp = new AjaxResponse();
		
		try {

			MerchantStoreInfo merchantStore = merchantStoreInfoService.findbyCode(store);
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				resp.setStatus(500);
				resp.setErrorString("Merchant store is null for code " + store);
				return resp;
			}

			LOGGER.debug("Index all data : " + store);
			searchFacade.indexAllData(merchantStore);
			response.setStatus(200);
			resp.setStatus(200);
			
		} catch(Exception e) {
			resp.setStatus(500);
			resp.setErrorMessage(e);
			response.sendError(503, "Exception while indexing all data for store " + store + " " + e.getMessage());
		}

		return resp;
		
	}

}
