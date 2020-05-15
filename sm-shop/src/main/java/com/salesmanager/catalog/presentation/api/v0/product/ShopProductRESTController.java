package com.salesmanager.catalog.presentation.api.v0.product;

import com.salesmanager.catalog.business.integration.core.service.CustomerInfoService;
import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.service.category.CategoryService;
import com.salesmanager.catalog.business.service.product.PricingService;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.catalog.business.service.product.attribute.ProductOptionService;
import com.salesmanager.catalog.business.service.product.attribute.ProductOptionValueService;
import com.salesmanager.catalog.business.service.product.manufacturer.ManufacturerService;
import com.salesmanager.catalog.business.service.product.review.ProductReviewService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.core.business.services.tax.TaxClassService;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.ProductCriteria;
import com.salesmanager.catalog.model.product.review.ProductReview;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.catalog.presentation.model.manufacturer.PersistableManufacturer;
import com.salesmanager.catalog.presentation.model.product.*;
import com.salesmanager.catalog.presentation.model.product.attribute.PersistableProductOption;
import com.salesmanager.catalog.presentation.model.product.attribute.PersistableProductOptionValue;
import com.salesmanager.catalog.presentation.populator.catalog.PersistableProductOptionPopulator;
import com.salesmanager.catalog.presentation.populator.catalog.PersistableProductOptionValuePopulator;
import com.salesmanager.catalog.presentation.populator.catalog.PersistableProductReviewPopulator;
import com.salesmanager.catalog.presentation.populator.catalog.ReadableProductPopulator;
import com.salesmanager.catalog.presentation.populator.manufacturer.PersistableManufacturerPopulator;
import com.salesmanager.catalog.presentation.controller.items.facade.ProductItemsFacade;
import com.salesmanager.catalog.presentation.controller.product.facade.ProductFacade;
import com.salesmanager.catalog.presentation.model.filter.QueryFilter;
import com.salesmanager.catalog.presentation.model.filter.QueryFilterType;
import com.salesmanager.catalog.presentation.util.CatalogImageFilePathUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * API to create, read, update and delete a Product
 * API to create Manufacturer
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping("/api/v0")
public class ShopProductRESTController {
	
	@Inject
	private MerchantStoreInfoService merchantStoreInfoService;
	
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private ProductFacade productFacade;
	
	@Inject
	private ProductItemsFacade productItemsFacade;
	
	@Inject
	private ProductReviewService productReviewService;
	
	@Inject
	private PricingService pricingService;

	@Inject
	private ProductOptionService productOptionService;
	
	@Inject
	private ProductOptionValueService productOptionValueService;
	
	@Inject
	private TaxClassService taxClassService;
	
	@Inject
	private ManufacturerService manufacturerService;
	
	@Inject
	private LanguageInfoService languageInfoService;
	
	@Autowired
	private CatalogImageFilePathUtils imageUtils;

