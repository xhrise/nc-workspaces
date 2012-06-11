package com.ufsoft.table.re;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;

/**
 * <pre>
 * </pre>
 * 
 * ����,���ƺ͸�ʽˢʱ��ѡ�����򶯻�Ч���Ļ�����
 * 
 * @author �����
 * @version Create on 2008-4-1
 */
public class BorderPlayRender implements SheetCellRenderer {
	/**
	 * ���廭�ʣ������������ĳ����Լ�����֮��ľ���
	 */
	private final int LINE_WINDT = 6;
	/**
	 * ���廭�ʣ����ߵ����������˵ĵ�
	 */
	private final int DOT_WIDTH = 0;
	/**
	 * ���ߵ�������
	 */
	private final int BORDER_BOUND = 0;
	/**
	 * �Ƿ����˸��Ʋ��������ݴ��жϣ��Ǵ�ѡ��ģ���л��ѡ�����򣬻���ֱ�Ӵӿ��������ȡ�Ѿ����������
	 */
	private boolean b_IsCopy = false;
	/**
	 * �����ı�����
	 */
	private CellsPane m_CellsPane;
	/**
	 * ���������˶����߳�
	 */
	private Thread thread = null;
	/**
	 * �����Ƿ�������
	 */
	private boolean b_isPlay = false;
	/**
	 * ���ڻ��Ƶ�����
	 */
	private AreaPosition areaPosition = null;
	/**
	 * �̼߳��ʱ��
	 */
	private final long timer = 100;
	/**
	 * �ϱ߿��֡
	 */
	private int iTopBorderFrame = 0;
	/**
	 * ��߿��֡
	 */
	private int iLeftBorderFrame = 0;
	/**
	 * �ұ߿��֡
	 */
	private int iRightBorderFrame = 0;
	/**
	 * �±߿��֡
	 */
	private int iBottomBorderFrame = 0;

	public BorderPlayRender() {
//		if (thread == null) {
//			thread = new Thread(new playRunnable());
//		}
	}

	/**
	 * �����Ŀ�ʼ
	 * 
	 * @param
	 * @return
	 */
	private void startPlay() {
		if (!isAlive()) {
			thread = new Thread(new playRunnable());
			threadStart();
		}
		setCopyState(false);
		if (!isPlay()) {
			setPlay(true);
			threadNotify();
		} else {
			setPlay(false);
			threadNotify();

		}

	}

	/**
	 * ���ö���״̬
	 * 
	 * @param boolean
	 *            b_isPlay��flase:ֹͣ����
	 * @return
	 */
	private void stopPlay() {
		if (m_CellsPane == null) {
			return;
		}
		setPlay(false);
		setCells(null);
	}

	/**
	 * �߿򶯻��Ļ��Ʒ���
	 * 
	 * @param
	 * @return
	 */
	private void borderMove() {
		if (m_CellsPane == null) {
			throw new IllegalArgumentException();
		}
		Graphics g = m_CellsPane.getGraphics();
		paintPlayBorder(g);
	}

	/**
	 * ���ѽ��̣�ʹ������������
	 * 
	 * @param
	 * @return
	 */
	private void threadNotify() {
		setPlay(true);
		if (thread != null) {
			synchronized (thread) {
				thread.notify();
			}
		}
	}

	/**
	 * �ж��߳��Ƿ��ڴ��״̬
	 * 
	 * @param
	 * @return
	 */
	private boolean isAlive() {
		if (thread == null) {
			return false;
		}
		return thread.isAlive();
	}

	/**
	 * ���ѽ��̣�ʹ������������
	 * 
	 * @param
	 * @return
	 */
	private void threadStart() {
		if (thread == null) {
			return;
		}
		thread.start();
		setPlay(true);
	}

	/**
	 * ��ÿ����ϱ߿���Ƶ�֡
	 * 
	 * @param
	 * @return int iTopBorderFrame
	 */
	private int getTopBorderFrame() {
		return iTopBorderFrame;
	}

