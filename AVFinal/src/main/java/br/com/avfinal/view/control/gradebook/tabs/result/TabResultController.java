package br.com.avfinal.view.control.gradebook.tabs.result;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.grid.ResultGrid;
import br.com.avfinal.mdl.service.ResultService;
import br.com.avfinal.mdl.service.StudentService;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.util.enums.FlgApproved;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.tabs.TabResult;
import br.com.avfinal.view.control.IGenericController;
import br.com.avfinal.view.control.gradebook.GradebookController;

@Component
public class TabResultController implements IGenericController {

	private static final Logger LOG = Logger.getLogger(TabResultController.class);

	private static final String CELL_APPROVED = "cell-approved";
	private static final String CELL_DISAPPROVED = "cell-disapproved";

	@Autowired
	private ApplicationContext context;
	@Autowired
	private GradebookController parentController;
	@Autowired
	private ResultService resultService;
	@Autowired
	private StudentService studentService;

	private TabResult view;
	private ResultGrid resultSelected;
	private ObservableList<ResultGrid> resultsMaster;
	private FilteredList<ResultGrid> resultsFiltered;

	public void initializeController(TabResult tabResult) {
		view = tabResult;
		initializeController();
	}

	@Override
	public void initializeController() {
		if (view != null) {
			initComponet();
			initConfig();
			initValues();
		} else {
			LOG.fatal("View não carregada");
			new MessageDisplay(parentController.stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString());
		}
	}

	@Override
	public void initConfig() {
		setPropertyTableResultado();
		configFilterField();
	}

	@Override
	public void initValues() {}

	@Override
	public void initComponet() {}

	public void calculateAverage() {
		view.getFilterField().clear();
		view.getFilterField().requestFocus();
		resultsMaster = resultService.calculateAverages(parentController.getGradebookEntity().getId());
		resultsFiltered = new FilteredList<>(resultsMaster, p -> true);
		SortedList<ResultGrid> sortedData = new SortedList<>(resultsFiltered);
		sortedData.comparatorProperty().bind(view.getTableResults().comparatorProperty());
		view.getTableResults().setItems(sortedData);
	}

	public void openRecovery() {
		// if (getResultado().getMed_final() >= 3D) {
		final RecoveryController recoveryController = context.getBean(RecoveryController.class);
		recoveryController.show();
		// }
		// else {
		// MessageBox.show(stage,
		// "Usuário já reprovado com média final igual a "+getResultado().getMed_final().toString().replace(".", ",")
		// +", \npois não alcançou média mínima para recuperação (3,0)!", stage.getTitle(), MessageBox.ICON_WARNING);
		// }
	}

	private void setPropertyTableResultado() {

		TableColumn<ResultGrid, Long> columnId = new TableColumn<ResultGrid, Long>();
		columnId.setCellValueFactory(new PropertyValueFactory<ResultGrid, Long>("idStudent"));
		columnId.setVisible(Boolean.FALSE);
		view.getTableResults().getColumns().add(columnId);

		// columnStudentResult.setCellValueFactory(cellData -> cellData.getValue().getNameStudentProperty());
		view.getColumnStudentResult().setCellValueFactory(new PropertyValueFactory<ResultGrid, String>("nameStudent"));
		view.getColumnStudentResult().setCellFactory(factoryCellNameStudent());

		view.getColumnBimester01().setCellValueFactory(new PropertyValueFactory<ResultGrid, Double>("medBimester01"));
		view.getColumnBimester01().setCellFactory(factoryCellDouble());

		view.getColumnBimester02().setCellValueFactory(new PropertyValueFactory<ResultGrid, Double>("medBimester02"));
		view.getColumnBimester02().setCellFactory(factoryCellDouble());

		view.getColumnBimester03().setCellValueFactory(new PropertyValueFactory<ResultGrid, Double>("medBimester03"));
		view.getColumnBimester03().setCellFactory(factoryCellDouble());

		view.getColumnBimester04().setCellValueFactory(new PropertyValueFactory<ResultGrid, Double>("medBimester04"));
		view.getColumnBimester04().setCellFactory(factoryCellDouble());

		view.getColumnFinal().setCellValueFactory(new PropertyValueFactory<ResultGrid, Double>("finalMedia"));
		view.getColumnFinal().setCellFactory(factoryCellDouble());

		view.getColumnRecovery().setCellValueFactory(new PropertyValueFactory<ResultGrid, Double>("medRecovery"));
		view.getColumnRecovery().setCellFactory(factoryCellDouble());

		view.getColumnApproved().setCellValueFactory(new PropertyValueFactory<ResultGrid, String>("flgApproved"));
		view.getColumnApproved().setCellFactory(factoryCellFlgApproved());
	}

