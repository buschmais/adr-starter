package com.salesmanager.core.integration.customer;

import com.salesmanager.common.model.integration.UpdatedEvent;

public class CustomerUpdatedEvent extends UpdatedEvent<CustomerDTO> {

    public CustomerUpdatedEvent(CustomerDTO dto) {
        super(dto);
    }

}