	/**
	 * ���ÿ����ϱ߿���Ƶ�֡
	 * 
	 * @param int
	 *            iTopBorderFrame
	 * @return
	 */
	private void setTopBorderFrame(int iTopBorderFrame) {
		this.iTopBorderFrame = iTopBorderFrame;
	}

	/**
	 * ��ÿ�����߿���Ƶ�֡
	 * 
	 * @param
	 * @return int iLeftBorderFrame
	 */
	private int getLeftBorderFrame() {
		return iLeftBorderFrame;
	}

	/**
	 * ���ÿ�����߿���Ƶ�֡
	 * 
	 * @param int
	 *            iLeftBorderFrame
	 * @return
	 */
	private void setLeftBorderFrame(int iLeftBorderFrame) {
		this.iLeftBorderFrame = iLeftBorderFrame;
	}

	/**
	 * ��ÿ����ұ߿���Ƶ�֡
	 * 
	 * @param
	 * @return int iRightBorderFrame
	 */
	private int getRightBorderFrame() {
		return iRightBorderFrame;
	}

	/**
	 * ���ÿ����ұ߿���Ƶ�֡
	 * 
	 * @param int
	 *            iRightBorderFrame
	 * @return
	 */
	private void setRightBorderFrame(int iRightBorderFrame) {
		this.iRightBorderFrame = iRightBorderFrame;
	}

	/**
	 * ��ÿ��Ƶױ߿���Ƶ�֡
	 * 
	 * @param
	 * @return int iBottomBorderFrame
	 */
	private int getBottomBorderFrame() {
		return iBottomBorderFrame;
	}

	/**
	 * ���ÿ��Ƶױ߿���Ƶ�֡
	 * 
	 * @param int
	 *            iBottomBorderFrame
	 * @return
	 */
	private void setBottomBorderFrame(int iBottomBorderFrame) {
		this.iBottomBorderFrame = iBottomBorderFrame;
	}

	/**
	 * ������Ƶ������Ա�����������ط�ʱ�����ڻ��ƴ�����ı߿�
	 * 
	 * @param
	 * @return AreaPosition areaPosition
	 */
	private AreaPosition getAreaPosition() {
		return areaPosition;
	}

	/**
	 * ��ñ��������
	 * 
	 * @param AreaPosition
	 *            areaPosition
	 * @return
	 */
	private void setAreaPosition(AreaPosition areaPosition) {
		this.areaPosition = areaPosition;
	}

	/**
	 * ����״̬���Ƿ����˸��Ʋ��������ݴ��жϣ��Ǵ�ѡ��ģ���л��ѡ�����򣬻���ֱ�Ӵӿ��������ȡ�Ѿ����������
	 * 
	 * @param AreaPosition
	 *            areaPosition
	 * @return
	 */
	private void setCopyState(boolean b_IsCopy) {
		this.b_IsCopy = b_IsCopy;
	}

	private boolean isCopyState() {
		return b_IsCopy;
	}

	/**
	 * �����Ƿ�������
	 * 
	 * @param
	 * @return boolean
	 */
	private boolean isPlay() {
		return b_isPlay;
	}

	private void setPlay(boolean b_isPlay) {
		this.b_isPlay = b_isPlay;
		if (!b_isPlay) {
			Rectangle cellRect = m_CellsPane.getCellRect(areaPosition, false); 
			m_CellsPane.repaint(cellRect);
		}else{
			getCells().requestFocus();
		}
	}

	/**
	 * ���ñ�����
	 * 
	 * @return CellsPane
	 */
	private void setCells(CellsPane m_CellsPane) {
		this.m_CellsPane = m_CellsPane;
	}

	/**
	 * ���ñ�����
	 * 
	 * @return CellsPane
	 */
	private CellsPane getCells() {
		return m_CellsPane;
	}

