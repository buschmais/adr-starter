/**
 * 
 */
package com.salesmanager.catalog.business.cms.product;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.catalog.model.content.FileContentType;


/**
 * @author Umesh Awasthi
 *
 */
public interface FileRemove
{
    public void removeFile(String merchantStoreCode, FileContentType staticContentType, String fileName) throws ServiceException;
    public void removeFiles(String merchantStoreCode) throws ServiceException;

}
