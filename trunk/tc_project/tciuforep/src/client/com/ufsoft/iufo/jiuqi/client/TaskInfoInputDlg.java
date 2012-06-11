/*
 * Created on 2004-3-25
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.ufsoft.iufo.jiuqi.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.ufsoft.iufo.jiuqi.pub.AllTask;
import com.ufsoft.iufo.jiuqi.pub.CodeInfo;
import com.ufsoft.iufo.jiuqi.pub.CodeRef;
import com.ufsoft.iufo.jiuqi.pub.GetMeasureDataRequest;
import com.ufsoft.iufo.jiuqi.pub.GetMeasureDataResponse;
import com.ufsoft.iufo.jiuqi.pub.GetTaskRequest;
import com.ufsoft.iufo.jiuqi.pub.GetTaskResponse;
import com.ufsoft.iufo.jiuqi.pub.Keyword;
import com.ufsoft.iufo.jiuqi.pub.LoginInfo;
import com.ufsoft.iufo.jiuqi.pub.MeasureDataValue;
import com.ufsoft.iufo.jiuqi.pub.StatusCodeConstants;
import com.ufsoft.iufo.jiuqi.pub.TaskInfo;
import com.ufsoft.iufo.jiuqi.pub.TaskValue;
import com.ufsoft.report.util.MultiLang;


/**
 * <p>Title: ����ӿڿͻ�������ؼ�����Ϣ¼�������</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author syang
 * @version 1.0
 */
public class TaskInfoInputDlg extends JDialog implements ActionListener, MouseListener{
    /**
	 * @i18n uiuforep00071=��������:
	 */
    private final static String LBL_TASK = MultiLang.getString("uiuforep00071");
    /**
	 * @i18n ok=ȷ��
	 */
    private final static String BTN_OK = MultiLang.getString("ok");
    /**
	 * @i18n cancel=ȡ��
	 */
    private final static String BTN_CANCEL = MultiLang.getString("cancel");
    /**
	 * @i18n uiuforep00072=����
	 */
    private final static String BTN_REF = MultiLang.getString("uiuforep00072");
    private JLabel lblTask = new JLabel(LBL_TASK);
    private JButton btnOK = new JButton(BTN_OK);
    private JButton btnCancel = new JButton(BTN_CANCEL);
    private JButton btnRef = new JButton(BTN_REF);
    private JComboBox taskCombo = null;
    private JTable keywordTable = null;
    private JPanel keywordSetPanel = null;
    private GetTaskRequest m_request = null;
    private LoginDlg m_pDialog = null;
    private TaskInfo[] aryTaskInfos = null;
    private ParaFileInfo m_paraFile = null;
    /**
	 * @i18n uiuforep00073=����ؼ���
	 * @i18n uiuforep00074=�ؼ���ֵ
	 */
    static final String[] strColumnName = new String[]{MultiLang.getString("uiuforep00073"), MultiLang.getString("uiuforep00074")};
    private TaskInfoInputTableModel model = null;
//    private Font titleFont = new Font("����",0,15);
//    private Font lblFont = new Font("����",0,13);
//    private Font btnFont = new Font("����",0,13);
    public static final int NAME_COLUMN = 0;
    public static final int VALUE_COLUMN = 1;

    /**
     * Tableģ�ͣ�������ʵ������Table����
     * <p>Title: </p>
     * <p>Description: </p>
     * <p>Copyright: Copyright (c) 2003</p>
     * <p>Company: </p>
     * @author not attributable
     * @version 1.0
     */
    public class TaskInfoInputTableModel extends AbstractTableModel{


        private Vector r_vector = new Vector();
        private String strTaskId = null;
        private String[] keyValues = null;
        private String[] keyValDisplayName = null;

        /**
         * TargerData ������ע�⡣
         */
        public TaskInfoInputTableModel(Vector valueVec, String taskId){
            super();
            reInit(valueVec, taskId);
        }

        /**
         * ���³�ʼ��Model
         * @param newValue Vector
         * @param taskId String
         */
        public void reInit(Vector newValue, String taskId){
            this.r_vector = newValue;
            this.strTaskId = taskId;
            keyValues = new String[newValue.size()];
            keyValDisplayName = new String[newValue.size()];
        }

        public Class getColumnClass(int c){
            return String.class;
        }

