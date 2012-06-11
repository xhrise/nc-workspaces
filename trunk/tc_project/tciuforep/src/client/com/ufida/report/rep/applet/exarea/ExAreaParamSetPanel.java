package com.ufida.report.rep.applet.exarea;

import nc.ui.pub.dsmanager.ParameterSetPanel;

import com.ufida.report.anareport.model.AreaDataModel;
import com.ufida.report.anareport.model.AreaParameter;
import com.ufsoft.report.AbstractWizardTabPanel;
import com.ufsoft.iufo.resource.StringResource;

public class ExAreaParamSetPanel extends AbstractWizardTabPanel {
    private AreaParameter parames;
    private AreaDataModel areaModel;
    private ParameterSetPanel m_panel;
	
	public ExAreaParamSetPanel(AreaDataModel areaData, AreaParameter parames){
		this.areaModel=areaData;
		this.parames=parames;
	}
	
	@Override
	public void addListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public ParameterSetPanel getContentPanel() {
		if(m_panel==null&&parames!=null){
			m_panel=new ParameterSetPanel(null,areaModel.getDSTool().getDSDef(),parames.getParams());
		}
		return m_panel;
	}

	/**
	 * @i18n miufo00140=参数值设置
	 */
	@Override
	public String getStepTitle() {
		return StringResource.getStringResource("miufo00140");
	}

	@Override
	public void initInfo() {
		// TODO Auto-generated method stub

	}


	@Override
	public void removeListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateInfo() {
		boolean isCheck=true;
		if(parames!=null){
			String subMsg =getContentPanel().validateParams();
			
			if(subMsg!=null&&subMsg.length()>0){
				isCheck=false;
			}
			
		}
		
		return isCheck;
	}

	public AreaParameter getParames() {
		return parames;
	}
	
}
 