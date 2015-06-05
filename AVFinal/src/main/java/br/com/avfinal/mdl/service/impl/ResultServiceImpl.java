package br.com.avfinal.mdl.service.impl;

import static br.com.avfinal.util.UtilAvFinal.isEmpty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.avfinal.entity.ResultDTO;
import br.com.avfinal.entity.grid.ResultGrid;
import br.com.avfinal.mdl.service.AssessmentService;
import br.com.avfinal.mdl.service.ResultService;
import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.util.enums.FlgApproved;

@Service
@Transactional
public class ResultServiceImpl implements ResultService {

	private static HashMap<Integer, Integer> mapQtdAssessment;

    @Autowired
    private AssessmentService assessmentService;

	@Override
	public ObservableList<ResultGrid> calculateAverages(Long idGradeBook) {

		ObservableList<ResultGrid> gridResults = FXCollections.observableArrayList();

		List<ResultDTO> listResultDTO = assessmentService.findAssessmentsByGradebook(idGradeBook);
		Map<Long, List<ResultDTO>> mapResult = new HashMap<Long, List<ResultDTO>>();
		ResultGrid result = null;

		for (ResultDTO res : listResultDTO) {
			Long aluno = res.getIdStudent();
			if (mapResult.containsKey(aluno)) {
				mapResult.get(aluno).add(res);
			} else {
				mapResult.put(aluno, new ArrayList<ResultDTO>());
				mapResult.get(aluno).add(res);
			}
		}

		for (Object key : mapResult.keySet()) {
			List<ResultDTO> listResultByStudent = mapResult.get(key);

			result = new ResultGrid();
			result.setIdStudent(listResultByStudent.get(0).getIdStudent());
			result.setNameStudent(listResultByStudent.get(0).getNameStudent());
			result.setMedBimester01(getMediaBimestre(Bimester.PRIMEIRO, listResultByStudent));
			result.setMedBimester02(getMediaBimestre(Bimester.SEGUNDO, listResultByStudent));
			result.setMedBimester03(getMediaBimestre(Bimester.TERCEIRO, listResultByStudent));
			result.setMedBimester04(getMediaBimestre(Bimester.QUARTO, listResultByStudent));
			result.setFinalMedia(getMediaFinal(result, listResultByStudent));
			result.setMedRecovery(getNotaRecoveryFinal(listResultByStudent));
			result.setFlgApproved(getFlgAprovado(result, listResultByStudent));

			gridResults.add(result);
		}

		return gridResults;
	}

	private String getFlgAprovado(ResultGrid result, List<ResultDTO> listResultByStudent) {

		if (result == null) {
			return "";
		} else if (!isEmpty(listResultByStudent.get(0).getFlgApproved())) {
			return listResultByStudent.get(0).getFlgApproved();
		} else if (result.getFinalMedia() >= Constants.CONST_MEDIA_BIMONTHLY_MINIMA.valuesToDouble()) {
			return FlgApproved.APPROVED.getValue();
		} else {
			return FlgApproved.DISAPPROVED.getValue();
		}
	}

	private Double getMediaBimestre(Bimester bimestre, List<ResultDTO> listResultByStudent) {

		Double media = 0D;
		List<Double> notas = new ArrayList<Double>();

		for (ResultDTO r : listResultByStudent) {
			if (bimestre.getNumber().equals(r.getNumBimester())) {
				if ((r.getNotaRecovery() != null) && (r.getNotaRecovery() > r.getNotaAssessment())) {
					notas.add(r.getNotaRecovery());
				} else {
					notas.add(r.getNotaAssessment());
				}
			}
		}

		if (notas.isEmpty()) {
			return 0D;
		}

		for (Double n : notas) {
			media += n;
		}

		BigDecimal resultArredondado = new BigDecimal(media / getQuantityAssessments().get(bimestre.getNumber()));
		return resultArredondado.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	private Double getMediaFinal(ResultGrid result, List<ResultDTO> listResultByStudent) {

		// if(isEmpty(listResultByStudent.get(0).getFinalMedia())) {
		// return 0D;
		// }

		Double media = (result.getMedBimester01() + result.getMedBimester02() + result.getMedBimester03() + result.getMedBimester04()) / 4;

		BigDecimal resultArredondado = new BigDecimal(media);
		media = resultArredondado.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

		return media;
	}

	private Double getNotaRecoveryFinal(List<ResultDTO> listResultByStudent) {
		final Double notaRecoveryFinal = listResultByStudent.get(0).getNotaRecoveryFinal();
		return notaRecoveryFinal == null ? 0 : notaRecoveryFinal;
	}

	public HashMap<Integer, Integer> getQuantityAssessments() {
		if (mapQtdAssessment == null) {
			mapQtdAssessment = assessmentService.getQuantityAssessments();
		}
		return mapQtdAssessment;
	}

}