	private void configFilterField() {
		view.getFilterField().textProperty().addListener((observable, oldValue, newValue) -> {
			resultsFiltered.setPredicate(student -> {
				// If filter text is empty, display all students.
				if ((newValue == null) || newValue.isEmpty()) {
					return true;
				}
				// Compare first name of every student with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				if (student.getNameStudent().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches first name.
				}
				return false; // Does not match.
			});
		});
	}

	private Callback<TableColumn<ResultGrid, String>, TableCell<ResultGrid, String>> factoryCellNameStudent() {
		return p -> {
			TableCell<ResultGrid, String> cell = new TableCell<ResultGrid, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (!isEmpty()) {
						setAlignment(Pos.CENTER_LEFT);
						setText(getString());
					} else {
						setText(null);
					}
				}

				private String getString() {
					return getItem() == null ? "" : getItem();
				}
			};

			return cell;
		};
	}

	private Callback<TableColumn<ResultGrid, Double>, TableCell<ResultGrid, Double>> factoryCellDouble() {
		return p -> {
			TableCell<ResultGrid, Double> cell = new TableCell<ResultGrid, Double>() {
				@Override
				public void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (!isEmpty()) {
						setAlignment(Pos.CENTER);
						if (getItem() < 7D) {
							setTextFill(Color.RED);
						} else {
							setTextFill(Color.DARKBLUE);
						}
						setText(getString());
					} else {
						setText(null);
						setTextFill(null);
					}

					getTableRow().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
						if (event.getClickCount() == 1) {
							tableResultOnClicked();
						}
					});

				}

				private String getString() {
					return getItem() == null ? "" : getItem().toString().replace(".", ",");
				}
			};
			return cell;
		};
	}

	private void tableResultOnClicked() {
		if (view.getTableResults().selectionModelProperty().getValue().getSelectedItem() != null) {
			view.getBtnRecupFinal().setDisable(Boolean.FALSE);
			setResultado(view.getTableResults().selectionModelProperty().getValue().getSelectedItem());
		} else {
			view.getBtnRecupFinal().setDisable(Boolean.TRUE);
		}
	}

	public void updateStudentMedia(String nota, String approved) {

		int index = view.getTableResults().getSelectionModel().getSelectedIndex();
		ResultGrid result = resultsFiltered.get(index).getClone();
		result.setMedRecovery(Double.valueOf(nota.replace(",", ".")));
		result.setFlgApproved(approved);

		try {
			studentService.update(result);
			calculateAverage();
			new MessageDisplay(view, Constants.MSG_SUCESS_SAVE.valuesToString()).showSucessMessage();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			new MessageDisplay(view, Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
		}

		view.getTableResults().getSelectionModel().select(index);
		view.getTableResults().requestFocus();
		tableResultOnClicked();

	}

	private Callback<TableColumn<ResultGrid, String>, TableCell<ResultGrid, String>> factoryCellFlgApproved() {
		return p -> {
			TableCell<ResultGrid, String> cell = new TableCell<ResultGrid, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (!isEmpty()) {

						setAlignment(Pos.CENTER);

						if (getItem().equals(FlgApproved.APPROVED.getValue())) {
							setItem(FlgApproved.APPROVED.name());
							getStyleClass().add(CELL_APPROVED);
						} else if (getItem().equals(FlgApproved.DISAPPROVED.getValue())) {
							setItem(FlgApproved.DISAPPROVED.name());
							getStyleClass().add(CELL_DISAPPROVED);
						}

						setText(getString());
					} else {
						getStyleClass().removeAll(CELL_APPROVED, CELL_DISAPPROVED);
						setText(null);
						setTextFill(null);
					}
				}

				private String getString() {
					return getItem() == null ? "" : getItem();
				}
			};

			return cell;
		};
	}

	private void setResultado(ResultGrid resultado) {
		resultSelected = resultado;
	}

	public ResultGrid getResultSelected() {
		return resultSelected;
	}

}
