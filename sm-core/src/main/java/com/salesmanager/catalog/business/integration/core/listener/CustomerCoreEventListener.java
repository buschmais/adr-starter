package com.salesmanager.catalog.business.integration.core.listener;

import com.salesmanager.catalog.business.integration.core.repository.CustomerInfoRepository;
import com.salesmanager.catalog.model.integration.core.CustomerInfo;
import com.salesmanager.common.model.integration.CreatedEvent;
import com.salesmanager.common.model.integration.DeletedEvent;
import com.salesmanager.common.model.integration.UpdatedEvent;
import com.salesmanager.core.integration.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CustomerCoreEventListener {


    private CustomerInfoRepository customerInfoRepository;

    @Autowired
    public CustomerCoreEventListener(CustomerInfoRepository customerInfoRepository) {
        this.customerInfoRepository = customerInfoRepository;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCustomerCreateEvent(CreatedEvent<CustomerDTO> event) {
        CustomerDTO customerDTO = event.getDto();
        if (customerDTO != null) {
            CustomerInfo customer = new CustomerInfo(
                    customerDTO.getId(),
                    customerDTO.getNick()
            );
            this.customerInfoRepository.save(customer);
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCustomerDeleteEvent(DeletedEvent<CustomerDTO> event) {
        CustomerDTO customerDTO = event.getDto();
        if (customerDTO != null) {
            this.customerInfoRepository.delete(customerDTO.getId());
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCustomerUpdateEvent(UpdatedEvent<CustomerDTO> event) {
        CustomerDTO customerDTO = event.getDto();
        if (customerDTO != null) {
            CustomerInfo customer = new CustomerInfo(
                    customerDTO.getId(),
                    customerDTO.getNick()
            );
            this.customerInfoRepository.save(customer);
        }
    }

}
