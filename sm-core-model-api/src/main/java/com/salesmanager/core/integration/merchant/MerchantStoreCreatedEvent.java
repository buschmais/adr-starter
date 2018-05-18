package com.salesmanager.core.integration.merchant;

import com.salesmanager.common.model.integration.CreatedEvent;

public class MerchantStoreCreatedEvent extends CreatedEvent<MerchantStoreDTO> {

    public MerchantStoreCreatedEvent(MerchantStoreDTO dto) {
        super(dto);
    }

}
