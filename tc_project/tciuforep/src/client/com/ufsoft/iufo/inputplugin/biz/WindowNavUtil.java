package com.ufsoft.iufo.inputplugin.biz;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.pub.iufo.cache.base.UserCache;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.datasource.DataSourceBO_Client;
import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.IPostRepDataEditorActive;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.iufo.input.table.IufoRefData;
import nc.ui.iufo.input.table.TableInputParam;
import nc.ui.iufo.input.view.FormTraceResultViewer;
import nc.ui.iufo.input.view.KeyCondPanel;
import nc.ui.iufo.release.ReleaseMngBO_Client;
import nc.ui.iufo.repdataright.RepDataRightUtil;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.release.ReleaseTaskVO;
import nc.vo.iufo.repdataright.RepDataRightVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.applet.IDataSourceParam;
import nc.vo.iuforeport.rep.ReportVO;
import netscape.javascript.JSObject;

import com.ufida.dataset.Context;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.applet.ComContextKey;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.util.ResourceManager;
import com.ufida.zior.view.Mainboard;
import com.ufsoft.iufo.check.ui.CheckResultBO_Client;
import com.ufsoft.iufo.fmtplugin.ContextFactory;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.pluginregister.TableInputPluginRegister;
import com.ufsoft.iufo.inputplugin.MeasTraceInfo;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseCordPanel;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepData;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepPanel;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iufo.inputplugin.biz.file.UnitTreeModel;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizUtil;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceExt;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceNavPanel;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaTraceBiz;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.TaskSelectDlg;
import com.ufsoft.iufo.inputplugin.biz.formulatracenew.UfoFormulaTraceBizHelper;
import com.ufsoft.iufo.inputplugin.inputcore.InputContextVO;
import com.ufsoft.iufo.inputplugin.inputcore.RefData;
import com.ufsoft.iufo.inputplugin.inputcore.WinNavPostRepDataActive;
import com.ufsoft.iufo.inputplugin.querynavigation.FormulaTraceNavigation;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.userrole.UserRoleUtil;
import com.ufsoft.iuforeport.repdatainput.TableInputActionHandler;
import com.ufsoft.iuforeport.tableinput.TableInputOperUtil;
import com.ufsoft.iuforeport.tableinput.TableSearchCondVO;
import com.ufsoft.iuforeport.tableinput.TableSearchUtil;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedData;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputAppletParam;
import com.ufsoft.iuforeport.tableinput.applet.RepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.ReportViewType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputAuth;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;

public class WindowNavUtil implements IUfoContextKey {

	private WindowNavUtil() {
	}

