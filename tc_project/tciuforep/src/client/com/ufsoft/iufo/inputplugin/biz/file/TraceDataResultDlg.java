package com.ufsoft.iufo.inputplugin.biz.file;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.script.extfunc.HRTraceDataResult;
import com.ufsoft.iufo.resource.StringResource;

public class TraceDataResultDlg extends UfoDialog implements ActionListener {
	private static final long serialVersionUID = -5005517090500885308L;
	private Object[][] sourDatas=null;
	private String strTitle=null;
	
	private JTextArea jTextTitle=null;
    private JPanel jContentPane = null;
    private JPanel jBtnPanel = null;
    private JScrollPane jTableScrollPane = null;
    private JScrollPane jTextScrollPane=null;
    private JTable jTable=null;
    private JButton jBtnOK = null;
    
    /**
	 * @i18n miufo00041=一
	 * @i18n miufo00042=二
	 * @i18n miufo00043=三
	 * @i18n miufo00044=四
	 * @i18n miufo00045=五
	 * @i18n miufo00046=六
	 * @i18n miufo00517=七
	 * @i18n miufo00518=八
	 * @i18n miufo00519=九
	 */
    public static void doTest(){
    	HRTraceDataResult result=new HRTraceDataResult();
    	result.setTitle("ABCDEFGABCDEFGABCDEFGABCDEFGABCDEFGABCDEFGABCDEFGABCDEFG");
    	
    	Object[][] rowDatas=new Object[5][9];
    	rowDatas[0][0]=StringResource.getStringResource("miufo00041");
    	rowDatas[0][1]=StringResource.getStringResource("miufo00042");
    	rowDatas[0][2]=StringResource.getStringResource("miufo00043");
    	rowDatas[0][3]=StringResource.getStringResource("miufo00044");
    	rowDatas[0][4]=StringResource.getStringResource("miufo00045");
    	rowDatas[0][5]=StringResource.getStringResource("miufo00046");
    	rowDatas[0][6]=StringResource.getStringResource("miufo00517");
    	rowDatas[0][7]=StringResource.getStringResource("miufo00518");
    	rowDatas[0][8]=StringResource.getStringResource("miufo00519");
    	
    	for (int i=1;i<rowDatas.length;i++){
    		for (int j=0;j<rowDatas[i].length;j++){
    			rowDatas[i][j]=""+(i*10+j);
    		}
    	}
    	result.setDatas(rowDatas);
    	
    	JDialog dlg=new TraceDataResultDlg(null,result);
    	dlg.setVisible(true);
    }
	
    public TraceDataResultDlg(Container parent,HRTraceDataResult result) {
        super(parent);
        this.sourDatas=result.getDatas();
        this.strTitle=result.getTitle();
        initialize();
    }	
    
    private void initialize() {
    	setTitle(MultiLangInput.getString("miufotableinput0005"));//"报表数据来源查看";
        setSize(660, 550);
        setContentPane(getJContentPane());
        setLocationRelativeTo(getParent());
    }    
    
    private JPanel getJContentPane(){
        if (jContentPane == null) {
            jContentPane = new UIPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJTextScrollPane());
            jContentPane.add(getJTableScrollPane(), null);
            jContentPane.add(getJBtnPanel(), null);
        }
        return jContentPane;
    }
    
    private JScrollPane getJTextScrollPane() {
        if (jTextScrollPane == null) {
        	jTextScrollPane = new UIScrollPane(getTextTitle());
        	jTextScrollPane.setBounds(18,15,620,50);
        }
        return jTextScrollPane;
    }
    
    private JScrollPane getJTableScrollPane() {
        if (jTableScrollPane == null) {
        	jTableScrollPane = new UIScrollPane(getJTable());
        	jTableScrollPane.setBounds(18, 85, 620,380);
        }
        return jTableScrollPane;
    }
    
    private JTextArea getTextTitle(){
    	if (jTextTitle==null){
    		jTextTitle=new JTextArea();
    		jTextTitle.setText(strTitle==null?strTitle:strTitle);
    	}
    	return jTextTitle;
    }
    
    private JPanel getJBtnPanel() {
        if (jBtnPanel == null) {
            jBtnPanel = new UIPanel();
            jBtnPanel.setBounds(310, 490, 80, 38);
            jBtnPanel.add(getJBtnOK(), null);
        }
        return jBtnPanel;
    }
    
    private JTable getJTable(){
    	if (jTable==null){
    		jTable=new UITable();
    		jTable.setAutoCreateColumnsFromModel(false);
    		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    		jTable.setModel(new TraceResultTableModel());
    		
    		for (int i=0;i<sourDatas[0].length;i++){
    			TableColumn col=new TableColumn(i,150);
    			col.setResizable(true);
    			jTable.addColumn(col);
    		}  		
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
	
	public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton) {
            JButton source = (JButton) event.getSource();
            if (source == jBtnOK) { //确定按钮的处理
                setResult(UfoDialog.ID_OK);
                this.close();
                return;
            }
        }
	}
	
	private class TraceResultTableModel extends AbstractTableModel{
		private static final long serialVersionUID = -4120699655158029969L;

		public int getColumnCount() {
			return sourDatas[0].length;
		}
		
		public String getColumnName(int column) {
			if (column>=sourDatas[0].length)
				return "";
				
			return ""+sourDatas[0][column];
		}

		public int getRowCount() {
			return sourDatas.length-1;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex+1>=sourDatas.length)
				return "";
			
			if (columnIndex>=sourDatas[rowIndex+1].length)
				return "";
			
			return sourDatas[rowIndex+1][columnIndex];
		}
	}
}
 