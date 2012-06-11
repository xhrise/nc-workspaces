/*
 * �������� 2006-4-7
 *
 */
package com.ufsoft.iufo.inputplugin.biz;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;

import nc.ui.pub.beans.UIFileChooser;
import nc.ui.sm.login.LoginNCForIUFO;
import nc.ui.sm.login.NCAppletStub;
import nc.util.iufo.pub.UFOString;
import nc.vo.iufo.repdataright.RepDataRightVO;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.pluginregister.TotalSourcePluginRegister;
import com.ufsoft.iufo.inputplugin.biz.data.CheckResultExt;
import com.ufsoft.iufo.inputplugin.biz.data.CheckResultPanel;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iufo.inputplugin.biz.file.TableInputSearchOper;
import com.ufsoft.iufo.inputplugin.biz.file.TraceDataResultDlg;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizUtil;
import com.ufsoft.iufo.inputplugin.inputcore.InputCorePlugin;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iuforeport.tableinput.TraceDataResult;
import com.ufsoft.iuforeport.tableinput.TraceableFuncInfo;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.LinkServletUtil;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputException;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UICloseableTabbedPane;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.extfunc.HRTraceDataResult;
import com.ufsoft.table.CellsModel;

/**
 * ���ؼ�����IUFOҵ������ӿڵ�ʵ����
 * 
 * @author liulp
 *
 */
public class InputBizOper implements IInputBizOper, IUfoContextKey{
	public static final String SPLIT_TABLEINPUT = ",\\,";
	public static final String KEYSPLIT_TABLEINPUT = ",\\,";
	public static final String FORMATMODEL_IN_SHOWMODEL_WHEN_DATAPROCESS = "formatModel_in_showModel_when_dataProcess";
	
	private UfoReport m_oUfoReport = null;

	/**
	 * @param tableInputTransObj
	 * @param cellsModel
	 */
	public InputBizOper(UfoReport ufoReport) {
		super();
		m_oUfoReport = ufoReport;
	}

	protected UfoReport getUfoReport() {
		return m_oUfoReport;
	}

	protected CellsModel getCellsModel() {
		return getUfoReport().getCellsModel();
	}

	protected TableInputTransObj getTransObj() { 
		return doGetTransObj(getUfoReport());
	}

	public static TableInputTransObj doGetTransObj(UfoReport ufoReport) {
		Context tableInputContextVO = (Context) ufoReport
				.getContextVo();
		if (tableInputContextVO != null) {
			Object tableInputTransObj = tableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
			TableInputTransObj tableInput = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
			
			return tableInput;
		}
		return null;
	}

	/**
	 * ��ӡ������Ϣ
	 * @param strDebugInfo
	 */
	private void debug(String strDebugInfo) {
		//if(isDebug()){
		AppDebug.debug(strDebugInfo);//@devTools System.out.println(strDebugInfo);
		//}
	}

	public Object performBizTask(int nBizType) {
		String strDebug = "performMenuTask(" + nBizType
				+ ") in " + this.getClass().getName();
		debug(strDebug);

		Object oCheck = preCheck(nBizType);

		if (oCheck == null) {
			strDebug = (String) doPerformMenuTask(nBizType);
		} else {
			strDebug = "false";
		}

		return strDebug;
	}

	private boolean isDebug() {
		return false;
	}

	/**
	 * ִ������ǰ�ļ��
	 * @return
	 */
	protected Object preCheck(int nBizType) {
		if (nBizType == ITableInputMenuType.MENU_TYPE_SAVE) {
			InputCorePlugin inputCorePlugin = (InputCorePlugin) getUfoReport()
					.getPluginManager().getPlugin(
							InputCorePlugin.class.getName());
			if (!inputCorePlugin.verifyBeforeSave()) {
				return Boolean.valueOf(false);
			}
		}
		return null;
	}

	/**
	 * ��������ֵ
	 * @return
	 */
	private void setDirty(boolean bDirty) {
		if (getUfoReport() != null && !bDirty) {
			getUfoReport().store();
		}
	}

	/**
	 * ִ�в˵�����
	 * @param nMenuType
	 * @return
	 */
	private Object doPerformMenuTask(int nMenuType) {
		String strReturn = null;
		Object transReturnObj = doLinkServletTask(nMenuType);
		innerPerformFilterDynArea(transReturnObj, nMenuType);
		strReturn = dealTransReturnObj(transReturnObj, nMenuType);
		if(strReturn != null){
			int chIndex = strReturn.indexOf("@");
			String strOperReturn = (chIndex > 0)?strReturn.substring(0, chIndex):strReturn;
			if (nMenuType == ITableInputMenuType.MENU_TYPE_SAVE
					&& "true".equals(strOperReturn)) {
				setDirty(false);
			}
		}
		return strReturn;
	}

