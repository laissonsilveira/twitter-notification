package br.com.avfinal.view.component.tabs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.grid.StudentGrid;
import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.table.CheckBoxTableCellAvFinal;
import br.com.avfinal.view.control.gradebook.tabs.frequency.TabFrequencyController;

@Component
public class TabFrequency extends AnchorPane {

	private static final Logger LOG = Logger.getLogger(TabFrequency.class);

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	@FXML
	private TextField nm_student;
	@FXML
	private TextField matriculation;
	@FXML
	private GridPane gridFrequency;
	@FXML
	private ComboBox<Bimester> cmbBimester;
	@FXML
	private ComboBox<String> cmbQtdClass;
	@FXML
	private Button btnSave;

	private TableView<StudentGrid> tableFrequency;

	private TableColumn<StudentGrid, String> columnStudent;

	private TableColumn<StudentGrid, Boolean> columnPresence, columnDeleteFrequency;

	private TableColumn<StudentGrid, Boolean> columnPresence01, columnPresence02, columnPresence03, columnPresence04;

	@Autowired
	private TabFrequencyController controller;

	public TabFrequency() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.PATH_FXML_TAB_FREQUENCY.valuesToString()));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public void initializeController() {
		controller.initializeController(this);
	}

	@FXML
	private void onSaveFrequency() {
		controller.saveFrequency();
	}

	@FXML
	private void onAddNewStudent() {
		controller.addNewStudent();
	}

	public ResourceBundle getResources() {
		return resources;
	}

	public void setResources(ResourceBundle resources) {
		this.resources = resources;
	}

	public URL getLocation() {
		return location;
	}

	public void setLocation(URL location) {
		this.location = location;
	}

	public TextField getNm_student() {
		return nm_student;
	}

	public TextField getMatriculation() {
		return matriculation;
	}

	public GridPane getGridFrequency() {
		return gridFrequency;
	}

	public TableView<StudentGrid> getTableFrequency() {
		return tableFrequency;
	}

	public TableColumn<StudentGrid, String> getColumnStudent() {
		if (columnStudent == null) {
			columnStudent = new TableColumn<StudentGrid, String>();
			columnStudent.setText("Aluno");
			columnStudent.setMaxWidth(276D);
			columnStudent.setMinWidth(276D);
			columnStudent.setPrefWidth(276D);
		}
		return columnStudent;
	}

	public TableView<StudentGrid> newInstanceTableFrequency() {
		tableFrequency = new TableView<StudentGrid>();
		tableFrequency.setId("tableFrequency");
		tableFrequency.setEditable(true);
		tableFrequency.setMaxWidth(502D);
		tableFrequency.setMinWidth(502D);
		tableFrequency.setPrefWidth(502D);
		tableFrequency.setPrefHeight(398D);

		tableFrequency.getColumns().add(getColumnHidden("id"));
		tableFrequency.getColumns().add(getColumnHidden("matriculation"));
		tableFrequency.getColumns().add(getColumnHidden("idFrequency"));
		tableFrequency.getColumns().add(getColumnStudent());
		tableFrequency.getColumns().add(getColumnPresence(cmbQtdClass.getValue()));
		tableFrequency.getColumns().add(getColumnDeleteFrequency());
		return tableFrequency;
	}

	private TableColumn<StudentGrid, String> getColumnHidden(String propValue) {
		TableColumn<StudentGrid, String> columnId = new TableColumn<StudentGrid, String>();
		columnId.setCellValueFactory(new PropertyValueFactory<StudentGrid, String>(propValue));
		columnId.setVisible(Boolean.FALSE);
		return columnId;
	}

	public TableColumn<StudentGrid, Boolean> getColumnPresence(String qtdClass) {
		final double widthPresence = 190D;
		if (qtdClass == null) {
			qtdClass = "1";
		}
		columnPresence = new TableColumn<StudentGrid, Boolean>();
		columnPresence.setText("Presença");
		columnPresence.setEditable(false);
		columnPresence.setResizable(false);
		columnPresence.setSortable(false);
		columnPresence.setMaxWidth(widthPresence);
		columnPresence.setMinWidth(widthPresence);
		columnPresence.setPrefWidth(widthPresence);

		if (qtdClass.equals("1")) {
			columnPresence.getColumns().add(getColumnPresence01(widthPresence));
		} else if (qtdClass.equals("2")) {
			columnPresence.getColumns().add(getColumnPresence01(widthPresence / 2));
			columnPresence.getColumns().add(getColumnPresence02(widthPresence / 2));
		} else if (qtdClass.equals("3")) {
			columnPresence.getColumns().add(getColumnPresence01(widthPresence / 3));
			columnPresence.getColumns().add(getColumnPresence02(widthPresence / 3));
			columnPresence.getColumns().add(getColumnPresence03(widthPresence / 3));
		} else if (qtdClass.equals("4")) {
			columnPresence.getColumns().add(getColumnPresence01(widthPresence / 4));
			columnPresence.getColumns().add(getColumnPresence02(widthPresence / 4));
			columnPresence.getColumns().add(getColumnPresence03(widthPresence / 4));
			columnPresence.getColumns().add(getColumnPresence04(widthPresence / 4));

		}

		return columnPresence;
	}

	private TableColumn<StudentGrid, Boolean> getColumnPresence01(double width) {
		columnPresence01 = new TableColumn<StudentGrid, Boolean>();
		columnPresence01.setText("Aula 01");
		columnPresence01.setEditable(false);
		columnPresence01.setResizable(false);
		columnPresence01.setSortable(false);
		columnPresence01.setMaxWidth(width);
		columnPresence01.setMinWidth(width);
		columnPresence01.setPrefWidth(width);
		columnPresence01.setCellValueFactory(new PropertyValueFactory<StudentGrid, Boolean>("flgFrequency01"));
		columnPresence01.setCellFactory(p -> new CheckBoxTableCellAvFinal<StudentGrid, Boolean>());
		return columnPresence01;
	}

	private TableColumn<StudentGrid, Boolean> getColumnPresence02(double width) {
		columnPresence02 = new TableColumn<StudentGrid, Boolean>();
		columnPresence02.setText("Aula 02");
		columnPresence02.setEditable(false);
		columnPresence02.setResizable(false);
		columnPresence02.setSortable(false);
		columnPresence02.setMaxWidth(width);
		columnPresence02.setMinWidth(width);
		columnPresence02.setPrefWidth(width);
		columnPresence02.setCellValueFactory(new PropertyValueFactory<StudentGrid, Boolean>("flgFrequency02"));
		columnPresence02.setCellFactory(p -> new CheckBoxTableCellAvFinal<StudentGrid, Boolean>());
		return columnPresence02;
	}

	private TableColumn<StudentGrid, Boolean> getColumnPresence03(double width) {
		columnPresence03 = new TableColumn<StudentGrid, Boolean>();
		columnPresence03.setText("Aula 03");
		columnPresence03.setEditable(false);
		columnPresence03.setResizable(false);
		columnPresence03.setSortable(false);
		columnPresence03.setMaxWidth(width);
		columnPresence03.setMinWidth(width);
		columnPresence03.setPrefWidth(width);
		columnPresence03.setCellValueFactory(new PropertyValueFactory<StudentGrid, Boolean>("flgFrequency03"));
		columnPresence03.setCellFactory(p -> new CheckBoxTableCellAvFinal<StudentGrid, Boolean>());
		return columnPresence03;
	}

	private TableColumn<StudentGrid, Boolean> getColumnPresence04(double width) {
		columnPresence04 = new TableColumn<StudentGrid, Boolean>();
		columnPresence04.setText("Aula 04");
		columnPresence04.setEditable(false);
		columnPresence04.setResizable(false);
		columnPresence04.setSortable(false);
		columnPresence04.setMaxWidth(width);
		columnPresence04.setMinWidth(width);
		columnPresence04.setPrefWidth(width);
		columnPresence04.setCellValueFactory(new PropertyValueFactory<StudentGrid, Boolean>("flgFrequency04"));
		columnPresence04.setCellFactory(p -> new CheckBoxTableCellAvFinal<StudentGrid, Boolean>());
		return columnPresence04;
	}

	public TableColumn<StudentGrid, Boolean> getColumnDeleteFrequency() {
		if (columnDeleteFrequency == null) {
			columnDeleteFrequency = new TableColumn<StudentGrid, Boolean>();
			columnDeleteFrequency.setEditable(false);
			columnDeleteFrequency.setResizable(false);
			columnDeleteFrequency.setSortable(false);
			columnDeleteFrequency.setMaxWidth(36D);
			columnDeleteFrequency.setMinWidth(36D);
			columnDeleteFrequency.setPrefWidth(36D);
			columnDeleteFrequency.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
		}
		return columnDeleteFrequency;
	}

	public ComboBox<Bimester> getCmbBimester() {
		return cmbBimester;
	}

	public ComboBox<String> getCmbQtdClass() {
		return cmbQtdClass;
	}

	public Button getBtnSave() {
		return btnSave;
	}

}
