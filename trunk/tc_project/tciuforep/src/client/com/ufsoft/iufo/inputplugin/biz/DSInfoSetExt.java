package com.ufsoft.iufo.inputplugin.biz;

import java.awt.Component;

import nc.vo.iufo.datasource.DataSourceVO;

import com.ufida.dataset.Context;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.file.DatasourceSetDlg;
import com.ufsoft.iufo.inputplugin.biz.file.InputChangeKeywordsOper;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

public class DSInfoSetExt extends AbsIufoUIBizMenuExt implements IUfoContextKey{
    private boolean m_bCanSetDSInfo = true;
    
	public DSInfoSetExt(UfoReport ufoReport) {
		super(ufoReport);
	}

    private boolean isCanSetDSInfo(){
        return m_bCanSetDSInfo; 
    }
    public void setCanSetDSInfo(boolean bCanSetDSInfo){
        this.m_bCanSetDSInfo = bCanSetDSInfo;
    }

	@Override
	protected String getImageFile() {
		return null;
	}

	@Override
	protected boolean isInAddToolBar() {
		return false;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}

	@Override
	protected UfoCommand doGetCommand(UfoReport ufoReport) {
		return new UfoCommand(){

			@Override
			public void execute(Object[] params) {
				UfoReport ufoReport = (UfoReport)params[0];
				DatasourceSetDlg dlg = new DatasourceSetDlg(ufoReport);
				TableInputTransObj tableInput = (TableInputTransObj)ufoReport.getContextVo().getAttribute(TABLE_INPUT_TRANS_OBJ);
				
				DataSourceInfo dataSrcInfo = tableInput.getRepDataParam().getDSInfo();
				if(dataSrcInfo == null){
					dataSrcInfo = new DataSourceInfo(null,null,null,null,null);
				} 
				dlg.setUnit(dataSrcInfo.getDSUnitPK());
				dlg.setUser(dataSrcInfo.getDSUserPK());
				String strNotEncodedDSPwd = nc.bs.iufo.toolkit.Encrypt.decode(dataSrcInfo.getDSPwd(), dataSrcInfo.getDSID());
				dlg.setPassword(strNotEncodedDSPwd);
				dlg.setVisible(true);
				if(dlg.getResult()  == UfoDialog.ID_OK){
					String strEncodedDSPwd = nc.bs.iufo.toolkit.Encrypt.encode(dlg.getPassword(), dataSrcInfo.getDSID());
					DataSourceVO dataSVo = new DataSourceVO();
					dataSVo.setId(dataSrcInfo.getDSID());
					dataSVo.setLoginDate(dataSrcInfo.getDSDate());
					dataSVo.setLoginUnit(dlg.getUnit()==null?null:dlg.getUnit().trim());
		            dataSVo.setLoginName(dlg.getUser()==null?null:dlg.getUser().trim());
		            dataSVo.setLoginPassw(dlg.getPassword()==null?null:dlg.getPassword().trim());
		            
		            InputBizOper inputBizOper = new InputChangeKeywordsOper(ufoReport, new Object[]{dataSVo, tableInput.getLangCode()});
					String strMsg = (String)inputBizOper.performBizTask(ITableInputMenuType.MENU_TYPE_CHECKDS);;
					if(strMsg != null && strMsg.trim().length() > 0){
						javax.swing.JOptionPane.showMessageDialog(getUfoReport(), strMsg);
						return;
					} 
					
					dataSrcInfo.setDSUnitPK(dlg.getUnit());
					dataSrcInfo.setDSUserPK(dlg.getUser());
					dataSrcInfo.setDSPwd(strEncodedDSPwd);
					//dataSrcInfo.setDSPwd(dlg.getPassword());
					tableInput.getRepDataParam().setDSInfo(dataSrcInfo);
				}
			}
			
		};
	}

	@Override
	protected String getMenuName() {
		return MultiLangInput.getString("repinput00002");//数据源信息配置
	}

	@Override
	protected String[] getPaths() {
        return new String[]{MultiLang.getString("file")};//"文件"
	}

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return isCanSetDSInfo();
    }

}
