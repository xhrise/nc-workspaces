package com.ufsoft.report.sysplugin.cellpostil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufida.zior.plugin.IPlugin;
import com.ufida.zior.view.Viewer;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.postil.PostilInternalFrame;
import com.ufsoft.report.sysplugin.postil.PostilVO;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;

/**
 * 批注各功能点的辅助类
 */
public class CellPostilManager {

	private IPlugin plugin = null;

	private boolean postilEditable;

	/**
	 * 每个CellsPane(一个页签对应一个CellsPane)对应一个批注(Frame)集合。
	 */
	private Map<CellsModel, PostilFrameContainer> postilFrameMap = new HashMap<CellsModel, PostilFrameContainer>();

	/** 当前是否全部显示了批注 */
	private boolean statusShow;

	CellPostilManager(IPlugin p, boolean postilEditable) {
		plugin = p;
		this.postilEditable = postilEditable;
	}

	/**
	 * 状态复位，释放资源
	 * 
	 * @create by zhaopq at 2009-1-8,上午09:52:43
	 */
	public void reset() {
		statusShow = false;
		for (PostilFrameContainer container : postilFrameMap.values()) {
			if (container != null) {
				container.destroy();
			}
		}
		postilFrameMap.clear();
	}

	public void destroy(CellsModel cellsModel) {
		PostilFrameContainer container = postilFrameMap.get(cellsModel);
		if (container == null) {
			return;
		}
		container.destroy();
		postilFrameMap.remove(cellsModel);
	}

	public void destroy(CellsPane cellsPane) {
		destroy(cellsPane.getDataModel());
	}

	/**
	 * 编辑批注
	 * 
	 * @create by zhaopq at 2009-1-7,上午09:53:57
	 * 
	 * @param cellPos
	 *            批注所在的位置
	 */
	public void editPostil(CellsPane cellsPane, CellPosition cellPos) {
		showPostil(cellsPane, cellPos, true).getTextArea().requestFocus();
	}

	/**
	 * 显示批注
	 * 
	 * @create by zhaopq at 2009-1-6,上午09:54:39
	 * 
	 * @param cellPos
	 *            批注所在的位置
	 * @param ignorePrivateState
	 *            是否忽略对批注私有状态位的处理
	 * @return 返回批注的显示UI对象
	 */
	public PostilInternalFrame showPostil(CellsPane cellsPane,
			CellPosition cellPos, boolean ignorePrivateState) {
		return getPostilFrameContainer(cellsPane).showPostil(cellPos,
				ignorePrivateState);
	}

	private PostilFrameContainer getPostilFrameContainer(CellsPane cellsPane) {
		PostilFrameContainer container = postilFrameMap.get(cellsPane
				.getDataModel());
		if (container == null) {
			container = new PostilFrameContainer(this, cellsPane);
			postilFrameMap.put(cellsPane.getDataModel(), container);
		}
		return container;
	}

	/**
	 * 插入批注
	 * 
	 * @create by zhaopq at 2009-1-6,上午09:56:23
	 * 
	 * @param cellPos
	 *            插入批注的位置
	 * @return 返回批注的显示UI对象
	 */
	public PostilInternalFrame insertPostil(CellsPane cellsPane,
			CellPosition cellPos) {
		return getPostilFrameContainer(cellsPane).insertPostil(cellPos);
	}

	/**
	 * 删除当前选中的批注
	 * 
	 * @create by zhaopq at 2009-1-6,上午09:57:37
	 */
	public void deleteSelectedPostils(CellsModel cellsModel) {
		for (CellPosition cellPos : getSelectCellls(cellsModel)) {
			hidePostil(cellsModel, cellPos);
			cellsModel.setBsFormat(cellPos, CellPostilDefPlugin.EXT_FMT_POSTIL,
					null);
		}
	}

