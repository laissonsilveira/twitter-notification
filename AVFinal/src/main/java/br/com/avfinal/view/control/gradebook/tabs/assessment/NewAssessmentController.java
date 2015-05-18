package br.com.avfinal.view.control.gradebook.tabs.assessment;

import static br.com.avfinal.util.UtilAvFinal.createBigDecimalField;
import static br.com.avfinal.util.UtilAvFinal.onChangeDateCalendar;
import static br.com.avfinal.util.UtilAvFinal.setCurrentDate;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.BigDecimalField;
import jfxtras.scene.control.CalendarPicker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.AssessmentEntity;
import br.com.avfinal.entity.StudentEntity;
import br.com.avfinal.mdl.service.AssessmentService;
import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.util.enums.TypeAssessment;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.event.GridLoadEvent;
import br.com.avfinal.view.component.factory.ControllerFactory;
import br.com.avfinal.view.component.listCell.BimesterListCell;
import br.com.avfinal.view.component.listCell.StudentListCell;
import br.com.avfinal.view.component.tabs.TabAssessment;
import br.com.avfinal.view.control.IGenericController;
import br.com.avfinal.view.control.gradebook.GradebookController;

@Component
public class NewAssessmentController implements IGenericController {

	private final static Logger LOG = Logger.getLogger(TabAssessment.class);

	@FXML
	private URL location;
	@FXML
	private ResourceBundle resources;

	@FXML
	private ComboBox<StudentEntity> cmbBoxAlunoNewAval;
	@FXML
	private ComboBox<Bimester> cmbBoxBimestreNewAval;
	@FXML
	private ComboBox<TypeAssessment> cmbBoxTipAval;
	@FXML
	private TextArea txtAreaObs;
	@FXML
	private TextField txtDescAval;
	@FXML
	private VBox vBoxCalendarAv;
	@FXML
	private VBox vBoxNota;

	@Autowired
	private Stage stagePrimary;
	@Autowired
	private GradebookController parent;
	@Autowired
	protected TabAssessment tabAssessment;
	@Autowired
	protected ApplicationContext context;
	@Autowired
	private AssessmentService assessmentService;
	@Autowired
	private ControllerFactory controllerFactory;

	private Stage stageNewAval;

	private BigDecimalField txtNota;
	private StudentEntity studentAv;
	protected Integer numberAssessmentForRecovery;
	protected String typeAssessmentForRecovery;
	private CalendarPicker calendarAssessment;

	@Override
	public void initComponet() {
		calendarAssessment = new CalendarPicker();
		txtNota = createBigDecimalField();
	}

	@Override
	public void initConfig() {
		createCalendar();
		createFieldNota();
		setConfigComboAluno();
		setConfigComboBimestre();
		setConfigComboTipAval();
	}

	@FXML
	public void initialize() {
		initComponet();
		initConfig();
		initValues();
	}

	@Override
	public void initializeController() {}

	@Override
	public void initValues() {
		setCurrentDate(calendarAssessment);
	}

	public void show() throws IOException {
		stageNewAval = new Stage();
		if (stagePrimary != null) {
			stageNewAval.initOwner(stagePrimary);
		}

		FXMLLoader FXMLloader = new FXMLLoader(getClass().getResource(Constants.PATH_FXML_NEW_ASSESSMENT_MODAL.valuesToString()));
		FXMLloader.setControllerFactory(controllerFactory);
		Parent parent = FXMLloader.load();
		Scene scene = new Scene(parent);
		stageNewAval.setTitle(Constants.TITLE_NEW_ASSESSMENT.valuesToString());
		stageNewAval.setScene(scene);
		stageNewAval.setFullScreen(false);
		stageNewAval.setResizable(false);
		stageNewAval.centerOnScreen();
		stageNewAval.initModality(Modality.WINDOW_MODAL);
		stageNewAval.show();
	}

	private void clear() {
		txtAreaObs.clear();
		cmbBoxAlunoNewAval.setValue(null);
		txtNota.setNumber(null);
	}

	protected void clearAll() {
		setCurrentDate(calendarAssessment);
		cmbBoxTipAval.setValue(null);
		cmbBoxBimestreNewAval.setValue(null);
		txtDescAval.clear();
		txtAreaObs.clear();
		clear();
	}

