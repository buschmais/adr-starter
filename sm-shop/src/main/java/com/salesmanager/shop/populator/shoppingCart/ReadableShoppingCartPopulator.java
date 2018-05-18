package com.salesmanager.shop.populator.shoppingCart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.salesmanager.catalog.api.ProductPriceApi;
import com.salesmanager.catalog.business.service.product.PricingService;
import com.salesmanager.catalog.presentation.util.CatalogImageFilePathUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.catalog.business.service.product.attribute.ProductAttributeService;
import com.salesmanager.core.business.services.shoppingcart.ShoppingCartCalculationService;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.catalog.model.product.attribute.ProductAttribute;
import com.salesmanager.catalog.model.product.attribute.ProductOption;
import com.salesmanager.catalog.model.product.attribute.ProductOptionDescription;
import com.salesmanager.catalog.model.product.attribute.ProductOptionValue;
import com.salesmanager.catalog.model.product.attribute.ProductOptionValueDescription;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.OrderSummary;
import com.salesmanager.core.model.order.OrderTotalSummary;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.shoppingcart.ShoppingCart;
import com.salesmanager.shop.model.order.total.ReadableOrderTotal;
import com.salesmanager.shop.model.shoppingcart.ReadableShoppingCart;
import com.salesmanager.shop.model.shoppingcart.ReadableShoppingCartAttribute;
import com.salesmanager.shop.model.shoppingcart.ReadableShoppingCartAttributeOption;
import com.salesmanager.shop.model.shoppingcart.ReadableShoppingCartAttributeOptionValue;
import com.salesmanager.shop.model.shoppingcart.ReadableShoppingCartItem;
import com.salesmanager.catalog.presentation.populator.catalog.ReadableProductPopulator;

