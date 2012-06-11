package com.ufsoft.iufo.inputplugin.querynavigation;

import java.io.Serializable;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.MultiLang;

public class QueryNaviItem implements Serializable, IUfoContextKey{ 
 
	private static final long serialVersionUID = -7934486249134166569L;

	private UfoReport report = null;
	
	//´°¿Ú±êÌâ
	private String title = "";
	
	QueryNaviItem(){
		
	}
	
	QueryNaviItem(UfoReport report, String title){   
		this.report = report;
		this.title = title;
	}
	 
	public UfoReport getReport() {
		return report;
	}

	public void setReport(UfoReport report) {
		this.report = report;
	}

	protected boolean equalsItem(String repCode, String strAlondID, String strTaskPK){
		if(repCode.equals(this.getRepCode()) && strAlondID.equals(this.getAloneID()) && strTaskPK.equals(this.getTaskPK())){
			return true;
		}
		
		return false;
	}
	
	protected String getRepName() {
		return report.getContextVo().getName();
	}
 
	protected String getRepCode() {
		return report.getContextVo().getReportcode();
	}
	
	 
	protected String getAloneID() {
		if(getTableInputTransObj() == null)
			return "";
//		ContextVO context = report.getContextVo();
//		if(context != null && context instanceof TableInputContextVO){
//			return ((TableInputContextVO) context).getInputTransObj().getRepDataParam().getAloneID();
//			
//		}
//		return "";
		return getTableInputTransObj().getRepDataParam().getTaskPK();
	}
	 
	protected String getTaskPK() {
		if(getTableInputTransObj() == null)
			return "";
//		ContextVO context = report.getContextVo();
//		if(context != null && context instanceof TableInputContextVO){
//			Object tableInputTransObj = ((TableInputContextVO) context).getAttribute(TABLE_INPUT_TRANS_OBJ);
//			TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
			
			return getTableInputTransObj().getRepDataParam().getTaskPK();
			
//		}
//		return "";
	}
	
	private TableInputTransObj getTableInputTransObj(){
		ContextVO context = report.getContextVo();
		TableInputTransObj inputTransObj = null;
		if(context != null && context instanceof TableInputContextVO){
			Object tableInputTransObj = ((TableInputContextVO) context).getAttribute(TABLE_INPUT_TRANS_OBJ);
			inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		}
		return inputTransObj;
	}
	
	/**
	 * @i18n uiuforep00126=¡ª
	 */
	public String getSimpleTitle(){
		String repCode = getRepCode();
		if(repCode != null)
			return title + MultiLang.getString("uiuforep00126")  + repCode;
		
		return title;
	}
	
	public String getTitle(){
		return toString();
	}
	
	/**
	 * @i18n uiuforep00126=¡ª
	 */
	public String toString(){
		String repCode = getRepCode();
		if(repCode == null){
			return title;
		}
			
		return title + MultiLang.getString("uiuforep00126")  + "(" + getRepCode() + ")" + getRepName();
	}
		
}
 