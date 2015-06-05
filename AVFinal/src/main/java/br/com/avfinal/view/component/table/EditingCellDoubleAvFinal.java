package br.com.avfinal.view.component.table;

import static br.com.avfinal.util.UtilAvFinal.getMessageRequired;
import static br.com.avfinal.util.enums.Constants.IS_CANCELED;
import static br.com.avfinal.view.component.AssessmentNoteTextField.DASH;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import br.com.avfinal.entity.grid.BaseGrid;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.AssessmentNoteTextField;
import br.com.avfinal.view.component.MessageDisplay;

public class EditingCellDoubleAvFinal<S extends BaseGrid> extends TableCell<S, Double> {

	private AssessmentNoteTextField textField;
	private Double oldValue;

	@Override
	public void startEdit() {
		
		if (getItem() == null) {
			setEditable(Boolean.FALSE);
			return;
		}
		
		super.startEdit();
		
		if (textField == null) {
			createField();
		}
		setGraphic(textField);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		oldValue = textField.getDoubleValue();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textField.requestFocus();
				textField.selectAll();
			}
		});

	}

	@Override
	public void commitEdit(Double newValue) {

		if ((boolean)getTableColumn().getProperties().get(Constants.IS_REQUIRED_VALUE.valuesToString()) && newValue == null) {

			new MessageDisplay(getStage().getScene().getRoot(), getMessageRequired(getTableColumn().getText())).showErrorMessage();

			getTableView().edit(getTableRow().getIndex(), getTableColumn());
			textField.setText(oldValue.toString());
			textField.requestFocus();

		} else {
			super.commitEdit(newValue);

			if (!newValue.equals(oldValue) && !(Boolean)getTableColumn().getProperties().get(IS_CANCELED.valuesToString())) {
				oldValue = newValue;
				textField.setText(getValueAvFinal(newValue));
				new MessageDisplay(getStage().getScene().getRoot(),Constants.MSG_SUCESS_EDIT.valuesToString()).showSucessMessage();
			} 
		}
	}

	private Stage getStage() {
		return (Stage)getTableView().getProperties().get(Stage.class);
	}

	private String getValueAvFinal(Double newValue) {
		if (newValue == null || newValue == -1D || newValue.toString().trim().isEmpty()) {
			return "0,0";
		}
		return newValue.toString().replace(".", ",");
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		textField.setText(getValueAvFinal(getItem()));
		setText(getString());
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}
	@Override
	public void updateItem(Double item, boolean empty) {
		super.updateItem(item, empty);

		if (isEmpty()) {
			setText(null);
			setGraphic(null);
		} else {
			this.setAlignment(Pos.CENTER);

			if (getItem() < 7) {
				this.setTextFill(Color.RED);
			}
			else {
				this.setTextFill(Color.DARKBLUE);
			}

			if (getItem() == -1D) {
				this.setTextFill(Color.BLACK);
				setItem(null);
				setConfigsUpdateItem(DASH);
				return;
			}

			setConfigsUpdateItem(getItem().toString().replace(".", ","));
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

	private void createField() {
		textField = new AssessmentNoteTextField(getString());
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ENTER) {
					getTableColumn().getProperties().put(IS_CANCELED.valuesToString(), Boolean.FALSE);
					commitEdit(textField.getDoubleValue());
				} else if (t.getCode() == KeyCode.ESCAPE) {
					getTableColumn().getProperties().put(IS_CANCELED.valuesToString(), Boolean.TRUE);
					cancelEdit();
				} else if (t.getCode() == KeyCode.TAB) {
					getTableColumn().getProperties().put(IS_CANCELED.valuesToString(), Boolean.FALSE);
					commitEdit(textField.getDoubleValue());
					TableColumn nextColumn = getNextColumn(!t.isShiftDown());
					if (nextColumn != null) {
						getTableView().edit(getTableRow().getIndex(), nextColumn);
					}
				}
			}
		});
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean outFocus, Boolean inFocus) {
				if (outFocus && textField != null) {
					commitEdit(textField.getDoubleValue());
				}
			}
		});
	}

	private String getString() {
		if (getItem() == null || getItem().toString().trim().isEmpty()) {
			return "0,0";
		}
		return getItem().toString().replace(".", ",");
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