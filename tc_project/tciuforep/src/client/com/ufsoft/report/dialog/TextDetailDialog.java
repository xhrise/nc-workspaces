package com.ufsoft.report.dialog;

/**
 * <p>Title: </p>
 * <p>Description: 文本细节对话框</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: 北京用友软件股份有限公司</p>
 * @author CaiJie
 * @version 1.0
 */


import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import nc.ui.pub.beans.UITextPane;

/**
 * 文本细节对话框提供一个对话框的JTextPane和一个细节区域的JTextPane,用户可以程序中
 *指定两个JTextPane的内容。
 */

public class TextDetailDialog
    extends DetailDialog {
  /**
   * 获取细节区域的JTextPane
   * @return JTextPane
   */
  public JTextPane getDetailTextPane() {
    return m_tpDetail;
  }

  /**
   * 构造函数
   */
  public TextDetailDialog() {
    super(null);
    initTextPane();
  }
  public TextDetailDialog(Component parent) {
	    super(parent);
	    initTextPane();
	  }

  /**
   * 获取对话框内容区域的JTextPane
   * @return JTextPane
   */
  public JTextPane getDialogTextPane() {
    return m_tpDialog;
  }

  /**
   * 分别添加JTextPane到细节面板和对话框面板并设置其不允许手工编辑
   */
  private void initTextPane() {
    m_tpDialog = new UITextPane();
    m_tpDetail = new UITextPane();

    //不允许手工编辑
    m_tpDialog.setEditable(false);
    m_tpDetail.setEditable(false);

    //设置背景颜色
    m_tpDialog.setBackground(this.getBackground());
    m_tpDetail.setBackground(this.getBackground());

    m_pnlDialogArea = super.getDialogArea();
    m_pnlDetailArea = super.getDetailArea();

    m_pnlDialogArea.add(m_tpDialog);
    m_pnlDetailArea.add(m_tpDetail);
    m_pnlDetailArea.setViewportView(m_tpDetail);
  }

  /**
   * 对话框内容JTextPane
   */
  private JTextPane m_tpDialog;

  /**
   * 对话框细节JTextPane
   */
  private JTextPane m_tpDetail;

  /**
   * 内容面板
   */
  private JPanel m_pnlDialogArea;

  /**
   * 细节面板
   */
  private JScrollPane m_pnlDetailArea;
}
