package com.salesmanager.catalog.presentation.controller.search.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.salesmanager.catalog.business.integration.core.service.MerchantStoreInfoService;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.salesmanager.catalog.business.service.category.CategoryService;
import com.salesmanager.catalog.business.service.product.PricingService;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.catalog.business.service.search.SearchService;
import com.salesmanager.core.business.utils.CoreConfiguration;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.ProductCriteria;
import com.salesmanager.catalog.model.product.ProductList;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.catalog.model.search.IndexProduct;
import com.salesmanager.catalog.model.search.SearchEntry;
import com.salesmanager.catalog.model.search.SearchFacet;
import com.salesmanager.catalog.model.search.SearchResponse;
import com.salesmanager.catalog.presentation.model.SearchProductList;
import com.salesmanager.catalog.presentation.model.SearchProductRequest;
import com.salesmanager.catalog.presentation.model.category.ReadableCategory;
import com.salesmanager.catalog.presentation.model.product.ReadableProduct;
import com.salesmanager.catalog.presentation.populator.catalog.ReadableCategoryPopulator;
import com.salesmanager.catalog.presentation.populator.catalog.ReadableProductPopulator;
import com.salesmanager.catalog.presentation.util.CatalogImageFilePathUtils;


@Service("searchFacade")
public class SearchFacadeImpl implements SearchFacade {
	
	@Inject
	private SearchService searchService;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private PricingService pricingService;
	
	@Autowired
	private CatalogImageFilePathUtils imageUtils;
	
	@Inject
	private CoreConfiguration coreConfiguration;

	@Autowired
	private MerchantStoreInfoService merchantStoreInfoService;

    
	private final static String CATEGORY_FACET_NAME = "categories";
	private final static String MANUFACTURER_FACET_NAME = "manufacturer";

	/**
	 * Index all products from the catalogue
	 * Better stop the system, remove ES indexex manually
	 * restart ES and run this query
	 */
	@Override
	@Async
	public void indexAllData(MerchantStoreInfo store) throws Exception {
		
		
		List<Product> products = productService.listByStore(store);
		
		for(Product product : products) {
			searchService.index(store, product);
		}
		
	}

	@Override
	public SearchProductList search(MerchantStoreInfo store, Language language, SearchProductRequest searchRequest) throws Exception {

		String query = String.format(coreConfiguration.getProperty("SEARCH_QUERY"), searchRequest.getQuery());
		SearchResponse response =  searchService.search(store, language.getCode(), query, searchRequest.getCount(), searchRequest.getStart());
		return this.copySearchResponse(response, store, searchRequest.getStart(), searchRequest.getCount(), language);
	}

	@Override
	public SearchProductList copySearchResponse(SearchResponse searchResponse, MerchantStoreInfo merchantStore, int start, int count, Language language) throws Exception {
		
		SearchProductList returnList = new SearchProductList();
		List<SearchEntry> entries = searchResponse.getEntries();
		
		if(!CollectionUtils.isEmpty(entries)) {
			List<Long> ids = new ArrayList<Long>();
			for(SearchEntry entry : entries) {
				IndexProduct indexedProduct = entry.getIndexProduct();
				Long id = Long.parseLong(indexedProduct.getId());
				
				//No highlights	
				ids.add(id);
			}
			
			ProductCriteria searchCriteria = new ProductCriteria();
			searchCriteria.setMaxCount(count);
			searchCriteria.setStartIndex(start);
			searchCriteria.setProductIds(ids);
			searchCriteria.setAvailable(true);
			
			ProductList productList = productService.listByStore(merchantStore, language, searchCriteria);
			
			ReadableProductPopulator populator = new ReadableProductPopulator();
			populator.setPricingService(pricingService);
			populator.setimageUtils(imageUtils);
			
			for(Product product : productList.getProducts()) {
				//create new proxy product
				ReadableProduct p = populator.populate(product, new ReadableProduct(), merchantStore, language);
				
				returnList.getProducts().add(p);
	
			}
			returnList.setProductCount(productList.getProducts().size());
		}
		
		//Facets
		Map<String,List<SearchFacet>> facets = searchResponse.getFacets();
		List<SearchFacet> categoriesFacets = null;
		List<SearchFacet> manufacturersFacets = null;
		if(facets!=null) {
			for(String key : facets.keySet()) {
				//supports category and manufacturer
				if(CATEGORY_FACET_NAME.equals(key)) {
					categoriesFacets = facets.get(key);
				}
				
				if(MANUFACTURER_FACET_NAME.equals(key)) {
					manufacturersFacets = facets.get(key);
				}
			}
			
			
			if(categoriesFacets!=null) {
				List<String> categoryCodes = new ArrayList<String>();
				Map<String,Long> productCategoryCount = new HashMap<String,Long>();
				for(SearchFacet facet : categoriesFacets) {
					categoryCodes.add(facet.getName());
					productCategoryCount.put(facet.getKey(), facet.getCount());
				}
				
				List<Category> categories = categoryService.listByCodes(merchantStore, categoryCodes, language);
				List<ReadableCategory> categoryProxies = new ArrayList<ReadableCategory>();
				ReadableCategoryPopulator populator = new ReadableCategoryPopulator();
				
				for(Category category : categories) {
					ReadableCategory categoryProxy = populator.populate(category, new ReadableCategory(), merchantStore, language);
					Long total = productCategoryCount.get(categoryProxy.getCode());
					if(total!=null) {
						categoryProxy.setProductCount(total.intValue());
					}
					categoryProxies.add(categoryProxy);
				}
				returnList.setCategoryFacets(categoryProxies);
			}
			
			//todo manufacturer facets
			if(manufacturersFacets!=null) {
				
			}
			
			
		}
		
		return returnList;
	}
	

}
