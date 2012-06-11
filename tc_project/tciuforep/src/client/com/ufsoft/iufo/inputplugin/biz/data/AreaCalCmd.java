/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;
/**
 * 区域计算命令
 * 
 * @author liulp
 *
 */
public class AreaCalCmd extends AbsIufoBizCmd{

    protected AreaCalCmd(UfoReport ufoReport) {
        super(ufoReport);
    }

    protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
    	ufoReport.stopCellEditing();
        IInputBizOper inputMenuOper = new InputBizOper(ufoReport);
        Object rtn = inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_CALCULATE_AREA);  
        if(rtn != null && rtn.toString().equals("true")){
        	String messageStr = StringResource.getStringResource("miufo1002854");
        	UfoPublic.sendMessage(messageStr, ufoReport);
        }            
    }

}
