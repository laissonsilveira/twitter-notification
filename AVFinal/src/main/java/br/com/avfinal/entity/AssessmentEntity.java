package br.com.avfinal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ASSESSMENT")
public class AssessmentEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1927519219134520846L;

	@Id
	@GeneratedValue
	@Column(name = "ID_ASSESSMENT")
	private Long id_assessment;

	@Column(name = "TIP_ASSESSMENT", nullable = false, length = 1)
	@NotNull
	private String tip_assessment;

	@Column(name = "NUM_TIP_ASSESSMENT", nullable = false)
	private Integer num_tip_assessment;

	@Column(name = "NUM_BIMESTRE", nullable = false)
	@NotNull
	private Integer num_bimester;

	@Column(name = "DES_ASSESSMENT", length = 80)
	@Size(max = 80)
	private String des_assessment;

	@Column(name = "DES_OBSERVATION", length = 255)
	@Size(max = 255)
	private String des_observation;

	@Column(name = "DAT_ASSESSMENT", nullable = false)
	@NotNull
	private Date dat_assessment;

	@Column(name = "NOTA_ASSESSMENT", precision = 2, scale = 1, nullable = false)
	@NotNull
	private Double nota_assessment;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID_ALUNO")
	@NotNull
	private StudentEntity student;

	@OneToOne(mappedBy = "assessment")
	private RecoveryEntity recovery;

	@Transient
	private String tip_assessment_recovery;

	public AssessmentEntity() {}

	public AssessmentEntity(Long id_assessment, String tip_assessment, Integer num_tip_assessment, Integer num_bimester, String des_assessment,
			String des_observation, Date dat_assessment, Double nota_assessment) {
		super();
		this.id_assessment = id_assessment;
		this.tip_assessment = tip_assessment;
		this.num_tip_assessment = num_tip_assessment;
		this.num_bimester = num_bimester;
		this.des_assessment = des_assessment;
		this.des_observation = des_observation;
		this.dat_assessment = dat_assessment;
		this.nota_assessment = nota_assessment;
	}

	public Long getId() {
		return id_assessment;
	}

	public String getTipAssessment() {
		return tip_assessment;
	}

	public void setTipAssessment(String tip_assessment) {
		this.tip_assessment = tip_assessment;
	}

	public String getTipAssessmentRecovery() {
		return tip_assessment_recovery;
	}

	public void setTipAssessmentRecovery(String tip_avaliacao_para_recuperacao) {
		tip_assessment_recovery = tip_avaliacao_para_recuperacao;
	}

	public Integer getNumTipAssessment() {
		return num_tip_assessment;
	}

	public void setNumTipAssessment(Integer num_tip_avaliacao) {
		num_tip_assessment = num_tip_avaliacao;
	}

	public Integer getNumBimester() {
		return num_bimester;
	}

	public void setNumBimester(Integer num_bimester) {
		this.num_bimester = num_bimester;
	}

	public String getDesAssessment() {
		return des_assessment;
	}

	public void setDesAssessment(String des_assessment) {
		this.des_assessment = des_assessment;
	}

	public String getDesObservation() {
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

	public Double getNotaAssessment() {
		return nota_assessment;
	}

	public void setNotaAssessment(Double nota_assessment) {
		this.nota_assessment = nota_assessment;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((id_assessment == null) ? 0 : id_assessment.hashCode());
		result = (prime * result) + ((num_bimester == null) ? 0 : num_bimester.hashCode());
		result = (prime * result) + ((num_tip_assessment == null) ? 0 : num_tip_assessment.hashCode());
		result = (prime * result) + ((student == null) ? 0 : student.hashCode());
		result = (prime * result) + ((tip_assessment == null) ? 0 : tip_assessment.hashCode());
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
		AssessmentEntity other = (AssessmentEntity) obj;
		if (id_assessment == null) {
			if (other.id_assessment != null)
				return false;
		} else if (!id_assessment.equals(other.id_assessment))
			return false;
		if (num_bimester == null) {
			if (other.num_bimester != null)
				return false;
		} else if (!num_bimester.equals(other.num_bimester))
			return false;
		if (num_tip_assessment == null) {
			if (other.num_tip_assessment != null)
				return false;
		} else if (!num_tip_assessment.equals(other.num_tip_assessment))
			return false;
		if (student == null) {
			if (other.student != null)
				return false;
		} else if (!student.equals(other.student))
			return false;
		if (tip_assessment == null) {
			if (other.tip_assessment != null)
				return false;
		} else if (!tip_assessment.equals(other.tip_assessment))
			return false;
		return true;
	}

}
