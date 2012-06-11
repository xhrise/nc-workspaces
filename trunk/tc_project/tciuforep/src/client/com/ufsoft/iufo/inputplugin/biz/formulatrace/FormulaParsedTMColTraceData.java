package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedTraceInfo;
import com.ufsoft.iuforeport.tableinput.applet.TraceDataParam;
import com.ufsoft.table.IArea;

public class FormulaParsedTMColTraceData implements IFormulaParsedTraceInfo  {
	protected static String STR_TRACE = "";//联查

	/**
	 * 是否可追踪,联查Location
	 */
	private boolean m_bCanTrace = false;
	/**
	 * 是否是在本表内追踪,联查Location
	 * NOTICE:当m_bCanTrace=true时，才有意义
	 */
	private boolean m_bTraceSelf = true;
	/**
	 * 公式追踪鼠标焦点Area
	 */
	private IArea m_oAnchorArea = null;
	/**
	 * 公式追踪本表联查Location的单元格
	 * NOTICE:当m_bTraceSelf=true时，才有意义
	 */
	private IArea[] m_oTracedPos = null;
	/**
	 * 公式追踪他表联查是否单值
	 * NOTICE:当m_bTraceSelf=false时，才有意义
	 */
	private boolean m_bTraceMultiValues = false;

	public static IFormulaParsedTraceInfo copyInstance(IFormulaParsedTraceInfo formulaParsedTraceInfo){
		if(formulaParsedTraceInfo == null){
			return null;
		}
		IFormulaParsedTraceInfo formulaParsedTraceInfoNew = new FormulaParsedTMColTraceData();
		formulaParsedTraceInfoNew.setCanTrace(formulaParsedTraceInfo.isCanTrace());
		formulaParsedTraceInfoNew.setTraceSelf(formulaParsedTraceInfo.isTraceSelf());
		formulaParsedTraceInfoNew.setTracedPos(formulaParsedTraceInfo.getTracedPos());
		formulaParsedTraceInfoNew.setTraceMultiValues(formulaParsedTraceInfo.isTraceMultiValues());
		return formulaParsedTraceInfoNew;
		
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedTraceInfo#isCanTrace()
	 */
	public boolean isCanTrace(){
		return this.m_bCanTrace;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedTraceInfo#setCanTrace(boolean)
	 */
	public void setCanTrace(boolean bCanTrace){
		this.m_bCanTrace = bCanTrace;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedTraceInfo#isTraceSelf()
	 */
	public boolean isTraceSelf(){
		return this.m_bTraceSelf;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedTraceInfo#setTraceSelf(boolean)
	 */
	public void setTraceSelf(boolean bTraceSelf){
		this.m_bTraceSelf = bTraceSelf;
	}
	public IArea getAnchorArea() {
		return m_oAnchorArea;
		
	}
	public void setAnchorArea(IArea anchorArea) {
		m_oAnchorArea = anchorArea;
		
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedTraceInfo#getTracedPos()
	 */
	public IArea[] getTracedPos(){
		return this.m_oTracedPos;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedTraceInfo#setTracedPos(com.ufsoft.table.IArea)
	 */
	public void setTracedPos(IArea[] oTracedPos){
		this.m_oTracedPos = oTracedPos;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedTraceInfo#isTraceMultiValues()
	 */
	public boolean isTraceMultiValues(){
		return this.m_bTraceMultiValues;
	}
	/* (non-Javadoc)
	 * @see com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaParsedTraceInfo#setTraceMultiValues(boolean)
	 */
	public void setTraceMultiValues(boolean bTraceMultiValues){
		this.m_bTraceMultiValues = bTraceMultiValues;
	}
	public Object toUITraceValue() {
		return FormulaParsedDataItem.doGetUITraceValue(this);
	}
	public String toString(){
		Object objValue = toUITraceValue();
		if(objValue!= null){
			return objValue.toString();
		}
		return "";
	}
	public int getRelaCellInAreaFml() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setRelaCellInAreaFml(int relaCell) {
		// TODO Auto-generated method stub
		
	}
	public com.ufida.dataset.tracedata.TraceDataParam getTraceDataParam() {
		return null;
	}
	public void setTraceDataParam(com.ufida.dataset.tracedata.TraceDataParam traceDataParam) {
		
	}	

}
