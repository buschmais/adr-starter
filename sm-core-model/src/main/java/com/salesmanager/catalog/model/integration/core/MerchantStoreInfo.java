package com.salesmanager.catalog.model.integration.core;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.reference.currency.Currency;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "MERCHANT_STORE_INFO", schema = SchemaConstant.SALESMANAGER_SCHEMA)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Getter
@Setter
public class MerchantStoreInfo {

    @Id
    @Column(name = "MERCHANT_ID", unique=true, nullable=false)
    private Integer id;

    @NotEmpty
    @Pattern(regexp="^[a-zA-Z0-9_]*$")
    @Column(name = "STORE_CODE", nullable=false, unique=true, length=100)
    private String code;

    @Column(name = "CURRENCY_ID", nullable=false)
    private String currency;

    @Column(name = "LANGUAGE_ID", nullable=false)
    private String defaultLanguage;

    @Column(name = "COUNTRY_ID", nullable = false)
    private String countryIsoCode;

    @Column(name = "CURRENCY_FORMAT_NATIONAL")
    private boolean currencyFormatNational;
}
