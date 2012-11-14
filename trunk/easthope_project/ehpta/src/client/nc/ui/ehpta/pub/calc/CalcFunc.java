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
	public static final String getLastDay(Object date) throws Exception {

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
		
		return (lastDay < 0xA ? "0" + lastDay : "" + lastDay);
	}
	
	/**
	 *  获取当前会计期间的前一期间
	 * @param period
	 * @return
	 * @throws Exception
	 */
	public static final String getUpperPeriod(String period) throws Exception {
		
		if(period == null || "".equals(period))
			return period;
		
		UFDate tempDate = new UFDate(period + "-01");
		
		int year = tempDate.getYear();
		int month = tempDate.getMonth();
		
		if(month == 0x1) {
			month = 0xC;
			year -= 0x1;
		} else 
			month -= 0x1;
		
		return year + "-" + (month < 0xA ? "0" + month : "" + month);
		
	}
	
	public static final Integer calcNumOf(UFDouble number) throws Exception {
		
		if(number.doubleValue() > number.intValue())
			return number.intValue() + 0x1;
		
		return number.intValue();
		
	}
}
