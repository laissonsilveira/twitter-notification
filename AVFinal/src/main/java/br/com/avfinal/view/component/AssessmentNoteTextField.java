package br.com.avfinal.view.component;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class AssessmentNoteTextField extends TextField {
	
	public static final String DASH = "-";
	
	public AssessmentNoteTextField(String text) {
		
		setText(text);
		
		setAlignment(Pos.CENTER_RIGHT);
		
		addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override  public void handle(KeyEvent inputevent) {
				
				String character = inputevent.getCharacter();

				if (!character.matches("[\\d,]")) {
					inputevent.consume();
				} else if (getText().length() > 3) {
					inputevent.consume();
				} else if (getText().isEmpty() && character.equals(",")) {
					inputevent.consume();
				} else if (getText().length() == 1) {
					if (getText().equals("1")) {
						if (character.matches("[1-9]")) inputevent.consume();
					} else {
						if (!character.equals(",")) inputevent.consume();
					}
				} else if (getText().length() == 2) {
					if ((getText().charAt(1) == ',') && character.equals(",")) inputevent.consume();
					if (getText().startsWith("10") && !character.equals(",")) inputevent.consume();
				} else if (getText().length() == 3) {
					if (getText().charAt(1)  == ',') inputevent.consume();
					if (getText().charAt(2)  == ',' && !character.matches("[0-9]")) inputevent.consume();
				}
			}
		});
		
	}

	public Double getDoubleValue() {
		if (this == null || this.getText().trim().isEmpty()) {
			return 0D;
		}
		return Double.valueOf(this.getText().replace(",", "."));
	}

}

