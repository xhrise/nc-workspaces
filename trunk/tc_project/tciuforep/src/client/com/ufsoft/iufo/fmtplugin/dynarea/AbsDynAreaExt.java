package com.ufsoft.iufo.fmtplugin.dynarea;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

/**
 * 设置为动态区
 * @author chxw
 */
public abstract class AbsDynAreaExt extends AbsActionExt implements IUfoContextKey{
	private DynAreaDefPlugIn m_dynAreaPlugIn = null;
	/**
	 * 新添加的动态区对象缓存:AreaPosition-DynAreaCell
	 */
	private Hashtable<AreaPosition, DynAreaCell> m_addedDynArea = new Hashtable<AreaPosition, DynAreaCell>();
	/**
	 * 修改前动态区对象缓存:AreaPosition-DynAreaCell
	 */
	private Hashtable<AreaPosition, DynAreaCell> m_updatedDynArea = new Hashtable<AreaPosition, DynAreaCell>();
	/**
	 * 删除的动态区对象缓存:如果删除的对象是上一次调用updateReport()后新添加的,直接删除不放入此Map中
	 */
	private ArrayList<DynAreaCell> m_removedDynArea = new ArrayList<DynAreaCell>();
	
	// @edit by ll at 2009-5-14,下午02:48:10 修改时记录原始VO，以便取消时进行恢复
	private Hashtable<String, DynAreaVO> m_UpdateOldVO = new Hashtable<String, DynAreaVO>();
	/**
	 * 报表动态区域实体链
	 */
	private List<DynAreaCell> m_dynAreas = null;
	
	/**
	 * 构造函数
	 * @param dynAreaPlug
	 */
	public AbsDynAreaExt(DynAreaDefPlugIn dynAreaPlug){
		m_dynAreaPlugIn = dynAreaPlug;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return null;
	}

	/**
	 * 动态区域设置实现；
	 * DynAreaMngDlg.ID_ADD为右键添加动态区，DynAreaMngDlg.ID_UPDATE为修改动态区，DynAreaMngDlg.ID_DELETE为删除动态区。
	 * 
	 * @param report
	 * @i18n uiiufofmt00001=不能添加动态区域:
	 * @i18n uiiufofmt00002=不能删除动态区域:
	 */
	protected void dynAreaMngImpl(UfoReport report){
		// @edit by wangyga at 2009-1-22,下午02:24:37 增加动态区设置的默认边线
		boolean isAddOrUpdate = true;
		for (;;) {
			//直接显示动态区域管理对话框
			DynAreaMngDlg dlgDynAreaMng = new DynAreaMngDlg(getReport().getTable().getCells(), m_dynAreas.toArray(new DynAreaCell[0])); 
			dlgDynAreaMng.show(); 
			int nRet = dlgDynAreaMng.getResult();
			if(nRet == UfoDialog.ID_OK){//将报表动态区域的增加、删除、和更新设置到动态区域业务模型中
				
				boolean haschanged = refreshDynArea();
                if(isAddOrUpdate && haschanged)
                	setDynAreaProperty();		
					
				break;
			} else if(nRet == UfoDialog.ID_CANCEL){
				cancelUpdateDynArea();
				break;
			}
			
			switch (nRet) {
				case DynAreaMngDlg.ID_ADD://添加动态区域操作
					addDynAreaImpl(dlgDynAreaMng, null);				
					continue;
				case DynAreaMngDlg.ID_UPDATE://修改动态区域  
					updateDynAreaImpl(dlgDynAreaMng);
					continue;
				case DynAreaMngDlg.ID_DELETE://删除动态区域
					delDynAreaImpl(dlgDynAreaMng);
					isAddOrUpdate = false;
					continue;
			}
		}     
	}

