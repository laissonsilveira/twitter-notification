package br.com.avfinal.view.component.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ControllerLoadedEvent extends Event {

	private static final long serialVersionUID = -8770393725767726321L;

    public static final EventType<ControllerLoadedEvent> LOADED = new EventType<ControllerLoadedEvent>(Event.ANY, "LOADED");
    
    public static final EventType<ControllerLoadedEvent> ANY = LOADED;
    
    public interface ControllerLoadedHandler<T extends Event> {
        void onControllerLoaded(T event);
    }
    
    public ControllerLoadedEvent() {
        super(LOADED);
    }
    
    public ControllerLoadedEvent(Object source, EventTarget target) {
        super(source, target, LOADED);
    }

    @Override
    public ControllerLoadedEvent copyFor(Object newSource, EventTarget newTarget) {
        return (ControllerLoadedEvent) super.copyFor(newSource, newTarget);
    }

    @SuppressWarnings("unchecked")
	@Override
    public EventType<? extends ControllerLoadedEvent> getEventType() {
        return (EventType<? extends ControllerLoadedEvent>) super.getEventType();
    }
    
}
