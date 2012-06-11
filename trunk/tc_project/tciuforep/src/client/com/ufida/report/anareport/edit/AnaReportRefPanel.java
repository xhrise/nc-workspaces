package com.ufida.report.anareport.edit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.re.IRefComp;

public class AnaReportRefPanel extends JPanel implements IRefComp {
	private AnaReportRefDlg m_dlg = null;
	private boolean m_isOK = false;
	private Object m_obj = null;
	public AnaReportRefPanel(Container parent, String strUserPK, String strUnitPK){
		this.setLayout(new BorderLayout());
		m_dlg = new AnaReportRefDlg(parent, strUserPK, strUnitPK){
			protected void closeDlgWithResult(boolean isOK) {
				m_isOK = isOK;	
				super.closeDlgWithResult(isOK);
				doClose();
			}
		};
		this.add(m_dlg.getContentPane(),BorderLayout.CENTER);
		
	}
	public Object getSelectValue() {
		if(m_isOK)
			return m_dlg.getReportVO();
		else
			return m_obj;
	}

	public String getTitleValue() {
		return StringResource
				.getStringResource(ReportDrillExt.RESID_CHOOSE_DRILL_REPORT);
	}

	public Object getValidateValue(String text) {
		return m_dlg.getValidateValue(text);
	}

	public void setDefaultValue(Object obj) {
		m_obj = obj;
		m_dlg.setDefaultValue(obj);
	}
	public void doClose(){
		
	}

}
