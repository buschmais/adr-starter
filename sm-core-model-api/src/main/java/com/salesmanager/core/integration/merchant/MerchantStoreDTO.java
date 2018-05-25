package com.salesmanager.core.integration.merchant;

import com.salesmanager.core.integration.AbstractCoreDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MerchantStoreDTO extends AbstractCoreDTO {

    private Integer id;

    private String code;

    private String currency;

    private String defaultLanguage;

    private String countryIsoCode;

    private boolean currencyFormatNational;

    private boolean useCache;

    private String storeTemplate;

    private String domainName;

    private List<String> languages;

}
