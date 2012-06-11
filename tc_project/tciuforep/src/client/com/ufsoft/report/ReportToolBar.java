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
 * 工具栏 创建时间 2004-8-4 16:34:26
 * 
 * @author caijie
 */
public class ReportToolBar extends JToolBar {
	private UfoReport m_report;
	//add by guogang 2007-6-14 是否将该组件加入到视图菜单
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
     * 增加一个工具栏项目
     * 
     * @param groupId -
     *            工具栏分组的ID,起始组为0.
     * @param itemID -
     *            分组中的位置。起始位置为0。如果小于0认为放在最前头；如果大于已经存在的项目数量，放在 末尾
     * @param action -
     *            工具栏触发事件的描述。参考菜单触发的事件
     */
    public Component addToolItem(IActionExt ext,ActionUIDes uiDes) {
//        if ((groupId < 0) | (groupId >= MAX_TOOL_GROUP)) {
//            String strError = MultiLang.getTableString("uiuforep0000819") + MAX_TOOL_GROUP  //输入参数错误:分组号groupID必须在0和
//            	+ MultiLang.getTableString("uiuforep0000820");//之间
//            throw new IllegalArgumentException(strError);
//        }
//        if (action == null) {
//            String strError = MultiLang.getTableString("uiuforep0000821");//输入参数错误:动作不能为空
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
        menuButton.setIcon(ResConst.getImageIcon(null));//用于菜单对齐。
        ((Container)comp).add(menuButton);
        return MenuUtil.getCompByPath(paths,fromIndex+1,menuButton,report);
    }

//
//
//    /**
//     * 某个工具栏分组的项目数量。
//     * 
//     * @param groupId -
//     *            分组ID
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
//     * 将项目添加到项目链表中,并更新项目的分组信息数组
//     * 
//     * @param groupId -
//     *            分组的ID
//     * @param itemID -
//     *            分组中的位置
//     * @param action -
//     *            触发事件的描述
//     * @return - 执行是否成功.
//     */
//    private boolean addToList(int groupId, int itemID,
//            final IActionExt action) {
//        if (m_arrTools.size() >= MAX_TOOL_GROUP * MAX_TOOL_GROUP_ITEM) {
//            String strError = MultiLang.getTableString("uiuforep0000822");//工具栏分组过多,不能创建新的分组
//            throw new IndexOutOfBoundsException(strError);
//        }
//
//        UFButton item = new UFButton(action);
//
//        //添加图片,如果找不到指定图片,将显示一个默认图片
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
//        //显示提示
//        item.setToolTipText(action.getName());
//
//        //响应事件
//        if (action != null) {
//        	item.addActionListener(ReportMenuBar.createExtListener(action, m_report,null));
//        }
//
//        //添加到项目链表
//        int index = getToolIndex(groupId, itemID);
//        if (index < 0) {//不能创建新组
//            return false;
//        } else {
//            m_arrTools.add(index, item);
//        }
//
//        return true;
//    }
//
//    /**
//     * 返回指定分组和分组位置处的项目在项目链表中对应的序号.如果错误则返回-1.
//     * 
//     * @param groupId -
//     *            工具栏分组的ID.
//     * @param itemID -
//     *            分组中的位置。起始位置为0。如果小于0认为放在最前头；如果大于已经存在的项目数量，放在 末尾
//     * @return int
//     */
//    private int getToolIndex(int groupID, int itemID) {
//        int result = 0;
//
//        // 统计插入组前所有组的项目总数
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
//        //更新分组信息
//        m_arrGroup[groupID]++;
//
//        return result;
//    }
//
//    /**
//     * 重新布置工具栏
//     */
//    private void updateToolBar() {
//        //删除所有的项目
//        removeAll();
//
//        //重新添加所有的项目,并将不同分组用分割条隔开
//        JButton tool = null;
//        int groupIndex = 0;
//        int toolIndex = 0;
//        int groupToolNum = 0;
//
//        //第一组
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
//        //其余组
//        for (; groupIndex < MAX_TOOL_GROUP; groupIndex++) {
//            groupToolNum = m_arrGroup[groupIndex];
//            if (groupToolNum > 0) {
//                addSeparator();//分隔条
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
//        //滚过式效果
//        this.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
//    }
//
//    /**
//     * 记录父容器
//     */
//    private Container container = ReportToolBar.this;
//
//    /**
//     * 最大group数
//     */
//    public final static int MAX_TOOL_GROUP = 128;
//
//    /**
//     * 每个group的最大项目数
//     */
//    private final int MAX_TOOL_GROUP_ITEM = 256;
//
//    /**
//     * 项目链表,按顺序存放(包括分割符)
//     */
//    private ArrayList m_arrTools = new ArrayList(MAX_TOOL_GROUP
//            * MAX_TOOL_GROUP_ITEM);
//
//    /**
//     * 项目的分组信息表,序号对应分组号groupId,值对应该组的项目数量,
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