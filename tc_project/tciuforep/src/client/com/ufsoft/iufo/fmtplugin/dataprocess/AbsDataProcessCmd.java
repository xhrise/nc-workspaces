package com.ufsoft.iufo.fmtplugin.dataprocess;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ufida.dataset.Context;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaFormatCreator;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessFld;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.IDataProcessType;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.sysplugin.cellattr.SetCellAttrCmd;
import com.ufsoft.report.sysplugin.combinecell.CombineCellCmd;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectModel;

abstract class AbsDataProcessCmd extends UfoCommand {
	private UfoReport _report;
	
	public void execute(Object[] params) {
		setReport((UfoReport) params[0]);
		DynAreaCell dynAreaCell = getDynAreaCellSel(); 
		if(dynAreaCell == null) return;
		Vector<DataProcessFld> vecAllDynAreaDPFlds = DataProcessUtil.getAllDynAreaDPFldVec(getCellsModel(),dynAreaCell);
		if(vecAllDynAreaDPFlds.size() == 0) return;
		
		boolean bDirty = excuteImpl(dynAreaCell,vecAllDynAreaDPFlds);
		
		if(bDirty){
			updateFormat(dynAreaCell,getDataProcessType());
		}
	}
	
	abstract protected int getDataProcessType();
	abstract protected void adjustDynAreaFormat(DynAreaCell dynAreaCell, AreaPosition oldArea);
    abstract protected boolean excuteImpl(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds);

    void updateFormat(DynAreaCell dynAreaCell,int type) {
    	String dynAreaPK = dynAreaCell.getDynAreaVO().getDynamicAreaPK();
        //设置数据区域的颜色
        AreaDataProcess areaDataProcess = getDynAreaModel().getDataProcess(dynAreaPK);//dynAreaVO.getAreaDataProcess();
        if(areaDataProcess != null){
            //分组，交叉表，交叉图表才有区域颜色
            if((type == IDataProcessType.PROCESS_GROUP || type == IDataProcessType.PROCESS_CROSSTABLE ||
                 type == IDataProcessType.PROCESS_CROSS_CHART) && areaDataProcess.isUserDefined()){
                Hashtable hashFormatColor = (Hashtable)areaDataProcess.getFormatColor().clone();
                if(hashFormatColor != null && hashFormatColor.size() >= 2){
                    hashFormatColor.remove(AreaFormatCreator.DPCOLOR_POS_KEY);
                    Enumeration enumFormat = hashFormatColor.keys();
                    while(enumFormat.hasMoreElements()){
                        String strAreaPos = (String)enumFormat.nextElement();
                        Integer nColor = (Integer)hashFormatColor.get(strAreaPos);
                        AreaPosition formatArea = AreaPosition.getInstance(strAreaPos);
                        AreaPosition dynArea = dynAreaCell.getArea();//getDynAreaVO().getOriArea();
                        AreaPosition absoluteFormatArea = AreaPosition.getInstance(
                        		CellPosition.getInstance(
                        				formatArea.getStart().getRow()+dynArea.getStart().getRow(),
                        				formatArea.getStart().getColumn()+dynArea.getStart().getColumn()
                        				
                        		),
                        		CellPosition.getInstance(
                        				formatArea.getEnd().getRow()+dynArea.getStart().getRow(),
                        				formatArea.getEnd().getColumn()+dynArea.getStart().getColumn()
                        		)                        				
                        );
                        CellPosition[] cells = (CellPosition[]) getCellsModel().getSeperateCellPos(absoluteFormatArea).toArray(new CellPosition[0]);
                        Hashtable<Integer,Integer> ht = new Hashtable<Integer,Integer>();
                        ht.put(new Integer(PropertyType.BackColor),new Integer(nColor.intValue()));
                        new SetCellAttrCmd(getReport()).setCellsFormat(cells,ht);
                        //清除这些区域的表样！
                        for (int i = 0; i < cells.length; i++) {
                        	Cell cell = getCellsModel().getCell(cells[i]);
                        	if(cell != null) cell.setValue(null);
						}
                    }
                } 
            } else if(type == IDataProcessType.PROCESS_GROUP_AGGREGEATE && areaDataProcess.isUserDefined()){
            	getCellsModel().setDirty(true);
            }
        }

        //去掉动态区域的原组合区域
        CombineCellCmd.delCombineCell(dynAreaCell.getArea(),//.getDynAreaVO().getOriArea(),
        		getReport().getTable());//utTable.delCombCell(new UfoArea(dynAreaVO.getPos()));
	}

