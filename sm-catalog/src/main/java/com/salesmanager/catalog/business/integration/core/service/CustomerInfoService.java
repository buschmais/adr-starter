package com.salesmanager.catalog.business.integration.core.service;

import com.salesmanager.catalog.business.integration.core.repository.CustomerInfoRepository;
import com.salesmanager.catalog.model.integration.core.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerInfoService {

    private CustomerInfoRepository customerInfoRepository;

    @Autowired
    public CustomerInfoService(CustomerInfoRepository customerInfoRepository) {
        this.customerInfoRepository = customerInfoRepository;
    }

    public CustomerInfo findById(Long id) {
        return this.customerInfoRepository.findById(id);
    }

    public CustomerInfo findByNick(String nick, Integer storeId) {
        return this.customerInfoRepository.findByNick(nick, storeId);
    }
}
