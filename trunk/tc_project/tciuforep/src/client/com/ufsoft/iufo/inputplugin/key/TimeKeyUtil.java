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
 * 功能: 提供一组对时间关键字操作方法的工具类。
 *   <p>1.计算指定日期在各种时间区间上的起始日期和结束日期。</p>
 * 创建日期:(2006-10-13 11:19:07)
 * @author chxiaowei
 */
public final class TimeKeyUtil {
	/**
	 * 周起始时间,默认为周一 
	 */
	private static int m_WeekStartAt = 1;
	
	/**
	 * 日期存储格式
	 */
	private static String DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * 根据当前月实际天数分配的旬区间数组
	 */
    private static int[] m_tendays;
	
    /**
	 * 功能：获得指定日期在各种期间类型中的开始日期。
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
			if(calendar.get(Calendar.MONTH) < 6){//上半年
				sStartDay = getYear(calendar) + "-01-01";
			} else{//下半年
				sStartDay = getYear(calendar) + "-07-01";
			}
		} else if(timePeriod.equals(KeyFmt.SEASON_PERIOD)){
			if(calendar.get(Calendar.MONTH) < 3){//一季度
				sStartDay = getYear(calendar) + "-01-01";
			} else if(calendar.get(Calendar.MONTH) < 6){//二季度
				sStartDay = getYear(calendar) + "-04-01";
			} else if(calendar.get(Calendar.MONTH) < 9){//三季度
				sStartDay = getYear(calendar) + "-07-01";
			} else{//四季度
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
			//得到当前天位于当前周的第几天
			int iDay = getWeek(calendar);
            sStartDay = getNextDay(calendar, -iDay+1);
        	
            //如果是上一年，则开始日期为：本年+"-01-01"
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
	 * 功能：获得指定日期在各种期间类型中的结束日期。
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
			if(calendar.get(Calendar.MONTH) < 6){//上半年
				sEndDay = getYear(calendar) + "-06-30";
			} else {//下半年
				sEndDay = getYear(calendar) + "-12-31";
			}
		} else if(timePeriod.equals(KeyFmt.SEASON_PERIOD)){
			if(calendar.get(Calendar.MONTH) < 3){//一季度
				sEndDay = getYear(calendar) + "-03-31";
			}else if(calendar.get(Calendar.MONTH) < 6){//二季度
				sEndDay = getYear(calendar) + "-06-30";
			}else if(calendar.get(Calendar.MONTH) < 9){//三季度
				sEndDay = getYear(calendar) + "-09-30";
			}else{//四季度
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
			//得到当前天位于当前周的第几天
            int index = getWeek(calendar);

            //这里不再取集中端首末周属性设置，按默认规则校验
            Vector weekVec = getWeekStartEndVec(calendar);
            
            //检查当前日期是否落在当年第一周内
            int daysAfterStart = 0;
            int daysAfterEnd = 0;
            String[] weekstr = (String[])weekVec.get(0);
            
            daysAfterStart = getDaysAfter(calendar, getCalendarByStr(weekstr[0]));
            daysAfterEnd = getDaysAfter(calendar, getCalendarByStr(weekstr[1]));
            if(daysAfterStart >= 0 && daysAfterEnd <= 0)
            {
            	sEndDay =  weekstr[1];
            }

            //检查当前日期是否落在当前的末周
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
            
            //当前日期不在第一周和末周时，计算末周所在日期
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
	 * 功能：获取该日期的年。
	 * @param calendar
	 * @return
	 */
	private static int getYear(Calendar calendar){
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * 功能：获取该日期的月份。由于Calendar取得的月份是从0开始的，因此这里加上1.
	 * @param calendar
	 * @return
	 */
	private static int getMonth(Calendar calendar){
		return calendar.get(Calendar.MONTH);
	}
	
	/**
	 * 功能：获取该日期的日期。
	 * @param calendar
	 * @return
	 */
	private static int getDay(Calendar calendar){
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 功能：获取该日期的周。
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
	 * 功能：获取指定年度的指定月份的最大天数。
	 * @param calendar
	 * @return
	 */
	public static int getDaysMonth(Calendar calendar){
		//getDaysMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 功能：获取指定年度的指定月份的最大天数。
	 * @param year int 指定年度
	 * @param month int 指定月份
	 * @return 该月最大天数
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
     * 功能：返回同指定日期相差指定天数后的日期。
     * @param calendar Calendar 指定日期
     * @param days int 相差天数
     * @return Date
     */
    public static Date getDateAfter(Calendar calendar, int days){
        java.util.GregorianCalendar mdate = new java.util.GregorianCalendar(getYear(calendar), getMonth(calendar), getDay(calendar));
        return new Date(mdate.getTime().getTime() + millisPerDay * days);
    }
    /**
     * 功能：返回某一日期距今天数，负数表示在今天之后。
     * @return calendar Calendar
     * @param when Calendar
     * @return int 相差天数
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
     * 功能：取得全部周的首末。
     *   <p>构造Vector,周的起始结束时间用String[1]表示，String[0]表示周的起始时间</p>
     * 
     * @param calendar Calendar
     * @return Vector
     */
    public static Vector getWeekStartEndVec(Calendar calendar){
        Vector<String[]> allWeekStEnVec = new Vector<String[]>();
        String[] weekStrEnd = new String[2];
        
        //取出本年的第一周
        weekStrEnd = getFirstWeekStartEndDate(calendar);
        allWeekStEnVec.addElement(weekStrEnd);
        int firstWeekEndAt = getFirstWeekEndDay(calendar);
        
        //取出本年的全部周
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
        
        //调整本年的最后一周
        weekStrEnd = (String[])allWeekStEnVec.get(weekNum-2);
        allWeekStEnVec.removeElementAt(weekNum-1);
        weekStrEnd[1] = getYear(calendar) + "-12-31";
        
        return allWeekStEnVec;
    }
    
    /**
     * 功能：取到当前年中第一周的起止日期
     * @param calendar Calendar
     * @return String[] weekStrEnd[0] 第一周的起始日期，weekStrEnd[1] 第一周的结束日期；
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
     * 功能：获得首周结束日在年中的位置。
     * @param calendar Calendar
     * @return int
     */
    private static int getFirstWeekEndDay(Calendar calendar){
        int year = getYear(calendar);
        int firstdayAtweek = 0;
        int days = getDaysAfter(getCalendarByStr(year + "-01-01"), getCalendarByStr("1980-01-06"));
        
        //取得当前日期是星期几
        int today = days % 7;
        if(today < 0){
            today += 7;
        }
        
        int firstDay = m_WeekStartAt;

        //如果单前日期比起始时间大,则可以用直接相减后加1的方法,
        //否则,需要按照类似于十进制减法借位的规则进行处理
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
     * 功能：根据天数返回本年年初起的日期,支持跨年度的日期处理
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
     * 功能：获得日类型的偏移日期。
     * @param calendar
     * @param days
     * @return
     */
    private static String getNextDay(Calendar calendar, int days){
    	Date date = getDateAfter(calendar, days);
        return getDateStr(date);
    }
    
	/**
     * 功能：检查是否闰年。
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
     * 功能：根据当前日期实例化旬的区间。
     *   <p>这里旬区间的划分不再支持用户的定制，统一为：上旬、中旬为10天，剩下天数归到下旬。</p>
     *      
     * @param isMustInit boolean 是否必须重新实例化
     * @param calendar 当前日期
     * @return int[]
     */
    private static int[] initTenDays(boolean isMustInit, Calendar calendar){
    	//如果不是必须重实例化,则直接返回已经实例化好的
    	if(m_tendays != null && m_tendays.length == 3){
    		if(!isMustInit){
    			return m_tendays;
    		}
    	} else {
    		m_tendays = new int[3];
    	}

    	//时间处理类，按照GregorianCalendar取得当前时间的相关属性
    	GregorianCalendar gCalendar = new GregorianCalendar();
    	gCalendar.set(getYear(calendar), getMonth(calendar), getDay(calendar));

     	//得到当前月中的实际天数
    	int monthDay = gCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
    	
    	//这里旬区间的划分不再支持用户的定制，统一为：上旬、中旬为10天，剩下天数归到下旬
    	m_tendays[0] = 10;
    	m_tendays[1] = 10;
    	m_tendays[2] = monthDay - 20;
    	return m_tendays;
    }
    
    /**
     * 功能：由日期字符串生成一个 Calendar 实例。
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
     * 功能: 格式化日期型对象为字符串.
     * @param aDate
     * @return
     */
    private static String getDateStr(Date aDate){
    	return getDateStr(aDate, DATE_FORMAT);
    }
    
    /**
     * 功能: 根据指定模式格式化日期型对象为字符串.
     * @param aDate
     * @param pattern
     * @return
     */
    private static String getDateStr(Date aDate, String pattern){
    	SimpleDateFormat dataFormate = new SimpleDateFormat(pattern);
        return dataFormate.format(aDate);
    }
    
    /**
     * 功能：转换整数月份值为字符串，小于10月份则补零.
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
     * 功能：转换整数日期值为字符串，小于10号则补零.
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
     * 功能：测试该工具类的各方法。
     * @param args
     */
    public static void main(String[] args){
    	AppDebug.debug(getStartDay("2006-10-14", KeyFmt.WEEK_PERIOD));//@devTools System.out.println(getStartDay("2006-10-14", KeyFmt.WEEK_PERIOD));
    	AppDebug.debug(getEndDay("2006-10-14", KeyFmt.WEEK_PERIOD));//@devTools System.out.println(getEndDay("2006-10-14", KeyFmt.WEEK_PERIOD));
    }
}
