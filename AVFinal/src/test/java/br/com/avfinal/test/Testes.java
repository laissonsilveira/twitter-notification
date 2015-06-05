package br.com.avfinal.test;

import java.math.BigDecimal;

import br.com.avfinal.util.enums.Bimester;
import br.com.avfinal.util.enums.Constants;

public class Testes {

	public static void main(String[] args) {
		// test01();
		test02();
	}

	protected static void test01() {
		double d = 3D;

		BigDecimal step = new BigDecimal("0.1");
		for (BigDecimal value = BigDecimal.valueOf(d); value.compareTo(BigDecimal.valueOf(7d)) < 0; value = value.add(step)) {
			// System.out.println(value);
			System.out.println(pointNeed(value.doubleValue()));
			// System.out.println();
		}
	}

	protected static void test02() {
		System.out.println(Bimester.valueOf(3));
	}

	protected static String pointNeed(Double media) {

		Double res = (Constants.CONST_POINTS_MINIMO_FINAL.valuesToDouble() - (media * Constants.CONST_CALC_MEDIA_BIMONTHLY.valuesToDouble()))
				/ Constants.CONST_CALC_EXAM_FINAL.valuesToDouble();

		BigDecimal resultArredondado = new BigDecimal(res);
		res = resultArredondado.setScale(1, BigDecimal.ROUND_UP).doubleValue();

		return String.valueOf(res)/* .replace(".", ",") */;
	}
}
