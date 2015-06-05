package br.com.avfinal.mdl.dao;

import java.util.List;

import br.com.avfinal.entity.StudentEntity;

public interface StudentDao {

	List<StudentEntity> findAll();
	
	StudentEntity save(StudentEntity entity);

	StudentEntity update(StudentEntity entity);

	List<StudentEntity> findStudentBy(Long idGradebook);

	StudentEntity findById(Long id);
	
	void delete(StudentEntity entity);

}
