package com.ufsoft.iufo.inputplugin.biz.formulatracenew;

import java.util.ArrayList;
import java.util.List;

import com.ufida.dataset.Context;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizLink;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizLinkMock;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizMock;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaTraceBiz;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaTraceBizLink;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iuforeport.tableinput.FormulaTraceUtil;
import com.ufsoft.iuforeport.tableinput.TraceDetailUtil;
import com.ufsoft.iuforeport.tableinput.TraceableFuncInfo;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.script.base.IParsed;
import com.ufsoft.script.expression.UfoCmdLet;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.script.function.ExtFunc;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;


public class UfoFormulaTraceBizHelper implements IUfoContextKey{	
	private static boolean bDebugFormulaTraceBiz = false;
	protected static boolean bDebugFormulaTraceBizLink = false;
	
	
	private UfoFormulaTraceBizHelper(){		
	}
	public static void setDebugFormulaTraceBizLink(boolean bDebugBizLink){
		bDebugFormulaTraceBizLink = bDebugBizLink;
	}

	public static void setDebugFormulaTraceBiz(boolean bDebugBiz){
		bDebugFormulaTraceBiz = bDebugBiz;
	}
	public static IFormulaTraceBiz getFormulaTraceBiz(){
		setDebugFormulaTraceBiz(false);//BIZ MOCKING:TRUE
		if(bDebugFormulaTraceBiz){
			return FormulaTraceBizMock.getInstance();
		}
		return UfoFormulaTraceBiz.getInstance();
	}
	public static IFormulaTraceBizLink getIFormulaTraceBizLink(){
		//DEBUG...
		setDebugFormulaTraceBizLink(false);//LINK NO MOCKING :FALSE
		if(bDebugFormulaTraceBizLink){
			return FormulaTraceBizLinkMock.getInstance();			
		}
		return FormulaTraceBizLink.getInstance();
	}
	/**
	 * �õ�ҵ���������ַ����������ַ���Ϊ����ȫ���滻��Ĺ�ʽ�ַ���
	 * @param formulaParsedDataItem
	 * @param dataModel
	 * @param ufoFmlExecutor
	 * @param area
	 * @return
	 */
	public static void getTraceStrOfNCFunc(IFormulaParsedDataItem formulaParsedDataItem, CellsModel dataModel, UfoFmlExecutor ufoFmlExecutor, IArea area){
		IArea formatArea = DynAreaCell.getFormatArea(area, dataModel).getStart();
		Object[] objs = ufoFmlExecutor.getFormulaModel().getRelatedFmlVO(formatArea, true);
		if (objs == null || objs.length <= 1 || objs[0]== null || objs[1]==null)
			return;
		
		IArea areaFml = (IArea) objs[0];
		areaFml = DynAreaCell.getRealArea(areaFml, dataModel);

		//�����������ʽ����С���λ��
		int nRow = area.getStart().getRow()- areaFml.getStart().getRow();
		int nCol = area.getStart().getColumn()- areaFml.getStart().getColumn();
		CellPosition relaCell = CellPosition.getInstance(nRow, nCol);
		formulaParsedDataItem.setRelaCell(relaCell);
		formulaParsedDataItem.setAbsCell((CellPosition) area);
		
		
		//����ҵ�������з��׷������
		UfoExpr ufoExpr = formulaParsedDataItem.getTracedExpr();
		TraceableFuncInfo[] funcs = TraceDetailUtil.getFuncTraceInfo(ufoExpr, ufoFmlExecutor.getCalcEnv(), false);
		if(funcs == null || funcs.length == 0){
			return;
		}

		//���ù�ʽ�����滻��Ĺ�ʽ����
		TraceableFuncInfo func = funcs[0];
		String strFormula = func.getStrFuncName() + "(" + func.getStrFuncParam() + ")";
		formulaParsedDataItem.setNCFuncStr(strFormula.trim());

	}
	/**
	 * �Ƿ�ҵ����(���ʻ�HR����)
	 * @param expr ������ĺ������ʽ
	 * @param env
	 * @return
	 */
	public static boolean isExtNCFunc(UfoExpr expr, UfoCalcEnv env){
		Object objFunc = expr.getElementObjByIndex(0);
		if(objFunc == null || !(objFunc instanceof ExtFunc)){
			return false;
		}
		
		ExtFunc extFunc = (ExtFunc)objFunc;
		String strDriverName = extFunc.getFuncDriverName();
		if(strDriverName == null 
				|| !strDriverName.equals("nc.ui.fi.uforeport.NCFuncForUFO")){
			return false;
		}
		
		return true;
	}
	/**
	 * �Ƿ�ҵ����(HR����)
	 * @param expr ������ĺ������ʽ
	 * @param env
	 * @return
	 */
	public static boolean isExtHRFunc(UfoExpr expr, UfoCalcEnv env){
		Object objFunc = expr.getElementObjByIndex(0);
		if(objFunc == null || !(objFunc instanceof ExtFunc)){
			return false;
		}
		
		ExtFunc extFunc = (ExtFunc)objFunc;
		String strDriverName = extFunc.getFuncDriverName();
		if(strDriverName == null 
				|| !strDriverName.equals("nc.bs.hr.iufo.func.HR_IUFO_FunctionDriver")){
			return false;
		}
		
		return true;
	}
	/**
	 * �Ƿ�HRҵ����(HR����)
	 * @param expr ������ĺ������ʽ
	 * @param env
	 * @return
	 */
	public static boolean isExtHRFunc(IParsed cmdLet, UfoCalcEnv env){
		List allExpr = new ArrayList();
		if(cmdLet instanceof UfoCmdLet){
			((UfoCmdLet)cmdLet).getAllExprsAndNoMeasCord(allExpr);
		} else{
			cmdLet.getAllExprs(allExpr);
		}
		
		List<UfoExpr> listAllExpr = UfoFmlExecutor.getCalaElemFromExpr(allExpr, env);
		if(listAllExpr == null || listAllExpr.size() == 0){
			return false;
		}
		
		return isExtHRFunc(listAllExpr.get(0), env);
		
	}
	public static UfoFmlExecutor getFormulaHandler(Context contextVO, CellsModel cellsModel){
		return UfoFmlExecutor.getInstance(contextVO, cellsModel,true);
	}
	/**
	 * �ڿͻ��˸��ݱ���������TableInputContextVO������ʽ������������UfoContextVO����
	 * @param contextVO
	 * @return
	 */
//	private static UfoContextVO getContext(TableInputContextVO contextVO){
//		String strOrgPK = contextVO.getAttribute(ORG_PK) == null ? null : (String)contextVO.getAttribute(ORG_PK);
//		Object repIdObj = contextVO.getAttribute(REPORT_PK);
//		String strRepPK = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null; 
//		
//		UfoContextVO context = new UfoContextVO();
//		context.setAttribute(MODEL, false);
//		context.setAttribute(ON_SERVER, false);
//		context.setAttribute(REP_MANAGER, true);
//		context.setAttribute(ANA_REP, false);
//		context.setName(contextVO.getName());
//	    context.setAttribute(TYPE, UfoContextVO.REPORT_TABLE);
//		context.setAttribute(PRIVATE,false);
//		context.setFormatRight(1);
//		context.setAttribute(ORG_PK, strOrgPK);
//		context.setContextId(strRepPK);
//		
//		Object tableInputTransObj = contextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
//		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
//	
//		if(inputTransObj!=null&&inputTransObj.getRepDataParam()!=null)
//		    context.setAttribute(CUR_USER_ID,inputTransObj.getRepDataParam().getOperUserPK());
//		context.setAttribute(LOGIN_UNIT_ID, contextVO.getAttribute(LOGIN_UNIT_ID));
//		context.setReportcode(contextVO.getReportcode());
//		try {
//			DataSourceInfo dataSourceInfo = inputTransObj.getRepDataParam().getDSInfo();
//			if (dataSourceInfo != null && dataSourceInfo.getDSID() != null) {
//				DataSourceVO dataSourceVO = DataSourceBO_Client.loadDataSByID(dataSourceInfo.getDSID());
//				context.setAttribute(DATA_SOURCE, dataSourceVO);
//				dataSourceVO.setLoginUnit(dataSourceInfo.getDSUnitPK());
//				dataSourceVO.setLoginName(dataSourceInfo.getDSUserPK());
//				String dsPassword = nc.bs.iufo.toolkit.Encrypt.decode(
//						dataSourceInfo.getDSPwd()
//						, dataSourceInfo.getDSID());
//				dataSourceVO.setLoginPassw(dsPassword);
//				dataSourceVO.setLoginDate(dataSourceInfo.getDSDate());
//			}
//		} catch (Exception e) {
//			AppDebug.debug(e);
//		}
//		context.setAttribute(CURRENT_LANG, inputTransObj.getLangCode());
//		String strInTask = "true";
//		boolean bInTask = false;
//		if(strInTask != null)
//			bInTask = Boolean.parseBoolean(strInTask);
//		context.setAttribute(IN_TASK, bInTask);
//		context.setAttribute(MEASURE_PUB_DATA_VO, contextVO.getPubDataVO());
//		context.setAttribute(CUR_UNIT_CODE, inputTransObj.getLoginUnit());
//		context.setAttribute(LOGIN_DATE, inputTransObj.getCurLoginDate());
//		context.setAttribute(CUR_UNIT_ID, inputTransObj.getRepDataParam().getOperUnitPK());
//		MeasurePubDataVO pubData=null;
//		String strAloneID=inputTransObj.getRepDataParam().getAloneID();
//		if (strAloneID!=null){
//			try{
//				pubData=MeasurePubDataBO_Client.findByAloneID(strAloneID);
//			}catch(Exception e){
//				AppDebug.debug(e);
//			}
//		}
//		if (pubData!=null && pubData.getUnitPK()!=null && pubData.getUnitPK().trim().length()>0)
//			context.setAttribute(CUR_UNIT_ID,pubData.getUnitPK());
//		context.setAttribute(CUR_USER_ID, inputTransObj.getRepDataParam().getOperUserPK());
//		return context;
//		
//	}
	/**
	 * ��ʾֵҪ����ʵ�ֿ�ѧ������
	 * ������ֵ���͵�ֵ��Ҫ���տ�
	 * @param objValValue
	 * @return
	 */
//	public static String getFormatValueStr(Object objValValue){
//		return FormulaTraceUtil.getFormatValueStr(objValValue);
//	}
}
