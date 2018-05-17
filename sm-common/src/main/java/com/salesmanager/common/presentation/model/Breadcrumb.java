package com.salesmanager.common.presentation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Breadcrumb implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BreadcrumbItemType itemType;
	private String languageCode;
	private String urlRefContent = null;
	private List<BreadcrumbItem> breadCrumbs = new ArrayList<BreadcrumbItem>();
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguage(String languageCode) {
		this.languageCode = languageCode;
	}
	public List<BreadcrumbItem> getBreadCrumbs() {
		return breadCrumbs;
	}
	public void setBreadCrumbs(List<BreadcrumbItem> breadCrumbs) {
		this.breadCrumbs = breadCrumbs;
	}
	public void setItemType(BreadcrumbItemType itemType) {
		this.itemType = itemType;
	}
	public BreadcrumbItemType getItemType() {
		return itemType;
	}
	public String getUrlRefContent() {
		return urlRefContent;
	}
	public void setUrlRefContent(String urlRefContent) {
		this.urlRefContent = urlRefContent;
	}

}
