package com.ufsoft.iufo.fmtplugin.key;
import com.ufida.iufo.pub.tools.AppDebug;

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

import nc.pub.iufo.cache.base.ICacheObject;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.iufo.fmtplugin.measure.DialogRefListener;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;

public class DynKeywordRefDlg extends UfoDialog implements ActionListener{
	  private JPanel ivjIUFODialogContentPane = null;
	    private JButton ivjJButtonOk = null;
	    private JButton ivjJButtonCancel = null;
	    private JLabel ivjLabel = null;
	    private JScrollPane ivjScrollPane = null;
	    private JTable table = null;
	    private KeyWordTable tablemodel = null;
	    private KeyVO[] vos = null;
	    private KeyVO selKeyVO;
	    public boolean isInitOver = false;
	    private KeyVO timeVo = null;
	    private ICacheObject[] cacheVos;
	    //关键字标的列名,
	    protected String[] m_aryObjColunmNames = {
	            StringResource.getStringResource("miufo1001051"),//StringResource.getStringResource("miufo1001052")选
	                                                             // 项"}; //"名 称"
	            StringResource.getStringResource("miufo1001052")};////"说 明"
	    public class KeyWordTable extends AbstractTableModel {
	        //选用标记的记录Vector
	        //private Vector flag = new Vector();
	        //可以参照的关键字集合
	        private Vector m_RefKeyVec = new Vector();
	        //vos 为当前报表中已经选用的关键字
	        //cacheVos为当前缓存中的全部关键字
	        public KeyWordTable(ICacheObject[] cacheVos,
	                KeyVO[] vos) {
	            for (int i = 0; i < cacheVos.length; i++) {
	                KeyVO vo = (KeyVO) cacheVos[i];
	                //私有关键字和停用的关键字不允许参照
	                if (vo.isPrivate() || vo.isStop().booleanValue())
	                    continue;
	                int iType = vo.getType();
	                if(timeVo != null && (iType == KeyVO.TYPE_TIME || iType == KeyVO.TYPE_ACC)){//如果报表还没有设置关键字，则显示全部关键字，相反则只显示一种时间关键字类型
	                	if(iType == KeyVO.TYPE_TIME || iType == KeyVO.TYPE_ACC){
		                	if(iType == timeVo.getType()){
		                		m_RefKeyVec.addElement(vo);
		                	}
		                }else{
		                	m_RefKeyVec.addElement(vo);
		                }	 
	                }else{
	                	m_RefKeyVec.addElement(vo);
	                }
	                              
	                if (vos != null && vos.length > 0) {
	                    for (int j = 0; j < vos.length; j++) {
	                        if (vos[j] != null) {
	                            if (vos[j].isPrivate())
	                                continue;
	                            if (vo.equals(vos[j])) {
	                            	if(m_RefKeyVec.size() > 0){
	                            		m_RefKeyVec.remove(m_RefKeyVec.size() - 1);	      
	                            	}	                            	                          
	                                break;
	                            }
	                        }
	                    }
	                }
	            }
	            Collections.sort(m_RefKeyVec);
	        }
	        public int getColumnCount() {
	            return m_aryObjColunmNames.length;

	        }
	        public String getColumnName(int col) {

	            return m_aryObjColunmNames[col];
	        }
	        public int getRowCount() {
	            return m_RefKeyVec.size();
	        }
	        public KeyVO getVO(int index) {
	            return (nc.vo.iufo.keydef.KeyVO) m_RefKeyVec.get(index);
	        }
	        public boolean isCellEditable(int row, int col) {
	            return false;
	        }
	        //返回选用的关键字所在的行
	        public KeyVO getSelRow(int index) {
	            return (KeyVO) m_RefKeyVec.get(index);
	        }
	        public void setValueAt(Object obj, int row, int column) {
	            nc.vo.iufo.keydef.KeyVO vo = (nc.vo.iufo.keydef.KeyVO) m_RefKeyVec
	                    .get(row);
	            switch (column) {
	                case 0 :
	                    break;
	                case 1 :
	                    break;

	                default :
	                    break;
	            }
	        }
	        public Object getValueAt(int row, int column) {
	            nc.vo.iufo.keydef.KeyVO vo = (nc.vo.iufo.keydef.KeyVO) m_RefKeyVec
	                    .get(row);
	            switch (column) {
	                case 0 :
	                    return vo.getName();
	                case 1 :
	                    return vo.getNote();
	                default :
	                    break;
	            }
	            return null;
	        }
	    }
	    class KeyCheckCellRenderer extends nc.ui.pub.beans.UICheckBox implements TableCellRenderer {
	        //焦点border
	        protected Border m_noFocusBorder;
	        /**
	         * 接口实现 创建日期：(00-11-20 15:21:13)
	         * 
	         * @author：
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
	     * KeywordRefDialog 构造子注解。
	     */
	    public DynKeywordRefDlg(Container parent, KeyVO[] hasDefVos) {
	        super(parent);
	        if (hasDefVos == null)
	            vos = new KeyVO[0];
	        else
	            vos = hasDefVos;
	        //groupVO = groupVo;
	        initialize();
	    }
	    /**
	     * KeywordRefDialog 构造子注解。
	     */
	    public DynKeywordRefDlg(Dialog parent, KeyVO[] hasDefVos) {
	        super(parent);
	        if (hasDefVos == null)
	            vos = new KeyVO[0];
	        else
	            vos = hasDefVos;
	        //groupVO = groupVo;
	        initialize();
	    }
	    
