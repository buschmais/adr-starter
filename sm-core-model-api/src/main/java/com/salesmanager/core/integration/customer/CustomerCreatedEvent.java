package com.salesmanager.core.integration.customer;

import com.salesmanager.common.model.integration.CreatedEvent;

public class CustomerCreatedEvent extends CreatedEvent<CustomerDTO> {

    public CustomerCreatedEvent(CustomerDTO dto) {
        super(dto);
    }

}
