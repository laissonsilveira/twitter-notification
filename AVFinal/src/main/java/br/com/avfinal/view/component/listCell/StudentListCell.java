package br.com.avfinal.view.component.listCell;

import javafx.scene.control.ListCell;
import br.com.avfinal.entity.StudentEntity;

public class StudentListCell extends ListCell<StudentEntity> {
	@Override protected void updateItem(StudentEntity item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			setText(item.getName());
		}
	}
}