package br.com.avfinal.mdl.dao;

import br.com.avfinal.entity.LoginEntity;

public interface LoginDao {
	
	LoginEntity getLoginByName(String nom_login);

}
