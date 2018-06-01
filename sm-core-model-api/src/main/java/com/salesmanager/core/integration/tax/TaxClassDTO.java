package com.salesmanager.core.integration.tax;

import com.salesmanager.core.integration.AbstractCoreDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaxClassDTO extends AbstractCoreDTO {

    private Long id;

    private String code;

}
