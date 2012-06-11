package com.ufsoft.report;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import com.sun.java.swing.plaf.windows.WindowsBorders;
import com.ufsoft.report.lookandfeel.plaf.Office2003ToolBarUI;
import com.ufsoft.report.menu.MenuUtil;
import com.ufsoft.report.menu.UFButton;
import com.ufsoft.report.menu.UFMenuButton;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.toolbar.dropdown.JPopupPanelButton;


/**
 * ������ ����ʱ�� 2004-8-4 16:34:26
 * 
 * @author caijie
 */
public class ReportToolBar extends JToolBar {
	private UfoReport m_report;
	//add by guogang 2007-6-14 �Ƿ񽫸�������뵽��ͼ�˵�
    private boolean isView=true;
    
    public boolean isView() {
		return isView;
	}

	public void setView(boolean isView) {
		this.isView = isView;
	}
    //add end
    /**
     * @param rpt
     * @since 3.0
     */
    public ReportToolBar(UfoReport rpt) {
        super();
        m_report = rpt;
        setRollover(true);        
        setFloatable(true);
        setUI(Office2003ToolBarUI.createUI(this));
        setBorder(WindowsBorders.getToolBarBorder());
    }

    /**
     * ����һ����������Ŀ
     * 
     * @param groupId -
     *            �����������ID,��ʼ��Ϊ0.
     * @param itemID -
     *            �����е�λ�á���ʼλ��Ϊ0�����С��0��Ϊ������ǰͷ����������Ѿ����ڵ���Ŀ���������� ĩβ
     * @param action -
     *            �����������¼����������ο��˵��������¼�
     */
    public Component addToolItem(IActionExt ext,ActionUIDes uiDes) {
//        if ((groupId < 0) | (groupId >= MAX_TOOL_GROUP)) {
//            String strError = MultiLang.getTableString("uiuforep0000819") + MAX_TOOL_GROUP  //�����������:�����groupID������0��
//            	+ MultiLang.getTableString("uiuforep0000820");//֮��
//            throw new IllegalArgumentException(strError);
//        }
//        if (action == null) {
//            String strError = MultiLang.getTableString("uiuforep0000821");//�����������:��������Ϊ��
//            throw new IllegalArgumentException(strError);
//        }
//        if (addToList(groupId, itemID, action)) {
//            updateToolBar();
//        }
    	
    	JComponent comp= MenuUtil.createActionComp(ext,uiDes,m_report);

        JComponent parent = getToolCompByPath(uiDes.getPaths(),0,this,m_report);//MenuUtil.getCompByPath(ext.getUIDes().getPaths(),0,this,m_report);
        if(parent == this && comp instanceof UFMenuButton){
            ((UFMenuButton)comp).addToToolBar(this);
        }else{
            parent.add(comp);
        }
        
        return comp;
        
    }

