package br.com.avfinal.util.enums;
public enum TypeAssessment {

	PARTICIPA��O ("P"),
	AVALIA��O ("A"),
	RECUPERA��O ("R"),
	TRABALHO ("T");

	TypeAssessment (String value) {
		this.setType(value);
	}

	private String value;

	public String getType() {
		return value;
	}

	private void setType(String value) {
		this.value = value;
	}

}