package com.salesmanager.catalog.presentation.controller.product.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.salesmanager.catalog.business.integration.core.service.CustomerInfoService;
import com.salesmanager.catalog.business.integration.core.service.LanguageInfoService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.catalog.business.service.category.CategoryService;
import com.salesmanager.catalog.business.service.product.PricingService;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.catalog.business.service.product.attribute.ProductOptionService;
import com.salesmanager.catalog.business.service.product.attribute.ProductOptionValueService;
import com.salesmanager.catalog.business.service.product.manufacturer.ManufacturerService;
import com.salesmanager.catalog.business.service.product.review.ProductReviewService;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.ProductCriteria;
import com.salesmanager.catalog.model.product.availability.ProductAvailability;
import com.salesmanager.catalog.model.product.manufacturer.Manufacturer;
import com.salesmanager.catalog.model.product.price.ProductPrice;
import com.salesmanager.catalog.model.product.review.ProductReview;
import com.salesmanager.catalog.presentation.model.manufacturer.PersistableManufacturer;
import com.salesmanager.catalog.presentation.model.manufacturer.ReadableManufacturer;
import com.salesmanager.catalog.presentation.model.product.PersistableProduct;
import com.salesmanager.catalog.presentation.model.product.PersistableProductReview;
import com.salesmanager.catalog.presentation.model.product.ProductPriceEntity;
import com.salesmanager.catalog.presentation.model.product.ReadableProduct;
import com.salesmanager.catalog.presentation.model.product.ReadableProductList;
import com.salesmanager.catalog.presentation.model.product.ReadableProductReview;
import com.salesmanager.catalog.presentation.populator.catalog.PersistableProductPopulator;
import com.salesmanager.catalog.presentation.populator.catalog.PersistableProductReviewPopulator;
import com.salesmanager.catalog.presentation.populator.catalog.ReadableProductPopulator;
import com.salesmanager.catalog.presentation.populator.catalog.ReadableProductReviewPopulator;
import com.salesmanager.catalog.presentation.populator.manufacturer.PersistableManufacturerPopulator;
import com.salesmanager.catalog.presentation.populator.manufacturer.ReadableManufacturerPopulator;
import com.salesmanager.common.presentation.util.DateUtil;
import com.salesmanager.catalog.presentation.util.CatalogImageFilePathUtils;

@Service("productFacade")
public class ProductFacadeImpl implements ProductFacade {
	
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private ManufacturerService manufacturerService;
	
	@Inject
	private ProductOptionService productOptionService;
	
	@Inject
	private ProductOptionValueService productOptionValueService;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private PricingService pricingService;
	
	@Inject
	private ProductReviewService productReviewService;

	@Autowired
	private CatalogImageFilePathUtils imageUtils;

	@Autowired
	private LanguageInfoService languageInfoService;

	@Autowired
	private CustomerInfoService customerInfoService;

	@Override
	public PersistableProduct saveProduct(MerchantStoreInfo store, PersistableProduct product, LanguageInfo language)
			throws Exception {
		

		com.salesmanager.catalog.presentation.model.manufacturer.Manufacturer manufacturer = product.getManufacturer();
		
		if(manufacturer == null || (manufacturer.getId()==null || manufacturer.getId().longValue()==0)
				&& StringUtils.isBlank(manufacturer.getCode())) {
			
			//get default manufacturer
			Manufacturer defaultManufacturer = manufacturerService.getByCode(store, "DEFAULT");
			
			if(defaultManufacturer != null) {
			
				com.salesmanager.catalog.presentation.model.manufacturer.Manufacturer m = new com.salesmanager.catalog.presentation.model.manufacturer.Manufacturer();
				m.setId(defaultManufacturer.getId());
				m.setCode(defaultManufacturer.getCode());
				product.setManufacturer(m);
			
			}
			
		}
		
		PersistableProductPopulator persistableProductPopulator = new PersistableProductPopulator();
		
		persistableProductPopulator.setCategoryService(categoryService);
		persistableProductPopulator.setManufacturerService(manufacturerService);
		persistableProductPopulator.setLanguageInfoService(languageInfoService);
		persistableProductPopulator.setProductOptionService(productOptionService);
		persistableProductPopulator.setProductOptionValueService(productOptionValueService);
		persistableProductPopulator.setCustomerInfoService(customerInfoService);
		
		Product target = null;
		if(product.getId() != null && product.getId().longValue() > 0) {
			target = productService.getById(product.getId());
		} else {
			target = new Product();
		}
		 
		
		persistableProductPopulator.populate(product, target, store, language);
		
		productService.create(target);
		
		product.setId(target.getId());

		return product;
		

	}

