/**  
 * 创建时间2004-8-1015:28:47
 * @author CaiJie 
 */
package com.ufsoft.report.dialog;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import nc.ui.pub.beans.UITextPane;

import com.ufsoft.report.util.MultiLang;

/**
 * 消息对话框
 */
public class MessageDialog extends BaseDialog {
    /**
     * 对话框内容JTextPane
     */
    private JTextPane m_tpMessage;
    /**
     * 内容面板
     */
    private JPanel m_pnlDialogArea;

    /**
     * 构造器
     */
    public MessageDialog(){
        super(null);
        initialization();        
    }
    public MessageDialog(Component parent){
    	super(parent);
    	initialization();
    }
    
    public void show(){          
        this.setModal(true);        
        this.setResizable(false);
        pack();       
        setLocationRelativeTo(null);
        super.show();
    }
    
    /**
     * 设置显示的消息
     * 创建时间2004-8-18  15:00:49
     * @param message
     */
    public void setMessage(String message){
        m_tpMessage.setText(message);
        m_tpMessage.revalidate();
    }
    
    /**
     * 分别添加JTextPane到细节面板和对话框面板并设置其不允许手工编辑
     */
    private void initialization() {
      this.setTitle(MultiLang.getString("uiuforep0000808"));//消息
      
      m_tpMessage = new UITextPane();   
      
      //不允许手工编辑
      m_tpMessage.setEditable(false);
      
      //设置背景颜色
      m_tpMessage.setBackground(this.getBackground());     
      
      m_pnlDialogArea = getDialogArea();    

      m_pnlDialogArea.add(m_tpMessage);
      
      //将确定按钮置中
      ButtonBar buttonBar = getButtonBar();
      buttonBar.setGroup(buttonBar.getButtonName(getOKButton()),ButtonBar.MIDDLE);
      //将取消按钮不可用
      getCancelButton().setVisible(false);
      //设置默认按钮为确定按钮
      getRootPane().setDefaultButton(getOKButton());
    }

}
