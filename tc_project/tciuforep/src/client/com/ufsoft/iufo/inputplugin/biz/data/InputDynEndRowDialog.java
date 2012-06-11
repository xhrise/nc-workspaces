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
 * 功能: 输入动态区结束行的对话框。
 * 创建日期:(2006-10-20 11:11:37)
 * @author chxiaowei
 */
public class InputDynEndRowDialog extends InputDialog {
    private Container m_Report;
    /**
     * 结束行输入项
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
     * 功能：初始化化对话框，放置输入项到该对话框上。
     * @return
     */
    private void init() {
        m_endRowItem = new InputItem("DynEndRow", StringResource.getStringResource("miufo1002751"), new DynEndRowValidator());
        m_endRowItem.getInputField().setPreferredSize(new Dimension(150,m_endRowItem.getInputField().getPreferredSize().height));
        
        //添加标签名和检测子到对话框
        this.addInputItem(m_endRowItem);
        this.setTitle(StringResource.getStringResource("miufo1002751"));
    }
    
    /**
     * 功能：显示模式对话框，不显示按钮栏上的细线，并这是窗口的显示位置。
     * @return
     */
    public void show() {
        pack();
        super.getButtonBar().showTopLine(false);
        setLocationRelativeTo(m_Report);  
        super.show();       
    }
    
    /**
     * 功能：返回动态区结束行输入值。
     * @param param
     * @return String
     */
    public String getText(){
        if(m_endRowItem == null) return null;
        return m_endRowItem.getText();
    }
    
    /**
     * 功能：动态区结束行位置是否合法的验证器。
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