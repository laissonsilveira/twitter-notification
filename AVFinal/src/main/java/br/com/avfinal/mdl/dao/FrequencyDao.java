package br.com.avfinal.mdl.dao;

import java.util.Date;
import java.util.List;

import br.com.avfinal.entity.FrequencyDateEntity;
import br.com.avfinal.entity.FrequencyEntity;

public interface FrequencyDao {

	FrequencyEntity findBy(Long idStudent, Date date);

	FrequencyEntity findById(Long id);

	FrequencyEntity save(FrequencyEntity frequencyEntity);

	List<FrequencyEntity> findAll();

	List<FrequencyEntity> findByGradebook(Long idGradebook, Integer numBimester);

	FrequencyDateEntity findQtdClassBy(Date date);

}
