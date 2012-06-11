package com.ufida.report.anareport.applet;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import nc.bs.iufo.datasetmanager.DataSetDirDMO;
import nc.ui.bi.query.manager.RptProvider;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.dsmanager.DatasetTreeDlg;
import nc.ui.pub.dsmanager.DesignWizardFactory;
import nc.ui.pub.querytoolize.AbstractWizardListPanel;
import nc.ui.pub.querytoolize.AbstractWizardStepPanel;
import nc.ui.pub.querytoolize.AbstractWizardTabPn;
import nc.ui.pub.querytoolize.WizardContainerDlg;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.pub.dsmanager.DataSetDesignObject;
import nc.vo.pub.dsmanager.exception.DSNotFoundException;

import com.ufida.dataset.Context;
import com.ufida.dataset.metadata.Field;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.rep.model.IBIField;
import com.ufida.zior.exception.MessageException;
import com.ufsoft.iufo.fmtplugin.ContextFactory;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.lookandfeel.plaf.Office2003ButtonUI;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

/**
 * 增加和删除左侧的数据集
 * 
 * @author ll
 * 
 */
public class AnaDataSourceExt extends AbsActionExt implements IUfoContextKey{
	public static final int DATASOURCE_ADD = 0;
	public static final int DATASOURCE_REMOVE = 1;
	public static final int DATASOURCE_PREVIEW = 2;
	public static final int DATASOURCE_EDIT = 3;
	public static final int DATASOURCE_REFRESH = 4;
	/**
	 * @i18n miufo1001633=插入
	 */
	public static final String RES_ID_INSERTDS = "miufo1001633";
	/**
	 * @i18n ubiquery0110=删除
	 */
	public static final String RES_ID_DELDS = "ubiquery0110";
	/**
	 * @i18n miufopublic251=刷新
	 */
	public static final String RES_ID_REFRESHDS = "miufopublic251";
	/**
	 * @i18n miufo00241=数据集
	 */
	public static final String RES_ID_DSMENU = "miufo00241";

	private int m_extType = DATASOURCE_ADD;
	private AnaReportPlugin m_plugin = null;

