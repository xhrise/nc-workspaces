package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.Container;

import javax.swing.JPanel;

import nc.itf.iufo.freequery.IFreeQueryCondition;
import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.ui.pub.beans.UIPanel;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iuforeport.freequery.SelMeasQueryDef;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

/**
 * 选择旧版本已经定义的指标查询
 * 
 * @author ll
 * 
 */
public class SelMeasureQueryDesigner implements IFreeQueryDesigner, IUfoContextKey {

	private SelMeasQueryDef m_queryDef = null;

	public SelMeasureQueryDesigner() {
	}

	/**
	 * @i18n uiuforep00147=此插件需要IUFO的报表上下文环境
	 */
	public boolean designQuery(Container parent, IFreeQueryModel queryDef, ContextVO context) {
		// return false;
		if (m_queryDef == null) {
			m_queryDef = new SelMeasQueryDef();
			if (!(context instanceof UfoContextVO))
				throw new IllegalArgumentException(MultiLang.getString("uiuforep00147"));
			// m_queryDef.setContextVO(context);
		}
		UfoContextVO cVO = (UfoContextVO) context;
		String unitID = (String)cVO.getAttribute(CUR_UNIT_ID);
		SelMeasQueryDlg refDialog = new SelMeasQueryDlg(parent, getMenuName(), unitID, null);
		refDialog.setSelectedVO((SelMeasQueryDef) queryDef);
		refDialog.setModal(true);
		refDialog.show();

		if (refDialog.getResult() == UfoDialog.ID_OK) {

			m_queryDef.setMeasQueryVO(refDialog.getSelectedQueryVO());
			m_queryDef.setMeasQueryCondVO(refDialog.getSelectedQueryCondVO());
			return true;
		}
		return false;
	}

	public String getImageFile() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @i18n uiuforep00148=选择指标查询
	 */
	public String getMenuName() {
		return MultiLang.getString("uiuforep00148");
	}

	public String getModelName() {
		return SelMeasQueryDef.class.getName();
	}

	public IFreeQueryModel getQueryDefResult() {
		return m_queryDef;
	}

	public JPanel getConditionPanel(IFreeQueryModel queryDef) {
		// TODO Auto-generated method stub
		return new UIPanel();
	}

	public IFreeQueryCondition getUserDifineCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
 