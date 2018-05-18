package com.salesmanager.common.model.integration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class AbstractEvent<T extends AbstractDTO> {

    private T dto;

}
