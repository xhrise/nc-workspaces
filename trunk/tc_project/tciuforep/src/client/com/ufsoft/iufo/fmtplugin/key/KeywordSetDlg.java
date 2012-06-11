package com.ufsoft.iufo.fmtplugin.key;
import java.awt.Container;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.pub.iufo.exception.CommonException;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.pub.lang.UFBoolean;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.measure.MeasureCheckCellRenderer;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.TableUtilities;
/**
 * �������ڣ�(2004-11-18)
 * @author��CaiJie
 * @since 3.1
 */
public class KeywordSetDlg extends UfoDialog implements java.awt.event.ActionListener, IUfoContextKey
{
    private DynAreaModel m_dynAreaModel = null;
	//�Ƿ������������ؼ���
	private boolean m_bIsEditAble = true;
    
	//�ؼ��ֱ������
	private JScrollPane m_KeywordTablePane = null;
	//�ؼ��ֱ�
	private JTable m_keywordTable = null;
	//�ؼ��ֱ�Model
	private KeywordTable m_keywordTableModel = null;
	
	
	private JButton m_btnOK = null;
	private JButton m_btnCancel = null;
	private JPanel m_ufoDialogContentPane = null;


	//�ؼ��ֱ������,���е�һ����checkbox�У�����û������
	/**
	 * @i18n uiiufofmt00056=ѡ      ��
	 * @i18n uiiufofmt00057=��      ��
	 * @i18n uiiufofmt00058=״      ̬
	 * @i18n uiiufofmt00059=˵      ��
	 * @i18n uiiufofmt00060=λ      ��
	 */
	protected String[] m_aryObjColunmNames =
	{StringResource.getStringResource("uiiufofmt00056"), StringResource.getStringResource("uiiufofmt00057"), StringResource.getStringResource("uiiufofmt00058"), StringResource.getStringResource("uiiufofmt00059"), StringResource.getStringResource("uiiufofmt00060")};
	private Container parent = null;
	private CellsModel cellsModel = null;
	private IContext context = null;		

/**
 * KeyWordDef ������ע�⡣
 */
public KeywordSetDlg(Container parent,CellsModel cellsModel,IContext context,boolean isEditable) {
	super(parent);
	this.parent = parent;
	this.cellsModel = cellsModel;
	this.context = context;
	m_bIsEditAble = isEditable;
	m_dynAreaModel = DynAreaModel.getInstance(cellsModel);
	
	initialize();
}

/**
 * actionPerformed��Ӧ�˵�������
 * @param event ActionEvent
 * @since
 */

public void actionPerformed(java.awt.event.ActionEvent event)
{
	if(event.getSource() == m_btnOK){
		try	{
			m_keywordTableModel.onOKButton();
			setResult(ID_OK);
			this.close();
		} catch(Exception ce)		{
			UfoPublic.sendErrorMessage(ce.getMessage(),this,null);
		}
	} else{
		this.close();
	}
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-1 15:18:29)
 * @return javax.swing.JButton
 */
private JButton getBtnCancel() {
	if(m_btnCancel == null)
	{
		m_btnCancel = new nc.ui.pub.beans.UIButton();
		m_btnCancel.setName("btnCancel");
		m_btnCancel.setFont(new java.awt.Font("dialog", 0, 12));
		m_btnCancel.setText(StringResource.getStringResource("miufo1000757"));  //"ȡ    ��"
		m_btnCancel.setBounds(400,350,75,22);
		m_btnCancel.addActionListener(this);
	}
	return m_btnCancel;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-1 15:18:29)
 * @return javax.swing.JButton
 */
private JButton getBtnOK() {
	if(m_btnOK == null)
	{
		m_btnOK = new nc.ui.pub.beans.UIButton();
		m_btnOK.setName("btnOk");
		m_btnOK.setFont(new java.awt.Font("dialog", 0, 12));
		m_btnOK.setText(StringResource.getStringResource("miufo1000758"));  //"ȷ    ��"
		m_btnOK.setBounds(300,350,75,22);
		//m_btnOK.setVisible(true);
		m_btnOK.addActionListener(this);
	}
	return m_btnOK;
}
/**
 * �����ؼ���table
 * �������ڣ�(2003-7-1 11:40:41)
 * @return nc.ui.pub.beans.UITablePane
 */
public JScrollPane getFatherTablePane() {
	if (m_KeywordTablePane == null) {
		try {

			m_keywordTable = new nc.ui.pub.beans.UITable();
			m_KeywordTablePane = new UIScrollPane(m_keywordTable);
			m_KeywordTablePane.setName("ivjScrollPane");
			m_KeywordTablePane.setBounds(20,20,525,310);
			//����ָ���б�
			initTableModel();
			m_keywordTable.setAutoCreateColumnsFromModel(false);
			m_keywordTable.setModel(m_keywordTableModel);

			m_keywordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumn column;
			for (int k = 0; k < m_aryObjColunmNames.length; k++)
			{
                    TableCellRenderer renderer;
                    if (k == 0) {
                        renderer = new MeasureCheckCellRenderer();
                    } else {
                        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
                        textRenderer.setHorizontalAlignment(JLabel.CENTER);
                        renderer = textRenderer;
                    }

                    TableCellEditor editor = null;

                    if (k == 0) {
                        editor = new DefaultCellEditor(new UICheckBox());
                        column = new TableColumn(k, 60, renderer, editor);
                    } else if (k == 4) {
                        JTextField field = new UITextField();
                        //ʵ���Զ���ʧȥ����ʱ�̣����и�ֵ
                        field.addFocusListener(new FocusAdapter() {
                            public void focusLost(FocusEvent e) {
                                /*TableCellEditor editor = m_keywordTable
                                        .getCellEditor();
                                if (editor != null) {
                                    Object value = editor.getCellEditorValue();
                                    
                                    
                                    m_keywordTableModel.setValueAt(value,
                                            m_keywordTable.getEditingRow(),
                                            m_keywordTable.getEditingColumn());
                                }*/
                            }
                        });
                        editor = new DefaultCellEditor(field);
                        column = new TableColumn(k, 110, renderer, editor);
                    } else {
                        column = new TableColumn(k, 110, renderer, editor);
                    }
                    m_keywordTable.addColumn(column);

                }

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_KeywordTablePane;
}
	/**
	 * ���õ����е�����ؼ��֣�������ʾ�ؼ��ֺ����عؼ���
	 * caijie  2004-11-23
	 * @return KeyVO - CellPosition
	 */
	public HashMap getKeyVOPosByAll()
	{	
        HashMap hm = new HashMap();
        for (int i = 0; i < m_keywordTableModel.m_oKeysVec.size(); i++) {
            Object[] objs = (Object[]) m_keywordTableModel.m_oKeysVec.get(i);
            if(((Boolean)objs[2]).booleanValue()){
                hm.put(objs[0],objs[1]);
            }
        }
		return  hm;
	}
	
	
/**
 * ���� UfoDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getUfoDialogContentPane() {
	if (m_ufoDialogContentPane == null) {
		try {
			m_ufoDialogContentPane = new UIPanel();
			m_ufoDialogContentPane.setName("UfoDialogContentPane");

			m_ufoDialogContentPane.setLayout(null);


			getUfoDialogContentPane().add(getFatherTablePane(), getFatherTablePane().getName());
			getUfoDialogContentPane().add(getBtnOK(), getBtnOK().getName());
			getUfoDialogContentPane().add(getBtnCancel(), getBtnCancel().getName());


			// user code begin {1}
			// user code ends
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return m_ufoDialogContentPane;
}
	/**
	 * ÿ�������׳��쳣ʱ������
	 * @param exception java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		
		AppDebug.debug(exception);//@devTools  exception.printStackTrace(System.out);
	}
/**
	 * ��ʼ���ࡣ
	 */
/* ���棺�˷������������ɡ� */
protected void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("KeyWordMng");
		setSize(570, 425);
		this.setContentPane(getUfoDialogContentPane());
		setTitle(StringResource.getStringResource("miufo1001070"));  //"�ؼ�������"
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}
/**
 * ȡ��ȫ���Ĺؼ���,��ʼ��tablemodel
 * �������ڣ�(2003-7-1 14:19:23)
 */
private void initTableModel()
{
	/**�ӻ����еõ�ȫ���Ĺؼ���
	*/
	m_keywordTableModel = new KeywordTable();//
}

/************�ڲ���********************************************/
private class KeywordTable extends AbstractTableModel
{
    //isStop == true ==>ͣ�� ��isStop == false ==>���� ��
    String[] stopStrs = new String[] {
            StringResource.getStringResource("miufo1001063"),//ͣ��
            StringResource.getStringResource("miufopublic289") };//����
    /**
     * ���п�ѡ�ؼ��֣�Ԫ��Ϊnew Object[3](KeyVO,CellPosition,Boolean�Ƿ�ѡ��)��
     */
    Vector m_oKeysVec = new Vector();
    
    KeyGroupVO groupvo = null;

    /**        
     * 1���ӱ�����Ĺؼ��֣������в����ٳ��֡�
     * 2���ӱ����ʱ��ؼ��֣��������ܳ��ֱ��ӱ���С��ʱ��ؼ��֡�
     * 3���ӱ������ܹؼ���ʱ������ؼ��ֲ����޸�Ϊ���꣬���ȣ��£�Ѯ��
     * [ע���ӱ�ؼ���ָ���б����ж�̬���Ĺؼ��֣�������ؼ��ֶ����޸��������ӱ�ؼ��ֵ�Լ��]
     * 
     */
    public KeywordTable() {
        Vector allPubKey = CacheProxy.getSingleton().getKeywordCache().getAllKeys();
        boolean isExistWeek = false; //�ӱ����Ƿ����ܹؼ��֣����У�������ؼ��ֲ���Ϊ���꣬���ȣ��£�Ѯ
        int maxLongTime = -1;//�ӱ��е�����ʱ��ؼ��֣������ܳ��ֱ��ӱ���С��ʱ��ؼ���
        
        ArrayList subtableKeyVOs = new ArrayList();//�ӱ������еĹؼ���
        DynAreaCell[] dynAreaCells = m_dynAreaModel.getDynAreaCells();
        KeyVO dynTimeKeyVo = null;
        for (int m = 0; m < dynAreaCells.length; m++) {
            Collection keys = getKeyModel().getKeyVOByArea(dynAreaCells[m].getArea()).values();
            for (Iterator iter = keys.iterator();iter.hasNext();) {
                KeyVO each = (KeyVO) iter.next();
                subtableKeyVOs.add(each);
                //������ʱ��ؼ��ּ�¼ midify by ����� 2008-6-10 ���ӶԻ���ڼ�ؼ��ֵĿ���
                if(isTimeKeyVo(each)){//ʱ��ؼ���
                	dynTimeKeyVo = each;
                    if(each.getTimeKeyIndex() == 5)//�ܹؼ���
                        isExistWeek = true;
                    if(maxLongTime == -1){
                    	if(each.getType() == KeyVO.TYPE_TIME ){
                    		maxLongTime = each.getTimeKeyIndex();
                    	}else if(each.getType() == KeyVO.TYPE_ACC){
                    		maxLongTime = each.getAccPeriodKeyIndex();
                    	}                        
                    }else{
                    	if(each.getType() == KeyVO.TYPE_TIME && each.getTimeKeyIndex() < maxLongTime){
                    		 maxLongTime = each.getTimeKeyIndex();
                    	}else if(each.getType() == KeyVO.TYPE_ACC && each.getAccPeriodKeyIndex() < maxLongTime){
                    		 maxLongTime = each.getAccPeriodKeyIndex();
                    	}
//                        if(each.getTimeKeyIndex() < maxLongTime)
//                            maxLongTime = each.getTimeKeyIndex();
                    }
                }     
            }
        }
        
        HashMap mainTableKeyVOs = getKeyModel().getMainKeyVOPos();
        KeyVO mainTimeKeyVo = null;//�������ؼ��ֲ�ΪNULL����ֻ��ʾһ��ʱ��ؼ�������
        if(isAnaPeport()){// add by wangyga 2008-7-10 ��������˵�����ڼ�ؼ���
        	allPubKey = filterKeyVo(allPubKey);
        }
        for (int i = 0; i < allPubKey.size(); i++) {
            KeyVO pubvo = (KeyVO) allPubKey.get(i);
            if (!pubvo.isPrivate()) {
                boolean isadd = false;
                //����Ѿ��������ж����Ϊ��ʾ�����عؼ���,�����ؼ����б�
                for (Iterator iter = mainTableKeyVOs.keySet().iterator(); iter.hasNext();) {
                    KeyVO each = (KeyVO) iter.next();
                    //add by �����
                    if(isTimeKeyVo(each)){
                    	mainTimeKeyVo = each;
                    }
                    CellPosition eachCellPos = (CellPosition) mainTableKeyVOs.get(each);
                    if (pubvo.equals(each)) {                   	
                        m_oKeysVec.add(new Object[]{each,eachCellPos,Boolean.TRUE});                       
                        isadd = true;
                        break;
                    }
                }
                //add by ����� 2008-6-10 ֻ��ʾһ��ʱ��ؼ������ͣ���Ȼʱ����߻���ڼ�
                if(isTimeKeyVo(pubvo)){
                	if(mainTimeKeyVo != null){
                		if(mainTimeKeyVo.getType() != pubvo.getType()){               			
                        	isadd = true;
                        }
                	}else if(dynTimeKeyVo != null){
                		if(pubvo.getType() != dynTimeKeyVo.getType()){
                			isadd = true;
                		}
                	}
                	
                }
                
                if(isadd) continue;
                               
                //���ʱ��ؼ��ֲ�����Ҫ�󣬲������б� 2008-6-10 ���ӶԻ���ڼ�ؼ��ֵĿ���
                if(isTimeKeyVo(pubvo)){
                    if(isExistWeek){//�ӱ������ܹؼ���ʱ������ʱ��ؼ��ֲ����޸�Ϊ���꣬���ȣ��£�Ѯ,ֻ��Ϊ�ꡣ2008-6-10 ���ӶԻ���ڼ�ؼ��ֵĿ���
                        if(isTimeKeyVo(pubvo)){
                            if(pubvo.getTimeKeyIndex() != 0){//����ؼ���
                                isadd = true;
                            }
                        }
                    }else{//�ӱ����ʱ��ؼ��֣��������ܳ��ֱ��ӱ���С��ʱ��ؼ��֡�
                        if(maxLongTime != -1){
                        	if(pubvo.getType() == KeyVO.TYPE_TIME && pubvo.getTimeKeyIndex() >= maxLongTime){
                        		isadd = true;
                        	}else if(pubvo.getType() == KeyVO.TYPE_ACC && pubvo.getAccPeriodKeyIndex() >= maxLongTime){
                        		isadd = true;
                        	}
                            
                        }
                    }
                }
                
                if(isadd) continue;
                
                //����ؼ����Ѿ����κ�һ����̬�����ж���,������ؼ����б�
                KeyVO key;
                for (int j = 0; j < subtableKeyVOs.size(); j++) {
                    key = (KeyVO) subtableKeyVOs.get(j);
                    if (pubvo.equals(key)) {
                        isadd = true;
                        break;
                    }
                }
                if(isadd) continue;
                
                if (!isadd) {
                    if (!pubvo.isStop().booleanValue()) {
                        m_oKeysVec.add(new Object[]{pubvo,null,Boolean.FALSE});
                    }
                }
            }
        }
    }
    
   
    private KeywordModel getKeyModel() {
		return KeywordModel.getInstance(cellsModel);
	}

    private boolean isTimeKeyVo(KeyVO keyVo){
    	if(keyVo == null){
    		throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//�������������Ϊ�� 
    	}
    	if(keyVo.getType() == KeyVO.TYPE_TIME || keyVo.getType() == KeyVO.TYPE_ACC){
    		return true;
    	}
    	return false;
    }

	public int getColumnCount() {
        return m_aryObjColunmNames.length;

    }

    public String getColumnName(int col) {

        return m_aryObjColunmNames[col];
    }

    //����Ƿ�Ϊ��ѡ�ؼ���ָ�����ڵ�Ԫ��������ѡ�ؼ���ɸѡ����
    public void onOKButton() throws Exception{
        boolean hasDefTimeKey = false;
        for(int i=0;i<m_oKeysVec.size();i++){
            Object[] objs = (Object[]) m_oKeysVec.get(i);
            KeyVO keyVO = (KeyVO) objs[0];
            boolean isSel = ((Boolean)objs[2]).booleanValue();
            if(isSel){
            	if(getKeyModel().isDuplicateKeyName(keyVO.getName(),false)){
            		String strMsg = StringResource
                	.getStringResource("miuforepkey0001"); //"�ؼ������Ʋ����ظ�"
                throw new RuntimeException(strMsg+":"+keyVO.getName());
            	}
            	//modify by ����� 2008-6-10 ��ӻ���ڼ�ؼ��ֵĿ���
            	int iKeyType = keyVO.getType();
                if(iKeyType == KeyVO.TYPE_TIME || iKeyType == KeyVO.TYPE_ACC){
                    if(hasDefTimeKey){
                        String strMsg = StringResource
                        	.getStringResource("miufo1001065"); //"��һ�ű�����ֻ�ܶ���һ��ʱ�����͹ؼ��֣�"
                        throw new RuntimeException(strMsg);
                    } else{
                        hasDefTimeKey = true;
                    }
                }
                if(keyVO.isStop().booleanValue()){
                    keyVO.setIsStop(new UFBoolean(false));
                }
                
                TableCellEditor editor = m_keywordTable.getCellEditor();
                if(editor != null){
                	editor.stopCellEditing();
                	/* note by ll, ����stopCellEditing()���������������setValueAt���ظ��ж�
                	Object value = editor.getCellEditorValue();
                	int nEditingRow = m_keywordTable.getEditingRow();
                	int nEditingCol = m_keywordTable.getEditingColumn();
                	//�������ĵ�Ԫ�Ƿ��������Ԫ����������ǣ�����ʾ�û�����
                	if(nEditingCol == 4){
	    				if(!isAreaPositionStr((String)value)){
	    					throw new Exception(StringResource.getStringResource("miufo1001067"));//"��������ȷ�ĵ�Ԫλ�ã�"
	    				}
	    				
	    				//�������ĵ�Ԫ�Ƿ�������õĹؼ���λ�ó�ͻ���Ƿ�λ�ڶ�̬���ϣ��ǣ���ʾ�û�����
	    				String cellPosStr = ((String)value).trim();
	    				CellPosition cellPos = CellPosition.getInstance(cellPosStr);
	    				if(checkCell(cellPos, (KeyVO)objs[0]) == false){
	    					value = null;
	    				}
                	}
                	m_keywordTableModel.setValueAt(value, nEditingRow, nEditingCol);
                	*/
                }
            }
        }
    }


    public int getRowCount() {
        return m_oKeysVec.size();
    }

    public boolean isCellEditable(int row, int col) {
        if (!m_bIsEditAble && col != 4)
            return false;
        if (col == 0) {
            return true;
        }
        if (col == 4) {
            Boolean flag = (Boolean) ((Object[])m_oKeysVec.get(row))[2];
            if (flag.booleanValue())
                return true;
        }
        return false;
    }

    private int getSelNum() {
        int count = 0;
        for (int i = 0; i < m_oKeysVec.size(); i++) {
            Boolean flag = (Boolean) ((Object[])m_oKeysVec.get(i))[2];
            if (flag.booleanValue())
                count += 1;
        }
        return count;
    }

    //����ָ��λ�õĹؼ��֣���col�޹أ�col����Ϊ����ֵ
    public void setValueAt(Object obj, int row, int col){
    	Object[] objs = (Object[])m_oKeysVec.get(row);
    	
    	switch (col) {
	    	case 0://ѡ��
	    		if (((Boolean) obj).booleanValue()) {
	    			if (isKeyDefMax()){
	    				throw new RuntimeException(StringResource.getStringResource("miufo1001066"));//"�Ѿ��ﵽ�ؼ�����϶�������ޣ�"
	    			} else {
	    				//����Ƿ�������ʱ��ؼ��ֱ��ӱ�ʱ��ؼ���С�������������򷵻�false
	    				if (!checkTimeKey((KeyVO)objs[0])) break;
	    				objs[2]= Boolean.TRUE;                         
	    			}
	    		} else {
	    			objs[2]= Boolean.FALSE;                       
	    		}
	    		break;
	    	case 4://��Ԫλ��
	    		String cellPosStr = ((String) obj).trim();
	    		if (cellPosStr.length() == 0) {
	    			objs[1] = null;
	    		} else {
	    			//�������ĵ�Ԫ�Ƿ��������Ԫ����������ǣ�����ʾ�û�����
	    			//edit by wangyga at 2008-12-23 ����03:55:12 ���뵥Ԫλ�õĽ���
	    				//�������ĵ�Ԫ�Ƿ�������õĹؼ���λ�ó�ͻ���Ƿ�λ�ڶ�̬���ϣ��ǣ���ʾ�û�����
	    				if(!isAreaPositionStr(cellPosStr))
	    					throw new RuntimeException(StringResource.getStringResource("miufo1001067"));
		    			CellPosition cellPos = CellPosition.getInstance(cellPosStr);
		    			objs[1] = cellPos; 
		    			if (checkCell(cellPos, (KeyVO)objs[0])) {
		    				objs[1] = cellPos; 
		    			}

//	    			if(!isAreaPositionStr(cellPosStr)){
//	    				obj = null;
//	    				throw new RuntimeException(StringResource.getStringResource("miufo1001067"));//"��������ȷ�ĵ�Ԫλ�ã�"
//	    			}
	
	    			
	    		}
	    	default:
	    		break;
    	}    
    }
    
    /**
     * ��������ַ����Ƿ�������Ԫλ��
     * @param areaStr
     * @return
     */
    private boolean isAreaPositionStr(String str){
    	CellPosition cellPos = TableUtilities.getCellPosByString(str);
		return (cellPos == null) ? false:true;
    }
    
    /**
     * �ؼ��ֶ����Ƿ񵽴�����
     * @return
     */
    private boolean isKeyDefMax(){
    	return getSelNum() >= KeyGroupVO.MaxCount;
    }
    
    /**
     * ����Ƿ���λ���ظ����Ƿ�λ�ڶ�̬���ϵ���������򷵻�false
     * @author caijie 
     * @since 3.1
     */
    private boolean checkCell(CellPosition cell, KeyVO selVO) throws RuntimeException{
        String cellName = cell.toString();
        String selectKeyPK = selVO.getKeywordPK();
        //����Ƿ���Ѷ���Ĺؼ���λ���ظ�
        for (int i = 0; i < m_oKeysVec.size(); i++) {
        	Object[] objs = (Object[]) m_oKeysVec.get(i);
        	KeyVO keyVO = (KeyVO) objs[0];
        	CellPosition cellPos = (CellPosition) objs[1];
        	Boolean flag = (Boolean) objs[2];

        	if (flag.booleanValue() && cellPos != null
        			&& (!keyVO.getKeywordPK().equals(selectKeyPK))
        			&& cellPos.toString().equals(cellName)) {
        		String strMsg = StringResource.getStringResource("miufo1001068"); //"��Ԫλ���ظ������������ã�"
        		throw new RuntimeException(strMsg);
        	}
        }
        
        //��鵥Ԫ��λ���Ƿ�λ�ڶ�̬����
        if(m_dynAreaModel.getDynAreaCellByPos(cell) != null){
        	String strError = StringResource.getStringResource("miufo1001069"); //"������ؼ��ֲ��ܽ���λ�����ڶ�̬�����ڣ�"
        	throw new RuntimeException(strError);
        }
        return true;         
    }

    //����Ƿ�������ʱ��ؼ��ֱ��ӱ�ʱ��ؼ���С�������������򷵻�false
    private boolean checkTimeKey(KeyVO selectKeyVO) {
        if (selectKeyVO == null || selectKeyVO.getType() != KeyVO.TYPE_TIME)
            return true;

        //����Ƕ�̬���ϵ�ʱ��ؼ���
        DynAreaCell[] dynCells = m_dynAreaModel.getDynAreaCells();
        if (dynCells.length != 0) {
            int selTimeindex = selectKeyVO.getTimeKeyIndex();            
            for (int i = 0; i < dynCells.length; i++) {
                Collection dynKeys = getKeyModel().getKeyVOByArea(dynCells[i].getArea()).values();
                for (Iterator iter = dynKeys.iterator(); iter.hasNext();) {
                    KeyVO vo = (KeyVO) iter.next();
                    if (vo.getType() == KeyVO.TYPE_TIME
                            && vo.getTimeKeyIndex() <= selTimeindex) {
                        String strError = "miuforep000001";
                        String[] params = { vo.getName() };
                        throw new CommonException(strError, params);
                    }
                }
            }
        }
       
        return true;
    }

    /**
     * add by wangyga �жϱ�������
     * @see nc.vo.iuforeport.rep.ReportDirVO
     * @return
     */
    private boolean isAnaPeport(){
    	boolean isAnaRep = context.getAttribute(ANA_REP) == null ? false : Boolean.parseBoolean(context.getAttribute(ANA_REP).toString());       	
    	return isAnaRep;   	
    }
    
    /**
     * add by wangyga 2008-7-10 ���˻���ڼ�ؼ���
     * @param vec
     * @return
     */
    private Vector<KeyVO> filterKeyVo(Vector<KeyVO> vec){
    	Vector<KeyVO> newVector = new Vector<KeyVO>();
    	if(vec != null && vec.size() > 0){
    		Vector vecKeyVo = (Vector)vec.clone();
    		int iSize = vecKeyVo.size();
    		for(int i = 0;i < iSize;i++){
    			KeyVO keyVo = (KeyVO)vecKeyVo.elementAt(i);
    			if(!keyVo.isAccPeriodKey()){
    				newVector.add(keyVo);
    			}
    		}
    	}
        return newVector;
    }
    
    public Object getValueAt(int row, int column) {
        Object[] objs = (Object[]) m_oKeysVec.get(row);
        KeyVO keyVO = (KeyVO) objs[0];
        CellPosition cellPos = (CellPosition) objs[1];
        Boolean isSel = ((Boolean)objs[2]);
        
        switch (column) {
        case 0:
            return isSel;
        case 1:
            return keyVO.getName();
        case 2:
            if (keyVO.isStop().booleanValue())
                return stopStrs[0];
            else
                return stopStrs[1];
        case 3:
            return keyVO.getNote();
        case 4:
            //vo = null;
            if (!isSel.booleanValue())
                return "";
            if (cellPos != null) {
                //UfoPublic.sendErrMsg(vo.getCell().toString());
                return cellPos.toString();
            }
            break;
        }
        return null;
    }
}
	
}
 