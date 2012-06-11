package com.ufida.report.spreedsheet.applet;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSource;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelVO;

import com.ufida.report.rep.model.MetaDataVOSelection;
import com.ufida.report.rep.model.QueryModelTreeDragSource;

public class SpreadQueryTreeDragSource extends QueryModelTreeDragSource {
	public SpreadQueryTreeDragSource(JTree tree) {
		super(tree);
	}

	public void dragGestureRecognized(DragGestureEvent dge) {
		DefaultMutableTreeNode dragNode = (DefaultMutableTreeNode) getTree().getSelectionPath().getLastPathComponent();
		if (dragNode.getUserObject() == null)
			return;
		if (dragNode.getUserObject() instanceof MetaDataVO) {
			MetaDataVO value = (MetaDataVO) dragNode.getUserObject();
			if (value.getDimflag())// 只支持指标字段的拖拽
				return;
			DefaultMutableTreeNode queryNode = (DefaultMutableTreeNode) dragNode.getParent();
			QueryModelVO queryModelVO = (QueryModelVO) queryNode.getUserObject();
			MetaDataVOSelection flavor = new MetaDataVOSelection(queryModelVO, value, null);
			dge.startDrag(DragSource.DefaultCopyDrop, flavor, this);
		} else if (dragNode.getUserObject() instanceof QueryModelVO) {
			QueryModelVO queryModelVO = (QueryModelVO) dragNode.getUserObject();
			MetaDataVOSelection flavor = new MetaDataVOSelection(queryModelVO, null, null);
			dge.startDrag(DragSource.DefaultCopyDrop, flavor, this);
		}
	}

}
