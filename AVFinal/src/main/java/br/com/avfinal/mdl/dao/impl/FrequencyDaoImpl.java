package br.com.avfinal.mdl.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.avfinal.entity.FrequencyDateEntity;
import br.com.avfinal.entity.FrequencyEntity;
import br.com.avfinal.mdl.dao.FrequencyDao;
import br.com.avfinal.mdl.dao.GenericDAO;

@Repository
public class FrequencyDaoImpl extends GenericDAO<FrequencyEntity> implements FrequencyDao {

	public FrequencyDaoImpl() {
		super(FrequencyEntity.class);
	}

	@Override
	public FrequencyEntity findBy(Long id_student, Date date) {

		StringBuilder hql = new StringBuilder("FROM FrequencyEntity f");
		hql.append(" WHERE f.student.id_student =:id_student AND f.frequency_date.dat_Frequency =:date");

		final TypedQuery<FrequencyEntity> query = createQuery(hql.toString());
		query.setParameter("id_student", id_student);
		query.setParameter("date", date);

		return getSingleResult(query);
	}

	@Override
	public FrequencyDateEntity findQtdClassBy(Date date) {

		StringBuilder hql = new StringBuilder("FROM FrequencyDateEntity f");
		hql.append(" WHERE f.dat_Frequency =:date");

		final TypedQuery<?> query = createQuery(FrequencyDateEntity.class, hql.toString());
		query.setParameter("date", date);

		return getSingleResult(query);
	}

	@Override
	public List<FrequencyEntity> findByGradebook(Long idGradebook, Integer numBimester) {
		StringBuilder hql = new StringBuilder("FROM FrequencyEntity f");
		hql.append(" WHERE f.student.gradebook.id_gradebook =:idGradebook");
		hql.append(" AND f.frequency_date.num_bimester =:numBimester");

		final TypedQuery<FrequencyEntity> query = createQuery(hql.toString());
		query.setParameter("idGradebook", idGradebook);
		query.setParameter("numBimester", numBimester);

		return getResultList(query);
	}

}
