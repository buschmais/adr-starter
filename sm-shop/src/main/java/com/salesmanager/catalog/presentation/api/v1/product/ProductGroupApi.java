package com.salesmanager.catalog.presentation.api.v1.product;

import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.catalog.business.service.product.relationship.ProductRelationshipService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.controller.items.facade.ProductItemsFacade;
import com.salesmanager.catalog.presentation.model.product.ReadableProductList;
import com.salesmanager.catalog.presentation.util.RestUtils;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.store.controller.store.facade.StoreFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Used for product grouping such as
 * featured items
 * @author carlsamson
 *
 */
@Controller
@RequestMapping("/api/v1")
public class ProductGroupApi {
	
	@Inject
	private LanguageService languageService;
	
	@Inject
	private RestUtils restUtils;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private ProductRelationshipService productRelationshipService;
	
	@Inject
	private ProductItemsFacade productItemsFacade;
	
	@Inject
	private StoreFacade storeFacade;

	@Inject
	private MerchantStoreInfoService merchantStoreInfoService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductGroupApi.class);
	
	
	/**
	 * Query for a product group
	 * public/products/group/{code}?lang=fr|en
	 * no lang it will take session lang or default store lang
	 * code can be any code used while creating product group, defeult being FEATURED
	 * @param store
	 * @param language
	 * @param groupCode
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/products/group/{code}")
	@ResponseBody
	public ReadableProductList getProductItemsByGroup(@PathVariable final String code, @RequestParam(value = "lang", required=false) String lang, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try {

			MerchantStoreInfo merchantStore = merchantStoreInfoService.findbyCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			Language language = restUtils.getRESTLanguage(request, merchantStore);
			
			
			ReadableProductList list = productItemsFacade.listItemsByGroup(code, merchantStore, language);
			
			if(list == null) {
				response.sendError(404, "Group not fount for code " + code);
				return null;
			}
			
			return list;
		
		} catch (Exception e) {
			LOGGER.error("Error while getting products",e);
			response.sendError(503, "An error occured while retrieving products " + e.getMessage());
		}
		
		return null;
		
	}
	
	
    @ResponseStatus(HttpStatus.CREATED)
	@RequestMapping( value="/private/products/{productId}/group/{code}", method=RequestMethod.POST)
    public @ResponseBody ReadableProductList addProductToGroup(@PathVariable Long productId, @PathVariable String code, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
		try {
    	
	    	MerchantStoreInfo merchantStore = merchantStoreInfoService.findbyCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			Language language = restUtils.getRESTLanguage(request, merchantStore);
	    	
	    	//get the product
	    	Product product = productService.getById(productId);
	    	
			if(product==null) {
				response.sendError(404, "Product not fount for id " + productId);
				return null;
			}
			
			ReadableProductList list = productItemsFacade.addItemToGroup(product, code, merchantStore, language);

	    	return list;
	    	
		} catch (Exception e) {
			LOGGER.error("Error while adding product to group",e);
			try {
				response.sendError(503, "Error while adding product to group " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
    }
    
    @ResponseStatus(HttpStatus.OK)
	@RequestMapping( value="/private/products/{productId}/group/{code}", method=RequestMethod.DELETE)
    public @ResponseBody ReadableProductList removeProductFromCategory(@PathVariable Long productId, @PathVariable String code, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
		try {
    	
	    	MerchantStoreInfo merchantStore = merchantStoreInfoService.findbyCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			Language language = restUtils.getRESTLanguage(request, merchantStore);
	    	
	    	//get the product
	    	Product product = productService.getById(productId);
	    	
			if(product==null) {
				response.sendError(404, "Product not fount for id " + productId);
				return null;
			}
			
			ReadableProductList list = productItemsFacade.removeItemFromGroup(product, code, merchantStore, language);

	    	return list;
	    	
		} catch (Exception e) {
			LOGGER.error("Error while removing product from category",e);
			try {
				response.sendError(503, "Error while removing product from category " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
    }


}
