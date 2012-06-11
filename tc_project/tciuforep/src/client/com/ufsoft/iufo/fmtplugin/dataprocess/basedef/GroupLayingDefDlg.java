package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;

import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;

/**
 @update
 2003-12-16 whtao
 改正切换已选项目时,取消标题区后自动获得拆分规则内容的错误
 @end
 @update
  2003-11-19 11:19 liulp
   分层的数据处理字段只能是字符型的修改
 @end
 @update 2003-10-31 09:58 liulp
  增加帮助链接
 @end
 @update
   2003-10-28 19:35 liulp
   字段映射对象FieldMap添加Name属性，MapName的意义改为(映射类型+指标/关键字的PK)
 @end
 @update
 2003-10-23 20:48 liulp
  添加逻辑控制：必须选择一个分组项目
 @end
 * 设置分组（分层）数据处理定义的对话框。
 *
 * 创建日期：(2003-8-12 17:01:24)
 * @author：刘良萍
 */
public class GroupLayingDefDlg extends com.ufsoft.report.dialog.UfoDialog implements ActionListener,
    ListSelectionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = -170593848211928270L;
	/**
     *	Dialog的Swing组件
     */
    private JButton ivjJBtnCancel = null;
    private JButton ivjJBtnOK = null;
    private JPanel ivjUfoDialogContentPane = null;
    private JButton ivjJBtnDown = null;
    private JButton ivjJBtnSelsToL = null;
    private JButton ivjJBtnSelsToR = null;
    private JButton ivjJBtnToBottom = null;
    private JButton ivjJBtnToL = null;
    private JButton ivjJBtnToR = null;
    private JButton ivjJBtnToTop = null;
    private JButton ivjJBtnUp = null;
    private JButton ivjJBtnRole = null;
    private JCheckBox ivjJCheckBTitle = null;
    private JCheckBox ivjJCheckDoLastLayer = null;
    private JLabel ivjJLCodeRole = null;
    private JLabel ivjJLRSelItem = null;
    private JLabel ivjJLSeledItem = null;
    private JPanel ivjJPanelGroupSel = null;
    private JPanel ivjJPanelProp = null;
    private JTextField ivjJTFRole = null;
    private JScrollPane ivjJScrolPaneItem = null;
    private JScrollPane ivjJScrolPaneSeledItem = null;
    private JList m_ReList = null;
    private JList m_SeledList = null;
    /**
     *	内部变量
     */
    //数据处理定义对象
    private GroupLayingDef m_oDataProcessDef = null;
    
