package nc.ui.iufo.input.control;

import java.awt.Color;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nc.pub.iufo.accperiod.AccPeriodSchemeUtil;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.hbbb.pub.Util;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.input.InputUtil;
import nc.ui.iufo.input.edit.IPostRepDataEditorActive;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.iufo.input.edit.TotalSourceEditor;
import nc.ui.iufo.input.table.IufoRefData;
import nc.ui.iufo.input.view.CheckResultViewer;
import nc.ui.iufo.input.view.NavRepTreeViewer;
import nc.ui.iufo.input.view.NavUnitTreeViewer;
import nc.ui.iufo.pub.UfoPublic;
import nc.ui.iufo.user.UserMngBO_Client;
import nc.util.iufo.pub.IDMaker;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.data.VerItem;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.pub.date.UFODate;
import nc.vo.iufo.task.TaskDefaultVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.dataset.Context;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.comp.KStatusBar;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.exception.MessageException;
import com.ufida.zior.view.Mainboard;
import com.ufsoft.iufo.check.ui.CheckResultBO_Client;
import com.ufsoft.iufo.check.vo.CheckDetailVO;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.autocalc.ReportCalUtil;
import com.ufsoft.iufo.inputplugin.inputcore.RefData;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.InputDirConstant;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.repdatainput.LoginEnvVO;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputAppletParam;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class RepDataControler implements Cloneable{
	private static final String REP_DATA_CONTROLER_KEY="###rep_data_controler_key###";
	
	// 单位树面板的ID
	transient public static String NAV_UNIT_TREE_ID = "iufo.input.dir_unit.view";

	// 报表树面板的ID
	transient public static String NAV_REP_TREE_ID = "iufo.input.dir_rep.view";

	// 第一个报表数据页签的ID
	transient public static String EDIT_DATA_ID = "iufo.input.data.view";

	// 审核结果页板的ID
	transient public static String CHECK_RESULT_ID = "iufo.input.checkresult.view";

	// 公式追踪面板ID
	transient public static final String FORMULA_TRACERESULT_ID = "iufo.input.formresult.view";

	// 汇总结果溯源或查看汇总下级面板的ID，系统中保证此类面板只打开一个
	transient public static String TOTAL_SOURCE_ID = "iufo.input.totalsource.view";

	// 所有时间关键字共用的关键字Key值，不同时间关键字在m_hashKeyVal中用的是同一个Key值
	transient private static final String COMM_TIME_KEY = "commtimekey";

	// 是否报表数据被有效打开过
	transient private boolean m_bRepHadOpened = false;

	// 报表数据窗口ID与报表录入条件的哈希表
	transient private Map<String, RepDataCondVO> m_hashRepDataCond = new Hashtable<String, RepDataCondVO>();

	// 各关键字最近录入的关键字值
	transient private Map<String, String> m_hashKeyVal = new Hashtable<String, String>();

	// 关键字组合PK与AloneID的哈希表,打开表时，使用该关键字组合最近一次的关键字条件来打开表
	transient private Map<String, String> m_hashAloneID = new Hashtable<String, String>();

	// 最近打开的报表数据窗口
	transient private RepDataEditor m_lastActiveRepDataEditor = null;

	// 最近选择的报表PK
	transient private String m_strSelectedRepPK = null;

	// 最近选择的任务PK
	transient private String m_strSelectedTaskPK = null;

	// 最近选择的单位PK
	transient private String m_strSelectedUnitPK = null;

	// 报表数据录入状态报表数据的版本
	transient private int m_iDataVer = 0;

	// 最近打开的综合查询报表数据版本PK
	transient private String m_strVerItemPK = null;

	// 最近打开的给定查询的报表数据子版本PK
	transient private String m_strVerSubItemPK = null;

	// 最近选择的报表PK是否合法
	transient private boolean m_bRepValidSelect = false;

	// 是否显示封存的任务
	transient private boolean m_bShowSealedTask = false;

	// 系统中所有的表间审核结果：键值为#任务PK$AloneID
	transient private Map<String, CheckResultVO> m_hashCheckResult = new Hashtable<String, CheckResultVO>();

	// 用户选择追踪的最近表间审核结果：键值同上
	transient private Map<String, CheckDetailVO> m_hashCheckDetail = new Hashtable<String, CheckDetailVO>();

	// 审核单元格需要标注的颜色
	transient private Map<String, Color> m_hashCheckColor = new Hashtable<String, Color>();

	// 审核结果追踪定位到的单元格列表，只对应一个报表数据窗口
	transient private Map<String, List<CellPosition>> m_hashCheckCells = new Hashtable<String, List<CellPosition>>();

	// 区域计算公式类
	transient private ReportCalUtil m_reportCalUtil = null;

	// 公式追踪菜单是否被选中
	transient private boolean bCanFormulaTrace = false;

	// 最后一次选择的录入方向
	transient private int m_iInputDir = InputDirConstant.DIR_RIGHT;

	static {
		RefData.setProxy(new IufoRefData());
	}
	
	public Object clone(){
		return this;
	}

	private RepDataControler(Mainboard mainBoard) {
		// 注册窗口激活监听器
		mainBoard.getEventManager().addListener(
				new RepDataEditorActiveListener());

		// 往主面板的context中注册当前用户及登录环境信息
		String userId = (String) mainBoard.getContext().getAttribute(
				IUfoContextKey.CUR_USER_ID);
		UserInfoVO userInfo;
		try {
			userInfo = UserMngBO_Client.loadUser(userId);
		} catch (Exception e) {
			throw new MessageException(e.toString());
		}
		// IUFOUICacheManager.getSingleton().getUserCache().getUserById((String)m_mainBoard.getContext().getAttribute(IUfoContextKey.CUR_USER_ID));
		mainBoard.getContext().setAttribute(IUfoContextKey.CUR_USER_INFO,
				userInfo);
		// UserRoleFuncInfoCenter.getInstance().getUserRoleFuncInfo(userInfo.getID());
		initLoginEnv(mainBoard);

		// 设置是否显示0的参数
		String strShow = (String) mainBoard.getContext().getAttribute(
				ITableInputAppletParam.PARAM_TI_DISPLAYZERO);
		if (strShow != null && "true".equalsIgnoreCase(strShow))
			ReportStyle.setShowZero(true);
		else
			ReportStyle.setShowZero(false);

		strShow = (String) mainBoard.getContext().getAttribute(
				ITableInputAppletParam.PARAM_TI_SHOWREFID);
		if (strShow != null && "true".equalsIgnoreCase(strShow))
			ReportStyle.setShowRefID(true);
		else
			ReportStyle.setShowRefID(false);

		// 取用户选择打开的报表
		String strReportPK = (String) mainBoard.getContext().getAttribute(
				IUfoContextKey.REPORT_PK);
		if ("repid".equalsIgnoreCase(strReportPK))
			strReportPK = null;

		// 设置默认选中的任务、报表、单位
		m_strSelectedTaskPK = (String) mainBoard.getContext().getAttribute(
				IUfoContextKey.TASK_PK);
		;
		m_strSelectedRepPK = strReportPK;
		m_strSelectedUnitPK = userInfo.getUnitId();

		// 得到录入状态下报表数据的版本
		setDataVer(isHBBBData(mainBoard) ? HBBBSysParaUtil.VER_HBBB : 0);
	}

	static void initRepDataControler(Mainboard mainBoard) {
		// 实例化对象
		RepDataControler repDataControler = new RepDataControler(mainBoard);
		mainBoard.getContext().setAttribute(REP_DATA_CONTROLER_KEY, repDataControler);

		// 处理默认的打开报表操作
		String strAloneID = (String) mainBoard.getContext().getAttribute(
				IUfoContextKey.ALONE_ID);

		if (strAloneID != null) {
			try {
				MeasurePubDataVO pubData = MeasurePubDataBO_Client
						.findByAloneID(strAloneID);
				repDataControler.setDataVer(pubData.getVer());

				// 如果当前报表有单位关键字，则修改当前选中的单位
				if (!UfoPublic.stringIsNull(pubData.getUnitPK()))
					repDataControler.m_strSelectedUnitPK = pubData.getUnitPK();

				// 记录各关键字最近录入的值
				KeyVO[] keys = pubData.getKeyGroup().getKeys();
				for (int i = 0; i < keys.length; i++)
					repDataControler.setKeyVals(new String[] { keys[i]
							.getKeywordPK() }, new String[] { pubData
							.getKeywordByPK(keys[i].getKeywordPK()) });

				// 如果选中了报表，则需要打开报表数据
				if (repDataControler.m_strSelectedRepPK != null)
					repDataControler.openEditWin(mainBoard,strAloneID, pubData,
							repDataControler.m_strSelectedTaskPK,
							repDataControler.m_strSelectedRepPK, true, null,
							null, null, true);
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}

		// 此分支对应无报表数据打开的操作，一种情况是AloneID未确定，另一种情况是按任务查询时，AloneID确定而报表不确定
		if (strAloneID == null || repDataControler.m_strSelectedRepPK == null) {
			try {
				// 定位应该默认选中的任务及报表
				String[] strRetVals = (String[]) ActionHandler.exec(
						"nc.ui.iufo.input.RepDataActionHandler",
						"loadFirstRepPK", new Object[] {
								repDataControler.getCurUserInfo(mainBoard),
								repDataControler.m_strSelectedTaskPK,
								new Boolean(repDataControler.isHBBBData(mainBoard)) });

				if (strRetVals != null && strRetVals.length >= 2) {
					// 重新设置当前选中的任务报表
					repDataControler.m_strSelectedTaskPK = strRetVals[0];
					repDataControler.m_bRepValidSelect = true;
					repDataControler.m_strSelectedRepPK = strRetVals[1];

					// 打开一个空的报表数据面板，初始化关键字面板
					repDataControler.doOpenRepEditWin(mainBoard,true);
					repDataControler.m_bRepHadOpened = true;
				}
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}
	}

	static void uninitializeRepDataControler(Mainboard mainBoard) {
		mainBoard.getContext().removeAttribute(REP_DATA_CONTROLER_KEY);
	}

	public static RepDataControler getInstance(Mainboard mainBoard) {
		if (mainBoard == null)
			return null;
		return (RepDataControler)mainBoard.getContext().getAttribute(REP_DATA_CONTROLER_KEY);
	}

	/**
	 * 打开报表数据窗口，此方法在系统初始化及从单位报表树双击树节点时调用
	 * 
	 * @param bInit
	 *            ，是否在系统初始化时，打开默认窗口，只有在RepDataControler.initRepDataControler方法中调用时
	 *            ，此参数值才为true
	 * @throws Exception
	 */
	public void doOpenRepEditWin(Mainboard mainBoard,boolean bInit) throws Exception {
		ReportCache repCache = IUFOUICacheManager.getSingleton()
				.getReportCache();
		KeyGroupCache kgCache = IUFOUICacheManager.getSingleton()
				.getKeyGroupCache();
		TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();

		TaskVO task = taskCache.getTaskVO(m_strSelectedTaskPK);
		ReportVO report = repCache.getByPK(m_strSelectedRepPK);
		KeyGroupVO keyGroup = kgCache.getByPK(report.getKeyCombPK());
		TaskDefaultVO taskDefault = taskCache
				.getTaskDefaultVO(m_strSelectedTaskPK);

		// 优先取最后一次活跃窗口的关键字值
		String strAloneID = null;
		if (m_lastActiveRepDataEditor != null)
			strAloneID = m_lastActiveRepDataEditor.getAloneID();

		// 如果取不到最近一次活跃窗口的关键字值，取该关键字组合最近一次打开的关键字值
		if (strAloneID == null)
			strAloneID = m_hashAloneID.get(report.getKeyCombPK());

		// 构造要打开报表数据的MeasurePubDataVO
		MeasurePubDataVO pubData = new MeasurePubDataVO();
		pubData.setKType(report.getKeyCombPK());
		pubData.setKeyGroup(keyGroup);
		pubData.setAccSchemePK(InputUtil.getAccSechemePK(m_strSelectedTaskPK));

		MeasurePubDataVO oldPubData = null;
		if (strAloneID != null) {
			oldPubData = MeasurePubDataBO_Client.findByAloneID(strAloneID);
		}

		boolean bKeyValValid = true;
		int iDefaultIndex = 0;
		for (KeyVO key : keyGroup.getKeys()) {
			iDefaultIndex++;

			// 单位关键字取当前选中的单位
			if (key.getKeywordPK().equals(KeyVO.CORP_PK)) {
				pubData.setKeyword1(m_strSelectedUnitPK);
				continue;
			}

			// 优先取以前AloneID对应的关键字值
			String strKeyVal = null;
			if (key.getTimeKeyIndex() >= 0) {
				strKeyVal = m_hashKeyVal.get(key.getKeywordPK());
				if (strKeyVal == null)
					strKeyVal = m_hashKeyVal.get(COMM_TIME_KEY);
			} else
				strKeyVal = m_hashKeyVal.get(key.getKeywordPK());

			if (isEmptyStr(strKeyVal) && oldPubData != null)
				strKeyVal = oldPubData.getKeywordByPK(key.getKeywordPK());

			// 综合查询且时间关键字，取登录时间
			if (isEmptyStr(strKeyVal) && key.isTTimeKeyVO() && isGeneralQuery(mainBoard)) {
				strKeyVal = (String) mainBoard.getContext().getAttribute(
						IUfoContextKey.LOGIN_DATE);
				// 登录时间转化成会计期间
				if (key.isAccPeriodKey()) {
					strKeyVal = AccPeriodSchemeUtil.getInstance()
							.getAccPeriodByNatDate(pubData.getAccSchemePK(),
									key.getKeywordPK(), strKeyVal);
				}
			}

			// 再取任务默认值
			if (isEmptyStr(strKeyVal) && taskDefault != null) {
				strKeyVal = taskDefault.getKeywordValueByIndex(iDefaultIndex);
			}

			// 判断关键字值是否为空及时间关键字是否合法
			if (isEmptyStr(strKeyVal)
					|| (key.isTTimeKeyVO() && doCheckTimeValid(task, strKeyVal,
							key) == false))
				bKeyValValid = false;
			else
				pubData.setKeywordByPK(key.getKeywordPK(), strKeyVal);
		}

		// 设置版本号，对综合查询还要做处理
		pubData.setVer(getDataVer());
		if (isHBBBData(mainBoard) && Util.isHBRepDataRelatingWithTask())
			pubData.setFormulaID(task.getId());

		// 打开报表数据的主、子版本PK，用于综合查询
		String strVerItemPK = null;
		String strSubVerItemPK = null;
		strAloneID = null;

		// 关键字值不合法,则报表数据不打开，关键字面板未有效初始化
		String strOrgPK = (String) mainBoard.getContext().getAttribute(
				IUfoContextKey.ORG_PK);
		if (bKeyValValid == false) {
			strAloneID = null;
		}
		// 非综合查询，则按关键字条件加载AloneID,打开报表数据及初始化关键字面板
		else if (!isGeneralQuery(mainBoard)) {
			// 只有报表数据被有效打开过时，才查找AloneID,打开报表数据
			if (m_bRepHadOpened == true) {
				strAloneID = MeasurePubDataBO_Client.getAloneID(pubData);
				pubData.setAloneID(strAloneID);

				// 对于舍位数据，要判断报表数据是否录入，如果未录入 ，置AloneID为空，在RepDataEditor界面给出出错提示
				if (getSelectedRepPK() != null) {
					if (getDataVer() >= 1000
							&& !CheckResultBO_Client.isAloneIDInput(strAloneID,
									new String[] { getSelectedRepPK() })) {
						strAloneID = null;
					}
					if (strAloneID != null) {
						boolean bHasRight = (Boolean) ActionHandler.exec(
								"nc.ui.iufo.input.RepDataActionHandler",
								"isHasDataRight", new Object[] { pubData,
										m_strSelectedTaskPK,
										m_strSelectedRepPK,
										getCurUserInfo(mainBoard), strOrgPK });
						if (!bHasRight)
							strAloneID = null;
					}
				}
			} else if (getDataVer() >= 1000)
				bKeyValValid = false;
		}
		// 综合查询，则要找到一个最接近最近打开的数据的AloneID
		else {
			try {
				// 加载已录入数据主版本
				String[] strKeyVals = adjustKeyVals(pubData);
				VerItem[] vers = (VerItem[]) ActionHandler.exec(
						"nc.ui.iufo.input.RepDataActionHandler",
						"loadDataVersByCond", new Object[] {
								getCurUserInfo(mainBoard).getID(), m_strSelectedTaskPK,
								m_strSelectedRepPK, strKeyVals, strOrgPK });

				if (vers != null && vers.length > 0) {
					// 从已录入数据的主版本中，找到系统最近查看的主版本
					strVerItemPK = vers[0].getVerPK();
					if (m_strVerItemPK != null) {
						for (int i = 0; i < vers.length; i++) {
							if (vers[i].getVerPK().equals(m_strVerItemPK)) {
								strVerItemPK = m_strVerItemPK;
								break;
							}
						}
					}

					// 查找已录入数据的子版本
					VerItem[] subVers = (VerItem[]) ActionHandler.exec(
							"nc.ui.iufo.input.RepDataActionHandler",
							"loadDataSubVersByCond", new Object[] {
									getCurUserInfo(mainBoard).getID(),
									m_strSelectedTaskPK, m_strSelectedRepPK,
									strKeyVals, strVerItemPK, strOrgPK });

					// 从已录入数据的子版本中，找到系统最近查看的子版本
					strSubVerItemPK = subVers[0].getVerPK();
					if (m_strVerSubItemPK != null) {
						for (int i = 0; i < subVers.length; i++) {
							int iPos = subVers[i].getVerPK().indexOf("@");
							String strPK = subVers[i].getVerPK().substring(0,
									iPos);
							if (m_strVerSubItemPK.equals(strPK)) {
								strSubVerItemPK = subVers[i].getVerPK();
								break;
							}
						}
					}

					int iPos = strSubVerItemPK.indexOf("@");
					strAloneID = strSubVerItemPK.substring(iPos + 1);
					strSubVerItemPK = strSubVerItemPK.substring(0, iPos);
					pubData = MeasurePubDataBO_Client.findByAloneID(strAloneID);
				}
				// 报表数据未有效打开过时，不能打开报表数据，所以此处置AloneID为空，则关键字值置为无效，避免弹出出错提示
				if (!m_bRepHadOpened) {
					strAloneID = null;
					bKeyValValid = false;
				}
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}

		openEditWin(mainBoard,strAloneID, pubData, m_strSelectedTaskPK,
				m_strSelectedRepPK, bKeyValValid, strVerItemPK,
				strSubVerItemPK, null, bInit);
	}

	public void doOpenRepEditWinWithPubData(Mainboard mainBoard,String strAloneID,
			MeasurePubDataVO pubData, String strTaskPK, String strRepPK,
			boolean bKeyValid, String strVerItemPK, String strVerSubItemPK)
			throws Exception {
		if (m_bRepHadOpened == true) {
			// 对于舍位数据，要判断报表数据是否录入，如果未录入 ，置AloneID为空，在RepDataEditor界面给出出错提示
			if (strAloneID != null && getDataVer() >= 1000
					&& getSelectedRepPK() != null) {
				if (!CheckResultBO_Client.isAloneIDInput(strAloneID,
						new String[] { getSelectedRepPK() }))
					strAloneID = null;
			}
		} else if (getDataVer() >= 1000)
			bKeyValid = false;

		openEditWin(mainBoard,strAloneID, pubData, strTaskPK, strRepPK, bKeyValid,
				strVerItemPK, strVerSubItemPK, null, false);
	}

	/**
	 * 打开报表数据的第二级方法，此方法在关键字面板的查询按扭事件中调用
	 * 
	 * @param bInit
	 *            ，是否是在系统初始化时调用该方法
	 * @param strAloneID
	 *            ，报表数据的AloneID，如果该值为空，表示报表数据不用打开
	 * @param pubData
	 *            ，关键字条件，关键字面板根据该值生成
	 * @param strTaskPK
	 *            ，当前任务PK
	 * @param strRepPK
	 *            ，当前报表PK
	 * @param bKeyValid
	 *            ，关键字值是否有效
	 * @param strVerItemPK
	 *            ，综合查询主版本PK
	 * @param strVerSubItemPK
	 *            ，综合查询子版本PK
	 * @return
	 */
	public void openEditWin(final Mainboard mainBoard,final String strAloneID,
			final MeasurePubDataVO pubData, String strTaskPK, String strRepPK,
			boolean bKeyValid, String strVerItemPK, String strVerSubItemPK,
			IPostRepDataEditorActive postActive, boolean bInit) {
		// 判断是否可以重用已打开的窗口ID
		String strExistEditorID = null;
		for (String strEditorID : m_hashRepDataCond.keySet()) {
			RepDataEditor editor = (RepDataEditor) mainBoard.getView(
					strEditorID);
			if (editor == null)
				continue;

			// 如果关键字条件不确定，判断是否有同样的已经打开的，同样的关键字组合的，没有关键字确定条件的窗口
			if (editor.getAloneID() == null) {
				if (strAloneID == null
						&& (editor.getPubData() == null || pubData.getKType()
								.equals(editor.getPubData().getKType()))) {
					strExistEditorID = strEditorID;
					break;
				}
			} else if (editor.getAloneID().equals(strAloneID)
					&& editor.getRepPK().equals(strRepPK)) {
				// 找到已经打开的窗口
				strExistEditorID = strEditorID;
				break;
			}
		}

		// 如果第一步未找到可重用的窗口ID，则取任何一个没有确定关键字条件的窗口
		if (strExistEditorID == null) {
			for (String strEditorID : m_hashRepDataCond.keySet()) {
				RepDataEditor editor = (RepDataEditor) mainBoard.getView(
						strEditorID);
				if (editor == null)
					continue;

				// 取任何一个没有确定关键字条件的窗口
				if (editor.getAloneID() == null) {
					strExistEditorID = strEditorID;
					break;
				}
			}
		}

		// 记录系统中最后一次查询的主版本与子版本
		if (strVerItemPK != null)
			m_strVerItemPK = strVerItemPK;
		if (strVerSubItemPK != null)
			m_strVerSubItemPK = strVerSubItemPK;

		String strEditorID = strExistEditorID;
		// 如果未找到可以重用的ID
		if (strEditorID == null) {
			// 系统初始化时，用初始化面板ID
			if (!m_bRepHadOpened)
				strEditorID = EDIT_DATA_ID;
			// 如果报表数据对应AloneID或ReportPK不存在，用随机生成的ID
			else if (UfoPublic.stringIsNull(strAloneID)
					|| UfoPublic.stringIsNull(strRepPK))
				strEditorID = EDIT_DATA_ID + "_" + IDMaker.makeID(20);
			// 根据AloneID与ReportPK生成ID
			else
				strEditorID = EDIT_DATA_ID + "_$" + strAloneID + "_#"
						+ strRepPK;
		}

		// 记下面板条件VO
		m_hashRepDataCond.put(strEditorID, new RepDataCondVO(strAloneID,
				pubData, strRepPK, strTaskPK, bKeyValid, postActive));

		// 打开报表数据面板
		final String strOpenEditorID = strEditorID;

		if (bInit)
			innerOpenEditWin(mainBoard,strOpenEditorID, strAloneID, pubData);
		else
			((KStatusBar) mainBoard.getStatusBar()).processDisplay(
					StringResource.getStringResource("miufoweb0014"),
					new Runnable() {
						public void run() {
							innerOpenEditWin(mainBoard,strOpenEditorID, strAloneID,
									pubData);
						}
					});
	}

	private void innerOpenEditWin(Mainboard mainBoard,String strOpenEditorID, String strAloneID,
			MeasurePubDataVO pubData) {
		RepDataEditor editor = (RepDataEditor) mainBoard.getView(
				strOpenEditorID);
		if (editor == null)
			editor = (RepDataEditor) mainBoard.openView(
					RepDataEditor.class.getName(), strOpenEditorID);
		else {
			RepDataEditor lastEditor = getLastActiveRepDataEditor();
			editor = (RepDataEditor) mainBoard.openView(
					RepDataEditor.class.getName(), strOpenEditorID);
			editor.reInit();
			editor.afterEditorActive();

			// 如果使用的是以前活跃的面板，不会走到RepDataActiveListner，需要手工设置单位、报表树的选 中节点
			if (editor == lastEditor) {
				if (mainBoard.getView(RepDataControler.NAV_UNIT_TREE_ID) != null
						&& editor.getPubData() != null
						&& editor.getPubData().getUnitPK() != null) {
					setSelectedUnitPK(editor.getPubData().getUnitPK());
					((NavUnitTreeViewer) mainBoard.getView(
							RepDataControler.NAV_UNIT_TREE_ID))
							.selectUnitTreeNode();
				}

				if (mainBoard.getView(RepDataControler.NAV_REP_TREE_ID) != null
						&& editor.getRepPK() != null
						&& editor.getTaskPK() != null) {
					setSelectedRepPK(editor.getRepPK());
					setSelectedTaskPK(editor.getTaskPK());
					((NavRepTreeViewer) mainBoard.getView(
							RepDataControler.NAV_REP_TREE_ID))
							.selectRepTreeNode();
				}
			}
		}

		// 如果AloneID不为空，置报表数据被有效打开过为TRUE
		if (strAloneID != null) {
			m_hashAloneID.put(pubData.getKType(), strAloneID);
			m_bRepHadOpened = true;
		}

		// 刷新审核结果面板内容
		doRefreshCheckResultPane(mainBoard);
	}

	/**
	 * 激活另一个报表数据窗口，此方法是避免激活汇总结果溯源等非报表数据窗口
	 * 
	 * @param oldEditor
	 */
	public void activeOtherRepDataEditor(Mainboard mainBoard,RepDataEditor oldEditor) {
		if (getLastActiveRepDataEditor() != oldEditor)
			return;

		for (String strID : m_hashRepDataCond.keySet()) {
			RepDataEditor editor = (RepDataEditor) mainBoard.getView(strID);
			if (editor != null) {
				setLastActiveRepDataEditor(editor);
				mainBoard.openView(RepDataEditor.class.getName(), strID);
				return;
			}
		}
		setLastActiveRepDataEditor(null);

		// 刷新审核结果面板，置审核结果列表内容为空
		doRefreshCheckResultPane(mainBoard);
	}

	/**
	 * 清除一个报表数据页签对应的条件VO
	 * 
	 * @param strID
	 */
	public void removeOneEditorID(String strID) {
		m_hashRepDataCond.remove(strID);
	}

	/**
	 * 打开汇总结果溯源或查看汇总下级窗口
	 * 
	 * @param cellsModel
	 * @param bTotalSub
	 * @i18n miufohbbb00123=查看汇总下级数据
	 * @i18n miufohbbb00124=查看汇总来源数据
	 */
	public void showTotalSourceView(Mainboard mainBoard,CellsModel cellsModel, boolean bTotalSub) {
		TotalSourceEditor editor = (TotalSourceEditor) mainBoard.openView(
				TotalSourceEditor.class.getName(), TOTAL_SOURCE_ID,
				EDIT_DATA_ID);
		editor.setCellsModel(cellsModel);
		editor.getContext().setAttribute(IUfoContextKey.OPERATION_STATE,
				IUfoContextKey.OPERATION_INPUT);
		editor.setTitle(bTotalSub ? StringResource
				.getStringResource("miufohbbb00123") : StringResource
				.getStringResource("miufohbbb00124"));
	}

	/**
	 * 刷新审核结果面板内容
	 */
	public void doRefreshCheckResultPane(Mainboard mainBoard) {
		CheckResultViewer view = (CheckResultViewer) mainBoard.getView(
				CHECK_RESULT_ID);
		if (view != null)
			view.changeRepDataEditor();
	}

	/**
	 * 取得登录环境VO
	 * 
	 * @return
	 */
	public LoginEnvVO getLoginEnv(Mainboard mainBoard) {
		return (LoginEnvVO) mainBoard.getContext().getAttribute(
				IUfoContextKey.LOGIN_ENV);
	}

	/**
	 * 得到自动计算对象
	 * 
	 * @return
	 */
	public synchronized ReportCalUtil getReportCalUtil() {
		RepDataEditor editor = getLastActiveRepDataEditor();
		String strRepPK = editor.getRepPK();
		String strAloneID = editor.getAloneID();

		// 如果自动计算对象不存在，或者ReportPK、AloneID与以前不相同，则重新生成该对象
		if (m_reportCalUtil == null
				|| (!UfoPublic.strIsEqual(strRepPK, m_reportCalUtil
						.getReportPK()) || !UfoPublic.strIsEqual(strAloneID,
						m_reportCalUtil.getAloneID()))) {
			Context context = new Context();
			context.setAttribute(IUfoContextKey.CLIENT_FLAG, Boolean.TRUE);
			m_reportCalUtil = new ReportCalUtil(strRepPK, editor
					.getCellsModel(), context);
			m_reportCalUtil.setAloneID(strAloneID);
		}

		return m_reportCalUtil;
	}

	/**
	 * 重置报表自动计算对象为空
	 */
	public synchronized void reSetReportCalUtil() {
		m_reportCalUtil = null;
	}

	public synchronized ReportCalUtil getReportCalUtilWithoutCreate() {
		return m_reportCalUtil;
	}

	/**
	 * 清除一个报表数据窗口对应的表间审核定位结果
	 * 
	 * @param editor
	 */
	public void clearTaskCheckResult(RepDataEditor editor) {
		if (editor.getAloneID() == null)
			return;

		// 保证该审核结果对应的所有报表数据窗口都关闭，才删除该审核结果
		String strKey = editor.getTaskCheckKey();
		for (String strID : m_hashRepDataCond.keySet()) {
			RepDataEditor oneEditor = (RepDataEditor) editor.getMainboard().getView(
					strID);
			if (oneEditor == null)
				continue;

			if (strKey.equals(oneEditor.getTaskCheckKey()))
				return;
		}

		m_hashCheckCells.remove(strKey);
		m_hashCheckDetail.remove(strKey);
		m_hashCheckResult.remove(strKey);
		m_hashCheckColor.remove(strKey);
	}

	/**
	 * 清除一个报表数据窗口对应的表间审核定位结果
	 * 
	 * @param editor
	 */
	public void clearTaskCheckDetail(RepDataEditor editor) {
		if (editor.getAloneID() == null)
			return;

		String strKey = editor.getTaskCheckKey();
		m_hashCheckCells.remove(strKey);
		m_hashCheckDetail.remove(strKey);
		m_hashCheckColor.remove(strKey);
	}

	/**
	 * 增加一个报表数据窗口做表间审核得到的审核结果
	 * 
	 * @param editor
	 * @param result
	 */
	public void addTaskCheckResult(RepDataEditor editor, CheckResultVO result) {
		String strKey = editor.getTaskCheckKey();
		m_hashCheckResult.remove(strKey);
		m_hashCheckCells.remove(strKey);
		m_hashCheckDetail.remove(strKey);
		m_hashCheckColor.remove(strKey);
		if (result != null)
			m_hashCheckResult.put(strKey, result);
		openCheckResultView(editor.getMainboard(),result);
	}

	/**
	 * 设置报表数据窗口对应的表间审核定位到的审核结果明细
	 * 
	 * @param editor
	 * @param detail
	 */
	public void setTaskCheckDetail(RepDataEditor editor, CheckDetailVO detail) {
		String strKey = editor.getTaskCheckKey();
		if (m_hashCheckResult.get(strKey) == null)
			return;

		m_hashCheckDetail.remove(strKey);
		if (detail != null)
			m_hashCheckDetail.put(strKey, detail);
	}

	/**
	 * 设置报表数据窗口对应的表间审核定位到的单元格数组
	 * 
	 * @param editor
	 * @param cells
	 */
	public void setTaskCheckCell(RepDataEditor editor, List<CellPosition> cells) {
		String strKey = editor.getTaskCheckKey();
		if (m_hashCheckResult.get(strKey) == null)
			return;

		m_hashCheckCells.remove(strKey);
		if (cells != null)
			m_hashCheckCells.put(strKey, cells);
	}

	/**
	 * 设置报表数据窗口对应的表间审核定位到的单元格背景色
	 * 
	 * @param editor
	 * @param color
	 */
	public void setTaskCheckColor(RepDataEditor editor, Color color) {
		String strKey = editor.getTaskCheckKey();
		if (m_hashCheckResult.get(strKey) == null)
			return;

		m_hashCheckColor.remove(strKey);
		if (color != null)
			m_hashCheckColor.put(strKey, color);
	}

	/**
	 * 得到当前报表数据窗口对应的表间审核结果的定位颜色
	 * 
	 * @param editor
	 * @return
	 */
	public Color getTaskCheckColor(RepDataEditor editor) {
		return m_hashCheckColor.get(editor.getTaskCheckKey());
	}

	/**
	 * 得到当前报表数据窗口对应的表间审核结果的定位到的单元格
	 * 
	 * @param editor
	 * @return
	 */
	public List<CellPosition> getTaskCheckCells(RepDataEditor editor) {
		return m_hashCheckCells.get(editor.getTaskCheckKey());
	}

	/**
	 * 得到当前报表数据窗口对应的表间审核结果
	 * 
	 * @param editor
	 * @return
	 */
	public CheckResultVO getTaskCheckResult(RepDataEditor editor) {
		return m_hashCheckResult.get(editor.getTaskCheckKey());
	}

	/**
	 * 得到当前报表数据窗口对应的表间审核定位结果
	 * 
	 * @param editor
	 * @return
	 */
	public CheckDetailVO getTaskCheckDetail(RepDataEditor editor) {
		return m_hashCheckDetail.get(editor.getTaskCheckKey());
	}

	/**
	 * 打开审核结果面板
	 * 
	 * @param result
	 */
	public void openCheckResultView(Mainboard mainBoard,CheckResultVO result) {
		// 如果有审核提示信息，则打开审核结果面板
		if (result != null && result.getDetailVO() != null
				&& result.getDetailVO().length > 0) {
			mainBoard.openView(CheckResultViewer.class.getName(),
					CHECK_RESULT_ID);
		}

		// 审核结果面板内容刷新
		doRefreshCheckResultPane(mainBoard);
	}

	/**
	 * 改变选中的单位时，根据报表数据权限刷新任务报表树范围
	 */
	public void refreshNavRepTree(Mainboard mainBoard) {
		NavRepTreeViewer view = (NavRepTreeViewer) mainBoard.getView(
				NAV_REP_TREE_ID);
		if (view != null)
			view.refresh();
	}

	public RepDataCondVO getRepDataCond(String strID) {
		if (strID == null)
			return null;
		return m_hashRepDataCond.get(strID);
	}

	/**
	 * 设置最近一次录入的关键字值
	 * 
	 * @param strKeyPKs
	 * @param strKeyVals
	 */
	public void setKeyVals(String[] strKeyPKs, String[] strKeyVals) {
		if (strKeyPKs == null || strKeyPKs.length <= 0)
			return;

		for (int i = 0; i < strKeyPKs.length; i++) {
			// 对于时间关键字，还要向COMM_TIME_KEY中置值
			if (strKeyPKs[i].compareTo(KeyVO.YEAR_PK) >= 0
					&& strKeyPKs[i].compareTo(KeyVO.DAY_PK) <= 0) {
				m_hashKeyVal.put(strKeyPKs[i], strKeyVals[i]);
				m_hashKeyVal.put(COMM_TIME_KEY, strKeyVals[i]);
			} else
				m_hashKeyVal.put(strKeyPKs[i], strKeyVals[i]);
		}
	}

	public boolean isHBBBData(Mainboard mainBoard) {
		return "true".equalsIgnoreCase((String) mainBoard.getContext()
				.getAttribute(IUfoContextKey.IS_HBBBDATA));
	}

	public boolean isGeneralQuery(Mainboard mainBoard) {
		return "true".equalsIgnoreCase((String) mainBoard.getContext()
				.getAttribute(IUfoContextKey.GENRAL_QUERY));
	}

	public UserInfoVO getCurUserInfo(Mainboard mainBoard) {
		return (UserInfoVO) mainBoard.getContext().getAttribute(
				IUfoContextKey.CUR_USER_INFO);
	}

	public String getSelectedRepPK() {
		return m_strSelectedRepPK;
	}

	public boolean isRepValidSelect() {
		return m_bRepValidSelect;
	}

	public void setRepValidSelect(boolean bRepValidSelect) {
		m_bRepValidSelect = bRepValidSelect;
	}

	public String getSelectedUnitPK() {
		return m_strSelectedUnitPK;
	}

	public void setSelectedUnitPK(String selectedUnitPK) {
		m_strSelectedUnitPK = selectedUnitPK;
	}

	public RepDataEditor getLastActiveRepDataEditor() {
		return m_lastActiveRepDataEditor;
	}

	public void setLastActiveRepDataEditor(RepDataEditor editor) {
		m_lastActiveRepDataEditor = editor;

		// 将活跃窗口的公式是否可以录入放到MeasureFmt中
		if (m_lastActiveRepDataEditor != null)
			MeasureFmt.setCanInput(m_lastActiveRepDataEditor.isCanFormInput());
	}

	public String getSelectedTaskPK() {
		return m_strSelectedTaskPK;
	}

	public void setSelectedTaskPK(String selectedTaskPK) {
		m_strSelectedTaskPK = selectedTaskPK;
	}

	public void setSelectedRepPK(String selectedRepPK) {
		m_strSelectedRepPK = selectedRepPK;
	}

	public boolean isCanFormulaTrace() {
		return bCanFormulaTrace;
	}

	public void setCanFormulaTrace(boolean canFormulaTrace) {
		bCanFormulaTrace = canFormulaTrace;
	}

	public int getDataVer() {
		return m_iDataVer;
	}

	public void setDataVer(int dataVer) {
		m_iDataVer = dataVer;
	}

	public boolean isShowSealedTask() {
		return m_bShowSealedTask;
	}

	public void setShowSealedTask(boolean showSealedTask) {
		m_bShowSealedTask = showSealedTask;
	}

	public boolean isRepHadOpened() {
		return m_bRepHadOpened;
	}

	public int getLastInputDir() {
		return m_iInputDir;
	}

	public void setLastInputDir(int inputDir) {
		m_iInputDir = inputDir;
	}

	private void initLoginEnv(Mainboard mainBoard) {
		LoginEnvVO loginEnv = new LoginEnvVO();
		IContext context = mainBoard.getContext();
		loginEnv.setCurLoginDate((String) context
				.getAttribute(IUfoContextKey.LOGIN_DATE));
		loginEnv.setLangCode((String) context
				.getAttribute(IUfoContextKey.CURRENT_LANG));
		loginEnv.setLoginUnit(getCurUserInfo(mainBoard).getUnitId());
		mainBoard.getContext().setAttribute(IUfoContextKey.LOGIN_ENV,
				loginEnv);
	}

	private boolean isEmptyStr(String strVal) {
		return strVal == null || strVal.trim().length() <= 0
				|| strVal.equalsIgnoreCase("null");
	}

	private String[] adjustKeyVals(MeasurePubDataVO pubData) {
		UnitCache unitCache = IUFOUICacheManager.getSingleton().getUnitCache();
		String[] strKeyVals = pubData.getKeywords();
		if (strKeyVals != null) {
			KeyVO[] keys = pubData.getKeyGroup().getKeys();
			for (int i = 0; i < keys.length; i++) {
				if (KeyVO.isUnitKeyVO(keys[i]) || KeyVO.isDicUnitKeyVO(keys[i])) {
					UnitInfoVO unitInfo = unitCache
							.getUnitInfoByPK(strKeyVals[i]);
					if (unitInfo != null)
						strKeyVals[i] = unitInfo.getCode();
				}
			}
		}

		return strKeyVals;
	}

	private boolean doCheckTimeValid(TaskVO taskVO, String strKeyVal, KeyVO key) {
		try {
			if (key.isTimeKeyVO()) {
				if (taskVO.getStartTime() != null
						&& !taskVO.getStartTime().equals("")) {
					UFODate inputUfoDate = new UFODate(strKeyVal.toString());
					UFODate startUfoDate = new UFODate(taskVO.getStartTime());
					startUfoDate = startUfoDate
							.getEndDay(key.getTimeProperty());
					if (inputUfoDate.compareTo(startUfoDate) < 0) {
						return false;
					}
				}
				if (taskVO.getEndTime() != null
						&& !taskVO.getEndTime().equals("")) {
					UFODate inputUfoDate = new UFODate(strKeyVal.toString());
					UFODate endUfoDate = new UFODate(taskVO.getEndTime());
					endUfoDate = endUfoDate.getEndDay(key.getTimeProperty());
					if (endUfoDate.compareTo(inputUfoDate) < 0) {
						return false;
					}
				}
			} else {
				String strAccSchemePK = taskVO.getAccPeriodScheme();
				if (AccPeriodSchemeUtil.getInstance().isAccPeriodValid(
						strAccSchemePK, key.getKeywordPK(), strKeyVal) == false)
					return false;

				// 对任务的有效期限作判断
				if (taskVO.getStartTime() != null
						&& !taskVO.getStartTime().equals("")) {
					String strStartDate = AccPeriodSchemeUtil.getInstance()
							.getAccPeriodByNatDate(taskVO.getAccPeriodScheme(),
									key.getKeywordPK(), taskVO.getStartTime());
					if (strStartDate != null && strStartDate.length() > 0
							&& strStartDate.compareTo(strKeyVal) > 0)
						return false;
				}
				if (taskVO.getEndTime() != null
						&& !taskVO.getEndTime().equals("")) {
					String strEndtDate = AccPeriodSchemeUtil.getInstance()
							.getAccPeriodByNatDate(taskVO.getAccPeriodScheme(),
									key.getKeywordPK(), taskVO.getEndTime());
					if (strEndtDate != null && strEndtDate.length() > 0
							&& strEndtDate.compareTo(strKeyVal) < 0)
						return false;
				}
			}
		} catch (Exception e) {
			AppDebug.debug(e);
			return false;
		}

		return true;
	}
	
	public static void removeFromContext(IContext context){
		if (context!=null)
			context.removeAttribute(REP_DATA_CONTROLER_KEY);
	}
	
	public boolean isRepEditorAllRemoved(){
		return m_hashRepDataCond.size()<=0;
	}
}
