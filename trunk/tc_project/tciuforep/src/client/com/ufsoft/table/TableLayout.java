package com.ufsoft.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.Scrollable;

import com.ufsoft.table.header.HeaderTree;
import com.ufsoft.table.header.TableHeader;

/**
 * ���ؼ��Ĳ��ֹ�����.�������������������еĲ��ֹ���
 * @version 1.0.0.1
 */
public class TableLayout
    implements LayoutManager, TableConstants, Serializable {
  /**�����е�����ͼ   */
  private JViewport m_mainView;
  /**��ͷ������   */
  private JViewport m_ColHeaderView;
  /**��ͷ������   */
  private JViewport m_RowHeaderView;
  /**��ֱ������   */
  private JScrollBar m_VScrollBar;
  /**ˮƽ������   */
  private JScrollBar m_HScrollBar;
  /** ����ҳǩ��ViewPort   */
  private JViewport m_PageView;
  /**������״�������ͼ.*/
  private JViewport m_rowTreeView, m_colTreeView;
  /**ҳǩ�͹�����֮��Ļ���   */
  private SlideBox m_SlideBox;
  /**�ĸ��������   */
  private Component m_upperLeft, m_upperRight, m_lowerLeft, m_lowerRight;
  /**ˮƽ�������Ĺ�������   */
  private int m_nHsbPolicy;
  /**��ֱ�������Ĺ�������   */
  private int m_nVsbPolicy;
  /**�Ƿ���ʾҳǩ   */
  private boolean m_bShowPagemark;

  //********************
   //********************
    /**��ֳ������б�����ͼ  */
    private JViewport m_rowHeader2;
  /**��ֳ������б�����ͼ   */
  private JViewport m_columnHeader2;
  /**ˮƽ������ʱ��,�Ҳ���ֵ���ͼ.   */
  private JViewport m_rightView;
  /**��ֱ������ʱ��,�·����ֵ���ͼ   */
  private JViewport m_downView;
  /**�������������ֳ��ֵ����·�����ͼ   */
  private JViewport m_rightDownView;
  /**������״�������ͼ.*/
  private JViewport m_rowTreeView2, m_colTreeView2;
  /**ˮƽ������,�������Ҳ��ˮƽ������   */
  private TableScrollBar m_hScrollBar2;
  /**��ֱ������,�������·��Ĵ�ֱ������   */
  private TableScrollBar m_vScrollBar2;
  /**�ָ���ͼ�ķָ���*/
  private SeperatorBar m_horBar, m_verBar;
  private SeperatorBox m_crossBox;
  /**���ֹ������������*/
  private TablePane m_Pane = null;
  /**�ָ����Ŀ��*/
  private int BALK = 8;
//***************************************************************
//********************���ɾ������ķ���*****************************
    /**
     * �������������.
     * @param s String �����ʶ,�μ�TableConstants
     * @param c Component ��������
     */
    public void addLayoutComponent(String s, Component c) {
      if (s == null) {
        throw new IllegalArgumentException();
      }
      if (s.equals(MAINVIEW)) {
        m_mainView = (JViewport) addSingletonComponent(m_mainView, c);
      }
      else if (s.equals(COLUMNHEADERVIEW)) {
        m_ColHeaderView = (JViewport) addSingletonComponent(m_ColHeaderView, c);
      }
      else if (s.equals(ROWHEADERVIEW)) {
        m_RowHeaderView = (JViewport) addSingletonComponent(m_RowHeaderView, c);
      }
      else if (s.equals(PAGEVIEW)) {
        m_PageView = (JViewport) addSingletonComponent(m_PageView, c);
      }
      else if (s.equals(VSCROLLBAR)) {
        m_VScrollBar = (JScrollBar) addSingletonComponent(m_VScrollBar, c);
      }
      else if (s.equals(HSCROLLBAR)) {
        m_HScrollBar = (JScrollBar) addSingletonComponent(m_HScrollBar, c);
      }
      else if (s.equals(SLIDEBOX)) {
        m_SlideBox = (SlideBox) addSingletonComponent(m_SlideBox, c);
      }
      else if (s.equals(ROW_TREE)) {
        m_rowTreeView = (JViewport) addSingletonComponent(m_rowTreeView, c);
      }
      else if (s.equals(COL_TREE)) {
        m_colTreeView = (JViewport) addSingletonComponent(m_colTreeView, c);
      }
      //********�ĸ���������
       else if (s.equals(UPPER_LEFT)) {
         m_upperLeft = addSingletonComponent(m_upperLeft, c);
       }
       else if (s.equals(UPPER_RIGHT)) {
         m_upperRight = addSingletonComponent(m_upperRight, c);
       }
       else if (s.equals(LOWER_lEFT)) {
         m_lowerLeft = addSingletonComponent(m_lowerLeft, c);
       }
       else if (s.equals(LOWER_RIGHT)) {
         m_lowerRight = addSingletonComponent(m_lowerRight, c);
       }
       //********�ָ����������
        else if (s.equals(COLUMNHEADERVIEW2)) {
          m_columnHeader2 = (JViewport) addSingletonComponent(m_columnHeader2,
              c);
        }
        else if (s.equals(ROWHEADERVIEW2)) {
          m_rowHeader2 = (JViewport) addSingletonComponent(m_rowHeader2, c);
        }
        else if (s.equals(RIGHT_VIEW)) {
          m_rightView = (JViewport) addSingletonComponent(m_rightView, c);
        }
        else if (s.equals(DOWN_VIEW)) {
          m_downView = (JViewport) addSingletonComponent(m_downView, c);
        }
        else if (s.equals(RIGHTDOWN_VIEW)) {
          m_rightDownView = (JViewport) addSingletonComponent(m_rightDownView,
              c);
        }
        else if (s.equals(HSCROLLBAR2)) {
          m_hScrollBar2 = (TableScrollBar) addSingletonComponent(m_hScrollBar2,
              c);
        }
        else if (s.equals(VSCROLLBAR2)) {
          m_vScrollBar2 = (TableScrollBar) addSingletonComponent(m_vScrollBar2,
              c);
        }
        else if (s.equals(HOR_SLIDEBOX)) {
          m_horBar = (SeperatorBar) addSingletonComponent(m_horBar, c);
        }
        else if (s.equals(VER_SLIDEBOX)) {
          m_verBar = (SeperatorBar) addSingletonComponent(m_verBar, c);
        }
        else if (s.equals(CROSS_SLIDEBOX)) {
          m_crossBox = (SeperatorBox) addSingletonComponent(m_crossBox, c);
        }
        else if (s.equals(ROW_TREE2)) {
          m_rowTreeView2 = (JViewport) addSingletonComponent(m_rowTreeView2, c);
        }
        else if (s.equals(COL_TREE2)) {
          m_colTreeView2 = (JViewport) addSingletonComponent(m_colTreeView2, c);
        }
        else {
          throw new IllegalArgumentException("Invalid Key:" + s);
        }
    }

  /**
   * ��������������ǰ���������Ѿ����ڣ���Ҫɾ�������оɵ����,ȷ����������Ψһ��
   * @param oldC �����
   * @param newC �����
   * @return the <code>newC</code>
   */
  private Component addSingletonComponent(Component oldC, Component newC) {
    if ( (oldC != null) && (oldC != newC)) {
      Container parent = oldC.getParent();
      if (parent != null) {
        parent.remove(oldC);
      }
    }
    return newC;
  }

  /**
   * ɾ�����ֵ����
   * @param c
   */
  public void removeLayoutComponent(Component c) {
    if (c == m_mainView) {
      m_mainView = null;
    }
    else if (c == m_ColHeaderView) {
      m_ColHeaderView = null;
    }
    else if (c == m_RowHeaderView) {
      m_RowHeaderView = null;
    }
    else if (c == m_PageView) {
      m_PageView = null;
    }
    else if (c == m_VScrollBar) {
      m_VScrollBar = null;
    }
    else if (c == m_HScrollBar) {
      m_HScrollBar = null;
    }
    else if (c == m_SlideBox) {
      m_SlideBox = null;
    }
    else if (c == m_rowTreeView) {
      m_rowTreeView = null;
    }
    else if (c == m_colTreeView) {
      m_colTreeView = null;
    }
    //�����4�����
    else if (c == m_upperLeft) {
      m_upperLeft = null;
    }
    else if (c == m_upperRight) {
      m_upperRight = null;
    }
    else if (c == m_lowerLeft) {
      m_lowerLeft = null;
    }
    else if (c == m_lowerRight) {
      m_lowerRight = null;
    }
    //�ָ������������
    else if (c == m_columnHeader2) {
      m_columnHeader2 = null;
    }
    else if (c == m_rowHeader2) {
      m_rowHeader2 = null;
    }
    else if (c == m_rightView) {
      m_rightView = null;
    }
    else if (c == m_downView) {
      m_downView = null;
    }
    else if (c == m_rightDownView) {
      m_rightDownView = null;
    }
    else if (c == m_hScrollBar2) {
      m_hScrollBar2 = null;
    }
    else if (c == m_vScrollBar2) {
      m_vScrollBar2 = null;
    }
    else if (c == m_horBar) {
      m_horBar = null;
    }
    else if (c == m_verBar) {
      m_verBar = null;
    }
    else if (c == m_crossBox) {
      m_crossBox = null;
    }
    else if (c == m_rowTreeView2) {
      m_rowTreeView2 = null;
    }
    else if (c == m_colTreeView2) {
      m_colTreeView2 = null;
    }
  }

//***********************************************************************
//�����������������������������������������������������ֹ������ӿڵķ�������������������������������������������������������������
   /**
    * �������е������ӵĳߴ���ΪTablePane��ȱʡ�ߴ�.
    * @param parent ��Ҫ���ֵ�����
    * @return Dimension
    */
   public Dimension preferredLayoutSize(Container parent) {
     TablePane tablePane = (TablePane) parent;
     int nVsbPolicy = tablePane.getVScrollBarPolicy();
     int nHsbPolicy = tablePane.getHScrollBarPolicy();
     //�������е�����Ƿ�����,��ô�ض���ʾ������.
     if (m_rightView != null || m_downView != null) {
       nVsbPolicy = TableConstants.VERTICAL_SCROLLBAR_ALWAYS;
       nHsbPolicy = TableConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
     }
     //��������
     Insets insets = parent.getInsets();
     int prefWidth = insets.left + insets.right;
     int prefHeight = insets.top + insets.bottom;
     Dimension extentSize = null;
     Dimension viewSize = null;
     Component view = null;
     //�������ͼ�ĳߴ�
     if (m_mainView != null) {
       extentSize = m_mainView.getPreferredSize();
       if (extentSize != null) {
         prefWidth += extentSize.width;
         prefHeight += extentSize.height;
       }
       viewSize = m_mainView.getViewSize();
       view = m_mainView.getView();
     }
     /*
      * ����У��б�����ڲ��ҿɼ�����Ҫ������ǵĳߴ�
      */
     if ( (m_RowHeaderView != null) && m_RowHeaderView.isVisible()) {
       prefWidth += m_RowHeaderView.getPreferredSize().width;
     }
     if ( (m_ColHeaderView != null) && m_ColHeaderView.isVisible()) {
       prefHeight += m_ColHeaderView.getPreferredSize().height;
     }
     // ���������,������ĳߴ�
     if (m_rowTreeView != null && m_rowTreeView.isVisible()) {
       prefWidth += m_rowTreeView.getPreferredSize().width;
     }
     if (m_colTreeView != null && m_colTreeView.isVisible()) {
       prefHeight += m_colTreeView.getPreferredSize().height;
     }
     //���䴹ֱ�������Ŀռ�
     if ( (m_VScrollBar != null) && (nVsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
       if (nVsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
         prefWidth += m_VScrollBar.getPreferredSize().width;
       }
       //�ж��Ƿ���Ҫ��ʾ�������ռ�,����ǲ�ִ��ڵ����,�����е���ͼ�������һ���޷���ʾ���е���Ϣ;
       //��ô����Ҫ��Ӷ�Ӧ�Ĺ�����.
       else if ( (viewSize != null) && (extentSize != null)) {
         boolean canScroll = ! ( (Scrollable) view).
             getScrollableTracksViewportHeight();
         if (canScroll && (viewSize.height > extentSize.height)) {
           prefWidth += m_VScrollBar.getPreferredSize().width;
         }
       }
     }
     //��ʾҳǩ��ʱ�򣬱���Ԥ���͹�������ͬ�Ŀռ�
     if ( (m_HScrollBar != null) && (nHsbPolicy != HORIZONTAL_SCROLLBAR_NEVER)) {
       if (nHsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
         prefHeight += m_HScrollBar.getPreferredSize().height;
       }
       else if ( (viewSize != null) && (extentSize != null)) {
         boolean canScroll = ! ( (Scrollable) view).
             getScrollableTracksViewportWidth();
         if (canScroll && (viewSize.width > extentSize.width)) {
           prefHeight += m_HScrollBar.getPreferredSize().height;
         }
       }
     }
     else if (m_PageView != null) {
       prefHeight += m_PageView.getPreferredSize().height;
     }
     //����Ƿ�����,���ҵ�ǰ�ı��û������,��ô��Ӧ������Ӧ�ķָ�������.
     if (!m_Pane.isFreezing()) {
       if (m_rightView != null) {
         prefWidth += BALK;
       }
       if (m_downView != null) {
         prefHeight += BALK;
       }
     }
     return new Dimension(prefWidth, prefHeight);
   }

  /**
   * �������������Ҫ�����С�ߴ�.
   * @param parent ��Ҫ���ֵ�����
   * @return Dimension
   */
  public Dimension minimumLayoutSize(Container parent) {
    Insets insets = parent.getInsets();
    int minWidth = insets.left + insets.right;
    int minHeight = insets.top + insets.bottom;
    //��������ڱ�����С�ߴ粻����Ӱ��.
    if (m_mainView != null) {
      Dimension size = m_mainView.getMinimumSize();
      minWidth += size.width;
      minHeight += size.height;
    }
    if ( (m_RowHeaderView != null) && m_RowHeaderView.isVisible()) {
      Dimension size = m_RowHeaderView.getMinimumSize();
      minWidth += size.width;
      minHeight = Math.max(minHeight, size.height);
    }

    if ( (m_ColHeaderView != null) && m_ColHeaderView.isVisible()) {
      Dimension size = m_ColHeaderView.getMinimumSize();
      minWidth = Math.max(minWidth, size.width);
      minHeight += size.height;
    }
    if ( (m_VScrollBar != null) && (m_nVsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
      Dimension size = m_VScrollBar.getMinimumSize();
      minWidth += size.width;
      minHeight = Math.max(minHeight, size.height);
    }
// ���������,������ĳߴ�
    if (m_rowTreeView != null && m_rowTreeView.isVisible()) {
      minWidth += m_rowTreeView.getMinimumSize().width;
    }
    if (m_colTreeView != null && m_colTreeView.isVisible()) {
      minHeight += m_colTreeView.getMinimumSize().height;
    }
    if ( (m_HScrollBar != null) && (m_nHsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
      Dimension size = m_HScrollBar.getMinimumSize();
      minWidth = Math.max(minWidth, size.width);
      minHeight += size.height;
      if (m_PageView != null) { //��Ҫ��ǻ���Ŀ��
        size = m_SlideBox.getMinimumSize();
        minWidth = Math.max(minWidth, size.width);
      }
    }
    else if (m_PageView != null) { //����ʾ��������������ʾҳǩ����Ҫ���ҳǩ�ĸ߶�
      Dimension size = m_PageView.getMinimumSize();
      minHeight += size.height;
    }
    if (m_PageView != null) { //���ҳǩ�Ŀ��
      Dimension size = m_PageView.getMinimumSize();
      minWidth = Math.max(minWidth, size.width);
    }
    return new Dimension(minWidth, minHeight);
  }

  /**
   * �������¹��򲼾�����:����������ÿռ�ķ��䣬�����ң����ϵ��¡�<ol>
   *    <li>�Ƿ�����:����б���ɼ���ȡ������ѿ�Ⱥ����ӿڵĸ߶ȣ�����б���ɼ���ȡ������
   * �Ѹ߶Ⱥ����ӿ��ӿڵĿ�ȣ������������ʾ,����ͱ������ʾ��ͬ;��ֱ�����������ʾ������
   * �����б���,ˮƽ��������ҳǩ��ȵĺ�Ϊ���ӿڵĿ�ȣ�����ֵ������������õı������䡣�ڿռ�
   * ���������Ҫע���������������Ĳ���������AsNeed���������ˮƽ�ռ䲻������ֹ���������
   * ����ҳǩ,��Ӱ������ռ�ĳߴ�,������Ҫ���¼����Ƿ���Ҫ��ʾ��ֱ������.
   *   <li>������δ����:���������,���ù������������,�ռ����Ĺ�������ҪΪ����ӵ��������
   * �ռ�,������������������Ӱ��,������Լ�.
   *   <li>�����ķ�����:����һ���������,ֻ�ǲ���Ҫ��ʾ�ָ���,�������ı��ռ�ķ������
   * ��֤����������.����,�������ĵĿռ�����У��ռ���ȫ���䵽�ڶ��������ϣ����ұ�֤�ڶ�������
   * ���ʵ���Сֵ��ע��ͼ�ӿڵĴ�С��
   * </ol>
   * @param parent Container ���ֵ�����
   */
  public void layoutContainer(Container parent) {
    TablePane tablePane = (TablePane) parent;
    //�õ����ֵ�����
    Rectangle availR = tablePane.getBounds();
    availR.x = availR.y = 0;
    //����߽�
    Insets insets = parent.getInsets();
    availR.x = insets.left;
    availR.y = insets.top;
    availR.width -= insets.left + insets.right;
    availR.height -= insets.top + insets.bottom;
    if (availR.width == 0 || availR.height == 0) {
      return;
    }

    //�������Ĺ�������
    int nVsbPolicy = tablePane.getVScrollBarPolicy();
    int nHsbPolicy = tablePane.getHScrollBarPolicy();

    //���Ȳ������Ŀռ�
    Rectangle colTreeR = new Rectangle(0, availR.y, 0, 0);
    if (m_colTreeView != null && m_colTreeView.isVisible()) {
      int colTreeHeight = Math.min(availR.height,
                                   m_colTreeView.getView().getPreferredSize().
                                   height);
      colTreeR.height = colTreeHeight;
      availR.y += colTreeHeight;
      availR.height -= colTreeHeight;
    }
    Rectangle rowTreeR = new Rectangle(availR.x, 0, 0, 0);
    if (m_rowTreeView != null && m_rowTreeView.isVisible()) {
      HeaderTree tree = (HeaderTree) m_rowTreeView.getView();
      int rowTreeWidth = Math.min(availR.width, tree.getPreferredSize().width);
      rowTreeR.width = rowTreeWidth;
      availR.x += rowTreeWidth;
      availR.width -= rowTreeWidth;
    }
    /* ����б�����ӣ����ĳߴ�Ϊָ���ĸ߶ȣ�����Ŀ�ȡ�
     */
    Rectangle colHeadR = new Rectangle(0, availR.y, 0, 0);
    if ( (m_ColHeaderView != null) && (m_ColHeaderView.isVisible())) {
      //Ϊ�б������߶ȣ���ʱ������ĵ�x���Ϳ��
      int colHeadHeight = Math.min(availR.height,
                                   m_ColHeaderView.getPreferredSize().height);
      colHeadR.height = colHeadHeight;
      //����ʣ��Ŀռ����ʼy��
      availR.y += colHeadHeight;
      availR.height -= colHeadHeight;
    }
    /* ����б���ɼ������ĳߴ�Ϊָ���Ŀ�ȣ�����ĸ߶�
     */
    Rectangle rowHeadR = new Rectangle(availR.x, 0, 0, 0);
    if ( (m_RowHeaderView != null) && (m_RowHeaderView.isVisible())) {
      int rowHeadWidth = Math.min(availR.width,
                                  m_RowHeaderView.getPreferredSize().width);
      //�����б���ķ��䣬��Ҳ��ȷ���˿�ȣ�����ʣ��ռ�����������
      rowHeadR.width = rowHeadWidth;
      availR.width -= rowHeadWidth;
      availR.x += rowHeadWidth;
    }
    //���ڷ�����Ĳ��ֵ�������.
    if (m_rightView != null || m_downView != null) {
      layoutSeperatedPane(availR, rowHeadR, colHeadR, rowTreeR,
                          colTreeR);
      adjustFreezing(tablePane);
      return;
    }

    /* ��ʱʣ��ռ���Ҫ��������ͼ����������ҳǩ���б���ĸ߶Ⱥ���ʼy�㻹û�����ã�
     * �б���Ŀ�Ⱥ���ʼx��û�����ã���Ҫ����ʣ��ռ�Ŀռ����������á�
     * ���ȱȽ�����ͼ����ѳߴ����ʾ�ߴ硣���ڲ���ʱ���϶��£�������Ҫ�ȶ��������
     * ��ѳߴ粼�֡����Ǽ���ViewPorts�Ĳ��ֹ�����������������ѳߴ硣
     */
    Component view = m_mainView.getView();
    //����ͼ��������ĳߴ�
    Dimension viewPrefSize = view.getPreferredSize();
    //����ͼ�ĳߴ�
    Dimension extentSize = m_mainView.toViewCoordinates(availR.getSize());
    boolean isEmpty = (availR.width < 0 || availR.height < 0);
    Scrollable sv = (Scrollable) view;
    /* �����ʣ��ռ䣬���ȼ���������Ŀռ�
     */
    Rectangle vsbR = new Rectangle(0, availR.y, 0, 0);
    boolean vsbNeeded;
    //�ж��Ƿ���ʾ��ֱ�Ĺ�����
    if (isEmpty) {
      vsbNeeded = false;
    }
    else if (nVsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
      vsbNeeded = true;
    }
    else if (nVsbPolicy == VERTICAL_SCROLLBAR_NEVER) {
      vsbNeeded = false;
    }
    else { // vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED
      vsbNeeded = viewPrefSize.height > extentSize.height;
    }
    if ( (m_VScrollBar != null) && vsbNeeded) {
      adjustForVSB(true, availR, vsbR);
      extentSize = m_mainView.toViewCoordinates(availR.getSize());
    }
    //�Ƿ���ʾˮƽ������
    boolean hsbNeeded;
    if (isEmpty) {
      hsbNeeded = false;
    }
    else if (nHsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
      hsbNeeded = true;
    }
    else if (nHsbPolicy == HORIZONTAL_SCROLLBAR_NEVER) {
      hsbNeeded = false;
    }
    else { // hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED
      hsbNeeded = viewPrefSize.width > extentSize.width;
    }
    //Ϊҳǩ�����飬������������Ӧ�Ŀռ�
    Rectangle hsbRect = new Rectangle(availR.x, 0, 0, 0);

    if ( ( (m_HScrollBar != null) && hsbNeeded) ||
        (m_PageView != null && m_bShowPagemark)) {
      adjustForHorComponent(hsbNeeded || m_bShowPagemark, availR, hsbRect);
      //���ҳǩ����ˮƽ����������ӣ���ô����Ӱ�촹ֱ������������
      if ( (m_VScrollBar != null) && !vsbNeeded &&
          (nVsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
        extentSize = m_mainView.toViewCoordinates(availR.getSize());
        vsbNeeded = viewPrefSize.height > extentSize.height;
        if (vsbNeeded) {
          adjustForVSB(true, availR, vsbR);
        }
      }
    }
    /* ��������ͼ�ĳߴ�
     */
    if (m_mainView != null) {
      m_mainView.setBounds(availR);
      if (sv != null) {
        extentSize = m_mainView.toViewCoordinates(availR.getSize());
        boolean oldHSBNeeded = hsbNeeded;
        boolean oldVSBNeeded = vsbNeeded;
        /*
         * ���ڵõ�������ͼ����Ŀռ䣬�Ƚ���ͼ�ռ�������ɵ���������Ծ����Ƿ���Ҫ��ʾ
         * ����������ˣ���Ҫ�ٴη���������Ŀռ�
         */
        //�Ƿ���Ҫ������ֱ������
        if (m_VScrollBar != null && nVsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED) {
          boolean newVSBNeeded = (viewPrefSize.height > extentSize.height);
          if (newVSBNeeded != vsbNeeded) {
            vsbNeeded = newVSBNeeded;
            adjustForVSB(vsbNeeded, availR, vsbR);
            extentSize = m_mainView.toViewCoordinates
                (availR.getSize());
          }
        }
        //�Ƿ���Ҫ����ˮƽ������
        if (m_HScrollBar != null &&
            nHsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED) {
          boolean newHSBbNeeded = (viewPrefSize.width > extentSize.width);
          if (newHSBbNeeded != hsbNeeded) { //��Ҫ��ʾˮƽ������
            hsbNeeded = newHSBbNeeded;
            if (!m_bShowPagemark) { //���û����ʾҳǩ����Ҫ������õĿռ�
              adjustForHorComponent(true, availR, hsbRect);
            }
            extentSize = m_mainView.toViewCoordinates(availR.getSize());
            if ( (m_VScrollBar != null) && !vsbNeeded &&
                (nVsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
              extentSize = m_mainView.toViewCoordinates
                  (availR.getSize());
              vsbNeeded = viewPrefSize.height >
                  extentSize.height;
              if (vsbNeeded) {
                adjustForVSB(true, availR, vsbR);
              }
            }
          }
        }
        if (oldHSBNeeded != hsbNeeded ||
            oldVSBNeeded != vsbNeeded) {
          m_mainView.setBounds(availR);
        }
      }
    }

    /*
     * ����ͼ�ĳߴ��Ѿ���ȷ�����������ø�������ĳߴ�
     */
    vsbR.height = availR.height;
    hsbRect.width = availR.width;
    rowTreeR.height = availR.height;
    rowTreeR.y = availR.y;
    rowHeadR.height = availR.height;
    rowHeadR.y = availR.y;
    colTreeR.width = availR.width;
    colTreeR.x = availR.x;
    colHeadR.width = availR.width;
    colHeadR.x = availR.x;
    /**
     * ���Ե���3�����֮��Ĺ�ϵ��
     */
    Rectangle hsbR = new Rectangle(0, 0, 0, 0);
    Rectangle slideR = new Rectangle(0, 0, 0, 0);
    Rectangle pageR = new Rectangle(0, 0, 0, 0);
    seperateRoom(hsbRect, pageR, slideR, hsbR, hsbNeeded, m_bShowPagemark);
    /* һ�´���Ϊ����������÷�Χ
     */
    setCompBound(m_RowHeaderView, rowHeadR);
    setCompBound(m_ColHeaderView, colHeadR);
    setCompBound(m_rowTreeView, rowTreeR);
    setCompBound(m_colTreeView, colTreeR);
    setCompBound(m_VScrollBar, vsbR);
    setCompBound(m_HScrollBar, hsbR);
    setCompBound(m_PageView, pageR);
    setCompBound(m_SlideBox, slideR);
    setCompBound(m_lowerLeft,
                 new Rectangle(rowHeadR.x, hsbRect.y, rowHeadR.width,
                               hsbRect.height));
    setCompBound(m_lowerRight,
                 new Rectangle(vsbR.x, hsbRect.y, vsbR.width, hsbRect.height));
    setCompBound(m_upperLeft,
                 new Rectangle(rowHeadR.x, colHeadR.y, rowHeadR.width,
                               colHeadR.height));
    setCompBound(m_upperRight,
                 new Rectangle(vsbR.x, colHeadR.y, vsbR.width, colHeadR.height));
    adjustFreezing(tablePane);
  }

  /**
   * ���ֲ�ִ��ڵĴ���
   * @param availR Rectangle
   * @param rowHeadR Rectangle
   * @param colHeadR Rectangle
   * @param rowTreeR Rectangle
   * @param colTreeR Rectangle
   */
  private void layoutSeperatedPane(Rectangle availR,
                                   Rectangle rowHeadR, Rectangle colHeadR,
                                   Rectangle rowTreeR, Rectangle colTreeR) {
    /* �����ʣ��ռ䣬���ȼ���������Ŀռ�
     */
    Rectangle vsbR = new Rectangle(0, availR.y, 0, 0);
    adjustForVSB(true, availR, vsbR);
    //Ϊҳǩ�����飬������������Ӧ�Ŀռ�
    Rectangle hsbRect = new Rectangle(availR.x, 0, 0, 0);
    adjustForHorComponent(true, availR, hsbRect);
    /* ��������ͼ�ĳߴ�  */
    Rectangle mainR = new Rectangle(availR);
    /*����ͼ�ĳߴ��Ѿ���ȷ�����������ø�������ĳߴ�     */
    vsbR.height = availR.height;
    hsbRect.width = availR.width;
    rowTreeR.height = availR.height;
    rowTreeR.y = availR.y;
    rowHeadR.height = availR.height;
    rowHeadR.y = availR.y;
    colTreeR.width = availR.width;
    colTreeR.x = availR.x;
    colHeadR.width = availR.width;
    colHeadR.x = availR.x;
    //�����Ƿָ�����ӿռ�Ŀռ����.
    Rectangle colHeadR2, colBalkR, rowHeadR2, rowBalkR, rightViewR, downViewR,
        rightDownViewR, vScrollR2, hScrollR2, colTreeR2, rowTreeR2;
    colHeadR2 = new Rectangle();
    colTreeR2 = new Rectangle();
    colBalkR = new Rectangle();
    rightViewR = new Rectangle();
    rowHeadR2 = new Rectangle();
    rowTreeR2 = new Rectangle();
    rowBalkR = new Rectangle();
    downViewR = new Rectangle();
    rightDownViewR = new Rectangle();
    hScrollR2 = new Rectangle();
    vScrollR2 = new Rectangle();

    //�����濪ʼ,��ʼ��������Ѿ�����ռ���ٴη���,���ݷ�����������Ƿ�����,�������������Ŀռ�
    boolean bFreezing = m_Pane.isFreezing();
    CellsPane cellsPane = (CellsPane) m_mainView.getView();
    //�����б���ķ���.
    if (m_rightView != null) {
      if (bFreezing) {
        //�õ�������е�λ��.
        int freezingCol = m_Pane.getSeperateCol();
        int colPos = cellsPane.getDataModel().getColumnHeaderModel().
            getPosition(freezingCol);
        int spareroom = colHeadR.width;
        colHeadR.width = colPos;
        seperateRoom(spareroom, colHeadR, colHeadR2, true);
      }
      else {
        int colPos = m_Pane.getSeperateX();
        int spareroom = colHeadR.width;
        colHeadR.width = colPos;
        seperateRoom(spareroom, colHeadR, colBalkR, true);
        //����ָ������Ҳ���ͼ�Ŀռ�.
        spareroom = colBalkR.width;
        colBalkR.width = BALK;
        seperateRoom(spareroom, colBalkR, colHeadR2, true);
        //����ˮƽ�������Ŀռ�
        hsbRect.width = colHeadR.width;
        hScrollR2.x = colHeadR2.x;
        hScrollR2.width = colHeadR2.width;
        hScrollR2.y = hsbRect.y;
        hScrollR2.height = hsbRect.height;
      }
      mainR.width = colHeadR.width;
      rightViewR.x = colHeadR2.x;
      rightViewR.width = colHeadR2.width;
      rightViewR.y = rowHeadR.y;
      rightViewR.height = rowHeadR.height;
      //�����б���Ŀռ���������������Ŀռ�
      colTreeR2.y = colTreeR.y;
      colTreeR2.height = colTreeR.height;
      colTreeR.x = colHeadR.x;
      colTreeR.width = colHeadR.width;
      colTreeR2.x = colHeadR2.x;
      colTreeR2.width = colHeadR2.width;
      colBalkR.y = colTreeR.y;
    }
    if (m_downView != null) {
      if (bFreezing) {
        //�õ�������е�λ��.
        int freezingRow = m_Pane.getSeperateRow();
        int rowPos = cellsPane.getDataModel().getRowHeaderModel().getPosition(
            freezingRow);
        int spareroom = rowHeadR.height;
        rowHeadR.height = rowPos;
        seperateRoom(spareroom, rowHeadR, rowHeadR2, false);
      }
      else {
        int rowPos = m_Pane.getSeperateY();
        int spareroom = rowHeadR.height;
        rowHeadR.height = rowPos;
        seperateRoom(spareroom, rowHeadR, rowBalkR, false);
        //����ָ������Ҳ���ͼ�Ŀռ�.
        spareroom = rowBalkR.height;
        rowBalkR.height = BALK;
        seperateRoom(spareroom, rowBalkR, rowHeadR2, false);
        //���䴹ֱ�������Ŀռ�
        vsbR.height = rowHeadR.height;
        vScrollR2.x = vsbR.x;
        vScrollR2.width = vsbR.width;
        vScrollR2.y = rowHeadR2.y;
        vScrollR2.height = rowHeadR2.height;

      }
      mainR.height = rowHeadR.height;
      downViewR.x = colHeadR.x;
      downViewR.width = colHeadR.width;
      downViewR.y = rowHeadR2.y;
      downViewR.height = rowHeadR2.height;
      //���·����Ҳ���ͼ�ĸ߶�
      rightViewR.height = rowHeadR.height;
      //�����б���Ŀռ���������������Ŀռ�
      rowTreeR2.x = rowTreeR.x;
      rowTreeR2.width = rowTreeR.width;
      rowTreeR.y = rowHeadR.y;
      rowTreeR.height = rowHeadR.height;
      rowTreeR2.y = rowHeadR2.y;
      rowTreeR2.height = rowHeadR2.height;
      rowBalkR.x = rowTreeR.x;
    }
    //���������ͼ����,�������Ŀռ�
    if (m_rightView != null && m_downView != null) {
      rightDownViewR.x = colHeadR2.x;
      rightDownViewR.width = colHeadR2.width;
      rightDownViewR.y = rowHeadR2.y;
      rightDownViewR.height = rowHeadR2.height;
    }
    //���,����ˮƽ��������ҳǩ������.
    Rectangle hsbR = new Rectangle();
    Rectangle pageR = new Rectangle();
    Rectangle slideR = new Rectangle();
    seperateRoom(hsbRect, pageR, slideR, hsbR, true, m_bShowPagemark);
    //�������������������ᴰ��,��ô�Ҳ�����²�������²ཫ��Ϊ����ͼ,��Ҫ�����������Ŀռ�
    if (bFreezing) {
      Rectangle temp = null;
      if (m_rightView != null) {
        temp = hsbR;
        hsbR = hScrollR2;
        hScrollR2 = temp;
      }
      if (m_downView != null) {
        temp = vsbR;
        vsbR = vScrollR2;
        vScrollR2 = temp;
      }
    }
    //���¿�ʼΪ�����ռ��������ǵķ�Χ.
    m_mainView.setBounds(mainR);
    setCompBound(m_RowHeaderView, rowHeadR);
    setCompBound(m_ColHeaderView, colHeadR);
    setCompBound(m_rowTreeView, rowTreeR);
    setCompBound(m_colTreeView, colTreeR);
    setCompBound(m_VScrollBar, vsbR);
    setCompBound(m_HScrollBar, hsbR);
    setCompBound(m_PageView, pageR);
    setCompBound(m_SlideBox, slideR);
    //�����4�����
    setCompBound(m_lowerLeft,
                 new Rectangle(rowHeadR.x, hsbRect.y, rowHeadR.width,
                               hsbRect.height));
    setCompBound(m_lowerRight,
                 new Rectangle(vsbR.x, hsbRect.y, vsbR.width, hsbRect.height));
    setCompBound(m_upperLeft,
                 new Rectangle(rowHeadR.x, colHeadR.y, rowHeadR.width,
                               colHeadR.height));
    setCompBound(m_upperRight,
                 new Rectangle(vsbR.x, colHeadR.y, vsbR.width, colHeadR.height));
    //�ָ�������������������ڷָ�������Ĵ�����ͼ�Ĵ�С��Ҫ���ƣ������û�ʹ���������ͼ
    //����Ŀռ��㹻����ô����setViewPosition��ʧȥ�����޷������������ʾλ�á��ڲ�ִ���
    //�����������ڵ�����£���ͼ����Ŀռ䲻���Դ�����ʾ��Ҫ�Ŀռ䡣
    if(bFreezing){
      int freezingRow = m_Pane.getSeperateRow();
      int rowPos = cellsPane.getDataModel().getRowHeaderModel().getPosition(
          freezingRow);
      int freezingCol = m_Pane.getSeperateCol();
      int colPos = cellsPane.getDataModel().getColumnHeaderModel().getPosition(
          freezingCol);
      Dimension dime = m_mainView.getViewSize();
      int maxHorSize= dime.width-colPos;
      int maxVerSize = dime.height-rowPos;
      if(maxHorSize<rightViewR.width){
        rightViewR.width=maxHorSize;
        rightDownViewR.width = maxHorSize;
        colHeadR2.width = maxHorSize;
        colTreeR2.width = maxHorSize;
      }
      if(maxVerSize<downViewR.height){
        downViewR.height = maxVerSize;
        rightDownViewR.height=maxVerSize;
        rowHeadR2.height = maxVerSize;
        rowTreeR2.height=maxVerSize;
      }

    }

    setCompBound(m_rowHeader2, rowHeadR2);
    setCompBound(m_columnHeader2, colHeadR2);
    setCompBound(m_rightView, rightViewR);
    setCompBound(m_downView, downViewR);
    setCompBound(m_rightDownView, rightDownViewR);
    //ȷ������״̬�¸���ͼ��λ������ָ����е�Ҫ��
    if(bFreezing){
      if(m_rightView != null){
        m_mainView.setViewPosition(new Point(0,m_mainView.getViewPosition().y));
        if(m_rightView.getViewPosition().x < m_Pane.getSeperateX()){
          m_rightView.setViewPosition(new Point(m_Pane.getSeperateX(),m_rightView.getViewPosition().y));
        }
      }
      if(m_downView != null){
        m_mainView.setViewPosition(new Point(m_mainView.getViewPosition().x,0));
        if(m_downView.getViewPosition().y < m_Pane.getSeperateY()){
          m_downView.setViewPosition(new Point(m_downView.getViewPosition().x,m_Pane.getSeperateY()));
        }
      }
      if(m_rightDownView != null){
        Point point = m_rightDownView.getViewPosition();
        Point sepPoint = new Point(m_Pane.getSeperateX(), m_Pane.getSeperateY());
        if(point.x < sepPoint.x || point.y < sepPoint.y){
          m_rightDownView.setViewPosition(sepPoint);
        }
      }
    }
    setCompBound(m_hScrollBar2, hScrollR2);
    setCompBound(m_vScrollBar2, vScrollR2);
    setCompBound(m_rowTreeView2, rowTreeR2);
    setCompBound(m_colTreeView2, colTreeR2);
    if (!bFreezing) {
      rowBalkR.width = m_Pane.getWidth();
      colBalkR.height = m_Pane.getHeight();
      setCompBound(m_verBar, colBalkR);
      setCompBound(m_horBar, rowBalkR);

      Rectangle cslR = new Rectangle(colBalkR.x, colBalkR.width, rowBalkR.y,
                                     rowBalkR.height);
      setCompBound(m_crossBox, cslR);
    }
  }

  /**
   * �����ڶ������������ͼλ�õĵ�����
   */
  private void adjustFreezing(TablePane tablePane) {
    boolean bFreezing = tablePane.isFreezing();
    Dimension viewSize = m_mainView.getViewSize();
    //������������ͼ�ĵ�����Ҫ�Ƕ��ڹ���������Сֵ���е����Ͳ��λ�õĵ�����
    int horMin = 0, verMin = 0;
    int verMax = viewSize.height, horMax = viewSize.width;
    if (bFreezing) {
      //������Ҫ���¼����ֵ�λ��
      int sepRow = tablePane.getSeperateRow();
      int sepCol = tablePane.getSeperateCol();
      TableHeader rowHeader = (TableHeader) m_RowHeaderView.getView();
      TableHeader colHeader = (TableHeader) m_ColHeaderView.getView();
      //��Ϊ��2������������Сֵ��
      horMin = colHeader.getModel().getPosition(sepCol);
      verMin = rowHeader.getModel().getPosition(sepRow);
    }
    //���ȵ�������ͼ�����Ĺ�������
    if (m_VScrollBar.isEnabled()) {
      m_VScrollBar.setValues(m_mainView.getViewPosition().y,
                             m_mainView.getExtentSize().height, 0, verMax);

    }
    if (m_HScrollBar.isEnabled()) {
      m_HScrollBar.setValues(m_mainView.getViewPosition().x,
                             m_mainView.getExtentSize().width, 0, horMax);
    }
    if (m_downView != null) {
      int value = Math.max(verMin, m_vScrollBar2.getValue());
      int ext = m_downView.getExtentSize().height;
      ext = Math.min(ext, verMax - verMin);
      if (ext > 0) {
        m_vScrollBar2.setValues(value, ext, verMin, verMax);
      }
    }
    if (m_rightView != null) {
      int value = Math.max(horMin, m_hScrollBar2.getValue());
      int ext = m_rightView.getExtentSize().width;//zzl��m_rightDownView��Ϊm_rightView
      ext = Math.min(ext, horMax - horMin);
      if (ext > 0) {
        m_hScrollBar2.setValues(value, ext, horMin, horMax);
      }
    }
  }

//��һ������ǿյļ��
  private void setCompBound(Component c, Rectangle r) {
    if (c != null) {
      c.setBounds(r);
    }
  }

  /**
   * ��һ�οռ���������Rectangle,���ߵĿռ�ֻ������Ҫ�����ά�����в��.
   * @param spareRoom ��Ҫ������Ŀռ䷶Χ
   * @param first ��һ������
   * @param second �ڶ�������
   * @param bHor �����γ��.
   */
  public void seperateRoom(int spareRoom, Rectangle first, Rectangle second,
                           boolean bHor) {
    if (spareRoom <= 0) {
      return;
    }
    if (bHor) { //ˮƽ����
      int space = Math.min(spareRoom, first.width);
      first.width = space;
      spareRoom -= space;
      if (spareRoom <= 0) {
        return;
      }
      second.x = first.x + first.width;
      second.y = first.y;
      second.width = spareRoom;
      second.height = first.height;
      return;
    }
    else {
      int space = Math.min(spareRoom, first.height);
      first.height = space;
      spareRoom -= space;
      if (spareRoom <= 0) {
        return;
      }
      second.y = first.y + first.height;
      second.x = first.x;
      second.height = spareRoom;
      second.width = first.width;
      return;
    }

  }

  /**
   * ������ֱ�������Ŀռ�
   */
  private void adjustForVSB(boolean wantsVSB, Rectangle available,
                            Rectangle vsbR) {
    int oldWidth = vsbR.width;
    if (wantsVSB) {
      int vsbWidth = Math.max(0, Math.min(m_VScrollBar.getPreferredSize().width,
                                          available.width));
      available.width -= vsbWidth;
      vsbR.width = vsbWidth;
      vsbR.x = available.x + available.width;
    }
    else {
      available.width += oldWidth;
    }
  }

  /**
   *����ҳǩ�����飬������3�ߵĿռ�
   * @param wantRoom �Ƿ���ҪΪ���������ռ�
   * @param available Rectangle ������������Ŀռ�
   * @param hsbRect Rectangle ��������
   */
  private void adjustForHorComponent(boolean wantsRoom, Rectangle available,
                                     Rectangle hsbRect) {
    int oldHeight = hsbRect.height;
    if (wantsRoom) {
      int hsbHeight = Math.max(0,
                               Math.min(available.height,
                                        m_HScrollBar.getPreferredSize().height));
      available.height -= hsbHeight;
      hsbRect.y = available.y + available.height;
      hsbRect.height = hsbHeight;
    }
    else {
      available.height += oldHeight;
    }
  }

  /**
   * Ϊҳǩ�����飬����������ռ�
   * @param all Rectangle �ɷ��������
   * @param page Rectangle ҳǩ�Ŀռ�
   * @param slideBox  Rectangle ����Ŀռ�
   * @param hsb Rectangle �������Ŀռ�
   * @param wantsHSB boolean �Ƿ���Ҫ������
   * @param wantsPage boolean �Ƿ���Ҫҳǩ
   * @param pageScale int ҳǩռ����ʾ�ռ�ı����������������ֻ�е����������������С�ߴ�ʱ����Ч��
   */
  private void seperateRoom(Rectangle all, Rectangle page, Rectangle slideBox,
                            Rectangle hsb,
                            boolean wantsHSB, boolean wantsPage) {
    if (wantsPage && m_PageView != null && ( (!wantsHSB) || m_HScrollBar == null)) { //ֻ��ʾҳǩ
      page.setBounds(all);
    }
    else if ( ( (!wantsPage) || m_PageView == null) && wantsHSB && m_HScrollBar != null) { //ֻ��ʾ������
      hsb.setBounds(all);
    }
    else if (wantsPage && wantsHSB && m_PageView != null && m_HScrollBar != null) { //��ʾ��������ҳǩ
      int pageScale = m_SlideBox.getPosition();
      int width = all.width;
      int pageWidth = m_PageView.getMinimumSize().width;
      int slideWidth = m_SlideBox.getMinimumSize().width;
      int hscWidth = m_HScrollBar.getMinimumSize().width;
      if (width < (pageWidth + slideWidth + hscWidth)) {
//        throw new IllegalArgumentException("ע�⣬�����д�����ʱ����������");
        pageWidth = width;
        slideWidth = 0;
        hscWidth = 0;
      }
      width -= slideWidth;
      int newPageWidth = (int) (width * (pageScale / 100.0));
      //����ռ䲻����С����С�ĳߴ�
      pageWidth = newPageWidth < pageWidth ? pageWidth : newPageWidth;
      //���������ĳߴ��Ƿ�С����С�ߴ�
      int newHscWidth = width - pageWidth;
      if (newHscWidth < hscWidth) {
        pageWidth = width - hscWidth;
      }
      else {
        hscWidth = newHscWidth;
      }
      //��ʼ����ռ�
      page.x = all.x;
      page.y = all.y;
      page.width = pageWidth;
      page.height = all.height;
      all.x += page.width;
      slideBox.x = all.x;
      slideBox.y = all.y;
      slideBox.width = slideWidth;
      slideBox.height = all.height;
      all.x += slideBox.width;
      hsb.x = all.x;
      hsb.y = all.y;
      hsb.width = hscWidth;
      hsb.height = all.height;
    }
  }

  /**
   * ��֤����������Ҫ���ֵ����һ��
   * @param pane TablePane �������ֹ�����������
   */
  public void syncWithScrollPane(TablePane pane) {
    this.m_Pane = pane;
    m_mainView = pane.getMainView();
    m_ColHeaderView = pane.getColumnHeader();
    m_RowHeaderView = pane.getRowHeader();
    m_VScrollBar = pane.getVScrollBar();
    m_HScrollBar = pane.getHScrollBar();
    m_PageView = pane.getPageView();
    m_SlideBox = pane.getSlideBox();
    m_upperLeft = pane.getCorner(UPPER_LEFT);
    m_upperRight = pane.getCorner(UPPER_RIGHT);
    m_lowerLeft = pane.getCorner(LOWER_lEFT);
    m_lowerRight = pane.getCorner(LOWER_RIGHT);
//    m_bShowPagemark = pane.isShowPageMark();
    m_rowHeader2 = pane.getRowHeader2();
    m_columnHeader2 = pane.getColumnHeader2();
    m_rightView = pane.getRightDownView();
    m_downView = pane.getDownView();
    m_rightDownView = pane.getRightDownView();

    m_hScrollBar2 = pane.getHScrollBar2();
    m_vScrollBar2 = pane.getVScrollBar2();
    m_verBar = pane.getVerBar();
    m_horBar = pane.getHorBar();
    m_crossBox = pane.getCrossBox();

  }
}
