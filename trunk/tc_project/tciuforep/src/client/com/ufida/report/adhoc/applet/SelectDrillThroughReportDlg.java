package com.ufida.report.adhoc.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;

import com.ufida.report.complexrep.applet.ImportReportDlg;
import com.ufida.report.complexrep.applet.ImportReportDlg.ResTree;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

public class SelectDrillThroughReportDlg extends UfoDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JButton btnOK = null;
	private JButton btnCancel = null;
	private JScrollPane jScrollPane = null;
	private ResTree jTree = null;
	
	private String m_userPK = null;
	/**
	 * This is the default constructor
	 */
	public SelectDrillThroughReportDlg(Container parent, String userPK) {
		super(parent);
		m_userPK = userPK;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getBtnOK(), null);
			jPanel.add(getBtnCancel(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1003314=确定
	 */    
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setText(StringResource.getStringResource("miufo1003314"));
			btnOK.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					SelectDrillThroughReportDlg.this.setResult(UfoDialog.ID_OK);
					SelectDrillThroughReportDlg.this.setVisible(false);
					SelectDrillThroughReportDlg.this.close();
				}});
		}
		return btnOK;
	}

	/**
	 * This method initializes btnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 * @i18n miufo1003315=取消
	 */    
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText(StringResource.getStringResource("miufo1003315"));
			btnCancel.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {	
					SelectDrillThroughReportDlg.this.setResult(UfoDialog.ID_CANCEL);
					SelectDrillThroughReportDlg.this.setVisible(false);
					SelectDrillThroughReportDlg.this.close();
				}});
		}
		return btnCancel;
	}


	
	public String  getImportReportID() {
		TreePath path = getJTree().getSelectionPath();
		if (path == null) {
			return null;
		}	
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		if (treeNode.isLeaf()) {
			IResTreeObject resTree=(IResTreeObject) treeNode.getUserObject();
			if(resTree!=null && resTree.getSrcVO()!=null && resTree.getSrcVO() instanceof ReportVO){
				return ((ReportVO) resTree.getSrcVO()).getPrimaryKey();
			}
		}
		return null;

	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTree());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */    
	private ResTree getJTree() {
		if (jTree == null) {			
			jTree = new ResTree(ImportReportDlg.getRootTreeNode(m_userPK,true,IAuthorizeTypes.AU_TYPE_VIEW));
			((DefaultTreeModel) jTree.getModel()).setAsksAllowsChildren(true);
			jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}
		return jTree;
	}

}
