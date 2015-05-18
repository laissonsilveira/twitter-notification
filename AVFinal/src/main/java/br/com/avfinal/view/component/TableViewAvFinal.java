package br.com.avfinal.view.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import br.com.avfinal.view.component.event.GridLoadEvent;

/**
 * @author Laisson R. Silveira <br>
 *         27/04/2014 <br>
 *         <a href="mailto:laisson.silveira@hotmail.com">laisson.silveira@hotmail.com</a>
 */
public class TableViewAvFinal<S> extends TableView<S> {

	private ObjectProperty<EventHandler<GridLoadEvent>> onGridLoadEvent = new SimpleObjectProperty<EventHandler<GridLoadEvent>>();

	public final ObjectProperty<EventHandler<GridLoadEvent>> onGridLoadEventProperty() {
		return onGridLoadEvent;
	}

	public final void setOnGridLoadEvent(final EventHandler<GridLoadEvent> HANDLER) {
		onGridLoadEventProperty().set(HANDLER);
	}

	public final EventHandler<GridLoadEvent> getOnGridLoadEvent() {
		return onGridLoadEventProperty().get();
	}

	public void fireGridLoadEvent(final GridLoadEvent GRID_LOAD_EVENT) {
		final EventHandler<GridLoadEvent> GRID_LOAD_EVENT_HANDLER = getOnGridLoadEvent();
		if (GRID_LOAD_EVENT_HANDLER != null) {
			GRID_LOAD_EVENT_HANDLER.handle(GRID_LOAD_EVENT);
		}
	}

}
