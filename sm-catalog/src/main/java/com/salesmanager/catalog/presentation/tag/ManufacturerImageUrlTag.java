package com.salesmanager.catalog.presentation.tag;

import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.model.manufacturer.Manufacturer;
import com.salesmanager.catalog.presentation.util.UriUtils;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.common.presentation.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;



public class ManufacturerImageUrlTag extends RequestContextAwareTag {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6319855234657139862L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerImageUrlTag.class);


	private String imageName;
	private String imageType;
	private Manufacturer manufacturer;

	@Autowired
	private MerchantStoreInfoService merchantStoreInfoService;

	@Autowired
	private UriUtils uriUtils;

	public int doStartTagInternal() throws JspException {
		try {
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();

			MerchantStoreDTO storeDTO = (MerchantStoreDTO) request.getAttribute(Constants.ADMIN_STORE_DTO);
			MerchantStoreInfo merchantStore = this.merchantStoreInfoService.findbyCode(storeDTO.getCode());
			
			StringBuilder imagePath = new StringBuilder();
			
			String baseUrl = uriUtils.getStoreUri(merchantStore, request.getContextPath());
			imagePath.append(baseUrl);
			
			pageContext.getOut().print(imagePath.toString());


			
		} catch (Exception ex) {
			LOGGER.error("Error while getting content url", ex);
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}


	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImageType() {
		return imageType;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	

}
