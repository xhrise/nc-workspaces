package com.ufsoft.iufo.inputplugin.inputcore;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import nc.pub.iufo.cache.base.CodeCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.code.CodeInfoTreeModel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iufo.code.CodeInfoVO;
import nc.vo.iufo.code.CodeVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.inputplugin.key.KeyFmt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.beans.UFOTree;
import com.ufsoft.table.re.AbstractRefEditor;
import com.ufsoft.table.re.IDName;
import com.ufsoft.table.re.IDNameToStringWrapper;
import com.ufsoft.table.re.IRefComp;
import com.ufsoft.table.re.TreeRefComp;

/**
 * 
 * @author zzl 2005-6-22
 */
public abstract class AbsCodeRefEditor extends AbstractRefEditor {
	private RefInfo refInfo ;
	/*
	 * @see com.ufsoft.table.re.AbstractRefEditor#getRefComp(com.ufsoft.table.CellsPane, java.lang.Object, int, int)
	 */
	protected IRefComp getRefComp(CellsPane table, Object value, int row,
			int col) {
		refInfo= getRefInfo(table,row,col);
		return getRefComp(refInfo);
	}
	public static IRefComp getRefComp(RefInfo refInfo){
		IRefComp refComp;
		final Hashtable htRefData = filterData(RefData.getData(refInfo, false),refInfo);//当时单位时，过滤掉上级单位。

		DefaultTreeModel treeModel = createTreeModel(htRefData);
		IDName idName = (IDName) htRefData.keySet().toArray(new IDName[0])[0];
		String strTitle = idName.getName();
		refComp = new TreeRefComp(treeModel,strTitle) {
			public Object getValidateValue(String id) {
				DefaultTreeModel treeModel = (DefaultTreeModel) getModel();
				TreeNode rootNode = (TreeNode) treeModel.getRoot();
				return findInChildNode(rootNode,id);
			}

			private IDName findInChildNode(TreeNode node, String id) {
				if (id == null || id.equals("")) {
					return null;
				}
				for(int i=0;i<node.getChildCount();i++){
					DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) node.getChildAt(i);
					IDName idName = (IDName) ((IDNameToStringWrapper)subNode.getUserObject()).getValue();
					if(idName != null && id.equals(idName.getID())){
						return idName;
					}else{
						IDName aim = findInChildNode(subNode,id);
						if(aim != null && aim.isRefNode()){
							return aim;
						}
					}
				}
				return null;
			}
		};

