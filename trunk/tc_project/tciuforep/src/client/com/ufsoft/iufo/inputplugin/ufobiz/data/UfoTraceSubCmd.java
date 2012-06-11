package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.util.Vector;

import javax.swing.JOptionPane;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoBizCmd;
import com.ufsoft.iuforeport.tableinput.TraceDataResult;
import com.ufsoft.iuforeport.tableinput.applet.ITraceDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TraceDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TraceDataScope;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;
import com.ufsoft.iufo.resource.StringResource;

public class UfoTraceSubCmd extends AbsUfoBizCmd {
	public UfoTraceSubCmd(RepDataEditor editor){
		super(editor);
	}
	
	/**
	 * @i18n miufohbbb00117=所选单元格没有提取指标或动态区没有录入关键字值，无法查看汇总下级
	 * @i18n miufohbbb00118=查看汇总下级数据失败:
	 */
	protected void executeIUFOBizCmd(RepDataEditor editor, Object[] params) {
	    CellPosition[] cells =(CellPosition[])params;
		if (cells==null || cells.length<=0 ){
            String strAlert = MultiLangInput.getString("miufotableinput0004");//请选择一个单元格
            JOptionPane.showMessageDialog(editor,strAlert);
            return;
		}

	    Vector<TraceDataScope> vScope=new Vector<TraceDataScope>();
	    for (int i=0;i<cells.length;i++){
	    	TraceDataScope scope=CellsModelOperator.getTraceDataScope(editor.getCellsModel(), cells[i]);
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
    		JOptionPane.showMessageDialog(editor,MultiLangInput.getString(StringResource.getStringResource("miufohbbb00117")));
    		return;
    	}
	    try{
		    ITraceDataParam traceParam=new TraceDataParam();
		    traceParam.setTraceScopes(scopes);
	    	TraceDataResult result=(TraceDataResult)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "traceSubData",
	    			new Object[]{editor.getRepDataParam(),RepDataControler.getInstance(getRepDataEditor().getMainboard()).getLoginEnv(getRepDataEditor().getMainboard()),editor.getCellsModel(),traceParam});
	    	UfoTraceDataCmd.treatTraceResult(result,editor,true);
	    }catch(Exception e){
	    	AppDebug.debug(e);
	    	UfoPublic.sendErrorMessage(StringResource.getStringResource("miufohbbb00118")+e.getMessage(),editor,null);
	    }		
	}
}
 