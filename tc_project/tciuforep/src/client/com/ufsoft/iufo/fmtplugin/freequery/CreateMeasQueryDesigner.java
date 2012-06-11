package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.Container;

import javax.swing.JPanel;

import nc.itf.iufo.freequery.IFreeQueryCondition;
import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.TaskCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.pub.beans.UIPanel;
import nc.util.iufo.pub.IDMaker;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.measure.MeasureQueryModelDef;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

public class CreateMeasQueryDesigner implements IFreeQueryDesigner,IUfoContextKey {

	private MeasureQueryModelDef m_modelDef = null;

	public CreateMeasQueryDesigner() {
	}

	/**
	 * 创建测试数据
	 * 
	 * @return
	 */
	public static MeasureQueryModelDef createDefaultQueryDef(ReportVO repVO, String strTaskPK, String loginUnitValue,
			String orgPK) {
		MeasureQueryModelDef def = new MeasureQueryModelDef(IDMaker.makeID(20));
		// 从报表环境中获取参数
		TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
		TaskVO taskVO = taskCache.getTaskVO(strTaskPK);
		KeyGroupVO keyGroupVO = null;

		if (taskVO != null) {
			String strKeyGroupPk = taskVO.getKeyGroupId();

			KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
			keyGroupVO = keyGroupCache.getByPK(strKeyGroupPk);
		}
		def.setMeasureDef(loginUnitValue, orgPK, repVO, keyGroupVO, null);

		return def;
	}

	public String getMenuName() {
		return StringResource.getStringResource("miufopublic141");// 指标查询
	}

	public String getModelName() {
		return MeasureQueryModelDef.class.getName();
	}

	public IFreeQueryModel getQueryDefResult() {
		return m_modelDef;
	}

	public String getImageFile() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @i18n uiuforep00016=此插件需要FreeQueryContextVO上下文环境
	 */
	public boolean designQuery(Container parent, IFreeQueryModel queryDef, ContextVO context) {
		if (!(context instanceof FreeQueryContextVO))
			throw new IllegalArgumentException(MultiLang.getString("uiuforep00016"));
		// return false;
		FreeQueryContextVO cVO = (FreeQueryContextVO) context;

		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		ReportVO iufoRepVO = null;
		m_modelDef = (MeasureQueryModelDef) queryDef;
		if (m_modelDef != null) {
			MeasureVO[] measVOs = m_modelDef.getMeasures();
			iufoRepVO = (measVOs == null || measVOs.length == 0) ? null : repCache.getByPK(measVOs[0].getSelReportPK());
		}
		if (iufoRepVO == null) {
			Object repIdObj = cVO.getAttribute(REPORT_PK);
			String strRepPK = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null; 
			
			iufoRepVO = repCache.getByPK(strRepPK);
			if (iufoRepVO == null) {
				iufoRepVO = createVisualRepVO();
			}
		}
		if (m_modelDef == null) {
			m_modelDef = createDefaultQueryDef(iufoRepVO, cVO.getTaskID(), cVO.getUnitValue(), cVO.getOrgID());// 根据报表环境创建
			m_modelDef.setVer(cVO.getVer() == null ? 0 : cVO.getVer());
			m_modelDef.setTaskID(cVO.getTaskID());
			m_modelDef.setQueryType(MeasureQueryModelDef.QUERY_FREE);
			SysPropVO sysProp = SysPropMng.getSysProp(ISysProp.HB_REPDATA_RELATING_TASK);
			if (sysProp != null && sysProp.getValue() != null && sysProp.getValue().equalsIgnoreCase("true"))
				m_modelDef.setHBByTask(true);

		}
		// UfoContextVO cVO = (UfoContextVO) m_modelDef.getContextVO();
		MultiMeasureRefDlg refDialog = new MultiMeasureRefDlg(new UfoDialog(parent), iufoRepVO, m_modelDef
				.getKeyGroupVO(), cVO.getCurUserID(), true, true, true, m_modelDef.getMeasures());
		refDialog.setSelUnitexVOs(m_modelDef.getExInfoVOs());
		refDialog.setModal(true);
		refDialog.show();

		if (refDialog.getResult() == UfoDialog.ID_OK) {
			MeasureVO[] selVOs = refDialog.getSelMeasureVOs();
			if (selVOs != null && selVOs.length > 0) {// 没有选择任何指标，视同取消
				m_modelDef.setMeasures(selVOs);
				m_modelDef.setExInfoVOs(refDialog.getSelUnitexVOs());
				return true;
			}
		}
		return false;
	}

	public ReportVO createVisualRepVO() {
		ReportVO rep = new ReportVO();
		rep.setModel(false);
		return rep;
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
