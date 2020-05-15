package com.salesmanager.shop.store.api.v1.order;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.salesmanager.catalog.api.ProductPriceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.business.services.shipping.ShippingQuoteService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.OrderSummary;
import com.salesmanager.core.model.order.OrderTotalSummary;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.shipping.ShippingSummary;
import com.salesmanager.core.model.shoppingcart.ShoppingCart;
import com.salesmanager.core.model.shoppingcart.ShoppingCartItem;
import com.salesmanager.shop.model.order.ReadableOrderTotalSummary;
import com.salesmanager.shop.populator.order.ReadableOrderSummaryPopulator;
import com.salesmanager.shop.store.controller.shoppingCart.facade.ShoppingCartFacade;
import com.salesmanager.shop.store.controller.store.facade.StoreFacade;
import com.salesmanager.common.presentation.util.LabelUtils;
import com.salesmanager.shop.utils.LanguageUtils;

@Controller
@RequestMapping("/api/v1")
public class OrderTotalApi {
	
	@Inject
	private StoreFacade storeFacade;
	
	@Inject
	private LanguageUtils languageUtils;
	
	@Inject
	private ShoppingCartFacade shoppingCartFacade;
	
	@Inject
	private LabelUtils messages;
	
	@Inject
	private ProductPriceApi productPriceApi;
	
	@Inject
	private CustomerService customerService;
	
	@Inject
	private ShippingQuoteService shippingQuoteService;
	
	@Inject
	private OrderService orderService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderTotalApi.class);
	
	/**
	 * This service calculates order total for a given shopping cart
	 * This method takes in consideration any applicable sales tax
	 * An optional request parameter accepts a quote id that was received using
	 * shipping api
	 * @param id
	 * @param quote
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/auth/cart/{id}/payment"}, method=RequestMethod.GET)
	@ResponseBody
	public ReadableOrderTotalSummary payment(
			@PathVariable final Long id,
			@RequestParam(value = "quote", required=false) Long quote,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
	
		try {
			MerchantStore merchantStore = storeFacade.getByCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			Language language = languageUtils.getRESTLanguage(request, merchantStore);	
			
			Principal principal = request.getUserPrincipal();
			String userName = principal.getName();
			
			Customer customer = customerService.getByNick(userName);
			
			if(customer == null) {
				response.sendError(503, "Error while getting user details to calculate shipping quote");
			}
			
			ShoppingCart shoppingCart = shoppingCartFacade.getShoppingCartModel(id, merchantStore);
			
			if(shoppingCart == null) {
				response.sendError(404, "Cart id " + id + " does not exist");
				return null;
			}
			
			if(shoppingCart.getCustomerId()==null) {
				response.sendError(404, "Cart id " + id + " does not exist for exist for user " + userName);
				return null;
			}
			
			if(shoppingCart.getCustomerId().longValue() != customer.getId().longValue()) {
				response.sendError(404, "Cart id " + id + " does not exist for exist for user " + userName);
				return null;
			}
			
			
			ShippingSummary shippingSummary = null;
			
			//get shipping quote if asked for
			if(quote != null) {
				shippingSummary = shippingQuoteService.getShippingSummary(quote, merchantStore);
			}
			
			OrderTotalSummary orderTotalSummary = null;
			
			OrderSummary orderSummary = new OrderSummary();
			orderSummary.setShippingSummary(shippingSummary);
	    	List<ShoppingCartItem> itemsSet = new ArrayList<ShoppingCartItem>(shoppingCart.getLineItems());
	    	orderSummary.setProducts(itemsSet);
			
			orderTotalSummary = orderService.caculateOrderTotal(orderSummary, customer, merchantStore, language);
			
			ReadableOrderTotalSummary returnSummary = new ReadableOrderTotalSummary();
			ReadableOrderSummaryPopulator populator = new ReadableOrderSummaryPopulator();
			populator.setMessages(messages);
			populator.setProductPriceApi(productPriceApi);
			populator.populate(orderTotalSummary, returnSummary, merchantStore, language);
			
			return returnSummary;
			

		
		} catch (Exception e) {
			LOGGER.error("Error while calculating order summary",e);
			try {
				response.sendError(503, "Error while calculating order summary " + e.getMessage());
			} catch (Exception ignore) {
			}
			return null;
		}
	}

}
