package com.salesmanager.core.business.services.user;

import java.util.List;
import java.util.Set;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.core.model.user.Group;
import com.salesmanager.core.model.user.GroupType;


public interface GroupService extends SalesManagerEntityService<Integer, Group> {


	List<Group> listGroup(GroupType groupType) throws ServiceException;
	List<Group> listGroupByIds(Set<Integer> ids) throws ServiceException;
	Group findByName(String groupName) throws ServiceException;

}
