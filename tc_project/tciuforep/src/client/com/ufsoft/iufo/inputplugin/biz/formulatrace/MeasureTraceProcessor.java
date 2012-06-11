package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import nc.vo.iufo.data.MeasureTraceVO;

import com.ufida.dataset.Context;
import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iuforeport.reporttool.temp.KeyDataGroup;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.script.CmdProxy;
import com.ufsoft.script.UfoFormulaProxy;
import com.ufsoft.script.base.CommonExprCalcEnv;
import com.ufsoft.script.base.UfoVal;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.script.extfunc.MeasFunc;
import com.ufsoft.script.extfunc.MeasFuncDriver;
import com.ufsoft.script.spreadsheet.ReportDynCalcEnv;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.iufo.resource.StringResource;

public class MeasureTraceProcessor {

	/**
	 * @i18n miufo01186=���ʽΪ�ա���ȷ������Դ���û����������á�
	 * @i18n miufo00596=Ԥ�����������
	 */
	public MeasureTraceVO[] trace(IContext contextVO,CellsModel cellsModel, IFormulaParsedDataItem formulaParsedDataItem,CellPosition cell) {
		UfoFmlExecutor ufoFmlExecutor = FormulaTraceBizHelper.getFormulaHandler((Context)contextVO, cellsModel);
		ReportDynCalcEnv dynCalcEnv = ufoFmlExecutor.getCalcEnv();
		UfoCalcEnv env = ufoFmlExecutor.getCalcEnv();
		
		//�����ʽ�ڶ�̬���У������ö�̬�����㻷��
		int unitAreaNum = -1, stepRow = -1, stepCol = -1;
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);		
		boolean isInDynArea = dynAreaModel.isInDynArea(cell);
		if(isInDynArea){
			DynAreaCell dynAreaCell = dynAreaModel.getDynAreaCellByPos(cell);
			stepRow = dynAreaCell.getDynAreaVO().isRowDirection() ? 
							 dynAreaCell.getDynAreaVO().getOriArea().getHeigth() : 0;
	        stepCol = dynAreaCell.getDynAreaVO().isRowDirection() ?
	                		 0 : dynAreaCell.getDynAreaVO().getOriArea().getWidth();
	        unitAreaNum = dynAreaCell.getOwnerUnitAreaNum(cell);
	        //���÷�����㻷������
	        FormulaTraceBiz.setDynAreaCalcEnv(ufoFmlExecutor, dynAreaCell, stepRow, stepCol, unitAreaNum);
	        KeyDataGroup[] objKeydatas = ufoFmlExecutor.getAllDynKeyDataGroups(dynAreaCell.getDynAreaPK());
	        if(objKeydatas == null || objKeydatas.length == 0){
	        	return null;
	        }
	        //����̬������йؼ���ֵ�洢�ڼ��㻷����
	        ufoFmlExecutor.getCalcEnv().setDynAllKeyDatas(objKeydatas);
		} else{
			dynCalcEnv.setDynAreaInfo(null, null);
		}
		
		UfoExpr expr  = formulaParsedDataItem.getTracedExpr();
		
		if(expr == null){
			throw new RuntimeException(StringResource.getStringResource("miufo01186"));
		}

		MeasureTraceVO[] tracevos = new MeasureTraceVO[1];
		env.setExEnv(CommonExprCalcEnv.EX_CALCEXFUNC,
					CommonExprCalcEnv.EX_VALUE_ON);
		try{
			//���ñ�ʶ���˼��㻷��Ϊָ�깫ʽ׷��
			env.setMeasureTraceVOs(tracevos);
			try{
				CmdProxy.preCalcExtFuncExpr(expr, ufoFmlExecutor.getCalcEnv(), 3);
			} catch(Exception e){
				AppDebug.debug(StringResource.getStringResource("miufo00596"), e);
			}
			tracevos = env.getMeasureTraceVOs();

			//����mselect��msum�����ñ���ָ��������preCalcExtFunc����ִ�к�̨sql���̣����Ҫ������ʵ������̡�liuyy
			//����MSELECTA��������������������������¼���׷��
			
			//���ڶ�̬��mselects��preCalcExtFunc��tracevosΪ�գ���Ҫ����ʵ������ȡ�
			if(isMSelectAFunc(expr, env) || (tracevos != null && tracevos.length < 1)){
				UfoVal[] vals = expr.getValue(env);
				if(vals == null || vals.length < 1){
					return null;
				}
				tracevos = env.getMeasureTraceVOs();
			}
//
//			
//			try{
//	            CmdProxy.preCalcExtFuncExpr(expr, env, 3);
//	        } catch(Exception e){
//	        	AppDebug.debug("Ԥ�����������",e);
//	        }
	        
//			tracevos = env.getMeasureTraceVOs();
//			if(tracevos.length < 1){
//		        //�Թ�ʽ����ִ�м���
//
//	            TableInputTransObj inputTransObj = ((TableInputContextVO)contextVO).getInputTransObj();
//				UfoContextVO context = FormulaTraceUtil.getContextVO(inputTransObj); 
//				//�������������������޹ص��ⲿ����ֵ
//				ReportCalcSrv reportCalcSrv = new ReportCalcSrv(context, cellsModel);
//				String strOld=(String) env.getExEnv(CommonExprCalcEnv.EX_CALCEXFUNC);
//				env.setExEnv(CommonExprCalcEnv.EX_CALCEXFUNC, CommonExprCalcEnv.EX_VALUE_ON);
//				UfoVal[] values = reportCalcSrv.calcFormula(formulaParsedDataItem);
//				reportCalcSrv.getFormHandler().getCalcEnv().setExEnv(CommonExprCalcEnv.EX_CALCEXFUNC,strOld);
//				
//				tracevos = env.getMeasureTraceVOs();
//			}
				 
		 }catch(Throwable e){
		  	AppDebug.debug(e);
		  	throw new RuntimeException(e.getMessage());
				 
		 } finally {
			 UfoFormulaProxy.clearPreCalcValues(new UfoExpr[]{expr},env);
			 
			 FormulaTraceBiz.clearDynAreaCalcEnv(ufoFmlExecutor);
		 }
 
		
		return tracevos;
	}
	
	/**
	 * MSELECTA��������������������������¼���׷��
	 * @return
	 */
	 boolean isMSelectAFunc(UfoExpr expr, UfoCalcEnv env){
		Object objFunc = expr.getElementObjByIndex(0);
		if(objFunc != null && objFunc instanceof MeasFunc){
			MeasFunc mFunc = (MeasFunc)objFunc;
			String funcName = mFunc.getFuncName();
			boolean isInEnvRep = mFunc.isInEnvRep(env);
			if(funcName.equalsIgnoreCase(MeasFuncDriver.MSELECTA) && !isInEnvRep)
				return true;
		}
		return false;
		
	}
	
	public static String getMeasFuncName(UfoExpr expr){
		Object objFunc = expr.getElementObjByIndex(0);
		if(objFunc != null && objFunc instanceof MeasFunc){
			MeasFunc mFunc = (MeasFunc)objFunc;
			String funcName = mFunc.getFuncName();
			return funcName;
		}
		return null;
		
	}
	
}
 