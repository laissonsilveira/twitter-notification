package br.com.avfinal.view.component.table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

public class CheckBoxTableCellAvFinal<S, T> extends TableCell<S, T> {
	private final CheckBox checkBox;
	private ObservableValue<T> ov;
	public CheckBoxTableCellAvFinal() {
		this.checkBox = new CheckBox();
		this.checkBox.setAlignment(Pos.CENTER);
		setAlignment(Pos.CENTER);
		setGraphic(checkBox);
	} 
	@Override public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			setGraphic(checkBox);
			if (ov != null && ov.getValue() instanceof BooleanProperty) {
				checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov.getValue());
			}	
			ov = getTableColumn().getCellObservableValue(getIndex());
			if (ov.getValue() instanceof BooleanProperty) {
				checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov.getValue());
			}
		}
	}
}