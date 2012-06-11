package com.ufsoft.iufo.inputplugin.biz.data;

import java.util.Vector;

import javax.swing.JOptionPane;

import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.ITraceDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TraceDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TraceDataScope;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellPosition;

public class TraceDataCmd extends AbsIufoBizCmd {

	public TraceDataCmd(UfoReport ufoReport){
		super(ufoReport);
	}
	
	protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
		if (params==null || params.length<2 || params[1]==null || 
				params[1] instanceof CellPosition[]==false || ((CellPosition[])params[1]).length<=0){
            String strAlert = MultiLangInput.getString("miufotableinput0004");//请选择一个单元格
            JOptionPane.showMessageDialog(ufoReport,strAlert);
            return;
		}
		
		boolean bTotal=((Boolean)params[0]).booleanValue();
	    CellPosition[] cells =(CellPosition[])params[1];
	    int iLen=bTotal?cells.length:1;	    
	    Vector<TraceDataScope> vScope=new Vector<TraceDataScope>();
	    for (int i=0;i<iLen;i++){
	    	TraceDataScope scope=CellsModelOperator.getTraceDataScope(ufoReport.getCellsModel(), cells[i]);
	    	if (scope!=null)
	    		vScope.add(scope);
	    }
	    
	    TraceDataScope[] scopes=vScope.toArray(new TraceDataScope[0]);
	    
	    if (bTotal){
	    	boolean bHasMeas=false;
	    	for (int i=0;i<scopes.length;i++){
	    		if (scopes[i].getMeasID()!=null && scopes[i].getMeasID().length()>0){
	    			bHasMeas=true;
	    			break;
	    		}
	    	}
	    	if (!bHasMeas){
	    		JOptionPane.showMessageDialog(ufoReport,MultiLangInput.getString("miufotableinput0007"));
	    		return;
	    	}
	    }else if (scopes.length<=0){
	    	JOptionPane.showMessageDialog(ufoReport,MultiLangInput.getString("miufotableinput0006"));
	    	return;
	    }else{
	    	TraceDataScope scope=scopes[0];
	    	String[] strKeyVals=scope.getDynKeyVals();
	    	if (strKeyVals!=null && strKeyVals.length>0){
	    		for (int i=0;i<strKeyVals.length;i++){
	    			if (strKeyVals[i]==null || strKeyVals[i].trim().length()<=0){
	    				JOptionPane.showMessageDialog(ufoReport,MultiLangInput.getString("miufotableinput0013"));
	    				return;
	    			}
	    		}
	    	}
	    }
	    
	    IInputBizOper inputMenuOper = new InputBizOper(ufoReport);
	    ITraceDataParam traceParam=new TraceDataParam();
	    traceParam.setTraceScopes(scopes);
	    traceParam.setTotal(bTotal);
	    InputBizOper.doGetTransObj(ufoReport).setTraceParam(traceParam);
	    
        inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_VIEW_SOURCE);			   
	}
	
	public static void doTraceOneNCFormula(String strFormula,UfoReport ufoReport,CellPosition relaCell){
		TraceDataScope scope=new TraceDataScope();
		scope.setFormula(strFormula);
		scope.setRelaCell(relaCell);

	    IInputBizOper inputMenuOper = new InputBizOper(ufoReport);
	    ITraceDataParam traceParam=new TraceDataParam();
	    traceParam.setTraceScopes(new TraceDataScope[]{scope});
	    traceParam.setTotal(false);
	    InputBizOper.doGetTransObj(ufoReport).setTraceParam(traceParam);
	    
        inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_VIEW_SOURCE);
	}
}
