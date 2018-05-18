package com.salesmanager.common.model.integration;

public class CreatedEvent<T extends AbstractDTO> extends AbstractEvent<T> {

    public CreatedEvent(T dto) {
        super(dto);
    }
}
