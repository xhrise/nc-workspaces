package com.ufsoft.iufo.inputplugin.biz.data;

import java.util.Vector;

import javax.swing.JOptionPane;

import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.biz.file.InputOpenRepOper;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.ITraceDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TraceDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TraceDataScope;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.CellPosition;

public class TraceSubCmd extends AbsIufoBizCmd {
	public TraceSubCmd(UfoReport ufoReport){
		super(ufoReport);
	}
	
	protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
		if (params==null || params.length<=0 ){
            String strAlert = MultiLangInput.getString("miufotableinput0004");//请选择一个单元格
            JOptionPane.showMessageDialog(ufoReport,strAlert);
            return;
		}
		
	    CellPosition[] cells =(CellPosition[])params;
	    Vector<TraceDataScope> vScope=new Vector<TraceDataScope>();
	    for (int i=0;i<cells.length;i++){
	    	TraceDataScope scope=CellsModelOperator.getTraceDataScope(ufoReport.getCellsModel(), cells[i]);
	    	if (scope!=null)
	    		vScope.add(scope);
	    }
	    
	    TraceDataScope[] scopes=vScope.toArray(new TraceDataScope[0]);
	    
    	boolean bHasMeas=false;
    	for (int i=0;i<scopes.length;i++){
    		if (scopes[i].getMeasID()!=null && scopes[i].getMeasID().length()>0){
    			bHasMeas=true;
    			break;
    		}
    	}
    	if (!bHasMeas){
    		JOptionPane.showMessageDialog(ufoReport,MultiLangInput.getString("miufotableinput0009"));
    		return;
    	}
	    
	    IInputBizOper inputMenuOper = new InputBizOper(ufoReport);
	    ITraceDataParam traceParam=new TraceDataParam();
	    traceParam.setTraceScopes(scopes);
	    InputBizOper.doGetTransObj(ufoReport).setTraceParam(traceParam);
	    
        inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_VIEW_SUB);
	}
}
