package com.salesmanager.common.model.integration;

public class DeletedEvent<T extends AbstractDTO> extends AbstractEvent<T> {

    public DeletedEvent(T dto) {
        super(dto);
    }
}
