package com.salesmanager.core.integration.merchant;

import com.salesmanager.core.integration.AbstractCoreDTO;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MerchantStoreDTO extends AbstractCoreDTO {

    private Integer id;

    private String code;

}
