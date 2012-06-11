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
 * <p>Title: ҳǩ��ť��ҳǩ�����</p>
 * <p>Description: ��ʾҳǩ��ҳǩ���İ�ť</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public class PageMarkWithButton
    extends JComponent {
  /**
   * ������Ҫ��ӵİ�ť
   */
  private JButton btnLeft = new nc.ui.pub.beans.UIButton();
  private JButton btnRight = new nc.ui.pub.beans.UIButton();
  private JButton btnHome = new nc.ui.pub.beans.UIButton();
  private JButton btnEnd = new nc.ui.pub.beans.UIButton();
  JButton[] allBtns = {
      btnHome, btnLeft, btnRight, btnEnd};
  /**
   * ��ťͼ���ļ���λ��
   */
  private String[] pics = {
      "reportcore/home.gif", "reportcore/left.gif", "reportcore/right.gif",
      "reportcore/end.gif"};
  /**
   * ҳǩ���
   */
  private PageMark pagemark = new PageMark();
  private JViewport pageMarkView = new UIViewport();
  /**
   * ҳǩ����ѳߴ�
   */
  private Dimension preferSize = new Dimension(15, 15);
  //*****************************************************
   /**
    * ���캯��
    */
   public PageMarkWithButton() {
     super();
     init();
   }

  /**
   * ���캯��
   * @param strs String[] ��Ҫ��ʾ��ҳǩ����
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
    * �����ʼ��
    */
   private void init() {
     ActionListener listener = new PageButtonListerer();
     for (int i = 0; i < allBtns.length; i++) {
       //���ͼƬ
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
   * ��ǰ����ʹ�õĲ��ֹ�����
   */
  private class PageLayout
      implements LayoutManager {
    //�����ڲ���ֱ�ӷ����ⲿ�����ԣ��÷�����Ч
    public void addLayoutComponent(String name, Component comp) {}

    //������ɾ�������������÷�����Ч
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
      //�õ����ֵ�����
      Rectangle availR = pageContainer.getBounds();
      availR.x = availR.y = 0;
      //����߽�
      Insets insets = parent.getInsets();
      availR.x = insets.left;
      availR.y = insets.top;
      availR.width -= insets.left + insets.right;
      availR.height -= insets.top + insets.bottom;
      Dimension btnSize = btnLeft.getPreferredSize();
      //Ϊ4����ť����ռ�
      for (int i = 0; i < allBtns.length && availR.width > 0; i++) {
        Rectangle rBtn = new Rectangle(availR.x, availR.y, btnSize.width,
                                       btnSize.height);
        allBtns[i].setBounds(rBtn);
        //-1Ϊ��ȥ����ť�ļ��
        availR.x += btnSize.width - 1;
        availR.width -= (btnSize.width + 1);
      }
      //Ϊ��ҳǩ��ͼ����ʣ��ռ�
      if (availR.width > 0) {
        pageMarkView.setBounds(availR);

      }

    }
  }
  /**
   * �õ�ҳǩ���
 * @return PageMark
 */
public PageMark getPagemark() {
    return pagemark;
  }

  /**
   * ��ӱ�ҳ
   * @param index int ���λ�á�
   * @param str String �������
   */

  public void addPage(int index, String str) {
    pagemark.insertName(index, str);
    revalidate();
    repaint();
  }

  /**
   * ��ť�¼��ļ�����
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
