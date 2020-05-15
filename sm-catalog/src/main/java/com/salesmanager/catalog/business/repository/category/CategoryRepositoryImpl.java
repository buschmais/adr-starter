package com.salesmanager.catalog.business.repository.category;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.salesmanager.catalog.model.category.Category;
import com.salesmanager.catalog.model.integration.core.MerchantStoreInfo;


public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
	
	@PersistenceContext
    private EntityManager em;
	
	@Override
	public List<Object[]> countProductsByCategories(MerchantStoreInfo store, List<Long> categoryIds) {

		
		StringBuilder qs = new StringBuilder();
		qs.append("select categories, count(product.id) from Product product ");
		qs.append("inner join product.categories categories ");
		qs.append("where categories.id in (:cid) ");
		qs.append("and product.available=true and product.dateAvailable<=:dt ");
		qs.append("group by categories.id");
		
    	String hql = qs.toString();
		Query q = this.em.createQuery(hql);

    	q.setParameter("cid", categoryIds);
    	q.setParameter("dt", new Date());


    	
    	@SuppressWarnings("unchecked")
		List<Object[]> counts =  q.getResultList();

    	
    	return counts;
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Category> listByStoreAndParent(MerchantStoreInfo store, Category category) {
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select c from Category c join fetch c.merchantStore cm ");
		
		if (store == null) {
			if (category == null) {
				//query.from(qCategory)
				queryBuilder.append(" where c.parent IsNull ");
					//.where(qCategory.parent.isNull())
					//.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			} else {
				//query.from(qCategory)
				queryBuilder.append(" join fetch c.parent cp where cp.id =:cid ");
					//.where(qCategory.parent.eq(category))
					//.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			}
		} else {
			if (category == null) {
				//query.from(qCategory)
				queryBuilder.append(" where c.parent IsNull and cm.id=:mid ");
					//.where(qCategory.parent.isNull()
					//	.and(qCategory.merchantStore.eq(store)))
					//.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			} else {
				//query.from(qCategory)
				queryBuilder.append(" join fetch c.parent cp where cp.id =:cId and cm.id=:mid ");
					//.where(qCategory.parent.eq(category)
					//	.and(qCategory.merchantStore.eq(store)))
					//.orderBy(qCategory.sortOrder.asc(),qCategory.id.desc());
			}
		}
		
		queryBuilder.append(" order by c.sortOrder asc");
		
    	String hql = queryBuilder.toString();
		Query q = this.em.createQuery(hql);

    	q.setParameter("cid", category.getId());
    	if (store != null) {
    		q.setParameter("mid", store.getId());
    	}
    	
		
		return q.getResultList();
	}

}
