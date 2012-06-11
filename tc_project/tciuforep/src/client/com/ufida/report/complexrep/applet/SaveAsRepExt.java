/*
 * 创建日期 2006-7-13
 */
package com.ufida.report.complexrep.applet;



import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;

import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.ActionUIDes;

/**
 * @author ljhua
 */
public class SaveAsRepExt extends ComplexActionExt {


	public SaveAsRepExt(ComplexRepPlugin plugin){
		super(plugin);
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		String strName=StringResource.getStringResource(StringResConst.STR_MENU_SAVEAS);
        uiDes.setName(strName);
        uiDes.setPopup(true);
        
        ActionUIDes uiDesTool = new ActionUIDes();
        uiDesTool.setTooltip(strName);
        uiDesTool.setToolBar(true);
        uiDesTool.setImageFile("reportcore/savefile.gif");
        
        ActionUIDes uiDesMenu = new ActionUIDes();
        uiDesMenu.setName(strName);
        uiDesMenu.setPaths(new String[]{StringResource.getStringResource(StringResConst.STR_MENU_MAIN)});
        
        return new ActionUIDes[]{uiDes,uiDesTool,uiDesMenu};
	}


	public UfoCommand getCommand() {
		return new UfoCommand(){
            public void execute(Object[] params) {
                if(params != null && params.length > 0 && params[0]!=null 
                		){
                	ReportSrv srv = new ReportSrv();
                	srv.create(new ReportVO[]{(ReportVO) params[0] });
                }
            }            
        };
	}



	public Object[] getParams(UfoReport container) {
		ReportVO repVO=null;
		BaseReportModel model=getPlugIn().getFocusModel();
		if(model!=null){
			SaveAsSubRepDlg dlg=new SaveAsSubRepDlg(getPlugIn());
        	dlg.show();
        	if(dlg.getResult()==UfoDialog.ID_OK){
        		repVO=dlg.getReportVO();
        		String userPK = ((BIContextVO) getPlugIn().getReport().getContextVo()).getCurUserID();
        		repVO.setOwnerid(userPK);
        		repVO.setDefinition(model);
        		repVO.setType(model.getReportType());

        	}
		}
		if(repVO!=null)
			return new Object[]{repVO};
		
		return null;
	}

}
