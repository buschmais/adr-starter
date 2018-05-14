package com.salesmanager.core.business.services.reference.currency;

import com.salesmanager.common.business.service.SalesManagerEntityService;
import com.salesmanager.core.model.reference.currency.Currency;

public interface CurrencyService extends SalesManagerEntityService<Long, Currency> {

	Currency getByCode(String code);

}
