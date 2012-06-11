package com.ufsoft.iufo.view;

import java.util.ArrayList;
import java.util.List;

import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;

import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.view.tree.IufoLazyTreeNodeModel;
import com.ufsoft.iufo.view.tree.MouseEventHandler;
import com.ufsoft.iufo.view.tree.TreeDataLoader;

public class RepNodeLoader implements TreeDataLoader {

	private Viewer viewer;

	private IufoLazyTreeNodeModel parentNodeModel;

	public RepNodeLoader(Viewer viewer, IufoLazyTreeNodeModel parentNodeModel) {
		this.viewer = viewer;
		this.parentNodeModel = parentNodeModel;
	}

	public IufoLazyTreeNodeModel[] loadData(Object[] params) {

		String strUnitId = (String) viewer.getContext().getAttribute(
				IUfoContextKey.CUR_UNIT_ID);
		String strModelId = (String) viewer.getContext().getAttribute(
					IUfoContextKey.MODEL_ID);
		 
		params = new String[] { strModelId,
				strUnitId, parentNodeModel.getId() };

		Object response = ActionHandler.exec(
				"nc.ui.iuforeport.rep.JTreeTableMngAction",
				"getReportNodesWithDirId", params);
		IResTreeObject[] result = (IResTreeObject[]) response;
		if (result == null) {
			return null;
		}

		MouseEventHandler mouseHandler = new RepNodeMouseEventHandler(viewer);
		
		List<IufoLazyTreeNodeModel> nodeModels = new ArrayList<IufoLazyTreeNodeModel>();

		for (IResTreeObject obj : result) {
			if (obj == null) {
				continue;
			}
			IufoLazyTreeNodeModel nodeModel = new IufoLazyTreeNodeModel();
			nodeModel.setLeaf(true);
			nodeModel.setId(ResMngToolKit.getVOIDByTreeObjectID(obj.getID()));// 去掉权限标识
			nodeModel.setLabel(obj.getLabel());
			nodeModel.setResRight(getFormatRight(obj.getRightType()));
			nodeModel.setMouseEventHandler(mouseHandler);
			nodeModels.add(nodeModel);
		}
		return nodeModels.toArray(new IufoLazyTreeNodeModel[nodeModels.size()]);
	}
	
	private int getFormatRight(int resRight){
		if(resRight==IResTreeObject.RIGHT_TYPE_RESMNG||resRight==IResTreeObject.RIGHT_TYPE_FULL||resRight==IResTreeObject.RIGHT_TYPE_MODIFY){
			return IUfoContextKey.RIGHT_FORMAT_WRITE;
		}else if(resRight==IResTreeObject.RIGHT_TYPE_PERSON){
			return IUfoContextKey.RIGHT_FORMAT_PERSONAL;
		}else if(resRight==IResTreeObject.RIGHT_TYPE_VIEW){
			return IUfoContextKey.RIGHT_FORMAT_READ;
		}else{
			return resRight;
		}
	}
}
