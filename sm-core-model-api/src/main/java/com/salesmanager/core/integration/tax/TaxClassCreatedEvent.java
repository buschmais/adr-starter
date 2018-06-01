package com.salesmanager.core.integration.tax;

import com.salesmanager.common.model.integration.CreatedEvent;

public class TaxClassCreatedEvent extends CreatedEvent<TaxClassDTO> {

    public TaxClassCreatedEvent(TaxClassDTO dto) {
        super(dto);
    }

}
