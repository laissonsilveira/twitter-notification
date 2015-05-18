package br.com.avfinal.view.control.gradebook.tabs.assessment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.grid.AssessmentGrid;
import br.com.avfinal.entity.grid.AssessmentGrid.Operator;
import br.com.avfinal.mdl.service.AssessmentService;
import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.util.enums.TypeAssessment;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.TableViewAvFinal;
import br.com.avfinal.view.component.listCell.BimesterListCell;
import br.com.avfinal.view.component.table.EditingCellDoubleAvFinal;
import br.com.avfinal.view.component.tabs.TabAssessment;
import br.com.avfinal.view.control.IGenericController;
import br.com.avfinal.view.control.gradebook.GradebookController;

@Component
public class TabAssessmentController implements IGenericController {

	private final static Logger LOG = Logger.getLogger(TabAssessmentController.class);

	private TableViewAvFinal<AssessmentGrid> tableAssessments;
	private TableColumn<AssessmentGrid, String> columnStudentAv, columnBimestreAv;
	private TableColumn<AssessmentGrid, Double> columnProvaAv, columnTrabalhoAv, columnParticipacaoAv;

	private ObservableList<AssessmentGrid> listAssessments;
	private Double oldValue;

	@Autowired
	private Stage stagePrimary;
	@Autowired
	private GradebookController controllerParent;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private AssessmentService assessmentService;

	private TabAssessment view;

	public void initializeController(TabAssessment view) {
		this.view = view;
		initializeController();
	}