        public int getColumnCount(){
            return strColumnName.length;
        }

        public String getColumnName(int c){
            return strColumnName[c];
        }

        public int getRowCount(){
            return r_vector.size();
        }

        public boolean isCanRef(int r){
            Keyword key = (Keyword)r_vector.get(r);
            if(key.getRef() != null){
                return true;
            }
            return false;
        }

        public boolean isDateKeyword(int r){
            Keyword key = (Keyword)r_vector.get(r);
            if(key.getType() == Keyword.TYPE_TIME){
                return true;
            }
            return false;
        }

        public CodeRef getCodeRef(int r){
            Keyword key = (Keyword)r_vector.get(r);
            return key.getRef();
        }

        /**
         * ʵ�ֽӿڣ�����ĳ����Ԫ����ʾֵ
         * @param r int
         * @param c int
         * @return Object
         */
        public Object getValueAt(int r, int c){
            Keyword key = (Keyword)r_vector.get(r);
            switch(c){
                case NAME_COLUMN:
                    return key.getName() + ":";
                case VALUE_COLUMN:
                    if(keyValDisplayName != null && keyValDisplayName[r] != null){
                        return keyValDisplayName[r];
                    }
                    return "";
                default:
                    return "";
            }
        }

        public boolean isCellEditable(int r, int c){
            if(c == NAME_COLUMN){
                return false;
            }
            return true;
        }

        /**
         * ��������ֵ
         * �������ڣ�(2002-5-9 16:37:39)
         * @param Aera java.lang.String
         * @i18n uiuforep00075=�ؼ���¼��ֵ����!
         * @i18n uiuforep00076=���ڲ��Ϸ�!���ڸ�ʽΪ"��-��-��"
         */
        public void setValueAt(Object obj, int r, int c){
            if(obj instanceof String){
                if(isCanRef(r)){
                    String strValue = checkInput(obj, r);
                    if(strValue == null){
                        showErrDialog(new Exception(MultiLang.getString("uiuforep00075")));
                        return;
                    }
                    keyValues[r] = strValue;
                }else if(isDateKeyword(r)){
                    //�������͹ؼ���,��������Ƿ�Ϸ�
                    String strValue = ((String)obj).trim();
                    if(isValidDate(strValue) == false){
                        showErrDialog(new Exception(MultiLang.getString("uiuforep00076")));
                        return;
                    }else{
                        keyValues[r] = strValue;
                    }

                } else {
                    keyValues[r] = ( (String)obj).trim();
                }
                keyValDisplayName[r] = ( (String)obj).trim();
            }
        }

        /**
         * У�鵥������
         * @param obj Object
         * @param r int
         * @return String
         */
        public String checkInput(Object obj, int r){
            Keyword key = (Keyword)r_vector.get(r);
            CodeInfo pCodeInfo = key.getRef().getRootCodeInfo();
            HashMap map = new HashMap();
            CodeRefDlg.loadALLCodeName(pCodeInfo, map);
            if(map.containsKey(obj)){
            	return (String)obj;
            }else{
            	return null;
            }
        }

