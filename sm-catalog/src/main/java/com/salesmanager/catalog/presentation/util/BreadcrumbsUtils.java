package com.salesmanager.catalog.presentation.util;

import com.salesmanager.catalog.business.service.category.CategoryService;
import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.presentation.util.LabelUtils;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.common.presentation.constants.Constants;
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
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;


@Component
public class BreadcrumbsUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(BreadcrumbsUtils.class);

	@Autowired
	private UriUtils uriUtils;

	@Inject
	private CategoryService categoryService;

	@Inject
	private LabelUtils messages;
	
	public Breadcrumb buildCategoryBreadcrumb(ReadableCategory categoryClicked, MerchantStoreInfo store, LanguageInfo language, String contextPath) throws Exception {
		
		/** Rebuild breadcrumb **/
		BreadcrumbItem home = new BreadcrumbItem();
		home.setItemType(BreadcrumbItemType.HOME);
		home.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, new Locale(language.getCode())));
		home.setUrl(uriUtils.getStoreUri(store, contextPath) + Constants.SHOP_URI);

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
	
	
	public Breadcrumb buildProductBreadcrumb(String refContent, ReadableProduct productClicked, MerchantStoreInfo store, LanguageInfo language, String contextPath) throws Exception {
		
		/** Rebuild breadcrumb **/
		BreadcrumbItem home = new BreadcrumbItem();
		home.setItemType(BreadcrumbItemType.HOME);
		home.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, new Locale(language.getCode())));
		home.setUrl(uriUtils.getStoreUri(store, contextPath) + Constants.SHOP_URI);

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

	private String buildCategoryUrl(MerchantStoreInfo store, String contextPath, String url) {
		StringBuilder resourcePath = new StringBuilder();
		resourcePath.append(uriUtils.getStoreUri(store, contextPath))

				.append(Constants.SHOP_URI)

				.append(Constants.CATEGORY_URI)
				.append(Constants.SLASH)
				.append(url)
				.append(Constants.URL_EXTENSION);

		return resourcePath.toString();

	}

	private String buildProductUrl(MerchantStoreInfo store, String contextPath, String url) {
		StringBuilder resourcePath = new StringBuilder();
		resourcePath.append(uriUtils.getStoreUri(store, contextPath))
				.append(Constants.SHOP_URI)
				.append(Constants.PRODUCT_URI)
				.append(Constants.SLASH)
				.append(url)
				.append(Constants.URL_EXTENSION);

		return resourcePath.toString();

	}

}