	/**
	 * 获得窗口变量UfoReport:并且焦点到该窗口 NOTICE: 适用于报表数据的切换功能,报表数据中
	 * 确定UfoReport唯一性的是三个参数：strRepCode,strAlondID,strTaskPK
	 * 
	 * @param contextVO
	 * @param cellsModel
	 * @param oper
	 * @param params
	 * @param bOpenNew
	 * @param strNewTitle
	 * @return
	 */
	public static UfoReport getReportInstance(TableInputContextVO contextVO,
			CellsModel cellsModel, int oper, Hashtable<String, Object> params,
			int repViewType, String strNewTitle) {
		UfoReport ufoReport = null;
		Object tableInputTransObj = contextVO
				.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj tableInput = tableInputTransObj != null
				&& (tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj) tableInputTransObj
				: null;

		if (contextVO != null && tableInput.getRepDataParam() != null) {
			// 判断相同条件的ufoReport是否已存在
			String strRepCode = contextVO.getReportcode();
			String strAlondID = tableInput.getRepDataParam().getAloneID();
			String strTaskPK = tableInput.getRepDataParam().getTaskPK();
			ufoReport = QueryNavigation.getSingleton().focusReport(strRepCode,
					strAlondID, strTaskPK);
		}
		if (ufoReport == null) {
			// ufoReport = new UfoReport(contextVO,cellsModel,
			// UfoReport.OPERATION_INPUT, params);

			ufoReport = new UfoReport(UfoReport.OPERATION_INPUT, contextVO,
					cellsModel, new TableInputPluginRegister(params));
			// 打开新窗口
			if (strNewTitle == null) {
				strNewTitle = "";
			}
			QueryNavigation.showReport(ufoReport, strNewTitle, repViewType);
		}

		// 负责表格空间各个插件状态的功能，应该由 公式导航打开报表接口new UfoReport 或 报表控件 提供统一的接口方法
		// 暂时刷新表格控件展现
		refreshCellsModel(ufoReport, cellsModel, contextVO);
		return ufoReport;
	}
	public static void traceZiorMeasure(Component comp,MeasTraceInfo measTraceInfo,
			final boolean bFormulaTrace) {
		if (measTraceInfo == null || measTraceInfo.getStrAloneID() == null
				|| measTraceInfo.getStrKeyVals() == null
				|| measTraceInfo.getStrMeasurePK() == null
				|| measTraceInfo.getStrReportPK() == null) {
			UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00102"),null);
			return;
		}
		
		JApplet applet=(JApplet)SwingUtilities.getAncestorOfClass(JApplet.class, comp);
		
		RefData.setProxy(new IufoRefData());
		
		Mainboard m_mainboard=KeyCondPanel.getMainboard(comp);
		if (m_mainboard==null)
			m_mainboard=FreeQueryMainBoardListner.getMainBoard();


		Object[] retObjs=null;
		try{
			retObjs=(Object[])ActionHandler.exec(TableInputActionHandler.class.getName(), "getMeasTraceContext",new Object[]{measTraceInfo,m_mainboard==null});
		}catch(Exception e){
			UfoPublic.sendWarningMessage(e.getMessage(),null);
			return;
		}
		
		IContext context=(IContext)retObjs[0];
		measTraceInfo=(MeasTraceInfo)retObjs[1];
		TaskVO[] tasks=(TaskVO[])retObjs[2];
		if (tasks.length==1){
			context.setAttribute(TASK_PK,tasks[0].getId());
		} else {
			TaskSelectDlg dlg = new TaskSelectDlg(tasks);
			dlg.show();
			if (dlg.getResult() == UfoDialog.ID_OK) {
				context.setAttribute(TASK_PK, dlg.getSelectedValueItem().getId());
			} else {
				return;
			}
		} 
		
		MeasurePubDataVO pubData=((MeasurePubDataVO)context.getAttribute(MEASURE_PUB_DATA_VO));

		if (m_mainboard == null) {// 从就框架到新框架暂时处理
			String uri = "zior-default.xml";
			URL url = ResourceManager.getResource(uri);
			if (comp instanceof UfoReport) {
				IContext oldContext = ((UfoReport) comp).getContext();
				if (oldContext != null) {
					context.setAttribute("SERVER_PORT",
							(String) oldContext.getAttribute("SERVER_PORT"));
				}
			}
			context.setAttribute(IUfoContextKey.OPERATION_STATE,IUfoContextKey.OPERATION_INPUT);
			context.setAttribute(IUfoContextKey.PERSPECTIVE_ID,IUfoContextKey.PERS_DATA_INPUT);
			context.setAttribute(CURRENT_LANG, measTraceInfo.getStrLangCode());
			//关闭透视图
			context.setAttribute(ComContextKey.PERSPECTIVE_SWITCH_STATE, false);
			context.setAttribute(IUfoContextKey.CUR_USER_ID,measTraceInfo.getStrOperUserPK());
			context.setAttribute(IUfoContextKey.REPORT_PK,measTraceInfo.getStrReportPK());
			context.setAttribute(IUfoContextKey.ALONE_ID, pubData.getAloneID());
			DataSourceInfo curDSInfo = measTraceInfo.getDataSource();

			if (curDSInfo != null) {
				try {
					String dataSourceID = curDSInfo.getDSID();
					if (dataSourceID != null) {
						context.setAttribute(IDataSourceParam.DS_UNIT, curDSInfo.getDSUnitPK());
						context.setAttribute(IDataSourceParam.DS_USER, curDSInfo.getDSUserPK());
						context.setAttribute(IUfoContextKey.DATA_SOURCE_ID, dataSourceID);
						context.setAttribute(IDataSourceParam.DS_PASSWORD, curDSInfo.getDSPwd());
						DataSourceVO dataSourceVO = DataSourceBO_Client.loadDataSByID(dataSourceID);
						if (dataSourceVO != null) {
							dataSourceVO.setLoginUnit(curDSInfo.getDSUnitPK());
							dataSourceVO.setLoginName(curDSInfo.getDSUserPK());
							dataSourceVO.setUnitId(curDSInfo.getDSUnitPK());
							String dsPassword = nc.bs.iufo.toolkit.Encrypt.decode(curDSInfo.getDSPwd(), dataSourceID);
							dataSourceVO.setLoginPassw(dsPassword);
							dataSourceVO.setLoginDate(curDSInfo.getDSDate());
							context.setAttribute(IUfoContextKey.DATA_SOURCE, dataSourceVO);
						}
						ContextFactory.addReplaceParam2Context(context);
					}

				} catch (Exception e) {
					AppDebug.debug(e);
				}
			}
			m_mainboard = new Mainboard(url.getFile(),context);
			m_mainboard.showByFrame(StringResource.getStringResource("miufohbbb00103"));
			FreeQueryMainBoardListner.setMainBoard(m_mainboard,applet);
		}
		RepDataControler control=RepDataControler.getInstance(m_mainboard);
		toFont(m_mainboard);
		final MeasTraceInfo finMeasTraceInfo=measTraceInfo;
		control.openEditWin(m_mainboard,pubData.getAloneID(),
				pubData, (String)context.getAttribute(TASK_PK), measTraceInfo.getStrReportPK(), true,
				null, null,new WinNavPostRepDataActive(measTraceInfo,bFormulaTrace),false);
	}