        /**
         * ¼����ַ����Ƿ�Ϊ�Ϸ�������
         * @param strValue String
         * @return boolean
         */
        public boolean isValidDate(String strValue){
          
            if(strValue == null || strValue.trim().length() == 0){
                return true;
            }
            if(strValue.trim().length() != 10){
                return false;
            }
            for(int i = 0; i < 10; i++){
                char c = strValue.trim().charAt(i);
                if(i == 4 || i == 7){
                    if(c != '-'){
                        return false;
                    }
                } else if(c < '0' || c > '9'){
                    return false;
                }
            }
            int year = Integer.parseInt(strValue.trim().substring(0, 4));
            int month = Integer.parseInt(strValue.trim().substring(5, 7));
            if(month < 1 || month > 12){
                return false;
            }
            int day = Integer.parseInt(strValue.trim().substring(8, 10));
            int MONTH_LENGTH[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            int LEAP_MONTH_LENGTH[] = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            int daymax = isLeapYear(year) ? LEAP_MONTH_LENGTH[month - 1] : MONTH_LENGTH[month - 1];
                         if(day < 1 || day > daymax){
                return false;
            }
            return true;
        }

        public boolean isLeapYear(int year){
            if( (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)){
                return true;
            } else{
                return false;
            }
        }


        /**
         * У��ȫ���Ĺؼ�������
         * @throws Exception
         * @return boolean
         * @i18n uiuforep00077=û��ѡ������!
         * @i18n report00344=�ؼ���
         * @i18n uiuforep00078=��ֵ��¼!
         */
        public boolean checkAllKeyInput() throws Exception{
            if(strTaskId == null){
                throw new Exception(MultiLang.getString("uiuforep00077"));
            }
            for(int i = 0; i < keyValues.length; i++){
                if(keyValues[i] == null || "".equals(keyValues[i])){
                    Keyword key = (Keyword)r_vector.get(i);
                    throw new Exception(MultiLang.getString("report00344") + key.getName() + MultiLang.getString("uiuforep00078"));
                }
            }
            return true;

        }

        /**
         * �õ�����ֵ
         * @return TaskValue
         */
        public TaskValue getSelTaskValue() throws Exception{
            checkAllKeyInput();
            return new TaskValue(strTaskId, keyValues);
        }
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws java.awt.HeadlessException
     */
    public TaskInfoInputDlg(JFrame arg0, String arg1, boolean arg2, GetTaskRequest request) throws Exception{
        super(arg0, arg1);
        m_request = request;
        m_pDialog = (LoginDlg)arg0;
        m_paraFile = m_pDialog.getParaFileInfo();
        init();
    }

    /**
	 * @i18n uiuforep00079=����ؼ�������
	 */
    private void init() throws Exception{
        try{
            aryTaskInfos = readTaskInfoFromServer();
        } catch(Exception e){
        	Log.getInstance().log(e.getMessage());
            throw e;
        }

        String[] aryTaskInfo = null;
        if(aryTaskInfos != null && aryTaskInfos.length > 0){
            aryTaskInfo = new String[aryTaskInfos.length];
            for(int i = 0; i < aryTaskInfos.length; i++){
                aryTaskInfo[i] = aryTaskInfos[i].getTaskName();
            }
        }
        setTitle(MultiLang.getString("uiuforep00079"));
        //�������壬��ֹ��������
//        setFont(titleFont);

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        getContentPane().add(getKeywordSetPanel(aryTaskInfos[0], aryTaskInfo), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        getContentPane().add(new JLabel(""), gbc);
        gbc.weightx = 0;
        getContentPane().add(Box.createVerticalStrut(13), gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        getContentPane().add(getJButtonPanel(), gbc);
        int nWidth = 500;
        int nHeight = 360;
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenwidth = screenSize.width;
        int screenheight = screenSize.height;
        setSize(nWidth,nHeight);
        setLocation((screenwidth-nWidth)/2,(screenheight-nHeight)/2);
    }

    private JPanel getJButtonPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
//        btnOK.setFont(btnFont);
//        btnCancel.setFont(btnFont);
        panel.add(btnOK);
        panel.add(btnCancel);
        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        return panel;
    }

    private JScrollPane getJTablePanel(TaskInfo taskInfo){
        if(keywordTable == null){
            keywordTable = new JTable();
            //�������壬��ֹ��������
//            keywordTable.setFont(titleFont);

            keywordTable.addMouseListener(this);
            keywordTable.setAutoCreateColumnsFromModel(false);
            setTableData(taskInfo);
            keywordTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            keywordTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            for(int k = 0; k < strColumnName.length; k++){
                TableCellRenderer renderer;
                DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
                if(k == NAME_COLUMN)
                {
                    textRenderer.setHorizontalAlignment(JLabel.LEFT);
                }else
                {
                    textRenderer.setHorizontalAlignment(JLabel.CENTER);
                }

                renderer = textRenderer;

                TableCellEditor editor;
                editor = new DefaultCellEditor(new JTextField());
                TableColumn column = new TableColumn(k, 100, renderer, editor);
                keywordTable.addColumn(column);
            }

            JTableHeader header = keywordTable.getTableHeader();
            //�������壬��ֹ��������
//            header.setFont(titleFont);

            header.setUpdateTableInRealTime(false);
        }
        keywordTable.setPreferredScrollableViewportSize(new Dimension(480, 175));
        JScrollPane ps = new JScrollPane(keywordTable);
        ps.setPreferredSize(new Dimension(480, 180));
        return ps;
    }

    /**
	 * @i18n uiuforep00080=�ؼ���ֵ����
	 */
    private JPanel getKeywordSetPanel(TaskInfo taskInfo, String[] aryTaskInfo){
        if(keywordSetPanel == null){
            keywordSetPanel = new JPanel();
            JPanel subPanel = new JPanel();
//            subPanel.setFont(titleFont);
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagConstraints subgbc = new GridBagConstraints();
            keywordSetPanel.setLayout(new GridBagLayout());
            subPanel.setLayout(new GridBagLayout());

            gbc.anchor = GridBagConstraints.NORTH;
            subgbc.anchor = GridBagConstraints.WEST;
//            lblTask.setFont(lblFont);
            subPanel.add(lblTask, subgbc);
            gbc.weightx = 1.0;
            subgbc.gridwidth = GridBagConstraints.CENTER;
            subPanel.add(getTaskComBox(aryTaskInfo), subgbc);
            subgbc.gridwidth = GridBagConstraints.REMAINDER;
            subgbc.weightx = 1.0;
            btnRef.addActionListener(this);
//            btnRef.setFont(btnFont);
            subPanel.add(btnRef, subgbc);
            keywordSetPanel.add(subPanel, gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            keywordSetPanel.add(new JLabel(""), gbc);
            gbc.weightx = 0;
            keywordSetPanel.add(Box.createVerticalStrut(13), gbc);

            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.SOUTH;
            keywordSetPanel.add(getJTablePanel(taskInfo), gbc);

            Border etched = BorderFactory.createEtchedBorder();
            Border title = BorderFactory.createTitledBorder(etched, MultiLang.getString("uiuforep00080"));
            keywordSetPanel.setBorder(title);
        }
        return keywordSetPanel;
    }

    private JComboBox getTaskComBox(String[] aryTaskInfo){
        if(taskCombo == null){
            taskCombo = new JComboBox(aryTaskInfo);
            taskCombo.setEditable(false);
            taskCombo.setMaximumRowCount(4);
            taskCombo.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent event){
                    try{
                        String item = (String)event.getItem();
                        if(event.getStateChange() == ItemEvent.SELECTED){
                            TaskInfo selectedTask = findSelectedTaskInfo(item);
                            if(selectedTask != null){
                                setTableData(selectedTask);
                            }
                        }
                    } catch(Exception e){
                    	Log.getInstance().log(e.getMessage());
                    }
                }
            });
        }
        return taskCombo;
    }