	/**
	 * ��ȡCellsModel
	 */
	private CellsModel getCellsModel() {
		return m_CellsPane.getDataModel();
	}

	/**
	 * add by ����� 2008-3-18 ��Ӹ��ƻ��߼���ʱ�߿�Ķ���Ч�������Ʒ���
	 * 
	 * @param Graphics
	 *            g, BorderPlayControl playControl����������
	 */
	private void paintPlayBorder(Graphics g) {
		if (g == null) {
			return;
		}
		Graphics2D g2d = (Graphics2D) g;

		float arryType[] = { LINE_WINDT, DOT_WIDTH, DOT_WIDTH };
		BasicStroke bStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, arryType, 0.0f);
		g2d.setStroke(bStroke);
		// �õ���ǰѡ�е�����
		AreaPosition selectArea = null;
		if (m_CellsPane == null) {
			return;
		}
		if (isCopyState()) {
			selectArea = getAreaPosition();
		} else {
			selectArea = getCellsModel().getSelectModel().getSelectedArea();
		}
		if (selectArea == null) {
			return;
		}
		Rectangle rect = m_CellsPane.getCellRect(selectArea, false);
		if (rect == null) {
			return;
		}
		int iPointX = rect.x + rect.width - 1;
		int iPointY = rect.y + rect.height - 1;
		Point leftTopPoint = new Point(rect.x, rect.y);
		Point rightTopPoint = new Point(iPointX, rect.y);
		Point leftBottomPoint = new Point(rect.x, iPointY);
		Point rightBottomPoint = new Point(iPointX, iPointY);
		// ���������ϱ߿򶯻�
		int iTopBorderFrame = getTopBorderFrame();// ����ϱ߿�֡
		paintHorizontalLine(g2d, iTopBorderFrame, leftTopPoint, rightTopPoint);
		iTopBorderFrame += LINE_WINDT;// ���ƻ��Ƶ�֡
		if (iTopBorderFrame > LINE_WINDT) {
			iTopBorderFrame = BORDER_BOUND;// ��ԭ֡
		}
		setTopBorderFrame(iTopBorderFrame);

		// ���������±߿򶯻�
		int iBottomBorderFrame = getBottomBorderFrame();
		paintHorizontalLine(g2d, iBottomBorderFrame, leftBottomPoint,
				rightBottomPoint);
		iBottomBorderFrame += LINE_WINDT;// ���ƻ��Ƶ�֡
		if (iBottomBorderFrame > LINE_WINDT) {
			iBottomBorderFrame = BORDER_BOUND;// ��ԭ֡
		}
		setBottomBorderFrame(iBottomBorderFrame);

		// ����������߿򶯻�
		int iLeftBorderFrame = getLeftBorderFrame();
		paintVerticalLine(g2d, iLeftBorderFrame, leftTopPoint, leftBottomPoint);
		iLeftBorderFrame += LINE_WINDT;// ���ƻ��Ƶ�֡
		if (iLeftBorderFrame > LINE_WINDT) {
			iLeftBorderFrame = BORDER_BOUND;// ��ԭ֡
		}
		setLeftBorderFrame(iLeftBorderFrame);

