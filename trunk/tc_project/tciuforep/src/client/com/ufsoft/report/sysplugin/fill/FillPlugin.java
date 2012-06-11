package com.ufsoft.report.sysplugin.fill;

import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import javax.swing.event.MouseInputAdapter;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.*;
import com.ufsoft.table.event.SelectEvent;
import com.ufsoft.table.re.*;
/**
 * 填充插件
 * @author zzl
 * @version 5.0
 * Create on 2004-10-25
 */
public  class FillPlugin extends AbstractPlugIn implements SelectListener{
	private AreaPosition m_areaSel = null;//填充时原选定的区域。
	private SelectModel sm;
	private Cursor FILL_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private Cursor REPORT_CURSOR;
	private boolean m_isFilling;
	private MouseInputAdapter mia = new MouseInputAdapter(){
	    private CellPosition posAnchor;
	    private boolean isPressed;
	    /**
	     * 当前参数Point是否在选择区的右下角点附近。
	     * @param p
	     * @return
	     */
		private boolean isInCtrlArea(Point p){
		    CellsPane cp = getFocusCellsPane();
			if(cp == null){
				return false;
			}
		    AreaPosition tmpArea = sm.getSelectedArea();
			Rectangle rect = cp.getCellRect(tmpArea, true);
			Point endPoint = new Point(rect.x + rect.width, rect.y + rect.height);
			if(endPoint.distance(p) < 5){
			    return true;
			}
			return false;
		}
		public void mouseMoved(MouseEvent e){
		    if(isInCtrlArea(e.getPoint())){
		        getReport().setCursor(FILL_CURSOR);	
				m_isFilling = true;
				m_areaSel = sm.getSelectedArea();
				posAnchor = sm.getAnchorCell();
		    }else{
				if(!isPressed){//如果出组件时，鼠标是松开状态。
				    getReport().setCursor(REPORT_CURSOR);
					m_isFilling = false;
				}
		    }
		}
		public void mousePressed(MouseEvent e){
		    if(isInCtrlArea(e.getPoint())){
		        isPressed = true;  
		        sm.setAnchorCell(posAnchor);
		    }
		}
		public void mouseReleased(MouseEvent e){
		    if(m_areaSel == null){
		        return;
		    }
		    getReport().setCursor(REPORT_CURSOR);	
			m_isFilling = false;
			if(isPressed){
			    isPressed = false;
				//判断填充方向。
				int fillTo = -1;
				AreaPosition tmpArea = sm.getSelectedArea();
				if(tmpArea.getWidth() > m_areaSel.getWidth()){
					if(tmpArea.getStart().getColumn() < m_areaSel.getStart().getColumn()){
						fillTo = FillCmd.FillToLeft;//向左.
					}else{
						fillTo = FillCmd.FillToRight;//向右.
					}
				}else{
					if(tmpArea.getStart().getRow() < m_areaSel.getStart().getRow()){
						fillTo = FillCmd.FillToUp;//向上.
					}else{
						fillTo = FillCmd.FillToDown;//向下.
					}
				}
				//开始填充.
				new FillCmd(getReport()).execute(new Object[]{tmpArea,new Integer(fillTo)});
			}
			m_areaSel = null;
		}
	};
	
	
	/* @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
	}

	/* @see com.ufsoft.report.plugin.IPlugIn#shutdown()
	 */
	public void shutdown() {
	}



	/* @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	public IPluginDescriptor createDescriptor() {
		return new FillDescriptor(this);
	}

	/* @see com.ufsoft.report.plugin.IPlugIn#setReport(com.ufsoft.report.UfoReport)
	 */
	public void setReport(UfoReport report) {
		super.setReport(report);
		sm = getReport().getCellsModel().getSelectModel();	
		REPORT_CURSOR = getReport().getCursor();
		getReport().getTable().getCells().addMouseListener(mia);
		getReport().getTable().getCells().addMouseMotionListener(mia);
	}
	/**
	 * 得到当前焦点组件所在的CellsPane。
	 * @return CellsPane
	 */
	public CellsPane getFocusCellsPane(){		
		Component focusCom = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
		while(true){
			if(focusCom == null){
				return null;
			}
			if(focusCom instanceof CellsPane){
				return (CellsPane)focusCom;
			}
			focusCom = focusCom.getParent();			
		}
	}

	/* @see com.ufsoft.report.plugin.IPlugIn#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}

	/* @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
	 */
	public String[] getSupportData() {
		return null;
	}

	/* @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.Class)
	 */
	public SheetCellRenderer getDataRender(String extFmtName) {
		return null;
	}

	/* @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.Class)
	 */
	public SheetCellEditor getDataEditor(String extFmtName) {
		return null;
	}

	/* @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
	 */
	public void actionPerform(UserUIEvent e) {
	}
	
	
	/* 当属于填充时的选择区域改变时，需要修正新区域使其与原始区域等高或等宽。
	 * ERROR 是否需要响应select事件，感觉在每次按下鼠标拖动，并且是填充的时候来判断选择区域就可以了
	 * ANSWER 因为一方面要显示拖动过程的显示结果，另一方面要限制拖动形成的选择区域（只能单项尺寸扩大）。	 
	 * @see com.ufsoft.table.SelectListener#selectedChanged(com.ufsoft.table.SelectEvent)
	 */
	public void selectedChanged(SelectEvent e) {
		if(e.getProperty() == SelectEvent.SELECTED_CHANGE ){
			if(m_isFilling && m_areaSel != null){
				//这时属于填充拖动状态，修改选择区域，使选择区域与原选择区域等宽或等高。
				AreaPosition tmpArea = (AreaPosition)sm.getSelectedArea();//最后选择的区域。
				tmpArea = tmpArea.getInstanceUnionWith(m_areaSel);
				AreaPosition newSelArea;
				if(tmpArea.getWidth()-m_areaSel.getWidth() >= tmpArea.getHeigth()-m_areaSel.getHeigth()){//横向扩展量大于纵向扩展量
					//修改选择区域为和原始选择区域同高。
					int c1 = tmpArea.getStart().getColumn();
					int c2 = tmpArea.getEnd().getColumn();
					int r1 = m_areaSel.getStart().getRow();
					int r2 = m_areaSel.getEnd().getRow();
					newSelArea = AreaPosition.getInstance(r1,c1,c2-c1+1,r2-r1+1);
				}else{
					//修改选择区域为和原始选择区域同宽。
					int c1 = m_areaSel.getStart().getColumn();
					int c2 = m_areaSel.getEnd().getColumn();
					int r1 = tmpArea.getStart().getRow();
					int r2 = tmpArea.getEnd().getRow();
					newSelArea = AreaPosition.getInstance(r1,c1,c2-c1+1,r2-r1+1);
				}
				if(!newSelArea.equals(tmpArea)){//防止本身的更新动作又引发操作，引起死循环。
				    sm.setSelectedArea(newSelArea);
				}
			}					
		}
	}

	/* （非 Javadoc）
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 */
	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		return null;
	}

}

