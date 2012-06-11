/*
 * DynAreaMngDlg.java
 * �������� 2004-11-26
 * Created by CaiJie
 */
package com.ufsoft.iufo.fmtplugin.dynarea;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;


/**
 * ��̬�������Ի���
 * @author caijie 
 * @since 3.1
 */
public class DynAreaMngDlg extends UfoDialog implements ActionListener, ListSelectionListener {
	private JButton m_btnAdd = null;
	private JButton m_btnCancel = null;
	private JButton m_btnDel = null;
	private JButton m_btnOK = null;
	private JButton m_btnUpdate = null;
	private JLabel ivjJLabel1 = null;
	private JLabel ivjJLabelDir = null;
	private JPanel m_UfoDialogContentPane = null;
	private JList m_DynAreaList = null;

	private DefaultListModel m_DynAreaListModel = null;
	//����Ķ�̬����ʵ���
//	private Hashtable m_dynAreaHashTable=null;
	//��ǰ��̬����ķ���
//	private int m_nDynDirection = -1;
	
//	private DynAreaModel m_dynAreaModel = null;

	//��ǰѡ�еĶ�̬����
	private DynAreaCell m_selectedDynCell=null;
    private DynAreaCell[] m_dynCells;

	/**
	 * ��Ӷ�̬����
	 */
	public final static int ID_ADD = 3;//��Ӷ�̬����
	/**
	 * �޸Ķ�̬����
	 */
	public final static int ID_UPDATE = 4;//�޸Ķ�̬����
	/**
	 * ɾ����̬����
	 */
	public final static int ID_DELETE = 5;//ɾ����̬����
/**
 * DynAreaMngDlg ������ע�⡣
 * @param parent java.awt.Container
 */
public DynAreaMngDlg(Container parent, DynAreaCell[] cells) {
	super(parent);   
	m_dynCells = cells;	
	initialize();
}
/**
 * Invoked when an action occurs.
 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getJBtnAdd()) {
            setResult(ID_ADD);
            close();

        } else if (e.getSource() == getJBtnUpdate()) {
            int selected = getJListDynArea().getSelectedIndex();        
            if(selected != -1){
                DynAreaCell selDynAreaCell =  (DynAreaCell) m_DynAreaListModel.get(selected);
                setDynAreaCell(selDynAreaCell);
                
                setResult(ID_UPDATE);
                close(); 
            }            
        } else if (e.getSource() == getJBtnCancel()) {
            setResult(ID_CANCEL);
            close();
        } else if (e.getSource() == getJBtnOK()) {
            setResult(ID_OK);
            close();
        } else if (e.getSource() == getJBtnDel()) {
            int selected = getJListDynArea().getSelectedIndex();  
            if(selected != -1){
                DynAreaCell selDynAreaCell =  (DynAreaCell) m_DynAreaListModel.get(selected); 
                setDynAreaCell(selDynAreaCell);
                setResult(ID_DELETE);
                close();
            }            
        }
}
    
//    /**
//     * ��������λ���ַ�����ø�����Ķ�̬�������
//     * Ҫ������λ���ַ�������Ͷ����λ���ַ���������ȫһ��
//     * caijie  2004-12-6
//     * @param pos
//     * @return
//     */
//    private DynAreaCell getDynAreaCellByPos(String pos){        
//        if(pos == null) return null;        
//        Enumeration e=m_dynAreaHashTable.elements();
//		while(e.hasMoreElements())
//		{
//		    DynAreaVO dynVO=(DynAreaVO)e.nextElement();
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
    return "TM_Format_DynAreaSet";
}
/**
 * �˴����뷽��������
 * @return DynAreaVO
 */
public DynAreaCell getCurrentSelectedDynAreaCell() {
	return m_selectedDynCell;
}

/**
 * �õ���̬����ķ�������Ѿ����ڶ�̬�������еĶ�̬����ֻ��һ����������ȡ��һ�������û�У�������Чֵ��     
 * @return int DynAreaVO.DIRECTION_UNDEFINED��ʾû�����á�
 * @see DynAreaVO
 */
