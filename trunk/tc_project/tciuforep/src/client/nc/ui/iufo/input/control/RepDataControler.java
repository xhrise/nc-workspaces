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
	
	// ��λ������ID
	transient public static String NAV_UNIT_TREE_ID = "iufo.input.dir_unit.view";

	// ����������ID
	transient public static String NAV_REP_TREE_ID = "iufo.input.dir_rep.view";

	// ��һ����������ҳǩ��ID
	transient public static String EDIT_DATA_ID = "iufo.input.data.view";

	// ��˽��ҳ���ID
	transient public static String CHECK_RESULT_ID = "iufo.input.checkresult.view";

	// ��ʽ׷�����ID
	transient public static final String FORMULA_TRACERESULT_ID = "iufo.input.formresult.view";

	// ���ܽ����Դ��鿴�����¼�����ID��ϵͳ�б�֤�������ֻ��һ��
	transient public static String TOTAL_SOURCE_ID = "iufo.input.totalsource.view";

	// ����ʱ��ؼ��ֹ��õĹؼ���Keyֵ����ͬʱ��ؼ�����m_hashKeyVal���õ���ͬһ��Keyֵ
	transient private static final String COMM_TIME_KEY = "commtimekey";

	// �Ƿ񱨱����ݱ���Ч�򿪹�
	transient private boolean m_bRepHadOpened = false;

	// �������ݴ���ID�뱨��¼�������Ĺ�ϣ��
	transient private Map<String, RepDataCondVO> m_hashRepDataCond = new Hashtable<String, RepDataCondVO>();

	// ���ؼ������¼��Ĺؼ���ֵ
	transient private Map<String, String> m_hashKeyVal = new Hashtable<String, String>();

	// �ؼ������PK��AloneID�Ĺ�ϣ��,�򿪱�ʱ��ʹ�øùؼ���������һ�εĹؼ����������򿪱�
	transient private Map<String, String> m_hashAloneID = new Hashtable<String, String>();

	// ����򿪵ı������ݴ���
	transient private RepDataEditor m_lastActiveRepDataEditor = null;

	// ���ѡ��ı���PK
	transient private String m_strSelectedRepPK = null;

	// ���ѡ�������PK
	transient private String m_strSelectedTaskPK = null;

	// ���ѡ��ĵ�λPK
	transient private String m_strSelectedUnitPK = null;

	// ��������¼��״̬�������ݵİ汾
	transient private int m_iDataVer = 0;

	// ����򿪵��ۺϲ�ѯ�������ݰ汾PK
	transient private String m_strVerItemPK = null;

	// ����򿪵ĸ�����ѯ�ı��������Ӱ汾PK
	transient private String m_strVerSubItemPK = null;

	// ���ѡ��ı���PK�Ƿ�Ϸ�
	transient private boolean m_bRepValidSelect = false;

	// �Ƿ���ʾ��������
	transient private boolean m_bShowSealedTask = false;

	// ϵͳ�����еı����˽������ֵΪ#����PK$AloneID
	transient private Map<String, CheckResultVO> m_hashCheckResult = new Hashtable<String, CheckResultVO>();

	// �û�ѡ��׷�ٵ���������˽������ֵͬ��
	transient private Map<String, CheckDetailVO> m_hashCheckDetail = new Hashtable<String, CheckDetailVO>();

	// ��˵�Ԫ����Ҫ��ע����ɫ
	transient private Map<String, Color> m_hashCheckColor = new Hashtable<String, Color>();

	// ��˽��׷�ٶ�λ���ĵ�Ԫ���б�ֻ��Ӧһ���������ݴ���
	transient private Map<String, List<CellPosition>> m_hashCheckCells = new Hashtable<String, List<CellPosition>>();

	// ������㹫ʽ��
	transient private ReportCalUtil m_reportCalUtil = null;

	// ��ʽ׷�ٲ˵��Ƿ�ѡ��
	transient private boolean bCanFormulaTrace = false;

	// ���һ��ѡ���¼�뷽��
	transient private int m_iInputDir = InputDirConstant.DIR_RIGHT;

	static {
		RefData.setProxy(new IufoRefData());
	}
	
	public Object clone(){
		return this;
	}

	private RepDataControler(Mainboard mainBoard) {
		// ע�ᴰ�ڼ��������
		mainBoard.getEventManager().addListener(
				new RepDataEditorActiveListener());

		// ��������context��ע�ᵱǰ�û�����¼������Ϣ
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

		// �����Ƿ���ʾ0�Ĳ���
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

		// ȡ�û�ѡ��򿪵ı���
		String strReportPK = (String) mainBoard.getContext().getAttribute(
				IUfoContextKey.REPORT_PK);
		if ("repid".equalsIgnoreCase(strReportPK))
			strReportPK = null;

		// ����Ĭ��ѡ�е����񡢱�����λ
		m_strSelectedTaskPK = (String) mainBoard.getContext().getAttribute(
				IUfoContextKey.TASK_PK);
		;
		m_strSelectedRepPK = strReportPK;
		m_strSelectedUnitPK = userInfo.getUnitId();

		// �õ�¼��״̬�±������ݵİ汾
		setDataVer(isHBBBData(mainBoard) ? HBBBSysParaUtil.VER_HBBB : 0);
	}

	static void initRepDataControler(Mainboard mainBoard) {
		// ʵ��������
		RepDataControler repDataControler = new RepDataControler(mainBoard);
		mainBoard.getContext().setAttribute(REP_DATA_CONTROLER_KEY, repDataControler);

		// ����Ĭ�ϵĴ򿪱������
		String strAloneID = (String) mainBoard.getContext().getAttribute(
				IUfoContextKey.ALONE_ID);

		if (strAloneID != null) {
			try {
				MeasurePubDataVO pubData = MeasurePubDataBO_Client
						.findByAloneID(strAloneID);
				repDataControler.setDataVer(pubData.getVer());

				// �����ǰ�����е�λ�ؼ��֣����޸ĵ�ǰѡ�еĵ�λ
				if (!UfoPublic.stringIsNull(pubData.getUnitPK()))
					repDataControler.m_strSelectedUnitPK = pubData.getUnitPK();

				// ��¼���ؼ������¼���ֵ
				KeyVO[] keys = pubData.getKeyGroup().getKeys();
				for (int i = 0; i < keys.length; i++)
					repDataControler.setKeyVals(new String[] { keys[i]
							.getKeywordPK() }, new String[] { pubData
							.getKeywordByPK(keys[i].getKeywordPK()) });

				// ���ѡ���˱�������Ҫ�򿪱�������
				if (repDataControler.m_strSelectedRepPK != null)
					repDataControler.openEditWin(mainBoard,strAloneID, pubData,
							repDataControler.m_strSelectedTaskPK,
							repDataControler.m_strSelectedRepPK, true, null,
							null, null, true);
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}

		// �˷�֧��Ӧ�ޱ������ݴ򿪵Ĳ�����һ�������AloneIDδȷ������һ������ǰ������ѯʱ��AloneIDȷ��������ȷ��
		if (strAloneID == null || repDataControler.m_strSelectedRepPK == null) {
			try {
				// ��λӦ��Ĭ��ѡ�е����񼰱���
				String[] strRetVals = (String[]) ActionHandler.exec(
						"nc.ui.iufo.input.RepDataActionHandler",
						"loadFirstRepPK", new Object[] {
								repDataControler.getCurUserInfo(mainBoard),
								repDataControler.m_strSelectedTaskPK,
								new Boolean(repDataControler.isHBBBData(mainBoard)) });

				if (strRetVals != null && strRetVals.length >= 2) {
					// �������õ�ǰѡ�е����񱨱�
					repDataControler.m_strSelectedTaskPK = strRetVals[0];
					repDataControler.m_bRepValidSelect = true;
					repDataControler.m_strSelectedRepPK = strRetVals[1];

					// ��һ���յı���������壬��ʼ���ؼ������
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
	 * �򿪱������ݴ��ڣ��˷�����ϵͳ��ʼ�����ӵ�λ������˫�����ڵ�ʱ����
	 * 
	 * @param bInit
	 *            ���Ƿ���ϵͳ��ʼ��ʱ����Ĭ�ϴ��ڣ�ֻ����RepDataControler.initRepDataControler�����е���ʱ
	 *            ���˲���ֵ��Ϊtrue
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

		// ����ȡ���һ�λ�Ծ���ڵĹؼ���ֵ
		String strAloneID = null;
		if (m_lastActiveRepDataEditor != null)
			strAloneID = m_lastActiveRepDataEditor.getAloneID();

		// ���ȡ�������һ�λ�Ծ���ڵĹؼ���ֵ��ȡ�ùؼ���������һ�δ򿪵Ĺؼ���ֵ
		if (strAloneID == null)
			strAloneID = m_hashAloneID.get(report.getKeyCombPK());

		// ����Ҫ�򿪱������ݵ�MeasurePubDataVO
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

			// ��λ�ؼ���ȡ��ǰѡ�еĵ�λ
			if (key.getKeywordPK().equals(KeyVO.CORP_PK)) {
				pubData.setKeyword1(m_strSelectedUnitPK);
				continue;
			}

			// ����ȡ��ǰAloneID��Ӧ�Ĺؼ���ֵ
			String strKeyVal = null;
			if (key.getTimeKeyIndex() >= 0) {
				strKeyVal = m_hashKeyVal.get(key.getKeywordPK());
				if (strKeyVal == null)
					strKeyVal = m_hashKeyVal.get(COMM_TIME_KEY);
			} else
				strKeyVal = m_hashKeyVal.get(key.getKeywordPK());

			if (isEmptyStr(strKeyVal) && oldPubData != null)
				strKeyVal = oldPubData.getKeywordByPK(key.getKeywordPK());

			// �ۺϲ�ѯ��ʱ��ؼ��֣�ȡ��¼ʱ��
			if (isEmptyStr(strKeyVal) && key.isTTimeKeyVO() && isGeneralQuery(mainBoard)) {
				strKeyVal = (String) mainBoard.getContext().getAttribute(
						IUfoContextKey.LOGIN_DATE);
				// ��¼ʱ��ת���ɻ���ڼ�
				if (key.isAccPeriodKey()) {
					strKeyVal = AccPeriodSchemeUtil.getInstance()
							.getAccPeriodByNatDate(pubData.getAccSchemePK(),
									key.getKeywordPK(), strKeyVal);
				}
			}

			// ��ȡ����Ĭ��ֵ
			if (isEmptyStr(strKeyVal) && taskDefault != null) {
				strKeyVal = taskDefault.getKeywordValueByIndex(iDefaultIndex);
			}

			// �жϹؼ���ֵ�Ƿ�Ϊ�ռ�ʱ��ؼ����Ƿ�Ϸ�
			if (isEmptyStr(strKeyVal)
					|| (key.isTTimeKeyVO() && doCheckTimeValid(task, strKeyVal,
							key) == false))
				bKeyValValid = false;
			else
				pubData.setKeywordByPK(key.getKeywordPK(), strKeyVal);
		}

		// ���ð汾�ţ����ۺϲ�ѯ��Ҫ������
		pubData.setVer(getDataVer());
		if (isHBBBData(mainBoard) && Util.isHBRepDataRelatingWithTask())
			pubData.setFormulaID(task.getId());

		// �򿪱������ݵ������Ӱ汾PK�������ۺϲ�ѯ
		String strVerItemPK = null;
		String strSubVerItemPK = null;
		strAloneID = null;

		// �ؼ���ֵ���Ϸ�,�򱨱����ݲ��򿪣��ؼ������δ��Ч��ʼ��
		String strOrgPK = (String) mainBoard.getContext().getAttribute(
				IUfoContextKey.ORG_PK);
		if (bKeyValValid == false) {
			strAloneID = null;
		}
		// ���ۺϲ�ѯ���򰴹ؼ�����������AloneID,�򿪱������ݼ���ʼ���ؼ������
		else if (!isGeneralQuery(mainBoard)) {
			// ֻ�б������ݱ���Ч�򿪹�ʱ���Ų���AloneID,�򿪱�������
			if (m_bRepHadOpened == true) {
				strAloneID = MeasurePubDataBO_Client.getAloneID(pubData);
				pubData.setAloneID(strAloneID);

				// ������λ���ݣ�Ҫ�жϱ��������Ƿ�¼�룬���δ¼�� ����AloneIDΪ�գ���RepDataEditor�������������ʾ
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
		// �ۺϲ�ѯ����Ҫ�ҵ�һ����ӽ�����򿪵����ݵ�AloneID
		else {
			try {
				// ������¼���������汾
				String[] strKeyVals = adjustKeyVals(pubData);
				VerItem[] vers = (VerItem[]) ActionHandler.exec(
						"nc.ui.iufo.input.RepDataActionHandler",
						"loadDataVersByCond", new Object[] {
								getCurUserInfo(mainBoard).getID(), m_strSelectedTaskPK,
								m_strSelectedRepPK, strKeyVals, strOrgPK });

				if (vers != null && vers.length > 0) {
					// ����¼�����ݵ����汾�У��ҵ�ϵͳ����鿴�����汾
					strVerItemPK = vers[0].getVerPK();
					if (m_strVerItemPK != null) {
						for (int i = 0; i < vers.length; i++) {
							if (vers[i].getVerPK().equals(m_strVerItemPK)) {
								strVerItemPK = m_strVerItemPK;
								break;
							}
						}
					}

					// ������¼�����ݵ��Ӱ汾
					VerItem[] subVers = (VerItem[]) ActionHandler.exec(
							"nc.ui.iufo.input.RepDataActionHandler",
							"loadDataSubVersByCond", new Object[] {
									getCurUserInfo(mainBoard).getID(),
									m_strSelectedTaskPK, m_strSelectedRepPK,
									strKeyVals, strVerItemPK, strOrgPK });

					// ����¼�����ݵ��Ӱ汾�У��ҵ�ϵͳ����鿴���Ӱ汾
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
				// ��������δ��Ч�򿪹�ʱ�����ܴ򿪱������ݣ����Դ˴���AloneIDΪ�գ���ؼ���ֵ��Ϊ��Ч�����ⵯ��������ʾ
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
			// ������λ���ݣ�Ҫ�жϱ��������Ƿ�¼�룬���δ¼�� ����AloneIDΪ�գ���RepDataEditor�������������ʾ
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
	 * �򿪱������ݵĵڶ����������˷����ڹؼ������Ĳ�ѯ��Ť�¼��е���
	 * 
	 * @param bInit
	 *            ���Ƿ�����ϵͳ��ʼ��ʱ���ø÷���
	 * @param strAloneID
	 *            ���������ݵ�AloneID�������ֵΪ�գ���ʾ�������ݲ��ô�
	 * @param pubData
	 *            ���ؼ����������ؼ��������ݸ�ֵ����
	 * @param strTaskPK
	 *            ����ǰ����PK
	 * @param strRepPK
	 *            ����ǰ����PK
	 * @param bKeyValid
	 *            ���ؼ���ֵ�Ƿ���Ч
	 * @param strVerItemPK
	 *            ���ۺϲ�ѯ���汾PK
	 * @param strVerSubItemPK
	 *            ���ۺϲ�ѯ�Ӱ汾PK
	 * @return
	 */
	public void openEditWin(final Mainboard mainBoard,final String strAloneID,
			final MeasurePubDataVO pubData, String strTaskPK, String strRepPK,
			boolean bKeyValid, String strVerItemPK, String strVerSubItemPK,
			IPostRepDataEditorActive postActive, boolean bInit) {
		// �ж��Ƿ���������Ѵ򿪵Ĵ���ID
		String strExistEditorID = null;
		for (String strEditorID : m_hashRepDataCond.keySet()) {
			RepDataEditor editor = (RepDataEditor) mainBoard.getView(
					strEditorID);
			if (editor == null)
				continue;

			// ����ؼ���������ȷ�����ж��Ƿ���ͬ�����Ѿ��򿪵ģ�ͬ���Ĺؼ�����ϵģ�û�йؼ���ȷ�������Ĵ���
			if (editor.getAloneID() == null) {
				if (strAloneID == null
						&& (editor.getPubData() == null || pubData.getKType()
								.equals(editor.getPubData().getKType()))) {
					strExistEditorID = strEditorID;
					break;
				}
			} else if (editor.getAloneID().equals(strAloneID)
					&& editor.getRepPK().equals(strRepPK)) {
				// �ҵ��Ѿ��򿪵Ĵ���
				strExistEditorID = strEditorID;
				break;
			}
		}

		// �����һ��δ�ҵ������õĴ���ID����ȡ�κ�һ��û��ȷ���ؼ��������Ĵ���
		if (strExistEditorID == null) {
			for (String strEditorID : m_hashRepDataCond.keySet()) {
				RepDataEditor editor = (RepDataEditor) mainBoard.getView(
						strEditorID);
				if (editor == null)
					continue;

				// ȡ�κ�һ��û��ȷ���ؼ��������Ĵ���
				if (editor.getAloneID() == null) {
					strExistEditorID = strEditorID;
					break;
				}
			}
		}

		// ��¼ϵͳ�����һ�β�ѯ�����汾���Ӱ汾
		if (strVerItemPK != null)
			m_strVerItemPK = strVerItemPK;
		if (strVerSubItemPK != null)
			m_strVerSubItemPK = strVerSubItemPK;

		String strEditorID = strExistEditorID;
		// ���δ�ҵ��������õ�ID
		if (strEditorID == null) {
			// ϵͳ��ʼ��ʱ���ó�ʼ�����ID
			if (!m_bRepHadOpened)
				strEditorID = EDIT_DATA_ID;
			// ����������ݶ�ӦAloneID��ReportPK�����ڣ���������ɵ�ID
			else if (UfoPublic.stringIsNull(strAloneID)
					|| UfoPublic.stringIsNull(strRepPK))
				strEditorID = EDIT_DATA_ID + "_" + IDMaker.makeID(20);
			// ����AloneID��ReportPK����ID
			else
				strEditorID = EDIT_DATA_ID + "_$" + strAloneID + "_#"
						+ strRepPK;
		}

		// �����������VO
		m_hashRepDataCond.put(strEditorID, new RepDataCondVO(strAloneID,
				pubData, strRepPK, strTaskPK, bKeyValid, postActive));

		// �򿪱����������
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

			// ���ʹ�õ�����ǰ��Ծ����壬�����ߵ�RepDataActiveListner����Ҫ�ֹ����õ�λ����������ѡ �нڵ�
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

		// ���AloneID��Ϊ�գ��ñ������ݱ���Ч�򿪹�ΪTRUE
		if (strAloneID != null) {
			m_hashAloneID.put(pubData.getKType(), strAloneID);
			m_bRepHadOpened = true;
		}

		// ˢ����˽���������
		doRefreshCheckResultPane(mainBoard);
	}

	/**
	 * ������һ���������ݴ��ڣ��˷����Ǳ��⼤����ܽ����Դ�ȷǱ������ݴ���
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

		// ˢ����˽����壬����˽���б�����Ϊ��
		doRefreshCheckResultPane(mainBoard);
	}

	/**
	 * ���һ����������ҳǩ��Ӧ������VO
	 * 
	 * @param strID
	 */
	public void removeOneEditorID(String strID) {
		m_hashRepDataCond.remove(strID);
	}

	/**
	 * �򿪻��ܽ����Դ��鿴�����¼�����
	 * 
	 * @param cellsModel
	 * @param bTotalSub
	 * @i18n miufohbbb00123=�鿴�����¼�����
	 * @i18n miufohbbb00124=�鿴������Դ����
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
	 * ˢ����˽���������
	 */
	public void doRefreshCheckResultPane(Mainboard mainBoard) {
		CheckResultViewer view = (CheckResultViewer) mainBoard.getView(
				CHECK_RESULT_ID);
		if (view != null)
			view.changeRepDataEditor();
	}

	/**
	 * ȡ�õ�¼����VO
	 * 
	 * @return
	 */
	public LoginEnvVO getLoginEnv(Mainboard mainBoard) {
		return (LoginEnvVO) mainBoard.getContext().getAttribute(
				IUfoContextKey.LOGIN_ENV);
	}

	/**
	 * �õ��Զ��������
	 * 
	 * @return
	 */
	public synchronized ReportCalUtil getReportCalUtil() {
		RepDataEditor editor = getLastActiveRepDataEditor();
		String strRepPK = editor.getRepPK();
		String strAloneID = editor.getAloneID();

		// ����Զ�������󲻴��ڣ�����ReportPK��AloneID����ǰ����ͬ�����������ɸö���
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
	 * ���ñ����Զ��������Ϊ��
	 */
	public synchronized void reSetReportCalUtil() {
		m_reportCalUtil = null;
	}

	public synchronized ReportCalUtil getReportCalUtilWithoutCreate() {
		return m_reportCalUtil;
	}

	/**
	 * ���һ���������ݴ��ڶ�Ӧ�ı����˶�λ���
	 * 
	 * @param editor
	 */
	public void clearTaskCheckResult(RepDataEditor editor) {
		if (editor.getAloneID() == null)
			return;

		// ��֤����˽����Ӧ�����б������ݴ��ڶ��رգ���ɾ������˽��
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
	 * ���һ���������ݴ��ڶ�Ӧ�ı����˶�λ���
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
	 * ����һ���������ݴ����������˵õ�����˽��
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
	 * ���ñ������ݴ��ڶ�Ӧ�ı����˶�λ������˽����ϸ
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
	 * ���ñ������ݴ��ڶ�Ӧ�ı����˶�λ���ĵ�Ԫ������
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
	 * ���ñ������ݴ��ڶ�Ӧ�ı����˶�λ���ĵ�Ԫ�񱳾�ɫ
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
	 * �õ���ǰ�������ݴ��ڶ�Ӧ�ı����˽���Ķ�λ��ɫ
	 * 
	 * @param editor
	 * @return
	 */
	public Color getTaskCheckColor(RepDataEditor editor) {
		return m_hashCheckColor.get(editor.getTaskCheckKey());
	}

	/**
	 * �õ���ǰ�������ݴ��ڶ�Ӧ�ı����˽���Ķ�λ���ĵ�Ԫ��
	 * 
	 * @param editor
	 * @return
	 */
	public List<CellPosition> getTaskCheckCells(RepDataEditor editor) {
		return m_hashCheckCells.get(editor.getTaskCheckKey());
	}

	/**
	 * �õ���ǰ�������ݴ��ڶ�Ӧ�ı����˽��
	 * 
	 * @param editor
	 * @return
	 */
	public CheckResultVO getTaskCheckResult(RepDataEditor editor) {
		return m_hashCheckResult.get(editor.getTaskCheckKey());
	}

	/**
	 * �õ���ǰ�������ݴ��ڶ�Ӧ�ı����˶�λ���
	 * 
	 * @param editor
	 * @return
	 */
	public CheckDetailVO getTaskCheckDetail(RepDataEditor editor) {
		return m_hashCheckDetail.get(editor.getTaskCheckKey());
	}

	/**
	 * ����˽�����
	 * 
	 * @param result
	 */
	public void openCheckResultView(Mainboard mainBoard,CheckResultVO result) {
		// ����������ʾ��Ϣ�������˽�����
		if (result != null && result.getDetailVO() != null
				&& result.getDetailVO().length > 0) {
			mainBoard.openView(CheckResultViewer.class.getName(),
					CHECK_RESULT_ID);
		}

		// ��˽���������ˢ��
		doRefreshCheckResultPane(mainBoard);
	}

	/**
	 * �ı�ѡ�еĵ�λʱ�����ݱ�������Ȩ��ˢ�����񱨱�����Χ
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
	 * �������һ��¼��Ĺؼ���ֵ
	 * 
	 * @param strKeyPKs
	 * @param strKeyVals
	 */
	public void setKeyVals(String[] strKeyPKs, String[] strKeyVals) {
		if (strKeyPKs == null || strKeyPKs.length <= 0)
			return;

		for (int i = 0; i < strKeyPKs.length; i++) {
			// ����ʱ��ؼ��֣���Ҫ��COMM_TIME_KEY����ֵ
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

		// ����Ծ���ڵĹ�ʽ�Ƿ����¼��ŵ�MeasureFmt��
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

				// ���������Ч�������ж�
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
