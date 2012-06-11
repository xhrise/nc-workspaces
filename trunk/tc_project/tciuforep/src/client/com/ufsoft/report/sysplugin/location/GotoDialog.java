/*
 * GotoDialog.java
 * 创建日期 2004-11-15
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
 * 定位对话框
 * @author caijie 
 * @since 3.1
 */
public class GotoDialog extends InputDialog {
    private UfoReport m_Report;
    /**
     * 定位输入项
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
     * 显示模式对话框
     */
    public void show() {
        //不显示按钮栏上的细线
        pack();
        super.getButtonBar().showTopLine(false);
        setLocationRelativeTo(m_Report);  
        super.show(); 
    }

    private void init() {
        //定位输入项    
        m_gotoItem = new InputItem("ReportToolGoto",MultiLang.getString("uiuforep0000722"),new GotoValidator());//"引用位置"
        int itemWidth = 150;
        m_gotoItem.getInputField().setPreferredSize(new Dimension(itemWidth,m_gotoItem.getInputField().getPreferredSize().height));
        
        //添加标签名和检测子到对话框
        this.addInputItem(m_gotoItem);
        this.setTitle(MultiLang.getString("uiuforep0000727"));//单元格定位
        // @edit by wangyga at 2009-10-22,下午01:32:23
        SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				m_gotoItem.getInputField().requestFocus();
			}
        });
    }
    
    /**
     * 返回定位值
     * @param param
     * @return String
     */
    public String getText(String param){
        
        if(m_gotoItem == null) return null;
        
        return m_gotoItem.getText();
    }
    
   
    /**
     * 定位验证子
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
            	// IUFOLogger.getLogger(this).fatal("引用无效,非法的定位字符串");
                UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000855"), m_Report, null);//"引用无效"
                return false;
            }
            
            return true;
        }
    }    
   
}