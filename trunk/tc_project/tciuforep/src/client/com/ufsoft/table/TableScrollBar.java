package com.ufsoft.table;

import java.awt.*;
import javax.swing.*;

import com.ufsoft.table.header.*;

/**
 * <p>Title:���ʹ�õĹ����� </p>
 * <p>Description: ���������������¹��ܣ�������ޱ�����������ĩ�ˣ���Ҫ�Զ���������ģ�͵�
 * ���ȡ�������Ҫ�����������������Ӧ�Ĺ������ϡ�</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public class TableScrollBar extends  nc.ui.pub.beans.UIScrollBar {  //JScrollBar {//
	private static final long serialVersionUID = -8772937661066158747L;
	/**��������Ӧ����ͼ�е����*/
	private CellsPane pane;
	/**��ǰ�Ƿ������ޱ�*/
	private boolean infinite;
	/**
	 * ���캯��
	 * @param orientation �������ķ��򣬲μ�TableConstants
	 * @param pane ��Ӧ���������ʵ����Scrollable�ӿڣ����еõ�ÿ�ι��������ش�С
	 * @param infinite �Ƿ����ޱ�
	 */
	public TableScrollBar(int orientation, CellsPane pane, boolean infinite) {
		super(orientation);
		if (pane == null) {
			throw new IllegalArgumentException();
		}
		this.pane = pane;
		this.infinite = infinite;
		setUI(TableScrollBarUI.createUI(this));
	}

	/**
	 * �Ƿ����ޱ�
	 * @return boolean
	 */
	public boolean isInfinite() {
		return infinite;
	}

	

	/**
	 * �õ���������Ӧ��ͷ��������ģ��
	 * @return HeaderModel
	 */
	public HeaderModel getHeaderModel() {
		if(orientation==TableScrollBar.HORIZONTAL){//ˮƽ������,������ģ��
			return pane.getDataModel().getColumnHeaderModel();
		}else {
			return pane.getDataModel().getRowHeaderModel();
		}
	}

	/* 
	 * @see javax.swing.JScrollBar#setValues(int, int, int, int)
	 */
	public void setValues(int newValue, int newExtent, int newMin, int newMax) {
		     super.setValues(newValue, newExtent, newMin, newMax);
//		//zzl���ϱ�һ���޸�Ϊ����һ�Σ�
//		//Ŀ���Ǳ�֤��ͼ�Ķ�ʱ�����������������棬���ټ��������ƶ���
//		//����������������������·���ʱ�������϶���ѡ���¼�����������ͼ���������ʵ�λ�ã�����ͼ���ŵ��������ʵ�λ�ã�
//		//����ͼ�ĵ�������������¼����������¼���������ͼ���������ƶ�������������ߵ�����ʹ�ұ߳��ְ��
//		javax.swing.event.ChangeListener[] aa = ((DefaultBoundedRangeModel) this
//				.getModel()).getChangeListeners();
//		com.ufsoft.table.TablePaneUI.SBChangeListener sb = null;
//		for (int i = 0; i < aa.length; i++) {
//			if (aa[i] instanceof com.ufsoft.table.TablePaneUI.SBChangeListener) {
//				sb = (com.ufsoft.table.TablePaneUI.SBChangeListener) aa[i];
//			}
//		}
////		this.getModel().removeChangeListener(sb);
//		super.setValues(newValue, newExtent, newMin, newMax);
////		this.getModel().addChangeListener(sb);
	}

	
	/**
	 * ���㵱ǰ���ɵ�ViewPort�еĹ��������ÿ����λ�ƶ��ľ���
	 * @param direction <0��ʶ��������>0��ʶ��������
	 * @return int
	 */
	public int getUnitIncrement(int direction) {
		JViewport vp = (JViewport) pane.getParent();
		Rectangle vr = vp.getViewRect();
		return pane.getScrollableUnitIncrement(vr, getOrientation(), direction);
	}

	/**
	 * ����һ��Block�ƶ�������λ��
	 * @param direction <0��ʶ��������>0��ʶ��������
	 * @return int
	 */
	public int getBlockIncrement(int direction) {
		JViewport vp = (JViewport) pane.getParent();
		Rectangle vr = vp.getViewRect();
		return pane
				.getScrollableBlockIncrement(vr, getOrientation(), direction);

	}
	public CellsPane getCellsPane(){
     return pane;   
    }
}