	/*
	 * public static void traceMeasure(UfoReport srcUfoReport,String
	 * strReportPK,String strAloneID,MeasTraceInfo measTraceInfo){ DEBUGING...;
	 * IRepDataParam repDataParam = null;
	 * if(InputBizOper.doGetTransObj(srcUfoReport).getRepDataParam() != null){
	 * repDataParam =
	 * (RepDataParam)((RepDataParam)InputBizOper.doGetTransObj(srcUfoReport).getRepDataParam()).clone();
	 * }else{ repDataParam = new RepDataParam(); } TableInputTransObj
	 * measTraceTransObj = new TableInputTransObj();
	 * repDataParam.setAloneID(strAloneID);
	 * repDataParam.setReportPK(strReportPK);
	 * measTraceTransObj.setRepDataParam(repDataParam);
	 * measTraceTransObj.setLangCode(new
	 * UfoContextVO(srcUfoReport.getContextVo()).getCurrentLan()); IInputBizOper
	 * measTraceBizOper = new
	 * MeasureTraceBizOper(srcUfoReport,measTraceTransObj,measTraceInfo);
	 * measTraceBizOper.performBizTask(ITableInputMenuType.BIZ_TYPE_MEASURE_TRACE); }
	 */

	/**
	 * 获取相关状态参数
	 * 
	 * @param tableInputContextVO
	 * @return
	 */
	public static Hashtable<String, Object> getParams(
			TableInputContextVO tableInputContextVO) {
		// 获取相关状态参数
		// 可能以后别的控制,需要从TableInputContextVO或服务器端获取
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		Object tableInputTransObj = tableInputContextVO
				.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj tableInput = tableInputTransObj != null
				&& (tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj) tableInputTransObj
				: null;

		if (tableInputContextVO == null || tableInput == null) {
			return params;
		}
		TableInputTransObj transObj = tableInput;
		if (transObj.getRepDataParam() != null) {
			params.put(ITableInputAppletParam.PARAM_REPDATA, "true");
			String strOperType = transObj.getRepDataParam().getOperType(); // getParameter(ITableInputAppletParam.PARAM_OPERTYPE);
			if (strOperType != null) {
				params.put(ITableInputAppletParam.PARAM_OPERTYPE, strOperType);
			}
		}
		return params;
	}

	/**
	 * 高亮显示指定区域 说明：设置公式追踪相关状态由在打开报表数据之前设置好
	 * 
	 * @param ufoReport
	 * @param curTracedPos
	 * @param bSetFormulaTrace
	 */
	public static void setTraceInfo(UfoReport ufoReport, IArea[] curTracedPos) {
		CellsModel cellsModel = ufoReport.getCellsModel();
		if (cellsModel != null) {
			// 设置新的高亮显示公式追踪的区域
			if (curTracedPos != null && curTracedPos.length > 0) {
				FormulaTraceBizUtil.highLightFormulaTracedPos(curTracedPos,
						ufoReport);
			}
		}
	}

	public static void setTraceInfo(RepDataEditor editor,CellsModel model, IArea[] curTracedPos) {
		List<CellPosition> cells = new ArrayList<CellPosition>();
		if (curTracedPos != null && curTracedPos.length > 0) {
			int iLength = curTracedPos.length;
			for (int i = 0; i < iLength; i++) {
				List<CellPosition> listTemp = model
						.getSeperateCellPos(curTracedPos[i]);
				if (listTemp != null)
					cells.addAll(listTemp);
			}
		}
		editor.setTraceCells(cells);
	}

	/**
	 * 刷新公式追踪结果面板
	 * 
	 * @param editor
	 * @param cellPos
	 */
	public static void refreshNavPanel(RepDataEditor editor,
			CellPosition cellPos) {
		FormTraceResultViewer viewer = (FormTraceResultViewer) editor
				.getMainboard()
				.getView(RepDataControler.FORMULA_TRACERESULT_ID);
		if (viewer == null) {
			return;
		}
		FormulaParsedData formulaParedData = null;
		if (cellPos == null) {
			viewer.getTracePanel().setFormulaParedData(null);
		}
		IFormulaTraceBiz formulaTraceBiz = UfoFormulaTraceBizHelper
				.getFormulaTraceBiz();
		boolean bExistFormula = formulaTraceBiz.existFormula(editor
				.getCellsModel(), cellPos);

		if (bExistFormula) {
			formulaParedData = formulaTraceBiz.parseFormula((Context) editor
					.getContext(), editor.getCellsModel(), cellPos);
			viewer.getTracePanel().setEditor(editor);
			viewer.getTracePanel().setFormulaParedData(formulaParedData);
		} else {
			viewer.getTracePanel().setFormulaParedData(null);
		}
	}

