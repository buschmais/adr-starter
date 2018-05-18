package com.salesmanager.catalog.business.integration.listener;

import com.salesmanager.common.model.integration.CreatedEvent;
import com.salesmanager.common.model.integration.DeletedEvent;
import com.salesmanager.common.model.integration.UpdatedEvent;
import com.salesmanager.core.integration.merchant.MerchantStoreDTO;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CoreEventListener {

    @EventListener
    public void handleMerchantStoreCreateEvent(CreatedEvent<MerchantStoreDTO> event) {
        System.out.println(event);
    }

    @EventListener
    public void handleMerchantStoreDeleteEvent(DeletedEvent<MerchantStoreDTO> event) {
        System.out.println(event);
    }

    @EventListener
    public void handleMerchantStoreUpdateEvent(UpdatedEvent<MerchantStoreDTO> event) {
        System.out.println(event);
    }

}
