/*
 * AreaSelectDlg.java
 * �������� 2004-11-26
 * Created by CaiJie
 */
package com.ufsoft.report.dialog;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.RootPaneContainer;

import com.ufsoft.report.UfoReport;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
/**
 * ������նԻ�����ࡣ������Ҫʵ�ֱ߽������ѡ���ܵĶԻ���̳и��ࡣ
 * @author caijie
 * @since 3.1
 */

public class AreaSelectDlg extends UfoDialog {
	private boolean m_bFold; //true ��ʾ���ڴ����۵�״̬
	private Container m_container = null;
	
	private CellsModel cellsModel = null;
	
	/**
	 * ���캯��
	 */
	public AreaSelectDlg() {
		super();
	}

	/**
	 * ���캯��
	 * @param owner��������
	 */
	public AreaSelectDlg(Container owner,CellsModel cellsModel) {
		super(owner);
		this.cellsModel = cellsModel;
		m_container = owner;
		
	}
	
	
	/**
	 * �رնԻ��򴰿ڡ� �رնԻ��򴰿ڣ�ͨ�������ڹر���Ϣ �������ڣ�(2001-1-9 14:51:50)
	 */
	protected void closeDlg() {
		EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
		WindowEvent we = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		eq.postEvent(we);
	}
	/**
	 * �����Ƿ��۵�״̬�� �������ڣ�(2001-1-9 13:55:59)
	 * 
	 * @return boolean
	 */
	public boolean getIsFold() {
		return m_bFold;
	}
	/**
	 * �õ���ͼ��ѡ�е��������ơ� �������ڣ�(2001-1-9 13:19:38)
	 * 
	 * @param evt
	 * @return java.lang.String
	 */
	protected String getViewAreaName(AWTEvent evt) {
//		if (evt.getSource() instanceof RootPaneContainer) {
//			RootPaneContainer container = (RootPaneContainer) evt.getSource();
//			if(container.getRootPane() instanceof UfoReport){
//				UfoReport ufoReport = (UfoReport)((RootPaneContainer) evt.getSource()).getRootPane();
//				AreaPosition area = ufoReport.getCellsModel().getSelectModel().getSelectedArea();
//				return area.toString();
//			}
//		}
		
//		if(m_report != null){
			AreaPosition area = cellsModel.getSelectModel().getSelectedArea();
//			return area.toString();
//		}
		return area.toString();
	}
	/**
	 * �˷��������ڵ�����ĶԻ���ֱ�ӷ�����Ļ�ϣ��м�û�������ؼ�ʱ���� �������ڣ�(2001-1-9 13:37:08)
	 * @return java.awt.Point
	 * @param mevt java.awt.event.MouseEvent
	 */
	protected Point getPointToScreen(java.awt.event.MouseEvent mevt) {
		Point p = mevt.getPoint();
		//ת�����������Ļ������
		Rectangle rcCom = ((Component) (mevt.getSource())).getBounds();
		p.x += rcCom.x;
		p.y += rcCom.y;
		return p;
	}

	/**
	 * �õ���ͼ�������Ļ��λ�ü���С�� �������ڣ�(2001-1-9 13:02:13)
	 * @return java.awt.Rectangle
	 * @param mevt java.awt.event.MouseEvent
	 */
	protected java.awt.Rectangle getViewToScreenSize(
			java.awt.event.MouseEvent mevt) {
		
//		if(m_report == null){
//			return null;
//		}
//		
		//edit by whtao if (mevt.getSource() instanceof UfoMainFrame) {
		if (mevt.getSource() instanceof RootPaneContainer) {

			//��������view��ͼ���Ҳ����򵼵�һ���裬���ѡȡ����
//			CellsPane viewPane = m_report.getTable().getCells();
			CellsPane viewPane = null;
            if(getContainer() instanceof CellsPane){
            	viewPane = (CellsPane)getContainer();
            } else if(getContainer() instanceof UfoReport){
            	viewPane = ((UfoReport)getContainer()).getTable().getCells();
            }
            if (viewPane != null) {
				// �ҵ�CellsPane �������Ļ��λ��---
				Container conDialog = null;

				conDialog = viewPane;
				Point pLocView, pParent = new Point();
				pLocView = conDialog.getLocation();
				conDialog = conDialog.getParent();
				while (conDialog != null) {
					pParent = conDialog.getLocation();
					pLocView.translate(pParent.x, pParent.y);
					if (conDialog instanceof UfoReport)
						break;
					conDialog = conDialog.getParent();
				}
				// ---------------------
				Dimension dViewSize = viewPane.getSize();
				Rectangle mm = new Rectangle(pLocView.x, pLocView.y,
						dViewSize.width, dViewSize.height);
				return mm;
			}
		}
		return null;
	}
	
	protected Container getContainer(){
		return m_container;
	}
	
	/**
	 * ���öԻ����۵����bDlgFold=true �۵��� �������ڣ�(2001-1-8 9:18:32)
	 * @param bDlgFold boolean
	 */
	public void setFold(boolean bDlgFold) {
		m_bFold = bDlgFold;
	}
	/**
	 * �رնԻ��򴰿ڣ�ͨ�������ڹر���Ϣ�� �������ڣ�(2001-1-9 15:02:33)
	 * @param n int
	 */
	public void setResult(int n) { 
		super.setResult(n);
		closeDlg();
	}

	protected CellsModel getCellsModel() {
		return cellsModel;
	}

	protected void setCellsModel(CellsModel cellsModel) {
		this.cellsModel = cellsModel;
	}
}

