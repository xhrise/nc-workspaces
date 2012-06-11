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
  ���Ӱ�������
 @end
 @update
   2003-10-28 19:35 liulp
    �ֶ�ӳ�����FieldMap���Name���ԣ�MapName�������Ϊ(ӳ������+ָ��/�ؼ��ֵ�PK)
 @end
 @update
  2003-10-23 13:07 liulp
  1)�޸ģ�������ֻ������һ�����������ƺ���ʾ��
  2003-10-23 20:34 liulp
  2)�б��⡢�б��⡢������Ŀ����ֱ�����ѡ��һ����Ŀ��
 @end
 * �˴���������������
 *
 * �������ڣ�(2003-8-13 11:57:54)
 * @author������Ƽ
 */
public class CrossTableDefDlg extends com.ufsoft.report.dialog.UfoDialog implements ActionListener, ListSelectionListener{
	private static final long serialVersionUID = 2318893075760385595L;
	//���ݴ��������
    private CrossTabDef m_oDataProcessDef = null;
    /**
     * �Ի������
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
    //���ܷ�ʽ
//    private JLabel ivjJLTotalType = null;
//    private JComboBox ivjj
    /**
     * �ڲ�����
     */
    //��ѡ��ĿList
    private JList m_oReItemList = null;
    //�б���List
    private JList m_oColTitleList = null;
    //�б���List
    private JList m_oRowTitleList = null;
    //����List
    private JList m_oTotalTitleList = null;
    //��ѡ��Ŀ��ListModel
    private ItemListModel m_oReItemModel = null;
    //����Ŀ��ListModel
    private ItemListModel m_oColTitleItemModel = null;
    //����Ŀ��ListModel
    private ItemListModel m_oRowTitleItemModel = null;
    //������Ŀ��ListModel
    private ItemListModel m_oTotalTitleItemModel = null;
    //���༭��Ŀ����ʱ�������С��С����������ǻ���༭�ģ���Ҫ��¼��ǰ���ĸ��б��ڱ༭
    private int m_currentList = 0;
    //��ǰ�༭�б��ʾ
    private final int COL_LIST = 0;
    private final int ROW_LIST = 1;
    private final int TOL_LIST = 2;
    //CrossTableFld���ͺ�����ʾ:˳��������ICubeConst�Ļ�������ֵ���
    private String[] m_oTypes = new String[]{
    		StringResource.getStringResource("miufopublic140"),   //"����"
    		StringResource.getStringResource("miufo1001255"),   //"����"
			StringResource.getStringResource("miufo1001256"),   //"���"
			StringResource.getStringResource("miufo1001257"),   //"��С"
			StringResource.getStringResource("miufo1001258")  //"ƽ��"
			};
    //��ʾ��ǰ�Ƿ��ڶ��б���в����������ڴ�ʱ�����������ã�����setProp()
    private boolean isListSeling = false;
    
    /**
     * CrossTableDefDlg ������ע�⡣
     */
    public CrossTableDefDlg(){
        super();
        initialize();
    }

    /**
     * �˴����뷽��������
     *
     * �������ڣ�(2003-8-22 14:25:40)
     * @author������Ƽ
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld java.util.Vector<CrossTableFld> - Ԫ��ΪCrossTableFld
     * @param seledDef com.ufsoft.iuforeport.reporttool.process.basedef.CrossTabDef
     */
    public CrossTableDefDlg(java.awt.Container parent, Vector<DataProcessFld> vecAllDynAreaDPFld, CrossTabDef seledDef){
        super(parent);

        this.m_oDataProcessDef = seledDef;

        initList(vecAllDynAreaDPFld);

        initialize();

    }

