package com.ufsoft.iufo.inputplugin.ref;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.re.IDName;
import com.ufsoft.table.re.IRefComp;

/**
 * 参照对话框
 * @created by wangyga at 2008-12-26,上午09:54:52
 *
 */

public class ComRefDlg extends UfoDialog implements IComRefDlg{

	private IRefComp m_refComp = null;
	
	private Object selectedValue = null;

	private static final long serialVersionUID = 1L;

	public ComRefDlg(Container parent) {
		super(parent);
		initialize();
	}

	public void setRefComp(IRefComp refComp) {
		m_refComp = refComp;
	}

	private void initialize() {
		
	}
    
	public void showModel() {
		setTitle(m_refComp.getTitleValue());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add((JComponent) m_refComp, BorderLayout.CENTER);
		Dimension refCompSize = ((JComponent) m_refComp).getPreferredSize();
		setSize(refCompSize);//(300,500);
		addCloseListener();
		setVisible(true);		
	}

	private void addCloseListener() {
		((JComponent) m_refComp).addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Object source = e.getSource();
				if (source instanceof JButton) {
					if (e.getClickCount() == 1) {
						closeRef();
					}
				} else {
					if (e.getClickCount() >= 2) {
						closeRef();
					}
				}

			}
		});
	}

	private void closeRef() {
		Object obj = m_refComp.getSelectValue();
		if (obj != null && !"".equals(obj.toString().trim())) {
			if (obj instanceof IDName) {
				if (((IDName) obj).getID() == null
						|| !((IDName) obj).isRefNode()) {
					return;
				}
				selectedValue = ((IDName)obj).getID();
			} else {
				selectedValue = obj.toString(); 
			}
		}
		setVisible(false);
	}
	
	public Object getSelectedValue(){
		return selectedValue;
	}

}
