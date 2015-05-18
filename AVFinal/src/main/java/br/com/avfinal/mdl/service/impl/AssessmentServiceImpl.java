package br.com.avfinal.mdl.service.impl;

import static br.com.avfinal.entity.grid.AssessmentGrid.AVALIACAO;
import static br.com.avfinal.entity.grid.AssessmentGrid.PARTICIPACAO;
import static br.com.avfinal.entity.grid.AssessmentGrid.TRABALHO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.avfinal.entity.AssessmentEntity;
import br.com.avfinal.entity.RecoveryEntity;
import br.com.avfinal.entity.ResultDTO;
import br.com.avfinal.entity.grid.AssessmentGrid;
import br.com.avfinal.entity.grid.AssessmentGrid.Operator;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.exception.ValidationMessage;
import br.com.avfinal.mdl.dao.AssessmentDao;
import br.com.avfinal.mdl.dao.RecoveryDao;
import br.com.avfinal.mdl.service.AssessmentService;
import br.com.avfinal.util.enums.TypeAssessment;

@Service
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

	@Autowired
	private AssessmentDao dao;

	@Autowired
	private RecoveryDao recoveryDao;

	@Override
	public void save(AssessmentEntity entity) throws ValidationException {
		validate(entity);
		if (entity.getTipAssessment().equals(TypeAssessment.RECUPERAÇÃO.getType())) {
			AssessmentEntity assessmentEntity = dao.findBy(entity.getTipAssessmentRecovery(), entity.getNumTipAssessment(), entity.getNumBimester(),
					entity.getStudent().getId());

			if ((assessmentEntity != null) && (entity != null)) {
				RecoveryEntity recoveryEntity = new RecoveryEntity();
				recoveryEntity.setAssessment(assessmentEntity);
				recoveryEntity.setDatAssessment(entity.getDatAssessment());
				recoveryEntity.setDesAssessment(entity.getDesAssessment());
				recoveryEntity.setDesObservation(entity.getDesObservation());
				recoveryEntity.setNotaRecovery(entity.getNotaAssessment());
				recoveryDao.save(recoveryEntity);
			}

		} else if (entity.getNumTipAssessment() == null) {
			entity.setNumTipAssessment(getQuantityAssessment(entity.getNumBimester(), entity.getTipAssessment(), entity.getStudent().getGradebook()
					.getId(), entity.getStudent().getId()) + 1);
			dao.save(entity);
		}
	}

	public void validate(AssessmentEntity entity) throws ValidationException {
		ValidationMessage.getInstance().validateEntity(entity).exception();
	}

	@Override
	public ObservableList<AssessmentGrid> loadItemsTableAssessment(Integer bimester, Long idGradebook, boolean isAssessment) {

		ObservableList<AssessmentGrid> listAssessment = FXCollections.observableArrayList();
		List<AssessmentEntity> listAvDTO = new ArrayList<AssessmentEntity>();

		if (isAssessment) {
			listAvDTO = findAssessmentBy(bimester, idGradebook);
		} else {
			final List<RecoveryEntity> recoveries = recoveryDao.findRecoveriesBy(bimester, idGradebook);
			for (RecoveryEntity recoveryEntity : recoveries) {
				AssessmentEntity assessmentEntity = new AssessmentEntity();
				assessmentEntity.setStudent(recoveryEntity.getAssessment().getStudent());
				assessmentEntity.setDatAssessment(recoveryEntity.getDatAssessment());
				assessmentEntity.setDesAssessment(recoveryEntity.getDesAssessment());
				assessmentEntity.setDesObservation(recoveryEntity.getDes_observation());
				assessmentEntity.setNotaAssessment(recoveryEntity.getNotaRecovery());
				assessmentEntity.setNumBimester(recoveryEntity.getAssessment().getNumBimester());
				assessmentEntity.setNumTipAssessment(recoveryEntity.getAssessment().getNumTipAssessment());
				assessmentEntity.setTipAssessment(recoveryEntity.getAssessment().getTipAssessment());
				listAvDTO.add(assessmentEntity);
			}
		}

		if (listAvDTO != null) {
			for (AssessmentEntity avDTO : listAvDTO) {

				AssessmentGrid avGrid = new AssessmentGrid();
				avGrid.setIdStudent(avDTO.getStudent().getId());
				avGrid.setNameStudent(avDTO.getStudent().getName());

				switch (avDTO.getTipAssessment()) {
					case PARTICIPACAO:
						makeAssessmentGrid(listAssessment, avDTO, avGrid);
						break;
					case AVALIACAO:
						makeAssessmentGrid(listAssessment, avDTO, avGrid);
						break;
					case TRABALHO:
						makeAssessmentGrid(listAssessment, avDTO, avGrid);
						break;
					default:
						break;
				}
			}
		}

		return listAssessment;
	}

	private void makeAssessmentGrid(ObservableList<AssessmentGrid> listAssessments, AssessmentEntity avDTO, AssessmentGrid avGrid) {
		if (listAssessments.contains(avGrid)) {
			for (AssessmentGrid assessmentGrid : listAssessments) {
				if (assessmentGrid.equals(avGrid)) {
					setNumMaxAssessment(avDTO, assessmentGrid);
					assessmentGrid.actionWithNotes(avDTO.getNumTipAssessment(), avDTO.getTipAssessment(), avDTO.getNotaAssessment(), Operator.SET);
					break;
				}
			}
		} else {
			setNumMaxAssessment(avDTO, avGrid);
			avGrid.actionWithNotes(avDTO.getNumTipAssessment(), avDTO.getTipAssessment(), avDTO.getNotaAssessment(), Operator.SET);
			listAssessments.add(avGrid);
		}
	}

	private void setNumMaxAssessment(AssessmentEntity avDTO, AssessmentGrid assessmentGrid) {
		switch (avDTO.getTipAssessment()) {
			case PARTICIPACAO:
				if (assessmentGrid.getNum_max_assessment_P() < avDTO.getNumTipAssessment()) {
					assessmentGrid.setNum_max_avaliacao_P(avDTO.getNumTipAssessment());
				}
				break;
			case AVALIACAO:
				if (assessmentGrid.getNum_max_assessment_A() < avDTO.getNumTipAssessment()) {
					assessmentGrid.setNum_max_avaliacao_A(avDTO.getNumTipAssessment());
				}
				break;
			case TRABALHO:
				if (assessmentGrid.getNum_max_assessment_T() < avDTO.getNumTipAssessment()) {
					assessmentGrid.setNum_max_avaliacao_T(avDTO.getNumTipAssessment());
				}
				break;
			default:
				break;
		}
	}

	@Override
	public List<ResultDTO> findAssessmentsByGradebook(Long idGradebook) {
		return dao.findAssessmentsByGradebook(idGradebook);
	}

	@Override
	public HashMap<Integer, Integer> getQuantityAssessments() {
		return dao.getQuantityAssessments();
	}

	@Override
	public Integer getQuantityAssessment(Integer bimester, String typeAssessment, Long idGradebook, Long idStudent) {
		return dao.getQuantityAssessment(bimester, typeAssessment, idGradebook, idStudent);
	}

	@Override
	public List<AssessmentEntity> findAssessmentBy(Integer bimester, Long idGradebook) {
		return dao.findAssessmentsBy(bimester, idGradebook);
	}

	@Override
	public void update(Long idStudent, String typeAssessment, Integer bimester, Integer numTypeAssessment, Double notaAssessment, boolean isAssessment) {
		if (idStudent != null) {
			final AssessmentEntity entity = dao.findBy(typeAssessment, numTypeAssessment, bimester, idStudent);
			if (isAssessment) {
				entity.setNotaAssessment(notaAssessment);
				dao.update(entity);
			} else {
				RecoveryEntity recoveryEntity = recoveryDao.findBy(entity.getId());
				if (recoveryEntity != null) {
					recoveryEntity.setNotaRecovery(notaAssessment);
					recoveryDao.update(recoveryEntity);
				}

			}
		}

	}

}
