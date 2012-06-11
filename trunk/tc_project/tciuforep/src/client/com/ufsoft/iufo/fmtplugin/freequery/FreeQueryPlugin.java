package com.ufsoft.iufo.fmtplugin.freequery;

import java.util.ArrayList;
import java.util.EventObject;

import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.TaskCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureQueryModelDef;
import nc.vo.iufo.task.TaskVO;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * �������ɲ�ѯ�Ĳ�� ʵ�ֵĹ����У�ָ��ѡ�񡢵���˳�򡢲�ѯ���ɺͱ������ݲ鿴�������´��ڣ�
 * 
 * @author ll 2007-10-25
 */
public class FreeQueryPlugin extends AbstractPlugIn implements IUfoContextKey{

	private boolean bCorpKeyword = false;

	private boolean bTimeKeyword = false;

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
		// �ӱ������л�ȡ����
		TableInputContextVO context = (TableInputContextVO) getReport().getContextVo();
		Object tableInputTransObj = context.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj tableInput = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
		String strTaskPK = tableInput.getRepDataParam().getTaskPK();
		TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
		TaskVO taskVO = taskCache.getTaskVO(strTaskPK);
		if (taskVO != null) {
			KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
			KeyGroupVO keyGroupVO = keyGroupCache.getByPK(taskVO.getKeyGroupId());

			KeyVO[] keyVOs = keyGroupVO.getKeys();
			for (int i = 0; i < keyVOs.length; i++) {
				if (KeyVO.isUnitKeyVO(keyVOs[i]))
					bCorpKeyword = true;
				else {
					if (keyVOs[i].isTimeKeyVO()||keyVOs[i].isAccPeriodKey())
						bTimeKeyword = true;
				}
			}
		}
	}

	/**
	 * ��ȡ���ɲ�ѯģ�Ͷ����ʵ��
	 * 
	 * @param defName
	 * @return
	 */
	public IFreeQueryModel getFreeQueryDef(String defName) {
		return (IFreeQueryModel) getReport().getCellsModel().getExtProp(defName);
	}

	/**
	 * �������ɲ�ѯģ�Ͷ����ʵ��
	 * 
	 * @param defName
	 * @param def
	 */
	public void setFreeQueryDef(String defName, IFreeQueryModel def) {
		if (def == null) {
			getReport().getCellsModel().getExtProps().remove(defName);

		} else {
			getReport().getCellsModel().putExtProp(defName, def);
		}
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
	 */
	public void shutdown() {
		// TODO �Զ����ɷ������

	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#store()
	 */
	public void store() {
		// TODO �Զ����ɷ������

	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
	 */
	public boolean isDirty() {
		// TODO �Զ����ɷ������
		return false;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
	 */
	public String[] getSupportData() {
		// TODO �Զ����ɷ������
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
	 */
	public SheetCellRenderer getDataRender(String extFmtName) {
		// TODO �Զ����ɷ������
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
	 */
	public SheetCellEditor getDataEditor(String extFmtName) {
		// TODO �Զ����ɷ������
		return null;
	}

	/*
	 * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
	 */
	public void actionPerform(UserUIEvent e) {
		// TODO �Զ����ɷ������

	}

	/*
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 */
	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		// TODO �Զ����ɷ������
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	public IPluginDescriptor createDescriptor() {
		// TODO �Զ����ɷ������
		return new AbstractPlugDes(this) {

			protected IExtension[] createExtensions() {
				ArrayList<IExtension> al = new ArrayList<IExtension>();
				if (bCorpKeyword)
					al.add(new FreeQueryExt(MeasureQueryModelDef.QUERYBY_CORP, getReport()));
				if (bTimeKeyword)
					al.add(new FreeQueryExt(MeasureQueryModelDef.QUERYBY_YEAR, getReport()));
				al.add(new FreeQueryExt(MeasureQueryModelDef.QUERY_FREE, getReport()));
				al.add(new FreeQueryExt(MeasureQueryModelDef.QUERY_ANAREOIRT, getReport()));
				return al.toArray(new IExtension[0]);
			}

		};
	}
}