	/**
	 * 打开动态区域设置对话框，添加一个新的动态区域到动态区域管理对话框
	 * @param dlgDynAreaMng
	 * @param area
	 * @param isContinue true/false 是否继续循环
	 */
	protected boolean addDynAreaImpl(DynAreaMngDlg dlgDynAreaMng, IArea area){
		DynAreaSetDlg dynAreaSetDlg = new DynAreaSetDlg(getReport().getTable().getCells(),getReport().getCellsModel());
		dynAreaSetDlg.setModal(false);                    
		
		//在5.1版本增加功能：根据选择区域，直接弹出动态区域设置对话框
		if(area != null){
			dynAreaSetDlg.setArea(AreaPosition.getInstance(area.toString()));
		}
        DynAreaModel dynAreaModel = getDynAreaModel();
		//因为所有的动态区域只能有一个方向，所以取动态区域管理的动态区域扩展方向
		if(dlgDynAreaMng == null && dynAreaModel.getDynAreaDirection() != DynAreaVO.DIRECTION_UNDEFINED){
			dynAreaSetDlg.setDirection(dynAreaModel.getDynAreaDirection());
			dynAreaSetDlg.setDirectionRead();
		} else if(dlgDynAreaMng != null && dlgDynAreaMng.getDynAreaDirection() != DynAreaVO.DIRECTION_UNDEFINED){
			dynAreaSetDlg.setDirection(dlgDynAreaMng.getDynAreaDirection());
			dynAreaSetDlg.setDirectionRead();
		}
		dynAreaSetDlg.show();

		//5.1版本增加功能：根据选择区域，直接弹出动态区域设置对话框
		//如果用户点击取消按钮，则直接关闭动态区域设置对话框返回
		if(area != null && dynAreaSetDlg.getResult() == UfoDialog.ID_CANCEL){
			return false;
		} else if (dynAreaSetDlg.getResult() == UfoDialog.ID_OK) {
			DynAreaVO newDynAreaVO = new DynAreaVO(dynAreaSetDlg.getArea(), dynAreaSetDlg.getDirection()); 
			DynAreaCell newDynCell = new DynAreaCell(newDynAreaVO.getOriArea(), newDynAreaVO);
			String errorStr = dynAreaModel.check(newDynCell);
			if(errorStr == null)
				errorStr = isIntersectWithAddOrUpdate(dynAreaSetDlg.getArea());
			
			if(errorStr == null){
				m_addedDynArea.put(newDynCell.getArea(),newDynCell);//加入到增加动态区域缓存中
				m_dynAreas.add(newDynCell);
			} else{
				UfoPublic.sendErrorMessage(errorStr,getReport(),null);
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 打开动态区域设置对话框，修改已存在的动态区域设置
	 * @param dlgDynAreaMng
	 * @param model
	 */
	private void updateDynAreaImpl(DynAreaMngDlg dlgDynAreaMng){
		DynAreaSetDlg dynAreaUpdateDlg = new DynAreaSetDlg(getReport().getTable().getCells(),getReport().getCellsModel());
		dynAreaUpdateDlg.setModal(false);                        
		
		DynAreaCell oldDynAreaCell = dlgDynAreaMng.getCurrentSelectedDynAreaCell();
		dynAreaUpdateDlg.setArea(oldDynAreaCell.getArea());
		dynAreaUpdateDlg.setDirection(oldDynAreaCell.getDynAreaVO().getDirection());
		//if (model.getDynAreaSize() > 1) {//如果当前动态区域已经多于1个，方向不能更改
		//修改动态区时不允许修改方向。
		dynAreaUpdateDlg.setDirectionRead();
		//}
		dynAreaUpdateDlg.show();

		if (dynAreaUpdateDlg.getResult() == UfoDialog.ID_OK) { 
			//加入模型中:如果修改的是新添入的区域对象,直接放在新对象Map中,否则放在更新对象Map中
			if(m_addedDynArea.get(oldDynAreaCell.getArea()) != null){
				m_addedDynArea.remove(oldDynAreaCell.getArea());
				String errorStr = isIntersectWithAddOrUpdate(dynAreaUpdateDlg.getArea());
				if(errorStr == null){
					oldDynAreaCell.setArea(dynAreaUpdateDlg.getArea());
					oldDynAreaCell.getDynAreaVO().setOriArea(dynAreaUpdateDlg.getArea());
					m_addedDynArea.put(dynAreaUpdateDlg.getArea(), oldDynAreaCell);
				} else{
					m_addedDynArea.put(oldDynAreaCell.getArea(), oldDynAreaCell);
					UfoPublic.sendErrorMessage(errorStr,getReport(),null);
				}
			} else{  
				DynAreaModel dynAreaModel = getDynAreaModel();
				String errorStr = dynAreaModel.checkUpdate(oldDynAreaCell, dynAreaUpdateDlg.getArea(), dynAreaUpdateDlg.getDirection());
				if(errorStr == null)
					errorStr = isIntersectWithAddOrUpdate(dynAreaUpdateDlg.getArea());
				if(errorStr == null){
					DynAreaCell newDynAreaCell = (DynAreaCell) oldDynAreaCell.clone();
					// @edit by ll at 2009-5-14,下午03:14:04
					newDynAreaCell.setValue(oldDynAreaCell.getDynAreaVO().clone());//DynAreaCell的value不复制，会导致动态区共用vo
					
					newDynAreaCell.setArea(dynAreaUpdateDlg.getArea());
					newDynAreaCell.getDynAreaVO().setOriArea(dynAreaUpdateDlg.getArea());
					m_updatedDynArea.put(dynAreaUpdateDlg.getArea(), newDynAreaCell);
					String dynPK = newDynAreaCell.getDynAreaPK();
					if(!m_UpdateOldVO.containsKey(dynPK))//只在第一次记录最原始的cell
						m_UpdateOldVO.put(dynPK, oldDynAreaCell.getDynAreaVO());
					int index = m_dynAreas.indexOf(oldDynAreaCell);
					m_dynAreas.remove(oldDynAreaCell);
					m_dynAreas.add(index,newDynAreaCell);
				} else{
					UfoPublic.sendErrorMessage(errorStr,getReport(),null);
				}
			}
		}
	}
	
	/**
	 * 打开动态区域设置对话框，删除选定的动态区域
	 * @param dlgDynAreaMng
	 * @param model
	 */
	private void delDynAreaImpl(DynAreaMngDlg dlgDynAreaMng){
		DynAreaSetDlg dynAreaUpdateDlg = new DynAreaSetDlg(getReport().getTable().getCells(),getReport().getCellsModel());
		dynAreaUpdateDlg.setModal(false);
		DynAreaCell deleteDyaAreaCell = dlgDynAreaMng.getCurrentSelectedDynAreaCell();

		//如果删除的是新增加的区域,直接删除,否则缓存到m_removedDynArea中用于还原
		try {
			DynAreaModel dynAreaModel = getDynAreaModel();
			dynAreaModel.checkRemove(deleteDyaAreaCell);
			
			if(m_addedDynArea.get(deleteDyaAreaCell.getArea()) == null){
				m_removedDynArea.add(deleteDyaAreaCell);
			} else{
				m_addedDynArea.remove(deleteDyaAreaCell.getArea());
			}
			m_dynAreas.remove(deleteDyaAreaCell);
		} catch(Exception e) {
			UfoPublic.sendErrorMessage(StringResource.getStringResource("uiiufofmt00002") + e.getMessage(),getReport(),e);
		}               
	}
	
	/**
	 * 将增加的、删除的、和更新的动态区域设置到动态区域模型中
	 */
	private boolean refreshDynArea(){
		boolean hasChanged = false;
		DynAreaModel model =  m_dynAreaPlugIn.getDynAreaModel(); 
		CellsModel cellsModel = getReport().getCellsModel();
		FormulaModel formulaModel = FormulaModel.getInstance(cellsModel);
		UfoFmlExecutor ufoFmlExecutor = formulaModel.getUfoFmlExecutor();
		for(Iterator iter = m_removedDynArea.iterator();iter.hasNext();){
			String strDynPK=((DynAreaCell)iter.next()).getDynAreaVO().getDynamicAreaPK();
			String strRepId = getReport().getContextVo().getAttribute(REPORT_PK) == null ? null : (String)getReport().getContextVo().getAttribute(REPORT_PK);
			
			model.removeDynArea(strRepId,strDynPK);
			//删除动态区公式链
			
//			FormulaDefPlugin formPlugIn=(FormulaDefPlugin) getReport().getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
//			if(formPlugIn!=null && formPlugIn.getFmlExecutor()!=null){
//				formPlugIn.getFmlExecutor().removeDynamicArea(strDynPK);
//			}
			ufoFmlExecutor.removeDynamicArea(strDynPK);
			m_dynAreaPlugIn.setDirty(true);
			hasChanged = true;
		}
		
		for(Enumeration enumer = m_updatedDynArea.keys(); enumer.hasMoreElements();){
			AreaPosition area = (AreaPosition) enumer.nextElement();
			String strRepId = getReport().getContextVo().getAttribute(REPORT_PK) == null ? null : (String)getReport().getContextVo().getAttribute(REPORT_PK);
			
			DynAreaCell dCell = (DynAreaCell)m_updatedDynArea.get(area);
			if(m_UpdateOldVO.containsKey(dCell.getDynAreaPK())){//需要进行vo对象的引用，因为update过程中复制出了新对象
				DynAreaVO vo = m_UpdateOldVO.get(dCell.getDynAreaPK());
				vo.setOriArea(dCell.getDynAreaVO().getOriArea());
				dCell.setValue(vo);
			}
			model.updateDynArea(strRepId,area,dCell);
			//删除动态区公式链
			String strDynPK=((DynAreaCell)m_updatedDynArea.get(area)).getDynAreaPK();
//			FormulaDefPlugin formPlugIn=(FormulaDefPlugin) getReport().getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
//			if(formPlugIn!=null && formPlugIn.getFmlExecutor()!=null){
//				formPlugIn.getFmlExecutor().updateDynamicArea(strDynPK);
//			}
			ufoFmlExecutor.removeDynamicArea(strDynPK);
			m_dynAreaPlugIn.setDirty(true);
			hasChanged = true;
		}
		
		for(Enumeration enumer = m_addedDynArea.keys(); enumer.hasMoreElements();){
			AreaPosition area = (AreaPosition) enumer.nextElement();
			model.addDynArea((DynAreaCell)m_addedDynArea.get(area));
			m_dynAreaPlugIn.setDirty(true);
			hasChanged = true;
		}
		return hasChanged;
	}
	/**
	 * 将更新的动态区域设置进行恢复
	 */
	private void cancelUpdateDynArea(){
		if(m_UpdateOldVO.size()==0)
			return;
		for(Enumeration enumer = m_updatedDynArea.keys(); enumer.hasMoreElements();){
			AreaPosition area = (AreaPosition) enumer.nextElement();
			DynAreaCell dCell = (DynAreaCell)m_updatedDynArea.get(area);
			String key = dCell.getDynAreaPK();
			if(m_UpdateOldVO.containsKey(key)){
				dCell.setValue(m_UpdateOldVO.get(key));
				m_UpdateOldVO.remove(key);
			}
		}
	}
	/**
	 * 检查选定区域单元格是否与添加的或者更新的动态区域区域相交
	 * @return
	 */
	private String isIntersectWithAddOrUpdate(AreaPosition area){
		if(m_dynAreas == null || m_dynAreas.size() == 0 || area == null){
			return null;
		}
		
		//是否与添加的动态区相交
		for(DynAreaCell dynAreaCell : m_addedDynArea.values()){
			if(dynAreaCell.getArea().intersection(area)){
				return StringResource.getStringResource("miufo1001776");
			}
		}
		
		//是否与更新的动态区相交
		for(DynAreaCell dynAreaCell : m_updatedDynArea.values()){
			if(dynAreaCell.getArea().intersection(area)){
				return StringResource.getStringResource("miufo1001776");
			}
		}
		return null;
	}
	
	/**
	 * 设置动态区的默认单元属性
	 * @create by wangyga at 2008-12-26,下午01:17:06
	 *
	 * @param area
	 */
	private void setDynAreaProperty(){
		
		CellsModel cellsModel = getReport().getCellsModel();
		IArea area = cellsModel.getSelectModel().getSelectedArea();
		CellPosition[] cellPoses = area.split();
		for(CellPosition pos : cellPoses){
			IufoFormat format =  (IufoFormat)cellsModel.getCellFormatIfNullNew(pos);
			
			format.setLineType(Format.TOPLINE, TableConstant.L_SOLID1);
			format.setLineType(Format.BOTTOMLINE, TableConstant.L_SOLID1);
			format.setLineType(Format.LEFTLINE, TableConstant.L_SOLID1);
			format.setLineType(Format.RIGHTLINE, TableConstant.L_SOLID1);
			format.setLineColor(Format.TOPLINE, Color.BLUE);
			format.setLineColor(Format.BOTTOMLINE, Color.BLUE);
			format.setLineColor(Format.LEFTLINE, Color.BLUE);
			format.setLineColor(Format.RIGHTLINE, Color.BLUE);
		}
	}
	
	/**
	 * 清除缓存对象
	 */
	private void clear(){
		DynAreaModel dynAreaModel = getDynAreaModel();
		m_dynAreas = new ArrayList<DynAreaCell>(Arrays.asList(dynAreaModel.getDynAreaCells()));
		
		m_addedDynArea.clear();
		m_updatedDynArea.clear();
		m_removedDynArea.clear();
	}
	
	/**
	 * @return 返回报表工具。
	 */
	private UfoReport getReport() {
		return this.getDynAreaPlugIn().getReport();
	}
	
	private DynAreaModel getDynAreaModel(){
		return DynAreaModel.getInstance(getReport().getCellsModel());
	}
	
	/**
	 * @return 返回动态区域插件。
	 */
	public DynAreaDefPlugIn getDynAreaPlugIn() {
		return m_dynAreaPlugIn;
	}
	
	@Override
	public Object[] getParams(UfoReport report){
		clear();
		excuteImpl(report);
		return null;
	}
	
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getDesName());
        uiDes.setPaths(new String[]{MultiLang.getString("format")});
        uiDes.setGroup(DynAreaDefPlugIn.MENU_GROUP);
        uiDes.setShowDialog(true);
        ActionUIDes uiDes2 = (ActionUIDes)uiDes.clone();
        uiDes2.setPopup(true);
        uiDes2.setPaths(new String[]{});
        return new ActionUIDes[]{uiDes, uiDes2};
	}

	public abstract String getDesName(); 
	public abstract void excuteImpl(UfoReport report);
}
