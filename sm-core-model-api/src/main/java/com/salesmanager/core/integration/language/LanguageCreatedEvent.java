package com.salesmanager.core.integration.language;

import com.salesmanager.common.model.integration.CreatedEvent;

public class LanguageCreatedEvent extends CreatedEvent<LanguageDTO> {

    public LanguageCreatedEvent(LanguageDTO dto) {
        super(dto);
    }

}
