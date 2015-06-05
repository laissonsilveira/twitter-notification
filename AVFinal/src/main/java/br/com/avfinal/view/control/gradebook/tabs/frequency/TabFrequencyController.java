package br.com.avfinal.view.control.gradebook.tabs.frequency;

import static br.com.avfinal.util.UtilAvFinal.getLongValue;
import static br.com.avfinal.util.UtilAvFinal.getValueBoolean;
import static br.com.avfinal.util.UtilAvFinal.setCurrentDate;
import static br.com.avfinal.util.enums.Constants.IS_CANCELED;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarPicker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.FrequencyDateEntity;
import br.com.avfinal.entity.FrequencyEntity;
import br.com.avfinal.entity.GradebookEntity;
import br.com.avfinal.entity.StudentEntity;
import br.com.avfinal.entity.grid.StudentGrid;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.mdl.service.FrequencyService;
import br.com.avfinal.mdl.service.StudentService;
import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.listCell.BimesterListCell;
import br.com.avfinal.view.component.table.ButtonCellAvFinal;
import br.com.avfinal.view.component.table.ButtonCellAvFinal.ButtonCellOnClick;
import br.com.avfinal.view.component.table.ButtonCellAvFinal.TypeButton;
import br.com.avfinal.view.component.table.EditingCellAvFinal;
import br.com.avfinal.view.component.tabs.TabFrequency;
import br.com.avfinal.view.control.IGenericController;
import br.com.avfinal.view.control.gradebook.GradebookController;

@Component
public class TabFrequencyController implements IGenericController {

	private static final Logger LOG = Logger.getLogger(TabFrequencyController.class);

	@Autowired
	private Stage stagePrimary;
	@Autowired
	private StudentService studentService;
	@Autowired
	private FrequencyService frequencyService;
	@Autowired
	private GradebookController controllerParent;

	private TabFrequency view;

	private boolean containsFrequencyInDateSelected = true;

	private String oldValue;
	private CalendarPicker frequencyCalendar;
	private ObservableList<StudentGrid> studentsListGrid;

