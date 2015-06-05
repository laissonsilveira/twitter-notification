package br.com.avfinal.entity.grid;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ResultGrid extends BaseGrid {
	
	private LongProperty idStudent = new SimpleLongProperty();
	private StringProperty nameStudent = new SimpleStringProperty();
	private DoubleProperty medBimester01 = new SimpleDoubleProperty();
	private DoubleProperty medBimester02 = new SimpleDoubleProperty();
	private DoubleProperty medBimester03 = new SimpleDoubleProperty();
	private DoubleProperty medBimester04 = new SimpleDoubleProperty();
	private DoubleProperty finalMedia = new SimpleDoubleProperty();
	private DoubleProperty medRecovery = new SimpleDoubleProperty();
	private StringProperty flgApproved = new SimpleStringProperty();
	
	public ResultGrid() {}

	public ResultGrid(Long id_student, String nm_student, Double med_bimester_01, Double med_bimester_02, Double med_bimester_03,
			Double med_bimester_04, Double med_final, Double med_recovery, String approved) {
		this.idStudent = new SimpleLongProperty(id_student);
		this.nameStudent = new SimpleStringProperty(nm_student);
		this.medBimester01 = new SimpleDoubleProperty(med_bimester_01);
		this.medBimester02 = new SimpleDoubleProperty(med_bimester_02);
		this.medBimester03 = new SimpleDoubleProperty(med_bimester_03);
		this.medBimester04 = new SimpleDoubleProperty(med_bimester_04);
		this.finalMedia = new SimpleDoubleProperty(med_final);
		this.medRecovery = new SimpleDoubleProperty(med_recovery);
		this.flgApproved = new SimpleStringProperty(approved);
	}

	public Long getIdStudent() {
		return idStudent.get();
	}
	
	public LongProperty getIdStudentProperty() {
		return idStudent;
	}
	
	public void setIdStudent(Long id_student) {
		this.idStudent.set(id_student);
	}
	
	public String getNameStudent() {
		return nameStudent.get();
	}
	
	public StringProperty getNameStudentProperty() {
		return nameStudent;
	}

	public void setNameStudent(String nameStudent) {
		this.nameStudent.set(nameStudent);
	}

	public Double getMedBimester01() {
		return medBimester01.get();
	}
	
	public DoubleProperty getMedBimester01Property() {
		return medBimester01;
	}

	public void setMedBimester01(Double med_bimester_01) {
		this.medBimester01.set(med_bimester_01);
	}

	public Double getMedBimester02() {
		return medBimester02.get();
	}

	public void setMedBimester02(Double med_bimester_02) {
		this.medBimester02.set(med_bimester_02);
	}

	public Double getMedBimester03() {
		return medBimester03.get();
	}

	public void setMedBimester03(Double med_bimester_03) {
		this.medBimester03.set(med_bimester_03);
	}

	public Double getMedBimester04() {
		return medBimester04.get();
	}

	public void setMedBimester04(Double med_bimester_04) {
		this.medBimester04.set(med_bimester_04);
	}

	public Double getFinalMedia() {
		return finalMedia.get();
	}

	public void setFinalMedia(Double med_final) {
		this.finalMedia.set(med_final);
	}

	public Double getMedRecovery() {
		return medRecovery.get();
	}

	public void setMedRecovery(Double med_recuperacao) {
		this.medRecovery.set(med_recuperacao);
	}

	public String getFlgApproved() {
		return flgApproved.get();
	}

	public void setFlgApproved(String flgApproved) {
		this.flgApproved.set(flgApproved);
	}

	public ResultGrid getClone() {
		ResultGrid rs = new ResultGrid();
		rs.setIdStudent(this.idStudent.get());
		rs.setNameStudent(this.nameStudent.get());
		rs.setMedBimester01(this.medBimester01.get());
		rs.setMedBimester02(this.medBimester02.get());
		rs.setMedBimester03(this.medBimester03.get());
		rs.setMedBimester04(this.medBimester04.get());
		rs.setFinalMedia(this.finalMedia.get());
		rs.setMedRecovery(this.medRecovery.get());
		rs.setFlgApproved(this.flgApproved.get());
		return rs;
	}

}
