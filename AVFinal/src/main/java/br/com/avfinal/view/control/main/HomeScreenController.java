package br.com.avfinal.view.control.main;

import static br.com.avfinal.util.enums.Constants.IS_CANCELED;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.GradebookEntity;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.mdl.service.GradebookService;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.table.EditingCellAvFinal;
import br.com.avfinal.view.control.GenericController;
import br.com.avfinal.view.control.gradebook.GradebookController;

@Component
public class HomeScreenController extends GenericController {

	static final Logger LOG = Logger.getLogger(HomeScreenController.class);

	@FXML
	private URL location;

	@FXML
	private ResourceBundle resources;

	@FXML
	private TextField professor, gerei, municipio, unidEscolar, curso, turno, serie, turma, disciplina;
	@FXML
	protected TableView<GradebookEntity> tableGradebooks;
	@FXML
	private TableColumn<GradebookEntity, String> columnCurso, columnTurno, columnSerie, columnTurma, columnDisciplina;

	private String oldValue;
	private String nameTeacher;
	private GradebookEntity gradebookSelected;
	private ObservableList<GradebookEntity> listGradebooks;

	@Autowired
	private GradebookService gradebookService;

	private void clearFields() {
		gerei.clear();
		municipio.clear();
		unidEscolar.clear();
		curso.clear();
		turno.clear();
		serie.clear();
		turma.clear();
		disciplina.clear();
	}

	private void clearTable() {
		listGradebooks.clear();
		setGradebookSelected(null);
	}

	@Override
	public void initComponet() {
		listGradebooks = FXCollections.observableArrayList();
		LOG.debug("View - initComponent()");
	}

	@Override
	public void initConfig() {
		setPropertiesTable();
		refreshGrid();
	}

	@FXML
	public void initialize() {
		initializeController();
	}

	@Override
	public void initValues() {
		professor.setText(nameTeacher);
	}

	@FXML
	protected void openDiario(ActionEvent event) {
		if (getGradebookSelected() != null) {
			try {
				final GradebookController gradebookController = context.getBean(GradebookController.class);
				gradebookController.setGradebookEntity(getGradebookSelected());
				gradebookController.show();
				tableGradebooks.getSelectionModel().clearSelection();
				setGradebookSelected(null);
			} catch (IOException e) {
				new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
				LOG.error(e.getMessage(), e);
			}
		} else {
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_SELECT_DAILY.valuesToString()).showInfoMessage();
			LOG.error(Constants.MSG_ERROR_SELECT_DAILY.valuesToString());
		}
	}

	@FXML
	protected void refreshGrid() {
		clearTable();
		List<GradebookEntity> gradebooks = null;
		try {
			gradebooks = gradebookService.findAll();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString());
		}
		if ((gradebooks != null) && !gradebooks.isEmpty()) {
			listGradebooks.setAll(gradebooks);
		}
	}

	@FXML
	protected void saveGradebook() {

		GradebookEntity gradebook = new GradebookEntity(curso.getText(), turno.getText(), serie.getText(), turma.getText(), disciplina.getText(),
				professor.getText(), gerei.getText(), municipio.getText(), unidEscolar.getText());

		try {
			gradebookService.save(gradebook);
			listGradebooks.add(gradebook);
			clearFields();
		} catch (ValidationException e) {
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_FIELD_REQUIRED_GENERAL.valuesToString(),
					e.getFieldValidationMessageList()).showErrorMessage();
			LOG.error(e.getMessage(), e);
		}

	}

	public void setGradebookSelected(GradebookEntity gradebookSelected) {
		this.gradebookSelected = gradebookSelected;
	}

	private void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	private void setPropertiesColumn(final TableColumn<GradebookEntity, String> column, Boolean isRequired, String nameColumn) {
		column.setCellValueFactory(new PropertyValueFactory<GradebookEntity, String>(nameColumn));
		column.setCellFactory(param -> new EditingCellAvFinal<GradebookEntity, String>(null));
		column.getProperties().put(Constants.IS_REQUIRED_VALUE.valuesToString(), isRequired);
		column.setOnEditStart(event -> setOldValue(event.getOldValue()));
		column.setOnEditCommit(ev -> {
			final GradebookEntity entity = ev.getRowValue();
			if (column.equals(columnCurso)) {
				entity.setCurso(ev.getNewValue());
			}
			if (column.equals(columnTurno)) {
				entity.setTurno(ev.getNewValue());
			}
			if (column.equals(columnSerie)) {
				entity.setSerie(ev.getNewValue());
			}
			if (column.equals(columnTurma)) {
				entity.setTurma(ev.getNewValue());
			}
			if (column.equals(columnDisciplina)) {
				entity.setDisciplina(ev.getNewValue());
			}
			updateRowTable(ev);
		});
	}

	private void setPropertiesTable() {
		setPropertiesColumn(columnCurso, Boolean.TRUE, "curso");
		setPropertiesColumn(columnTurno, Boolean.TRUE, "turno");
		setPropertiesColumn(columnSerie, Boolean.TRUE, "serie");
		setPropertiesColumn(columnTurma, Boolean.TRUE, "turma");
		setPropertiesColumn(columnDisciplina, Boolean.TRUE, "disciplina");

		tableGradebooks.getProperties().put(Stage.class, stagePrimary);
		tableGradebooks.setItems(listGradebooks);
	}

	@Override
	public void show() throws IOException {
		load(getClass().getResource(Constants.PATH_FXML_HOMESCREEN.valuesToString()));
		stagePrimary.setTitle(Constants.TITLE_APP.valuesToString());
		stagePrimary.show();
		LOG.debug("View - show() -> " + stagePrimary.getTitle());
	}

	private void updateRowTable(CellEditEvent<GradebookEntity, String> ev) {
		try {
			if (!getOldValue().equalsIgnoreCase(ev.getNewValue())) {
				gradebookService.update(ev.getRowValue());
			}
		} catch (Exception e) {
			ev.getTableColumn().getProperties().put(IS_CANCELED.valuesToString(), Boolean.TRUE);
			new MessageDisplay(stagePrimary.getScene().getRoot(), e.toString()).showErrorMessage();
			LOG.error(e.getMessage(), e);
		}
	}

	public void setTeacherName(String nameTeacher) {
		this.nameTeacher = nameTeacher;
	}

	public GradebookEntity getGradebookSelected() {
		if (gradebookSelected == null) {
			gradebookSelected = new GradebookEntity();
			gradebookSelected = tableGradebooks.getSelectionModel().getSelectedItem();
		}
		return gradebookSelected;
	}

	private String getOldValue() {
		return oldValue;
	}

}
