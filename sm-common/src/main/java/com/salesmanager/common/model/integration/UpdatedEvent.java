package com.salesmanager.common.model.integration;

public class UpdatedEvent<T extends AbstractDTO> extends AbstractEvent<T> {

    public UpdatedEvent(T dto) {
        super(dto);
    }

}
