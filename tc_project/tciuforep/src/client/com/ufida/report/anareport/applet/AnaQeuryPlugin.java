package com.ufida.report.anareport.applet;

import java.util.ArrayList;
import java.util.EventObject;

import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureQueryModelDef;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;

import com.ufida.dataset.IContext;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

public class AnaQeuryPlugin extends AbstractPlugIn implements IUfoContextKey{

	private boolean bCorpKeyword = false;

	private boolean bTimeKeyword = false;

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
		
	}

	/**
	 * 获取自由查询模型定义的实例
	 * 
	 * @param defName
	 * @return
	 */
	public IFreeQueryModel getFreeQueryDef(String defName) {
		return (IFreeQueryModel) getReport().getCellsModel().getExtProp(defName);
	}

	/**
	 * 设置自由查询模型定义的实例
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
		// TODO 自动生成方法存根

	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#store()
	 */
	public void store() {
		// TODO 自动生成方法存根

	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
	 */
	public boolean isDirty() {
		// TODO 自动生成方法存根
		return false;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
	 */
	public String[] getSupportData() {
		// TODO 自动生成方法存根
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
	 */
	public SheetCellRenderer getDataRender(String extFmtName) {
		// TODO 自动生成方法存根
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
	 */
	public SheetCellEditor getDataEditor(String extFmtName) {
		// TODO 自动生成方法存根
		return null;
	}

	/*
	 * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
	 */
	public void actionPerform(UserUIEvent e) {
		// TODO 自动生成方法存根

	}

	/*
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 */
	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		// TODO 自动生成方法存根
		return null;
	}

	private void createBefore(){

		// 从报表环境中获取参数
		IContext context = getReport().getContextVo();
		String strTaskPK =(String)context.getAttribute(IUfoContextKey.TASK_PK);
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
	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	public IPluginDescriptor createDescriptor() {
		createBefore();
		// TODO 自动生成方法存根
		return new AbstractPlugDes(this) {

			protected IExtension[] createExtensions() {
				ArrayList<IExtension> al = new ArrayList<IExtension>();
				if (bCorpKeyword){
					al.add(new AnaQueryExt(MeasureQueryModelDef.QUERYBY_CORP, getReport()));
					al.add(new AnaQueryExt(MeasureQueryModelDef.QEURY_NEXTCORP, getReport()));
				}
					
				if (bTimeKeyword)
					al.add(new AnaQueryExt(MeasureQueryModelDef.QUERYBY_YEAR, getReport()));
			    
				al.add(new AnaQueryExt(MeasureQueryModelDef.QUERY_ANAREOIRT, getReport()));
				return al.toArray(new IExtension[0]);
			}

		};
	}
}