		return new WrapTreeRefComp(refComp,refInfo);
	}
	
	/**
	 * 当是单位参照时，过滤掉当前单位的上级单位。modify by wangyga 币种代码参照要过滤掉封存的币种	
	 * @param data
	 * @param refInfo
	 * @return
	 */
	public static Hashtable filterData(Hashtable data, RefInfo refInfo) {
		if(refInfo.getType() == KeyFmt.TYPE_CODE && CodeVO.COIN_CODE_ID.equals(refInfo.getCodeGroupPK())){//币种代码参照要过滤掉封存的币种		
			try {
				CodeCache codeCache=IUFOUICacheManager.getSingleton().getCodeCache();
	    		CodeVO	codeVo = codeCache.findCodeByID(refInfo.getCodeGroupPK()); 
				if(codeVo == null){
					JOptionPane.showMessageDialog(null,MultiLang.getString("miufobusiref0001"));
					return null;
				}
				Hashtable codeTable = new Hashtable();
				Hashtable dataTable = new Hashtable();
				Object rootObj = data.keySet().toArray()[0];
				Hashtable<IDName,Hashtable> subTable = (Hashtable)data.get(rootObj);								
				for(IDName idName : subTable.keySet()){
					Hashtable subData = subTable.get(idName);
					String codeInfoId = idName.getID();
					CodeInfoVO codeInfoVo = codeCache.findCodeInfoByID(codeVo, codeInfoId);
					if(codeInfoVo != null && codeInfoVo.getUsedTag() == CodeInfoVO.USED_TAG_YES){
						codeTable.put(idName, subData);
					}
				}
				dataTable.put(rootObj, codeTable);
				return dataTable;
			} catch (Exception e) {
				AppDebug.debug(e);
				throw new RuntimeException(e.getMessage());
			}			
		}else if(refInfo.getType() == RefInfo.TYPE_UNIT){//单位关键字是需要过滤。
			String curUnitCode = refInfo.getCurUnitCode();
			IDName root = (IDName) data.keySet().toArray()[0];
			Hashtable curUnitData = filterDataImpl(data,curUnitCode);
			curUnitData = curUnitData == null ? new Hashtable() : curUnitData;
			Hashtable rtn = new Hashtable();
			rtn.put(root, curUnitData);
			return rtn;
		}else{
			return data;
		}
	}
	private static Hashtable filterDataImpl(Hashtable<IDName,Hashtable> data, String unitCode) {
		for(IDName idName : data.keySet()){
			Hashtable subData = data.get(idName);
			if(unitCode.equals(idName.getID())){
				Hashtable curUnitList = new Hashtable<IDName, Hashtable>();
				curUnitList.put(idName, subData);
				return curUnitList;
			}
			Hashtable subsubData = filterDataImpl(subData,unitCode);
			if(subsubData != null) return subsubData;
		}
		return null;
	}
	static DefaultTreeModel createTreeModel(Hashtable htRefData){    	
		Object rootObj = htRefData.keySet().toArray()[0];        
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new IDNameToStringWrapper((IDName)rootObj));
		addTreeChild(root, (Hashtable) htRefData.get(rootObj));
		return new DefaultTreeModel(root, false);
	}
	public void setValue(Object newValue) {
		if(newValue instanceof IDName){
			((JTextField)getComponent()).setText((newValue != null) ? ((IDName)newValue).getID() : "");
		}else{
			throw new IllegalArgumentException();
		}
	}

	abstract protected RefInfo getRefInfo(CellsPane table, int row, int col);

	/**
	 * @param root
	 * @param object void
	 */
	private static void addTreeChild(DefaultMutableTreeNode pNode, Hashtable ht) {
		if(ht == null){
			return;
		}
		Object[] objs = ht.keySet().toArray();
		Arrays.sort(objs);
		for(int i=0;i<objs.length;i++){
			Object obj = objs[i];
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(new IDNameToStringWrapper((IDName)obj));           
			addTreeChild(node,(Hashtable) ht.get(obj));
			pNode.add(node);
		}      
	}
  
	/**
	 * add by guogang 2007-8-6
	 * 增加代码参照末级验证
	 * @i18n uiuforep00001=不存在代码值为
	 * @i18n uiuforep00002=的
	 * @i18n uiuforep00003=代码值为
	 * @i18n uiuforep00004=不是末级成员
	 */
	public boolean checkBeforStopEditing() {
		Object value=getCellEditorValue();
		if(value!=null&&!"".equals(value) && !RefData.isFromOffline()){//added by liulp,2008-01-09,only check for iufoinput
			String strInputVal=value.toString();
			if(refInfo!=null&&refInfo.getType()==KeyFmt.TYPE_CODE){
				CodeCache codeCache=IUFOUICacheManager.getSingleton().getCodeCache();
				String strCodeID=this.refInfo.getCodeGroupPK();
				try {
					CodeVO codeVO=codeCache.findCodeByID(strCodeID);
					if (codeVO==null){
						JOptionPane.showMessageDialog(null,MultiLang.getString("miufobusiref0001"));
						return false;
					}
					CodeInfoVO codeInfoVO=codeCache.findCodeInfoByID(codeVO,strInputVal);
					if(codeInfoVO==null){
						String message=MultiLang.getString("uiuforep00001")+strInputVal+MultiLang.getString("uiuforep00002") + codeVO.getName();
						JOptionPane.showMessageDialog(null,message);
						return false;
					}
					if (CodeInfoTreeModel.isRefNode(codeCache, codeVO, strInputVal, true)==false){
						String message=MultiLang.getString("uiuforep00003")+strInputVal+MultiLang.getString("uiuforep00002") + codeVO.getName()+MultiLang.getString("uiuforep00004");
						JOptionPane.showMessageDialog(null,message);
						return false;
					}
				} catch (Exception e) {
					AppDebug.debug(e);
				}
			}
			
		}
		return true;
	}
  
}

/**
 * 目的是添加刷新按钮。
 * @author zzl 2005-9-29
 */
