package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIScrollPane;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dataprocess.cube.ICubeConst;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;

/**
 @update 2003-10-31 09:58 liulp
  增加帮助链接
 @end
 @update
   2003-10-28 19:35 liulp
    字段映射对象FieldMap添加Name属性，MapName的意义改为(映射类型+指标/关键字的PK)
 @end
 @update
  2003-10-23 13:07 liulp
  1)修改：汇总项只能增加一个，增加限制和提示框
  2003-10-23 20:34 liulp
  2)行标题、列标题、汇总项目必须分别至少选择一个项目！
 @end
 * 此处插入类型描述。
 *
 * 创建日期：(2003-8-13 11:57:54)
 * @author：刘良萍
 */
public class CrossTableDefDlg extends com.ufsoft.report.dialog.UfoDialog implements ActionListener, ListSelectionListener{
	private static final long serialVersionUID = 2318893075760385595L;
	//数据处理定义对象
    private CrossTabDef m_oDataProcessDef = null;
    /**
     * 对话框组件
     */
    private JButton ivjJBtnCancel = null;
    private JButton ivjJBtnOK = null;
    private JPanel ivjUfoDialogContentPane = null;
    private JButton ivjJBColTitleDown = null;
    private JButton ivjJBColTitleUp = null;
    private JButton ivjJBFromColTitle = null;
    private JButton ivjJBFromRowTitle = null;
    private JButton ivjJBFromTotal = null;
    private JButton ivjJBRowTitleDown = null;
    private JButton ivjJBRowTitleUp = null;
    private JButton ivjJBToColTitle = null;
    private JButton ivjJBToRowTitle = null;
    private JButton ivjJBTotalDown = null;
    private JButton ivjJBTotalUp = null;
    private JButton ivjJBToTotal = null;
    private JComboBox ivjJCBTotal = null;
    private JCheckBox ivjJCheckBoxCount = null;
    private JLabel ivjJLCol = null;
    private JLabel ivjJLReItem = null;
    private JLabel ivjJLRowTitle = null;
    private JLabel ivjJLTotal = null;
    private JLabel ivjJLTotalType = null;
    private JPanel ivjJPanelRe = null;
    private JPanel ivjJPanelSet = null;
    private JPanel ivjJPTitleItemProp = null;
    private JRadioButton ivjJRBBefor = null;
    private JRadioButton ivjJRBBehind = null;
    private JScrollPane ivjJSPColTitle = null;
    private JScrollPane ivjJSPReItem = null;
    private JScrollPane ivjJSPRowTitle = null;
    private JScrollPane ivjJSPTotal = null;
    //汇总方式
//    private JLabel ivjJLTotalType = null;
//    private JComboBox ivjj
    /**
     * 内部变量
     */
    //可选项目List
    private JList m_oReItemList = null;
    //列标题List
    private JList m_oColTitleList = null;
    //行标题List
    private JList m_oRowTitleList = null;
    //汇总List
    private JList m_oTotalTitleList = null;
    //可选项目的ListModel
    private ItemListModel m_oReItemModel = null;
    //列项目的ListModel
    private ItemListModel m_oColTitleItemModel = null;
    //行项目的ListModel
    private ItemListModel m_oRowTitleItemModel = null;
    //汇总项目的ListModel
    private ItemListModel m_oTotalTitleItemModel = null;
    //当编辑项目属性时，由于行、列、汇总三项是互斥编辑的，需要记录当前是哪个列表在编辑
    private int m_currentList = 0;
    //当前编辑列表标示
    private final int COL_LIST = 0;
    private final int ROW_LIST = 1;
    private final int TOL_LIST = 2;
    //CrossTableFld类型汉字显示:顺序索引与ICubeConst的汇总类型值相等
    private String[] m_oTypes = new String[]{
    		StringResource.getStringResource("miufopublic140"),   //"汇总"
    		StringResource.getStringResource("miufo1001255"),   //"计数"
			StringResource.getStringResource("miufo1001256"),   //"最大"
			StringResource.getStringResource("miufo1001257"),   //"最小"
			StringResource.getStringResource("miufo1001258")  //"平均"
			};
    //标示当前是否在对列表进行操作，避免在此时进行属性设置，调用setProp()
    private boolean isListSeling = false;
    
    /**
     * CrossTableDefDlg 构造子注解。
     */
    public CrossTableDefDlg(){
        super();
        initialize();
    }

    /**
     * 此处插入方法描述。
     *
     * 创建日期：(2003-8-22 14:25:40)
     * @author：刘良萍
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld java.util.Vector<CrossTableFld> - 元素为CrossTableFld
     * @param seledDef com.ufsoft.iuforeport.reporttool.process.basedef.CrossTabDef
     */
    public CrossTableDefDlg(java.awt.Container parent, Vector<DataProcessFld> vecAllDynAreaDPFld, CrossTabDef seledDef){
        super(parent);

        this.m_oDataProcessDef = seledDef;

        initList(vecAllDynAreaDPFld);

        initialize();

    }

