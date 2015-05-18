package br.com.avfinal.mdl.service;

import br.com.avfinal.entity.LoginEntity;
import br.com.avfinal.exception.ValidationException;

public interface LoginService {

	void validateLogin(LoginEntity login) throws ValidationException;

}
