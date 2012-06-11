/*
 * 创建日期 2006-5-17
 */
package com.ufida.report.multidimension.calc;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;

import com.ufida.report.rep.model.BIFuncInfo;
import com.ufida.report.rep.model.ICalcElements;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
/**
 * @author ljhua
 */
public class DefFormulaPane extends nc.ui.pub.beans.UIPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JScrollPane jScrollPane = null;
	private JTextArea jTextArea = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JTree functionTree = null;
	private JLabel jLabel3 = null;
	private JScrollPane jScrollPane1 = null;
	private JScrollPane jScrollPane2 = null;
	private JScrollPane jScrollPane3 = null;
	private JLabel lbFuncNote = null;
	
	private ICalcElements m_elements = null;
	
	private JList jList = null;
	private JList listMember = null;



	private class FuncTree extends nc.ui.pub.beans.UITree {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FuncTree() {
			init();
		}

		private void init() {
			this.setRootVisible(false);
			
			this.getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);

			
			this.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent e) {
					
					 if(e.getClickCount()>=1){
						DefaultMutableTreeNode node=getSelectedChildNode();
						if(node==null)
							return;
						if((node.getUserObject() instanceof BIFuncInfo )==false)
							return;
						
						String strNote=((BIFuncInfo)node.getUserObject()).getFuncDesc();
						getNoteLable().setText(strNote);
						
						if ( (e.getClickCount() >= 2)) {
							BIFuncInfo func=(BIFuncInfo)node.getUserObject();
							StringBuffer strBuf=new StringBuffer();
							strBuf.append(func.getFuncName());
							strBuf.append("(");

							for (int i=0,size=func.getParamLen()-1;i<size;i++){
								strBuf.append(" ");
								strBuf.append(",");
							}
							if(func.getParamLen()>0)
								strBuf.append(" ");

							strBuf.append(")");
		
							addString(strBuf.toString());
						}
					}

				}
				private void addString(String str) {
					if (str != null) {
						int insertPos = getJTextArea().getCaretPosition();
						try {
							getJTextArea().insert(str, insertPos);
						} catch (Exception e1) {
							getJTextArea().append(str);
						}
						getJTextArea().setCaretPosition(insertPos + str.length()-1);
					}
				}

				public void mouseEntered(MouseEvent e) {

				}

				public void mouseExited(MouseEvent e) {

				}

				public void mousePressed(MouseEvent e) {

				}

				public void mouseReleased(MouseEvent e) {

				}
			});
		}

		private DefaultMutableTreeNode getSelectedChildNode(){
			DefaultMutableTreeNode node=null;
			Object obj =getSelectionModel().getSelectionPath()==null?null: getSelectionModel().getSelectionPath().getLastPathComponent();
			if ((obj != null) && (obj instanceof DefaultMutableTreeNode)) {
				node = (DefaultMutableTreeNode) obj;
				if ((node.getChildCount() != 0) || (node.isRoot())) {
					return null;
				}
			}
			return node;
		}
		

	
	}
	

	

	private class ListMouseListener implements MouseListener{
		public ListMouseListener(){}
		public void mouseClicked(MouseEvent e) {
			if((e.getSource() instanceof JList)==false)
				return;
			if ((e != null) && (e.getClickCount() >= 2)) {
				Object obj = ((JList) e.getSource()).getSelectedValue();
				if(obj ==null)
					return;
				String str=null;
				if(obj instanceof String[]){
					String[] strTemps=(String[])obj;
					if(strTemps.length>=2)
						str=strTemps[1];
				}
				else if(obj instanceof String)
					str=(String) obj;
				
				if (str != null) {
					addString(str);
				}
			}

		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}
	}
	private void addString(String str) {
		if (str != null) {
			int insertPos = getJTextArea().getCaretPosition();
			try {
				getJTextArea().insert(str, insertPos);
			} catch (Exception e1) {
				getJTextArea().append(str);
			}
			getJTextArea().setCaretPosition(insertPos + str.length());
		}
	}
	
	/**
	 * This is the default constructor
	 */
	public DefFormulaPane() {
		super();
		initialize();
	}
	private JLabel getNoteLable(){
		if(lbFuncNote==null){
		lbFuncNote = new nc.ui.pub.beans.UILabel();
		lbFuncNote.setBounds(10, 261, 405, 17);
		lbFuncNote.setText("");
		}
		return lbFuncNote;
	}
	private JLabel getLeftLable() {
		if (jLabel1 == null) {
			jLabel1 = new nc.ui.pub.beans.UILabel();
			jLabel1.setBounds(10, 99, 124, 19);
			jLabel1.setText("");
		}
		return jLabel1;
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		
		jLabel3 = new nc.ui.pub.beans.UILabel();
		jLabel2 = new nc.ui.pub.beans.UILabel();

		jLabel = new nc.ui.pub.beans.UILabel();
		this.setLayout(null);
		this.setSize(424, 285);
		jLabel.setText(StringResource.getStringResource("uibimultical014"));//公式
		jLabel.setBounds(14, 10, 59, 18);
		
	
		jLabel2.setBounds(163, 100, 124, 19);
		jLabel2.setText(StringResource.getStringResource("miufo1000177"));//"函数"
		jLabel3.setBounds(316, 99, 97, 19);
		jLabel3.setText(StringResource.getStringResource("miufo1003111"));//操作符

		this.add(jLabel, null);
		this.add(getJScrollPane(), null);
		this.add(getLeftLable(), null);
		this.add(jLabel2, null);
		this.add(jLabel3, null);
		this.add(getJScrollPane1(), null);
		this.add(getJScrollPane2(), null);
		this.add(getJScrollPane3(), null);
		this.add(getNoteLable(), null);
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setBounds(82, 9, 331, 85);
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new UITextArea();
		}
		return jTextArea;
	}
	/**
	 * This method initializes jTree1	
	 * 	
	 * @return javax.swing.JTree	
	 */    
	private JTree getFunctionTree() {
		if (functionTree == null) {
			functionTree = new FuncTree();
		}
		return functionTree;
	}
	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane();
			jScrollPane1.setBounds(10, 122, 146, 136);
			jScrollPane1.setViewportView(getListMember());
		}
		return jScrollPane1;
	}
	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new UIScrollPane();
			jScrollPane2.setBounds(163, 122, 146, 136);
			jScrollPane2.setViewportView(getFunctionTree());
		}
		return jScrollPane2;
	}
	/**
	 * This method initializes jScrollPane3	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			jScrollPane3 = new UIScrollPane();
			jScrollPane3.setBounds(317, 122, 98, 136);
			jScrollPane3.setViewportView(getOperationList());
		}
		return jScrollPane3;
	}
	
	private DefaultTreeModel createDefaultFunctionTreeModel() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(StringResource.getStringResource("miufo1000177"));//函数
		
		
		DefaultMutableTreeNode node = null;

		UfoSimpleObject[] vos = m_elements.getFuncCatalogs();
		BIFuncInfo[] functions = null;
		if (vos == null || vos.length == 0) {

		} else {
			for (int i = 0; i < vos.length; i++) {
				if (vos[i] == null) {
					continue;
				}
				node = new DefaultMutableTreeNode(vos[i]);
				
				functions = m_elements.getFuncInfos(vos[i].getID());
				if ((functions != null) && (functions.length != 0)) {
					for (int j = 0; j < functions.length; j++) {
						DefaultMutableTreeNode funcNode=new DefaultMutableTreeNode(functions[j]);
						node.add(funcNode);
					}
				}
				root.add(node);
			}
		}
		return new DefaultTreeModel(root);

	}

	private void  initOperList() {

		String[] oprs = m_elements.getOperators();
		if (oprs == null || oprs.length == 0) {
			getOperationList().removeAll();
			return;
		}

		getOperationList().setListData(oprs);
	}
	private void initMemberList(Object[] elements){

		if (elements == null || elements.length == 0) {
			getListMember().setListData(new Object[]{});
			return;
		}

		getListMember().setListData(elements);
	}
	public void initData(ICalcElements elements, String strFormula,String strLabel) {
		
		 m_elements=elements ;
		setFormula(strFormula);
		getLeftLable().setText(strLabel);

		if(m_elements!=null){
			getFunctionTree().setModel(createDefaultFunctionTreeModel());
			expandTree(getFunctionTree(),(DefaultMutableTreeNode) getFunctionTree().getModel().getRoot());
			initOperList();
			initMemberList(elements.getElements());
		}
	}
	
	public String getFormula(){
		return getJTextArea().getText();
	}
	public void setFormula(String strFormula){
		getJTextArea().setText(strFormula);
	}
	public void setMemberNames(String[][] strNames){
		initMemberList(strNames);
		
	}
	
	
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getOperationList() {
		if (jList == null) {
			jList = new UIList();
			jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jList.addMouseListener(new ListMouseListener());
		}
		return jList;
	}
	/**
	 * This method initializes listMember	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getListMember() {
		if (listMember == null) {
			listMember = new UIList();
			listMember.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			listMember.addMouseListener(new ListMouseListener());
			listMember.setCellRenderer(new ListCellRenderer(){
				
			    public Component getListCellRendererComponent(
					          JList list,
					          Object value,
					          int index,
					         boolean isSelected,
					          boolean cellHasFocus)
				{
			    	if(value instanceof String[]){
			    		String[] strTemps=(String[])value;
			    		if(strTemps!=null && strTemps.length>0){
			    			JLabel lb=new nc.ui.pub.beans.UILabel();
					    	lb.setText(strTemps[0]);
					    	
					    	lb.setBackground(isSelected ? UIManager.getColor("Tree.selectionBackground") : Color.white);
					    	
					    	lb.setOpaque(true);
					    	
					    	return lb;
			    		}
			    			
			    	}
			    	
			    	
//			    	lb.setForeground( isSelected ? Color.BLUE :Color.black);
//			        if (isSelected) {
//			            lb.setBackground(list.getSelectionBackground());
//			            lb.setForeground(list.getSelectionForeground());
//			          } else {
//			            lb.setBackground(list.getBackground());
//			            lb.setForeground(list.getForeground());
//			          }
					return null;
				}
			});
		}
		return listMember;
	}
	
	 public static void expandTree(JTree tree, DefaultMutableTreeNode node) {
        TreePath path = new TreePath(node.getPath());
        tree.expandPath(path);
        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            DefaultMutableTreeNode childNode =(DefaultMutableTreeNode) node.getChildAt(i);
            expandTree(tree, childNode);
        }

    }
          }  //  @jve:decl-index=0:visual-constraint="10,10"
