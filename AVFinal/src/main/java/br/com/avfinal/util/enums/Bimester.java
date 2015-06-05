package br.com.avfinal.util.enums;

public enum Bimester {

	PRIMEIRO(1),
	SEGUNDO(2),
	TERCEIRO(3),
	QUARTO(4);

	Bimester(Integer value) {
		setNumber(value);
	}

	private Integer number;

	public Integer getNumber() {
		return number;
	}

	private void setNumber(Integer number) {
		this.number = number;
	}

	public static Bimester valueOf(Integer number) {
		for (Bimester bimester : values()) {
			if (bimester.getNumber().equals(number)) {
				return bimester;
			}
		}
		return null;
	}

}