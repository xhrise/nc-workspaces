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
 * ��ע�����ܵ�ĸ�����
 */
public class CellPostilManager {

	private IPlugin plugin = null;

	private boolean postilEditable;

	/**
	 * ÿ��CellsPane(һ��ҳǩ��Ӧһ��CellsPane)��Ӧһ����ע(Frame)���ϡ�
	 */
	private Map<CellsModel, PostilFrameContainer> postilFrameMap = new HashMap<CellsModel, PostilFrameContainer>();

	/** ��ǰ�Ƿ�ȫ����ʾ����ע */
	private boolean statusShow;

	CellPostilManager(IPlugin p, boolean postilEditable) {
		plugin = p;
		this.postilEditable = postilEditable;
	}

	/**
	 * ״̬��λ���ͷ���Դ
	 * 
	 * @create by zhaopq at 2009-1-8,����09:52:43
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
	 * �༭��ע
	 * 
	 * @create by zhaopq at 2009-1-7,����09:53:57
	 * 
	 * @param cellPos
	 *            ��ע���ڵ�λ��
	 */
	public void editPostil(CellsPane cellsPane, CellPosition cellPos) {
		showPostil(cellsPane, cellPos, true).getTextArea().requestFocus();
	}

	/**
	 * ��ʾ��ע
	 * 
	 * @create by zhaopq at 2009-1-6,����09:54:39
	 * 
	 * @param cellPos
	 *            ��ע���ڵ�λ��
	 * @param ignorePrivateState
	 *            �Ƿ���Զ���ע˽��״̬λ�Ĵ���
	 * @return ������ע����ʾUI����
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
	 * ������ע
	 * 
	 * @create by zhaopq at 2009-1-6,����09:56:23
	 * 
	 * @param cellPos
	 *            ������ע��λ��
	 * @return ������ע����ʾUI����
	 */
	public PostilInternalFrame insertPostil(CellsPane cellsPane,
			CellPosition cellPos) {
		return getPostilFrameContainer(cellsPane).insertPostil(cellPos);
	}

	/**
	 * ɾ����ǰѡ�е���ע
	 * 
	 * @create by zhaopq at 2009-1-6,����09:57:37
	 */
	public void deleteSelectedPostils(CellsModel cellsModel) {
		for (CellPosition cellPos : getSelectCellls(cellsModel)) {
			hidePostil(cellsModel, cellPos);
			cellsModel.setBsFormat(cellPos, CellPostilDefPlugin.EXT_FMT_POSTIL,
					null);
		}
	}

	/**
	 * ������ע
	 * 
	 * @create by zhaopq at 2009-1-6,����09:58:47
	 * 
	 * @param cellPos
	 *            ��ע��λ��
	 */
	public void hidePostil(CellsModel cellsModel, CellPosition cellPos) {
		// ���ڴ����Ƴ�����ע��frame
		PostilFrameContainer container = postilFrameMap.get(cellsModel);
		if (container == null) {
			return;
		}
		container.destroy(cellPos);
	}

	/**
	 * ������ʾ������ע
	 * 
	 * @create by zhaopq at 2009-1-6,����09:59:43
	 */
	public void reShowAllPostils() {
		clearAllCachedPostilFrams(false);
		showAllPostils(false);
	}

	/**
	 * ��ʾ������ע
	 * 
	 * @create by zhaopq at 2009-1-5,����10:00:29
	 * @param ignorePrivateState
	 *            �Ƿ���Զ���ע˽��״̬λ�Ĵ���
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
	 * ����������ע
	 * 
	 * @create by zhaopq at 2009-1-6,����10:01:08
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
	 * ��Ԫ�����Ƿ�����ע
	 * 
	 * @create by zhaopq at 2009-1-9,����10:01:37
	 * 
	 * @param cellPos
	 *            ��Ԫ��λ��
	 * @return true ����ע false ����ע
	 */
	public boolean hasPostilOnCell(CellsModel cellsModel, CellPosition cellPos) {
		return getPostilVO(cellsModel, cellPos) != null;
	}

	private PostilVO getPostilVO(CellsModel cellsModel, CellPosition cellPos) {
		return (PostilVO) cellsModel.getBsFormat(cellPos,
				CellPostilDefPlugin.EXT_FMT_POSTIL);
	}

	/**
	 * ��ǰ��ע����״̬�Ƿ�Ϊ��ʾ
	 * 
	 * @create by zhaopq at 2009-1-5,����07:52:27
	 * 
	 * @return true ��ʾ false ����ʾ
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
	 * ��Ԫ���ڵ���ע�Ƿ�Ϊ��ʾ״̬
	 * 
	 * @create by zhaopq at 2009-1-8,����10:12:08
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
	 * �õ�ѡ�е����һ����Ԫ���λ��
	 * 
	 * @create by zhaopq at 2009-1-8,����03:06:14
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
	 * ��ɾ����ע�������Ƿ����.
	 * <p>
	 * ��ѡ�еĵ�Ԫ����������һ����עʱ����ɾ����ע�����ܿ���.
	 * 
	 * @create by zhaopq at 2009-1-8,����01:49:21
	 * 
	 * @return true ���� false ������
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
	 * ��������ע�������Ƿ����.
	 * <p>
	 * ��ѡ�еĵ�Ԫ����������һ���ո�ʱ����������ע�����ܿ���.
	 * 
	 * @create by zhaopq at 2009-1-8,����01:51:35
	 * 
	 * @return true ���� false ������
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
	 * ���༭��ע�������Ƿ����.
	 * <p>
	 * ��ѡ�е����һ����Ԫ������עʱ�����༭��ע�����ܿ��ã��༭�����Ƶ�����ע��.
	 * 
	 * @create by zhaopq at 2009-1-8,����01:52:58
	 * 
	 * @return true ���� false ������
	 */
	public boolean isEditeEnable(CellsModel cellsModel) {
		if (cellsModel == null) {
			return false;
		}
		CellPosition[] cellPosArray = getSelectCellls(cellsModel);
		return getPostilVO(cellsModel, cellPosArray[cellPosArray.length - 1]) != null;
	}

	/**
	 * ��������ע�������Ƿ����.
	 * <p>
	 * ��ѡ�е����һ����Ԫ������ע����ע����ʾʱ����������ע�����ܿ���.
	 * 
	 * @create by zhaopq at 2009-1-8,����01:53:03
	 * 
	 * @return true ���� false ������
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
	 * ����ʾ��ע�������Ƿ����.
	 * <p>
	 * ��ѡ�е����һ����Ԫ������ע����ע������ʱ������ʾ��ע�����ܿ���.
	 * 
	 * @create by zhaopq at 2009-1-8,����01:53:07
	 * 
	 * @return true ���� false ������
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
