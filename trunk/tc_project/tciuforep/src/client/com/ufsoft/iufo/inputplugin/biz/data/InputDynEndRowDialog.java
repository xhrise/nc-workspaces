package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.Container;
import java.awt.Dimension;

import nc.util.iufo.pub.UFOString;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.IInputValidator;
import com.ufsoft.report.dialog.InputDialog;
import com.ufsoft.report.dialog.InputItem;
import com.ufsoft.report.util.UfoPublic;

/**
 * ����: ���붯̬�������еĶԻ���
 * ��������:(2006-10-20 11:11:37)
 * @author chxiaowei
 */
public class InputDynEndRowDialog extends InputDialog {
    private Container m_Report;
    /**
     * ������������
     */
    private InputItem m_endRowItem = null;
    
    /**
     * @param report
     */
    public InputDynEndRowDialog(Container report) {
        super(report, " ", true);
		m_Report = report;
        init();
    }
    
    /**
     * ���ܣ���ʼ�����Ի��򣬷���������öԻ����ϡ�
     * @return
     */
    private void init() {
        m_endRowItem = new InputItem("DynEndRow", StringResource.getStringResource("miufo1002751"), new DynEndRowValidator());
        m_endRowItem.getInputField().setPreferredSize(new Dimension(150,m_endRowItem.getInputField().getPreferredSize().height));
        
        //��ӱ�ǩ���ͼ���ӵ��Ի���
        this.addInputItem(m_endRowItem);
        this.setTitle(StringResource.getStringResource("miufo1002751"));
    }
    
    /**
     * ���ܣ���ʾģʽ�Ի��򣬲���ʾ��ť���ϵ�ϸ�ߣ������Ǵ��ڵ���ʾλ�á�
     * @return
     */
    public void show() {
        pack();
        super.getButtonBar().showTopLine(false);
        setLocationRelativeTo(m_Report);  
        super.show();       
    }
    
    /**
     * ���ܣ����ض�̬������������ֵ��
     * @param param
     * @return String
     */
    public String getText(){
        if(m_endRowItem == null) return null;
        return m_endRowItem.getText();
    }
    
    /**
     * ���ܣ���̬��������λ���Ƿ�Ϸ�����֤����
     */
    private class DynEndRowValidator implements IInputValidator {     
        /*
         * @see com.ufsoft.report.dialog.IInputValidator#isValid(java.lang.String)
         */
        public boolean isValid(String input) {
            if((input == null ) || input.length() == 0 )return false;
            if(!UFOString.isInt(input)) {
                UfoPublic.sendErrorMessage(StringResource.getStringResource("ugrpreport00032"), m_Report, null);
                return false;
            } 
            return true;
        }
    }

}