    private TaskInfo findSelectedTaskInfo(String taskName){
        for(int i = 0; i < aryTaskInfos.length; i++){
            if(taskName.equalsIgnoreCase(aryTaskInfos[i].getTaskName())){
                return aryTaskInfos[i];
            }
        }
        return null;
    }

    /**
     * ����TaskInfo���ñ���е���ʾ����
     * @param taskInfo TaskInfo
     */
    private void setTableData(TaskInfo taskInfo){
        if(taskInfo != null){
            Vector vecData = new Vector();
            Keyword[] keywords = taskInfo.getKeywords();

            if(keywords != null && keywords.length > 0){
                int keywordLen = keywords.length;
                for(int i = 0; i < keywordLen; i++){
                    vecData.addElement(keywords[i]);
                }
            }
            if(model == null){
                model = new TaskInfoInputTableModel(vecData, taskInfo.getTaskId());
            } else{
                model.reInit(vecData, taskInfo.getTaskId());
                model.fireTableDataChanged();
            }
            keywordTable.setModel(model);
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equalsIgnoreCase(BTN_CANCEL)){
            this.setVisible(false);
        } else if(e.getActionCommand().equalsIgnoreCase(BTN_OK)){
            //����Ƿ���ֹؼ���û��¼������
            try{
                TaskValue selTaskValue = model.getSelTaskValue();
                /**
                 */
                selTaskValue.setLoginUnitId(((LoginInfo)m_request.getRequestContent()).getUnitId());

                MeasureDataValue retValue = getMeasureDataFromServer(selTaskValue);
                if(retValue != null){
                    writeMeasureDataValueToFile(retValue);
                }

            } catch(Exception ex){
            	Log.getInstance().log(ex.getMessage());
                showErrDialog(ex);
                return;
            }

            //�رս���*�����ã��벻Ҫ�ڸöδ����������κδ���*
            this.dispose();
            m_pDialog.close();
        } else if(e.getActionCommand().equalsIgnoreCase(BTN_REF)){
            //���ò��ս���
            int row = keywordTable.getSelectedRow();
            //δѡ���У�����
            if(row < 0){
                return;
            }
            if(model.isCanRef(row)){
                CodeRef codeRef = model.getCodeRef(row);
                try{
                    CodeRefDlg refDlg = new CodeRefDlg(this, codeRef);
                    refDlg.setModal(true);
                    refDlg.show();
                    model.setValueAt(refDlg.getReturnValue(), row, VALUE_COLUMN);
                    model.fireTableCellUpdated(row, VALUE_COLUMN);
                }catch(Exception ex){
                	Log.getInstance().log(ex.getMessage());
                }
            }
        }
    }