class WrapTreeRefComp  extends nc.ui.pub.beans.UIPanel implements IRefComp{
    /**
     * <code>serialVersionUID</code> 的注释
     */
    private static final long serialVersionUID = -6563908989483384294L;
    private IRefComp m_refComp;
    private RefInfo m_refInfo = null;
    public WrapTreeRefComp(IRefComp refComp, RefInfo refInfo){
        m_refComp = refComp;
        m_refInfo = refInfo;
        addRefreshButton(refComp,refInfo);
        setPreferredSize(new Dimension(300,500));
    }

    public void setDefaultValue(Object obj) {
        m_refComp.setDefaultValue(obj);
    }

    public Object getSelectValue() {
    	Object rtnObj = m_refComp.getSelectValue();
    	if(rtnObj instanceof IDNameToStringWrapper){
    		rtnObj = ((IDNameToStringWrapper)rtnObj).getValue();
    	}
    	return rtnObj;
    }

    public Object getValidateValue(String text) {  
    	Object value = m_refComp.getValidateValue(text);
    	//查找封存的币种
    	if(value == null && m_refInfo.getType() == KeyFmt.TYPE_CODE && CodeVO.COIN_CODE_ID.equals(m_refInfo.getCodeGroupPK()))
    		value = getSelectedValue(text);   	
        return value;
    }

    private Object getSelectedValue(String id){
		CodeCache codeCache=IUFOUICacheManager.getSingleton().getCodeCache();
		if(m_refInfo == null)
			return null;
		String strCodeID=m_refInfo.getCodeGroupPK();
		try {
			CodeVO codeVO=codeCache.findCodeByID(strCodeID);			
			CodeInfoVO codeInfoVO=codeCache.findCodeInfoByID(codeVO,id);
			if(codeInfoVO == null)
				return null;
			IDName value = new IDName(codeInfoVO.getId(),codeInfoVO.getContent());
			return value;
		} catch (Exception e) {
            AppDebug.debug(e);
		}		
		return null;
	}
	
    /**
	 * @i18n repinput00001=刷新
	 */
    private void addRefreshButton(IRefComp refComp,final RefInfo refInfo) {
    	Icon icon = UIManager.getIcon("WrapTreeRefComp.Refresh.image");
        String strImagePath = "/images/reportcore";
        if (icon == null) {
            icon = new ImageIcon(getClass().getResource(
            		strImagePath + "/refresh.gif"));
        }
        final JButton refreshBtn = new nc.ui.pub.beans.UIButton(icon);
        refreshBtn.setPreferredSize(new Dimension(22,22));
        
        JPanel buttonPanel = new UIPanel();
        buttonPanel.setPreferredSize(new Dimension(300,25));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshBtn);
      
        final UFOTree tree = (UFOTree) refComp;
        UIScrollPane treePanel = new UIScrollPane(tree);
        
        //界面美化
        this.setLayout(new BorderLayout());//new BorderLayout());
        this.add(treePanel,BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.NORTH);
        refreshBtn.setBounds(278,0,22,22);
        refreshBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                final Hashtable htRefData = AbsCodeRefEditor.filterData(RefData.getData(refInfo,true),refInfo); 
                Object rtnObj=getSelectValue();
                tree.setModel(AbsCodeRefEditor.createTreeModel(htRefData));
                tree.getTreeNodeSearcher().refreshSearchContent();
                setDefaultValue(rtnObj);
            }                        
        });
        registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ActionListener[] listeners=refreshBtn.getActionListeners();
				ActionEvent event = null;
				for(int i=0;i<listeners.length;i++){
					event=new ActionEvent(e.getSource(),ActionEvent.ACTION_PERFORMED,"");
					listeners[i].actionPerformed(event);
				}
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_F5,0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    /*
     * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener)
     */
    public synchronized void addMouseListener(MouseListener l) {
        ((Component)m_refComp).addMouseListener(l);
    }

	public String getTitleValue() {
		if(m_refInfo != null){
			//增加单位参照弹出窗口的标题
			if(m_refInfo.getType() == RefInfo.TYPE_UNIT){
				return MultiLangInput.getString("uiufotableinput0029");//单位参照
			}
		}
		return m_refComp.getTitleValue();
	}
    
}  