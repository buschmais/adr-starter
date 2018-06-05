package com.salesmanager.catalog.presentation.controller.admin.product;

import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.service.product.attribute.ProductOptionService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.business.ajax.AjaxResponse;
import com.salesmanager.catalog.model.product.attribute.ProductOption;
import com.salesmanager.catalog.model.product.attribute.ProductOptionDescription;
import com.salesmanager.core.integration.language.LanguageDTO;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.common.presentation.model.admin.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.common.presentation.util.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
public class OptionsController {
	
	@Inject
	LanguageInfoService languageInfoService;
	
	@Inject
	ProductOptionService productOptionService;
	
	@Inject
	LabelUtils messages;

	@Autowired
	private MerchantStoreInfoService merchantStoreInfoService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OptionsController.class);
	
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/options/options.html", method=RequestMethod.GET)
	public String displayOptions(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);



		
		return "catalogue-options-list";
		
		
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/options/editOption.html", method=RequestMethod.GET)
	public String displayOptionEdit(@RequestParam("id") long optionId, HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayOption(optionId,request,response,model,locale);
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/options/createOption.html", method=RequestMethod.GET)
	public String displayOption(HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayOption(null,request,response,model,locale);
	}
	
	private String displayOption(Long optionId, HttpServletRequest request, HttpServletResponse response,Model model,Locale locale) throws Exception {

		
		this.setMenu(model, request);
		MerchantStoreDTO storeDTO = (MerchantStoreDTO) request.getAttribute(Constants.ADMIN_STORE_DTO);
		MerchantStoreInfo store = this.merchantStoreInfoService.findbyCode(storeDTO.getCode());
		
		List<LanguageInfo> languages = store.getLanguages();

		Set<ProductOptionDescription> descriptions = new HashSet<ProductOptionDescription>();
		
		ProductOption option = new ProductOption();
		
		if(optionId!=null && optionId!=0) {//edit mode
			
			
			option = productOptionService.getById(store, optionId);
			
			
			if(option==null) {
				return "redirect:/admin/options/options.html";
			}
			
			Set<ProductOptionDescription> optionDescriptions = option.getDescriptions();
			
			
			
			for(LanguageInfo l : languages) {
			
				ProductOptionDescription optionDescription = null;
				
				if(optionDescriptions!=null) {
					
					for(ProductOptionDescription description : optionDescriptions) {
						
						String code = description.getLanguage().getCode();
						if(code.equals(l.getCode())) {
							optionDescription = description;
						}
						
					}
					
				}
				
				if(optionDescription==null) {
					optionDescription = new ProductOptionDescription();
					optionDescription.setLanguage(l);
				}
				
				descriptions.add(optionDescription);
			
			}

		} else {
			
			for(LanguageInfo l : languages) {
				
				ProductOptionDescription desc = new ProductOptionDescription();
				desc.setLanguage(l);
				descriptions.add(desc);
				
			}
			
		}
		

		option.setDescriptions(descriptions);
		model.addAttribute("option", option);
		return "catalogue-options-details";
		
		
	}
		
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/options/save.html", method=RequestMethod.POST)
	public String saveOption(@Valid @ModelAttribute("option") ProductOption option, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		

		//display menu
		setMenu(model,request);

		MerchantStoreDTO storeDTO = (MerchantStoreDTO) request.getAttribute(Constants.ADMIN_STORE_DTO);
		MerchantStoreInfo store = this.merchantStoreInfoService.findbyCode(storeDTO.getCode());
		ProductOption dbEntity =	null;	

		if(option.getId() != null && option.getId() >0) { //edit entry
			//get from DB
			dbEntity = productOptionService.getById(option.getId());
			
			if(dbEntity==null) {
				return "redirect:/admin/options/options.html";
			}
		}
		
		//validate if it contains an existing code
		ProductOption byCode = productOptionService.getByCode(store, option.getCode());
		if(byCode!=null) {
			ObjectError error = new ObjectError("code",messages.getMessage("message.code.exist", locale));
			result.addError(error);
		}

			
		Map<String,LanguageInfo> langs = languageInfoService.getLanguagesMap();
			

		List<ProductOptionDescription> descriptions = option.getDescriptionsList();
		
		if(descriptions!=null) {
				
				for(ProductOptionDescription description : descriptions) {
					
					String code = description.getLanguage().getCode();
					LanguageInfo l = langs.get(code);
					description.setLanguage(l);
					description.setProductOption(option);
	
				}
				
		}
			
		option.setDescriptions(new HashSet<ProductOptionDescription>(descriptions));
		option.setMerchantStore(store);

		
		if (result.hasErrors()) {
			return "catalogue-options-details";
		}
		

		
		
		productOptionService.saveOrUpdate(option);


		

		model.addAttribute("success","success");
		return "catalogue-options-details";
	}

	
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/options/paging.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageOptions(HttpServletRequest request, HttpServletResponse response) {
		
		String optionName = request.getParameter("name");


		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			
			LanguageDTO languageDTO = (LanguageDTO) request.getAttribute("LANGUAGE");
			LanguageInfo language = this.languageInfoService.findbyCode(languageDTO.getCode());

			MerchantStoreDTO storeDTO = (MerchantStoreDTO) request.getAttribute(Constants.ADMIN_STORE_DTO);
			MerchantStoreInfo store = this.merchantStoreInfoService.findbyCode(storeDTO.getCode());
			
			List<ProductOption> options = null;
					
			if(!StringUtils.isBlank(optionName)) {
				
				options = productOptionService.getByName(store, optionName, language);
				
			} else {
				
				options = productOptionService.listByStore(store, language);
				
			}
					
					

			for(ProductOption option : options) {
				
				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("optionId", option.getId());
				entry.put("display", option.isReadOnly());
				ProductOptionDescription description = option.getDescriptions().iterator().next();
				
				entry.put("name", description.getName());
				entry.put("type", option.getProductOptionType());//TODO resolve with option type label
				resp.addDataEntry(entry);
				
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			

		
		} catch (Exception e) {
			LOGGER.error("Error while paging options", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		
		
	}
	
	

	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-options", "catalogue-options");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	@RequestMapping(value="/admin/options/remove.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteOption(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("optionId");

		MerchantStoreDTO storeDTO = (MerchantStoreDTO) request.getAttribute(Constants.ADMIN_STORE_DTO);
		MerchantStoreInfo store = this.merchantStoreInfoService.findbyCode(storeDTO.getCode());
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long id = Long.parseLong(sid);
			
			ProductOption entity = productOptionService.getById(id);

			if(entity==null || entity.getMerchantStore().getId().intValue()!=store.getId().intValue()) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				
			} else {
				
				productOptionService.delete(entity);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting option", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}

}
