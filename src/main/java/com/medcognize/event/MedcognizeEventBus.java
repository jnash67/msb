package com.medcognize.event;


import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.medcognize.MedcognizeUI;
import org.springframework.stereotype.Component;

/**
 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */
@Component
public class MedcognizeEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        MedcognizeUI.getMedcognizeEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
        MedcognizeUI.getMedcognizeEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        MedcognizeUI.getMedcognizeEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,
                                      final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}