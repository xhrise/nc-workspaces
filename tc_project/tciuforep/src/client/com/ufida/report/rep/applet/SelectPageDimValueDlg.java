/*
 * Created on 2005-7-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.rep.applet;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.bi.integration.dimension.DimMemberVO;

import com.ufida.report.adhoc.model.PageDimField;
import com.ufida.report.free.IMeasQueryCreator;
import com.ufida.report.free.IRptProviderCreator;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.beans.UFOTree;

/**
 * @author caijie
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SelectPageDimValueDlg extends UfoDialog {

	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel jContentPane = null;

	private JScrollPane jScrollPane = null;

	private JButton OKBtn = null;

	private JButton cancelBtn = null;
    
	private boolean isTree;
	private JList allValueList = null;
	
	private JTree allValueTree=null;

	private PageDimField pageDimField = null;

	private IMember[] allMembers = null;
	/**
	 * This is the default constructor
	 */
	public SelectPageDimValueDlg(Container parent, PageDimField pageDimField) {
		super(parent);
		if (pageDimField == null) {
			throw new IllegalArgumentException();
		}
		this.pageDimField = pageDimField;
		String fldAlias = pageDimField.getID();
		if (fldAlias.equals(IMeasQueryCreator.DICUNITCODE)
				|| fldAlias.equals(IMeasQueryCreator.DICUNITNAME)
				|| fldAlias.equals(IMeasQueryCreator.UNITCODE)
				|| fldAlias.equals(IMeasQueryCreator.UNITNAME)
				|| fldAlias.indexOf(IMeasQueryCreator.REFER_CODE_NAME) >= 0
				|| fldAlias.equals(IRptProviderCreator.COLUMN_UNITCODE)
				|| fldAlias.equals(IRptProviderCreator.COLUMN_UNITNAME)
				|| fldAlias.indexOf(IRptProviderCreator.REFALIAS_CODENAME) >= 0) {
			this.isTree = true;
		}
		initialize();
	}

	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @i18n mbirep00019=选择维度值
	 */
	private void initialize() {
		this.setTitle(StringResource.getStringResource("mbirep00019"));
		this.setSize(280, 320);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setBounds(5, 5, 250, 280);
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
			UIPanel btnPanel = new UIPanel();
			btnPanel.setPreferredSize(new Dimension(280, 40));
			btnPanel.setLayout(null);
			btnPanel.add(getOKBtn(), getOKBtn().getName());
			btnPanel.add(getCancelBtn(), getCancelBtn().getName());
			jContentPane.add(btnPanel, BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(5, 5, 240, 260);
			if(isTree){
				jScrollPane.setViewportView(getAllValueTree());
			}else{
				jScrollPane.setViewportView(getAllValueList());
			}
			
		}
		return jScrollPane;
	}

	/**
	 * This method initializes OKBtn
	 * 
	 * @return javax.swing.JButton
	 * @i18n miufo1003314=确定
	 */
	private JButton getOKBtn() {
		if (OKBtn == null) {
			OKBtn = new UIButton();
			OKBtn.setBounds(20, 10, 75, 22);
			OKBtn.setText(StringResource.getStringResource("miufo1003314"));
			OKBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeDlgWithResult(true);
				}
			});
		}
		return OKBtn;
	}

	/**
	 * This method initializes cancelBtn
	 * 
	 * @return javax.swing.JButton
	 * @i18n miufo1003315=取消
	 */
	private JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new UIButton();
			cancelBtn.setBounds(200, 10, 75, 22);
			cancelBtn.setText(StringResource.getStringResource("miufo1003315"));
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeDlgWithResult(false);
				}
			});
		}
		return cancelBtn;
	}

	/**
	 * This method initializes allValueList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getAllValueList() {
		if (allValueList == null) {
			DefaultListModel listModel = new DefaultListModel();
			allValueList = new UIList(listModel);
			allValueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			IMember[] vos = getAllMembers();
			if (vos != null) {
				if(pageDimField.isShowCode()){
					for(int i = 0; i < vos.length; i++) listModel.addElement(vos[i].getMemcode());
				}else{
					for(int i = 0; i < vos.length; i++) listModel.addElement(vos[i].getName());
				}				
			}
			allValueList.addMouseListener(new MouseAdapter(){
	            public void mouseClicked(MouseEvent e) {
	                if(e.getClickCount()>=2){
	                	if (allValueList.getSelectedIndex() == -1) 
	        				return;
	                	closeDlgWithResult(true);
	                }
	            }
	        });
		}
		return allValueList;
	}
	private JTree getAllValueTree() {
		if (allValueTree == null) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(pageDimField.getName());
			DefaultTreeModel treeModel = new DefaultTreeModel(root,false);
			allValueTree = new UFOTree(treeModel);
			IMember[] vos = getAllMembers();
			addTreeChild(root,"",0,vos);
			allValueTree.addMouseListener(new MouseAdapter(){
	            public void mouseClicked(MouseEvent e) {
	                if(e.getClickCount()>=2){
	                	DefaultMutableTreeNode node = (DefaultMutableTreeNode)allValueTree.getSelectionPath().getLastPathComponent();
	                	if(node.getUserObject() instanceof DimMemberVO){
	                		closeDlgWithResult(true);
	                	}
	                	
	                }
	            }
	        });
		}
		return allValueTree;
	}
	
	private void addTreeChild(TreeNode root, String fatherLevel, int beginIndex,
			IMember[] treeNodes) {
		if (root==null||treeNodes == null || beginIndex >= treeNodes.length) {
			return;
		}
		String nodeLevel ="";
		DimMemberVO node=null;
		if(treeNodes[beginIndex] instanceof DimMemberVO){
			node=(DimMemberVO)treeNodes[beginIndex];
			if(node.getLevels()[0]!=null){
				nodeLevel= node.getLevels()[0];
			}
		}
		if(node==null){
			return;
		}
		DefaultMutableTreeNode son = new DefaultMutableTreeNode(node);
		
			if(beginIndex==0){
				if (root instanceof DefaultMutableTreeNode) {
					((DefaultMutableTreeNode) root).add(son);
					 beginIndex++;
					 addTreeChild(son, nodeLevel, beginIndex, treeNodes);
				}
			}else{

				if (nodeLevel.indexOf(fatherLevel)>=0) {
					beginIndex++;
					if (root instanceof DefaultMutableTreeNode) {
						((DefaultMutableTreeNode) root).add(son);
					}
					addTreeChild(son, nodeLevel, beginIndex, treeNodes);
				} else {
					TreeNode father= root.getParent();
					if (father instanceof DefaultMutableTreeNode) {
						Object obj = ((DefaultMutableTreeNode) father).getUserObject();
						if (obj instanceof DimMemberVO) {
							
							if(((DimMemberVO)obj).getLevels()[0]!=null){
								nodeLevel= ((DimMemberVO)obj).getLevels()[0];
							}
							
						}

					}
					addTreeChild(father, nodeLevel, beginIndex, treeNodes);
				}
			}

	}
	public DimMemberVO getSelectValue() {
		DimMemberVO returnObj=null;
		try {
			
			if(isTree){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)(getAllValueTree().getSelectionPath().getLastPathComponent());
				if(node.getUserObject() instanceof DimMemberVO){
					returnObj=(DimMemberVO)node.getUserObject();
				}
			}else{
				if (getAllValueList().getSelectedIndex() > -1) {
                   returnObj=(DimMemberVO) getAllMembers()[getAllValueList().getSelectedIndex()];
			      }
			}
		} catch (Exception e) {		
			AppDebug.debug(e);
		}
		return returnObj;
	}

	public static void main(String[] args) {

	}

    /**
     * 如果是tree则不用排序，否则会根据显示排序
     * @return
     */
	private IMember[] getAllMembers() {
		if(allMembers == null){
			allMembers = pageDimField.getAllValues();
			if(allMembers == null) return null;
			if(!isTree){
				TreeMap set = new TreeMap();//排序
				if(pageDimField.isShowCode()){
					for(int i = 0; i < allMembers.length; i++) set.put(allMembers[i].getMemcode(), allMembers[i]);
				}else{
					for(int i = 0; i < allMembers.length; i++) set.put(allMembers[i].getName(), allMembers[i]);
				}
				allMembers = (IMember[]) set.values().toArray(new IMember[0]);
			}
			
		}
		return allMembers;
	}
	private void closeDlgWithResult(boolean isOK){
		if(isOK)
			setResult(UfoDialog.ID_OK);
		else
			setResult(UfoDialog.ID_CANCEL);
		close();
	}
} // @jve:decl-index=0:visual-constraint="10,10"
 