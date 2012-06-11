package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;

import com.ufsoft.iufo.fmtplugin.freequery.MultiSelMeasureRefRightPanelList;
import com.ufsoft.iufo.fmtplugin.freequery.MultiSelMeasureRefRightPanelSample;
import com.ufsoft.report.util.UfoPublic;

import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;
import com.ufsoft.iufo.resource.StringResource;
/**
 * 指标的多选参照
 * @author wangyga
 *
 */
public class MultiSelMeasureRefDlg extends MeasureRefDlg{

	private static final long serialVersionUID = 1L;

	public MultiSelMeasureRefDlg(JDialog parent, ReportVO currentRepVO,
			KeyGroupVO currentKeyGroupVO, String strUserPK, boolean isContains,
			boolean repMgr, boolean includeRefMeas) {
		super(parent, currentRepVO, currentKeyGroupVO, strUserPK, isContains, repMgr,
				includeRefMeas);
		
	}


	/**
	 * This method initializes rightPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	protected MeasureRefRightPanel getRightPanelList() {
		if (rightMeasureListPanel == null) {
			rightMeasureListPanel = new MultiSelMeasureRefRightPanelList(this, isContainsCurrentReport,
					m_oCurrentKeyGroupVO, excludeMeasuresList, isIncludeRefMeasures());
		}
		return rightMeasureListPanel;
	}

	protected MeasureRefRightPanel getRightPanelSample() {
		if (rightMeasureSamplePanel == null) {
			rightMeasureSamplePanel = new MultiSelMeasureRefRightPanelSample(this, isContainsCurrentReport,
					m_oCurrentKeyGroupVO, excludeMeasuresList, isIncludeRefMeasures());
		}
		return rightMeasureSamplePanel;
	}
	
	/**
	 * 返回指标,按如下格式 报表名称->指标编码 创建日期：(2003-9-19 10:53:07)
	 * 
	 * @return java.lang.String
	 */
	public String getStrRefMeasure() {
		MeasureVO[] selMeasureVos = getRightPanel().getSelMeasureVOs();
		if(selMeasureVos == null || selMeasureVos.length == 0)
			return "";
		String strRefMeasures = "";
		String strRepCode = getCurRepCode();
		for (int i = 0; i < selMeasureVos.length; i++) {
			String strMesure = "" + strRepCode + "->" + selMeasureVos[i].getName() + "";
			if(i != selMeasureVos.length -1)
				strRefMeasures+=strMesure+",";
			else
				strRefMeasures+=strMesure+"";
		}
		
		return "'"+strRefMeasures+"'";		
	}

	private String getCurRepCode(){
		ReportVO repVo = getRightPanel().getCurReportVO();
		if(repVo != null)
			return repVo.getCode();
		return null;
	}


	/**
	 * @i18n miufo00113=请选择指标
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == refButton) {// 确定
			MeasureVO[] selMeasureVos = getRightPanel().getSelMeasureVOs();
			if(selMeasureVos == null || selMeasureVos.length == 0){
				UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo00113"), this);
				return;
			}
				
			setResult(ID_OK);
			close();
		} else if (event.getSource() == closeButton) {
			setResult(ID_CANCEL);
			close();
		}
	}
	
	
}
 