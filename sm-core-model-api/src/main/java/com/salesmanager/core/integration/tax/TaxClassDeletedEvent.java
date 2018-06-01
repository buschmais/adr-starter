package com.salesmanager.core.integration.tax;

import com.salesmanager.common.model.integration.DeletedEvent;

public class TaxClassDeletedEvent extends DeletedEvent<TaxClassDTO> {

    public TaxClassDeletedEvent(TaxClassDTO dto) {
        super(dto);
    }

}
