package com.salesmanager.catalog.presentation.model.customer;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReadableCustomerInfo implements Serializable {

    private String firstName;

    private String lastName;

}
