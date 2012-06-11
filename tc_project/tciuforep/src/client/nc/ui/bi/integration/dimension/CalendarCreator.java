/*
 * 创建日期 2005-5-26
 */
package nc.ui.bi.integration.dimension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import nc.util.iufo.pub.IDMaker;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.integration.dimension.DimensionException;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.integration.dimension.TimeMemberVO;
import nc.vo.iufo.pub.date.UFODate;

import com.ufsoft.iufo.resource.StringResource;

/**
 * @author bh
 * 
 * 日历生成器
 */
public class CalendarCreator {

	private class CalendarItem {
		private int year;

		private int halfYear;

		private int quarter;

		private int month;

		private int week;

		private int tenDay;

		private TimeMemberVO vo;

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public TimeMemberVO getVO() {
			return vo;
		}

		public void setVO(TimeMemberVO vo) {
			this.vo = vo;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public int getWeek() {
			return week;
		}

		public void setWeek(int week) {
			this.week = week;
		}

		public int getTenDay() {
			return tenDay;
		}

		public void setTenDay(int tenDay) {
			this.tenDay = tenDay;
		}

		public int getHalfYear() {
			return halfYear;
		}

		public void setHalfYear(int halfYear) {
			this.halfYear = halfYear;
		}

		public int getQuarter() {
			return quarter;
		}

		public void setQuarter(int quarter) {
			this.quarter = quarter;
		}

	}

	// 日历生成规则
	private ArrayList m_alRule;

	// 开始日期
	private UFODate m_StartDate;

	// 结束日期
	private UFODate m_EndDate;

	private DimensionVO m_voDef;

	private String m_calendarGenRule;

	private String m_calendarSplitString;


	private DimMemberSrv m_srv = null;// 维度成员服务

	// //存储级次编码和PK的对照
	// private HashMap map_codePK = new HashMap();

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public CalendarCreator(String sStartDate, String sEndDate, String[] sRule, String calendarGenRule,
			String calendarSplitString, DimensionVO voDef) {
		super();
		m_alRule = new ArrayList();
		for (int i = 0; i < sRule.length; i++) {
			m_alRule.add(sRule[i]);
		}

		m_calendarGenRule = calendarGenRule;
		m_calendarSplitString = calendarSplitString;
		m_StartDate = new UFODate(sStartDate);
		m_EndDate = new UFODate(sEndDate);
		m_voDef = voDef;
	}

	private String getCalendarGenRule() {
		if (m_calendarGenRule == null) {
			m_calendarGenRule = CalendarDesignAction.CALENDAR_GEN_SPLITER[0];
		}
		return m_calendarGenRule;
	}

	private String getCalendarSplitString() {
		if (m_calendarSplitString == null
				|| CalendarDesignAction.CALENDAR_NULL_SPLITER.equalsIgnoreCase(m_calendarSplitString)) {
			m_calendarSplitString = "";
		}
		return m_calendarSplitString;
	}

	/**
	 * 检查时间期间是否合法
	 * 
	 * @param sPeriods
	 * @return
	 */
	public boolean isRight(String[] sPeriods) {
		return true;
	}

	@SuppressWarnings("unchecked")
	private Vector addMembers(Vector v, CalendarItem[] vos) {
		if (vos != null && vos.length > 0) {
			for (int i = 0; i < vos.length; i++) {
				vos[i].getVO().setTableName(m_voDef.getTablename());
				vos[i].getVO().setCalattr(new Integer(DimRescource.INT_CACLRULE_ADD));
				v.add(vos[i]);
			}
		}
		return v;
	}

	/**
	 * 生成日历树根节点
	 * 
	 * @return
	 */
	private CalendarItem[] genRoot() {

		CalendarItem vo = new CalendarItem();
		vo.setVO((TimeMemberVO) getMemberSrv().getRoot());

		return new CalendarItem[] { vo };
	}

	private DimMemberSrv getMemberSrv() {
		if (m_srv == null)
			m_srv = new DimMemberSrv(getDimDef());

		return m_srv;
	}

