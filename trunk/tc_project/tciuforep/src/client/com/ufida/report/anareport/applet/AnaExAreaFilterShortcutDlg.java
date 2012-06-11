package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.bi.integration.dimension.DimMemberVO;

import com.ufida.report.anareport.model.AnaReportCondition;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.checkbox.CheckBoxList;
import com.ufsoft.report.checkbox.CheckboxTree;
import com.ufsoft.report.checkbox.TreeCheckingModel;
import com.ufsoft.report.dialog.UfoDialog;

public class AnaExAreaFilterShortcutDlg extends UfoDialog {
	private JPanel jCmdPanel = null;
	private CheckboxTree jTree;
	private CheckBoxList jList;
	
	private AnaReportCondition anaField=null;
	private ArrayList<String> oldSelecteds=null;

	public AnaExAreaFilterShortcutDlg(Container parent,
			AnaReportCondition anaField,ArrayList<String> oldSelecteds) {
		super(parent, StringResource.getStringResource("miufo00429"), true);
		this.anaField=anaField;
		this.oldSelecteds=oldSelecteds;
		getContentPane().setLayout(new BorderLayout());
		anaField.getAllValues();
		if(anaField.isTree()){
			getContentPane().add(new UIScrollPane(getCheckboxTree()),BorderLayout.CENTER);
		}else{
			getContentPane().add(new UIScrollPane(getCheckboxList()),BorderLayout.CENTER);
		}
		
		getContentPane().add(getCmdPanel(), BorderLayout.SOUTH);
		this.setSize(300, 500);
		
	}

	private CheckboxTree getCheckboxTree(){
		if(jTree==null){
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(anaField.getName());
			DefaultTreeModel treeModel = new DefaultTreeModel(root,false);
			jTree=new CheckboxTree(treeModel);
			jTree.setRootIsNode(false);
			jTree.getCheckingModel().setCheckingMode(TreeCheckingModel.CheckingMode.SIMPLE);
			IMember[] vos =anaField.getAllValues();
			addTreeChild(root,"",0,vos);
			if(this.oldSelecteds!=null&&oldSelecteds.size()>0){
				jTree.expandAll();
			}
		}
		return jTree;
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
					if(this.oldSelecteds!=null&&oldSelecteds.indexOf(node.getMemname())>=0){
						if(son!=null){
							TreePath path=new TreePath(((DefaultTreeModel)jTree.getModel()).getPathToRoot(son));						       
							jTree.addCheckingPath(path);
							
						}
					}
					 beginIndex++;
					 addTreeChild(son, nodeLevel, beginIndex, treeNodes);
				}
			}else{

				if (nodeLevel.indexOf(fatherLevel)>=0) {
					beginIndex++;
					if (root instanceof DefaultMutableTreeNode) {
						((DefaultMutableTreeNode) root).add(son);
						
						if(this.oldSelecteds!=null&&oldSelecteds.indexOf(node.getMemname())>=0){
							if(son!=null){
								TreePath path=new TreePath(((DefaultTreeModel)jTree.getModel()).getPathToRoot(son));						       
								jTree.addCheckingPath(path);
								
							}
						}
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
	private CheckBoxList getCheckboxList(){
		if(jList==null){
			DefaultListModel listModel = new DefaultListModel();
			jList=new CheckBoxList(listModel);
//			jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			
			IMember[] vos =anaField.getAllValues();
			if (vos != null) {
				if(anaField.isShowCode()){
					for(int i = 0; i < vos.length; i++) {
						listModel.addElement(vos[i].getMemcode());
						if(this.oldSelecteds!=null&&oldSelecteds.indexOf(vos[i].getMemcode())>=0){
							jList.addSelectionInterval(i, i);
						}
					}
				}else{
					for(int i = 0; i < vos.length; i++) {
						listModel.addElement(vos[i].getName());
						if(this.oldSelecteds!=null&&oldSelecteds.indexOf(vos[i].getName())>=0){
							jList.addSelectionInterval(i, i);
						}
					}
				}				
			}

		}
		return jList;
	}
	
	private JPanel getCmdPanel() {
		if (jCmdPanel == null) {
			jCmdPanel = new UIPanel(new FlowLayout(FlowLayout.TRAILING));
			jCmdPanel.add(createOkButton());
			jCmdPanel.add(createCancleButton());
		}
		return jCmdPanel;
	}

	public ArrayList<String> getSelectValues(){
		ArrayList<String> values=new ArrayList<String>();
		if(jTree!=null){
			DefaultMutableTreeNode node=null;
			DimMemberVO value=null;
			TreePath[] treeNodes=jTree.getCheckingPaths();
			for (int i = 0; i < treeNodes.length; i++) {
				node = (DefaultMutableTreeNode) treeNodes[i]
						.getLastPathComponent();
				if (node.getUserObject() instanceof DimMemberVO) {
					value = (DimMemberVO) node.getUserObject();
					values.add(value.getMemname());
				}
			}
		}else{
			Object[] listNodes=jList.getSelectedValues();
			for(int i=0;i<listNodes.length;i++){
				values.add((String)listNodes[i]);
			}
		}
		return values;
	}
}