	@Override
	public void initializeController() {
		if (view != null) {
			initComponet();
			initConfig();
			initValues();
		} else {
			LOG.fatal("View não carregada");
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString());
		}
	}

	public void initializeController(TabFrequency tabFrequency) {
		view = tabFrequency;
		initializeController();
	}

	@Override
	public void initConfig() {
		studentsListGrid.clear();
		setConfigComboBimester();
		setConfigComboClass();
		setPropertiesTxtNewStudent();
	}

	@Override
	public void initComponet() {
		studentsListGrid = FXCollections.observableArrayList();
		frequencyCalendar = getCalendarPicker();
		createCalendar();
		createTable();
	}

	private void createTable() {
		view.getGridFrequency().add(view.newInstanceTableFrequency(), 0, 0, 1, 3);
		setPropertiesTable();
	}

	private CalendarPicker getCalendarPicker() {
		final CalendarPicker calendarPicker = new CalendarPicker();
		calendarPicker.setId("calendar");
		return calendarPicker;
	}

	@Override
	public void initValues() {
		setCurrentDate(frequencyCalendar);
		view.getCmbQtdClass().setValue(getQtdClass(frequencyCalendar.calendarProperty().getValue()).toString());
	}

	private Integer getQtdClass(Calendar calendar) {
		FrequencyDateEntity entity = frequencyService.findQtdClassBy(calendar.getTime());
		containsFrequencyInDateSelected = ((entity != null) && (entity.getFlgQtdClass() != null));
		setBimester(entity == null ? null : entity.getNumBimester());
		return containsFrequencyInDateSelected ? entity.getFlgQtdClass() : 1;
	}

	private void setBimester(Integer numBimester) {
		view.getCmbBimester().setDisable(containsFrequencyInDateSelected);
		view.getCmbBimester().setValue(Bimester.valueOf(numBimester));
	}

	@SuppressWarnings("unchecked")
	private void setPropertiesTable() {
		view.getTableFrequency().getProperties().put(Stage.class, stagePrimary);
		view.getTableFrequency().setItems(studentsListGrid);

		setPropertiesColumnStudent();
		view.getColumnDeleteFrequency().setCellFactory(p -> new ButtonCellAvFinal(stagePrimary, getButtonCallBack(), TypeButton.DELETE));
	}

	private void setConfigComboBimester() {
		view.getCmbBimester().setCellFactory(param -> new BimesterListCell());
		view.getCmbBimester().setItems(FXCollections.observableArrayList(Bimester.values()));
	}

	private void setConfigComboClass() {
		view.getCmbQtdClass().valueProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
			if (newValue != null) {
				if ((frequencyCalendar.calendarProperty().getValue() != null) && (view.getCmbQtdClass().getValue() != null)) {
					createTable();
					findFrequencyStudends();
				} else {
					studentsListGrid.clear();
				}
			}
		});
	}

	private void setPropertiesTxtNewStudent() {
		view.getMatriculation().addEventFilter(KeyEvent.KEY_TYPED, inputevent -> {
			if (!inputevent.getCharacter().matches("\\d")) {
				inputevent.consume();
			}
		});
	}

	private void createCalendar() {
		frequencyCalendar.setLocale(Locale.getDefault());
		frequencyCalendar.calendarProperty().addListener((ChangeListener<Calendar>) (observable, oldValue, newValue) -> {
			if (newValue != null) {
				Date dateSelected = newValue.getTime();
				Date dateToday = new Date();
				int dateCompare = dateSelected.compareTo(dateToday);
				if (dateCompare <= 0) {
					final String qtdClass = getQtdClass(newValue).toString();
					if (qtdClass.equals(view.getCmbQtdClass().getValue())) {
						createTable();
						findFrequencyStudends();
					} else {
						view.getCmbQtdClass().setValue(qtdClass);
					}
				} else {
					studentsListGrid.clear();
				}
			}
		});
		view.getGridFrequency().add(frequencyCalendar, 1, 0, 1, 1);
	}

	private void addStudentInGrid() {
		for (StudentEntity studentEntity : controllerParent.getListStudentsGradebook()) {
			StudentGrid a = new StudentGrid();
			a.setId(studentEntity.getId());
			a.setName(studentEntity.getName());
			a.setIdGradebook(studentEntity.getGradebook().getId());
			a.setMatriculation(studentEntity.getMatriculation());
			studentsListGrid.add(a);
		}
	}

	private void findFrequencyStudends() {

		Date dateSelected = frequencyCalendar.calendarProperty().getValue().getTime();
		Date dateToday = new Date();
		int dateCompare = dateSelected.compareTo(dateToday);
		if (dateCompare <= 0) {
			if (studentsListGrid.isEmpty()) {
				addStudentInGrid();
			}

			try {
				for (StudentGrid studentGrid : studentsListGrid) {
					if (containsFrequencyInDateSelected) {
						FrequencyEntity frequency = frequencyService.findBy(studentGrid.getId(), dateSelected);
						if (frequency == null) {
							setFrequencyStudent(studentGrid);
							continue;
						}
						studentGrid.setFlgFrequency01(getValueBoolean(frequency.getFlg_Frequency01()));
						studentGrid.setFlgFrequency02(getValueBoolean(frequency.getFlg_Frequency02()));
						studentGrid.setFlgFrequency03(getValueBoolean(frequency.getFlg_Frequency03()));
						studentGrid.setFlgFrequency04(getValueBoolean(frequency.getFlg_Frequency04()));
						studentGrid.setIdFrequency(frequency.getId());
					} else {
						setFrequencyStudent(studentGrid);
					}
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				new MessageDisplay(view, Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();

			}
		} else {
			studentsListGrid.clear();
		}
	}

	private void setFrequencyStudent(StudentGrid studentGrid) {
		studentGrid.setFlgFrequency01(false);
		studentGrid.setFlgFrequency02(false);
		studentGrid.setFlgFrequency03(false);
		studentGrid.setFlgFrequency04(false);
	}

	private void setPropertiesColumnStudent() {
		view.getColumnStudent().setCellValueFactory(new PropertyValueFactory<StudentGrid, String>("name"));
		view.getColumnStudent().setCellFactory(getCellFactory());
		view.getColumnStudent().getProperties().put(Constants.IS_REQUIRED_VALUE.valuesToString(), Boolean.TRUE);
		view.getColumnStudent().setOnEditStart(
				ev -> oldValue = ev.getTableView().getItems().get(ev.getTablePosition().getRow()).getName().toUpperCase());
		view.getColumnStudent().setOnEditCommit(ev -> {
			ev.getTableView().getItems().get(ev.getTablePosition().getRow()).setName(ev.getNewValue().toUpperCase());
			updateRowTable(ev);
		});
	}

	private void updateRowTable(CellEditEvent<StudentGrid, String> ev) {
		try {
			if (!oldValue.equalsIgnoreCase(ev.getNewValue())) {
				final StudentGrid studentGrid = ev.getTableView().getItems().get(ev.getTablePosition().getRow());
				studentService.update(studentGrid.getId(), studentGrid.getName().toUpperCase());
			}
		} catch (Exception e) {
			ev.getTableColumn().getProperties().put(IS_CANCELED.valuesToString(), Boolean.TRUE);
			LOG.error(e.getMessage(), e);
			new MessageDisplay(view, Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
		}
	}

	private ButtonCellOnClick getButtonCallBack() {
		return indexSelected -> {
			Map<Boolean, String> result = new HashMap<Boolean, String>();
			try {
				studentService.delete(studentsListGrid.get(indexSelected).getId());
				studentsListGrid.remove(indexSelected);
				result.put(Boolean.TRUE, Constants.MSG_SUCESS_REMOVE.valuesToString());
			} catch (Exception e) {
				result.put(Boolean.FALSE, e.toString());
			}
			return result;
		};
	}

	public void saveFrequency() {
		try {
			frequencyService.save(studentsListGrid, frequencyCalendar.getCalendar(),//
					view.getCmbBimester().getValue(), view.getCmbQtdClass().getValue());
			new MessageDisplay(view, Constants.MSG_SUCESS_SAVE.valuesToString()).showSucessMessage();
		} catch (ValidationException e) {
			LOG.error(e.getMessage(), e);
			new MessageDisplay(view, e.getMessage(), e.getFieldValidationMessageList()).showErrorMessage();
		}
	}

	public void addNewStudent() {

		StudentGrid studentGrid = new StudentGrid(view.getNm_student().getText().toUpperCase(), getLongValue(view.getMatriculation().getText()),
				controllerParent.getGradebookEntity().getId());
		if (existsStudent(studentGrid)) {
			try {
				StudentEntity entity = new StudentEntity(studentGrid.getName(), studentGrid.getMatriculation());
				entity.setGradebook(new GradebookEntity(studentGrid.getIdGradebook()));
				entity = studentService.save(entity);
				studentGrid.setId(entity.getId());
				studentsListGrid.add(studentGrid);
				controllerParent.getListStudentsGradebook().add(entity);
				clearNewAluno();
			} catch (ValidationException e) {
				LOG.error(e.getMessage(), e);
				new MessageDisplay(view, e.getMessage(), e.getFieldValidationMessageList()).showErrorMessage();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				new MessageDisplay(view, Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
			}
		} else {
			view.getNm_student().requestFocus();
		}

	}

	private boolean existsStudent(StudentGrid studentGrid) {
		if (studentsListGrid.contains(studentGrid)) {
			new MessageDisplay(view, Constants.MSG_ERROR_STUDENT_EXISTS.valuesToString(), //
					view.getNm_student().idProperty()).showErrorMessage();
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	private void clearNewAluno() {
		view.getNm_student().clear();
		view.getMatriculation().clear();
	}

	private Callback<TableColumn<StudentGrid, String>, TableCell<StudentGrid, String>> getCellFactory() {
		return param -> new EditingCellAvFinal<StudentGrid, String>(view.getColumnStudent());
	}

}
