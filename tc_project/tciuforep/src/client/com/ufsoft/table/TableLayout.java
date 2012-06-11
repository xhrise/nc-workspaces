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
 * 表格控件的布局管理器.负责各个表格组件在面板中的布局管理。
 * @version 1.0.0.1
 */
public class TableLayout
    implements LayoutManager, TableConstants, Serializable {
  /**容器中的主视图   */
  private JViewport m_mainView;
  /**列头部容器   */
  private JViewport m_ColHeaderView;
  /**行头部容器   */
  private JViewport m_RowHeaderView;
  /**垂直滚动条   */
  private JScrollBar m_VScrollBar;
  /**水平滚动条   */
  private JScrollBar m_HScrollBar;
  /** 容纳页签的ViewPort   */
  private JViewport m_PageView;
  /**容纳树状组件的视图.*/
  private JViewport m_rowTreeView, m_colTreeView;
  /**页签和滚动条之间的滑块   */
  private SlideBox m_SlideBox;
  /**四个角落组件   */
  private Component m_upperLeft, m_upperRight, m_lowerLeft, m_lowerRight;
  /**水平滚动条的滚动策略   */
  private int m_nHsbPolicy;
  /**垂直滚动条的滚动策略   */
  private int m_nVsbPolicy;
  /**是否显示页签   */
  private boolean m_bShowPagemark;

  //********************
   //********************
    /**拆分出来的行标题视图  */
    private JViewport m_rowHeader2;
  /**拆分出来的列标题视图   */
  private JViewport m_columnHeader2;
  /**水平方向拆分时候,右侧出现的视图.   */
  private JViewport m_rightView;
  /**垂直方向拆分时候,下方出现的视图   */
  private JViewport m_downView;
  /**沿着两个方向拆分出现的右下方的视图   */
  private JViewport m_rightDownView;
  /**容纳树状组件的视图.*/
  private JViewport m_rowTreeView2, m_colTreeView2;
  /**水平方向拆分,出现在右侧的水平滚动条   */
  private TableScrollBar m_hScrollBar2;
  /**垂直方向拆分,出现在下方的垂直滚动条   */
  private TableScrollBar m_vScrollBar2;
  /**分割视图的分割条*/
  private SeperatorBar m_horBar, m_verBar;
  private SeperatorBox m_crossBox;
  /**布局管理机管理的组件*/
  private TablePane m_Pane = null;
  /**分割栏的宽度*/
  private int BALK = 8;
//***************************************************************
//********************添加删除组件的方法*****************************
    /**
     * 向面板中添加组件.
     * @param s String 组件标识,参见TableConstants
     * @param c Component 加入的组件
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
      //********四个角落的组件
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
       //********分割窗体产生的组件
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
   * 向容器中添加组件前，如果组件已经存在，需要删除容器中旧的组件,确保加入的组件唯一。
   * @param oldC 旧组件
   * @param newC 新组件
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
   * 删除布局的组件
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
    //角落的4个组件
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
    //分割栏产生的组件
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
//×××××××××××××××××××××××××布局管理器接口的方法×××××××××××××××××××××××××××××
   /**
    * 计算所有的组件相加的尺寸作为TablePane的缺省尺寸.
    * @param parent 需要布局的容器
    * @return Dimension
    */
   public Dimension preferredLayoutSize(Container parent) {
     TablePane tablePane = (TablePane) parent;
     int nVsbPolicy = tablePane.getVScrollBarPolicy();
     int nHsbPolicy = tablePane.getHScrollBarPolicy();
     //如果表格中的情况是分栏表,那么必定显示滚动条.
     if (m_rightView != null || m_downView != null) {
       nVsbPolicy = TableConstants.VERTICAL_SCROLLBAR_ALWAYS;
       nHsbPolicy = TableConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
     }
     //父组件间隔
     Insets insets = parent.getInsets();
     int prefWidth = insets.left + insets.right;
     int prefHeight = insets.top + insets.bottom;
     Dimension extentSize = null;
     Dimension viewSize = null;
     Component view = null;
     //添加主视图的尺寸
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
      * 如果行，列标题存在并且可见，需要添加它们的尺寸
      */
     if ( (m_RowHeaderView != null) && m_RowHeaderView.isVisible()) {
       prefWidth += m_RowHeaderView.getPreferredSize().width;
     }
     if ( (m_ColHeaderView != null) && m_ColHeaderView.isVisible()) {
       prefHeight += m_ColHeaderView.getPreferredSize().height;
     }
     // 如果树存在,添加树的尺寸
     if (m_rowTreeView != null && m_rowTreeView.isVisible()) {
       prefWidth += m_rowTreeView.getPreferredSize().width;
     }
     if (m_colTreeView != null && m_colTreeView.isVisible()) {
       prefHeight += m_colTreeView.getPreferredSize().height;
     }
     //分配垂直滚动条的空间
     if ( (m_VScrollBar != null) && (nVsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
       if (nVsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
         prefWidth += m_VScrollBar.getPreferredSize().width;
       }
       //判断是否需要显示滚动条空间,如果是拆分窗口的情况,在所有的视图中如果有一个无法显示所有的信息;
       //那么就需要添加对应的滚动条.
       else if ( (viewSize != null) && (extentSize != null)) {
         boolean canScroll = ! ( (Scrollable) view).
             getScrollableTracksViewportHeight();
         if (canScroll && (viewSize.height > extentSize.height)) {
           prefWidth += m_VScrollBar.getPreferredSize().width;
         }
       }
     }
     //显示页签的时候，必须预留和滚动条相同的空间
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
     //如果是分栏表,并且当前的表格没有锁定,那么还应该有相应的分割栏出现.
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
   * 计算容器中组件要求的最小尺寸.
   * @param parent 需要布局的容器
   * @return Dimension
   */
  public Dimension minimumLayoutSize(Container parent) {
    Insets insets = parent.getInsets();
    int minWidth = insets.left + insets.right;
    int minHeight = insets.top + insets.bottom;
    //分栏表对于表格的最小尺寸不会有影响.
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
// 如果树存在,添加树的尺寸
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
      if (m_PageView != null) { //需要标记滑块的宽度
        size = m_SlideBox.getMinimumSize();
        minWidth = Math.max(minWidth, size.width);
      }
    }
    else if (m_PageView != null) { //不显示滚动条，但是显示页签，需要标记页签的高度
      Dimension size = m_PageView.getMinimumSize();
      minHeight += size.height;
    }
    if (m_PageView != null) { //标记页签的宽度
      Dimension size = m_PageView.getMinimumSize();
      minWidth = Math.max(minWidth, size.width);
    }
    return new Dimension(minWidth, minHeight);
  }

  /**
   * 按照以下规则布局容器:对于组件可用空间的分配，自左到右，自上到下。<ol>
   *    <li>非分栏表:如果行标题可见，取它的最佳宽度和主视口的高度；如果列标题可见，取它的最
   * 佳高度和主视口视口的宽度；标题树如果显示,规则和标题的显示相同;垂直滚动条如果显示，规则
   * 类似行标题,水平滚动条和页签宽度的和为主视口的宽度，具体值按照面板中设置的比例分配。在空间
   * 分配过程需要注意的是如果滚动条的策略设置是AsNeed，如果由于水平空间不足而出现滚动条或者
   * 存在页签,会影响纵向空间的尺寸,这是需要重新计算是否需要显示垂直滚动条.
   *   <li>分栏表未锁定:这种情况下,设置滚动条必须出现,空间分配的过程中需要为新添加的组件分配
   * 空间,由于无需计算滚动条的影响,分配相对简单.
   *   <li>锁定的分栏表:和上一种情况相似,只是不需要显示分割栏,被锁定的表格空间的分配必须
   * 保证是整行整列.另外,滚动条的的空间分配中，空间完全分配到第二滚动条上，并且保证第二滚动条
   * 设资的最小值是注视图视口的大小。
   * </ol>
   * @param parent Container 布局的容器
   */
  public void layoutContainer(Container parent) {
    TablePane tablePane = (TablePane) parent;
    //得到布局的区域
    Rectangle availR = tablePane.getBounds();
    availR.x = availR.y = 0;
    //处理边界
    Insets insets = parent.getInsets();
    availR.x = insets.left;
    availR.y = insets.top;
    availR.width -= insets.left + insets.right;
    availR.height -= insets.top + insets.bottom;
    if (availR.width == 0 || availR.height == 0) {
      return;
    }

    //滚动条的滚动规则
    int nVsbPolicy = tablePane.getVScrollBarPolicy();
    int nHsbPolicy = tablePane.getHScrollBarPolicy();

    //首先布局树的空间
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
    /* 如果列标题可视，它的尺寸为指定的高度，任意的宽度。
     */
    Rectangle colHeadR = new Rectangle(0, availR.y, 0, 0);
    if ( (m_ColHeaderView != null) && (m_ColHeaderView.isVisible())) {
      //为列标题分配高度，暂时不分配的的x起点和宽度
      int colHeadHeight = Math.min(availR.height,
                                   m_ColHeaderView.getPreferredSize().height);
      colHeadR.height = colHeadHeight;
      //调整剩余的空间的起始y点
      availR.y += colHeadHeight;
      availR.height -= colHeadHeight;
    }
    /* 如果行标题可见，它的尺寸为指定的宽度，任意的高度
     */
    Rectangle rowHeadR = new Rectangle(availR.x, 0, 0, 0);
    if ( (m_RowHeaderView != null) && (m_RowHeaderView.isVisible())) {
      int rowHeadWidth = Math.min(availR.width,
                                  m_RowHeaderView.getPreferredSize().width);
      //类似列标题的分配，它也是确定了宽度，对于剩余空间作出调整。
      rowHeadR.width = rowHeadWidth;
      availR.width -= rowHeadWidth;
      availR.x += rowHeadWidth;
    }
    //对于分栏表的布局单独处理.
    if (m_rightView != null || m_downView != null) {
      layoutSeperatedPane(availR, rowHeadR, colHeadR, rowTreeR,
                          colTreeR);
      adjustFreezing(tablePane);
      return;
    }

    /* 此时剩余空间需要容纳主视图，滚动条，页签。行标题的高度和起始y点还没有设置，
     * 列标题的宽度和起始x还没有设置，需要计算剩余空间的空间后对它们设置。
     * 首先比较主视图的最佳尺寸和显示尺寸。由于布局时自上而下，所以需要先对于组件的
     * 最佳尺寸布局。我们假设ViewPorts的布局管理器会计算出它的最佳尺寸。
     */
    Component view = m_mainView.getView();
    //主视图容纳组件的尺寸
    Dimension viewPrefSize = view.getPreferredSize();
    //主视图的尺寸
    Dimension extentSize = m_mainView.toViewCoordinates(availR.getSize());
    boolean isEmpty = (availR.width < 0 || availR.height < 0);
    Scrollable sv = (Scrollable) view;
    /* 如果有剩余空间，首先计算滚动条的空间
     */
    Rectangle vsbR = new Rectangle(0, availR.y, 0, 0);
    boolean vsbNeeded;
    //判断是否显示垂直的滚动条
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
    //是否显示水平滚动条
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
    //为页签，滑块，滚动条分配相应的空间
    Rectangle hsbRect = new Rectangle(availR.x, 0, 0, 0);

    if ( ( (m_HScrollBar != null) && hsbNeeded) ||
        (m_PageView != null && m_bShowPagemark)) {
      adjustForHorComponent(hsbNeeded || m_bShowPagemark, availR, hsbRect);
      //如果页签或者水平滚动条被添加，那么可能影响垂直滚动条的设置
      if ( (m_VScrollBar != null) && !vsbNeeded &&
          (nVsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
        extentSize = m_mainView.toViewCoordinates(availR.getSize());
        vsbNeeded = viewPrefSize.height > extentSize.height;
        if (vsbNeeded) {
          adjustForVSB(true, availR, vsbR);
        }
      }
    }
    /* 设置主视图的尺寸
     */
    if (m_mainView != null) {
      m_mainView.setBounds(availR);
      if (sv != null) {
        extentSize = m_mainView.toViewCoordinates(availR.getSize());
        boolean oldHSBNeeded = hsbNeeded;
        boolean oldVSBNeeded = vsbNeeded;
        /*
         * 现在得到了主视图分配的空间，比较视图空间和它容纳的组件，可以决定是否需要显示
         * 滚动条，如此，需要再次分配滚动条的空间
         */
        //是否需要调整垂直滚动条
        if (m_VScrollBar != null && nVsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED) {
          boolean newVSBNeeded = (viewPrefSize.height > extentSize.height);
          if (newVSBNeeded != vsbNeeded) {
            vsbNeeded = newVSBNeeded;
            adjustForVSB(vsbNeeded, availR, vsbR);
            extentSize = m_mainView.toViewCoordinates
                (availR.getSize());
          }
        }
        //是否需要调整水平滚动条
        if (m_HScrollBar != null &&
            nHsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED) {
          boolean newHSBbNeeded = (viewPrefSize.width > extentSize.width);
          if (newHSBbNeeded != hsbNeeded) { //需要显示水平滚动条
            hsbNeeded = newHSBbNeeded;
            if (!m_bShowPagemark) { //如果没有显示页签，需要分配可用的空间
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
     * 主视图的尺寸已经被确定，可以设置各个组件的尺寸
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
     * 可以调整3个组件之间的关系了
     */
    Rectangle hsbR = new Rectangle(0, 0, 0, 0);
    Rectangle slideR = new Rectangle(0, 0, 0, 0);
    Rectangle pageR = new Rectangle(0, 0, 0, 0);
    seperateRoom(hsbRect, pageR, slideR, hsbR, hsbNeeded, m_bShowPagemark);
    /* 一下代码为各个组件设置范围
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
   * 布局拆分窗口的代码
   * @param availR Rectangle
   * @param rowHeadR Rectangle
   * @param colHeadR Rectangle
   * @param rowTreeR Rectangle
   * @param colTreeR Rectangle
   */
  private void layoutSeperatedPane(Rectangle availR,
                                   Rectangle rowHeadR, Rectangle colHeadR,
                                   Rectangle rowTreeR, Rectangle colTreeR) {
    /* 如果有剩余空间，首先计算滚动条的空间
     */
    Rectangle vsbR = new Rectangle(0, availR.y, 0, 0);
    adjustForVSB(true, availR, vsbR);
    //为页签，滑块，滚动条分配相应的空间
    Rectangle hsbRect = new Rectangle(availR.x, 0, 0, 0);
    adjustForHorComponent(true, availR, hsbRect);
    /* 设置主视图的尺寸  */
    Rectangle mainR = new Rectangle(availR);
    /*主视图的尺寸已经被确定，可以设置各个组件的尺寸     */
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
    //以下是分割栏添加空间的空间分配.
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

    //从下面开始,开始处理对于已经分配空间的再次分配,根据分栏的情况和是否锁定,来分配各个组件的空间
    boolean bFreezing = m_Pane.isFreezing();
    CellsPane cellsPane = (CellsPane) m_mainView.getView();
    //首先列标题的分配.
    if (m_rightView != null) {
      if (bFreezing) {
        //得到冻结的列的位置.
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
        //分配分割栏和右侧视图的空间.
        spareroom = colBalkR.width;
        colBalkR.width = BALK;
        seperateRoom(spareroom, colBalkR, colHeadR2, true);
        //分配水平滚动条的空间
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
      //按照列标题的空间来调整列树组件的空间
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
        //得到冻结的列的位置.
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
        //分配分割栏和右侧视图的空间.
        spareroom = rowBalkR.height;
        rowBalkR.height = BALK;
        seperateRoom(spareroom, rowBalkR, rowHeadR2, false);
        //分配垂直滚动条的空间
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
      //重新分配右侧视图的高度
      rightViewR.height = rowHeadR.height;
      //按照行标题的空间来调整列树组件的空间
      rowTreeR2.x = rowTreeR.x;
      rowTreeR2.width = rowTreeR.width;
      rowTreeR.y = rowHeadR.y;
      rowTreeR.height = rowHeadR.height;
      rowTreeR2.y = rowHeadR2.y;
      rowTreeR2.height = rowHeadR2.height;
      rowBalkR.x = rowTreeR.x;
    }
    //如果右下视图存在,调整它的空间
    if (m_rightView != null && m_downView != null) {
      rightDownViewR.x = colHeadR2.x;
      rightDownViewR.width = colHeadR2.width;
      rightDownViewR.y = rowHeadR2.y;
      rightDownViewR.height = rowHeadR2.height;
    }
    //最后,分配水平滚动条和页签的区间.
    Rectangle hsbR = new Rectangle();
    Rectangle pageR = new Rectangle();
    Rectangle slideR = new Rectangle();
    seperateRoom(hsbRect, pageR, slideR, hsbR, true, m_bShowPagemark);
    //以下这个处理是如果冻结窗体,那么右侧或者下侧或者右下侧将成为主视图,需要更换滚动条的空间
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
    //以下开始为各个空间设置它们的范围.
    m_mainView.setBounds(mainR);
    setCompBound(m_RowHeaderView, rowHeadR);
    setCompBound(m_ColHeaderView, colHeadR);
    setCompBound(m_rowTreeView, rowTreeR);
    setCompBound(m_colTreeView, colTreeR);
    setCompBound(m_VScrollBar, vsbR);
    setCompBound(m_HScrollBar, hsbR);
    setCompBound(m_PageView, pageR);
    setCompBound(m_SlideBox, slideR);
    //角落的4个组件
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
    //分割窗体产生的组件。对于由于分割而产生的窗体视图的大小需要控制，由于用户使用中如果视图
    //分配的空间足够大，那么方法setViewPosition会失去作用无法控制组件的显示位置。在拆分窗口
    //并且锁定窗口的情况下，视图分配的空间不可以大于显示需要的空间。
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
    //确保锁定状态下各视图的位置满足分割行列的要求。
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
   * 处理窗口冻结而产生的视图位置的调整。
   */
  private void adjustFreezing(TablePane tablePane) {
    boolean bFreezing = tablePane.isFreezing();
    Dimension viewSize = m_mainView.getViewSize();
    //锁定产生的视图的调整主要是对于滚动条的最小值进行调整和拆分位置的调整。
    int horMin = 0, verMin = 0;
    int verMax = viewSize.height, horMax = viewSize.width;
    if (bFreezing) {
      //这里需要重新计算拆分的位置
      int sepRow = tablePane.getSeperateRow();
      int sepCol = tablePane.getSeperateCol();
      TableHeader rowHeader = (TableHeader) m_RowHeaderView.getView();
      TableHeader colHeader = (TableHeader) m_ColHeaderView.getView();
      //作为第2个滚动条的最小值。
      horMin = colHeader.getModel().getPosition(sepCol);
      verMin = rowHeader.getModel().getPosition(sepRow);
    }
    //首先调整主视图关联的滚动条。
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
      int ext = m_rightView.getExtentSize().width;//zzl将m_rightDownView改为m_rightView
      ext = Math.min(ext, horMax - horMin);
      if (ext > 0) {
        m_hScrollBar2.setValues(value, ext, horMin, horMax);
      }
    }
  }

//做一个组件非空的检查
  private void setCompBound(Component c, Rectangle r) {
    if (c != null) {
      c.setBounds(r);
    }
  }

  /**
   * 将一段空间分配给两个Rectangle,两者的空间只是在需要分配的维度上有差别.
   * @param spareRoom 需要被分配的空间范围
   * @param first 第一个区域
   * @param second 第二个区域
   * @param bHor 分配的纬度.
   */
  public void seperateRoom(int spareRoom, Rectangle first, Rectangle second,
                           boolean bHor) {
    if (spareRoom <= 0) {
      return;
    }
    if (bHor) { //水平分配
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
   * 调整垂直滚动条的空间
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
   *调整页签，滑块，滚动条3者的空间
   * @param wantRoom 是否需要为该区域分配空间
   * @param available Rectangle 可以用来分配的空间
   * @param hsbRect Rectangle 分配区域
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
   * 为页签，滑块，滚动条分配空间
   * @param all Rectangle 可分配的区域
   * @param page Rectangle 页签的空间
   * @param slideBox  Rectangle 滑块的空间
   * @param hsb Rectangle 滚动条的空间
   * @param wantsHSB boolean 是否需要滚动条
   * @param wantsPage boolean 是否需要页签
   * @param pageScale int 页签占据显示空间的比例。这各比例设置只有当各个组件都满足最小尺寸时才生效。
   */
  private void seperateRoom(Rectangle all, Rectangle page, Rectangle slideBox,
                            Rectangle hsb,
                            boolean wantsHSB, boolean wantsPage) {
    if (wantsPage && m_PageView != null && ( (!wantsHSB) || m_HScrollBar == null)) { //只显示页签
      page.setBounds(all);
    }
    else if ( ( (!wantsPage) || m_PageView == null) && wantsHSB && m_HScrollBar != null) { //只显示滚动条
      hsb.setBounds(all);
    }
    else if (wantsPage && wantsHSB && m_PageView != null && m_HScrollBar != null) { //显示滚动条和页签
      int pageScale = m_SlideBox.getPosition();
      int width = all.width;
      int pageWidth = m_PageView.getMinimumSize().width;
      int slideWidth = m_SlideBox.getMinimumSize().width;
      int hscWidth = m_HScrollBar.getMinimumSize().width;
      if (width < (pageWidth + slideWidth + hscWidth)) {
//        throw new IllegalArgumentException("注意，代码有错，布局时发生了问题");
        pageWidth = width;
        slideWidth = 0;
        hscWidth = 0;
      }
      width -= slideWidth;
      int newPageWidth = (int) (width * (pageScale / 100.0));
      //分配空间不可以小于最小的尺寸
      pageWidth = newPageWidth < pageWidth ? pageWidth : newPageWidth;
      //检查滚动条的尺寸是否小于最小尺寸
      int newHscWidth = width - pageWidth;
      if (newHscWidth < hscWidth) {
        pageWidth = width - hscWidth;
      }
      else {
        hscWidth = newHscWidth;
      }
      //开始分配空间
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
   * 保证和容器中需要布局的组件一致
   * @param pane TablePane 关联布局管理器的容器
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
