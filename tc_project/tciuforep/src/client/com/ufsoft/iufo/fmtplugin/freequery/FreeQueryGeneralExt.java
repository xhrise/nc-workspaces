package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.Component;
import java.util.Vector;

import javax.swing.KeyStroke;

import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.pub.iufo.cache.ReportCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.vo.iufo.measure.MeasureQueryModelDef;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.formatcore.FreeReportContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.iufo.sysprop.vo.SysPropVO;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.table.CellPosition;

/**
 * 自由查询定义插件扩展基类
 * 
 * @author ll
 */
public class FreeQueryGeneralExt extends AbsActionExt implements IUfoContextKey{// implements

	private FreeQueryPlugin m_plugin = null;

	private UfoReport m_report;// 报表工具

	private IFreeQueryDesigner m_designer;// 查询设计向导

	private FreeQueryContextVO m_context;// 自由查询的上下文环境

	/**
	 * 构造函数
	 * 
	 * @param rep
	 *            报表
	 * @param plugin
	 *            自由查询插件
	 * @param designer
	 *            查询设计向导
	 */
	public FreeQueryGeneralExt(UfoReport rep, FreeQueryPlugin plugin, IFreeQueryDesigner designer) {
		m_report = rep;
		m_plugin = plugin;
		m_designer = designer;
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		return m_designer.getMenuName();
	}

	/**
	 * 获取参数
	 */
	public Object[] getParams(UfoReport container) {

		// 设置当前环境
		createFreeQueryContextVO(getReport().getContextVo());
		// IFreeQueryModel queryDef = (IFreeQueryModel)
		// getReport().getCellsModel().getExtProp(m_designer.getModelName());

		// 从界面获得选中的指标
		IFreeQueryModel queryDef = createBySelectedArea(container);

		if (queryDef == null) { // 调用查询的设计向导
			if (m_designer.designQuery(container, queryDef, m_context)) {
				queryDef = (IFreeQueryModel) m_designer.getQueryDefResult();
			}
		}
		if (queryDef == null)
			return null;

		// queryDef.setFilterParams(getKeyValues(getReport().getContextVo()));

		getReport().getCellsModel().putExtProp(m_designer.getModelName(), queryDef);

		// 进入自由查询的报表界面
		if (container.getContextVo() instanceof FreeQueryContextVO) {// 已经是从自由查询进入的界面
			// return null;
		}
		return showNextReport(container);
	}

	protected Object[] showNextReport(UfoReport container) {
		UfoReport newReport = getFreeQueryReport(container);
		QueryNavigation.showReport(newReport, getNextReportTitle(), true,false);
		return null;
	}

	protected String getNextReportTitle() {
		return StringResource.getStringResource("uibifreequery01");// 自由查询
	}

	protected void createFreeQueryContextVO(ContextVO contextVO) {
		if (contextVO instanceof FreeQueryContextVO) {
			m_context = (FreeQueryContextVO) contextVO;
			// m_context.setContextId(null);
		} else if (contextVO instanceof TableInputContextVO) {
			if (m_context == null)
				m_context = new FreeQueryContextVO(contextVO);
			TableInputContextVO context = (TableInputContextVO) contextVO;
			Object tableInputTransObj = context.getAttribute(TABLE_INPUT_TRANS_OBJ);
			TableInputTransObj tableInput = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
			
			String taskID = tableInput.getRepDataParam().getTaskPK();
			String userID = tableInput.getRepDataParam().getOperUserPK();
			// String repID =
			// context.getInputTransObj().getRepDataParam().getReportPK();
			String lang = tableInput.getLangCode();
			m_context.setCurUserID(userID);
			m_context.setTaskID(taskID);
			m_context.setContextId(null);// TODO
			m_context.setLang(lang);
		}

	}

	protected UfoReport getFreeQueryReport(UfoReport report) {
		IFreeQueryModel queryDef = (IFreeQueryModel) getReport().getCellsModel().getExtProp(m_designer.getModelName());

		return FreeQueryReport.getFreeInstance(setQueryModelToContext(queryDef), report, m_designer);
	}

