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
 * 剪切,复制和格式刷时，选择区域动画效果的绘制器
 * 
 * @author 王宇光
 * @version Create on 2008-4-1
 */
public class BorderPlayRender implements SheetCellRenderer {
	/**
	 * 定义画笔：虚线中线条的长度以及线条之间的距离
	 */
	private final int LINE_WINDT = 6;
	/**
	 * 定义画笔：虚线的中线条两端的点
	 */
	private final int DOT_WIDTH = 0;
	/**
	 * 虚线的相对起点
	 */
	private final int BORDER_BOUND = 0;
	/**
	 * 是否做了复制操作，根据此判断，是从选择模型中获得选择区域，还是直接从控制类里获取已经保存的区域
	 */
	private boolean b_IsCopy = false;
	/**
	 * 关联的表格组件
	 */
	private CellsPane m_CellsPane;
	/**
	 * 控制虚线运动的线程
	 */
	private Thread thread = null;
	/**
	 * 动画是否在运行
	 */
	private boolean b_isPlay = false;
	/**
	 * 正在绘制的区域
	 */
	private AreaPosition areaPosition = null;
	/**
	 * 线程间隔时间
	 */
	private final long timer = 100;
	/**
	 * 上边框的帧
	 */
	private int iTopBorderFrame = 0;
	/**
	 * 左边框的帧
	 */
	private int iLeftBorderFrame = 0;
	/**
	 * 右边框的帧
	 */
	private int iRightBorderFrame = 0;
	/**
	 * 下边框的帧
	 */
	private int iBottomBorderFrame = 0;

	public BorderPlayRender() {
//		if (thread == null) {
//			thread = new Thread(new playRunnable());
//		}
	}

	/**
	 * 动画的开始
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
	 * 设置动画状态
	 * 
	 * @param boolean
	 *            b_isPlay：flase:停止动画
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
	 * 边框动画的绘制方法
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
	 * 唤醒进程，使动画继续运行
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
	 * 判断线程是否处于存活状态
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
	 * 唤醒进程，使动画继续运行
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
	 * 获得控制上边框绘制的帧
	 * 
	 * @param
	 * @return int iTopBorderFrame
	 */
	private int getTopBorderFrame() {
		return iTopBorderFrame;
	}

	/**
	 * 设置控制上边框绘制的帧
	 * 
	 * @param int
	 *            iTopBorderFrame
	 * @return
	 */
	private void setTopBorderFrame(int iTopBorderFrame) {
		this.iTopBorderFrame = iTopBorderFrame;
	}

	/**
	 * 获得控制左边框绘制的帧
	 * 
	 * @param
	 * @return int iLeftBorderFrame
	 */
	private int getLeftBorderFrame() {
		return iLeftBorderFrame;
	}

	/**
	 * 设置控制左边框绘制的帧
	 * 
	 * @param int
	 *            iLeftBorderFrame
	 * @return
	 */
	private void setLeftBorderFrame(int iLeftBorderFrame) {
		this.iLeftBorderFrame = iLeftBorderFrame;
	}

	/**
	 * 获得控制右边框绘制的帧
	 * 
	 * @param
	 * @return int iRightBorderFrame
	 */
	private int getRightBorderFrame() {
		return iRightBorderFrame;
	}

	/**
	 * 设置控制右边框绘制的帧
	 * 
	 * @param int
	 *            iRightBorderFrame
	 * @return
	 */
	private void setRightBorderFrame(int iRightBorderFrame) {
		this.iRightBorderFrame = iRightBorderFrame;
	}

	/**
	 * 获得控制底边框绘制的帧
	 * 
	 * @param
	 * @return int iBottomBorderFrame
	 */
	private int getBottomBorderFrame() {
		return iBottomBorderFrame;
	}

	/**
	 * 设置控制底边框绘制的帧
	 * 
	 * @param int
	 *            iBottomBorderFrame
	 * @return
	 */
	private void setBottomBorderFrame(int iBottomBorderFrame) {
		this.iBottomBorderFrame = iBottomBorderFrame;
	}

	/**
	 * 保存绘制的区域，以便鼠标点击其他地方时，还在绘制此区域的边框
	 * 
	 * @param
	 * @return AreaPosition areaPosition
	 */
	private AreaPosition getAreaPosition() {
		return areaPosition;
	}

	/**
	 * 获得保存的区域
	 * 
	 * @param AreaPosition
	 *            areaPosition
	 * @return
	 */
	private void setAreaPosition(AreaPosition areaPosition) {
		this.areaPosition = areaPosition;
	}