    /**
     * GroupLayingDefDlg ������ע�⡣
     * fullItem,ȫ������Ŀ, Vector��ΪCrossTableFld����
     * seledDef����ѡ�����Ŀ����ʵ�����
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
        //ȷ��
        else if(e.getSource() == getJBtnOK()){
            //���ý��������ݴ�����
            int iColSize = m_oColTitleItemModel.getSize();
            int iRowSize = m_oRowTitleItemModel.getSize();
            int iTotalSize = m_oTotalTitleItemModel.getSize();
            if(iColSize <= 0 || iRowSize <= 0 || iTotalSize <= 0){
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001259"),this);  //"�б��⡢�б��⡢������Ŀ����ֱ�����ѡ��һ����Ŀ��"
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
        //��ӵ��б���List��
        else if(e.getSource() == ivjJBToColTitle){
            int selIndex = m_oReItemList.getSelectedIndex();
            moveToSeledList(selIndex, m_oColTitleItemModel, COL_LIST);
        }
        //��ӵ��б���List��
        else if(e.getSource() == ivjJBToRowTitle){
            int selIndex = m_oReItemList.getSelectedIndex();
            moveToSeledList(selIndex, m_oRowTitleItemModel, ROW_LIST);
        }
        //��ӵ�����List��
        else if(e.getSource() == ivjJBToTotal){
            //Ŀǰ�㷨������ֻ�ܼ���һ����2003-10-23 11:13��
            if(m_oTotalTitleItemModel.getSize() <= 0){
                int selIndex = m_oReItemList.getSelectedIndex();
                moveToSeledList(selIndex, m_oTotalTitleItemModel, TOL_LIST);
                //������Ϊѡ��״̬���Ա�����ͳ�Ʒ�ʽ��ȱʡֵ
                m_oTotalTitleList.setSelectedIndex(0);
            } else{
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001260"),this);  //"���汾ֻ֧��һ�������"
            }
        }
        //���б���List��ɾ��
        else if(e.getSource() == ivjJBFromColTitle){

            int selIndex = m_oColTitleList.getSelectedIndex();
            moveToReList(selIndex, m_oColTitleItemModel);
        }
        //���б���List��ɾ��
        else if(e.getSource() == ivjJBFromRowTitle){

            int selIndex = m_oRowTitleList.getSelectedIndex();
            moveToReList(selIndex, m_oRowTitleItemModel);
        }
        //�ӻ���List��ɾ��
        else if(e.getSource() == ivjJBFromTotal){

            int selIndex = m_oTotalTitleList.getSelectedIndex();
            moveToReList(selIndex, m_oTotalTitleItemModel);
        }
        //�б�������
        else if(e.getSource() == ivjJBColTitleDown){

            moveDown(m_oColTitleItemModel, m_oColTitleList);
        }
        //�б�������
        else if(e.getSource() == ivjJBColTitleUp){

            moveUp(m_oColTitleItemModel, m_oColTitleList);
        }
        //�б�������
        else if(e.getSource() == ivjJBRowTitleDown){

            moveDown(m_oRowTitleItemModel, m_oRowTitleList);
        }
        //�б�������
        else if(e.getSource() == ivjJBRowTitleUp){

            moveUp(m_oRowTitleItemModel, m_oRowTitleList);
        }
        //��������
        else if(e.getSource() == ivjJBTotalDown){

            moveDown(m_oTotalTitleItemModel, m_oTotalTitleList);
        }
        //��������
        else if(e.getSource() == ivjJBTotalUp){

            moveUp(m_oTotalTitleItemModel, m_oTotalTitleList);
        }
        //��������
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
        //���ܷ�ʽѡ��
        else if(e.getSource() == getJCBTotal()){
            int iSelTotalItemIndex = m_oTotalTitleList.getSelectedIndex();
            if(iSelTotalItemIndex >= 0){
                int iSelTotalTypeIndex = getJCBTotal().getSelectedIndex();
                if(iSelTotalTypeIndex >= 0){
                    CrossTableFld selTotalItem = (CrossTableFld)m_oTotalTitleItemModel.getObjectAt(iSelTotalItemIndex);
                    selTotalItem.setType(iSelTotalTypeIndex);
                }
            } else{
                //��ʾ��������ѡ��һ�������
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001261"),this);  //"����ѡ��һ�������"
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
        hb.enableHelpKey(getContentPane(), "TM_Data_Process_Cross", null);

    }

    /**
     * �˴����뷽��������
     *
     * �������ڣ�(2003-8-13 15:08:01)
     * @author������Ƽ
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.CrossTabDef
     */
    public CrossTabDef getDataProcessDef(){
        return m_oDataProcessDef;
    }

