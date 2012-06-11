package com.ufsoft.iufo.inputplugin.key;
import com.ufida.iufo.pub.tools.AppDebug;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

/**
 * ����: �ṩһ���ʱ��ؼ��ֲ��������Ĺ����ࡣ
 *   <p>1.����ָ�������ڸ���ʱ�������ϵ���ʼ���ںͽ������ڡ�</p>
 * ��������:(2006-10-13 11:19:07)
 * @author chxiaowei
 */
public final class TimeKeyUtil {
	/**
	 * ����ʼʱ��,Ĭ��Ϊ��һ 
	 */
	private static int m_WeekStartAt = 1;
	
	/**
	 * ���ڴ洢��ʽ
	 */
	private static String DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * ���ݵ�ǰ��ʵ�����������Ѯ��������
	 */
    private static int[] m_tendays;
	
    /**
	 * ���ܣ����ָ�������ڸ����ڼ������еĿ�ʼ���ڡ�
	 * @param timeValue
	 * @param timePeriod
	 * @return
	 */
	public static String getStartDay(String timeValue, String timePeriod){
		Date date = null;
        try {
            date = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.SIMPLIFIED_CHINESE).parse(timeValue);
        } catch (ParseException e) {
            throw new RuntimeException("Error:" + timePeriod);
        }

        Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String sStartDay = null;
		if(timePeriod.equals(KeyFmt.YEAR_PERIOD)){
			sStartDay = getYear(calendar) + "-01-01";
		} else if(timePeriod.equals(KeyFmt.HALFYEAR_PERIOD)){
			if(calendar.get(Calendar.MONTH) < 6){//�ϰ���
				sStartDay = getYear(calendar) + "-01-01";
			} else{//�°���
				sStartDay = getYear(calendar) + "-07-01";
			}
		} else if(timePeriod.equals(KeyFmt.SEASON_PERIOD)){
			if(calendar.get(Calendar.MONTH) < 3){//һ����
				sStartDay = getYear(calendar) + "-01-01";
			} else if(calendar.get(Calendar.MONTH) < 6){//������
				sStartDay = getYear(calendar) + "-04-01";
			} else if(calendar.get(Calendar.MONTH) < 9){//������
				sStartDay = getYear(calendar) + "-07-01";
			} else{//�ļ���
				sStartDay = getYear(calendar) + "-10-01";
			}
		} else if(timePeriod.equals(KeyFmt.MONTH_PERIOD)){
			sStartDay = getYear(calendar) + "-" + getMonthStr(getMonth(calendar)+1) + "-01";
		} else if(timePeriod.equals(KeyFmt.TENDAYS_PERIOD)) {
			initTenDays(false, calendar);
            int iDay = getDay(calendar);
            if(iDay <= m_tendays[0]){
                sStartDay = getYear(calendar) + "-" + getMonthStr(getMonth(calendar)+1) + "-01";
            } else if(iDay > m_tendays[0] && iDay <= m_tendays[1] + m_tendays[0]){
                sStartDay = getYear(calendar) + "-" + getMonthStr(getMonth(calendar)+1) + "-" + (m_tendays[0] + 1);
            } else{
                sStartDay = getYear(calendar) + "-" + getMonthStr(getMonth(calendar)+1) + "-" + (m_tendays[0] + m_tendays[1] + 1);
            }
		} else if(timePeriod.equals(KeyFmt.WEEK_PERIOD)){
			//�õ���ǰ��λ�ڵ�ǰ�ܵĵڼ���
			int iDay = getWeek(calendar);
            sStartDay = getNextDay(calendar, -iDay+1);
        	
            //�������һ�꣬��ʼ����Ϊ������+"-01-01"
        	String strYear = timeValue.substring(0, 4);
        	if(strYear.compareTo(sStartDay.substring(0,4))>0 ){
        		sStartDay = strYear+"-01-01";
        	}
		} else if(timePeriod.equals(KeyFmt.DAY_PERIOD)){
			sStartDay = getYear(calendar) + "-" + getMonthStr(getMonth(calendar)+1) + "-" + getDayStr(getDay(calendar));
		}
		
