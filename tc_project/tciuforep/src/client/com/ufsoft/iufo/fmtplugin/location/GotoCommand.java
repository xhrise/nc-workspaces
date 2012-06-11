
package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * ��Ԫ��λ�����
 * @author �����
 * 2008-5-19
 */
public class GotoCommand extends UfoCommand {

	/**
	 * ����
	 */
	private UfoReport m_Report ;


	 /**
	  * @param rep
	  * 
	  */
	 public GotoCommand(UfoReport rep) {
		 super();
		 m_Report = rep;
	 }
	 
	/**
	 * ��λ������Ԫ��
	 * �����Ԫλ���ַ����Ƿ�����Ŀ�굥Ԫ������ǰ������ʵ���еĵ�Ԫ������Ϊ������Ч 
	 * @param params[0] : ״̬����ִ����
	 * 
	 */
	public void execute(Object[] params) {
		if (params == null || params.length == 0) {
			return;
		}
		AbsLocation absLocation = (AbsLocation) params[0];
		if (absLocation != null) {
			absLocation.location();
		}    
	}	

    /**
     * @return ���� UfoReport��
     */
    public UfoReport getReport() {
        return m_Report;
    }
}
