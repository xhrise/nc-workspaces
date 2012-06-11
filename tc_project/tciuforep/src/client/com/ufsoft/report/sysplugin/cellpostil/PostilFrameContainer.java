package com.ufsoft.report.sysplugin.cellpostil;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import com.ufsoft.report.sysplugin.postil.PostilInternalFrame;
import com.ufsoft.report.sysplugin.postil.PostilVO;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
/**
 * 
 * @author zhaopq
 * @created at 2009-4-29,����01:36:56
 *
 */
class PostilFrameContainer {

	private CellsPane cellsPane;

	/**
	 * ���Map������һ�ű������Ѿ���ʾ����ע����Ԫ�أ��������������Ϊ����������������ע��
	 * ��ĳ��Ԫ�����עΪ��ʾ״̬���������עһ�������Map��
	 */
	private Map<CellPosition, PostilInternalFrame> postilFrames = new HashMap<CellPosition, PostilInternalFrame>(
			0);

	private boolean hasTakeAllPostils;
	
	private CellPostilManager manager;

	PostilFrameContainer(CellPostilManager manager,CellsPane cellsPane) {
		this.manager = manager;
		this.cellsPane = cellsPane;
	}

	void destroy() {
		clearAllCachedPostilFrams(true);
	}

	void destroy(CellPosition cellPos) {
		PostilInternalFrame frame = postilFrames.get(cellPos);
		if (frame != null) {
			frame.setVisible(false);
			frame.dispose();
			postilFrames.remove(cellPos);
			frame.getFrameParent().remove(frame);
		}
	}
	
	/**
	 * ������ע
	 * @create by zhaopq at 2009-1-6,����09:58:47
	 *
	 * @param cellPos   ��ע��λ��
	 */
	void hidePostil(CellPosition cellPos) {
		setPostilState(getPostilVO(cellPos), false);
		destroy(cellPos);
	}
	

	boolean hasPostilFrame(CellPosition cellPos) {
		return postilFrames.get(cellPos) != null;
	}

	void clearAllCachedPostilFrams(boolean ignorePrivateState) {
		for (PostilInternalFrame frame : postilFrames.values()) {
			if (ignorePrivateState) {
				PostilVO pvo = getPostilVO(frame.getCellPos());
				setPostilState(pvo, false);
			}
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
	 * ��ʾ������ע
	 * 
	 * @create by zhaopq at 2009-1-5,����10:00:29
	 * @param ignorePrivateState
	 *            �Ƿ���Զ���ע˽��״̬λ�Ĵ���
	 */
	void showAllPostils(boolean ignorePrivateState) {
		for (final PostilInternalFrame frame : getAllPostilFrames()) {
			PostilVO pvo = getPostilVO(frame.getCellPos());

			if (!ignorePrivateState) {
				frame.setVisible(pvo.isShowState());
			} else {
				setPostilState(pvo, true);
				frame.setVisible(true);
			}
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {
					((JViewport)frame.getTextArea().getParent()).setViewPosition(new Point(0,0));
				}
			});
			frame.repaint();
			frame.revalidate();
		}
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
	PostilInternalFrame showPostil(CellPosition cellPos,
			boolean ignorePrivateState) {
		PostilVO pvo = getPostilVO(cellPos);
		if (pvo == null) {
			return null;
		}
		final PostilInternalFrame frame = createPostilFrameIfNull(cellPos);
		if (!ignorePrivateState) {
			setPostilState(pvo, true);
		}
		frame.setVisible(true);
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				((JViewport)frame.getTextArea().getParent()).setViewPosition(new Point(0,0));
			}
		});
		frame.repaint();
		frame.revalidate();
		return frame;
	}

	private PostilInternalFrame createPostilFrameIfNull(CellPosition cellPos) {
		PostilInternalFrame frame = postilFrames.get(cellPos);
		if (frame == null) {
			PostilVO pvo = getPostilVO(cellPos);
			frame = createPostilInternalFrame(pvo,cellPos);
		}
		return frame;
	}
	
	private Collection<PostilInternalFrame> getAllPostilFrames() {
		if (!hasTakeAllPostils) {
//			postilFrames.clear();
			for (CellPosition cellPos : getPostilCellPositions()) {
				createPostilFrameIfNull(cellPos);
			}
			hasTakeAllPostils = true;
		}

		return postilFrames.values();
	}
	
	private List<CellPosition> getPostilCellPositions() {
		List<CellPosition> result = new ArrayList<CellPosition>();
		for (int i = 0; i < cellsPane.getDataModel().getRowNum(); i++) {
			for (int j = 0; j < cellsPane.getDataModel().getColNum(); j++) {
				CellPosition cellPos = CellPosition.getInstance(i, j);
				PostilVO pvo = getPostilVO(cellPos);
				if (pvo != null) {
					result.add(cellPos);
				}
			}
		}
		return result;
	}
	
	private PostilVO getPostilVO(CellPosition cellPos) {
		return (PostilVO) cellsPane.getDataModel().getBsFormat(cellPos,
				CellPostilDefPlugin.EXT_FMT_POSTIL);
	}
	
	private PostilInternalFrame createPostilInternalFrame(final PostilVO pvo,
			final CellPosition cellPos) {
		Rectangle rect = cellsPane.getCellRect(cellPos, true);
		rect.x = rect.x + rect.width;
		final PostilInternalFrame frame = new PostilInternalFrame(
				manager.isPostilEditable());

		frame.setContent(pvo.getContent());

		frame.setLocation(cellsPane, cellPos, pvo);
		frame.setSize(new Dimension(pvo.getSize().width, pvo.getSize().height));

		cellsPane.add(frame);
		frame.setFrameParent(cellsPane);
		frame.setCellPos(cellPos);

		frame.getTextArea().addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
//				frame.setVisible(true);
//				frame.repaint();
			}

			public void focusLost(FocusEvent e) {
				if (pvo != null) {
					pvo.setContent(frame.getContent());
					pvo.setSize(frame.getSize());
					if (!manager.isStatusShow() && !pvo.isShowState()) {
						hidePostil(cellPos);
					}
					cellsPane.getDataModel().setDirty(true);
				}
			}
		});
		postilFrames.put(cellPos, frame);
		return frame;
	}
	
	
	/**
	 * ������ע
	 * @create by zhaopq at 2009-1-6,����09:56:23
	 *
	 * @param cellPos   ������ע��λ��
	 * @return    ������ע����ʾUI����
	 */
	PostilInternalFrame insertPostil(CellPosition cellPos) {
		PostilVO pvo = new PostilVO("");
		pvo.setShowState(manager.isStatusShow());
		cellsPane.getDataModel().setBsFormat(cellPos, CellPostilDefPlugin.EXT_FMT_POSTIL, pvo);
		return createPostilInternalFrame(pvo,cellPos);
	}
	
}
