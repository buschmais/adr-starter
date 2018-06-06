package com.salesmanager.catalog.presentation.model.product.attribute;

import java.io.Serializable;

import com.salesmanager.common.presentation.model.Entity;


public class ProductOptionValue extends Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