    /**
     * ���� JBColTitleDown ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToDown = StringResource.getStringResource("miufo1001262");  //"������"
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
     * ���� JBColTitleUp ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToUp = StringResource.getStringResource("miufo1001263");  //"������"
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
     * ���� JBFromColTitle ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strToLeft = StringResource.getStringResource("miufo1001264");  //"��ȥ"
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
     * ���� JBFromRowTitle ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToLeft = StringResource.getStringResource("miufo1001264");  //"��ȥ"
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
     * ���� JBFromTotal ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToLeft = StringResource.getStringResource("miufo1001264");  //"��ȥ"
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
     * ���� JBRowTitleDown ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToDown = StringResource.getStringResource("miufo1001262");  //"������"
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
     * ���� JBRowTitleUp ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToUp = StringResource.getStringResource("miufo1001263");  //"������"
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
     * ���� JBtnCancel ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
    private JButton getJBtnCancel(){
        if(ivjJBtnCancel == null){
            try{
                ivjJBtnCancel = new UIButton();
                ivjJBtnCancel.setName("JBtnCancel");
                ivjJBtnCancel.setText("Cancel");
                ivjJBtnCancel.setBounds(485, 475, 85, 27);
                // user code begin {1}
                String strCancel = StringResource.getStringResource(StringResource.CANCEL);//"ȡ��";
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
     * ���� JBtnOK ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
    private JButton getJBtnOK(){
        if(ivjJBtnOK == null){
            try{
                ivjJBtnOK = new UIButton();
                ivjJBtnOK.setName("JBtnOK");
                ivjJBtnOK.setText("OK");
                ivjJBtnOK.setBounds(375, 475, 85, 27);
                // user code begin {1}
                String strOK = StringResource.getStringResource(StringResource.OK);//"ȷ��";
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
     * ���� JBToColTitle ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToRight = StringResource.getStringResource("miufo1000080");  //"����"
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
     * ���� JBToRowTitle ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToRight = StringResource.getStringResource("miufo1000080");  //"����"
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
     * ���� JBTotalDown ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToDown = StringResource.getStringResource("miufo1001262");  //"������"
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
     * ���� JBTotalUp ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToUp = StringResource.getStringResource("miufo1001263");  //"������"
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
     * ���� JBToTotal ����ֵ��
     * @return JButton
     */
    /* ���棺�˷������������ɡ� */
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
                String strSelToRight = StringResource.getStringResource("miufo1000080");  //"����"
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
     * ���� JCBTotal ����ֵ��
     * @return JComboBox
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� JCheckBoxCount ����ֵ��
     * @return JCheckBox
     */
    /* ���棺�˷������������ɡ� */
    private JCheckBox getJCheckBoxCount(){
        if(ivjJCheckBoxCount == null){
            try{
                ivjJCheckBoxCount = new UICheckBox();
                ivjJCheckBoxCount.setName("JCheckBoxCount");
                ivjJCheckBoxCount.setText("SummaryItem");
                ivjJCheckBoxCount.setBounds(20, 15, 97, 22);
                ivjJCheckBoxCount.addActionListener(this);
                // user code begin {1}
                String strSummaryItem = StringResource.getStringResource("miufo1001265");  //"С����"
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
     * ���� JLCol ����ֵ��
     * @return JLabel
     */
    /* ���棺�˷������������ɡ� */
    private JLabel getJLCol(){
        if(ivjJLCol == null){
            try{
                ivjJLCol = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLCol.setName("JLCol");
                ivjJLCol.setText("Col:");
                ivjJLCol.setBounds(223, 15, 60, 14);
                // user code begin {1}
                String strColumn = StringResource.getStringResource("miufo1001266");  //"�У�"
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
     * ���� JLCol ����ֵ��
     * @return JLabel
     */
    /* ���棺�˷������������ɡ� */
    private JLabel getJLTotalType(){
        if(ivjJLTotalType == null){
            try{
                ivjJLTotalType = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLTotalType.setName("JLTotalType");
                ivjJLTotalType.setText("Way:");
                ivjJLTotalType.setBounds(223, 348, 42, 25);
                // user code begin {1}
                String strWay = StringResource.getStringResource("miufo1001267");  //"��ʽ��"
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
     * ���� JLReItem ����ֵ��
     * @return JLabel
     */
    /* ���棺�˷������������ɡ� */
    private JLabel getJLReItem(){
        if(ivjJLReItem == null){
            try{
                ivjJLReItem = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLReItem.setName("JLReItem");
                ivjJLReItem.setText("ReadyItem:");
                ivjJLReItem.setBounds(9, 14, 113, 14);
                // user code begin {1}
                String strReadyItem = StringResource.getStringResource("miufo1001268");  //"��ѡ��Ŀ��"
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
     * ���� JLRowTitle ����ֵ��
     * @return JLabel
     */
    /* ���棺�˷������������ɡ� */
    private JLabel getJLRowTitle(){
        if(ivjJLRowTitle == null){
            try{
                ivjJLRowTitle = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLRowTitle.setName("JLRowTitle");
                ivjJLRowTitle.setText("Row:");
                ivjJLRowTitle.setBounds(51, 190, 45, 14);
                // user code begin {1}
                String strRow = StringResource.getStringResource("miufo1001269");  //"�У�"
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
     * ���� JLTotal ����ֵ��
     * @return JLabel
     */
    /* ���棺�˷������������ɡ� */
    private JLabel getJLTotal(){
        if(ivjJLTotal == null){
            try{
                ivjJLTotal = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLTotal.setName("JLTotal");
                ivjJLTotal.setText("TotalItem:");
                ivjJLTotal.setBounds(223, 190, 73, 14);
                // user code begin {1}
                String strTotalItem =StringResource.getStringResource("miufo1001270");  //"ͳ���"
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
     * ���� JPanelRe ����ֵ��
     * @return JPanel
     */
    /* ���棺�˷������������ɡ� */
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
                String strTitle = StringResource.getStringResource("miufo1001271");  //"Դ��Ŀ"
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
     * ���� JPanelSet ����ֵ��
     * @return JPanel
     */
    /* ���棺�˷������������ɡ� */
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
                String strTitle = StringResource.getStringResource("miufo1001272");  //"��Ŀѡ��"
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
     * ���� JPTitleItemProp ����ֵ��
     * @return JPanel
     */
    /* ���棺�˷������������ɡ� */
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
                String strTitle = StringResource.getStringResource("miufo1001273");  //"��Ŀ���Ա༭"
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
     * ���� JRBBefor ����ֵ��
     * @return JRadioButton
     */
    /* ���棺�˷������������ɡ� */
    private JRadioButton getJRBBefor(){
        if(ivjJRBBefor == null){
            try{
                ivjJRBBefor = new UIRadioButton();
                ivjJRBBefor.setName("JRBBefor");
                ivjJRBBefor.setText("Befor");
                ivjJRBBefor.setBounds(180, 10, 70, 22);
                // user code begin {1}
                String strBefor = StringResource.getStringResource("miufo1001274");  //"ǰ"
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
     * ���� JRBBehind ����ֵ��
     * @return JRadioButton
     */
    /* ���棺�˷������������ɡ� */
    private JRadioButton getJRBBehind(){
        if(ivjJRBBehind == null){
            try{
                ivjJRBBehind = new UIRadioButton();
                ivjJRBBehind.setName("JRBBehind");
                ivjJRBBehind.setText("Behind");
                ivjJRBBehind.setBounds(270, 10, 108, 22);
                ivjJRBBehind.addActionListener(this);
                // user code begin {1}
                String strBehind = StringResource.getStringResource("miufo1001275");  //"��"
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
     * ���� JSPColTitle ����ֵ��
     * @return JScrollPane
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� JSPReItem ����ֵ��
     * @return JScrollPane
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� JSPRowTitle ����ֵ��
     * @return JScrollPane
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� JSPTotal ����ֵ��
     * @return JScrollPane
     */
    /* ���棺�˷������������ɡ� */
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
            setName("CrossTableDefDlg");
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setSize(600, 555);
            setContentPane(getUfoDialogContentPane());
        } catch(java.lang.Throwable ivjExc){
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(StringResource.getStringResource("miufo1001276"));  //"�����"
        //���û������ֵ
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
     * ��ʼ����ѡ����Ŀ�б����ѡ����Ŀ�б�
     * ��ѡ���б�Ϊȫ����Ŀ��ȥ��ѡ���б�����ѡ�����Ŀ���ʣ����Ŀ
     * fullItem ,ȫ����Ŀ��Vector��ΪCrossTableFld����
     * �������ڣ�(2003-8-19 16:43:09)
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
            //��ʱ�൱���޸�
            //������ѡ�����ĿVector������Vector��GroupLayingFld.getMapType()+GroupLayingFld.getMapName()����

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
            //������ѡ��Ŀ�б�
            m_oColTitleItemModel = new ItemListModel(colVec);
            m_oRowTitleItemModel = new ItemListModel(rowVec);
            m_oTotalTitleItemModel = new ItemListModel(totalVec);
        } else{
            //��ʱ�൱���½�
            //������ѡ��Ŀ�б�
            m_oReItemModel = new ItemListModel(fullItem);
            m_oColTitleItemModel = new ItemListModel();
            m_oRowTitleItemModel = new ItemListModel();
            m_oTotalTitleItemModel = new ItemListModel();
        }

        //������ѡ����Ŀ�б���ʱ��Ҫͨ��ѭ���ȽϹ�����ѡ�����Ŀ
        //����
        CrossTableFld crossTableFld = null;
        DataProcessFld item = null;
        String strKey = null;
        Vector<CrossTableFld> vecRSelectItem = new Vector<CrossTableFld>();
        for(int i = 0; i < fullItem.size(); i++){
            item = (DataProcessFld)fullItem.get(i);
            strKey = item.getMapName();
            if(!seledNameHash.containsKey(strKey)){
                //��ѡ��Ŀ�ĸ�ʽ��Ϣ���Ƕ�̬�����ԭʼ����λ����Ϣ
                crossTableFld = new CrossTableFld(item);
                vecRSelectItem.add(crossTableFld);
            } else{
                //�����顢�ֲ��ֶε�λ����Ϣ����Ϊ��̬�����ԭʼ����λ����Ϣ
                //modified by liulp
                crossTableFld = (CrossTableFld)seledNameHash.get(strKey);
                crossTableFld.setFormatRelatCell(item.getFormatRelatCell());
                crossTableFld.setDisplayRelatCell(item.getDisplayRelatCell());
            }
        }
        //�����ѡ��Ŀ�б�
        m_oReItemModel = new ItemListModel(vecRSelectItem);
    }

    /**
     * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
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
            System.err.println(StringResource.getStringResource("miufo1000953"));  //"com.ufsoft.iuforeport.reporttool.pub.UfoDialog �� main() �з����쳣"
AppDebug.debug(exception);//@devTools             exception.printStackTrace(System.out);
        }
    }

    /**
     * ���б���ѡ��һ����Ŀ����һλ
     * �������ڣ�(2003-8-20 22:46:21)
     * @param index int
     * @param sourceListmodel com.ufsoft.iuforeport.reporttool.process.basedef.ItemListModel Դ�б�
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
     * �ӿ�ѡ�б���ѡ��һ����Ŀ��Ŀ���б�
     * �������ڣ�(2003-8-20 22:46:21)
     * @param index int
     * @param sourceListmodel com.ufsoft.iuforeport.reporttool.process.basedef.ItemListModel Դ�б�
     */
    private void moveToReList(int selIndex, ItemListModel sourceListmodel){
        if(selIndex < 0){
            return;
        }
        FieldMap item = sourceListmodel.removeItem(selIndex);
        ( (CrossTableFld)item).setType(ICubeConst.DIM_NON); m_oReItemModel.addItem(item);
    }

    /**
     * �ӿ�ѡ�б���ѡ��һ����Ŀ��Ŀ���б�
     * �������ڣ�(2003-8-20 22:46:21)
     * @param index int
     * @param aimListmodel com.ufsoft.iuforeport.reporttool.process.basedef.ItemListModel Ŀ���б�
     */
    private void moveToSeledList(int selIndex, ItemListModel aimListmodel, int listIndex){

        if(selIndex < 0){
            return;
        }
        FieldMap item = m_oReItemModel.removeItem(selIndex);
        //���ݲ�ͬ���б����ò�ͬ��ֵ
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
     * ���б���ѡ��һ����Ŀ����һλ
     * �������ڣ�(2003-8-20 22:46:21)
     * @param index int
     * @param sourceListmodel com.ufsoft.iuforeport.reporttool.process.basedef.ItemListModel Դ�б�
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
     * �˴����뷽��������
     *
     * �������ڣ�(2003-8-13 15:08:01)
     * @author������Ƽ
     * @param newDataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.CrossTabDef
     */
    public void setDataProcessDef(CrossTabDef newDataProcessDef){
        m_oDataProcessDef = newDataProcessDef;
    }

    /**
     * ������Ŀ����
     * �������ڣ�(2003-8-21 0:18:28)
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

    //�б�ѡ������Ӧ����
    public void valueChanged(ListSelectionEvent event){
        //��������帳ֵ�������С��С����������б�
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


