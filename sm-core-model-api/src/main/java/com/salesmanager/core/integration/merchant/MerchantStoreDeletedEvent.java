package com.salesmanager.core.integration.merchant;

import com.salesmanager.common.model.integration.DeletedEvent;

public class MerchantStoreDeletedEvent extends DeletedEvent<MerchantStoreDTO> {

    public MerchantStoreDeletedEvent(MerchantStoreDTO dto) {
        super(dto);
    }

}
