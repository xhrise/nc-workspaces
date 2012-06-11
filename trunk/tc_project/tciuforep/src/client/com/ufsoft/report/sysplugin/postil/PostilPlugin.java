package com.ufsoft.report.sysplugin.postil;

import java.util.EventObject;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * @author zzl Create on 2005-3-7 功能点：右键添加两个菜单：编辑批注、删除批注；鼠标移动到单元格上时自动显示。
 *         待优化的地方：编辑批注时，使用不带边框并且可以拖动大小的dialog，暂时没有找到实现的方法。 Modify by zhaopq on
 *         2009-1-8 待处理地方：因不能获取分栏操作的事件，无法释放各分栏的批注对象，所以分栏暂不支持批注。
 */
public class PostilPlugin extends AbstractPlugIn implements UserActionListner {
	/** 扩展数据类型－批注 */
	public static final String EXT_FMT_POSTIL = "iufopostil";

	/** 菜单组 */
	public static final String EXT_MENU_GROUP = "postil_group";

	// @edit by zhaopq at 2009-1-9,上午09:48:50
	private PostilHelper helper = new PostilHelper(this);

	public void startup() {
	}

	public void shutdown() {
		helper.reset();
	}

	public void store() {
	}

	public IPluginDescriptor createDescriptor() {
		return new PostilDescriptor(this);
	}

	public void setReport(final UfoReport report) {
		super.setReport(report);
	}

	public boolean isDirty() {
		return false;
	}

	public String[] getSupportData() {
		return new String[] { EXT_FMT_POSTIL };
	}

	public SheetCellRenderer getDataRender(String extFmtName) {
		return new PostilRender();
	}

	public SheetCellEditor getDataEditor(String extFmtName) {
		return null;
	}

	/**
	 * @created by zhaopq at 2009-1-8,下午07:10:58
	 * 
	 */
	public void userActionPerformed(UserUIEvent e) {
		if (e.getEventType() == UserUIEvent.MODEL_CHANGED) {
			// 如果发生了model切换需要释放旧model批注的UI资源
			helper.reset();
		}
	}

	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		return null;
	}

	public PostilHelper getHelper() {
		return helper;
	}

	public void setHelper(PostilHelper helper) {
		this.helper = helper;
	}

}
