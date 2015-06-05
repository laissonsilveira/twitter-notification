package br.com.avfinal.mdl.service;

import java.util.List;

import br.com.avfinal.entity.GradebookEntity;
import br.com.avfinal.exception.ValidationException;

public interface GradebookService {
	
	List<GradebookEntity> findAll();
	
	void save(GradebookEntity entity) throws ValidationException;
	
	void validate(GradebookEntity entity) throws ValidationException;
	
	void update(GradebookEntity entity);
	
}
