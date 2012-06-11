package com.ufsoft.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JViewport;

import nc.ui.pub.beans.UIViewport;

import com.ufsoft.report.resource.ResConst;

/**
 * <p>Title: 页签按钮和页签的组合</p>
 * <p>Description: 显示页签和页签左侧的按钮</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public class PageMarkWithButton
    extends JComponent {
  /**
   * 所有需要添加的按钮
   */
  private JButton btnLeft = new nc.ui.pub.beans.UIButton();
  private JButton btnRight = new nc.ui.pub.beans.UIButton();
  private JButton btnHome = new nc.ui.pub.beans.UIButton();
  private JButton btnEnd = new nc.ui.pub.beans.UIButton();
  JButton[] allBtns = {
      btnHome, btnLeft, btnRight, btnEnd};
  /**
   * 按钮图象文件的位置
   */
  private String[] pics = {
      "reportcore/home.gif", "reportcore/left.gif", "reportcore/right.gif",
      "reportcore/end.gif"};
  /**
   * 页签组件
   */
  private PageMark pagemark = new PageMark();
  private JViewport pageMarkView = new UIViewport();
  /**
   * 页签的最佳尺寸
   */
  private Dimension preferSize = new Dimension(15, 15);
  //*****************************************************
   /**
    * 构造函数
    */
   public PageMarkWithButton() {
     super();
     init();
   }

  /**
   * 构造函数
   * @param strs String[] 需要显示的页签名称
   */
  public PageMarkWithButton(String[] strs) {
    super();
    if (strs != null) {
      for (int i = 0; i < strs.length; i++) {
        pagemark.insertName(i, strs[i]);
      }
    }
    init();
  }

//************************************************
   /**
    * 对象初始化
    */
   private void init() {
     ActionListener listener = new PageButtonListerer();
     for (int i = 0; i < allBtns.length; i++) {
       //添加图片
       ImageIcon icon = ResConst.getImageIcon(pics[i]);
       allBtns[i].setIcon(icon);
       allBtns[i].setPreferredSize(preferSize);
       allBtns[i].addActionListener(listener);
       this.add(allBtns[i]);
     }
     pageMarkView.setView(pagemark);
     this.add(pageMarkView);
     this.setLayout(new PageLayout());
   }

  /**
   * 当前容器使用的布局管理器
   */
  private class PageLayout
      implements LayoutManager {
    //由于内部类直接访问外部类属性，该方法无效
    public void addLayoutComponent(String name, Component comp) {}

    //不存在删除组件的情况，该方法无效
    public void removeLayoutComponent(Component comp) {}

    public Dimension preferredLayoutSize(Container parent) {
      int prefWidth = btnLeft.getPreferredSize().width * 4 +
          pagemark.getComponentWidth();
      int prefHeight = Math.max(btnLeft.getPreferredSize().height,
                                pagemark.getPreferredSize().height);
      return new Dimension(prefWidth, prefHeight);
    }

    public Dimension minimumLayoutSize(Container parent) {
      return preferredLayoutSize(parent);
    }

    public void layoutContainer(Container parent) {
      PageMarkWithButton pageContainer = (PageMarkWithButton) parent;
      //得到布局的区域
      Rectangle availR = pageContainer.getBounds();
      availR.x = availR.y = 0;
      //处理边界
      Insets insets = parent.getInsets();
      availR.x = insets.left;
      availR.y = insets.top;
      availR.width -= insets.left + insets.right;
      availR.height -= insets.top + insets.bottom;
      Dimension btnSize = btnLeft.getPreferredSize();
      //为4个按钮分配空间
      for (int i = 0; i < allBtns.length && availR.width > 0; i++) {
        Rectangle rBtn = new Rectangle(availR.x, availR.y, btnSize.width,
                                       btnSize.height);
        allBtns[i].setBounds(rBtn);
        //-1为了去除按钮的间隔
        availR.x += btnSize.width - 1;
        availR.width -= (btnSize.width + 1);
      }
      //为了页签视图分配剩余空间
      if (availR.width > 0) {
        pageMarkView.setBounds(availR);

      }

    }
  }
  /**
   * 得到页签组件
 * @return PageMark
 */
public PageMark getPagemark() {
    return pagemark;
  }

  /**
   * 添加表页
   * @param index int 添加位置。
   * @param str String 添加内容
   */

  public void addPage(int index, String str) {
    pagemark.insertName(index, str);
    revalidate();
    repaint();
  }

  /**
   * 按钮事件的监听器
   */

  private class PageButtonListerer
      implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnHome) {
        pagemark.moveHome();
      }
      else if (e.getSource() == btnLeft) {
        int selected = pagemark.getSelected();
        selected = selected == 0 ? 0 : selected - 1;
        pagemark.setSelected(selected);
      }
      else if (e.getSource() == btnRight) {
        int selected = pagemark.getSelected();
        if (selected != pagemark.getPageNum() - 1) {
          selected++;
        }
        pagemark.setSelected(selected);

      }
      else if (e.getSource() == btnEnd) {
        pagemark.moveEnd();

      }
    }
  }
}
