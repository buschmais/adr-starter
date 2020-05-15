package com.salesmanager.catalog.presentation.model.product.attribute;

import java.io.Serializable;
import java.util.List;

public class PersistableProductOptionValue extends ProductOptionValueEntity
		implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ProductOptionValueDescription> descriptions;

	public void setDescriptions(List<ProductOptionValueDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public List<ProductOptionValueDescription> getDescriptions() {
		return descriptions;
	}

}
