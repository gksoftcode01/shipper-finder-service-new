package ai.yarmook.shipperfinder.core.config.listeners;

import ai.yarmook.shipperfinder.core.listeners.BeforeDeleteEventListener;
import ai.yarmook.shipperfinder.core.listeners.DeleteEventListener;
import ai.yarmook.shipperfinder.core.listeners.InsertEventListener;
import ai.yarmook.shipperfinder.core.listeners.UpdateEventListener;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EventListenerConfig {

    @PersistenceContext
    EntityManager emf;

    private final UpdateEventListener updateEventListener;

    private final InsertEventListener insertEventListener;

    private final DeleteEventListener deleteEventListener;

    private final BeforeDeleteEventListener beforeDeleteEventListener;

    public static final Logger logger = LoggerFactory.getLogger(EventListenerConfig.class);

    public EventListenerConfig(
        UpdateEventListener updateEventListener,
        InsertEventListener insertEventListener,
        DeleteEventListener deleteEventListener,
        BeforeDeleteEventListener beforeDeleteEventListener
    ) {
        this.updateEventListener = updateEventListener;
        this.insertEventListener = insertEventListener;
        this.deleteEventListener = deleteEventListener;
        this.beforeDeleteEventListener = beforeDeleteEventListener;
    }

    @PostConstruct
    @Transactional
    public void init() {
        Session sess = emf.unwrap(Session.class);
        SessionFactory sessionFactory = sess.getSessionFactory();

        logger.info("Shipper Finder ---->>>>attempt register listeners");

        EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(EventListenerRegistry.class);
        if (registry.getEventListenerGroup(EventType.POST_UPDATE).count() <= 1) {
            registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(updateEventListener);
        }

        if (registry.getEventListenerGroup(EventType.POST_INSERT).count() <= 1) {
            registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(insertEventListener);
        }

        if (registry.getEventListenerGroup(EventType.POST_DELETE).count() <= 1) {
            registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(deleteEventListener);
        }
        //
        //        if (registry.getEventListenerGroup(EventType.PRE_DELETE).count() <= 1) {
        //                    registry.getEventListenerGroup(EventType.PRE_DELETE).appendListener(beforeDeleteEventListener);
        //                }
    }
}