	/**
	 * 检测生成规则是否符合要求
	 * 
	 * @param ruleList
	 * @return
	 * @i18n mbidim00037=日期生成规则为空
	 * @i18n ubiquery0112=层次
	 * @i18n mbidim00038=必选
	 * @i18n mbidim00039=如果选择层次
	 * @i18n mbidim00040=、层次
	 * @i18n mbidim00041=则层次
	 * @i18n mbidim00042=和层次
	 * @i18n mbidim00043=不能同时选择
	 */
	public static String validateCalendarRule(final ArrayList ruleList) {

		if (ruleList == null || ruleList.isEmpty()) {
			return StringResource.getStringResource("mbidim00037");
		}

		// // ?年必须选择
		if (ruleList.contains(DimRescource.TIME_YEAR) == false) {
			return StringResource.getStringResource("ubiquery0112") + DimRescource.TIME_YEAR
					+ StringResource.getStringResource("mbidim00038");
		}

		// 如果选择日或者旬，周，则必须选择月
		if (ruleList.contains(DimRescource.TIME_DAY) || ruleList.contains(DimRescource.TIME_TENDAY)
				|| ruleList.contains(DimRescource.TIME_WEEK)) {
			if (ruleList.contains(DimRescource.TIME_MONTH) == false) {
				return StringResource.getStringResource("mbidim00039") + DimRescource.TIME_DAY
						+ StringResource.getStringResource("mbidim00040") + DimRescource.TIME_TENDAY
						+ StringResource.getStringResource("mbidim00040") + DimRescource.TIME_WEEK
						+ StringResource.getStringResource("mbidim00041") + DimRescource.TIME_MONTH
						+ StringResource.getStringResource("mbidim00038");
			}
		}
		// 周和旬不能同时选择
		if (ruleList.contains(DimRescource.TIME_WEEK) && ruleList.contains(DimRescource.TIME_TENDAY)) {
			return StringResource.getStringResource("ubiquery0112") + DimRescource.TIME_WEEK
					+ StringResource.getStringResource("mbidim00042") + DimRescource.TIME_TENDAY
					+ StringResource.getStringResource("mbidim00043");
		}
		return null;
	}

	public TimeMemberVO[] createCalendar() throws DimensionException {

		if (validateCalendarRule(m_alRule) != null) {
			throw new IllegalArgumentException(validateCalendarRule(m_alRule));
		}

		CalendarItem[] vos = null;
		Vector vMembers = new Vector();

		// Root
		vos = genRoot();
		// vMembers = addMembers(vMembers, vos);
		// 年
		vos = genYear(vos, null);
		vMembers = addMembers(vMembers, vos);
		String parent = DimRescource.TIME_YEAR;

		if (m_alRule.contains(DimRescource.TIME_HALFYEAR)) {
			vos = genHalfYear(vos, parent);
			vMembers = addMembers(vMembers, vos);
			parent = DimRescource.TIME_HALFYEAR;
		}
		if (m_alRule.contains(DimRescource.TIME_QUARTER)) {
			vos = genQuarter(vos, parent);
			vMembers = addMembers(vMembers, vos);
			parent = DimRescource.TIME_QUARTER;
		}
		if (m_alRule.contains(DimRescource.TIME_MONTH)) {
			vos = genMonth(vos, parent);
			vMembers = addMembers(vMembers, vos);
			parent = DimRescource.TIME_MONTH;
		}
		if (m_alRule.contains(DimRescource.TIME_TENDAY)) {
			vos = genTenDay(vos, parent);
			vMembers = addMembers(vMembers, vos);
			parent = DimRescource.TIME_TENDAY;
		} else if (m_alRule.contains(DimRescource.TIME_WEEK)) {
			vos = genWeek(vos, parent);
			vMembers = addMembers(vMembers, vos);
			parent = DimRescource.TIME_WEEK;
		}
		if (m_alRule.contains(DimRescource.TIME_DAY)) {
			vos = genDay(vos, parent);
			vMembers = addMembers(vMembers, vos);
		}

		vos = new CalendarItem[vMembers.size()];
		vMembers.copyInto(vos);

		TimeMemberVO[] result = new TimeMemberVO[vos.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = vos[i].getVO();
		}
		return result;
	}

