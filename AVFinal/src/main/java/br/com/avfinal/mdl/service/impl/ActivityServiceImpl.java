package br.com.avfinal.mdl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.avfinal.entity.ActivityEntity;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.exception.ValidationMessage;
import br.com.avfinal.mdl.dao.ActivityDao;
import br.com.avfinal.mdl.service.ActivityService;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityDao activityDao;

	@Override
	public ActivityEntity save(ActivityEntity entity) throws ValidationException {
		validate(entity);
		return activityDao.save(entity);
	}

	@Override
	public void update(ActivityEntity entity) throws ValidationException {
		validate(entity);
		activityDao.update(entity);
	}

	@Override
	public void delete(ActivityEntity entity) {
		activityDao.delete(entity);
	}

	@Override
	public List<ActivityEntity> findAll(Long idGradebook) {
		return activityDao.findBy(idGradebook);
	}

	private void validate(ActivityEntity entity) throws ValidationException {
		ValidationMessage.getInstance().validateEntity(entity).exception();
	}

}
