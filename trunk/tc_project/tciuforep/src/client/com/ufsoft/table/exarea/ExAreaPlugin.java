package com.ufsoft.table.exarea;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.insertdelete.DeleteCmd;
import com.ufsoft.report.sysplugin.insertdelete.InsertCellCmd;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;

/**
 * 可扩展区域定义插件
 * @author liuyy
 * @2008-04-21
 *
 */
public class ExAreaPlugin extends AbstractPlugIn implements UserActionListner,
		HeaderModelListener {

	public void startup() {
		UFOTable ufoTable = getReport().getTable();
		ufoTable.getReanderAndEditor().registRender(ExAreaCell.class,
				new ExAreaRender("iufo_fmt_exarea"));

		// 定义拖拽支持
		//		AdhocCellsPaneDnD dnd2 = new AdhocCellsPaneDnD(this.getReport().getTable().getCells(), this);
		//		ExAreaMouseActionHandler hander = new ExAreaMouseActionHandler(this.getReport().getTable()
		//				.getCells(), null);
		//		this.getReport().getTable().getCells().getUI().setCellsPaneUIMouseActionHandler(hander);

		//		this.getReport().getTable().getCells().addMouseListener(hander);
		//		this.getReport().getTable().getCells().addMouseMotionListener(hander);

		//要保证右键事件最后响应,与状态有关
		//		this.getReport().resetGlobalPopMenuSupport();

	}

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			protected IExtension[] createExtensions() {
				return new IExtension[] {
						new ExAreaExt((ExAreaPlugin) getPlugin()),
						new ExAreaDeleteExt((ExAreaPlugin) getPlugin()),
						new ExAreaCombineExt((ExAreaPlugin) getPlugin()),
						new ExAreaSeperateByRowExt((ExAreaPlugin) getPlugin()),
						new ExAreaSeperateByColExt((ExAreaPlugin) getPlugin()), 
						new ExAreaMngExt((ExAreaPlugin) getPlugin()),
				};
			}
		};
	}

	public ExAreaModel getExAreaModel() {
		return ExAreaModel.getInstance(getCellsModel());
	}