	/**
	 * 生成年份
	 * 
	 * @param year
	 * @return
	 */
	@SuppressWarnings( { "deprecation", "unchecked" })
	private CalendarItem[] genYear(CalendarItem[] vosParent, String sParent) {
		int startYear = m_StartDate.getYear();
		int endYear = m_EndDate.getYear();
		int iLen = endYear - startYear + 1;

		CalendarItem[] vos = null;
		String name = null;
		String code = null;
		String sYear = null;

		String[] items = getCalendarGenRule().split("-");
		String yearFormat = items[0];
		SimpleDateFormat format = new SimpleDateFormat(yearFormat);

		Vector vMember = new Vector();
		for (int j = 0; j < vosParent.length; j++) {
			vos = new CalendarItem[iLen];
			for (int i = 0; i < iLen; i++) {
				int year = startYear + i;
				sYear = Integer.toString(year);
				name = sYear + DimRescource.TIME_YEAR;
				// code = DimRescource.PRE_YEAR + sYear;

				Date date = new Date();
				date.setYear(year - 1900);
				code = format.format(date);

				// 构造vo
				vos[i] = buildVO(vosParent[j], name, code);
				vos[i].setYear(year);
				vMember.add(vos[i]);
			}
		}

		if (vMember.size() > 0) {
			vos = new CalendarItem[vMember.size()];
			vMember.copyInto(vos);
		}
		return vos;
	}

	/**
	 * 生成半年
	 * 
	 * @param voParents
	 * @param sParent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private CalendarItem[] genHalfYear(CalendarItem[] vosParent, String sParent) {
		CalendarItem[] vos = null;
		Vector vMembers = new Vector();
		String name = null;
		String code = null;
		String sYear = null;

		for (int i = 0; i < vosParent.length; i++) {

			int year = vosParent[i].getYear();
			sYear = Integer.toString(year);

			vos = new CalendarItem[2];
			for (int j = 0; j < vos.length; j++) {
				vos[j] = new CalendarItem();
				if (j == 0)
					name = sYear + DimRescource.TIME_YEAR + DimRescource.TIME_UP + DimRescource.TIME_HALFYEAR;
				else
					name = sYear + DimRescource.TIME_YEAR + DimRescource.TIME_DOWN + DimRescource.TIME_HALFYEAR;
				code = DimRescource.PRE_YEAR + sYear + getCalendarSplitString() + DimRescource.PRE_HALFYEAR + j;
				// 构造vo
				vos[j] = buildVO(vosParent[i], name, code);
				vos[j].setHalfYear(j);
				vMembers.add(vos[j]);
			}
		}
		// 获得时间范围内的有效成员
		vos = getValidItemsFromVector(vMembers, (m_StartDate.getMonth() - 1) / 6, (12 - m_EndDate.getMonth()) / 6);
		return vos;
	}

	/**
	 * 生成季
	 * 
	 * @param voParents
	 * @param sParent
	 * @return
	 */
	@SuppressWarnings( { "unchecked" })
	private CalendarItem[] genQuarter(CalendarItem[] vosParent, String sParent) {
		CalendarItem[] vos = null;
		Vector vMembers = new Vector();
		String name = null;
		String code = null;
		String sYear = null;
		// 年-季
		if (sParent.equals(DimRescource.TIME_YEAR)) {
			for (int i = 0; i < vosParent.length; i++) {

				int year = vosParent[i].getYear();
				sYear = Integer.toString(year);

				vos = new CalendarItem[4];
				for (int j = 0; j < vos.length; j++) {
					vos[j] = new CalendarItem();
					int quarter = j + 1;

					name = sYear + DimRescource.TIME_YEAR + quarter + DimRescource.TIME_QUARTER;
					code = DimRescource.PRE_YEAR + sYear + getCalendarSplitString() + DimRescource.PRE_QUARTER
							+ quarter;
					// 构造vo
					vos[j] = buildVO(vosParent[i], name, code);
					vos[j].setQuarter(quarter);
					vMembers.add(vos[j]);
				}
			}
			// 获得时间范围内的有效成员
			vos = getValidItemsFromVector(vMembers, (m_StartDate.getMonth() - 1) / 3, (12 - m_EndDate.getMonth()) / 3);
		}
		// 半年-季
		else if (sParent.equals(DimRescource.TIME_HALFYEAR)) {
			for (int i = 0; i < vosParent.length; i++) {

				int year = vosParent[i].getYear();
				sYear = Integer.toString(year);
				int quarter = 0;
				vos = new CalendarItem[2];
				for (int j = 0; j < vos.length; j++) {
					vos[j] = new CalendarItem();

					// 上半年：1、2季
					if (vosParent[i].getHalfYear() == 0) {
						quarter = j + 1;
					} else {// 下半年：3、4季
						quarter = j + 3;
					}
					name = sYear + DimRescource.TIME_YEAR + quarter + DimRescource.TIME_QUARTER;
					code = DimRescource.PRE_YEAR + sYear + getCalendarSplitString() + DimRescource.PRE_QUARTER
							+ quarter;
					// 构造vo
					vos[j] = buildVO(vosParent[i], name, code);
					vos[j].setQuarter(quarter);
					vMembers.add(vos[j]);
				}
			}
			// 获得时间范围内的有效成员
			vos = getValidItemsFromVector(vMembers, ((m_StartDate.getMonth() - 1) % 6) / 3,
					((12 - m_EndDate.getMonth()) % 6) / 3);
		}
		// vos = new CalendarItem[vMembers.size()];
		// vMembers.copyInto(vos);
		return vos;
	}