public class ReadableShoppingCartPopulator extends AbstractDataPopulator<ShoppingCart, ReadableShoppingCart> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReadableShoppingCartPopulator.class);
	
	private ProductPriceApi productPriceApi;
	private PricingService pricingService;
    private ShoppingCartCalculationService shoppingCartCalculationService;
    private ProductAttributeService productAttributeService;
    
    private CatalogImageFilePathUtils imageUtils;
	
	@Override
	public ReadableShoppingCart populate(ShoppingCart source, ReadableShoppingCart target, MerchantStore store,
			Language language) throws ConversionException {
    	Validate.notNull(source, "Requires ShoppingCart");
    	Validate.notNull(language, "Requires Language not null");
    	Validate.notNull(store, "Requires MerchantStore not null");
    	Validate.notNull(productPriceApi, "Requires to set priceApi");
    	Validate.notNull(productAttributeService, "Requires to set productAttributeService");
    	Validate.notNull(shoppingCartCalculationService, "Requires to set shoppingCartCalculationService");
    	Validate.notNull(imageUtils, "Requires to set imageUtils");
    	
    	if(target == null) {
    		target = new ReadableShoppingCart();
    	}
    	target.setCode(source.getShoppingCartCode());
    	int cartQuantity = 0;
    	
    	target.setCustomer(source.getCustomerId());
    	
    	try {
    	
    		Set<com.salesmanager.core.model.shoppingcart.ShoppingCartItem> items = source.getLineItems();

            if(items!=null) {

                for(com.salesmanager.core.model.shoppingcart.ShoppingCartItem item : items) {


                	ReadableShoppingCartItem shoppingCartItem = new ReadableShoppingCartItem();

                	ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
                	readableProductPopulator.setPricingService(pricingService);
                	readableProductPopulator.setimageUtils(imageUtils);
                	readableProductPopulator.populate(item.getProduct(), shoppingCartItem,  store, language);



                    shoppingCartItem.setPrice(item.getItemPrice());
					shoppingCartItem.setFinalPrice(productPriceApi.getStoreFormattedAmountWithCurrency(store.toDTO(), item.getItemPrice()));
			
                    shoppingCartItem.setQuantity(item.getQuantity());
                    
                    cartQuantity = cartQuantity + item.getQuantity();
                    
                    BigDecimal subTotal = item.getItemPrice().multiply(new BigDecimal(item.getQuantity()));
                    
                    //calculate sub total (price * quantity)
                    shoppingCartItem.setSubTotal(subTotal);

					shoppingCartItem.setDisplaySubTotal(productPriceApi.getStoreFormattedAmountWithCurrency(store.toDTO(), subTotal));


                    Set<com.salesmanager.core.model.shoppingcart.ShoppingCartAttributeItem> attributes = item.getAttributes();
                    if(attributes!=null) {
                        for(com.salesmanager.core.model.shoppingcart.ShoppingCartAttributeItem attribute : attributes) {

                        	ProductAttribute productAttribute = productAttributeService.getById(attribute.getProductAttributeId());
                        	
                        	if(productAttribute==null) {
                        		LOGGER.warn("Product attribute with ID " + attribute.getId() + " not found, skipping cart attribute " + attribute.getId());
                        		continue;
                        	}
                        	
                        	ReadableShoppingCartAttribute cartAttribute = new ReadableShoppingCartAttribute();
                        	

                            cartAttribute.setId(attribute.getId());
                            
                            ProductOption option = productAttribute.getProductOption();
                            ProductOptionValue optionValue = productAttribute.getProductOptionValue();


                            List<ProductOptionDescription> optionDescriptions = option.getDescriptionsSettoList();
                            List<ProductOptionValueDescription> optionValueDescriptions = optionValue.getDescriptionsSettoList();
                            
                            String optName = null;
                            String optValue = null;
                            if(!CollectionUtils.isEmpty(optionDescriptions) && !CollectionUtils.isEmpty(optionValueDescriptions)) {
                            	
                            	optName = optionDescriptions.get(0).getName();
                            	optValue = optionValueDescriptions.get(0).getName();
                            	
                            	for(ProductOptionDescription optionDescription : optionDescriptions) {
                            		if(optionDescription.getLanguage() != null && optionDescription.getLanguage().getId().intValue() == language.getId().intValue()) {
                            			optName = optionDescription.getName();
                            			break;
                            		}
                            	}
                            	
                            	for(ProductOptionValueDescription optionValueDescription : optionValueDescriptions) {
                            		if(optionValueDescription.getLanguage() != null && optionValueDescription.getLanguage().getId().intValue() == language.getId().intValue()) {
                            			optValue = optionValueDescription.getName();
                            			break;
                            		}
                            	}

                            }
                            
                            if(optName != null) {
                            	ReadableShoppingCartAttributeOption attributeOption = new ReadableShoppingCartAttributeOption();
                            	attributeOption.setCode(option.getCode());
                            	attributeOption.setId(option.getId());
                            	attributeOption.setName(optName);
                            	cartAttribute.setOption(attributeOption);
                            }
                            
                            if(optValue != null) {
                            	ReadableShoppingCartAttributeOptionValue attributeOptionValue = new ReadableShoppingCartAttributeOptionValue();
                            	attributeOptionValue.setCode(optionValue.getCode());
                            	attributeOptionValue.setId(optionValue.getId());
                            	attributeOptionValue.setName(optValue);
                            	cartAttribute.setOptionValue(attributeOptionValue);
                            }
                            shoppingCartItem.getCartItemattributes().add(cartAttribute);  
                        }
                       
                    }
                    target.getProducts().add(shoppingCartItem);
                }
            }
            
            //Calculate totals using shoppingCartService
            //OrderSummary contains ShoppingCart items

            OrderSummary summary = new OrderSummary();
            List<com.salesmanager.core.model.shoppingcart.ShoppingCartItem> productsList = new ArrayList<com.salesmanager.core.model.shoppingcart.ShoppingCartItem>();
            productsList.addAll(source.getLineItems());
            summary.setProducts(productsList);
            
            //OrdetTotalSummary contains all calculations
            
            OrderTotalSummary orderSummary = shoppingCartCalculationService.calculate(source, store, language );

            if(CollectionUtils.isNotEmpty(orderSummary.getTotals())) {
            	List<ReadableOrderTotal> totals = new ArrayList<ReadableOrderTotal>();
            	for(com.salesmanager.core.model.order.OrderTotal t : orderSummary.getTotals()) {
            		ReadableOrderTotal total = new ReadableOrderTotal();
            		total.setCode(t.getOrderTotalCode());
            		total.setValue(t.getValue());
            		totals.add(total);
            	}
            	target.setTotals(totals);
            }
            
            target.setSubtotal(orderSummary.getSubTotal());
            target.setDisplaySubTotal(productPriceApi.getStoreFormattedAmountWithCurrency(store.toDTO(), orderSummary.getSubTotal()));
           
            
            target.setTotal(orderSummary.getTotal());
            target.setDisplayTotal(productPriceApi.getStoreFormattedAmountWithCurrency(store.toDTO(), orderSummary.getTotal()));

            
            target.setQuantity(cartQuantity);
            target.setId(source.getId());
            
            
    	} catch(Exception e) {
    		throw new ConversionException(e);
    	}

        return target;
    	
 
	}

	@Override
	protected ReadableShoppingCart createTarget() {
		return null;
	}

	public ProductPriceApi getProductPriceApi() {
		return productPriceApi;
	}

	public void setProductPriceApi(ProductPriceApi productPriceApi) {
		this.productPriceApi = productPriceApi;
	}

	public ShoppingCartCalculationService getShoppingCartCalculationService() {
		return shoppingCartCalculationService;
	}

	public void setShoppingCartCalculationService(ShoppingCartCalculationService shoppingCartCalculationService) {
		this.shoppingCartCalculationService = shoppingCartCalculationService;
	}

	public CatalogImageFilePathUtils getImageUtils() {
		return imageUtils;
	}

	public void setImageUtils(CatalogImageFilePathUtils imageUtils) {
		this.imageUtils = imageUtils;
	}

	public ProductAttributeService getProductAttributeService() {
		return productAttributeService;
	}

	public void setProductAttributeService(ProductAttributeService productAttributeService) {
		this.productAttributeService = productAttributeService;
	}

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}
}
