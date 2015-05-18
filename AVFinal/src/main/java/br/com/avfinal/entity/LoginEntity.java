package br.com.avfinal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "LOGIN")
public class LoginEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4493201438729390595L;

	@Id
	@Column(name = "ID_LOGIN", nullable = false)
	@GeneratedValue
	private Long id_login;

	@Column(name = "NOM_LOGIN", nullable = false, unique = true, length = 30)
	@NotEmpty
	private String nm_login;

	@Column(name = "PASS_LOGIN", nullable = false, length = 30)
	@NotEmpty
	private String pass_login;

	@Column(name = "NOM_PROFESSOR", length = 50)
	private String nm_teacher;

	public LoginEntity() {}

	public LoginEntity(String nm_login, String pass_login, String nm_teacher) {
		super();
		this.nm_login = nm_login;
		this.pass_login = pass_login;
		this.nm_teacher = nm_teacher;
	}

	public Long getId() {
		return id_login;
	}

	public String getLogin() {
		return nm_login;
	}

	public String getPassword() {
		return pass_login;
	}

	public String getNameTeacher() {
		return nm_teacher;
	}

	public void setNameTeacher(String nm_teacher) {
		this.nm_teacher = nm_teacher;
	}

}
