package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;
import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JTextField;

import nc.itf.iufo.iufo.exproperty.IIUFOExPropConstants;
import nc.pub.iufo.cache.base.CodeCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.exproperty.ExPropOperator;
import nc.ui.iufo.exproperty.IExPropOperator;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIEditorPane;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.pub.date.UFODate;
import nc.vo.iufo.unit.UnitPropVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.businessquery.AbsRepToolTreeRefDlg;
import com.ufsoft.iufo.fmtplugin.businessquery.UnitTreeRefDlg;
import com.ufsoft.iufo.fmtplugin.formula.FormulaDefPlugin;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.inputplugin.inputcore.AbsUfoContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.exception.MessageException;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.script.base.IParsed;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.script.function.UfoFuncList;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;
import com.ufsoft.table.re.timeref.DatePickerDialog;

/**
 @update 2004-07-29 modified by liulp
  筛选条件可以接受空值，相当于删除筛选条件
 @end
 * 此处插入类型描述。
 * 创建日期：(2004-3-30 15:44:50)
 * @author：刘良萍
 */
public class SetFilterDefDlg extends com.ufsoft.report.dialog.UfoDialog implements java.awt.event.
    ActionListener{
    //“单位->"：UnitPropRef.PRE_UNIT_PROP
    /**
	 * @i18n miufodp00002=单位->
	 */
    private String PRE_UNIT_PROP = StringResource.getStringResource("miufodp00002");

    /**
     * 数据处理定义对象
     */
    private DataProcessDef m_oDataProcessDef = null;

    /**
     * 参照数据处理字段列表对象
     */
    private Vector m_vecRefDPFld = null;

    /**
     * 参照数据处理字段列表对象
     */
    private Vector m_vecDPFilterData = null;

    /**
     * 所定义数据处理筛选的动态区域关键字数组
     */
    private KeyVO[] m_oDynAreaKeyVOs = null;

    //界面组件
    private javax.swing.JButton ivjJBtnCancel = null;
    private javax.swing.JButton ivjJBtnOK = null;
    private javax.swing.JButton ivjJBtnRef = null;
    private javax.swing.JLabel ivjJLabelCond = null;
    private javax.swing.JPanel ivjJPanel1 = null;
    private javax.swing.JPanel ivjJPanel2 = null;
    private javax.swing.JPanel ivjUfoDialogContentPane = null;
    private javax.swing.JComboBox ivjJComboBoxRefList = null;
    private javax.swing.JScrollPane ivjJScrollPane1 = null;
    private JEditorPane jEPanelCond = null;

	private UfoReport _report;

    /**
     * 此处插入方法描述。
     * 创建日期：(2004-3-30 15:45:32)
     * @author：刘良萍
     * @param parent java.awt.Container
     * @param vecDPFilterData java.util.Vector
     * @param vecRefDPFld java.util.Vector
     * @param processDef com.ufsoft.iuforeport.reporttool.process.basedef.DataProcessDef
     * @param dynAreaKeyVOs nc.vo.iufo.keydef.KeyVO[]
     */
    public SetFilterDefDlg(java.awt.Container parent, java.util.Vector vecDPFilterData, java.util.Vector vecRefDPFld,
                           DataProcessDef processDef, KeyVO[] dynAreaKeyVOs){
        super(parent);
        _report = (UfoReport)parent;
        this.m_oDataProcessDef = processDef;
        this.m_vecRefDPFld = vecRefDPFld;
        this.m_vecDPFilterData = vecDPFilterData;
        this.m_oDynAreaKeyVOs = dynAreaKeyVOs;

        initialize();

    }

    /**
     * 获得日期参照值
     * @return String
     */
    private String getRefDateValue(){
//        nc.ui.pub.beans.calendar.UICalendar calendar = new nc.ui.pub.beans.calendar.UICalendar(this, "1000-01-01",
//            "3003-12-31");
//        try{
//            nc.vo.pub.lang.UFDate date = new nc.vo.pub.lang.UFDate(new java.util.Date());
//            calendar.setNewdate(date);
//        } catch(Exception ex){
//        	AppDebug.debug(ex);
//        }
//        calendar.showModal();
//        calendar.destroy();
//        return calendar.getCalendarString();
    	JTextField textField = new UITextField();
    	DatePickerDialog dialog = new DatePickerDialog(textField,this);
    	Point location = this.getLocation();
    	location.translate(100, 0);
    	dialog.setLocation(location);
    	dialog.setSize(200, 250);
    	dialog.setModal(true);
    	dialog.setVisible(true);
    	return textField.getText();
    }

    /**
     *
     * @return String
     */
    private String doGetDefFilterCond(){
        String strReturn = null;
        //得到旧的筛选条件内容
        String strFilterCond = null;
        DataProcessFilterCond dpFilterCond = m_oDataProcessDef.getDPFilterCond();
        if(dpFilterCond != null){
            strFilterCond = dpFilterCond.getFilterCond();
        }
        //解析得到用户显示值
        if(strFilterCond != null && strFilterCond.length() > 0){
            try{
                if(getParent() != null){
                    //得到语法解析器
                	UfoFmlExecutor handler = getFormulaHandler();
                    //设置动态区域关键字数组
                     ( (UfoCalcEnv)handler.getCalcEnv()).setKeys(m_oDynAreaKeyVOs);
                    //解析
                    IParsed objExpr = handler.parseLogicExpr(strFilterCond,false);
                    strReturn = objExpr.toUserDefString(handler.getCalcEnv());
                }
            } catch(ParseException ex){
                UfoPublic.sendWarningMessage(ex.getMessage(),this);
            }
        }
        return strReturn;
    }

    /**
     * 将参照返回的值追加到条件内容编辑框里
     * @param strNewReturnValue String
     */
    private void doAppendTextAreaCond(String strNewReturnValue){
        if(strNewReturnValue != null && strNewReturnValue.length() > 0){
//            StringBuffer sbAreaText = new StringBuffer(getJTextAreaCond().getText());
            StringBuffer sbAreaText = new StringBuffer(getJEPanelCond().getText());
            sbAreaText.append(strNewReturnValue);
//            getJTextAreaCond().setText(sbAreaText.toString());
            getJEPanelCond().setText(sbAreaText.toString());
        }
    }

    /**
     * 筛选条件的语法校验
     * @param strFilterCond String
     * @return String
     */
    private String doCheckFilterCond(String strFilterCond){
        String strReturn = null;
        if(strFilterCond != null && strFilterCond.trim().length() > 0){
            try{
                if(getParent() != null){
                    //得到语法解析器
                	UfoFmlExecutor handler = getFormulaHandler();
                    //设置动态区域关键字数组
                     ( (UfoCalcEnv)handler.getCalcEnv()).setKeys(m_oDynAreaKeyVOs);
                    //解析
                    IParsed objExpr = handler.parseLogicExpr(strFilterCond,true);
                    strReturn = objExpr.toString(handler.getCalcEnv());
                }
            } catch(ParseException ex){
                UfoPublic.sendMessage(ex.getMessage(),this);
            }
        } else if(strFilterCond != null && strFilterCond.trim().length() == 0){
            strReturn = "";
        }
        return strReturn;
    }
    private UfoFmlExecutor getFormulaHandler(){
    	FormulaModel formulaModel = FormulaModel.getInstance(_report.getCellsModel());
		return formulaModel.getUfoFmlExecutor();
		
//    	FormulaDefPlugin pi = (FormulaDefPlugin) _report.getPluginManager().getPlugin(
//				FormulaDefPlugin.class.getName());
//    	UfoFmlExecutor handler=pi.getFmlExecutor();
//		return handler;	
    }
    /**
     * 保存到m_oDataProcessDef
     * @param strFilterCond String
     */
    private void doSaveToDataProcessDef(String strFilterCond){
        if(strFilterCond == null || strFilterCond.trim().length()<=0){
            m_oDataProcessDef.setDPFilterCond(null);
        }else{
            DataProcessFilterCond dpFilterCond = m_oDataProcessDef.getDPFilterCond();
            if(dpFilterCond == null){
                dpFilterCond = new DataProcessFilterCond();
                m_oDataProcessDef.setDPFilterCond(dpFilterCond);
            }
            dpFilterCond.setFilterCond(strFilterCond);
        }
    }

    /**
     * 得到树参照形式的参照值
     * @param selDPFilterData DataProcessFilterData
     * @return String
     */
    private String getRefTreeValue(DataProcessFilterData selDPFilterData){
        if(selDPFilterData == null){
            return null;
        }
        String strReturnValue = null;

        String strRefDlgUrl = selDPFilterData.getRefDlgURL();
        int nTreeRefType = AbsRepToolTreeRefDlg.TREE_REF_TYPE_UNIT;
        if(strRefDlgUrl.equals(DataProcessFilterData.REPDLGURL_CODE_VALUE)){
            nTreeRefType = AbsRepToolTreeRefDlg.TREE_REF_TYPE_CODE;
        } else if(strRefDlgUrl.equals(DataProcessFilterData.REPDLGURL_FUNC)){
            nTreeRefType = AbsRepToolTreeRefDlg.TREE_REF_TYPE_FUNC;
        }
        AbsRepToolTreeRefDlg dlg = null;
        String strRefPara = selDPFilterData.getRefUrlPara();
        if(nTreeRefType == AbsRepToolTreeRefDlg.TREE_REF_TYPE_UNIT){
            //单位树参照
            dlg = new UnitTreeRefDlg(this);
            ((UnitTreeRefDlg)dlg).setUfoContext((AbsUfoContextVO)_report.getContextVo());
        } else if(nTreeRefType == AbsRepToolTreeRefDlg.TREE_REF_TYPE_CODE){
            //编码树参照
            CodeVO codeVO = null;
            try{
                CodeCache codeCache = IUFOUICacheManager.getSingleton().getCodeCache();
                if(codeCache == null){
                    return null;
                }
                //编码
                codeVO = codeCache.findCodeByID(strRefPara);
            } catch(Exception ex){
                return null;
            }
            dlg = new CodeTreeRefDlg(this, codeVO);
        } else{
            //函数树参照:AbsRepToolTreeRefDlg.TREE_REF_TYPE_FUNC
            dlg = new FuncTreeRefDlg(this);
        }

        dlg.setModal(true);
        dlg.showModal();

        int iSelType = getSelType(nTreeRefType, strRefPara);
        //ITEM_UNIT_LEVEL_CODE  || iSelIndex == ITEM_UNIT_CODE || iSelIndex == ITEM_UNIT_NAME
        if(dlg.getResult() == UfoDialog.ID_OK){
            strReturnValue = dlg.getReturnValue(iSelType);
            //函数返回值不需要引号
            if(nTreeRefType != AbsRepToolTreeRefDlg.TREE_REF_TYPE_FUNC){
                if(strReturnValue != null && strReturnValue.length() > 0){
                    strReturnValue = "\'" + strReturnValue + "\'";
                }
            }
        }
        return strReturnValue;
    }

    /**
     * 得到运算符参照列表矢量
     * @return Vector
     */
    private Vector getRefItemOfOP(){
        Vector vecItemsOP = new Vector();
        String[] strItems = UfoFuncList.OPERATOR_ITEMS;
        int iLen = strItems.length;
        for(int i = 0; i < iLen; i++){
            vecItemsOP.add(strItems[i]);
        }
        return vecItemsOP;
    }

    /**
     * 得到单位结构参照列表矢量
     * @return Vector
     */
    private Vector getRefItemOfUnitProp(){
        Vector vecRefItem = new Vector();
        try{
            //nc.ui.iufo.unit.UnitMngBO_Client.loadAllInputUnitProp();
            IExPropOperator exPropOper = ExPropOperator.getExPropOper(IIUFOExPropConstants.EXPROP_MODULE_UNIT);
            nc.vo.iufo.unit.UnitPropVO[] unitProps = UnitPropVO.toUnitPropVOs(exPropOper.loadAllInputExProps(null));
            
            int iPropLen = unitProps != null ? unitProps.length : 0;
            for(int i = 0; i < iPropLen; i++){
                vecRefItem.add(PRE_UNIT_PROP + unitProps[i].getPropName());
            }
        } catch(Exception ex){
            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001308"),this);  //"装载单位结构错误！"
        }
        return vecRefItem;
    }

    /**
     * 得到列表类型的参照值
     *
     * @param selDPFilterData  DataProcessFilterData
     * @return String
     */
    private String getRefListValue(DataProcessFilterData selDPFilterData){
        String strReturnValue = null;

        Vector vecRefItem = null;
        boolean bMeasOrKey = false;
        boolean bOp = false;
        String strRefItemName = selDPFilterData.getRefItemName();
        String strTitle = strRefItemName + StringResource.getStringResource("miufopublic283");  //"参照"
        if(strRefItemName.equals(DataProcessFilterData.getRefItemNameOfMeasOrKey())){
            //指标/关键字
            vecRefItem = m_vecRefDPFld;
            bMeasOrKey = true;
        } else if(strRefItemName.equals(DataProcessFilterData.getRefItemNameOfUnitStructure())){
            //单位结构
            vecRefItem = getRefItemOfUnitProp();
        } else if(strRefItemName.equals(DataProcessFilterData.getRefItemNameOfTimeKeys())){
            //日期关键字
            String strRefPara = selDPFilterData.getRefUrlPara();
            vecRefItem = UFODate.getDateKeyName(strRefPara);
        } else if(strRefItemName.equals(DataProcessFilterData.getRefItemNameOfOp())){
            //运算符
            vecRefItem = getRefItemOfOP();
            bOp = true;
        }

        RepToolListRefDlg dlg = new RepToolListRefDlg(this, vecRefItem, strTitle);
        dlg.setModal(true);
        dlg.showModal();

        if(dlg.getResult() == UfoDialog.ID_OK){
            Object objReturn = dlg.getReturnValue();
            if(bMeasOrKey){
                //指标返回形式为：mselect('name')；关键字的返回形式为：k('name')
                DataProcessFld selItem = (DataProcessFld)objReturn;
                strReturnValue = selItem.toString();
                if(strReturnValue != null && strReturnValue.length() > 0){
                    strReturnValue = "(\'" + strReturnValue + "\')";
                    int nMapType = selItem.getMapType();
                    if(nMapType == FieldMap.FIELD_MAP_MEASURE){
                        strReturnValue = "mselect" + strReturnValue;
                    } else if(nMapType == FieldMap.FIELD_MAP_KEYWORD){
                        strReturnValue = "K" + strReturnValue;
                    }
                }
            } else{
                strReturnValue = (String)objReturn;
                if(!bOp){
                    if(strReturnValue != null && strReturnValue.length() > 0){
                        strReturnValue = "\'" + strReturnValue + "\'";
                        strReturnValue = "K" + "(" + strReturnValue + ")";
                    }
                }
            }
        }
        return strReturnValue;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param event ActionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent event){
        if(event.getSource() == getJBtnOK()){
            //筛选条件的语法校验
//            String strFilterCond = getJTextAreaCond().getText();
            String strFilterCond = getJEPanelCond().getText();
            strFilterCond = doCheckFilterCond(strFilterCond);
            if(strFilterCond != null){
                //保存到m_oDataProcessDef
                doSaveToDataProcessDef(strFilterCond);
                //关闭窗口
                setResult(UfoDialog.ID_OK);
                close();
            } else{
                return;
            }
        } else if(event.getSource() == getJBtnCancel()){
            this.setResult(UfoDialog.ID_CANCEL);
            close();
        } else if(event.getSource() == getJBtnRef()){
            DataProcessFilterData selDPFilterData = (DataProcessFilterData)getJComboBoxRefList().getSelectedItem();
            String strRefDlgUrl = selDPFilterData.getRefDlgURL();
            String strReturnValue = null;
            if(strRefDlgUrl.equals(DataProcessFilterData.REFDLGURL_LIST_REFDLG)){
                //指标/关键字;单位结构；日期关键字;运算符
                strReturnValue = getRefListValue(selDPFilterData);
            } else if(strRefDlgUrl.equals(DataProcessFilterData.REFDLGURL_UNIT_INFO) ||
                      strRefDlgUrl.equals(DataProcessFilterData.REPDLGURL_CODE_VALUE) ||
                      strRefDlgUrl.equals(DataProcessFilterData.REPDLGURL_FUNC)){
                //单位**信息(树)，编码信息树
                strReturnValue = getRefTreeValue(selDPFilterData);
            } else if(strRefDlgUrl.equals(DataProcessFilterData.REFDLGURL_TIME_VALUE)){
                //日期
                strReturnValue = getRefDateValue();
            }
            //将参照值追加到条件内容编辑框里
            doAppendTextAreaCond(strReturnValue);
        }
    }

    /**
     * 增加帮助。
     *
     * 创建日期：(2004-03-30 15:56:54)
     * 创建者：刘良萍
     */
    private void addHelp(){
        javax.help.HelpBroker hb = ResConst.getHelpBroker();
        if(hb == null){
            return;
        }
        hb.enableHelpKey(getContentPane(), "TM_Data_Process_Order", null);
    }

    public DataProcessDef getDataProcessDef(){
        return m_oDataProcessDef;
    }

    /**
     * 返回 JBtnCancel 特性值。
     * @return javax.swing.JButton
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JButton getJBtnCancel(){
        if(ivjJBtnCancel == null){
            try{
                ivjJBtnCancel = new nc.ui.pub.beans.UIButton();
                ivjJBtnCancel.setName("JBtnCancel");
                ivjJBtnCancel.setText("Cancel");
                ivjJBtnCancel.setBounds(354, 3, 75, 22);
                // user code begin {1}
                String strCancel = StringResource.getStringResource(StringResource.CANCEL);
                ivjJBtnCancel.setText(strCancel);
                ivjJBtnCancel.addActionListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnCancel;
    }

    /**
     * 返回 JBtnOK 特性值。
     * @return javax.swing.JButton
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JButton getJBtnOK(){
        if(ivjJBtnOK == null){
            try{
                ivjJBtnOK = new nc.ui.pub.beans.UIButton();
                ivjJBtnOK.setName("JBtnOK");
                ivjJBtnOK.setText("OK");
                ivjJBtnOK.setBounds(239, 3, 75, 22);
                // user code begin {1}
                String strOK = StringResource.getStringResource(StringResource.OK);
                ivjJBtnOK.setText(strOK);                
                ivjJBtnOK.addActionListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnOK;
    }

    /**
     * 返回 JBtnRef 特性值。
     * @return javax.swing.JButton
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JButton getJBtnRef(){
        if(ivjJBtnRef == null){
            try{
                ivjJBtnRef = new nc.ui.pub.beans.UIButton();
                ivjJBtnRef.setName("JBtnRef");
                ivjJBtnRef.setText("Ref");
                ivjJBtnRef.setBounds(462, 29, 75, 22);
                // user code begin {1}
                String strRef = StringResource.getStringResource("miufopublic283");  //"参照"
                ivjJBtnRef.setText(strRef);
                ivjJBtnRef.addActionListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnRef;
    }

    /**
     * 返回 JComboBox1 特性值。
     * @return javax.swing.JComboBox
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JComboBox getJComboBoxRefList(){
        if(ivjJComboBoxRefList == null){
            try{
                ivjJComboBoxRefList = new UIComboBox();
                ivjJComboBoxRefList.setName("JComboBoxRefList");
                ivjJComboBoxRefList.setBounds(324, 29, 130, 25);
                // user code begin {1}
                initRefList(ivjJComboBoxRefList, m_vecDPFilterData);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJComboBoxRefList;
    }

    /**
     * 返回 JLabelCond 特性值。
     * @return javax.swing.JLabel
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JLabel getJLabelCond(){
        if(ivjJLabelCond == null){
            try{
                ivjJLabelCond = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLabelCond.setName("JLabelCond");
                ivjJLabelCond.setText(StringResource.getStringResource("miufo1001309"));  //"CondContent："
                ivjJLabelCond.setBounds(7, 7, 75, 16);
                // user code begin {1}
                String strCondContent = StringResource.getStringResource("miufo1001310");  //"条件内容："
                ivjJLabelCond.setText(strCondContent);
                
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabelCond;
    }

    /**
     * 返回 JPanel1 特性值。
     * @return javax.swing.JPanel
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JPanel getJPanel1(){
        if(ivjJPanel1 == null){
            try{
                ivjJPanel1 = new UIPanel();
                ivjJPanel1.setName("JPanel1");
                ivjJPanel1.setLayout(null);
                ivjJPanel1.setBounds(31, 20, 534, 191);
                getJPanel1().add(getJLabelCond(), getJLabelCond().getName());
                getJPanel1().add(getJComboBoxRefList(), getJComboBoxRefList().getName());
                getJPanel1().add(getJBtnRef(), getJBtnRef().getName());
                getJPanel1().add(getJScrollPane1(), getJScrollPane1().getName());
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanel1;
    }

    /**
     * 返回 JPanel2 特性值。
     * @return javax.swing.JPanel
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JPanel getJPanel2(){
        if(ivjJPanel2 == null){
            try{
                ivjJPanel2 = new UIPanel();
                ivjJPanel2.setName("JPanel2");
                ivjJPanel2.setLayout(null);
                ivjJPanel2.setBounds(30, 228, 536, 35);
                getJPanel2().add(getJBtnOK(), getJBtnOK().getName());
                getJPanel2().add(getJBtnCancel(), getJBtnCancel().getName());
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanel2;
    }

    /**
     * 返回 JScrollPane1 特性值。
     * @return javax.swing.JScrollPane
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JScrollPane getJScrollPane1(){
        if(ivjJScrollPane1 == null){
            try{
                ivjJScrollPane1 = new UIScrollPane();
                ivjJScrollPane1.setName("JScrollPane1");
//                ivjJScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                ivjJScrollPane1.setBounds(13, 27, 305, 155);
                ivjJScrollPane1.getViewport().add(getJEPanelCond(), null);
                ivjJScrollPane1.setBorder(BorderFactory.createLineBorder(Color.black));
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJScrollPane1;
    }

    /**
     * 返回 jEPanelCond 特性值。
     * @return javax.swing.JEditorPane
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JEditorPane getJEPanelCond(){
        if(jEPanelCond == null){
            try{
                jEPanelCond = new UIEditorPane();
                jEPanelCond.setName("JEPanelCond");
//                jEPanelCond.setBounds(0, 0, 160, 120);
                // user code begin {1}
                //将已经设置的内容放入“筛选条件”编辑框中
                String strFilterCond = doGetDefFilterCond();
                //debug
                //K('年')=2004 and mselect("buy->月评分",,,k('时间')=20040331)>=75
                //            strFilterCond = "K(\'年\')=2004 and mselect(\"buy->月评分\",,,k(\'时间\')=20040331)>=75";
                //end
                jEPanelCond.setText(strFilterCond);
                jEPanelCond.addKeyListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }

        return jEPanelCond;
    }

//    /**
//     * 返回 JTextAreaCond 特性值。
//     * @return javax.swing.JTextArea
//     */
//    /* 警告：此方法将重新生成。 */
//    private javax.swing.JTextArea getJTextAreaCond(){
//        if(ivjJTextAreaCond == null){
//            try{
//                ivjJTextAreaCond = new UITextArea();
//                ivjJTextAreaCond.setName("JTextAreaCond");
//                ivjJTextAreaCond.setBounds(0, 0, 160, 120);
//                // user code begin {1}
//                //将已经设置的内容放入“筛选条件”编辑框中
//                String strFilterCond = doGetDefFilterCond();
//                //debug
//                //K('年')=2004 and mselect("buy->月评分",,,k('时间')=20040331)>=75
//    //            strFilterCond = "K(\'年\')=2004 and mselect(\"buy->月评分\",,,k(\'时间\')=20040331)>=75";
//                //end
//                ivjJTextAreaCond.setText(strFilterCond);
//                // user code end
//            } catch(java.lang.Throwable ivjExc){
//                // user code begin {2}
//                // user code end
//                handleException(ivjExc);
//            }
//        }
//
//        return ivjJTextAreaCond;
//    }

    /**
     * 返回 UfoDialogContentPane 特性值。
     * @return javax.swing.JPanel
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JPanel getUfoDialogContentPane(){
        if(ivjUfoDialogContentPane == null){
            try{
                ivjUfoDialogContentPane = new UIPanel();
                ivjUfoDialogContentPane.setName("UfoDialogContentPane");
                ivjUfoDialogContentPane.setLayout(null);
                getUfoDialogContentPane().add(getJPanel1(), getJPanel1().getName());
                getUfoDialogContentPane().add(getJPanel2(), getJPanel2().getName());
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjUfoDialogContentPane;
    }

    /**
     * 每当部件抛出异常时被调用
     * @param exception java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception){

        /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
        // System.out.println("--------- 未捕捉到的异常 ---------");
        // exception.printStackTrace(System.out);
    }

    /**
     * 初始化类。
     */
    /* 警告：此方法将重新生成。 */
    private void initialize(){
        try{
            // user code begin {1}
            // user code end
            setName("SetFilterDefDlg");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(584, 308);
            setContentPane(getUfoDialogContentPane());
        } catch(java.lang.Throwable ivjExc){
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(StringResource.getStringResource("miufo1001311"));  //"数据处理->筛选"
        addHelp();
        // user code end
    }

    /**
     * 初始化参照列表
     * @param vecDPFilterData Vector
     * @param jRefItemList JComboBox
     */
    private void initRefList(JComboBox jRefItemList, Vector vecDPFilterData){
        if(jRefItemList == null || vecDPFilterData == null || vecDPFilterData.size() <= 0){
            return;
        }
        //初始化参照项目列表：
        //1）指标/关键字（其参照对话狂列表包含除去2）、3）、4）的内容
        //2）日期关键字，则同时增加“日期属性列表”、“日期值”的两个参照、
        //3）单位关键字，则同时增加“单位编码、单位名称、单位级次编码”3个参照
        //4）编码型关键字或指标：增加相应编码内容的参照(去掉“单位级次编码”,2005-04-28,from zhuyf,chengjie)
        int iSize = vecDPFilterData.size();
        for(int i = 0; i < iSize; i++){
            DataProcessFilterData dpFilterData = (DataProcessFilterData)vecDPFilterData.get(i);
            jRefItemList.addItem(dpFilterData);
        }

    }

    /**
     * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args){
        try{
            SetFilterDefDlg aSetFilterDefDlg;
            aSetFilterDefDlg = new SetFilterDefDlg(null, new java.util.Vector(), new java.util.Vector(),
                new DataProcessDef(), null);
            aSetFilterDefDlg.setModal(true);
            aSetFilterDefDlg.addWindowListener(new java.awt.event.WindowAdapter(){
                public void windowClosing(java.awt.event.WindowEvent e){
                    System.exit(0);
                };
            });
            aSetFilterDefDlg.show();
            java.awt.Insets insets = aSetFilterDefDlg.getInsets();
            aSetFilterDefDlg.setSize(aSetFilterDefDlg.getWidth() + insets.left + insets.right,
                                     aSetFilterDefDlg.getHeight() + insets.top + insets.bottom);
            aSetFilterDefDlg.setVisible(true);
        } catch(Throwable exception){
            System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog 's main():Exception ");
AppDebug.debug(exception);//@devTools             exception.printStackTrace(System.out);
        }
    }

    /**
     * 得到选择返回值的类型
     *
     * @param nTreeRefType int
     * @param strRefPara String
     * @return int
     */
    private int getSelType(int nTreeRefType, String strRefPara){
        int nSelType = -1;
        if(nTreeRefType == AbsRepToolTreeRefDlg.TREE_REF_TYPE_UNIT){
            //ITEM_UNIT_LEVEL_CODE,, ITEM_UNIT_CODE ,, ITEM_UNIT_NAME
            nSelType = Integer.parseInt(strRefPara);
        }
        return nSelType;
    }
}


 