	/**
	 * 通过插件直接启动公式追踪功能的一次计算 说明：设置公式追踪相关状态由在打开报表数据之前设置好
	 * 
	 * @param cellsModel
	 * @param curCell
	 *            如果为null,则为缺省值A1
	 * @param isClear
	 *            是否要清除高亮显示的区域：切换报表的时候就要清除、指标追踪的时候不清除
	 */
	public static void startFormulaTrace(UfoReport report,
			CellPosition curCell, boolean isClear) {
		if (report == null || report.getCellsModel() == null) {
			return;
		}
		// 清除旧的公式追踪联查区域
		if (isClear) {
			FormulaTraceNavPanel panel = FormulaTraceBizUtil
					.getFormulaTraceNavPanel(report);
			if (panel != null) {
				panel.clear();
			}

		}
		// #用启动公式追踪功能
		FormulaTracePlugIn plugIn = (FormulaTracePlugIn) report
				.getPluginManager().getPlugin(
						FormulaTracePlugIn.class.getName());
		IExtension[] exts = plugIn.getDescriptor().getExtensions();
		FormulaTraceExt ext = ((FormulaTraceExt) exts[0]);
		String strAlondID = InputBizOper.doGetTransObj(report)
				.getRepDataParam().getAloneID();
		if (strAlondID == null) {
			return;
		}
		Object[] params = ext.getParams(report);
		UfoCommand cmd = ext.getCommand();
		if (cmd != null) {
			cmd.run(params, null);
		}

	}

	/**
	 * @param cellsModel
	 */
	public static void refreshCellsModel(UfoReport ufoReport,
			CellsModel cellsModel, Object transContextObj) {
		// 刷新UfoReport的UfoContextVO属性
		WindowNavUtil.doRefreshContextVO(ufoReport, transContextObj);

		ufoReport.getTable().setCurCellsModel(cellsModel);
		WindowNavUtil.doAfterCellsModel(ufoReport, transContextObj);
	}

	/**
	 * 准备CellsModel进入录入状态
	 */
	private static void doAfterCellsModel(UfoReport ufoReport,
			Object transContextObj) {
		// 设置readonly标志值
		ufoReport.setReadOnly(WindowNavUtil.isReadOnly(transContextObj),
				new TableInputAuth(ufoReport));

	}

	private static boolean isReadOnly(Object transContextObj) {
		// 表格控件是否只读：true,false
		if (transContextObj instanceof InputContextVO) {
			InputContextVO contextVO = (InputContextVO) transContextObj;
			return (Integer) contextVO.getAttribute(DATA_RIGHT) < InputContextVO.RIGHT_DATA_WRITE;
		}
		return false;
	}

	/**
	 * 刷新UfoReport的UfoContextVO属性
	 * 
	 * @param ufoReport
	 * @param transContextObj
	 */
	private static void doRefreshContextVO(UfoReport ufoReport,
			Object transContextObj) {
		// 刷新UfoReport的UfoContextVO属性
		if (transContextObj instanceof UfoContextVO) {
			UfoContextVO newUfoContextVO = (UfoContextVO) transContextObj;
			TableInputContextVO oldInputContextVO = (TableInputContextVO) ufoReport
					.getContextVo();
			Object showRepTreeObj = oldInputContextVO
					.getAttribute(SHOW_REP_TREE);
			boolean isShowRepTree = showRepTreeObj == null ? false : Boolean
					.parseBoolean(showRepTreeObj.toString());

			Object showKeyPanelObj = oldInputContextVO
					.getAttribute(SHOW_KEY_PANEL);
			boolean isShowKeyPanel = showKeyPanelObj == null ? false : Boolean
					.parseBoolean(showKeyPanelObj.toString());

			Object genralQueryObj = oldInputContextVO
					.getAttribute(GENRAL_QUERY);
			boolean isgenralQuery = genralQueryObj == null ? false : Boolean
					.parseBoolean(genralQueryObj.toString());

			TableInputContextVO newInputContextVO = InputBizOper
					.geneNewInputContextVO(ufoReport, newUfoContextVO);
			ufoReport.setContextVO(newInputContextVO);
			newInputContextVO.setAttribute(LOGIN_UNIT_CODE, oldInputContextVO
					.getAttribute(LOGIN_UNIT_CODE));
			newInputContextVO.setAttribute(LOGIN_UNIT_ID, oldInputContextVO
					.getAttribute(LOGIN_UNIT_ID));
			newInputContextVO.setAttribute(SHOW_REP_TREE, isShowRepTree);
			newInputContextVO.setAttribute(SHOW_KEY_PANEL, isShowKeyPanel);
			newInputContextVO.setAttribute(GENRAL_QUERY, isgenralQuery);
		}
	}

	/**
	 * 
	 * @param strReportPK
	 * @param cellsModel
	 * @param strMeasurePK
	 * @param strKeyVals
	 *            如果是主表送主表关键字值，是动态区只送动态区自己的关键字
	 * @return
	 */
	public static IArea[] calMeasureTracedPos(String strReportPK,
			CellsModel cellsModel, String strMeasurePK, String[] strKeyVals) {
		IArea[] curTracedPos = null;

		// curTracedPos = new IArea[]{AreaPosition.getInstance(2,2,5,5)};
		CellPosition mesureCell = CellsModelOperator.getMeasureDataPos(
				strReportPK, cellsModel, strMeasurePK, strKeyVals);
		curTracedPos = new IArea[] { mesureCell };
		return curTracedPos;
	}

