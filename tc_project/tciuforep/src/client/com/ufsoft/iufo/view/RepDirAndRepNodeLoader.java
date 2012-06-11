package com.ufsoft.iufo.view;

import java.util.ArrayList;
import java.util.List;

import nc.util.iufo.iufo.resmng.IIUFOResMngConsants;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;

import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.i18n.MultiLangUtil;
import com.ufsoft.iufo.view.tree.IufoLazyTreeNodeModel;
import com.ufsoft.iufo.view.tree.MouseEventHandler;
import com.ufsoft.iufo.view.tree.TreeDataLoader;

public class RepDirAndRepNodeLoader implements TreeDataLoader {

	private Viewer viewer;

	private String selectedRepId;

	public RepDirAndRepNodeLoader(Viewer viewer) {
		this.viewer = viewer;
	}

	public RepDirAndRepNodeLoader(Viewer viewer, String selectedRepId) {
		this.viewer = viewer;
		this.selectedRepId = selectedRepId;
	}

	public IufoLazyTreeNodeModel[] loadData(Object[] params) {
		String strUserId = (String) viewer.getContext().getAttribute(
				IUfoContextKey.CUR_USER_ID);
		String strUnitId = (String) viewer.getContext().getAttribute(
				IUfoContextKey.CUR_UNIT_ID);
        String strModelId = (String) viewer.getContext().getAttribute(
				IUfoContextKey.MODEL_ID);
        
		params = new String[] { MultiLangUtil.getLanguage(),
				strModelId, strUserId, strUnitId,
				selectedRepId };

		Object response = ActionHandler.exec(
				"nc.ui.iuforeport.rep.JTreeTableMngAction",
				"getTreeDirAndNode", params);
		IResTreeObject[] result = (IResTreeObject[]) response;

		if (result == null || result.length == 0) {
			return null;
		}
		List<IufoLazyTreeNodeModel> nodeModels = new ArrayList<IufoLazyTreeNodeModel>();

		MouseEventHandler mouseHandler = new RepNodeMouseEventHandler(viewer);

		for (IResTreeObject obj : result) {
			if (obj == null ) {
				continue;
			}
			IufoLazyTreeNodeModel nodeModel = new IufoLazyTreeNodeModel();
			nodeModel.setLeaf(isLeaf(obj));
			nodeModel.setId(ResMngToolKit.getVOIDByTreeObjectID(obj.getID()));//去掉权限标识
			nodeModel.setParentId(ResMngToolKit.getVOIDByTreeObjectID(obj.getParentID()));
			nodeModel.setLabel(obj.getLabel());
			nodeModel.setResRight(getFormatRight(obj.getRightType()));
			if(!ResMngToolKit.isSharedRootTreeObj(obj.getID())){
				if (!nodeModel.isLeaf()) {
					nodeModel.setDataLoader(new RepNodeLoader(viewer, nodeModel));
				} else {
					nodeModel.setMouseEventHandler(mouseHandler);
				}
			}
		
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

	private boolean isLeaf(IResTreeObject obj) {
		return obj.getType() == IResTreeObject.OBJECT_TYPE_FILE;
	}

}
