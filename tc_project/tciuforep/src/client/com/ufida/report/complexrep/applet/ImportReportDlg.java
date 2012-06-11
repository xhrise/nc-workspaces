package com.ufida.report.complexrep.applet;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import nc.pub.iufo.cache.base.BDCacheConstants;
import nc.pub.iufo.cache.base.UICacheManager;
import nc.pub.iufo.cache.base.UserCache;
import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import nc.util.bi.resmng.IBIResMngConstants;

import nc.util.iufo.resmng.ResMngBizHelper;
import nc.util.iufo.resmng.loader.ITreeLoader;
import nc.util.iufo.resmng.loader.LoaderParam;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.user.UserInfoVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.rep.model.BIContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.userrole.UserRoleFuncInfo;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;

public class ImportReportDlg extends UfoDialog {
	private  static final String[] posShow = new String[] { "uibicomplex0001"/**上方*/, "uibicomplex0002"/**下方*/, "uibicomplex0003"/**左方*/, "uibicomplex0004"/**右方*/ };

	private String[] posRtn = new String[] { ComplexRepPlugin.TOP, ComplexRepPlugin.BOTTOM, ComplexRepPlugin.LEFT,
			ComplexRepPlugin.RIGHT };

	private JPanel jContentPane = null;

	private nc.ui.pub.beans.UITabbedPane jTabbedPane = null;

	private JPanel jPanelImport = null;

	private JPanel jPanelNewAdhoc = null;

	private JPanel jPanelNewMulti = null;

	private JPanel jPanelNewChart = null;

	private JPanel jPanelNewSpread = null;
	private JPanel jPanelNewFree = null;
	private JButton jButtonCancle = null;

	private JButton jButtonOK = null;

	private JComboBox jComboBoxPos = null;

	private JLabel jLabelPos = null;

	private JScrollPane jScrollPane = null;

	private JTree jTreeImport = null;

	private JCheckBox jCheckBoxLinkChart = null;

	private JCheckBox jCheckBoxLinkMulti = null;

	private JLabel jLabelAdhoc = null;

	private JLabel jLabelSpread = null;

	private JLabel jLabelFree = null;
	private JButton jButtonSelectChart = null;

	private JButton jButtonSelectMulti = null;

	private JLabel jLabelMulti = null;

	private JLabel jLabelChart = null;

	private ComplexRepPlugin m_plugin;
	
	public  static  class ResTree extends nc.ui.pub.beans.UITree{
		 public ResTree(TreeNode root) {
	        super(root, false);
	        setRootVisible(true);
	        if(root instanceof DefaultTreeModel){
	        	((DefaultTreeModel) root).setAsksAllowsChildren(true);
	        }
	    }
		public String convertValueToText(Object value, boolean selected,
				boolean expanded, boolean leaf, int row, boolean hasFocus) {
			if (value != null) {
				String strRet=value.toString();
				if (value instanceof DefaultMutableTreeNode){
					Object obj= ((DefaultMutableTreeNode) value).getUserObject();
					if(obj!=null && obj instanceof IResTreeObject){
						strRet=((IResTreeObject)obj).getLabel();
					}
					if(((DefaultMutableTreeNode) value).isRoot())
						strRet=StringResource.getStringResource(strRet);
				}
				return strRet;
			}
			return "";
		}

	}

