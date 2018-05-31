package com.salesmanager.core.integration.customer;

import com.salesmanager.common.model.integration.DeletedEvent;

public class CustomerDeletedEvent extends DeletedEvent<CustomerDTO> {

    public CustomerDeletedEvent(CustomerDTO dto) {
        super(dto);
    }

}
