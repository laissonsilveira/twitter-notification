package br.com.avfinal.view.control.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.LoginEntity;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.mdl.service.LoginService;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.control.GenericController;

@Component
public class LoginController extends GenericController {

	private static final Logger LOG = Logger.getLogger(LoginController.class);

	@FXML
	private URL location;
	@FXML
	private ResourceBundle resources;

	@FXML
	private TextField nm_login;
	@FXML
	private PasswordField pass_login;

	@Autowired
	private LoginService loginService;

	@Override
	public void initComponet() {}

	@Override
	public void initConfig() {
		nm_login.setOnKeyPressed(event -> {
			if (event.getSource().equals("ENTER")) {
				login();
			}
		});
	}

	@Override
	public void initValues() {}

	@FXML
	private void login() {

		LoginEntity login = new LoginEntity(nm_login.getText(), pass_login.getText(), null);

		try {
			loginService.validateLogin(login);
			final HomeScreenController homeScreen = context.getBean(HomeScreenController.class);
			homeScreen.setTeacherName(login.getNameTeacher());
			homeScreen.show();
		} catch (ValidationException e) {
			showErrorMessage(e.getMessage(), e.getFieldValidationMessageList());
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			showErrorMessage();
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void show() throws IOException {
		load(getClass().getResource(Constants.PATH_FXML_LOGIN.valuesToString()), ResourceBundle.getBundle(Constants.PATH_I18N_APP.valuesToString()));
		stagePrimary.setTitle(Constants.TITLE_LOGIN.valuesToString());
		stagePrimary.show();
		LOG.debug("View - show() -> " + stagePrimary.getTitle());
	}
}