    /**
     * ��÷�������ַ
     * @return String
     * @i18n uiuforep00081=��������ַδ����
     */
    private String getServletAddress() throws Exception {
        String strServerAddress = m_pDialog.getServerAddress();
        String strServerPort = m_pDialog.getServerPort();
        if(strServerAddress == null || strServerAddress.equalsIgnoreCase("")){
            throw new Exception(MultiLang.getString("uiuforep00081"));
        }
        String strServlet = m_pDialog.getServerConfig().toString();
        StringBuffer sbAddress = new StringBuffer();
        sbAddress.append(strServerAddress);
        if(strServerPort != null && !strServerPort.trim().equalsIgnoreCase("")){
            sbAddress.append(":");
            sbAddress.append(strServerPort);
        }
        sbAddress.append(strServlet);
        return sbAddress.toString();
    }

    /**
     * �ӷ������˻�ȡ������û���������Ϣ
     * @throws Exception
     * @return TaskInfo[]
     * @i18n uiuforep00082=�û�û�з�������
     */

    private TaskInfo[] readTaskInfoFromServer() throws Exception{
        HttpRequestOperator operator = new HttpRequestOperator(getServletAddress());
        GetTaskResponse response = (GetTaskResponse)operator.sendRequest(m_request);
        if(response != null){
            if(response.getStatusCode().equalsIgnoreCase(StatusCodeConstants.HAS_TASK)){
                AllTask allTask = (AllTask)response.getResponseContent();
                if(allTask != null){
                    return allTask.getTasks();
                }
                return null;
            } else if(response.getStatusCode().equalsIgnoreCase(StatusCodeConstants.NO_TASK)){
                throw new Exception(MultiLang.getString("uiuforep00082"));
            }
        }
        return null;
    }

    /**
     * �ӷ������˻�ȡָ������
     * @param taskValue TaskValue
     * @throws Exception
     * @return MeasureDataValue
     * @i18n uiuforep00083=���ݻ�ȡ�ɹ�
     * @i18n uiuforep00084=û������
     * @i18n uiuforep00085=û���ϴ�����ָ���
     * @i18n uiuforep00086=û�����ò���ָ����IUFOָ����չ�ϵ
     * @i18n uiuforep00087=ȡ����λ������
     * @i18n uiuforep00088=û������Ȩ��
     */
    private MeasureDataValue getMeasureDataFromServer(TaskValue taskValue) throws Exception{
        if(taskValue == null){
            return null;
        }

        HttpRequestOperator operator = new HttpRequestOperator(getServletAddress());
        GetMeasureDataRequest request = new GetMeasureDataRequest(taskValue);

        String taskInfo = getTaskInfo(taskValue);
        
        Log.getInstance().log(taskInfo);
        
        GetMeasureDataResponse response = (GetMeasureDataResponse)operator.sendRequest(request);
        if(response != null){
            String strStatusCode = response.getStatusCode();
            String returnType = "";
            if(strStatusCode.equalsIgnoreCase(StatusCodeConstants.HAS_MEASURE_DATA)){
                MeasureDataValue retValue = (MeasureDataValue)response.getResponseContent();
                returnType = MultiLang.getString("uiuforep00083");
                return retValue;
            } else if(strStatusCode.equalsIgnoreCase(StatusCodeConstants.NO_MEASURE_DATA)){
            	returnType = MultiLang.getString("uiuforep00084");
                throw new Exception(returnType);
            } else if(strStatusCode.equalsIgnoreCase(StatusCodeConstants.NO_CWZBK)){
            	returnType = MultiLang.getString("uiuforep00085");
                throw new Exception(returnType);
            } else if(strStatusCode.equalsIgnoreCase(StatusCodeConstants.NO_MEASURE_MAP)){
            	returnType = MultiLang.getString("uiuforep00086");
                throw new Exception(returnType);
            } else if(strStatusCode.equalsIgnoreCase(StatusCodeConstants.UNIT_NOT_EXIST)){
            	returnType = MultiLang.getString("uiuforep00087");
                throw new Exception(returnType);
            } else if(strStatusCode.equalsIgnoreCase(StatusCodeConstants.NO_DATA_RIGHT)){
            	returnType = MultiLang.getString("uiuforep00088");
                throw new Exception(returnType);
            }
            
        }
        return null;
    }

