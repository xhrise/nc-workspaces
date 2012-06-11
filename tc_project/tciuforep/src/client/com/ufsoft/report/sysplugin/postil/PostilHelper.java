package com.ufsoft.report.sysplugin.postil;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;

/**
 * 批注各功能点的辅助类
 * 
 * @author zhaopq
 * @created at 2009-1-4,下午01:42:14
 * 
 */
public class PostilHelper {

	private PostilPlugin plugin;

	/**
	 * 这个Map保存了报表下已经显示的批注界面元素，保留这个集合是为了批量隐藏所有批注。
	 * 若某单元格的批注为显示状态，则这个批注一定在这个Map中
	 */
	private Map<CellPosition, PostilInternalFrame> postilFrames = new HashMap<CellPosition, PostilInternalFrame>(
			0);

	private boolean hasTakeAllPostils;

	/**当前是否全部显示了批注*/
	private boolean isShow;

	public PostilHelper(PostilPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * 状态复位，释放资源
	 * @create by zhaopq at 2009-1-8,上午09:52:43
	 */
	public void reset(){
		isShow = false;
		hasTakeAllPostils = false;
		for(PostilInternalFrame postil : postilFrames.values()){
			if(postil!=null){
				postil.dispose();
			}
		}
		postilFrames.clear();
	}

	private class PostilInternalFrameCreater {
		private PostilVO pvo;

		private CellPosition cellPos;

		private PostilInternalFrame frame;

		PostilInternalFrameCreater(CellPosition cellPos, PostilVO pvo) {
			this.pvo = pvo;
			this.cellPos = cellPos;
		}

		PostilInternalFrame createPostilInternalFrame() {
			Rectangle rect = plugin.getReport().getTable().getCells().getCellRect(cellPos,
					true);
			rect.x = rect.x + rect.width;
			CellsPane frameParent = plugin.getReport().getTable().getCells(rect);
			frame = new PostilInternalFrame(true);

			frame.setContent(pvo.getContent());
			
			frame.setLocation(frameParent, cellPos,pvo);
			frame.setSize(new Dimension(pvo.getSize().width,
					pvo.getSize().height));

			frameParent.add(frame);
			frame.setFrameParent(frameParent);
			frame.setCellPos(cellPos);

			frame.getTextArea().addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
					frame.setVisible(true);
					frame.repaint();
				}

				public void focusLost(FocusEvent e) {
					if (pvo != null) {
						pvo.setContent(frame.getContent());
						pvo.setSize(frame.getSize());
						if (!isShow && !pvo.isShowState()) {
							hidePostil(cellPos);
						}
						plugin.getReport().getCellsModel().setDirty(true);
					}
				}
			});
			postilFrames.put(cellPos, frame);
			return frame;
		}
	}

	private PostilInternalFrame getPostilFrame(CellPosition cellPos) {
		PostilInternalFrame frame = postilFrames.get(cellPos);
		if (frame == null) {
			PostilVO pvo = getPostilVO(cellPos);
			frame = new PostilInternalFrameCreater(cellPos, pvo)
					.createPostilInternalFrame();
		}
		return frame;
	}

	/**
	 * 编辑批注
	 * @create by zhaopq at 2009-1-7,上午09:53:57
	 *
	 * @param cellPos  批注所在的位置
	 */
	public void editPostil(CellPosition cellPos) {
		showPostil(cellPos, true).getTextArea().requestFocus();
	}

	/**
	 * 显示批注
	 * @create by zhaopq at 2009-1-6,上午09:54:39
	 *
	 * @param cellPos            批注所在的位置
	 * @param ignorePrivateState 是否忽略对批注私有状态位的处理
	 * @return   返回批注的显示UI对象
	 */
	public PostilInternalFrame showPostil(CellPosition cellPos,
			boolean ignorePrivateState) {
		PostilInternalFrame frame = getPostilFrame(cellPos);
		PostilVO pvo = getPostilVO(cellPos);
		if (!ignorePrivateState) {
			setPostilState(pvo, true);
		}
		frame.setVisible(true);
		frame.repaint();
		frame.revalidate();
		return frame;
	}

	private List<CellPosition> getPostilCellPositions() {
		List<CellPosition> result = new ArrayList<CellPosition>();
		for (int i = 0; i < plugin.getReport().getCellsModel().getRowNum(); i++) {
			for (int j = 0; j < plugin.getReport().getCellsModel().getColNum(); j++) {
				CellPosition cellPos = CellPosition.getInstance(i, j);
				PostilVO pvo = getPostilVO(cellPos);
				if (pvo != null) {
					result.add(cellPos);
				}
			}
		}
		return result;
	}

	private Collection<PostilInternalFrame> getAllPostilFrames() {
		if (!hasTakeAllPostils) {
			postilFrames.clear();
			for (CellPosition cellPos : getPostilCellPositions()) {
				PostilVO pvo = getPostilVO(cellPos);
				new PostilInternalFrameCreater(cellPos, pvo)
						.createPostilInternalFrame();

			}
			hasTakeAllPostils = true;
		}

		return postilFrames.values();
	}

	/**
	 * 插入批注
	 * @create by zhaopq at 2009-1-6,上午09:56:23
	 *
	 * @param cellPos   插入批注的位置
	 * @return    返回批注的显示UI对象
	 */
	public PostilInternalFrame insertPostil(CellPosition cellPos) {
		PostilVO pvo = new PostilVO("");
		pvo.setShowState(isShow);
		plugin.getReport().getCellsModel().setBsFormat(cellPos, PostilPlugin.EXT_FMT_POSTIL, pvo);

		return new PostilInternalFrameCreater(cellPos, pvo)
				.createPostilInternalFrame();
	}

	/**
	 * 删除当前选中的批注
	 * @create by zhaopq at 2009-1-6,上午09:57:37
	 */
	public void deleteAllSelectedPostils() {
		for (CellPosition cellPos : getSelectCellls()) {
			hidePostil(cellPos);
			plugin.getReport().getCellsModel().setBsFormat(cellPos, PostilPlugin.EXT_FMT_POSTIL, null);
		}
	}

	/**
	 * 隐藏批注
	 * @create by zhaopq at 2009-1-6,上午09:58:47
	 *
	 * @param cellPos   批注的位置
	 */
	public void hidePostil(CellPosition cellPos) {
		setPostilState(getPostilVO(cellPos), false);
		// 从内存中移除掉标注的frame
		PostilInternalFrame frame = postilFrames.get(cellPos);
		if (frame != null) {
			frame.setVisible(false);
			frame.dispose();
			postilFrames.remove(cellPos);
			frame.getFrameParent().remove(frame);
		}
	}

	/**
	 * 重新显示所有批注
	 * @create by zhaopq at 2009-1-6,上午09:59:43
	 */
	public void reShowAllPostils() {
		clearAllCachedPostilFrams(false);
		showAllPostils(false);
	}

	/**
	 * 显示所有批注
	 * @create by zhaopq at 2009-1-5,上午10:00:29
	 * @param ignorePrivateState  是否忽略对批注私有状态位的处理
	 */
	public void showAllPostils(boolean ignorePrivateState) {
		for (PostilInternalFrame frame : getAllPostilFrames()) {
			PostilVO pvo = getPostilVO(frame.getCellPos());

			if (!ignorePrivateState) {
				frame.setVisible(pvo.isShowState());
			} else {
				setPostilState(pvo, true);
				frame.setVisible(true);
			}

			frame.repaint();
			frame.revalidate();
		}
	}

	/**
	 * 隐藏所有批注
	 * @create by zhaopq at 2009-1-6,上午10:01:08
	 */
	public void hideAllPostils() {
		clearAllCachedPostilFrams(true);
	}

	private void clearAllCachedPostilFrams(boolean ignorePrivateState) {
		for (PostilInternalFrame frame : postilFrames.values()) {
			if (ignorePrivateState) {
				PostilVO pvo = getPostilVO(frame.getCellPos());
				setPostilState(pvo, false);
			}
			frame.setVisible(false);
			frame.dispose();
			frame.getFrameParent().remove(frame);
		}
		postilFrames.clear();
		hasTakeAllPostils = false;
	}

	private void setPostilState(PostilVO pvo, boolean state) {
		if(pvo!=null){
			pvo.setShowState(state);
		}
	}


	/**
	 * 单元格上是否含有批注
	 * @create by zhaopq at 2009-1-9,上午10:01:37
	 *
	 * @param cellPos   单元格位置
	 * @return  true 有批注  false 无批注
	 */
	public boolean hasPostilOnCell(CellPosition cellPos) {
		return getPostilVO(cellPos) != null;
	}


	private PostilVO getPostilVO(CellPosition cellPos) {
		return (PostilVO) plugin.getReport().getCellsModel().getBsFormat(cellPos,
				PostilPlugin.EXT_FMT_POSTIL);
	}

	/**
	 * 当前批注的总状态是否为显示
	 * 
	 * @create by zhaopq at 2009-1-5,下午07:52:27
	 * 
	 * @return true 显示 false 不显示
	 */
	public boolean isShow() {
		return isShow;
	}

	public boolean shouldBeShown(CellPosition cellPos) {
		PostilVO pvo = getPostilVO(cellPos);
		if(pvo!=null){
			return pvo.isShowState();
		}
		return false;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}


	/**
	 * 单元格内的批注是否为显示状态
	 * 
	 * @create by zhaopq at 2009-1-8,上午10:12:08
	 * 
	 * @param cellPos
	 * @return
	 */
	public boolean isCellPostilShow(CellPosition cellPos) {
		return postilFrames.get(cellPos) != null
				&& getPostilVO(cellPos).isShowState();
	}
	
	/**
	 * 得到选中的最后一个单元格的位置
	 * @create by zhaopq at 2009-1-8,下午03:06:14
	 *
	 * @return
	 */
	public CellPosition getSelectedLastCell(){
		CellPosition[] cellPosArray = getSelectCellls();
		return cellPosArray[cellPosArray.length-1];
	}

	private CellPosition[] getSelectCellls(){
		return plugin.getReport().getCellsModel().getSelectModel().getSelectedCells();
	}
	
	/**
	 * “删除批注”功能是否可用.
	 * <p>
	 * 当选中的单元格中至少有一个批注时，“删除批注”功能可用.
	 * 
	 * @create by zhaopq at 2009-1-8,下午01:49:21
	 * 
	 * @return true 可用  false 不可用
	 */
	public boolean isDeleteEnable() {
		for (CellPosition cellPos : getSelectCellls()) {
			PostilVO pvo = getPostilVO(cellPos);
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
	 * @return true 可用  false 不可用
	 */
	public boolean isInsertEnable() {
		for (CellPosition cellPos : getSelectCellls()) {
			PostilVO pvo = getPostilVO(cellPos);
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
	 * @return true 可用  false 不可用
	 */
	public boolean isEditeEnable() {
		CellPosition[] cellPosArray = getSelectCellls();
		return getPostilVO(cellPosArray[cellPosArray.length - 1]) != null;
	}

	/**
	 * “隐藏批注”功能是否可用.
	 * <p>
	 * 当选中的最后一个单元格有批注且批注被显示时，“隐藏批注”功能可用.
	 * 
	 * @create by zhaopq at 2009-1-8,下午01:53:03
	 * 
	 * @return true 可用  false 不可用
	 */
	public boolean isHideEnable() {
		CellPosition[] cellPosArray = getSelectCellls();
		return hasPostilOnCell(cellPosArray[cellPosArray.length - 1])
				&& isCellPostilShow(cellPosArray[cellPosArray.length - 1]);
	}

	/**
	 * “显示批注”功能是否可用.
	 * <p>
	 * 当选中的最后一个单元格有批注且批注被隐藏时，“显示批注”功能可用.
	 * 
	 * @create by zhaopq at 2009-1-8,下午01:53:07
	 * 
	 * @return true 可用  false 不可用
	 */
	public boolean isShowEnable() {
		CellPosition[] cellPosArray = getSelectCellls();
		return hasPostilOnCell(cellPosArray[cellPosArray.length - 1])
				&& !isCellPostilShow(cellPosArray[cellPosArray.length - 1]);
	}
	
}