	@Override
	public ReadableProduct getProduct(MerchantStoreInfo store, Long id, LanguageInfo language)
			throws Exception {

		Product product = productService.getById(id);
		
		if(product==null) {
			return null;
		}
		
		ReadableProduct readableProduct = new ReadableProduct();
		
		ReadableProductPopulator populator = new ReadableProductPopulator();
		
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(product, readableProduct, store, language);
		
		return readableProduct;
	}

	@Override
	public ReadableProduct getProduct(MerchantStoreInfo store, String sku,
			LanguageInfo language) throws Exception {
		
		Product product = productService.getByCode(sku, language);
		
		if(product==null) {
			return null;
		}
		
		ReadableProduct readableProduct = new ReadableProduct();
		
		ReadableProductPopulator populator = new ReadableProductPopulator();
		
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(product, readableProduct, store, language);
		
		return readableProduct;
	}

	@Override
	public ReadableProduct updateProductPrice(ReadableProduct product,
			ProductPriceEntity price, LanguageInfo language) throws Exception {
		
		
		Product persistable = productService.getById(product.getId());
		
		if(persistable==null) {
			throw new Exception("product is null for id " + product.getId());
		}
		
		java.util.Set<ProductAvailability> availabilities = persistable.getAvailabilities();
		for(ProductAvailability availability : availabilities) {
			ProductPrice productPrice = availability.defaultPrice();
			productPrice.setProductPriceAmount(price.getOriginalPrice());
			if(price.isDiscounted()) {
				productPrice.setProductPriceSpecialAmount(price.getDiscountedPrice());
				if(!StringUtils.isBlank(price.getDiscountStartDate())) {
					Date startDate = DateUtil.getDate(price.getDiscountStartDate());
					productPrice.setProductPriceSpecialStartDate(startDate);
				}
				if(!StringUtils.isBlank(price.getDiscountEndDate())) {
					Date endDate = DateUtil.getDate(price.getDiscountEndDate());
					productPrice.setProductPriceSpecialEndDate(endDate);
				}
			}
			
		}
		
		productService.update(persistable);
		
		ReadableProduct readableProduct = new ReadableProduct();
		
		ReadableProductPopulator populator = new ReadableProductPopulator();
		
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(persistable, readableProduct, persistable.getMerchantStore(), language);
		
		return readableProduct;
	}

	@Override
	public ReadableProduct updateProductQuantity(ReadableProduct product,
			int quantity, LanguageInfo language) throws Exception {
		Product persistable = productService.getById(product.getId());
		
		if(persistable==null) {
			throw new Exception("product is null for id " + product.getId());
		}
		
		java.util.Set<ProductAvailability> availabilities = persistable.getAvailabilities();
		for(ProductAvailability availability : availabilities) {
			availability.setProductQuantity(quantity);
		}
		
		productService.update(persistable);
		
		ReadableProduct readableProduct = new ReadableProduct();
		
		ReadableProductPopulator populator = new ReadableProductPopulator();
		
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(persistable, readableProduct, persistable.getMerchantStore(), language);
		
		return readableProduct;
	}

	@Override
	public void deleteProduct(Product product) throws Exception {
		productService.delete(product);
		
	}

	@Override
	public ReadableProductList getProductListsByCriterias(MerchantStoreInfo store, LanguageInfo language,
			ProductCriteria criterias) throws Exception {

		
		Validate.notNull(criterias, "ProductCriteria must be set for this product");
		
		if(CollectionUtils.isNotEmpty(criterias.getCategoryIds())) {
			
			
			if(criterias.getCategoryIds().size()==1) {
				
				com.salesmanager.catalog.model.category.Category category = categoryService.getById(criterias.getCategoryIds().get(0));
				
				if(category != null) {
					String lineage = new StringBuilder().append(category.getLineage()).append(category.getId()).append("/").toString();
					
					List<com.salesmanager.catalog.model.category.Category> categories = categoryService.listByLineage(store, lineage);
				
					List<Long> ids = new ArrayList<Long>();
					if(categories!=null && categories.size()>0) {
						for(com.salesmanager.catalog.model.category.Category c : categories) {
							ids.add(c.getId());
						}
					} 
					ids.add(category.getId());
					criterias.setCategoryIds(ids);
				}
			}

			
		}
		
		com.salesmanager.catalog.model.product.ProductList products = productService.listByStore(store, language, criterias);

		
		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		
		
		ReadableProductList productList = new ReadableProductList();
		for(Product product : products.getProducts()) {

			//create new proxy product
			ReadableProduct readProduct = populator.populate(product, new ReadableProduct(), store, language);
			productList.getProducts().add(readProduct);
			
		}
		
		productList.setTotalCount(products.getTotalCount());
		
		
		return productList;
	}

