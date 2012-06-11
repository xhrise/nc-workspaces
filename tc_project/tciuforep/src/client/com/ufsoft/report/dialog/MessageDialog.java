/**  
 * ����ʱ��2004-8-1015:28:47
 * @author CaiJie 
 */
package com.ufsoft.report.dialog;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import nc.ui.pub.beans.UITextPane;

import com.ufsoft.report.util.MultiLang;

/**
 * ��Ϣ�Ի���
 */
public class MessageDialog extends BaseDialog {
    /**
     * �Ի�������JTextPane
     */
    private JTextPane m_tpMessage;
    /**
     * �������
     */
    private JPanel m_pnlDialogArea;

    /**
     * ������
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
     * ������ʾ����Ϣ
     * ����ʱ��2004-8-18  15:00:49
     * @param message
     */
    public void setMessage(String message){
        m_tpMessage.setText(message);
        m_tpMessage.revalidate();
    }
    
    /**
     * �ֱ����JTextPane��ϸ�����ͶԻ�����岢�����䲻�����ֹ��༭
     */
    private void initialization() {
      this.setTitle(MultiLang.getString("uiuforep0000808"));//��Ϣ
      
      m_tpMessage = new UITextPane();   
      
      //�������ֹ��༭
      m_tpMessage.setEditable(false);
      
      //���ñ�����ɫ
      m_tpMessage.setBackground(this.getBackground());     
      
      m_pnlDialogArea = getDialogArea();    

      m_pnlDialogArea.add(m_tpMessage);
      
      //��ȷ����ť����
      ButtonBar buttonBar = getButtonBar();
      buttonBar.setGroup(buttonBar.getButtonName(getOKButton()),ButtonBar.MIDDLE);
      //��ȡ����ť������
      getCancelButton().setVisible(false);
      //����Ĭ�ϰ�ťΪȷ����ť
      getRootPane().setDefaultButton(getOKButton());
    }

}