	protected void innerPerformFilterDynArea(Object transReturnObj,
			int nMenuType) {
		if (transReturnObj == null
				|| transReturnObj instanceof Object[] == false)
			return;

		Object[] transObjs = (Object[]) transReturnObj;
		if (transObjs.length < 3 || transObjs[2] == null
				|| transObjs[2] instanceof Object[] == false)
			return;

		Object[] otherObjs = (Object[]) transObjs[2];
		if (otherObjs.length <= 0)
			return;

		InputCorePlugin inputCorePlugin = (InputCorePlugin) getUfoReport()
				.getPluginManager().getPlugin(InputCorePlugin.class.getName());

		//����operType�����в�ͬ�Ĳ������õ���ͬ�ķ���ֵ
		switch (nMenuType) {
				//��ѯ��������
			case ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATA:
			case ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATASUBMIT:	
				if (otherObjs.length > 2)
					inputCorePlugin
							.setHashDynAloneID((Hashtable<String, Vector<String>>) otherObjs[2]);							
				break;
				//����Excel����
			case ITableInputMenuType.MENU_TYPE_IMPORTDATA_EXCEL:
				//�򿪱���
			case ITableInputMenuType.MENU_TYPE_OPEN:
				if (otherObjs.length > 1)
					inputCorePlugin
							.setHashDynAloneID((Hashtable<String, Vector<String>>) otherObjs[1]);						
				break;
			//���汨��
			case ITableInputMenuType.MENU_TYPE_SAVE:
					inputCorePlugin
							.setHashDynAloneID((Hashtable<String, Vector<String>>) otherObjs[0]);
				break;
			//�л��ؼ��ֵ��ύ    
			case ITableInputMenuType.BIZ_TYPE_CHANGEKEYWORDDSUBMIT:
				if (otherObjs.length > 2) {
					inputCorePlugin
							.setHashDynAloneID((Hashtable<String, Vector<String>>) otherObjs[2]);
				}							
				break;
			default:
				break;
		}
	}

	protected boolean checkReturnObjs(Object returnObjs, StringBuffer sbErrMsg) {
		boolean bError = false;
		if (isTableInputException(returnObjs)) {
			bError = true;
			sbErrMsg.append(((TableInputException) returnObjs).getMessage());
		} else if (returnObjs instanceof Object[]) {
			Object[] retObjects = (Object[]) returnObjs;
			int iLen = retObjects.length;
			for (int i = 0; i < iLen; i++) {
				if (retObjects[i] instanceof Object[]) {
					return checkReturnObjs(retObjects[i], sbErrMsg);
				}
				if (isTableInputException(retObjects[i])) {
					bError = true;
					sbErrMsg.append(((TableInputException) retObjects[i])
							.getMessage());
					sbErrMsg.append("\r\n");
				}
			}

		}
		return bError;

	}

	private boolean isTableInputException(Object srcObject) {
		return (srcObject instanceof TableInputException);
	}

