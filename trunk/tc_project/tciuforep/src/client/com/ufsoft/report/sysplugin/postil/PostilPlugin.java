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
 * @author zzl Create on 2005-3-7 ���ܵ㣺�Ҽ���������˵����༭��ע��ɾ����ע������ƶ�����Ԫ����ʱ�Զ���ʾ��
 *         ���Ż��ĵط����༭��עʱ��ʹ�ò����߿��ҿ����϶���С��dialog����ʱû���ҵ�ʵ�ֵķ����� Modify by zhaopq on
 *         2009-1-8 ������ط������ܻ�ȡ�����������¼����޷��ͷŸ���������ע�������Է����ݲ�֧����ע��
 */
public class PostilPlugin extends AbstractPlugIn implements UserActionListner {
	/** ��չ�������ͣ���ע */
	public static final String EXT_FMT_POSTIL = "iufopostil";

	/** �˵��� */
	public static final String EXT_MENU_GROUP = "postil_group";

	// @edit by zhaopq at 2009-1-9,����09:48:50
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
	 * @created by zhaopq at 2009-1-8,����07:10:58
	 * 
	 */
	public void userActionPerformed(UserUIEvent e) {
		if (e.getEventType() == UserUIEvent.MODEL_CHANGED) {
			// ���������model�л���Ҫ�ͷž�model��ע��UI��Դ
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
