package com.cloud.common.utils;

import java.math.BigDecimal;

/**
 * BigDecimal计算工具类
 * 
 * @author jwj
 *
 */
public class BigDecimalUtil {

	public static final BigDecimal ZERO = BigDecimal.ZERO;

	private BigDecimalUtil() {
	}

	/**
	 * 提供精确加法计算的add方法 1
	 *
	 * @param value1
	 *            被加数
	 * @param value2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
		BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
		return b1.add(b2).doubleValue();

	}

	/**
	 * 提供精确减法运算的sub方法
	 *
	 * @param value1
	 *            被减数
	 * @param value2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double value1, double value2) {

		BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
		BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
		return b1.subtract(b2).doubleValue();

	}

	/**
	 * 提供精确乘法运算的mul方法
	 *
	 * @param value1
	 *            被乘数
	 * @param value2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
		BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 *
	 * @param value1
	 *            被除数
	 * @param value2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static BigDecimal div(BigDecimal value1, BigDecimal value2, int scale) throws IllegalAccessException {
		if (value2.equals(ZERO)) {
			// 如果除数为0，抛出异常信息。
			throw new IllegalArgumentException("除数不能为0");
		}
		return value1.divide(value2, scale, BigDecimal.ROUND_DOWN);
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 *
	 * @param num
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果   ROUND_HALF_UP  向下取整ROUND_DOWN 
	 */
	private static BigDecimal round(BigDecimal num, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("精确度不能小于0");
		}
		return num.setScale(scale, BigDecimal.ROUND_DOWN);
	}

	public static BigDecimal rounds(BigDecimal num, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("精确度不能小于0");
		}
		BigDecimal num2 = new BigDecimal(num.toString());
		return num2.setScale(scale, BigDecimal.ROUND_DOWN);
	}

	/**
	 * 提供精确加法计算的add方法，确认精确度
	 *
	 * @param value1
	 *            被加数
	 * @param value2
	 *            加数
	 * @param scale
	 *            小数点后保留几位
	 * @return 两个参数求和之后，按精度四舍五入的结果
	 */
	// public static BigDecimal add(BigDecimal value1, BigDecimal value2, int
	// scale) {
	// if (value1 == null) {
	// value1 = ZERO;
	// }
	// if (value2 == null) {
	// value2 = ZERO;
	// }
	// return round(value1.add(value2), scale);
	// }

	/**
	 * 提供精确加法计算的add方法，确认精确度
	 *
	 * @param values
	 *            需要相加的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 两个参数求和之后，按精度四舍五入的结果
	 */
	public static BigDecimal add(int scale, BigDecimal... values) {
		BigDecimal sum = ZERO;
		for (BigDecimal value : values) {
			if (value == null) {
				value = ZERO;
			}
			sum = sum.add(value);
		}
		return round(sum, scale);
	}

	/**
	 * 提供精确减法运算的sub方法，确认精确度
	 *
	 * @param value1
	 *            被减数
	 * @param value2
	 *            减数
	 * @param scale
	 *            小数点后保留几位
	 * @return 两个参数的求差之后，按精度四舍五入的结果
	 */
	public static BigDecimal sub(BigDecimal value1, BigDecimal value2, int scale) {
		BigDecimal value3 = new BigDecimal(value1.toString());
		BigDecimal value4 = new BigDecimal(value2.toString());
		return round(value3.subtract(value4), scale);
	}

	/**
	 * 提供精确乘法运算的mul方法，确认精确度
	 *
	 * @param value1
	 *            被乘数
	 * @param value2
	 *            乘数
	 * @param scale
	 *            小数点后保留几位
	 * @return 两个参数的乘积之后，按精度四舍五入的结果
	 */
	public static BigDecimal mul(BigDecimal value1, BigDecimal value2, int scale) {
		BigDecimal value3 = new BigDecimal(value1.toString());
		BigDecimal value4 = new BigDecimal(value2.toString());
		return round(value3.multiply(value4), scale);
	}

	/**
	 * @param values
	 * @return
	 * @Description: 加法, 没有四舍五入
	 * @author wangdg
	 * @date 2017/12/12 13:54
	 */
	public static BigDecimal add(BigDecimal... values) {
		BigDecimal sum = ZERO;
		for (BigDecimal value : values) {
			if (value == null) {
				value = ZERO;
			}
			sum = sum.add(value);
		}
		return sum;

	}

	/**
	 * @param values
	 * @return
	 * @Description: 减法, 没有四舍五入
	 * @author wangdg
	 * @date 2017/12/12 13:54
	 */
	public static BigDecimal sub(BigDecimal total, BigDecimal... values) {
		BigDecimal totals = new BigDecimal(total.toString());
		for (BigDecimal value : values) {
			totals = totals.subtract(value);
		}
		return totals;
	}
	
	
	/**
	 * 向上取整
	 * @param num 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 */
	public static BigDecimal roundUp(BigDecimal num, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("精确度不能小于0");
		}
		return num.setScale(scale, BigDecimal.ROUND_UP);
	}
	
	
			/*BigDecimal.ROUND_HALF_DOWN
			int a = bigdemical.compareTo(bigdemical2)
			a = -1,表示bigdemical小于bigdemical2；
			a = 0,表示bigdemical等于bigdemical2；
			a = 1,表示bigdemical大于bigdemical2；
*/
	
	
	/*
	 public static void main(String[] args) throws IllegalAccessException {
    	 BigDecimal num1 = new BigDecimal("1.74");  
    	 BigDecimal num2 = new BigDecimal("60032.38");
         System.out.println(BigDecimalUtil.div(num1, num2, 10));
	}
	 */

}
