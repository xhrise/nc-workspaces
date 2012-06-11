package com.ufsoft.iufo.fmtplugin.key;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.pub.iufo.accperiod.AccPeriodSchemeUtil;
import nc.pub.iufo.exchangerate.ExchangeRateUtil;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.uapbd.exratescheme.ExrateSchemeVO;

import com.ufsoft.iufo.fmtplugin.measure.DialogRefListener;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * @author yp 2008-10-14
 */
public class NCERateSchemeRefDlg extends UfoDialog implements ActionListener {
    private JPanel ivjIUFODialogContentPane = null;
    private JButton ivjJButtonOk = null;
    private JButton ivjJButtonCancel = null;
    private JScrollPane ivjScrollPane = null;
    private JTable table = null;
    private AccSchemeTable tablemodel = null;
    private ExrateSchemeVO[] vos = null;
    private ExrateSchemeVO selRateScheme;
    public boolean isInitOver = false;

    //�ؼ��ֱ������,
    /**
	 * @i18n miufo1001012=����
	 */
    protected String[] m_aryObjColunmNames = {StringResource.getStringResource("miufo1001012"), StringResource.getStringResource("miufo1001051")};////"����"
    public class AccSchemeTable extends AbstractTableModel {
        //ѡ�ñ�ǵļ�¼Vector
        //private Vector flag = new Vector();
        //���Բ��յĹؼ��ּ���
        private Vector<ExrateSchemeVO> m_RefAccSchemeVec = new Vector();
        public AccSchemeTable(
        		ExrateSchemeVO[] vos) {
            for (int i = 0; i < vos.length; i++) {
            	ExrateSchemeVO vo = (ExrateSchemeVO) vos[i];
                m_RefAccSchemeVec.addElement(vo);
            }
        }
        public int getColumnCount() {
            return m_aryObjColunmNames.length;

        }
        public String getColumnName(int col) {

            return m_aryObjColunmNames[col];
        }
        public int getRowCount() {
            return m_RefAccSchemeVec.size();
        }
        public ExrateSchemeVO getVO(int index) {
            return (ExrateSchemeVO) m_RefAccSchemeVec.get(index);
        }
        public boolean isCellEditable(int row, int col) {
            return false;
        }
        //����ѡ�õĹؼ������ڵ���
        public ExrateSchemeVO getSelRow(int index) {
            return (ExrateSchemeVO) m_RefAccSchemeVec.get(index);
        }
        public void setValueAt(Object obj, int row, int column) {
        	ExrateSchemeVO vo = (ExrateSchemeVO) m_RefAccSchemeVec
                    .get(row);
            switch (column) {
                case 0 :
                    break;
             

                default :
                    break;
            }
        }
        public Object getValueAt(int row, int column) {
        	ExrateSchemeVO vo = (ExrateSchemeVO) m_RefAccSchemeVec
                    .get(row);
            switch (column) {
                case 0 :
                    return vo.getExrateschemecode();
                case 1 :
                    return vo.getExrateschemename();
                default :
                    break;
            }
            return null;
        }
    }
    class AccSchemeCheckCellRenderer extends nc.ui.pub.beans.UICheckBox implements TableCellRenderer {
        //����border
        protected Border m_noFocusBorder;
        /**
         * �ӿ�ʵ�� 
         * 
         * @author��
         */
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            if (value instanceof Boolean) {
                Boolean b = (Boolean) value;
                setSelected(b.booleanValue());
            }

            setBackground(isSelected && !hasFocus ? table
                    .getSelectionBackground() : table.getBackground());
            setForeground(isSelected && !hasFocus ? table
                    .getSelectionForeground() : table.getForeground());

    //      setFont(table.getFont());
            setBorder(hasFocus
                    ? nc.ui.pub.beans.UIManager
                            .getBorder("Table.focusCellHighlightBorder")
                    : m_noFocusBorder);

