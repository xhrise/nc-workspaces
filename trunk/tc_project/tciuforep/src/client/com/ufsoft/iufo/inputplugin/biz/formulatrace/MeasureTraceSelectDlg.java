package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import nc.vo.iufo.data.MeasureTraceVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.iufo.pub.tools.FileUtil;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.sysplugin.xml.ExtNameFileFilter;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.iufo.resource.StringResource;

public class MeasureTraceSelectDlg extends UfoDialog implements ActionListener{

	private JButton btnExp;
	private static final long serialVersionUID = -5005517090500885308L;
	private MeasureTraceVO[] measureTracesVOs = null;
	private MeasureTraceVO m_oSelectedValueItem = null;
	
	private JLabel jLabel = null;
    private JPanel jContentPane = null;
    private JPanel jBtnPanel = null;
    private JScrollPane jScrollPane = null;
    private JTable jTable=null;
    private JButton jBtnOK = null;
    private JButton jBtnCancel = null;
	
    public MeasureTraceSelectDlg(Container parent,MeasureTraceVO[] measureTracesVOs) {
        super(parent);
        this.measureTracesVOs = measureTracesVOs;
        initialize();
    }	
    public MeasureTraceVO getSelectedValueItem(){
    	return m_oSelectedValueItem;
    }
    private void setSelectedValueItem(MeasureTraceVO selectedValueItem){
    	this.m_oSelectedValueItem = selectedValueItem;
    }
    /**
	 * @i18n uiuforep00013=公式追踪查看来源选择
	 */
    private void initialize() {
    	setTitle(MultiLang.getString("uiuforep00013"));//"公式追踪查看来源选择";//TODO
        setLocation(220,80);
        setSize(632, 582);
        setContentPane(getJContentPane());
    }    
    
    private JPanel getJContentPane(){
        if (jContentPane == null) {
            jContentPane = new UIPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJLabel(), null);
            jContentPane.add(getJScrollPane(), null);
            jContentPane.add(getJBtnPanel());
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
            jBtnPanel.setLayout(null);
            jBtnPanel.setBounds(16, 478, 534, 38);
            jBtnPanel.add(getJBtnOK());
            jBtnPanel.add(getJBtnCancel());
            jBtnPanel.add(getButton());
        }
        return jBtnPanel;
    }
    
    private JTable getJTable(){
    	if (jTable==null){
    		jTable=new UITable();
    		
    		jTable.setAutoCreateColumnsFromModel(false);
    		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    		MeasureTraceTableModel model = new MeasureTraceTableModel(measureTracesVOs);
			jTable.setModel(model);
    		
    		int nColCount = model.getColumnCount();//MeasureTraceTableModel.doCalColCount(measureTracesVOs);
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
            jBtnOK.setBounds(298,9,75,20);
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
        	jBtnCancel.setBounds(400,9,75,20);
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
                MeasureTraceVO curSelectedValueItem = measureTracesVOs[nSelectedIndex];
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
	/**
	 * @return
	 * @i18n miufo00993=导出数据
	 */
	protected JButton getButton() {
		if (btnExp == null) {
			btnExp = new JButton();
			btnExp.addActionListener(new ActionListener() {
				/**
				 * @i18n miufo00992=导出成功！
				 */
				public void actionPerformed(final ActionEvent e) {
					
				    JFileChooser chooser = new UIFileChooser();
					ExtNameFileFilter xf = new ExtNameFileFilter("csv");
					chooser.setFileFilter(xf);
					chooser.setMultiSelectionEnabled(false);		
					int returnVal = chooser.showSaveDialog(jContentPane);
					File file = null;
					if(returnVal == JFileChooser.APPROVE_OPTION){
						file = chooser.getSelectedFile();
						file = xf.getModifiedFile(file);
						
					//	FileOutputStream stream =  new FileOutputStream(file);
						 
					}
					if(file == null){
						return;
					}
					 
					expModel(file);
					
					UfoPublic.sendMessage(StringResource.getStringResource("miufo00992"), jContentPane);
					
				}
			});
			btnExp.setText(StringResource.getStringResource("miufo00993"));
			btnExp.setBounds(22, 4, 106, 28);
		}
		return btnExp;
	}
	
	
	private void expModel(File file){
		MeasureTraceTableModel model  = (MeasureTraceTableModel) jTable.getModel();
		int col = model.getColumnCount();
		int row = model.getRowCount();
		String strFile = "";
		
		for (int j = 0; j < col; j++) {
			strFile += model.getColumnName(j);
			if(j < col - 1){
				strFile += ",";
			}
		}
		strFile += "\n";
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Object obj = model.getValueAt(i, j);
				if(obj != null){
					strFile += obj;
				}
				if(j < col - 1){
					strFile += ",";
				}
			}
			strFile += "\n";
		}
		
		String path = file.getAbsolutePath();
		 
		PrintWriter out1 = null;
		try {
			out1 = new PrintWriter(new BufferedWriter(new FileWriter(path)));
			out1.print(strFile);
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());

		} finally {
			if (out1 != null)
				out1.close();
		}
//		 
//		PrintWriter out1;
//		try {
//			out1 = new PrintWriter(new BufferedWriter(new FileWriter(file)));
//			out1.print(strFile);
//			
//		} catch (IOException e) {
//			AppDebug.debug(e);
//			UfoPublic.showErrorDialog(this, "文件保存失败！", "");
//		}
		
	}

}
  