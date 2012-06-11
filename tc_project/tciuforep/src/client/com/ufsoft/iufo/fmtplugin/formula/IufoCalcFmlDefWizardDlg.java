package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.RootPaneContainer;

import com.ufida.dataset.IContext;
import com.ufsoft.table.CellsModel;

/**
 * ֮���Լ̳�����Ϊ��Ԥ��ҪCalcFmlDefWizardDlg����CalcFmlDefWizardDlg ������ô���ʱһֱ��ѭ���ɷ��¼�
 * @author wangyga
 * @created at 2009-1-12,����01:06:02
 *
 */
public class IufoCalcFmlDefWizardDlg extends CalcFmlDefWizardDlg {
	private static final long serialVersionUID = 3637689752417890751L;

	public IufoCalcFmlDefWizardDlg(Container owner,CellsModel cellsMode,IContext context) { 
		super(owner,cellsMode,context);
	}

	private void myDispatchEvent(AWTEvent event) {
		if (event instanceof ActiveEvent) {
			((ActiveEvent) event).dispatch();
		} else if (event.getSource() instanceof Component) {
			((Component) event.getSource()).dispatchEvent(event);
		} else if (event.getSource() instanceof MenuComponent) {
			((MenuComponent) event.getSource()).dispatchEvent(event);
		}
	}

	/**
	 * ʵ��������ա�
	 * �������ڣ�(2001-1-4 15:01:43)
	 */
	public void show() {
		super.show();
		EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();

		while (true) {
			try {
				AWTEvent evt = eq.getNextEvent();
				if (evt.getSource() == this
						&& evt.getID() == WindowEvent.WINDOW_CLOSING) {
					myDispatchEvent(evt);
					break;
				} else if (evt.getSource() instanceof RootPaneContainer
						&& evt.getID() == WindowEvent.WINDOW_CLOSING) {
					Toolkit.getDefaultToolkit().beep();
				} else {
					try {
						myDispatchEvent(evt);
					} catch (NullPointerException e) {
						//add by zzl 2005.8.17 ����������Ⲷ��,��Ϊ�˽����ʽ�������������ַ�ʱ���ֵĴ���.
					}
				}
			} catch (InterruptedException ie) {
			}
		}
	}
}
