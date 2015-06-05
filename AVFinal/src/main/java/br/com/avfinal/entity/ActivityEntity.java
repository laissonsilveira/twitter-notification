package br.com.avfinal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "ACTIVITY")
public class ActivityEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 5735795224065410191L;

	@Id
	@Column(name = "ID_ACTIVITY", nullable = false)
	@GeneratedValue
	private Long id_activity;

	@Column(name = "DAT_ACTIVITY", nullable = false)
	@Temporal(value = TemporalType.DATE)
	@NotNull
	private Date date_activity;

	@Column(name = "DES_OBSERVATION", nullable = false, length = 255)
	@NotEmpty
	@Size(min = 1, max = 255)
	private String des_activity;

	@ManyToOne
	@JoinColumn(name = "GRADEBOOK_ID", referencedColumnName = "ID_GRADEBOOK")
	private GradebookEntity gradebook;

	public ActivityEntity() {}

	public Long getId() {
		return id_activity;
	}

	public void setId(Long id_activity) {
		this.id_activity = id_activity;
	}

	public Date getDate_activity() {
		return date_activity;
	}

	public void setDate(Date date_activity) {
		this.date_activity = date_activity;
	}

	public String getDes_activity() {
		return des_activity;
	}

	public void setDesActivity(String des_activity) {
		this.des_activity = des_activity;
	}

	public GradebookEntity getGradebook() {
		return gradebook;
	}

	public void setGradebook(GradebookEntity gradebook) {
		this.gradebook = gradebook;
	}

}
