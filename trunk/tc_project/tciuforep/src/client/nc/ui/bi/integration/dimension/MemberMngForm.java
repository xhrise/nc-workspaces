/**
 * Form1.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import com.ufida.web.action.ActionForm;

/**
 * ��������������
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
     * ActionForm����ֵУ��
     * 
     * @return У�������ʾ��Ϣ����
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