    /**
     * GroupLayingDefDlg 构造子注解。
     * fullItem,全部的项目, Vector中为CrossTableFld对象
     * seledDef，已选择的项目所在实体对象
     */
    public CrossTableDefDlg(Vector<DataProcessFld> fullItem, CrossTabDef seledDef){
        super();
        this.m_oDataProcessDef = seledDef;
        initList(fullItem);
        initialize();
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(java.awt.event.ActionEvent e){
        if(e.getSource() == getJBtnCancel()){
            this.setResult(UfoDialog.ID_CANCEL);
            close();
        }
        //确定
        else if(e.getSource() == getJBtnOK()){
            //设置交叉表的数据处理定义
            int iColSize = m_oColTitleItemModel.getSize();
            int iRowSize = m_oRowTitleItemModel.getSize();
            int iTotalSize = m_oTotalTitleItemModel.getSize();
            if(iColSize <= 0 || iRowSize <= 0 || iTotalSize <= 0){
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001259"),this);  //"行标题、列标题、汇总项目必须分别至少选择一个项目！"
                return;
            }
            CrossTableFld[] colFlds = new CrossTableFld[iColSize];
            m_oColTitleItemModel.getAll().copyInto(colFlds);
            CrossTableFld[] rowFlds = new CrossTableFld[iRowSize];
            m_oRowTitleItemModel.getAll().copyInto(rowFlds);
            CrossTableFld[] totalFlds = new CrossTableFld[iTotalSize];
            m_oTotalTitleItemModel.getAll().copyInto(totalFlds);

            this.m_oDataProcessDef.setColDimFlds(colFlds);
            this.m_oDataProcessDef.setRowDimFlds(rowFlds);
            this.m_oDataProcessDef.setSummaryFlds(totalFlds);

            setResult(UfoDialog.ID_OK);
            close();
        }
        //添加到列标题List中
        else if(e.getSource() == ivjJBToColTitle){
            int selIndex = m_oReItemList.getSelectedIndex();
            moveToSeledList(selIndex, m_oColTitleItemModel, COL_LIST);
        }
        //添加到行标题List中
        else if(e.getSource() == ivjJBToRowTitle){
            int selIndex = m_oReItemList.getSelectedIndex();
            moveToSeledList(selIndex, m_oRowTitleItemModel, ROW_LIST);
        }
        //添加到汇总List中
        else if(e.getSource() == ivjJBToTotal){
            //目前算法汇总项只能计算一个（2003-10-23 11:13）
            if(m_oTotalTitleItemModel.getSize() <= 0){
                int selIndex = m_oReItemList.getSelectedIndex();
                moveToSeledList(selIndex, m_oTotalTitleItemModel, TOL_LIST);
                //并设置为选中状态：以便设置统计方式的缺省值
                m_oTotalTitleList.setSelectedIndex(0);
            } else{
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001260"),this);  //"本版本只支持一个汇总项！"
            }
        }
        //从列标题List中删除
        else if(e.getSource() == ivjJBFromColTitle){

            int selIndex = m_oColTitleList.getSelectedIndex();
            moveToReList(selIndex, m_oColTitleItemModel);
        }
        //从行标题List中删除
        else if(e.getSource() == ivjJBFromRowTitle){

            int selIndex = m_oRowTitleList.getSelectedIndex();
            moveToReList(selIndex, m_oRowTitleItemModel);
        }
        //从汇总List中删除
        else if(e.getSource() == ivjJBFromTotal){

            int selIndex = m_oTotalTitleList.getSelectedIndex();
            moveToReList(selIndex, m_oTotalTitleItemModel);
        }
        //列标题下移
        else if(e.getSource() == ivjJBColTitleDown){

            moveDown(m_oColTitleItemModel, m_oColTitleList);
        }
        //列标题上移
        else if(e.getSource() == ivjJBColTitleUp){

            moveUp(m_oColTitleItemModel, m_oColTitleList);
        }
        //行标题下移
        else if(e.getSource() == ivjJBRowTitleDown){

            moveDown(m_oRowTitleItemModel, m_oRowTitleList);
        }
        //行标题上移
        else if(e.getSource() == ivjJBRowTitleUp){

            moveUp(m_oRowTitleItemModel, m_oRowTitleList);
        }
        //汇总下移
        else if(e.getSource() == ivjJBTotalDown){

            moveDown(m_oTotalTitleItemModel, m_oTotalTitleList);
        }
        //汇总下移
        else if(e.getSource() == ivjJBTotalUp){

            moveUp(m_oTotalTitleItemModel, m_oTotalTitleList);
        }
        //设置属性
        else if(e.getSource() == ivjJCheckBoxCount){

            setProp();
            if(ivjJCheckBoxCount.isSelected()){
                ivjJRBBefor.setEnabled(true);
                ivjJRBBehind.setEnabled(true);
            } else{
                ivjJRBBefor.setEnabled(false);
                ivjJRBBehind.setEnabled(false);
            }
        } else if(e.getSource() == ivjJRBBefor || e.getSource() == ivjJRBBehind){
            if(ivjJCheckBoxCount.isSelected()){
                setProp();
            }
        }
        //汇总方式选择
        else if(e.getSource() == getJCBTotal()){
            int iSelTotalItemIndex = m_oTotalTitleList.getSelectedIndex();
            if(iSelTotalItemIndex >= 0){
                int iSelTotalTypeIndex = getJCBTotal().getSelectedIndex();
                if(iSelTotalTypeIndex >= 0){
                    CrossTableFld selTotalItem = (CrossTableFld)m_oTotalTitleItemModel.getObjectAt(iSelTotalItemIndex);
                    selTotalItem.setType(iSelTotalTypeIndex);
                }
            } else{
                //提示：“请先选中一个汇总项”
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001261"),this);  //"请先选中一个汇总项！"
            }
        }
    }

    /**
     * 增加帮助。
     *
     * 创建日期：(2003-10-31 09:56:54)
     * 创建者：刘良萍
     */
    private void addHelp(){
        javax.help.HelpBroker hb = ResConst.getHelpBroker();
        if(hb == null){
            return;
        }
        hb.enableHelpKey(getContentPane(), "TM_Data_Process_Cross", null);

    }

    /**
     * 此处插入方法描述。
     *
     * 创建日期：(2003-8-13 15:08:01)
     * @author：刘良萍
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.CrossTabDef
     */
    public CrossTabDef getDataProcessDef(){
        return m_oDataProcessDef;
    }

    /**
     * 返回 JBColTitleDown 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBColTitleDown(){
        if(ivjJBColTitleDown == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltodown.jpg");
                ivjJBColTitleDown = new UIButton(icon);
                ivjJBColTitleDown.setName("JBColTitleDown");
                ivjJBColTitleDown.setToolTipText("seltodown");
                ivjJBColTitleDown.setText("");
                ivjJBColTitleDown.setBounds(390, 51, icon.getIconWidth(), icon.getIconHeight());
                ivjJBColTitleDown.addActionListener(this);
                // user code begin {1}
                String strSelToDown = StringResource.getStringResource("miufo1001262");  //"向下移"
                ivjJBColTitleDown.setToolTipText(strSelToDown);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBColTitleDown;
    }

    /**
     * 返回 JBColTitleUp 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBColTitleUp(){
        if(ivjJBColTitleUp == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoup.jpg");
                ivjJBColTitleUp = new UIButton(icon);
                ivjJBColTitleUp.setName("JBColTitleUp");
                ivjJBColTitleUp.setToolTipText("seltoup");
                ivjJBColTitleUp.setText("");
                ivjJBColTitleUp.setBounds(390, 110, icon.getIconWidth(), icon.getIconHeight());
                ivjJBColTitleUp.addActionListener(this);
                // user code begin {1}
                String strSelToUp = StringResource.getStringResource("miufo1001263");  //"向上移"
                ivjJBColTitleUp.setToolTipText(strSelToUp);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBColTitleUp;
    }

    /**
     * 返回 JBFromColTitle 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBFromColTitle(){
        if(ivjJBFromColTitle == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoleft.jpg");
                ivjJBFromColTitle = new UIButton(icon);
                ivjJBFromColTitle.setName("JBFromColTitle");
                ivjJBFromColTitle.setToolTipText("seltoleft");
                ivjJBFromColTitle.setText("");
                ivjJBFromColTitle.setBounds(181, 110, icon.getIconWidth(), icon.getIconHeight());
                ivjJBFromColTitle.addActionListener(this);
                // user code begin {1}
                String strToLeft = StringResource.getStringResource("miufo1001264");  //"除去"
                ivjJBFromColTitle.setToolTipText(strToLeft);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBFromColTitle;
    }

    /**
     * 返回 JBFromRowTitle 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBFromRowTitle(){
        if(ivjJBFromRowTitle == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoleft.jpg");
                ivjJBFromRowTitle = new UIButton(icon);
                ivjJBFromRowTitle.setName("JBFromRowTitle");
                ivjJBFromRowTitle.setToolTipText("seltoleft");
                ivjJBFromRowTitle.setText("");
                ivjJBFromRowTitle.setBounds(9, 281, icon.getIconWidth(), icon.getIconHeight());
                ivjJBFromRowTitle.addActionListener(this);
                // user code begin {1}
                String strSelToLeft = StringResource.getStringResource("miufo1001264");  //"除去"
                ivjJBFromRowTitle.setToolTipText(strSelToLeft);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBFromRowTitle;
    }

    /**
     * 返回 JBFromTotal 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBFromTotal(){
        if(ivjJBFromTotal == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoleft.jpg");
                ivjJBFromTotal = new UIButton(icon);
                ivjJBFromTotal.setName("JBFromTotal");
                ivjJBFromTotal.setToolTipText("seltoleft");
                ivjJBFromTotal.setText("");
                ivjJBFromTotal.setBounds(181, 281, icon.getIconWidth(), icon.getIconHeight());
                ivjJBFromTotal.addActionListener(this);
                // user code begin {1}
                String strSelToLeft = StringResource.getStringResource("miufo1001264");  //"除去"
                ivjJBFromTotal.setToolTipText(strSelToLeft);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBFromTotal;
    }

    /**
     * 返回 JBRowTitleDown 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBRowTitleDown(){
        if(ivjJBRowTitleDown == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltodown.jpg");
                ivjJBRowTitleDown = new UIButton(icon);
                ivjJBRowTitleDown.setName("JBRowTitleDown");
                ivjJBRowTitleDown.setToolTipText("seltodown");
                ivjJBRowTitleDown.setText("");
                ivjJBRowTitleDown.setBounds(75, 170, icon.getIconWidth(), icon.getIconHeight());
                ivjJBRowTitleDown.addActionListener(this);
                // user code begin {1}
                String strSelToDown = StringResource.getStringResource("miufo1001262");  //"向下移"
                ivjJBRowTitleDown.setToolTipText(strSelToDown);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBRowTitleDown;
    }

    /**
     * 返回 JBRowTitleUp 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBRowTitleUp(){
        if(ivjJBRowTitleUp == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoup.jpg");
                ivjJBRowTitleUp = new UIButton(icon);
                ivjJBRowTitleUp.setName("JBRowTitleUp");
                ivjJBRowTitleUp.setToolTipText("seltoup");
                ivjJBRowTitleUp.setText("");
                ivjJBRowTitleUp.setBounds(124, 170, icon.getIconWidth(), icon.getIconHeight());
                ivjJBRowTitleUp.addActionListener(this);
                // user code begin {1}
                String strSelToUp = StringResource.getStringResource("miufo1001263");  //"向上移"
                ivjJBRowTitleUp.setToolTipText(strSelToUp);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBRowTitleUp;
    }

    /**
     * 返回 JBtnCancel 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnCancel(){
        if(ivjJBtnCancel == null){
            try{
                ivjJBtnCancel = new UIButton();
                ivjJBtnCancel.setName("JBtnCancel");
                ivjJBtnCancel.setText("Cancel");
                ivjJBtnCancel.setBounds(485, 475, 85, 27);
                // user code begin {1}
                String strCancel = StringResource.getStringResource(StringResource.CANCEL);//"取消";
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
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnOK(){
        if(ivjJBtnOK == null){
            try{
                ivjJBtnOK = new UIButton();
                ivjJBtnOK.setName("JBtnOK");
                ivjJBtnOK.setText("OK");
                ivjJBtnOK.setBounds(375, 475, 85, 27);
                // user code begin {1}
                String strOK = StringResource.getStringResource(StringResource.OK);//"确定";
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
     * 返回 JBToColTitle 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBToColTitle(){
        if(ivjJBToColTitle == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoright.jpg");
                ivjJBToColTitle = new UIButton(icon);
                ivjJBToColTitle.setName("JBToColTitle");
                ivjJBToColTitle.setToolTipText("seltoright");
                ivjJBToColTitle.setText("");
                ivjJBToColTitle.setBounds(181, 51, icon.getIconWidth(), icon.getIconHeight());
                ivjJBToColTitle.addActionListener(this);
                // user code begin {1}
                String strSelToRight = StringResource.getStringResource("miufo1000080");  //"增加"
                ivjJBToColTitle.setToolTipText(strSelToRight);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBToColTitle;
    }

    /**
     * 返回 JBToRowTitle 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBToRowTitle(){
        if(ivjJBToRowTitle == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoright.jpg");
                ivjJBToRowTitle = new UIButton(icon);
                ivjJBToRowTitle.setName("JBToRowTitle");
                ivjJBToRowTitle.setToolTipText("seltoright");
                ivjJBToRowTitle.setText("");
                ivjJBToRowTitle.setBounds(9, 232, icon.getIconWidth(), icon.getIconHeight());
                ivjJBToRowTitle.addActionListener(this);
                // user code begin {1}
                String strSelToRight = StringResource.getStringResource("miufo1000080");  //"增加"
                ivjJBToRowTitle.setToolTipText(strSelToRight);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBToRowTitle;
    }

    /**
     * 返回 JBTotalDown 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBTotalDown(){
        if(ivjJBTotalDown == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltodown.jpg");
                ivjJBTotalDown = new UIButton(icon);
                ivjJBTotalDown.setName("JBTotalDown");
                ivjJBTotalDown.setToolTipText("seltodown");
                ivjJBTotalDown.setText("");
                ivjJBTotalDown.setBounds(390, 232, icon.getIconWidth(), icon.getIconHeight());
                ivjJBTotalDown.addActionListener(this);
                // user code begin {1}
                String strSelToDown = StringResource.getStringResource("miufo1001262");  //"向下移"
                ivjJBTotalDown.setToolTipText(strSelToDown);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBTotalDown;
    }

    /**
     * 返回 JBTotalUp 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBTotalUp(){
        if(ivjJBTotalUp == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoup.jpg");
                ivjJBTotalUp = new UIButton(icon);
                ivjJBTotalUp.setName("JBTotalUp");
                ivjJBTotalUp.setToolTipText("seltoup");
                ivjJBTotalUp.setText("");
                ivjJBTotalUp.setBounds(390, 281, icon.getIconWidth(), icon.getIconHeight());
                ivjJBTotalUp.addActionListener(this);
                // user code begin {1}
                String strSelToUp = StringResource.getStringResource("miufo1001263");  //"向上移"
                ivjJBTotalUp.setToolTipText(strSelToUp);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBTotalUp;
    }

    /**
     * 返回 JBToTotal 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBToTotal(){
        if(ivjJBToTotal == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoright.jpg");
                ivjJBToTotal = new UIButton(icon);
                ivjJBToTotal.setName("JBToTotal");
                ivjJBToTotal.setToolTipText("seltoright");
                ivjJBToTotal.setText("");
                ivjJBToTotal.setBounds(181, 232, icon.getIconWidth(), icon.getIconHeight());
                ivjJBToTotal.addActionListener(this);
                // user code begin {1}
                String strSelToRight = StringResource.getStringResource("miufo1000080");  //"增加"
                ivjJBToTotal.setToolTipText(strSelToRight);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBToTotal;
    }

    /**
     * 返回 JCBTotal 特性值。
     * @return JComboBox
     */
    /* 警告：此方法将重新生成。 */
    private JComboBox getJCBTotal(){
        if(ivjJCBTotal == null){
            try{
                ivjJCBTotal = new UIComboBox();
                ivjJCBTotal.setName("JCBTotal");
//            ivjJCBTotal.setBounds(332, 465, 328, 23);
                ivjJCBTotal.setBounds(273, 348, 112, 25);
                // user code begin {1}
                for(int i = 0; i < m_oTypes.length; i++){
                    ivjJCBTotal.addItem(m_oTypes[i]);
                }
                ivjJCBTotal.addActionListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJCBTotal;
    }

    /**
     * 返回 JCheckBoxCount 特性值。
     * @return JCheckBox
     */
    /* 警告：此方法将重新生成。 */
    private JCheckBox getJCheckBoxCount(){
        if(ivjJCheckBoxCount == null){
            try{
                ivjJCheckBoxCount = new UICheckBox();
                ivjJCheckBoxCount.setName("JCheckBoxCount");
                ivjJCheckBoxCount.setText("SummaryItem");
                ivjJCheckBoxCount.setBounds(20, 15, 97, 22);
                ivjJCheckBoxCount.addActionListener(this);
                // user code begin {1}
                String strSummaryItem = StringResource.getStringResource("miufo1001265");  //"小计项"
                ivjJCheckBoxCount.setText(strSummaryItem);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJCheckBoxCount;
    }

    /**
     * 返回 JLCol 特性值。
     * @return JLabel
     */
    /* 警告：此方法将重新生成。 */
    private JLabel getJLCol(){
        if(ivjJLCol == null){
            try{
                ivjJLCol = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLCol.setName("JLCol");
                ivjJLCol.setText("Col:");
                ivjJLCol.setBounds(223, 15, 60, 14);
                // user code begin {1}
                String strColumn = StringResource.getStringResource("miufo1001266");  //"列："
                ivjJLCol.setText(strColumn);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLCol;
    }

    /**
     * 返回 JLCol 特性值。
     * @return JLabel
     */
    /* 警告：此方法将重新生成。 */
    private JLabel getJLTotalType(){
        if(ivjJLTotalType == null){
            try{
                ivjJLTotalType = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLTotalType.setName("JLTotalType");
                ivjJLTotalType.setText("Way:");
                ivjJLTotalType.setBounds(223, 348, 42, 25);
                // user code begin {1}
                String strWay = StringResource.getStringResource("miufo1001267");  //"方式："
                ivjJLTotalType.setText(strWay);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLTotalType;
    }

    /**
     * 返回 JLReItem 特性值。
     * @return JLabel
     */
    /* 警告：此方法将重新生成。 */
    private JLabel getJLReItem(){
        if(ivjJLReItem == null){
            try{
                ivjJLReItem = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLReItem.setName("JLReItem");
                ivjJLReItem.setText("ReadyItem:");
                ivjJLReItem.setBounds(9, 14, 113, 14);
                // user code begin {1}
                String strReadyItem = StringResource.getStringResource("miufo1001268");  //"备选项目："
                ivjJLReItem.setText(strReadyItem);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLReItem;
    }

    /**
     * 返回 JLRowTitle 特性值。
     * @return JLabel
     */
    /* 警告：此方法将重新生成。 */
    private JLabel getJLRowTitle(){
        if(ivjJLRowTitle == null){
            try{
                ivjJLRowTitle = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLRowTitle.setName("JLRowTitle");
                ivjJLRowTitle.setText("Row:");
                ivjJLRowTitle.setBounds(51, 190, 45, 14);
                // user code begin {1}
                String strRow = StringResource.getStringResource("miufo1001269");  //"行："
                ivjJLRowTitle.setText(strRow);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLRowTitle;
    }

    /**
     * 返回 JLTotal 特性值。
     * @return JLabel
     */
    /* 警告：此方法将重新生成。 */
    private JLabel getJLTotal(){
        if(ivjJLTotal == null){
            try{
                ivjJLTotal = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLTotal.setName("JLTotal");
                ivjJLTotal.setText("TotalItem:");
                ivjJLTotal.setBounds(223, 190, 73, 14);
                // user code begin {1}
                String strTotalItem =StringResource.getStringResource("miufo1001270");  //"统计项："
                ivjJLTotal.setText(strTotalItem);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLTotal;
    }

    /**
     * 返回 JPanelRe 特性值。
     * @return JPanel
     */
    /* 警告：此方法将重新生成。 */
    private JPanel getJPanelRe(){
        if(ivjJPanelRe == null){
            try{
                ivjJPanelRe = new UIPanel();
                ivjJPanelRe.setName("JPanelRe");
                ivjJPanelRe.setLayout(null);
                ivjJPanelRe.setBounds(20, 20, 108, 445);
                getJPanelRe().add(getJSPReItem(), getJSPReItem().getName());
                getJPanelRe().add(getJLReItem(), getJLReItem().getName());
                Border etched = BorderFactory.createEtchedBorder();
                Border title = BorderFactory.createTitledBorder(etched, "Src Item");
                getJPanelRe().setBorder(title);
                // user code begin {1}
                String strTitle = StringResource.getStringResource("miufo1001271");  //"源项目"
                ((javax.swing.border.TitledBorder)title).setTitle(strTitle);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanelRe;
    }

    /**
     * 返回 JPanelSet 特性值。
     * @return JPanel
     */
    /* 警告：此方法将重新生成。 */
    private JPanel getJPanelSet(){
        if(ivjJPanelSet == null){
            try{
                ivjJPanelSet = new UIPanel();
                ivjJPanelSet.setName("JPanelSet");
                ivjJPanelSet.setLayout(null);
                ivjJPanelSet.setBounds(133, 20, 447, 400);
                getJPanelSet().add(getJSPRowTitle(), getJSPRowTitle().getName());
                getJPanelSet().add(getJSPColTitle(), getJSPColTitle().getName());
                getJPanelSet().add(getJSPTotal(), getJSPTotal().getName());
                //added by liulp,2004-04-08 15:14
                getJPanelSet().add(getJLTotalType(), getJLTotalType().getName());
                getJPanelSet().add(getJBToRowTitle(), getJBToRowTitle().getName());
                getJPanelSet().add(getJBFromRowTitle(), getJBFromRowTitle().getName());
                getJPanelSet().add(getJBToColTitle(), getJBToColTitle().getName());
                getJPanelSet().add(getJBFromColTitle(), getJBFromColTitle().getName());
                getJPanelSet().add(getJBToTotal(), getJBToTotal().getName());
                getJPanelSet().add(getJBFromTotal(), getJBFromTotal().getName());
                getJPanelSet().add(getJBRowTitleUp(), getJBRowTitleUp().getName());
                getJPanelSet().add(getJBRowTitleDown(), getJBRowTitleDown().getName());
                getJPanelSet().add(getJBTotalDown(), getJBTotalDown().getName());
                getJPanelSet().add(getJBTotalUp(), getJBTotalUp().getName());
                getJPanelSet().add(getJBColTitleUp(), getJBColTitleUp().getName());
                getJPanelSet().add(getJBColTitleDown(), getJBColTitleDown().getName());
                getJPanelSet().add(getJLCol(), getJLCol().getName());
                getJPanelSet().add(getJLRowTitle(), getJLRowTitle().getName());
                getJPanelSet().add(getJLTotal(), getJLTotal().getName());
                getJPanelSet().add(getJCBTotal(), getJCBTotal().getName());

                Border etched = BorderFactory.createEtchedBorder();
                Border title = BorderFactory.createTitledBorder(etched, "Item Select");
                getJPanelSet().setBorder(title);
                // user code begin {1}
                String strTitle = StringResource.getStringResource("miufo1001272");  //"项目选择"
                ((javax.swing.border.TitledBorder)title).setTitle(strTitle);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanelSet;
    }

    /**
     * 返回 JPTitleItemProp 特性值。
     * @return JPanel
     */
    /* 警告：此方法将重新生成。 */
    private JPanel getJPTitleItemProp(){
        if(ivjJPTitleItemProp == null){
            try{
                ivjJPTitleItemProp = new UIPanel();
                ivjJPTitleItemProp.setName("JPTitleItemProp");
                ivjJPTitleItemProp.setLayout(null);
                ivjJPTitleItemProp.setBounds(133, 425, 447, 45);
                getJPTitleItemProp().add(getJCheckBoxCount(), getJCheckBoxCount().getName());

                ButtonGroup group = new ButtonGroup();
                group.add(getJRBBefor());
                group.add(getJRBBehind());

                getJPTitleItemProp().add(getJRBBefor(), getJRBBefor().getName());
                getJPTitleItemProp().add(getJRBBehind(), getJRBBehind().getName());

                Border etched = BorderFactory.createEtchedBorder();
                Border title = BorderFactory.createTitledBorder(etched, "Item Prop Edit");
                getJPTitleItemProp().setBorder(title);
                // user code begin {1}
                String strTitle = StringResource.getStringResource("miufo1001273");  //"项目属性编辑"
                ((javax.swing.border.TitledBorder)title).setTitle(strTitle);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPTitleItemProp;
    }

    /**
     * 返回 JRBBefor 特性值。
     * @return JRadioButton
     */
    /* 警告：此方法将重新生成。 */
    private JRadioButton getJRBBefor(){
        if(ivjJRBBefor == null){
            try{
                ivjJRBBefor = new UIRadioButton();
                ivjJRBBefor.setName("JRBBefor");
                ivjJRBBefor.setText("Befor");
                ivjJRBBefor.setBounds(180, 10, 70, 22);
                // user code begin {1}
                String strBefor = StringResource.getStringResource("miufo1001274");  //"前"
                ivjJRBBefor.setText(strBefor);
                ivjJRBBefor.addActionListener(this);
                ivjJRBBefor.setSelected(false);
                ivjJRBBefor.setEnabled(false);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJRBBefor;
    }

    /**
     * 返回 JRBBehind 特性值。
     * @return JRadioButton
     */
    /* 警告：此方法将重新生成。 */
    private JRadioButton getJRBBehind(){
        if(ivjJRBBehind == null){
            try{
                ivjJRBBehind = new UIRadioButton();
                ivjJRBBehind.setName("JRBBehind");
                ivjJRBBehind.setText("Behind");
                ivjJRBBehind.setBounds(270, 10, 108, 22);
                ivjJRBBehind.addActionListener(this);
                // user code begin {1}
                String strBehind = StringResource.getStringResource("miufo1001275");  //"后"
                ivjJRBBehind.setText(strBehind);
                ivjJRBBehind.setSelected(true);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJRBBehind;
    }

    /**
     * 返回 JSPColTitle 特性值。
     * @return JScrollPane
     */
    /* 警告：此方法将重新生成。 */
    private JScrollPane getJSPColTitle(){
        if(ivjJSPColTitle == null){
            try{
                m_oColTitleList = new UIList(m_oColTitleItemModel);
                m_oColTitleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                ivjJSPColTitle = new UIScrollPane(m_oColTitleList);
                ivjJSPColTitle.setName("JSPColTitle");
                ivjJSPColTitle.setBounds(223, 30, 162, 135);
                m_oColTitleList.addListSelectionListener(this);
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSPColTitle;
    }

    /**
     * 返回 JSPReItem 特性值。
     * @return JScrollPane
     */
    /* 警告：此方法将重新生成。 */
    private JScrollPane getJSPReItem(){
        if(ivjJSPReItem == null){
            try{
                m_oReItemList = new UIList(m_oReItemModel);
                m_oReItemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                ivjJSPReItem = new UIScrollPane(m_oReItemList);
                ivjJSPReItem.setName("JSPReItem");
                ivjJSPReItem.setBounds(9, 30, 90, 400);
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSPReItem;
    }

    /**
     * 返回 JSPRowTitle 特性值。
     * @return JScrollPane
     */
    /* 警告：此方法将重新生成。 */
    private JScrollPane getJSPRowTitle(){
        if(ivjJSPRowTitle == null){
            try{
                m_oRowTitleList = new UIList(m_oRowTitleItemModel);
                m_oRowTitleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                ivjJSPRowTitle = new UIScrollPane(m_oRowTitleList);
                ivjJSPRowTitle.setName("JSPRowTitle");
                ivjJSPRowTitle.setBounds(51, 208, 120, 180);
                m_oRowTitleList.addListSelectionListener(this);
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSPRowTitle;
    }

    /**
     * 返回 JSPTotal 特性值。
     * @return JScrollPane
     */
    /* 警告：此方法将重新生成。 */
    private JScrollPane getJSPTotal(){
        if(ivjJSPTotal == null){
            try{
                m_oTotalTitleList = new UIList(m_oTotalTitleItemModel);
                m_oTotalTitleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                ivjJSPTotal = new UIScrollPane(m_oTotalTitleList);
                ivjJSPTotal.setName("JSPTotal");
//            ivjJSPTotal.setBounds(223, 208, 162, 180);
                ivjJSPTotal.setBounds(223, 208, 162, 130);
                // user code begin {1}
                m_oTotalTitleList.addListSelectionListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJSPTotal;
    }

    /**
     * 返回 UfoDialogContentPane 特性值。
     * @return JPanel
     */
    /* 警告：此方法将重新生成。 */
    private JPanel getUfoDialogContentPane(){
        if(ivjUfoDialogContentPane == null){
            try{
                ivjUfoDialogContentPane = new UIPanel();
                ivjUfoDialogContentPane.setName("UfoDialogContentPane");
                ivjUfoDialogContentPane.setLayout(null);
                getUfoDialogContentPane().add(getJBtnOK(), getJBtnOK().getName());
                getUfoDialogContentPane().add(getJBtnCancel(), getJBtnCancel().getName());
                getUfoDialogContentPane().add(getJPanelRe(), getJPanelRe().getName());
                getUfoDialogContentPane().add(getJPanelSet(), getJPanelSet().getName());
                getUfoDialogContentPane().add(getJPTitleItemProp(), getJPTitleItemProp().getName());
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
            setName("CrossTableDefDlg");
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setSize(600, 555);
            setContentPane(getUfoDialogContentPane());
        } catch(java.lang.Throwable ivjExc){
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(StringResource.getStringResource("miufo1001276"));  //"交叉表"
        //设置汇总项的值
        if(m_oTotalTitleItemModel.getSize() > 0){
            CrossTableFld firstTotalFld = (CrossTableFld)m_oTotalTitleItemModel.getObjectAt(0);
            m_oTotalTitleList.setSelectedIndex(0);
            ivjJCBTotal.setSelectedIndex(firstTotalFld.getType());
            m_oTotalTitleList.setSelectedIndex(-1);
//            getJBtnCancel().requestFocus(true);
        }
        addHelp();
        // user code end
    }

    /**
     * 初始化可选择项目列表和已选择项目列表
     * 可选择列表为全部项目除去已选择列表中已选择的项目后的剩余项目
     * fullItem ,全部项目，Vector中为CrossTableFld对象
     * 创建日期：(2003-8-19 16:43:09)
     */
    public void initList(Vector<DataProcessFld> fullItem){

        Vector<CrossTableFld> colVec = new Vector<CrossTableFld>();
        Vector<CrossTableFld> rowVec = new Vector<CrossTableFld>();
        Vector<CrossTableFld> totalVec = new Vector<CrossTableFld>();
        Hashtable<String, CrossTableFld> seledNameHash = new Hashtable<String, CrossTableFld>();
        CrossTableFld[] colFlds = m_oDataProcessDef.getColDimFlds();
        CrossTableFld[] rowFlds = m_oDataProcessDef.getRowDimFlds();
        CrossTableFld[] totalFlds = m_oDataProcessDef.getSummaryFlds();
        int colLen = colFlds != null ? colFlds.length : 0;
        int rowLen = rowFlds != null ? rowFlds.length : 0;
        int totalLen = totalFlds != null ? totalFlds.length : 0;
        if(colLen + rowLen + totalLen > 0){
            //此时相当于修改
            //构造已选择的项目Vector，构造Vector（GroupLayingFld.getMapType()+GroupLayingFld.getMapName()），

            String strSeledName = null;
            CrossTableFld curFld = null;
            if(colFlds != null){
	            for(int i = 0; i < colLen; i++){
	                curFld = (CrossTableFld)colFlds[i].clone();
	                colVec.addElement(curFld);
	                strSeledName = curFld.getMapName();
	                seledNameHash.put(strSeledName, curFld);
	            }
            }
            if(rowFlds != null){
	            for(int i = 0; i < rowLen; i++){
	                curFld = (CrossTableFld)rowFlds[i].clone();
	                rowVec.addElement(curFld);
	                strSeledName = curFld.getMapName();
	                seledNameHash.put(strSeledName, curFld);
	            }
            }
            if(totalFlds != null){
	            for(int i = 0; i < totalLen; i++){
	                curFld = (CrossTableFld)totalFlds[i].clone();
	                totalVec.addElement(curFld);
	                strSeledName = curFld.getMapName();
	                seledNameHash.put(strSeledName, curFld);
	            }
            }
            //构造已选项目列表
            m_oColTitleItemModel = new ItemListModel(colVec);
            m_oRowTitleItemModel = new ItemListModel(rowVec);
            m_oTotalTitleItemModel = new ItemListModel(totalVec);
        } else{
            //此时相当于新建
            //构造已选项目列表
            m_oReItemModel = new ItemListModel(fullItem);
            m_oColTitleItemModel = new ItemListModel();
            m_oRowTitleItemModel = new ItemListModel();
            m_oTotalTitleItemModel = new ItemListModel();
        }

        //构建可选择项目列表，此时需要通过循环比较过滤已选择的项目
        //过滤
        CrossTableFld crossTableFld = null;
        DataProcessFld item = null;
        String strKey = null;
        Vector<CrossTableFld> vecRSelectItem = new Vector<CrossTableFld>();
        for(int i = 0; i < fullItem.size(); i++){
            item = (DataProcessFld)fullItem.get(i);
            strKey = item.getMapName();
            if(!seledNameHash.containsKey(strKey)){
                //可选项目的格式信息就是动态区域的原始定义位置信息
                crossTableFld = new CrossTableFld(item);
                vecRSelectItem.add(crossTableFld);
            } else{
                //将分组、分层字段的位置信息回设为动态区域的原始定义位置信息
                //modified by liulp
                crossTableFld = (CrossTableFld)seledNameHash.get(strKey);
                crossTableFld.setFormatRelatCell(item.getFormatRelatCell());
                crossTableFld.setDisplayRelatCell(item.getDisplayRelatCell());
            }
        }
        //构造可选项目列表
        m_oReItemModel = new ItemListModel(vecRSelectItem);
    }

    /**
     * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args){
        try{
            CrossTableDefDlg aCrossTableDefDlg;
            Vector<DataProcessFld> a = new Vector<DataProcessFld>();
            for(int i = 0; i < 20; i++){
                CrossTableFld item = new CrossTableFld();
                item.setName("new-OldName_" + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i +
                             i + i);
                item.setMapName("new_" + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i + i +
                                i);
                a.addElement(item);
            }
            aCrossTableDefDlg = new CrossTableDefDlg(a, null);
            aCrossTableDefDlg.setModal(true);
            aCrossTableDefDlg.addWindowListener(new java.awt.event.WindowAdapter(){
                public void windowClosing(java.awt.event.WindowEvent e){
                    System.exit(0);
                };
            });
            aCrossTableDefDlg.show();
            java.awt.Insets insets = aCrossTableDefDlg.getInsets();
            aCrossTableDefDlg.setSize(aCrossTableDefDlg.getWidth() + insets.left + insets.right,
                                      aCrossTableDefDlg.getHeight() + insets.top + insets.bottom);
            aCrossTableDefDlg.setVisible(true);
        } catch(Throwable exception){
            System.err.println(StringResource.getStringResource("miufo1000953"));  //"com.ufsoft.iuforeport.reporttool.pub.UfoDialog 的 main() 中发生异常"
AppDebug.debug(exception);//@devTools             exception.printStackTrace(System.out);
        }
    }

    /**
     * 将列表中选择一个项目下移一位
     * 创建日期：(2003-8-20 22:46:21)
     * @param index int
     * @param sourceListmodel com.ufsoft.iuforeport.reporttool.process.basedef.ItemListModel 源列表
     */
    private void moveDown(ItemListModel sourceListmodel, JList sourceList){
        int selIndex = sourceList.getSelectedIndex();
        if(selIndex < 0){
            return;
        }
        sourceListmodel.putItemDown(selIndex);
        if(selIndex < sourceListmodel.getSize() - 1){
            sourceList.setSelectedIndex(selIndex + 1);
        }
    }

    /**
     * 从可选列表中选择一个项目到目标列表
     * 创建日期：(2003-8-20 22:46:21)
     * @param index int
     * @param sourceListmodel com.ufsoft.iuforeport.reporttool.process.basedef.ItemListModel 源列表
     */
    private void moveToReList(int selIndex, ItemListModel sourceListmodel){
        if(selIndex < 0){
            return;
        }
        FieldMap item = sourceListmodel.removeItem(selIndex);
        ( (CrossTableFld)item).setType(ICubeConst.DIM_NON); m_oReItemModel.addItem(item);
    }

    /**
     * 从可选列表中选择一个项目到目标列表
     * 创建日期：(2003-8-20 22:46:21)
     * @param index int
     * @param aimListmodel com.ufsoft.iuforeport.reporttool.process.basedef.ItemListModel 目标列表
     */
    private void moveToSeledList(int selIndex, ItemListModel aimListmodel, int listIndex){

        if(selIndex < 0){
            return;
        }
        FieldMap item = m_oReItemModel.removeItem(selIndex);
        //根据不同的列表设置不同的值
        switch(listIndex){
            case COL_LIST:
                 ( (CrossTableFld)item).setType(ICubeConst.DIM_Dimension);
                ( (CrossTableFld)item).setTitleType(ICubeConst.dgCol);
                break;
            case ROW_LIST:
                 ( (CrossTableFld)item).setType(ICubeConst.DIM_Dimension);
                ( (CrossTableFld)item).setTitleType(ICubeConst.dgRow);
                break;
            case TOL_LIST:
                 ( (CrossTableFld)item).setType(ICubeConst.DIM_Sum);
                break;
        }
        aimListmodel.addItem(item);
    }

    /**
     * 将列表中选择一个项目上移一位
     * 创建日期：(2003-8-20 22:46:21)
     * @param index int
     * @param sourceListmodel com.ufsoft.iuforeport.reporttool.process.basedef.ItemListModel 源列表
     */
    private void moveUp(ItemListModel sourceListmodel, JList sourceList){
        int selIndex = sourceList.getSelectedIndex();
        if(selIndex < 0){
            return;
        }
        sourceListmodel.putItemUp(selIndex);
        if(selIndex > 0){
            sourceList.setSelectedIndex(selIndex - 1);
        }
    }

    /**
     * 此处插入方法描述。
     *
     * 创建日期：(2003-8-13 15:08:01)
     * @author：刘良萍
     * @param newDataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.CrossTabDef
     */
    public void setDataProcessDef(CrossTabDef newDataProcessDef){
        m_oDataProcessDef = newDataProcessDef;
    }

    /**
     * 设置项目属性
     * 创建日期：(2003-8-21 0:18:28)
     */
    private void setProp(){
        if(!isListSeling){
            int selIndex;
            CrossTableFld item = null;
            boolean isTotal = false;
            int pos = CrossTableFld.SUBTOTAL_PRE;
            switch(m_currentList){
                case COL_LIST:
                    selIndex = m_oColTitleList.getSelectedIndex();
                    item = (CrossTableFld)m_oColTitleItemModel.getObjectAt(selIndex);
                    m_oColTitleList.removeSelectionInterval(selIndex, selIndex);
                    break;
                case ROW_LIST:
                    selIndex = m_oRowTitleList.getSelectedIndex();
                    item = (CrossTableFld)m_oRowTitleItemModel.getObjectAt(selIndex);
                    m_oRowTitleList.removeSelectionInterval(selIndex, selIndex);
                    break;
                case TOL_LIST:
                    selIndex = m_oTotalTitleList.getSelectedIndex();
                    item = (CrossTableFld)m_oTotalTitleItemModel.getObjectAt(selIndex);
                    m_oTotalTitleList.removeSelectionInterval(selIndex, selIndex);
                    break;
            }
            if(ivjJCheckBoxCount.isSelected()){
                isTotal = true;
            }
            if(ivjJRBBehind.isSelected()){
                pos = CrossTableFld.SUBTOTAL_AFTER;
            }
            if(item != null){
	            item.setSubTotal(isTotal);
	            item.setSubTotalPos(pos);
            }
        }
    }

    //列表选择后的响应处理
    public void valueChanged(ListSelectionEvent event){
        //对属性面板赋值仅限于行、列、汇总三个列表
        isListSeling = true;
        CrossTableFld item = null;
        int rowSel, colSel, totalSel;
        colSel = m_oColTitleList.getSelectedIndex();
        rowSel = m_oRowTitleList.getSelectedIndex();
        totalSel = m_oTotalTitleList.getSelectedIndex();
        if(event.getSource() == m_oColTitleList){
            m_currentList = COL_LIST;
            if(colSel < 0){
                return;
            }
            item = (CrossTableFld)m_oColTitleItemModel.getObjectAt(colSel);
            m_oRowTitleList.removeSelectionInterval(rowSel, rowSel);
            m_oTotalTitleList.removeSelectionInterval(totalSel, totalSel);

        } else if(event.getSource() == m_oRowTitleList){
            m_currentList = ROW_LIST;
            if(rowSel < 0){
                return;
            }
            item = (CrossTableFld)m_oRowTitleItemModel.getObjectAt(rowSel);
            m_oColTitleList.removeSelectionInterval(colSel, colSel);
            m_oTotalTitleList.removeSelectionInterval(totalSel, totalSel);
        } else if(event.getSource() == m_oTotalTitleList){
            m_currentList = TOL_LIST;
            if(totalSel < 0){
                return;
            }
            item = (CrossTableFld)m_oTotalTitleItemModel.getObjectAt(totalSel);
            ivjJCBTotal.setSelectedIndex(item.getType());
            m_oRowTitleList.removeSelectionInterval(rowSel, rowSel);
            m_oColTitleList.removeSelectionInterval(colSel, colSel);
        } else{
            return;
        }
        if(item.isSubTotal()){
            ivjJCheckBoxCount.setSelected(true);
            ivjJRBBefor.setEnabled(true);
            ivjJRBBehind.setEnabled(true);
            if(item.getSubTotalPos() == CrossTableFld.SUBTOTAL_PRE){
                ivjJRBBefor.setSelected(true);
            } else{
                ivjJRBBehind.setSelected(true);
            }
        } else{
            ivjJCheckBoxCount.setSelected(false);
            ivjJRBBefor.setEnabled(false);
            ivjJRBBehind.setEnabled(false);
        }
        isListSeling = false;
    }
}


