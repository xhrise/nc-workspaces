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
 * �����
 * @author zzl
 * @version 5.0
 * Create on 2004-10-25
 */
public  class FillPlugin extends AbstractPlugIn implements SelectListener{
	private AreaPosition m_areaSel = null;//���ʱԭѡ��������
	private SelectModel sm;
	private Cursor FILL_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private Cursor REPORT_CURSOR;
	private boolean m_isFilling;
	private MouseInputAdapter mia = new MouseInputAdapter(){
	    private CellPosition posAnchor;
	    private boolean isPressed;
	    /**
	     * ��ǰ����Point�Ƿ���ѡ���������½ǵ㸽����
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
				if(!isPressed){//��������ʱ��������ɿ�״̬��
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
				//�ж���䷽��
				int fillTo = -1;
				AreaPosition tmpArea = sm.getSelectedArea();
				if(tmpArea.getWidth() > m_areaSel.getWidth()){
					if(tmpArea.getStart().getColumn() < m_areaSel.getStart().getColumn()){
						fillTo = FillCmd.FillToLeft;//����.
					}else{
						fillTo = FillCmd.FillToRight;//����.
					}
				}else{
					if(tmpArea.getStart().getRow() < m_areaSel.getStart().getRow()){
						fillTo = FillCmd.FillToUp;//����.
					}else{
						fillTo = FillCmd.FillToDown;//����.
					}
				}
				//��ʼ���.
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
	 * �õ���ǰ����������ڵ�CellsPane��
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
	
	
	/* ���������ʱ��ѡ������ı�ʱ����Ҫ����������ʹ����ԭʼ����ȸ߻�ȿ�
	 * ERROR �Ƿ���Ҫ��Ӧselect�¼����о���ÿ�ΰ�������϶�������������ʱ�����ж�ѡ������Ϳ�����
	 * ANSWER ��Ϊһ����Ҫ��ʾ�϶����̵���ʾ�������һ����Ҫ�����϶��γɵ�ѡ������ֻ�ܵ���ߴ����󣩡�	 
	 * @see com.ufsoft.table.SelectListener#selectedChanged(com.ufsoft.table.SelectEvent)
	 */
	public void selectedChanged(SelectEvent e) {
		if(e.getProperty() == SelectEvent.SELECTED_CHANGE ){
			if(m_isFilling && m_areaSel != null){
				//��ʱ��������϶�״̬���޸�ѡ������ʹѡ��������ԭѡ������ȿ��ȸߡ�
				AreaPosition tmpArea = (AreaPosition)sm.getSelectedArea();//���ѡ�������
				tmpArea = tmpArea.getInstanceUnionWith(m_areaSel);
				AreaPosition newSelArea;
				if(tmpArea.getWidth()-m_areaSel.getWidth() >= tmpArea.getHeigth()-m_areaSel.getHeigth()){//������չ������������չ��
					//�޸�ѡ������Ϊ��ԭʼѡ������ͬ�ߡ�
					int c1 = tmpArea.getStart().getColumn();
					int c2 = tmpArea.getEnd().getColumn();
					int r1 = m_areaSel.getStart().getRow();
					int r2 = m_areaSel.getEnd().getRow();
					newSelArea = AreaPosition.getInstance(r1,c1,c2-c1+1,r2-r1+1);
				}else{
					//�޸�ѡ������Ϊ��ԭʼѡ������ͬ��
					int c1 = m_areaSel.getStart().getColumn();
					int c2 = m_areaSel.getEnd().getColumn();
					int r1 = tmpArea.getStart().getRow();
					int r2 = tmpArea.getEnd().getRow();
					newSelArea = AreaPosition.getInstance(r1,c1,c2-c1+1,r2-r1+1);
				}
				if(!newSelArea.equals(tmpArea)){//��ֹ����ĸ��¶���������������������ѭ����
				    sm.setSelectedArea(newSelArea);
				}
			}					
		}
	}

	/* ���� Javadoc��
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 */
	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		return null;
	}

}

