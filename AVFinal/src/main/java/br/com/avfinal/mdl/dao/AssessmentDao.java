package br.com.avfinal.mdl.dao;

import java.util.HashMap;
import java.util.List;

import br.com.avfinal.entity.AssessmentEntity;
import br.com.avfinal.entity.ResultDTO;

public interface AssessmentDao {

	AssessmentEntity save(AssessmentEntity entity);

	AssessmentEntity update(AssessmentEntity assessment);

	List<ResultDTO> findAssessmentsByGradebook(Long idGradebook);

	HashMap<Integer, Integer> getQuantityAssessments();

	Integer getQuantityAssessment(Integer bimester, String typeAssessment, Long idGradebook, Long idStudent);

	List<AssessmentEntity> findAssessmentsBy(Integer bimester, Long idGradebook);

	AssessmentEntity findBy(String tipAssessment, Integer numTipAssessment, Integer numBimestre, Long idStudent);

	AssessmentEntity findById(Long idAssessment);

}
