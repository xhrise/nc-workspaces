package com.ufsoft.report.sysplugin.fillcell;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.exception.MessageException;
import com.ufida.zior.plugin.IPlugin;
import com.ufida.zior.util.UIUtilities;
import com.ufida.zior.view.Viewer;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.fill.FillCmd;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.event.CellsModelSelectedListener;
import com.ufsoft.table.event.MouseListenerAdapter;
import com.ufsoft.table.event.MouseEventAdapter;

public class FillCellHandler extends MouseListenerAdapter implements CellsModelSelectedListener{

	private AreaPosition m_areaSel = null;//���ʱԭѡ��������
	private Cursor FILL_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private Cursor REPORT_CURSOR;
	private boolean m_isFilling;
	private CellPosition posAnchor;
	private boolean isPressed;
	
	IPlugin plugin = null;
	
	FillCellHandler(IPlugin p){
		this.plugin = p;
	}
	
	@Override
	public void mouseMoved(MouseEventAdapter e) {
		CellsPane cellsPane = getCellsPane();
		if (cellsPane == null) {
			return;
		}
		if (isInCtrlArea(e.getPoint())) {
			cellsPane.setCursor(FILL_CURSOR);
			m_isFilling = true;
			SelectModel selectModel = getCellsModel().getSelectModel();
			m_areaSel = selectModel.getSelectedArea();
			posAnchor = selectModel.getAnchorCell();
		} else {
			if (!isPressed) {// ��������ʱ��������ɿ�״̬��
				cellsPane.setCursor(REPORT_CURSOR);
				m_isFilling = false;
			}
		}
	}

	@Override
	public void mousePressed(MouseEventAdapter e) {
		if(isInCtrlArea(e.getPoint())){
			SelectModel selectModel = getCellsModel().getSelectModel();
	        isPressed = true;  
	        selectModel.setAnchorCell(posAnchor);
	    }
	}

	@Override
	public void mouseReleased(MouseEventAdapter e) {
		if(m_areaSel == null){
	        return;
	    }
	    getCellsPane().setCursor(REPORT_CURSOR);	
		m_isFilling = false;
		if(isPressed){
		    isPressed = false;
			//�ж���䷽��
			int fillTo = -1;
			SelectModel selectModel = getCellsModel().getSelectModel();
			AreaPosition tmpArea = selectModel.getSelectedArea();
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
			
			try {//�˵ط��ƹ��˿�ܵ�ִ�У��Լ������쳣
				
				doFill(fillTo);
			} catch (MessageException e1) {
				UIUtilities.sendMessage(e1, getCellsPane());
			} catch(Throwable e2){
				AppDebug.debug(e2);
				UIUtilities.sendErrorMessage(e2.getMessage(), getCellsPane(), e2);
			}
			
		}
		m_areaSel = null;
	
	}
	
	public void anchorChanged(CellsModel model, CellPosition oldAnchor,
			CellPosition newAnchor) {
		
	}

	public void selectedChanged(CellsModel cellsModel,
			AreaPosition[] changedArea) {
		if(m_isFilling && m_areaSel != null){
			//��ʱ��������϶�״̬���޸�ѡ������ʹѡ��������ԭѡ������ȿ��ȸߡ�
			SelectModel selectModel = cellsModel.getSelectModel();
			AreaPosition tmpArea = (AreaPosition)selectModel.getSelectedArea();//���ѡ�������
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
				selectModel.setSelectedArea(newSelArea);
			}
		}	
	}
	
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
	    AreaPosition tmpArea = getCellsModel().getSelectModel().getSelectedArea();
		Rectangle rect = cp.getCellRect(tmpArea, true);
		Point endPoint = new Point(rect.x + rect.width, rect.y + rect.height);
		if(endPoint.distance(p) < 5){
		    return true;
		}
		return false;
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
	
	private void doFill(int fillType) {
		switch (fillType) {
		case FillCmd.FillToUp:
            new FillToUpAction(plugin).execute(null);
			break;
		case FillCmd.FillToDown:
            new FillToDownAction(plugin).execute(null);
			break;
		case FillCmd.FillToLeft:
            new FillToLeftAction(plugin).execute(null);
			break;
		case FillCmd.FillToRight:
            new FillToRightAction(plugin).execute(null);
			break;
		default:
			break;
		}
	}
	
	CellsModel getCellsModel(){
		ReportDesigner editor = getEditor();
		return editor == null ? null : editor.getCellsModel();
	}

	CellsPane getCellsPane(){
		ReportDesigner editor = getEditor();
		return editor == null ? null : editor.getCellsPane();
	}
	
	UFOTable getTable(){
		ReportDesigner editor = getEditor();
		return editor == null ? null : editor.getTable();
	}
	
	private ReportDesigner getEditor(){
		Viewer viewer = plugin.getMainboard().getCurrentView();
		if(!(viewer instanceof ReportDesigner)){
			return null;
		}
		return (ReportDesigner)viewer;
	}

}
