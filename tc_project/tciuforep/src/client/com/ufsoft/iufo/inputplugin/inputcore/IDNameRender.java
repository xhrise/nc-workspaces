package com.ufsoft.iufo.inputplugin.inputcore;

import java.awt.Component;
import java.lang.reflect.Method;

import com.ufsoft.iufo.inputplugin.key.KeyFmt;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.re.DefaultSheetCellRenderer;
import com.ufsoft.table.re.IDName;

/**
 * 
 * @author zzl 2005-6-26
 */
public class IDNameRender extends DefaultSheetCellRenderer {
    /**
     * <code>serialVersionUID</code> 的注释
     */
    private static final long serialVersionUID = 8216385011479752344L;

    /*
     * modify by guogang 修正动态区关键字缩小字体渲染时出错的问题
     * @see com.ufsoft.table.re.DefaultSheetCellRenderer#getCellRendererComponent(com.ufsoft.table.CellsPane, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getCellRendererComponent(CellsPane table, Object obj, boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
//        Component editorComp = super.getCellRendererComponent(table,obj,isSelected,hasFocus,row,column);
        
        String strShow = getShowString(table,cell,row,column);
        Component editorComp = super.getCellRendererComponent(table,strShow,isSelected,hasFocus,row,column,cell);
        try{
            Method method = editorComp.getClass().getMethod("setText",new Class[]{String.class});
            method.invoke(editorComp,new Object[]{strShow});
        }catch(Exception e){
            throw new RuntimeException("IDName Render is not support setText method!");
        }
    	
        return editorComp;        
    }
    /**
     * ”参照型指标是否显示代码值“的设置，适用于关键字（除单位和对方单位关键字外）。
     * @param table
     * @param cell
     * @param row
     * @param column
     * @return String
     */
    private String getShowString(CellsPane table,Cell cell,int row,int column) {
        IDName idName = (IDName) cell.getValue();
        KeyFmt keyFmt = (KeyFmt) cell.getExtFmt(KeyFmt.EXT_FMT_KEYINPUT);  
        String mainText = getNameOrID(keyFmt,idName);
        String rtnText = mainText;
        if(keyFmt != null){                     
            String addText = keyFmt.toString();            
            if(addText != null && (!keyFmt.isInDynArea())){
                rtnText = addText+":"+mainText;
            }
        }
        return rtnText;        
    }
    private String getNameOrID(KeyFmt keyFmt, IDName idName){
        if(//KeyFmt.isUnitKey(keyFmt) && 
        		keyFmt != null && !keyFmt.isInDynArea()){
            return idName.getName();
        }else{
    		 return ReportStyle.isShowRefID() ? idName.getID() : idName.getName();
        }
    }
    protected String decorateValue(Component render,Object value ,Format format){
		if(value==null){
			return "";
		}else if(value instanceof IDName){
			return ((IDName)value).getID()+"|"+((IDName)value).getName();
		}else{
			return value.toString();
		}
	}
}
