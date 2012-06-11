package com.ufsoft.report.sysplugin.fill;

import java.awt.Container;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.format.Format;
/**
 * <pre>填充区域命令
 * </pre>
 * @author zzl
 * @version 5.0
 * Create on 2004-11-2
 */
public class FillCmd extends UfoCommand {
	/**填充方向*/
	static final public int FillToUp = 0;  
	/**填充方向*/
	static final public int FillToDown = 1;
	/**填充方向*/
	static final public int FillToLeft = 2;
	/**填充方向*/
	static final public int FillToRight = 3; 

	private UfoReport m_report = null;
	private CellsModel m_cm = null;
    private UFOTable m_table;
	
	/**
	 * @param report 
	 * 
	 */
	public FillCmd(UfoReport report) {
		super();
		m_report = report;
		m_cm = m_report.getCellsModel();
        m_table = m_report.getTable();
	}
	/* @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {
		AreaPosition area = (AreaPosition)params[0]; 
		int fillTo = ((Integer)params[1]).intValue();		
		FillOptionDlg dlg = new FillOptionDlg(m_report);
		dlg.setVisible(true);		
		if(dlg.getResult() != UfoDialog.ID_OK){	
			return;
		}	
		boolean isSequenceFill = dlg.isSequenceFill();
		boolean isFillFormat = dlg.isFillFormat();
				
		//先检查填充方向上是否相同组合单元或都不是组合单元。
		if(!isRuleArea(area,fillTo)){
			UfoPublic.sendWarningMessage(MultiLang.getString("miuforep0000501"),m_report);
			return;
		}    	
		//包装填充事件.
		UFOTable ufoTable = m_report.getTable();
		UserUIEvent event = new UserUIEvent(ufoTable,UserUIEvent.FILL,new Integer(fillTo),area);//填充子类型暂时放到oldvale里吧
		if (!ufoTable.checkEvent(event)) return;
		ufoTable.fireEvent(event);
		fillExcute(area,fillTo,isSequenceFill,isFillFormat);
	}
	/**
	 * 执行填充操作.
	 * @param areas
	 * @param fillTo
	 * @param isFillFormat 
	 * @param isSequenceFill 
	 */
	private void fillExcute(AreaPosition area, int fillTo, boolean isSequenceFill, boolean isFillFormat){	    
		FillPluginFormat format = new FillPluginFormat(isSequenceFill);
			int startRow = area.getStart().getRow();
		int startCol = area.getStart().getColumn();
		int endRow = area.getEnd().getRow();
		int endCol = area.getEnd().getColumn();
		CellPosition firPos;// 填充区域中的一条区域的首单元格。
		switch (fillTo) {
		case FillToUp:
			for (int dCol = 0; dCol < area.getWidth(); dCol++) {
				firPos = CellPosition.getInstance(endRow, startCol + dCol);
				format.setPosChange(-1, 0);
				fillExcuteOnePiece(area, firPos, format,isFillFormat);
			}
			break;
		case FillToDown:
			for (int dCol = 0; dCol < area.getWidth(); dCol++) {
				firPos = CellPosition.getInstance(startRow, startCol + dCol);
				format.setPosChange(1, 0);
				fillExcuteOnePiece(area, firPos, format,isFillFormat);
			}
			break;
		case FillToLeft:
			for (int dRow = 0; dRow < area.getHeigth(); dRow++) {
				firPos = CellPosition.getInstance(startRow + dRow, endCol);
				format.setPosChange(0, -1);
				fillExcuteOnePiece(area, firPos, format,isFillFormat);
			}
			break;
		case FillToRight:
			for (int dRow = 0; dRow < area.getHeigth(); dRow++) {
				firPos = CellPosition.getInstance(startRow + dRow, startCol);
				format.setPosChange(0, 1);
				fillExcuteOnePiece(area, firPos, format,isFillFormat);
			}
			break;
		}
	}
	/**
	 * 判断一个区域在填充方向上,是否满足单元大小相同的要求
	 * 
	 * @param position
	 * @param fillTo
	 * @return boolean
	 */
    private boolean isRuleArea(AreaPosition areaPos, int fillTo) {
        CellPosition startPos = areaPos.getStart();
        CellPosition endPos = areaPos.getEnd();
        if(fillTo == FillToUp || fillTo == FillToDown){
            for (int i = startPos.getColumn(); i < endPos.getColumn(); i++) {
                CellPosition cellFirst = CellPosition.getInstance(startPos.getRow(),i);
                AreaPosition areaFirst = m_cm.getCombinedCellArea(cellFirst);
                if(!cellFirst.equals(areaFirst.getStart())){
                    continue;
                }
                AreaPosition areaPre = areaFirst;
                while (true) {
                    int row = areaPre.getStart().getRow();
                    int col = areaPre.getStart().getColumn();
                    int width = areaPre.getWidth();
                    int height = areaPre.getHeigth();
                    CellPosition cellTmp = CellPosition.getInstance(row+height,col);
                    if(!areaPos.contain(cellTmp)){
                        break;
                    }
                    AreaPosition areaTmp = m_cm.getCombinedCellArea(cellTmp);
                    if(cellTmp.equals(areaTmp.getStart()) && 
                       areaTmp.getWidth() == width && 
                       areaTmp.getHeigth() == height){
                        areaPre = areaTmp;
                    }else{
                        return false;
                    }
                }
            }
        }else if(fillTo == FillToLeft || fillTo == FillToRight){
            for (int i = startPos.getRow(); i < endPos.getRow(); i++) {
                CellPosition cellFirst = CellPosition.getInstance(i,startPos.getColumn());
                AreaPosition areaFirst = m_cm.getCombinedCellArea(cellFirst);
                if(!cellFirst.equals(areaFirst.getStart())){
                    continue;
                }
                AreaPosition areaPre = areaFirst;
                while (true) {
                    int row = areaPre.getStart().getRow();
                    int col = areaPre.getStart().getColumn();
                    int width = areaPre.getWidth();
                    int height = areaPre.getHeigth();
                    CellPosition cellTmp = CellPosition.getInstance(row,col+width);
                    if(!areaPos.contain(cellTmp)){
                        break;
                    }
                    AreaPosition areaTmp = m_cm.getCombinedCellArea(cellTmp);
                    if(cellTmp.equals(areaTmp.getStart()) && 
                       areaTmp.getWidth() == width && 
                       areaTmp.getHeigth() == height){
                        areaPre = areaTmp;
                    }else{
                        return false;
                    }
                }
            }
        }else{
            throw new IllegalArgumentException();
        }
        return true;
    }

