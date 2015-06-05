package br.com.avfinal.view.component.table;

import static br.com.avfinal.util.UtilAvFinal.getMessageRequired;
import static br.com.avfinal.util.enums.Constants.IS_CANCELED;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import br.com.avfinal.entity.BaseEntity;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.MessageDisplay;

public class EditingCellAvFinal<S extends BaseEntity, T extends Object> extends TableCell<S, T> {

	private TextField textField;
	private T oldValue;
	private TableColumn<S, T> cellParent;
	
	public EditingCellAvFinal() {}
	
	public EditingCellAvFinal(TableColumn<S, T> cell) {
		this.cellParent = cell;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void startEdit() {
		super.startEdit();
		if (textField == null) {
			createTextField();
		}
		
		if (getTableColumn().equals(cellParent)) {
			textField.setText(textField.getText().toUpperCase());
		}
		
		setGraphic(textField);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		oldValue = (T) textField.getText();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textField.requestFocus();
				textField.selectAll();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void commitEdit(T newValue) {

		if ((boolean)getTableColumn().getProperties().get(Constants.IS_REQUIRED_VALUE.valuesToString()) && fieldIsEmpty(newValue)) {
			new MessageDisplay(getStage().getScene().getRoot(), getMessageRequired(getTableColumn().getText())).showErrorMessage();
			getTableView().edit(getTableRow().getIndex(), getTableColumn());
			textField.setText(oldValue.toString());
			textField.requestFocus();
			textField.selectAll();

		} else {
			if (getTableColumn().equals(cellParent)) {
				newValue = (T) newValue.toString().toUpperCase();
			}
			super.commitEdit(newValue);

			if (!newValue.equals(oldValue) && !(Boolean)getTableColumn().getProperties().get(IS_CANCELED.valuesToString())) {
				oldValue = newValue;
				new MessageDisplay(getStage().getScene().getRoot(),Constants.MSG_SUCESS_EDIT.valuesToString()).showSucessMessage();
			}
		}
	}

	private Stage getStage() {
		return (Stage)getTableView().getProperties().get(Stage.class);
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getItemString());
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateItem(T item, boolean empty) {
		if (!empty && getTableColumn().equals(cellParent)) {
			item = (T) item.toString().toUpperCase();
		}
		super.updateItem(item, empty);

		if (isEmpty()) {
			setText(null);
			setGraphic(null);
		} else {
			setConfigsUpdateItem(getItemString());
		}
	}

	private void setConfigsUpdateItem(String text) {
		if (isEditing()) {
			if (textField != null) {
				textField.setText(text);
			}
			setGraphic(textField);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		} else {
			setText(text);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		}
	}

	private void createTextField() {
		textField = new TextField(getItemString());
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ENTER) {
					getTableColumn().getProperties().put(IS_CANCELED.valuesToString(), Boolean.FALSE);
					commitEdit((T)textField.getText());
				} else if (t.getCode() == KeyCode.ESCAPE) {
					getTableColumn().getProperties().put(IS_CANCELED.valuesToString(), Boolean.TRUE);
					cancelEdit();
				} else if (t.getCode() == KeyCode.TAB) {
					getTableColumn().getProperties().put(IS_CANCELED.valuesToString(), Boolean.FALSE);
					commitEdit((T)textField.getText());
					TableColumn nextColumn = getNextColumn(!t.isShiftDown());
					if (nextColumn != null) {
						getTableView().edit(getTableRow().getIndex(), nextColumn);
					}
				}
			}
		});
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@SuppressWarnings("unchecked")
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean outFocus, Boolean inFocus) {
				if (outFocus && textField != null) {
					commitEdit((T)textField.getText());
				}
			}
		});

	}

	private boolean fieldIsEmpty(Object value) {
		if (value == null) {
			return Boolean.TRUE;
		}

		if (value instanceof String && value.toString().trim().isEmpty()) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	private String getItemString() {
		return getItem() == null ? "" :  getItem().toString();
	}

	/**
	 *
	 * @param forward true gets the column to the right, false the column to the left of the current column
	 * @return
	 */
	private TableColumn<S, ?> getNextColumn(boolean forward) {
		List<TableColumn<S, ?>> columns = new ArrayList<>();
		for (TableColumn<S, ?> column : getTableView().getColumns()) {
			columns.addAll(getLeaves(column));
		}
		//There is no other column that supports editing.
		if (columns.size() < 2) {
			return null;
		}
		int currentIndex = columns.indexOf(getTableColumn());
		int nextIndex = currentIndex;
		if (forward) {
			nextIndex++;
			if (nextIndex > columns.size() - 1) {
				nextIndex = 0;
			}
		} else {
			nextIndex--;
			if (nextIndex < 0) {
				nextIndex = columns.size() - 1;
			}
		}
		return columns.get(nextIndex);
	}

	private List<TableColumn<S, ?>> getLeaves(TableColumn<S, ?> root) {
		List<TableColumn<S, ?>> columns = new ArrayList<>();
		if (root.getColumns().isEmpty()) {
			//We only want the leaves that are editable.
			if (root.isEditable()) {
				columns.add(root);
			}
			return columns;
		} else {
			for (TableColumn<S, ?> column : root.getColumns()) {
				columns.addAll(getLeaves(column));
			}
			return columns;
		}
	}
}