	    /**
	     * KeywordRefDialog 构造子注解。
	     */
	    public DynKeywordRefDlg(Dialog parent, KeyVO[] hasDefVos,KeyVO timeVo,ICacheObject[] cacheVos) {
	        super(parent);
	        if (hasDefVos == null)
	            vos = new KeyVO[0];
	        else
	            vos = hasDefVos;
	        //groupVO = groupVo;
	        this.timeVo = timeVo;
	        this.cacheVos = cacheVos;
	        initialize();
	    }
	    /**
	     * 此处插入方法描述。 创建日期：(2003-3-5 15:20:36)
	     * 
	     * @param event
	     *            java.awt.event.ActionEvent
	     */
	    public void actionPerformed(java.awt.event.ActionEvent event) {
	        if (event.getSource() == ivjJButtonOk) {
	            int selIndex = table.getSelectedRow();
	            selKeyVO = tablemodel.getSelRow(selIndex);
	            if (!checkKey(selKeyVO)) {
	                UfoPublic
	                        .sendWarningMessage(StringResource
	                                .getStringResource("miufo1001053"),null); //"不能参照已经参照过的关键字！"
	                return;
	            }
	            setResult(ID_OK);
	            if(getParent() instanceof DialogRefListener){
	               ((DialogRefListener) this.getParent()).onRef(event);
	            }else {//公式中关键字参照添加的分支。
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
	     * 检察官尖子参照得是否重复 出现重复，则返回false 创建日期：(2003-9-17 15:08:27)
	     * 
	     * @return boolean
	     */
	    private boolean checkKey(KeyVO vo) {
	        for (int i = 0; i < vos.length; i++) {
	            if (vos[i] != null && vos[i].equals(vo))
	                return false;
	        }
	        return true;
	    }
	    /**
	     * 此处插入方法描述。 创建日期：(2003-3-5 14:47:07)
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
	     * 返回 IUFODialogContentPane 特性值。
	     * 
	     * @return JPanel
	     */
	    /* 警告：此方法将重新生成。 */
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
	                Border title = BorderFactory.createTitledBorder(etched,
	                        StringResource.getStringResource("miufo1001054")); //"关键字列表"
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
	     * 返回 ivjJButtonCancel 特性值。
	     * 
	     * @return JButton
	     */
	    /* 警告：此方法将重新生成。 */
	    private JButton getJButtonCancel() {
	        if (ivjJButtonCancel == null) {
	            try {
	                ivjJButtonCancel = new nc.ui.pub.beans.UIButton();
	                ivjJButtonCancel.setName("JButton2");
	                ivjJButtonCancel.setText(StringResource
	                        .getStringResource("miufo1000764")); //"关 闭"
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
	     * 返回 ivjJButtonOk 特性值。
	     * 
	     * @return JButton
	     */
	    /* 警告：此方法将重新生成。 */
	    private JButton getJButtonOk() {
	        if (ivjJButtonOk == null) {
	            try {
	                ivjJButtonOk = new nc.ui.pub.beans.UIButton();
	                ivjJButtonOk.setName("JButton1");
	                ivjJButtonOk.setText(StringResource
	                        .getStringResource("miufo1000766")); //"参 照"
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
	    /**
	     * 返回 Label 特性值。
	     * 
	     * @return JLabel
	     */
	    /* 警告：此方法将重新生成。 */
	    private JLabel getLabel() {
	        if (ivjLabel == null) {
	            try {
	                ivjLabel = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
	                ivjLabel.setName("Label");
	    //          ivjLabel.setFont(new java.awt.Font("dialog", 1, 12));
	                ivjLabel.setText(StringResource
	                        .getStringResource("miufo1001055")); //"关键字列表："
	                ivjLabel.setBounds(19, 18, 108, 22);
	                // user code begin {1}
	                // user code end
	            } catch (java.lang.Throwable ivjExc) {
	                // user code begin {2}
	                // user code end
	                handleException(ivjExc);
	            }
	        }
	        return ivjLabel;
	    }
	    /**
	     * 此处插入方法描述。 创建日期：(2003-7-7 10:40:40)
	     * 
	     * @return nc.vo.iufo.keydef.KeyGroupVO
	     */
	    public KeyVO getRefVO() {
	        return selKeyVO;
	    }
	    /**
	     * 返回 ScrollPane 特性值。
	     * 
	     * @return JScrollPane
	     */
	    /* 警告：此方法将重新生成。 */
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
	     * 返回 table 特性值。
	     * 
	     * @return JTable
	     */
	    /* 警告：此方法将重新生成。 */
	    private JTable getTable() {
	        table = null;
	        if (table == null) {
	            try {
	                initTableModel();
	                table = new nc.ui.pub.beans.UITable();
	                table.setAutoCreateColumnsFromModel(false);
	                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	                table.addMouseListener(getTableMouseListener());
	                initTableModel();
	                table.setModel(tablemodel);
	                table
	                        .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	                //table.addMouseListener(this);
	                //table.addKeyListener(this);

	                TableColumn column;
	                for (int k = 0; k < m_aryObjColunmNames.length; k++) {

	                    TableCellRenderer renderer = new DefaultTableCellRenderer();
	                    //renderer.setHorizontalAlignment( JLabel.CENTER);

	                    TableCellEditor editor = null;
	                    column = new TableColumn(k, 150, renderer, editor);
	                    table.addColumn(column);

	                }

	            } catch (java.lang.Throwable ivjExc) {
	                // user code begin {2}
	                // user code end
	                handleException(ivjExc);
	            }
	        }
	        return table;
	    }
	    
	    /**
	     * table's MouseListener
	     * @return
	     */
	    private MouseListener getTableMouseListener(){
	    	return new MouseAdapter(){

				public void mouseClicked(MouseEvent e) {
					JTable source = (JTable)e.getSource();
					int selIndex = source.getSelectedRow();
		            selKeyVO = tablemodel.getSelRow(selIndex);
		            setResult(ID_OK);
		            close();
				}	    		
	    	};
	    }
	    
	    /**
	     * 每当部件抛出异常时被调用
	     * 
	     * @param exception
	     *            java.lang.Throwable
	     */
	    private void handleException(java.lang.Throwable exception) {
	    	AppDebug.debug(exception);
	        /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	        // System.out.println("--------- 未捕捉到的异常 ---------");
	        // exception.printStackTrace(System.out);
	    }
	    /**
	     * 初始化类。
	     */
	    /* 警告：此方法将重新生成。 */
	    private void initialize() {
	        try {
	            // user code begin {1}
	            // user code end
	            setName("KeywordRefDialog");
	            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	            setSize(350, 400);
	            setContentPane(getIUFODialogContentPane());
	            setResizable(false);
	            setTitle(StringResource.getStringResource("miufo1001056")); //"关键字参照"
	        } catch (java.lang.Throwable ivjExc) {
	            handleException(ivjExc);
	        }
	        isInitOver = true;
	        // user code begin {2}
	        // user code end
	    }
	    /**
	     * 取到全部的关键字,初始化tablemodel 创建日期：(2003-7-1 14:19:23)
	     */
	    private void initTableModel() {
	        tablemodel = new KeyWordTable(cacheVos, vos);
	        
	    }
	    /**
	     * 此处插入方法描述。 创建日期：(2003-3-5 15:20:36)
	     * 
	     * @param event
	     *            java.awt.event.ActionEvent
	     */
	    protected void innerActionPerformed(java.awt.event.ActionEvent event) {
	        if (event.getSource() == ivjJButtonOk) {
	            int selIndex = table.getSelectedRow();
	            selKeyVO = tablemodel.getSelRow(selIndex);
	            setResult(ID_OK);
	            close();
	        } else {
	            setResult(ID_CANCEL);
	            close();
	        }
	    }
}
