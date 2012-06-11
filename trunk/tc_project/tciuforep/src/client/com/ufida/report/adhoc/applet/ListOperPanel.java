package com.ufida.report.adhoc.applet;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;

import nc.ui.pub.beans.UIPanel;

import com.ufsoft.iufo.resource.StringResource;

public class ListOperPanel extends UIPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6757255879348821372L;
	private JButton btnMoveUp = null;
	private JButton btnMoveDown = null;
	private JButton btnDelete = null;
	private JList m_opList = null;
	/**
	 * This is the default constructor
	 */
	public ListOperPanel(JList opList) {
		super();
		m_opList = opList;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//this.setSize(64, 104);
		Dimension size = new Dimension(75,20);
		getBtnMoveUp().setSize(size);
		getBtnMoveDown().setSize(size);
		getBtnDelete().setSize(size);
		getBtnMoveUp().setPreferredSize(size);
		getBtnMoveDown().setPreferredSize(size);
		getBtnDelete().setPreferredSize(size);
		getBtnMoveUp().setMinimumSize(size);
		getBtnMoveDown().setMinimumSize(size);
		getBtnDelete().setMinimumSize(size);
		
		
		this.add(getBtnMoveUp());
		this.add(getBtnMoveDown());
		this.add(getBtnDelete());
	}

	/**
	 * This method initializes btnMoveUp	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	public JButton getBtnMoveUp() {
		if (btnMoveUp == null) {
			btnMoveUp = new nc.ui.pub.beans.UIButton();
			btnMoveUp.setText(StringResource.getStringResource("mbiadhoc00031"));
			btnMoveUp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DefaultListModel model = (DefaultListModel) m_opList.getModel();
                    int index = m_opList.getSelectedIndex();
                    if(index == -1 || index == 0) return ;
                    Object vo =  model.get(index);
                    model.removeElementAt(index);
                    model.insertElementAt(vo,index-1);                    
                    setSelectedOrder(index - 1);
                }
			    });
		}
		return btnMoveUp;
	}

	private void setSelectedOrder(int selectedIndex){
		if(selectedIndex < 0) selectedIndex = 0;		
		DefaultListModel model = (DefaultListModel) m_opList.getModel();
		if(selectedIndex >=  model.getSize()) selectedIndex = model.getSize()-1;		
        m_opList.setSelectedIndex(selectedIndex);
	}
	/**
	 * This method initializes btnMoveDown	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	public JButton getBtnMoveDown() {
		if (btnMoveDown == null) {
			btnMoveDown = new nc.ui.pub.beans.UIButton();			
			btnMoveDown.setText(StringResource.getStringResource("mbiadhoc00032"));
			btnMoveDown.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DefaultListModel model = (DefaultListModel) m_opList.getModel();
                    int index = m_opList.getSelectedIndex();
                    if(index == -1 || index == model.getSize()-1) return ;
                    Object vo =  model.get(index);
                    model.removeElementAt(index);
                    model.insertElementAt(vo,index+1);
                    setSelectedOrder(index + 1);
                }
			    });
		}
		return btnMoveDown;
	}

	/**
	 * This method initializes btnDelete	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	public JButton getBtnDelete() {
		if (btnDelete == null) {
			btnDelete = new nc.ui.pub.beans.UIButton();			
			btnDelete.setText(StringResource.getStringResource("mbiadhoc00033"));
			btnDelete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DefaultListModel model = (DefaultListModel) m_opList.getModel();
                    int index = m_opList.getSelectedIndex();
                    if(index == -1) return ;                   
                    try {
						model.removeElementAt(index);
						setSelectedOrder(index);
					} catch (Exception e1) {						
					}
                }
			    });
		}
		return btnDelete;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
