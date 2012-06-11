/*
 * AreaMngDlg.java
 * �������� 2004-11-26
 * Created by CaiJie
 */
package com.ufsoft.table.exarea;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaCell;
import com.ufsoft.report.util.MultiLang;


/**
 * ����Ի��� 
 */
class ExAreaMngDlg extends UfoDialog implements ActionListener {
	private JScrollPane scrollPane;
	private static final long serialVersionUID = 2757473813882111752L;
	private JButton m_btnAdd = null;
	private JButton m_btnDel = null;
	private JButton m_btnUpdate = null;
    private JButton m_btnCancel=null;
	private JPanel m_UfoDialogContentPane = null;
	private JList m_AreaList = null;

	private DefaultListModel m_AreaListModel = null;

	//��ǰѡ������
	private  AreaCell m_selectedCell=null;
    private  ExAreaModel exAreaModel;

    
    
	/**
	 * ���
	 */
	final static int ID_ADD = 3;//��� 
	/**
	 * �޸�
	 */
	final static int ID_UPDATE = 4;//�޸� 
	/**
	 * ɾ��
	 */
	final static int ID_DELETE = 5;//ɾ�� 


	
ExAreaMngDlg(Container parent, ExAreaModel exAreaModel,ExAreaCell selCell) {
	super(parent);   
	setResizable(false);
	this.exAreaModel = exAreaModel;	
	m_selectedCell = selCell;
	initialize();

	
}
/**
 * Invoked when an action occurs.
 * @i18n miufo00083=�������ɾ���Ŀ���չ����
 * @i18n miufo00082=������ʾ
 * @i18n miufo00084=ȷ��ɾ���ÿ���չ����
 * @i18n miufo00085=ɾ��ȷ��
 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getJBtnAdd()) {
        	setAreaCell(null);
            setResult(ID_ADD);
            hide();

        } else if (e.getSource() == getJBtnUpdate()) {
            int selected = getJListArea().getSelectedIndex();        
            if(selected != -1){
                 AreaCell selAreaCell =  ( AreaCell) m_AreaListModel.get(selected);
                setAreaCell(selAreaCell);
                
                setResult(ID_UPDATE);
                hide();
            }            
//        } else if (e.getSource() == getJBtnCancel()) {
//            setResult(ID_CANCEL);
//            close();
//        } else if (e.getSource() == getJBtnOK()) {
//            setResult(ID_OK);
//            close();
        } else if (e.getSource() == getJBtnDel()) {
        	
        	int selected = getJListArea().getSelectedIndex();  
            if(selected < 0){
                 UfoPublic.showErrorDialog(this, MultiLang.getString("miufo00083"), MultiLang.getString("miufo00082"));
                 return;
            }
            
        	if(UfoPublic.showConfirmDialog(this, MultiLang.getString("miufo00084"), MultiLang.getString("miufo00085"), JOptionPane.YES_NO_OPTION) != 0){
        		return;
        	}
            
            AreaCell selAreaCell =  (AreaCell) m_AreaListModel.get(selected); 
            setAreaCell(selAreaCell);
            setResult(ID_DELETE);
            hide();
                      
        }else if(e.getSource()==getJBtnClose()){
        	setResult(ID_CANCEL);
            close();
        }
}
    
//    /**
//     * ��������λ���ַ�����ø�����Ķ���
//     * Ҫ������λ���ַ�������Ͷ����λ���ַ���������ȫһ��
//     * caijie  2004-12-6
//     * @param pos
//     * @return
//     */
//    private AreaCell getAreaCellByPos(String pos){        
//        if(pos == null) return null;        
//        Enumeration e=m_AreaHashTable.elements();
//		while(e.hasMoreElements())
//		{
//		    AreaVO dynVO=(AreaVO)e.nextElement();
//		    if(dynVO.getAreaPosition().toString().equals(pos)){
//		        return dynVO;
//		    }		   
//		}
//        return null;
//    }    
/**
 * @return
 * @see com.ufsoft.report.dialog.UfoDialog#getHelpID()
 */
protected String getHelpID(){
    return "TM_Format_AreaSet";
}

public AreaCell getCurrentSelectedAreaCell() {
	return m_selectedCell;
}

private javax.swing.JButton getJBtnAdd() {
	if (m_btnAdd == null) {
		try {
			m_btnAdd = new nc.ui.pub.beans.UIButton();
			m_btnAdd.setName("JBtnAdd");
			m_btnAdd.setText(StringResource.getStringResource("miufo1000950"));  //"���"
			// user code begin {1}
			m_btnAdd.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_btnAdd;
}
/**
 * ���� JBtnCancel ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnClose() {
	if (m_btnCancel == null) {
		try {
			m_btnCancel =createCloseButton();
			m_btnCancel.addActionListener(this);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_btnCancel;
}
/**
 * ���� JBtnDel ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnDel() {
	if (m_btnDel == null) {
		try {
			m_btnDel = new nc.ui.pub.beans.UIButton();
			m_btnDel.setName("JBtnDel");
			m_btnDel.setText(StringResource.getStringResource("miufopublic243"));  //"ɾ��"
			// user code begin {1}
			m_btnDel.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_btnDel;
}
/**
 * ���� JBtnOK ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
//private javax.swing.JButton getJBtnOK() {
//	if (m_btnOK == null) {
//		try {
//			m_btnOK = new nc.ui.pub.beans.UIButton();
//			m_btnOK.setName("JBtnOK");
//			m_btnOK.setText(StringResource.getStringResource("miufopublic246"));  //"ȷ��"
//			m_btnOK.setBounds(52, 307, 75, 22);
//			// user code begin {1}
//			m_btnOK.addActionListener(this);
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return m_btnOK;
//}
/**
 * ���� JBtnUpdate ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnUpdate() {
	if (m_btnUpdate == null) {
		try {
			m_btnUpdate = new nc.ui.pub.beans.UIButton();
			m_btnUpdate.setName("JBtnUpdate");
			m_btnUpdate.setText(StringResource.getStringResource("miufopublic244"));  //"�޸�"
			// user code begin {1}
			m_btnUpdate.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_btnUpdate;
}
  

private javax.swing.JList getJListArea() {
	if (m_AreaList == null) {
		try {
			m_AreaList = new UIList();
			m_AreaList.setName("JListArea");
			m_AreaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// user code begin {1}
//			m_AreaList.addListSelectionListener(this);
//			m_AreaList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_AreaList;
}
/**
 * ���� UfoDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getUfoDialogContentPane() {
	if (m_UfoDialogContentPane == null) {
		try {
			m_UfoDialogContentPane = new UIPanel();
			m_UfoDialogContentPane.setName("UfoDialogContentPane");
			m_UfoDialogContentPane.setLayout(new BorderLayout());
			JPanel topPanel=new UIPanel(new FlowLayout());
			topPanel.add(getJBtnAdd());
			topPanel.add(getJBtnUpdate());
			topPanel.add(getJBtnDel());
			m_UfoDialogContentPane.add(topPanel,BorderLayout.NORTH);
			m_UfoDialogContentPane.add(getScrollPane(),BorderLayout.CENTER);
//			getUfoDialogContentPane().add(getJBtnOK(), getJBtnOK().getName());
			JPanel bottomPanel=new UIPanel(new FlowLayout(FlowLayout.TRAILING));
			bottomPanel.add(getJBtnClose());
			m_UfoDialogContentPane.add(bottomPanel,BorderLayout.SOUTH);
			
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_UfoDialogContentPane;
}
/**
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	// System.out.println("--------- δ��׽�����쳣 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * ��ʼ���ࡣ
 * @i18n miufo00086=����չ������
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
    try { 
        setName("AreaMngDlg");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(312, 313);
        setTitle(MultiLang.getString("miufo00086")); 
        setContentPane(getUfoDialogContentPane());
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    m_AreaListModel = new DefaultListModel();
    getJListArea().setModel(m_AreaListModel);
    setLocationRelativeTo(this);
   
}
 
 

	private void initAreaList() {
		ExAreaCell[] m_areaCells = this.exAreaModel.getExAreaCells();
		if (m_areaCells.length == 0) {
			return;
		}
		m_AreaListModel.removeAllElements();
		for (AreaCell cell : m_areaCells) {
			m_AreaListModel.addElement(cell);
		}
		if (m_selectedCell != null) {
			getJListArea().setSelectedValue(m_selectedCell, true);
		} else {
			getJListArea().setSelectedIndex(0);
		}

	}

@Override
public void show() {
	initAreaList();
	super.show();
}
/**
 * �˴����뷽��������
 * 
 * @param AreaCell
 *            AreaVO
 */
public void setAreaCell(AreaCell AreaCell) {
	m_selectedCell = AreaCell;
}
/**
 *  �˴����뷽��������
 * @param newDynDirection int
 */
//public void setDynDirection(int newDynDirection) {
//    m_nDynDirection = newDynDirection;
//    if (m_nDynDirection == AreaVO.DIRECTION_COL) {
//        getJLabelDir().setText(StringResource.getStringResource("miufo1000956"));  //"����չ"
//    } else if (m_nDynDirection == AreaVO.DIRECTION_ROW) {
//        getJLabelDir().setText(StringResource.getStringResource("miufo1000957"));  //"����չ"
//    }
//}
 /**
   * Called whenever the value of the selection changes.
   * @param e the event that characterizes the change.
   */
public void valueChanged(javax.swing.event.ListSelectionEvent e) {
	if (e.getSource()==getJListArea()){
		repaint();
		int[] selected = getJListArea().getSelectedIndices();
		if (selected.length!=0){
			getJBtnDel().setEnabled(true);
			getJBtnUpdate().setEnabled(true);
		}else{
			getJBtnDel().setEnabled(false);
		}
	}
}
	/**
	 * @return
	 */
	protected JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getJListArea());
		}
		return scrollPane;
	}
}
 