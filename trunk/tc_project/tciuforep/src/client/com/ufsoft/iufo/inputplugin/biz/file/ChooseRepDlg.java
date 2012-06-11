/*
 * 创建日期 2006-4-13
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.dialog.UfoDialog;

public class ChooseRepDlg extends UfoDialog implements ActionListener,ListSelectionListener{
	private static final long serialVersionUID = 6502919280824518573L;

	private JPanel jPanelContents = null;
    private JPanel jPanelReports = null;
    private JPanel jPanelBtn = null;
    private JButton jBtnCancel = null;
    private JButton jBtnOK = null;
    private JScrollPane jScrollPaneList = null;
    private JList jListReports = null;
    
    
    private ChooseRepData[] m_oChooseRepDatas = null;
    private String m_strSelReportPK = null;

    /**
     * This is the default constructor
     */
    public ChooseRepDlg(Container parent,ChooseRepData[] chooseRepDatas) {
        super(parent);
        m_oChooseRepDatas = chooseRepDatas;
        
        initialize();
    }
    private ChooseRepData[] getChooseRepDatas(){
        return m_oChooseRepDatas;
    }
    public  String getSeledReportPK(){
        return m_strSelReportPK ;
    }
    
    private void setSelReportPK(String strSelReportPK){
        this.m_strSelReportPK = strSelReportPK;
    }
    
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setLocation(220,80);
        
        setTitle(MultiLangInput.getString("uiufotableinput0001"));//"打开报表";
        this.setSize(632, 574);
        this.setContentPane(getJPanelContents());
    }

    /**
     * This method initializes jPanel   
     *  
     * @return javax.swing.JPanel   
     */    
    private JPanel getJPanelContents() {
        if (jPanelContents == null) {
            jPanelContents = new UIPanel();
            jPanelContents.setLayout(null);
            jPanelContents.add(getJPanelReports(), null);
            jPanelContents.add(getJPanelBtn(), null);
        }
        return jPanelContents;
    }
    /**
     * This method initializes jPanel1  
     *  
     * @return javax.swing.JPanel   
     */    
    private JPanel getJPanelReports() {
        if (jPanelReports == null) {
            jPanelReports = new UIPanel();
            jPanelReports.setLayout(null);
            jPanelReports.setBounds(36, 14, 551, 481);
            Border etched = BorderFactory.createEtchedBorder();
            Border title = BorderFactory.createTitledBorder(etched,MultiLangInput.getString("uiufotableinput0001"));//"打开报表";
            jPanelReports.setBorder(title);
            jPanelReports.add(getJScrollPaneList(), null);
        }
        return jPanelReports;
    }

    /**
     * This method initializes jScrollPane  
     *  
     * @return javax.swing.JScrollPane  
     */    
    private JScrollPane getJScrollPaneList() {
        if (jScrollPaneList == null) {
            jScrollPaneList = new UIScrollPane();
            jScrollPaneList.setBounds(5, 18, 539, 460);
            Border etched = BorderFactory.createEtchedBorder();
            Border title = BorderFactory.createTitledBorder(etched,MultiLangInput.getString("uiufotableinput0015"));//"报表列表";
            jScrollPaneList.setBorder(title);
            jScrollPaneList.setViewportView(getJListReports());
        }
        return jScrollPaneList;
    }
    /**
     * This method initializes jList    
     *  
     * @return javax.swing.JList    
     */    
    private JList getJListReports() {
        if (jListReports == null) {
            jListReports = new UIList(getChooseRepDatas());
            jListReports.addListSelectionListener(this);
        }
        return jListReports;
    }
    
    private JPanel getJPanelBtn() {
        if (jPanelBtn == null) {
            jPanelBtn = new UIPanel();
            jPanelBtn.setLayout(null);
            jPanelBtn.setBounds(115, 497, 373, 34);
            jPanelBtn.add(getJBtnCancel(), null);
            jPanelBtn.add(getJBtnOK(), null);
        }
        return jPanelBtn;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */    
    private JButton getJBtnOK() {
        if (jBtnOK == null) {
            jBtnOK = new nc.ui.pub.beans.UIButton();
            jBtnOK.setBounds(94, 7, 75, 22);
            jBtnOK.addActionListener(this);
            jBtnOK.setText(MultiLangInput.getString("uiufotableinput0013"));//"确定";
        }
        return jBtnOK;
    }

    /**
     * This method initializes jBtnCancel	
     * 	
     * @return javax.swing.JButton	
     */   
    private JButton getJBtnCancel() {
        if (jBtnCancel == null) {
            jBtnCancel = new nc.ui.pub.beans.UIButton();
            jBtnCancel.setBounds(228, 7, 75, 22);
            jBtnCancel.addActionListener(this);
            jBtnCancel.setText(MultiLangInput.getString("uiufotableinput0014"));//取消
        }
        return jBtnCancel;
    }

    
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton) {
            JButton source = (JButton) event.getSource();
            if (source == jBtnOK) { //确定按钮的处理
                setResult(UfoDialog.ID_OK);
                if(getSeledReportPK() == null){
                    JOptionPane.showMessageDialog(this,MultiLangInput.getString("uiufotableinput0012")); 
                }else{
                    this.close();
                }
                return;
            } else if (source == jBtnCancel) { //取消按钮的处理
                setResult(UfoDialog.ID_CANCEL);
                this.close();
                return;
            }
        }
    }
    public void valueChanged(ListSelectionEvent e) {
        if(getJListReports().getSelectedValue() != null){
            String strSelReportPK = ((ChooseRepData)getJListReports().getSelectedValue()).getReportPK();        
            setSelReportPK(strSelReportPK);
        }        
    }
}  //  @jve:decl-index=0:visual-constraint="-221,10"

