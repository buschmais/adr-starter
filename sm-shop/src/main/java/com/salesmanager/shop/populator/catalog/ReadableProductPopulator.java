package com.salesmanager.shop.populator.catalog;

import com.salesmanager.catalog.api.CatalogImageFilePathApi;
import com.salesmanager.catalog.api.ProductPriceApi;
import com.salesmanager.catalog.business.service.product.PricingService;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.availability.ProductAvailability;
import com.salesmanager.catalog.model.product.description.ProductDescription;
import com.salesmanager.catalog.model.product.image.ProductImage;
import com.salesmanager.catalog.model.product.manufacturer.ManufacturerDescription;
import com.salesmanager.catalog.model.product.price.FinalPrice;
import com.salesmanager.catalog.presentation.model.category.ReadableCategory;
import com.salesmanager.catalog.presentation.model.manufacturer.ReadableManufacturer;
import com.salesmanager.catalog.presentation.model.product.ReadableImage;
import com.salesmanager.catalog.presentation.model.product.ReadableProduct;
import com.salesmanager.catalog.presentation.util.CatalogImageFilePathUtils;
import com.salesmanager.common.business.constants.Constants;
import com.salesmanager.common.presentation.util.DateUtil;
import com.salesmanager.common.business.exception.ConversionException;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ReadableProductPopulator extends
		AbstractDataPopulator<Product, ReadableProduct> {
	
	private PricingService pricingService;

	private ProductPriceApi productPriceApi;
	
	private CatalogImageFilePathUtils imageUtils;

	private CatalogImageFilePathApi imageFilePathApi;

	private CustomerService customerService;

	public CatalogImageFilePathUtils getimageUtils() {
		return imageUtils;
	}

	public void setimageUtils(CatalogImageFilePathUtils imageUtils) {
		this.imageUtils = imageUtils;
	}

	public CatalogImageFilePathApi getImageFilePathApi() {
		return imageFilePathApi;
	}

	public void setImageFilePathApi(CatalogImageFilePathApi imageFilePathApi) {
		this.imageFilePathApi = imageFilePathApi;
	}

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

	public ProductPriceApi getProductPriceApi() {
		return productPriceApi;
	}

	public void setProductPriceApi(ProductPriceApi productPriceApi) {
		this.productPriceApi = productPriceApi;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	@Override
	public ReadableProduct populate(Product source,
			ReadableProduct target, MerchantStore store, Language language)
			throws ConversionException {
		Validate.notNull(pricingService, "Requires to set PricingService");
		Validate.notNull(imageUtils, "Requires to set imageUtils");
		Validate.notNull(language, "Language cannot be null");
		
		try {
			

			ProductDescription description = source.getProductDescription();
			
			Set<ProductDescription> descriptions = source.getDescriptions();
			for(ProductDescription desc : descriptions) {
				
				if(desc.getLanguage()!=null && desc.getLanguage().getId().intValue() == language.getId().intValue()) {
					description = desc;
					break;
				}
				
			}

	
			target.setId(source.getId());
			target.setAvailable(source.isAvailable());
			target.setProductHeight(source.getProductHeight());
			target.setProductLength(source.getProductLength());
			target.setProductWeight(source.getProductWeight());
			target.setProductWidth(source.getProductWidth());
			target.setPreOrder(source.isPreOrder());
			target.setRefSku(source.getRefSku());
			target.setSortOrder(source.getSortOrder());
			
			
			target.setCondition(source.getCondition());
			
			
			//RENTAL
			if(source.getRentalDuration()!=null) {
				target.setRentalDuration(source.getRentalDuration());
			}
			if(source.getRentalPeriod()!=null) {
				target.setRentalPeriod(source.getRentalPeriod());
			}
			target.setRentalStatus(source.getRentalStatus());
			
			/**
			 * END RENTAL
			 */
			
			
			if(source.getDateAvailable() != null) {
				target.setDateAvailable(DateUtil.formatDate(source.getDateAvailable()));
			}
			
			if(source.getProductReviewAvg()!=null) {
				double avg = source.getProductReviewAvg().doubleValue();
				double rating = Math.round(avg * 2) / 2.0f;
				target.setRating(rating);
			}
			target.setProductVirtual(source.getProductVirtual());
			if(source.getProductReviewCount()!=null) {
				target.setRatingCount(source.getProductReviewCount().intValue());
			}
			if(description!=null) {
				com.salesmanager.catalog.presentation.model.product.ProductDescription tragetDescription = new com.salesmanager.catalog.presentation.model.product.ProductDescription();
				tragetDescription.setFriendlyUrl(description.getSeUrl());
				tragetDescription.setName(description.getName());
				tragetDescription.setId(description.getId());
				if(!StringUtils.isBlank(description.getMetatagTitle())) {
					tragetDescription.setTitle(description.getMetatagTitle());
				} else {
					tragetDescription.setTitle(description.getName());
				}
				tragetDescription.setMetaDescription(description.getMetatagDescription());
				tragetDescription.setDescription(description.getDescription());
				tragetDescription.setHighlights(description.getProductHighlight());
				tragetDescription.setLanguage(description.getLanguage().getCode());
				target.setDescription(tragetDescription);
				
			}
			
			if(source.getManufacturer()!=null) {
				ManufacturerDescription manufacturer = source.getManufacturer().getDescriptions().iterator().next(); 
				ReadableManufacturer manufacturerEntity = new ReadableManufacturer();
				com.salesmanager.catalog.presentation.model.manufacturer.ManufacturerDescription d = new com.salesmanager.catalog.presentation.model.manufacturer.ManufacturerDescription();
				d.setName(manufacturer.getName());
				manufacturerEntity.setDescription(d);
				manufacturerEntity.setId(source.getManufacturer().getId());
				manufacturerEntity.setOrder(source.getManufacturer().getOrder());
				manufacturerEntity.setCode(source.getManufacturer().getCode());
				target.setManufacturer(manufacturerEntity);
			}
			
			Set<ProductImage> images = source.getImages();
			if(images!=null && images.size()>0) {
				List<ReadableImage> imageList = new ArrayList<ReadableImage>();
				
				String contextPath = imageUtils.getContextPath();
				
				for(ProductImage img : images) {
					ReadableImage prdImage = new ReadableImage();
					prdImage.setImageName(img.getProductImage());
					prdImage.setDefaultImage(img.isDefaultImage());

					StringBuilder imgPath = new StringBuilder();
					imgPath.append(contextPath != null ? contextPath : "").append(imageFilePathApi.buildProductImageUtils(store.toDTO(), source.getSku(), img.getProductImage()));

					prdImage.setImageUrl(imgPath.toString());
					prdImage.setId(img.getId());
					prdImage.setImageType(img.getImageType());
					if(img.getProductImageUrl()!=null){
						prdImage.setExternalUrl(img.getProductImageUrl());
					}
					if(img.getImageType()==1 && img.getProductImageUrl()!=null) {//video
						prdImage.setVideoUrl(img.getProductImageUrl());
					}
					
					if(prdImage.isDefaultImage()) {
						target.setImage(prdImage);
					}
					
					imageList.add(prdImage);
				}
				target
				.setImages(imageList);
			}
			
			if(!CollectionUtils.isEmpty(source.getCategories())) {
				
				ReadableCategoryPopulator categoryPopulator = new ReadableCategoryPopulator();
				List<ReadableCategory> categoryList = new ArrayList<ReadableCategory>();
				
				for(Category category : source.getCategories()) {
					
					ReadableCategory readableCategory = new ReadableCategory();
					categoryPopulator.populate(category, readableCategory, store, language);
					categoryList.add(readableCategory);
					
				}
				
				target.setCategories(categoryList);
				
			}
			

			
			//remove products from invisible category -> set visible = false
/*			Set<Category> categories = source.getCategories();
			boolean isVisible = true;
			if(!CollectionUtils.isEmpty(categories)) {
				for(Category c : categories) {
					if(c.isVisible()) {
						isVisible = true;
						break;
					} else {
						isVisible = false;
					}
				}
			}*/
			
			//target.setVisible(isVisible);
			
	
			target.setSku(source.getSku());
	
			FinalPrice price = pricingService.calculateProductPrice(source);

			target.setFinalPrice(productPriceApi.getStoreFormattedAmountWithCurrency(store.toDTO(), price.getFinalPrice()));
			target.setPrice(price.getFinalPrice());
			target.setOriginalPrice(productPriceApi.getStoreFormattedAmountWithCurrency(store.toDTO(), price.getOriginalPrice()));
	
			if(price.isDiscounted()) {
				target.setDiscounted(true);
			}
			
			//availability
			for(ProductAvailability availability : source.getAvailabilities()) {
				if(availability.getRegion().equals(Constants.ALL_REGIONS)) {//TODO REL 2.1 accept a region
					target.setQuantity(availability.getProductQuantity());
					target.setQuantityOrderMaximum(availability.getProductQuantityOrderMax());
					target.setQuantityOrderMinimum(availability.getProductQuantityOrderMin());
					if(availability.getProductQuantity().intValue() > 0 && target.isAvailable()) {
							target.setCanBePurchased(true);
					}
				}
			}
			
			
			return target;
		
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}




	@Override
	protected ReadableProduct createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