	/**
	 * 生成月
	 * 
	 * @param vosParent
	 * @param sParent
	 */
	@SuppressWarnings( { "deprecation", "unchecked" })
	private CalendarItem[] genMonth(CalendarItem[] vosParent, String sParent) {
		CalendarItem[] vos = null;
		Vector vMembers = new Vector();
		String name = null;
		String code = null;
		String sYear = null;
		String sMonth = null;

		String[] items = getCalendarGenRule().split("-");
		String strFormat = items[0] + getCalendarSplitString() + items[1];
		SimpleDateFormat format = new SimpleDateFormat(strFormat);

		// 年-月
		if (sParent.equals(DimRescource.TIME_YEAR)) {

			for (int i = 0; i < vosParent.length; i++) {
				int year = vosParent[i].getYear();
				sYear = Integer.toString(year);

				vos = new CalendarItem[12];
				for (int j = 0; j < vos.length; j++) {
					vos[j] = new CalendarItem();
					int month = j + 1;

					sMonth = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
					name = sYear + DimRescource.TIME_YEAR + sMonth + DimRescource.TIME_MONTH;

					// code = DimRescource.PRE_YEAR + sYear +
					// DimRescource.PRE_MONTH + sMonth;
					Date date = new Date(year);
					date.setYear(year - 1900);
					date.setMonth(month - 1);
					code = format.format(date);

					// 构造vo
					vos[j] = buildVO(vosParent[i], name, code);
					vos[j].setMonth(month);
					vMembers.add(vos[j]);
				}
			}
			// 获得时间范围内的有效成员
			vos = getValidItemsFromVector(vMembers, m_StartDate.getMonth() - 1, (12 - m_EndDate.getMonth()));
		}
		// 半年-月
		else if (sParent.equals(DimRescource.TIME_HALFYEAR)) {
			for (int i = 0; i < vosParent.length; i++) {

				int year = vosParent[i].getYear();
				sYear = Integer.toString(year);
				int month = 0;
				vos = new CalendarItem[6];
				for (int j = 0; j < vos.length; j++) {
					vos[j] = new CalendarItem();
					// 上半年：1-6月
					if (vosParent[i].getHalfYear() == 0) {
						month = j + 1;
					} else {// 下半年：7-12月
						month = j + 7;
					}

					sMonth = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
					name = sYear + DimRescource.TIME_YEAR + sMonth + DimRescource.TIME_MONTH;
					// code = DimRescource.PRE_YEAR + sYear +
					// DimRescource.PRE_MONTH + sMonth;

					Date date = new Date(year);
					date.setYear(year - 1900);
					date.setMonth(month - 1);
					code = format.format(date);

					// 构造vo
					vos[j] = buildVO(vosParent[i], name, code);
					vos[j].setMonth(month);
					vMembers.add(vos[j]);
				}
			}
			// 获得时间范围内的有效成员
			vos = getValidItemsFromVector(vMembers, (m_StartDate.getMonth() - 1) % 6, (12 - m_EndDate.getMonth()) % 6);
		}
		// 季-月
		else if (sParent.equals(DimRescource.TIME_QUARTER)) {

			for (int i = 0; i < vosParent.length; i++) {

				int year = vosParent[i].getYear();
				sYear = Integer.toString(year);
				int month = 0;

				vos = new CalendarItem[3];
				for (int j = 0; j < vos.length; j++) {
					vos[j] = new CalendarItem();

					// 1季：1-3月
					if (vosParent[i].getQuarter() == 1)
						month = j + 1;
					// 2季：4-6月
					else if (vosParent[i].getQuarter() == 2)
						month = j + 4;
					// 3季：7-9月
					else if (vosParent[i].getQuarter() == 3)
						month = j + 7;
					// 4季：10-12月
					else if (vosParent[i].getQuarter() == 4)
						month = j + 10;

					sMonth = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
					name = sYear + DimRescource.TIME_YEAR + sMonth + DimRescource.TIME_MONTH;
					// code = DimRescource.PRE_YEAR + sYear +
					// DimRescource.PRE_MONTH + sMonth;
					Date date = new Date(year);
					date.setYear(year - 1900);
					date.setMonth(month - 1);
					code = format.format(date);

					// 构造vo
					vos[j] = buildVO(vosParent[i], name, code);
					vos[j].setMonth(month);
					vMembers.add(vos[j]);
				}
			}
			// 获得时间范围内的有效成员
			vos = getValidItemsFromVector(vMembers, (m_StartDate.getMonth() - 1) % 3, (12 - m_EndDate.getMonth()) % 3);

		}
		// 获得时间范围内的有效成员
		// vos = new CalendarItem[vMembers.size()];
		// vMembers.copyInto(vos);
		return vos;
	}