	/**
	 * 老框架公式追踪入口 为自由报表保留，计划V6.0在自由报表迁移到新UI框架之后删除
	 * 
	 * @param measTraceInfo
	 */
	public static void traceMeasure(MeasTraceInfo measTraceInfo) {

		if (measTraceInfo == null || measTraceInfo.getStrAloneID() == null
				|| measTraceInfo.getStrKeyVals() == null
				|| measTraceInfo.getStrMeasurePK() == null
				|| measTraceInfo.getStrReportPK() == null) {
			UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00102"),
					null);
			return;
		}

		CellsModel cellsModel = null;
		// 主表
		MeasurePubDataVO pubData = null;
		// 动态区
		MeasurePubDataVO tPubData = null;
		TableInputContextVO inputContextVO = null;
		// 构建新的空ContextVO
		TableInputTransObj tableInput = new TableInputTransObj();
		tableInput.setRepDataParam(new RepDataParam());
		TableInputContextVO newInputContextVO = new TableInputContextVO(
				new UfoContextVO(), tableInput);

		// 新的传送类型
		tableInput.setType(TableInputTransObj.TYPE_REPDATA);
		tableInput.getRepDataParam().setOperType(
				TableInputParam.OPERTYPE_REPDATA_INPUT);
		newInputContextVO.setAttribute(SHOW_REP_TREE, measTraceInfo
				.isShowRepTree());
		newInputContextVO.setAttribute(GENRAL_QUERY, measTraceInfo
				.isGeneryQuery());
		newInputContextVO.setAttribute(CURRENT_LANG, measTraceInfo.getStrLangCode());
		newInputContextVO.setAttribute(LOGIN_UNIT_ID, measTraceInfo
				.getStrLoginUnitID());
		// 登陆单位code
		// ，在TableInputOperServlet.doGetNewAloneID()中调用tableInputTransObj.getLoginUnit()获取id来检查的
		UnitCache unitCache = IUFOUICacheManager.getSingleton().getUnitCache();
		UnitInfoVO unitInfo = unitCache.getUnitInfoByPK(measTraceInfo
				.getStrLoginUnitID());
		;
		if (unitInfo != null) {
			newInputContextVO.setAttribute(LOGIN_UNIT_CODE, unitInfo.getCode());
			tableInput.setLoginUnit(unitInfo.getCode());
		}
		tableInput.setLangCode(measTraceInfo.getStrLangCode());

		newInputContextVO.setAttribute(ORG_PK, measTraceInfo.getStrOrgPK());
		tableInput.getRepDataParam().setOrgPK(measTraceInfo.getStrOrgPK());

		// 设置报表相关
		ReportCache rc = IUFOUICacheManager.getSingleton().getReportCache();
		tableInput.getRepDataParam()
				.setReportPK(measTraceInfo.getStrReportPK());
		// 根据报表pk获取新的报表VO
		ReportVO reportVO = rc.getByPK(measTraceInfo.getStrReportPK());
		newInputContextVO.setReportcode(reportVO.getCode());
		newInputContextVO.setName(reportVO.getName());
		// 设置登陆用户信息
		if (measTraceInfo.getStrOperUserPK() != null) {
			tableInput.getRepDataParam().setOperUserPK(
					measTraceInfo.getStrOperUserPK());
		} else {
			tableInput.getRepDataParam().setOperUserPK(reportVO.getUserPK());
		}

		// 准备代码参照录入的环境
		RefData.setProxy(new IufoRefData());
		// 根据aloneid获取keygroupvo
		String unitCode = "";

