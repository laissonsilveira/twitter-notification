package br.com.avfinal.mdl.dao;

import java.util.List;

import br.com.avfinal.entity.ActivityEntity;

public interface ActivityDao {

	ActivityEntity save(ActivityEntity entity);

	ActivityEntity update(ActivityEntity entity);
	
	void delete(ActivityEntity entity);

	List<ActivityEntity> findBy(Long idGradebook);

}
