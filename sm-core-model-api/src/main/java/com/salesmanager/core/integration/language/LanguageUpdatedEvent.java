package com.salesmanager.core.integration.language;

import com.salesmanager.common.model.integration.UpdatedEvent;

public class LanguageUpdatedEvent extends UpdatedEvent<LanguageDTO> {

    public LanguageUpdatedEvent(LanguageDTO dto) {
        super(dto);
    }

}
