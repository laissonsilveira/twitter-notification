package br.com.avfinal.mdl.service;

import java.util.List;

import br.com.avfinal.entity.StudentEntity;
import br.com.avfinal.entity.grid.ResultGrid;
import br.com.avfinal.exception.ValidationException;

public interface StudentService {
	
	List<StudentEntity> findAll();
	
	StudentEntity save(StudentEntity entity) throws ValidationException;

	void validate(StudentEntity entity) throws ValidationException;
	
	List<StudentEntity> findStudentBy(Long idGradebook);
	
	void update(ResultGrid result);

	void delete(Long idStudent);

	void update(Long id, String name);

}
