package com.salesmanager.shop.populator.order.transaction;

import java.util.HashMap;
import java.util.Map;

import com.salesmanager.catalog.api.ProductPriceApi;
import org.apache.commons.lang3.Validate;

import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.payments.Payment;
import com.salesmanager.core.model.payments.PaymentType;
import com.salesmanager.core.model.payments.TransactionType;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.order.transaction.PersistablePayment;

public class PersistablePaymentPopulator extends AbstractDataPopulator<PersistablePayment, Payment> {
	
	
	ProductPriceApi productPriceApi;



	@Override
	public Payment populate(PersistablePayment source, Payment target, MerchantStore store, Language language)
			throws ConversionException {
		
		Validate.notNull(source,"PersistablePayment cannot be null");
		Validate.notNull(productPriceApi,"productPriceApi must be set");
		if(target == null) {
			target = new Payment();
		}
		
		try {
		
			target.setAmount(productPriceApi.getAmount(source.getAmount()));
			target.setModuleName(source.getPaymentModule());
			target.setPaymentType(PaymentType.valueOf(source.getPaymentType()));
			target.setTransactionType(TransactionType.valueOf(source.getTransactionType()));
			
			Map<String,String> metadata = new HashMap<String,String>();
			metadata.put("paymentToken", source.getPaymentToken());
			target.setPaymentMetaData(metadata);
			
			return target;
		
		} catch(Exception e) {
			throw new ConversionException(e);
		}
	}

	@Override
	protected Payment createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public ProductPriceApi getProductPriceApi() {
		return productPriceApi;
	}

	public void setProductPriceApi(ProductPriceApi productPriceApi) {
		this.productPriceApi = productPriceApi;
	}
}
