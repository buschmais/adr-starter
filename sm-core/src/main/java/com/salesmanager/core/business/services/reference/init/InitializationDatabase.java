package com.salesmanager.core.business.services.reference.init;

import com.salesmanager.common.business.exception.ServiceException;

public interface InitializationDatabase {
	
	boolean isEmpty();
	
	void populate(String name) throws ServiceException;

}
