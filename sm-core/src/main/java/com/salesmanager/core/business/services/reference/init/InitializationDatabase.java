package com.salesmanager.core.business.services.reference.init;

import com.salesmanager.common.business.exception.ServiceException;

public interface InitializationDatabase {
	
	boolean isEmpty();
	
	void initCore(String name) throws ServiceException;

	void initCatalog(String name) throws ServiceException;


}