	/**
	 * 生成旬
	 * 
	 * @param vosParent
	 * @param sParent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private CalendarItem[] genTenDay(CalendarItem[] vosParent, String sParent) throws DimensionException {
		if (!sParent.equals(DimRescource.TIME_MONTH))
			throw new DimensionException(DimensionException.ERR_MISTSKERULE);
		CalendarItem[] vos = null;
		Vector vMembers = new Vector();
		String name = null;
		String code = null;
		String sYear = null;

		// 月-旬
		for (int i = 0; i < vosParent.length; i++) {

			int year = vosParent[i].getYear();
			sYear = Integer.toString(year);
			// String sParentCode = vosParent[i].getVO().getMemcode();
			// int month =
			// Integer.parseInt(sParentCode.substring(sParentCode.indexOf(DimRescource.PRE_MONTH)
			// + 1));
			int month = vosParent[i].getMonth();

			vos = new CalendarItem[3];
			for (int j = 0; j < vos.length; j++) {
				vos[j] = new CalendarItem();

				switch (j) {
				case 0:// 上旬
					name = sYear + DimRescource.TIME_YEAR + month + DimRescource.TIME_MONTH + DimRescource.TIME_UP
							+ DimRescource.TIME_TENDAY;
					break;
				case 1:// 中旬
					name = sYear + DimRescource.TIME_YEAR + month + DimRescource.TIME_MONTH + DimRescource.TIME_MID
							+ DimRescource.TIME_TENDAY;
					break;
				case 2:// 下旬
					name = sYear + DimRescource.TIME_YEAR + month + DimRescource.TIME_MONTH + DimRescource.TIME_DOWN
							+ DimRescource.TIME_TENDAY;
					break;
				}
				int tenDayIndex = j + 1;
				code = DimRescource.PRE_YEAR + sYear + getCalendarSplitString() + DimRescource.PRE_MONTH + month
						+ getCalendarSplitString() + DimRescource.PRE_TENDAY + tenDayIndex;
				// 构造vo
				vos[j] = buildVO(vosParent[i], name, code);
				vos[j].setTenDay(tenDayIndex);
				vMembers.add(vos[j]);
			}
		}
		// 获得时间范围内的有效成员
		vos = getValidItemsFromVector(vMembers, Math.min(2, m_StartDate.getDay() / 10), Math.max(0, 2 - m_EndDate
				.getDay() / 10));
		// vos = new CalendarItem[vMembers.size()];
		// vMembers.copyInto(vos);
		return vos;
	}

	@SuppressWarnings("unchecked")
	private CalendarItem[] genWeek(CalendarItem[] vosParent, String sParent) throws DimensionException {
		if (!sParent.equals(DimRescource.TIME_MONTH))
			throw new DimensionException(DimensionException.ERR_MISTSKERULE);
		CalendarItem[] vos = null;
		Vector vMembers = new Vector();
		String name = null;
		String code = null;
		String sYear = null;

		int lastTotalWeeks = 0;
		// 月-周
		for (int i = 0; i < vosParent.length; i++) {

			int year = vosParent[i].getYear();
			sYear = Integer.toString(year);
			// String sParentCode = vosParent[i].getVO().getMemcode();
			// int month =
			// Integer.parseInt(sParentCode.substring(sParentCode.indexOf(DimRescource.PRE_MONTH)
			// + 1));
			int month = vosParent[i].getMonth();

			int totalDays = getDayNumbers(year, month);
			int totalWeeks = totalDays / 7;
			if ((totalDays % 7) != 0)
				totalWeeks++;

			if (i == vosParent.length - 1)
				lastTotalWeeks = totalWeeks;
			vos = new CalendarItem[totalWeeks];
			for (int j = 0; j < vos.length; j++) {
				vos[j] = new CalendarItem();
				int weekIndex = j + 1;
				name = sYear + DimRescource.TIME_YEAR + month + DimRescource.TIME_MONTH + weekIndex
						+ DimRescource.TIME_WEEK;
				code = DimRescource.PRE_YEAR + sYear + getCalendarSplitString() + DimRescource.PRE_MONTH + month
						+ getCalendarSplitString() + DimRescource.PRE_WEEK + weekIndex;
				// 构造vo
				vos[j] = buildVO(vosParent[i], name, code);
				vos[j].setWeek(weekIndex);
				vMembers.add(vos[j]);
			}
		}
		// 获得时间范围内的有效成员
		vos = getValidItemsFromVector(vMembers, (m_StartDate.getDay() - 1) / 7,
				(lastTotalWeeks - (m_EndDate.getDay() - 1) / 7) - 1);
		// vos = new CalendarItem[vMembers.size()];
		// vMembers.copyInto(vos);
		return vos;
	}

	/**
	 * 生成日
	 * 
	 * @param vosParent
	 * @param sParent
	 * @return
	 */
	@SuppressWarnings( { "deprecation", "unchecked" })
	private CalendarItem[] genDay(CalendarItem[] vosParent, String sParent) throws DimensionException {
		CalendarItem[] vos = null;
		Vector vMembers = new Vector();
		String name = null;
		String code = null;
		String sYear = null;
		String sMonth = null;
		String sDay = null;

		String[] items = getCalendarGenRule().split("-");
		String strFormat = items[0] + getCalendarSplitString() + items[1] + getCalendarSplitString() + items[2];
		SimpleDateFormat format = new SimpleDateFormat(strFormat);

		// 月-日
		if (sParent.equals(DimRescource.TIME_MONTH)) {
			int lastDayNumbers = 0;
			for (int i = 0; i < vosParent.length; i++) {

				int year = vosParent[i].getYear();
				sYear = Integer.toString(year);
				// String sParentCode = vosParent[i].getVO().getMemcode();
				// int month =
				// Integer.parseInt(sParentCode.substring(sParentCode.indexOf(DimRescource.PRE_MONTH)
				// + 1));
				int month = vosParent[i].getMonth();
				int dayNumbers = getDayNumbers(year, month);
				if (i == vosParent.length - 1)
					lastDayNumbers = dayNumbers;
				vos = new CalendarItem[dayNumbers];
				for (int j = 0; j < vos.length; j++) {
					vos[j] = new CalendarItem();
					int day = j + 1;

					sMonth = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
					sDay = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);
					name = sYear + DimRescource.TIME_YEAR + sMonth + DimRescource.TIME_MONTH + sDay
							+ DimRescource.TIME_DAY;

					// code = DimRescource.PRE_YEAR + sYear +
					// DimRescource.PRE_MONTH + sMonth + DimRescource.PRE_DAY +
					// sDay;
					Date date = new Date(year - 1900, month - 1, day);
					code = format.format(date);

					// 构造vo
					vos[j] = buildVO(vosParent[i], name, code);
					vMembers.add(vos[j]);
				}
			}
			// 获得时间范围内的有效成员
			vos = getValidItemsFromVector(vMembers, m_StartDate.getDay() - 1, lastDayNumbers - m_EndDate.getDay());
		}
		// 旬-日
		else if (sParent.equals(DimRescource.TIME_TENDAY)) {
			for (int i = 0; i < vosParent.length; i++) {
				int year = vosParent[i].getYear();
				sYear = Integer.toString(year);
				int month = vosParent[i].getMonth();

				if (vosParent[i].getTenDay() == 3) {
					vos = new CalendarItem[getDayNumbers(year, month) - 20];
				} else {
					vos = new CalendarItem[10];
				}

				for (int j = 0; j < vos.length; j++) {
					vos[j] = new CalendarItem();
					int day = 0;
					// 上旬
					if (vosParent[i].getTenDay() == 1)
						day = j + 1;
					// 中旬
					else if (vosParent[i].getTenDay() == 2)
						day = j + 11;
					// 下旬
					else
						day = j + 21;
					sMonth = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
					sDay = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);
					name = sYear + DimRescource.TIME_YEAR + sMonth + DimRescource.TIME_MONTH + sDay
							+ DimRescource.TIME_DAY;

					// code = DimRescource.PRE_YEAR + sYear +
					// DimRescource.PRE_MONTH + sMonth + DimRescource.PRE_DAY +
					// sDay;
					Date date = new Date(year - 1900, month - 1, day);
					code = format.format(date);
					// 构造vo
					vos[j] = buildVO(vosParent[i], name, code);
					vMembers.add(vos[j]);
				}
			}
			// 获得时间范围内的有效成员
			vos = getValidItemsFromVector(vMembers, Math.min(2, m_StartDate.getDay() - 1) / 10, Math.max(0,
					2 - m_EndDate.getDay() / 10));

		}// 周-日
		else if (sParent.equals(DimRescource.TIME_WEEK)) {
			for (int i = 0; i < vosParent.length; i++) {
				int year = vosParent[i].getYear();
				sYear = Integer.toString(year);

				int month = vosParent[i].getMonth();
				int week = vosParent[i].getWeek();
				int totalDays = getDayNumbers(year, month);
				int totalWeeks = totalDays / 7;
				if ((totalDays % 7) != 0)
					totalWeeks++;

				if (totalWeeks == week) {
					int remainDays = totalDays - (totalWeeks - 1) * 7;
					vos = new CalendarItem[remainDays];
				} else {
					vos = new CalendarItem[7];
				}

				int dayStart = (week - 1) * 7;
				for (int j = 0; j < vos.length; j++) {
					vos[j] = new CalendarItem();
					int day = dayStart + j + 1;

					sMonth = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
					sDay = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);
					name = sYear + DimRescource.TIME_YEAR + sMonth + DimRescource.TIME_MONTH + sDay
							+ DimRescource.TIME_DAY;

					// code = DimRescource.PRE_YEAR + sYear +
					// DimRescource.PRE_MONTH + sMonth + DimRescource.PRE_DAY +
					// sDay;
					Date date = new Date(year - 1900, month - 1, day);
					code = format.format(date);
					// 构造vo
					vos[j] = buildVO(vosParent[i], name, code);
					vMembers.add(vos[j]);
				}
			}
			// 获得时间范围内的有效成员
			vos = getValidItemsFromVector(vMembers, (m_StartDate.getDay() - 1) % 7, (6 - (m_EndDate.getDay() - 1) % 7));
		}
		// vos = new CalendarItem[vMembers.size()];
		// vMembers.copyInto(vos);
		return vos;
	}

	// 计算某年某月有多少天
	private int getDayNumbers(int year, int month) {
		CalendarItem[] vos = null;
		switch (month) {
		case 1:
			vos = new CalendarItem[31];
			break;
		case 2:
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				vos = new CalendarItem[29];
			else
				vos = new CalendarItem[28];
			break;
		case 3:
			vos = new CalendarItem[31];
			break;
		case 4:
			vos = new CalendarItem[30];
			break;
		case 5:
			vos = new CalendarItem[31];
			break;
		case 6:
			vos = new CalendarItem[30];
			break;
		case 7:
			vos = new CalendarItem[31];
			break;
		case 8:
			vos = new CalendarItem[31];
			break;
		case 9:
			vos = new CalendarItem[30];
			break;
		case 10:
			vos = new CalendarItem[31];
			break;
		case 11:
			vos = new CalendarItem[30];
			break;
		case 12:
			vos = new CalendarItem[31];
			break;

		default:
			throw new DimensionException(DimensionException.ERR_CREATECALENDAR);
		}

		return vos.length;
	}

	/**
	 * 构造vo
	 * 
	 * @param voParent
	 * @param name
	 * @param code
	 * @return
	 */
	private CalendarItem buildVO(CalendarItem voParent, String name, String code) {
		CalendarItem vo = new CalendarItem();
		vo.setVO(new TimeMemberVO());
		vo.getVO().setMemberID(IDMaker.makeID(20));
		int depth = voParent.getVO().getDepth().intValue() + 1;
		vo.getVO().setMemname(name);
		vo.getVO().setMemcode(code);
		vo.getVO().setJoinfield(code);

		vo.setYear(voParent.getYear());
		vo.setHalfYear(voParent.getHalfYear());
		vo.setQuarter(voParent.getQuarter());
		vo.setMonth(voParent.getMonth());
		vo.setTenDay(voParent.getTenDay());
		vo.setWeek(voParent.getWeek());

		vo.getVO().setDepth(new Integer(depth));
		/** TODO */
		// vo.setCalcAttrs(new Integer(0));
		genLvlAttribute(vo, voParent, new Integer(1));
		return vo;
	}

	private void genLvlAttribute(CalendarItem vo, CalendarItem voParent, Integer rule) {

		// 根据父结点生成级次属性

		vo = (CalendarItem) vo;
		// voSelected = (CalendarItem) voSelected;

		String[] lvls = new String[DimRescource.INT_MAX_FLDPRE_NUMBER];
		Integer[] rules = new Integer[DimRescource.INT_MAX_FLDPRE_NUMBER];
		int iDepth = 0;
		Object oLvl = null;
		String[] parentLvls = voParent.getVO().getLevels();
		for (int i = 0; i < DimRescource.INT_MAX_FLDPRE_NUMBER; i++) {
			iDepth = i;
			if (i >= parentLvls.length)
				break;
			oLvl = parentLvls[i];
			if (oLvl != null && oLvl.toString().trim().length() != 0) {
				lvls[i] = voParent.getVO().getLevels()[i];
				rules[i] = voParent.getVO().getCalcAttrs()[i];
			} else
				break;
		}

		lvls[iDepth] = vo.getVO().getMemberID();
		rules[iDepth] = rule;
		vo.getVO().setLevels(lvls);
		vo.getVO().setCalcAttrs(rules);

		vo.getVO().setDepth(new Integer(iDepth));
	}

	/**
	 * @return 返回 m_voDef。
	 */
	private DimensionVO getDimDef() {
		return m_voDef;
	}

	/**
	 * 获得有效时间范围内的CalendarItem[]
	 * 
	 * @param aimVec
	 * @param startPos
	 * @param endPos
	 * @return
	 */
	private CalendarItem[] getValidItemsFromVector(Vector<CalendarItem> aimVec, int removeFirst, int removeLast) {
		int length = aimVec.size() - removeFirst - removeLast;
		if (length <= 0)
			return null;
		CalendarItem[] vos = new CalendarItem[length];
		vos = (CalendarItem[]) aimVec.subList(removeFirst, aimVec.size() - removeLast).toArray(vos);
		return vos;
	}

}
