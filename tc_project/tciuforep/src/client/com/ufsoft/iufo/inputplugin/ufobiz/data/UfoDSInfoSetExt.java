package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.event.ActionEvent;

import nc.ui.iufo.input.control.DataSourceConfig;
import nc.ui.iufo.input.control.RepDataControler;
import nc.vo.iufo.datasource.DataSourceLoginVO;
import nc.vo.iufo.datasource.DataSourceVO;

import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.file.DatasourceSetDlg;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

public class UfoDSInfoSetExt extends AbstractPluginAction{

	public void execute(ActionEvent e) {
		DataSourceConfig config=DataSourceConfig.getInstance(getMainboard());
		RepDataControler controler=RepDataControler.getInstance(getMainboard());
		
		DatasourceSetDlg dlg = new DatasourceSetDlg(getMainboard());
		DataSourceLoginVO login=null;
		DataSourceVO dataSVo=(DataSourceVO)getMainboard().getContext().getAttribute(IUfoContextKey.DATA_SOURCE);
		String strCurUnitPK=null;
		
		if (config!=null && controler!=null){
			strCurUnitPK=controler.getSelectedUnitPK();
			if (strCurUnitPK==null)
				strCurUnitPK=controler.getCurUserInfo(getMainboard()).getUnitId();
			
			login=config.getOneSourceConfig(strCurUnitPK);
		}else{
			login=new DataSourceLoginVO();
			login.setDSUnit(dataSVo.getLoginUnit());
			login.setDSUser(dataSVo.getLoginName());
			login.setDSPass(dataSVo.getLoginPassw());
		}
		if (login!=null){
			dlg.setUnit(login.getDSUnit());
			dlg.setUser(login.getDSUser());
			dlg.setPassword(login.getDSPass());
		}

		dlg.setVisible(true);
		while (true){
			if(dlg.getResult()  == UfoDialog.ID_OK){
				DataSourceVO newDSVO=(DataSourceVO)dataSVo.clone();
				newDSVO.setLoginUnit(dlg.getUnit()==null?null:dlg.getUnit().trim());
				newDSVO.setLoginName(dlg.getUser()==null?null:dlg.getUser().trim());
				newDSVO.setLoginPassw(dlg.getPassword()==null?null:dlg.getPassword().trim());
	            
	    		String strResult=(String)ActionHandler.exec("nc.ui.iufo.datasource.UpdateDSBaseAction", "checkDSLogin",
						new Object[]{newDSVO,getMainboard().getContext().getAttribute(IUfoContextKey.CURRENT_LANG)});;
				if(strResult != null && strResult.trim().length() > 0){
					javax.swing.JOptionPane.showMessageDialog(getMainboard(), strResult);
					dlg.setResult(UfoDialog.ID_CANCEL);
					dlg.show();
					continue;
				} 
				dlg.dispose();
				
				if (config!=null && controler!=null)
					config.addOneSourceConfig(strCurUnitPK, newDSVO.getLoginUnit(), newDSVO.getLoginName(),newDSVO.getLoginPassw());
				else
					getMainboard().getContext().setAttribute(IUfoContextKey.DATA_SOURCE,newDSVO);
			}
			return;
		}
	}

	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(MultiLangInput.getString("repinput00002")+"...");
		pad.setGroupPaths(new String[]{MultiLang.getString("file")});
		pad.setExtensionPoints(XPOINT.MENU);
		
		return pad;
	}
	
	public boolean isEnabled() {
		DataSourceVO dsVO=(DataSourceVO)getMainboard().getContext().getAttribute(IUfoContextKey.DATA_SOURCE);
		return dsVO!=null && dsVO.getType()!=DataSourceVO.TYPE8XDECENT;
	}
}
