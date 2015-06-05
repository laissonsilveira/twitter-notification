package br.com.avfinal.mdl.service;

import java.util.HashMap;
import java.util.List;

import javafx.collections.ObservableList;
import br.com.avfinal.entity.AssessmentEntity;
import br.com.avfinal.entity.ResultDTO;
import br.com.avfinal.entity.grid.AssessmentGrid;
import br.com.avfinal.exception.ValidationException;

public interface AssessmentService {
	
	void save(AssessmentEntity entity) throws ValidationException;
	
	List<ResultDTO> findAssessmentsByGradebook(Long idGradebook);

	HashMap<Integer, Integer> getQuantityAssessments();

	List<AssessmentEntity> findAssessmentBy(Integer bimester, Long idGradebook) ;

	ObservableList<AssessmentGrid> loadItemsTableAssessment(Integer bimester, Long idGradebook, boolean isAssessment);

	Integer getQuantityAssessment(Integer bimester, String typeAssessment, Long idGradebook, Long idStudent);

	void update(Long idStudent, String typeAssessment, Integer bimester, Integer numTypeAssessment, Double notaAssessment, boolean isAssessment);
	
}
