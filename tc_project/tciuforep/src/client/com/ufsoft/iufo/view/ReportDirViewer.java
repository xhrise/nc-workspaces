package com.ufsoft.iufo.view;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.unit.ITreeDataSearcher;
import nc.vo.iuforeport.rep.ReportDirVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.AppWorker;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.util.ResourceManager;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.impl.TreeViewer;
import com.ufsoft.iufo.fmtplugin.BDContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.view.tree.IufoLazyTree;
import com.ufsoft.report.ReportContextKey;

/**
 * 报表目录树
 * @author zhaopq
 * @created at 2009-5-14,下午02:46:15
 */
public class ReportDirViewer extends TreeViewer {
	private static final long serialVersionUID = 927062937621955164L;
	
	private IufoLazyTree ufoTree = null;
	
	private ImageIcon unitImage = null;
	
	private ImageIcon reportImage = null;
	
	@Override
	protected void startup() {
		super.startup();
		
		loadTreeData();

		// liuyy+ 提高报表打开速度，先一步完成Cache init.
		new AppWorker("init Cache Manager.") {
			@Override
			protected Object construct() throws Exception {
				IUFOUICacheManager.getSingleton();
				return null;
			}

		}.start(1000);
	}

	@Override
	protected JTree createTree() {
		if(ufoTree == null){
			ufoTree = new IufoLazyTree(new TreeDataSearcher());
			ufoTree.setCellRenderer(new DefaultTreeCellRenderer(){

				private static final long serialVersionUID = 1L;

				@Override
				public Icon getLeafIcon() {
					if(reportImage == null){
						reportImage = ResourceManager.createIcon("/images/reportcore/report.gif");
					}
					return reportImage;
				}

				@Override
				public Icon getClosedIcon() {
					if(unitImage == null){
						unitImage = ResourceManager.createIcon("/images/reportcore/unit_info.gif");
					}
					return unitImage;
				}

				@Override
				public Icon getOpenIcon() {
					return getClosedIcon();
				}
				
				
			});
		}
		return ufoTree;
	}

	protected void refreshViewer() {	
		ufoTree.constructTreeModel();
		loadTreeData();
	}

	@Override
	public void refresh() {
		refreshViewer();
	}

	private void loadTreeData(){
		
		String reportPk = (String) getContext().getAttribute(
				ReportContextKey.REPORT_PK);
		
		ufoTree.initTree(new RepDirAndRepNodeLoader(this, reportPk), null);

		ufoTree.focusTreeNode(getReportNameWithCode(reportPk));
	}
	
	private String getReportNameWithCode(String reportId) {
		ReportVO reportVO = (ReportVO) IUFOUICacheManager.getSingleton()
				.getReportCache().get(reportId);
		if(reportVO == null){
			return null;
		}
		
		if(reportVO.getRepType() == ReportDirVO.REPORT_DIR_TYPE_ANALYSIS){
			return reportVO.getName();
		}
		return reportVO.getNameWithCode();
	}


	@Override
	protected void doLinkWithViewer(Viewer currentActiveView) {
		if (currentActiveView instanceof ReportFormatDesigner) {
			String reportPk = (String)currentActiveView.getContext().getAttribute(ReportContextKey.REPORT_PK);
			ufoTree.focusTreeNode(getReportNameWithCode(reportPk));
		}
	}

	@Override
	protected void doCollapseTree() {
		if(!ufoTree.isCollapsed()){
			ufoTree.collapseWholeTree();
			ufoTree.expandedTreeOneDegree();
		}
	}
	
	protected void dblClickTreeNode() {
	}

	@Override
	protected void shutdown() {
		
	}
	
	private class TreeDataSearcher implements ITreeDataSearcher{

		public String[] searchNodePaths(String strText, String strOldPK,
				boolean down) {
			String strLoginUnitId = (String) getContext().getAttribute(BDContextKey.LOGIN_UNIT_ID);
			String strOrgPk = (String)getContext().getAttribute(IUfoContextKey.ORG_PK);
			String strModelId = (String) getContext().getAttribute(IUfoContextKey.MODEL_ID);
			String strUserId = (String) getContext().getAttribute(BDContextKey.CUR_USER_ID);
			
			Object response = ActionHandler.exec(
					"nc.ui.iuforeport.rep.JTreeTableMngAction",
					"searchNodePaths", new Object[]{strLoginUnitId,strText,strOldPK,Boolean.valueOf(down),strOrgPk,strModelId,strUserId});
			
			return (String[])response;
		}
		
	}
}
