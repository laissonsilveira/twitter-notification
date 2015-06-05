package br.com.avfinal.mdl.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.avfinal.entity.ActivityEntity;
import br.com.avfinal.mdl.dao.ActivityDao;
import br.com.avfinal.mdl.dao.GenericDAO;

@Repository
public class ActivityDaoImpl extends GenericDAO<ActivityEntity> implements ActivityDao {

	public ActivityDaoImpl() {
		super(ActivityEntity.class);
	}
	
	@Override
	public List<ActivityEntity> findBy(Long idGradebook) {
		
		StringBuilder hql = new StringBuilder("FROM ActivityEntity a");
		hql.append(" WHERE a.gradebook.id_gradebook =:idGradebook");
		
		final TypedQuery<ActivityEntity> query = createQuery(hql.toString());
		query.setParameter("idGradebook", idGradebook);
		
		return query.getResultList();
	}

}
