package br.com.avfinal.util.enums;
public enum FlgApproved {

	APPROVED ("A"),
	DISAPPROVED ("R");

	FlgApproved (String value) {
		this.setValue(value);
	}

	private String value;

	public String getValue() {
		return value;
	}

	private void setValue(String value) {
		this.value = value;
	}
	
	public static String getValueBy(String nameApproved) {
		String valueApproved = null;
		if (FlgApproved.APPROVED.name().equalsIgnoreCase(nameApproved)) {
			valueApproved = APPROVED.getValue();
		} else {
			valueApproved = DISAPPROVED.getValue();
		}
		return valueApproved;
	}

}