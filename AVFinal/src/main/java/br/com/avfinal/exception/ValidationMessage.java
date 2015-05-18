package br.com.avfinal.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import br.com.avfinal.util.enums.Constants;

public class ValidationMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<FieldValidationMessage> fieldValidationMessageList;
	private final Validator validator = Validation.byDefaultProvider()
			.configure()
			.messageInterpolator(new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator(Constants.PATH_MESSAGES_VALIDATION.valuesToString())))
			.buildValidatorFactory()
			.getValidator();

	public ValidationMessage() {
		this.fieldValidationMessageList = new ArrayList<FieldValidationMessage>();
	}
	
	public static ValidationMessage getInstance() {
		return new ValidationMessage();
	}
	
	public <T> ValidationMessage validateEntity(T entity) {

		Set<ConstraintViolation<T>> violations = validator.validate(entity);

		if (!violations.isEmpty()) {
			for (ConstraintViolation<T> constraintViolation : violations) {
				add(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
			}
		}
		
		return this;
	}
	
	public void add(String fieldName, String messageKey) {
		fieldValidationMessageList.add(new FieldValidationMessage(fieldName, messageKey));
	}

	public void add(String fieldName, String messageKey, Object[] argments) {
		fieldValidationMessageList.add(new FieldValidationMessage(fieldName, messageKey, argments));
	}

	public void add(String fieldName, String messageKey, String fieldKey) {
		fieldValidationMessageList.add(new FieldValidationMessage(fieldName, messageKey, fieldKey));
	}

	public void exception(String msgError) throws ValidationException {
		throw new ValidationException(msgError);
	}
	
	public void exception() throws ValidationException {
		if (!fieldValidationMessageList.isEmpty()) {
			throw new ValidationException(fieldValidationMessageList, Constants.MSG_FIELD_REQUIRED_GENERAL.valuesToString());
		}
	}

	public boolean messageIsEmpty(){
		return fieldValidationMessageList.isEmpty();
	}

	public List<FieldValidationMessage> getFieldValidationMessageList() {
		return fieldValidationMessageList;
	}

	public boolean isEmptyValidate(Object value) {
		if (value == null) {
			return true;
		} else if ("".equals(value)) {
			return true;
		}
		return (value instanceof Integer && (value.equals(0)));
	}

}
