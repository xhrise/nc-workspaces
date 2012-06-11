package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.sm.login.LoginNCForIUFO;
import nc.ui.sm.login.NCAppletStub;
import nc.util.iufo.pub.UFOString;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.util.UIUtilities;
import com.ufida.zior.view.Editor;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.inputplugin.biz.FreeQueryMainBoardListner;
import com.ufsoft.iufo.inputplugin.biz.file.TraceDataResultDlg;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.ufobiz.AbsUfoBizCmd;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.tableinput.TraceDataResult;
import com.ufsoft.iuforeport.tableinput.TraceableFuncInfo;
import com.ufsoft.iuforeport.tableinput.applet.ITraceDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TraceDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TraceDataScope;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.extfunc.HRTraceDataResult;
import com.ufsoft.table.CellPosition;

public class UfoTraceDataCmd extends AbsUfoBizCmd {
	public UfoTraceDataCmd(RepDataEditor editor){
		super(editor);
	}
	
	/**
	 * @i18n miufohbbb00115=所选单元格没有定义指标或动态区没有录入关键字值，无法查看汇总结果来源
	 * @i18n miufohbbb00116=联查数据失败:
	 */
	protected void executeIUFOBizCmd(RepDataEditor editor, Object[] params) {
		if (params==null || params.length<2 || params[1]==null || 
				params[1] instanceof CellPosition[]==false || ((CellPosition[])params[1]).length<=0){
            String strAlert = MultiLangInput.getString("miufotableinput0004");//请选择一个单元格
            JOptionPane.showMessageDialog(editor,strAlert);
            return;
		}
		
		boolean bTotal=((Boolean)params[0]).booleanValue();
	    CellPosition[] cells =(CellPosition[])params[1];
	    int iLen=bTotal?cells.length:1;	    
	    Vector<TraceDataScope> vScope=new Vector<TraceDataScope>();
	    for (int i=0;i<iLen;i++){
	    	TraceDataScope scope=CellsModelOperator.getTraceDataScope(editor.getCellsModel(), cells[i]);
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
	    		JOptionPane.showMessageDialog(editor,MultiLangInput.getString(StringResource.getStringResource("miufohbbb00115")));
	    		return;
	    	}
	    }else if (scopes.length<=0){
	    	JOptionPane.showMessageDialog(editor,MultiLangInput.getString("miufotableinput0006"));
	    	return;
	    }else{
	    	TraceDataScope scope=scopes[0];
	    	String[] strKeyVals=scope.getDynKeyVals();
	    	if (strKeyVals!=null && strKeyVals.length>0){
	    		for (int i=0;i<strKeyVals.length;i++){
	    			if (strKeyVals[i]==null || strKeyVals[i].trim().length()<=0){
	    				JOptionPane.showMessageDialog(editor,MultiLangInput.getString("miufotableinput0013"));
	    				return;
	    			}
	    		}
	    	}
	    }
	    
	    ITraceDataParam traceParam=new TraceDataParam();
	    traceParam.setTraceScopes(scopes);
	    traceParam.setTotal(bTotal);
	    int iPort=Integer.parseInt(""+editor.getMainboard().getContext().getAttribute("SERVER_PORT"));
	    
	    try{
	    	TraceDataResult result=(TraceDataResult)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "traceData",
	    			new Object[]{editor.getRepDataParam(),RepDataControler.getInstance(editor.getMainboard()).getLoginEnv(editor.getMainboard()),editor.getCellsModel(),traceParam,iPort});
	    	treatTraceResult(result,editor,false);
	    }catch(Exception e){
	    	AppDebug.debug(e);
	    	UfoPublic.sendErrorMessage(StringResource.getStringResource("miufohbbb00116")+e.getMessage(),editor,null);
	    }
	}
	
	public static void treatTraceResult(TraceDataResult result,Editor container,boolean bTotalSub) {
		TraceableFuncInfo[] funcs = result.getFuncs();
		if (funcs != null && funcs.length > 0) {
			traceNCData(result,container);
		} else if (result.getResult()!=null && result.getResult() instanceof HRTraceDataResult) {
			TraceDataResultDlg dlg = new TraceDataResultDlg(container,(HRTraceDataResult)result.getResult());
			dlg.show();
		} else if (result.getCellsModel() != null) {
			RepDataControler.getInstance(container.getMainboard()).showTotalSourceView(container.getMainboard(),result.getCellsModel(),bTotalSub);
		} else {
			UfoPublic.sendMessage(MultiLangInput.getString("miufotableinput0006"), container);
		}
	}

	private static void traceNCData(TraceDataResult result,Container container) {
		try {
			JApplet applet=getReportApplet(container);
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
				new LoginNCForIUFO().login(applet, strRegKey,container);
			}

			ClassLoader clsLoader = container.getClass().getClassLoader();
			Class c = clsLoader.loadClass("nc.ui.fi.uforeport.FunctionDetailDisplay");
			Method m = c.getMethod("invoke", new Class[] { String.class,Container.class});
			m.invoke(c.newInstance(), strFunc,container);
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
			UIUtilities.sendMessage("此函数不支持联查", container);
		} catch (InstantiationException e) {
			AppDebug.debug(e);
		}
	}
	
	public static JApplet getReportApplet(Container container){
		JApplet applet=(JApplet)SwingUtilities.getAncestorOfClass(JApplet.class, container);
		if (applet==null)
			applet=FreeQueryMainBoardListner.getApplet();
		return applet;
	}
	
}
 