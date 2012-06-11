package com.ufsoft.table.demo;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

/**
 * <p>Title: DemoBudget使用的选择对话框</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ufsoft</p>
 * @author wupeng
 * @version 1.0
 */

public class SelectDialog extends UfoDialog {
  JPanel panel1 = new UIPanel();
  JButton tbnOk = new nc.ui.pub.beans.UIButton();
  JCheckBox cbFormat = new UICheckBox();
  JCheckBox jCheckBox1 = new UICheckBox();
  JTextField tfRow = new UITextField();
  JTextField tfCol = new UITextField();
  JLabel jLabel1 = new nc.ui.pub.beans.UILabel();
  JLabel jLabel2 = new nc.ui.pub.beans.UILabel();
  private int rowNum,colNum;
  private boolean format,split,cancle=true;
  /**
 * @return int
 */
public int getRowNum (){
    return rowNum;
  }
  /**
 * @return int
 */
public int getColNum(){
    return colNum;
  }
  /**
 * @return boolean
 */
public boolean isFormat(){
    return format;
  }
  /**
 * @return boolean
 */
public boolean isSplit(){
    return split;
  }
  /**
 * @return boolean
 */
public boolean isCancle(){
    return cancle;
  }

  /**
 * @param frame
 * @param title
 * @param modal
 */
public SelectDialog(Frame frame, String title, boolean modal) {
    super(frame, title);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
AppDebug.debug(ex);//@devTools       AppDebug.debug(ex);
    }
  }

  /**
 * 
 */
public SelectDialog() {
    this(null, "", false);
  }

  /**
 * @i18n ok=确定
 * @i18n miufo00123=显示格式
 * @i18n area_seperate=分栏
 * @i18n uiuforep0000864=行
 * @i18n Column=列
 * @i18n cancel=取消
 */
private void jbInit() throws Exception {
    panel1.setLayout(null);
    tbnOk.setBounds(new Rectangle(156, 151, 63, 25));
    tbnOk.setText(MultiLang.getString("ok"));
    tbnOk.addActionListener(new SelectDialog_tbnOk_actionAdapter(this));
    this.setResizable(false);
    cbFormat.setText(MultiLang.getString("miufo00123"));
    cbFormat.setBounds(new Rectangle(72, 85, 83, 25));
    jCheckBox1.setText(MultiLang.getString("area_seperate"));
    jCheckBox1.setBounds(new Rectangle(71, 119, 83, 25));
    tfRow.setSelectionEnd(0);
    tfRow.setText("5");
    tfRow.setBounds(new Rectangle(79, 38, 57, 22));
    tfCol.setText("5");
    tfCol.setBounds(new Rectangle(206, 39, 57, 22));
    jLabel1.setText(MultiLang.getString("uiuforep0000864"));
    jLabel1.setBounds(new Rectangle(38, 42, 34, 16));
    jLabel2.setText(MultiLang.getString("Column"));
    jLabel2.setBounds(new Rectangle(164, 43, 34, 16));
    tbnCancle.addActionListener(new SelectDialog_tbnCancle_actionAdapter(this));
    tbnCancle.setText(MultiLang.getString("cancel"));
    tbnCancle.addActionListener(new SelectDialog_tbnCancle_actionAdapter(this));
    tbnCancle.setBounds(new Rectangle(230, 151, 63, 25));
    panel1.setPreferredSize(new Dimension(350, 200));
    getContentPane().add(panel1);
    panel1.add(tfRow, null);
    panel1.add(jLabel1, null);
    panel1.add(tfCol, null);
    panel1.add(jLabel2, null);
    panel1.add(cbFormat, null);
    panel1.add(jCheckBox1, null);
    panel1.add(tbnOk, null);
    panel1.add(tbnCancle, null);
    this.setSize(400,300);
  }

  

  /**
 * @i18n miufo00124=行列数量输入>0的整数
 */
void tbnOk_actionPerformed(ActionEvent e) {
    try {
      rowNum = Integer.parseInt(tfRow.getText());
      colNum = Integer.parseInt(tfCol.getText());
      if (rowNum > 0 && colNum > 0) {
      	cancle = false;
        format = cbFormat.isSelected();
        split = jCheckBox1.isSelected();
        dispose();
      }
    }
    catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this,MultiLang.getString("miufo00124"));
    }
  }
  JButton tbnCancle = new nc.ui.pub.beans.UIButton();

  void tbnCancle_actionPerformed(ActionEvent e) {
    this.dispose();
    cancle = true;
  }
  
  class SelectDialog_tbnOk_actionAdapter implements java.awt.event.ActionListener {
    SelectDialog adaptee;
  
    SelectDialog_tbnOk_actionAdapter(SelectDialog adaptee) {
      this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
      adaptee.tbnOk_actionPerformed(e);
    }
  }
  
  class SelectDialog_tbnCancle_actionAdapter implements java.awt.event.ActionListener {
    SelectDialog adaptee;
  
    SelectDialog_tbnCancle_actionAdapter(SelectDialog adaptee) {
      this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
      adaptee.tbnCancle_actionPerformed(e);
    }
  }

}


 