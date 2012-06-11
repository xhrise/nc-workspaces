/*
 * �������� 2005-6-28
 *
 */
package com.ufsoft.iuforeport.tableinput.applet;

import java.util.Hashtable;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.input.table.IufoRefData;
import nc.ui.iufo.input.table.TableInputParam;
import nc.ui.iuforeport.rep.RepToolAction;
import nc.vo.iufo.data.MeasurePubDataVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.pluginregister.TableInputPluginRegister;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputFilePlugIn;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepPanel;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iufo.inputplugin.biz.file.SaveRepDataCmd;
import com.ufsoft.iufo.inputplugin.biz.file.SaveRepDataExt;
import com.ufsoft.iufo.inputplugin.biz.file.TableInputSearchOper;
import com.ufsoft.iufo.inputplugin.inputcore.RefData;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.UfoQueryNavApplet;
import com.ufsoft.iuforeport.tableinput.TableSearchCondVO;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.PluginRegister;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.util.MultiLang;

/** 
 * @author liulp
 *
 */
public class TableInputApplet extends UfoQueryNavApplet implements IUfoContextKey{

	private static final long serialVersionUID = -3200554433174527191L;

	/**
	 * ��ʼ��
	 */
	public void init() {
		
		//modify by ljhua 2007-3-9 �޸�˳�򣬽����InputFilePlugIn����ʱ�������ؼ���¼��Ի���
		//��ʼ�����ؼ���һЩ״̬
		initTableInputCtrl();

		//��δ���õ�ǰ��������ʾ����ǰ����δ���á�������ۺϲ�ѯδ���õ�ǰ����ʱ��������
		if (!isSetupUserTask()) {
			javax.swing.JOptionPane.showMessageDialog(getRootPane(),
					StringResource.getStringResource("miufo76")); //δ���õ�ǰ����
			return;
		}

		//��ʼ�����ؼ���������
		Hashtable params = new Hashtable();
		Object param = getParameter(ITableInputAppletParam.PARAM_REPDATA);
		if (param != null) {
			params.put(ITableInputAppletParam.PARAM_REPDATA, param);
		}
		param = getParameter(ITableInputAppletParam.PARAM_OPERTYPE);
		if (param != null) {
			params.put(ITableInputAppletParam.PARAM_OPERTYPE, param);
		}
		PluginRegister plreg = new TableInputPluginRegister(params);

		setUfoReport(new UfoReport(UfoReport.OPERATION_INPUT,
				geneInitContext(), null, plreg));

		if (!isNeedDisTable()) {
			//���ռ䲻��ʾ�����ݱ�������װ�أ�
			getUfoReport().getTable().setVisible(false);
		}

		//���õ���������� add by wangyga 2008-7-10 
		setReportNavPanel();

		//�õ����ؼ�Ҫչ�ֵ�cellsModel �͡��˵�״̬������
		//      int nBizType = ITableInputMenuType.MENU_TYPE_OPEN;
		//      InputOpenRepOper inputOpenRepOper = new InputOpenRepOper(getUfoReport());
		//      inputOpenRepOper.performBizTask(nBizType);

		//�޸ı���򿪷�ʽ������Ӧ�ۺϲ�ѯ���Ӳ�ѯ��������汾��
		if (isOpenRepData((TableInputContextVO) getUfoReport().getContextVo())) {
			TableSearchCondVO searchCondVO = new TableSearchCondVO();
			IInputBizOper inputMenuOper = new TableInputSearchOper(
					getUfoReport(), new Object[] { searchCondVO });
			inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_OPEN);
		} else{
			ChooseRepPanel chooseRepPanel = GeneralQueryUtil.getChooseRepPanel(getUfoReport());
			TableSearchCondVO searchCondVO = new TableSearchCondVO();
			String strRootUnit = chooseRepPanel.getRootUnit();
			searchCondVO.setStrOperUnitPK(strRootUnit);
			IInputBizOper inputMenuOper = new TableInputSearchOper(getUfoReport(), new Object[] { searchCondVO });
			inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_SEARCH_REPS);
		}

		super.init();

	}

	/**
	 * ��ʼ�����ؼ���һЩ״̬
	 */
	private void initTableInputCtrl() {
		//׼���������¼��Ļ���,
		String strParamReportData = getParameter(ITableInputAppletParam.PARAM_OPERTYPE);
		if (TableInputParam.OPERTYPE_REPDATA_INPUT.equals(strParamReportData)
				|| TableInputParam.OPERTYPE_REPDATA_EDIT_REP
						.equals(strParamReportData)
				|| TableInputParam.OPERTYPE_REPDATA_EDIT_TASK
						.equals(strParamReportData)) {
			RefData.setProxy(new IufoRefData());
		}

		//����������ʾ���뻹�����Ƶ�����:true,false
		String strShowRefID = getParameter(ITableInputAppletParam.PARAM_TI_SHOWREFID);
		boolean bShowRefID = "true".equalsIgnoreCase(strShowRefID) ? true
				: false;
		ReportStyle.setShowRefID(bShowRefID);
		//add by ����� �Ա���ʽ��Ԫ�Ƿ�����༭�Ŀ��ƣ����л�����ʱ�Ѿ����˿��ƣ��˴������ٿ���
		//��ʽ�Ƿ��¼��:true,false
		//String strFormulaCanInput = getParameter(ITableInputAppletParam.PARAM_TI_FORMULACANINPUT);
		//boolean bFormulaCanInput = "true".equalsIgnoreCase(strFormulaCanInput) ? true
		//		: false;
		//MeasureFmt.setCanInput(bFormulaCanInput);

		//����Ϊ0ʱ�Ƿ���ʾ:true,false
		String strDisplayZero = getParameter(ITableInputAppletParam.PARAM_TI_DISPLAYZERO);
		boolean bDisplayZero = "true".equalsIgnoreCase(strDisplayZero) ? true
				: false;
		ReportStyle.setShowZero(bDisplayZero);
		
		//���ܽ���Ƿ������ֹ��޸�
        String strCanModifyTotalResult = getParameter(ITableInputAppletParam.PARAM_TOTAL_CANEDIT);
        boolean bCanModifyTotalResult = "true".equalsIgnoreCase(strCanModifyTotalResult)?true:false;
        TableInputAuth.setCanModifyTotalResult(bCanModifyTotalResult);
	}
	


	private void setReportNavPanel(){
		TableInputContextVO inputContextVo = (TableInputContextVO)getUfoReport().getContextVo();
		Object tableInputTransObj = inputContextVo.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
	
//		TableInputTransObj intputTransObj = inputContextVo.getInputTransObj();
		IRepDataParam repDataParam = inputTransObj.getRepDataParam();
		MeasurePubDataVO pubDataVo = null;
		try {
			pubDataVo = MeasurePubDataBO_Client.findByAloneID(repDataParam.getAloneID());
		} catch (Exception e) {
			AppDebug.debug(e);
			throw new RuntimeException(e.getMessage());
		}		
		if(pubDataVo != null && pubDataVo.getVer() >=1000){//��Ϊƽ���ʱ������ʾ��ѯ���
				getUfoReport().getReportNavPanel().getNorthComp().setVisible(false);
		}
				
		//��ʱ����ʾ��˽�����
		//		if(getUfoReport().getReportNavPanel().getSouthComp() != null)
		//			getUfoReport().getReportNavPanel().getSouthComp().setVisible(true);
		//���õ������Ĭ����ʾ
//		if (getUfoReport().getReportNavPanel().getNorthPane() != null)
//			getUfoReport().getReportNavPanel().getNorthPane()
//					.setDividerLocation(50);
		//		if(getUfoReport().getReportNavPanel().getSouthComp() != null)
		//			getUfoReport().getReportNavPanel().getSouthPane().setDividerLocation(400);
//		if (getUfoReport().getReportNavPanel().getWestPane() != null)
//			getUfoReport().getReportNavPanel().getWestPane()
//					.setDividerLocation(170);
	}
  
	
	private boolean isRepDataByRepView() {
		boolean bRepDataByRepView = false;
		if (isReportData()) {
			String strOperType = getParameter(ITableInputAppletParam.PARAM_OPERTYPE);
			bRepDataByRepView = TableInputParam.OPERTYPE_REPDATA_VIEW_REP2
					.equals(strOperType);
		}
		return bRepDataByRepView;
	}

	/**
	 * �õ����ؼ�������Ϣ����
	 * @return
	 */
	private TableInputTransObj geneTransObj() {
		TableInputTransObj m_oTransObj = new TableInputTransObj();
		if (isReportData()) {
			IRepDataParam oRepDataParam = new RepDataParam();
			//����PK
			String strReportPK = getParameter(REPORT_PK);
			if (strReportPK == null || strReportPK.length() > 0) {
				oRepDataParam.setReportPK(strReportPK);
			}
			//��������operType
			String strOperType = getParameter(ITableInputAppletParam.PARAM_OPERTYPE);
			oRepDataParam.setOperType(strOperType);
			//��������operType
			String strIsNeedFilterDataRight = getParameter(ITableInputAppletParam.PARAM_NEEDFILTER_DR);
			oRepDataParam.setIsNeedFilterDataRight(new Boolean(
					strIsNeedFilterDataRight).booleanValue());
			//AloneID
			String strAloneID = getParameter(ITableInputAppletParam.PARAM_ALONEID);
			oRepDataParam.setAloneID(strAloneID);
			//������λPK
			String strOperUnitPK = getParameter(CUR_UNIT_ID);
			oRepDataParam.setOperUnitPK(strOperUnitPK);
			//�����û�PK
			String strOperUserPK = getParameter(CUR_USER_ID);
			oRepDataParam.setOperUserPK(strOperUserPK);
			//��֯��ϵ
			String strOrgPK = getParameter(ORG_PK);
			oRepDataParam.setOrgPK(strOrgPK);
			//������������PK
			String strTaskPK = getParameter(TASK_PK);
			oRepDataParam.setTaskPK(strTaskPK);
			//�Ƿ�ϲ�����
			String strIsHBBBData = getParameter(ITableInputAppletParam.PARAM_IS_HBBBDTA);
			oRepDataParam.setIsHBBBData(new Boolean(strIsHBBBData)
					.booleanValue());
			//����IUFO���ݵ�ImportAloneID
			String strImportAloneID = getParameter(ITableInputAppletParam.PARAM_IMPORT_ALONEID);
			oRepDataParam.setImportAloneID(strImportAloneID);
			//����Դ��Ϣ
			DataSourceInfo curDSInfo = getCurDataSourceInfo();
			oRepDataParam.setDSInfo(curDSInfo);
			//������
			m_oTransObj.setRepDataParam(oRepDataParam);
			m_oTransObj.setType(TableInputTransObj.TYPE_REPDATA);
		} else if (isMQuery()) {
			String strMQueryPK = getParameter(ITableInputAppletParam.PARAM_MQUERY_PK);
			//������
			m_oTransObj.setMQueryPK(strMQueryPK);
			m_oTransObj.setType(TableInputTransObj.TYPE_MQUERY);
		} else if (isHBDraft()) {
			IHBDraftParam oHBDraftParam = new HBDraftParam();
			String strUnitPK = getParameter(ITableInputAppletParam.PARAM_HB_UNITPK);
			oHBDraftParam.setUnitCode(strUnitPK);
			String strReportPK = getParameter(ITableInputAppletParam.PARAM_HB_REPORTPK);
			oHBDraftParam.setRepID(strReportPK);
			String strAccTime = getParameter(ITableInputAppletParam.PARAM_HB_ACCTIME);
			oHBDraftParam.setAccTime(strAccTime);
			String strTaskPK = getParameter(ITableInputAppletParam.PARAM_HB_TASKPK);
			oHBDraftParam.setTaskID(strTaskPK);
			String strDraftType = getParameter(ITableInputAppletParam.PARAM_HB_DRAFTTYPE);
			oHBDraftParam.setDraftType(strDraftType);
			//������
			m_oTransObj.setHBDraftParam(oHBDraftParam);
			m_oTransObj.setType(TableInputTransObj.TYPE_HBDRAFT);

			//�����û�PK
			IRepDataParam oRepDataParam = new RepDataParam();
			String strOperUserPK = getParameter(CUR_USER_ID);
			oRepDataParam.setOperUserPK(strOperUserPK);
			//��֯��ϵ
			String strOrgPK = getParameter(ORG_PK);
			oRepDataParam.setOrgPK(strOrgPK);

			m_oTransObj.setRepDataParam(oRepDataParam);
		} else if (isPrint()) {
			IPrintParam oPrintParam = new PrintParam();
			String strNType = getParameter(ITableInputAppletParam.PARAM_PRINT_NTYPE);
			oPrintParam.setNType(strNType);
			String strSessionID = getParameter(ITableInputAppletParam.PARAM_PRINT_SESSIONID);
			oPrintParam.setSessionID(strSessionID);

			oPrintParam
					.setAloneID(getParameter(ITableInputAppletParam.PARAM_PRINT_ALONE_ID));
			oPrintParam
					.setRepID(getParameter(ITableInputAppletParam.PARAM_PRINT_REP_ID));
			oPrintParam
					.setTaskID(getParameter(ITableInputAppletParam.PARAM_PRINT_TASK_ID));
			oPrintParam
					.setTotalID(getParameter(ITableInputAppletParam.PARAM_PRINT_TOTAL_ID));
			oPrintParam
					.setUnitID(getParameter(ITableInputAppletParam.PARAM_PRINT_UNIT_ID));
			oPrintParam
					.setNeedCutZero("true"
							.equalsIgnoreCase(getParameter(ITableInputAppletParam.PARAM_PRINT_NEEDCUTZERO)));
			oPrintParam
					.setFrom(getParameter(ITableInputAppletParam.PARAM_PRINT_FROM));
			oPrintParam.setOrgPK(getParameter(ORG_PK));

			//������
			m_oTransObj.setPrintParam(oPrintParam);
			m_oTransObj.setType(TableInputTransObj.TYPE_PRINT);
		}
		//��ǰ��¼ʱ��
		String strCurLoginDate = getParameter("CurrentDate");
		m_oTransObj.setCurLoginDate(strCurLoginDate);
		m_oTransObj.setLoginUnit(getParameter(CUR_UNIT_CODE));

		//�û�ѡ������ֱ���
		String strLangCode = getParameter(ITableInputAppletParam.PARAM_LANGCODE);
		m_oTransObj.setLangCode(strLangCode);

		//DEBUGING:print the TableInputTransObj parameter's information
		if (isDebug()) {
			debug("[print the TrbleInputTransObj parameter's information] begin...");
			debug("=====================");
			debug("=====strType = " + m_oTransObj.getType());
			if (m_oTransObj.getRepDataParam() != null) {
				IRepDataParam oRepDataParam = m_oTransObj.getRepDataParam();
				debug("strReportPK = " + oRepDataParam.getReportPK());
				debug("strAloneID = " + oRepDataParam.getAloneID());
				debug("strOperUnitPK = " + oRepDataParam.getOperUnitPK());
				debug("strOperUserPK = " + oRepDataParam.getOperUserPK());
				debug("strTaskPK = " + oRepDataParam.getTaskPK());
				debug("strImportAloneID = " + oRepDataParam.getImportAloneID());
				if (oRepDataParam.getDSInfo() != null) {
					debug("strDSID = " + oRepDataParam.getDSInfo().getDSID());
					debug("strDSUnitPK = "
							+ oRepDataParam.getDSInfo().getDSUnitPK());
					debug("strDSUserPK = "
							+ oRepDataParam.getDSInfo().getDSUserPK());
					//					debug("strDSPwd = "  + oRepDataParam.getDSInfo().getDSPwd());
					debug("strDSDate = "
							+ oRepDataParam.getDSInfo().getDSDate());
				}
			}
			if (m_oTransObj.getMQueryPK() != null) {
				debug("strMQueryPK = " + m_oTransObj.getMQueryPK());
			}
			if (m_oTransObj.getHBDraftParam() != null) {
				IHBDraftParam oHBDraftParam = m_oTransObj.getHBDraftParam();
				debug("strAccTime = " + oHBDraftParam.getAccTime());
				debug("strDraftType = " + oHBDraftParam.getDraftType());
				debug("strRepID = " + oHBDraftParam.getRepID());
				debug("strTaskID = " + oHBDraftParam.getTaskID());
				debug("strUnitCode = " + oHBDraftParam.getUnitCode());
			}
			if (m_oTransObj.getPrintParam() != null) {
				IPrintParam oPrintParam = m_oTransObj.getPrintParam();
				debug("strNType = " + oPrintParam.getNType());
				debug("strSessionID = " + oPrintParam.getSessionID());
			}
			debug("=====================");
			debug("strLangCode = " + m_oTransObj.getLangCode());
			debug("[print the TrbleInputTransObj parameter's information] end...");
		}
		return m_oTransObj;
	}

	/**
	 * �������Դ��Ϣ
	 * @return
	 */
	private DataSourceInfo getCurDataSourceInfo() {
		String strDSID = getParameter(ITableInputAppletParam.DS_DEFAULT);
		String strDSUnitPK = getParameter(ITableInputAppletParam.DS_UNIT);
		String strDSUserPK = getParameter(ITableInputAppletParam.DS_USER);
		//				String strDSPwd = nc.bs.iufo.toolkit.Encrypt.decode(getParameter(IDataSourceParam.DS_PASSWORD), strDSID);
		String strNotEncodedDSPwd = getParameter(ITableInputAppletParam.DS_PASSWORD);
		String strDSDate = getParameter(ITableInputAppletParam.DS_DATE);
		DataSourceInfo curDSInfo = new DataSourceInfo(strDSID, strDSUnitPK,
				strDSUserPK, strNotEncodedDSPwd, strDSDate);
		return curDSInfo;
	}

	/**
	 * �Ƿ�Ϊָ���ѯ
	 * @return
	 */
	private boolean isMQuery() {
		return getParameter(ITableInputAppletParam.PARAM_MQUERY_PK) != null;
	}

	/**
	 * �Ƿ�Ϊ�ϲ������׸�
	 * @return
	 */
	private boolean isHBDraft() {
		return getParameter(ITableInputAppletParam.PARAM_HB_DRAFTTYPE) != null;
	}

	/**
	 * �Ƿ�Ϊ��������״̬
	 * @return
	 */
	private boolean isReportData() {
		return getParameter(ITableInputAppletParam.PARAM_REPDATA) != null;
	}

	/**
	 * �Ƿ�Ϊ��ӡ����
	 * @return
	 */
	private boolean isPrint() {
		return getParameter(ITableInputAppletParam.PARAM_PRINT_NTYPE) != null
				|| getParameter(ITableInputAppletParam.PARAM_PRINT_ALONE_ID) != null;
	}

	/**
	 * �Ƿ���Ҫ��ʾ���
	 * @return
	 */
	private boolean isNeedDisTable() {
		String strParamReportData = getParameter(ITableInputAppletParam.PARAM_OPERTYPE);
		return !isReportData()
				|| TableInputParam.OPERTYPE_REPDATA_EDIT_REP
						.equals(strParamReportData)
				|| TableInputParam.OPERTYPE_REPDATA_EDIT_TASK
						.equals(strParamReportData)
				|| TableInputParam.OPERTYPE_REPDATA_VIEW_REP2
						.equals(strParamReportData)
				|| TableInputParam.OPERTYPE_REPDATA_INPUT
						.equals(strParamReportData);
	}

	/**
	 * �Ƿ���Ҫ��ʾ���
	 * @return
	 */
	private boolean isNeedSelRep() {
		boolean bReturn = false;

		String strParamReportData = getParameter(ITableInputAppletParam.PARAM_OPERTYPE);
		if (TableInputParam.OPERTYPE_REPDATA_INPUT.equals(strParamReportData)) {
			bReturn = true;
		} else if (isReportData() == true
				&& TableInputParam.OPERTYPE_REPDATA_EDIT_REP
						.equals(strParamReportData) == false
				&& TableInputParam.OPERTYPE_REPDATA_VIEW_REP2
						.equals(strParamReportData) == false) {
			bReturn = true;
		}

		return bReturn;
	}

	/**
	 * ��ñ���Ļ�����Ϣ
	 * @return
	 */
	private ContextVO geneInitContext() {
		TableInputContextVO inputContextVO = new TableInputContextVO(
				new UfoContextVO(), geneTransObj());
		inputContextVO.setAttribute(CUR_UNIT_CODE, getParameter(CUR_UNIT_CODE));
		inputContextVO.setAttribute(LOGIN_UNIT_CODE, getParameter(CUR_UNIT_CODE));
		inputContextVO.setAttribute(ORG_PK, getParameter(ORG_PK));
		//���Ƿ���ʾ������
		String strShowRepTree = getParameter(ITableInputAppletParam.PARAM_SHOWREP);
		inputContextVO.setAttribute(SHOW_REP_TREE, new Boolean(strShowRepTree));
		String strShowKeyPanel = getParameter(ITableInputAppletParam.PARAM_SHOWKEY);
		inputContextVO.setAttribute(SHOW_KEY_PANEL, new Boolean(strShowKeyPanel));
		String strGenralQuery = getParameter(ITableInputAppletParam.PARAM_IS_GENERALQUERY);
		inputContextVO.setAttribute(GENRAL_QUERY, new Boolean(strGenralQuery));
		String strOperUnitPK = getParameter(CUR_UNIT_ID);
		inputContextVO.setAttribute(LOGIN_UNIT_ID, strOperUnitPK);
		return inputContextVO;
	}

	//	/**
	//	 * ���������ʾ���ؼ�����Panel
	//	 * @return
	//	 */
	//	private javax.swing.JPanel getFrameContentPane(){
	//        if(ivjFrameContentPane == null){
	//            try{
	//            	ivjFrameContentPane = new UIPanel();
	//            	ivjFrameContentPane.setName("FrameContentPane");
	//            	ivjFrameContentPane.setLayout(new BorderLayout());
	//                // user code begin {1}
	//                // user code end
	//            } catch(java.lang.Throwable ivjExc){
	//                // user code begin {2}
	//                // user code end
	//                handleException(ivjExc);
	//            }
	//        }
	//        return ivjFrameContentPane;
	//    }
	//    /**
	//     * ÿ�������׳��쳣ʱ������
	//     * @param exception java.lang.Throwable
	//     */
	//    private void handleException(java.lang.Throwable exception){
	//
	//        /* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	//        // System.out.println("--------- δ��׽�����쳣 ---------");
	//        // exception.printStackTrace(System.out);
	//    }

	//    /**
	//     * ��������ֵ
	//     * @return
	//     */
	//    private void setDirty(boolean bDirty){
	//    	if(m_oUfoReport != null && !bDirty){
	//    		m_oUfoReport.store();
	//    	}
	//    }

	/**
	 * �Ƿ����õ�ǰ����
	 */
	private boolean isSetupUserTask() {
		String strTaskPK = getParameter(TASK_PK);
		if (strTaskPK == null || strTaskPK.trim().length() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * �Ƿ�򿪱�������
	 * ������֧�ְ�����鿴�������ݡ��鿴ָ��ԭ�����ݡ��鿴�ϲ��������ݡ��ۺϲ�ѯ�ӿڵ�
	 */
	private static boolean isOpenRepData(TableInputContextVO inputContextVO) {
		Object tableInputTransObj = inputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
	
		if (TableInputTransObj.isReportData(inputTransObj)) {
			String strRepPK = inputTransObj
					.getRepDataParam().getReportPK();
			if (strRepPK == null || strRepPK.trim().length() == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * �Ƿ�����Ϊtrue
	 * @return
	 */
	public boolean isDirty() {
		if (getUfoReport() != null) {
			InputFilePlugIn inputFilePlugIn = (InputFilePlugIn) getUfoReport()
					.getPluginManager().getPlugin(
							InputFilePlugIn.class.getName());
			IExtension[] extensions = inputFilePlugIn.getDescriptor()
					.getExtensions();
			int nIndex = 0;
			//    	        //�򿪱���
			//    	        nIndex++;
			//����
			SaveRepDataExt saveRepDataExt = (SaveRepDataExt) extensions[nIndex];
			boolean isSaveRepEnable = saveRepDataExt.isEnabledSelf();
			System.out.println("[iufodebug]: isSaveRepEnable = "
					+ isSaveRepEnable);
			return isSaveRepEnable && getUfoReport().isDirty();
		}
		return false;
	}

	//    /**
	//     * ׼��CellsModel����¼��״̬
	//     */
	//    private void doAfterCellsModel() {        
	//        //���ؼ��Ƿ�ֻ����true,false
	//        String strReadOnly = getParameter(ITableInputAppletParam.PARAM_TI_READONLY);
	//        boolean bReadOnly = "true".equalsIgnoreCase(strReadOnly)?true:false;
	//        m_oUfoReport.setReadOnly(bReadOnly);
	//        
	//    }
	//
	//	/* ���� Javadoc��
	//	 * @see com.ufsoft.iuforeport.tableinput.ITableInputMenuOper#performMenuTask(int)
	//	 */
	//	public Object performMenuTask(String strMenuTypeInt) {
	//		Object oReturn = "performMenuTask(" + strMenuTypeInt+ ") int TableInputApplet.java";
	//		debug(oReturn.toString());
	//		
	//		//�õ����ؼ�Ҫչ�ֵ�cellsModel
	//		int nMenuType = new Integer(strMenuTypeInt).intValue();
	//		if(nMenuType == ITableInputMenuType.MENU_TYPE_SAVE){
	//		    InputCorePlugin inputCorePlugin = (InputCorePlugin)m_oUfoReport.getPluginManager().getPlugin(InputCorePlugin.class.getName());
	//		    if(!inputCorePlugin.verifyBeforeSave()){
	//		        oReturn = Boolean.valueOf(false);
	//		    }
	//		}		
	//
	////		if(nMenuType == ITableInputMenuType.MENU_TYPE_CALCULATE_U8ACC){
	////			//NOTICE: IUFO3.1SP�汾�У�U8Acc��ʱ��֧��
	////		  	//��U8��ɢ���ݹ�ʽ�ļ���ʱ,����һ��Ԥ���㣺���ݵ�ǰ¼�����ݺͱ����ʽ���õ�U8����ɢ��ʽ����
	////			//ArrayList listU8AccFuncs = (ArrayList)doPerformMnnuTask(ITableInputMenuType.MENU_TYPE_CALCULATE_U8Acc);
	//////		    testUsingJSObject();		    
	////			//����U8��ɢ����Դ������Ϣ�Ի���
	//////			DataSourceVO u8AccDSVO = new DataSourceVO();
	////		}
	//		
	//		if(oReturn instanceof String){
	//		    oReturn = (String)doPerformMnnuTask(nMenuType);
	//		}else{
	//		    oReturn = "false";
	//		}
	//		showErrorMsg(oReturn.toString(),isInitMenuType(nMenuType));
	//		
	//		return oReturn; 		
	//        return null;
	//	}

	//	/**
	//	 * �������IE���ڵ�window����
	//	 *
	//	 * �������ڣ�(2002-10-31 14:32:45)
	//	 */
	//	private netscape.javascript.JSObject getJSwin() {
	//
	//		if (win == null) {
	//			win = netscape.javascript.JSObject.getWindow(this);
	//		}
	//		return win;
	//	}

	public void testUsingJSObject() {
		//		debug("[debug===]:TableINputApplet is being testUsingJSObject!");
		//		try {
		//			JSObject win = (JSObject)getJSwin();
		//			System.out.println("win =" + win.getClass() + "," + win.toString());
		//			JSObject doc = (JSObject)getJSwin().getMember("document");
		//			System.out.println("doc =" + doc.getClass() + "," + doc.toString());
		//			win.eval("alert(\"This alert comes from Java!\")");  
		////			win.eval("testSelfFunc('hello,testSelfFunc'");  //eval���ܴ�����
		//			win.eval("alert(\"This alert comes from Java end!\")");  
		//			
		//			
		//			JSObject all = (JSObject)doc.getMember("all");
		//			System.out.println("all =" + all.getClass() + "," + all.toString());
		//			JSObject jsObjIufoForU81 = (JSObject)all.call("item", new Object[]{"IufoForU81"});
		//			if(jsObjIufoForU81!=null){
		//				//IufoLogin(ver.value, account.value, year.value,user.value, password.value,address.value,form1.hid_acctime.value)
		//				//���������ȡֵ�����кϷ��Լ��飿
		//				String strAddress = "10.7.5.52";
		//				Boolean bLogin = (Boolean)jsObjIufoForU81.call("IufoLogin", new Object[]{"8.60SQL", "testAccount", "2005", "liulp","iufo",strAddress,"2005-08-31"});
		////				bLogin = new Boolean("true"); 
		//				if(!bLogin.booleanValue()){
		//					debug("U8�ؼ�����¼ʧ�ܣ�");
		//				}
		//				int iFuncCount = 2;
		//				String[] strFormulas = new String[]{"",""};//FormulaContent,str=form1.hid_u8_form_text.value
		//				String[] strValues = new String[2];
		//				debug("U8���ص�ֵ����������������");
		//				for(int i = 0;i<iFuncCount;i++){
		//					if(strFormulas[i] ==null){
		//						debug("û�ж����ȡ����ʽ:" + i);
		//						continue;
		//					}
		//					strValues[i] = accessData(jsObjIufoForU81,strFormulas[i],strAddress);
		//					debug("��["+i+"��]="+strValues[i]);
		//				}
		//			}
		//		}
		//		catch (Exception e) {
		//			e.printStackTrace(System.out);
		//		}
	}

	//	/**
	//	 * ����U8��ʽ
	//	 * @param jsObjIufoForU81
	//	 * @param strFormula
	//	 * @param strAddress
	//	 * @return
	//	 */
	//	private String accessData(JSObject jsObjIufoForU81, String strFormula, String strAddress) {
	//		String strValue = null;
	//		Object[] objParams = null;
	//	  	if(strAddress == null || strAddress.length()==0){
	//	  		objParams = new Object[]{strFormula};
	//	 	}else{
	//	  		objParams = new Object[]{strFormula,strAddress};
	//	 	}	 		
	//		strValue =  (String)jsObjIufoForU81.call("GetFormulaValue",objParams);
	//		return strValue;
	//	}

	//	/**
	//	 * ����״̬ʱ������ִ�й��������ķ�����Ϣ
	//	 * @param strSaveErrMsg
	//	 */
	//	private void showErrorMsg(String strAlertMsg,boolean bInitMenuType) {
	//	    boolean bErrorMsg = false;
	//	    String strReason = strAlertMsg;
	//	    if(strAlertMsg!=null && !strAlertMsg.equals("true")){
	//	        bErrorMsg = true;
	//	    }
	//        if(isDebug()){
	//            strReason = "debuging...������ϢΪ--" + strReason;
	//        }	        
	//		if((bInitMenuType && bErrorMsg) || isDebug()){                				
	//			javax.swing.JOptionPane.showMessageDialog(
	//                this,
	//                strReason);
	//		}		
	//	}

	//    /**
	//     * @param cellsModel
	//     */
	//    private void refreshCellsModel(CellsModel cellsModel) {
	//        m_oUfoReport.getTable().setCurCellsModel(cellsModel);            
	//        doAfterCellsModel();
	//    }
	//    /**
	//     * ִ�в˵�����
	//     * @param nMenuType
	//     * @return
	//     */
	//    private Object doPerformMnnuTask(int nMenuType) {
	//        String strReturn = null;
	//        if(nMenuType != ITableInputMenuType.MENU_TYPE_VIEW_SOURCE){
	//            Object transReturnObj = doLinkServletTask(nMenuType);           
	//            strReturn = dealTransReturnObj(transReturnObj,nMenuType);           
	//            if(nMenuType ==  ITableInputMenuType.MENU_TYPE_SAVE && "true".equals(strReturn)){
	//                setDirty(false);
	//            }
	//        }else{
	//            //#�鿴������Դ�ݲ�֧��
	//            DynAreaPlugin dynAreaPlugin = (DynAreaPlugin)m_oUfoReport.getPluginManager().getPlugin(DynAreaPlugin.class.getName());
	//
	//            CellsModel curCellsModel = getCellsModel();
	//            CellPosition selCellPos = curCellsModel.getSelectModel().getAnchorCell();
	//            CellPosition selOrigCellPos = dynAreaPlugin.getOrigCellPos(selCellPos);
	//
	//            if(selCellPos == null || selOrigCellPos == null){
	//                String strAlertMsg = StringResource.getStringResource("miufotableinput000006");//"����ѡ����Ч��Ԫ���ܲ鿴��Դ!";     
	//                javax.swing.JOptionPane.showMessageDialog(
	//                        this,
	//                        strAlertMsg);
	//                return "";
	//            }
	//            
	//            //�õ���̬����ǰ�еĹؼ���ֵ����
	//            Hashtable keyValuesHash = dynAreaPlugin.getUnitKeys(selCellPos);
	//            int iKeyCount = keyValuesHash!=null?keyValuesHash.size():0;
	//            Enumeration enumKeyFmts = keyValuesHash.keys();
	//            String[][] strKeyValues = new String[iKeyCount][2];
	//            int iIndex = 0;
	//            while(enumKeyFmts.hasMoreElements()){
	//                KeyFmt keyFmt = (KeyFmt)enumKeyFmts.nextElement();
	//                strKeyValues[iIndex][0] = keyFmt.getKeyPK();
	//                strKeyValues[iIndex][1] = (String)keyValuesHash.get(keyFmt);
	//                iIndex++;                   
	//            }
	//            String strKeySplit = KEYSPLIT_TABLEINPUT;
	//            StringBuffer sbReturn = new StringBuffer("" + (selOrigCellPos.getRow()+1) + strKeySplit + (selCellPos.getColumn()+1));
	//            StringBuffer sbKeyValues = new StringBuffer();
	//            for (int i=0;i<iKeyCount;i++){
	//                if(strKeyValues[i][1]== null || strKeyValues[i][1].length() <=0){
	//                    String strAlertMsg = StringResource.getStringResource("miufotableinput000007");//"ѡ��Ķ�̬�����б�������д�ؼ��ֵ�ֵ���ܲ鿴��Դ!"
	//                    javax.swing.JOptionPane.showMessageDialog(
	//                            this,
	//                            strAlertMsg);
	//                    return "";
	//                }
	//                sbKeyValues.append(strKeyValues[i][0]+"_" + strKeyValues[i][1]);
	//                if (i<iKeyCount-1){
	//                    sbKeyValues.append(strKeySplit);
	//                }
	//            }
	//            strReturn = sbReturn.append(sbKeyValues).toString();
	//        }
	//        return strReturn;
	//    }
	//    /**
	//     * ִ������Servlet�Ĳ���
	//     * @param nMenuType
	//     * @return
	//     */
	//    public Object doLinkServletTask(int nMenuType){
	//        Object returnObj = null;
	//        CellsModel cellsModel = null;
	//        switch(nMenuType){
	//            //�򿪱���
	//            case ITableInputMenuType.MENU_TYPE_OPEN:
	//                //�ɹ���õ����ؼ�Ҫչ�ֵ�cellsModel
	//                returnObj = LinkServletUtil.linkTableInputOperServlet(ITableInputMenuType.MENU_TYPE_OPEN,getTransObj(),null);
	//                break;
	//            //����IUFO����
	//            case ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO:
	//                //�ɹ���õ����ؼ�Ҫչ�ֵ�cellsModel
	//                returnObj = LinkServletUtil.linkTableInputOperServlet(ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO,getTransObj(),null);
	//                break;
	//            //���汨��
	//            case ITableInputMenuType.MENU_TYPE_SAVE:
	//                //�õ����ؼ�¼������
	//                cellsModel = getCellsModel();
	//                //���棬�����򷵻ز����ɹ���־����
	//                returnObj = LinkServletUtil.linkTableInputOperServlet(ITableInputMenuType.MENU_TYPE_SAVE,getTransObj(),cellsModel);
	//                break;
	//            //�������
	//            case ITableInputMenuType.MENU_TYPE_CALCULATE_AREA:
	//                //�õ����ؼ�¼������
	//                cellsModel = getCellsModel();
	//                //������㣬�ɹ��򷵻ز����ɹ���־����
	//                returnObj = LinkServletUtil.linkTableInputOperServlet(ITableInputMenuType.MENU_TYPE_CALCULATE_AREA,getTransObj(),cellsModel);
	//                break;
	//            //����
	//            case ITableInputMenuType.MENU_TYPE_CALCULATE:
	//                //�õ����ؼ�¼������
	//                cellsModel = getCellsModel();
	//                //���㣬�ɹ��򷵻ز����ɹ���־����
	//                returnObj = LinkServletUtil.linkTableInputOperServlet(ITableInputMenuType.MENU_TYPE_CALCULATE,getTransObj(),cellsModel);
	//            default:
	//                break;
	//        }
	//            
	//        return returnObj;
	//    }
	//  /**
	//   * ��ñ��ؼ��е�CellsModel
	//   * @return 
	//   */
	//  private CellsModel getCellsModel() {
	//      if(m_oUfoReport != null){
	//          m_oUfoReport.stopCellEditing();//ǿ�н������ؼ�¼��״̬������û��ʧȥ���������
	//          CellsModel cellsModel = m_oUfoReport.getTable().getSheet(0).getModel();
	//          return cellsModel;
	//      }
	//      return null;
	//  }
	/**
	 *��ie���á�
	 */
	public String save() {
//		if (isRepDataByRepView()) {
//			UfoPublic.showErrorDialog(this, "ϵͳ������¼���ۺϲ�ѯ���ܰ汾���ݡ�", MultiLang
//					.getString("error"));
//		}

		new SaveRepDataCmd(getUfoReport()).execute(null);
		return "";
	}
}
 