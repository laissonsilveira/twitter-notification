package br.com.avfinal.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<FieldValidationMessage> fieldValidationMessageList;

	public ValidationException() {
		super();
	}

	public ValidationException(String mensagemErro, Throwable throwable) {
		super(mensagemErro, throwable);
		FieldValidationMessage message = new FieldValidationMessage("", mensagemErro);
		getFieldValidationMessageList().add(message);
	}
	 
	public ValidationException(String mensagemErro) {
		super(mensagemErro);
		FieldValidationMessage message = new FieldValidationMessage("", mensagemErro);
		getFieldValidationMessageList().add(message);
	}

	public ValidationException(Throwable throwable) {
		super(throwable);
	}
	
	public ValidationException(List<FieldValidationMessage> fieldValidationMessageList) {
		this();
		setFieldValidationMessageList(fieldValidationMessageList);
	}
	
	public ValidationException(List<FieldValidationMessage> fieldValidationMessageList, String messageErro) {
		super(messageErro);
		setFieldValidationMessageList(fieldValidationMessageList);
	}

	public List<FieldValidationMessage> getFieldValidationMessageList() {
		if (fieldValidationMessageList == null) {
			fieldValidationMessageList = new ArrayList<FieldValidationMessage>();
		}
		return fieldValidationMessageList;
	}

	public void setFieldValidationMessageList(List<FieldValidationMessage> fieldValidationMessageList) {
		this.fieldValidationMessageList = fieldValidationMessageList;
	}
	
	@Override
	@Deprecated
	public String toString() {
		
		StringBuilder msgValidate = new StringBuilder("");
		String twoPoints = ": ";
		
		for (FieldValidationMessage fd : fieldValidationMessageList) {
			if (fd.getFieldName() == null || "".equals(fd.getFieldName())) {
				twoPoints = "";
			}
			msgValidate.append(msgValidate.toString().equals("") ? "" : "\n").append(fd.getFieldName()).append(twoPoints).append(fd.getMessageKey());
		}
		
		return msgValidate.toString();
	}
	
}
