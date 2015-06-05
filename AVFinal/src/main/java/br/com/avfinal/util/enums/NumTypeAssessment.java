package br.com.avfinal.util.enums;
public enum NumTypeAssessment {

	PRIMEIRA (1),      
	SEGUNDA (2),       
	TERCEIRA (3),      
	QUARTA (4),        
	QUINTA (5),        
	SEXTA (6),         
	SÉTIMA (7),        
	OITAVA (8),        
	NONA (9),          
	DÉCIMA (10),
	DÉCIMA_PRIMEIRA (11),
	DÉCIMA_SEGUNDA (12), 
	DÉCIMA_TERCEIRA (13),
	DÉCIMA_QUARTA (14),  
	DÉCIMA_QUINTA (15),  
	DÉCIMA_SEXTA (16),   
	DÉCIMA_SÉTIMA (17),  
	DÉCIMA_OITAVA (18),  
	DÉCIMA_NONA (19),    
	VIGÉSIMA (20);             

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