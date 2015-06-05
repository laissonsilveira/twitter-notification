package br.com.avfinal.entity.grid;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import br.com.avfinal.entity.BaseEntity;

public class StudentGrid extends BaseEntity {

	private SimpleLongProperty id = new SimpleLongProperty();
	private SimpleLongProperty matriculation = new SimpleLongProperty();
	private BooleanProperty flgFrequency01 = new SimpleBooleanProperty();
	private BooleanProperty flgFrequency02 = new SimpleBooleanProperty();
	private BooleanProperty flgFrequency03 = new SimpleBooleanProperty();
	private BooleanProperty flgFrequency04 = new SimpleBooleanProperty();
	private SimpleStringProperty name = new SimpleStringProperty();
	private SimpleLongProperty idGradebook = new SimpleLongProperty();
	private SimpleLongProperty idFrequency = new SimpleLongProperty();

	public StudentGrid() {}

	public StudentGrid(String name, Long matriculation, Long id_gradebook) {
		this.name = new SimpleStringProperty(name);
		this.matriculation = new SimpleLongProperty(matriculation);
		idGradebook = new SimpleLongProperty(id_gradebook);
	}

	public Long getId() {
		return id.get();
	}

	public void setId(Long id) {
		this.id.set(id);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public BooleanProperty getFlgFrequency01() {
		return flgFrequency01;
	}

	public void setFlgFrequency01(Boolean flg_frequency01) {
		flgFrequency01.set(flg_frequency01);
	}

	public BooleanProperty getFlgFrequency02() {
		return flgFrequency02;
	}

	public void setFlgFrequency02(Boolean flg_frequency02) {
		flgFrequency02.set(flg_frequency02);
	}

	public BooleanProperty getFlgFrequency03() {
		return flgFrequency03;
	}

	public void setFlgFrequency03(Boolean flg_frequency03) {
		flgFrequency03.set(flg_frequency03);
	}

	public BooleanProperty getFlgFrequency04() {
		return flgFrequency04;
	}

	public void setFlgFrequency04(Boolean flg_frequency04) {
		flgFrequency04.set(flg_frequency04);
	}

	public Long getMatriculation() {
		return matriculation.get();
	}

	public void setMatriculation(Long matriculation) {
		this.matriculation.set(matriculation);
	}

	public Long getIdGradebook() {
		return idGradebook.get();
	}

	public void setIdGradebook(Long id_gradebook) {
		idGradebook.set(id_gradebook);
	}

	public Long getIdFrequency() {
		return idFrequency.get();
	}

	public void setIdFrequency(Long id_frequency) {
		idFrequency.set(id_frequency);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((matriculation.getValue() == null) ? 0 : matriculation.getValue().hashCode());
		result = (prime * result) + ((name.getValue() == null) ? 0 : name.getValue().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentGrid other = (StudentGrid) obj;
		if (matriculation.getValue() == null) {
			if (other.matriculation.getValue() != null)
				return false;
		} else if (!matriculation.getValue().equals(other.matriculation.getValue()))
			return false;
		if (name.getValue() == null) {
			if (other.name.getValue() != null)
				return false;
		} else if (!name.getValue().equalsIgnoreCase(other.name.getValue()))
			return false;
		return true;
	}

}
