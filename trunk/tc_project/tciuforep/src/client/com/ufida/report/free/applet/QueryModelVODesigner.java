package com.ufida.report.free.applet;

import java.awt.Container;

import javax.swing.JPanel;

import nc.itf.iufo.freequery.IFreeQueryCondition;
import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.bi.query.measquery.QueryModelVODef;

import com.ufida.report.adhoc.applet.SelectQueryModelDlg;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.iufo.resource.StringResource;

public class QueryModelVODesigner implements IFreeQueryDesigner {

	private QueryModelVODef m_queryDef = null;

	/**
	 * @i18n miufo00351=此插件需要BIContext的报表上下文环境
	 */
	public boolean designQuery(Container parent, IFreeQueryModel queryDef, ContextVO context) {
		if (!(context instanceof FreeQueryContextVO))
			throw new IllegalArgumentException(StringResource.getStringResource("miufo00351"));
		String userID = ((FreeQueryContextVO) context).getCurUserID();
		m_queryDef = (QueryModelVODef) queryDef;
		SelectQueryModelDlg dlg = new SelectQueryModelDlg(parent, userID);
		dlg.setLocationRelativeTo(parent);
		dlg.setResizable(false);
		dlg.setModal(true);
		dlg.setAlwaysOnTop(true);
		dlg.show();
		if (dlg.getResult() == UfoDialog.ID_OK) {
			QueryModelVO queryModel = dlg.getQueryModel();
			if(m_queryDef == null)
				m_queryDef = new QueryModelVODef(queryModel);
			else
				m_queryDef.setQueryVO(queryModel);
			return true;
		}
		return false;
	}

	/**
	 * @i18n miufo00352=BI查询
	 */
	public String getMenuName() {
		return StringResource.getStringResource("miufo00352");
	}

	public String getModelName() {
		return QueryModelVODef.class.getName();
	}

	public IFreeQueryModel getQueryDefResult() {
		return m_queryDef;
	}

	public JPanel getConditionPanel(IFreeQueryModel queryDef) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getImageFile() {
		// TODO Auto-generated method stub
		return null;
	}

	public IFreeQueryCondition getUserDifineCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
 