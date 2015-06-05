package br.com.avfinal.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "ALUNO")
public class StudentEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5754474863693743047L;

	@Id
	@Column(name = "ID_ALUNO", nullable = false)
	@GeneratedValue
	private Long id_student;

	@Column(name = "NOME", nullable = false, unique = true, length = 30)
	@NotEmpty
	private String nm_student;

	@Column(name = "MATRICULA", nullable = false, unique = true)
	@NotNull
	private Long matriculation;

	@Column(name = "FLG_APROVADO", length = 1)
	private String flg_approved;

	@Column(name = "MEDIA_FINAL", precision = 2, scale = 1)
	private Double final_media;

	@Column(name = "NOTA_RECUPERACAO_FINAL", precision = 2, scale = 1)
	private Double final_recuperation;

	@ManyToOne
	@JoinColumn(name = "GRADEBOOK_ID", referencedColumnName = "ID_GRADEBOOK")
	private GradebookEntity gradebook;

	@OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
	private List<FrequencyEntity> frequencies;

	@OneToMany(mappedBy = "student")
	private List<AssessmentEntity> assessments;

	public StudentEntity() {}

	public StudentEntity(String nm_student, Long matriculation) {
		super();
		this.nm_student = nm_student;
		this.matriculation = matriculation.equals(0L) ? null : matriculation;
	}

	public StudentEntity(String name) {
		super();
		nm_student = name;
	}

	public StudentEntity(Long id_student) {
		super();
		this.id_student = id_student;
	}

	public Long getId() {
		return id_student;
	}

	public String getName() {
		return nm_student;
	}

	public void setName(String nm_student) {
		this.nm_student = nm_student;
	}

	public Long getMatriculation() {
		return matriculation;
	}

	public void setMatriculation(Long matriculation) {
		this.matriculation = matriculation;
	}

	public void setFinalMedia(Double final_media) {
		this.final_media = final_media;
	}

	public void setFinalRecuperation(Double final_recuperation) {
		this.final_recuperation = final_recuperation;
	}

	public void setFlgApproved(String flg_approved) {
		this.flg_approved = flg_approved;
	}

	public GradebookEntity getGradebook() {
		return gradebook;
	}

	public void setGradebook(GradebookEntity gradebook) {
		this.gradebook = gradebook;
	}

	public List<FrequencyEntity> getFrequencies() {
		return frequencies;
	}

	public List<AssessmentEntity> getAssessments() {
		return assessments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((id_student == null) ? 0 : id_student.hashCode());
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
		StudentEntity other = (StudentEntity) obj;
		if (id_student == null) {
			if (other.id_student != null)
				return false;
		} else if (!id_student.equals(other.id_student))
			return false;
		return true;
	}

}