		// ���������ұ߿򶯻�
		int iRightBorderFrame = getRightBorderFrame();
		paintVerticalLine(g2d, iLeftBorderFrame, rightTopPoint,
				rightBottomPoint);
		iRightBorderFrame += LINE_WINDT;// ���ƿ��Ƶ�֡
		if (iRightBorderFrame > LINE_WINDT) {
			iRightBorderFrame = BORDER_BOUND;// ��ԭ֡
		}
		setRightBorderFrame(iRightBorderFrame);
		setAreaPosition(selectArea);
		setCopyState(true);
	}

	/**
	 * add by ����� ���ƺ���ı߿򶯻�
	 * 
	 * @param Graphics2D
	 *            g2d,int iFrame(���ƶ�����֡),Point leftPoint,Point rightPoint
	 */
	private void paintHorizontalLine(Graphics2D g2d, int iFrame,
			Point leftPoint, Point rightPoint) {
		g2d.setColor(Color.BLACK);
		g2d.drawLine(leftPoint.x + iFrame, leftPoint.y, rightPoint.x,
				rightPoint.y);
		g2d.setColor(Color.WHITE);
		g2d.drawLine(leftPoint.x + iFrame - LINE_WINDT, leftPoint.y,
				rightPoint.x, rightPoint.y);
	}

	/**
	 * add by ����� ��������ı߿򶯻�
	 * 
	 * @param Graphics2D
	 *            g2d,int iFrame(���ƶ�����֡),Point leftPoint,Point rightPoint
	 */
	private void paintVerticalLine(Graphics2D g2d, int iFrame, Point topPoint,
			Point bottomPoint) {
		g2d.setColor(Color.BLACK);
		g2d.drawLine(topPoint.x, topPoint.y + iFrame, bottomPoint.x,
				bottomPoint.y);
		g2d.setColor(Color.WHITE);
		g2d.drawLine(topPoint.x, topPoint.y + iFrame - LINE_WINDT,
				bottomPoint.x, bottomPoint.y);
	}

	/**
	 * add by ����� 2008-4-2 �߿򶯻��ĵľ�̬���Ʒ���
	 * 
	 * @param CellsPane
	 *            cellsPane
	 * 
	 */
	public static void controlPlay(CellsPane cellsPane) {
		if (cellsPane == null) {
			return;
		}
		BorderPlayRender playRender = getPlayRender(cellsPane);
		if (playRender == null) {
			return;
		}
//		if (playRender.getCells() == null) {
			playRender.setCells(cellsPane);// ����������
//		}
		playRender.startPlay();//������ʼ
	}

	/**
	 * ��ñ߿򶯻���Ⱦ��
	 * 
	 * @param CellsPane
	 *            cellsPane
	 * @return BorderPlayRender
	 */
	private static BorderPlayRender getPlayRender(CellsPane cellsPane) {
		if (cellsPane == null) {
			return null;
		}
		CellRenderAndEditor renderAndEditor = cellsPane.getReanderAndEditor();// �������ͱ༭���Ĺ�����
		SheetCellRenderer playRender = renderAndEditor
				.getRender(BorderPlayRender.class);// ��û�����
		if(!(playRender instanceof BorderPlayRender)){//��ֹ�е�applet����Ҫ���ر༭���ʱ������ܻ����Ⱦ������
			return null;
		}
//		if(!(playRender instanceof BorderPlayRender)){
//			playRender = new BorderPlayRender();
//			renderAndEditor.registRender(BorderPlayRender.class, playRender);//ע����Ⱦ��
//		}
		return (BorderPlayRender)playRender;
	}

	/**
	 * ��ñ߿򶯻���Ⱦ��,ֹͣ����
	 * 
	 * @param CellsPane
	 *            cellsPane
	 * @return BorderPlayRender
	 */
	public static void stopPlay(CellsPane cellsPane) {
		BorderPlayRender playRender = BorderPlayRender.getPlayRender(cellsPane);// ��ö�����Ⱦ��
		if (playRender == null) {
			return;
		}
		if (playRender.isPlay()) {
			playRender.stopPlay();
		}
	}

	private class playRunnable implements Runnable {
		public void run() {
			while (true) {
				try {
					thread.sleep(timer);
					if (!isPlay()) {
						synchronized (thread) {
							thread.wait();
						}
					}

					borderMove();// ���û��Ʒ���

				} catch (Throwable e) {
					stopPlay();
					break;
				}
			}
		}
	}

	public Component getCellRendererComponent(CellsPane cellsPane,Object value,
			 boolean isSelected, boolean hasFocus, int row,
			int column, Cell cell) {
		return null;
	}

}
