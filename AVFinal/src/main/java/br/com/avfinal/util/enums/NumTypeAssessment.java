package br.com.avfinal.util.enums;
public enum NumTypeAssessment {

	PRIMEIRA (1),      
	SEGUNDA (2),       
	TERCEIRA (3),      
	QUARTA (4),        
	QUINTA (5),        
	SEXTA (6),         
	S�TIMA (7),        
	OITAVA (8),        
	NONA (9),          
	D�CIMA (10),
	D�CIMA_PRIMEIRA (11),
	D�CIMA_SEGUNDA (12), 
	D�CIMA_TERCEIRA (13),
	D�CIMA_QUARTA (14),  
	D�CIMA_QUINTA (15),  
	D�CIMA_SEXTA (16),   
	D�CIMA_S�TIMA (17),  
	D�CIMA_OITAVA (18),  
	D�CIMA_NONA (19),    
	VIG�SIMA (20);             

	NumTypeAssessment (Integer value) {
		this.setNumber(value);
	}

	private Integer value;

	public Integer getNumber() {
		return value;
	}

	private void setNumber(Integer value) {
		this.value = value;
	}

}