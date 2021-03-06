package com.salesmanager.catalog.presentation.controller.product;

import com.salesmanager.catalog.business.integration.core.service.CustomerInfoService;
import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.business.service.product.PricingService;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.catalog.business.service.product.review.ProductReviewService;
import com.salesmanager.catalog.model.integration.core.CustomerInfo;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.catalog.presentation.controller.ControllerConstants;
import com.salesmanager.catalog.presentation.populator.catalog.PersistableProductReviewPopulator;
import com.salesmanager.catalog.presentation.populator.catalog.ReadableProductPopulator;
import com.salesmanager.catalog.presentation.populator.catalog.ReadableProductReviewPopulator;
import com.salesmanager.catalog.presentation.util.CatalogImageFilePathUtils;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.review.ProductReview;
import com.salesmanager.core.integration.language.LanguageDTO;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import com.salesmanager.common.presentation.constants.Constants;
import com.salesmanager.catalog.presentation.model.product.PersistableProductReview;
import com.salesmanager.catalog.presentation.model.product.ReadableProduct;
import com.salesmanager.catalog.presentation.model.product.ReadableProductReview;
import com.salesmanager.common.presentation.util.DateUtil;
import com.salesmanager.common.presentation.util.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Entry point for logged in customers
 * @author Carl Samson
 *
 */
@Controller
@RequestMapping(Constants.SHOP_URI + "/customer")
public class CustomerProductReviewController {
	
	@Inject
	private ProductService productService;
	
	@Inject
	private LanguageInfoService languageInfoService;
	
	@Inject
	private PricingService pricingService;
	
	@Inject
	private ProductReviewService productReviewService;
	
	@Inject
	private CustomerInfoService customerInfoService;
	
	@Inject
	private LabelUtils messages;
	
	@Autowired
	private CatalogImageFilePathUtils imageUtils;

	@Autowired
	private MerchantStoreInfoService merchantStoreInfoService;

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/review.html", method=RequestMethod.GET)
	public String displayProductReview(@RequestParam Long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	    MerchantStoreDTO storeDTO = (MerchantStoreDTO) request.getSession().getAttribute(Constants.MERCHANT_STORE_DTO);
	    LanguageDTO languageDTO = (LanguageDTO) request.getAttribute(Constants.LANGUAGE_DTO);

		MerchantStoreInfo store = this.merchantStoreInfoService.findbyCode(storeDTO.getCode());
        LanguageInfo language = this.languageInfoService.findbyCode(languageDTO.getCode());

        //get product
        Product product = productService.getById(productId);
        if(product==null) {
        	return "redirect:" + Constants.SHOP_URI;
        }
        
        if(product.getMerchantStore().getId().intValue()!=storeDTO.getId().intValue()) {
        	return "redirect:" + Constants.SHOP_URI;
        }
        
        
        //create readable product
        ReadableProduct readableProduct = new ReadableProduct();
        ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
        readableProductPopulator.setPricingService(pricingService);
        readableProductPopulator.setimageUtils(imageUtils);
		readableProductPopulator.populate(product, readableProduct,  store, language);
        model.addAttribute("product", readableProduct);
        

        CustomerInfo customer =  customerInfoService.findByNick(request.getRemoteUser(), store.getId());
        
        List<ProductReview> reviews = productReviewService.getByProduct(product, language);
	    for(ProductReview r : reviews) {
	    	if(r.getCustomer().getId().longValue()==customer.getId().longValue()) {
	    		
				ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
				ReadableProductReview rev = new ReadableProductReview();
				reviewPopulator.populate(r, rev, store, language);
	    		
	    		model.addAttribute("customerReview", rev);
	    		break;
	    	}
	    }
        
        
        ProductReview review = new ProductReview();
        review.setCustomer(customer);
        review.setProduct(product);
        
        ReadableProductReview productReview = new ReadableProductReview();
        ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
        reviewPopulator.populate(review,  productReview, store, language);
        
        model.addAttribute("review", productReview);
        model.addAttribute("reviews", reviews);
		
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.review).append(".").append(store.getStoreTemplate());

		return template.toString();
		
	}
	
	
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value="/review/submit.html", method=RequestMethod.POST)
	public String submitProductReview(@ModelAttribute("review") PersistableProductReview review, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		MerchantStoreDTO storeDTO = (MerchantStoreDTO) request.getSession().getAttribute(Constants.MERCHANT_STORE_DTO);
		LanguageDTO languageDTO = (LanguageDTO) request.getAttribute("LANGUAGE_DTO");
		LanguageInfo language = this.languageInfoService.findbyCode(languageDTO.getCode());

		MerchantStoreInfo store = this.merchantStoreInfoService.findbyCode(storeDTO.getCode());

		CustomerInfo customer =  customerInfoService.findByNick(request.getRemoteUser(), store.getId());
        
        if(customer==null) {
        	return "redirect:" + Constants.SHOP_URI;
        }

	    
	    Product product = productService.getById(review.getProductId());
	    if(product==null) {
	    	return "redirect:" + Constants.SHOP_URI;
	    }
	    
	    if(StringUtils.isBlank(review.getDescription())) {
	    	FieldError error = new FieldError("description","description",messages.getMessage("NotEmpty.review.description", locale));
			bindingResult.addError(error);
	    }
	    
	    if(review.getRating()==null) {
	    	FieldError error = new FieldError("rating","rating",messages.getMessage("NotEmpty.review.rating", locale, "Product rating is required"));
			bindingResult.addError(error);
	    }
	    

	    
        ReadableProduct readableProduct = new ReadableProduct();
        ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
        readableProductPopulator.setPricingService(pricingService);
        readableProductPopulator.setimageUtils(imageUtils);
        readableProductPopulator.populate(product, readableProduct,  store, language);
        model.addAttribute("product", readableProduct);
	    

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.review).append(".").append(store.getStoreTemplate());

        if ( bindingResult.hasErrors() )
        {

            return template.toString();

        }
		
        
        //check if customer has already evaluated the product
	    List<ProductReview> reviews = productReviewService.getByProduct(product);
	    
	    for(ProductReview r : reviews) {
	    	if(r.getCustomer().getId().longValue()==customer.getId().longValue()) {
				ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
				ReadableProductReview rev = new ReadableProductReview();
				reviewPopulator.populate(r, rev, store, language);
	    		
	    		model.addAttribute("customerReview", rev);
	    		return template.toString();
	    	}
	    }

	    
	    PersistableProductReviewPopulator populator = new PersistableProductReviewPopulator();
	    populator.setCustomerInfoService(customerInfoService);
	    populator.setLanguageInfoService(languageInfoService);
	    populator.setProductService(productService);
	    
	    review.setDate(DateUtil.formatDate(new Date()));
	    review.setCustomerId(customer.getId());
	    
	    ProductReview productReview = populator.populate(review, store, language);
	    productReviewService.create(productReview);
        
        model.addAttribute("review", review);
        model.addAttribute("success", "success");
        
		ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
		ReadableProductReview rev = new ReadableProductReview();
		reviewPopulator.populate(productReview, rev, store, language);
		
        model.addAttribute("customerReview", rev);

		return template.toString();
		
	}
	
	
	
}
