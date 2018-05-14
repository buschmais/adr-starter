package com.salesmanager.shop.populator.order;

import com.salesmanager.common.business.constants.Constants;
import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.catalog.business.service.product.PricingService;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.OrderTotal;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.order.total.ReadableOrderTotal;
import com.salesmanager.common.presentation.util.LabelUtils;
import com.salesmanager.shop.utils.LocaleUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class ReadableOrderTotalPopulator extends
		AbstractDataPopulator<OrderTotal, ReadableOrderTotal> {
	
	
	private PricingService pricingService;


	private LabelUtils messages;




	@Override
	public ReadableOrderTotal populate(OrderTotal source,
			ReadableOrderTotal target, MerchantStore store, Language language)
			throws ConversionException {
		
			Validate.notNull(pricingService,"PricingService must be set");
			Validate.notNull(messages,"LabelUtils must be set");
			
			Locale locale = LocaleUtils.getLocale(language);
		
			try {
				
				target.setCode(source.getOrderTotalCode());
				target.setId(source.getId());
				target.setModule(source.getModule());
				target.setOrder(source.getSortOrder());
				

				target.setTitle(messages.getMessage(source.getOrderTotalCode(), locale, source.getOrderTotalCode()));
				target.setText(source.getText());
				
				target.setValue(source.getValue());
				target.setTotal(pricingService.getDisplayAmount(source.getValue(), store));
				
				if(!StringUtils.isBlank(source.getOrderTotalCode())) {
					if(Constants.OT_DISCOUNT_TITLE.equals(source.getOrderTotalCode())) {
						target.setDiscounted(true);
					}
				}
				
			} catch(Exception e) {
				throw new ConversionException(e);
			}
			
			return target;
		
	}

	@Override
	protected ReadableOrderTotal createTarget() {
		return new ReadableOrderTotal();
	}
	
	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}
	
	public LabelUtils getMessages() {
		return messages;
	}

	public void setMessages(LabelUtils messages) {
		this.messages = messages;
	}

}