    /**
     * @param paths
     * @param i
     * @param bar
     * @param m_report2
     * @return JComponent
     */
    private JComponent getToolCompByPath(String[] paths, int fromIndex, JComponent comp, UfoReport report) {
        if(paths == null || paths.length == fromIndex){
            return (JComponent) comp;
        }
        Component[] comps = comp.getComponents();
        for(int i=0;i<comps.length;i++){
            if(comps[i] instanceof UFMenuButton && comps[i].getName().equals(paths[fromIndex])){
                return MenuUtil.getCompByPath(paths,fromIndex+1,(JComponent)comps[i],report);
            }
        }
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setImageFile(null);
        uiDes.setName(paths[fromIndex]);
        UFMenuButton menuButton = new UFMenuButton(null,uiDes);//(paths[fromIndex],report);
        menuButton.setIcon(ResConst.getImageIcon(null));//���ڲ˵����롣
        ((Container)comp).add(menuButton);
        return MenuUtil.getCompByPath(paths,fromIndex+1,menuButton,report);
    }

//
//
//    /**
//     * ĳ���������������Ŀ������
//     * 
//     * @param groupId -
//     *            ����ID
//     * @return int
//     */
//    public int getGroupNum(int groupId) {
//        if ((groupId < 0) | (groupId >= MAX_TOOL_GROUP)) {
//            return 0;
//        }
//        return m_arrGroup[groupId];
//    }
//
//    /**
//     * ����Ŀ��ӵ���Ŀ������,��������Ŀ�ķ�����Ϣ����
//     * 
//     * @param groupId -
//     *            �����ID
//     * @param itemID -
//     *            �����е�λ��
//     * @param action -
//     *            �����¼�������
//     * @return - ִ���Ƿ�ɹ�.
//     */
//    private boolean addToList(int groupId, int itemID,
//            final IActionExt action) {
//        if (m_arrTools.size() >= MAX_TOOL_GROUP * MAX_TOOL_GROUP_ITEM) {
//            String strError = MultiLang.getTableString("uiuforep0000822");//�������������,���ܴ����µķ���
//            throw new IndexOutOfBoundsException(strError);
//        }
//
//        UFButton item = new UFButton(action);
//
//        //���ͼƬ,����Ҳ���ָ��ͼƬ,����ʾһ��Ĭ��ͼƬ
//        String img = action.getUIDes().getImageFile();
//        if (img != null) {
//            ImageIcon icon = ResConst.getImageIcon(img);
//            if (icon != null) {
//                item.setIcon(icon);
//            } else {
//                item.setIcon(ResConst.getDefaultImageIcon());
//            }
//
//        }
//
//        //��ʾ��ʾ
//        item.setToolTipText(action.getName());
//
//        //��Ӧ�¼�
//        if (action != null) {
//        	item.addActionListener(ReportMenuBar.createExtListener(action, m_report,null));
//        }
//
//        //��ӵ���Ŀ����
//        int index = getToolIndex(groupId, itemID);
//        if (index < 0) {//���ܴ�������
//            return false;
//        } else {
//            m_arrTools.add(index, item);
//        }
//
//        return true;
//    }
//
//    /**
//     * ����ָ������ͷ���λ�ô�����Ŀ����Ŀ�����ж�Ӧ�����.��������򷵻�-1.
//     * 
//     * @param groupId -
//     *            �����������ID.
//     * @param itemID -
//     *            �����е�λ�á���ʼλ��Ϊ0�����С��0��Ϊ������ǰͷ����������Ѿ����ڵ���Ŀ���������� ĩβ
//     * @return int
//     */
//    private int getToolIndex(int groupID, int itemID) {
//        int result = 0;
//
//        // ͳ�Ʋ�����ǰ���������Ŀ����
//        for (int i = 0; i < groupID; i++) {
//            result = result + m_arrGroup[i];
//        }
//
//        int pos = 0;
//        if (itemID > m_arrGroup[groupID]) {
//            pos = m_arrGroup[groupID];
//        } else if (itemID > 0) {
//            pos = itemID;
//        }
//        result = result + pos;
//
//        //���·�����Ϣ
//        m_arrGroup[groupID]++;
//
//        return result;
//    }
//
//    /**
//     * ���²��ù�����
//     */
//    private void updateToolBar() {
//        //ɾ�����е���Ŀ
//        removeAll();
//
//        //����������е���Ŀ,������ͬ�����÷ָ�������
//        JButton tool = null;
//        int groupIndex = 0;
//        int toolIndex = 0;
//        int groupToolNum = 0;
//
//        //��һ��
//        while (m_arrGroup[groupIndex] <= 0)
//            groupIndex++;
//        for (groupToolNum = 0; groupToolNum < m_arrGroup[groupIndex]; groupToolNum++) {
//            tool = (JButton) m_arrTools.get(toolIndex);
//            if (tool != null) {
//                add(tool);
//            }
//            toolIndex++;
//        }
//        groupIndex++;
//
//        //������
//        for (; groupIndex < MAX_TOOL_GROUP; groupIndex++) {
//            groupToolNum = m_arrGroup[groupIndex];
//            if (groupToolNum > 0) {
//                addSeparator();//�ָ���
//                for (int i = 0; i < groupToolNum; i++) {
//                    tool = (JButton) m_arrTools.get(toolIndex);
//                    if (tool != null) {
//                        add(tool);
//                    }
//                    toolIndex++;
//                }
//            }
//        }
//
//        //����ʽЧ��
//        this.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
//    }
//
//    /**
//     * ��¼������
//     */
//    private Container container = ReportToolBar.this;
//
//    /**
//     * ���group��
//     */
//    public final static int MAX_TOOL_GROUP = 128;
//
//    /**
//     * ÿ��group�������Ŀ��
//     */
//    private final int MAX_TOOL_GROUP_ITEM = 256;
//
//    /**
//     * ��Ŀ����,��˳����(�����ָ��)
//     */
//    private ArrayList m_arrTools = new ArrayList(MAX_TOOL_GROUP
//            * MAX_TOOL_GROUP_ITEM);
//
//    /**
//     * ��Ŀ�ķ�����Ϣ��,��Ŷ�Ӧ�����groupId,ֵ��Ӧ�������Ŀ����,
//     */
//    private int[] m_arrGroup = new int[MAX_TOOL_GROUP];
//
    public void adjustEnabled(Component focusComp){
        Component[] buttons = getComponents();
        for(int i=0;i<buttons.length;i++){
            if(buttons[i] instanceof UFButton){
                ((UFButton)buttons[i]).adjustEnabled(focusComp);
            } else if(buttons[i] instanceof JPopupPanelButton){
            	((JPopupPanelButton)buttons[i]).adjustEnabled(focusComp);
            }
            
        }
    }
  
}