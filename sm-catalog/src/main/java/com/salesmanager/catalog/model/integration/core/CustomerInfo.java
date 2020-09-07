package com.salesmanager.catalog.model.integration.core;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.jqassistant.contrib.plugin.ddd.annotation.DDD;

import javax.persistence.*;

@DDD.AggregateRoot
@Entity
@Table(name = "CUSTOMER_INFO")
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

    @NotEmpty
    @Column (name ="CUSTOMER_FIRST_NAME", length=64, nullable=false)
    private String firstName;

    @NotEmpty
    @Column (name ="CUSTOMER_LAST_NAME", length=64, nullable=false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name="MERCHANT_ID", nullable=false)
    private MerchantStoreInfo merchantStore;

    @ManyToOne
    @JoinColumn(name = "LANGUAGE_ID", nullable=false)
    private LanguageInfo defaultLanguage;

}
