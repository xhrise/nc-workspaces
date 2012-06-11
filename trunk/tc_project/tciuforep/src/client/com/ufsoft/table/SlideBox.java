package com.ufsoft.table;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>Title: 滑块组件.</p>
 * <p>Description: 处理滑块的拖动,提供水平和垂直的滑块.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 * @version 1.0.0.1
 */
public class SlideBox
    extends JComponent {
  /** 记录滑块方向的静态值：垂直方向*/
  public static final int VERTICAL = 0;
  /** 记录滑块方向的静态值：水平方向*/
  public static final int HORIZONTAL = 1;
  /** 记录滑块当前分割区域，左侧占有的比例*/
  private int position;
  //***************************构造函数
   /**
    * 构造函数
    */
   private SlideBox( ) {
     super();
     addMouseMotionListener(new MotionListener(this));
   }

  /**
   * 创建一个滑块
 * @param orientation
   * @return SlideBox
   */
  static public SlideBox createDefaultSlideBox(int orientation) {
    SlideBox sb = new SlideBox();
    sb.setBorder(BorderFactory.createRaisedBevelBorder());
    Dimension dim = orientation == HORIZONTAL ? new Dimension(7, 18) :
        new Dimension(18, 7);
    sb.setPreferredSize(dim);
    sb.setForeground(Color.gray);
    sb.position = 40;
    return sb;
  }

//******************************基于属性的方法****************//
  /**
   * 设置滑块当前分割区域，左侧占有的比例
   * @param pos int 滑块当前分割区域，左侧占有的比例
   */
  public void setPosition(int pos) {
    if (pos != position) {
      position = pos;
      JComponent componet = (JComponent) getParent();
      componet.revalidate();
    }
  }

  /**
   * 得到滑块当前分割区域，左侧占有的比例
   * @return int 滑块当前分割区域，左侧占有的比例
   */
  public int getPosition() {
    return position;
  }

  /**
   * 得到这个组件的最小尺寸。这里认为是它的缺省尺寸
   * @return Dimension
   */
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  /**
   * 得到这个组件的最大尺寸。这里认为是它的缺省尺寸
   * @return Dimension
   */
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

//*****************************8绘制代码开始**************************//
  /**
   * 重载组件的绘制。
   * @param g Graphics 绘图句柄
   */
  protected void paintComponent(Graphics g) {
    Graphics scratchGraphics = g.create();
    try {
      //得到绘制的区域
      Rectangle rect = getBounds();
      //绘制矩形区域
      g.fillRect(1, 1, rect.width - 2, rect.height - 2);
      //画边界
//      g.setColor(lineColor);
//      g.drawRect(0, 0, rect.width, rect.height);
    }
    finally {
      scratchGraphics.dispose();
    }
  }

  /**
   * 内部类处理滑块响应的鼠标事件。
   */

  private class MotionListener
      implements MouseMotionListener {
    //以下变量分别记录左侧空间，右侧空间，滑块占用的空间，左侧最小空间，右侧最小空间
    int leftSpace, rightSpace, curSpace, leftMinSpace, rightMinSpace;
    SlideBox slideBox;
    /**
     * @param sb
     */
    public MotionListener(SlideBox sb) {
      slideBox = sb;
    }

    public void mouseDragged(MouseEvent e) {
      Point p = e.getPoint();
      int pos = getNewPosition(p);
      slideBox.setPosition(pos);
    }

    public void mouseMoved(MouseEvent e) {
      //修改光标
      setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
    }

    /**
     * 计算鼠标拖动后，滑块新的分割比例
     */
    private int getNewPosition(Point p) {
      init();
      int xOffset = p.x;
      int xPage = leftSpace + xOffset;
      int xScrollBar = rightSpace - xOffset;
      if (xPage < leftMinSpace) {
        xPage = leftMinSpace;
        xScrollBar = (leftSpace + rightSpace) - xPage;
      }
      else if (xScrollBar < rightMinSpace) {
        xScrollBar = rightMinSpace;
        xPage = (leftSpace + rightSpace) - xScrollBar;
      }
      return 100 * xPage / (xPage + xScrollBar);
    }

    private void init() {
      //记录开始拖动的时候，组件的位置。
      TablePane pane = (TablePane) slideBox.getParent();
      leftSpace = pane.getPageView().getBounds().width;
      leftMinSpace = pane.getPageView().getMinimumSize().width;
      rightSpace = pane.getHScrollBar().getBounds().width;
      rightMinSpace = pane.getHScrollBar().getMinimumSize().width;
      curSpace = pane.getSlideBox().getBounds().width;
    }
  }
}
