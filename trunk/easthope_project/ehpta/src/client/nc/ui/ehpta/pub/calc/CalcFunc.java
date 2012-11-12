package nc.ui.ehpta.pub.calc;

import java.util.Calendar;
import java.util.Date;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class CalcFunc {
	
	private static Calendar calendar = null;
	
	private CalcFunc() {
		
	}
	
	/**
	 * 功能 ： 传入一个日期，返回当前日期月份的最后一天的字符串
	 * 
	 *  Author : river 
	 *  
	 *  Create : 2012-08-20
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static final String builder(Object date) throws Exception {

		if(date == null)
			throw new Exception("日期 is NULL");
		
		if(calendar == null)
			calendar = Calendar.getInstance(); 
		
		if(date instanceof String)
			calendar.setTime(new UFDate(date.toString()).toDate());
		
		else if(date instanceof UFDate)
			calendar.setTime(((UFDate)date).toDate());
		
		else if(date instanceof Date) 
			calendar.setTime((Date)date );
			
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		return (lastDay < 10 ? "0" + lastDay : "" + lastDay);
	}
	
	public static final Integer calcNumOf(UFDouble number) throws Exception {
		
		if(number.doubleValue() > number.intValue())
			return number.intValue() + 1;
		
		return number.intValue();
		
	}
}