	/**
	 * 
	 * @param returnObjs
	 * @param nMenuType
	 * @return ���ظ�html����ʾ��Ϣ
	 * @i18n uiuforep00117=��û�д򿪱��������Ȩ�ޣ����飡
	 * @i18n uiuforep00118=��ǰ�����ʵ������(
	 * @i18n uiuforep00119=)����������ƣ�ֻ��ʾ
	 * @i18n uiuforep00120=�����ݣ�
	 * @i18n uiuforep00121=��ǰ�����ʵ������(
	 * @i18n uiuforep00122=�����ݣ�
	 */
	protected String dealTransReturnObj(Object returnObjs, int nMenuType) {
		String strDebugReturn = "";
		if (returnObjs == null || !(returnObjs instanceof Object[])) {
			strDebugReturn = MultiLangInput.getString("uiufotableinput0026");//"ϵͳ�����������ͨѶʧ�ܣ�"
			return strDebugReturn;
		}
		StringBuffer sbErrorRetObjs = new StringBuffer();
		if (checkReturnObjs(returnObjs, sbErrorRetObjs)) {
			strDebugReturn = sbErrorRetObjs.toString();
			return strDebugReturn;
		}

		Object[] transReturnObjs = (Object[]) returnObjs;
		Object transContextObj = transReturnObjs[0];
		Object transCoreReturnObj = transReturnObjs[1];
		//��������Ȩ�޿���
		int[] iRightType = null;
		CellsModel dataModel = null;
		if(transCoreReturnObj instanceof CellsModel){
			dataModel = (CellsModel)transCoreReturnObj;
		} else if (transCoreReturnObj instanceof Object[] && 
				((Object[])transCoreReturnObj)[0] != null && ((Object[]) transCoreReturnObj)[0] instanceof CellsModel) {
			iRightType = (int[]) ((Object[]) transCoreReturnObj)[1];
			if (iRightType[0] < RepDataRightVO.RIGHT_TYPE_VIEW || 
					!(((Object[]) transCoreReturnObj)[0] instanceof CellsModel)) {
				dataModel = null;
				javax.swing.JOptionPane.showMessageDialog(getUfoReport(), MultiLang.getString("uiuforep00117"));
				TableInputSearchOper.createEmptyTable(getUfoReport());
    			return "true";
			}
			dataModel = (CellsModel) ((Object[]) transCoreReturnObj)[0];
		}
		//#�����ص�UfoContextVO,��ɶ� UfoReport��һЩ����

		//#�����صĺ���ֵ�����ظ�ҳ����ʾ����Ϣ����
		debug("returnObj's class =" + transCoreReturnObj.getClass().getName());
		if (dataModel != null && dataModel instanceof CellsModel) {
			CellsModel cellsModel = (CellsModel) dataModel;
			//debug("���ؼ���Ҫˢ�£� cellsModel = " + cellsModel.toString());           
			TableInputTransObj tableInputTransObj = getTransObj();
			//����̬Ԥ��ʱ�����ݸ�ʽģ����������
			if (tableInputTransObj.isDataExplore()) {
				CellsModel formatModel = (CellsModel) m_oUfoReport.getCellsModel().getClientProperty(FORMATMODEL_IN_SHOWMODEL_WHEN_DATAPROCESS);
				if(formatModel!=null){
				cellsModel.setClientProperty(FORMATMODEL_IN_SHOWMODEL_WHEN_DATAPROCESS, formatModel);
				CellsModelOperator.applyNewFormatModel2DataModel(formatModel,
						cellsModel);
				}
			}
			
			//add by  ����� �������ݵ���Ϣ����,��ӱ���ʽ��Ԫ�Ƿ�����༭����,����жϵ�����
			if (nMenuType == ITableInputMenuType.MENU_TYPE_OPEN
					|| nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATA) {				 
				Object[] aryTransCoreReturnObj = (Object[]) transCoreReturnObj;
				if (aryTransCoreReturnObj != null
						&& aryTransCoreReturnObj.length == 3) {
					if(aryTransCoreReturnObj[2]!=null){
						Boolean bIsCanPut = (Boolean) aryTransCoreReturnObj[2];
						MeasureFmt.setCanInput(bIsCanPut);
					}					
				}	   
			}
			//end
			
			//ˢ�±��ؼ�չ��
			WindowNavUtil.refreshCellsModel(getUfoReport(), cellsModel,
					transContextObj);
			//�������ģ�͸��£���ˢ���Զ�������
			InputAutoCalcPlugIn pi = (InputAutoCalcPlugIn)getUfoReport().getPluginManager().getPlugin(InputAutoCalcPlugIn.class.getName());
        	if(pi != null && pi.getAutoCalcUtil() != null){
        		pi.getAutoCalcUtil().resetCellsModel(getUfoReport().getCellsModel());
        	}
			//������refreshCellsModel֮����������
			if (nMenuType == ITableInputMenuType.MENU_TYPE_OPEN 
					|| nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_REPS
					|| nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_VERS
					|| nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATA
					|| nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_SUBVERS
					|| nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATASUBMIT) {
				getUfoReport().getTable().setVisible(true);
				getUfoReport().revalidate();//�����ˢ����ʾ
				cellsModel.setDirty(false);
			} else if (nMenuType == ITableInputMenuType.MENU_TYPE_CALCULATE
					|| nMenuType == ITableInputMenuType.MENU_TYPE_CALCULATE_AREA
					|| nMenuType == ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO) {
				cellsModel.setDirty(true);//��֤����󣬹رձ�����ʾ���档
			}

			//���������λ�汾���ݣ�����ʾ�������
			if(!GeneralQueryUtil.isGeneralQuery(this.getUfoReport().getContext()) &&
					GeneralQueryUtil.isSWDataVer(this.getUfoReport())){
				String strMenuName = MultiLang.getString("uiuforep00008"); //"��ѯ����"
				String[] strParantMenuNames = new String[]{MultiLang.getString("window"),MultiLang.getString("panelManager")};
				JCheckBoxMenuItem menuItem = FormulaTraceBizUtil.getMenuItem(strMenuName, strParantMenuNames, this.getUfoReport());
				if(menuItem != null){
					menuItem.setSelected(false);
					menuItem.setEnabled(false);
				}
			}
			
			strDebugReturn = "true";

			//modify by chxw 2007-07-03 ����ģ�͵�����������ͬʵ������ģ�������в�һ��ʱ��������ʾ
			StringBuffer strPromptMsgBuf = new StringBuffer();
			if (cellsModel.getRealDataRowNum() > cellsModel.getRowNum()) {
				strPromptMsgBuf.append(MultiLang.getString("uiuforep00118"));
				strPromptMsgBuf.append(cellsModel.getRealDataRowNum());
				strPromptMsgBuf.append(MultiLang.getString("uiuforep00119"));
				strPromptMsgBuf.append(cellsModel.getRowNum());
				strPromptMsgBuf.append(MultiLang.getString("uiuforep00120"));
			}
			if (cellsModel.getRealDataColNum() > cellsModel.getColNum()) {
				if (strPromptMsgBuf.length() > 0) {
					strPromptMsgBuf.append("\n");
				}
				strPromptMsgBuf.append(MultiLang.getString("uiuforep00121"));
				strPromptMsgBuf.append(cellsModel.getRealDataColNum());
				strPromptMsgBuf.append(MultiLang.getString("uiuforep00119"));
				strPromptMsgBuf.append(cellsModel.getColNum());
				strPromptMsgBuf.append(MultiLang.getString("uiuforep00122"));
			}
			if (strPromptMsgBuf.length() > 0) {
				strDebugReturn = strPromptMsgBuf.toString();
			}
		} else if (transCoreReturnObj instanceof TableInputException) {
			//������Ϣ����
			strDebugReturn = ((TableInputException) transCoreReturnObj)
					.getMessage();
			debug("performMenuTask():   " + strDebugReturn);
		}
		//      else if(transReturnObj instanceof String){
		//          //U8��ɢ����
		//          strReturn = (String)transReturnObj;//"UBAcc"
		//      }
		else if (transCoreReturnObj instanceof Boolean) {
			//�޸�����
			Boolean bReturnObj = (Boolean) transCoreReturnObj;
			if (bReturnObj.booleanValue()
					&& nMenuType == ITableInputMenuType.MENU_TYPE_SAVE) {
				//�޸ı��ؼ�����
				getUfoReport().setDirty(false);
			}
			//boolean��str����
			strDebugReturn = bReturnObj.toString();
		} else if (nMenuType == ITableInputMenuType.MENU_TYPE_CHECK_IN
				|| nMenuType == ITableInputMenuType.MENU_TYPE_CHECK_BETWEEN
				|| nMenuType == ITableInputMenuType.MENU_TYPE_SAVE) {
			//(�Զ�)��˽��

			//modify by ljhua 2007-3-13 ����˽���ŵ��¶������ʾ
			CheckResultVO checkResultVO = (CheckResultVO) transCoreReturnObj;
			setCheckResult(checkResultVO);
			if (checkResultVO.getCheckState() == CheckResultVO.PASS) {
				strDebugReturn = "true";
			} else if (checkResultVO.getCheckState() == CheckResultVO.NOPASS) {
				strDebugReturn = "false";
			}
			if(nMenuType == ITableInputMenuType.MENU_TYPE_SAVE){
				strDebugReturn = "true@" + strDebugReturn;
			}
			//            String strNoteLocal = (String)transCoreReturnObj;
			//            if( strNoteLocal.length() > 0){
			////                javax.swing.JOptionPane.showMessageDialog(
			////                        getUfoReport(),strNoteLocal);
			//            	StringBuffer sbheckTitle = new StringBuffer();
			//            	if(nMenuType == ITableInputMenuType.MENU_TYPE_SAVE){
			//            		sbheckTitle.append(MultiLangInput.getString("uiufotableinput0028"));//"��ʾ��Ϣ"
			//            	}else{
			//            		if(nMenuType == ITableInputMenuType.MENU_TYPE_CHECK_BETWEEN){
			//            			sbheckTitle.append(MultiLangInput.getString("uiufotableinput0006"));//"������"
			//	            	}else if(nMenuType == ITableInputMenuType.MENU_TYPE_CHECK_IN){
			//	            		sbheckTitle.append(MultiLangInput.getString("uiufotableinput0005"));//"�������"            		
			//	            	}
			//            		sbheckTitle.append(":");
			//            		sbheckTitle.append(MultiLangInput.getString("uiufotableinput0027"));//"��˽��"
			//            	}
			//            	MessageDialog.showWarningDlg(getUfoReport(), sbheckTitle.toString(), strNoteLocal);
			//            }
		} else if (nMenuType == ITableInputMenuType.MENU_TYPE_EXPORT_EXCEL
				&& transCoreReturnObj instanceof HSSFWorkbook) {
			saveWorkBook2Local(getUfoReport(),
					(HSSFWorkbook) transCoreReturnObj, "xls");
		} else if (nMenuType == ITableInputMenuType.MENU_TYPE_EXPORT_HTML
				&& transCoreReturnObj instanceof String) {
			{
				String strHTMLContent = (String) transCoreReturnObj;
				saveHTML2Local(getUfoReport(), strHTMLContent, "htm");
			}
		} else if ((nMenuType == ITableInputMenuType.MENU_TYPE_VIEW_SUB || nMenuType == ITableInputMenuType.MENU_TYPE_VIEW_SOURCE)
				&& transCoreReturnObj instanceof TraceDataResult) {
			TraceDataResult traceResult = (TraceDataResult) transCoreReturnObj;
			treatTraceResult(traceResult, nMenuType);
			strDebugReturn = "true";
		} else if (nMenuType == ITableInputMenuType.BIZ_TYPE_FORMULATRACE_CAL
				&& transCoreReturnObj instanceof IFormulaTraceValueItem) {
			IFormulaTraceValueItem formulaTraceValueItem = (IFormulaTraceValueItem) transCoreReturnObj;
			//��ʽ׷��-����
			FormulaTraceOperBizHelper.doFormulaTraceCal(getUfoReport(),
					formulaTraceValueItem);
			strDebugReturn = "true";
		} else if (nMenuType == ITableInputMenuType.BIZ_TYPE_FORMULATRACE_MULTIVALUES) {
			//��ʽ׷��-��ȡ����Ķ�ֵ
			IFormulaTraceValueItem[] formulaTraceValueItems = (IFormulaTraceValueItem[]) transCoreReturnObj;
			FormulaTraceOperBizHelper.doFormulaTraceMultiValues(getUfoReport(),
					formulaTraceValueItems);
			strDebugReturn = "true";
		} else if(nMenuType == ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO){
			//����IUFO����
			String strMsg = (String)((Object[])transCoreReturnObj)[0];
			if(strMsg != null && strMsg.equals("true")){
				strMsg = "";
			}
			strDebugReturn = strMsg;
		} else if (nMenuType == ITableInputMenuType.MENU_TYPE_CHECKDS) {
			//���ù�����Դ
			String strMsg = (String)((Object[])transCoreReturnObj)[0];
			if(strMsg != null && strMsg.equals("true")){
				strMsg = "";
			}
			strDebugReturn = strMsg;
		}
		return strDebugReturn;
	}

