package br.com.avfinal.mdl.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.avfinal.entity.RecoveryEntity;
import br.com.avfinal.mdl.dao.GenericDAO;
import br.com.avfinal.mdl.dao.RecoveryDao;

@Repository
public class RecoveryDaoImpl extends GenericDAO<RecoveryEntity> implements RecoveryDao {

	public RecoveryDaoImpl() {
		super(RecoveryEntity.class);
	}
	
	@Override
	public List<RecoveryEntity> findRecoveriesBy(Integer bimester, Long idGradebook) {

		StringBuilder hql = new StringBuilder("FROM RecoveryEntity r");
		hql.append(" WHERE r.assessment.num_bimester =:bimester");
		hql.append(" AND r.assessment.student.gradebook.id_gradebook =:idGradebook");
		
		final TypedQuery<RecoveryEntity> query = createQuery(hql.toString());
		query.setParameter("bimester", bimester);
		query.setParameter("idGradebook", idGradebook);
		
		return query.getResultList();
		
	}

	@Override
	public RecoveryEntity findBy(Long idAssessement) {
		StringBuilder hql = new StringBuilder("FROM RecoveryEntity r");
		hql.append(" WHERE r.assessment.id_assessment =:idAssessement");
		
		final TypedQuery<RecoveryEntity> query = createQuery(hql.toString());
		query.setParameter("idAssessement", idAssessement);
		
		return query.getSingleResult();
	}

}
