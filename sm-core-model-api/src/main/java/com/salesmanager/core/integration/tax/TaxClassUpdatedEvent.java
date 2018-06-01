package com.salesmanager.core.integration.tax;

import com.salesmanager.common.model.integration.UpdatedEvent;

public class TaxClassUpdatedEvent extends UpdatedEvent<TaxClassDTO> {

    public TaxClassUpdatedEvent(TaxClassDTO dto) {
        super(dto);
    }

}
