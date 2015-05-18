package br.com.avfinal.entity;

import java.util.Date;

public class ResultDTO {
	
	private Long id_student; 
	private String nm_student; 
	private String tip_assessment; 
	private Integer num_tip_assessment;
	private Integer num_bimester; 
	private Date dat_assessment; 
	private Double nota_assessment;
	private Double nota_recovery;
	private Double nota_recovery_final;
	private String flg_approved;
	private Double final_media;
	
	public ResultDTO() {
	}

	public ResultDTO(Long id_student, String nm_student, String tip_assessment, 
			Integer num_tip_assessment, Double media_final,Integer num_bimester, String flg_approved, 
			Date dat_assessment, Double nota_assessment, Double nota_recovery_final, Double nota_recovery) {
		super();
		this.id_student = id_student;
		this.nm_student = nm_student;
		this.tip_assessment = tip_assessment;
		this.num_tip_assessment = num_tip_assessment;
		this.num_bimester = num_bimester;
		this.dat_assessment = dat_assessment;
		this.nota_assessment = nota_assessment;
		this.nota_recovery = nota_recovery;
		this.nota_recovery_final = nota_recovery_final;
		this.flg_approved = flg_approved;
		this.final_media = media_final;
	}
	
	public Long getIdStudent() {
		return id_student;
	}

	public void setIdStudent(Long id_student) {
		this.id_student = id_student;
	}

	public String getNameStudent() {
		return nm_student;
	}

	public void setNameStudent(String nm_student) {
		this.nm_student = nm_student;
	}
	
	public String getTipAssessment() {
		return tip_assessment;
	}

	public void setTipAssessement(String tip_assessment) {
		this.tip_assessment = tip_assessment;
	}

	public void setNumTipAssessment(Integer num_tip_assessment) {
		this.num_tip_assessment = num_tip_assessment;
	}

	public Integer getNumBimester() {
		return num_bimester;
	}

	public void setNumBimester(Integer num_bimester) {
		this.num_bimester = num_bimester;
	}
	
	public Integer getNumTipAssessment() {
		return num_tip_assessment;
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
	
	public Double getNotaRecovery() {
		return nota_recovery;
	}
	
	public void setNotaRecovery(Double nota_recovery) {
		this.nota_recovery = nota_recovery;
	}

	public Double getNotaRecoveryFinal() {
		return nota_recovery_final;
	}

	public void setNotaRecoveryFinal(Double nota_recovery_final) {
		this.nota_recovery_final = nota_recovery_final;
	}

	public String getFlgApproved() {
		return flg_approved;
	}
	
	public void setFlg_aprovado(String flg_approved) {
		this.flg_approved = flg_approved;
	}

	public Double getFinalMedia() {
		return final_media;
	}

	public void setFinalMedia(Double final_media) {
		this.final_media = final_media;
	}

}
