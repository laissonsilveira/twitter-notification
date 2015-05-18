package br.com.avfinal.entity.grid;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class AssessmentGrid extends BaseGrid {
	
	public static final String TRABALHO = "T";
	public static final String AVALIACAO = "A";
	public static final String PARTICIPACAO = "P";
	
	private SimpleLongProperty id_student = new SimpleLongProperty();
	private SimpleStringProperty nm_student = new SimpleStringProperty();
	private SimpleIntegerProperty num_max_avaliacao_A = new SimpleIntegerProperty();
	private SimpleIntegerProperty num_max_avaliacao_T = new SimpleIntegerProperty();
	private SimpleIntegerProperty num_max_avaliacao_P = new SimpleIntegerProperty();
	private SimpleIntegerProperty num_max_avaliacao_R = new SimpleIntegerProperty();
	
	private SimpleDoubleProperty A_1,A_2,A_3,A_4,A_5,A_6,A_7,A_8,A_9,A_10, T_1,T_2,T_3,T_4,T_5,T_6,T_7,T_8,T_9,T_10,
	P_1,P_2,P_3,P_4,P_5,P_6,P_7,P_8,P_9,P_10;
	
	public AssessmentGrid() {}
	
	public enum Operator {
		SET,GET;
	}

	public Double actionWithNotes(int num, String tip_avaliacao, Double nota_avaliacao, Operator operator) {
		switch (tip_avaliacao) {
			case PARTICIPACAO:
				switch (num) {
					case 1:
						if (isOperatorSet(operator)) setP_1(nota_avaliacao); else return getP_1();
						break;
					case 2:
						if (isOperatorSet(operator)) setP_2(nota_avaliacao); else return getP_2();
						break;
					case 3:
						if (isOperatorSet(operator)) setP_3(nota_avaliacao); else return getP_3();
						break;
					case 4:
						if (isOperatorSet(operator)) setP_4(nota_avaliacao); else return getP_4();
						break;
					case 5:
						if (isOperatorSet(operator)) setP_5(nota_avaliacao); else return getP_5();
						break;
					case 6:
						if (isOperatorSet(operator)) setP_6(nota_avaliacao); else return getP_6();
						break;
					case 7:
						if (isOperatorSet(operator)) setP_7(nota_avaliacao); else return getP_7();
						break;
					case 8:
						if (isOperatorSet(operator)) setP_8(nota_avaliacao); else return getP_8();
						break;
					case 9:
						if (isOperatorSet(operator)) setP_9(nota_avaliacao); else return getP_9();
						break;
					case 10:
						if (isOperatorSet(operator)) setP_10(nota_avaliacao); else return getP_10();
						break;
					default:
						break;
				}
				break;
			case AVALIACAO:
				switch (num) {
					case 1:
						if (isOperatorSet(operator)) setA_1(nota_avaliacao); else return getA_1();
						break;
					case 2:
						if (isOperatorSet(operator)) setA_2(nota_avaliacao); else return getA_2();
						break;
					case 3:
						if (isOperatorSet(operator)) setA_3(nota_avaliacao); else return getA_3();
						break;
					case 4:
						if (isOperatorSet(operator)) setA_4(nota_avaliacao); else return getA_4();
						break;
					case 5:
						if (isOperatorSet(operator)) setA_5(nota_avaliacao); else return getA_5();
						break;
					case 6:
						if (isOperatorSet(operator)) setA_6(nota_avaliacao); else return getA_6();
						break;
					case 7:
						if (isOperatorSet(operator)) setA_7(nota_avaliacao); else return getA_7();
						break;
					case 8:
						if (isOperatorSet(operator)) setA_8(nota_avaliacao); else return getA_8();
						break;
					case 9:
						if (isOperatorSet(operator)) setA_9(nota_avaliacao); else return getA_9();
						break;
					case 10:
						if (isOperatorSet(operator)) setA_10(nota_avaliacao); else return getA_10();
						break;
					default:
						break;
				}
				break;
			case TRABALHO:
				switch (num) {
					case 1:
						if (isOperatorSet(operator)) setT_1(nota_avaliacao); else return getT_1();
						break;
					case 2:
						if (isOperatorSet(operator)) setT_2(nota_avaliacao); else return getT_2();
						break;
					case 3:
						if (isOperatorSet(operator)) setT_3(nota_avaliacao); else return getT_3();
						break;
					case 4:
						if (isOperatorSet(operator)) setT_4(nota_avaliacao); else return getT_4();
						break;
					case 5:
						if (isOperatorSet(operator)) setT_5(nota_avaliacao); else return getT_5();
						break;
					case 6:
						if (isOperatorSet(operator)) setT_6(nota_avaliacao); else return getT_6();
						break;
					case 7:
						if (isOperatorSet(operator)) setT_7(nota_avaliacao); else return getT_7();
						break;
					case 8:
						if (isOperatorSet(operator)) setT_8(nota_avaliacao); else return getT_8();
						break;
					case 9:
						if (isOperatorSet(operator)) setT_9(nota_avaliacao); else return getT_9();
						break;
					case 10:
						if (isOperatorSet(operator)) setT_10(nota_avaliacao); else return getT_10();
						break;
					default:
						break;
				}
				break;
		}
		return null;
	}

	private boolean isOperatorSet(Operator operator) {
		return Operator.SET.equals(operator);
	}
	
	public Double getA_1() {
		if (A_1 == null) {
			A_1 = new SimpleDoubleProperty(-1D);
		}
		return A_1.get();
	}

	public void setA_1(Double a_1) {
		if (A_1 == null) {
			A_1 = new SimpleDoubleProperty();
		}
		A_1.set(a_1);
	}

	public Double getA_2() {
		if (A_2 == null) {
			A_2 = new SimpleDoubleProperty(-1D);
		}
		return A_2.get();
	}

	public void setA_2(Double a_2) {
		if (A_2 == null) {
			A_2 = new SimpleDoubleProperty();
		}
		A_2.set(a_2);
	}

	public Double getA_3() {
		if (A_3 == null) {
			A_3 = new SimpleDoubleProperty(-1D);
		}
		return A_3.get();
	}

	public void setA_3(Double a_3) {
		if (A_3 == null) {
			A_3 = new SimpleDoubleProperty();
		}
		A_3.set(a_3);
	}

	public Double getA_4() {
		if (A_4 == null) {
			A_4 = new SimpleDoubleProperty(-1D);
		}
		return A_4.get();
	}

	public void setA_4(Double a_4) {
		if (A_4 == null) {
			A_4 = new SimpleDoubleProperty();
		}
		A_4.set(a_4);
	}

	public Double getA_5() {
		if (A_5 == null) {
			A_5 = new SimpleDoubleProperty(-1D);
		}
		return A_5.get();
	}

	public void setA_5(Double a_5) {
		if (A_5 == null) {
			A_5 = new SimpleDoubleProperty();
		}
		A_5.set(a_5);
	}

	public Double getA_6() {
		if (A_6 == null) {
			A_6 = new SimpleDoubleProperty(-1D);
		}
		return A_6.get();
	}

	public void setA_6(Double a_6) {
		if (A_6 == null) {
			A_6 = new SimpleDoubleProperty();
		}
		A_6.set(a_6);
	}

	public Double getA_7() {
		if (A_7 == null) {
			A_7 = new SimpleDoubleProperty(-1D);
		}
		return A_7.get();
	}

	public void setA_7(Double a_7) {
		if (A_7 == null) {
			A_7 = new SimpleDoubleProperty();
		}
		A_7.set(a_7);
	}

	public Double getA_8() {
		if (A_8 == null) {
			A_8 = new SimpleDoubleProperty(-1D);
		}
		return A_8.get();
	}

	public void setA_8(Double a_8) {
		if (A_8 == null) {
			A_8 = new SimpleDoubleProperty();
		}
		A_8.set(a_8);
	}

	public Double getA_9() {
		if (A_9 == null) {
			A_9 = new SimpleDoubleProperty(-1D);
		}
		return A_9.get();
	}

	public void setA_9(Double a_9) {
		if (A_9 == null) {
			A_9 = new SimpleDoubleProperty();
		}
		A_9.set(a_9);
	}

	public Double getA_10() {
		if (A_10 == null) {
			A_10 = new SimpleDoubleProperty(-1D);
		}
		return A_10.get();
	}

	public void setA_10(Double a_10) {
		if (A_10 == null) {
			A_10 = new SimpleDoubleProperty();
		}
		A_10.set(a_10);
	}

	public Double getT_1() {
		if (T_1 == null) {
			T_1 = new SimpleDoubleProperty(-1D);
		}
		return T_1.get();
	}

	public void setT_1(Double t_1) {
		if (T_1 == null) {
			T_1 = new SimpleDoubleProperty();
		}
		T_1.set(t_1);
	}

	public Double getT_2() {
		if (T_2 == null) {
			T_2 = new SimpleDoubleProperty(-1D);
		}
		return T_2.get();
	}

	public void setT_2(Double t_2) {
		if (T_2 == null) {
			T_2 = new SimpleDoubleProperty();
		}
		T_2.set(t_2);
	}

	public Double getT_3() {
		if (T_3 == null) {
			T_3 = new SimpleDoubleProperty(-1D);
		}
		return T_3.get();
	}

	public void setT_3(Double t_3) {
		if (T_3 == null) {
			T_3 = new SimpleDoubleProperty();
		}
		T_3.set(t_3);
	}

	public Double getT_4() {
		if (T_4 == null) {
			T_4 = new SimpleDoubleProperty(-1D);
		}
		return T_4.get();
	}

	public void setT_4(Double t_4) {
		if (T_4 == null) {
			T_4 = new SimpleDoubleProperty();
		}
		T_4.set(t_4);
	}

	public Double getT_5() {
		if (T_5 == null) {
			T_5 = new SimpleDoubleProperty(-1D);
		}
		return T_5.get();
	}

	public void setT_5(Double t_5) {
		if (T_5 == null) {
			T_5 = new SimpleDoubleProperty();
		}
		T_5.set(t_5);
	}

	public Double getT_6() {
		if (T_6 == null) {
			T_6 = new SimpleDoubleProperty(-1D);
		}
		return T_6.get();
	}

	public void setT_6(Double t_6) {
		if (T_6 == null) {
			T_6 = new SimpleDoubleProperty();
		}
		T_6.set(t_6);
	}

	public Double getT_7() {
		if (T_7 == null) {
			T_7 = new SimpleDoubleProperty(-1D);
		}
		return T_7.get();
	}

	public void setT_7(Double t_7) {
		if (T_7 == null) {
			T_7 = new SimpleDoubleProperty();
		}
		T_7.set(t_7);
	}

	public Double getT_8() {
		if (T_8 == null) {
			T_8 = new SimpleDoubleProperty(-1D);
		}
		return T_8.get();
	}

	public void setT_8(Double t_8) {
		if (T_8 == null) {
			T_8 = new SimpleDoubleProperty();
		}
		T_8.set(t_8);
	}

	public Double getT_9() {
		if (T_9 == null) {
			T_9 = new SimpleDoubleProperty(-1D);
		}
		return T_9.get();
	}

	public void setT_9(Double t_9) {
		if (T_9 == null) {
			T_9 = new SimpleDoubleProperty();
		}
		T_9.set(t_9);
	}

	public Double getT_10() {
		if (T_10 == null) {
			T_10 = new SimpleDoubleProperty(-1D);
		}
		return T_10.get();
	}

	public void setT_10(Double t_10) {
		if (T_10 == null) {
			T_10 = new SimpleDoubleProperty();
		}
		T_10.set(t_10);
	}

	public Double getP_1() {
		if (P_1 == null) {
			P_1 = new SimpleDoubleProperty(-1D);
		}
		return P_1.get();
	}

	public void setP_1(Double p_1) {
		if (P_1 == null) {
			P_1 = new SimpleDoubleProperty();
		}
		P_1.set(p_1);
	}

	public Double getP_2() {
		if (P_2 == null) {
			P_2 = new SimpleDoubleProperty(-1D);
		}
		return P_2.get();
	}

	public void setP_2(Double p_2) {
		if (P_2 == null) {
			P_2 = new SimpleDoubleProperty();
		}
		P_2.set(p_2);
	}

	public Double getP_3() {
		if (P_3 == null) {
			P_3 = new SimpleDoubleProperty(-1D);
		}
		return P_3.get();
	}

	public void setP_3(Double p_3) {
		if (P_3 == null) {
			P_3 = new SimpleDoubleProperty();
		}
		P_3.set(p_3);
	}

	public Double getP_4() {
		if (P_4 == null) {
			P_4 = new SimpleDoubleProperty(-1D);
		}
		return P_4.get();
	}

	public void setP_4(Double p_4) {
		if (P_4 == null) {
			P_4 = new SimpleDoubleProperty();
		}
		P_4.set(p_4);
	}

	public Double getP_5() {
		if (P_5 == null) {
			P_5 = new SimpleDoubleProperty(-1D);
		}
		return P_5.get();
	}

	public void setP_5(Double p_5) {
		if (P_5 == null) {
			P_5 = new SimpleDoubleProperty();
		}
		P_5.set(p_5);
	}

	public Double getP_6() {
		if (P_6 == null) {
			P_6 = new SimpleDoubleProperty(-1D);
		}
		return P_6.get();
	}

	public void setP_6(Double p_6) {
		if (P_6 == null) {
			P_6 = new SimpleDoubleProperty();
		}
		P_6.set(p_6);
	}

	public Double getP_7() {
		if (P_7 == null) {
			P_7 = new SimpleDoubleProperty(-1D);
		}
		return P_7.get();
	}

	public void setP_7(Double p_7) {
		if (P_7 == null) {
			P_7 = new SimpleDoubleProperty();
		}
		P_7.set(p_7);
	}

	public Double getP_8() {
		if (P_8 == null) {
			P_8 = new SimpleDoubleProperty(-1D);
		}
		return P_8.get();
	}

	public void setP_8(Double p_8) {
		if (P_8 == null) {
			P_8 = new SimpleDoubleProperty();
		}
		P_8.set(p_8);
	}

	public Double getP_9() {
		if (P_9 == null) {
			P_9 = new SimpleDoubleProperty(-1D);
		}
		return P_9.get();
	}

	public void setP_9(Double p_9) {
		if (P_9 == null) {
			P_9 = new SimpleDoubleProperty();
		}
		P_9.set(p_9);
	}

	public Double getP_10() {
		if (P_10 == null) {
			P_10 = new SimpleDoubleProperty(-1D);
		}
		return P_10.get();
	}

	public void setP_10(Double p_10) {
		if (P_10 == null) {
			P_10 = new SimpleDoubleProperty();
		}
		P_10.set(p_10);
	}

	public Long getId_student() {
		return id_student.get();
	}

	public void setIdStudent(Long id_student) {
		this.id_student.set(id_student);
	}
	
	public String getNm_student() {
		return nm_student.get();
	}

	public void setNameStudent(String name_student) {
		this.nm_student.set(name_student);
	}
	
	public Integer getNum_max_assessment_A() {
		return num_max_avaliacao_A.get();
	}
	
	public void setNum_max_avaliacao_A(Integer num_max_avaliacao) {
		this.num_max_avaliacao_A.set(num_max_avaliacao);
	}
	
	public Integer getNum_max_assessment_T() {
		return num_max_avaliacao_T.get();
	}
	
	public void setNum_max_avaliacao_T(Integer num_max_avaliacao) {
		this.num_max_avaliacao_T.set(num_max_avaliacao);
	}
	
	public Integer getNum_max_assessment_P() {
		return num_max_avaliacao_P.get();
	}
	
	public void setNum_max_avaliacao_P(Integer num_max_avaliacao) {
		this.num_max_avaliacao_P.set(num_max_avaliacao);
	}
	
	public Integer getNum_max_avaliacao_R() {
		return num_max_avaliacao_R.get();
	}
	
	public void setNum_max_avaliacao_R(Integer num_max_avaliacao) {
		this.num_max_avaliacao_R.set(num_max_avaliacao);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_student == null) ? 0 : id_student.hashCode());
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
		AssessmentGrid other = (AssessmentGrid) obj;
		if (id_student == null) {
			if (other.id_student != null)
				return false;
		} else if (!id_student.getValue().equals(other.id_student.getValue()))
			return false;
		return true;
	}

}

