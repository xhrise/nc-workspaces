package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.report.util.MultiLang;

public class MultiValueSelectDlg extends UfoDialog implements ActionListener{

	private static final long serialVersionUID = -5005517090500885308L;
	private IFormulaTraceValueItem[] m_oFormulaTraceValueItems = null;
	private IFormulaTraceValueItem m_oSelectedValueItem = null;
	
	private JLabel jLabel = null;
    private JPanel jContentPane = null;
    private JPanel jBtnPanel = null;
    private JScrollPane jScrollPane = null;
    private JTable jTable=null;
    private JButton jBtnOK = null;
    private JButton jBtnCancel = null;
	
    public MultiValueSelectDlg(Container parent,IFormulaTraceValueItem[] formulaTraceValueItems) {
        super(parent);
        this.m_oFormulaTraceValueItems = formulaTraceValueItems;
        initialize();
    }	
    public IFormulaTraceValueItem getSelectedValueItem(){
    	return m_oSelectedValueItem;
    }
    private void setSelectedValueItem(IFormulaTraceValueItem selectedValueItem){
    	this.m_oSelectedValueItem = selectedValueItem;
    }
    /**
	 * @i18n uiuforep00013=公式追踪查看来源选择
	 */
    private void initialize() {
    	setTitle(MultiLangInput.getString(MultiLang.getString("uiuforep00013")));//"公式追踪查看来源选择";//TODO
        setLocation(220,80);
        setSize(632, 532);
        setContentPane(getJContentPane());
    }    
    
    private JPanel getJContentPane(){
        if (jContentPane == null) {
            jContentPane = new UIPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJLabel(), null);
            jContentPane.add(getJScrollPane(), null);
            jContentPane.add(getJBtnPanel(), null);
        }
        return jContentPane;
    }
    /**
	 * @i18n uiuforep00014=请选择要追踪的一项值:
	 */
    private JLabel getJLabel(){
        if (jLabel == null) {
        	jLabel = new UILabel(MultiLang.getString("uiuforep00014"));//TODO 请选择要追踪的一项值:
        	jLabel.setBounds(18, 10, 150,26);
        }
        return jLabel;    	
    }
    
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new UIScrollPane(getJTable());
            jScrollPane.setBounds(18, 36, 590,440);
            jScrollPane.setViewportView(getJTable());
        }
        return jScrollPane;
    }
    
    private JPanel getJBtnPanel() {
        if (jBtnPanel == null) {
            jBtnPanel = new UIPanel();
            jBtnPanel.setBounds(270, 478, 280, 38);
            jBtnPanel.add(getJBtnOK(), null);
            jBtnPanel.add(getJBtnCancel(), null);
        }
        return jBtnPanel;
    }
    
    private JTable getJTable(){
    	if (jTable==null){
    		jTable=new UITable();
    		
    		jTable.setAutoCreateColumnsFromModel(false);
    		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    		jTable.setModel(new MultiValueTableModel(m_oFormulaTraceValueItems));
    		
    		int nColCount = MultiValueTableModel.doCalColCount(m_oFormulaTraceValueItems);
    		for (int i=0;i<nColCount;i++){
    			TableColumn col=new TableColumn(i,100);
    			col.setResizable(true);
    			jTable.addColumn(col);
    		}
    	    jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	    jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    	}
    	return jTable;
    }
    
    private JButton getJBtnOK() {
        if (jBtnOK == null) {
            jBtnOK = new nc.ui.pub.beans.UIButton();
            jBtnOK.setText(MultiLangInput.getString("uiufotableinput0013"));//"确定";
            jBtnOK.addActionListener(this);
            jBtnOK.setBounds(0,0,40,20);
        }
        return jBtnOK;
    }
    /**
	 * @i18n cancel=取消
	 */
    private JButton getJBtnCancel() {
        if (jBtnCancel == null) {
        	jBtnCancel = new nc.ui.pub.beans.UIButton();
        	jBtnCancel.setText(MultiLangInput.getString(MultiLang.getString("cancel")));//"取消";TODO
        	jBtnCancel.addActionListener(this);
        	jBtnCancel.setBounds(60,0,40,20);
        }
        return jBtnCancel;
    }
	
	/**
	 * @i18n uiuforep00015=必须选择一个值才能追踪
	 */
	public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton) {
            JButton source = (JButton) event.getSource();
            if (source == jBtnOK) { //确定按钮的处理
                setResult(UfoDialog.ID_OK);
                //set curSelctValue
                int nSelectedIndex = getJTable().getSelectedRow();
                if(nSelectedIndex < 0){
                	UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00015"), this);//必须选择一个值才能追踪 TODO
                	return;
                }
                IFormulaTraceValueItem curSelectedValueItem = m_oFormulaTraceValueItems[nSelectedIndex];
                setSelectedValueItem(curSelectedValueItem);
                this.close();
                return;
            }else if (source == jBtnCancel) { //取消按钮的处理
                setResult(UfoDialog.ID_CANCEL);
                this.close();
                return;
            }
        }
	}

}
 