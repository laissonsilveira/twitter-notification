package br.com.avfinal.view.component.tabs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.control.gradebook.tabs.report.TabReportController;

@Component
public class TabReport extends AnchorPane {

	private final static Logger LOG = Logger.getLogger(TabReport.class);

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	@FXML
	private ComboBox<Bimester> cmbBoxBimester;
	@FXML
	private CheckBox checkFrequency;
	@FXML
	private CheckBox checkAssessment;
	@FXML
	private CheckBox checkActivity;

	@Autowired
	private TabReportController controller;

	public void initializeController() {
		controller.initializeController(this);
	}

	public TabReport() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.PATH_FXML_TAB_REPORT.valuesToString()));
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
	private void onOpenReport() {
		controller.openReport();
	}

	public ComboBox<Bimester> getCmbBoxBimester() {
		return cmbBoxBimester;
	}

	public CheckBox getCheckFrequency() {
		return checkFrequency;
	}

	public CheckBox getCheckAssessment() {
		return checkAssessment;
	}

	public CheckBox getCheckActivity() {
		return checkActivity;
	}

}
