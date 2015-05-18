package br.com.avfinal.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "GRADEBOOK")
public class GradebookEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1515522472437965789L;

	@Id
	@Column(name = "ID_GRADEBOOK", nullable = false)
	@GeneratedValue
	private Long id_gradebook;

	@Column(name = "CURSO", nullable = false, length = 30)
	@NotEmpty
	private String curso;

	@Column(name = "TURNO", nullable = false, length = 30)
	@NotEmpty
	private String turno;

	@Column(name = "SERIE", nullable = false, length = 30)
	@NotEmpty
	private String serie;

	@Column(name = "TURMA", nullable = false, length = 30)
	@NotEmpty
	private String turma; // FIXME criar um objeto com numero, sala, horario, e tipo da turma

	@Column(name = "DISCIPLINA", nullable = false, length = 30)
	@NotEmpty
	private String disciplina;

	@Column(name = "PROFESSOR", length = 30)
	@NotEmpty
	private String professor;

	@Column(name = "GEREI", nullable = false, length = 30)
	@NotEmpty
	private String gerei;

	@Column(name = "MUNICIPIO", nullable = false, length = 30)
	@NotEmpty
	private String municipio;

	@Column(name = "UNID_ESCOLAR", nullable = false, length = 50)
	@NotEmpty
	private String unidEscolar;

	@OneToMany(mappedBy = "gradebook")
	private List<StudentEntity> students;

	@OneToMany(mappedBy = "gradebook")
	private List<ActivityEntity> activities;

	public GradebookEntity() {}

	public GradebookEntity(String curso, String turno, String serie, String turma, String disciplina, String professor, String gerei,
			String municipio, String unidEscolar) {
		super();
		this.curso = curso;
		this.turno = turno;
		this.serie = serie;
		this.turma = turma;
		this.disciplina = disciplina;
		this.professor = professor;
		this.gerei = gerei;
		this.municipio = municipio;
		this.unidEscolar = unidEscolar;
	}

	public GradebookEntity(Long idGradebook) {
		super();
		id_gradebook = idGradebook;
	}

	public Long getId() {
		return id_gradebook;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public String getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public String getGerei() {
		return gerei;
	}

	public void setGerei(String gerei) {
		this.gerei = gerei;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUnidEscolar() {
		return unidEscolar;
	}

	public void setUnidEscolar(String unid_escolar) {
		unidEscolar = unid_escolar;
	}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	public List<ActivityEntity> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityEntity> activities) {
		this.activities = activities;
	}

}
