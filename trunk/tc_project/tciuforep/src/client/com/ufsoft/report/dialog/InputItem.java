/*
 * InputItem.java
 * �������� 2004-11-15
 * Created by CaiJie
 */
package com.ufsoft.report.dialog;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.util.MultiLang;

/**
 * һ�����������ǩ�����������֤�ӵ�����������
 * @author caijie
 * @since 3.1
 */
public class InputItem {    
    /**
     * ����
     */
    private String m_strName = null;
    /**
     * �����ǩ
     */
    private JLabel m_label = null;

    /**
     * ������
     */
    private JTextField m_tfinput = null;

    /**
     * �����
     */
    private IInputValidator m_verifier = null;

  /**
   * ��֤��
   * @param name String ����
   * @param text String �Ի������ʾ����
   * @param verifier IInputValidator ��֤��
   */
    public InputItem(String name, String text, IInputValidator verifier) {        
        if(name == null){
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000726")); //����������Ϊ��
        }            
        m_strName = name;     
        m_label = new com.ufsoft.table.beans.UFOLabel();
        m_label.setText(text);  
        
        m_tfinput = new UITextField();
        
        //�����
        m_label.setAlignmentX(0.0f);
        m_tfinput.setAlignmentX(0.0f);
   
        m_tfinput.setMinimumSize( new Dimension(200,m_label.getMinimumSize().height));
        m_tfinput.setPreferredSize(new Dimension(200,m_label.getPreferredSize().height));
        m_verifier = verifier;    
       
    }

    /**
     * ���ر�ǩ
     * @return ���� JLabel��
     */
    public JLabel getLabel() {
        return m_label;
    }
    /**
     * ���������ı���
     * @return ���� JTextField��
     */
    public JTextField getInputField() {
        return m_tfinput;
    }
    /**
     * ������֤��
     * @return ���� InputVerifier��
     */
    public IInputValidator getValidator() {
        return m_verifier;
    }
    /**
     * �����������е�ֵ
     * @return String
     */
    public String getText(){
        if(m_tfinput == null) return null;        
        return m_tfinput.getText();
    }
    /**
     * ����������
     * @return ���� String��
     */
    public String getName() {
        return m_strName;
    }
}