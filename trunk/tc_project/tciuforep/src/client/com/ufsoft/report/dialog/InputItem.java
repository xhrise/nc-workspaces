/*
 * InputItem.java
 * 创建日期 2004-11-15
 * Created by CaiJie
 */
package com.ufsoft.report.dialog;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;

import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.util.MultiLang;

/**
 * 一个包含输入标签、输入域和验证子的完整输入项
 * @author caijie
 * @since 3.1
 */
public class InputItem {    
    /**
     * 名称
     */
    private String m_strName = null;
    /**
     * 输入标签
     */
    private JLabel m_label = null;

    /**
     * 输入域
     */
    private JTextField m_tfinput = null;

    /**
     * 检测子
     */
    private IInputValidator m_verifier = null;

  /**
   * 验证子
   * @param name String 名称
   * @param text String 对话框的显示文字
   * @param verifier IInputValidator 验证子
   */
    public InputItem(String name, String text, IInputValidator verifier) {        
        if(name == null){
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000726")); //输入项名称为空
        }            
        m_strName = name;     
        m_label = new com.ufsoft.table.beans.UFOLabel();
        m_label.setText(text);  
        
        m_tfinput = new UITextField();
        
        //左对齐
        m_label.setAlignmentX(0.0f);
        m_tfinput.setAlignmentX(0.0f);
   
        m_tfinput.setMinimumSize( new Dimension(200,m_label.getMinimumSize().height));
        m_tfinput.setPreferredSize(new Dimension(200,m_label.getPreferredSize().height));
        m_verifier = verifier;    
       
    }

    /**
     * 返回标签
     * @return 返回 JLabel。
     */
    public JLabel getLabel() {
        return m_label;
    }
    /**
     * 返回输入文本域
     * @return 返回 JTextField。
     */
    public JTextField getInputField() {
        return m_tfinput;
    }
    /**
     * 返回验证子
     * @return 返回 InputVerifier。
     */
    public IInputValidator getValidator() {
        return m_verifier;
    }
    /**
     * 返回输入域中的值
     * @return String
     */
    public String getText(){
        if(m_tfinput == null) return null;        
        return m_tfinput.getText();
    }
    /**
     * 输入项名称
     * @return 返回 String。
     */
    public String getName() {
        return m_strName;
    }
}