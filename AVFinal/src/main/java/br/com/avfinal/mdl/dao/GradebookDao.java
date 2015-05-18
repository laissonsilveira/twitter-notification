package br.com.avfinal.mdl.dao;

import java.util.List;

import br.com.avfinal.entity.GradebookEntity;

public interface GradebookDao {

	List<GradebookEntity> findAll();
	
	GradebookEntity save(GradebookEntity entity);

	GradebookEntity update(GradebookEntity entity);

}