	/**
	 * 设置状态：是否做了复制操作，根据此判断，是从选择模型中获得选择区域，还是直接从控制类里获取已经保存的区域
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
	 * 动画是否在运行
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
	 * 设置表格面板
	 * 
	 * @return CellsPane
	 */
	private void setCells(CellsPane m_CellsPane) {
		this.m_CellsPane = m_CellsPane;
	}

	/**
	 * 设置表格面板
	 * 
	 * @return CellsPane
	 */
	private CellsPane getCells() {
		return m_CellsPane;
	}

	/**
	 * 获取CellsModel
	 */
	private CellsModel getCellsModel() {
		return m_CellsPane.getDataModel();
	}

	/**
	 * add by 王宇光 2008-3-18 添加复制或者剪切时边框的动画效果：绘制方法
	 * 
	 * @param Graphics
	 *            g, BorderPlayControl playControl动画控制类
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
		// 得到当前选中的区域
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
		// 绘制区域上边框动画
		int iTopBorderFrame = getTopBorderFrame();// 获得上边框帧
		paintHorizontalLine(g2d, iTopBorderFrame, leftTopPoint, rightTopPoint);
		iTopBorderFrame += LINE_WINDT;// 控制绘制的帧
		if (iTopBorderFrame > LINE_WINDT) {
			iTopBorderFrame = BORDER_BOUND;// 还原帧
		}
		setTopBorderFrame(iTopBorderFrame);

		// 绘制区域下边框动画
		int iBottomBorderFrame = getBottomBorderFrame();
		paintHorizontalLine(g2d, iBottomBorderFrame, leftBottomPoint,
				rightBottomPoint);
		iBottomBorderFrame += LINE_WINDT;// 控制绘制的帧
		if (iBottomBorderFrame > LINE_WINDT) {
			iBottomBorderFrame = BORDER_BOUND;// 还原帧
		}
		setBottomBorderFrame(iBottomBorderFrame);

		// 绘制区域左边框动画
		int iLeftBorderFrame = getLeftBorderFrame();
		paintVerticalLine(g2d, iLeftBorderFrame, leftTopPoint, leftBottomPoint);
		iLeftBorderFrame += LINE_WINDT;// 控制绘制的帧
		if (iLeftBorderFrame > LINE_WINDT) {
			iLeftBorderFrame = BORDER_BOUND;// 还原帧
		}
		setLeftBorderFrame(iLeftBorderFrame);

		// 绘制区域右边框动画
		int iRightBorderFrame = getRightBorderFrame();
		paintVerticalLine(g2d, iLeftBorderFrame, rightTopPoint,
				rightBottomPoint);
		iRightBorderFrame += LINE_WINDT;// 绘制控制的帧
		if (iRightBorderFrame > LINE_WINDT) {
			iRightBorderFrame = BORDER_BOUND;// 还原帧
		}
		setRightBorderFrame(iRightBorderFrame);
		setAreaPosition(selectArea);
		setCopyState(true);
	}

	/**
	 * add by 王宇光 绘制横向的边框动画
	 * 
	 * @param Graphics2D
	 *            g2d,int iFrame(控制动画的帧),Point leftPoint,Point rightPoint
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
	 * add by 王宇光 绘制竖向的边框动画
	 * 
	 * @param Graphics2D
	 *            g2d,int iFrame(控制动画的帧),Point leftPoint,Point rightPoint
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
	 * add by 王宇光 2008-4-2 边框动画的的静态控制方法
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
			playRender.setCells(cellsPane);// 关联表格组件
//		}
		playRender.startPlay();//动画开始
	}

	/**
	 * 获得边框动画渲染器
	 * 
	 * @param CellsPane
	 *            cellsPane
	 * @return BorderPlayRender
	 */
	private static BorderPlayRender getPlayRender(CellsPane cellsPane) {
		if (cellsPane == null) {
			return null;
		}
		CellRenderAndEditor renderAndEditor = cellsPane.getReanderAndEditor();// 绘制器和编辑器的管理类
		SheetCellRenderer playRender = renderAndEditor
				.getRender(BorderPlayRender.class);// 获得绘制器
		if(!(playRender instanceof BorderPlayRender)){//防止有的applet不需要加载编辑插件时，因获不能获得渲染器报错
			return null;
		}
//		if(!(playRender instanceof BorderPlayRender)){
//			playRender = new BorderPlayRender();
//			renderAndEditor.registRender(BorderPlayRender.class, playRender);//注册渲染器
//		}
		return (BorderPlayRender)playRender;
	}

	/**
	 * 获得边框动画渲染器,停止动画
	 * 
	 * @param CellsPane
	 *            cellsPane
	 * @return BorderPlayRender
	 */
	public static void stopPlay(CellsPane cellsPane) {
		BorderPlayRender playRender = BorderPlayRender.getPlayRender(cellsPane);// 获得动画渲染器
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

					borderMove();// 调用绘制方法

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
