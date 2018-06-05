package com.salesmanager.catalog.business.service.product.file;

import javax.inject.Inject;

import com.salesmanager.catalog.business.cms.product.StaticProductFileManager;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.catalog.business.repository.product.file.DigitalProductRepository;
import com.salesmanager.catalog.business.service.product.ProductService;
import com.salesmanager.common.business.service.SalesManagerEntityServiceImpl;
import com.salesmanager.catalog.model.product.Product;
import com.salesmanager.catalog.model.product.file.DigitalProduct;
import com.salesmanager.catalog.model.content.FileContentType;
import com.salesmanager.catalog.model.content.InputContentFile;

@Service("digitalProductService")
public class DigitalProductServiceImpl extends SalesManagerEntityServiceImpl<Long, DigitalProduct> 
	implements DigitalProductService {
	

	private DigitalProductRepository digitalProductRepository;
	
    @Inject
	StaticProductFileManager productDownloadsFileManager;
    
    @Inject
    ProductService productService;

	@Inject
	public DigitalProductServiceImpl(DigitalProductRepository digitalProductRepository) {
		super(digitalProductRepository);
		this.digitalProductRepository = digitalProductRepository;
	}
	
	@Override
	public void addProductFile(Product product, DigitalProduct digitalProduct, InputContentFile inputFile) throws ServiceException {
	
		Assert.notNull(digitalProduct,"DigitalProduct cannot be null");
		Assert.notNull(product,"Product cannot be null");
		digitalProduct.setProduct(product);

		try {
			
			Assert.notNull(inputFile.getFile(),"InputContentFile.file cannot be null");
			
			Assert.notNull(product.getMerchantStore(),"Product.merchantStore cannot be null");
			this.saveOrUpdate(digitalProduct);
			
			productDownloadsFileManager.addFile(product.getMerchantStore().getCode(), inputFile);
			
			product.setProductVirtual(true);
			productService.update(product);
		
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			try {

				if(inputFile.getFile()!=null) {
					inputFile.getFile().close();
				}

			} catch(Exception ignore) {}
		}
		
		
	}
	
	@Override
	public DigitalProduct getByProduct(MerchantStoreInfo store, Product product) throws ServiceException {
		return digitalProductRepository.findByProduct(store.getId(), product.getId());
	}
	
	@Override
	public void delete(DigitalProduct digitalProduct) throws ServiceException {
		
		Assert.notNull(digitalProduct,"DigitalProduct cannot be null");
		Assert.notNull(digitalProduct.getProduct(),"DigitalProduct.product cannot be null");
		//refresh file
		digitalProduct = this.getById(digitalProduct.getId());
		super.delete(digitalProduct);
		productDownloadsFileManager.removeFile(digitalProduct.getProduct().getMerchantStore().getCode(), FileContentType.PRODUCT, digitalProduct.getProductFileName());
		digitalProduct.getProduct().setProductVirtual(false);
		productService.update(digitalProduct.getProduct());
	}
	
	
	@Override
	public void saveOrUpdate(DigitalProduct digitalProduct) throws ServiceException {
		
		Assert.notNull(digitalProduct,"DigitalProduct cannot be null");
		Assert.notNull(digitalProduct.getProduct(),"DigitalProduct.product cannot be null");
		if(digitalProduct.getId()==null || digitalProduct.getId().longValue()==0) {
			super.save(digitalProduct);
		} else {
			super.create(digitalProduct);
		}
		
		digitalProduct.getProduct().setProductVirtual(true);
		productService.update(digitalProduct.getProduct());
		
		
	}
	

	

}