	/**
	 * This is the default constructor
	 * 
	 * @param posReport
	 * @param m_plugin
	 */
	public ImportReportDlg(ComplexRepPlugin plugin, UfoReport posReport) {
		super(plugin.getReport().getFrame());
		m_plugin = plugin;
		m_posReport = posReport;
		initialize();
		getJButtonSelectChart().setEnabled(false);
		getJButtonSelectMulti().setEnabled(false);
		initCheckBox();
	}
	private void initCheckBox(){
		if(m_plugin==null || m_plugin.getModel()==null){
			getJCheckBoxLinkChart().setEnabled(false);
			getJCheckBoxLinkMulti().setEnabled(false);
			return;
		}
		getJCheckBoxLinkChart().setEnabled(m_plugin.getModel().isHaveChartModel());
		getJCheckBoxLinkMulti().setEnabled(m_plugin.getModel().isHaveMultiDimModel());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setBounds(0, 0, 600, 400);
		this.setContentPane(getJContentPane());
		
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabelPos = new nc.ui.pub.beans.UILabel();
			jLabelPos.setBounds(15, 312, 63, 30);
			jLabelPos.setText(StringResource.getStringResource("uibicomplex0005"));//插入位置
			jContentPane = new UIPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJTabbedPane(), null);
			jContentPane.add(getJButtonCancle(), null);
			jContentPane.add(getJButtonOK(), null);
			jContentPane.add(getJComboBoxPos(), null);
			jContentPane.add(jLabelPos, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return nc.ui.pub.beans.UITabbedPane
	 */
	private nc.ui.pub.beans.UITabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new nc.ui.pub.beans.UITabbedPane();
			jTabbedPane.setBounds(0, 2, 592, 298);
			jTabbedPane.addTab(StringResource.getStringResource("uibicomplex0006")/**导入报表*/, null, getJPanelImport(), null);
			jTabbedPane.addTab(StringResource.getStringResource("uibicomplex0007")/**新建AdHoc*/, null, getJPanelNewAdhoc(), null);
			jTabbedPane.addTab(StringResource.getStringResource("uibicomplex0008")/**新建多维表*/, null, getJPanelNewMulti(), null);
			jTabbedPane.addTab(StringResource.getStringResource("uibicomplex0009")/**新建图表*/, null, getJPanelNewChart(), null);
			jTabbedPane.addTab(StringResource.getStringResource("uibicomplex0010")/**新建电子表格*/, null, getJPanelNewSpread(), null);
			jTabbedPane.addTab(StringResource.getStringResource("uibicomplex0026")/**新建自由查询*/, null, getJPanelNewFree(), null);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelImport() {
		if (jPanelImport == null) {
			jPanelImport = new UIPanel();
			jPanelImport.setLayout(new BorderLayout());
			jPanelImport.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jPanelImport;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelNewAdhoc() {
		if (jPanelNewAdhoc == null) {
			jLabelAdhoc = new nc.ui.pub.beans.UILabel();
			jLabelAdhoc.setText(StringResource.getStringResource("uibicomplex0011"));//AdHoc报表介绍
			jLabelAdhoc.setBounds(5, 5, 580, 200);
			jPanelNewAdhoc = new UIPanel();
			jPanelNewAdhoc.setLayout(null);
			jPanelNewAdhoc.add(jLabelAdhoc, null);
		}
		return jPanelNewAdhoc;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelNewMulti() {
		if (jPanelNewMulti == null) {
			jLabelMulti = new nc.ui.pub.beans.UILabel();
			jLabelMulti.setBounds(5, 5, 580, 200);
			jLabelMulti.setText(StringResource.getStringResource("uibicomplex0012"));//多维表介绍
			jPanelNewMulti = new UIPanel();
			jPanelNewMulti.setLayout(null);
			jPanelNewMulti.add(getJCheckBoxLinkChart(), null);
			jPanelNewMulti.add(getJButtonSelectChart(), null);
			jPanelNewMulti.add(jLabelMulti, null);
		}
		return jPanelNewMulti;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelNewChart() {
		if (jPanelNewChart == null) {
			jLabelChart = new nc.ui.pub.beans.UILabel();
			jLabelChart.setBounds(5, 5, 580, 200);
			jLabelChart.setText(StringResource.getStringResource("uibicomplex0013"));//图表介绍
			jPanelNewChart = new UIPanel();
			jPanelNewChart.setLayout(null);
			jPanelNewChart.add(getJCheckBoxLinkMulti(), null);
			jPanelNewChart.add(getJButtonSelectMulti(), null);
			jPanelNewChart.add(jLabelChart, null);
		}
		return jPanelNewChart;
	}

	/**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelNewSpread() {
		if (jPanelNewSpread == null) {
			jLabelSpread = new nc.ui.pub.beans.UILabel();
			jLabelSpread.setText(StringResource.getStringResource("uibicomplex0014"));//电子表格介绍
			jLabelSpread.setBounds(5, 5, 580, 200);
			jPanelNewSpread = new UIPanel();
			jPanelNewSpread.setLayout(null);
			jPanelNewSpread.add(jLabelSpread, null);
		}
		return jPanelNewSpread;
	}
	/**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelNewFree() {
		if (jPanelNewFree == null) {
			jLabelFree = new nc.ui.pub.beans.UILabel();
			jLabelFree.setText(StringResource.getStringResource("uiufofurl530128"));//自由查询
			jLabelFree.setBounds(5, 5, 580, 200);
			jPanelNewFree = new UIPanel();
			jPanelNewFree.setLayout(null);
			jPanelNewFree.add(jLabelFree, null);
		}
		return jPanelNewFree;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancle() {
		if (jButtonCancle == null) {
			jButtonCancle = new nc.ui.pub.beans.UIButton();
			
			jButtonCancle.setBounds(481, 332, 75, 22);
			jButtonCancle.setText(StringResource.getStringResource("miufopublic247"));
			jButtonCancle.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setResult(ID_CANCEL);
					close();
				}

			});
		}
		return jButtonCancle;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new nc.ui.pub.beans.UIButton();
			jButtonOK.setBounds(385, 332, 75, 22);
			jButtonOK.setText(StringResource.getStringResource("miufopublic246"));
			jButtonOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!isNew() && getImportRepPK() == null) {
						JOptionPane.showMessageDialog(ImportReportDlg.this, StringResource.getStringResource("uibicomplex0015"));//请选择一个报表节点!
						return;
					}
					setResult(ID_OK);
					close();
				}

			});
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxPos() {
		if (jComboBoxPos == null) {
			String[] strSelected=new String[posShow.length];
			for(int i=0,size=posShow.length;i<size;i++){
				strSelected[i]=StringResource.getStringResource(posShow[i]);
			}
			jComboBoxPos = new UIComboBox(strSelected);
			jComboBoxPos.setBounds(89, 312, 73, 30);
		}
		return jComboBoxPos;
	}

	public static TreeNode getRootTreeNode(String userPK,boolean bIncludeFile,int iRightType) {
		
		//1.获得用户拥有的的资源
		UserCache userCache=(UserCache) UICacheManager.getSingleton().getCache(BDCacheConstants.UserCacheObjName);
		UserInfoVO	userVO=userCache.getUserById(userPK);
        UserRoleFuncInfo userRoleFuncInfo = new UserRoleFuncInfo(userVO.getID());
        try{
        	userRoleFuncInfo.init();	
        }catch(Exception e){
        	AppDebug.debug(e);
        }
        userVO.setUserRoleFuncInfo(userRoleFuncInfo);
		IResTreeObject[] treeObjss=null;
		try {
			ITreeLoader treeLoader = ResMngBizHelper.getTreeLoader(IBIResMngConstants.MODULE_REPORT);
			String strResOwnerPK=ResMngBizHelper.getResOwnerPK(IBIResMngConstants.MODULE_REPORT,userPK);
			boolean bHaveResMngRight  = AuthUIBizHelper.haveResMngRight(IBIResMngConstants.MODULE_REPORT,userVO);
//			int iRightType=IAuthorizeTypes.AU_TYPE_VIEW;
			LoaderParam loaderParam=new LoaderParam(strResOwnerPK,bHaveResMngRight,iRightType,true);
			treeObjss= treeLoader.loadResTreeObjs(userPK,
					bIncludeFile, loaderParam,null);
//			treeObjss = BIReportSrv.getInstance().getAllFiles();// (userVO,true);
		} catch (Exception e) {
			AppDebug.debug(e);
			treeObjss = new IResTreeObject[0];
		}
		
		//2.组织树
		DefaultMutableTreeNode root = null;
		
		IResTreeObject[] treeobjs = treeObjss;
		if (treeobjs != null && treeobjs.length > 0) {
			Hashtable ht = new Hashtable();
			
			//获得root
			for (int j = 0; j < treeobjs.length; j++) {
				if(treeobjs[j]==null )
					continue;
				
				//对于包含报表节点，排除复合报表
				if(bIncludeFile==true && treeobjs[j].getSrcVO()!=null
						&& treeobjs[j].getSrcVO() instanceof ReportVO
						&& ((ReportVO) (treeobjs[j].getSrcVO())).getType().intValue() == ReportResource.INT_REPORT_COMPLEX){
					continue;
				}
				DefaultMutableTreeNode tmpNode = new DefaultMutableTreeNode(treeobjs[j]);
				tmpNode.setAllowsChildren(treeobjs[j].getType() == IResTreeObject.OBJECT_TYPE_DIR);
				ht.put(treeobjs[j].getID(), tmpNode);
				
				if(treeobjs[j].getParentID()==null || "".equals(treeobjs[j].getParentID())){
					root=tmpNode;
				}
			}
			
			for (int j = 0; j < treeobjs.length; j++) {//对于有效节点进行上下级链接
				if(treeobjs[j]==null || !ht.containsKey(treeobjs[j].getID()) )
					continue;
				if(treeobjs[j].getParentID()!=null &&  "".equals(treeobjs[j].getParentID())==false){
					DefaultMutableTreeNode itself = (DefaultMutableTreeNode) ht.get(treeobjs[j].getID());
					DefaultMutableTreeNode parent =(DefaultMutableTreeNode) ht.get(treeobjs[j].getParentID());
					if(itself!=null && parent!=null)
						parent.add(itself);
				}
	
			}
		}
		if(root==null)
			root=new DefaultMutableTreeNode();
		
		return root;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new UIScrollPane();
			jScrollPane.setViewportView(getJTreeImport());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTree
	 * 
	 * @return javax.swing.JTree
	 */
	private JTree getJTreeImport() {
		if (jTreeImport == null) {
			String userPK = ((BIContextVO) m_plugin.getReport().getContextVo()).getCurUserID();
			jTreeImport = new ResTree(getRootTreeNode(userPK,true,IAuthorizeTypes.AU_TYPE_VIEW));
			
			((DefaultTreeModel) jTreeImport.getModel()).setAsksAllowsChildren(true);
		}
		return jTreeImport;
	}

	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxLinkChart() {
		if (jCheckBoxLinkChart == null) {
			jCheckBoxLinkChart = new UICheckBox();
			jCheckBoxLinkChart.setText(StringResource.getStringResource("uibicomplex0016"));//是否要与图表联动
			jCheckBoxLinkChart.setBounds(10, 218, 143, 26);
			jCheckBoxLinkChart.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						getJButtonSelectChart().setEnabled(true);
					} else {
						getJButtonSelectChart().setEnabled(false);
					}
				}
			});
		}
		return jCheckBoxLinkChart;
	}

	/**
	 * This method initializes jCheckBox1
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxLinkMulti() {
		if (jCheckBoxLinkMulti == null) {
			jCheckBoxLinkMulti = new UICheckBox();
			jCheckBoxLinkMulti.setText(StringResource.getStringResource("uibicomplex0017"));//是否要与多维表联动
			jCheckBoxLinkMulti.setBounds(10, 218, 143, 26);
			jCheckBoxLinkMulti.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						getJButtonSelectMulti().setEnabled(true);
					} else {
						getJButtonSelectMulti().setEnabled(false);
					}
				}
			});
		}
		return jCheckBoxLinkMulti;
	}

	/**
	 * This method initializes jButtonSelectChart
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSelectChart() {
		if (jButtonSelectChart == null) {
			jButtonSelectChart = new nc.ui.pub.beans.UIButton();
			jButtonSelectChart.setBounds(184, 218, 75, 22);
			jButtonSelectChart.setText(StringResource.getStringResource("uibicomplex0018"));//选择图表
			jButtonSelectChart.addActionListener(getSelectActionListener());
		}
		return jButtonSelectChart;
	}

	/**
	 * This method initializes jButtonSelectMulti
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSelectMulti() {
		if (jButtonSelectMulti == null) {
			jButtonSelectMulti = new nc.ui.pub.beans.UIButton();
			jButtonSelectMulti.setBounds(184, 218, 75, 22);
			jButtonSelectMulti.setText(StringResource.getStringResource("uibicomplex0019"));//选择多维表
			jButtonSelectMulti.addActionListener(getSelectActionListener());
		}
		return jButtonSelectMulti;
	}

	private ActionListener getSelectActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isLinkState = true;
				ImportReportDlg.this.setVisible(false);
			}

		};
	}

	/** *****对外提供的方法************** */
	private boolean isLinkState = false;

	public void setLinkState(boolean isLinkState) {
		this.isLinkState = isLinkState;
	}

	public boolean isLinkState() {
		return isLinkState;
	}

	public boolean isNew() {
		return getJTabbedPane().getSelectedComponent() != getJPanelImport();
	}

	public String getPos() {
		int index = getJComboBoxPos().getSelectedIndex();
		return posRtn[index];
	}

	public int getNewType() {
		if (getJTabbedPane().getSelectedComponent() == getJPanelNewAdhoc()) {
			return ReportResource.INT_REPORT_ADHOC;
		} else if (getJTabbedPane().getSelectedComponent() == getJPanelNewMulti()) {
			return ReportResource.INT_REPORT_MULTI;
		} else if (getJTabbedPane().getSelectedComponent() == getJPanelNewChart()) {
			return ReportResource.INT_REPORT_CHART;
		} else if (getJTabbedPane().getSelectedComponent() == getJPanelNewSpread()) {
			return ReportResource.INT_REPORT_SPREADSHT;
		} else if (getJTabbedPane().getSelectedComponent() == getJPanelNewFree()) {
			return ReportResource.INT_REPORT_FREE;
		} else {
			return -1;
		}
	}

	public String getImportRepPK() {
		TreePath path = getJTreeImport().getSelectionPath();
		if (path == null) {
			return null;
		}
		String strRepPK=null;
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		if (treeNode.isLeaf()) {
			IResTreeObject resTree=(IResTreeObject) treeNode.getUserObject();
			if(resTree!=null && resTree.getSrcVO()!=null 
					&& resTree.getSrcVO() instanceof ReportVO){
				strRepPK=((ReportVO) resTree.getSrcVO()).getPrimaryKey();
			}
		}
		return strRepPK;

	}

	public boolean isLinkAction() {
		if (getJTabbedPane().getSelectedComponent() == getJPanelNewChart()) {
			return getJCheckBoxLinkMulti().isSelected();
		} else if (getJTabbedPane().getSelectedComponent() == getJPanelNewMulti()) {
			return getJCheckBoxLinkChart().isSelected();
		} else {
			return false;
		}
	}

	public UfoReport getLinkReport() {
		return m_plugin.getFocusSubReport();
	}

	private UfoReport m_posReport;

	public UfoReport getPosReport() {
		return m_posReport;
	}
}
