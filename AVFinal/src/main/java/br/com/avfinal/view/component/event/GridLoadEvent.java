package br.com.avfinal.view.component.event;

import javafx.event.Event;
import javafx.event.EventType;

public class GridLoadEvent extends Event {

	private static final long serialVersionUID = 1021070946488245810L;

//	public enum Type {
//        LOAD,
//    }
//   
//	private final Type TYPE;

    public GridLoadEvent() {
        super(new EventType<GridLoadEvent>(Event.ANY));
//        this.TYPE = Type.LOAD;
    }

//    public GridLoadEvent(final Object SOURCE, final EventTarget TARGET) {
//        super(SOURCE, TARGET, new EventType<GridLoadEvent>());
//        this.TYPE = Type.LOAD;
//    }

//    public Type getType() {
//        return TYPE;
//    }
}
