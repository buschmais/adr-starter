package com.salesmanager.core.business.modules.cms.common;

import com.salesmanager.common.business.exception.ServiceException;


public interface ImageRemove {
	
	
	public void removeImages(final String merchantStoreCode) throws ServiceException;
	
}
