package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iuforeport.tableinput.applet.FormulaTraceParam;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceParam;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.UfoReport;
/**
 *  需向服务器端发出请求的公式追踪业务接口实现类
 * @author liulp
 *
 */
public class FormulaTraceBizLink implements IFormulaTraceBizLink{
	private static IFormulaTraceBizLink s_oFormulaTraceBizLink = new FormulaTraceBizLink();
	private FormulaTraceBizLink(){		
	} 
	public static IFormulaTraceBizLink getInstance(){
		return s_oFormulaTraceBizLink;
	}
	public Object getCalulatedValue(UfoReport ufoReport, IFormulaParsedDataItem formulaParsedDataItem) {
		// TODO Auto-generated method stub
//	    IInputBizOper inputMenuOper = new InputBizOper(ufoReport);
	    IInputBizOper inputMenuOper = new FormulaTraceBizOper(ufoReport);
	    IFormulaTraceParam formulaTraceParam = new FormulaTraceParam();
	    formulaTraceParam.setParsedDataItem(formulaParsedDataItem);
	    InputBizOper.doGetTransObj(ufoReport).setFormulaTraceParam(formulaTraceParam);
	    
        inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_FORMULATRACE_CAL);	
		return "ValueFromBizLink";
	}
	public IFormulaTraceValueItem[] getTraceMultiValues(UfoReport ufoReport, IFormulaParsedDataItem formulaParsedDataItem) {
		//to get multi values from server
		//pop up dlg,to get user's selected value
		//trace single value to other task-report
	    IInputBizOper inputMenuOper = new FormulaTraceBizOper(ufoReport);
	    IFormulaTraceParam formulaTraceParam = new FormulaTraceParam();
	    formulaTraceParam.setParsedDataItem(formulaParsedDataItem);
	    InputBizOper.doGetTransObj(ufoReport).setFormulaTraceParam(formulaTraceParam);
		// TODO need to debug this function by chxw,liulp
        inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_FORMULATRACE_MULTIVALUES);	
		return null;
	}
	public void doTraceValue(UfoReport ufoReport, IFormulaTraceValueItem formulaTraceValueItem) {
	    IInputBizOper inputMenuOper = new FormulaTraceBizOper(ufoReport);
	    IFormulaTraceParam formulaTraceParam = new FormulaTraceParam();
	    formulaTraceParam.setTraceValueItem(formulaTraceValueItem);
	    InputBizOper.doGetTransObj(ufoReport).setFormulaTraceParam(formulaTraceParam);
	    
        inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_FORMULATRACE_TRACE);
//		UfoPublic.sendWarningMessage("FormulaTraceBizLink.doTraceValue():waitring for trace single values' implementation", ufoReport);
		return;
		
	}
}
