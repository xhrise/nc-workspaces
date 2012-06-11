package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoOpenedRepBizMenuExt;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.MenuUtil;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
/**
 * 公式追踪菜单的扩展
 * @author liulp
 *
 */
public class FormulaTraceExt  extends AbsIufoOpenedRepBizMenuExt{
	public FormulaTraceExt(UfoReport ufoReport) {
		super(ufoReport);
	}

	@Override
	protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new FormulaTraceCmd(ufoReport);
	}

	@Override
	protected String getMenuName() {
		return FormulaTraceBizUtil.doGetStrFormulaTrace();
	}
	
	@Override
	protected String[] getPaths() {
		return doGetDataMenuPaths();
	}
	
    public Object[] getParams(UfoReport container) {
    	CellPosition cell=container.getCellsModel().getSelectModel().getAnchorCell();
        return new Object[]{cell};
    }
    
    @Override
    public boolean isEnabled(Component focusComp) {
    	return GeneralQueryUtil.isGeneralDataVer(getUfoReport());
    }
    
    /**
     * 添加额外的事件处理
     */
	public void initListenerByComp(Component stateChangeComp) {
		if (stateChangeComp instanceof JCheckBoxMenuItem) {
			final JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) stateChangeComp;
			final String name = menuItem.getName();

			if (name != null) {
				menuItem.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							UfoReport report=getUfoReport();
							JComponent com=MenuUtil.getCompByPath(new String[]{MultiLang.getString("view"),MultiLang.getString("panelManager")}, 0, report.getJMenuBar(), report);
							JCheckBoxMenuItem windowMenu=null;
							if(com instanceof JMenu){
								JMenu menu=(JMenu)com;
								if(menu.getItemCount()<1){
									ActionListener[] listeners=menu.getActionListeners();
									ActionEvent event = null;
									for(int i=0;i<listeners.length;i++){
										event=new ActionEvent(e.getSource(),ActionEvent.ACTION_PERFORMED,"");
										listeners[i].actionPerformed(event);
									}
								}

								JPopupMenu popmenu=menu.getPopupMenu();
								for(int i=0;i<popmenu.getComponentCount();i++){
									if(name.equals(popmenu.getComponent(i).getName())&&popmenu.getComponent(i) instanceof JCheckBoxMenuItem)
										windowMenu=(JCheckBoxMenuItem)popmenu.getComponent(i);
								}

							}
							if(windowMenu!=null)
								windowMenu.setSelected(true);

						}

					}
				});

			}
		}
	}

	public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setCheckBoxMenuItem(true);
        uiDes.setViewComponentPos(ReportNavPanel.SOUTH_NAV);
        uiDes.setName(getMenuName());
        uiDes.setPaths(getPaths());
        uiDes.setGroup("traceDataGroup");
//        ActionUIDes uiDes1 = (ActionUIDes)uiDes.clone();
//        uiDes1.setPaths(null);
//        uiDes1.setPopup(true);
//        return new ActionUIDes[]{uiDes, uiDes1};
        return new ActionUIDes[]{uiDes};
    }

}
