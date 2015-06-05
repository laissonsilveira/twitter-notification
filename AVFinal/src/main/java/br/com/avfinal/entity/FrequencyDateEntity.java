package br.com.avfinal.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "FREQUENCY_DATE")
public class FrequencyDateEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5887337071649352231L;

	@Id
	@Column(name = "DATE_FREQUENCY", nullable = false)
	@Temporal(value = TemporalType.DATE)
	@NotNull
	private Date dat_Frequency;

	@Column(name = "NUM_BIMESTER", nullable = false)
	@NotNull
	private Integer num_bimester;

	@Column(name = "FLG_QTD_CLASS", length = 1, nullable = false)
	private Integer flg_qtdClass;

	@OneToMany(mappedBy = "frequency_date", cascade = CascadeType.REMOVE)
	private List<FrequencyEntity> frequencies;

	public Date getDatFrequency() {
		return dat_Frequency;
	}

	public void setDatFrequency(Date dat_Frequency) {
		this.dat_Frequency = dat_Frequency;
	}

	public Integer getFlgQtdClass() {
		return flg_qtdClass;
	}

	public void setFlgQtdClass(Integer flg_qtdClass) {
		this.flg_qtdClass = flg_qtdClass;
	}

	public Integer getNumBimester() {
		return num_bimester;
	}

	public void setNumBimester(Integer num_bimester) {
		this.num_bimester = num_bimester;
	}

}
