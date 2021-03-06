package com.salesmanager.catalog.presentation.api.v0.category;


import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.catalog.business.service.category.CategoryService;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.common.presentation.constants.Constants;
import com.salesmanager.catalog.presentation.model.category.PersistableCategory;
import com.salesmanager.catalog.presentation.model.category.ReadableCategory;
import com.salesmanager.catalog.presentation.controller.category.facade.CategoryFacade;

/**
 * Rest services for category management
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/services")
public class ShoppingCategoryRESTController {
	
	@Inject
	private LanguageInfoService languageInfoService;
	
	@Inject
	private MerchantStoreInfoService merchantStoreInfoService;
	
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private CategoryFacade categoryFacade;
	

	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCategoryRESTController.class);
	
	private MerchantStoreInfo getMerchantStoreFromRequest(HttpServletRequest request, String store) {
		MerchantStoreDTO merchantStoreDTO = (MerchantStoreDTO) request.getAttribute(Constants.MERCHANT_STORE_DTO);

		if(merchantStoreDTO!=null) {
			if(!merchantStoreDTO.getCode().equals(store)) {
				merchantStoreDTO = null;
			}
		}
		MerchantStoreInfo merchantStore;

		if(merchantStoreDTO== null) {
			merchantStore = merchantStoreInfoService.findbyCode(store);
		} else {
			merchantStore = this.merchantStoreInfoService.findbyCode(merchantStoreDTO.getCode());
		}
		return merchantStore;
	}
	
	@RequestMapping( value="/public/{store}/category/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ReadableCategory getCategory(@PathVariable final String store, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		
		
		try {
			
			/** default routine **/

			MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);

			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);
				return null;
			}
			
			LanguageInfo language = languageInfoService.findbyCode(merchantStore.getDefaultLanguage());
			
			Map<String,LanguageInfo> langs = languageInfoService.getLanguagesMap();

			
			if(!StringUtils.isBlank(request.getParameter(Constants.LANG))) {
				String lang = request.getParameter(Constants.LANG);
				if(lang!=null) {
					language = langs.get(language);
				}
			}
			
			if(language==null) {
				language = languageInfoService.findbyCode(merchantStore.getDefaultLanguage());
			}
			
			
			/** end default routine **/

			
			ReadableCategory category  = categoryFacade.getById(merchantStore, id, language);
			
			if(category==null) {
				response.sendError(503,  "Invalid category id");
				return null;
			}


			return category;
		
		} catch (Exception e) {
			LOGGER.error("Error while saving category",e);
			try {
				response.sendError(503, "Error while saving category " + e.getMessage());
			} catch (Exception ignore) {
			}
			return null;
		}
	}
	

	
	
	/**
	 * Create new category for a given MerchantStore
	 */
	@RequestMapping( value="/private/{store}/category", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableCategory createCategory(@PathVariable final String store, @Valid @RequestBody PersistableCategory category, HttpServletRequest request, HttpServletResponse response) {
		
		
		try {


			MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);
			
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);
				return null;
			}
			
			categoryFacade.saveCategory(merchantStore, category);

			
			category.setId(category.getId());

			return category;
		
		} catch (Exception e) {
			LOGGER.error("Error while saving category",e);
			try {
				response.sendError(503, "Error while saving category " + e.getMessage());
			} catch (Exception ignore) {
			}
			return null;
		}
	}
	

	
	/**
	 * Deletes a category for a given MerchantStore
	 */
	@RequestMapping( value="/private/{store}/category/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable final String store, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Category category = categoryService.getById(id);
		if(category != null && category.getMerchantStore().getCode().equalsIgnoreCase(store)){
			categoryService.delete(category);
		}else{
			response.sendError(404, "No Category found for ID : " + id);
		}
	}

	
	

	
}
