package com.salesmanager.core.integration.merchant;

import com.salesmanager.common.model.integration.UpdatedEvent;

public class MerchantStoreUpdatedEvent extends UpdatedEvent<MerchantStoreDTO> {

    public MerchantStoreUpdatedEvent(MerchantStoreDTO dto) {
        super(dto);
    }

}
