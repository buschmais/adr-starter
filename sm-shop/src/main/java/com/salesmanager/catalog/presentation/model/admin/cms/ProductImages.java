package com.salesmanager.catalog.presentation.model.admin.cms;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public class ProductImages implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7732719188032287938L;
	private long productId;

	private List<MultipartFile> file;

	public void setFile(List<MultipartFile> file) {
		this.file = file;
	}

	private String fileName;

	//@NotEmpty(message="{merchant.files.invalid}")
	//@Valid
	public List<MultipartFile> getFile()
	{
		return file;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

}
