package br.com.avfinal.exception;
import java.io.Serializable;

public class FieldValidationMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String fieldName;
    private String messageKey;
    private String fieldKey;
    private Object[] arguments;

	public FieldValidationMessage() {
    }
    
    public FieldValidationMessage(String fieldName, String messageKey) {
        setFieldName(fieldName);
        setMessageKey(messageKey);
    }
    
    public FieldValidationMessage(String fieldName, String messageKey, Object[] arguments) {
    	this(fieldName, messageKey);
    	setArguments(arguments);
    }
    
    public FieldValidationMessage(String fieldName, String messageKey, String fieldKey) {
    	this(fieldName, messageKey);
        setFieldKey(fieldKey);
    }
    
    public String getFieldName() {
        return this.fieldName;
    }
    
    protected void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public String getMessageKey() {
        return this.messageKey;
    }
    
    protected void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

}
