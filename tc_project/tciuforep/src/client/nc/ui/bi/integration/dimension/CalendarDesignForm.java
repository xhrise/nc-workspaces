/**
 * Form1.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import nc.vo.bi.integration.dimension.DimMemberVO;

import com.ufida.web.action.ActionForm;

/**
 * 类作用描述文字
 * ll
 * 2006-01-17
 */
public class CalendarDesignForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
 	private String rules;
     //
 	private String startDate;
 	private String endDate;
 	
 	private DimMemberVO[] memberVOs = null;
 	private String isMoveMember = null;
 	
 	private String[][] calendarGenRuleItems;
 	private String calendarGenRule; 	
 	private String calendarGenSpliter;
 	
 	private boolean isReplace;

 	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getIsMoveMember() {
		return isMoveMember;
	}

	public void setIsMoveMember(String isMoveMember) {
		this.isMoveMember = isMoveMember;
	}

	public DimMemberVO[] getMemberVOs() {
		return memberVOs;
	}

	public void setMemberVOs(DimMemberVO[] memberVOs) {
		this.memberVOs = memberVOs;
	}

	public String getCalendarGenRule() {
		return calendarGenRule;
	}

	public void setCalendarGenRule(String calendarGenRule) {
		this.calendarGenRule = calendarGenRule;
	}

	public String[][] getCalendarGenRuleItems() {
		return calendarGenRuleItems;
	}

	public void setCalendarGenRuleItems(String[][] calendarGenRuleItems) {
		this.calendarGenRuleItems = calendarGenRuleItems;
	}

	public String getCalendarGenSpliter() {
		return calendarGenSpliter;
	}

	public void setCalendarGenSpliter(String calendarGenSpliter) {
		this.calendarGenSpliter = calendarGenSpliter;
	}
	public boolean getIsReplace() {
		return isReplace;
	}

	public void setIsReplace(boolean bReplace) {
		this.isReplace = bReplace;
	}

    /**
     * ActionForm属性值校验
     * 
     * @return 校验错误提示信息集合
     */
   // public Vector validate() {
   //     return null;
   // }

}
