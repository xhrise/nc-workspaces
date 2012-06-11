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
 * 创建日期：(2004-11-18)
 * @author：CaiJie
 * @since 3.1
 */
public class KeywordSetDlg extends UfoDialog implements java.awt.event.ActionListener, IUfoContextKey
{
    private DynAreaModel m_dynAreaModel = null;
	//是否允许继续定义关键字
	private boolean m_bIsEditAble = true;
    
	//关键字表的容器
	private JScrollPane m_KeywordTablePane = null;
	//关键字表
	private JTable m_keywordTable = null;
	//关键字表Model
	private KeywordTable m_keywordTableModel = null;
	
	
	private JButton m_btnOK = null;
	private JButton m_btnCancel = null;
	private JPanel m_ufoDialogContentPane = null;


	//关键字标的列名,其中第一个是checkbox列，所以没有列名
	/**
	 * @i18n uiiufofmt00056=选      择
	 * @i18n uiiufofmt00057=名      称
	 * @i18n uiiufofmt00058=状      态
	 * @i18n uiiufofmt00059=说      明
	 * @i18n uiiufofmt00060=位      置
	 */
	protected String[] m_aryObjColunmNames =
	{StringResource.getStringResource("uiiufofmt00056"), StringResource.getStringResource("uiiufofmt00057"), StringResource.getStringResource("uiiufofmt00058"), StringResource.getStringResource("uiiufofmt00059"), StringResource.getStringResource("uiiufofmt00060")};
	private Container parent = null;
	private CellsModel cellsModel = null;
	private IContext context = null;		

/**
 * KeyWordDef 构造子注解。
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
 * actionPerformed响应菜单操作。
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
 * 此处插入方法描述。
 * 创建日期：(2003-9-1 15:18:29)
 * @return javax.swing.JButton
 */
private JButton getBtnCancel() {
	if(m_btnCancel == null)
	{
		m_btnCancel = new nc.ui.pub.beans.UIButton();
		m_btnCancel.setName("btnCancel");
		m_btnCancel.setFont(new java.awt.Font("dialog", 0, 12));
		m_btnCancel.setText(StringResource.getStringResource("miufo1000757"));  //"取    消"
		m_btnCancel.setBounds(400,350,75,22);
		m_btnCancel.addActionListener(this);
	}
	return m_btnCancel;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-1 15:18:29)
 * @return javax.swing.JButton
 */
private JButton getBtnOK() {
	if(m_btnOK == null)
	{
		m_btnOK = new nc.ui.pub.beans.UIButton();
		m_btnOK.setName("btnOk");
		m_btnOK.setFont(new java.awt.Font("dialog", 0, 12));
		m_btnOK.setText(StringResource.getStringResource("miufo1000758"));  //"确    定"
		m_btnOK.setBounds(300,350,75,22);
		//m_btnOK.setVisible(true);
		m_btnOK.addActionListener(this);
	}
	return m_btnOK;
}
/**
 * 创建关键字table
 * 创建日期：(2003-7-1 11:40:41)
 * @return nc.ui.pub.beans.UITablePane
 */
public JScrollPane getFatherTablePane() {
	if (m_KeywordTablePane == null) {
		try {

			m_keywordTable = new nc.ui.pub.beans.UITable();
			m_KeywordTablePane = new UIScrollPane(m_keywordTable);
			m_KeywordTablePane.setName("ivjScrollPane");
			m_KeywordTablePane.setBounds(20,20,525,310);
			//构造指标列表
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
                        //实现自动在失去焦点时刻，进行赋值
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
	 * 设置的所有的主表关键字，包括显示关键字和隐藏关键字
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
 * 返回 UfoDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
	 * 每当部件抛出异常时被调用
	 * @param exception java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		
		AppDebug.debug(exception);//@devTools  exception.printStackTrace(System.out);
	}
/**
	 * 初始化类。
	 */
/* 警告：此方法将重新生成。 */
protected void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("KeyWordMng");
		setSize(570, 425);
		this.setContentPane(getUfoDialogContentPane());
		setTitle(StringResource.getStringResource("miufo1001070"));  //"关键字设置"
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
}
/**
 * 取到全部的关键字,初始化tablemodel
 * 创建日期：(2003-7-1 14:19:23)
 */
private void initTableModel()
{
	/**从缓存中得到全部的关键字
	*/
	m_keywordTableModel = new KeywordTable();//
}

/************内部类********************************************/
private class KeywordTable extends AbstractTableModel
{
    //isStop == true ==>停用 ；isStop == false ==>启用 ；
    String[] stopStrs = new String[] {
            StringResource.getStringResource("miufo1001063"),//停用
            StringResource.getStringResource("miufopublic289") };//启用
    /**
     * 所有可选关键字（元素为new Object[3](KeyVO,CellPosition,Boolean是否选中)）
     */
    Vector m_oKeysVec = new Vector();
    
    KeyGroupVO groupvo = null;

    /**        
     * 1）子表定义过的关键字，主表中不能再出现。
     * 2）子表存在时间关键字，则主表不能出现比子表跨度小的时间关键字。
     * 3）子表中有周关键字时，主表关键字不能修改为半年，季度，月，旬。
     * [注：子表关键字指所有本表中动态区的关键字，即主表关键字定义修改受所有子表关键字的约束]
     * 
     */
    public KeywordTable() {
        Vector allPubKey = CacheProxy.getSingleton().getKeywordCache().getAllKeys();
        boolean isExistWeek = false; //子表中是否有周关键字，如有，则主表关键字不能为半年，季度，月，旬
        int maxLongTime = -1;//子表中的最长跨度时间关键字，主表不能出现比子表跨度小的时间关键字
        
        ArrayList subtableKeyVOs = new ArrayList();//子表中所有的关键字
        DynAreaCell[] dynAreaCells = m_dynAreaModel.getDynAreaCells();
        KeyVO dynTimeKeyVo = null;
        for (int m = 0; m < dynAreaCells.length; m++) {
            Collection keys = getKeyModel().getKeyVOByArea(dynAreaCells[m].getArea()).values();
            for (Iterator iter = keys.iterator();iter.hasNext();) {
                KeyVO each = (KeyVO) iter.next();
                subtableKeyVOs.add(each);
                //将最长跨度时间关键字记录 midify by 王宇光 2008-6-10 增加对会计期间关键字的控制
                if(isTimeKeyVo(each)){//时间关键字
                	dynTimeKeyVo = each;
                    if(each.getTimeKeyIndex() == 5)//周关键字
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
        KeyVO mainTimeKeyVo = null;//如果主表关键字不为NULL，则只显示一种时间关键字类型
        if(isAnaPeport()){// add by wangyga 2008-7-10 分析表过滤掉会计期间关键字
        	allPubKey = filterKeyVo(allPubKey);
        }
        for (int i = 0; i < allPubKey.size(); i++) {
            KeyVO pubvo = (KeyVO) allPubKey.get(i);
            if (!pubvo.isPrivate()) {
                boolean isadd = false;
                //如果已经在主表中定义的为显示或隐藏关键字,则加入关键字列表
                for (Iterator iter = mainTableKeyVOs.keySet().iterator(); iter.hasNext();) {
                    KeyVO each = (KeyVO) iter.next();
                    //add by 王宇光
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
                //add by 王宇光 2008-6-10 只显示一种时间关键字类型：自然时间或者会计期间
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
                               
                //如果时间关键字不满足要求，不加入列表 2008-6-10 增加对会计期间关键字的控制
                if(isTimeKeyVo(pubvo)){
                    if(isExistWeek){//子表中有周关键字时，主表时间关键字不能修改为半年，季度，月，旬,只能为年。2008-6-10 增加对会计期间关键字的控制
                        if(isTimeKeyVo(pubvo)){
                            if(pubvo.getTimeKeyIndex() != 0){//非年关键字
                                isadd = true;
                            }
                        }
                    }else{//子表存在时间关键字，则主表不能出现比子表跨度小的时间关键字。
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
                
                //如果关键字已经在任何一个动态区域中定义,不加入关键字列表
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
    		throw new IllegalArgumentException(StringResource.getStringResource("miufo1000496"));//输入参数不允许为空 
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

    //检查是否为已选关键字指定所在单元，并将已选关键字筛选出来
    public void onOKButton() throws Exception{
        boolean hasDefTimeKey = false;
        for(int i=0;i<m_oKeysVec.size();i++){
            Object[] objs = (Object[]) m_oKeysVec.get(i);
            KeyVO keyVO = (KeyVO) objs[0];
            boolean isSel = ((Boolean)objs[2]).booleanValue();
            if(isSel){
            	if(getKeyModel().isDuplicateKeyName(keyVO.getName(),false)){
            		String strMsg = StringResource
                	.getStringResource("miuforepkey0001"); //"关键字名称不能重复"
                throw new RuntimeException(strMsg+":"+keyVO.getName());
            	}
            	//modify by 王宇光 2008-6-10 添加会计期间关键字的控制
            	int iKeyType = keyVO.getType();
                if(iKeyType == KeyVO.TYPE_TIME || iKeyType == KeyVO.TYPE_ACC){
                    if(hasDefTimeKey){
                        String strMsg = StringResource
                        	.getStringResource("miufo1001065"); //"在一张报表中只能定义一个时间类型关键字！"
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
                	/* note by ll, 改用stopCellEditing()解决，不用再做和setValueAt的重复判断
                	Object value = editor.getCellEditorValue();
                	int nEditingRow = m_keywordTable.getEditingRow();
                	int nEditingCol = m_keywordTable.getEditingColumn();
                	//检查输入的单元是否是区域或单元，如果都不是，则提示用户错误
                	if(nEditingCol == 4){
	    				if(!isAreaPositionStr((String)value)){
	    					throw new Exception(StringResource.getStringResource("miufo1001067"));//"请输入正确的单元位置！"
	    				}
	    				
	    				//检查输入的单元是否合已设置的关键字位置冲突和是否位于动态区上，是，提示用户错误
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

    //更新指定位置的关键字，和col无关，col可以为任意值
    public void setValueAt(Object obj, int row, int col){
    	Object[] objs = (Object[])m_oKeysVec.get(row);
    	
    	switch (col) {
	    	case 0://选择
	    		if (((Boolean) obj).booleanValue()) {
	    			if (isKeyDefMax()){
	    				throw new RuntimeException(StringResource.getStringResource("miufo1001066"));//"已经达到关键字组合定义的上限！"
	    			} else {
	    				//检查是否有主表时间关键字比子表时间关键字小的情况情况，有则返回false
	    				if (!checkTimeKey((KeyVO)objs[0])) break;
	    				objs[2]= Boolean.TRUE;                         
	    			}
	    		} else {
	    			objs[2]= Boolean.FALSE;                       
	    		}
	    		break;
	    	case 4://单元位置
	    		String cellPosStr = ((String) obj).trim();
	    		if (cellPosStr.length() == 0) {
	    			objs[1] = null;
	    		} else {
	    			//检查输入的单元是否是区域或单元，如果都不是，则提示用户错误
	    			//edit by wangyga at 2008-12-23 下午03:55:12 输入单元位置的交验
	    				//检查输入的单元是否合已设置的关键字位置冲突和是否位于动态区上，是，提示用户错误
	    				if(!isAreaPositionStr(cellPosStr))
	    					throw new RuntimeException(StringResource.getStringResource("miufo1001067"));
		    			CellPosition cellPos = CellPosition.getInstance(cellPosStr);
		    			objs[1] = cellPos; 
		    			if (checkCell(cellPos, (KeyVO)objs[0])) {
		    				objs[1] = cellPos; 
		    			}

//	    			if(!isAreaPositionStr(cellPosStr)){
//	    				obj = null;
//	    				throw new RuntimeException(StringResource.getStringResource("miufo1001067"));//"请输入正确的单元位置！"
//	    			}
	
	    			
	    		}
	    	default:
	    		break;
    	}    
    }
    
    /**
     * 检查输入字符串是否是区域单元位置
     * @param areaStr
     * @return
     */
    private boolean isAreaPositionStr(String str){
    	CellPosition cellPos = TableUtilities.getCellPosByString(str);
		return (cellPos == null) ? false:true;
    }
    
    /**
     * 关键字定义是否到达上限
     * @return
     */
    private boolean isKeyDefMax(){
    	return getSelNum() >= KeyGroupVO.MaxCount;
    }
    
    /**
     * 检查是否有位置重复和是否位于动态区上的情况，有则返回false
     * @author caijie 
     * @since 3.1
     */
    private boolean checkCell(CellPosition cell, KeyVO selVO) throws RuntimeException{
        String cellName = cell.toString();
        String selectKeyPK = selVO.getKeywordPK();
        //检查是否和已定义的关键字位置重复
        for (int i = 0; i < m_oKeysVec.size(); i++) {
        	Object[] objs = (Object[]) m_oKeysVec.get(i);
        	KeyVO keyVO = (KeyVO) objs[0];
        	CellPosition cellPos = (CellPosition) objs[1];
        	Boolean flag = (Boolean) objs[2];

        	if (flag.booleanValue() && cellPos != null
        			&& (!keyVO.getKeywordPK().equals(selectKeyPK))
        			&& cellPos.toString().equals(cellName)) {
        		String strMsg = StringResource.getStringResource("miufo1001068"); //"单元位置重复，请重新设置！"
        		throw new RuntimeException(strMsg);
        	}
        }
        
        //检查单元格位置是否位于动态区上
        if(m_dynAreaModel.getDynAreaCellByPos(cell) != null){
        	String strError = StringResource.getStringResource("miufo1001069"); //"主表定义关键字不能将其位置设在动态区域内！"
        	throw new RuntimeException(strError);
        }
        return true;         
    }

    //检查是否有主表时间关键字比子表时间关键字小的情况情况，有则返回false
    private boolean checkTimeKey(KeyVO selectKeyVO) {
        if (selectKeyVO == null || selectKeyVO.getType() != KeyVO.TYPE_TIME)
            return true;

        //检查是动态区上的时间关键字
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
     * add by wangyga 判断报表类型
     * @see nc.vo.iuforeport.rep.ReportDirVO
     * @return
     */
    private boolean isAnaPeport(){
    	boolean isAnaRep = context.getAttribute(ANA_REP) == null ? false : Boolean.parseBoolean(context.getAttribute(ANA_REP).toString());       	
    	return isAnaRep;   	
    }
    
    /**
     * add by wangyga 2008-7-10 过滤会计期间关键字
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
 