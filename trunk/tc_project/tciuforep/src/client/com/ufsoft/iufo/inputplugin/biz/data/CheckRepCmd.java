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
 * 审核报表的UfoCommand
 * 
 * @author liulp
 *
 */
public class CheckRepCmd extends AbsIufoBizCmd{

    protected CheckRepCmd(UfoReport ufoReport) {
        super(ufoReport);
    }

    protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
        IInputBizOper inputMenuOper = new InputBizOper(ufoReport);
        Object objReturn = inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_CHECK_IN);
        if(objReturn == null){
        	return;
        }
        
        if(Boolean.parseBoolean((String)objReturn)){
        	UfoPublic.sendMessage(StringResource.getStringResource("miufo1000125"), ufoReport);
        } else{
        	UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1000126"), ufoReport);
        }        
    }

}
