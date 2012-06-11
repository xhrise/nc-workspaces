package com.ufsoft.report.sysplugin.cellattr;

import java.util.ArrayList;
import java.util.Hashtable;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.format.ConditionFormat;
import com.ufsoft.table.format.Format;
/**
 * 系统插件功能点：设置单元的条件格式属性
 * @author guogang
 *
 */
public class SetConditionAttrExt extends SetCellAttrExt {
	 /**
	  * 构造方法
	  * @param rep
	  */
	 public SetConditionAttrExt(UfoReport rep) {
	       super(rep);       
	    }
	 /**
	  * @see com.ufsoft.report.plugin.IActionExt#getCommand()
	  */
	 public UfoCommand getCommand() {
	        
	        return new SetCellConditionCmd(this.getReport());
	    }
	 /**
	  * @see com.ufsoft.report.plugin.IActionExt#getParams()
	  */
	@SuppressWarnings("unchecked")
	public Object[] getParams(UfoReport container) {
		 
		 Object[] params =new Object[8+1];
		 params=getCellParams(params);
		 Format format =computeFormat();
//		 Hashtable conditionFormat=null;
		 ArrayList<ConditionFormat> conditionFormat=null;
		 CellPosition[] selectedCells = getSelectCells();
		 if(selectedCells!=null && selectedCells.length==1 && format!= null && format.isCondition()){
//			 conditionFormat=(Hashtable)conditionList.get(selectedCells[0]);
			 conditionFormat=(ArrayList<ConditionFormat>)conditionList.get(selectedCells[0]);
		 }
//		 ConditionFormatDialog dlg=new ConditionFormatDialog(container,format,conditionFormat);
		 MultiConditionFormatDialog dlg=new MultiConditionFormatDialog(container,format,conditionFormat);
		 dlg.setVisible(true);
		 if (dlg.getResult() == UfoDialog.ID_OK) {                   
             params[0] = ConditionFormat.EXT_FMT_CONDITIONFMT;  
          
             params[8] = dlg.getConditionFmt();//用于子类的扩展属性
            
         }else{
        	 params=null;
         }
		 return params;
	}

	/**
	 * 获取该功能点挂载的位置信息
	  * @see com.ufsoft.report.plugin.IActionExt#getUIDesArr()
	  */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(MultiLang.getString("uiuforep0001111"));
        uiDes1.setPaths(new String[]{MultiLang.getString("format")});
        uiDes1.setShowDialog(true);
        return new ActionUIDes[]{uiDes1};
	}
 
}
