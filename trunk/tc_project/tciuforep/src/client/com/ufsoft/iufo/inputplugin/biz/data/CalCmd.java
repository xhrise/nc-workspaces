/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import com.sun.corba.se.pept.protocol.MessageMediator;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.component.ProcessController;
import com.ufsoft.report.exception.MessageException;
import com.ufsoft.report.util.UfoPublic;

public class CalCmd extends AbsIufoBizCmd{

    protected CalCmd(UfoReport ufoReport) {
        super(ufoReport);
    }

    /**
     * @i18n miufo1002854=计算成功
     * @i18n miufo1000119=计算时发生错误，可能没有完全计算完成
     * @i18n miufo1000033=计算
     */
    protected void executeIUFOBizCmd(final UfoReport ufoReport, Object[] params) {
    	ufoReport.stopCellEditing();
    	ProcessController controller = new ProcessController(true);
		controller.setRunnable(new Runnable() {
			public void run() {
				try{
					IInputBizOper inputMenuOper = new InputBizOper(ufoReport);
			        Object objReturn = inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_CALCULATE);
			        if(objReturn != null && objReturn instanceof String && 
			        		((String)objReturn).equals("true")){
			        	String messageStr = StringResource.getStringResource("miufo1002854");
			        	UfoPublic.sendMessage(messageStr, ufoReport); 
			        	
			        } else if(objReturn != null && objReturn instanceof String && 
			        		((String)objReturn).equals("false")){
			        	String errorStr = StringResource.getStringResource("miufo1000119");
			        	UfoPublic.sendErrorMessage(errorStr, ufoReport, null);
			        	
			        }	
			        
				} catch(Throwable e){
					UfoPublic.sendMessage(e, ufoReport);
					
				}
		        
			}
		});
		controller.setString(StringResource.getStringResource("miufo1000033"));
		ufoReport.getStatusBar().setProcessDisplay(controller);        
    }

}
