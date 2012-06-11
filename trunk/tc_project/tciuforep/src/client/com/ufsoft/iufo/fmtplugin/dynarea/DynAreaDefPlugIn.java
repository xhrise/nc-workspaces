/*
 * DynAreaPlugIn.java
 * 创建日期 2004-11-26
 * Created by CaiJie
 */
package com.ufsoft.iufo.fmtplugin.dynarea;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import javax.swing.JLabel;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.service.DataProcessSrv;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.inputplugin.dynarea.RowNumber;
import com.ufsoft.iufo.inputplugin.dynarea.RowNumberRender;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.insertdelete.DeleteCmd;
import com.ufsoft.report.sysplugin.insertdelete.DeleteInsertDialog;
import com.ufsoft.report.sysplugin.insertdelete.InsertCellCmd;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * @author caijie 
 * @since 3.1
 */
public class DynAreaDefPlugIn  extends AbstractPlugIn implements UserActionListner, HeaderModelListener, IUfoContextKey{
	public static final String MENU_GROUP = "dynArea";
	public static final String EXT_FMT_DYNAMICAREA = "iufo_fmt_dynamicarea";
	
	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new DynAreaDefRender());
//		CellRenderAndEditor.getInstance().registRender(RowNumber.class,new SheetCellRenderer(){
//        	/**
//			 * @i18n uiiufofmt00003=自动编号
//			 */
//        	JLabel label = new nc.ui.pub.beans.UILabel(StringResource.getStringResource("uiiufofmt00003"));
//			public Component getCellRendererComponent(CellsPane cellsPane, Object value, boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
//				//edit by wangyga 此版暂时这样处理
//		     	if(cellsPane.getOperationState() == ReportContextKey.OPERATION_INPUT){
//					return null;
//				}
//				return label;
//			}        	
//        });
		
		CellRenderAndEditor.registRender(RowNumber.class,new RowNumberRender());
	}
	
    /*
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup() {
    	  getReport().getEventManager().addListener(this);
//        UFOTable ufoTable = getReport().getTable();
//        ufoTable.getReanderAndEditor().registRender(DynAreaVO.class, getDataRender(EXT_FMT_DYNAMICAREA));
//        ufoTable.getCells().registExtSheetRenderer(getDynDirectRender());
//        ufoTable.getReanderAndEditor().registRender(RowNumber.class,new SheetCellRenderer(){
//        	/**
//			 * @i18n uiiufofmt00003=自动编号
//			 */
//        	JLabel label = new nc.ui.pub.beans.UILabel(StringResource.getStringResource("uiiufofmt00003"));
//			public Component getCellRendererComponent(CellsPane cellsPane, Object value, boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
//				return label;
//			}        	
//        });
    }


    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return new DynAreaDescriptor(this);
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.Class)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
//        return new DynAreaRender(m_dynAreaModel.getDynAreaDirection());
        
    	return new SheetCellRenderer(){
            Color lineColor = new Color(153,153,255); //线条颜色
            Color fillColor = Color.BLUE;//填充颜色
            int LineBorderWidth = 2;//线条周围的空白宽度
             
       
			public Component getCellRendererComponent(CellsPane cellsPane,  Object value,
			        boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {




				if(getDynAreaModel().getDynAreaDirection() == DynAreaVO.DIRECTION_COL){
				    return DynAreaPublic.createHorizontalArrows(new Color(153,153,255), 2);
				}else if(getDynAreaModel().getDynAreaDirection() == DynAreaVO.DIRECTION_ROW){
				    return DynAreaPublic.createVerticvalArrows(new Color(153,153,255), 2);
				}else{
				    return null;
				}				    
			}
        };
   
    }

    
    /**
     * @return 返回 DynAreaModel。
     */
    public DynAreaModel getDynAreaModel() {
        return DynAreaModel.getInstance(getCellsModel());
    }
    
    /* （非 Javadoc）
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e) throws ForbidedOprException {    
    	if(e instanceof UserUIEvent){
    		UserUIEvent ee = (UserUIEvent)e;
    		if(ee.getEventType() == UserUIEvent.INSERTCELL){
    			return isSupportInsertCell(ee);
    		} else if(ee.getEventType() == UserUIEvent.DELETECELL){
    			return isSupportDeleteCell(ee);
    		} else if(ee.getEventType() == UserUIEvent.COMBINECELL){
    			return isSupportCombineCell(ee);
    		} 
    	} 

    	if(DataProcessSrv.isCheckDelHeader() && e instanceof HeaderEvent){
    		HeaderEvent ee = (HeaderEvent)e;
    		if(!ee.isHeaderAdd() && ee.isRow()){
    			return isSupportDeleteRow(ee);
    		} else if(!ee.isHeaderAdd() && !ee.isRow()){
    			return isSupportDeleteColumn(ee);
    		}else if(ee.isHeaderAdd()){
    			return isSupportAddRowOrCol(ee);
    		}
    		
    	}
    	return null;
    }

    /**
     * @param ee
     * @return
     * @throws ForbidedOprException
     * @i18n uiiufofmt00038=删除的区域中含有动态区，不允许删除！
     */
    private String isSupportDeleteCell(UserUIEvent ee) throws ForbidedOprException {
    	int deleteType = ((Integer)(ee.getOldValue())).intValue();
    	DynAreaCell[] dynAreaCells=null;
    	AreaPosition aimArea = (AreaPosition) ee.getNewValue();
    	dynAreaCells=getDynAreaModel().getDynAreaCellByArea(aimArea);
    	if(dynAreaCells != null && dynAreaCells.length != 0){
    		throw new ForbidedOprException(StringResource.getStringResource("uiiufofmt00038"));
    	}
    	AreaPosition toMoveArea = DeleteCmd.getToMoveArea(aimArea,deleteType,getReport().getCellsModel());
    	dynAreaCells= getDynAreaModel().getDynAreaCellByArea(toMoveArea);
    	if(dynAreaCells != null && dynAreaCells.length != 0){
    		throw new ForbidedOprException(StringResource.getStringResource("uiiufofmt00072"));
    	}
//    	dynAreaCells = getDynAreaModel().getDynAreaCellByArea(aimArea);
//    	if(dynAreaCells != null && dynAreaCells.length != 0){
//    		throw new ForbidedOprException(StringResource.getStringResource("uiiufofmt00038"));
//    	}
    	
    	return null;
    }

    /**
     * 插入单元扩展方向上不能存在动态区域
     * @param ee
     * @return
     * @throws ForbidedOprException
     */
    private String isSupportInsertCell(UserUIEvent ee) throws ForbidedOprException {
    	int insertType = ((Integer)(ee.getOldValue())).intValue();
    	AreaPosition aimArea = (AreaPosition) ee.getNewValue();
    	
    	AreaPosition extendArea = InsertCellCmd.getToMoveArea(aimArea,insertType,getReport().getCellsModel());
    	DynAreaCell[] dynCells = getDynAreaModel().getDynAreaCellByArea(extendArea);
    	if(dynCells != null && dynCells.length != 0){
    		throw new ForbidedOprException(StringResource.getStringResource("miufo1001749"));
    	}
    	
//    	if(!aimArea.getStart().equals(aimArea.getEnd())){//modify by wangyga 此判断不合适
//    		throw new ForbidedOprException(StringResource.getStringResource("miufo1001749"));
//    	}
    	
    	return null;
    }

    /**
     * 组合单元区域不能跨过动态区域,并且不能穿过行动态区所在的行,不能穿过列动态区的列.
     * @param event
     * @return String
     * @throws ForbidedOprException
     */
    private String isSupportCombineCell(UserUIEvent event) throws ForbidedOprException {
    	AreaPosition ccArea = (AreaPosition) event.getOldValue();
    	AreaPosition extArea;
    	int direction = getDynAreaModel().getDynAreaDirection();
    	if(direction == DynAreaVO.DIRECTION_UNDEFINED){
    		return null;
    	}else if(direction == DynAreaVO.DIRECTION_ROW){
    		int width = getReport().getCellsModel().getColNum();
    		extArea = AreaPosition.getInstance(ccArea.getStart().getRow(),0,width,ccArea.getHeigth());
    	}else if(direction == DynAreaVO.DIRECTION_COL){
    		int height = getReport().getCellsModel().getRowNum();
    		extArea = AreaPosition.getInstance(0,ccArea.getStart().getColumn(),ccArea.getWidth(),height);
    	}else{
    		throw new IllegalArgumentException();
    	}
    	DynAreaCell[] dynCells = getDynAreaModel().getDynAreaCellByArea(extArea);
    	if(dynCells == null || dynCells.length == 0){
    		return null;
    	}
    	DynAreaCell dynCell = dynCells[0];
    	if(dynCell.getArea().contain(ccArea)){
    		return null;
    	}else if(dynCell.getArea().intersection(ccArea)){
    		throw new ForbidedOprException(StringResource.getStringResource("miufo1001829"));//"组合单元不能同时包含固定区域和动态区域"
    	}else{
    		//本句话准确描述应该为"动态区域延伸区域不能设置组合单元",因为动态区域只要求其扩展方向上不存在组合单元.
    		throw new ForbidedOprException(StringResource.getStringResource("miufo1001831"));//"区域所在行或列存在动态区域，不能设置组合单元"
    	}
    }
    private String isSupportAddRowOrCol(HeaderEvent ee) throws ForbidedOprException {
    	int startIndex = ee.getStartIndex();
    	int count = ee.getCount();
    	AreaPosition aimArea=null;
    	if(ee.isRow()){
    		aimArea = AreaPosition.getInstance(startIndex, 0, getCellsModel().getColNum(), count);// -1 liuyy 2008-12-1
    	}else{
    		 aimArea = AreaPosition.getInstance(0, startIndex, count, getCellsModel().getRowNum());// -1
    	}
    	DynAreaCell[] dynAreaCells = getDynAreaModel().getDynAreaCellByArea(aimArea);
    	if(dynAreaCells != null && dynAreaCells.length != 0){
    		throw new ForbidedOprException(StringResource.getStringResource("miufo1001749"));
    	}
    	return null;
    }
    /**
     * @param ee
     * @return
     * @throws ForbidedOprException
     * @i18n uiiufofmt00038=删除的区域中含有动态区，不允许删除！
     * modify by guogang 2007-12-7 限制破坏动态区完整性的动作
     */
    private String isSupportDeleteRow(HeaderEvent ee) throws ForbidedOprException {
    	int startIndex = ee.getStartIndex();
    	int count = ee.getCount();
    	AreaPosition aimArea = AreaPosition.getInstance(startIndex, 0, getCellsModel().getColNum(), count);// -1 liuyy 2008-12-1
    	DynAreaCell[] dynAreaCells = getDynAreaModel().getDynAreaCellByArea(aimArea);
    	if(dynAreaCells != null && dynAreaCells.length != 0){
    		for(int i=0;i<dynAreaCells.length;i++){
    			if(dynAreaCells[i].getDirection()==DynAreaVO.DIRECTION_COL){
    				throw new ForbidedOprException(StringResource.getStringResource("miufo1001743"));
    			}
    		}
    		
    	}
//    	dynAreaCells = getDynAreaModel().getDynAreaCellByArea(aimArea);
//    	if(dynAreaCells != null && dynAreaCells.length != 0){
//    		throw new ForbidedOprException(StringResource.getStringResource("uiiufofmt00038"));
//    	}
    	return null;
    }

    /**
     * @param ee
     * @return
     * @throws ForbidedOprException
     * @i18n uiiufofmt00038=删除的区域中含有动态区，不允许删除！
     * modify by guogang 2007-11-13 去掉删除动态区的限制
     */
    private String isSupportDeleteColumn(HeaderEvent ee) throws ForbidedOprException {
    	int startIndex = ee.getStartIndex();
    	int count = ee.getCount();
    	AreaPosition aimArea = AreaPosition.getInstance(0, startIndex, count, getCellsModel().getRowNum());// -1
    	DynAreaCell[] dynAreaCells = getDynAreaModel().getDynAreaCellByArea(aimArea);
    	if(dynAreaCells != null && dynAreaCells.length != 0){
    		for(int i=0;i<dynAreaCells.length;i++){
    			if(dynAreaCells[i].getDirection()==DynAreaVO.DIRECTION_ROW){
    				throw new ForbidedOprException(StringResource.getStringResource("miufo1001743"));
    			}
    		}
    	}
//    	dynAreaCells = getDynAreaModel().getDynAreaCellByArea(aimArea);
//    	if(dynAreaCells != null && dynAreaCells.length != 0){
//    		throw new ForbidedOprException(StringResource.getStringResource("uiiufofmt00038"));
//    	}
    	return null;
    }

    public void userActionPerformed(UserUIEvent e) {
    	//处理事件。
	    switch (e.getEventType()){
	        //删除区域包含关键字不允许删除
	        case UserUIEvent.DELETECELL:
	    		processDeleteCellEvent(e);
	    		break;
	    	case UserUIEvent.INSERTCELL:
	    		processInsertCellEvent(e);
	    		break;
	    }
    }


    public int getPriority() {
    	return HeaderModelListener.NORM_PRIORITY;
    }
    /**
     * 由单元格插入引起的动态区修改
     * add by guogang 2007-11-14
     * @param e
     */
    private void processInsertCellEvent(UserUIEvent e) {
    	
    	int insertType = ((Integer)(e.getOldValue())).intValue();
    	AreaPosition aimArea = (AreaPosition) e.getNewValue();
    	AreaPosition extendArea = InsertCellCmd.getToMoveArea(aimArea,insertType,getReport().getCellsModel());
    	DynAreaCell[] dynCells = getDynAreaModel().getDynAreaCellByArea(extendArea);
    	AreaPosition area=null;
    	AreaPosition newArea=null;
    	int dynDirection=-1;
    	
    	if(dynCells!=null){
    	for(int i=0;i<dynCells.length;i++){
    		area=dynCells[i].getDynAreaVO().getOriArea();
    		dynDirection=dynCells[i].getDynAreaVO().getDirection();
    		if(dynDirection==DynAreaVO.DIRECTION_COL){
    		if (insertType == DeleteInsertDialog.CELL_MOVE_DOWN) {
    			//列动态区行扩展
    			if(aimArea.getStart().getRow()>=area.getStart().getRow()){
    				//在动态区上
    			newArea=AreaPosition.getInstance(area.getStart().getRow(),
						area.getStart().getColumn(),area.getWidth(),area.getHeigth()+1);
    			}else{
    				newArea=AreaPosition.getInstance(area.getStart().getRow()+1,
    						area.getStart().getColumn(),area.getWidth(),area.getHeigth());	
    			}
    		} else if (insertType == DeleteInsertDialog.CELL_MOVE_RIGHT) {
    			//列动态区行缩小
    			if(aimArea.getStart().getRow()>=area.getStart().getRow()){
    				//在动态区上
        			newArea=AreaPosition.getInstance(area.getStart().getRow(),
    						area.getStart().getColumn(),area.getWidth(),area.getHeigth()-1);
        			}else{
        				newArea=area;	
        			}
    		}
    		}else if(dynDirection==DynAreaVO.DIRECTION_ROW){
    			if (insertType == DeleteInsertDialog.CELL_MOVE_DOWN) {
        			//行动态区缩小
    				if(aimArea.getStart().getColumn()>=area.getStart().getColumn()){
        				//在动态区上
            			newArea=AreaPosition.getInstance(area.getStart().getRow(),
        						area.getStart().getColumn(),area.getWidth()-1,area.getHeigth());
            			}else{
            				newArea=area;	
            			}
        		} else if (insertType == DeleteInsertDialog.CELL_MOVE_RIGHT) {
        			//行动态区扩展
        			if(aimArea.getStart().getColumn()>=area.getStart().getColumn()){
        				//在动态区上
        			newArea=AreaPosition.getInstance(area.getStart().getRow(),
    						area.getStart().getColumn(),area.getWidth()+1,area.getHeigth());
        			}else{
        				newArea=AreaPosition.getInstance(area.getStart().getRow(),
        						area.getStart().getColumn()+1,area.getWidth(),area.getHeigth());	
        			}
        		}
    		}
    		dynCells[i].getDynAreaVO().setOriArea(newArea);
    		dynCells[i].setArea(newArea);
    	}
		
    	}
	}
    /**
     * 由单元格删除引起的动态区修改
     * add by guogang 2007-11-14
     * @param e
     */
	private void processDeleteCellEvent(UserUIEvent e) {
		
		int deleteType = ((Integer)(e.getOldValue())).intValue();
    	AreaPosition aimArea = (AreaPosition) e.getNewValue();
    	DynAreaCell[] dynCells = getDynAreaModel().getDynAreaCellByArea(aimArea);
    	AreaPosition area=null;
    	AreaPosition newArea=null;
    	int dynDirection=-1;
    	
    	if(dynCells!=null){
    	for(int i=0;i<dynCells.length;i++){
    		area=dynCells[i].getDynAreaVO().getOriArea();
    		dynDirection=dynCells[i].getDynAreaVO().getDirection();
    		if(dynDirection==DynAreaVO.DIRECTION_COL){
    			//列动态区行缩小
    			if(aimArea.getStart().getRow()>=area.getStart().getRow()&&aimArea.getStart().getRow()<=area.getEnd().getRow()){
    				//在动态区上
        			newArea=AreaPosition.getInstance(area.getStart().getRow(),
    						area.getStart().getColumn(),area.getWidth(),area.getHeigth()-1);
        			}else if(aimArea.getStart().getRow()<area.getStart().getRow()){
        				newArea=AreaPosition.getInstance(area.getStart().getRow()-1,
        						area.getStart().getColumn(),area.getWidth(),area.getHeigth());
        			}
    		}else if(dynDirection==DynAreaVO.DIRECTION_ROW){
        			//行动态区缩小
    				if(aimArea.getStart().getColumn()>=area.getStart().getColumn()&&aimArea.getStart().getColumn()<=area.getEnd().getColumn()){
        				//在动态区上
            			newArea=AreaPosition.getInstance(area.getStart().getRow(),
        						area.getStart().getColumn(),area.getWidth()-1,area.getHeigth());
            			}else if(aimArea.getStart().getColumn()<area.getStart().getColumn()){
            				newArea=AreaPosition.getInstance(area.getStart().getRow(),
            						area.getStart().getColumn()-1,area.getWidth(),area.getHeigth());
            			}
    		}
    		if(newArea!=null){
    		dynCells[i].getDynAreaVO().setOriArea(newArea);
    		dynCells[i].setArea(newArea);
    		}
    		newArea=null;
    	}
		
    	}
	}
	
    public void headerCountChanged(HeaderEvent e){
//  	TODO 处理动态区设计区域大小变化.

    	DynAreaCell[] dynCells=getDynAreaModel().getDynAreaCells();
    	if(dynCells==null || dynCells.length==0)
    		return;

    	//对于删除动态区分支，此处不处理，因为已在cellsModel中处理过。

    	AreaPosition area=null;
    	AreaPosition newArea=null;
    	for(int i=0,size=dynCells.length;i<size;i++){
    		area=dynCells[i].getDynAreaVO().getOriArea();
    		if(e.isRow()){
    			if(e.getStartIndex()>area.getEnd().getRow()){
    				continue;
    			}
    			if(e.isHeaderAdd()){
    				//插行
    				if(area.getStart().getRow()>=e.getStartIndex())
    					newArea=AreaPosition.getInstance(area.getStart().getRow()+e.getCount(),
    							area.getStart().getColumn(),area.getWidth(),area.getHeigth());
    				else
    					newArea=AreaPosition.getInstance(area.getStart().getRow(), area.getStart().getColumn(),
    							area.getWidth(), area.getHeigth()+e.getCount());
    			}
    			else{
    				//删行
    				if(area.getStart().getRow()==e.getStartIndex()){
    					if(e.getCount()>=area.getHeigth()){
    						//删除动态区
    					}else{
    						newArea=AreaPosition.getInstance(area.getStart().getRow(),area.getStart().getColumn(),
    								area.getWidth(),area.getHeigth()-e.getCount());
    					}
    				}else if(area.getStart().getRow()<e.getStartIndex()){
    					newArea=AreaPosition.getInstance(area.getStart().getRow(),area.getStart().getColumn(),
    							area.getWidth(),area.getHeigth()-Math.min(e.getCount(),(area.getEnd().getRow()-e.getStartIndex()+1)));
    				}else{
    					if(area.getEnd().getRow()<e.getStartIndex()+e.getCount()){
    						//删除动态区
    					}else{
    						if(area.getStart().getRow()>=e.getStartIndex()+e.getCount())
							   newArea=AreaPosition.getInstance(area.getStart().getRow() - e.getCount(), area.getStart().getColumn(), area.getWidth(), 
    									area.getHeigth());
    						else
    							newArea=AreaPosition.getInstance(e.getStartIndex(), area.getStart().getColumn(), area.getWidth(), 
    									area.getEnd().getRow()-e.getStartIndex()-e.getCount()+1);
    					}
    				}

    			}
    		}else{
    			//列
    			if(e.getStartIndex()>area.getEnd().getColumn()){
    				continue;
    			}
    			if(e.isHeaderAdd()){
    				//插列
    				if(area.getStart().getColumn()>=e.getStartIndex())
    					newArea=AreaPosition.getInstance(area.getStart().getRow(),
    							area.getStart().getColumn()+e.getCount(),area.getWidth(),area.getHeigth());
    				else
    					newArea=AreaPosition.getInstance(area.getStart().getRow(), area.getStart().getColumn(),
    							area.getWidth()+e.getCount(), area.getHeigth());
    			}
    			else{
    				//删列
    				if(area.getStart().getColumn()==e.getStartIndex()){
    					if(e.getCount()>=area.getWidth()){
    						//删除动态区
    					}else{
    						newArea=AreaPosition.getInstance(area.getStart().getRow(),area.getStart().getColumn(),
    								area.getWidth()-e.getCount(),area.getHeigth());
    					}
    				}else if(area.getStart().getColumn()<e.getStartIndex()){
    					newArea=AreaPosition.getInstance(area.getStart().getRow(),area.getStart().getColumn(),
    							area.getWidth()-Math.min(e.getCount(),(area.getEnd().getColumn()-e.getStartIndex()+1)),area.getHeigth());
    				}else{
    					if(area.getEnd().getColumn()<e.getStartIndex()+e.getCount()){
    						//删除动态区
    					}else{
    						if(area.getStart().getColumn()>=e.getStartIndex()+e.getCount()){
    							//整个区域左移
    							newArea=AreaPosition.getInstance(area.getStart().getRow(),e.getStartIndex(), 
    									area.getWidth(), 
    									area.getHeigth());
    						}else
    							//
    							newArea=AreaPosition.getInstance(area.getStart().getRow(),e.getStartIndex(), 
    									area.getEnd().getColumn()-e.getStartIndex()-e.getCount()+1, 
    									area.getHeigth());
    					}
    				}

    			}
    		}


    		//更改动态取原始区域大小
    		if(newArea!=null){
    			dynCells[i].getDynAreaVO().setOriArea(newArea);
    		}else{
    			String strRepId = getReport().getContextVo().getAttribute(REPORT_PK) == null ? null : (String)getReport().getContextVo().getAttribute(REPORT_PK);
    			
    			getDynAreaModel().removeDynArea(strRepId, dynCells[i].getDynAreaVO().getDynamicAreaPK());
    		}
    		newArea=null;
    	}
    }


    public void headerPropertyChanged(PropertyChangeEvent e) {
    }

}
 