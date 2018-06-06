package com.salesmanager.catalog.presentation.model.product;

import java.io.Serializable;

import com.salesmanager.catalog.presentation.model.customer.ReadableCustomerInfo;


public class ReadableProductReview extends ProductReviewEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReadableCustomerInfo customer;
	public ReadableCustomerInfo getCustomer() {
		return customer;
	}
	public void setCustomer(ReadableCustomerInfo customer) {
		this.customer = customer;
	}

}
