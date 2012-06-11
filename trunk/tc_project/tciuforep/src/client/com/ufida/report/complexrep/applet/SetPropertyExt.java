/*
 * 创建日期 2006-7-13
 */
package com.ufida.report.complexrep.applet;

import com.ufida.report.rep.model.BaseReportModel;
import com.ufida.report.rep.model.BiModelProperty;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;

import com.ufsoft.report.plugin.ActionUIDes;

/**
 * 子表属性设置
 * @author ljhua
 */
public class SetPropertyExt extends ComplexActionExt {


	 
	 public SetPropertyExt(ComplexRepPlugin plugin) {
        super( plugin);
	    }
	public ActionUIDes[] getUIDesArr() {

        ActionUIDes uiDes = new ActionUIDes();
        String strName=StringResource.getStringResource(StringResConst.MENU_PROPERTY);
        uiDes.setName(strName);
        uiDes.setPopup(true);
        
        ActionUIDes uiDesMenu = new ActionUIDes();
        uiDesMenu.setName(strName);
        uiDesMenu.setPaths(new String[]{StringResource.getStringResource(StringResConst.STR_MENU_MAIN)});
        
        return new ActionUIDes[]{uiDes,uiDesMenu};
	}

	public UfoCommand getCommand() {
		return new UfoCommand(){
            public void execute(Object[] params) {
                if(params != null && params.length > 0 && params[0]!=null && 
                		params[0] instanceof BiModelProperty
                		){
                	BiModelProperty prop=(BiModelProperty)params[0];
                	BaseReportModel model=getPlugIn().getFocusModel();
                	if(model!=null){
                		model.setProperty(prop);
                		getPlugIn().resetFocusSubRepProp();
//                		getPlugIn().getFocusSubReport().getReportNavPanel().revalidate();
//                		getPlugIn().getFocusSubReport().getReportNavPanel().repaint();
                	}
                	
                	
                }
            }            
        };
	}

	/* （非 Javadoc）
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {

		Object objParam=null;
		BaseReportModel model=getPlugIn().getFocusModel();
		if(model!=null){
			SetPropertyDlg dlg=new SetPropertyDlg(container,model.getProperty(),model.getReportType().intValue());
        	dlg.show();
        	if(dlg.getResult()==UfoDialog.ID_OK){
        		objParam=dlg.getProperty();
        	}
		}
		if(objParam!=null)
			return new Object[]{objParam};
		
		return null;
	}


}
