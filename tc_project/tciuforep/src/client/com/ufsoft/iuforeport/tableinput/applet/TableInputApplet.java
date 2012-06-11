/*
 * 创建日期 2005-6-28
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
	 * 初始化
	 */
	public void init() {
		
		//modify by ljhua 2007-3-9 修改顺序，解决在InputFilePlugIn启动时，弹出关键字录入对话框
		//初始化表格控件的一些状态
		initTableInputCtrl();

		//如未设置当前任务，则提示“当前任务未设置”，解决综合查询未设置当前任务时白屏问题
		if (!isSetupUserTask()) {
			javax.swing.JOptionPane.showMessageDialog(getRootPane(),
					StringResource.getStringResource("miufo76")); //未设置当前任务
			return;
		}

		//初始化表格控件对象及其插件
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
			//表格空间不显示（根据报表数据装载）
			getUfoReport().getTable().setVisible(false);
		}

		//设置导航面板属性 add by wangyga 2008-7-10 
		setReportNavPanel();

		//得到表格控件要展现的cellsModel 和　菜单状态的数据
		//      int nBizType = ITableInputMenuType.MENU_TYPE_OPEN;
		//      InputOpenRepOper inputOpenRepOper = new InputOpenRepOper(getUfoReport());
		//      inputOpenRepOper.performBizTask(nBizType);

		//修改报表打开方式，以适应综合查询增加查询条件，如版本等
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
	 * 初始化表格控件的一些状态
	 */
	private void initTableInputCtrl() {
		//准备代码参照录入的环境,
		String strParamReportData = getParameter(ITableInputAppletParam.PARAM_OPERTYPE);
		if (TableInputParam.OPERTYPE_REPDATA_INPUT.equals(strParamReportData)
				|| TableInputParam.OPERTYPE_REPDATA_EDIT_REP
						.equals(strParamReportData)
				|| TableInputParam.OPERTYPE_REPDATA_EDIT_TASK
						.equals(strParamReportData)) {
			RefData.setProxy(new IufoRefData());
		}

		//参照类型显示编码还是名称的问题:true,false
		String strShowRefID = getParameter(ITableInputAppletParam.PARAM_TI_SHOWREFID);
		boolean bShowRefID = "true".equalsIgnoreCase(strShowRefID) ? true
				: false;
		ReportStyle.setShowRefID(bShowRefID);
		//add by 王宇光 对报表公式单元是否允许编辑的控制，在切换报表时已经做了控制，此处不必再控制
		//公式是否可录入:true,false
		//String strFormulaCanInput = getParameter(ITableInputAppletParam.PARAM_TI_FORMULACANINPUT);
		//boolean bFormulaCanInput = "true".equalsIgnoreCase(strFormulaCanInput) ? true
		//		: false;
		//MeasureFmt.setCanInput(bFormulaCanInput);

		//数据为0时是否显示:true,false
		String strDisplayZero = getParameter(ITableInputAppletParam.PARAM_TI_DISPLAYZERO);
		boolean bDisplayZero = "true".equalsIgnoreCase(strDisplayZero) ? true
				: false;
		ReportStyle.setShowZero(bDisplayZero);
		
		//汇总结果是否允许手工修改
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
		if(pubDataVo != null && pubDataVo.getVer() >=1000){//设为平衡表时，不显示查询面板
				getUfoReport().getReportNavPanel().getNorthComp().setVisible(false);
		}
				
		//初时不显示审核结果面板
		//		if(getUfoReport().getReportNavPanel().getSouthComp() != null)
		//			getUfoReport().getReportNavPanel().getSouthComp().setVisible(true);
		//设置导航面板默认显示
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
	 * 得到表格控件传输信息对象
	 * @return
	 */
	private TableInputTransObj geneTransObj() {
		TableInputTransObj m_oTransObj = new TableInputTransObj();
		if (isReportData()) {
			IRepDataParam oRepDataParam = new RepDataParam();
			//报表PK
			String strReportPK = getParameter(REPORT_PK);
			if (strReportPK == null || strReportPK.length() > 0) {
				oRepDataParam.setReportPK(strReportPK);
			}
			//操作类型operType
			String strOperType = getParameter(ITableInputAppletParam.PARAM_OPERTYPE);
			oRepDataParam.setOperType(strOperType);
			//操作类型operType
			String strIsNeedFilterDataRight = getParameter(ITableInputAppletParam.PARAM_NEEDFILTER_DR);
			oRepDataParam.setIsNeedFilterDataRight(new Boolean(
					strIsNeedFilterDataRight).booleanValue());
			//AloneID
			String strAloneID = getParameter(ITableInputAppletParam.PARAM_ALONEID);
			oRepDataParam.setAloneID(strAloneID);
			//操作单位PK
			String strOperUnitPK = getParameter(CUR_UNIT_ID);
			oRepDataParam.setOperUnitPK(strOperUnitPK);
			//操作用户PK
			String strOperUserPK = getParameter(CUR_USER_ID);
			oRepDataParam.setOperUserPK(strOperUserPK);
			//组织体系
			String strOrgPK = getParameter(ORG_PK);
			oRepDataParam.setOrgPK(strOrgPK);
			//报表所属任务PK
			String strTaskPK = getParameter(TASK_PK);
			oRepDataParam.setTaskPK(strTaskPK);
			//是否合并数据
			String strIsHBBBData = getParameter(ITableInputAppletParam.PARAM_IS_HBBBDTA);
			oRepDataParam.setIsHBBBData(new Boolean(strIsHBBBData)
					.booleanValue());
			//导入IUFO数据的ImportAloneID
			String strImportAloneID = getParameter(ITableInputAppletParam.PARAM_IMPORT_ALONEID);
			oRepDataParam.setImportAloneID(strImportAloneID);
			//数据源信息
			DataSourceInfo curDSInfo = getCurDataSourceInfo();
			oRepDataParam.setDSInfo(curDSInfo);
			//＃设置
			m_oTransObj.setRepDataParam(oRepDataParam);
			m_oTransObj.setType(TableInputTransObj.TYPE_REPDATA);
		} else if (isMQuery()) {
			String strMQueryPK = getParameter(ITableInputAppletParam.PARAM_MQUERY_PK);
			//＃设置
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
			//＃设置
			m_oTransObj.setHBDraftParam(oHBDraftParam);
			m_oTransObj.setType(TableInputTransObj.TYPE_HBDRAFT);

			//操作用户PK
			IRepDataParam oRepDataParam = new RepDataParam();
			String strOperUserPK = getParameter(CUR_USER_ID);
			oRepDataParam.setOperUserPK(strOperUserPK);
			//组织体系
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

			//＃设置
			m_oTransObj.setPrintParam(oPrintParam);
			m_oTransObj.setType(TableInputTransObj.TYPE_PRINT);
		}
		//当前登录时间
		String strCurLoginDate = getParameter("CurrentDate");
		m_oTransObj.setCurLoginDate(strCurLoginDate);
		m_oTransObj.setLoginUnit(getParameter(CUR_UNIT_CODE));

		//用户选择的语种编码
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
	 * 获得数据源信息
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
	 * 是否为指标查询
	 * @return
	 */
	private boolean isMQuery() {
		return getParameter(ITableInputAppletParam.PARAM_MQUERY_PK) != null;
	}

	/**
	 * 是否为合并工作底稿
	 * @return
	 */
	private boolean isHBDraft() {
		return getParameter(ITableInputAppletParam.PARAM_HB_DRAFTTYPE) != null;
	}

	/**
	 * 是否为报表数据状态
	 * @return
	 */
	private boolean isReportData() {
		return getParameter(ITableInputAppletParam.PARAM_REPDATA) != null;
	}

	/**
	 * 是否为打印功能
	 * @return
	 */
	private boolean isPrint() {
		return getParameter(ITableInputAppletParam.PARAM_PRINT_NTYPE) != null
				|| getParameter(ITableInputAppletParam.PARAM_PRINT_ALONE_ID) != null;
	}

	/**
	 * 是否需要显示表格
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
	 * 是否需要显示表格
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
	 * 获得报表的环境信息
	 * @return
	 */
	private ContextVO geneInitContext() {
		TableInputContextVO inputContextVO = new TableInputContextVO(
				new UfoContextVO(), geneTransObj());
		inputContextVO.setAttribute(CUR_UNIT_CODE, getParameter(CUR_UNIT_CODE));
		inputContextVO.setAttribute(LOGIN_UNIT_CODE, getParameter(CUR_UNIT_CODE));
		inputContextVO.setAttribute(ORG_PK, getParameter(ORG_PK));
		//＃是否显示报表树
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
	//	 * 获得用来显示表格控件的主Panel
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
	//     * 每当部件抛出异常时被调用
	//     * @param exception java.lang.Throwable
	//     */
	//    private void handleException(java.lang.Throwable exception){
	//
	//        /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	//        // System.out.println("--------- 未捕捉到的异常 ---------");
	//        // exception.printStackTrace(System.out);
	//    }

	//    /**
	//     * 设置脏标记值
	//     * @return
	//     */
	//    private void setDirty(boolean bDirty){
	//    	if(m_oUfoReport != null && !bDirty){
	//    		m_oUfoReport.store();
	//    	}
	//    }

	/**
	 * 是否设置当前任务
	 */
	private boolean isSetupUserTask() {
		String strTaskPK = getParameter(TASK_PK);
		if (strTaskPK == null || strTaskPK.trim().length() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 是否打开报表数据
	 * 报表工具支持按任务查看报表数据、查看指定原表数据、查看合并报表数据、综合查询接口等
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
	 * 是否脏标记为true
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
			//    	        //打开报表
			//    	        nIndex++;
			//保存
			SaveRepDataExt saveRepDataExt = (SaveRepDataExt) extensions[nIndex];
			boolean isSaveRepEnable = saveRepDataExt.isEnabledSelf();
			System.out.println("[iufodebug]: isSaveRepEnable = "
					+ isSaveRepEnable);
			return isSaveRepEnable && getUfoReport().isDirty();
		}
		return false;
	}

	//    /**
	//     * 准备CellsModel进入录入状态
	//     */
	//    private void doAfterCellsModel() {        
	//        //表格控件是否只读：true,false
	//        String strReadOnly = getParameter(ITableInputAppletParam.PARAM_TI_READONLY);
	//        boolean bReadOnly = "true".equalsIgnoreCase(strReadOnly)?true:false;
	//        m_oUfoReport.setReadOnly(bReadOnly);
	//        
	//    }
	//
	//	/* （非 Javadoc）
	//	 * @see com.ufsoft.iuforeport.tableinput.ITableInputMenuOper#performMenuTask(int)
	//	 */
	//	public Object performMenuTask(String strMenuTypeInt) {
	//		Object oReturn = "performMenuTask(" + strMenuTypeInt+ ") int TableInputApplet.java";
	//		debug(oReturn.toString());
	//		
	//		//得到表格控件要展现的cellsModel
	//		int nMenuType = new Integer(strMenuTypeInt).intValue();
	//		if(nMenuType == ITableInputMenuType.MENU_TYPE_SAVE){
	//		    InputCorePlugin inputCorePlugin = (InputCorePlugin)m_oUfoReport.getPluginManager().getPlugin(InputCorePlugin.class.getName());
	//		    if(!inputCorePlugin.verifyBeforeSave()){
	//		        oReturn = Boolean.valueOf(false);
	//		    }
	//		}		
	//
	////		if(nMenuType == ITableInputMenuType.MENU_TYPE_CALCULATE_U8ACC){
	////			//NOTICE: IUFO3.1SP版本中，U8Acc暂时不支持
	////		  	//含U8离散数据公式的计算时,先坐一次预计算：根据当前录入数据和报表格式，得到U8的离散公式集合
	////			//ArrayList listU8AccFuncs = (ArrayList)doPerformMnnuTask(ITableInputMenuType.MENU_TYPE_CALCULATE_U8Acc);
	//////		    testUsingJSObject();		    
	////			//弹出U8离散数据源配置信息对话框
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
	//	 * 获得所在IE窗口的window对象。
	//	 *
	//	 * 创建日期：(2002-10-31 14:32:45)
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
		////			win.eval("testSelfFunc('hello,testSelfFunc'");  //eval不能传参数
		//			win.eval("alert(\"This alert comes from Java end!\")");  
		//			
		//			
		//			JSObject all = (JSObject)doc.getMember("all");
		//			System.out.println("all =" + all.getClass() + "," + all.toString());
		//			JSObject jsObjIufoForU81 = (JSObject)all.call("item", new Object[]{"IufoForU81"});
		//			if(jsObjIufoForU81!=null){
		//				//IufoLogin(ver.value, account.value, year.value,user.value, password.value,address.value,form1.hid_acctime.value)
		//				//输入参数获取值并进行合法性检验？
		//				String strAddress = "10.7.5.52";
		//				Boolean bLogin = (Boolean)jsObjIufoForU81.call("IufoLogin", new Object[]{"8.60SQL", "testAccount", "2005", "liulp","iufo",strAddress,"2005-08-31"});
		////				bLogin = new Boolean("true"); 
		//				if(!bLogin.booleanValue()){
		//					debug("U8控件，登录失败！");
		//				}
		//				int iFuncCount = 2;
		//				String[] strFormulas = new String[]{"",""};//FormulaContent,str=form1.hid_u8_form_text.value
		//				String[] strValues = new String[2];
		//				debug("U8返回的值＝＝＝＝＝＝＝＝");
		//				for(int i = 0;i<iFuncCount;i++){
		//					if(strFormulas[i] ==null){
		//						debug("没有定义的取数公式:" + i);
		//						continue;
		//					}
		//					strValues[i] = accessData(jsObjIufoForU81,strFormulas[i],strAddress);
		//					debug("第["+i+"个]="+strValues[i]);
		//				}
		//			}
		//		}
		//		catch (Exception e) {
		//			e.printStackTrace(System.out);
		//		}
	}

	//	/**
	//	 * 计算U8公式
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
	//	 * 调试状态时，弹出执行公共方法的返回信息
	//	 * @param strSaveErrMsg
	//	 */
	//	private void showErrorMsg(String strAlertMsg,boolean bInitMenuType) {
	//	    boolean bErrorMsg = false;
	//	    String strReason = strAlertMsg;
	//	    if(strAlertMsg!=null && !strAlertMsg.equals("true")){
	//	        bErrorMsg = true;
	//	    }
	//        if(isDebug()){
	//            strReason = "debuging...返回信息为--" + strReason;
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
	//     * 执行菜单任务
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
	//            //#查看数据来源暂不支持
	//            DynAreaPlugin dynAreaPlugin = (DynAreaPlugin)m_oUfoReport.getPluginManager().getPlugin(DynAreaPlugin.class.getName());
	//
	//            CellsModel curCellsModel = getCellsModel();
	//            CellPosition selCellPos = curCellsModel.getSelectModel().getAnchorCell();
	//            CellPosition selOrigCellPos = dynAreaPlugin.getOrigCellPos(selCellPos);
	//
	//            if(selCellPos == null || selOrigCellPos == null){
	//                String strAlertMsg = StringResource.getStringResource("miufotableinput000006");//"必须选择有效单元才能查看来源!";     
	//                javax.swing.JOptionPane.showMessageDialog(
	//                        this,
	//                        strAlertMsg);
	//                return "";
	//            }
	//            
	//            //得到动态区域当前行的关键字值集合
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
	//                    String strAlertMsg = StringResource.getStringResource("miufotableinput000007");//"选择的动态区域行必须先填写关键字的值才能查看来源!"
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
	//     * 执行连接Servlet的操作
	//     * @param nMenuType
	//     * @return
	//     */
	//    public Object doLinkServletTask(int nMenuType){
	//        Object returnObj = null;
	//        CellsModel cellsModel = null;
	//        switch(nMenuType){
	//            //打开报表
	//            case ITableInputMenuType.MENU_TYPE_OPEN:
	//                //成功则得到表格控件要展现的cellsModel
	//                returnObj = LinkServletUtil.linkTableInputOperServlet(ITableInputMenuType.MENU_TYPE_OPEN,getTransObj(),null);
	//                break;
	//            //导入IUFO数据
	//            case ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO:
	//                //成功则得到表格控件要展现的cellsModel
	//                returnObj = LinkServletUtil.linkTableInputOperServlet(ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO,getTransObj(),null);
	//                break;
	//            //保存报表
	//            case ITableInputMenuType.MENU_TYPE_SAVE:
	//                //得到表格控件录入数据
	//                cellsModel = getCellsModel();
	//                //保存，返回则返回操作成功标志变量
	//                returnObj = LinkServletUtil.linkTableInputOperServlet(ITableInputMenuType.MENU_TYPE_SAVE,getTransObj(),cellsModel);
	//                break;
	//            //区域计算
	//            case ITableInputMenuType.MENU_TYPE_CALCULATE_AREA:
	//                //得到表格控件录入数据
	//                cellsModel = getCellsModel();
	//                //区域计算，成功则返回操作成功标志变量
	//                returnObj = LinkServletUtil.linkTableInputOperServlet(ITableInputMenuType.MENU_TYPE_CALCULATE_AREA,getTransObj(),cellsModel);
	//                break;
	//            //计算
	//            case ITableInputMenuType.MENU_TYPE_CALCULATE:
	//                //得到表格控件录入数据
	//                cellsModel = getCellsModel();
	//                //计算，成功则返回操作成功标志变量
	//                returnObj = LinkServletUtil.linkTableInputOperServlet(ITableInputMenuType.MENU_TYPE_CALCULATE,getTransObj(),cellsModel);
	//            default:
	//                break;
	//        }
	//            
	//        return returnObj;
	//    }
	//  /**
	//   * 获得表格控件中的CellsModel
	//   * @return 
	//   */
	//  private CellsModel getCellsModel() {
	//      if(m_oUfoReport != null){
	//          m_oUfoReport.stopCellEditing();//强行结束表格控件录入状态，接收没有失去焦点的数据
	//          CellsModel cellsModel = m_oUfoReport.getTable().getSheet(0).getModel();
	//          return cellsModel;
	//      }
	//      return null;
	//  }
	/**
	 *供ie调用。
	 */
	public String save() {
//		if (isRepDataByRepView()) {
//			UfoPublic.showErrorDialog(this, "系统不允许录入综合查询汇总版本数据。", MultiLang
//					.getString("error"));
//		}

		new SaveRepDataCmd(getUfoReport()).execute(null);
		return "";
	}
}
 