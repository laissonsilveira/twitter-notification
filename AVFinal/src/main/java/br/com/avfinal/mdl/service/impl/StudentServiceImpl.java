package br.com.avfinal.mdl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.avfinal.entity.StudentEntity;
import br.com.avfinal.entity.grid.ResultGrid;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.exception.ValidationMessage;
import br.com.avfinal.mdl.dao.StudentDao;
import br.com.avfinal.mdl.service.StudentService;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentDao dao;

	@Override
	public List<StudentEntity> findAll() {
		return dao.findAll();
	}

	@Override
	public StudentEntity save(StudentEntity entity) throws ValidationException {
		validate(entity);
		return dao.save(entity);
	}

	@Override
	public void validate(StudentEntity entity) throws ValidationException {
		ValidationMessage.getInstance().validateEntity(entity).exception();
	}

	@Override
	public void update(ResultGrid result) {
		StudentEntity studentEntity = dao.findById(result.getIdStudent());
		if (studentEntity != null) {
			studentEntity.setFinalMedia(result.getFinalMedia());
			studentEntity.setFinalRecuperation(result.getMedRecovery());
			studentEntity.setFlgApproved(result.getFlgApproved());
			;
			dao.update(studentEntity);
		}
	}

	@Override
	public void update(Long id, String name) {
		StudentEntity entity = dao.findById(id);
		if (entity != null) {
			entity.setName(name);
			dao.update(entity);
		}
	}

	@Override
	public List<StudentEntity> findStudentBy(Long idGradebook) {
		return dao.findStudentBy(idGradebook);
	}

	@Override
	public void delete(Long idStudent) {
		StudentEntity studentEntity = dao.findById(idStudent);
		if (studentEntity != null) {
			dao.delete(studentEntity);
		}
	}

}
