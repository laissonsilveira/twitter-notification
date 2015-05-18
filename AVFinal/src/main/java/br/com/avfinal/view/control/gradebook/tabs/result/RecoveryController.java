package br.com.avfinal.view.control.gradebook.tabs.result;

import static br.com.avfinal.util.UtilAvFinal.createBigDecimalField;
import static br.com.avfinal.util.UtilAvFinal.mapNeedPoints;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.BigDecimalField;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.grid.ResultGrid;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.util.enums.FlgApproved;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.factory.ControllerFactory;
import br.com.avfinal.view.control.IGenericController;

@Component
public class RecoveryController implements IGenericController {

	private final static Logger LOG = Logger.getLogger(RecoveryController.class);

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	@FXML
	private TextField txtMedFinal, txtPtNeed, txtStudentAssessment;
	@FXML
	private VBox vBoxTestGradeFinal;
	@FXML
	private BigDecimalField txtTestGradeFinal;
	@FXML
	private ToggleGroup tgApproved;
	@FXML
	private RadioButton rdApproved, rdDisapproved;

	private Stage stageRecovery;

	@Autowired
	private TabResultController parentResult;

	@Autowired
	private ControllerFactory controllerFactory;

	@Autowired
	private Stage stagePrimary;

	public void show() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.PATH_FXML_RECOVERY.valuesToString()));
			loader.setControllerFactory(controllerFactory);
			final Parent parent = loader.load();
			Scene scene = new Scene(parent);
			stageRecovery = new Stage();
			stageRecovery.initOwner(stagePrimary);
			stageRecovery.setTitle(Constants.TITLE_RECOVERY.valuesToString());
			stageRecovery.setScene(scene);
			stageRecovery.setFullScreen(Boolean.FALSE);
			stageRecovery.setResizable(Boolean.FALSE);
			stageRecovery.centerOnScreen();
			stageRecovery.initModality(Modality.WINDOW_MODAL);
			stageRecovery.show();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
		}
	}

	@FXML
	public void initialize() {
		initializeController();
	}

	@Override
	public void initializeController() {
		initComponet();
		initConfig();
		initValues();
	}

	@Override
	public void initConfig() {
		initRadioButton();
		createFieldNotaFinal();
	}

	@Override
	public void initValues() {
		final ResultGrid resultSelected = parentResult.getResultSelected();
		txtStudentAssessment.setText(resultSelected.getNameStudent());
		txtMedFinal.setText(resultSelected.getFinalMedia().toString().replace(".", ","));
		if (resultSelected.getFinalMedia() > 3D) { // CORRIGIR Adicionar o calculo em vez que criar fixo
			txtPtNeed.setText(mapNeedPoints().get(resultSelected.getFinalMedia()).toString().replace(".", ","));
		}
		txtTestGradeFinal.setNumber(new BigDecimal(resultSelected.getMedRecovery()));
	}

	@Override
	public void initComponet() {}

	private void initRadioButton() {
		rdApproved.setUserData(FlgApproved.APPROVED.name());
		rdDisapproved.setUserData(FlgApproved.DISAPPROVED.name());
		// if (FlgApproved.APPROVED.getValue().equals(parent.getResultSelected().getFlgApproved())) {
		// tgApproved.selectToggle(rdApproved);
		// } else {
		// tgApproved.selectToggle(rdDisapproved);
		// }
	}

	@FXML
	protected void updateMedia() {
		final String tbApproved = tgApproved.getSelectedToggle().getUserData().toString();
		parentResult.updateStudentMedia(txtTestGradeFinal.getText(), FlgApproved.getValueBy(tbApproved));
		stageRecovery.close();
	}

	@FXML
	protected void onCancel() {
		stageRecovery.close();
	}

	private void createFieldNotaFinal() {
		txtTestGradeFinal = createBigDecimalField();
		vBoxTestGradeFinal.getChildren().add(txtTestGradeFinal);
	}

}