	@Autowired
	private CustomerInfoService customerInfoService;
	

	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShopProductRESTController.class);

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
	
	/**
	 * Create new product for a given MerchantStore
	 */
	@RequestMapping( value="/private/{store}/product", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableProduct createProduct(@PathVariable final String store, @Valid @RequestBody PersistableProduct product, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {

			MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);
			
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);
				return null;
			}
			
			productFacade.saveProduct(merchantStore, product, languageInfoService.findbyCode(merchantStore.getDefaultLanguage()));
			
			return product;
			
		} catch (Exception e) {
			LOGGER.error("Error while saving product",e);
			try {
				response.sendError(503, "Error while saving product " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
		
	}
	

	@RequestMapping( value="/private/{store}/product/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable final String store, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Product product = productService.getById(id);
		if(product != null && product.getMerchantStore().getCode().equalsIgnoreCase(store)){
			productService.delete(product);
		}else{
			response.sendError(404, "No Product found for ID : " + id);
		}
	}
	
	/**
	 * Method for creating a manufacturer
	 * @param store
	 * @param manufacturer
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( value="/private/{store}/manufacturer", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableManufacturer createManufacturer(@PathVariable final String store, @Valid @RequestBody PersistableManufacturer manufacturer, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {

			MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);
			
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);
				return null;
			}

			PersistableManufacturerPopulator populator = new PersistableManufacturerPopulator();
			populator.setLanguageInfoService(languageInfoService);
			
			com.salesmanager.catalog.model.product.manufacturer.Manufacturer manuf = new com.salesmanager.catalog.model.product.manufacturer.Manufacturer();
			
			populator.populate(manufacturer, manuf, merchantStore, languageInfoService.findbyCode(merchantStore.getDefaultLanguage()));
		
			manufacturerService.save(manuf);
			
			manufacturer.setId(manuf.getId());
			
			return manufacturer;
			
		} catch (Exception e) {
			LOGGER.error("Error while saving product",e);
			try {
				response.sendError(503, "Error while saving product " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
		
	}
	
	
	@RequestMapping( value="/private/{store}/product/optionValue", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableProductOptionValue createProductOptionValue(@PathVariable final String store, @Valid @RequestBody PersistableProductOptionValue optionValue, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {

			MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);
			
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);
				return null;
			}

			PersistableProductOptionValuePopulator populator = new PersistableProductOptionValuePopulator();
			populator.setLanguageInfoService(languageInfoService);
			
			com.salesmanager.catalog.model.product.attribute.ProductOptionValue optValue = new com.salesmanager.catalog.model.product.attribute.ProductOptionValue();
			populator.populate(optionValue, optValue, merchantStore, languageInfoService.findbyCode(merchantStore.getDefaultLanguage()));
		
			productOptionValueService.save(optValue);
			
			optionValue.setId(optValue.getId());
			
			return optionValue;
			
		} catch (Exception e) {
			LOGGER.error("Error while saving product option value",e);
			try {
				response.sendError(503, "Error while saving product option value" + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
		
	}
	
	
	@RequestMapping( value="/private/{store}/product/option", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableProductOption createProductOption(@PathVariable final String store, @Valid @RequestBody PersistableProductOption option, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {

			MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);
			
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);
				return null;
			}

			PersistableProductOptionPopulator populator = new PersistableProductOptionPopulator();
			populator.setLanguageInfoService(languageInfoService);
			
			com.salesmanager.catalog.model.product.attribute.ProductOption opt = new com.salesmanager.catalog.model.product.attribute.ProductOption();
			populator.populate(option, opt, merchantStore, languageInfoService.findbyCode(merchantStore.getDefaultLanguage()));
		
			productOptionService.save(opt);
			
			option.setId(opt.getId());
			
			return option;
			
		} catch (Exception e) {
			LOGGER.error("Error while saving product option",e);
			try {
				response.sendError(503, "Error while saving product option" + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
	}
	
	
	@RequestMapping( value="/private/{store}/product/review", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableProductReview createProductReview(@PathVariable final String store, @Valid @RequestBody PersistableProductReview review, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {

			MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);
			
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(500, "Merchant store is null for code " + store);
				return null;
			}
			
			
			//rating already exist
			ProductReview prodReview = productReviewService.getByProductAndCustomer(review.getProductId(), review.getCustomerId());
			if(prodReview!=null) {
				response.sendError(500, "A review already exist for this customer and product");
				return null;
			}
			
			//rating maximum 5
			if(review.getRating()>Constants.MAX_REVIEW_RATING_SCORE) {
				response.sendError(503, "Maximum rating score is " + Constants.MAX_REVIEW_RATING_SCORE);
				return null;
			}
			
			

			PersistableProductReviewPopulator populator = new PersistableProductReviewPopulator();
			populator.setLanguageInfoService(languageInfoService);
			populator.setCustomerInfoService(customerInfoService);
			populator.setProductService(productService);
			
			com.salesmanager.catalog.model.product.review.ProductReview rev = new com.salesmanager.catalog.model.product.review.ProductReview();
			populator.populate(review, rev, merchantStore, languageInfoService.findbyCode(merchantStore.getDefaultLanguage()));
		
			productReviewService.create(rev);

			
			review.setId(rev.getId());
			
			return review;
			
		} catch (Exception e) {
			LOGGER.error("Error while saving product review",e);
			try {
				response.sendError(503, "Error while saving product review" + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
	}
	

	@RequestMapping("/public/products/{store}")
	@ResponseBody
	public ReadableProductList getProducts(@PathVariable String store, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		/** default routine **/

		MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
			return null;
		}
		
		LanguageInfo l = languageInfoService.findbyCode(merchantStore.getDefaultLanguage());
		
		String lang = l.getCode();
		
		if(!StringUtils.isBlank(request.getParameter(Constants.LANG))) {
			
			lang = request.getParameter(Constants.LANG);
			
		}
		
		
		/** end default routine **/
		
		

		
		return this.getProducts(0, 10000, store, lang, null, null, request, response);
	}
	
