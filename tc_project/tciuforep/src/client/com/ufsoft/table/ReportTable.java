package com.ufsoft.table;

import java.awt.Component;
import java.util.Arrays;
import java.util.EventListener;

import nc.ui.pub.beans.util.NCOptionPane;

import com.ufida.zior.event.EventManager;
import com.ufida.zior.exception.MessageException;

import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

/**
 * <p>
 * Title: ���ؼ� ����UFOTable
 * 
 * @author wangyga
 * @created at 2009-2-24,����01:10:35
 * 
 */
public class ReportTable extends UFOTable {

	private static final long serialVersionUID = -9024099000772954528L;

	/**
	 * �������Ĺ��캯��
	 * 
	 * @param horPolicy
	 *            int ˮƽ�������Ĳ��ԣ��μ���TableConstants
	 * @param verPolicy
	 *            int ��ֱ�������Ĳ��ԣ��μ���TableConstants
	 * @param pane
	 */

	protected ReportTable(int horPolicy, int verPolicy, CellsPane pane) {
		super(horPolicy, verPolicy, pane);
	}

	/**
	 * ������ģ�ʹ�����
	 * 
	 * @see createTableImpl()
	 */
	public static ReportTable createReportTable(boolean showRowHeader,
			boolean showColHeader) {
		CellsPane pane = new CellsPane(null, showRowHeader, showColHeader);
		return new ReportTable(TableConstants.HORIZONTAL_SCROLLBAR_ALWAYS,
				TableConstants.VERTICAL_SCROLLBAR_ALWAYS, pane);
	}

	/**
	 * �����¿�ܵ�UserActionListner���ɷ�
	 */
	@Override
	public void fireEvent(UserUIEvent event) {

		getEventManager().dispatch(event);

	}

	@Override
	public boolean checkEvent(UserUIEvent e) {
		boolean result = false;
		try {
			String strResult = null;
			if (getCells().getEventManager() == null) {
				return true;
			}
			EventListener[] eventListeners = getEventManager().getListeners(e);
			Examination[] listeners = Arrays.asList(eventListeners).toArray(
					new Examination[0]);
			if (listeners == null || listeners.length == 0) {
				return true;
			}
			for (Examination listener : listeners) {
				String strInfo = listener.isSupport(Examination.USER_DEFINE, e);
				if (strInfo != null) {
					if (strResult == null) {
						strResult = strInfo;
					} else {
						strResult += strInfo;
					}
				}
			}
			if (strResult != null) {
				String title = MultiLang.getString("report00407");
				int value = NCOptionPane.showConfirmDialog(null, strResult,
						title, NCOptionPane.YES_NO_OPTION);
				result = value == NCOptionPane.YES_OPTION;
			} else {
				result = true;
			}
		} catch (MessageException e1) {
			Component parent = null;
			if (e.getSource() instanceof Component) {
				parent = (Component) e.getSource();
			}
			UfoPublic.sendMessage(e1, parent);
			result = false;
		}

		return result;
	}

	private EventManager getEventManager() {
		EventManager eventManager = getCells().getEventManager();
		if (eventManager == null) {
			throw new IllegalArgumentException("EventManager is null");
		}
		return eventManager;
	}
}