	private void treatTraceResult(TraceDataResult result, int nMenuType) {
		TraceableFuncInfo[] funcs = result.getFuncs();
		if (funcs != null && funcs.length > 0) {
			traceNCData(result);
		} else if (result.getResult()!=null && result.getResult() instanceof HRTraceDataResult) {
			TraceDataResultDlg dlg = new TraceDataResultDlg(getUfoReport(),(HRTraceDataResult)result.getResult());
			dlg.show();
		} else if (result.getCellsModel() != null) {
			QueryNavigation
					.showReport(
//							new UfoReport(getUfoReport().getContextVo(), result
//									.getCellsModel(),
//									UfoReport.OPERATION_TOTALSOURCE, null),
							new UfoReport(UfoReport.OPERATION_TOTALSOURCE, getUfoReport().getContextVo(), result
									.getCellsModel(),
										new TotalSourcePluginRegister()),
							MultiLangInput
									.getString(nMenuType == ITableInputMenuType.MENU_TYPE_VIEW_SOURCE ? "miufotableinput0008"
											: "miufotableinput0010"), true,false);
		} else {
			UfoPublic.sendMessage(MultiLangInput
					.getString("miufotableinput0006"), getUfoReport());
		}
	}

	private void traceNCData(TraceDataResult result) {
		try {
			JApplet applet=getReportApplet(getUfoReport());
			if (applet==null)
				return;
			
			String strRegKey=result.getRegKey();
			TraceableFuncInfo[] funcs=result.getFuncs();
			String strFunc = "";
			for (int i = 0; i < funcs.length; i++) {
				strFunc += funcs[i].getStrFuncName() + "("
						+ funcs[i].getStrFuncParam() + ")\r\n";
			}
			strFunc = strFunc.trim();
			
			NCAppletStub ncStub=NCAppletStub.getInstance();
			if (!UFOString.compareString(ncStub.getParameter("ACCOUNT_ID"),result.getAccountID())
				|| !UFOString.compareString(ncStub.getParameter("CORP_ID"),result.getCorpPK())
				|| !UFOString.compareString(ncStub.getParameter("LANGUAGE"),result.getLang())
				|| !UFOString.compareString(ncStub.getParameter("USER_CODE"),result.getUserCode())
				|| !UFOString.compareString(ncStub.getParameter("WORK_DATE"),result.getLoginDate())){
				new LoginNCForIUFO().login(applet, strRegKey);
			}

			ClassLoader clsLoader = getClass().getClassLoader();
			Class c = clsLoader.loadClass("nc.ui.fi.uforeport.FunctionDetailDisplay");
			Method m = c.getMethod("invoke", new Class[] { String.class });
			m.invoke(c.newInstance(), strFunc);
		} catch (ClassNotFoundException e) {
			AppDebug.debug(e);
		} catch (SecurityException e) {
			AppDebug.debug(e);
		} catch (NoSuchMethodException e) {
			AppDebug.debug(e);
		} catch (IllegalArgumentException e) {
			AppDebug.debug(e);
		} catch (IllegalAccessException e) {
			AppDebug.debug(e);
		} catch (InvocationTargetException e) {
			AppDebug.debug(e);
		} catch (InstantiationException e) {
			AppDebug.debug(e);
		}
	}
	
