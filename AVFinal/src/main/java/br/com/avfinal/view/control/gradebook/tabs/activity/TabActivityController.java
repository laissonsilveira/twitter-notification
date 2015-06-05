package br.com.avfinal.view.control.gradebook.tabs.activity;

import static br.com.avfinal.util.UtilAvFinal.getContextMenuEditar;
import static br.com.avfinal.util.UtilAvFinal.onChangeDateCalendar;
import static br.com.avfinal.util.UtilAvFinal.setCurrentDate;
import static br.com.avfinal.util.UtilAvFinal.setDataInCalendarPicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarPicker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.avfinal.entity.ActivityEntity;
import br.com.avfinal.exception.ValidationException;
import br.com.avfinal.mdl.service.ActivityService;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.MessageDisplay;
import br.com.avfinal.view.component.table.ButtonCellAvFinal;
import br.com.avfinal.view.component.table.ButtonCellAvFinal.ButtonCellOnClick;
import br.com.avfinal.view.component.table.ButtonCellAvFinal.TypeButton;
import br.com.avfinal.view.component.tabs.TabActivity;
import br.com.avfinal.view.control.IGenericController;
import br.com.avfinal.view.control.gradebook.GradebookController;

@Component
public class TabActivityController implements IGenericController {

	private final static Logger LOG = Logger.getLogger(TabActivityController.class);

	@Autowired
	private Stage stagePrimary;
	@Autowired
	private GradebookController controllerParent;
	@Autowired
	private ActivityService activityService;

	private TabActivity view;

	private Integer index;
	private boolean isEditing;
	private CalendarPicker activityCalendar;
	private ObservableList<ActivityEntity> listActivities;