    /**
     * 根据数据处理区域的大小，调整所属动态区域的范围。
     * 创建日期：(2003-9-1 13:30:45)
     * @author：刘良萍
     * @param strDynAreaPos java.lang.String 动态区域的绝对位置
     * @param dataArea com.ufsoft.iuforeport.reporttool.pub.UfoArea 数据处理区域的对应相对区域
     * @param dataArea AreaPosition 动态区域的对应相对区域,dynAreaCell可以计算得到。
     */
    void adjustDynAreaRange(DynAreaCell dynAreaCell, AreaPosition dataArea, AreaPosition dynRelateUfoArea){
        if(dynAreaCell == null || dataArea == null){
            return;
        }

        //动态区域的区域位置
        AreaPosition area = dynAreaCell.getArea();
        
        //计算增加的行列数
        int nRowCount = dataArea.getEnd().getRow() - dynRelateUfoArea.getEnd().getRow();//dataArea.End.Row - dynRelateUfoArea.End.Row;
        int nColCount = dataArea.getEnd().getColumn() - dynRelateUfoArea.getEnd().getColumn();//dataArea.End.Col - dynRelateUfoArea.End.Col;
        if(nRowCount <= 0 && nColCount <= 0){
            return;
        } else{
            //不能减少动态区域的已有的行、列
            if(nRowCount < 0){
                nRowCount = 0;
            }
            if(nColCount < 0){
                nColCount = 0;
            }
        }
//        DynAreaCell[] dynAs = getDynAreaModel().getDynAreaCellByArea(area);//DynamicAreaVO[] dynAs = utTable.getDynAreaVOByArea(area);      
        //#插入行
        getCellsModel().getRowHeaderModel().addHeader(area.getEnd().getRow()+1,nRowCount);//utTable.insertEmptyRow(area.End.Row + 1, nRowCount);
        //#插入列
        getCellsModel().getColumnHeaderModel().addHeader(area.getEnd().getColumn()+1,nColCount);//utTable.insertEmptyCol(area.End.Col + 1, nColCount);
        //#更新动态区域的大小设置
  
//      if(dynAs != null && dynAs.length == 1){
        AreaPosition newArea = AreaPosition.getInstance(
        		area.getStart(),
        		CellPosition.getInstance(area.getEnd().getRow()+nRowCount,area.getEnd().getColumn()+nColCount)
        );//UfoArea newArea = new UfoArea(area.Start, new UfoCell(area.End.Row + nRowCount, area.End.Col + nColCount));
//      dynAreaCell.getDynAreaVO().setOriArea(newArea);
        dynAreaCell.setArea(newArea);
        
        adjustDynAreaFormat(dynAreaCell, area);
//      }
    }
    
	UfoReport getReport() {
		return _report;
	}
	Context getContextVO(){
		return _report.getContextVo();
	}

	void setReport(UfoReport report) {
		_report = report;
	}
	CellsModel getCellsModel(){
		return _report.getCellsModel();
	}
	DynAreaModel getDynAreaModel(){
		return DynAreaModel.getInstance(getCellsModel());
	}
	DynAreaCell getDynAreaCellSel(){
		SelectModel selectModel = getCellsModel().getSelectModel();
		return getDynAreaModel().getDynAreaCellByPos(selectModel.getAnchorCell());
	}
	KeywordModel getKeywordModel(){
		return KeywordModel.getInstance(getCellsModel());
	}
	MeasureModel getMeasureModel(){
		return MeasureModel.getInstance(getCellsModel());
	}
	FormulaModel getFormulaModel(){
		return FormulaModel.getInstance(getCellsModel());
	}
}
