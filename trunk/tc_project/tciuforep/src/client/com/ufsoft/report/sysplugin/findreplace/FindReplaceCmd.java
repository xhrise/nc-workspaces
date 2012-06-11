package com.ufsoft.report.sysplugin.findreplace;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.util.UIUtilities;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectModel;

public class FindReplaceCmd extends UfoCommand {
	private boolean isFind;
	
	public FindReplaceCmd(boolean isFind){
		this.isFind=isFind;
	}

	private UfoReport report;

	public void execute(Object[] params) {
		report = (UfoReport) params[0];
		new FindReplaceDlg(report,this,isFind).setVisible(true);
	}
	/**
	 * 
	 * @param findContent 查找的内容
	 * @param diffBigSmall 是否区分大小写
	 */
	public boolean findNext(String findContent, boolean diffBigSmall) {
		CellPosition anchorPos = getSelectModel().getAnchorCell();
		CellPosition nextPos = findNextPos(anchorPos, findContent, diffBigSmall);
		if(nextPos != null&&report!=null){
			report.getTable().getCells().changeSelectionByUser(nextPos.getRow(), nextPos.getColumn(), false, false, false);
			return true;
		}
		return false;
	}
	private CellPosition findNextPos(CellPosition beginPos, String findContent, boolean diffBigSmall){
		CellPosition nextPos = beginPos;
		while(true){
			nextPos = getNextPos(nextPos);	
			if(isMatch(nextPos,findContent,diffBigSmall)){
				return nextPos;
			}			
			if(nextPos.equals(beginPos)){
				return null;
			}
		}	
	}
	
	public void replaceOne(String findContent, String replaceContent, boolean diffBigSmall) {
		boolean hasFind = findNext(findContent,diffBigSmall);
		if(hasFind){
			replaceImpl(findContent,replaceContent,diffBigSmall);
		}
	}

   /**
    * 
    * @param findContent
    * @param replaceContent
    * @param diffBigSmall
    */
	public void replaceAll(String findContent, String replaceContent, boolean diffBigSmall) {
		CellPosition beginPos = getSelectModel().getAnchorCell();//开始位置
		CellPosition nextPos = findNextPos(beginPos, findContent,diffBigSmall);
		if(nextPos == null)
			return;
		replaceImplByPos(nextPos, findContent, replaceContent, diffBigSmall);
		beginPos = nextPos;//第一个替换的位置
		
		while(true){
			nextPos = findNextPos(nextPos, findContent,diffBigSmall);
			if(nextPos == null || beginPos.equals(nextPos))
				break;
			replaceImplByPos(nextPos, findContent, replaceContent, diffBigSmall);
		}
	}
	private CellPosition getNextPos(CellPosition curPos){
		AreaPosition allArea = AreaPosition.getInstance(0,0,
				getCellsModel().getColNum(),getCellsModel().getRowNum());
		ArrayList allPos = getCellsModel().getSeperateCellPos(allArea);
		int index = allPos.indexOf(curPos);
		index ++;
		if(index == allPos.size()){
			index = 0;
		}
		return (CellPosition) allPos.get(index);
	}
	
	private boolean isValidateType(Object selectValue){
		boolean isType=false;
		if(selectValue instanceof String||selectValue instanceof Double){
			isType=true;
		}
		
		return isType;
	}
	
	private boolean isNumberType(Object selectValue){
		if(selectValue instanceof Double){
			return true;
		}
		return false;
	}
		
	/**
	 * 单元位置是否匹配查找字符串.
	 * @param nextPos
	 * @param findContent
	 * @param diffBigSmall 
	 * @return
	 */
	private boolean isMatch(CellPosition curPos, String findContent, boolean diffBigSmall) {
		Object objValue = getCellsModel().getCellValue(curPos);
		if(!isValidateType(objValue)){
			return false;
		}
		String value =objValue.toString();
		if(isNull(value)){
			if(isNull(findContent)){
				return true;
			}else{
				return false;
			}
		}else{
			//value不是空值.
			if(isNull(findContent)){
				return false;
			}
			String strValue = (String) value;
			if(!diffBigSmall){
				strValue = strValue.toLowerCase();
				findContent = findContent.toLowerCase();
			}
			return strValue.indexOf(findContent) > -1;			
		}
	}
	private void replaceImpl(String findContent, String replaceContent, boolean diffBigSmall) {
		CellPosition anchorPos = getSelectModel().getAnchorCell();
		replaceImplByPos(anchorPos, findContent, replaceContent, diffBigSmall);
	}
	private void replaceImplByPos(CellPosition aimPos, String findContent, String replaceContent, boolean diffBigSmall) {
		CellPosition anchorPos = aimPos;
		Object valueObj =  getCellsModel().getCellValue(anchorPos);
		String value = String.valueOf(valueObj);
		if(isNull(value) && isNull(replaceContent)){
			return;
		}
		if(isNull(value)){
			value = replaceContent;
		}else{
			value = value.replaceAll(findContent,replaceContent);
		}
		
		try {
			getCellsModel().setCellValueByAuth(anchorPos.getRow(),anchorPos.getColumn(),isNumberType(valueObj) ? Double.parseDouble(value) :value);
		} catch (NumberFormatException e) {
			AppDebug.debug(e);
			UIUtilities.sendWarningMessage("类型不匹配，不能替换", null);
		} catch (Throwable e) {
			AppDebug.debug(e);
		}
		
		
	}
	
	private boolean isNull(String str){
		return str == null || str.equals("");
	}
	private CellsModel getCellsModel(){
		return report.getCellsModel();
	}
	private SelectModel getSelectModel(){
		return report.getCellsModel().getSelectModel();
	}
}
