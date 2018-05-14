package com.salesmanager.catalog.business.cms.product;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.core.model.content.FileContentType;
import com.salesmanager.core.model.content.OutputContentFile;

import java.util.List;


/**
 * Methods to retrieve the static content from the CMS
 * @author Carl Samson
 *
 */
public interface FileGet
{

	public OutputContentFile getFile(final String merchantStoreCode, FileContentType fileContentType, String contentName) throws ServiceException;
    public List<String> getFileNames(final String merchantStoreCode, FileContentType fileContentType) throws ServiceException;
    public List<OutputContentFile> getFiles(final String merchantStoreCode, FileContentType fileContentType) throws ServiceException;
}
