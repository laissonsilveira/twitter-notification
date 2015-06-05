package br.com.avfinal.mdl.service;

import java.util.List;

import br.com.avfinal.entity.ActivityEntity;
import br.com.avfinal.exception.ValidationException;


public interface ActivityService {

	ActivityEntity save(ActivityEntity entity) throws ValidationException;

	void update(ActivityEntity entity) throws ValidationException;
	
	void delete(ActivityEntity entity);

	List<ActivityEntity> findAll(Long idGradebook);

}
