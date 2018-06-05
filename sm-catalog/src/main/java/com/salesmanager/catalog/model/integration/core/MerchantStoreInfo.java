package com.salesmanager.catalog.model.integration.core;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MERCHANT_STORE_INFO")
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

    @Column(name = "USE_CACHE")
    private boolean useCache = false;

    @Column(name="STORE_TEMPLATE", length=25)
    private String storeTemplate;

    @Column(name="DOMAIN_NAME", length=80)
    private String domainName;

    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "MERCHANT_INFO_LANGUAGE")
    private List<LanguageInfo> languages = new ArrayList<>();
}
