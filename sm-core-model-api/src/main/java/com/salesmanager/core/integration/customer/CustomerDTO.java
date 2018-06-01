package com.salesmanager.core.integration.customer;

import com.salesmanager.core.integration.AbstractCoreDTO;
import lombok.Value;

@Value
public class CustomerDTO extends AbstractCoreDTO {

    private Long id;

    private String nick;

    private String merchantStoreCode;

    private String firstName;

    private String lastName;

    private String defaultLanguageCode;

}
