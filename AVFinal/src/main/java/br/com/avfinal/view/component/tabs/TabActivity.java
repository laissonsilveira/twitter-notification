package br.com.avfinal.view.component.tabs;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.ActivityEntity;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.control.gradebook.tabs.activity.TabActivityController;

@Component
public class TabActivity extends AnchorPane {

	private final static Logger LOG = Logger.getLogger(TabActivity.class);

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	@FXML
	private VBox vBoxCalendarAtividade;
	@FXML
	private TextArea des_activity;
	@FXML
	private Button buttonAddAtividade;
	@FXML
	private TableView<ActivityEntity> tableActivities;
	@FXML
	private TableColumn<ActivityEntity, Date> columnDateActivity;
	@FXML
	private TableColumn<ActivityEntity, String> columnContentActivity;
	@FXML
	private TableColumn<ActivityEntity, Boolean> columnEditAtividade, columnDeleteAtividade;

	@Autowired
	private TabActivityController controller;

	public TabActivity() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.PATH_FXML_TAB_ACTIVITY.valuesToString()));
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
	private void onSaveActivity() {
		controller.saveActivity();
	}

	@FXML
	private void onCancelActivity() {
		controller.cancelActivity();
	}

	public ResourceBundle getResources() {
		return resources;
	}

	public URL getLocation() {
		return location;
	}

	public VBox getvBoxCalendarAtividade() {
		return vBoxCalendarAtividade;
	}

	public TextArea getDes_activity() {
		return des_activity;
	}

	public Button getButtonAddAtividade() {
		return buttonAddAtividade;
	}

	public TableView<ActivityEntity> getTableActivities() {
		return tableActivities;
	}

	public TableColumn<ActivityEntity, Date> getColumnDateActivity() {
		return columnDateActivity;
	}

	public TableColumn<ActivityEntity, String> getColumnContentActivity() {
		return columnContentActivity;
	}

	public TableColumn<ActivityEntity, Boolean> getColumnEditAtividade() {
		return columnEditAtividade;
	}

	public TableColumn<ActivityEntity, Boolean> getColumnDeleteAtividade() {
		return columnDeleteAtividade;
	}

}
