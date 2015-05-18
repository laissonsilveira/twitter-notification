package br.com.avfinal;

import java.io.IOException;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import br.com.avfinal.configuration.AVFinalConfig;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.control.main.LoginController;

/**
 * @author Laisson R. Silveira <br>
 *         AvFinal - 06/12/2012 <br>
 *         <a href="mailto:laisson.silveira@hotmail.com">laisson.silveira@hotmail.com</a><br>
 * @version 1.3.328 (1.3 build 328)
 */
public class StartAvFinal extends Application {

	private static final Logger LOG = Logger.getLogger(StartAvFinal.class);
	private AnnotationConfigApplicationContext context;

	public static void main(String[] args) {
		BasicConfigurator.configure();
		launch(args);
	}

	@Override
	public void start(final Stage stageApp) {
		LOG.info("Starting App...");

		Task<Void> taskContext = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				context = new AnnotationConfigApplicationContext(AVFinalConfig.class);
				return null;
			}

			@Override
			protected void failed() {
				LOG.error(getException().getMessage(), getException());
				getException().printStackTrace();
			}

			@Override
			protected void succeeded() {
				stageApp.close();
				openLogin();
			}
		};

		openWaitProgress(stageApp);

		Thread t = new Thread(taskContext);
		t.setDaemon(true);
		t.start();
	}

	private void openWaitProgress(Stage stageApp) {
		try {
			final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.PATH_FXML_WAIT_PROGRESS.valuesToString()));
			Parent parentLoad = fxmlLoader.load();
			stageApp.setScene(new Scene(parentLoad));
			stageApp.initStyle(StageStyle.TRANSPARENT);
			stageApp.centerOnScreen();
			stageApp.show();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			new MessageDisplay(stageApp.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
		}
	}

	private void openLogin() {
		final LoginController loginController = context.getBean(LoginController.class);
		try {
			loginController.show();
			loginController.stagePrimary.setOnCloseRequest(event -> context.close());
		} catch (IOException e) {
			LOG.error(Constants.MSG_ERROR_GENERIC.valuesToString(), e);
			new MessageDisplay(loginController.stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
		}
	}

	// private void configureBD(Stage stageApp) {
	// File dir = null;
	// Preferences prefs = Preferences.userNodeForPackage(StartAvFinal.class);
	// String filePath = prefs.get("DBPath", null);
	//
	// if (filePath != null) {
	// dir = new File(filePath);
	// } else {
	// DirectoryChooser directoryChooser = new DirectoryChooser();
	// dir = directoryChooser.showDialog(stageApp);
	//
	// if (dir != null) {
	// prefs.put("DBPath", dir.getPath());
	// } else {
	// prefs.remove("DBPath");
	// }
	// }
	//
	// if (dir == null) {
	// throw new NullPointerException("É necessário informar uma caminho!");
	// }
	//
	// }

}
