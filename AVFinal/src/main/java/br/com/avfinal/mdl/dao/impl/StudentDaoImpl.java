package br.com.avfinal.mdl.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.avfinal.entity.StudentEntity;
import br.com.avfinal.mdl.dao.GenericDAO;
import br.com.avfinal.mdl.dao.StudentDao;

@Repository
public class StudentDaoImpl extends GenericDAO<StudentEntity> implements StudentDao {
	
	public StudentDaoImpl() {
		super(StudentEntity.class);
	}

	@Override
	public List<StudentEntity> findStudentBy(Long idGradebook) {
		TypedQuery<StudentEntity> query = createQuery("FROM StudentEntity s WHERE s.gradebook.id_gradebook =:idGradebook ORDER BY s.nm_student");
		query.setParameter("idGradebook", idGradebook);
		return query.getResultList();
	}

}
