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
 �����л���ѡ��Ŀʱ,ȡ�����������Զ���ò�ֹ������ݵĴ���
 @end
 @update
  2003-11-19 11:19 liulp
   �ֲ�����ݴ����ֶ�ֻ�����ַ��͵��޸�
 @end
 @update 2003-10-31 09:58 liulp
  ���Ӱ�������
 @end
 @update
   2003-10-28 19:35 liulp
   �ֶ�ӳ�����FieldMap���Name���ԣ�MapName�������Ϊ(ӳ������+ָ��/�ؼ��ֵ�PK)
 @end
 @update
 2003-10-23 20:48 liulp
  ����߼����ƣ�����ѡ��һ��������Ŀ
 @end
 * ���÷��飨�ֲ㣩���ݴ�����ĶԻ���
 *
 * �������ڣ�(2003-8-12 17:01:24)
 * @author������Ƽ
 */
public class GroupLayingDefDlg extends com.ufsoft.report.dialog.UfoDialog implements ActionListener,
    ListSelectionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = -170593848211928270L;
	/**
     *	Dialog��Swing���
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
     *	�ڲ�����
     */
    //���ݴ��������
    private GroupLayingDef m_oDataProcessDef = null;
    
//    //3.5 old property:�Ƿ������˫�����
//    private int m_nClickCount = 0;
//    //3.5 old property:��˫�����ʱ����(����)
//    private long m_nDBClickDelay = 500;
    
    //��ѡ��Ŀ��ListModel
    private ItemListModel m_oRSelItemModel = null;
    //��ѡ��Ŀ��ListModel
    private ItemListModel m_oSeledItemModel = null;
    //��̬�������������ݴ����ֶζ�Ӧ��ָ�꣩����������hash(mapName,Integer(measType))
    private Hashtable m_hashDataTypes = null;
    /**
     * GroupLayingDefDlg ������ע�⡣
     */
    public GroupLayingDefDlg(){
        super();
        initList(null);
        initialize();
    }

    /**
     * GroupLayingDefDlg ������ע�⡣
     * @param parent java.awt.Container
     */
    public GroupLayingDefDlg(java.awt.Container parent){
        super(parent);
        initList(null);
        initialize();
    }

    /**
     * GroupLayingDefDlg ������ע�⡣
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld,ȫ������Ŀ, Vector��ΪDataProcessFld����
     * @param hashDataTypes java.util.Hashtable - ��̬�������������ݴ����ֶζ�Ӧ��ָ�꣩����������hash(mapName,Integer(measType))
     * @param seledDef����ѡ�����Ŀ����ʵ�����
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
        //���÷�������ݴ�����
        else if(e.getSource() == getJBtnOK()){
            //���÷�������ݴ�����
            //����ѡ��Ŀ�б���ȡ�����ݣ�����ѡ������Ͳ�ͬ�����з��ࣨ���⣬�ֲ㡣����
            //����Vector
            Vector<GroupLayingFld> groupFldVec = new Vector<GroupLayingFld>();
            //�ֲ�Vector
            Vector<GroupLayingFld> layingFldVec = new Vector<GroupLayingFld>();
            //��ϸ����Hashtable
            Hashtable<String, DataProcessFld> detailFldTable = new Hashtable<String, DataProcessFld>();
            Vector ItemVec = m_oSeledItemModel.getAll();
            if(ItemVec == null || ItemVec.size() <= 0){
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001285"),this);  //"��������ѡ��һ��������Ŀ��"
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
						StringResource.getStringResource("miufo1001287"),this);  //"����ѡ��Ŀ��������һ���Ǳ�������Ŀ����Ϊϸ����չ��"
                    return;
                }
            }
            GroupLayingFld item;
            //����ѡ��Ŀ���з��ദ��
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
                    //���Ǳ���ķ��顢�б������ķֲ㣬����Ҫ����ϸ��������ʾ�б���
                    detailFldTable.put(item.getMapName(), item);
                }
            }
            //��ʣ���ѡ��Ŀ���д���ȫ�����ӵ���ϸ����Hashtable
            ItemVec = m_oRSelItemModel.getAll();
            for(int j = 0; j < ItemVec.size(); j++){
                item = (GroupLayingFld)ItemVec.get(j);
                detailFldTable.put(item.getMapName(), item);
            }
            //��m_oDataProcessDef���и�ֵ
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
        //�ӿ�ѡ��Ŀ�б�������ѡ��Ŀ����ѡ��Ŀ�б�
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
        //����ѡ��Ŀ�б�ɾ����ѡ��Ŀ����ֻ���ӵ���ѡ��Ŀ�б�
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
        //�ӿ�ѡ��Ŀ�б�����һ����Ŀ����ѡ��Ŀ�б�
        else if(e.getSource() == ivjJBtnSelsToR){
            FieldMap item;
            while(m_oRSelItemModel.getSize() > 0){
                item = m_oRSelItemModel.removeItem(0);
                m_oSeledItemModel.addItem(item);
            }
        }
        //����ѡ��Ŀ�б�ɾ��һ����Ŀ����ֻ���ӵ���ѡ��Ŀ�б�
        else if(e.getSource() == ivjJBtnSelsToL){
            FieldMap item;
            while(m_oSeledItemModel.getSize() > 0){
                item = m_oSeledItemModel.removeItem(0);
                m_oRSelItemModel.addItem(item);
            }
        }
        //����ѡ��Ŀ�б��У���ѡ�е���Ŀ��λ������һλ
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
        //����ѡ��Ŀ�б��У���ѡ�е���Ŀ��λ������һλ
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
        //����ѡ��Ŀ�б��У���ѡ�е���Ŀ�������
        else if(e.getSource() == ivjJBtnToTop){
            int selIndex = m_SeledList.getSelectedIndex();
            if(selIndex < 0){
                return;
            }
            m_oSeledItemModel.moveItemToTop(selIndex);
            m_SeledList.setSelectedIndex(0);
        }
        //����ѡ��Ŀ�б��У���ѡ�е���Ŀ��λ��������ĩ
        else if(e.getSource() == ivjJBtnToBottom){
            int selIndex = m_SeledList.getSelectedIndex();
            //����һ����Ŀʱ������
            if(selIndex < 0){
                return;
            }
            m_oSeledItemModel.moveItemToBottom(selIndex);
            m_SeledList.setSelectedIndex(m_oSeledItemModel.getSize() - 1);
        }
        //��ѡ����Ŀ�����Ƿ������
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

        //��ѡ����Ŀ���ñ������󣬡�����ȷ�ϡ�
        else if(e.getSource() == ivjJBtnRole){
            int selIndex = m_SeledList.getSelectedIndex();
            if(selIndex < 0){
                return;
            }
            String role = ivjJTFRole.getText().trim();
            if(checkRole(role)){
                GroupLayingFld item = (GroupLayingFld)m_oSeledItemModel.getObjectAt(selIndex);
                item.setCodeRule(role);
            	//��Ҫ����ĩ�㲻���鹴ѡ�Ķ���
                boolean bNotDoLastLayer = getJCheckDoLastLayer().isSelected();
                item.setDoLastLayer(!bNotDoLastLayer);
            }
        }

    }

    /**
     * ���Ӱ�����
     *
     * �������ڣ�(2003-10-31 09:56:54)
     * �����ߣ�����Ƽ
     */
    private void addHelp(){
        javax.help.HelpBroker hb = ResConst.getHelpBroker();
        if(hb == null){
            return;
        }
        hb.enableHelpKey(getContentPane(), "TM_Data_Process_Group", null);

    }

    /**
     * У��������
     * �������ڣ�(2003-8-20 9:41:38)
     * @return boolean
     * @param role java.lang.String
     */
    private boolean checkRole(String role){
        if(role == null || role.length() == 0){
            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001288"),this);  //"�������ֱ������"
            return false;
        }
        StringTokenizer token = new StringTokenizer(role, GroupLayingDef.STR_DELIM);
        try{
            while(token.hasMoreTokens()){
                String subStr = token.nextToken();
                new Integer(subStr);
            }
        } catch(NumberFormatException ne){
            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001289"),this);  //"��������ȷ�����֣�"
            return false;
        }
        return true;
    }

    /**
     * ����ѡ����Ŀ�Ƿ���Կ����С�������򡱡�
     *  ˵����ֻ���ַ����͵Ĳ���
     * �������ڣ�(2003-11-18 18:25:03)
     * @author������Ƽ
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
     * �˴����뷽��������
     *
     * �������ڣ�(2003-8-12 17:05:57)
     * @author������Ƽ
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.GroupLayingDef
     */
    public GroupLayingDef getDataProcessDef(){
        return m_oDataProcessDef;
    }

    /**
     * ���� JBtnCancel ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
    private JButton getJBtnCancel(){
        if(ivjJBtnCancel == null){
            try{
                ivjJBtnCancel = new nc.ui.pub.beans.UIButton();
                ivjJBtnCancel.setName("JBtnCancel");
                ivjJBtnCancel.setText("Cancel");
                ivjJBtnCancel.setBounds(425, 420, 75, 22);
                // user code begin {1}
                ivjJBtnCancel.addActionListener(this);
                //ȡ��
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
     * ���� JBtnDown ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToDown = StringResource.getStringResource("miufo1001290");  //"����"
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
     * ���� JBtnOK ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� JBtnOK ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
    private JButton getJBtnRole(){
        if(ivjJBtnRole == null){
            try{
                ivjJBtnRole = new nc.ui.pub.beans.UIButton();
                ivjJBtnRole.setName("JBtnRole");
                ivjJBtnRole.setText("RoleConfirm");
                ivjJBtnRole.setBounds(415, 20, 75, 22);
                // user code begin {1}
                String strRoleConfirm = StringResource.getStringResource("miufo1001291");  //"����ȷ��"
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
     * ���� JBtnSelsToL ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelsToLeft = StringResource.getStringResource("miufo1001292");  //"��������"
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
     * ���� JBtnSelsToR ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelsToRight = StringResource.getStringResource("miufo1001293");  //"��������"
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
     * ���� JBtnToBottom ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToBottom = StringResource.getStringResource("miufo1001294");  //"������β"
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
     * ���� JBtnToL ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToLeft = StringResource.getStringResource("miufo1001295");  //"����"
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
     * ���� JBtnToR ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToRight = StringResource.getStringResource("miufo1001296");  //"����"
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
     * ���� JBtnToTop ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToTop = StringResource.getStringResource("miufo1001297");  //"������ͷ"
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
     * ���� JBtnUp ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToUp = StringResource.getStringResource("miufo1001298");  //"����"
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
     * ���� JCheckBTitle ����ֵ��
     * @return JCheckBox
     */
    /* ���棺�˷������������ɡ� */
    private JCheckBox getJCheckBTitle(){
        if(ivjJCheckBTitle == null){
            try{
                ivjJCheckBTitle = new UICheckBox();
                ivjJCheckBTitle.setName("JCheckBTitle");
                ivjJCheckBTitle.setText("TitleArea");
                ivjJCheckBTitle.setBounds(5, 20, 80, 22);
                ivjJCheckBTitle.addActionListener(this);
                // user code begin {1}
                String strTitleArea = StringResource.getStringResource("miufo1001299");  //"������"
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
	 * @i18n miufodp00001=ĩ�㲻����
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
                String strTitleArea = StringResource.getStringResource("miufo1001299");  //ĩ�㲻����
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
     * ���� JLCodeRole ����ֵ��
     * @return JLabel
     */
    /* ���棺�˷������������ɡ� */
    private JLabel getJLCodeRole(){
        if(ivjJLCodeRole == null){
            try{
                ivjJLCodeRole = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLCodeRole.setName("JLCodeRole");
                ivjJLCodeRole.setText("CodeRole:");
                ivjJLCodeRole.setBounds(90, 20, 106, 22);
                ivjJLCodeRole.setForeground(new java.awt.Color(0, 0, 0));
                // user code begin {1}
                String strCodeRule = StringResource.getStringResource("miufo1001300");  //"�����ֹ���:"
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
     * ���� JLRSelItem ����ֵ��
     * @return JLabel
     */
    /* ���棺�˷������������ɡ� */
    private JLabel getJLRSelItem(){
        if(ivjJLRSelItem == null){
            try{
                ivjJLRSelItem = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLRSelItem.setName("JLRSelItem");
 //               ivjJLRSelItem.setFont(new java.awt.Font("dialog", 1, 12));
                ivjJLRSelItem.setText(StringResource.getStringResource("miufo1001301"));  //"ToBeSelItem��"
                ivjJLRSelItem.setBounds(15, 24, 99, 14);
                ivjJLRSelItem.setForeground(java.awt.SystemColor.controlText);
                // user code begin {1}
                String strToBeSelItem = StringResource.getStringResource("miufo1001302");  //"��ѡ��Ŀ��"
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
     * ���� JLSeledItem ����ֵ��
     * @return JLabel
     */
    /* ���棺�˷������������ɡ� */
    private JLabel getJLSeledItem(){
        if(ivjJLSeledItem == null){
            try{
                ivjJLSeledItem = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLSeledItem.setName("JLSeledItem");
                ivjJLSeledItem.setText("SeledItem");
                ivjJLSeledItem.setBounds(350, 24, 99, 14);
                ivjJLSeledItem.setForeground(java.awt.Color.black);
                // user code begin {1}
                String strSeledItem = StringResource.getStringResource("miufo1001303");  //"��ѡ��Ŀ��"
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
     * ���� JPanelGroupSel ����ֵ��
     * @return JPanel
     */
    /* ���棺�˷������������ɡ� */
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
                String strItemSelection = StringResource.getStringResource("miufo1001272");  //"��Ŀѡ��"
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
     * ���� JPanelProp ����ֵ��
     * @return JPanel
     */
    /* ���棺�˷������������ɡ� */
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
                String strItemPropEdit = StringResource.getStringResource("miufo1001273");  //"��Ŀ���Ա༭"
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
     * ���� JScrollPane1 ����ֵ��
     * @return JScrollPane
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� JScrollPane2 ����ֵ��
     * @return JScrollPane
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� JTFRole ����ֵ��
     * @return JTextField
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� UfoDialogContentPane ����ֵ��
     * @return JPanel
     */
    /* ���棺�˷������������ɡ� */
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
     * ÿ�������׳��쳣ʱ������
     * @param exception java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception){

        /* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
        // System.out.println("--------- δ��׽�����쳣 ---------");
        // exception.printStackTrace(System.out);
    }

    /**
     * ��ʼ���ࡣ
     */
    /* ���棺�˷������������ɡ� */
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
     * ��ʼ����ѡ����Ŀ�б����ѡ����Ŀ�б�
     * ��ѡ���б�Ϊȫ����Ŀ��ȥ��ѡ���б�����ѡ�����Ŀ���ʣ����Ŀ
     * fullItem ,ȫ����Ŀ��Vector��ΪDataProcessFld����
     * �������ڣ�(2003-8-19 16:43:09)
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

            //��ʱ�൱���޸�
            //������ѡ�����ĿVector������Vector��GroupLayingFld.getMapType()+GroupLayingFld.getMapName()����

            for(int i = 0; i < groupLen; i++){
                seledVec.addElement(groupFlds[i]);
                seledNameHash.put(groupFlds[i].getMapName(), groupFlds[i]);
            }
            for(int i = 0; i < layingLen; i++){
                seledVec.addElement(layingFlds[i]);
                seledNameHash.put(layingFlds[i].getMapName(), layingFlds[i]);
            }
            //������ѡ��Ŀ�б�
            m_oSeledItemModel = new ItemListModel(seledVec);

        } else{
            //��ʱ�൱���½�
            //������ѡ��Ŀ�б�
            m_oSeledItemModel = new ItemListModel();
        }

        //������ѡ����Ŀ�б���ʱ��Ҫͨ��ѭ���ȽϹ�����ѡ�����Ŀ
        //����
        GroupLayingFld groupLayingFld = null;
        DataProcessFld item = null;
        String strKey = null;
        Vector<GroupLayingFld> vecRSelectItem = new Vector<GroupLayingFld>();
        for(int i = 0; i < fullItem.size(); i++){
            item = (DataProcessFld)fullItem.get(i);
            strKey = item.getMapName();
            if(!seledNameHash.containsKey(strKey)){
                //��ѡ��Ŀ�ĸ�ʽ��Ϣ���Ƕ�̬�����ԭʼ����λ����Ϣ
                groupLayingFld = new GroupLayingFld(item);
                vecRSelectItem.add(groupLayingFld);
            } else{
                //�����顢�ֲ��ֶε�λ����Ϣ����Ϊ��̬�����ԭʼ����λ����Ϣ
                //modified by liulp
                groupLayingFld = (GroupLayingFld)seledNameHash.get(strKey);
                groupLayingFld.setFormatRelatCell(item.getFormatRelatCell());
                groupLayingFld.setDisplayRelatCell(item.getDisplayRelatCell());
            }
        }
        //�����ѡ��Ŀ�б�
        m_oRSelItemModel = new ItemListModel(vecRSelectItem);
    }

    /**
     * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
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
        //"com.ufsoft.iuforeport.reporttool.pub.UfoDialog �� main() �з����쳣");
        //exception.printStackTrace(System.out);
        //}
    }

    /**
     * �˴����뷽��������
     *
     * �������ڣ�(2003-8-12 17:05:57)
     * @author������Ƽ
     * @param newDataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.GroupLayingDef
     */
    public void setDataProcessDef(GroupLayingDef newDataProcessDef){
        m_oDataProcessDef = newDataProcessDef;
    }

    /**
     * ���÷���ֲ����ݴ����ֶε��������ֵ��
     *
     * �������ڣ�(2003-10-15 15:44:59)
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
                ivjJCheckDoLastLayer.setSelected(!item.isDoLastLayer());//added by liulp,2005-06-15,ĩ����鲻��ʾ
                ivjJTFRole.setEnabled(true);
            } else{
                ivjJTFRole.setText("");
                ivjJTFRole.setEnabled(false);
            }
        }
    }

//�б�ѡ������Ӧ����
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