	public static JApplet getReportApplet(UfoReport ufoReport){
		Container parent=ufoReport.getParent();
		while (parent!=null){
			if (parent instanceof JApplet)
				return (JApplet)parent;
			parent=parent.getParent();
		}
		return null;
	}
	
	

	/**
	 * copy from ExcelExpCmd
	 * �����ļ�ѡ����棬�������ɵ�Excel�ļ������û������ļ�
	 * @param parent
	 * @param workBook
	 * @param strFilePostfix
	 */
	public static void saveWorkBook2Local(Component parent,
			HSSFWorkbook workBook, String strFilePostfix) {
		if (parent == null || workBook == null) {
			return;
		}

		if (strFilePostfix == null) {
			strFilePostfix = "xls";
		}
		JFileChooser chooser = new UIFileChooser();
		ExtNameFileFilter xf = new ExtNameFileFilter(strFilePostfix);
		chooser.setFileFilter(xf);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showSaveDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			file = xf.getModifiedFile(file);
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(file);
				workBook.write(stream);
			} catch (FileNotFoundException e) {
				AppDebug.debug(e);//@devTools                 AppDebug.debug(e);
			} catch (IOException e) {
				AppDebug.debug(e);//@devTools                 AppDebug.debug(e);
			} finally {
				try {
					if (stream != null) {
						stream.close();
					}
				} catch (IOException e) {
					AppDebug.debug(e);//@devTools                     AppDebug.debug(e);
				}
			}
		}
	}

	/**
	 * �����ļ�ѡ����棬�������ɵ�HTML���ݵ��û������ļ�
	 * @param parent
	 * @param strHTMLContent
	 * @param strFilePostfix
	 */
	public static void saveHTML2Local(Component parent, String strHTMLContent,
			String strFilePostfix) {
		if (parent == null || strHTMLContent == null) {
			return;
		}

		if (strFilePostfix == null) {
			strFilePostfix = "htm";
		}
		JFileChooser chooser = new UIFileChooser();
		ExtNameFileFilter xf = new ExtNameFileFilter(strFilePostfix);
		chooser.setFileFilter(xf);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showSaveDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			file = xf.getModifiedFile(file);
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(file), "UTF-8"));
				writer.write(strHTMLContent);
			} catch (FileNotFoundException e) {
				AppDebug.debug(e);//@devTools                 AppDebug.debug(e);
			} catch (UnsupportedEncodingException e) {
				AppDebug.debug(e);//@devTools                 e.printStackTrace(System.out);
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		}
	}

	/**
	 * ִ������Servlet�Ĳ���
	 * @param nMenuType
	 * @return
	 */
	protected Object doLinkServletTask(int nMenuType) {
		return doLinkServletTask(nMenuType, getCellsModel(), getTransObj(),
				getInputCorePlugin(getUfoReport()));
	}

	public static Object doLinkServletTask(int nMenuType, UfoReport ufoReport,
			boolean bNeedCeelsModel) {
		if (ufoReport == null) {
			return new Boolean(false);
		}
		CellsModel cellsModel = null;
		if (bNeedCeelsModel) {
			cellsModel = ufoReport.getCellsModel();
		}
		return doLinkServletTask(nMenuType, cellsModel,
				doGetTransObj(ufoReport), getInputCorePlugin(ufoReport));
	}

	private static InputCorePlugin getInputCorePlugin(UfoReport ufoReport) {
		return (InputCorePlugin) ufoReport.getPluginManager().getPlugin(
				InputCorePlugin.class.getName());
	}

	/**
	 * ִ������Servlet�Ĳ���
	 * @param nMenuType
	 * @return
	 */
	public static Object doLinkServletTask(int nMenuType,
			CellsModel inCellsModel, TableInputTransObj inputTransObj,
			InputCorePlugin inputCorePlugin) {
		Object returnObj = null;
		CellsModel cellsModel = null;

		switch (nMenuType) {
		//�򿪱���
		case ITableInputMenuType.MENU_TYPE_OPEN:
			//�ɹ���õ����ؼ�Ҫչ�ֵ�cellsModel�����õ������˵�״̬������ 
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.MENU_TYPE_OPEN, inputTransObj, null,
					null);
			break;
		//����IUFO����
		case ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO:
			//�ɹ���õ����ؼ�Ҫչ�ֵ�cellsModel
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO,
					inputTransObj, null);
			break;
		//���汨��
		case ITableInputMenuType.MENU_TYPE_SAVE:
			//�õ����ؼ�¼������
			cellsModel = inCellsModel;
			//���棬�����򷵻ز����ɹ���־����
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.MENU_TYPE_SAVE, inputTransObj,
					cellsModel, new Object[] { inputCorePlugin
							.getHashDynAloneID() });
			break;
		//�������
		case ITableInputMenuType.MENU_TYPE_CALCULATE_AREA:
			//�õ����ؼ�¼������
			cellsModel = inCellsModel;
			//������㣬�ɹ��򷵻ؽ�����CellsModel
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.MENU_TYPE_CALCULATE_AREA,
					inputTransObj, cellsModel);
			break;
		//����
		case ITableInputMenuType.MENU_TYPE_CALCULATE:
			//�õ����ؼ�¼������
			cellsModel = inCellsModel;
			//���㣬�ɹ��򷵻ؽ�����CellsModel
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.MENU_TYPE_CALCULATE, inputTransObj,
					cellsModel);
			break;
		//�鿴��Դ
		case ITableInputMenuType.MENU_TYPE_VIEW_SOURCE:
			cellsModel = inCellsModel;

			if (inputTransObj.getTraceParam().isTotal() || inputTransObj.getTraceParam().getTraceScopes()[0].getFormula()!=null)
				cellsModel = null;
			//���㣬�ɹ��򷵻ؽ�����CellsModel
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.MENU_TYPE_VIEW_SOURCE, inputTransObj,
					cellsModel);
			break;
		//�鿴�����¼�����
		case ITableInputMenuType.MENU_TYPE_VIEW_SUB:
			cellsModel = inCellsModel;
			//���㣬�ɹ��򷵻ؽ�����CellsModel
			returnObj = LinkServletUtil
					.linkTableInputOperServlet(
							ITableInputMenuType.MENU_TYPE_VIEW_SUB,
							inputTransObj, null);
			break;
		//��ʽ׷��-����
		case ITableInputMenuType.BIZ_TYPE_FORMULATRACE_CAL:
			//�õ����ؼ�¼��ĵ�ǰ����
			cellsModel = inCellsModel;
			//���棬�����򷵻ز����ɹ���־����
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.BIZ_TYPE_FORMULATRACE_CAL,
					inputTransObj, cellsModel);
			break;
		//��ʽ׷��-��ȡ����Ķ�ֵ
		case ITableInputMenuType.BIZ_TYPE_FORMULATRACE_MULTIVALUES:
			//�õ����ؼ�¼��ĵ�ǰ����
			cellsModel = inCellsModel;
			//���棬�����򷵻ز����ɹ���־����
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.BIZ_TYPE_FORMULATRACE_MULTIVALUES,
					inputTransObj, cellsModel);
			break;
		//��ʽ׷��-����ֵ
		case ITableInputMenuType.BIZ_TYPE_FORMULATRACE_TRACE:
			//�õ����ؼ�¼��ĵ�ǰ����
			cellsModel = inCellsModel;
			//���棬�����򷵻ز����ɹ���־����
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.BIZ_TYPE_FORMULATRACE_TRACE,
					inputTransObj, cellsModel);
			break;
		//ָ��׷��
		case ITableInputMenuType.BIZ_TYPE_MEASURE_TRACE:
			//�õ����ؼ�¼��ĵ�ǰ����
			cellsModel = inCellsModel;
			//���棬�����򷵻ز����ɹ���־����
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.BIZ_TYPE_MEASURE_TRACE, inputTransObj,
					null);
			break;
		//������ˣ�������                
		case ITableInputMenuType.MENU_TYPE_CHECK_IN:
		case ITableInputMenuType.MENU_TYPE_CHECK_BETWEEN:
			//�õ����ؼ�¼������
			cellsModel = inCellsModel;
			//������ˣ�������˽��CheckResoultVO
			returnObj = LinkServletUtil.linkTableInputOperServlet(nMenuType,
					inputTransObj, cellsModel);
			break;
		//�򿪱���ѡ��    
		case ITableInputMenuType.BIZ_TYPE_CHOOSEREP:
			//�õ�������������б�����Ϣ ChooseRepData[]
			returnObj = LinkServletUtil.linkTableInputOperServlet(nMenuType,
					inputTransObj, null);
			break;
		//�򿪿ɵ��뱨��ѡ��    
		case ITableInputMenuType.BIZ_TYPE_IMPORTABLEREP:
			//�õ�������������б�����Ϣ ChooseRepData[]
			returnObj = LinkServletUtil.linkTableInputOperServlet(nMenuType,
					inputTransObj, null);
			break;
		//�л��ؼ���    
		case ITableInputMenuType.BIZ_TYPE_CHANGEKEYWORDDATA:
			//�õ��л��ؼ��ֵĽ�����ʾ��Ҫ�����ݶ���
			returnObj = LinkServletUtil.linkTableInputOperServlet(nMenuType,
					inputTransObj, null);
			break;
		//            //����Excel
		//            case ITableInputMenuType.MENU_TYPE_EXPORT_EXCEL:
		////                returnObj = LinkServletUtil.linkTableInputOperServlet(nMenuType,inputTransObj,null);
		//
		//                //�õ����������ݶ���workBook���ǿ����л�����Ķ�����ֻ���ڿͻ��˽��д���
		//                String strReportPK4ExportExcel = inputTransObj.getRepDataParam().getReportPK();
		//                String strAloneID4ExportExcel = inputTransObj.getRepDataParam().getAloneID();
		//                IExcelExport exportObj = new RepDataExport();
		//                ((RepDataExport)exportObj).setOrgPK(inputTransObj.getRepDataParam().getOrgPK());
		//                CSomeParam cSomeParam = new CSomeParam();
		//                cSomeParam.setAloneId(strAloneID4ExportExcel);
		//                cSomeParam.setRepId(strReportPK4ExportExcel);
		//                cSomeParam.setUserID(inputTransObj.getRepDataParam().getOperUserPK());
		//                MeasurePubDataVO pubData = null;
		//                try {
		//                    pubData = nc.ui.iufo.data.MeasurePubDataBO_Client.findByAloneID(strAloneID4ExportExcel);
		//                    cSomeParam.setUnitId(pubData.getUnitPK());
		//                } catch (Exception e1) {
		//AppDebug.debug(e1);//@devTools                     e1.printStackTrace(System.out);
		//                }
		//                ((RepDataExport)exportObj).setParam(cSomeParam);
		//                ((RepDataExport)exportObj).setLoginDate(inputTransObj.getCurLoginDate());
		//                
		//                HSSFWorkbook workBook=new HSSFWorkbook();
		//                HSSFFontFactory fontFactory=new HSSFFontFactory(workBook);
		//                String strSheetName = "sheet1";
		//                try {
		//                    TableDataToExcel.translate(exportObj,workBook,strSheetName,fontFactory);
		//                } catch (Exception e) {
		//AppDebug.debug(e);//@devTools                     e.printStackTrace(System.out);
		//                }
		////        	    workBook.createExternSheet(); liuyy. 2007-07-19 
		//                
		//                returnObj = new Object[]{"contextVO",workBook};
		////                returnObj = LinkServletUtil.linkTableInputOperServlet(nMenuType,inputTransObj,null);
		//                break;       
		//����HTML  
		case ITableInputMenuType.MENU_TYPE_EXPORT_HTML:
			//�õ����������ݶ���
			returnObj = LinkServletUtil.linkTableInputOperServlet(nMenuType,
					inputTransObj, null);
			break;
		case ITableInputMenuType.MENU_TYPE_BATCHPRINT:
			returnObj = LinkServletUtil.linkTableInputOperServlet(
					ITableInputMenuType.MENU_TYPE_BATCHPRINT, inputTransObj,
					null, null);
			break;
		default:
			break;
		}

		return returnObj;
	}

	protected static TableInputContextVO geneNewInputContextVO(
			UfoReport oldUfoReport, UfoContextVO newUfoContextVO) {
		TableInputContextVO oldTableInputContextVO = (TableInputContextVO) oldUfoReport
				.getContextVo();
		Object tableInputTransObj = oldTableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
	
		TableInputContextVO newInputContextVO = new TableInputContextVO(
				newUfoContextVO, inputTransObj);
		return newInputContextVO;
	}

	private void setCheckResult(CheckResultVO checkResultVO) {
		if(checkResultVO == null)
			return;		
		InputCheckPlugIn plugIn = (InputCheckPlugIn) getUfoReport()
				.getPluginManager().getPlugin(InputCheckPlugIn.class.getName());
		IExtension[] exts = plugIn.getDescriptor().getExtensions();
		CheckResultExt checkResultExt = (CheckResultExt) exts[0];
		CheckResultPanel panel = (CheckResultPanel) checkResultExt.getPanel();
		panel.init(checkResultVO);
		//���ͨ������ʾ������
		if(checkResultVO.getCheckState() == CheckResultVO.NOPASS){
			if(!checkResultExt.isShow()){
				checkResultExt.setShow(true);
			}
			UICloseableTabbedPane tabs = getUfoReport().getReportNavPanel().getPanelById(checkResultExt.getNavPanelPos());;
			if (tabs.indexOfTab(checkResultExt.getName()) < 0) {
				tabs.add(checkResultExt.getName(), checkResultExt.getPanel());

			} 
		} else{
			if(checkResultExt.isShow()){
				checkResultExt.setShow(false);
			}
			UICloseableTabbedPane tabs = getUfoReport().getReportNavPanel().getPanelById(checkResultExt.getNavPanelPos());;
			if (tabs.indexOfTab(checkResultExt.getName()) >= 0) {
				tabs.removeTabAt(checkResultExt.getName());

			}
		}
	}
} 