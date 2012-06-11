/*
 * 创建日期 2006-9-7
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.iufo.fmtplugin.measure.MeasureCheckCellRenderer;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepData;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.dialog.UfoDialog;

public class ImportExcelDataSettingDlg extends UfoDialog  implements ActionListener{

	private static final long serialVersionUID = 2235091383805600296L;
	private JScrollPane jScrollPane = null;
    private JButton jBtnOK = null;
    private JButton jBtnCancel = null;
    private JPanel jContentPane = null;
    
    /**
     * 类变量
     */
    private ImportExcelTableModel m_oTableModel = null;
    private ChooseRepData[] m_oChooseRepDatas = null;
    private List<String[]> m_listResultArray = null;

    public ImportExcelDataSettingDlg(Container parent,ChooseRepData[] chooseRepDatas,Map<String,Object> matchMap) {
        super(parent);
        
        m_oChooseRepDatas = chooseRepDatas;
        initTable(matchMap);  
        
        initialize();
    }
    public List<String[]> getResultArray(){
        return m_listResultArray;
    }
    /**
     * @param matchMap
     */
    private void initTable(Map<String,Object> matchMap) {
        //通过matchMap，得到ImportExcelTableRow[]
        ImportExcelTableRow[] importExcelTableRows = null;
        if(matchMap != null && matchMap.size() > 0){
            //设置列标题
            String[] mapKeys = new String[matchMap.size()];
            Set<String> keySet = matchMap.keySet();
            keySet.toArray(mapKeys);
            importExcelTableRows = new ImportExcelTableRow[mapKeys.length];
            for(int i = 0; i < importExcelTableRows.length ; i++ ){
                //sheetname=repcode
                ChooseRepData chooseRepData = ImportExcelDataBizUtil.getCurRepDataByCode(m_oChooseRepDatas,mapKeys[i]);
                importExcelTableRows[i] = new ImportExcelTableRow(chooseRepData,mapKeys[i]);                
            }
        }
        this.m_oTableModel = new ImportExcelTableModel(importExcelTableRows);
    }

    /**
     * This method initializes jContentPane 
     *  
     * @return javax.swing.JPanel   
     */    
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJScrollPane(), null);
            jContentPane.add(getJBtnOK(), null);
            jContentPane.add(getJBtnCancel(), null);
        }
        return jContentPane;
    }
    
    /**
    *   内部变量
    */
    private JTable m_oTable = null;

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setLocation(220,80);
        this.setSize(494, 389);
        this.setContentPane(getJContentPane());
        m_oTableModel.addMouseListener(m_oTable);
        setTitle(MultiLangInput.getString("uiufotableinput0016"));//"导入Excel匹配动态区域设置";
    }
    /**
     * This method initializes jScrollPane  
     *  
     * @return javax.swing.JScrollPane  
     */    
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
//          jScrollPane = new JScrollPane();
//          jScrollPane.setBounds(27, 23, 467, 289);
            
            m_oTable = new nc.ui.pub.beans.UITable();
            m_oTable.setAutoCreateColumnsFromModel(false);
            m_oTable.setModel(m_oTableModel);

            jScrollPane = new UIScrollPane(m_oTable);
            jScrollPane.setBounds(27, 23, 467, 289);
            jScrollPane.setName("JScrollPane");
            for (int k = 0,kCount = m_oTableModel.getHeads().length; k < kCount; k++) {
                TableCellRenderer renderer;
                DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
                renderer = textRenderer;

                TableCellEditor editor = null;
                int nWidth = 50;
                if(k == ImportExcelTableModel.COL_SELECT){
                    nWidth = 30;
                    renderer = new MeasureCheckCellRenderer();
                    editor = new DefaultCellEditor(new UICheckBox());
                }else if(k == ImportExcelTableModel.COL_DYNAREA_ENDROW){
                    editor = new DefaultCellEditor(new UITextField());
                }
                TableColumn column = new TableColumn(k, nWidth, renderer, editor);
                m_oTable.addColumn(column);
            }
            //报表选择列表
            JComboBox iufoexcelComboBox = new UIComboBox(); 
            for(int i = 0,iCount = m_oChooseRepDatas.length; i < iCount ; i++ ){
                iufoexcelComboBox.addItem(m_oChooseRepDatas[i]);                
            }
          
            TableColumnModel tcm = m_oTable.getColumnModel();
            TableColumn typetc = tcm.getColumn(ImportExcelTableModel.COL_IUFO_REPS);
            typetc.setCellEditor(new DefaultCellEditor(iufoexcelComboBox));
        }
        return jScrollPane;
    }

    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */    
    private JButton getJBtnOK() {
        if (jBtnOK == null) {
            jBtnOK = new JButton();
            jBtnOK.setBounds(156, 329, 70, 26);
            jBtnOK.setText(MultiLangInput.getString("uiufotableinput0013"));//"确定"
            jBtnOK.addActionListener(this);
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
            jBtnCancel = new JButton();
            jBtnCancel.setBounds(296, 329, 70, 26);
            jBtnCancel.setText(MultiLangInput.getString("uiufotableinput0014"));//取消
            jBtnCancel.addActionListener(this);
        }
        return jBtnCancel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getJBtnCancel()) {
            this.setResult(UfoDialog.ID_CANCEL);
            close();
        } else
            if (e.getSource() == getJBtnOK()) {
                //设置数据处理定义的排序
                ImportExcelTableRow[] importExcelTableRows = m_oTableModel.getRows();
                //{sheetname,repcode,dynendrow}
                m_listResultArray = new ArrayList<String[]>();
                if(importExcelTableRows != null){
	                int iSize = importExcelTableRows.length;
	                for(int i =0;i < iSize;i++){
	                    if(importExcelTableRows[i].isSelected()){
	                        String[] strInfos = new String[3];
	                        strInfos[0] = importExcelTableRows[i].getSheetName();
	                        strInfos[1] = importExcelTableRows[i].getRepCode();
	                        strInfos[2] = Integer.toString(importExcelTableRows[i].getDynAreaEndRow());
	                        m_listResultArray.add(strInfos);
	                    }
	                }                
                }
                setResult(UfoDialog.ID_OK);
                close();
            }
    }

}
