package br.com.avfinal.mdl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.avfinal.entity.LoginEntity;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.exception.ValidationMessage;
import br.com.avfinal.mdl.dao.LoginDao;
import br.com.avfinal.mdl.service.LoginService;
import br.com.avfinal.util.UtilAvFinal;
import br.com.avfinal.util.enums.Constants;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginDao dao;

	@Override
	public void validateLogin(LoginEntity login) throws ValidationException {
		ValidationMessage.getInstance().validateEntity(login).exception();

		LoginEntity user = dao.getLoginByName(login.getLogin());
		if ((user == null) || !user.getPassword().equals(UtilAvFinal.md5(login.getPassword()))) {
			ValidationMessage.getInstance().exception(Constants.MSG_VALIDATE_USER.valuesToString());
		} else {
			login.setNameTeacher(user.getNameTeacher());
		}

	}

}