		try {
			pubData = MeasurePubDataBO_Client.findByAloneID(measTraceInfo
					.getStrAloneID());
			if (reportVO == null) {
				UfoPublic.sendWarningMessage(MultiLang
						.getString("uiuforep00105"), null);
				return;
			} else {

				// 存放动态区的自己的关键字值
				Vector<String> keyValues = new Vector<String>();
				// 非空的所有关键字值
				Vector<String> vKeyValues = new Vector<String>();
				String[] allKeyValus = pubData.getKeywords();

				for (int j = 0; j < allKeyValus.length; j++) {
					if (allKeyValus[j] != null) {
						vKeyValues.add(allKeyValus[j]);
					}
				}

				// 根据报表获取主表的keygrouppk，如果不同说明aloneid是有公有关键字动态区的aloneid,
				// 需要过滤掉动态区自己的关键字值，重新构造MeasurePubDataVO
				if (!reportVO.getKeyCombPK().equals(pubData.getKType())) {
					tPubData = new MeasurePubDataVO();
					tPubData.setKType(reportVO.getKeyCombPK());
					tPubData.setVer(pubData.getVer());
					tPubData.setFormulaID(pubData.getFormulaID());

					KeyGroupCache kgc = IUFOUICacheManager.getSingleton()
							.getKeyGroupCache();
					KeyGroupVO kgvo = kgc.getByPK(reportVO.getKeyCombPK());
					tPubData.setKeyGroup(kgvo);
					tPubData.setAccSchemePK(pubData.getAccSchemePK());

					kgvo = pubData.getKeyGroup();
					KeyVO[] keyvos = kgvo.getKeys();
					for (int k = 0; k < keyvos.length; k++) {
						tPubData.setKeywordByPK(keyvos[k].getKeywordPK(),
								pubData
										.getKeywordByPK(keyvos[k]
												.getKeywordPK()));
						if (tPubData.getKeyByPK(keyvos[k].getKeywordPK()) < 0) {
							if ((keyvos[k].getCode() != null)
									&& (KeyVO.CODE_TYPE_CORP.equals(keyvos[k]
											.getCode()) || KeyVO.CODE_TYPE_DIC_CORP
											.equals(keyvos[k].getCode()))) {
								unitInfo = unitCache.getUnitInfoByPK(pubData
										.getKeywordByPK(keyvos[k]
												.getKeywordPK()));
								keyValues.add(unitInfo.getCode());
							} else {
								keyValues.add(pubData.getKeywordByPK(keyvos[k]
										.getKeywordPK()));
							}
						}
					}
					// tPubData.setKeywords(pubData.getKeywords());

					pubData = MeasurePubDataBO_Client.findByKeywords(tPubData);

				} else {
					// 说明是动态区私有关键字，要过滤掉主表的关键字
					if (measTraceInfo.getStrKeyVals().length > vKeyValues
							.size()) {
						for (int i = vKeyValues.size(); i < measTraceInfo
								.getStrKeyVals().length; i++) {
							keyValues.add(measTraceInfo.getStrKeyVals()[i]);
						}
					}
				}
				// 只获取动态区的关键字值
				if (keyValues.size() > 0)
					measTraceInfo.setStrKeyVals(keyValues
							.toArray(new String[0]));
			}
			// 报表工具录入下，只对原表数据进行追踪
			if (!measTraceInfo.isGeneryQuery() && pubData != null
					&& pubData.getVer() != 0) {
				UfoPublic.sendWarningMessage(MultiLang
						.getString("uiuforep00165"), null);
				return;
			}
			newInputContextVO.setAttribute(MEASURE_PUB_DATA_VO, pubData);
			// 设置数据源
			if (measTraceInfo.getDataSource() != null) {
				tableInput.getRepDataParam().setDSInfo(
						measTraceInfo.getDataSource());
			}
			tableInput.getRepDataParam().setAloneID(pubData.getAloneID());
			// 检查是否有数据录入
			if (pubData == null
					|| !CheckResultBO_Client.isAloneIDInput(pubData
							.getAloneID(), new String[] { measTraceInfo
							.getStrReportPK() })) {
				UfoPublic.sendWarningMessage("[(" + reportVO.getCode() + ")"
						+ reportVO.getName() + "]"
						+ MultiLang.getString("uiuforep00104"), null);
				return;
			}

			// 设置用户、任务、组织、单位,在新打开时有用：ITableInputMenuType.MENU_TYPE_OPEN
			// 操作单位从数据来,用户从传入参数来
			// 当是切换单位参照的单位树用登陆单位LoginUnitPK构建，当是报表数据中的单位参照则用OperUnitPK来构建单位树
			String strOperUnitPK = pubData.getUnitPK();
			// 可能没有单位关键字
			if (strOperUnitPK != null) {
				tableInput.getRepDataParam().setOperUnitPK(strOperUnitPK);
				unitInfo = unitCache.getUnitInfoByPK(strOperUnitPK);
				unitCode = unitInfo.getCode();
				// CurUnitId与OperUnitPK一致
				newInputContextVO.setAttribute(CUR_UNIT_CODE, unitCode);
				newInputContextVO.setAttribute(CUR_UNIT_ID, strOperUnitPK);
			}
			// 获取该报表对应的所有任务，让用户选择
			TaskCache taskCache = IUFOUICacheManager.getSingleton()
					.getTaskCache();
			// 获取报表所属的所有任务(还包含当前登陆单位没有权限的任务)
			String[] taskIds = taskCache.getTaskIdsByReportId(measTraceInfo
					.getStrReportPK());

			// 如果该报表reportPK所属的任务在传送的taskId中，则在newInputContextVO中设置该taskId
			// 如果传送的taskId为空或该报表reportPK所属的当前登陆单位有权限的任务不是传送的taskId中,则让用户选择
			// 分配给当前登陆用户的任务
			ReleaseTaskVO[] taskVOsByUnit = null;
			// 分配给当前登陆单位权限的任务，如果任务设置了权限控制，获取taskIds的reportPk有可能没有分配给该任务
			ArrayList<TaskVO> rightTaskVo = new ArrayList<TaskVO>();
			String loginUnitId = newInputContextVO.getAttribute(LOGIN_UNIT_ID) == null ? null
					: (String) newInputContextVO.getAttribute(LOGIN_UNIT_ID);

			if (taskIds != null && loginUnitId != null) {
				UserCache userCache = IUFOUICacheManager.getSingleton()
						.getUserCache();
				// 登陆操作用户信息
				UserInfoVO userInfoVO = userCache.getUserById(tableInput
						.getRepDataParam().getOperUserPK());
				if (UserRoleUtil.isRoleAdministrator(userInfoVO))
					taskVOsByUnit = ReleaseMngBO_Client
							.loadTaskByUnitID(loginUnitId);
				else
					taskVOsByUnit = ReleaseMngBO_Client
							.loadTaskByUser(userInfoVO);
				// measTraceInfo传送的taskId是否是合法的任务
				boolean isExistTask = false;

				if (taskVOsByUnit != null) {
					String strOrgPK = newInputContextVO.getAttribute(ORG_PK) == null ? null
							: (String) newInputContextVO.getAttribute(ORG_PK);

					for (int n = 0; n < taskIds.length; n++) {
						for (int m = 0; m < taskVOsByUnit.length; m++) {
							TaskVO task = taskCache.getTaskVO(taskVOsByUnit[m]
									.getTaskID());
							if (taskIds[n].equals(task.getId())) {
								int nDataRight = RepDataRightUtil.getDataRight(
										userInfoVO, strOperUnitPK, taskIds[n],
										measTraceInfo.getStrReportPK(),
										strOrgPK);
								if (nDataRight >= RepDataRightVO.RIGHT_TYPE_VIEW) {
									rightTaskVo.add(task);
									if (measTraceInfo.getStrTaskId() != null
											&& measTraceInfo.getStrTaskId()
													.equals(taskIds[n])) {
										isExistTask = true;
									}
								}
							}

						}

					}
				}

				if (isExistTask) {
					tableInput.getRepDataParam().setTaskPK(
							measTraceInfo.getStrTaskId());
					newInputContextVO.setAttribute(TASK_PK, measTraceInfo
							.getStrTaskId());
				} else if (rightTaskVo.size() > 0) {
					if (rightTaskVo.size() == 1) {
						tableInput.getRepDataParam().setTaskPK(
								rightTaskVo.get(0).getId());
						newInputContextVO.setAttribute(TASK_PK, rightTaskVo
								.get(0).getId());
					} else {
						TaskSelectDlg dlg = new TaskSelectDlg(rightTaskVo
								.toArray(new TaskVO[0]));
						dlg.show();
						if (dlg.getResult() == UfoDialog.ID_OK) {
							tableInput.getRepDataParam().setTaskPK(
									dlg.getSelectedValueItem().getId());
							newInputContextVO.setAttribute(TASK_PK, dlg
									.getSelectedValueItem().getId());
						} else {
							return;
						}
					}
				} else {
					UfoPublic.sendWarningMessage(MultiLang
							.getString("uiuforep00101"), null);
					return;
				}
			} else {
				UfoPublic.sendWarningMessage(MultiLang
						.getString("uiuforep00102"), null);
				return;
			}

		} catch (Exception e) {
			AppDebug.debug(e);
			UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00105"),
					null);
			return;
		}

		Hashtable<String, Object> params = WindowNavUtil
				.getParams(newInputContextVO);
		// 获取指标窗口，如果在导航中不存在该窗口就构建一个新的窗口，构建过程中会根据newInputContextVO的aloneid构建左边的树和查询面板中的关键字信息

		UfoReport ufoReport = WindowNavUtil.getReportInstance(
				newInputContextVO, cellsModel, UfoReport.OPERATION_INPUT,
				params, ReportViewType.VIEW_REPORT, MultiLang
						.getString("uiuforep00103"));

		// 设置新的查询条件
		ChooseCordPanel cordPanel = GeneralQueryUtil
				.getChooseCordPanel(ufoReport);
		ChooseRepPanel repsPanel = GeneralQueryUtil
				.getChooseRepPanel(ufoReport);
		boolean isAddData = false;
		TableSearchCondVO searchCondVO = cordPanel.getTableSearchCondObj();
		searchCondVO.setStrKeyValues(pubData.getKeywords());
		if (measTraceInfo.isGeneryQuery()) {
			// 防止设置版本信息的时候再去后台查询子版本信息
			cordPanel.setMeasureTrace(true);
			int ver = pubData.getVer();
			if (ver > 1000) {
				ver = (ver - 999 - 1) / 10;
			}
			cordPanel.getVersionComb().addItem(
					cordPanel.new VerItem(Integer.toString(ver), null));
			cordPanel.getSubVersionComb().addItem(
					cordPanel.new VerItem(measTraceInfo.getStrAloneID(), null));
			cordPanel.setMeasureTrace(false);
			// 使得其打开报表的时候定位正确的版本
			tableInput.getRepDataParam().setVer(Integer.toString(ver));
			tableInput.getRepDataParam().setSubVer(
					measTraceInfo.getStrAloneID());
		} else {
			// 设置是否是合并报表
			if (pubData.getVer() == HBBBSysParaUtil.VER_HBBB) {
				tableInput.getRepDataParam().setIsHBBBData(true);
			}
		}
		// 如果是按单位组织的时候，追踪之前要先设置好查询面板的查询条件特别是报表列表
		if (!measTraceInfo.isShowRepTree()) {
			String[][] reportsOfTask = TableSearchUtil.loadReportsByUserTask(
					tableInput, pubData.getUnitPK());
			if (reportsOfTask != null) {
				cordPanel.addRepItemsToList(reportsOfTask);
				cordPanel.switchTreeView(null, measTraceInfo.getStrReportPK());
			}
			DefaultMutableTreeNode unitNode = null;
			UnitTreeModel unitTree = repsPanel.getUnitTreeModel();
			if (unitTree.getTreeModel() != null
					&& unitTree.getTreeModel().getRoot() instanceof DefaultMutableTreeNode)
				unitNode = unitTree.getUnitCodeFromTreeNode(
						(DefaultMutableTreeNode) (unitTree.getTreeModel()
								.getRoot()), unitInfo.getCode());
			if (unitNode != null) {
				isAddData = true;
			}
		} else {
			ChooseRepData[] reports = repsPanel.getRepTreeModel().getReports();
			if (reports != null) {
				for (int repNum = 0; repNum < reports.length; repNum++) {
					if (reports[repNum].getReportPK().equals(
							measTraceInfo.getStrReportPK())) {
						isAddData = true;
						break;
					}
				}
			}
		}

		// 模拟切换报表动作，加载报表的数据
		if (isAddData) {
			// 说明：设置公式追踪相关状态由在打开报表数据之前设置好
			if (measTraceInfo.isFormulaTraceOpen()) {
				String strMenuName = FormulaTraceBizUtil.doGetStrFormulaTrace();// "公式追踪"
				String[] strParantMenuNames = new String[] { FormulaTraceBizUtil
						.doGetStrData() };// "数据"
				FormulaTraceBizUtil.setMenuSelected(strMenuName,
						strParantMenuNames, ufoReport);
			}
			repsPanel.setSelNodeOfNavTree(measTraceInfo.getStrReportPK(),
					unitCode);
			// 会根据公式追踪菜单状态调用startFormulaTrace方法
			if (measTraceInfo.isShowRepTree()) {
				repsPanel.switchRep(measTraceInfo.getStrReportPK());
			} else {
				repsPanel.switchUnit(unitCode);
			}

			// 进行指标追踪
			inputContextVO = (TableInputContextVO) ufoReport.getContextVo();
			cellsModel = ufoReport.getCellsModel();
			ChooseCordPanel chooseCordPanel = GeneralQueryUtil
					.getChooseCordPanel(ufoReport);
			if (cellsModel == null
					|| (measTraceInfo.isGeneryQuery() && chooseCordPanel
							.getDataSubVerType() == null)) {
				UfoPublic.sendWarningMessage(MultiLang
						.getString("uiuforep00104"), null);
			} else {
				// 计算获得公式位置信息
				String strMeasurePK = measTraceInfo.getStrMeasurePK();
				String[] strKeyVals = measTraceInfo.getStrKeyVals();
				Object repIdObj = inputContextVO.getAttribute(REPORT_PK);
				String strReportPK = repIdObj != null
						&& (repIdObj instanceof String) ? (String) repIdObj
						: null;

				IArea[] curTracedPos = WindowNavUtil.calMeasureTracedPos(
						strReportPK, cellsModel, strMeasurePK, strKeyVals);

				// 追踪相关状态设置:主要是高亮显示
				WindowNavUtil.setTraceInfo(ufoReport, curTracedPos);

			}
		} else {
			UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00105"),
					null);
		}

	}

	/**
	 * 使窗口跳到最前边
	 * 
	 * @create by wangyga at 2009-6-10,下午12:44:02
	 * 
	 * @param container
	 */
	public static void toFont(Container container) {
		Container c = SwingUtilities.getAncestorOfClass(JRootPane.class,
				container);
		if (c == null) {
			return;
		}
		Container parent = c.getParent();

		if (parent instanceof Frame) {
			((Frame) parent).setExtendedState(Frame.MAXIMIZED_BOTH);
			((Frame) parent).toFront();
		} else if (parent instanceof Applet) {
			JSObject jsWin = JSObject.getWindow((Applet) parent);
			if (jsWin == null) {
				throw new IllegalArgumentException();
			}
			jsWin.eval("window.focus()");
		} else {
			throw new IllegalArgumentException();
		}
	}
}
 