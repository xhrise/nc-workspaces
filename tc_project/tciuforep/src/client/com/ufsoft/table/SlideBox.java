package com.ufsoft.table;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>Title: �������.</p>
 * <p>Description: ��������϶�,�ṩˮƽ�ʹ�ֱ�Ļ���.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 * @version 1.0.0.1
 */
public class SlideBox
    extends JComponent {
  /** ��¼���鷽��ľ�ֵ̬����ֱ����*/
  public static final int VERTICAL = 0;
  /** ��¼���鷽��ľ�ֵ̬��ˮƽ����*/
  public static final int HORIZONTAL = 1;
  /** ��¼���鵱ǰ�ָ��������ռ�еı���*/
  private int position;
  //***************************���캯��
   /**
    * ���캯��
    */
   private SlideBox( ) {
     super();
     addMouseMotionListener(new MotionListener(this));
   }

  /**
   * ����һ������
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

//******************************�������Եķ���****************//
  /**
   * ���û��鵱ǰ�ָ��������ռ�еı���
   * @param pos int ���鵱ǰ�ָ��������ռ�еı���
   */
  public void setPosition(int pos) {
    if (pos != position) {
      position = pos;
      JComponent componet = (JComponent) getParent();
      componet.revalidate();
    }
  }

  /**
   * �õ����鵱ǰ�ָ��������ռ�еı���
   * @return int ���鵱ǰ�ָ��������ռ�еı���
   */
  public int getPosition() {
    return position;
  }

  /**
   * �õ�����������С�ߴ硣������Ϊ������ȱʡ�ߴ�
   * @return Dimension
   */
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  /**
   * �õ������������ߴ硣������Ϊ������ȱʡ�ߴ�
   * @return Dimension
   */
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

//*****************************8���ƴ��뿪ʼ**************************//
  /**
   * ��������Ļ��ơ�
   * @param g Graphics ��ͼ���
   */
  protected void paintComponent(Graphics g) {
    Graphics scratchGraphics = g.create();
    try {
      //�õ����Ƶ�����
      Rectangle rect = getBounds();
      //���ƾ�������
      g.fillRect(1, 1, rect.width - 2, rect.height - 2);
      //���߽�
//      g.setColor(lineColor);
//      g.drawRect(0, 0, rect.width, rect.height);
    }
    finally {
      scratchGraphics.dispose();
    }
  }

  /**
   * �ڲ��ദ������Ӧ������¼���
   */

  private class MotionListener
      implements MouseMotionListener {
    //���±����ֱ��¼���ռ䣬�Ҳ�ռ䣬����ռ�õĿռ䣬�����С�ռ䣬�Ҳ���С�ռ�
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
      //�޸Ĺ��
      setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
    }

    /**
     * ��������϶��󣬻����µķָ����
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
      //��¼��ʼ�϶���ʱ�������λ�á�
      TablePane pane = (TablePane) slideBox.getParent();
      leftSpace = pane.getPageView().getBounds().width;
      leftMinSpace = pane.getPageView().getMinimumSize().width;
      rightSpace = pane.getHScrollBar().getBounds().width;
      rightMinSpace = pane.getHScrollBar().getMinimumSize().width;
      curSpace = pane.getSlideBox().getBounds().width;
    }
  }
}
