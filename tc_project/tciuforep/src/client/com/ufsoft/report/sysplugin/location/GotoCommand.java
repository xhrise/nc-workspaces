
package com.ufsoft.report.sysplugin.location;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;

/**
 * ��Ԫ��λ�����
 * @author wupeng
 * 2004-8-26
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
	 * @param params[0] : ��Ԫλ�õ��ַ���
	 * 
	 */
	public void execute(Object[] params) {
		if(params==null || params.length == 0){
			return ;
		}
		
		try {
            String areaPos = (String)params[0];
            AreaPosition area = AreaPosition.getInstance(areaPos);
            
            // Ŀ��������һ����Ԫ��
            if(area.isCell()){ 
            	if((area.getStart() == null) 
                        || (area.getStart().getColumn() > m_Report.getTable().getCells().getColumnCount())
                        || (area.getStart().getRow() > m_Report.getTable().getCells().getRowCount())) {
            		// IUFOLogger.getLogger(this).fatal("������Ч,�Ƿ��Ķ�λ�ַ���");
                    UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000855"), getReport(), null);//������Ч
                    return;
                }
            	
            	m_Report.getTable().getCells().getSelectionModel().clear();
                m_Report.getTable().getCells().getSelectionModel().setAnchorCell(area.getStart());
                
                return;
            } 
            
            // Ŀ��������һ������Ԫ�������ʼ��Ԫ�������Ƿ���Ч
            if((area.getStart() == null) 
            		|| (area.getStart().getColumn() > m_Report.getTable().getCells().getColumnCount())
            		|| (area.getStart().getRow() > m_Report.getTable().getCells().getRowCount())) {
            	// IUFOLogger.getLogger(this).fatal("������Ч,�Ƿ��Ķ�λ�ַ���");
            	UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000855"), getReport(), null);//������Ч
            	return;
            }

            // ��������Ԫ�������Ƿ���Ч
            if((area.getEnd() == null) 
            		|| (area.getEnd().getColumn() > m_Report.getTable().getCells().getColumnCount())
            		|| (area.getEnd().getRow() > m_Report.getTable().getCells().getRowCount())) {
            	// IUFOLogger.getLogger(this).fatal("������Ч,�Ƿ��Ķ�λ�ַ���");
            	UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000855"), getReport(), null);//������Ч
            	return;
            }

            m_Report.getTable().getCells().getSelectionModel().clear();
            m_Report.getTable().getCells().getSelectionModel().setSelectedArea(area);
          
        } catch (Exception e) {
            AppDebug.debug(e);
            // IUFOLogger.getLogger(this).fatal("��Ԫ��λʧ��:" + e.toString());
            UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000856"), getReport(), e);//��Ԫ��λʧ��
        }
        
	}	

    /**
     * @return ���� UfoReport��
     */
    public UfoReport getReport() {
        return m_Report;
    }
}
