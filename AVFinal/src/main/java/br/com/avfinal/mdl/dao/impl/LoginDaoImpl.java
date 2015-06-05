package br.com.avfinal.mdl.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.avfinal.entity.LoginEntity;
import br.com.avfinal.mdl.dao.GenericDAO;
import br.com.avfinal.mdl.dao.LoginDao;

@Repository
public class LoginDaoImpl extends GenericDAO<LoginEntity> implements LoginDao {
	
	public LoginDaoImpl() {
		super(LoginEntity.class);
	}

	@Override
	public LoginEntity getLoginByName(String nm_login) {
		TypedQuery<LoginEntity> query = createQuery("SELECT l FROM LoginEntity l WHERE l.nm_login =:nm_login");
		query.setParameter("nm_login", nm_login);
		final List<LoginEntity> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}
	
}
