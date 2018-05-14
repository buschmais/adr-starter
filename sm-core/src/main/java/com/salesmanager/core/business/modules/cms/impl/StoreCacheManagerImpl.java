package com.salesmanager.core.business.modules.cms.impl;

import org.infinispan.manager.DefaultCacheManager;

/**
 * Used for managing images
 * @author casams1
 *
 */
public class StoreCacheManagerImpl extends CacheManagerImpl {
	
	private final static String NAMED_CACHE = "StoreRepository";

	public StoreCacheManagerImpl(String location, DefaultCacheManager defaultCacheManager) {
		super.init(NAMED_CACHE,location, defaultCacheManager);
	}

}

