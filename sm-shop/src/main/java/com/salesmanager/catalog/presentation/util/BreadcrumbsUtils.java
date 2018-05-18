package com.salesmanager.catalog.presentation.util;

import com.salesmanager.catalog.business.service.category.CategoryService;
import com.salesmanager.common.presentation.util.LabelUtils;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.catalog.presentation.model.category.ReadableCategory;
import com.salesmanager.catalog.presentation.model.product.ReadableProduct;
import com.salesmanager.common.presentation.model.Breadcrumb;
import com.salesmanager.common.presentation.model.BreadcrumbItem;
import com.salesmanager.common.presentation.model.BreadcrumbItemType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.*;


@Component
public class BreadcrumbsUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(BreadcrumbsUtils.class);


	@Inject
	private LabelUtils messages;
	
	@Inject
	private CategoryService categoryService;

	@Value("#{catalogEhCacheManager.getCache('catalogCache')}")
	private Cache catalogCache;

	@Autowired
	@Qualifier("coreRestTemplate")
	private RestTemplate coreRestTemplate;

	private static final String STORE_URI_KEY = "STORE_URI";
	
	public Breadcrumb buildCategoryBreadcrumb(ReadableCategory categoryClicked, MerchantStore store, Language language, String contextPath) throws Exception {
		
		/** Rebuild breadcrumb **/
		BreadcrumbItem home = new BreadcrumbItem();
		home.setItemType(BreadcrumbItemType.HOME);
		home.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, new Locale(language.getCode())));
		home.setUrl(getStoreUri(store, contextPath));

		Breadcrumb breadCrumb = new Breadcrumb();
		breadCrumb.setLanguage(language.getCode());
		
		List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
		items.add(home);
		
		//if(!StringUtils.isBlank(refContent)) {

			//List<String> categoryIds = parseBreadCrumb(refContent);
			List<String> categoryIds = parseCategoryLineage(categoryClicked.getLineage());
			List<Long> ids = new ArrayList<Long>();
			for(String c : categoryIds) {
				ids.add(Long.parseLong(c));
			}
			
			ids.add(categoryClicked.getId());
			
			
			List<Category> categories = categoryService.listByIds(store, ids, language);
			
			//category path - use lineage
			for(Category c : categories) {
				BreadcrumbItem categoryBreadcrump = new BreadcrumbItem();
				categoryBreadcrump.setItemType(BreadcrumbItemType.CATEGORY);
				categoryBreadcrump.setLabel(c.getDescription().getName());
				categoryBreadcrump.setUrl(buildCategoryUrl(store, contextPath, c.getDescription().getSeUrl()));
				items.add(categoryBreadcrump);
			}
			
			breadCrumb.setUrlRefContent(buildBreadCrumb(ids));
			
		//}
		


		breadCrumb.setBreadCrumbs(items);
		breadCrumb.setItemType(BreadcrumbItemType.CATEGORY);
		
		
		return breadCrumb;
	}
	
	
	public Breadcrumb buildProductBreadcrumb(String refContent, ReadableProduct productClicked, MerchantStore store, Language language, String contextPath) throws Exception {
		
		/** Rebuild breadcrumb **/
		BreadcrumbItem home = new BreadcrumbItem();
		home.setItemType(BreadcrumbItemType.HOME);
		home.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, new Locale(language.getCode())));
		home.setUrl(getStoreUri(store, contextPath));

		Breadcrumb breadCrumb = new Breadcrumb();
		breadCrumb.setLanguage(language.getCode());
		
		List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
		items.add(home);
		
		if(!StringUtils.isBlank(refContent)) {

			List<String> categoryIds = parseBreadCrumb(refContent);
			List<Long> ids = new ArrayList<Long>();
			for(String c : categoryIds) {
				ids.add(Long.parseLong(c));
			}
			
			
			List<Category> categories = categoryService.listByIds(store, ids, language);
			
			//category path - use lineage
			for(Category c : categories) {
				BreadcrumbItem categoryBreadcrump = new BreadcrumbItem();
				categoryBreadcrump.setItemType(BreadcrumbItemType.CATEGORY);
				categoryBreadcrump.setLabel(c.getDescription().getName());
				categoryBreadcrump.setUrl(buildCategoryUrl(store, contextPath, c.getDescription().getSeUrl()));
				items.add(categoryBreadcrump);
			}
			

			breadCrumb.setUrlRefContent(buildBreadCrumb(ids));
		} 
		
		BreadcrumbItem productBreadcrump = new BreadcrumbItem();
		productBreadcrump.setItemType(BreadcrumbItemType.PRODUCT);
		productBreadcrump.setLabel(productClicked.getDescription().getName());
		productBreadcrump.setUrl(buildProductUrl(store, contextPath, productClicked.getDescription().getFriendlyUrl()));
		items.add(productBreadcrump);
		
		
		


		breadCrumb.setBreadCrumbs(items);
		breadCrumb.setItemType(BreadcrumbItemType.CATEGORY);
		
		
		return breadCrumb;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private List<String> parseBreadCrumb(String refContent) throws Exception {
		
		/** c:1,2,3 **/
		String[] categoryComa = refContent.split(":");
		String[] categoryIds = categoryComa[1].split(",");
		return new LinkedList(Arrays.asList(categoryIds));
		
		
	}
	

	private List<String> parseCategoryLineage(String lineage) throws Exception {
		
		String[] categoryPath = lineage.split(Constants.CATEGORY_LINEAGE_DELIMITER);
		List<String> returnList = new LinkedList<String>();
		for(String c : categoryPath) {
			if(!StringUtils.isBlank(c)) {
				returnList.add(c);
			}
		}
		return returnList;

	}
	
	private String buildBreadCrumb(List<Long> ids) throws Exception {
		
		if(CollectionUtils.isEmpty(ids)) {
			return null;
		}
			StringBuilder sb = new StringBuilder();
			sb.append("c:");
			int count = 1;
			for(Long c : ids) {
				sb.append(c);
				if(count < ids.size()) {
					sb.append(",");
				}
				count++;
			}
		
		
		return sb.toString();
		
	}

	private String buildCategoryUrl(MerchantStore store, String contextPath, String url) {
		StringBuilder resourcePath = new StringBuilder();
		resourcePath.append(getStoreUri(store, contextPath))

				.append(Constants.SHOP_URI)

				.append(Constants.CATEGORY_URI)
				.append(Constants.SLASH)
				.append(url)
				.append(Constants.URL_EXTENSION);

		return resourcePath.toString();

	}

	private String buildProductUrl(MerchantStore store, String contextPath, String url) {
		StringBuilder resourcePath = new StringBuilder();
		resourcePath.append(getStoreUri(store, contextPath))
				.append(Constants.SHOP_URI)
				.append(Constants.PRODUCT_URI)
				.append(Constants.SLASH)
				.append(url)
				.append(Constants.URL_EXTENSION);

		return resourcePath.toString();

	}

	private String getStoreUri(MerchantStore store, String contextPath) {
		MultiValueMap<String, Object> parameter = new LinkedMultiValueMap<>();
		parameter.add("store", store.toDTO());
		parameter.add("contextPath", contextPath);

		if (catalogCache.get(STORE_URI_KEY) == null) {
			String shopUrl = coreRestTemplate.postForObject("/store/shop/uri", parameter, String.class);
			catalogCache.put(STORE_URI_KEY, shopUrl);
		}
		return (String) catalogCache.get(STORE_URI_KEY).get();
	}

}
