package br.com.avfinal.view.control.gradebook.tabs.assessment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.mdl.service.AssessmentService;
import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.util.enums.NumTypeAssessment;
import br.com.avfinal.util.enums.TypeAssessment;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.factory.ControllerFactory;
import br.com.avfinal.view.component.listCell.NumTypeAssessmentListCell;
import br.com.avfinal.view.component.listCell.TypeAssessmentListCell;
import br.com.avfinal.view.control.IGenericController;
import br.com.avfinal.view.control.gradebook.GradebookController;

@Component
public class AssessmentModalController implements IGenericController {

	private final static Logger LOG = Logger.getLogger(AssessmentModalController.class);

	@FXML
	private URL location;
	@FXML
	private ResourceBundle resources;

	@FXML
	private Button btnOkModal;
	@FXML
	private ComboBox<NumTypeAssessment> cmbBoxNumTipAvalModal;
	@FXML
	private ComboBox<TypeAssessment> cmbBoxTipAvalModal;

	@Autowired
	private GradebookController parentController;
	@Autowired
	private AssessmentService assessmentService;
	@Autowired
	private NewAssessmentController newAssessmentController;
	@Autowired
	private ControllerFactory controllerFactory;

	private TypeAssessment typeAssessment;
	private Stage stageAvModal;
	private String typeAssessmentForRecovery;
	private Integer numberAssessmentForRecovery;
	private Bimester bimestre;

	@Override
	public void initComponet() {}

	@Override
	public void initConfig() {
		setConfigCmbBoxTipAvalModal();
		setConfigCmbBoxNumTipAvalModal();
	}

	@FXML
	public void initialize() {
		initConfig();
	}

	@Override
	public void initializeController() {}

	@Override
	public void initValues() {}

	public void show(final Stage parentStage) {

		stageAvModal = new Stage();
		if (parentStage != null) {
			stageAvModal.initOwner(parentStage);
		}

		Parent parent = null;
		try {
			FXMLLoader FXMLloader = new FXMLLoader(getClass().getResource(Constants.PATH_FXML_ASSESSMENT_MODAL.valuesToString()));
			FXMLloader.setControllerFactory(controllerFactory);
			parent = FXMLloader.load();
		} catch (IOException e) {
			new MessageDisplay(stageAvModal.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
			LOG.error(e.getMessage(), e);
		}

		Scene scene = new Scene(parent);
		stageAvModal.setTitle(Constants.TITLE_MODAL_ASSESSMENT.valuesToString());
		stageAvModal.setScene(scene);
		stageAvModal.setFullScreen(false);
		stageAvModal.setResizable(false);
		stageAvModal.initModality(Modality.WINDOW_MODAL);
		stageAvModal.centerOnScreen();
		stageAvModal.setOnCloseRequest(event -> {
			numberAssessmentForRecovery = null;
			typeAssessmentForRecovery = null;
		});
		stageAvModal.showAndWait();

		newAssessmentController.setNumberAssessmentForRecovery(numberAssessmentForRecovery);
		newAssessmentController.setTypeAssessmentForRecovery(typeAssessmentForRecovery);
	}

	@FXML
	protected void selectedAssessment() {
		if (cmbBoxNumTipAvalModal.getValue() != null) {
			numberAssessmentForRecovery = cmbBoxNumTipAvalModal.getValue().getNumber();
			typeAssessmentForRecovery = cmbBoxTipAvalModal.getValue().getType();
			stageAvModal.close();
		} else {
			new MessageDisplay(stageAvModal.getScene().getRoot(), "Selecione uma avaliação!").showInfoMessage();
		}
	}

	private void setConfigCmbBoxNumTipAvalModal() {
		cmbBoxNumTipAvalModal.valueProperty().addListener((ChangeListener<NumTypeAssessment>) (observable, oldValue, newValue) -> {
			if (newValue != null) {
				btnOkModal.setDisable(Boolean.FALSE);
			}
		});
		cmbBoxNumTipAvalModal.setButtonCell(new NumTypeAssessmentListCell());
		cmbBoxNumTipAvalModal.setCellFactory(param -> new NumTypeAssessmentListCell());
	}

	private void setConfigCmbBoxTipAvalModal() {
		cmbBoxTipAvalModal.valueProperty().addListener((ChangeListener<TypeAssessment>) (observable, oldValue, newValue) -> {
			cmbBoxNumTipAvalModal.setDisable(Boolean.FALSE);
			cmbBoxNumTipAvalModal.setValue(null);
			typeAssessment = newValue;
			setItensCmbBoxNumTipAvalModal();
		});
		cmbBoxTipAvalModal.setButtonCell(new TypeAssessmentListCell());
		cmbBoxTipAvalModal.setCellFactory(param -> new TypeAssessmentListCell());

		ObservableList<TypeAssessment> itens = FXCollections.observableArrayList();
		itens.add(TypeAssessment.AVALIAÇÃO);
		itens.add(TypeAssessment.TRABALHO);
		cmbBoxTipAvalModal.setItems(itens);
	}

	private void setItensCmbBoxNumTipAvalModal() {
		ObservableList<NumTypeAssessment> itens = FXCollections.observableArrayList();
		Integer qtdMax = assessmentService.getQuantityAssessment(bimestre.getNumber(), //
				typeAssessment.getType(), parentController.getGradebookEntity().getId(), null);
		for (int i = 0; i < qtdMax; i++) {
			itens.add(NumTypeAssessment.values()[i]);
		}
		cmbBoxNumTipAvalModal.setItems(itens);
	}

	public void setBimestre(Bimester bimestre) {
		this.bimestre = bimestre;
	}

}