	/**
	 * 隐藏批注
	 * 
	 * @create by zhaopq at 2009-1-6,上午09:58:47
	 * 
	 * @param cellPos
	 *            批注的位置
	 */
	public void hidePostil(CellsModel cellsModel, CellPosition cellPos) {
		// 从内存中移除掉标注的frame
		PostilFrameContainer container = postilFrameMap.get(cellsModel);
		if (container == null) {
			return;
		}
		container.destroy(cellPos);
	}

	/**
	 * 重新显示所有批注
	 * 
	 * @create by zhaopq at 2009-1-6,上午09:59:43
	 */
	public void reShowAllPostils() {
		clearAllCachedPostilFrams(false);
		showAllPostils(false);
	}

	/**
	 * 显示所有批注
	 * 
	 * @create by zhaopq at 2009-1-5,上午10:00:29
	 * @param ignorePrivateState
	 *            是否忽略对批注私有状态位的处理
	 */
	public void showAllPostils(boolean ignorePrivateState) {
		for (CellsPane cellsPane : getAllOpenedCellsPane()) {
			showAllPostils(ignorePrivateState, cellsPane);
		}
	}

	public void showAllPostils(boolean ignorePrivateState, CellsPane cellsPane) {
		getPostilFrameContainer(cellsPane).showAllPostils(ignorePrivateState);
	}

	private List<CellsPane> getAllOpenedCellsPane() {
		List<CellsPane> result = new ArrayList<CellsPane>();
		for (Viewer viewer : plugin.getMainboard().getAllViews()) {
			if (viewer instanceof ReportDesigner) {
				result.add(((ReportDesigner) viewer).getCellsPane());
			}
		}
		return result;
	}

	/**
	 * 隐藏所有批注
	 * 
	 * @create by zhaopq at 2009-1-6,上午10:01:08
	 */
	public void hideAllPostils() {
		clearAllCachedPostilFrams(true);
	}

	private void clearAllCachedPostilFrams(boolean ignorePrivateState) {
		for (PostilFrameContainer container : postilFrameMap.values()) {
			if (container != null) {
				container.clearAllCachedPostilFrams(ignorePrivateState);
			}
		}
		postilFrameMap.clear();
	}

	/**
	 * 单元格上是否含有批注
	 * 
	 * @create by zhaopq at 2009-1-9,上午10:01:37
	 * 
	 * @param cellPos
	 *            单元格位置
	 * @return true 有批注 false 无批注
	 */
	public boolean hasPostilOnCell(CellsModel cellsModel, CellPosition cellPos) {
		return getPostilVO(cellsModel, cellPos) != null;
	}

	private PostilVO getPostilVO(CellsModel cellsModel, CellPosition cellPos) {
		return (PostilVO) cellsModel.getBsFormat(cellPos,
				CellPostilDefPlugin.EXT_FMT_POSTIL);
	}

	/**
	 * 当前批注的总状态是否为显示
	 * 
	 * @create by zhaopq at 2009-1-5,下午07:52:27
	 * 
	 * @return true 显示 false 不显示
	 */
	public boolean isStatusShow() {
		return statusShow;
	}

	public boolean shouldBeShown(CellsModel cellsModel, CellPosition cellPos) {
		PostilVO pvo = getPostilVO(cellsModel, cellPos);
		if (pvo != null) {
			return pvo.isShowState();
		}
		return false;
	}

	public void setStatusShow(boolean isShow) {
		this.statusShow = isShow;
	}

	/**
	 * 单元格内的批注是否为显示状态
	 * 
	 * @create by zhaopq at 2009-1-8,上午10:12:08
	 * 
	 * @param cellPos
	 * @return
	 */
	public boolean isCellPostilShow(CellsPane cellsPane, CellPosition cellPos) {

		return hasPostilFrame(cellsPane, cellPos)
				&& getPostilVO(cellsPane.getDataModel(), cellPos).isShowState();
	}

	private boolean hasPostilFrame(CellsPane cellsPane, CellPosition cellPos) {
		PostilFrameContainer container = postilFrameMap.get(cellsPane
				.getDataModel());
		return container != null && container.hasPostilFrame(cellPos);
	}

