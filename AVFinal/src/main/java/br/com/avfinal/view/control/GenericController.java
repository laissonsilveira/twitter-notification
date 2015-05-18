package br.com.avfinal.view.control;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.avfinal.exception.FieldValidationMessage;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.factory.ControllerFactory;
import br.com.avfinal.view.component.factory.TabBuilderFactory;

public abstract class GenericController implements IGenericController {

	static final Logger LOG = Logger.getLogger(GenericController.class);

	@Autowired
	protected ApplicationContext context;

	@Autowired
	private ControllerFactory controllerFactory;

	@Autowired
	private TabBuilderFactory tabBuilderFactory;

	@Autowired
	public Stage stagePrimary;

	protected abstract void show() throws IOException;

	protected void load(URL location) throws IOException {
		FXMLLoader loader = new FXMLLoader(location);
		load(loader);
	}

	protected void load(URL location, ResourceBundle resources) throws IOException {
		FXMLLoader loader = new FXMLLoader(location, resources);
		load(loader);
	}

	private void load(FXMLLoader loader) throws IOException {
		loader.setControllerFactory(controllerFactory);
		loader.setBuilderFactory(tabBuilderFactory);
		Parent parent = loader.load();
		final Scene scene = new Scene(parent);
		stagePrimary.setScene(scene);
		stagePrimary.setFullScreen(false);
		stagePrimary.setResizable(false);
		stagePrimary.centerOnScreen();
	}

	@Override
	public void initializeController() {
		LOG.debug("View - initialize()");

		initComponet();
		LOG.debug("View - initComponent()");

		initConfig();
		LOG.debug("View - initConfig()");

		initValues();
		LOG.debug("View - initValues()");
	}

	public void showErrorMessage() {
		showErrorMessage(Constants.MSG_ERROR_GENERIC.valuesToString());
	}

	public void showErrorMessage(String message) {
		new MessageDisplay(stagePrimary.getScene().getRoot(), message).showErrorMessage();
	}

	public void showErrorMessage(String message, List<FieldValidationMessage> fields) {
		new MessageDisplay(stagePrimary.getScene().getRoot(), message, fields).showErrorMessage();
	}

}
