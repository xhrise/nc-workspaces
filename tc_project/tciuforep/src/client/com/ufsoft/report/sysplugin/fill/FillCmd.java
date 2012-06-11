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
 * <pre>�����������
 * </pre>
 * @author zzl
 * @version 5.0
 * Create on 2004-11-2
 */
public class FillCmd extends UfoCommand {
	/**��䷽��*/
	static final public int FillToUp = 0;  
	/**��䷽��*/
	static final public int FillToDown = 1;
	/**��䷽��*/
	static final public int FillToLeft = 2;
	/**��䷽��*/
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
				
		//�ȼ����䷽�����Ƿ���ͬ��ϵ�Ԫ�򶼲�����ϵ�Ԫ��
		if(!isRuleArea(area,fillTo)){
			UfoPublic.sendWarningMessage(MultiLang.getString("miuforep0000501"),m_report);
			return;
		}    	
		//��װ����¼�.
		UFOTable ufoTable = m_report.getTable();
		UserUIEvent event = new UserUIEvent(ufoTable,UserUIEvent.FILL,new Integer(fillTo),area);//�����������ʱ�ŵ�oldvale���
		if (!ufoTable.checkEvent(event)) return;
		ufoTable.fireEvent(event);
		fillExcute(area,fillTo,isSequenceFill,isFillFormat);
	}
	/**
	 * ִ��������.
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
		CellPosition firPos;// ��������е�һ��������׵�Ԫ��
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
	 * �ж�һ����������䷽����,�Ƿ����㵥Ԫ��С��ͬ��Ҫ��
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
	 * ������䷽�������һ�������е�һ�л�һ�С�
	 * @param area
	 * @param firPos
	 * @param format
     * @param isFillFormat 
	 */
	private void fillExcuteOnePiece(AreaPosition area,CellPosition firPos,FillPluginFormat format, boolean isFillFormat){
		int preNullNum = 0;//��һ������ǰ�Ŀյ�Ԫ����
		int postNullNum = 0;//��һ�����ݺ�Ŀյ�Ԫ����
		String firValue = "";
		String secValue = "";
		CellPosition curPos = firPos;
		//���ҵ�һ���ǿյ�Ԫ��
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
		//���ҵڶ����ǿյ�Ԫ��
		while(true){
			if(!area.contain(curPos)){
				postNullNum = preNullNum;//���û�еڶ����ǿյ�Ԫ�����Ƶ�һ���ǿյ�Ԫǰ�Ŀո�����
				break;
			}
			Cell tmpCell = m_cm.getCell(curPos.getRow(),curPos.getColumn());
			secValue = tmpCell==null||tmpCell.getValue()==null?"":tmpCell.getValue().toString();
			if(!secValue.equals(""))break;
			curPos = format.getNextCellPos(curPos);
			postNullNum++;
		}
		curPos = firPos;
		//��ʼ��䡣
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