		return sStartDay;
	}
	
	/**
	 * ���ܣ����ָ�������ڸ����ڼ������еĽ������ڡ�
	 * @param timeValue
	 * @param timePeriod
	 * @return
	 */
	public static String getEndDay(String timeValue, String timePeriod){
		Date date = null;
        try {
            date = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.SIMPLIFIED_CHINESE).parse(timeValue);
        } catch (ParseException e) {
            throw new RuntimeException("Error:" + timePeriod);
        }

        Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String sEndDay = null;
		if(timePeriod.equals(KeyFmt.YEAR_PERIOD)){ 
			sEndDay = getYear(calendar) + "-12-31";
		} else if(timePeriod.equals(KeyFmt.HALFYEAR_PERIOD)){
			if(calendar.get(Calendar.MONTH) < 6){//�ϰ���
				sEndDay = getYear(calendar) + "-06-30";
			} else {//�°���
				sEndDay = getYear(calendar) + "-12-31";
			}
		} else if(timePeriod.equals(KeyFmt.SEASON_PERIOD)){
			if(calendar.get(Calendar.MONTH) < 3){//һ����
				sEndDay = getYear(calendar) + "-03-31";
			}else if(calendar.get(Calendar.MONTH) < 6){//������
				sEndDay = getYear(calendar) + "-06-30";
			}else if(calendar.get(Calendar.MONTH) < 9){//������
				sEndDay = getYear(calendar) + "-09-30";
			}else{//�ļ���
				sEndDay = getYear(calendar) + "-12-31";
			}
		} else if(timePeriod.equals(KeyFmt.MONTH_PERIOD)){
			sEndDay = getYear(calendar) + "-" + getMonthStr(getMonth(calendar)+1) + "-" + getDaysMonth(calendar);
		} else if(timePeriod.equals(KeyFmt.TENDAYS_PERIOD)) {
			initTenDays(false, calendar);
            int iDay = getDay(calendar);
            if(iDay <= m_tendays[0]){
                iDay = m_tendays[0];
            } else if(iDay > m_tendays[0] && iDay <= m_tendays[1] + m_tendays[0]){
                iDay = m_tendays[1] + m_tendays[0];
            } else{
                iDay = getDaysMonth(calendar);
            }
            sEndDay = "" + getYear(calendar) + "-" + getMonthStr(getMonth(calendar)+1) + "-" + getDayStr(iDay);
		} else if(timePeriod.equals(KeyFmt.WEEK_PERIOD)){
			//�õ���ǰ��λ�ڵ�ǰ�ܵĵڼ���
            int index = getWeek(calendar);

            //���ﲻ��ȡ���ж���ĩ���������ã���Ĭ�Ϲ���У��
            Vector weekVec = getWeekStartEndVec(calendar);
            
            //��鵱ǰ�����Ƿ����ڵ����һ����
            int daysAfterStart = 0;
            int daysAfterEnd = 0;
            String[] weekstr = (String[])weekVec.get(0);
            
            daysAfterStart = getDaysAfter(calendar, getCalendarByStr(weekstr[0]));
            daysAfterEnd = getDaysAfter(calendar, getCalendarByStr(weekstr[1]));
            if(daysAfterStart >= 0 && daysAfterEnd <= 0)
            {
            	sEndDay =  weekstr[1];
            }

            //��鵱ǰ�����Ƿ����ڵ�ǰ��ĩ��
            if(sEndDay == null )
            {
                weekstr = (String[])weekVec.lastElement();
                daysAfterStart = getDaysAfter(calendar, getCalendarByStr(weekstr[0]));
                daysAfterEnd = getDaysAfter(calendar, getCalendarByStr(weekstr[1]));
                if(daysAfterStart >= 0 && daysAfterEnd <= 0)
                {
                    sEndDay =  weekstr[1];
                }
            }
            
            //��ǰ���ڲ��ڵ�һ�ܺ�ĩ��ʱ������ĩ����������
            if(sEndDay == null)
            {
            	int MonthDay = getDaysMonth(calendar);
            	int diff = getDay(calendar) + 7 - index - MonthDay;
            	int iDay = 0;
            	int iMonth = getMonth(calendar)+1;
            	int iYear = getYear(calendar);
            	if(diff == 0){
            		iDay = MonthDay;
            	} else if(diff > 0){
            		iDay = diff;
            		iMonth += 1;
            		if(iMonth > 12){
            			iYear += 1;
            			iMonth = 1;
            		}
            	} else{
            		iDay = getDay(calendar) + 7 - index;
            	}
            	sEndDay = "" + iYear + "-" + getMonthStr(iMonth) + "-" + getDayStr(iDay);
            }
		} else if(timePeriod.equals(KeyFmt.DAY_PERIOD)){
			sEndDay = getYear(calendar) + "-" + getMonthStr(getMonth(calendar)+1) + "-" + getDayStr(getDay(calendar));
		}
		
		return sEndDay;
	}
	
	/**
	 * ���ܣ���ȡ�����ڵ��ꡣ
	 * @param calendar
	 * @return
	 */
	private static int getYear(Calendar calendar){
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * ���ܣ���ȡ�����ڵ��·ݡ�����Calendarȡ�õ��·��Ǵ�0��ʼ�ģ�����������1.
	 * @param calendar
	 * @return
	 */
	private static int getMonth(Calendar calendar){
		return calendar.get(Calendar.MONTH);
	}
	
	/**
	 * ���ܣ���ȡ�����ڵ����ڡ�
	 * @param calendar
	 * @return
	 */
	private static int getDay(Calendar calendar){
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * ���ܣ���ȡ�����ڵ��ܡ�
	 * @param calendar
	 * @return
	 */
	private static int getWeek(Calendar calendar){
		int i = calendar.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            default:
                return 6;
        }
	}
	
	/**
	 * ���ܣ���ȡָ����ȵ�ָ���·ݵ����������
	 * @param calendar
	 * @return
	 */
	public static int getDaysMonth(Calendar calendar){
		//getDaysMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * ���ܣ���ȡָ����ȵ�ָ���·ݵ����������
	 * @param year int ָ�����
	 * @param month int ָ���·�
	 * @return �����������
	 */
	public static int getDaysMonth(int year, int month){
        switch(month){
            case 1:
                return 31;
            case 2:
                if(isLeapYear(year)){
                    return 29;
                } else {
                    return 28;
                }
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;
            default:
                return 30;
        }
    }
	
	private static final long millisPerDay = 24 * 60 * 60 * 1000;
	
	/**
     * ���ܣ�����ָͬ���������ָ������������ڡ�
     * @param calendar Calendar ָ������
     * @param days int �������
     * @return Date
     */
    public static Date getDateAfter(Calendar calendar, int days){
        java.util.GregorianCalendar mdate = new java.util.GregorianCalendar(getYear(calendar), getMonth(calendar), getDay(calendar));
        return new Date(mdate.getTime().getTime() + millisPerDay * days);
    }
    /**
     * ���ܣ�����ĳһ���ھ��������������ʾ�ڽ���֮��
     * @return calendar Calendar
     * @param when Calendar
     * @return int �������
     */
    public static int getDaysAfter(Calendar calendar, Calendar when){
        int days = 0;
        if(when != null){
            java.util.GregorianCalendar mdatewhen = new java.util.GregorianCalendar(getYear(when), getMonth(when)-1,
                getDay(when));
            java.util.GregorianCalendar mdateEnd = new java.util.GregorianCalendar(getYear(calendar), getMonth(calendar)-1, getDay(calendar));
            days = (int) ( (mdateEnd.getTime().getTime() - mdatewhen.getTime().getTime()) / millisPerDay);
        }
        return days;
    }

    /**
     * ���ܣ�ȡ��ȫ���ܵ���ĩ��
     *   <p>����Vector,�ܵ���ʼ����ʱ����String[1]��ʾ��String[0]��ʾ�ܵ���ʼʱ��</p>
     * 
     * @param calendar Calendar
     * @return Vector
     */
    public static Vector getWeekStartEndVec(Calendar calendar){
        Vector<String[]> allWeekStEnVec = new Vector<String[]>();
        String[] weekStrEnd = new String[2];
        
        //ȡ������ĵ�һ��
        weekStrEnd = getFirstWeekStartEndDate(calendar);
        allWeekStEnVec.addElement(weekStrEnd);
        int firstWeekEndAt = getFirstWeekEndDay(calendar);
        
        //ȡ�������ȫ����
        int weekNum = 1 ;
        int secendWeekStr = firstWeekEndAt + 1;
        int i = 0;
        int iYear = 365;
        if(isLeapYear(calendar.get(Calendar.YEAR))){
            iYear = 366;
        }
        
        while(iYear >= secendWeekStr + 7 * i)
        {
        	weekStrEnd = new String[2];

        	weekStrEnd[0] = getDateByDayIndex(secendWeekStr + 7 * i, calendar);
        	weekStrEnd[1] = getDateByDayIndex(secendWeekStr + 7 * i + 6, calendar);
        	allWeekStEnVec.addElement(weekStrEnd);
        	i+=1;
        }
        weekNum = allWeekStEnVec.size();
        
        //������������һ��
        weekStrEnd = (String[])allWeekStEnVec.get(weekNum-2);
        allWeekStEnVec.removeElementAt(weekNum-1);
        weekStrEnd[1] = getYear(calendar) + "-12-31";
        
        return allWeekStEnVec;
    }
    
    /**
     * ���ܣ�ȡ����ǰ���е�һ�ܵ���ֹ����
     * @param calendar Calendar
     * @return String[] weekStrEnd[0] ��һ�ܵ���ʼ���ڣ�weekStrEnd[1] ��һ�ܵĽ������ڣ�
     */
    private static String[] getFirstWeekStartEndDate(Calendar calendar)
    {
        String[] weekStrEnd = new String[2];
        
        int firstWeekEndAt = getFirstWeekEndDay(calendar);
        weekStrEnd[0] = getDateByDayIndex(1, calendar);
        weekStrEnd[1] = getDateByDayIndex(firstWeekEndAt, calendar);
        return weekStrEnd;
    }
    
    /**
     * ���ܣ�������ܽ����������е�λ�á�
     * @param calendar Calendar
     * @return int
     */
    private static int getFirstWeekEndDay(Calendar calendar){
        int year = getYear(calendar);
        int firstdayAtweek = 0;
        int days = getDaysAfter(getCalendarByStr(year + "-01-01"), getCalendarByStr("1980-01-06"));
        
        //ȡ�õ�ǰ���������ڼ�
        int today = days % 7;
        if(today < 0){
            today += 7;
        }
        
        int firstDay = m_WeekStartAt;

        //�����ǰ���ڱ���ʼʱ���,�������ֱ��������1�ķ���,
        //����,��Ҫ����������ʮ���Ƽ�����λ�Ĺ�����д���
        if(today >= firstDay){
            firstdayAtweek = today - firstDay + 1;
        } else{
            firstdayAtweek = today + 7 - firstDay + 1;
        }
        
        int endday = firstdayAtweek;
        endday = 1 + 7 - firstdayAtweek;        
        return endday;
    }
    
    /**
     * ���ܣ������������ر�������������,֧�ֿ���ȵ����ڴ���
     * @param dayIndex int
     * @param calendar Calendar
     * @return java.lang.String
     */
    private static String getDateByDayIndex(int dayIndex, Calendar calendar){
        int year = getYear(calendar);
        while(true){
            int yearDays = 365;
            if(isLeapYear(year)){
                yearDays = 366;
            }

            if(dayIndex == 0){
                dayIndex = 1;
                break;
            } else if(dayIndex <= yearDays){
                break;
            } else {
                dayIndex -= yearDays;
                year += 1;
            }
        }
        
        for(int i = 1; i < 13; i++){
            int monthDay = getDaysMonth(year, i);
            if(dayIndex - monthDay > 0){
                dayIndex -= monthDay;
            } else{
                String reStr = year + "-";
                if(i < 10){
                    reStr += "0";
                }
                reStr += i + "-";
                if(dayIndex < 10){
                    reStr += "0";
                }
                reStr += dayIndex;
                return reStr;
            }
        }
        return null;
    }
    
    /**
     * ���ܣ���������͵�ƫ�����ڡ�
     * @param calendar
     * @param days
     * @return
     */
    private static String getNextDay(Calendar calendar, int days){
    	Date date = getDateAfter(calendar, days);
        return getDateStr(date);
    }
    
	/**
     * ���ܣ�����Ƿ����ꡣ
     * @param year int
     * @return boolean
     */
    public static boolean isLeapYear(int year){
        if((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)){
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * ���ܣ����ݵ�ǰ����ʵ����Ѯ�����䡣
     *   <p>����Ѯ����Ļ��ֲ���֧���û��Ķ��ƣ�ͳһΪ����Ѯ����ѮΪ10�죬ʣ�������鵽��Ѯ��</p>
     *      
     * @param isMustInit boolean �Ƿ��������ʵ����
     * @param calendar ��ǰ����
     * @return int[]
     */
    private static int[] initTenDays(boolean isMustInit, Calendar calendar){
    	//������Ǳ�����ʵ����,��ֱ�ӷ����Ѿ�ʵ�����õ�
    	if(m_tendays != null && m_tendays.length == 3){
    		if(!isMustInit){
    			return m_tendays;
    		}
    	} else {
    		m_tendays = new int[3];
    	}

    	//ʱ�䴦���࣬����GregorianCalendarȡ�õ�ǰʱ����������
    	GregorianCalendar gCalendar = new GregorianCalendar();
    	gCalendar.set(getYear(calendar), getMonth(calendar), getDay(calendar));

     	//�õ���ǰ���е�ʵ������
    	int monthDay = gCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
    	
    	//����Ѯ����Ļ��ֲ���֧���û��Ķ��ƣ�ͳһΪ����Ѯ����ѮΪ10�죬ʣ�������鵽��Ѯ
    	m_tendays[0] = 10;
    	m_tendays[1] = 10;
    	m_tendays[2] = monthDay - 20;
    	return m_tendays;
    }
    
    /**
     * ���ܣ��������ַ�������һ�� Calendar ʵ����
     * @param dateStr
     * @return
     */
    private static Calendar getCalendarByStr(String dateStr){
    	Calendar calendar = Calendar.getInstance();
    	
    	try {
            Date date = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.SIMPLIFIED_CHINESE).parse(dateStr);
            calendar.setTime(date);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    	return calendar;
    }
    
    /**
     * ����: ��ʽ�������Ͷ���Ϊ�ַ���.
     * @param aDate
     * @return
     */
    private static String getDateStr(Date aDate){
    	return getDateStr(aDate, DATE_FORMAT);
    }
    
    /**
     * ����: ����ָ��ģʽ��ʽ�������Ͷ���Ϊ�ַ���.
     * @param aDate
     * @param pattern
     * @return
     */
    private static String getDateStr(Date aDate, String pattern){
    	SimpleDateFormat dataFormate = new SimpleDateFormat(pattern);
        return dataFormate.format(aDate);
    }
    
    /**
     * ���ܣ�ת�������·�ֵΪ�ַ�����С��10�·�����.
     * @param month
     * @return
     */
    public static String getMonthStr(int month){
    	if(month < 10){
    		return "0"+month;
    	} else {
    		return ""+month;
    	}
    }
    
    /**
     * ���ܣ�ת����������ֵΪ�ַ�����С��10������.
     * @param day
     * @return
     */
    public static String getDayStr(int day){
    	if(day < 10){
    		return "0"+day;
    	} else {
    		return ""+day;
    	}
    }
    
    /**
     * ���ܣ����Ըù�����ĸ�������
     * @param args
     */
    public static void main(String[] args){
    	AppDebug.debug(getStartDay("2006-10-14", KeyFmt.WEEK_PERIOD));//@devTools System.out.println(getStartDay("2006-10-14", KeyFmt.WEEK_PERIOD));
    	AppDebug.debug(getEndDay("2006-10-14", KeyFmt.WEEK_PERIOD));//@devTools System.out.println(getEndDay("2006-10-14", KeyFmt.WEEK_PERIOD));
    }
}
