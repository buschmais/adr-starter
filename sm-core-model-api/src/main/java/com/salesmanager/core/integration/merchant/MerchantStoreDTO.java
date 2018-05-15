package com.salesmanager.core.integration.merchant;

import com.salesmanager.core.integration.AbstractCoreDTO;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
public class MerchantStoreDTO extends AbstractCoreDTO {

    private final Integer id;

    private final String code;

}
