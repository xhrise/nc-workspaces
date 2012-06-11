package com.ufsoft.table.re.timeref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

public class CalendarUtils {

	/**
	 * ���ĳ��ĳ�µ�����
	 * 
	 * @param int
	 *            year, int month
	 * @return int
	 */
	protected static int getMonthDays(int year, int month) {
		if (year <= 0 || month <= 0) {
			throw new IllegalArgumentException(StringResource.getStringResource("mhbmeet00002"));//�����벻С��0��ֵ
		}
		if (month == 2) {
			if ((year % 4) == 0) {
				return 29;
			} else {
				return 28;
			}
		} else if (month == 1 || month == 3 || month == 5 || month == 7
				|| month == 8 || month == 10 || month == 12) {
			return 31;
		}
		return 30;
	}

	/**
	 * ����ָ���ĸ�ʽ�������ڸ�ʽ���ַ���
	 * 
	 * @param Date
	 *            date, String strFormat
	 * @return String
	 */
	protected static String formatDate(Date date, String strFormat) {
		if (date == null || strFormat == null || strFormat.length() == 0) {
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//�������������Ϊ��
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(strFormat);
			return sdf.format(date);
		} catch (Exception ex) {
			AppDebug.debug(ex);
		}
		return null;
	}

	/**
	 * ����ָ���ĸ�ʽ�������ڸ�ʽ��Date
	 * 
	 * @param Date
	 *            date, String strFormat
	 * @return String
	 */
	protected static Date parseDate(String date, String strFormat) {
		if (date == null || strFormat == null || strFormat.length() == 0) {
			throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//�������������Ϊ��
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(date);
			return sdf.parse(strFormat);
		} catch (Exception ex) {
			AppDebug.debug(ex);
		}
		return null;
	}
	
	/**
	 * ��õ�ǰ��
	 * @param     
	 * @return int
	 */
	protected static int getCurentMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH);
	}

	/**
	 * ��õ�ǰ��
	 * @param     
	 * @return int
	 */
	protected static int getCurentYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

}
