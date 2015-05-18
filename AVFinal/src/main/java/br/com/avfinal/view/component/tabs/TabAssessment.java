package br.com.avfinal.view.component.tabs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.control.gradebook.tabs.assessment.TabAssessmentController;

@Component
public class TabAssessment extends AnchorPane {

	private final static Logger LOG = Logger.getLogger(TabAssessment.class);

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	@FXML
	private ToggleGroup tglGrTypeAval;
	@FXML
	private RadioButton rdAssessment, rdRecovery;
	@FXML
	private ComboBox<Bimester> cmbBoxBimesterAval;
	@FXML
	private GridPane gridPaneTabAssessment;

	@Autowired
	private TabAssessmentController controller;

	public TabAssessment() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.PATH_FXML_TAB_ASSESSMENT.valuesToString()));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@FXML
	protected void onAddAssessment() throws IOException {
		controller.addAssessment();
	}

	public void initializeController() {
		controller.initializeController(this);
	}

	public ResourceBundle getResources() {
		return resources;
	}

	public URL getLocation() {
		return location;
	}

	public ToggleGroup getTglGrTypeAval() {
		return tglGrTypeAval;
	}

	public RadioButton getRdAssessment() {
		return rdAssessment;
	}

	public RadioButton getRdRecovery() {
		return rdRecovery;
	}

	public ComboBox<Bimester> getCmbBoxBimesterAval() {
		return cmbBoxBimesterAval;
	}

	public GridPane getGridPaneTabAssessment() {
		return gridPaneTabAssessment;
	}

	public TabAssessmentController getController() {
		return controller;
	}

}
