package br.com.avfinal.mdl.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.avfinal.entity.AssessmentEntity;
import br.com.avfinal.entity.ResultDTO;
import br.com.avfinal.mdl.dao.AssessmentDao;
import br.com.avfinal.mdl.dao.GenericDAO;

@Repository
public class AssessmentDaoImpl extends GenericDAO<AssessmentEntity> implements AssessmentDao {
	
	public AssessmentDaoImpl() {
		super(AssessmentEntity.class);
	}

	@Override
	public List<AssessmentEntity> findAssessmentsBy(Integer bimester, Long idGradebook) {

		StringBuilder hql = new StringBuilder("FROM AssessmentEntity a");
		hql.append(" WHERE a.num_bimester =:bimester AND a.student.gradebook.id_gradebook =:idGradebook");
		
		final TypedQuery<AssessmentEntity> query = createQuery(hql.toString());
		query.setParameter("bimester", bimester);
		query.setParameter("idGradebook", idGradebook);
		
		return query.getResultList();
	}
	
	@Override
	public AssessmentEntity findBy(String tipAssessment, Integer numTipAssessment, Integer numBimester, Long idStudent) {
		StringBuilder hql = new StringBuilder("FROM AssessmentEntity a");
		hql.append(" WHERE a.tip_assessment =:tipAssessment");
		hql.append(" AND a.num_tip_assessment =:numTipAssessment");
		hql.append(" AND a.num_bimester =:numBimester");
		hql.append(" AND a.student.id_student =:idStudent");
		
		final TypedQuery<AssessmentEntity> query = createQuery(hql.toString());
		query.setParameter("tipAssessment", tipAssessment);
		query.setParameter("numTipAssessment", numTipAssessment);
		query.setParameter("numBimester", numBimester);
		query.setParameter("idStudent", idStudent);
		
		return getSingleResult(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResultDTO> findAssessmentsByGradebook(Long idGradebook) {

		List<ResultDTO> listResultado = new ArrayList<ResultDTO>();
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT AL.id_student, AL.nm_student, AV.tip_assessment, AV.num_tip_assessment, AV.num_bimester, AL.flg_approved,");
		hql.append(" AV.dat_assessment, AV.nota_assessment, RP.nota_recovery, AL.final_recuperation, AL.final_media"); 
		hql.append(" FROM AssessmentEntity AV");
		hql.append(" RIGHT JOIN AV.student AS AL");
		hql.append(" LEFT JOIN AV.recovery AS RP");
		hql.append(" RIGHT JOIN AL.gradebook AS DR");
		if (idGradebook != null) hql.append(" WHERE DR.id_gradebook =:idGradebook");

		final Query query = createQueryGeneric(hql.toString());
		if (idGradebook != null) query.setParameter("idGradebook", idGradebook);

		List<Object[]> objects = (List<Object[]>) query.getResultList();

		for (Object[] arrayObject : objects) {
			ResultDTO resultadoAv = new ResultDTO();
			resultadoAv.setIdStudent((Long)arrayObject[0]);
			resultadoAv.setNameStudent((String)arrayObject[1]);
			resultadoAv.setTipAssessement((String)arrayObject[2]);
			resultadoAv.setNumTipAssessment((Integer)arrayObject[3]);
			resultadoAv.setNumBimester((Integer)arrayObject[4]);
			resultadoAv.setFlg_aprovado((String)arrayObject[5]);
			resultadoAv.setDatAssessment((Date)arrayObject[6]);
			resultadoAv.setNotaAssessment((Double)arrayObject[7]);
			resultadoAv.setNotaRecovery((Double)arrayObject[8]);
			resultadoAv.setNotaRecoveryFinal((Double)arrayObject[9]);
			resultadoAv.setFinalMedia((Double)arrayObject[10]);
			listResultado.add(resultadoAv);
		}

		return listResultado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Integer, Integer> getQuantityAssessments() {
		HashMap<Integer, Integer> mapQtdAssessments = new HashMap<Integer, Integer>();
		
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT a.num_bimester, MAX(a.num_tip_assessment) AS num_tip FROM AssessmentEntity a");
		hql.append(" GROUP BY a.num_bimester, a.tip_assessment");

		List<Object[]> assessments = (List<Object[]>) createQueryGeneric(hql.toString()).getResultList();
		
		for (Object[] arrayObject : assessments) {
			
			final Integer numBimestre = (Integer) arrayObject[0];
			final Integer num_tip = (Integer) arrayObject[1];
			
			if (mapQtdAssessments.containsKey(numBimestre)) {
				int value = mapQtdAssessments.get(numBimestre);
				mapQtdAssessments.put(numBimestre, value + num_tip);
			} else {
				mapQtdAssessments.put(numBimestre, num_tip);
			}
		}
		
		return mapQtdAssessments;
	}

	@Override
	public Integer getQuantityAssessment(Integer bimester, String typeAssessment, Long idGradebook, Long idStudent) {
		
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT MAX(a.num_tip_assessment) AS qtd FROM AssessmentEntity a");
		hql.append(" WHERE a.num_bimester =:numBimester");
		hql.append(" AND a.tip_assessment =:tipAssessment");
		hql.append(" AND a.student.gradebook.id_gradebook =:idGradebook");
		
		if (idStudent != null) {
			hql.append(" AND a.student.id_student =:idStudent");
		}
		
		final Query query = createQueryGeneric(hql.toString());
		query.setParameter("numBimester", bimester);
		query.setParameter("tipAssessment", typeAssessment);
		query.setParameter("idGradebook", idGradebook);
		
		if (idStudent != null) {
			query.setParameter("idStudent", idStudent);
		}
		
		final Integer singleResult = getSingleResult(query);
		return singleResult == null ? 0 : singleResult;
	}
	

}