    /**
	 * @i18n uiuforep00089=����Id: 
	 * @i18n uiuforep00090=��¼��λ����: 
	 * @i18n report00344=�ؼ���
	 * @i18n uiuforep00091=��ֵ: 
	 */
    private String getTaskInfo(TaskValue taskValue) {
    	StringBuffer sb = new StringBuffer(MultiLang.getString("uiuforep00089") + (taskValue.getTaskId() == null ? "" : taskValue.getTaskId()));
    	sb.append("\r\n");
    	sb.append(MultiLang.getString("uiuforep00090") + (taskValue.getLoginUnitId() == null ? "" : taskValue.getLoginUnitId()));
    	sb.append("\r\n");
    	if(taskValue.getKeywordValue() != null && taskValue.getKeywordValue().length>0){
    		for(int i=0, n = taskValue.getKeywordValue().length; i<n; i++){
    			sb.append(MultiLang.getString("report00344") + (i+1) + MultiLang.getString("uiuforep00091") + (taskValue.getKeywordValue()[i] == null ? "" : taskValue.getKeywordValue()[i]));
    			sb.append("\r\n");
    		}
    	}
		return sb.toString();
	}

	/**
     * ���ӷ������˻�ȡ��ָ������д���ļ���
     * @param dataValue MeasureDataValue
     * @throws IOException
	 * @i18n uiuforep00092=����Ľ���ļ�δ�ҵ�
     */
    private void writeMeasureDataValueToFile(MeasureDataValue dataValue) throws Exception{
        File file = null;
        FileWriter out = null;
        if(m_paraFile !=null && m_paraFile.getCWSJK_File() !=null){
            String strFilename = m_paraFile.getCWSJK_File();
            try{
                file = new File(strFilename);
                out = new FileWriter(file);
                PrintWriter writer = new PrintWriter(out);
                writer.println(dataValue.getDataValue());
            } finally{
                if(out != null){
                    try{
                        out.close();
                    } catch(IOException ex){

                    }
                }
            }
        }else{
            throw new Exception(MultiLang.getString("uiuforep00092"));
        }
    }

    public void mouseClicked(MouseEvent MEvent){
        if(MEvent.getSource() == keywordTable){
            int row = keywordTable.getSelectedRow();
            if(row < 0){
                row = 0;
                //������ֵ���ε��ͬһ��ʱ������������ظ����������
            }
            if(model.isCanRef(row)){
                btnRef.setEnabled(true);
            } else{
                btnRef.setEnabled(false);
            }
        }
    }

    public void mouseEntered(MouseEvent MEvent){
    }

    public void mouseExited(MouseEvent MEvent){

    }

    public void mousePressed(MouseEvent MEvent){
        if(MEvent.getSource() == keywordTable){
            int row = keywordTable.getSelectedRow();
            if(row < 0){
                row = 0;
                //������ֵ���ε��ͬһ��ʱ������������ظ����������
            }
            if(model.isCanRef(row)){
                btnRef.setEnabled(true);
            } else{
                btnRef.setEnabled(false);
            }
        }
    }

    public void mouseReleased(MouseEvent MEvent){
    }

    /**
	 * @i18n uiuforep00093=������Ϣ
	 */
    private void showErrDialog(Exception ex){
        JOptionPane.showMessageDialog(this, ex.getMessage(), MultiLang.getString("uiuforep00093"), JOptionPane.INFORMATION_MESSAGE);
    }

}
 