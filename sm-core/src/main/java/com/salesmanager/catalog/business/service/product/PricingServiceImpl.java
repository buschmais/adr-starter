package com.salesmanager.catalog.business.service.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.salesmanager.catalog.model.integration.core.CustomerInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.catalog.business.util.ProductPriceUtils;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.attribute.ProductAttribute;
import com.salesmanager.catalog.model.product.price.FinalPrice;
import com.salesmanager.core.model.reference.currency.Currency;

/**
 * Contains all the logic required to calculate product price
 * @author Carl Samson
 *
 */
@Service("pricingService")
public class PricingServiceImpl implements PricingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingServiceImpl.class);
	

	@Inject
	private ProductPriceUtils priceUtil;
	
	@Override
	public FinalPrice calculateProductPrice(Product product) throws ServiceException {
		return priceUtil.getFinalPrice(product);
	}
	
	@Override
	public FinalPrice calculateProductPrice(Product product, CustomerInfo customer) throws ServiceException {
		/** TODO add rules for price calculation **/
		return priceUtil.getFinalPrice(product);
	}
	
	@Override
	public FinalPrice calculateProductPrice(Product product, List<ProductAttribute> attributes) throws ServiceException {
		return priceUtil.getFinalProductPrice(product, attributes);
	}
	
	@Override
	public FinalPrice calculateProductPrice(Product product, List<ProductAttribute> attributes, CustomerInfo customer) throws ServiceException {
		/** TODO add rules for price calculation **/
		return priceUtil.getFinalProductPrice(product, attributes);
	}

	@Override
	public String getDisplayAmount(BigDecimal amount, MerchantStoreInfo store) throws ServiceException {
		try {
			String price= priceUtil.getStoreFormatedAmountWithCurrency(store,amount);
			return price;
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount.toString());
			throw new ServiceException(e);
		}
	}
	
	@Override
	public String getDisplayAmount(BigDecimal amount, Locale locale,
			Currency currency, MerchantStoreInfo store) throws ServiceException {
		try {
			String price= priceUtil.getFormatedAmountWithCurrency(locale, currency, amount);
			return price;
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amunt " + amount.toString() + " using locale " + locale.toString() + " and currency " + currency.toString());
			throw new ServiceException(e);
		}
	}

	@Override
	public String getStringAmount(BigDecimal amount, MerchantStoreInfo store)
			throws ServiceException {
		try {
			String price = priceUtil.getAdminFormatedAmount(store, amount);
			return price;
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount.toString());
			throw new ServiceException(e);
		}
	}

	@Override
	public BigDecimal getAmount(String amount) throws ServiceException {

		try {
			return priceUtil.getAmount(amount);
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount);
			throw new ServiceException(e);
		}

	}


	
}
