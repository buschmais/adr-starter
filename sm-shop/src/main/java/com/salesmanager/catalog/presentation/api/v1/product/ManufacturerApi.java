package com.salesmanager.catalog.presentation.api.v1.product;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.service.product.manufacturer.ManufacturerService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.util.RestUtils;
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

import com.salesmanager.catalog.model.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.catalog.presentation.model.manufacturer.PersistableManufacturer;
import com.salesmanager.catalog.presentation.model.manufacturer.ReadableManufacturer;
import com.salesmanager.catalog.presentation.controller.product.facade.ProductFacade;

/**
 * Manufacturer management
 * Collection, Manufacturer ...
 * @author c.samson
 *
 */
@Controller
@RequestMapping("/api/v1")
public class ManufacturerApi {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerApi.class);
	
	@Inject
	private MerchantStoreInfoService merchantStoreInfoService;
	
	@Inject
	private RestUtils restUtils;
	
	@Inject
	private ProductFacade productFacade;

	@Inject
	private ManufacturerService manufacturerService;
	
	
	/**
	 * Method for creating a manufacturer
	 * @param store
	 * @param manufacturer
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( value="/private/manufacturers", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PersistableManufacturer create(@Valid @RequestBody PersistableManufacturer manufacturer, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {
			
			MerchantStoreInfo merchantStore = this.merchantStoreInfoService.findbyCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			Language language = restUtils.getRESTLanguage(request, merchantStore);

			productFacade.saveOrUpdateManufacturer(manufacturer, merchantStore, language);

			return manufacturer;
			
		} catch (Exception e) {
			LOGGER.error("Error while creating manufacturer",e);
			try {
				response.sendError(503, "Error while creating manufacturer " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
		
	}
	
	@RequestMapping( value="/manufacturers/{id}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ReadableManufacturer get(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {
			
			MerchantStoreInfo merchantStore = this.merchantStoreInfoService.findbyCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			Language language = restUtils.getRESTLanguage(request, merchantStore);

			ReadableManufacturer manufacturer = productFacade.getManufacturer(id, merchantStore, language);
			
			if(manufacturer==null) {
				response.sendError(404, "No Manufacturer found for ID : " + id);
			}
			
			return manufacturer;

		} catch (Exception e) {
			LOGGER.error("Error while getting manufacturer",e);
			try {
				response.sendError(503, "Error while getting manufacturer " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			
		}
		
		return null;
		
		
	}
	
	@RequestMapping( value="/manufacturers/", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ReadableManufacturer> getAll(HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {
			
			MerchantStoreInfo merchantStore = this.merchantStoreInfoService.findbyCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			Language language = restUtils.getRESTLanguage(request, merchantStore);

			return productFacade.getAllManufacturers(merchantStore, language);

		} catch (Exception e) {
			LOGGER.error("Error while getting manufacturer list",e);
			try {
				response.sendError(503, "Error while getting manufacturer list " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			
		}
		
		return null;
		
	}
	
	@RequestMapping( value="/private/manufacturers/{id}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public PersistableManufacturer update(@PathVariable Long id, @Valid @RequestBody PersistableManufacturer manufacturer, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {
			
			MerchantStoreInfo merchantStore = this.merchantStoreInfoService.findbyCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			Language language = restUtils.getRESTLanguage(request, merchantStore);

			productFacade.saveOrUpdateManufacturer(manufacturer, merchantStore, language);

			return manufacturer;
			
		} catch (Exception e) {
			LOGGER.error("Error while creating manufacturer",e);
			try {
				response.sendError(503, "Error while creating manufacturer " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}
		
	}
	
	@RequestMapping( value="/manufacturers/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void delete(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		try {
			
			MerchantStoreInfo merchantStore = this.merchantStoreInfoService.findbyCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			Language language = restUtils.getRESTLanguage(request, merchantStore);
			
			Manufacturer manufacturer = manufacturerService.getById(id);
					
			if(manufacturer != null){
				productFacade.deleteManufacturer(manufacturer, merchantStore, language);
			}else{
				response.sendError(404, "No Manufacturer found for ID : " + id);
			}

		} catch (Exception e) {
			LOGGER.error("Error while deleting manufacturer id " + id,e);
			try {
				response.sendError(503, "Error while deleting manufacturer id " + id + " - " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			
		}

	}

}
