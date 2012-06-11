package com.ufsoft.iufo.inputplugin.patch31;

import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import nc.ui.pub.beans.UIMenuItem;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.menu.UFPopupMenu;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.sysplugin.edit.EditClearExt;
import com.ufsoft.report.sysplugin.edit.EditCopyExt;
import com.ufsoft.report.sysplugin.edit.EditCutExt;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.report.sysplugin.edit.FormatBrushExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.UFOTable;
import com.ufsoft.iufo.resource.StringResource;
/**
 * 工具栏三组:剪切、复制、粘贴、清除；填充向下，向右，向上，向左；页面设置、打印预览、打印。
 * 右键菜单：剪切、复制、粘贴、清除；填充（向下右上左）；分栏；锁定；
 * @author zzl 2005-8-11
 * @deprecated by 2008-4-28 王宇光 复制，剪切，粘贴等编辑功能放到统一插件：EditPlugin  
 * 
 */
public class Patch31PlugDes extends AbstractPlugDes {

    public Patch31PlugDes(IPlugIn plugin) {
        super(plugin);
    }

    protected IExtension[] createExtensions() {
        return new IExtension[]{
                new Cut(getReport()),new Copy(getReport()),new Paste(getReport()),new FormatBrushExt(getReport()),new Clear(getReport())
        };
    }

}

class Cut extends EditCutExt{
    public Cut(UfoReport report) {
		super(report);
	}
	protected int getCutType() {
        return UFOTable.CELL_CONTENT;
    }
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLangInput.getString("cut"));
        uiDes.setImageFile("reportcore/cut.gif");
        uiDes.setToolBar(true);
        uiDes.setGroup(MultiLang.getString("edit"));
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        return new ActionUIDes[]{uiDes};
    }    
}
class Copy extends EditCopyExt{
	public Copy(UfoReport report) {
		super(report);
	}
    protected int getCopyType() {
        return UFOTable.CELL_CONTENT;
    }    
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLangInput.getString("copy"));
        uiDes.setImageFile("reportcore/copy.gif");
        uiDes.setToolBar(true);
        uiDes.setGroup(MultiLang.getString("edit"));
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
//        ActionUIDes uiDes1 = (ActionUIDes) uiDes.clone();
//        uiDes1.setToolBar(false);
//        uiDes1.setPopup(true);
        return new ActionUIDes[]{uiDes};
    }
}
class Paste extends EditPasteExt{
    public Paste(UfoReport report) {
		super(report);
	}

	public ActionUIDes[] getUIDesArr() {
		// modyfy by 王宇光 2008-3-22 添加选择性粘贴
		ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLangInput.getString("paste"));
        uiDes.setImageFile("reportcore/paste.gif");
        uiDes.setToolBar(true);
                     
        uiDes.setGroup(MultiLang.getString("edit"));
        uiDes.setComboType(2);
        uiDes.setListCombo(true);
        uiDes.setComboComponent(createComboxMenu());       
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
//        ActionUIDes uiDes1 = (ActionUIDes) uiDes.clone();
//        uiDes1.setToolBar(false);
//        uiDes1.setPopup(true);
        return new ActionUIDes[]{uiDes};
    }
	/**
	 * 返回下拉弹出的菜单部分
	 * 
	 * @param
	 * @return JPopupMenu
	 * @i18n miufo1000275=内容
	 * @i18n miufo1000877=格式
	 * @i18n miufo1004057=边框除外
	 * @i18n uiufo53rq004=转置
	 * @i18n miufo1004049=选择性粘贴
	 */
	private JPopupMenu createComboxMenu(){
		JPopupMenu menu = new UFPopupMenu();
		
        JMenuItem contentItem = new UIMenuItem(StringResource.getStringResource("miufo1000275"));
        contentItem.setActionCommand(EditPasteExt.CONTENT);
        menu.add(contentItem);
        
        JMenuItem formatItem = new UIMenuItem(StringResource.getStringResource("miufo1000877"));
        formatItem.setActionCommand(EditPasteExt.FORMAT);
        menu.add(formatItem);
        
        JMenuItem noBorderItem = new UIMenuItem(StringResource.getStringResource("miufo1004057"));
        noBorderItem.setActionCommand(EditPasteExt.NO_BORDER);
        menu.add(noBorderItem);
        
        JMenuItem turnItem = new UIMenuItem(StringResource.getStringResource("uiufo53rq004"));
        turnItem.setActionCommand(EditPasteExt.TRANSFER);
        menu.add(turnItem);
        
        menu.addSeparator();
        
        JMenuItem chooseItem = new UIMenuItem(StringResource.getStringResource("miufo1004049"));
        chooseItem.setActionCommand(EditPasteExt.CHOOSE_DIALOG);
        menu.add(chooseItem);
        return menu;
	}
}
class Clear extends EditClearExt{
    public Clear(UfoReport report) {
		super(report);
	}
	protected int getClearType() {
        return UFOTable.CELL_CONTENT;
    }
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLangInput.getString("clear"));
        uiDes.setImageFile("reportcore/delete.gif");
        uiDes.setToolBar(true);
        uiDes.setGroup(MultiLang.getString("edit"));
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
//        ActionUIDes uiDes1 = (ActionUIDes) uiDes.clone();
//        uiDes1.setToolBar(false);
//        uiDes1.setPopup(true);
        return new ActionUIDes[]{uiDes};
    }
} 