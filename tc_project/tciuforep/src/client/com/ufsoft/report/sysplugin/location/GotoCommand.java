
package com.ufsoft.report.sysplugin.location;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;

/**
 * 单元定位的命令。
 * @author wupeng
 * 2004-8-26
 */
public class GotoCommand extends UfoCommand {

	/**
	 * 报表
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
	 * 定位单个单元格
	 * 如果单元位置字符串非法或者目标单元超出当前报表中实际有的单元，则认为引用无效 
	 * @param params[0] : 单元位置的字符串
	 * 
	 */
	public void execute(Object[] params) {
		if(params==null || params.length == 0){
			return ;
		}
		
		try {
            String areaPos = (String)params[0];
            AreaPosition area = AreaPosition.getInstance(areaPos);
            
            // 目标区域是一个单元格
            if(area.isCell()){ 
            	if((area.getStart() == null) 
                        || (area.getStart().getColumn() > m_Report.getTable().getCells().getColumnCount())
                        || (area.getStart().getRow() > m_Report.getTable().getCells().getRowCount())) {
            		// IUFOLogger.getLogger(this).fatal("引用无效,非法的定位字符串");
                    UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000855"), getReport(), null);//引用无效
                    return;
                }
            	
            	m_Report.getTable().getCells().getSelectionModel().clear();
                m_Report.getTable().getCells().getSelectionModel().setAnchorCell(area.getStart());
                
                return;
            } 
            
            // 目标区域是一个区域单元，检查起始单元格引用是否有效
            if((area.getStart() == null) 
            		|| (area.getStart().getColumn() > m_Report.getTable().getCells().getColumnCount())
            		|| (area.getStart().getRow() > m_Report.getTable().getCells().getRowCount())) {
            	// IUFOLogger.getLogger(this).fatal("引用无效,非法的定位字符串");
            	UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000855"), getReport(), null);//引用无效
            	return;
            }

            // 检查结束单元格引用是否有效
            if((area.getEnd() == null) 
            		|| (area.getEnd().getColumn() > m_Report.getTable().getCells().getColumnCount())
            		|| (area.getEnd().getRow() > m_Report.getTable().getCells().getRowCount())) {
            	// IUFOLogger.getLogger(this).fatal("引用无效,非法的定位字符串");
            	UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000855"), getReport(), null);//引用无效
            	return;
            }

            m_Report.getTable().getCells().getSelectionModel().clear();
            m_Report.getTable().getCells().getSelectionModel().setSelectedArea(area);
          
        } catch (Exception e) {
            AppDebug.debug(e);
            // IUFOLogger.getLogger(this).fatal("单元定位失败:" + e.toString());
            UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000856"), getReport(), e);//单元定位失败
        }
        
	}	

    /**
     * @return 返回 UfoReport。
     */
    public UfoReport getReport() {
        return m_Report;
    }
}