	@Override
	public void initializeController() {
		if (controllerParent != null) {
			initComponet();
			initConfig();
			initValues();
			configTable();
		} else {
			LOG.fatal("View não carregada");
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString());
		}
	}

	@Override
	public void initComponet() {
		listAssessments = FXCollections.observableArrayList();
		createTableAssessment();
	}

	@Override
	public void initConfig() {
		setConfigComboBimestre();
		setConfigRadioButton();
	}

	private void configTable() {
		columnBimestreAv.setText(view.getCmbBoxBimesterAval().getValue().name().concat(" Bimestre"));
		tableAssessments.getProperties().put(Stage.class, stagePrimary);
	}

	@Override
	public void initValues() {
		view.getCmbBoxBimesterAval().setValue(Bimester.PRIMEIRO);
	}

	private void setConfigRadioButton() {
		view.getTglGrTypeAval().getSelectedToggle().selectedProperty()
				.addListener((ChangeListener<Boolean>) (component, recuperacao, assessment) -> {
					try {
						loadItensTableAssessment();
					} catch (Exception e) {
						new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
						LOG.error(e.getMessage(), e);
					}
				});
	}

	private void loadItensTableAssessment() {

		listAssessments.clear();
		createTableAssessment();
		configTable();

		int sizeAssessments = 0;
		int sizeWorkSchool = 0;
		int sizeParticipation = 0;

		ObservableList<AssessmentGrid> assessmentsMounted = FXCollections.observableArrayList();
		assessmentsMounted = assessmentService.loadItemsTableAssessment(view.getCmbBoxBimesterAval().getValue().getNumber(), controllerParent
				.getGradebookEntity().getId(), view.getRdAssessment().isSelected());

		for (AssessmentGrid av : assessmentsMounted) {
			if (sizeAssessments < av.getNum_max_assessment_A()) {
				sizeAssessments = av.getNum_max_assessment_A();
			}
			if (sizeWorkSchool < av.getNum_max_assessment_T()) {
				sizeWorkSchool = av.getNum_max_assessment_T();
			}

			if (sizeParticipation < av.getNum_max_assessment_P()) {
				sizeParticipation = av.getNum_max_assessment_P();
			}
		}

		changeConfigTableAssessment(sizeParticipation, sizeAssessments, sizeWorkSchool);

		listAssessments.setAll(assessmentsMounted);
	}

	private void changeConfigTableAssessment(int sizeParticipacao, int sizeProvas, int sizeTrabalhos) {
		columnProvaAv.getColumns().addAll(createColumnNota(sizeProvas, TypeAssessment.AVALIAÇÃO.getType()));
		columnTrabalhoAv.getColumns().addAll(createColumnNota(sizeTrabalhos, TypeAssessment.TRABALHO.getType()));
		columnParticipacaoAv.getColumns().addAll(createColumnNota(sizeParticipacao, TypeAssessment.PARTICIPAÇÃO.getType()));
	}

	private List<TableColumn<AssessmentGrid, Double>> createColumnNota(int qtdNotas, String prefixo) {

		List<TableColumn<AssessmentGrid, Double>> columns = new ArrayList<TableColumn<AssessmentGrid, Double>>();

		for (int i = 1; i <= qtdNotas; i++) {
			TableColumn<AssessmentGrid, Double> nota = new TableColumn<AssessmentGrid, Double>(String.valueOf(i));
			nota.setCellValueFactory(new PropertyValueFactory<AssessmentGrid, Double>(prefixo.concat("_").concat(String.valueOf(i))));
			nota.getProperties().put(Constants.IS_REQUIRED_VALUE.valuesToString(), Boolean.FALSE);
			nota.setCellFactory(factoryCellDouble());
			nota.setOnEditStart(ev -> oldValue = ev.getRowValue().actionWithNotes(getNumAvInGrid(ev), getTypeAvInGrid(ev), null, Operator.GET));
			nota.setOnEditCommit(ev -> {
				ev.getRowValue().actionWithNotes(getNumAvInGrid(ev), getTypeAvInGrid(ev), ev.getNewValue(), Operator.SET);
				updateRowTable(ev);
			});
			nota.getProperties().put(Constants.TIPO_ASSESSMENT.valuesToString(), prefixo);
			nota.setMinWidth(40D);
			nota.setPrefWidth(182.5D / qtdNotas);
			nota.setMaxWidth(182.5D);
			nota.setEditable(Boolean.TRUE);
			columns.add(nota);
		}

		return columns;
	}

	protected void updateRowTable(CellEditEvent<AssessmentGrid, Double> ev) {
		try {
			if (!oldValue.equals(ev.getNewValue())) {
				assessmentService.update(ev.getRowValue().getId_student(), getTypeAvInGrid(ev), view.getCmbBoxBimesterAval().getValue().getNumber(),
						Integer.valueOf(ev.getTableColumn().getText()), ev.getNewValue(), view.getRdAssessment().isSelected());
			}
		} catch (Exception e) {
			ev.getTableColumn().getProperties().put(Constants.IS_CANCELED.valuesToString(), Boolean.TRUE);
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
			LOG.error(e.getMessage(), e);
		}
	}

	private int getNumAvInGrid(CellEditEvent<AssessmentGrid, Double> ev) {
		return Integer.valueOf(ev.getTablePosition().getTableColumn().getText()).intValue();
	}

	private String getTypeAvInGrid(CellEditEvent<AssessmentGrid, Double> ev) {
		return ev.getTablePosition().getTableColumn().getProperties().get(Constants.TIPO_ASSESSMENT.valuesToString()).toString();
	}

	@SuppressWarnings("unchecked")
	private void createTableAssessment() {

		columnStudentAv = new TableColumn<AssessmentGrid, String>();
		columnStudentAv.setText("Aluno");
		columnStudentAv.setEditable(false);
		columnStudentAv.setMaxWidth(240.0);
		columnStudentAv.setMinWidth(240.0);
		columnStudentAv.setPrefWidth(240.0);
		columnStudentAv.setCellValueFactory(new PropertyValueFactory<AssessmentGrid, String>("nm_student"));

		columnBimestreAv = new TableColumn<AssessmentGrid, String>();
		columnBimestreAv.setEditable(false);
		columnBimestreAv.setMaxWidth(548.0);
		columnBimestreAv.setMinWidth(548.0);
		columnBimestreAv.setPrefWidth(548.0);

		columnProvaAv = new TableColumn<AssessmentGrid, Double>();
		columnProvaAv.setText("Avaliação");
		columnProvaAv.setMaxWidth(182.5);
		columnProvaAv.setMinWidth(182.5);
		columnProvaAv.setPrefWidth(182.5);

		columnTrabalhoAv = new TableColumn<AssessmentGrid, Double>();
		columnTrabalhoAv.setText("Trabalho");
		columnTrabalhoAv.setMaxWidth(182.5);
		columnTrabalhoAv.setMinWidth(182.5);
		columnTrabalhoAv.setPrefWidth(182.5);

		columnParticipacaoAv = new TableColumn<AssessmentGrid, Double>();
		columnParticipacaoAv.setText("Participação");
		columnParticipacaoAv.setMaxWidth(182.5);
		columnParticipacaoAv.setMinWidth(182.5);
		columnParticipacaoAv.setPrefWidth(182.5);

		columnBimestreAv.getColumns().addAll(columnProvaAv, columnTrabalhoAv, columnParticipacaoAv);

		tableAssessments = new TableViewAvFinal<AssessmentGrid>();
		tableAssessments.setEditable(true);
		tableAssessments.minHeight(295.0);
		tableAssessments.setMaxHeight(295.0);
		tableAssessments.setPrefHeight(295.0);
		tableAssessments.setMinWidth(788.0);
		tableAssessments.setMaxWidth(788.0);
		tableAssessments.setPrefHeight(788.0);
		tableAssessments.setOnGridLoadEvent(event -> loadItensTableAssessment());
		;

		tableAssessments.getColumns().addAll(columnStudentAv, columnBimestreAv);
		tableAssessments.setItems(listAssessments);
		view.getGridPaneTabAssessment().add(tableAssessments, 0, 1);
	}

	private Callback<TableColumn<AssessmentGrid, Double>, TableCell<AssessmentGrid, Double>> factoryCellDouble() {
		return param -> new EditingCellDoubleAvFinal<AssessmentGrid>();
	}

	public void addAssessment() {
		try {
			final NewAssessmentController newAssessment = context.getBean(NewAssessmentController.class);
			newAssessment.show();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
		}
	}

	private void setConfigComboBimestre() {

		view.getCmbBoxBimesterAval().valueProperty().addListener((ChangeListener<Bimester>) (observable, oldValue, newValue) -> {
			try {
				loadItensTableAssessment();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
			}
		});
		view.getCmbBoxBimesterAval().setCellFactory(param -> new BimesterListCell());

		view.getCmbBoxBimesterAval().setItems(FXCollections.observableArrayList(Bimester.values()));
	}

	public TableViewAvFinal<AssessmentGrid> getTableAssessments() {
		return tableAssessments;
	}

}