            return this;
        }
    }
    /**
     * NCAccSchemeRefDlg ������ע�⡣
     */
    public NCERateSchemeRefDlg() {
    	vos= ExchangeRateUtil.getInstance().getAllExrateSchemeVOs();
        //groupVO = groupVo;
        initialize();
    }
    /**
     * �˴����뷽�������� 
     * 
     * @param event
     *            java.awt.event.ActionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent event) {
        if (event.getSource() == ivjJButtonOk) {
            int selIndex = table.getSelectedRow();
            selRateScheme = tablemodel.getSelRow(selIndex);
            setResult(ID_OK);
            if(getParent() instanceof DialogRefListener){
               ((DialogRefListener) this.getParent()).onRef(event);
            }else {//��ʽ�йؼ��ֲ�����ӵķ�֧��
               close();
            }
        } else {
            setResult(ID_CANCEL);
            if(getParent() instanceof DialogRefListener)
            ((DialogRefListener) getParent()).beforeDialogClosed(this);
            close();
        }
    }
    /**
     * �˴����뷽�������� 
     */
    protected void customInit() {
    }
    public void dispose() {
        setResult(ID_CANCEL);
        if(getParent() instanceof DialogRefListener){   
            ((DialogRefListener) getParent()).beforeDialogClosed(this);     
        }
        super.dispose();
    }
    /**
     * ���� IUFODialogContentPane ����ֵ��
     * 
     * @return JPanel
     * @i18n miufo01033=����ڼ䷽���б�
     */
    /* ���棺�˷������������ɡ� */
    private JPanel getIUFODialogContentPane() {
        if (ivjIUFODialogContentPane == null) {
            try {
                ivjIUFODialogContentPane = new UIPanel();
                ivjIUFODialogContentPane.setName("IUFODialogContentPane");
                ivjIUFODialogContentPane.setLayout(null);
                getIUFODialogContentPane().add(getScrollPane(),
                        getScrollPane().getName());
                //getIUFODialogContentPane().add(getLabel(),
                // getLabel().getName());
                getIUFODialogContentPane().add(getJButtonOk(),
                        getJButtonOk().getName());
                getIUFODialogContentPane().add(getJButtonCancel(),
                        getJButtonCancel().getName());
                Border etched = BorderFactory.createEtchedBorder();
                Border title = BorderFactory.createTitledBorder(StringResource.getStringResource("miufo00852")); 
                getScrollPane().setBorder(title);

                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjIUFODialogContentPane;
    }
    /**
     * ���� ivjJButtonCancel ����ֵ��
     * 
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
    private JButton getJButtonCancel() {
        if (ivjJButtonCancel == null) {
            try {
                ivjJButtonCancel = new nc.ui.pub.beans.UIButton();
                ivjJButtonCancel.setName("JButton2");
                ivjJButtonCancel.setText(StringResource
                        .getStringResource("miufo1000764")); //"�� ��"
                ivjJButtonCancel.setBounds(235, 330, 75, 22);

                ivjJButtonCancel.addActionListener(this);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJButtonCancel;
    }
    /**
     * ���� ivjJButtonOk ����ֵ��
     * 
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
    private JButton getJButtonOk() {
        if (ivjJButtonOk == null) {
            try {
                ivjJButtonOk = new nc.ui.pub.beans.UIButton();
                ivjJButtonOk.setName("JButton1");
                ivjJButtonOk.setText(StringResource
                        .getStringResource("miufo1000766")); //"�� ��"
                ivjJButtonOk.setBounds(130, 330, 75, 22);
                if (this.getParent() instanceof DialogRefListener) {
                    ((DialogRefListener) this.getParent())
                            .setRefDialogAndRefOper(this, ivjJButtonOk);
                    if (((DialogRefListener) this.getParent()).getRefOper() != ivjJButtonOk)
                        ivjJButtonOk.setEnabled(false);
                }
                ivjJButtonOk.addActionListener(this);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJButtonOk;
    }
    
    public ExrateSchemeVO getRefVO() {
        return selRateScheme;
    }
    /**
     * ���� ScrollPane ����ֵ��
     * 
     * @return JScrollPane
     */
    /* ���棺�˷������������ɡ� */
    private JScrollPane getScrollPane() {
        if (ivjScrollPane == null) {
            try {
                ivjScrollPane = new UIScrollPane(getTable());
                ivjScrollPane.setName("ScrollPane");
                ivjScrollPane.setBounds(19, 18, 305, 300);
                // user code begin {1}
                // user code end
            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjScrollPane;
    }
    /**
     * ���� table ����ֵ��
     * 
     * @return JTable
     */
    /* ���棺�˷������������ɡ� */
    private JTable getTable() {
        table = null;
        if (table == null) {
            try {
                initTableModel();
                table = new nc.ui.pub.beans.UITable();
                table.setAutoCreateColumnsFromModel(false);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//                initTableModel();
                table.setModel(tablemodel);
                table
                        .setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                TableColumn column;
                for (int k = 0; k < m_aryObjColunmNames.length; k++) {

                    TableCellRenderer renderer = new DefaultTableCellRenderer();
                    //renderer.setHorizontalAlignment( JLabel.CENTER);

                    TableCellEditor editor = null;
                    column = new TableColumn(k, 150, renderer, editor);
                    table.addColumn(column);

                }
                table.addMouseListener(createTableMouseListener());

            } catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return table;
    }
    
    private MouseListener createTableMouseListener(){
    	return new MouseAdapter(){

			public void mouseClicked(MouseEvent e) {
					JTable table = (JTable) e.getSource();
					int selIndex = table.getSelectedRow();
					selRateScheme = tablemodel.getSelRow(selIndex);
					setResult(ID_OK);
					close();
			}   		
    	};
    }
    /**
     * ÿ�������׳��쳣ʱ������
     * 
     * @param exception
     *            java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception) {

        /* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
        // System.out.println("--------- δ��׽�����쳣 ---------");
        // exception.printStackTrace(System.out);
    }
    /**
     * ��ʼ���ࡣ
     * @i18n miufo00853=NC����ڼ䷽������
     */
    /* ���棺�˷������������ɡ� */
    private void initialize() {
        try {
            // user code begin {1}
            // user code end
            setName("NCAccSchemeRefDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(350, 400);
            setContentPane(getIUFODialogContentPane());
            setResizable(false);
            setTitle(StringResource.getStringResource("miufo00853")); 
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        isInitOver = true;
        // user code begin {2}
        // user code end
    }

    private void initTableModel() {
        tablemodel = new AccSchemeTable(vos);
        
    }
    /**
     * �˴����뷽�������� �������ڣ�(2003-3-5 15:20:36)
     * 
     * @param event
     *            java.awt.event.ActionEvent
     */
    protected void innerActionPerformed(java.awt.event.ActionEvent event) {
        if (event.getSource() == ivjJButtonOk) {
            int selIndex = table.getSelectedRow();
            selRateScheme = tablemodel.getSelRow(selIndex);
            setResult(ID_OK);
            close();
        } else {
            setResult(ID_CANCEL);
            close();
        }
    }
}