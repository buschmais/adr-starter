package com.salesmanager.core.integration.language;

import com.salesmanager.core.integration.AbstractCoreDTO;
import lombok.Value;

@Value
public class LanguageDTO extends AbstractCoreDTO {

    private Integer id;

    private String code;

}
