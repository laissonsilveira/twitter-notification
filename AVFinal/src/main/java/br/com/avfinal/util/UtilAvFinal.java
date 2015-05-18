package br.com.avfinal.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.BigDecimalField;
import jfxtras.scene.control.CalendarPicker;
import br.com.avfinal.util.enums.Constants;
import br.com.avfinal.view.component.MessageDisplay;

public class UtilAvFinal {

	public static String md5(String password) {

		String pass = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(password.getBytes()));
			pass = hash.toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return pass;
	}

	public static boolean isEmpty(Object object) {
		if ("".equals(object) || "0".equals(object) || (object == null)) {
			return true;
		} else if (object instanceof Integer) {
			return (Integer) object == 0;
		}
		return false;
	}

	public static Long getLongValue(String text) {
		if (isEmpty(text)) {
			return 0L;
		}
		return Long.parseLong(text);
	}

	public static Boolean getValueBoolean(String value) {
		return Constants.YES.equals(value) ? true : false;
	}

	public static String getYesNoValueString(Boolean value) {
		return value ? Constants.YES : Constants.NO;
	}

	public static void setCurrentDate(CalendarPicker calendar) {
		setDataInCalendarPicker(calendar, new java.util.Date());
	}

	public static void setDataInCalendarPicker(CalendarPicker calendar, java.util.Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		calendar.setCalendar(c);
		calendar.setDisplayedCalendar(c);
	}

	public static BigDecimalField createBigDecimalField() {
		BigDecimalField field = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.5"), new DecimalFormat("#0.0"));
		// field.getStyleClass().add("bigDecimalField");
		field.setMaxValue(new BigDecimal(10));
		field.setMinValue(new BigDecimal(0));
		field.setMinWidth(70D);
		field.setPrefWidth(70D);
		field.setMaxWidth(70D);
		return field;
	}

	public final static Map<Double, Double> mapNeedPoints() {
		final Map<Double, Double> map = new HashMap<Double, Double>();
		map.put(3.0, 6.9);
		map.put(4.0, 5.6);
		map.put(5.0, 4.3);
		map.put(6.0, 3.0);
		map.put(3.1, 6.8);
		map.put(4.1, 5.5);
		map.put(5.1, 4.2);
		map.put(6.1, 2.9);
		map.put(3.2, 6.7);
		map.put(4.2, 5.4);
		map.put(5.2, 4.1);
		map.put(6.2, 2.7);
		map.put(3.3, 6.5);
		map.put(4.3, 5.2);
		map.put(5.3, 3.9);
		map.put(6.3, 2.6);
		map.put(3.4, 6.4);
		map.put(4.4, 5.1);
		map.put(5.4, 3.8);
		map.put(6.4, 2.5);
		map.put(3.5, 6.3);
		map.put(4.5, 5.0);
		map.put(5.5, 3.7);
		map.put(6.5, 2.4);
		map.put(3.6, 6.1);
		map.put(4.6, 4.8);
		map.put(5.6, 3.5);
		map.put(6.6, 2.2);
		map.put(3.7, 6.0);
		map.put(4.7, 4.7);
		map.put(5.7, 3.4);
		map.put(6.7, 2.1);
		map.put(3.8, 5.9);
		map.put(4.8, 4.6);
		map.put(5.8, 3.3);
		map.put(6.8, 2.0);
		map.put(3.9, 5.7);
		map.put(4.9, 4.4);
		map.put(5.9, 3.1);
		map.put(6.9, 1.8);
		return map;
	}

	public final static String getMessageRequired(String field) {
		return isEmpty(field) ? Constants.MSG_FIELD_REQUIRED.valuesToString() : field.concat(": ".concat(Constants.MSG_FIELD_REQUIRED
				.valuesToString()));
	}

	public static ChangeListener<Calendar> onChangeDateCalendar(final Stage stage, final CalendarPicker calendarPicker) {
		return new ChangeListener<Calendar>() {
			@Override
			public void changed(ObservableValue<? extends Calendar> observable, Calendar oldValue, Calendar newValue) {
				if (newValue != null) {
					validarData(newValue);
				}
			}

			private void validarData(Calendar calendar) {
				java.util.Date dataSelected = calendar.getTime();
				java.util.Date dataAtual = new java.util.Date();

				int dateCompare = dataSelected.compareTo(dataAtual);

				if (dateCompare > 0) {
					new MessageDisplay(stage.getScene().getRoot(), Constants.MSG_ERROR_SELECTED_DATE.valuesToString()).showInfoMessage();
					setCurrentDate(calendarPicker);
				}

			};
		};
	}

	public static ContextMenu getContextMenuEditar(EventHandler<ActionEvent> action) {
		ContextMenu contextMenu = new ContextMenu();
		MenuItem item = new MenuItem("Editar");
		item.setOnAction(action);
		contextMenu.getItems().add(item);
		return contextMenu;
	}

}