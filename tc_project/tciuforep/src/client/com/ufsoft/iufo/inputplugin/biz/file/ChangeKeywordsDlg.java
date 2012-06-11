/*
 * �������� 2006-4-11
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import nc.pub.iufo.accperiod.IUFODefaultNCAccSchemeUtil;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.input.CSomeParam;
import nc.ui.iufo.input.InputKeywordsUtil;
import nc.ui.iufo.input.InputUtil;
import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iuforeport.rep.RepFormatModel;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.dataset.Context;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dataexplorer.DataExplorerReport;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.inputcore.AccTimeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.CodeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.inputcore.TimeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.UnitRefTextField;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;

public class ChangeKeywordsDlg extends UfoDialog implements ActionListener,
		IUfoContextKey {
	private static final long serialVersionUID = -8601833673096892888L;

	private static final int KEYWORDS_MAX_COUNT = 10;

	private JPanel jContentPane = null;

	private JPanel jBtnPanel = null;

	private JScrollPane jScrollPane = null;

	private JButton jBtnOK = null;

	private JButton jBtnCancel = null;

	private JPanel jPanel = null;

	private JLabel[] jLabelKeywords = new nc.ui.pub.beans.UILabel[KEYWORDS_MAX_COUNT];

	private JTextField[] jTextFieldKeywords = new JTextField[KEYWORDS_MAX_COUNT];

	/**
	 * Ҫ¼��Ĺؼ�������Ϣ
	 */
	private ChangeKeywordsData[] m_oChangeKeywordsDatas = null;

	/**
	 * ¼��ؼ���ֵ����
	 */
	private String[] m_strInputKeyValues = null;

	private Container _report;

	/**
	 * This is the default constructor
	 */
	public ChangeKeywordsDlg(Container parent,
			ChangeKeywordsData[] changeKeywordsDatas) {
		super(parent);
		_report = parent;
		this.m_oChangeKeywordsDatas = changeKeywordsDatas;

		initialize();
	}

	private ChangeKeywordsData getChangeKeywordsData(int nIndex) {
		return m_oChangeKeywordsDatas[nIndex];
	}

	private int getKeyRowCount() {
		if (m_oChangeKeywordsDatas != null) {
			return m_oChangeKeywordsDatas.length;
		}
		return 0;
	}

	public String[] getInputKeyValues() {
		return m_strInputKeyValues;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLocation(220, 80);
		this.setSize(436, 100 + getPanelHeight());
		this.setContentPane(getJContentPane());
		setTitle(MultiLangInput.getString("uiufotableinput0002"));// "�л��ؼ���";
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJBtnPanel(), null);
			jContentPane.add(getJScrollPane(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jBtnPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJBtnPanel() {
		if (jBtnPanel == null) {
			jBtnPanel = new UIPanel();
			jBtnPanel.setBounds(0, 20 + getPanelHeight(), 428, 58);
			jBtnPanel.add(getJBtnOK(), null);
			jBtnPanel.add(getJBtnCancel(), null);
		}
		return jBtnPanel;
	}

	private int getRowBeginY() {
		return 9;
	}

	private int getRowY(int nRowIndex) {
		return getRowBeginY() + nRowIndex * getRowHeight();
	}

	private int getRowHeight() {
		return 25;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(50, 20, 328, getPanelHeight());

			Border etched = BorderFactory.createEtchedBorder();
			Border title = BorderFactory.createTitledBorder(etched,
					MultiLangInput.getString("uiufotableinput0002"));// "�л��ؼ���";
			jScrollPane.setBorder(title);

			jScrollPane.setViewportView(getJPanel());
		}
		return jScrollPane;
	}

	/**
	 * @return
	 */
	private int getPanelHeight() {
		int nPanelHeight = 50 + getKeyRowCount() * 30;
		if (nPanelHeight < 88) {
			nPanelHeight = 88;
		}
		return nPanelHeight;
	}

	/**
	 * This method initializes jBtnOK
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnOK() {
		if (jBtnOK == null) {
			jBtnOK = new nc.ui.pub.beans.UIButton();
			jBtnOK.setText(MultiLangInput.getString("uiufotableinput0013"));// "ȷ��";
			jBtnOK.addActionListener(this);
		}
		return jBtnOK;
	}

	/**
	 * This method initializes jBtnCancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnCancel() {
		if (jBtnCancel == null) {
			jBtnCancel = new nc.ui.pub.beans.UIButton();
			// jBtnCancel.setMinimumSize(new Dimension(75, 22));
			jBtnCancel.setText(MultiLangInput.getString("uiufotableinput0014"));// ȡ��
			jBtnCancel.addActionListener(this);
		}
		return jBtnCancel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new UIPanel();
			jPanel.setLayout(null);

			int nRowCount = getKeyRowCount();
			for (int nRowIndex = 0; nRowIndex < nRowCount; nRowIndex++) {
				addKeywordRow(jPanel, nRowIndex);
			}
		}
		return jPanel;
	}

	private void addKeywordRow(JPanel parentPanel, int nRowIndex) {
		if (parentPanel == null) {
			return;
		}
		ChangeKeywordsData changeKeywordsData = getChangeKeywordsData(nRowIndex);
		if (changeKeywordsData == null) {
			return;
		}

		int nRowY = getRowY(nRowIndex);
		jLabelKeywords[nRowIndex] = new nc.ui.pub.beans.UILabel();
		jLabelKeywords[nRowIndex].setBounds(30, nRowY, 67, getRowHeight());
		jLabelKeywords[nRowIndex].setText(changeKeywordsData.getKeyName());
		jLabelKeywords[nRowIndex].setHorizontalAlignment(SwingConstants.RIGHT);
		parentPanel.add(jLabelKeywords[nRowIndex], null);

		jTextFieldKeywords[nRowIndex] = null;
		if (changeKeywordsData.isTimeType()) {
			try {
				Date defaultDate = null;
				if (changeKeywordsData.getKeyValue() != null) {
					defaultDate = new SimpleDateFormat("yyyy-MM-dd")
							.parse(changeKeywordsData.getKeyValue());// "2006-12-10")
				}
				jTextFieldKeywords[nRowIndex] = new TimeRefTextField(changeKeywordsData.getKeywordPK(),
						defaultDate);
			} catch (ParseException e) {
				AppDebug.debug(e);
			}
		} else if (changeKeywordsData.isAccPeriodTimeType()) {// add by �����
			// 2008-6-13
			// ��ӻ���ڼ����
			String strAccPeriodPk = null;
			if (_report instanceof DataExplorerReport) {// ����Ǹ�ʽ̬Ԥ��,��ȡĬ�ϵĻ���ڼ�
				strAccPeriodPk = IUFODefaultNCAccSchemeUtil.getInstance()
						.getIUFODefaultNCAccScheme();
			} else {
				strAccPeriodPk = getAccPeriodPk();
				if (strAccPeriodPk == null
						|| strAccPeriodPk.trim().length() == 0) {// ��ǰ����Ϊ���ڼ�Ϊ��ʱ����ȡĬ�ϵ�
					strAccPeriodPk = IUFODefaultNCAccSchemeUtil.getInstance()
							.getIUFODefaultNCAccScheme();
				}
			}
			String strKeyWordPk = changeKeywordsData.getKeywordPK();
			String strKeyValue = changeKeywordsData.getKeyValue();
			jTextFieldKeywords[nRowIndex] = new AccTimeRefTextField(
					strKeyValue, strAccPeriodPk, strKeyWordPk);
		} else if (changeKeywordsData.isCorpKey()
				|| changeKeywordsData.isDicCorpKey()) {
			// modify by chxw ����л��ؼ��ֺ󣬹ؼ��ֲ�����Ҫ�ֹ�ˢ�²������ݵ�����
			IContext context =null;
			
			if (_report instanceof UfoReport)
				context=(Context)((UfoReport) _report).getContextVo();
			else
				context=((RepDataEditor)_report).getMainboard().getContext();
			String curUnitCode = (String) context.getAttribute(CUR_UNIT_CODE);
			String strOrgPK = (String)context.getAttribute(ORG_PK);

			String loginUnitId = (String) context.getAttribute(LOGIN_UNIT_CODE);
			jTextFieldKeywords[nRowIndex] = new UnitRefTextField(
					loginUnitId == null ? curUnitCode : loginUnitId, strOrgPK);

		} else if (changeKeywordsData.getKeyRef() != null) {// ��ͨ��������
			jTextFieldKeywords[nRowIndex] = new CodeRefTextField(
					changeKeywordsData.getKeyRef());
		} else {
			jTextFieldKeywords[nRowIndex] = new nc.ui.pub.beans.UITextField();
		}

		jTextFieldKeywords[nRowIndex]
				.setBounds(112, nRowY, 115, getRowHeight());
		jTextFieldKeywords[nRowIndex].setText(changeKeywordsData.getKeyValue());
		parentPanel.add(jTextFieldKeywords[nRowIndex], null);

	}

	/**
	 * add by ����� 2008-6-13 �������Ļ���ڼ䷽������
	 * 
	 * @param
	 * @return String
	 */
	private String getAccPeriodPk() {
		String strAccPreiodPk = null;// ����ڼ�PK
		
		IContext context=null;
		IRepDataParam repDataParam=null;
		if (_report instanceof UfoReport){
			context=(Context)((UfoReport) _report).getContextVo();
			repDataParam=((TableInputTransObj)context.getAttribute(TABLE_INPUT_TRANS_OBJ)).getRepDataParam();
		}else{
			context=((RepDataEditor)_report).getMainboard().getContext();
			repDataParam=((RepDataEditor)_report).getRepDataParam();
		}		
		
		if (context != null) {
			String strTaskPK = repDataParam.getTaskPK();
			strAccPreiodPk = InputUtil.getAccSechemePK(strTaskPK);
		}
		return strAccPreiodPk;
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof JButton) {
			JButton source = (JButton) event.getSource();
			if (source == jBtnOK) { // ȷ����ť�Ĵ���
				setResult(UfoDialog.ID_OK);
				// ¼��ֵ����
				m_strInputKeyValues = new String[getKeyRowCount()];
				for (int i = 0; i < getKeyRowCount(); i++) {
					m_strInputKeyValues[i] = jTextFieldKeywords[i].getText();
				}
				String strCheckErr = checkValidInputValues();
				if (strCheckErr != null) {
					JOptionPane.showMessageDialog(this, strCheckErr);
					m_strInputKeyValues = null;
				} else {
					this.close();
				}
				return;
			} else if (source == jBtnCancel) { // ȡ����ť�Ĵ���
				setResult(UfoDialog.ID_CANCEL);
				m_strInputKeyValues = null;
				this.close();
				return;
			}
		}
	}

	/**
	 * ¼��ؼ��ּ���
	 * 
	 * @return
	 */
	private String checkValidInputValues() {
		if (m_oChangeKeywordsDatas == null
				|| m_oChangeKeywordsDatas.length == 0) {
			return null;
		}

		// �õ�������������
		IContext context=null;
		IRepDataParam repDataParam=null;
		if (_report instanceof UfoReport){
			context=(Context)((UfoReport) _report).getContextVo();
			repDataParam=((TableInputTransObj)context.getAttribute(TABLE_INPUT_TRANS_OBJ)).getRepDataParam();
		}else{
			context=((RepDataEditor)_report).getMainboard().getContext();
			repDataParam=((RepDataEditor)_report).getRepDataParam();
		}
		
		String strCurOrgPK = (String)context.getAttribute(ORG_PK);
		String strRepID = repDataParam.getReportPK();
		if(strRepID==null){
			strRepID = (String)context.getAttribute(REPORT_PK);
		}
		String strTaskPK = repDataParam.getTaskPK();
		String strOpenType = repDataParam.getOperType();
		String strUserUnitId = repDataParam.getOperUnitPK();

		// �õ����񼰹ؼ�����Ϣ
		TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
		KeyGroupCache kgCache = IUFOUICacheManager.getSingleton()
				.getKeyGroupCache();

		// �õ���ǰ�����û�
		TaskVO taskVO = taskCache.getTaskVO(strTaskPK);

		boolean bCheckTimeInTask = true;

		// �õ��ؼ������PK:����������ģ�Ҳ�����Ƿ��������(����������)
		String strKeyGroupID = getKeyGroupID(strRepID, strOpenType, taskVO,_report instanceof UfoReport==false);
		KeyGroupVO keyGroup = kgCache.getByPK(strKeyGroupID);

		// �õ��ؼ�����ϵĹؼ�������ֵ
		String strTaskAttr = keyGroup.getTTimeProp();
		// KeyVO[] keys = keyGroup.getKeys();

		// �û�������ڼ�͵�λ����
		StringBuffer sbInputDate = new StringBuffer();
		StringBuffer sbUnitCode = new StringBuffer();
		int len = m_oChangeKeywordsDatas.length;
		for (int i = 0; i < len; i++) {
			// ����ؼ�������
			StringBuffer sbCheckedValue = new StringBuffer();
			KeyVO tempKey = createTempKeyVO(m_oChangeKeywordsDatas[i]);
			// modify by wangyga 2008-6-26
			// �˴���ʱ�����������ڸ�ʽ̬������Ԥ���л��ؼ���ʱ������л���ڼ�ؼ��֣�ȥ��У��
			if (KeyVO.isAccPeriodKey(tempKey.getKeywordPK())
					&& _report instanceof DataExplorerReport) {
				continue;
			}
			// end
			String strCheckErr = InputKeywordsUtil.checkInputKeyValue(tempKey,
					m_strInputKeyValues[i], taskVO, strTaskAttr,
					bCheckTimeInTask, strUserUnitId, sbUnitCode, sbInputDate,
					sbCheckedValue, strCurOrgPK,taskVO!=null?taskVO.getAccPeriodScheme():IUFODefaultNCAccSchemeUtil.getInstance().getIUFODefaultNCAccScheme());

			if (strCheckErr != null && strCheckErr.length() > 0) {
				return strCheckErr;
			}
		}

		return null;
	}

	/**
	 * ��ChangeKeywordsData���򴴽�һ����ʱ��KeyVO��������InputKeywordsUtil.checkInputKeyValue
	 * 
	 * @param ckwData
	 * @return
	 */
	private KeyVO createTempKeyVO(ChangeKeywordsData ckwData) {
		KeyVO key = new KeyVO();
		key.setName(ckwData.getKeyName());
		// key.set(ckwData.getKeyValue());
		key.setLen(ckwData.getLen());
		key.setType(ckwData.getType());
		key.setKeywordPK(ckwData.getKeywordPK());
		key.setRef(ckwData.getKeyRef());

		return key;
	}

	/**
	 * �õ���ǰ�򿪱�Ĺؼ������pk
	 * 
	 * @param strRepID
	 * @param strOpenType
	 * @param taskVO
	 * @return
	 */
	public final static String getKeyGroupID(String strRepID,
			String strOpenType, TaskVO taskVO,boolean bRepData) {
		String strKeyGroupID = null;
		if (bRepData) {
			if (taskVO != null) {
				strKeyGroupID = taskVO.getKeyGroupId();
			}
		} else {
			String[] strRepIDs = null;
			String strOperType1 = strOpenType;
			if (strOperType1 != null) {
				strRepIDs = new String[1];
				strRepIDs[0] = strRepID;
			}
			// �жϱ����Ƿ����
			ReportVO[] repVOs = IUFOUICacheManager.getSingleton()
					.getReportCache().getByPks(strRepIDs);
			if (repVOs == null || repVOs.length < 1 || repVOs[0] == null) {
				// �����ѱ�ɾ��
				throw new CommonException("miufo1002847"); // "�����ѱ�ɾ��"
			}
			// �жϱ����ʽ�Ƿ����
			RepFormatModel[] repFmtModels = IUFOUICacheManager.getSingleton()
					.getRepFormatCache().getFormatByPks(strRepIDs);
			if (repFmtModels != null && repFmtModels.length >= 0
					&& repFmtModels[0] != null
					&& repFmtModels[0].getFormatModel() != null) {
				strKeyGroupID = repFmtModels[0].getMainKeyCombPK();
			} else {
				// ������û����Ƹ�ʽ���޷��鿴����
				throw new CommonException("miufo1002848"); // "������û����Ƹ�ʽ���޷��鿴����"
			}
		}
		return strKeyGroupID;
	}

	/**
	 * �ж��Ƿ��Ƿ���������
	 * 
	 * @param action
	 * @return
	 */
	private final static boolean isAnaRepLook(String strOperType) {
		boolean bReturn = false;
		if (strOperType != null
				&& strOperType
						.equals(CSomeParam.STR_OPERATION[CSomeParam.VIEW_ANALYSIS])) {
			bReturn = true;
		}
		return bReturn;
	}
}
