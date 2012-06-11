package com.ufida.report.rep.applet;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import nc.itf.iufo.freequery.IMember;
import nc.vo.bi.integration.dimension.DimMemberVO;

import com.ufida.report.adhoc.model.PageDimField;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.re.GeneralTreeRefComp;
import com.ufsoft.table.re.IRefComp;
import com.ufsoft.table.re.GeneralListRefComp;
import com.ufsoft.table.re.RefTextField;

public class PageDimRefTextField extends RefTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PageDimField pageDimField;
	private IMember[] allMembers ;
	
	public PageDimRefTextField(PageDimField pageDimField){
    	super();
    	this.pageDimField=pageDimField;   	
    }
    
    @Override
	protected void initRef() {
    	IRefComp refComp=null;
    	String strTitle=StringResource.getStringResource("mbirep00019");
    	getAllMembers();//确保有值,在获取成员的过程中可能设置其是树还是列表
		if (pageDimField.isTree()) {
			refComp=getTreeRefComp(strTitle);			
		}else{
			refComp=getListRefComp(strTitle);
			
		}
		if(refComp!=null){
			setRefComp(refComp,null);
		}
	}

	private IRefComp getTreeRefComp(String title) {
		IRefComp refComp = null;
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(pageDimField.getName());
		DefaultTreeModel treeModel = new DefaultTreeModel(root,false);
		IMember[] vos =getAllMembers();
		addTreeChild(root,"",0,vos);
		refComp=new GeneralTreeRefComp(treeModel,title);
		
		return refComp;
	}

	private IRefComp getListRefComp(String title) {
		IRefComp refComp = null;
		IMember[] vos =getAllMembers();
		
		if(vos == null) return refComp;
		DefaultListModel listModel = new DefaultListModel();
		if(!pageDimField.isOrdered()){
			vos=getOrderList(vos);
		}
		
		if (vos != null) {
			if(pageDimField.isShowCode()){
				for(int i = 0; i < vos.length; i++) listModel.addElement(vos[i].getMemcode());
			}else{
				for(int i = 0; i < vos.length; i++) listModel.addElement(vos[i].getName());
			}				
		}
		refComp=new GeneralListRefComp(listModel,title);
		
		return refComp;

	}
	private IMember[] getAllMembers() {
		if(allMembers==null){
			allMembers=pageDimField.getAllValues();
		}
		return allMembers;
	}
	private IMember[] getOrderList(IMember[] allMembers){
		TreeMap<String,IMember> set = new TreeMap<String,IMember>();//排序
		if(pageDimField.isShowCode()){
			for(int i = 0; i < allMembers.length; i++) set.put(allMembers[i].getMemcode(), allMembers[i]);
		}else{
			for(int i = 0; i < allMembers.length; i++) set.put(allMembers[i].getName(), allMembers[i]);
		}
		this.allMembers = (IMember[]) set.values().toArray(new IMember[0]);
		
		return this.allMembers;
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

	@Override
	protected void addCloseListener() {
        if(m_refComp instanceof GeneralTreeRefComp){
        	((GeneralTreeRefComp)m_refComp).getTreeRefComp().addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount()>=2){
                        Object obj = m_refComp.getSelectValue();
                        if(obj instanceof DimMemberVO){ 
                        	setText(((DimMemberVO)obj).getMemname());
                            grabFocus();
                            dialog.setVisible(false);
                            pageDimField.setSelectedValue((DimMemberVO)obj);
                        }
                    }
                }
            });
        }
        if(m_refComp instanceof GeneralListRefComp){
        	((GeneralListRefComp)m_refComp).getListRefComp().addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount()>=2){
                    	int selectIndex=((GeneralListRefComp)m_refComp).getSelectedIndex();
                    	DimMemberVO returnObj=null;
                        if ( selectIndex> -1) {
                        	setText(m_refComp.getSelectValue().toString());
                            grabFocus();
                            dialog.setVisible(false);
                            returnObj=(DimMemberVO) getAllMembers()[selectIndex];
         			      }
                        pageDimField.setSelectedValue(returnObj);
                        
                    }
                }
            });
        }
    	
    
	}

	public PageDimField getPageDimField() {
		return pageDimField;
	}
    
    
}
