package com.ufsoft.report.sysplugin.cellpostil;

import java.util.EventObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.exception.ForbidedOprException;
import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.event.ViewerListener;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.postil.PostilRender;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * ��ʽ���̬ʱ����ע���
 */
public class CellPostilDefPlugin extends AbstractPlugin implements
		UserActionListner, ViewerListener {

	/** ��չ�������ͣ���ע */
	public static final String EXT_FMT_POSTIL = "iufopostil";

	/** �˵��� */
	public static final String GROUP = "postil_group";

	private CellPostilManager helper;

	static {
		CellRenderAndEditor.registRender(EXT_FMT_POSTIL, new PostilRender());
	}

	public CellPostilDefPlugin(boolean postilEditable) {
		helper = new CellPostilManager(this, postilEditable);
	}

	public CellPostilDefPlugin() {
		helper = new CellPostilManager(this, true);// ��ʽ̬���Ա༭��ע
	}

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] { new PostilInsertAction(this),
				new PostilEditAction(this), new PostilDeleteAction(this),
				new PostilControlAction(this), new PostilOneShowAction(this),
				new PostilOneHideAction(this), };
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
	}

	@Override
	public void startup() {
		getEventManager().addListener(this);
	}

	public void userActionPerformed(UserUIEvent e) {
		if (e.getEventType() == UserUIEvent.MODEL_CHANGED) {
			// ���������model�л���Ҫ�ͷž�model��ע��UI��Դ
			CellsModel cellsModel = (CellsModel) e.getOldValue();
			if (cellsModel == null) {
				AppDebug.warn("������CellsModel���л�����ȴû���ͷŵ���Ӧ����עUI��Դ��");
			}
			helper.destroy(cellsModel);
		}
	}

	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		// TODO Auto-generated method stub
		return null;
	}

	CellPostilManager getPostilManager() {
		return helper;
	}

	public void onActive(Viewer currentActiveView, Viewer oldActiveView) {
	}

	public void onClose(Viewer view) {//�رձ���ʱ����Ҫ�ͷŵ���ע��Դ
		if (view instanceof ReportDesigner) {
			CellsModel cellsModel = ((ReportDesigner) view).getCellsModel();
			getPostilManager().destroy(cellsModel);
		}
	}

	public void onOpen(Viewer view) {// ���µı���ʱ��������ע����ʾ��
		if (view instanceof ReportDesigner) {
			CellsPane cellsPane = ((ReportDesigner) view).getCellsPane();
			if (getPostilManager().isStatusShow()) {
				getPostilManager().showAllPostils(true, cellsPane);
			}
		}
	}

}