int getDynAreaDirection() {    
    if(m_dynCells.length > 0){
        return m_dynCells[0].getDynAreaVO().getDirection();
    }
	return DynAreaVO.DIRECTION_UNDEFINED;
}
/**
 * ���� JBtnAdd ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnAdd() {
	if (m_btnAdd == null) {
		try {
			m_btnAdd = new nc.ui.pub.beans.UIButton();
			m_btnAdd.setName("JBtnAdd");
			m_btnAdd.setText(StringResource.getStringResource("miufo1000950"));  //"���"
			m_btnAdd.setBounds(199, 53, 75, 22);
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
private javax.swing.JButton getJBtnCancel() {
	if (m_btnCancel == null) {
		try {
			m_btnCancel = new nc.ui.pub.beans.UIButton();
			m_btnCancel.setName("JBtnCancel");
			m_btnCancel.setText(StringResource.getStringResource("miufopublic247"));  //"ȡ��"
			m_btnCancel.setBounds(184, 307, 75, 22);
			// user code begin {1}
			m_btnCancel.addActionListener(this);
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
			m_btnDel.setBounds(199, 180, 75, 22);
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
private javax.swing.JButton getJBtnOK() {
	if (m_btnOK == null) {
		try {
			m_btnOK = new nc.ui.pub.beans.UIButton();
			m_btnOK.setName("JBtnOK");
			m_btnOK.setText(StringResource.getStringResource("miufopublic246"));  //"ȷ��"
			m_btnOK.setBounds(52, 307, 75, 22);
			// user code begin {1}
			m_btnOK.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_btnOK;
}
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
			m_btnUpdate.setBounds(199, 115, 75, 22);
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
/**
 * ���� JLabel1 ����ֵ��
 * @return javax.swing.JLabel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new UILabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabel1.setText(StringResource.getStringResource("miufopublic282"));  //"����"
			ivjJLabel1.setBounds(18, 28, 42, 16);
			ivjJLabel1.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel1;
}
/**
 * ���� JLabelDir ����ֵ��
 * @return javax.swing.JLabel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JLabel getJLabelDir() {
	if (ivjJLabelDir == null) {
		try {
			ivjJLabelDir = new UILabel();
			ivjJLabelDir.setName("JLabelDir");
			ivjJLabelDir.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabelDir.setText("");
			ivjJLabelDir.setBounds(66, 28, 102, 16);
			ivjJLabelDir.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabelDir;
}
/**
 * ���� JList1 ����ֵ��
 * @return javax.swing.JList
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JList getJListDynArea() {
	if (m_DynAreaList == null) {
		try {
			m_DynAreaList = new UIList();
			m_DynAreaList.setName("JListDynArea");
			m_DynAreaList.setBounds(18, 53, 149, 229);
			m_DynAreaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// user code begin {1}
			m_DynAreaList.addListSelectionListener(this);
			m_DynAreaList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_DynAreaList;
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
			m_UfoDialogContentPane.setLayout(null);
			getUfoDialogContentPane().add(getJLabel1(), getJLabel1().getName());
			UIScrollPane scrollPane = new UIScrollPane();
			scrollPane.setBounds(18, 53, 149, 229);
			scrollPane.setViewportView(getJListDynArea());
//			getUfoDialogContentPane().add(getJListDynArea(), getJListDynArea().getName());
			getUfoDialogContentPane().add(scrollPane,  scrollPane.getName());
			getUfoDialogContentPane().add(getJLabelDir(), getJLabelDir().getName());
			getUfoDialogContentPane().add(getJBtnAdd(), getJBtnAdd().getName());
			getUfoDialogContentPane().add(getJBtnUpdate(), getJBtnUpdate().getName());
			getUfoDialogContentPane().add(getJBtnDel(), getJBtnDel().getName());
			getUfoDialogContentPane().add(getJBtnOK(), getJBtnOK().getName());
			getUfoDialogContentPane().add(getJBtnCancel(), getJBtnCancel().getName());
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
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
    try {
        // user code begin {1}
        // user code end
        setName("DynAreaMngDlg");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(292, 378);
        setTitle(StringResource.getStringResource("miufo1000952"));  //"��̬�������"
        setContentPane(getUfoDialogContentPane());
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    m_DynAreaListModel = new DefaultListModel();
    getJListDynArea().setModel(m_DynAreaListModel);
    setLocationRelativeTo(this);
    initDynAreaList();
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		DynAreaMngDlg aDynAreaMngDlg = new DynAreaMngDlg(null, null);
//		aDynAreaMngDlg.setModal(true);
//		aDynAreaMngDlg.addWindowListener(new java.awt.event.WindowAdapter() {
//			public void windowClosing(java.awt.event.WindowEvent e) {
//				System.exit(0);
//			};
//		});
		aDynAreaMngDlg.show();
//		java.awt.Insets insets = aDynAreaMngDlg.getInsets();
//		aDynAreaMngDlg.setSize(aDynAreaMngDlg.getWidth() + insets.left + insets.right, aDynAreaMngDlg.getHeight() + insets.top + insets.bottom);
//		aDynAreaMngDlg.setVisible(true);
	} catch (Throwable exception) {
		System.err.println(StringResource.getStringResource("miufo1000953"));  //"com.ufsoft.iuforeport.reporttool.pub.UfoDialog �� main() �з����쳣"
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-8-27 9:53:04)
 * @param newDynAreaMap 
 */
private void initDynAreaList() {    
	if (m_dynCells.length != 0){	    
	    m_DynAreaListModel.removeAllElements();
	    for(int i=0;i<m_dynCells.length;i++){            
            m_DynAreaListModel.addElement(m_dynCells[i]);
            DynAreaVO dynAreaVO = m_dynCells[i].getDynAreaVO();
            if (dynAreaVO.getDirection() == DynAreaVO.DIRECTION_COL){
                getJLabelDir().setText(StringResource.getStringResource("miufopublic366"));  //"����"
            }else if (dynAreaVO.getDirection() == DynAreaVO.DIRECTION_ROW){
                getJLabelDir().setText(StringResource.getStringResource("miufopublic367"));  //"����"
            }
        }
		getJListDynArea().setSelectedIndex(0);
	}
}
/**
 * �˴����뷽��������
 * @param dynAreaCell DynAreaVO
 */
public void setDynAreaCell(DynAreaCell dynAreaCell) {
	m_selectedDynCell = dynAreaCell;
}
/**
 *  �˴����뷽��������
 * @param newDynDirection int
 */
//public void setDynDirection(int newDynDirection) {
//    m_nDynDirection = newDynDirection;
//    if (m_nDynDirection == DynAreaVO.DIRECTION_COL) {
//        getJLabelDir().setText(StringResource.getStringResource("miufo1000956"));  //"����չ"
//    } else if (m_nDynDirection == DynAreaVO.DIRECTION_ROW) {
//        getJLabelDir().setText(StringResource.getStringResource("miufo1000957"));  //"����չ"
//    }
//}
 /**
   * Called whenever the value of the selection changes.
   * @param e the event that characterizes the change.
   */
public void valueChanged(javax.swing.event.ListSelectionEvent e) {
	if (e.getSource()==getJListDynArea()){
		repaint();
		int[] selected = getJListDynArea().getSelectedIndices();
		if (selected.length!=0){
			getJBtnDel().setEnabled(true);
			getJBtnUpdate().setEnabled(true);
		}else{
			getJBtnDel().setEnabled(false);
		}
	}
}
}
