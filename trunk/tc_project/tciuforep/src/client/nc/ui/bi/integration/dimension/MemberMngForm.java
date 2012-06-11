/**
 * Form1.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import com.ufida.web.action.ActionForm;

/**
 * 类作用描述文字
 * ll
 * 2006-01-17
 */
public class MemberMngForm extends ActionForm {

 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String 	dimID;
 	private boolean canBeModified;
 	private boolean isCalendar;
 	    /**
     * ActionForm属性值校验
     * 
     * @return 校验错误提示信息集合
     */
   // public Vector validate() {
   //     return null;
   // }

	public String getDimID() {
		return dimID;
	}

	public void setDimID(String dimID) {
		this.dimID = dimID;
	}

	public boolean canBeModified() {
		return canBeModified;
	}

	public void setBeModified(boolean canBeModified) {
		this.canBeModified = canBeModified;
	}
	public boolean isCalendar() {
		return isCalendar;
	}

	public void setIsCalendar(boolean bIsCalendar) {
		this.isCalendar = bIsCalendar;
	}

}
