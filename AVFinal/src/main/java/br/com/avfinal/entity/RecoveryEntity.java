package br.com.avfinal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RECOVERY")
public class RecoveryEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1823952558811204112L;

	@Id
	@GeneratedValue
	@Column(name = "ID_RECOVERY")
	private Integer id_recovery;

	@Column(name = "NOTA_RECOVERY", precision = 2, scale = 1, nullable = false)
	private Double nota_recovery;

	@Column(name = "DES_ASSESSMENT", length = 80)
	private String des_assessment;

	@Column(name = "DES_OBSERVATION", length = 255)
	private String des_observation;

	@Column(name = "DAT_ASSESSMENT", nullable = false)
	private Date dat_assessment;

	@OneToOne
	@JoinColumn(name = "ASSESSMENT_ID", referencedColumnName = "ID_ASSESSMENT")
	private AssessmentEntity assessment;

	public RecoveryEntity() {}

	public Integer getIdRecovery() {
		return id_recovery;
	}

	public void setIdRecovery(Integer id_recovery) {
		this.id_recovery = id_recovery;
	}

	public Double getNotaRecovery() {
		return nota_recovery;
	}

	public void setNotaRecovery(Double nota_recovery) {
		this.nota_recovery = nota_recovery;
	}

	public String getDesAssessment() {
		return des_assessment;
	}

	public void setDesAssessment(String des_assessment) {
		this.des_assessment = des_assessment;
	}

	public String getDes_observation() {
		return des_observation;
	}

	public void setDesObservation(String des_observation) {
		this.des_observation = des_observation;
	}

	public Date getDatAssessment() {
		return dat_assessment;
	}

	public void setDatAssessment(Date dat_assessment) {
		this.dat_assessment = dat_assessment;
	}

	public AssessmentEntity getAssessment() {
		return assessment;
	}

	public void setAssessment(AssessmentEntity assessment) {
		this.assessment = assessment;
	}

}