	public void initializeController(TabActivity tabActivity) {
		view = tabActivity;
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
			new MessageDisplay(stagePrimary.getScene().getRoot(), Constants.MSG_ERROR_GENERIC.valuesToString());
		}
	}

	@Override
	public void initComponet() {
		listActivities = FXCollections.observableArrayList();
		activityCalendar = new CalendarPicker();
		createCalendar();
	}

	@Override
	public void initConfig() {
		setPropertiesTable();
	}

	@Override
	public void initValues() {
		fillGrid();
	}

	public void saveActivity() {
		try {
			if (isEditing) {
				ActivityEntity activity = listActivities.get(index);
				activity.setDate(getDateAtividade());
				activity.setDesActivity(view.getDes_activity().getText());
				activityService.update(activity);
			} else {
				ActivityEntity atv = new ActivityEntity();
				atv.setDate(getDateAtividade());
				atv.setDesActivity(view.getDes_activity().getText());
				atv.setGradebook(controllerParent.getGradebookEntity());
				activityService.save(atv);
			}
			cancelActivity();
			fillGrid();
			new MessageDisplay(view, Constants.MSG_SUCESS_SAVE.valuesToString()).showSucessMessage();
		} catch (ValidationException e) {
			LOG.error(e.getMessage(), e);
			new MessageDisplay(view, e.getMessage(), e.getFieldValidationMessageList()).showErrorMessage();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			new MessageDisplay(view, Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
		}

	}

	public void cancelActivity() {
		clear();
		isEditing = Boolean.FALSE;
		setCurrentDate(activityCalendar);
		view.getButtonAddAtividade().setText("Adicionar");
		view.getDes_activity().requestFocus();
	}

	private void setPropertiesTable() {

		view.getColumnDateActivity().setCellValueFactory(new PropertyValueFactory<ActivityEntity, Date>("date_activity"));
		view.getColumnDateActivity().setCellFactory(getCellFactoryData());

		view.getColumnContentActivity().setCellValueFactory(new PropertyValueFactory<ActivityEntity, String>("des_activity"));

		setPropertiesColumnDeleteAtividade();
		setPropertiesColumnEditAtividade();

		view.getTableActivities().setContextMenu(getContextMenuEditar(event -> {
			index = view.getTableActivities().getSelectionModel().getSelectedIndex();
			onEdit(view.getTableActivities().selectionModelProperty().getValue().getSelectedItem());
		}));

		view.getTableActivities().setRowFactory(param -> new TableRow<ActivityEntity>() {
			@Override
			protected void updateItem(ActivityEntity item, boolean empty) {
				super.updateItem(item, empty);

				addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
					if (event.getClickCount() == 2) {
						index = view.getTableActivities().getSelectionModel().getSelectedIndex();
						onEdit(view.getTableActivities().selectionModelProperty().getValue().getSelectedItem());
					}
				});
			}
		});

		view.getTableActivities().setItems(listActivities);
	}

	private void fillGrid() {
		try {
			listActivities.setAll(activityService.findAll(controllerParent.getGradebookEntity().getId()));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			new MessageDisplay(view, Constants.MSG_ERROR_GENERIC.valuesToString()).showErrorMessage();
		}
	}

	private Callback<TableColumn<ActivityEntity, Date>, TableCell<ActivityEntity, Date>> getCellFactoryData() {
		Callback<TableColumn<ActivityEntity, Date>, TableCell<ActivityEntity, Date>> cellFactory = param -> {
			TableCell<ActivityEntity, Date> cell = new TableCell<ActivityEntity, Date>() {
				@Override
				public void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (!isEmpty()) {
						setAlignment(Pos.CENTER);
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
						setText(format.format(getItem()));
					} else {
						setText("");
					}
				}
			};
			return cell;
		};
		return cellFactory;
	}

	private void onEdit(final ActivityEntity selectedItem) {
		isEditing = Boolean.TRUE;
		view.getButtonAddAtividade().setText("Salvar");

		if (selectedItem != null) {
			setDataInCalendarPicker(activityCalendar, selectedItem.getDate_activity());
			view.getDes_activity().setText(selectedItem.getDes_activity());
		}
	}

	@SuppressWarnings("unchecked")
	private void setPropertiesColumnDeleteAtividade() {
		view.getColumnDeleteAtividade().setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
		view.getColumnDeleteAtividade().setCellFactory(p -> new ButtonCellAvFinal(stagePrimary, getButtonDeleteCallBack(), TypeButton.DELETE));
	}

	@SuppressWarnings("unchecked")
	private void setPropertiesColumnEditAtividade() {
		view.getColumnEditAtividade().setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
		view.getColumnEditAtividade().setCellFactory(p -> new ButtonCellAvFinal(stagePrimary, getButtonEditCallBack(), TypeButton.EDIT));
	}

	private ButtonCellOnClick getButtonEditCallBack() {
		return indexSelected -> {
			index = indexSelected;
			onEdit(view.getTableActivities().getItems().get(indexSelected));
			return null;
		};
	}

	private ButtonCellOnClick getButtonDeleteCallBack() {
		return indexSelected -> {
			Map<Boolean, String> result = new HashMap<Boolean, String>();
			try {
				if (isEditing) {
					result.put(Boolean.FALSE, Constants.MSG_FAILURE_REMOVE.valuesToString());
				} else {
					activityService.delete(view.getTableActivities().getItems().get(indexSelected));
					listActivities.remove(indexSelected);
					result.put(Boolean.TRUE, Constants.MSG_SUCESS_REMOVE.valuesToString());
				}
			} catch (Exception e) {
				result.put(Boolean.FALSE, Constants.MSG_FAILURE_REMOVE.valuesToString());
				LOG.error(e.getMessage(), e);
			}
			return result;
		};
	}

	private void createCalendar() {
		activityCalendar.setLocale(Locale.getDefault());
		activityCalendar.calendarProperty().addListener(onChangeDateCalendar(stagePrimary, activityCalendar));
		activityCalendar.setMaxWidth(290D);
		activityCalendar.setMinWidth(290D);
		activityCalendar.setPrefWidth(290D);
		activityCalendar.setId("date_activity");

		setCurrentDate(activityCalendar);
		view.getvBoxCalendarAtividade().getChildren().add(activityCalendar);
	}

	private Date getDateAtividade() {
		return activityCalendar.getCalendar() == null ? null : activityCalendar.getCalendar().getTime();
	}

	private void clear() {
		index = null;
		view.getDes_activity().clear();
	}

}
