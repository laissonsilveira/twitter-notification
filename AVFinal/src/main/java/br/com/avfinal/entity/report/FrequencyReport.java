package br.com.avfinal.entity.report;

import java.util.Date;

public class FrequencyReport {

	private String name;
	private Long matriculation;
	private Date dateFreq;
	private String presence;

	public FrequencyReport() {}

	public FrequencyReport(String name, Long matriculation, Date dateFreq, String presence) {
		super();
		this.name = name;
		this.matriculation = matriculation;
		this.dateFreq = dateFreq;
		this.presence = presence;
	}

	public String getName() {
		return name;
	}

	public Long getMatriculation() {
		return matriculation;
	}

	public Date getDateFreq() {
		return dateFreq;
	}

	public String getPresence() {
		return presence;
	}

}