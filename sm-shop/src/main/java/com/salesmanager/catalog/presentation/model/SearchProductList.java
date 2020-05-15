package com.salesmanager.catalog.presentation.model;

import java.util.ArrayList;
import java.util.List;

import com.salesmanager.catalog.presentation.model.category.ReadableCategory;


/**
 * Object representing the results of a search query
 * @author Carl Samson
 *
 */
public class SearchProductList extends ProductList {
	

	private static final long serialVersionUID = 1L;
	private List<ReadableCategory> categoryFacets = new ArrayList<ReadableCategory>();
	public List<ReadableCategory> getCategoryFacets() {
		return categoryFacets;
	}
	public void setCategoryFacets(List<ReadableCategory> categoryFacets) {
		this.categoryFacets = categoryFacets;
	}


}