//	private ExAreaComponent exareaComp = null;
//	public SheetCellRenderer getDataRender(String extFmtName) {
//		return new SheetCellRenderer() {
//			//			
//			//			Color lineColor = new Color(49, 106, 197); //线条颜色
//			//			Color fillColor = new Color(254, 254, 254);//填充颜色
//			////			int LineBorderWidth = 2;//线条周围的空白宽度
//
//			public Component getCellRendererComponent(
//					final CellsPane cellsPane,  Object value,
//					boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
// 
//				if (value == null) {
//					return null;
//				}
//				final ExAreaCell exCell = (ExAreaCell) value;
//				JComponent comp = new ExAreaComponent(exCell, cellsPane);
//				return comp;
//
//			}
//		};
//
//	}

	public void userActionPerformed(UserUIEvent e) {
			    switch (e.getEventType()){
			    case UserUIEvent.PASTE:
			    	preccessPaste(e);
			    	
			    	break;
			          
			    }
	}
	
	private void preccessPaste(UserUIEvent e){
		EditParameter p = getEditParameter(e);
    	AreaPosition[] newAreas = p.getPasteAreas();
		AreaPosition oldArea = p.getCopyArea();
    	ExAreaCell[] copyExcells = getExAreaModel().getContainExCells(oldArea);
    	for(AreaPosition newArea: newAreas){
    		if(newArea.equals(oldArea))//modify by wangyga 2008-10-20 添加判断区域是否相同
    			continue;
    		//如果复制中包含可扩展区，且粘贴区域存在可扩展区，执行清除操作。
    		if(copyExcells != null && copyExcells.length > 0){
    			ExAreaCell[] exCellsInNewArea = getExAreaModel().getIntersectionExCells(newArea);
    			if(exCellsInNewArea.length > 0){
    				getExAreaModel().removeExArea(exCellsInNewArea);
    				String error = exCellsInNewArea[0].fireUIEvent(ExAreaModelListener.REMOVE, exCellsInNewArea[0], exCellsInNewArea[0]);
    				if(error != null && error.length() > 1){
    					UfoPublic.showErrorDialog(getReport(), error, MultiLang.getString("miufo00082")); 
    					return;
    				} 
    				getReport().updateUI();
    			}
    			
    		}
    		
    		//区域处理
    		for(ExAreaCell exCell: copyExcells){
    			AreaPosition oldExArea = exCell.getArea();
    			AreaPosition newExArea = (AreaPosition) oldExArea.getMoveArea(oldArea.getStart(), newArea.getStart());
    			
    			if(p.getEditType() == EditParameter.COPY || p.getEditType() == EditParameter.BRUSH){
    				ExAreaCell exCell2 = (ExAreaCell) exCell.clone();
//    				if(exCell2.getExAreaName() != null){
    					exCell2.setExAreaName(null);//exCell2.getExAreaName() + "_copy");
//    				}
    				exCell2.setArea(newExArea);
    				getExAreaModel().addExArea(exCell2);
    				//editPlugin已经作了cells的复制处理，此处不用再作处理。
    				 
    			} else if(p.getEditType() == EditParameter.CUT){   				
    				//EditPlugin已处理移动单元格业务，此处不用处理。只需改变区域。
    				exCell.fireUIEvent(ExAreaModelListener.CHANGE_POS, null, p);
    				exCell.setArea(newExArea);				
    			}
    			
    		}
    	}
    	
	}
	

	/**
	 * @i18n miufo00130=组合区域不能与可扩展区域交叉。
	 * @i18n miufo00131=可扩展区域不支持转置
	 */
	public String isSupport(int source, EventObject e)
			throws ForbidedOprException {
		if (e instanceof UserUIEvent) {
			UserUIEvent ee = (UserUIEvent) e;
			switch (ee.getEventType()) {
			case UserUIEvent.INSERTCELL:
			    return isSupportInsertCell(ee);
			case UserUIEvent.DELETECELL:
				return isSupportDeleteCell(ee);
			case UserUIEvent.COMBINECELL:
				AreaPosition ccArea = (AreaPosition) ee.getOldValue();
				ExAreaCell[] cells = getExAreaModel().getExAreaCells();
				for (int i = 0; i < cells.length; i++) {
					AreaPosition area2 = cells[i].getArea();
					if (area2.intersection(ccArea) && !area2.contain(ccArea)) {
						throw new ForbidedOprException(MultiLang.getString("miufo00130"));
					}
				}
				break;
				
			case UserUIEvent.CUT:	
			case UserUIEvent.COPY:
				EditParameter p = getEditParameter(ee);
				if(p == null || p.getCopyArea() == null){
					return null;
				}
				AreaPosition operArea = p.getCopyArea();
				validateSelectedArea(operArea);
				 
				break;
				
			case UserUIEvent.PASTE:	
				EditParameter ep = getEditParameter(ee);
			
				//end	
				AreaPosition[] newAreas = ep.getPasteAreas();
				if (newAreas == null || newAreas.length == 0) {
					return null;
				}
				
				for(AreaPosition newarea: newAreas){
					validateSelectedArea(newarea);
				}
				
				ExAreaCell[] copyCells = getExAreaModel().getContainExCells(ep.getCopyArea());
				if(copyCells != null && copyCells.length > 0){
					if(ep.isTransfer()){
						throw new ForbidedOprException(MultiLang.getString("miufo00131"));
					}
				}
//				
//				ExAreaCell copyCell = getExAreaModel().getExArea(ep.getCopyArea());
//				if(copyCell != null && !copyCell.getArea().equals(ep.getCopyArea())){
//					copyCell = null;
//				}
				break;
			default:
				break;
			}
		}
			
		return null;
	}

	/**
	 * @i18n miufo00132=不能对可扩展区域作部分更改。
	 */
	private void validateSelectedArea(AreaPosition operArea)
			throws ForbidedOprException {
		ExAreaCell[] exCells = getExAreaModel().getExAreaCells();
		for(ExAreaCell ex: exCells){
			//交叠但不包含
			AreaPosition exArea = ex.getArea();
			if(!exArea.intersection(operArea)){
				continue;
			}
			if(operArea.contain(exArea) || exArea.contain(operArea)){   
				continue;
			}
			throw new ForbidedOprException(MultiLang.getString("miufo00132"));
		}
	}
	
	//交叠但不包含
	private boolean validatePasteArea(ExAreaCell copyCell, AreaPosition pasteArea){
		AreaPosition pa2 = null;
		if(copyCell != null){
			pa2 = (AreaPosition) copyCell.getArea().getMoveArea(copyCell.getArea().getStart(), pasteArea.getStart());
		}
		
		ExAreaCell[] exCells = getExAreaModel().getExAreaCells();
		for(ExAreaCell ex: exCells){
			//交叠但不包含
			AreaPosition exArea = ex.getArea();
			if(!exArea.intersection(pasteArea)){
				continue;
			}
			
			if(pasteArea.contain(exArea)){  //   || exArea.contain(area)
				continue;
			}
			return false;
		}
		return true;
	}
	

	private EditParameter getEditParameter(UserUIEvent ee){
		
		EditParameter parameter = (EditParameter) ee.getNewValue();

		return parameter;
		
	}
	
	/**
	 * @i18n miufo00133=删除区域中含有可扩展区，不允许删除。
	 * @i18n miufo00134=移动区域中含有可扩展区，不允许删除。
	 */
	private String isSupportDeleteCell(UserUIEvent ee)
			throws ForbidedOprException {
		int deleteType = ((Integer) (ee.getOldValue())).intValue();
		ExAreaCell[] exCells = null;
		AreaPosition aimArea = (AreaPosition) ee.getNewValue();
		exCells = getExAreaModel().getIntersectionExCells(aimArea);
		if (exCells != null && exCells.length != 0) {
			throw new ForbidedOprException(MultiLang.getString("miufo00133"));
		}
		AreaPosition toMoveArea = DeleteCmd.getToMoveArea(aimArea, deleteType,
				getReport().getCellsModel());
		exCells = getExAreaModel().getIntersectionExCells(toMoveArea);
		if (exCells != null && exCells.length != 0) {
			throw new ForbidedOprException(MultiLang.getString("miufo00134"));
		}
		return null;
	}
	
    /**
	 * @i18n miufo00135=插入方向上不能存在可扩展区。
	 */
    private String isSupportInsertCell(UserUIEvent ee) throws ForbidedOprException {
    	int insertType = ((Integer)(ee.getOldValue())).intValue();
    	AreaPosition aimArea = (AreaPosition) ee.getNewValue();
    	
    	AreaPosition extendArea = InsertCellCmd.getToMoveArea(aimArea,insertType,getReport().getCellsModel());
    	ExAreaCell[] exCells = getExAreaModel().getIntersectionExCells(extendArea);
    	if(exCells != null && exCells.length != 0){
    		throw new ForbidedOprException(MultiLang.getString("miufo00135"));
    	}
    	
//    	if(!aimArea.getStart().equals(aimArea.getEnd())){//modify by wangyga 去掉此判断
//    		throw new ForbidedOprException(StringResource.getStringResource("miufo1001749"));
//    	}
    	
    	return null;
    }

	public int getPriority() {
		return 0;
	}

	public void headerCountChanged(HeaderEvent e) {
//		if (e.isHeaderAdd()) {
//			return;
//		}
		//可扩展区域的删除实际已在CellsModel的事件响应中处理完成，需要更新ExAreaModel缓存
		getExAreaModel().resetCache();
		
	}

	public void headerPropertyChanged(PropertyChangeEvent e) {

	}

}
 