	/**
	 * 获得报表的环境信息
	 * 
	 * @return
	 */
	protected FreeQueryContextVO setQueryModelToContext(IFreeQueryModel queryDef) {
		FreeQueryTranceObj obj = (FreeQueryTranceObj)m_context.getAttribute(FreeReportContextKey.FREEQEURY_TRANS_OBJ);
		if (obj == null)
			obj = new FreeQueryTranceObj();
		obj.setQueryDefs(new IFreeQueryModel[] { queryDef });
		obj.setQueryDesigners(new IFreeQueryDesigner[] { m_designer });

		m_context.setAttribute(FreeReportContextKey.FREEQEURY_TRANS_OBJ,obj);

		return m_context;
	}

	/**
	 * 命令处理参数过程
	 * 
	 * @param params
	 */
	public void executeCmd(Object[] params) {
	}

	// // TODO 查询执行器需要根据接口获取
	// BIMultiDataSet dSet = (new MeasQueryExcutor()).getQueryResult(queryDef,
	// getReport().getContextVo());
	// if (dSet == null)
	// return;

	// DataSetPreviewDlg pDlg = new DataSetPreviewDlg(getReport(), m_designer,
	// getReport().getContextVo());
	// pDlg.setDatas(dSet, queryDef);
	// pDlg.showModal();
	// if (pDlg.getResult() == UfoDialog.ID_OK) {
	// // JOptionPane.showMessageDialog(getReport(), "进入报表设计界面");
	// // 设置查询导航面板
	// initReportDesigner(queryDef, dSet, true);
	// }

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
	 */
	public String getImageFile() {
		return m_designer.getImageFile();
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getHint()
	 */
	public String getHint() {

		return getName();
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getAccelerator()
	 */
	public KeyStroke getAccelerator() {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {

		return new FreeQueryCmd(getReport()) {
			public void execute(Object[] params) {
				executeCmd(params);
			}
		};
	}

	/**
	 * @return 返回UfoReport。
	 */
	public UfoReport getReport() {
		return m_report;
	}

	/*
	 * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
	 */
	public String[] getPath() {
		return new String[] { StringResource.getStringResource("uibifreequery01") };// 自由查询;
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
	 */
	public boolean isEnabled(Component focusComp) {
		// return StateUtil.isCPane1THeader(m_report, focusComp);
		return true;
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	/**
	 * @i18n format=格式
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(m_designer.getMenuName());
		uiDes.setPaths(getPath());
		return new ActionUIDes[] { uiDes };
	}

	protected FreeQueryPlugin getPlugIn() {
		return m_plugin;
	}

	private IFreeQueryModel createBySelectedArea(UfoReport report) {
		CellPosition[] pos = report.getCellsModel().getSelectModel().getSelectedCells();
		Vector<MeasureVO> vec = new Vector<MeasureVO>();
		if (pos != null) {
			MeasureModel measModel = MeasureModel.getInstance(report.getCellsModel());
			for (int i = 0; i < pos.length; i++) {
				MeasureVO mVO = measModel.getMainMeasureVOByPos(pos[i]);// TODO
				// 尚未处理动态区指标
				if (mVO != null) {
					// mVO.setSelReportPK(strCurrRepPK);
					vec.addElement(mVO);
				}
			}
		}
		if (vec.size() > 0) {
			ReportCache repCache = IUFOUICacheManager.getSingleton().getReportCache();
			Object repIdObj = report.getContextVo().getAttribute(REPORT_PK);
			String strReportPK = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null;
			
			ReportVO repVO = repCache.getByPK(strReportPK);

			MeasureQueryModelDef queryDef = CreateMeasQueryDesigner.createDefaultQueryDef(repVO, m_context.getTaskID(),
					m_context.getUnitValue(), m_context.getOrgID());
			queryDef.setMeasures(vec.toArray(new MeasureVO[0]));
			queryDef.setVer(m_context.getVer());
			queryDef.setTaskID(m_context.getTaskID());
			queryDef.setQueryType(MeasureQueryModelDef.QUERY_FREE);
			SysPropVO sysProp=SysPropMng.getSysProp(ISysProp.HB_REPDATA_RELATING_TASK);
			if (sysProp!=null && sysProp.getValue()!=null && sysProp.getValue().equalsIgnoreCase("true"))
				queryDef.setHBByTask(true);

			return queryDef;
		}

		return null;
	}
}
