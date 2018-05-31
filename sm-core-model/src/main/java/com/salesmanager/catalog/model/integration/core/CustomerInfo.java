package com.salesmanager.catalog.model.integration.core;


import com.salesmanager.core.constants.SchemaConstant;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER_INFO", schema = SchemaConstant.SALESMANAGER_SCHEMA)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Getter
@Setter
public class CustomerInfo {

    @Id
    @Column(name = "CUSTOMER_ID", unique=true, nullable=false)
    private Long id;

    @Column(name="CUSTOMER_NICK", length=96)
    private String nick;

}
