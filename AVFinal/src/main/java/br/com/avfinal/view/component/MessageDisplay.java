package br.com.avfinal.view.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.apache.log4j.Logger;

import br.com.avfinal.exception.FieldValidationMessage;
import br.com.avfinal.util.enums.Constants;

public class MessageDisplay extends Tooltip {

	private static final Logger LOG = Logger.getLogger(MessageDisplay.class);

	private static Node node;
	private static List<FieldValidationMessage> fields;

	public MessageDisplay() {}

	public MessageDisplay(final Node node, String message) {
		newToolTip(node, message, null);
	}

	public MessageDisplay(final Node node, String message, List<FieldValidationMessage> list) {
		newToolTip(node, message, list);
	}

	public MessageDisplay(final Node node, String message, StringProperty idProperty) {
		List<FieldValidationMessage> fieldsValidate = new ArrayList<FieldValidationMessage>();
		fieldsValidate.add(new FieldValidationMessage(idProperty.getValue(), message));
		newToolTip(node, message, fieldsValidate);
	}

	private void newToolTip(final Node node, String message, List<FieldValidationMessage> list) {
		if (node == null) {
			final String msg = "'Node' não pode estar nulo";
			LOG.error(msg);
			throw new IllegalArgumentException(msg);
		}

		MessageDisplay.fields = list;
		MessageDisplay.node = node;
		setText(message == null ? "" : message);
		setAutoHide(Boolean.TRUE);
		setPrefWidth(250D);
		setMinWidth(250D);
		setMaxWidth(250D);
		setWrapText(true);
	}

	private void showMessage() {

		Node parent = node.getScene().getRoot();

		final double midleScreen = parent.getScene().getWidth() / 2;
		final double midleToolTip = getPrefWidth() / 2;

		Point2D p = parent.localToScene(midleScreen - midleToolTip, 0D);

		if (containsFields()) {
			setBorderRed(getNodesFXML());
		}

		super.show(parent, p.getX() + parent.getScene().getX() + parent.getScene().getWindow().getX(), p.getY() + parent.getScene().getY()
				+ parent.getScene().getWindow().getY());

		new Thread(getTask()).start();
	}

	public void showErrorMessage() {
		setStyle("-fx-background-color: #FF6A6A; -fx-font-size: 11.0px; -fx-font-weight: bold; -fx-alignment: center;");
		Tooltip.install(node.getScene().getRoot(), this);
		showMessage();
	}

	public void showInfoMessage() {
		setStyle("-fx-background-color: #EEC900; -fx-font-size: 11.0px; -fx-font-weight: bold; -fx-alignment: center;");
		Tooltip.install(node.getScene().getRoot(), this);
		showMessage();
	}

	public void showSucessMessage() {
		setStyle("-fx-background-color: #90EE90; -fx-font-size: 11.0px; -fx-font-weight: bold; -fx-alignment: center;");
		Tooltip.install(node.getScene().getRoot(), this);
		showMessage();
	}

	private Map<String, Node> getNodesFXML() {

		Map<String, Node> nodesFXML = new HashMap<String, Node>();

		putNodesInMap(node, nodesFXML);

		return nodesFXML;
	}

	private void setBorderRed(Map<String, Node> nodesFXML) {

		for (Node node : nodesFXML.values()) {
			node.setStyle("-fx-border-color: null;");
			Tooltip.uninstall(node, new Tooltip());
		}

		for (FieldValidationMessage fieldName : fields) {
			Node field = nodesFXML.get(fieldName.getFieldName());
			if (field != null) {

				StringBuilder style = new StringBuilder();
				style.append("-fx-border-color: red; ");

				if (field instanceof TextField) {
					style.append("-fx-background-repeat: no-repeat; ").append("-fx-background-position: right center; ")
					.append("-fx-background-image: url('" + Constants.PATH_ICON_REMOVE_GRID.valuesToString() + "');");
				}

				field.setStyle(style.toString());

				Tooltip tooltipField = new Tooltip();
				tooltipField.setText(fieldName.getMessageKey());
				tooltipField.setAutoHide(Boolean.TRUE);
				tooltipField.setStyle("-fx-background-color: #FF6A6A; -fx-font-size: 11.0px; -fx-font-weight: bold; -fx-alignment: center;");
				Tooltip.install(field, tooltipField);

				if (field instanceof TextInputControl) {
					field.setOnKeyPressed(event -> {
						setDefaultField(field, tooltipField);
						event.consume();
						field.setOnKeyPressed(null);
					});
				} else {
					field.setOnMouseClicked(event -> {
						setDefaultField(field, tooltipField);
						event.consume();
						field.setOnMouseClicked(null);
					});
				}

			}
		}
	}

	private void setDefaultField(Node field, Tooltip tooltipField) {
		field.setStyle("-fx-border-color: null;");
		Tooltip.uninstall(field, tooltipField);
	}

	private boolean containsFields() {
		return (fields != null) && !fields.isEmpty();
	}

	private void putNodesInMap(Node node, Map<String, Node> nodesFXML) {

		final String id = node.getId();
		if ((id != null) && !id.isEmpty()) {
			nodesFXML.put(id, node);
		}

		ObservableList<Node> nodes = FXCollections.observableArrayList();

		if (node instanceof AnchorPane) {
			nodes = ((AnchorPane) node).getChildren();
		} else if (node instanceof GridPane) {
			nodes = ((GridPane) node).getChildren();
		} else if (node instanceof VBox) {
			nodes = ((VBox) node).getChildren();
		} else if (node instanceof HBox) {
			nodes = ((HBox) node).getChildren();
		}

		for (Node children : nodes) {
			putNodesInMap(children, nodesFXML);
		}
	}

	private Task<Integer> getTask() {
		Task<Integer> task = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				int iterations;
				for (iterations = 1; iterations <= 3; iterations++) {
					if (isCancelled()) {
						break;
					}
					updateProgress(iterations, 3);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException interrupted) {
						if (isCancelled()) {
							break;
						}
					}
				}
				return iterations;
			}
		};
		task.progressProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			if (newValue.intValue() == 1) {
				hide();
			}
		});
		return task;
	}

	@Override
	public void hide() {
		setGraphic(null);
		super.hide();
		Tooltip.uninstall(node.getScene().getRoot(), this);
	}

}
