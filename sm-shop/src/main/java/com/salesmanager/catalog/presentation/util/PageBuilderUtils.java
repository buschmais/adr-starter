package com.salesmanager.catalog.presentation.util;

import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.shop.constants.Constants;

public class PageBuilderUtils {
	
	public static String build404(MerchantStoreInfo store) {
		return new StringBuilder().append("404").append(".").append(store.getStoreTemplate()).toString();
	}
	
	public static String buildHomePage(MerchantStoreInfo store) {
		return "redirect:" + Constants.SHOP_URI;
	}

}
