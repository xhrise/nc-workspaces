package com.ufsoft.iufo.inputplugin.inputcore;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.pub.iufo.accperiod.AccPeriodSchemeUtil;
import nc.pub.iufo.accperiod.IAccountCalendar;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.util.iufo.reportdata.DateInputCheckUtil;
import nc.vo.bd.period.AccperiodVO;
import nc.vo.bd.period.AccperiodschemeVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.table.beans.UFOTree;
import com.ufsoft.table.re.IDName;
import com.ufsoft.table.re.IRefComp;
import com.ufsoft.iufo.resource.StringResource;
/**
 * <pre>
 * </pre>
 * 
 * 会计期间年参照
 * 
 * @author 王宇光
 * @version Create on 2008-6-12
 */
public class AccPeriodYearRefComp extends nc.ui.pub.beans.UIPanel  implements IRefComp{
	private static final long serialVersionUID = 1L;
	private String strAccPreiodPk = null;
	private String strAccPeriodType = null;
	private UFOTree ufoTree = null;
	private UIButton refreshButton = null;
	private String strDefaultDate = null;
	public AccPeriodYearRefComp(String strAccPreiodPk,String strAccPeriodType,String strDefaultDate){
		super();
		this.strAccPreiodPk = strAccPreiodPk;
		this.strAccPeriodType = strAccPeriodType;
		this.strDefaultDate = strDefaultDate;
		initialize();
	}
	
	private void initialize(){
		setPreferredSize(new Dimension(300,500));
		this.setLayout(new BorderLayout());
		if(ufoTree == null){
			ufoTree = new UFOTree();
		}
		ufoTree.setModel(getPeriodYearTreeModel(strAccPreiodPk,strAccPeriodType));
		UIScrollPane treePanel = new UIScrollPane(ufoTree);
		
		JPanel buttonPanel = new UIPanel();
        buttonPanel.setPreferredSize(new Dimension(300,25));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(getRefreshButton());
        this.add(treePanel,BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.NORTH);
		initTreeSelected();
	}
	
	/**
	 * @i18n miufo00248=没有默认的会计期间方案，请重新设置
	 * @i18n miufo5508000002=会计年
	 * @i18n miufohbbb00110=该期间方案无数据
	 */
	private DefaultTreeModel getPeriodYearTreeModel(String strAccPreiodPk,String strAccPeriodType){
		if(!checkAccPreiod(strAccPreiodPk)){
			return new DefaultTreeModel(new DefaultMutableTreeNode(StringResource.getStringResource("miufo00248")),false);
		}
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(StringResource.getStringResource("miufo5508000002"));		
		try {
			IAccountCalendar calendar=AccPeriodSchemeUtil.getInstance().doGetAccCalendar(strAccPreiodPk);
			AccperiodVO[] periods=calendar.getYearVOsOfCurrentScheme();
			if(periods == null || periods.length ==0){
				return new DefaultTreeModel(new DefaultMutableTreeNode(StringResource.getStringResource("miufohbbb00110")),false);
			}
			addTreeChild(root,periods);
		} catch (Exception e) {
			AppDebug.debug(e);
			return null;
		}
		
		DefaultTreeModel treeModel = new DefaultTreeModel(root,false);
		
		return treeModel;
	}
	
	private void addTreeChild(DefaultMutableTreeNode pNode,AccperiodVO[] periods){
		if(pNode == null || periods == null || periods.length == 0){
			return;
		}
		int iLength = periods.length;
		for (int i = 0; i < iLength; i++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(periods[i].getPeriodyear());
			pNode.add(node);
		}
	}
	
	public Object getSelectValue() {
		DefaultMutableTreeNode node = ((DefaultMutableTreeNode)ufoTree.getSelectionPath().getLastPathComponent());
		if(node.isRoot()){
			return new IDName(null,null);
		}
        return node.getUserObject();
	}

	/**
	 * @i18n miufo00064=会计期间参照
	 */
	public String getTitleValue() {
		return StringResource.getStringResource("miufo00064");
	}

	public Object getValidateValue(String text) {
		if(DateInputCheckUtil.isValidDate(strAccPeriodType, text)){
			text=DateInputCheckUtil.convert2StandDate(strAccPeriodType, text);
		}
		IAccountCalendar calendar=AccPeriodSchemeUtil.getInstance().doGetAccCalendar(strAccPreiodPk);
		AccperiodVO[] periods=calendar.getYearVOsOfCurrentScheme();
		if(periods != null && periods.length >0){
			int iLength = periods.length;
			for (int i = 0; i < iLength; i++) {
				String value = periods[i].getPeriodyear();
				if(value != null && value.equals(text)){
					return text;
				}
			}
		}
		 return null;
	}

	private void initTreeSelected(){
		DefaultTreeModel treeModel = (DefaultTreeModel)ufoTree.getModel();
		if(treeModel == null) return;
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
		int iChildCount = root.getChildCount();
		DefaultMutableTreeNode defaultNode = null;
		if(strDefaultDate != null && strDefaultDate.length() > 0){
			for(int i = 0;i < iChildCount;i++){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i);
				String value = (String)node.getUserObject();
				if(value.equals(getDefaultYear())){
					defaultNode = node;
					break;
				}
			}
		}else{
			if(iChildCount > 0){
				defaultNode = (DefaultMutableTreeNode)root.getFirstChild();		
			}				
		}		
		if(defaultNode == null){
			defaultNode=root;
		}
		ufoTree.setSelectionPath(new TreePath(defaultNode.getPath()));
		EventQueue.invokeLater(new Runnable(){
			public void run() { 
				ufoTree.requestFocus();
			}
        	
        });
		
	}
	
	private String getDefaultYear(){
		if(strDefaultDate != null && strDefaultDate.length() > 3){
			return strDefaultDate.substring(0,4);
		}
		return null;
	}
	
	public void setDefaultValue(Object obj) {
		if(obj instanceof String){
			strDefaultDate=(String)obj;
		}
		initTreeSelected();	
	}
	
	/**
	 * 交验会计期间是否存在
	 * @param strAccPreiodPk
	 * @return
	 */
	private boolean checkAccPreiod(String strAccPreiodPk){
		AccperiodschemeVO schemeVO=AccPeriodSchemeUtil.getInstance().getPeriodSchemeByPK(strAccPreiodPk);
		if(schemeVO != null)
			return true;
		return false;
	}
	
	private JButton getRefreshButton(){
		Icon icon = UIManager.getIcon("WrapTreeRefComp.Refresh.image");
        String strImagePath = "/images/reportcore";
        if (icon == null) {
            icon = new ImageIcon(getClass().getResource(
            		strImagePath + "/refresh.gif"));
        }
		if(refreshButton == null){
			refreshButton = new UIButton(icon);
			refreshButton.setBounds(278,0,22,22);
			refreshButton.setPreferredSize(new Dimension(22,22));   
			refreshButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					ufoTree.setModel(getPeriodYearTreeModel(strAccPreiodPk,strAccPeriodType));
					initTreeSelected();
				}
				
			});
			registerKeyboardAction(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					ActionListener[] listeners=refreshButton.getActionListeners();
					ActionEvent event = null;
					for(int i=0;i<listeners.length;i++){
						event=new ActionEvent(e.getSource(),ActionEvent.ACTION_PERFORMED,"");
						listeners[i].actionPerformed(event);
					}
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_F5,0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		}
		return refreshButton;
	}
	
	/*
     * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener)
     */
    public synchronized void addMouseListener(MouseListener l) {
    	ufoTree.addMouseListener(l);
    }
}
 