//    //3.5 old property:是否是鼠标双击标记
//    private int m_nClickCount = 0;
//    //3.5 old property:是双击最大时间间隔(毫秒)
//    private long m_nDBClickDelay = 500;
    
    //待选项目的ListModel
    private ItemListModel m_oRSelItemModel = null;
    //已选项目的ListModel
    private ItemListModel m_oSeledItemModel = null;
    //动态区域内所有数据处理字段对应（指标）的数据类型hash(mapName,Integer(measType))
    private Hashtable m_hashDataTypes = null;
    /**
     * GroupLayingDefDlg 构造子注解。
     */
    public GroupLayingDefDlg(){
        super();
        initList(null);
        initialize();
    }

    /**
     * GroupLayingDefDlg 构造子注解。
     * @param parent java.awt.Container
     */
    public GroupLayingDefDlg(java.awt.Container parent){
        super(parent);
        initList(null);
        initialize();
    }

    /**
     * GroupLayingDefDlg 构造子注解。
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld,全部的项目, Vector中为DataProcessFld对象
     * @param hashDataTypes java.util.Hashtable - 动态区域内所有数据处理字段对应（指标）的数据类型hash(mapName,Integer(measType))
     * @param seledDef，已选择的项目所在实体对象
     */
    public GroupLayingDefDlg(java.awt.Container parent, Vector<DataProcessFld> vecAllDynAreaDPFld, Hashtable hashDataTypes,
                             GroupLayingDef seledDef){
        super(parent);
        this.m_oDataProcessDef = seledDef;
        this.m_hashDataTypes = hashDataTypes;
        initList(vecAllDynAreaDPFld);

        initialize();
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(java.awt.event.ActionEvent e){
        if(e.getSource() == ivjJBtnCancel){
            this.setResult(UfoDialog.ID_CANCEL);
            close();
        }
        //设置分组的数据处理定义
        else if(e.getSource() == getJBtnOK()){
            //设置分组的数据处理定义
            //从已选项目列表中取出数据，根据选择的类型不同来进行分类（标题，分层。。）
            //标题Vector
            Vector<GroupLayingFld> groupFldVec = new Vector<GroupLayingFld>();
            //分层Vector
            Vector<GroupLayingFld> layingFldVec = new Vector<GroupLayingFld>();
            //详细内容Hashtable
            Hashtable<String, DataProcessFld> detailFldTable = new Hashtable<String, DataProcessFld>();
            Vector ItemVec = m_oSeledItemModel.getAll();
            if(ItemVec == null || ItemVec.size() <= 0){
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001285"),this);  //"必须至少选择一个分组项目！"
                return;
            }
            if(m_oRSelItemModel.getSize() < 1){
                int titleNum = 0;
                for(int i = 0; i < ItemVec.size(); i++){
                    GroupLayingFld fldTemp = (GroupLayingFld)ItemVec.get(i);
                    if(fldTemp.isTitle()){
                        titleNum += 1;
                    }
                }
                if(titleNum == ItemVec.size()){
                    UfoPublic.sendWarningMessage(
                        StringResource.getStringResource("miufo1001286")+"\n" +
						StringResource.getStringResource("miufo1001287"),this);  //"或已选项目中至少有一个非标题区项目，作为细节扩展！"
                    return;
                }
            }
            GroupLayingFld item;
            //对已选项目进行分类处理
            for(int i = 0; i < ItemVec.size(); i++){
                item = (GroupLayingFld)ItemVec.get(i);
                if(item.isTitle()){
                    groupFldVec.addElement(item);
                } else{
                    if(item.getCodeRule() != null && item.getCodeRule().trim().length() != 0){
                        layingFldVec.addElement(item);
                    } else{
                        groupFldVec.addElement(item);
                    }
                    //不是标题的分组、有编码规则的分层，都需要放入细节区域显示列表中
                    detailFldTable.put(item.getMapName(), item);
                }
            }
            //对剩余可选项目进行处理，全部增加到详细内容Hashtable
            ItemVec = m_oRSelItemModel.getAll();
            for(int j = 0; j < ItemVec.size(); j++){
                item = (GroupLayingFld)ItemVec.get(j);
                detailFldTable.put(item.getMapName(), item);
            }
            //对m_oDataProcessDef进行赋值
            GroupLayingFld[] flds = new GroupLayingFld[groupFldVec.size()];
            groupFldVec.copyInto(flds);
            m_oDataProcessDef.setGroupDPFlds(flds);
            flds = new GroupLayingFld[layingFldVec.size()];
            layingFldVec.copyInto(flds);
            m_oDataProcessDef.setLayingDPFds(flds);
            m_oDataProcessDef.setDetailFlds(detailFldTable);

            setResult(com.ufsoft.report.dialog.UfoDialog.ID_OK);
            close();
        }
        //从可选项目列表增加所选项目到已选项目列表
        else if(e.getSource() == ivjJBtnToR){
            int[] selIndexs = m_ReList.getSelectedIndices();
            if(selIndexs == null || selIndexs.length == 0){
                return;
            }
            FieldMap item;
            for(int i = 0; i < selIndexs.length; i++){
                int index = selIndexs[i];
                if(i > 0){
                    index -= i;
                }
                item = m_oRSelItemModel.removeItem(index);
                m_oSeledItemModel.addItem(item);
            }
        }
        //从已选项目列表删除所选项目，将只增加到可选项目列表
        else if(e.getSource() == ivjJBtnToL){
            int[] selIndexs = m_SeledList.getSelectedIndices();
            if(selIndexs == null || selIndexs.length == 0){
                return;
            }
            FieldMap item;
            for(int i = 0; i < selIndexs.length; i++){
                int index = selIndexs[i];
                if(i > 0){
                    index -= i;
                }
                item = m_oSeledItemModel.removeItem(index);
                m_oRSelItemModel.addItem(item);
            }
        }
        //从可选项目列表增加一组项目到已选项目列表
        else if(e.getSource() == ivjJBtnSelsToR){
            FieldMap item;
            while(m_oRSelItemModel.getSize() > 0){
                item = m_oRSelItemModel.removeItem(0);
                m_oSeledItemModel.addItem(item);
            }
        }
        //从已选项目列表删除一组项目，将只增加到可选项目列表
        else if(e.getSource() == ivjJBtnSelsToL){
            FieldMap item;
            while(m_oSeledItemModel.getSize() > 0){
                item = m_oSeledItemModel.removeItem(0);
                m_oRSelItemModel.addItem(item);
            }
        }
        //在已选项目列表中，将选中的项目的位置上移一位
        else if(e.getSource() == ivjJBtnUp){
            int selIndex = m_SeledList.getSelectedIndex();
            if(selIndex < 0){
                return;
            }
            m_oSeledItemModel.putItemUp(selIndex);
            if(selIndex > 0){
                m_SeledList.setSelectedIndex(selIndex - 1);
            }
        }
        //在已选项目列表中，将选中的项目的位置下移一位
        else if(e.getSource() == ivjJBtnDown){
            int selIndex = m_SeledList.getSelectedIndex();
            if(selIndex < 0){
                return;
            }
            m_oSeledItemModel.putItemDown(selIndex);
            if(selIndex < m_oSeledItemModel.getSize() - 1){
                m_SeledList.setSelectedIndex(selIndex + 1);
            }

        }
        //在已选项目列表中，将选中的项目的移至最顶
        else if(e.getSource() == ivjJBtnToTop){
            int selIndex = m_SeledList.getSelectedIndex();
            if(selIndex < 0){
                return;
            }
            m_oSeledItemModel.moveItemToTop(selIndex);
            m_SeledList.setSelectedIndex(0);
        }
        //在已选项目列表中，将选中的项目的位置移至最末
        else if(e.getSource() == ivjJBtnToBottom){
            int selIndex = m_SeledList.getSelectedIndex();
            //仅有一个项目时，返回
            if(selIndex < 0){
                return;
            }
            m_oSeledItemModel.moveItemToBottom(selIndex);
            m_SeledList.setSelectedIndex(m_oSeledItemModel.getSize() - 1);
        }
        //对选中项目设置是否标题区
        else if(e.getSource() == ivjJCheckBTitle){
            int selIndex = m_SeledList.getSelectedIndex();
            if(selIndex < 0){
                return;
            }
            if(ivjJCheckBTitle.isSelected()){
                GroupLayingFld item = (GroupLayingFld)m_oSeledItemModel.getObjectAt(selIndex);
                item.setTitle(true);
                item.setCodeRule(null);
                ivjJTFRole.setEnabled(false);
                ivjJBtnRole.setEnabled(false);//added by liulp,2005-06-15
                ivjJCheckDoLastLayer.setEnabled(false);//added by liulp,2005-06-15
            } else{
                GroupLayingFld item = (GroupLayingFld)m_oSeledItemModel.getObjectAt(selIndex);
                item.setTitle(false);
                if(checkRuleDataType(item.getMapName())){
                    ivjJTFRole.setEnabled(true);
                    ivjJBtnRole.setEnabled(true);//added by liulp,2005-06-15
                    ivjJCheckDoLastLayer.setEnabled(true);//added by liulp,2005-06-15
                    ivjJTFRole.setText("");
                    ivjJCheckDoLastLayer.setSelected(false);//added by liulp,2005-06-15
                }
                //String role = ivjJTFRole.getText().trim();
                //if (checkRuleDataType(item.getMapName())) {
                //if (checkRole(role)) {
                //item.setCodeRule(role);
                //} else {
                //item.setCodeRule("");
                //}
                //
                //} else {
                //ivjJTFRole.setEnabled(false);
                //}
            }
        }

        //对选中项目设置编码规则后，“规则确认”
        else if(e.getSource() == ivjJBtnRole){
            int selIndex = m_SeledList.getSelectedIndex();
            if(selIndex < 0){
                return;
            }
            String role = ivjJTFRole.getText().trim();
            if(checkRole(role)){
                GroupLayingFld item = (GroupLayingFld)m_oSeledItemModel.getObjectAt(selIndex);
                item.setCodeRule(role);
            	//需要加上末层不分组勾选的定义
                boolean bNotDoLastLayer = getJCheckDoLastLayer().isSelected();
                item.setDoLastLayer(!bNotDoLastLayer);
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
        hb.enableHelpKey(getContentPane(), "TM_Data_Process_Group", null);

    }

    /**
     * 校验编码规则
     * 创建日期：(2003-8-20 9:41:38)
     * @return boolean
     * @param role java.lang.String
     */
    private boolean checkRole(String role){
        if(role == null || role.length() == 0){
            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001288"),this);  //"请输入拆分编码规则！"
            return false;
        }
        StringTokenizer token = new StringTokenizer(role, GroupLayingDef.STR_DELIM);
        try{
            while(token.hasMoreTokens()){
                String subStr = token.nextToken();
                new Integer(subStr);
            }
        } catch(NumberFormatException ne){
            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001289"),this);  //"请输入正确的数字！"
            return false;
        }
        return true;
    }

    /**
     * 检验选中项目是否可以可以有“编码规则”。
     *  说明：只有字符类型的才行
     * 创建日期：(2003-11-18 18:25:03)
     * @author：刘良萍
     * @return boolean
     * @param strMapName java.lang.String
     */
    private boolean checkRuleDataType(String strMapName){
        boolean bRule = false;
        if(strMapName != null){
            Integer nType = (Integer)m_hashDataTypes.get(strMapName);
            if(nType != null && nType.intValue() == nc.vo.iufo.measure.MeasureVO.TYPE_CHAR){
                bRule = true;
            }
        }
        return bRule;
    }

    /**
     * 此处插入方法描述。
     *
     * 创建日期：(2003-8-12 17:05:57)
     * @author：刘良萍
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.GroupLayingDef
     */
    public GroupLayingDef getDataProcessDef(){
        return m_oDataProcessDef;
    }

    /**
     * 返回 JBtnCancel 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnCancel(){
        if(ivjJBtnCancel == null){
            try{
                ivjJBtnCancel = new nc.ui.pub.beans.UIButton();
                ivjJBtnCancel.setName("JBtnCancel");
                ivjJBtnCancel.setText("Cancel");
                ivjJBtnCancel.setBounds(425, 420, 75, 22);
                // user code begin {1}
                ivjJBtnCancel.addActionListener(this);
                //取消
                String strCancel = StringResource.getStringResource(StringResource.CANCEL);
                ivjJBtnCancel.setText(strCancel);
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
     * 返回 JBtnDown 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnDown(){
        if(ivjJBtnDown == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltodown.jpg");
                ivjJBtnDown = new nc.ui.pub.beans.UIButton(icon);
                ivjJBtnDown.setName("JBtnDown");
                ivjJBtnDown.setToolTipText("seltodown");
                ivjJBtnDown.setBounds(445, 154, icon.getIconWidth(), icon.getIconHeight());
                ivjJBtnDown.addActionListener(this);
                // user code begin {1}
                String strSelToDown = StringResource.getStringResource("miufo1001290");  //"下移"
                ivjJBtnDown.setToolTipText(strSelToDown);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnDown;
    }

    /**
     * 返回 JBtnOK 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnOK(){
        if(ivjJBtnOK == null){
            try{
                ivjJBtnOK = new nc.ui.pub.beans.UIButton();
                ivjJBtnOK.setName("JBtnOK");
                ivjJBtnOK.setText("OK");
                ivjJBtnOK.setBounds(285, 420, 75, 22);
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
     * 返回 JBtnOK 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnRole(){
        if(ivjJBtnRole == null){
            try{
                ivjJBtnRole = new nc.ui.pub.beans.UIButton();
                ivjJBtnRole.setName("JBtnRole");
                ivjJBtnRole.setText("RoleConfirm");
                ivjJBtnRole.setBounds(415, 20, 75, 22);
                // user code begin {1}
                String strRoleConfirm = StringResource.getStringResource("miufo1001291");  //"规则确认"
                ivjJBtnRole.setText(strRoleConfirm);
                ivjJBtnRole.addActionListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnRole;
    }

    /**
     * 返回 JBtnSelsToL 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnSelsToL(){
        if(ivjJBtnSelsToL == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/selstoleft.jpg");
                ivjJBtnSelsToL = new nc.ui.pub.beans.UIButton(icon);
                ivjJBtnSelsToL.setName("JBtnSelsToL");
                ivjJBtnSelsToL.setToolTipText("selstoleft");
                ivjJBtnSelsToL.setBounds(200, 254, icon.getIconWidth(), icon.getIconHeight());
                ivjJBtnSelsToL.addActionListener(this);
                // user code begin {1}
                String strSelsToLeft = StringResource.getStringResource("miufo1001292");  //"批量左移"
                ivjJBtnSelsToL.setToolTipText(strSelsToLeft);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnSelsToL;
    }

    /**
     * 返回 JBtnSelsToR 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnSelsToR(){
        if(ivjJBtnSelsToR == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/selstoright.jpg");
                ivjJBtnSelsToR = new nc.ui.pub.beans.UIButton(icon);
                ivjJBtnSelsToR.setName("JBtnSelsToR");
                ivjJBtnSelsToR.setToolTipText("selstoright");
                ivjJBtnSelsToR.setBounds(200, 204, icon.getIconWidth(), icon.getIconHeight());
                ivjJBtnSelsToR.addActionListener(this);
                // user code begin {1}
                String strSelsToRight = StringResource.getStringResource("miufo1001293");  //"批量右移"
                ivjJBtnSelsToR.setToolTipText(strSelsToRight);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnSelsToR;
    }

    /**
     * 返回 JBtnToBottom 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnToBottom(){
        if(ivjJBtnToBottom == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltobottom.jpg");
                ivjJBtnToBottom = new nc.ui.pub.beans.UIButton(icon);
                ivjJBtnToBottom.setName("JBtnToBottom");
                ivjJBtnToBottom.setToolTipText("seltobottom");
                ivjJBtnToBottom.setBounds(445, 254, icon.getIconWidth(), icon.getIconHeight());
                ivjJBtnToBottom.addActionListener(this);
                // user code begin {1}
                String strSelToBottom = StringResource.getStringResource("miufo1001294");  //"移至列尾"
                ivjJBtnToBottom.setToolTipText(strSelToBottom);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnToBottom;
    }

    /**
     * 返回 JBtnToL 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnToL(){
        if(ivjJBtnToL == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoleft.jpg");
                ivjJBtnToL = new nc.ui.pub.beans.UIButton(icon);
                ivjJBtnToL.setName("JBtnToL");
                ivjJBtnToL.setToolTipText("seltoleft");
                ivjJBtnToL.setBounds(200, 154, icon.getIconWidth(), icon.getIconHeight());
                ivjJBtnToL.addActionListener(this);
                // user code begin {1}
                String strSelToLeft = StringResource.getStringResource("miufo1001295");  //"左移"
                ivjJBtnToL.setToolTipText(strSelToLeft);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnToL;
    }

    /**
     * 返回 JBtnToR 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnToR(){
        if(ivjJBtnToR == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoright.jpg");
                ivjJBtnToR = new nc.ui.pub.beans.UIButton(icon);
                ivjJBtnToR.setName("JBtnToR");
                ivjJBtnToR.setToolTipText("seltoright");
                ivjJBtnToR.setBounds(200, 104, icon.getIconWidth(), icon.getIconHeight());
                ivjJBtnToR.addActionListener(this);
                // user code begin {1}
                String strSelToRight = StringResource.getStringResource("miufo1001296");  //"右移"
                ivjJBtnToR.setToolTipText(strSelToRight);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnToR;
    }

    /**
     * 返回 JBtnToTop 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnToTop(){
        if(ivjJBtnToTop == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltotop.jpg");
                ivjJBtnToTop = new nc.ui.pub.beans.UIButton(icon);
                ivjJBtnToTop.setName("JBtnToTop");
                ivjJBtnToTop.setToolTipText("seltotop");
                ivjJBtnToTop.setBounds(445, 204, icon.getIconWidth(), icon.getIconHeight());
                ivjJBtnToTop.addActionListener(this);
                // user code begin {1}
                String strSelToTop = StringResource.getStringResource("miufo1001297");  //"移至列头"
                ivjJBtnToTop.setToolTipText(strSelToTop);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnToTop;
    }

    /**
     * 返回 JBtnUp 特性值。
     * @return JButton
     */
    /* 警告：此方法将重新生成。 */
    private JButton getJBtnUp(){
        if(ivjJBtnUp == null){
            try{
                ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoup.jpg");
                ivjJBtnUp = new nc.ui.pub.beans.UIButton(icon);
                ivjJBtnUp.setName("JBtnUp");
                ivjJBtnUp.setToolTipText("seltoup");
                ivjJBtnUp.setBounds(445, 104, icon.getIconWidth(), icon.getIconHeight());
                ivjJBtnUp.addActionListener(this);
                // user code begin {1}
                String strSelToUp = StringResource.getStringResource("miufo1001298");  //"上移"
                ivjJBtnUp.setToolTipText(strSelToUp);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnUp;
    }

    /**
     * 返回 JCheckBTitle 特性值。
     * @return JCheckBox
     */
    /* 警告：此方法将重新生成。 */
    private JCheckBox getJCheckBTitle(){
        if(ivjJCheckBTitle == null){
            try{
                ivjJCheckBTitle = new UICheckBox();
                ivjJCheckBTitle.setName("JCheckBTitle");
                ivjJCheckBTitle.setText("TitleArea");
                ivjJCheckBTitle.setBounds(5, 20, 80, 22);
                ivjJCheckBTitle.addActionListener(this);
                // user code begin {1}
                String strTitleArea = StringResource.getStringResource("miufo1001299");  //"标题区"
                ivjJCheckBTitle.setText(strTitleArea);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJCheckBTitle;
    }
    /**
	 * @i18n miufodp00001=末层不分组
	 */
    private JCheckBox getJCheckDoLastLayer(){
        if(ivjJCheckDoLastLayer == null){
            try{
            	ivjJCheckDoLastLayer = new UICheckBox();
            	ivjJCheckDoLastLayer.setName("JCheckBTitle");
            	ivjJCheckDoLastLayer.setText("TitleArea");
            	ivjJCheckDoLastLayer.setBounds(300, 20, 95, 22);
            	ivjJCheckDoLastLayer.addActionListener(this);
                // user code begin {1}
                String strTitleArea = StringResource.getStringResource("miufo1001299");  //末层不分组
                strTitleArea = StringResource.getStringResource("miufodp00001");
                ivjJCheckDoLastLayer.setText(strTitleArea);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJCheckDoLastLayer;
    }
    /**
     * 返回 JLCodeRole 特性值。
     * @return JLabel
     */
    /* 警告：此方法将重新生成。 */
    private JLabel getJLCodeRole(){
        if(ivjJLCodeRole == null){
            try{
                ivjJLCodeRole = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLCodeRole.setName("JLCodeRole");
                ivjJLCodeRole.setText("CodeRole:");
                ivjJLCodeRole.setBounds(90, 20, 106, 22);
                ivjJLCodeRole.setForeground(new java.awt.Color(0, 0, 0));
                // user code begin {1}
                String strCodeRule = StringResource.getStringResource("miufo1001300");  //"编码拆分规则:"
                ivjJLCodeRole.setText(strCodeRule);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLCodeRole;
    }

    /**
     * 返回 JLRSelItem 特性值。
     * @return JLabel
     */
    /* 警告：此方法将重新生成。 */
    private JLabel getJLRSelItem(){
        if(ivjJLRSelItem == null){
            try{
                ivjJLRSelItem = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLRSelItem.setName("JLRSelItem");
 //               ivjJLRSelItem.setFont(new java.awt.Font("dialog", 1, 12));
                ivjJLRSelItem.setText(StringResource.getStringResource("miufo1001301"));  //"ToBeSelItem："
                ivjJLRSelItem.setBounds(15, 24, 99, 14);
                ivjJLRSelItem.setForeground(java.awt.SystemColor.controlText);
                // user code begin {1}
                String strToBeSelItem = StringResource.getStringResource("miufo1001302");  //"待选项目："
                ivjJLRSelItem.setText(strToBeSelItem);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLRSelItem;
    }

    /**
     * 返回 JLSeledItem 特性值。
     * @return JLabel
     */
    /* 警告：此方法将重新生成。 */
    private JLabel getJLSeledItem(){
        if(ivjJLSeledItem == null){
            try{
                ivjJLSeledItem = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLSeledItem.setName("JLSeledItem");
                ivjJLSeledItem.setText("SeledItem");
                ivjJLSeledItem.setBounds(350, 24, 99, 14);
                ivjJLSeledItem.setForeground(java.awt.Color.black);
                // user code begin {1}
                String strSeledItem = StringResource.getStringResource("miufo1001303");  //"已选项目："
                ivjJLSeledItem.setText(strSeledItem);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLSeledItem;
    }

    /**
     * 返回 JPanelGroupSel 特性值。
     * @return JPanel
     */
    /* 警告：此方法将重新生成。 */
    private JPanel getJPanelGroupSel(){
        if(ivjJPanelGroupSel == null){
            try{
                ivjJPanelGroupSel = new UIPanel();
                ivjJPanelGroupSel.setName("JPanelGroupSel");
                ivjJPanelGroupSel.setLayout(null);
                ivjJPanelGroupSel.setBounds(15, 13, 512, 340);
                getJPanelGroupSel().add(getJLRSelItem(), getJLRSelItem().getName());
                getJPanelGroupSel().add(getJScrolPaneItem(), getJScrolPaneItem().getName());
                getJPanelGroupSel().add(getJBtnToR(), getJBtnToR().getName());
                getJPanelGroupSel().add(getJBtnToL(), getJBtnToL().getName());
                getJPanelGroupSel().add(getJBtnSelsToR(), getJBtnSelsToR().getName());
                getJPanelGroupSel().add(getJBtnSelsToL(), getJBtnSelsToL().getName());
                getJPanelGroupSel().add(getJLSeledItem(), getJLSeledItem().getName());
                getJPanelGroupSel().add(getJScrolPaneSeledItem(), getJScrolPaneSeledItem().getName());
                getJPanelGroupSel().add(getJBtnUp(), getJBtnUp().getName());
                getJPanelGroupSel().add(getJBtnToTop(), getJBtnToTop().getName());
                getJPanelGroupSel().add(getJBtnDown(), getJBtnDown().getName());
                getJPanelGroupSel().add(getJBtnToBottom(), getJBtnToBottom().getName());

                Border etched = BorderFactory.createEtchedBorder();
                Border title = BorderFactory.createTitledBorder(etched, "Item Selection");
                getJPanelGroupSel().setBorder(title);
                // user code begin {1}
                String strItemSelection = StringResource.getStringResource("miufo1001272");  //"项目选择"
                ((javax.swing.border.TitledBorder)title).setTitle(strItemSelection);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanelGroupSel;
    }

    /**
     * 返回 JPanelProp 特性值。
     * @return JPanel
     */
    /* 警告：此方法将重新生成。 */
    private JPanel getJPanelProp(){
        if(ivjJPanelProp == null){
            try{
                ivjJPanelProp = new UIPanel();
                ivjJPanelProp.setName("JPanelProp");
                ivjJPanelProp.setLayout(null);
                ivjJPanelProp.setBounds(15, 360, 512, 50);
                getJPanelProp().add(getJCheckBTitle(), getJCheckBTitle().getName());
                getJPanelProp().add(getJLCodeRole(), getJLCodeRole().getName());
                getJPanelProp().add(getJTFRole(), getJTFRole().getName());
//              added by liulp,2005-06-15 begin
                getJPanelProp().add(getJCheckDoLastLayer(), getJCheckDoLastLayer().getName());
                //added by liulp,2005-06-15 end
                getJPanelProp().add(getJBtnRole(), getJBtnRole().getName());

                Border etched = BorderFactory.createEtchedBorder();
                Border title = BorderFactory.createTitledBorder(etched, "Item Prop Edit");
                getJPanelProp().setBorder(title);
                // user code begin {1}
                String strItemPropEdit = StringResource.getStringResource("miufo1001273");  //"项目属性编辑"
                ((javax.swing.border.TitledBorder)title).setTitle(strItemPropEdit);
//            m_SeledList.setSelectedIndex(0);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanelProp;
    }

    /**
     * 返回 JScrollPane1 特性值。
     * @return JScrollPane
     */
    /* 警告：此方法将重新生成。 */
    private JScrollPane getJScrolPaneItem(){
        if(ivjJScrolPaneItem == null){
            try{
                m_ReList = new UIList(m_oRSelItemModel);
                m_ReList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                ivjJScrolPaneItem = new UIScrollPane(m_ReList);
                ivjJScrolPaneItem.setAutoscrolls(true);
                ivjJScrolPaneItem.setName("JScrolPaneItem");
                ivjJScrolPaneItem.setBounds(15, 44, 150, 290);
                // user code begin {1}
                m_ReList.setSelectedIndex(0);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJScrolPaneItem;
    }

    /**
     * 返回 JScrollPane2 特性值。
     * @return JScrollPane
     */
    /* 警告：此方法将重新生成。 */
    private JScrollPane getJScrolPaneSeledItem(){
        if(ivjJScrolPaneSeledItem == null){
            try{
                m_SeledList = new UIList(m_oSeledItemModel);
                m_SeledList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                ivjJScrolPaneSeledItem = new UIScrollPane(m_SeledList);
                ivjJScrolPaneSeledItem.setAutoscrolls(true);
                m_SeledList.addListSelectionListener(this);
                ivjJScrolPaneSeledItem.setName("JScrolPaneSeledItem");
                ivjJScrolPaneSeledItem.setBounds(270, 44, 150, 290);
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJScrolPaneSeledItem;
    }

    /**
     * 返回 JTFRole 特性值。
     * @return JTextField
     */
    /* 警告：此方法将重新生成。 */
    private JTextField getJTFRole(){
        if(ivjJTFRole == null){
            try{
                ivjJTFRole = new UITextField();
                ivjJTFRole.setName("JTFRole");
                ivjJTFRole.setBounds(190, 20, 109, 22);
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJTFRole;
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
                getUfoDialogContentPane().add(getJPanelGroupSel(), getJPanelGroupSel().getName());
                getUfoDialogContentPane().add(getJPanelProp(), getJPanelProp().getName());
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
            setName("GroupLayingDefDlg");
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setSize(550, 500);
            setResizable(false);
            setContentPane(getUfoDialogContentPane());
        } catch(java.lang.Throwable ivjExc){
            handleException(ivjExc);
        }
        // user code begin {2}
        GroupLayingFld item = null;
        if(m_oSeledItemModel.getSize() >0){
        	item = (GroupLayingFld)m_oSeledItemModel.getObjectAt(0);
        }
        setPanelPropValues(item);
        
        addHelp();
        // user code end
    }

    /**
     * 初始化可选择项目列表和已选择项目列表
     * 可选择列表为全部项目除去已选择列表中已选择的项目后的剩余项目
     * fullItem ,全部项目，Vector中为DataProcessFld对象
     * 创建日期：(2003-8-19 16:43:09)
     */
    public void initList(Vector<DataProcessFld> fullItem){

        Vector<GroupLayingFld> seledVec = new Vector<GroupLayingFld>();
        Hashtable<String, GroupLayingFld> seledNameHash = new Hashtable<String, GroupLayingFld>();
        GroupLayingFld[] groupFlds = m_oDataProcessDef.getGroupDPFlds();
        GroupLayingFld[] layingFlds = m_oDataProcessDef.getLayingDPFds();
        if(groupFlds == null){
        	groupFlds = new GroupLayingFld[0];
        }
        if(layingFlds == null){
        	layingFlds = new GroupLayingFld[0];
        }
        int groupLen = groupFlds.length;
        int layingLen = layingFlds.length;
        if(groupLen + layingLen > 0){

            //此时相当于修改
            //构造已选择的项目Vector，构造Vector（GroupLayingFld.getMapType()+GroupLayingFld.getMapName()），

            for(int i = 0; i < groupLen; i++){
                seledVec.addElement(groupFlds[i]);
                seledNameHash.put(groupFlds[i].getMapName(), groupFlds[i]);
            }
            for(int i = 0; i < layingLen; i++){
                seledVec.addElement(layingFlds[i]);
                seledNameHash.put(layingFlds[i].getMapName(), layingFlds[i]);
            }
            //构造已选项目列表
            m_oSeledItemModel = new ItemListModel(seledVec);

        } else{
            //此时相当于新建
            //构造已选项目列表
            m_oSeledItemModel = new ItemListModel();
        }

        //构建可选择项目列表，此时需要通过循环比较过滤已选择的项目
        //过滤
        GroupLayingFld groupLayingFld = null;
        DataProcessFld item = null;
        String strKey = null;
        Vector<GroupLayingFld> vecRSelectItem = new Vector<GroupLayingFld>();
        for(int i = 0; i < fullItem.size(); i++){
            item = (DataProcessFld)fullItem.get(i);
            strKey = item.getMapName();
            if(!seledNameHash.containsKey(strKey)){
                //可选项目的格式信息就是动态区域的原始定义位置信息
                groupLayingFld = new GroupLayingFld(item);
                vecRSelectItem.add(groupLayingFld);
            } else{
                //将分组、分层字段的位置信息回设为动态区域的原始定义位置信息
                //modified by liulp
                groupLayingFld = (GroupLayingFld)seledNameHash.get(strKey);
                groupLayingFld.setFormatRelatCell(item.getFormatRelatCell());
                groupLayingFld.setDisplayRelatCell(item.getDisplayRelatCell());
            }
        }
        //构造可选项目列表
        m_oRSelItemModel = new ItemListModel(vecRSelectItem);
    }

    /**
     * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args){
        //try {
        //Vector a = new Vector();
        //for (int i = 0; i < 20; i++) {
        //GroupLayingFld item = new GroupLayingFld();
        //item.setMapName(
        //"new_"
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i);
        //item.setName(
        //"new-Name_"
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i
        //+ i);
        //a.addElement(item);
        //}
        //GroupLayingDefDlg aGroupLayingDefDlg;
        //aGroupLayingDefDlg = new GroupLayingDefDlg(a, null);
        //aGroupLayingDefDlg.setModal(true);
        //aGroupLayingDefDlg.addWindowListener(new java.awt.event.WindowAdapter() {
        //public void windowClosing(java.awt.event.WindowEvent e) {
        //System.exit(0);
        //};
        //});
        //aGroupLayingDefDlg.show();
        //java.awt.Insets insets = aGroupLayingDefDlg.getInsets();
        //aGroupLayingDefDlg.setSize(
        //aGroupLayingDefDlg.getWidth() + insets.left + insets.right,
        //aGroupLayingDefDlg.getHeight() + insets.top + insets.bottom);
        //aGroupLayingDefDlg.setVisible(true);
        //} catch (Throwable exception) {
        //System.err.println(
        //"com.ufsoft.iuforeport.reporttool.pub.UfoDialog 的 main() 中发生异常");
        //exception.printStackTrace(System.out);
        //}
    }

    /**
     * 此处插入方法描述。
     *
     * 创建日期：(2003-8-12 17:05:57)
     * @author：刘良萍
     * @param newDataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.GroupLayingDef
     */
    public void setDataProcessDef(GroupLayingDef newDataProcessDef){
        m_oDataProcessDef = newDataProcessDef;
    }

    /**
     * 设置分组分层数据处理字段的属性面板值。
     *
     * 创建日期：(2003-10-15 15:44:59)
     * @param item com.ufsoft.iuforeport.reporttool.process.basedef.GroupLayingFld
     */
    private void setPanelPropValues(GroupLayingFld item){
        if(item == null){
            return;
        }

        if(item.isTitle()){
            ivjJCheckBTitle.setSelected(true);
            ivjJTFRole.setEnabled(false);
        } else{
            ivjJCheckBTitle.setSelected(false);
            if(checkRuleDataType(item.getMapName())){
                String content = item.getCodeRule();
                if(content == null){
                    content = "";
                }
                ivjJTFRole.setText(content);
                ivjJCheckDoLastLayer.setSelected(!item.isDoLastLayer());//added by liulp,2005-06-15,末层分组不显示
                ivjJTFRole.setEnabled(true);
            } else{
                ivjJTFRole.setText("");
                ivjJTFRole.setEnabled(false);
            }
        }
    }

//列表选择后的响应处理
    public void valueChanged(ListSelectionEvent event){
        if(event.getSource() == m_SeledList){
            int selInedx = m_SeledList.getSelectedIndex();
            if(selInedx >= 0){
                GroupLayingFld item = (GroupLayingFld)m_oSeledItemModel.getObjectAt(selInedx);
                setPanelPropValues(item);
            }

        }
    }
} 