	public AnaDataSourceExt(AnaReportPlugin plugin, int extType) {
		super();
		m_plugin = plugin;
		m_extType = extType;
	}

	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				UfoReport report = (UfoReport) params[0];
				if (m_extType == DATASOURCE_ADD)
					execAddQuery(report);
				else if (m_extType == DATASOURCE_REMOVE)
					execRemoveQuery(report);
				else if (m_extType == DATASOURCE_REFRESH)
					execRefreshQuery(report);
				else
					doDataSetPreView(report);

			}
		};
	}

	public Object[] getParams(UfoReport container) {
		return new Object[] { container, m_plugin };
	}

	/**
	 * @i18n miufo00242=数据结果预览
	 * @i18n miufo00241=数据集
	 * @i18n iufobi00057=全部展开
	 * @i18n iufobi00058=全部折叠
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes mainMenu = new ActionUIDes();
//		ActionUIDes uiDes = new ActionUIDes();
//		uiDes.setToolBar(true);
//		uiDes.setGroup(StringResource.getStringResource(RES_ID_DSMENU));
//		uiDes.setPaths(new String[] {});
		if (m_extType == DATASOURCE_ADD) {
//			mainMenu.setName(StringResource.getStringResource(RES_ID_INSERTDS));
//			mainMenu.setGroup(StringResource.getStringResource(RES_ID_DSMENU));
//			mainMenu.setPaths(new String[] { StringResource.getStringResource(RES_ID_DSMENU) });
//			uiDes.setName(StringResource.getStringResource(RES_ID_INSERTDS));
//			uiDes.setTooltip(StringResource.getStringResource(RES_ID_INSERTDS));
//			uiDes.setImageFile("reportcore/add_dataset.gif");
			JButton addButton=new JButton();
			addButton.setUI(new Office2003ButtonUI());
			addButton.setToolTipText(StringResource.getStringResource(RES_ID_INSERTDS));
			addButton.setIcon(ResConst.getImageIcon("reportcore/add_dataset.gif"));
			addButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					try {
						execAddQuery(m_plugin.getReport());
					} catch (MessageException e2) {
						UfoPublic.sendMessage(e2, m_plugin.getReport());

					}
				}
				
			});
			m_plugin.getQueryPanel().getNorthPanel().add(addButton);
			JPopupMenu panelPopMenu=m_plugin.getQueryPanel().getPopUpMenu(AnaDataSetPanel.class.getName());
			if(panelPopMenu==null){
				panelPopMenu=new UIPopupMenu();
			}
			JPopupMenu datasetPopMenu=m_plugin.getQueryPanel().getPopUpMenu(DataSetDefVO.class.getName());
            if(datasetPopMenu==null){
            	datasetPopMenu=new UIPopupMenu();
            }
			AbstractAction addMenu=new AbstractAction(StringResource.getStringResource(RES_ID_INSERTDS)){

				public void actionPerformed(ActionEvent e) {
					try {
						execAddQuery(m_plugin.getReport());
					} catch (MessageException e2) {
						UfoPublic.sendMessage(e2, m_plugin.getReport());

					}
				}
				
			};
			panelPopMenu.add(addMenu);
			datasetPopMenu.add(addMenu);
			m_plugin.getQueryPanel().initTreeRightMouseListener(AnaDataSetPanel.class.getName(), panelPopMenu);
			m_plugin.getQueryPanel().initTreeRightMouseListener(DataSetDefVO.class.getName(), datasetPopMenu);
			datasetPopMenu=m_plugin.getQueryPanel().getPopUpMenu(RptProvider.class.getName());
            if(datasetPopMenu==null){
            	datasetPopMenu=new UIPopupMenu();
            }
            datasetPopMenu.add(addMenu);
            m_plugin.getQueryPanel().initTreeRightMouseListener(RptProvider.class.getName(), datasetPopMenu);
		} else if (m_extType == DATASOURCE_REMOVE) {
//			mainMenu.setName(StringResource.getStringResource(RES_ID_DELDS));
//			mainMenu.setGroup(StringResource.getStringResource(RES_ID_DSMENU));
//			mainMenu.setPaths(new String[] { StringResource.getStringResource(RES_ID_DSMENU) });
//			uiDes.setName(StringResource.getStringResource(RES_ID_DELDS));
//			uiDes.setTooltip(StringResource.getStringResource(RES_ID_DELDS));
//			uiDes.setImageFile("reportcore/delete_dataset.gif");
			JButton removeButton=new JButton();
			removeButton.setUI(new Office2003ButtonUI());
			removeButton.setToolTipText(StringResource.getStringResource(RES_ID_DELDS));
			removeButton.setIcon(ResConst.getImageIcon("reportcore/delete_dataset.gif"));
			removeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						execRemoveQuery(m_plugin.getReport());
					} catch (MessageException e2) {
						UfoPublic.sendMessage(e2, m_plugin.getReport());

					}
				}

			});
			m_plugin.getQueryPanel().getNorthPanel().add(removeButton);
			
			JPopupMenu popMenu=m_plugin.getQueryPanel().getPopUpMenu(DataSetDefVO.class.getName());
			if(popMenu==null){
				popMenu=new UIPopupMenu();
			}
			AbstractAction removeMenu=new AbstractAction(StringResource.getStringResource(RES_ID_DELDS)){

				public void actionPerformed(ActionEvent e) {
					try {
						execRemoveQuery(m_plugin.getReport());
					} catch (MessageException e2) {
						UfoPublic.sendMessage(e2, m_plugin.getReport());

					}
				}
				
			};
			popMenu.add(removeMenu);
			m_plugin.getQueryPanel().initTreeRightMouseListener(DataSetDefVO.class.getName(), popMenu);
			popMenu=m_plugin.getQueryPanel().getPopUpMenu(RptProvider.class.getName());
            if(popMenu==null){
            	popMenu=new UIPopupMenu();
            }
            popMenu.add(removeMenu);
            m_plugin.getQueryPanel().initTreeRightMouseListener(RptProvider.class.getName(),popMenu);
		} else if (m_extType == DATASOURCE_REFRESH) {
//			mainMenu.setName(StringResource.getStringResource(RES_ID_REFRESHDS));
//			mainMenu.setGroup(StringResource.getStringResource(RES_ID_DSMENU));
//			mainMenu.setPaths(new String[] { StringResource.getStringResource(RES_ID_DSMENU) });
//			uiDes.setName(StringResource.getStringResource(RES_ID_REFRESHDS));
//			uiDes.setTooltip(StringResource.getStringResource(RES_ID_REFRESHDS));
//			uiDes.setImageFile("reportcore/refresh_dataset.gif");
			JButton refButton=new JButton();
			refButton.setUI(new Office2003ButtonUI());
			refButton.setToolTipText(StringResource.getStringResource(RES_ID_REFRESHDS));
			refButton.setIcon(ResConst.getImageIcon("reportcore/refresh_dataset.gif"));
			refButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						execRefreshQuery(m_plugin.getReport());
					} catch (MessageException e2) {
						UfoPublic.sendMessage(e2, m_plugin.getReport());

					}
				}

			});
			m_plugin.getQueryPanel().getNorthPanel().add(refButton);
			JPopupMenu panelPopMenu=m_plugin.getQueryPanel().getPopUpMenu(AnaDataSetPanel.class.getName());
			if(panelPopMenu==null){
				panelPopMenu=new UIPopupMenu();
			}
			JPopupMenu datasetPopMenu=m_plugin.getQueryPanel().getPopUpMenu(DataSetDefVO.class.getName());
            if(datasetPopMenu==null){
            	datasetPopMenu=new UIPopupMenu();
            }
            AbstractAction refMenu=new AbstractAction(StringResource.getStringResource(RES_ID_REFRESHDS)){

				public void actionPerformed(ActionEvent e) {
					try {
						execRefreshQuery(m_plugin.getReport());
					} catch (MessageException e2) {
						UfoPublic.sendMessage(e2, m_plugin.getReport());

					}
				}
				
			};
            panelPopMenu.add(refMenu);
            datasetPopMenu.add(refMenu);
            m_plugin.getQueryPanel().initTreeRightMouseListener(AnaDataSetPanel.class.getName(), panelPopMenu);
			m_plugin.getQueryPanel().initTreeRightMouseListener(DataSetDefVO.class.getName(), datasetPopMenu);
			datasetPopMenu=m_plugin.getQueryPanel().getPopUpMenu(RptProvider.class.getName());
            if(datasetPopMenu==null){
            	datasetPopMenu=new UIPopupMenu();
            }
            datasetPopMenu.add(refMenu);
            m_plugin.getQueryPanel().initTreeRightMouseListener(RptProvider.class.getName(),datasetPopMenu);
		}else if(m_extType == DATASOURCE_EDIT&&m_plugin.isAegisFormat()){
			JPopupMenu popMenu=m_plugin.getQueryPanel().getPopUpMenu(RptProvider.class.getName());
			if(popMenu==null){
				popMenu=new UIPopupMenu();
			}
			popMenu.add(new AbstractAction(MultiLang.getString("edit")){

				public void actionPerformed(ActionEvent e) {
					try {
						execEditDataSetDef(m_plugin.getReport());
					} catch (MessageException e2) {
						UfoPublic.sendMessage(e2, m_plugin.getReport());

					}
				}
				
			});
			popMenu.addSeparator();
			m_plugin.getQueryPanel().initTreeRightMouseListener(RptProvider.class.getName(), popMenu);
		}else if(m_extType == DATASOURCE_PREVIEW){
			mainMenu.setName(StringResource.getStringResource("miufo00242"));
			mainMenu.setPaths(new String[] { StringResource.getStringResource(RES_ID_DSMENU) });
			JPopupMenu panelPopMenu=m_plugin.getQueryPanel().getPopUpMenu(AnaDataSetPanel.class.getName());
			if(panelPopMenu==null){
				panelPopMenu=new UIPopupMenu();
			}
			JPopupMenu datasetPopMenu=m_plugin.getQueryPanel().getPopUpMenu(DataSetDefVO.class.getName());
            if(datasetPopMenu==null){
            	datasetPopMenu=new UIPopupMenu();
            }
            AbstractAction previewMenu=new AbstractAction(StringResource.getStringResource("miufo00242")){

				public void actionPerformed(ActionEvent e) {
					try {
						doDataSetPreView(m_plugin.getReport());
					} catch (MessageException e2) {
						UfoPublic.sendMessage(e2, m_plugin.getReport());

					}
				}
				
			};
            datasetPopMenu.add(previewMenu);
            
			JButton collButton=new JButton();
			collButton.setUI(new Office2003ButtonUI());
			collButton.setToolTipText(StringResource.getStringResource("iufobi00057"));
			collButton.setIcon(ResConst.getImageIcon("reportcore/collapse.gif"));
			collButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					try {
						m_plugin.getQueryPanel().expandAll();
					} catch (MessageException e2) {
						UfoPublic.sendMessage(e2, m_plugin.getReport());

					}
				}
				
			});
			m_plugin.getQueryPanel().getNorthPanel().add(collButton);
			panelPopMenu.add(new AbstractAction(StringResource.getStringResource("iufobi00057")){

				public void actionPerformed(ActionEvent e) {
					m_plugin.getQueryPanel().expandAll();
				}
				
			});
			JButton expandButton=new JButton();
			expandButton.setUI(new Office2003ButtonUI());
			expandButton.setToolTipText(StringResource.getStringResource("iufobi00058"));
			expandButton.setIcon(ResConst.getImageIcon("reportcore/expand.gif"));
			expandButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					try {
						m_plugin.getQueryPanel().collapsePathAll(false);
					} catch (MessageException e2) {
						UfoPublic.sendMessage(e2, m_plugin.getReport());

					}
				}
				
			});
			m_plugin.getQueryPanel().getNorthPanel().add(expandButton);
			panelPopMenu.add(new AbstractAction(StringResource.getStringResource("iufobi00058")){

				public void actionPerformed(ActionEvent e) {
					m_plugin.getQueryPanel().collapsePathAll(false);
				}
				
			});
			m_plugin.getQueryPanel().initTreeRightMouseListener(AnaDataSetPanel.class.getName(), panelPopMenu);
			m_plugin.getQueryPanel().initTreeRightMouseListener(DataSetDefVO.class.getName(), datasetPopMenu);
			datasetPopMenu=m_plugin.getQueryPanel().getPopUpMenu(RptProvider.class.getName());
            if(datasetPopMenu==null){
            	datasetPopMenu=new UIPopupMenu();
            }
            datasetPopMenu.add(previewMenu);
            m_plugin.getQueryPanel().initTreeRightMouseListener(RptProvider.class.getName(),datasetPopMenu);
		}
		return new ActionUIDes[] { mainMenu};
	}
	private void execEditDataSetDef(UfoReport report){
		
		DataSetDefVO selDS = getSelectedQuery();
		//手动刷新,以防备编辑的不是最近的对象
		execRefreshQuery(report);
		m_plugin.getQueryPanel().setSelectPath(selDS);
		selDS = getSelectedQuery();
		if (selDS == null)
			return;
				
		Context context =ContextFactory.createContext(report);
		ContextFactory.initNCContext(context);
		DataSetDefVO cloneVo = (DataSetDefVO) selDS.clone();
		DataSetDesignObject dsdo = new DataSetDesignObject();
		dsdo.setCurDataSetDef(cloneVo);
		dsdo.setContext(context);
		dsdo.setShowTree(false);
		dsdo.setSaveWhenFinished(!m_plugin.isFromQueryReport());
		dsdo.setStatus(DataSetDesignObject.STATUS_UPDATE);
		// 向导列表面板
		String providerClass = cloneVo.getDataSetDef().getProvider()
				.getClass().getName();
		AbstractWizardListPanel listPn = DesignWizardFactory
				.createDataSetWizard(dsdo, providerClass);
		//设置修改时的Context
		if(dsdo.getCurDataSetDef().getDataSetDef()!=null 
				&& dsdo.getCurDataSetDef().getDataSetDef().getProvider()!=null){
			dsdo.getCurDataSetDef().getDataSetDef().getProvider().setContext(context);
		}
		
		// 弹框
		Dimension dm = new Dimension(1024,730);
		WizardContainerDlg dlg = new WizardContainerDlg(report);
		AbstractWizardTabPn tabPn = listPn.getWizardTabPn();
		AbstractWizardStepPanel[] panels = listPn.getStepPanels();
		if (panels != null && panels.length >=2 && panels[1] == null) return ;
		if (dm != null) {
			dlg.setSize(dm);
		}
		dlg.setListPn(listPn);
		dlg.setTabPn(tabPn);
		listPn.setSelectedIndex(1);
		dlg.showModal();
		if (dlg != null && dlg.getResult() == UIDialog.ID_OK) {
			DataSetDefVO dsDef = dsdo.getCurDataSetDef();
			if(m_plugin.isFromQueryReport()){
				m_plugin.getModel().getDataSource().addDateSetDefVOs(new DataSetDefVO[]{dsDef}, DataSetDirDMO.PrivateDataSetPK.equals(dsDef.getPk_datasetdir()));
			}
			DefaultMutableTreeNode selNode=getSelectedQueryNode();
			MutableTreeNode parent = (MutableTreeNode)selNode.getParent();
			int editIndex=parent.getIndex(selNode);
			((DefaultTreeModel) (m_plugin.getQueryPanel().getQueryModelTree()
					.getModel())).removeNodeFromParent(selNode);
			if (dsDef != null) {
				Field[] flds = dsDef.getDataSetDef().getMetaData().getFields(
						true);
				DefaultMutableTreeNode subRoot = new DefaultMutableTreeNode(
						dsDef);
				if (flds != null) {
					for (int i = 0; i < flds.length; i++) {
						subRoot.add(new DefaultMutableTreeNode(flds[i]));
					}
				}
				DefaultTreeModel model = (DefaultTreeModel) m_plugin
						.getQueryPanel().getQueryModelTree().getModel();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) model
						.getRoot();
				model.insertNodeInto(subRoot, root, editIndex);
				m_plugin.getQueryPanel().getQueryModelTree().expandPath(
						new TreePath(subRoot.getPath()));
				//手动刷新
				execRefreshQuery(report);
			}

		}
		
	}
	/**
	 * @i18n miufo00243=数据集已经使用，是否还要删除？
	 * @i18n miufo00785=数据集已经使用,请先删除扩展区
	 */
	private void execRemoveQuery(UfoReport report) {

		DataSetDefVO selDS = getSelectedQuery();
		if (selDS == null)
			return;
		String dataSourcePK = selDS.getPk_datasetdef();
		if (MessageDialog.showYesNoDlg(report, null, StringResource.getStringResource(RES_ID_DELDS) + selDS.getName()
				+ "?") == UIDialog.ID_YES) {

			if (m_plugin.getModel().isUsedDataSource(dataSourcePK)) {
				MessageDialog.showErrorDlg(report, null, StringResource.getStringResource("miufo00785"));
					return;
			}

			// 删除树上的节点
			DefaultMutableTreeNode selNode = getSelectedQueryNode();
			((DefaultTreeModel) m_plugin.getQueryPanel().getQueryModelTree().getModel()).removeNodeFromParent(selNode);
			m_plugin.getQueryPanel().getQueryModelTree().expandRow(0);

			// 需要删除模型中的查询
			m_plugin.getModel().removeDataSourcePK(new String[] { dataSourcePK });
			m_plugin.removeFromConditionPanel(dataSourcePK);
			m_plugin.setDirty(true);
			// 删除模型中的页维度字段
			// m_plugin.getConditionPanel().removeAllPageDim();
			// m_plugin.getConditionPanel().repaint();
		}
	}
	private void execRefreshQuery(UfoReport report) {
		String[] dsPKs = m_plugin.getModel().getDataSource().getAllDSPKs();
		m_plugin.getModel().clearDataSourceCache(dsPKs);
		m_plugin.refreshDataSource(false);
		
		//2008-10-09, ll, 数据态时同时起到刷新报表数据的作用
		if(report.getOperationState() == UfoReport.OPERATION_INPUT)
			m_plugin.refreshDataModel(true);
	}
	/**
	 * @i18n miufo00244=请先选中要查看的数据集
	 */
	public void doDataSetPreView(UfoReport container) {
		DataSetDefVO dsVO = getSelectedQuery();
		if (dsVO == null) {
			JOptionPane.showMessageDialog(container, StringResource.getStringResource("miufo00244"));
			return;
		}
		//@edit by yza at 2009-2-24 NC数据源异常提示
		try{
			DataSetPreviewDlg dlg = new DataSetPreviewDlg(container, m_plugin.getContextVO(), dsVO.getName());
			dlg.setDatas(dsVO.getDataSetDef());
			dlg.showModal();
		}catch(DSNotFoundException ex){
			AppDebug.debug(ex);
			MessageDialog.showErrorDlg(container,MultiLang.getString("error"), ex.getMessage());
		}
		
		return;
	}

	private DefaultMutableTreeNode getSelectedNode() {
		TreePath selPath = m_plugin.getQueryPanel().getQueryModelTree().getSelectionPath();
		if (selPath == null)
			return null;

		DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();

		return selNode;
	}

	private DefaultMutableTreeNode getSelectedQueryNode() {
		DefaultMutableTreeNode selNode = getSelectedNode();
		if (selNode == null)
			return null;

		if (selNode.getUserObject() instanceof IBIField) {
			selNode = (DefaultMutableTreeNode) selNode.getParent();
		} else if (selNode.getUserObject() instanceof Field) {
			selNode = (DefaultMutableTreeNode) selNode.getParent();
		}

		if (selNode.getUserObject() instanceof DataSetDefVO) {
			return selNode;
		}
		return null;

	}

	private DataSetDefVO getSelectedQuery() {

		DefaultMutableTreeNode selNode = getSelectedQueryNode();
		if (selNode == null)
			return null;

		return (DataSetDefVO) selNode.getUserObject();
	}

	private void execAddQuery(UfoReport report) {

		//yza+ 2008-6-27 加入私有数据集提供者ID,过滤私有数据集用
		String strRepId = report.getContextVo().getAttribute(REPORT_PK) == null ? null : (String)report.getContextVo().getAttribute(REPORT_PK);
		
//		String ownerID = report.getContextVo().getContextId()==null?"":report.getContextVo().getContextId();
		//added by csli 090701:对QE环境做特殊处理
		Integer obj = (Integer)m_plugin.getReport().getContextVo().getAttribute(ReportResource.OPEN_IN_MODAL_DIALOG);
		DatasetTreeDlg dlg=null;
		if(obj!=null&&obj >=0){
			dlg=new DatasetTreeDlg(m_plugin.getReport(),true,false,new String[]{"R","G","H"});
		}else{
			dlg=new DatasetTreeDlg(m_plugin.getReport(), true,strRepId);
		}
		
		dlg.setUsingType(true, true);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			// 获得多选结果
			DataSetDefVO[] defs = dlg.getSelDatasetDefs();
			m_plugin.addDataSource(defs, false);
			m_plugin.setDirty(true);
		}
	}
}
   