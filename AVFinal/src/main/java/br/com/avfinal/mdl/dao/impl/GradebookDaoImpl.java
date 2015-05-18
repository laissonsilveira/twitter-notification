package br.com.avfinal.mdl.dao.impl;

import org.springframework.stereotype.Repository;

import br.com.avfinal.entity.GradebookEntity;
import br.com.avfinal.mdl.dao.GenericDAO;
import br.com.avfinal.mdl.dao.GradebookDao;

@Repository
public class GradebookDaoImpl extends GenericDAO<GradebookEntity> implements GradebookDao {

	public GradebookDaoImpl() {
		super(GradebookEntity.class);
	}

}
