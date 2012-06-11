/*
 * 创建日期 2006-7-17
 */
package com.ufida.report.complexrep.applet;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * @author ljhua
 */
public class SelRepDirDlg extends UfoDialog {



	private javax.swing.JPanel jContentPane = null;

	private JTree jTree = null;
	private JScrollPane jScrollPane = null;
	private JButton btnOK = null;

	private String m_strUserPK=null;
	private IResTreeObject m_dir=null;
	/**
	 * This is the default constructor
	 */
	public SelRepDirDlg(Container container,String strUserPK) {
		super(container);
		m_strUserPK=strUserPK;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(212, 293);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getBtnOK(), null);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */    
	private JTree getJTree() {
		if (jTree == null) {
			jTree = new ImportReportDlg.ResTree(ImportReportDlg.getRootTreeNode(m_strUserPK,false,IAuthorizeTypes.AU_TYPE_MODIFY));
			jTree.addTreeSelectionListener(new TreeSelectionListener(){
				public void valueChanged(TreeSelectionEvent e){
					TreePath path = jTree.getSelectionPath();
					if (path !=null){
						DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
						if(treeNode.isRoot()==false){
							m_dir=(IResTreeObject) treeNode.getUserObject();
							setResult(ID_OK);
							close();
						}
					}
				}
			});
		}
		return jTree;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(12, 7, 178, 206);
			jScrollPane.setViewportView(getJTree());
		}
		return jScrollPane;
	}
	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new nc.ui.pub.beans.UIButton();
			btnOK.setBounds(50, 226, 75, 22);
			btnOK.setText(StringResource.getStringResource("miufopublic253"));
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setResult(UfoDialog.ID_CANCEL);
					close();
				}
			});
		}
		return btnOK;
	}
	public IResTreeObject getSelectedDir(){
		return m_dir;
	}
    }  //  @jve:decl-index=0:visual-constraint="10,10"
