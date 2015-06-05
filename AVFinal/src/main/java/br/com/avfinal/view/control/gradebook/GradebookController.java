package br.com.avfinal.view.control.gradebook;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.GradebookEntity;
import br.com.avfinal.entity.StudentEntity;
import br.com.avfinal.mdl.service.StudentService;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.tabs.TabActivity;
import br.com.avfinal.view.component.tabs.TabAssessment;
import br.com.avfinal.view.component.tabs.TabFrequency;
import br.com.avfinal.view.component.tabs.TabReport;
import br.com.avfinal.view.component.tabs.TabResult;
import br.com.avfinal.view.control.GenericController;
import br.com.avfinal.view.control.gradebook.tabs.frequency.TabFrequencyController;

@Component
public class GradebookController extends GenericController {

	private static final Logger LOG = Logger.getLogger(GradebookController.class);

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	// Header Gradebook
	@FXML
	private TextField professor, gerei, municipio, unidEscolar, curse, turno, serie, turma, disciplina;

	// Tabs Gradebook
	@FXML
	@Autowired
	private TabFrequency tabFrequency;
	@FXML
	@Autowired
	private TabAssessment tabAssessment;
	@FXML
	@Autowired
	private TabActivity tabActivity;
	@FXML
	@Autowired
	private TabResult tabResult;
	@FXML
	@Autowired
	private TabReport tabReport;

	// General
	@Autowired
	private StudentService studentService;
	@Autowired
	private TabFrequencyController tabFrequencyController;

	private GradebookEntity gradebookEntity;
	private ObservableList<StudentEntity> listStudentsGradebook;

	@FXML
	public void initialize() {

		assert tabFrequency != null : "fx:id=\"tabFrequency\" was not injected: check your FXML file 'Gradebook.fxml'.";
		assert tabAssessment != null : "fx:id=\"tabAssessment\" was not injected: check your FXML file 'Gradebook.fxml'.";
		assert tabActivity != null : "fx:id=\"tabActivity\" was not injected: check your FXML file 'Gradebook.fxml'.";
		assert tabResult != null : "fx:id=\"tabResult\" was not injected: check your FXML file 'Gradebook.fxml'.";
		assert tabReport != null : "fx:id=\"tabReport\" was not injected: check your FXML file 'Gradebook.fxml'.";

		initializeController();

		tabFrequency.initializeController();
		tabAssessment.initializeController();
		tabActivity.initializeController();
		tabResult.initializeController();
		tabReport.initializeController();
	}

	@Override
	public void show() throws IOException {

		load(getClass().getResource(Constants.PATH_FXML_GRADEBOOK.valuesToString()));
		stagePrimary.setTitle(Constants.TITLE_GRADEBOOK.valuesToString());
		// stagePrimary.initModality(Modality.WINDOW_MODAL);
		stagePrimary.show();
		LOG.debug("View - show() -> " + stagePrimary.getTitle());
	}

	@Override
	public void initConfig() {
		loadStudents();
	}

	@Override
	public void initValues() {
		professor.setText(getGradebookEntity().getProfessor());
		gerei.setText(getGradebookEntity().getGerei());
		municipio.setText(getGradebookEntity().getMunicipio());
		unidEscolar.setText(getGradebookEntity().getUnidEscolar());
		curse.setText(getGradebookEntity().getCurso());
		turno.setText(getGradebookEntity().getTurno());
		serie.setText(getGradebookEntity().getSerie());
		turma.setText(getGradebookEntity().getTurma());
		disciplina.setText(getGradebookEntity().getDisciplina());
	}

	@Override
	public void initComponet() {
		listStudentsGradebook = FXCollections.observableArrayList();
	}

	private void loadStudents() {
		listStudentsGradebook.clear();
		try {
			listStudentsGradebook.addAll(FXCollections.observableArrayList(studentService.findStudentBy(getGradebookEntity().getId())));
		} catch (Exception e) {
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
			LOG.error(e.getMessage(), e);
		}
	}

	public GradebookEntity getGradebookEntity() {
		if (gradebookEntity == null) {
			gradebookEntity = new GradebookEntity();
		}
		return gradebookEntity;
	}

	public void setGradebookEntity(GradebookEntity gradebookEntity) {
		this.gradebookEntity = gradebookEntity;
	}

	public ObservableList<StudentEntity> getListStudentsGradebook() {
		return listStudentsGradebook;
	}

}
