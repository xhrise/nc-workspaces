package com.ufsoft.table;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.ufsoft.table.format.*;

/**
 * <p>Title:ҳǩ��� </p>
 * <p>Description:��ʾ���ؼ������ɵı�ҳ��ͨ�����ҳǩ���Ըı䵱ǰ��ʾ�ı�ҳ��</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */
public class PageMark
    extends JComponent {
  /**��¼ҳǩ�����ơ�*/
  private ArrayList names = new ArrayList();
  /**��¼�����ڵ�ǰ�����¶�Ӧ��ˮƽ���ش�С����������һ��ҳǩ�Ŀ��*/
  private ArrayList sizes = new ArrayList();
  /**��ǰѡ���ҳǩ*/
  private int selectPage = 0;
  /**ҳǩ������*/
  private Font font = FontFactory.createFont("Dialog", Font.PLAIN, 12);
  /**��������Ķ�����*/
  private FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
  /**ˮƽ�ָ����ľ���*/
  private int xGap = 10;
  /**������ɫ*/
  private Color cLine = Color.black;
  /**ѡ������ı�����ɫ*/
  private Color cEmpty = Color.white;
  /**����ĸ߶�*/
  private int prefHeigth = 15;

  private EventListenerList listenerList = new EventListenerList();

//******************************************
   /**
    * ���캯��
    */
  public PageMark() {
    super();
    addMouseListener(new PageMouseListener());
  }

//**********************************************

   /**
    * ��������.
    * @param index int �����λ�ã���ҳΪ0�����С��0��������ҳ������������һҳ��׷�ӵ�ĩβ
    * @param strName String ��������ƣ����ﲻ������Ψһ�����Ʒǿյ��жϡ�
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
   * ɾ������
   * @param index int ɾ��λ�á�
   * @exception IllegalArgumentException ���λ�÷�Χ���ڵ�ǰ��Χ֮�ڣ��׳��쳣��
   */
  public void removeName(int index) {
    checkScopes(index);
    if (index == (names.size() - 1) && index == selectPage) { //���ɾ�����һҳ�����Ұ���ѡ����Ŀ
      selectPage--;
    }
    names.remove(index);
    calculatStringSize();
    updateView();
  }

  /**
   * ���ҳǩ��ʶ�Ƿ����
 * @param index
   */
  private void checkScopes(int index) {
    if (index < 0 || index >= names.size()) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * ���õ�ǰѡ�е�ҳǩλ��
   * @param index int λ�á�
   * @exception IllegalArgumentException ���λ�÷�Χ���ڵ�ǰ��Χ֮�ڣ��׳��쳣��
   */
  public void setSelected(int index) {
    checkScopes(index);
    selectPage = index;
    //֪ͨ���ؼ�����ǰѡ���ҳ��ı䡣

    //�������޸�JViewPort����ʾλ��
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
   * ���ѡ�е�ҳǩλ��
   * @return int λ����Ϣ��
   * @exception IllegalArgumentException
   */

  public int getSelected() {
    return selectPage;
  }

  /**
   * ��ð���ҳǩ������
   * @return int ����ҳǩ��������
   */
  public int getPageNum() {
    return names.size();
  }

  /**
   * �õ����е�ҳǩ���ơ�
   * @return <code>ArrayList</code> ҳǩ����
   */
  public ArrayList getNames() {
    return names;
  }

  /**
   * �ƶ�������ҳ
   */
  public void moveHome() {
    setSelected(0);
  }

  /**
   * �ƶ��������һҳ
   */
  public void moveEnd() {
    setSelected(names.size() - 1);
  }

  /**
   * ���㵱ǰ�ַ�ռ�ݵ���ʾ�ռ�
   */
  private void calculatStringSize() {
    int size = names.size();
    sizes.clear();
    for (int i = 0; i < size; i++) {
      sizes.add(new Integer(metrics.stringWidth( (String) names.get(i))));
    }
  }

  /**
   * �������
   */
  private void updateView() {
    repaint();
  }

//****************************************�������
   /**
    * ���Ƶ�ǰ������ݡ�
 * @param g
    */
   protected void paintComponent(Graphics g) {

     Graphics scratchGraphics = g.create();
     try {
       //�õ����Ƶ�����
       int heigth = prefHeigth;
       Rectangle rect = getBounds();
       if (! (rect.width > 0 || rect.height > 0)) {
         return;
       }
       rect.x = 0;
       rect.y = 0;
       Rectangle rectCur = new Rectangle(rect.x, rect.y, xGap, heigth);
       //������ʼ���
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
         //�����ı�
         paintString(i == selectPage, str, scratchGraphics, rectCur);
         //�ж��Ƿ��ҿռ���ʾ
         if (!rectCur.intersects(rect)) {
           return;
         }
         //���Ƽ��
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
   * ����ҳǩ������ࡣ
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
   * ����ҳǩ�����Ҳࡣ
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
   * ����ҳǩ���ı���
 * @param bSelected
 * @param strText
 * @param g
 * @param rect
   */
  private void paintString(boolean bSelected, String strText, Graphics g,
                           Rectangle rect) {
    g.translate(rect.x, rect.y);
    //������������������λ�á�
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
   * ����ҳǩ�ļ�϶��
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
   * �õ���ǰ�����ȫ����ռ�ݵĿ��
   * @return int ������
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
    * �������ӵ���������
    */
   private class PageMouseListener
       extends MouseAdapter {
     public void mouseClicked(MouseEvent e) {
       Point p = e.getPoint();
       //���������ĸ�ҳǩ��
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

  //*********************�¼�֪ͨ****************************//
   /**
    * ��Ӽ�����
 * @param listener
    */
  public void addPagemarkListener(PagemarkListener listener) {
    listenerList.add(PagemarkListener.class, listener);
  }

  /**
   * ɾ��������.
 * @param listener
 */
public void removePagemarkListener(PagemarkListener listener) {
    listenerList.remove(PagemarkListener.class, listener);
  }
/**
 * ֪ͨҳǩ��ѡ���Ѿ��ı� 
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
