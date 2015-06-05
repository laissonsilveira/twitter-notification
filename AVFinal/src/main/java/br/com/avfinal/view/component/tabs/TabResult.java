package br.com.avfinal.view.component.tabs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.grid.ResultGrid;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.control.gradebook.tabs.result.TabResultController;

@Component
public class TabResult extends AnchorPane {

	private static final Logger LOG = Logger.getLogger(TabResult.class);

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	@FXML
	private Button btnRecupFinal;
	@FXML
	private ComboBox<String> cmbBoxBimesterRes;
	@FXML
	private TextField filterField;
	@FXML
	private TableView<ResultGrid> tableResults;
	@FXML
	private TableColumn<ResultGrid, String> columnStudentResult, columnApproved;
	@FXML
	private TableColumn<ResultGrid, Double> columnBimester01, columnBimester02, columnBimester03, columnBimester04, columnFinal, columnRecovery;

	@Autowired
	private TabResultController controller;

	public TabResult() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.PATH_FXML_TAB_RESULT.valuesToString()));
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
	private void onCalculateAverage() {
		controller.calculateAverage();
	}

	@FXML
	private void onOpenRecovery() {
		controller.openRecovery();
	}

	public Button getBtnRecupFinal() {
		return btnRecupFinal;
	}

	public ComboBox<String> getCmbBoxBimesterRes() {
		return cmbBoxBimesterRes;
	}

	public TextField getFilterField() {
		return filterField;
	}

	public TableView<ResultGrid> getTableResults() {
		return tableResults;
	}

	public TableColumn<ResultGrid, String> getColumnStudentResult() {
		return columnStudentResult;
	}

	public TableColumn<ResultGrid, String> getColumnApproved() {
		return columnApproved;
	}

	public TableColumn<ResultGrid, Double> getColumnBimester01() {
		return columnBimester01;
	}

	public TableColumn<ResultGrid, Double> getColumnBimester02() {
		return columnBimester02;
	}

	public TableColumn<ResultGrid, Double> getColumnBimester03() {
		return columnBimester03;
	}

	public TableColumn<ResultGrid, Double> getColumnBimester04() {
		return columnBimester04;
	}

	public TableColumn<ResultGrid, Double> getColumnFinal() {
		return columnFinal;
	}

	public TableColumn<ResultGrid, Double> getColumnRecovery() {
		return columnRecovery;
	}

}
