package br.com.avfinal.view.component.listCell;

import javafx.scene.control.ListCell;
import br.com.avfinal.util.enums.NumTypeAssessment;

public class NumTypeAssessmentListCell extends ListCell<NumTypeAssessment> {
	@Override protected void updateItem(NumTypeAssessment item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			setText(item.name());
		}
	}
}