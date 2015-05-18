package br.com.avfinal.view.component.table;

import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.messagebox.MessageBox;

@SuppressWarnings("rawtypes")
public class ButtonCellAvFinal extends TableCell {
	
	private final Button cellButton = new Button();
	
	public ButtonCellAvFinal(final Stage parent, final ButtonCellOnClick buttonCellOnClick, final TypeButton typeButton) {
		cellButton.getStyleClass().add("buttonDelete");
		cellButton.setGraphic(new ImageView(new Image(TypeButton.DELETE.equals(typeButton) ? Constants.PATH_ICON_REMOVE_GRID.valuesToString() : Constants.PATH_ICON_EDIT_GRID.valuesToString())));
		cellButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent t) {
				if (TypeButton.EDIT.equals(typeButton) && buttonCellOnClick != null) {
					buttonCellOnClick.onClick(getTableRow().getIndex());
					return;
				}
				if (TypeButton.DELETE.equals(typeButton) && MessageBox.show(parent, Constants.MSG_QUESTION_REMOVE.valuesToString(), parent.getTitle(), 
						MessageBox.ICON_QUESTION | MessageBox.YES | MessageBox.NO) == MessageBox.YES) {
					if (buttonCellOnClick != null) {
						Map<Boolean, String> result = buttonCellOnClick.onClick(getTableRow().getIndex());
						if (result.containsKey(Boolean.TRUE)) {
							new MessageDisplay(parent.getScene().getRoot(), result.get(Boolean.TRUE)).showSucessMessage();
						} else if (result.containsKey(Boolean.FALSE)) {
							new MessageDisplay(parent.getScene().getRoot(), result.get(Boolean.FALSE)).showErrorMessage();
						}
					}
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void updateItem(Object t, boolean empty) {
		super.updateItem(t, empty);
		if(!empty){
			setGraphic(cellButton);
		} else {
			setGraphic(null);
		}
	}
	
	public interface ButtonCellOnClick {
		Map<Boolean, String> onClick(int indexSelected);
	}
	
	public enum TypeButton {
		EDIT, DELETE;
	}
	
}