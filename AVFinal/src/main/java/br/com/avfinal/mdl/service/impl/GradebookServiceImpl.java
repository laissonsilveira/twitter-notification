package br.com.avfinal.mdl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.avfinal.entity.GradebookEntity;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.exception.ValidationMessage;
import br.com.avfinal.mdl.dao.GradebookDao;
import br.com.avfinal.mdl.service.GradebookService;

@Service
@Transactional
public class GradebookServiceImpl implements GradebookService {

	@Autowired
	private GradebookDao dao;

	@Override
	public List<GradebookEntity> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(GradebookEntity entity) throws ValidationException {
		validate(entity);
		dao.save(entity);
	}

	@Override
	public void validate(GradebookEntity entity) throws ValidationException {
		ValidationMessage.getInstance().validateEntity(entity).exception();
	}

	@Override
	public void update(GradebookEntity entity) {
		dao.update(entity);
	}

}