    /**
	 * 根据填充方向方向，填充一个区域中的一行或一列。
	 * @param area
	 * @param firPos
	 * @param format
     * @param isFillFormat 
	 */
	private void fillExcuteOnePiece(AreaPosition area,CellPosition firPos,FillPluginFormat format, boolean isFillFormat){
		int preNullNum = 0;//第一个数据前的空单元数。
		int postNullNum = 0;//第一个数据后的空单元数。
		String firValue = "";
		String secValue = "";
		CellPosition curPos = firPos;
		//查找第一个非空单元格。
		while(true){
			if(!area.contain(curPos)){
				preNullNum = 0;
				break;
				}
			Cell tmpCell = m_cm.getCell(curPos.getRow(),curPos.getColumn());
			firValue = tmpCell==null||tmpCell.getValue()==null?"":tmpCell.getValue().toString();
			if(!firValue.equals(""))break;
			curPos = format.getNextCellPos(curPos);
			preNullNum++;
		}
		curPos = format.getNextCellPos(curPos);
		//查找第二个非空单元格。
		while(true){
			if(!area.contain(curPos)){
				postNullNum = preNullNum;//如果没有第二个非空单元，则复制第一个非空单元前的空格数。
				break;
			}
			Cell tmpCell = m_cm.getCell(curPos.getRow(),curPos.getColumn());
			secValue = tmpCell==null||tmpCell.getValue()==null?"":tmpCell.getValue().toString();
			if(!secValue.equals(""))break;
			curPos = format.getNextCellPos(curPos);
			postNullNum++;
		}
		curPos = firPos;
		//开始填充。
		format.init(firValue,secValue);
		String curValue = firValue;
		int i = 0;
		while(area.contain(curPos)){
			i = i % (preNullNum+postNullNum+2);
			if(i == preNullNum || i == preNullNum+postNullNum+1){
//				m_cm.setCellValueByAuth(curPos.getRow(),curPos.getColumn(),curValue);
                m_table.simulateKeyBoardInput(curPos.getRow(),curPos.getColumn(),curValue);
				curValue = format.getNextValue(curValue);
			}else{
//				m_cm.setCellValueByAuth(curPos.getRow(),curPos.getColumn(),"");
                m_table.simulateKeyBoardInput(curPos.getRow(),curPos.getColumn(),"");
			}
			if(isFillFormat){
				Format firFormat = (Format) (m_cm.getCellFormat(firPos)==null?null:m_cm.getCellFormat(firPos).clone());
				m_cm.setCellFormatByAuth(curPos.getRow(),curPos.getColumn(),firFormat);
			}
			curPos = format.getNextCellPos(curPos);
			
			i++;
		}
	}
}