	@Override
	public ReadableProduct addProductToCategory(Category category, Product product, LanguageInfo language) throws Exception {
		
		Validate.notNull(category,"Category cannot be null");
		Validate.notNull(product,"Product cannot be null");
		
		product.getCategories().add(category);
		
		productService.update(product);
		
		ReadableProduct readableProduct = new ReadableProduct();
		
		ReadableProductPopulator populator = new ReadableProductPopulator();
		
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(product, readableProduct, product.getMerchantStore(), language);
		
		return readableProduct;
		
		
	}

	@Override
	public ReadableProduct removeProductFromCategory(Category category, Product product, LanguageInfo language)
			throws Exception {
		
		Validate.notNull(category,"Category cannot be null");
		Validate.notNull(product,"Product cannot be null");
		
		product.getCategories().remove(category);
		productService.update(product);	
		
		ReadableProduct readableProduct = new ReadableProduct();
		
		ReadableProductPopulator populator = new ReadableProductPopulator();
		
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(product, readableProduct, product.getMerchantStore(), language);
		
		return readableProduct;
	}

	@Override
	public ReadableProduct getProductByCode(MerchantStoreInfo store, String uniqueCode, LanguageInfo language)
			throws Exception {
		
		Product product = productService.getByCode(uniqueCode, language);
		
		
		ReadableProduct readableProduct = new ReadableProduct();
		
		ReadableProductPopulator populator = new ReadableProductPopulator();
		
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);
		populator.populate(product, readableProduct, product.getMerchantStore(), language);
		
		return readableProduct;
	}

	@Override
	public void saveOrUpdateReview(PersistableProductReview review, MerchantStoreInfo store, LanguageInfo language) throws Exception {
		PersistableProductReviewPopulator populator = new PersistableProductReviewPopulator();
		populator.setLanguageInfoService(languageInfoService);
		populator.setCustomerInfoService(customerInfoService);
		populator.setProductService(productService);
		
		com.salesmanager.catalog.model.product.review.ProductReview rev = new com.salesmanager.catalog.model.product.review.ProductReview();
		populator.populate(review, rev, store, language);
	
		if(review.getId()==null) {
			productReviewService.create(rev);
		} else {
			productReviewService.update(rev);
		}

		
		review.setId(rev.getId());

	}

	@Override
	public void deleteReview(ProductReview review, MerchantStoreInfo store, LanguageInfo language) throws Exception {
		productReviewService.delete(review);
		
	}

	@Override
	public List<ReadableProductReview> getProductReviews(Product product, MerchantStoreInfo store, LanguageInfo language)
			throws Exception {
		
		
		List<ProductReview> reviews = productReviewService.getByProduct(product);
		
		ReadableProductReviewPopulator populator = new ReadableProductReviewPopulator();
		
		List<ReadableProductReview> productReviews = new ArrayList<ReadableProductReview>();
		
		for(ProductReview review : reviews) {
			ReadableProductReview readableReview = new ReadableProductReview();
			populator.populate(review, readableReview, store, language);
			productReviews.add(readableReview);
		}
		
		
		
		return productReviews;
	}

	@Override
	public void saveOrUpdateManufacturer(PersistableManufacturer manufacturer, MerchantStoreInfo store, LanguageInfo language)
			throws Exception {
		
		PersistableManufacturerPopulator populator = new PersistableManufacturerPopulator();
		populator.setLanguageInfoService(languageInfoService);

		
		Manufacturer manuf = new Manufacturer();
		populator.populate(manufacturer, manuf, store,  language);
		
		manufacturerService.saveOrUpdate(manuf);
		
		manufacturer.setId(manuf.getId());
		
	}

	@Override
	public void deleteManufacturer(Manufacturer manufacturer, MerchantStoreInfo store, LanguageInfo language) throws Exception {
		manufacturerService.delete(manufacturer);
		
	}

	@Override
	public ReadableManufacturer getManufacturer(Long id, MerchantStoreInfo store, LanguageInfo language) throws Exception {
		Manufacturer manufacturer = manufacturerService.getById(id);
		
		if(manufacturer==null) {
			return null;
		}
		
		ReadableManufacturer readableManufacturer = new ReadableManufacturer();
		
		ReadableManufacturerPopulator populator = new ReadableManufacturerPopulator();
		populator.populate(manufacturer, readableManufacturer, store, language);

		
		return readableManufacturer;
	}

	@Override
	public List<ReadableManufacturer> getAllManufacturers(MerchantStoreInfo store, LanguageInfo language) throws Exception {
		
		
		List<Manufacturer> manufacturers = manufacturerService.listByStore(store);
		ReadableManufacturerPopulator populator = new ReadableManufacturerPopulator();
		List<ReadableManufacturer> returnList = new ArrayList<ReadableManufacturer>();
		
		for(Manufacturer m : manufacturers) {
			
			ReadableManufacturer readableManufacturer = new ReadableManufacturer();
			populator.populate(m, readableManufacturer, store, language);
			returnList.add(readableManufacturer);
		}

		return returnList;
	}

}
