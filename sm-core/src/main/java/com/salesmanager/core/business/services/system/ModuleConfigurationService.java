package com.salesmanager.core.business.services.system;

import java.util.List;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.core.model.system.IntegrationModule;

public interface ModuleConfigurationService extends
		SalesManagerEntityService<Long, IntegrationModule> {

	List<IntegrationModule> getIntegrationModules(String module);

	IntegrationModule getByCode(String moduleCode);
	
	void createOrUpdateModule(String json) throws ServiceException;
	


}
