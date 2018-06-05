/**
 * 
 */
package com.salesmanager.catalog.business.cms.product;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.catalog.model.content.InputContentFile;

import java.util.List;


/**
 * @author Umesh Awasthi
 *
 */
public interface FilePut
{
    public void addFile(final String merchantStoreCode, InputContentFile inputStaticContentData) throws ServiceException;
    public void addFiles(final String merchantStoreCode, List<InputContentFile> inputStaticContentDataList) throws ServiceException;
}
