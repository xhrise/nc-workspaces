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
 * ��ע�����ܵ�ĸ�����
 * 
 * @author zhaopq
 * @created at 2009-1-4,����01:42:14
 * 
 */
public class PostilHelper {

	private PostilPlugin plugin;

	/**
	 * ���Map�����˱������Ѿ���ʾ����ע����Ԫ�أ��������������Ϊ����������������ע��
	 * ��ĳ��Ԫ�����עΪ��ʾ״̬���������עһ�������Map��
	 */
	private Map<CellPosition, PostilInternalFrame> postilFrames = new HashMap<CellPosition, PostilInternalFrame>(
			0);

	private boolean hasTakeAllPostils;

	/**��ǰ�Ƿ�ȫ����ʾ����ע*/
	private boolean isShow;

	public PostilHelper(PostilPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * ״̬��λ���ͷ���Դ
	 * @create by zhaopq at 2009-1-8,����09:52:43
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
	 * �༭��ע
	 * @create by zhaopq at 2009-1-7,����09:53:57
	 *
	 * @param cellPos  ��ע���ڵ�λ��
	 */
	public void editPostil(CellPosition cellPos) {
		showPostil(cellPos, true).getTextArea().requestFocus();
	}

	/**
	 * ��ʾ��ע
	 * @create by zhaopq at 2009-1-6,����09:54:39
	 *
	 * @param cellPos            ��ע���ڵ�λ��
	 * @param ignorePrivateState �Ƿ���Զ���ע˽��״̬λ�Ĵ���
	 * @return   ������ע����ʾUI����
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
	 * ������ע
	 * @create by zhaopq at 2009-1-6,����09:56:23
	 *
	 * @param cellPos   ������ע��λ��
	 * @return    ������ע����ʾUI����
	 */
	public PostilInternalFrame insertPostil(CellPosition cellPos) {
		PostilVO pvo = new PostilVO("");
		pvo.setShowState(isShow);
		plugin.getReport().getCellsModel().setBsFormat(cellPos, PostilPlugin.EXT_FMT_POSTIL, pvo);

		return new PostilInternalFrameCreater(cellPos, pvo)
				.createPostilInternalFrame();
	}

	/**
	 * ɾ����ǰѡ�е���ע
	 * @create by zhaopq at 2009-1-6,����09:57:37
	 */
	public void deleteAllSelectedPostils() {
		for (CellPosition cellPos : getSelectCellls()) {
			hidePostil(cellPos);
			plugin.getReport().getCellsModel().setBsFormat(cellPos, PostilPlugin.EXT_FMT_POSTIL, null);
		}
	}

	/**
	 * ������ע
	 * @create by zhaopq at 2009-1-6,����09:58:47
	 *
	 * @param cellPos   ��ע��λ��
	 */
	public void hidePostil(CellPosition cellPos) {
		setPostilState(getPostilVO(cellPos), false);
		// ���ڴ����Ƴ�����ע��frame
		PostilInternalFrame frame = postilFrames.get(cellPos);
		if (frame != null) {
			frame.setVisible(false);
			frame.dispose();
			postilFrames.remove(cellPos);
			frame.getFrameParent().remove(frame);
		}
	}

	/**
	 * ������ʾ������ע
	 * @create by zhaopq at 2009-1-6,����09:59:43
	 */
	public void reShowAllPostils() {
		clearAllCachedPostilFrams(false);
		showAllPostils(false);
	}

	/**
	 * ��ʾ������ע
	 * @create by zhaopq at 2009-1-5,����10:00:29
	 * @param ignorePrivateState  �Ƿ���Զ���ע˽��״̬λ�Ĵ���
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
	 * ����������ע
	 * @create by zhaopq at 2009-1-6,����10:01:08
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
	 * ��Ԫ�����Ƿ�����ע
	 * @create by zhaopq at 2009-1-9,����10:01:37
	 *
	 * @param cellPos   ��Ԫ��λ��
	 * @return  true ����ע  false ����ע
	 */
	public boolean hasPostilOnCell(CellPosition cellPos) {
		return getPostilVO(cellPos) != null;
	}


	private PostilVO getPostilVO(CellPosition cellPos) {
		return (PostilVO) plugin.getReport().getCellsModel().getBsFormat(cellPos,
				PostilPlugin.EXT_FMT_POSTIL);
	}

	/**
	 * ��ǰ��ע����״̬�Ƿ�Ϊ��ʾ
	 * 
	 * @create by zhaopq at 2009-1-5,����07:52:27
	 * 
	 * @return true ��ʾ false ����ʾ
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
	 * ��Ԫ���ڵ���ע�Ƿ�Ϊ��ʾ״̬
	 * 
	 * @create by zhaopq at 2009-1-8,����10:12:08
	 * 
	 * @param cellPos
	 * @return
	 */
	public boolean isCellPostilShow(CellPosition cellPos) {
		return postilFrames.get(cellPos) != null
				&& getPostilVO(cellPos).isShowState();
	}
	
	/**
	 * �õ�ѡ�е����һ����Ԫ���λ��
	 * @create by zhaopq at 2009-1-8,����03:06:14
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
	 * ��ɾ����ע�������Ƿ����.
	 * <p>
	 * ��ѡ�еĵ�Ԫ����������һ����עʱ����ɾ����ע�����ܿ���.
	 * 
	 * @create by zhaopq at 2009-1-8,����01:49:21
	 * 
	 * @return true ����  false ������
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
	 * ��������ע�������Ƿ����.
	 * <p>
	 * ��ѡ�еĵ�Ԫ����������һ���ո�ʱ����������ע�����ܿ���.
	 * 
	 * @create by zhaopq at 2009-1-8,����01:51:35
	 * 
	 * @return true ����  false ������
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
	 * ���༭��ע�������Ƿ����.
	 * <p>
	 * ��ѡ�е����һ����Ԫ������עʱ�����༭��ע�����ܿ��ã��༭�����Ƶ�����ע��.
	 * 
	 * @create by zhaopq at 2009-1-8,����01:52:58
	 * 
	 * @return true ����  false ������
	 */
	public boolean isEditeEnable() {
		CellPosition[] cellPosArray = getSelectCellls();
		return getPostilVO(cellPosArray[cellPosArray.length - 1]) != null;
	}

	/**
	 * ��������ע�������Ƿ����.
	 * <p>
	 * ��ѡ�е����һ����Ԫ������ע����ע����ʾʱ����������ע�����ܿ���.
	 * 
	 * @create by zhaopq at 2009-1-8,����01:53:03
	 * 
	 * @return true ����  false ������
	 */
	public boolean isHideEnable() {
		CellPosition[] cellPosArray = getSelectCellls();
		return hasPostilOnCell(cellPosArray[cellPosArray.length - 1])
				&& isCellPostilShow(cellPosArray[cellPosArray.length - 1]);
	}

	/**
	 * ����ʾ��ע�������Ƿ����.
	 * <p>
	 * ��ѡ�е����һ����Ԫ������ע����ע������ʱ������ʾ��ע�����ܿ���.
	 * 
	 * @create by zhaopq at 2009-1-8,����01:53:07
	 * 
	 * @return true ����  false ������
	 */
	public boolean isShowEnable() {
		CellPosition[] cellPosArray = getSelectCellls();
		return hasPostilOnCell(cellPosArray[cellPosArray.length - 1])
				&& !isCellPostilShow(cellPosArray[cellPosArray.length - 1]);
	}
	
}
