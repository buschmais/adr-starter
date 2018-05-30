package com.salesmanager.core.integration.language;

import com.salesmanager.common.model.integration.DeletedEvent;

public class LanguageDeletedEvent extends DeletedEvent<LanguageDTO> {

    public LanguageDeletedEvent(LanguageDTO dto) {
        super(dto);
    }

}
