package nc.ui.ehpta.pub.calc;

import java.util.Calendar;
import java.util.Date;

import nc.vo.pub.lang.UFDate;

public class CalcFunc {
	
	private static Calendar calendar = null;
	
	private CalcFunc() {
		
	}
	
	/**
	 * ���� �� ����һ�����ڣ����ص�ǰ�����·ݵ����һ����ַ���
	 * 
	 *  Author : river 
	 *  
	 *  Create : 2012-08-20
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static final String bulder(Object date) throws Exception {
		
		if(date == null)
			throw new Exception("���� is NULL");
		
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
}
