package br.com.avfinal.mdl.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.avfinal.entity.FrequencyDateEntity;
import br.com.avfinal.entity.FrequencyEntity;
import br.com.avfinal.entity.grid.StudentGrid;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.util.enums.Bimester;

public interface FrequencyService {

	void save(List<StudentGrid> alunos, Calendar calendar, Bimester bimester, String qtdClass) throws ValidationException;

	FrequencyEntity findBy(Long idStudent, Date date);

	List<FrequencyEntity> findAll();

	List<FrequencyEntity> findByGradebook(Long idGradebook, Integer numBimester);

	FrequencyDateEntity findQtdClassBy(Date date);

}
