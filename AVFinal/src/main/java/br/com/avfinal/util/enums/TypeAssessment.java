package br.com.avfinal.util.enums;
public enum TypeAssessment {

	PARTICIPAÇÃO ("P"),
	AVALIAÇÃO ("A"),
	RECUPERAÇÃO ("R"),
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