package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.Container;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.TaskCache;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.measure.UnitExInfoVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.bi.base.BIException;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.FreeReportContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;

public class FreeQueryExt extends AbsActionExt implements IUfoContextKey{

	/**
	 * @i18n miufo1002461=分析报表
	 */
	public static final String STRING_ID_ANAREPORT = "miufo1002461";
	// 查询下级数据,查询本年数据,自由查询
	public static final String[] MENU_NAMES = new String[] { "miufofreequery0002", "miufofreequery0003", "miufofreequery0001", STRING_ID_ANAREPORT };

	private int m_queryType;

	private UfoReport m_report;// 报表工具

	public FreeQueryExt(int queryType, UfoReport report) {
		m_queryType = queryType;
		m_report = report;
	}

	@Override
	public UfoCommand getCommand() {
		return new FreeQueryCmd(m_report);
	}

	/**
	 * @i18n miufo00113=请选择指标
	 */
	@Override
	public Object[] getParams(UfoReport container) {
		// 1。设置新的环境信息
		try{
		FreeQueryContextVO context = createFreeQueryContextVO(container.getContextVo());
		String strReportPK = context.getIufoRepID();
		// 2。获取当前选中区域中的指标
		MeasureVO[] selMeasures = getMeasureVOByPos(container, strReportPK);
		UnitExInfoVO[] selUnitInfos = null;
		// 3。若没有选中指标，从参照中选择
		if (selMeasures == null) {
			Object[] obj = getMeasureVOByRef(container, context, null);
			if(obj != null){
				selMeasures = (MeasureVO[])obj[0];
				selUnitInfos = (UnitExInfoVO[])obj[1];
			}
		}
		if (selMeasures == null||selMeasures.length==0){
			JOptionPane.showMessageDialog(container, StringResource.getStringResource("miufo00113"));
			return null;
		}
			

		return new Object[] { context, selMeasures, selUnitInfos, m_queryType };
		}catch(BIException ex){
			JOptionPane.showMessageDialog(container, ex.getMessage());
		}
		return null;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLangInput.getString(MENU_NAMES[m_queryType]));
		uiDes.setPaths(new String[] {MultiLangInput.getString("miufofreequery0001") });// 自由查询;
		return new ActionUIDes[] { uiDes };
	}

	/**
	 * @i18n uiuforep00130=不支持对此版本数据的自由查询。
	 */
	protected FreeQueryContextVO createFreeQueryContextVO(ContextVO contextVO) throws BIException {
		if (contextVO instanceof FreeQueryContextVO) {
			return (FreeQueryContextVO) contextVO;
			// m_context.setContextId(null);
		} else if (contextVO instanceof TableInputContextVO) {
			FreeQueryContextVO freeContext = new FreeQueryContextVO();
			TableInputContextVO context = (TableInputContextVO) contextVO;
			Object tableInputTransObj = context.getAttribute(TABLE_INPUT_TRANS_OBJ);
			TableInputTransObj tableInput = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
			
			IRepDataParam repData = tableInput.getRepDataParam();

			Integer nVer = GeneralQueryUtil.getVer(context);
			if(nVer == null || (nVer != HBBBSysParaUtil.VER_SEPARATE && nVer != HBBBSysParaUtil.VER_HBBB))
				throw new BIException(MultiLang.getString("uiuforep00130"));
			String strOrgPK = context.getAttribute(ORG_PK) == null ? null : (String)context.getAttribute(ORG_PK);

			freeContext.setOrgID(strOrgPK);
			//登陆单位，用于过滤数据权限
			String unitId = context.getAttribute(LOGIN_UNIT_ID) == null ? null : (String)context.getAttribute(LOGIN_UNIT_ID);

			freeContext.setLoginUnitID(unitId);
			UnitInfoVO unitInfo = IUFOUICacheManager.getSingleton().getUnitCache().getUnitInfoByPK(unitId);
			if (unitInfo != null) {
				String unitValue = unitInfo.getPropValue(strOrgPK);
				freeContext.setUnitValue(unitValue);
			}
			freeContext.setCurUserID(repData.getOperUserPK());
			freeContext.setTaskID(repData.getTaskPK());
			freeContext.setIufoRepID(repData.getReportPK());
			freeContext.setContextId(null);// TODO
			freeContext.setLang(tableInput.getLangCode());
			freeContext.setAloneId(repData.getAloneID());
			freeContext.setVer(new Integer(repData.getVer()));
			
			freeContext.setAttribute(FreeReportContextKey.FREEQEURY_TRANS_OBJ, new FreeQueryTranceObj());
			((FreeQueryTranceObj)freeContext.getAttribute(FreeReportContextKey.FREEQEURY_TRANS_OBJ)).setDSInfo(repData.getDSInfo());
			return freeContext;
		}
		return null;

	}

	private MeasureVO[] getMeasureVOByPos(UfoReport report, String strCurrRepPK) {
		CellPosition[] pos = report.getCellsModel().getSelectModel().getSelectedCells();
		Vector<MeasureVO> vec = new Vector<MeasureVO>();
		if (pos != null) {
			DynAreaModel dynModel = DynAreaModel.getInstance(report.getCellsModel());
			for (int i = 0; i < pos.length; i++) {
				CellPosition formatPos = DynAreaCell.getFormatArea(pos[i], report.getCellsModel()).getStart();
//				CellPosition formatPos = pos[i];
				// 根据对应的格式态位置查找选中指标 TODO
//				MeasureVO mVO =  dynModel.getMeasureVOAfterDataProcess(formatPos.getRow(), formatPos.getColumn());
				MeasureVO mVO = dynModel.getMeasureModel().getMeasureVOByPos(formatPos);
				if (mVO != null) {
					mVO.setSelReportPK(strCurrRepPK);
					if(!vec.contains(mVO) && isSameKeyGroup(vec, mVO))
						vec.addElement(mVO);
				}
			}
		}
		if (vec.size() > 0)
			return vec.toArray(new MeasureVO[0]);
		return null;
	}
	private boolean isSameKeyGroup(Vector<MeasureVO> vec, MeasureVO mVO){
		if(vec.size() == 0)
			return true;
		MeasureVO m0 = vec.get(0);
		return m0.getKeyCombPK().equals(mVO.getKeyCombPK());
	}

	private Object[] getMeasureVOByRef(Container parent, FreeQueryContextVO context, MeasureVO[] selMeasures) {
		ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
		ReportVO repVO = repCache.getByPK(context.getIufoRepID());
		if (repVO == null) {// 创建虚拟的报表，以便弹出参照
			repVO = new ReportVO();
			repVO.setModel(false);
		}
		// 从报表环境中获取参数
		TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
		TaskVO taskVO = taskCache.getTaskVO(context.getTaskID());

		String strKeyGroupPk = taskVO.getKeyGroupId();
		context.setKeyGroupID(strKeyGroupPk);

		KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
		KeyGroupVO keyGroupVO = keyGroupCache.getByPK(context.getKeyGroupID());

		MultiMeasureRefDlg refDialog = new MultiMeasureRefDlg(new UfoDialog(parent), repVO, keyGroupVO, context
				.getCurUserID(), true, true, true, selMeasures);
		refDialog.setModal(true);
		refDialog.show();

		if (refDialog.getResult() == UfoDialog.ID_OK) {
			Object[] result = new Object[2];
			result[0] = refDialog.getSelMeasureVOs();
			result[1] = refDialog.getSelUnitexVOs();
			return result;
		}
		return null;
	}
	// private IFreeQueryModel createBySelectedArea(UfoReport report) {
	// if (vec.size() > 0) {
	// MeasureQueryModelDef queryDef =
	// CreateMeasQueryDesigner.createDefaultQueryDef(repVO, m_context
	// .getTaskID(), m_context.getCurUserID());
	// queryDef.setMeasures(vec.toArray(new MeasureVO[0]));
	// return queryDef;
	// }
	//		
	// return null;
	// }

}
 