package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedCalInfo;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.table.CellPosition;


public class FormulaParsedTMColValueData implements IFormulaParsedCalInfo {
	protected static String STR_CALULATE = "";//计算
	/**
	 * 公式追踪子项的计算值
	 */
	private Object m_objFormulaValue;
	/**
	 * 是否需要进一步计算:计算Button
	 *  他表指标引用、业务函数,总账或其他外系统公式以及不在本任务中的公式
	 */
	private boolean m_bNeedToCal = false;
	public static IFormulaParsedCalInfo copyInstance(IFormulaParsedCalInfo formulaParsedCalInfo){
		if(formulaParsedCalInfo == null){
			return null;
		}
		IFormulaParsedCalInfo formulaParsedCalInfoNew = new FormulaParsedTMColValueData();
		formulaParsedCalInfoNew.setFormulaValue(formulaParsedCalInfo.getFormulaValue());
		formulaParsedCalInfoNew.setNeedToCal(formulaParsedCalInfo.isNeedToCal());
		return formulaParsedCalInfoNew;
		
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedCalInfo#getFormulaValue()
	 */
	public Object getFormulaValue(){
		return this.m_objFormulaValue;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedCalInfo#getTracedExpr()
	 */
	public UfoExpr getTracedExpr() {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedCalInfo#setFormulaValue(java.lang.Object)
	 */
	public void setFormulaValue(Object objFormulaValue){
		this.m_objFormulaValue = objFormulaValue;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedCalInfo#isNeedToCal()
	 */
	public boolean isNeedToCal(){
		return this.m_bNeedToCal;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedCalInfo#setNeedToCal(boolean)
	 */
	public void setNeedToCal(boolean bNeedToCal){
		this.m_bNeedToCal = bNeedToCal;
	}
	public Object toUICalValue() {
		return FormulaParsedDataItem.doGetUICalValue(this);
	}
	public String toString(){
		Object objValue = toUICalValue();
		if(objValue!= null){
			return objValue.toString();
		}
		return "";
	}
	public String getNCFuncStr() {
		// TODO Auto-generated method stub
		return null;
	}
	public CellPosition getRelaCell() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isNCFunc() {
		// TODO Auto-generated method stub
		return false;
	}
	public void setNCFunc(boolean bNCFunc) {
		// TODO Auto-generated method stub
		
	}
	public void setNCFuncStr(String func) {
		// TODO Auto-generated method stub
		
	}
	public void setRelaCell(CellPosition cell) {
		// TODO Auto-generated method stub
		
	}
	public boolean isAreaFunc() {
		// TODO Auto-generated method stub
		return false;
	}
	public void setAreaFunc(boolean bAreaFunc) {
		// TODO Auto-generated method stub
		
	}
	public CellPosition getAbsCell() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setAbsCell(CellPosition cell) {
		// TODO Auto-generated method stub
		
	}
	public String getDynAreaPK() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isInDynArea() {
		// TODO Auto-generated method stub
		return false;
	}
	public void setDynAreaPK(String strDynAreaPK) {
		// TODO Auto-generated method stub
		
	}
	public void setInDynArea(boolean isInDynArea) {
		// TODO Auto-generated method stub
		
	}
	public int getUnitDataNum() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setUnitDataNum(int unitDataNum) {
		// TODO Auto-generated method stub
		
	}
	
}
