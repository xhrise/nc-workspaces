package com.ufsoft.iufo.view;

import nc.util.iufo.resmng.ResMngToolKit;

import com.ufida.zior.docking.core.Dockable;
import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.view.tree.IufoLazyTreeNodeModel;
import com.ufsoft.iufo.view.tree.MouseEventHandlerAdapter;

public class RepNodeMouseEventHandler extends MouseEventHandlerAdapter {
	private Viewer viewer;

	public RepNodeMouseEventHandler(Viewer viewer) {
		this.viewer = viewer;
	}

	public void doDoubleClicked(Object param) {
		IufoLazyTreeNodeModel nodeModel = (IufoLazyTreeNodeModel) param;
		String designerId = createReportDesignerId(nodeModel.getId());
		if(viewer.getView(designerId)==null){
			viewer.openView(ReportFormatDesigner.class.getName(),designerId);
			// @edit by wangyga at 2009-6-5,上午10:51:07 此处判断的报表权限不全面
//			newViewer.getContext().setAttribute(IUfoContextKey.FORMAT_RIGHT, nodeModel.getResRight());
		}
	}

	@Override
	public void doOneClicked(Object param) {
		IufoLazyTreeNodeModel nodeModel = (IufoLazyTreeNodeModel) param;
		Dockable dockable = viewer.getMainboard().getDockingManager().getDockable(createReportDesignerId(nodeModel.getId()));
		if(dockable!=null){
			dockable.active();
		}
	}

	private String createReportDesignerId(String reportId) {
		return "format_" + ResMngToolKit.getVOIDByTreeObjectID(reportId);// 报表ID需要过滤掉权限标识
	}

}
