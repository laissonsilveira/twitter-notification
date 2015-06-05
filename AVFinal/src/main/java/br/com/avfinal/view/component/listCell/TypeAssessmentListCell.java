package br.com.avfinal.view.component.listCell;

import javafx.scene.control.ListCell;
import br.com.avfinal.util.enums.TypeAssessment;

public class TypeAssessmentListCell extends ListCell<TypeAssessment> {
	@Override protected void updateItem(TypeAssessment item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			setText(item.name());
		}
	}
}