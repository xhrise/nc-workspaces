package com.ufsoft.report.dialog;

/**
 * <p>Title: </p>
 * <p>Description: �ı�ϸ�ڶԻ���</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: ������������ɷ����޹�˾</p>
 * @author CaiJie
 * @version 1.0
 */


import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import nc.ui.pub.beans.UITextPane;

/**
 * �ı�ϸ�ڶԻ����ṩһ���Ի����JTextPane��һ��ϸ�������JTextPane,�û����Գ�����
 *ָ������JTextPane�����ݡ�
 */

public class TextDetailDialog
    extends DetailDialog {
  /**
   * ��ȡϸ�������JTextPane
   * @return JTextPane
   */
  public JTextPane getDetailTextPane() {
    return m_tpDetail;
  }

  /**
   * ���캯��
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
   * ��ȡ�Ի������������JTextPane
   * @return JTextPane
   */
  public JTextPane getDialogTextPane() {
    return m_tpDialog;
  }

  /**
   * �ֱ����JTextPane��ϸ�����ͶԻ�����岢�����䲻�����ֹ��༭
   */
  private void initTextPane() {
    m_tpDialog = new UITextPane();
    m_tpDetail = new UITextPane();

    //�������ֹ��༭
    m_tpDialog.setEditable(false);
    m_tpDetail.setEditable(false);

    //���ñ�����ɫ
    m_tpDialog.setBackground(this.getBackground());
    m_tpDetail.setBackground(this.getBackground());

    m_pnlDialogArea = super.getDialogArea();
    m_pnlDetailArea = super.getDetailArea();

    m_pnlDialogArea.add(m_tpDialog);
    m_pnlDetailArea.add(m_tpDetail);
    m_pnlDetailArea.setViewportView(m_tpDetail);
  }

  /**
   * �Ի�������JTextPane
   */
  private JTextPane m_tpDialog;

  /**
   * �Ի���ϸ��JTextPane
   */
  private JTextPane m_tpDetail;

  /**
   * �������
   */
  private JPanel m_pnlDialogArea;

  /**
   * ϸ�����
   */
  private JScrollPane m_pnlDetailArea;
}
