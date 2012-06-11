/*
 * GotoDialog.java
 * �������� 2004-11-15
 * Created by CaiJie
 */
package com.ufsoft.report.sysplugin.location;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.IInputValidator;
import com.ufsoft.report.dialog.InputDialog;
import com.ufsoft.report.dialog.InputItem;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.TableUtilities;

/**
 * ��λ�Ի���
 * @author caijie 
 * @since 3.1
 */
public class GotoDialog extends InputDialog {
    private UfoReport m_Report;
    /**
     * ��λ������
     */
    private InputItem m_gotoItem = null;
    /**
     * @param rep
     */
    public GotoDialog(UfoReport rep) {
        super(rep, " ", true);
		m_Report = rep;
        init();
    }

    /**
     * ��ʾģʽ�Ի���
     */
    public void show() {
        //����ʾ��ť���ϵ�ϸ��
        pack();
        super.getButtonBar().showTopLine(false);
        setLocationRelativeTo(m_Report);  
        super.show(); 
    }

    private void init() {
        //��λ������    
        m_gotoItem = new InputItem("ReportToolGoto",MultiLang.getString("uiuforep0000722"),new GotoValidator());//"����λ��"
        int itemWidth = 150;
        m_gotoItem.getInputField().setPreferredSize(new Dimension(itemWidth,m_gotoItem.getInputField().getPreferredSize().height));
        
        //��ӱ�ǩ���ͼ���ӵ��Ի���
        this.addInputItem(m_gotoItem);
        this.setTitle(MultiLang.getString("uiuforep0000727"));//��Ԫ��λ
        // @edit by wangyga at 2009-10-22,����01:32:23
        SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				m_gotoItem.getInputField().requestFocus();
			}
        });
    }
    
    /**
     * ���ض�λֵ
     * @param param
     * @return String
     */
    public String getText(String param){
        
        if(m_gotoItem == null) return null;
        
        return m_gotoItem.getText();
    }
    
   
    /**
     * ��λ��֤��
     * @author caijie 
     * @since 3.1
     */
    private class GotoValidator implements IInputValidator {     
        
    	/*
         * @see com.ufsoft.report.dialog.IInputValidator#isValid(java.lang.String)
         */
        public boolean isValid(String input) {
            if((input == null ) || input.length() == 0 )return false;
            AreaPosition area = TableUtilities.getAreaPosByString(input);
            if(area == null) {
            	// IUFOLogger.getLogger(this).fatal("������Ч,�Ƿ��Ķ�λ�ַ���");
                UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000855"), m_Report, null);//"������Ч"
                return false;
            }
            
            return true;
        }
    }    
   
}