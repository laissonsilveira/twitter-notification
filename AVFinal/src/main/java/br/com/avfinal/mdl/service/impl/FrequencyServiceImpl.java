package br.com.avfinal.mdl.service.impl;

import static br.com.avfinal.util.UtilAvFinal.getYesNoValueString;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.avfinal.entity.FrequencyDateEntity;
import br.com.avfinal.entity.FrequencyEntity;
import br.com.avfinal.entity.StudentEntity;
import br.com.avfinal.entity.grid.StudentGrid;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.exception.ValidationMessage;
import br.com.avfinal.mdl.dao.FrequencyDao;
import br.com.avfinal.mdl.service.FrequencyService;
import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;

@Service
@Transactional
public class FrequencyServiceImpl implements FrequencyService {

	@Autowired
	private FrequencyDao dao;

	@Override
	public void save(List<StudentGrid> listStudent, Calendar dateToSave, Bimester bimester, String qtdClass) throws ValidationException {

		validateSaveWith(listStudent, dateToSave, bimester, qtdClass);

		FrequencyDateEntity frequencyDate = new FrequencyDateEntity();
		frequencyDate.setDatFrequency(dateToSave.getTime());
		frequencyDate.setNumBimester(bimester.getNumber());
		frequencyDate.setFlgQtdClass(Integer.valueOf(qtdClass));

		for (StudentGrid studentGrid : listStudent) {
			FrequencyEntity frequencyEntity = new FrequencyEntity();
			frequencyEntity.setId(studentGrid.getIdFrequency());
			frequencyEntity.setStudent(new StudentEntity(studentGrid.getId()));
			frequencyEntity.setFrequencyDate(frequencyDate);

			if (qtdClass.equals("1")) {
				frequencyEntity.setFlgFrequency01(getYesNoValueString(studentGrid.getFlgFrequency01().getValue()));
				frequencyEntity.setFlgFrequency02(null);
				frequencyEntity.setFlgFrequency03(null);
				frequencyEntity.setFlgFrequency04(null);
			} else if (qtdClass.equals("2")) {
				frequencyEntity.setFlgFrequency01(getYesNoValueString(studentGrid.getFlgFrequency01().getValue()));
				frequencyEntity.setFlgFrequency02(getYesNoValueString(studentGrid.getFlgFrequency02().getValue()));
				frequencyEntity.setFlgFrequency03(null);
				frequencyEntity.setFlgFrequency04(null);
			} else if (qtdClass.equals("3")) {
				frequencyEntity.setFlgFrequency01(getYesNoValueString(studentGrid.getFlgFrequency01().getValue()));
				frequencyEntity.setFlgFrequency02(getYesNoValueString(studentGrid.getFlgFrequency02().getValue()));
				frequencyEntity.setFlgFrequency03(getYesNoValueString(studentGrid.getFlgFrequency03().getValue()));
				frequencyEntity.setFlgFrequency04(null);
			} else if (qtdClass.equals("4")) {
				frequencyEntity.setFlgFrequency01(getYesNoValueString(studentGrid.getFlgFrequency01().getValue()));
				frequencyEntity.setFlgFrequency02(getYesNoValueString(studentGrid.getFlgFrequency02().getValue()));
				frequencyEntity.setFlgFrequency03(getYesNoValueString(studentGrid.getFlgFrequency03().getValue()));
				frequencyEntity.setFlgFrequency04(getYesNoValueString(studentGrid.getFlgFrequency04().getValue()));
			}
			dao.save(frequencyEntity);
		}

	}

	private void validateSaveWith(List<StudentGrid> listStudent, Calendar data, Bimester bimester, String qtdClass) throws ValidationException {
		ValidationMessage message = new ValidationMessage();

		if ((listStudent == null) || listStudent.isEmpty()) {
			message.add("tableFrequency", Constants.MSG_ERROR_SELECT_STUDENT.valuesToString());
		}
		if (data == null) {
			message.add("calendar", Constants.MSG_ERROR_SELECT_DATE.valuesToString());
		}
		if (qtdClass == null) {
			message.add("cmbQtdClass", Constants.MSG_FIELD_REQUIRED.valuesToString());
		}
		if (bimester == null) {
			message.add("cmbBimester", Constants.MSG_FIELD_REQUIRED.valuesToString());
		}

		message.exception();
	}

	@Override
	public FrequencyEntity findBy(Long idStudent, Date date) {
		return dao.findBy(idStudent, date);
	}

	@Override
	public List<FrequencyEntity> findAll() {
		return dao.findAll();
	}

	@Override
	public List<FrequencyEntity> findByGradebook(Long idGradebook, Integer numBimester) {
		return dao.findByGradebook(idGradebook, numBimester);
	}

	@Override
	public FrequencyDateEntity findQtdClassBy(Date date) {
		return dao.findQtdClassBy(date);
	}

}