/*	*//**
	 * Will get products for a given category
	 * supports language by setting land as a query parameter
	 * supports paging by adding start and max as query parameters
	 * @param store
	 * @param language
	 * @param category
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/public/products/page/{start}/{max}/{store}/{language}/{category}.html")
	@ResponseBody
	public ReadableProductList getProducts(@PathVariable String store, @PathVariable final String category, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		*//** default routine **//*
		
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		if(merchantStore!=null) {
			if(!merchantStore.getCode().equals(store)) {
				merchantStore = null;
			}
		}
		
		if(merchantStore== null) {
			merchantStore = merchantStoreService.getByCode(store);
		}
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
			return null;
		}
		
		Language language = merchantStore.getDefaultLanguage();
		
		String lang = language.getCode();
		
		if(!StringUtils.isBlank(request.getParameter(Constants.LANG))) {
			
			lang = request.getParameter(Constants.LANG);
			
		}
		
		
		*//** end default routine **//*
		
		
		//start
		int iStart = 0;
		if(!StringUtils.isBlank(request.getParameter(Constants.START))) {
			
			String start = request.getParameter(Constants.START);
			
			try {
				iStart = Integer.parseInt(start);
			} catch(Exception e) {
				LOGGER.error("Cannot parse start parameter " + start);
			}

		}
		
		//max
		int iMax = 0;
		if(!StringUtils.isBlank(request.getParameter(Constants.MAX))) {
			
			String max = request.getParameter(Constants.MAX);
			
			try {
				iMax = Integer.parseInt(max);
			} catch(Exception e) {
				LOGGER.error("Cannot parse max parameter " + max);
			}

		}

		
		return this.getProducts(iStart, iMax, store, lang, category, null, request, response);
	}*/
	
	
	/**
	 * An entry point for filtering by another entity such as Manufacturer
	 * filter=BRAND&filter-value=123
	 * @param start
	 * @param max
	 * @param store
	 * @param language
	 * @param category
	 * @param filterType
	 * @param filterValue
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/products/public/page/{start}/{max}/{store}/{language}/{category}.html/filter={filterType}/filter-value={filterValue}")
	@ResponseBody
	public ReadableProductList getProductsFilteredByType(@PathVariable int start, @PathVariable int max, @PathVariable String store, @PathVariable final String language, @PathVariable final String category, @PathVariable final String filterType, @PathVariable final String filterValue, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		List<QueryFilter> queryFilters = null;
		try {
			if(filterType.equals(QueryFilterType.BRAND.name())) {//the only one implemented so far
				QueryFilter filter = new QueryFilter();
				filter.setFilterType(QueryFilterType.BRAND);
				filter.setFilterId(Long.parseLong(filterValue));
				if(queryFilters==null) {
					queryFilters = new ArrayList<QueryFilter>();
				}
				queryFilters.add(filter);
			}
		} catch(Exception e) {
			LOGGER.error("Invalid filter or filter-value " + filterType + " - " + filterValue,e);
		}
		
		return this.getProducts(start, max, store, language, category, queryFilters, request, response);
	}
	
	
	private ReadableProductList getProducts(final int start, final int max, final String store, final String language, final String category, final List<QueryFilter> filters, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {

			
			/**
			 * How to Spring MVC Rest web service - ajax / jquery
			 * http://codetutr.com/2013/04/09/spring-mvc-easy-rest-based-json-services-with-responsebody/
			 */

			MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);
			
			Map<String,LanguageInfo> langs = languageInfoService.getLanguagesMap();
			
			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
				return null;
			}
			


			LanguageInfo lang = langs.get(language);
			if(lang==null) {
				lang = langs.get(Constants.DEFAULT_LANGUAGE);
			}
			
			ProductCriteria productCriteria = new ProductCriteria();
			productCriteria.setMaxCount(max);
			productCriteria.setStartIndex(start);
			
			//get the category by code
			if(!StringUtils.isBlank(category)) {
				Category cat = categoryService.getBySeUrl(merchantStore, category);
				
				if(cat==null) {
					LOGGER.error("Category " + category + " is null");
					response.sendError(503, "Category is null");//TODO localized message
					return null;
				}
				
				
				String lineage = new StringBuilder().append(cat.getLineage()).append(cat.getId()).append("/").toString();
				
				List<Category> categories = categoryService.listByLineage(store, lineage);
				
				List<Long> ids = new ArrayList<Long>();
				if(categories!=null && categories.size()>0) {
					for(Category c : categories) {
						ids.add(c.getId());
					}
				} 
				ids.add(cat.getId());
				
				
				productCriteria.setCategoryIds(ids);
			}
			
			if(filters!=null) {
				for(QueryFilter filter : filters) {
					if(filter.getFilterType().name().equals(QueryFilterType.BRAND.name())) {//the only filter implemented
						productCriteria.setManufacturerId(filter.getFilterId());
					}
				}
			}

			com.salesmanager.catalog.model.product.ProductList products = productService.listByStore(merchantStore, lang, productCriteria);

			
			ReadableProductPopulator populator = new ReadableProductPopulator();
			populator.setPricingService(pricingService);
			populator.setimageUtils(imageUtils);
			
			
			ReadableProductList productList = new ReadableProductList();
			for(Product product : products.getProducts()) {

				//create new proxy product
				ReadableProduct readProduct = populator.populate(product, new ReadableProduct(), merchantStore, lang);
				productList.getProducts().add(readProduct);
				
			}
			
			productList.setTotalCount(products.getTotalCount());
			
			
			return productList;
			
		
		} catch (Exception e) {
			LOGGER.error("Error while getting products",e);
			response.sendError(503, "An error occured while retrieving products " + e.getMessage());
		}
		
		return null;

	}
	
	
	@RequestMapping(value = "/public/{store}/product/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ReadableProduct getProduct(@PathVariable String store, @PathVariable final Long id, @RequestParam String lang, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		/** bcz of the filter **/
		MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);
		
		if(merchantStore==null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);
			return null;
		}

		LanguageInfo language = null;
		
		if(!StringUtils.isBlank(lang)) {
			language = languageInfoService.findbyCode(lang);
		}
		
		if(language==null) {
			language = languageInfoService.findbyCode(merchantStore.getDefaultLanguage());
		}
		
		ReadableProduct product = productFacade.getProduct(merchantStore, id, language);
		
		if(product==null) {
			response.sendError(404, "Product not fount for id " + id);
			return null;
		}
		
		return product;
		
	}

	
	/**
	 * Update the price of an item
	 * ?lang=en|fr otherwise default store language
	 */
	@RequestMapping( value="/private/{store}/product/price/{sku}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ReadableProduct updateProductPrice(@PathVariable final String store, @Valid @RequestBody ProductPriceEntity price, @PathVariable final String sku, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {

			MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);

			String lang = request.getParameter("lang");
			LanguageInfo language = null;


			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);
				return null;
			}
			
			if(StringUtils.isBlank(lang)) {
				language = languageInfoService.findbyCode(merchantStore.getDefaultLanguage());
			} else {
				language = languageInfoService.findbyCode(lang);
			}
			
			if(language==null) {
				language = languageInfoService.findbyCode(merchantStore.getDefaultLanguage());
			}
			
			ReadableProduct product = productFacade.getProduct(merchantStore, sku, language);
			
			if(product==null) {
				LOGGER.error("Product is null for sku " +sku);
				response.sendError(503, "Product is null for sku " +sku);
				return null;
			}
			
			product = productFacade.updateProductPrice(product, price, language);
			
			return product;

			
		} catch (Exception e) {
			LOGGER.error("Error while saving product",e);
			try {
				response.sendError(503, "Error while updating product " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
		
	}

	/**
	 * Update the quantity of an item
	 * ?lang=en|fr otherwise default store language
	 */
	@RequestMapping( value="/private/{store}/product/quantity/{sku}/{qty}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ReadableProduct updateProductQuantity(@PathVariable final String store, @PathVariable final String sku, @PathVariable final int qty, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {

			MerchantStoreInfo merchantStore = getMerchantStoreFromRequest(request, store);

			String lang = request.getParameter("lang");
			LanguageInfo language = null;


			if(merchantStore==null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);
				return null;
			}
			
			if(StringUtils.isBlank(lang)) {
				language = languageInfoService.findbyCode(merchantStore.getDefaultLanguage());
			} else {
				language = languageInfoService.findbyCode(lang);
			}
			
			if(language==null) {
				language = languageInfoService.findbyCode(merchantStore.getDefaultLanguage());
			}
			
			ReadableProduct product = productFacade.getProduct(merchantStore, sku, language);
			
			if(product==null) {
				LOGGER.error("Product is null for sku " +sku);
				response.sendError(503, "Product is null for sku " +sku);
				return null;
			}
			
			product = productFacade.updateProductQuantity(product, qty, language);
			
			return product;

			
		} catch (Exception e) {
			LOGGER.error("Error while saving product",e);
			try {
				response.sendError(503, "Error while updating product " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
		
	}	

}
