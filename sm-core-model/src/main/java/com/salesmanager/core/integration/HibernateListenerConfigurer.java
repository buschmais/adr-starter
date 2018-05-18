package com.salesmanager.core.integration;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Component
public class HibernateListenerConfigurer {

    @PersistenceUnit(unitName = "shopizerContainer")
    private EntityManagerFactory entityManagerFactory;

    private CreatedEventListener createdEventListener;

    private DeletedEventListener deletedEventListener;

    private UpdatedEventListener updatedEventListener;

    @Autowired
    public HibernateListenerConfigurer(CreatedEventListener createdEventListener, DeletedEventListener deletedEventListener,
                                       UpdatedEventListener updatedEventListener) {
        this.createdEventListener = createdEventListener;
        this.deletedEventListener = deletedEventListener;
        this.updatedEventListener = updatedEventListener;
    }


    @PostConstruct
    protected void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(createdEventListener);
        registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(deletedEventListener);
        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(updatedEventListener);
    }
}