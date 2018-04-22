package com.ktar5.jazzy.coordination;

import com.ktar5.jazzy.util.EditorEvent;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.config.IBusConfiguration;
import org.pmw.tinylog.Logger;

public class EventCoordinator {
    private static EventCoordinator instance;
    private final MBassador<EditorEvent> eventBus;
    
    public EventCoordinator() {
        eventBus = initializeEventBus();
    }
    
    public static EventCoordinator get() {
        if (instance == null) {
            instance = new EventCoordinator();
        }
        return instance;
    }
    
    public void fireEvent(EditorEvent editorEvent) {
        eventBus.publish(editorEvent);
    }
    
    public void registerListener(Object object) {
        eventBus.subscribe(object);
    }
    
    private MBassador<EditorEvent> initializeEventBus() {
        return new MBassador<>(new BusConfiguration()
                .addFeature(Feature.SyncPubSub.Default())
                .addFeature(Feature.AsynchronousMessageDispatch.Default())
                .addFeature(Feature.AsynchronousHandlerInvocation.Default())
                .addPublicationErrorHandler(Logger::error)
                .setProperty(IBusConfiguration.Properties.BusId, "Game Event Bus"));
    }
    
}
