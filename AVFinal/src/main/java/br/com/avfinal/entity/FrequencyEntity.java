package br.com.avfinal.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.avfinal.util.enums.Constants;

@Entity
@Table(name = "FREQUENCY")
public class FrequencyEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6154088353549499325L;

	@Id
	@Column(name = "ID_FREQUENCY", nullable = false)
	@GeneratedValue
	private Long id_frequency;

	@Column(name = "FLG_FREQUENCY_01", length = 1, nullable = false)
	@NotNull
	private String flg_Frequency01;

	@Column(name = "FLG_FREQUENCY_02", length = 1)
	private String flg_Frequency02;

	@Column(name = "FLG_FREQUENCY_03", length = 1)
	private String flg_Frequency03;

	@Column(name = "FLG_FREQUENCY_04", length = 1)
	private String flg_Frequency04;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID_ALUNO")
	private StudentEntity student;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "FREQUENCY_DATE_STUDENT", referencedColumnName = "DATE_FREQUENCY")
	private FrequencyDateEntity frequency_date;

	public Long getId() {
		return id_frequency;
	}

	public void setId(Long id_frequency) {
		this.id_frequency = id_frequency;
	}

	public String getFlg_Frequency01() {
		return flg_Frequency01;
	}

	public String getFlgFrequencyReport01() {
		return flg_Frequency01.equals(Constants.YES) ? Constants.ATTENDANCE : Constants.LACK;
	}

	public void setFlgFrequency01(String flg_Frequency01) {
		this.flg_Frequency01 = flg_Frequency01;
	}

	public String getFlg_Frequency02() {
		return flg_Frequency02;
	}

	public String getFlgFrequencyReport02() {
		return flg_Frequency02.equals(Constants.YES) ? Constants.ATTENDANCE : Constants.LACK;
	}

	public void setFlgFrequency02(String flg_Frequency02) {
		this.flg_Frequency02 = flg_Frequency02;
	}

	public String getFlg_Frequency03() {
		return flg_Frequency03;
	}

	public String getFlgFrequencyReport03() {
		return flg_Frequency03.equals(Constants.YES) ? Constants.ATTENDANCE : Constants.LACK;
	}

	public void setFlgFrequency03(String flg_Frequency03) {
		this.flg_Frequency03 = flg_Frequency03;
	}

	public String getFlg_Frequency04() {
		return flg_Frequency04;
	}

	public String getFlgFrequencyReport04() {
		return flg_Frequency04.equals(Constants.YES) ? Constants.ATTENDANCE : Constants.LACK;
	}

	public void setFlgFrequency04(String flg_Frequency04) {
		this.flg_Frequency04 = flg_Frequency04;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public FrequencyDateEntity getFrequencyDate() {
		return frequency_date;
	}

	public void setFrequencyDate(FrequencyDateEntity frequency_date) {
		this.frequency_date = frequency_date;
	}

}
