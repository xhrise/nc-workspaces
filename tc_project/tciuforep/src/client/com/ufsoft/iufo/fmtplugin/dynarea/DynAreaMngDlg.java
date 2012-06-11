/*
 * DynAreaMngDlg.java
 * 创建日期 2004-11-26
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
 * 动态区域管理对话框。
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
	//报表的动态区域实体表
//	private Hashtable m_dynAreaHashTable=null;
	//当前动态区域的方向
//	private int m_nDynDirection = -1;
	
//	private DynAreaModel m_dynAreaModel = null;

	//当前选中的动态区域
	private DynAreaCell m_selectedDynCell=null;
    private DynAreaCell[] m_dynCells;

	/**
	 * 添加动态区域
	 */
	public final static int ID_ADD = 3;//添加动态区域
	/**
	 * 修改动态区域
	 */
	public final static int ID_UPDATE = 4;//修改动态区域
	/**
	 * 删除动态区域
	 */
	public final static int ID_DELETE = 5;//删除动态区域
/**
 * DynAreaMngDlg 构造子注解。
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
//     * 根据区域位置字符串获得该区域的动态区域对象
//     * 要求区域位置字符串必须和对象的位置字符串必须完全一致
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
 * 此处插入方法描述。
 * @return DynAreaVO
 */
public DynAreaCell getCurrentSelectedDynAreaCell() {
	return m_selectedDynCell;
}

/**
 * 得到动态区域的方向，如果已经存在动态区域，所有的动态区域只有一个方向，所以取第一个；如果没有，返回无效值。     
 * @return int DynAreaVO.DIRECTION_UNDEFINED表示没有设置。
 * @see DynAreaVO
 */
int getDynAreaDirection() {    
    if(m_dynCells.length > 0){
        return m_dynCells[0].getDynAreaVO().getDirection();
    }
	return DynAreaVO.DIRECTION_UNDEFINED;
}
/**
 * 返回 JBtnAdd 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBtnAdd() {
	if (m_btnAdd == null) {
		try {
			m_btnAdd = new nc.ui.pub.beans.UIButton();
			m_btnAdd.setName("JBtnAdd");
			m_btnAdd.setText(StringResource.getStringResource("miufo1000950"));  //"添加"
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
 * 返回 JBtnCancel 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBtnCancel() {
	if (m_btnCancel == null) {
		try {
			m_btnCancel = new nc.ui.pub.beans.UIButton();
			m_btnCancel.setName("JBtnCancel");
			m_btnCancel.setText(StringResource.getStringResource("miufopublic247"));  //"取消"
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
 * 返回 JBtnDel 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBtnDel() {
	if (m_btnDel == null) {
		try {
			m_btnDel = new nc.ui.pub.beans.UIButton();
			m_btnDel.setName("JBtnDel");
			m_btnDel.setText(StringResource.getStringResource("miufopublic243"));  //"删除"
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
 * 返回 JBtnOK 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBtnOK() {
	if (m_btnOK == null) {
		try {
			m_btnOK = new nc.ui.pub.beans.UIButton();
			m_btnOK.setName("JBtnOK");
			m_btnOK.setText(StringResource.getStringResource("miufopublic246"));  //"确定"
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
 * 返回 JBtnUpdate 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBtnUpdate() {
	if (m_btnUpdate == null) {
		try {
			m_btnUpdate = new nc.ui.pub.beans.UIButton();
			m_btnUpdate.setName("JBtnUpdate");
			m_btnUpdate.setText(StringResource.getStringResource("miufopublic244"));  //"修改"
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
 * 返回 JLabel1 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new UILabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabel1.setText(StringResource.getStringResource("miufopublic282"));  //"方向"
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
 * 返回 JLabelDir 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JList1 特性值。
 * @return javax.swing.JList
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 UfoDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	// System.out.println("--------- 未捕捉到的异常 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
    try {
        // user code begin {1}
        // user code end
        setName("DynAreaMngDlg");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(292, 378);
        setTitle(StringResource.getStringResource("miufo1000952"));  //"动态区域管理"
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
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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
		System.err.println(StringResource.getStringResource("miufo1000953"));  //"com.ufsoft.iuforeport.reporttool.pub.UfoDialog 的 main() 中发生异常"
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-27 9:53:04)
 * @param newDynAreaMap 
 */
private void initDynAreaList() {    
	if (m_dynCells.length != 0){	    
	    m_DynAreaListModel.removeAllElements();
	    for(int i=0;i<m_dynCells.length;i++){            
            m_DynAreaListModel.addElement(m_dynCells[i]);
            DynAreaVO dynAreaVO = m_dynCells[i].getDynAreaVO();
            if (dynAreaVO.getDirection() == DynAreaVO.DIRECTION_COL){
                getJLabelDir().setText(StringResource.getStringResource("miufopublic366"));  //"纵向"
            }else if (dynAreaVO.getDirection() == DynAreaVO.DIRECTION_ROW){
                getJLabelDir().setText(StringResource.getStringResource("miufopublic367"));  //"横向"
            }
        }
		getJListDynArea().setSelectedIndex(0);
	}
}
/**
 * 此处插入方法描述。
 * @param dynAreaCell DynAreaVO
 */
public void setDynAreaCell(DynAreaCell dynAreaCell) {
	m_selectedDynCell = dynAreaCell;
}
/**
 *  此处插入方法描述。
 * @param newDynDirection int
 */
//public void setDynDirection(int newDynDirection) {
//    m_nDynDirection = newDynDirection;
//    if (m_nDynDirection == DynAreaVO.DIRECTION_COL) {
//        getJLabelDir().setText(StringResource.getStringResource("miufo1000956"));  //"列扩展"
//    } else if (m_nDynDirection == DynAreaVO.DIRECTION_ROW) {
//        getJLabelDir().setText(StringResource.getStringResource("miufo1000957"));  //"行扩展"
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
