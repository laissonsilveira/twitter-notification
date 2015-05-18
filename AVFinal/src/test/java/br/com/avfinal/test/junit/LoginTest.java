package br.com.avfinal.test.junit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import br.com.avfinal.entity.LoginEntity;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.mdl.service.LoginService;
import br.com.avfinal.mdl.service.impl.LoginServiceImpl;

@Ignore
public class LoginTest {

	static final Logger logger = Logger.getLogger(LoginTest.class);

	public LoginTest() {
		BasicConfigurator.configure();
	}

	@Test(expected = ValidationException.class)
	public void efetuarLogin() throws ValidationException {
		LoginService emService = new LoginServiceImpl();
		// DebugUtils.transactionRequired("LoginTest.efetuarLogin()");
		LoginEntity login = new LoginEntity("1", "1", null);
		emService.validateLogin(login);
	}
}
