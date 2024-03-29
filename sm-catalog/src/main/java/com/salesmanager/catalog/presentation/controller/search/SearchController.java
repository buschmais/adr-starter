package com.salesmanager.catalog.presentation.controller.search;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.controller.ControllerConstants;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.catalog.business.service.category.CategoryService;
import com.salesmanager.catalog.business.service.product.PricingService;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.catalog.business.service.search.SearchService;
import com.salesmanager.catalog.model.search.SearchKeywords;
import com.salesmanager.catalog.model.search.SearchResponse;
import com.salesmanager.common.presentation.constants.Constants;
import com.salesmanager.catalog.presentation.model.SearchProductList;
import com.salesmanager.catalog.presentation.controller.search.facade.SearchFacade;
import com.salesmanager.catalog.presentation.model.search.AutoCompleteRequest;
import com.salesmanager.catalog.presentation.util.CatalogImageFilePathUtils;

@Controller
public class SearchController {
	
	@Inject
	private MerchantStoreInfoService merchantStoreInfoService;
	
	@Inject
	private SearchService searchService;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private PricingService pricingService;
	
	@Inject
	private SearchFacade searchFacade;

	@Autowired
	private CatalogImageFilePathUtils imageUtils;

	@Autowired
	private LanguageInfoService languageInfoService;

	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
	
	private final static int AUTOCOMPLETE_ENTRIES_COUNT = 15;

	
	
	/**
	 * Retrieves a list of keywords for a given series of character typed by the end user
	 * This is used for auto complete on search input field
	 * @param json
	 * @param store
	 * @param language
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/services/public/search/{store}/{language}/autocomplete.json", produces="application/json;charset=UTF-8")
	@ResponseBody
	public String autocomplete(@RequestParam("q") String query, @PathVariable String store, @PathVariable final String language, Model model, HttpServletRequest request, HttpServletResponse response)  {

		MerchantStoreDTO merchantStoreDTO = (MerchantStoreDTO) request.getAttribute(Constants.MERCHANT_STORE_DTO);

		if(merchantStoreDTO!=null) {
			if(!merchantStoreDTO.getCode().equals(store)) {
				merchantStoreDTO = null; //reset for the current request
			}
		}
		
		try {

			MerchantStoreInfo merchantStore;

			if(merchantStoreDTO== null) {
				merchantStore = merchantStoreInfoService.findbyCode(store);
			} else {
				merchantStore = merchantStoreInfoService.findbyCode(merchantStoreDTO.getCode());
			}
			
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
				return null;
			}
			
			AutoCompleteRequest req = new AutoCompleteRequest(store,language);
			/** formatted toJSONString because of te specific field names required in the UI **/
			SearchKeywords keywords = searchService.searchForKeywords(req.getCollectionName(), req.toJSONString(query), AUTOCOMPLETE_ENTRIES_COUNT);
			return keywords.toJSONString();

			
		} catch (Exception e) {
			LOGGER.error("Exception while autocomplete " + e);
		}
		
		return null;
		
	}

	
	/**
	 * Displays the search result page
	 * @param json
	 * @param store
	 * @param language
	 * @param start
	 * @param max
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/services/public/search/{store}/{language}/{start}/{max}/search.json", method=RequestMethod.POST)
	@ResponseBody
	//public SearchProductList search(@RequestBody String json, @PathVariable String store, @PathVariable final String language, @PathVariable int start, @PathVariable int max, Model model, HttpServletRequest request, HttpServletResponse response) {
	public SearchProductList search(@PathVariable String store, @PathVariable final String language, @PathVariable int start, @PathVariable int max, Model model, HttpServletRequest request, HttpServletResponse response) {
		SearchProductList returnList = new SearchProductList();
		MerchantStoreDTO merchantStoreDTO = (MerchantStoreDTO) request.getAttribute(Constants.MERCHANT_STORE_DTO);
		
		String json = null;
		
		try {
			
			StringWriter writer = new StringWriter();
			IOUtils.copy(request.getInputStream(), writer, "UTF-8");
			json = writer.toString();
			
			Map<String,LanguageInfo> langs = languageInfoService.getLanguagesMap();
			
			if(merchantStoreDTO!=null) {
				if(!merchantStoreDTO.getCode().equals(store)) {
					merchantStoreDTO = null; //reset for the current request
				}
			}

			MerchantStoreInfo merchantStore;

			if(merchantStoreDTO== null) {
				merchantStore = merchantStoreInfoService.findbyCode(store);
			} else {
				merchantStore = merchantStoreInfoService.findbyCode(merchantStoreDTO.getCode());
			}
			
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
				return null;
			}
			
			LanguageInfo l = langs.get(language);
			if(l==null) {
				l = languageInfoService.findbyCode(Constants.DEFAULT_LANGUAGE);
			}

			SearchResponse resp = searchService.search(merchantStore, language, json, max, start);
			return searchFacade.copySearchResponse(resp, merchantStore, start, max, l);

		} catch (Exception e) {
			LOGGER.error("Exception occured while querying " + json,e);
		}
		

		
		return returnList;
		
	}
	
	/**
	 * Displays the search page after a search query post
	 * @param query
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/shop/search/search.html"}, method=RequestMethod.POST)
	public String displaySearch(@RequestParam("q") String query, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStoreDTO store = (MerchantStoreDTO) request.getAttribute(Constants.MERCHANT_STORE_DTO);

		model.addAttribute("q",query);
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Search.search).append(".").append(store.getStoreTemplate());
		return template.toString();
	}
	
	
}
