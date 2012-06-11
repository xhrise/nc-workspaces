package com.ufsoft.table;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.ufsoft.table.format.*;

/**
 * <p>Title:页签组件 </p>
 * <p>Description:显示表格控件中容纳的表页，通过点击页签可以改变当前显示的表页。</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */
public class PageMark
    extends JComponent {
  /**记录页签的名称。*/
  private ArrayList names = new ArrayList();
  /**记录名称在当前字体下对应的水平象素大小，用来计算一个页签的宽度*/
  private ArrayList sizes = new ArrayList();
  /**当前选择的页签*/
  private int selectPage = 0;
  /**页签的字体*/
  private Font font = FontFactory.createFont("Dialog", Font.PLAIN, 12);
  /**基于字体的度量器*/
  private FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
  /**水平分隔符的距离*/
  private int xGap = 10;
  /**线条颜色*/
  private Color cLine = Color.black;
  /**选中区域的背景颜色*/
  private Color cEmpty = Color.white;
  /**组件的高度*/
  private int prefHeigth = 15;

  private EventListenerList listenerList = new EventListenerList();

//******************************************
   /**
    * 构造函数
    */
  public PageMark() {
    super();
    addMouseListener(new PageMouseListener());
  }

//**********************************************

   /**
    * 插入名称.
    * @param index int 插入的位置，首页为0。如果小于0，插入首页，如果大于最后一页，追加的末尾
    * @param strName String 插入的名称，这里不作名称唯一，名称非空的判断。
    */
   public void insertName(int index, String strName) {
     int pos;
     if (index < 0) {
       pos = 0;
     }
     else if (index < names.size()) {
       pos = index;
     }
     else {
       pos = names.size();
     }
     names.add(pos, strName);
     calculatStringSize();
     updateView();
   }

  /**
   * 删除名称
   * @param index int 删除位置。
   * @exception IllegalArgumentException 如果位置范围不在当前范围之内，抛出异常。
   */
  public void removeName(int index) {
    checkScopes(index);
    if (index == (names.size() - 1) && index == selectPage) { //如果删除最后一页，而且包含选中项目
      selectPage--;
    }
    names.remove(index);
    calculatStringSize();
    updateView();
  }

  /**
   * 检查页签标识是否存在
 * @param index
   */
  private void checkScopes(int index) {
    if (index < 0 || index >= names.size()) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * 设置当前选中的页签位置
   * @param index int 位置。
   * @exception IllegalArgumentException 如果位置范围不在当前范围之内，抛出异常。
   */
  public void setSelected(int index) {
    checkScopes(index);
    selectPage = index;
    //通知表格控件，当前选择的页面改变。

    //在这里修改JViewPort的显示位置
    JViewport view = (JViewport) getParent();
    int x = 0;
    for (int i = 0; i < index; i++) {
      x += ( (Integer) sizes.get(i)).intValue();
      x += xGap;
    }
    view.setViewPosition(new Point(x, 0));
    fireChange(index);
  }

  /**
   * 获得选中的页签位置
   * @return int 位置信息。
   * @exception IllegalArgumentException
   */

  public int getSelected() {
    return selectPage;
  }

  /**
   * 获得包含页签的数量
   * @return int 包含页签的数量。
   */
  public int getPageNum() {
    return names.size();
  }

  /**
   * 得到所有的页签名称。
   * @return <code>ArrayList</code> 页签名称
   */
  public ArrayList getNames() {
    return names;
  }

  /**
   * 移动到达首页
   */
  public void moveHome() {
    setSelected(0);
  }

  /**
   * 移动到达最后一页
   */
  public void moveEnd() {
    setSelected(names.size() - 1);
  }

  /**
   * 计算当前字符占据的显示空间
   */
  private void calculatStringSize() {
    int size = names.size();
    sizes.clear();
    for (int i = 0; i < size; i++) {
      sizes.add(new Integer(metrics.stringWidth( (String) names.get(i))));
    }
  }

  /**
   * 更新组件
   */
  private void updateView() {
    repaint();
  }

//****************************************绘制组件
   /**
    * 绘制当前组件内容。
 * @param g
    */
   protected void paintComponent(Graphics g) {

     Graphics scratchGraphics = g.create();
     try {
       //得到绘制的区域
       int heigth = prefHeigth;
       Rectangle rect = getBounds();
       if (! (rect.width > 0 || rect.height > 0)) {
         return;
       }
       rect.x = 0;
       rect.y = 0;
       Rectangle rectCur = new Rectangle(rect.x, rect.y, xGap, heigth);
       //绘制起始间隔
       paintFirst(selectPage == 0, scratchGraphics, rectCur);
       int nSize = names.size();
       for (int i = 0; i < nSize; i++) {
         if (!rectCur.intersects(rect)) {
           return;
         }
         String str = (String) names.get(i);
         if (str == null) {
           str = " ";
         }
         //绘制文本
         paintString(i == selectPage, str, scratchGraphics, rectCur);
         //判断是否右空间显示
         if (!rectCur.intersects(rect)) {
           return;
         }
         //绘制间隔
         if (i == nSize - 1) {
           break;
         }
         else {
           paintGap(i == selectPage, i + 1 == selectPage, scratchGraphics,
                    rectCur);
         }
       }
       if (rectCur.intersects(rect)) {
         paintLast(nSize - 1 == selectPage, scratchGraphics, rectCur);
       }
     }
     finally {
       scratchGraphics.dispose();
     }
   }

  public void repaint() {
    super.repaint();
    setSelected(selectPage);
  }

  /**
   * 绘制页签的最左侧。
 * @param bSelected
 * @param g
 * @param rect
   */
  private void paintFirst(boolean bSelected, Graphics g, Rectangle rect) {
    g.translate(rect.x, rect.y);
    if (bSelected) {
      g.setColor(cEmpty);
      g.fillPolygon(new int[] {0, rect.width, rect.width}
                    , new int[] {0, 0, rect.height}
                    , 3);
    }
    g.setColor(cLine);
    g.drawLine(0, 0, rect.width, rect.height);
    g.translate( -rect.x, -rect.y);
    rect.x += rect.width;

  }

  /**
   * 绘制页签的最右侧。
 * @param bSelected
 * @param g
 * @param rect
   */
  private void paintLast(boolean bSelected, Graphics g, Rectangle rect) {
    g.translate(rect.x, rect.y);
    if (bSelected) {
      g.setColor(cEmpty);
      g.fillPolygon(new int[] {0, rect.width, 0}
                    , new int[] {0, 0, rect.height}
                    , 3);
    }
    g.setColor(cLine);
    g.drawLine(0, rect.height, rect.width, 0);
    g.translate( -rect.x, -rect.y);
    rect.x += rect.width;

  }

  /**
   * 绘制页签的文本。
 * @param bSelected
 * @param strText
 * @param g
 * @param rect
   */
  private void paintString(boolean bSelected, String strText, Graphics g,
                           Rectangle rect) {
    g.translate(rect.x, rect.y);
    //计算文字输出后的象素位置。
    int width = g.getFontMetrics().stringWidth(strText);
    if (bSelected) {
      g.setColor(cEmpty);
      g.fillRect(0, 0, width, rect.height);
    }
    g.setColor(cLine);
    g.drawString(strText, 0, rect.height - 3);
    g.translate( -rect.x, -rect.y);
    rect.x += width;
  }

  /**
   * 绘制页签的间隙。
 * @param bLeftSelected
 * @param bRightSelected
 * @param g
 * @param rect
   */
  private void paintGap(boolean bLeftSelected, boolean bRightSelected,
                        Graphics g, Rectangle rect) {
    int width = rect.width;
    g.translate(rect.x, rect.y);
    if (bRightSelected) {
      g.setColor(cEmpty);
      g.fillPolygon(new int[] {0, width, width}
                    , new int[] {0, 0, rect.height}
                    , 3);
      g.setColor(cLine);
      g.drawLine(0, 0, width, rect.height);
      g.drawLine(0, rect.height, width / 2, rect.height / 2);
    }
    else {
      if (bLeftSelected) {
        g.setColor(cEmpty);
        g.fillPolygon(new int[] {0, width, 0}
                      , new int[] {0, 0, rect.height}
                      , 3);
      }
      g.setColor(cLine);
      g.drawLine(0, rect.height, width, 0);
      g.drawLine(width / 2, rect.height / 2, width, rect.height);
    }
    g.translate( -rect.x, -rect.y);
    rect.x = rect.x + rect.width;
  }

  /**
   * 得到当前组件完全绘制占据的宽度
   * @return int 组件宽度
   */
  public int getComponentWidth() {
    int size = sizes.size();
    if (size == 0) {
      return 0;
    }
    else {
      int len = (size + 1) * xGap;
      for (int i = 0; i < size; i++) {
        len += ( (Integer) sizes.get(i)).intValue();
      }
      return len;
    }
  }

//******************************************************
   /**
    * 组件上添加的鼠标监听器
    */
   private class PageMouseListener
       extends MouseAdapter {
     public void mouseClicked(MouseEvent e) {
       Point p = e.getPoint();
       //计算点击到哪个页签。
       Rectangle rect = getBounds();
       if (p.y <= rect.height) {
         int x = p.x;
         int nSize = sizes.size();
         for (int i = 0; i < nSize; i++) {
           if (i == 0) {
             x -= xGap;
           }
           else {
             x -= xGap / 2;
           }
           x -= ( (Integer) sizes.get(i)).intValue();
           if (i + 1 == nSize) {
             x -= xGap;
           }
           else {
             x -= (xGap / 2);
           }
           if (x <= 0) {
             setSelected(i);
             break;
           }
         }
       }
     }
   }

  //*********************事件通知****************************//
   /**
    * 添加监听器
 * @param listener
    */
  public void addPagemarkListener(PagemarkListener listener) {
    listenerList.add(PagemarkListener.class, listener);
  }

  /**
   * 删除监听器.
 * @param listener
 */
public void removePagemarkListener(PagemarkListener listener) {
    listenerList.remove(PagemarkListener.class, listener);
  }
/**
 * 通知页签的选择已经改变 
 * @param sel*/
  private void fireChange(int sel) {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == PagemarkListener.class) {
        ( (PagemarkListener) listeners[i + 1]).selectNewPage(sel);
      }
    }
  }

}
