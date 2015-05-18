package br.com.avfinal.test;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainTeste extends Application {

	public static void main(String[] args) {
		// Application.launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {

		primaryStage.setTitle("Testes");
		Group root = new Group();
		Scene scene = new Scene(root, 330, 120, Color.WHITE);

		BorderPane mainPane = new BorderPane();
		root.getChildren().add(mainPane);

		final Label label = new Label("TESTES");

		final HBox hb = new HBox();
		hb.setSpacing(5);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(label);
		mainPane.setTop(hb);

		new Button("Start");
		new Button("Cancel");
		final TextField textField = new TextField();
		setPropText(textField);
		final HBox hb2 = new HBox();
		hb2.setSpacing(5);
		hb2.setAlignment(Pos.CENTER);
		hb2.getChildren().addAll(/* startButton, cancelButton, */textField);
		mainPane.setBottom(hb2);

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void setPropText(final TextField textField) {

		textField.setAlignment(Pos.CENTER_RIGHT);
		textField.addEventFilter(KeyEvent.KEY_TYPED, inputevent -> {

			String textInput = textField.getText();
			String character = inputevent.getCharacter();

			if (!character.matches("[\\d,]")) {
				inputevent.consume();
			} else if (textInput.length() > 3) {
				inputevent.consume();
			} else if (textInput.isEmpty() && character.matches("[0,]")) {
				inputevent.consume();
			} else if (textInput.length() == 1) {
				if (textInput.equals("1")) {
					if (character.matches("[1-9]"))
						inputevent.consume();
				} else {
					if (!character.equals(","))
						inputevent.consume();
				}
			} else if ((textInput.length() == 2) && (textInput.charAt(1) == ',') && character.equals(",")) {
				inputevent.consume();
			} else if ((textInput.length() == 3) && (textInput.charAt(1) == ',')) {
				inputevent.consume();
			}
		});

	}

}
