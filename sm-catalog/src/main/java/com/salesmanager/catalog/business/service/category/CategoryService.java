package com.salesmanager.catalog.business.service.category;

import java.util.List;

import com.salesmanager.catalog.model.integration.core.LanguageInfo;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;
import com.salesmanager.common.business.exception.ServiceException;
import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.category.CategoryDescription;

public interface CategoryService extends SalesManagerEntityService<Long, Category> {

	List<Category> listByLineage(MerchantStoreInfo store, String lineage) throws ServiceException;
	
	List<Category> listBySeUrl(MerchantStoreInfo store, String seUrl) throws ServiceException;
	
	CategoryDescription getDescription(Category category, LanguageInfo language) throws ServiceException;

	void addCategoryDescription(Category category, CategoryDescription description) throws ServiceException;

	void addChild(Category parent, Category child) throws ServiceException;

	List<Category> listByParent(Category category) throws ServiceException;
	
	List<Category> listByStoreAndParent(MerchantStoreInfo store, Category category) throws ServiceException;
	
	
	List<Category> getByName(MerchantStoreInfo store, String name, LanguageInfo language) throws ServiceException;
	
	List<Category> listByStore(MerchantStoreInfo store) throws ServiceException;

	Category getByCode(MerchantStoreInfo store, String code)
			throws ServiceException;

	List<Category> listByStore(MerchantStoreInfo store, LanguageInfo language)
			throws ServiceException;

	void saveOrUpdate(Category category) throws ServiceException;

	List<Category> listByDepth(MerchantStoreInfo store, int depth);

	/**
	 * Get root categories by store for a given language
	 * @param store
	 * @param depth
	 * @param language
	 * @return
	 */
	List<Category> listByDepth(MerchantStoreInfo store, int depth, LanguageInfo language);
	
	/**
	 * Returns category hierarchy filter by featured
	 * @param store
	 * @param depth
	 * @param language
	 * @return
	 */
	List<Category> listByDepthFilterByFeatured(MerchantStoreInfo store, int depth, LanguageInfo language);

	List<Category> listByLineage(String storeCode, String lineage)
			throws ServiceException;

	Category getByCode(String storeCode, String code) throws ServiceException;

	Category getBySeUrl(MerchantStoreInfo store, String seUrl);

	List<Category> listByParent(Category category, LanguageInfo language);

	Category getByLanguage(long categoryId, LanguageInfo language);

	/**
	 * Returns a list by category containing the category code and the number of products
	 * 1->obj[0] = book
	 *    obj[1] = 150
	 * 2->obj[0] = novell
	 *    obj[1] = 35
	 *   ...
	 * @param store
	 * @param categoryIds
	 * @return
	 * @throws ServiceException
	 */
	List<Object[]> countProductsByCategories(MerchantStoreInfo store,
			List<Long> categoryIds) throws ServiceException;

	/**
	 * Returns a list of Category by category code for a given language
	 * @param store
	 * @param codes
	 * @param language
	 * @return
	 */
	List<Category> listByCodes(MerchantStoreInfo store, List<String> codes,
							   LanguageInfo language);

	/**
	 * List of Category by id
	 * @param store
	 * @param ids
	 * @param language
	 * @return
	 */
	List<Category> listByIds(MerchantStoreInfo store, List<Long> ids,
							 LanguageInfo language);


	
	

}
