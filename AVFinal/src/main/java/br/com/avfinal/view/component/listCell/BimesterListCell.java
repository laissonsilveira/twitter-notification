package br.com.avfinal.view.component.listCell;

import javafx.scene.control.ListCell;
import br.com.avfinal.util.enums.Bimester;

public class BimesterListCell extends ListCell<Bimester> {
	@Override protected void updateItem(Bimester item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			setText(item.name());
		}
	}
}