	@FXML
	protected void closeNewAssessment() {
		stageNewAval.close();
	}

	private void createCalendar() {

		calendarAssessment.setLocale(Locale.getDefault());
		calendarAssessment.calendarProperty().addListener(onChangeDateCalendar(stageNewAval, calendarAssessment));
		calendarAssessment.setMaxWidth(290);
		calendarAssessment.setMinWidth(290);
		calendarAssessment.setPrefWidth(290);

		vBoxCalendarAv.getChildren().add(calendarAssessment);

	}

	private void createFieldNota() {
		vBoxNota.getChildren().add(txtNota);
	}

	private AssessmentEntity getAssessmentScreen() {
		AssessmentEntity av = new AssessmentEntity();
		av.setStudent(getStudentAv());
		av.setDatAssessment(calendarAssessment.getCalendar() == null ? null : new Date(calendarAssessment.getCalendar().getTime().getTime()));
		av.setTipAssessment(cmbBoxTipAval.getValue() == null ? null : cmbBoxTipAval.getValue().getType());
		av.setTipAssessmentRecovery(typeAssessmentForRecovery);
		av.setNumTipAssessment(numberAssessmentForRecovery);
		av.setNumBimester(cmbBoxBimestreNewAval.getValue() == null ? null : cmbBoxBimestreNewAval.getValue().getNumber());
		av.setDesAssessment(txtDescAval.getText());
		av.setDesObservation(txtAreaObs.getText());
		av.setNotaAssessment(txtNota.getNumber() == null ? null : txtNota.getNumber().doubleValue());
		return av;
	}

	private StudentEntity getStudentAv() {
		if (studentAv == null) {
			studentAv = new StudentEntity();
		}
		return studentAv;
	}

	private void openModalAssessment() {
		final AssessmentModalController assessmentModal = context.getBean(AssessmentModalController.class);
		assessmentModal.setBimestre(cmbBoxBimestreNewAval.getValue());
		assessmentModal.show(stageNewAval);
	}

	@FXML
	protected void saveNewAssessment() {
		try {
			assessmentService.save(getAssessmentScreen());
			new MessageDisplay(stageNewAval.getScene().getRoot(), Constants.MSG_SUCESS_SAVE.valuesToString()).showSucessMessage();
			clear();
			tabAssessment.getController().getTableAssessments().fireGridLoadEvent(new GridLoadEvent());
		} catch (Exception e) {
			new MessageDisplay(stageNewAval.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
			LOG.error(e.getMessage(), e);
		}
	}

	private void setConfigComboAluno() {
		cmbBoxAlunoNewAval.valueProperty().addListener((ChangeListener<StudentEntity>) (observable, oldValue, newValue) -> setStudentAv(newValue));
		cmbBoxAlunoNewAval.setButtonCell(new StudentListCell());
		cmbBoxAlunoNewAval.setCellFactory(param -> new StudentListCell());
		cmbBoxAlunoNewAval.setItems(parent.getListStudentsGradebook());
	}

	private void setConfigComboBimestre() {

		cmbBoxBimestreNewAval.valueProperty().addListener((ChangeListener<Bimester>) (observable, oldValue, newValue) -> {
			cmbBoxTipAval.setDisable(Boolean.FALSE);
			cmbBoxTipAval.setValue(null);
		});
		cmbBoxBimestreNewAval.setCellFactory(param -> new BimesterListCell());

		cmbBoxBimestreNewAval.setItems(FXCollections.observableArrayList(Bimester.values()));
	}

	private void setConfigComboTipAval() {

		cmbBoxTipAval.valueProperty().addListener((ChangeListener<TypeAssessment>) (observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue.name().equals(TypeAssessment.RECUPERAÇÃO.name())) {
					openModalAssessment();
				}
			}
		});

		cmbBoxTipAval.setItems(FXCollections.observableArrayList(TypeAssessment.values()));
	}

	private void setStudentAv(StudentEntity studentAv) {
		this.studentAv = studentAv;
	}

	public void setNumberAssessmentForRecovery(Integer numberAssessmentForRecovery) {
		this.numberAssessmentForRecovery = numberAssessmentForRecovery;
	}

	public void setTypeAssessmentForRecovery(String typeAssessmentForRecovery) {
		this.typeAssessmentForRecovery = typeAssessmentForRecovery;
	}

}