	/**
	 * 得到选中的最后一个单元格的位置
	 * 
	 * @create by zhaopq at 2009-1-8,下午03:06:14
	 * 
	 * @return
	 */
	public CellPosition getSelectedLastCell(CellsModel cellsModel) {
		CellPosition[] cellPosArray = getSelectCellls(cellsModel);
		return cellPosArray[cellPosArray.length - 1];
	}

	private CellPosition[] getSelectCellls(CellsModel cellsModel) {
		return cellsModel.getSelectModel().getSelectedCells();
	}

	/**
	 * “删除批注”功能是否可用.
	 * <p>
	 * 当选中的单元格中至少有一个批注时，“删除批注”功能可用.
	 * 
	 * @create by zhaopq at 2009-1-8,下午01:49:21
	 * 
	 * @return true 可用 false 不可用
	 */
	public boolean isDeleteEnable(CellsModel cellsModel) {
		if (cellsModel == null) {
			return false;
		}
		for (CellPosition cellPos : getSelectCellls(cellsModel)) {
			PostilVO pvo = getPostilVO(cellsModel, cellPos);
			if (pvo != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * “插入批注”功能是否可用.
	 * <p>
	 * 当选中的单元格中至少有一个空格时，“插入批注”功能可用.
	 * 
	 * @create by zhaopq at 2009-1-8,下午01:51:35
	 * 
	 * @return true 可用 false 不可用
	 */
	public boolean isInsertEnable(CellsModel cellsModel) {
		if (cellsModel == null) {
			return false;
		}
		for (CellPosition cellPos : getSelectCellls(cellsModel)) {
			PostilVO pvo = getPostilVO(cellsModel, cellPos);
			if (pvo == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * “编辑批注”功能是否可用.
	 * <p>
	 * 当选中的最后一个单元格有批注时，“编辑批注”功能可用，编辑焦点移到该批注上.
	 * 
	 * @create by zhaopq at 2009-1-8,下午01:52:58
	 * 
	 * @return true 可用 false 不可用
	 */
	public boolean isEditeEnable(CellsModel cellsModel) {
		if (cellsModel == null) {
			return false;
		}
		CellPosition[] cellPosArray = getSelectCellls(cellsModel);
		return getPostilVO(cellsModel, cellPosArray[cellPosArray.length - 1]) != null;
	}

	/**
	 * “隐藏批注”功能是否可用.
	 * <p>
	 * 当选中的最后一个单元格有批注且批注被显示时，“隐藏批注”功能可用.
	 * 
	 * @create by zhaopq at 2009-1-8,下午01:53:03
	 * 
	 * @return true 可用 false 不可用
	 */
	public boolean isHideEnable(CellsPane cellsPane) {
		if (cellsPane == null || cellsPane.getDataModel()==null) {
			return false;
		}
		CellPosition[] cellPosArray = getSelectCellls(cellsPane.getDataModel());
		return hasPostilOnCell(cellsPane.getDataModel(),
				cellPosArray[cellPosArray.length - 1])
				&& isCellPostilShow(cellsPane,
						cellPosArray[cellPosArray.length - 1]);
	}

	/**
	 * “显示批注”功能是否可用.
	 * <p>
	 * 当选中的最后一个单元格有批注且批注被隐藏时，“显示批注”功能可用.
	 * 
	 * @create by zhaopq at 2009-1-8,下午01:53:07
	 * 
	 * @return true 可用 false 不可用
	 */
	public boolean isShowEnable(CellsPane cellsPane) {
		if (cellsPane == null || cellsPane.getDataModel()==null) {
			return false;
		}
		CellPosition[] cellPosArray = getSelectCellls(cellsPane.getDataModel());
		return hasPostilOnCell(cellsPane.getDataModel(),
				cellPosArray[cellPosArray.length - 1])
				&& !isCellPostilShow(cellsPane,
						cellPosArray[cellPosArray.length - 1]);
	}

	boolean isPostilEditable() {
		return postilEditable;
	}

	void setPostilEditable(boolean postilEditable) {
		this.postilEditable = postilEditable;
	}
}
