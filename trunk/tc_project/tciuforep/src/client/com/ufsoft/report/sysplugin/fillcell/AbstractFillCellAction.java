package com.ufsoft.report.sysplugin.fillcell;

import java.awt.event.ActionEvent;

import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPlugin;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.sysplugin.fill.FillCmd;
import com.ufsoft.report.sysplugin.fill.FillOptionDlg;
import com.ufsoft.report.sysplugin.fill.FillPluginFormat;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.format.Format;

public abstract class AbstractFillCellAction extends AbstractPluginAction{

	private FillCellPlugin plugin = null;
	
	public AbstractFillCellAction(IPlugin p){
		plugin = (FillCellPlugin)p;
	}
	
	protected abstract int getFillType();
	
	protected abstract String getName();
	
	@Override
	public void execute(ActionEvent e) {
		CellsModel cellsModel = getCellsModel();
		if(cellsModel == null){
			return;
		}
		AreaPosition area = cellsModel.getSelectModel().getSelectedArea();
		
		FillOptionDlg dlg = new FillOptionDlg(getTable());
		dlg.setVisible(true);		
		if(dlg.getResult() != UfoDialog.ID_OK){	
			return;
		}	
		boolean isSequenceFill = dlg.isSequenceFill();
		boolean isFillFormat = dlg.isFillFormat();
				
		//�ȼ����䷽�����Ƿ���ͬ��ϵ�Ԫ�򶼲�����ϵ�Ԫ��
		int fillTo = getFillType();
		if(!isRuleArea(area,fillTo)){
			UfoPublic.sendWarningMessage(MultiLang.getString("miuforep0000501"),getTable());
			return;
		}    	
		//��װ����¼�.
		UFOTable ufoTable = getTable();
		if(ufoTable == null){
			return;
		}
		UserUIEvent event = new UserUIEvent(ufoTable,UserUIEvent.FILL,new Integer(fillTo),area);//�����������ʱ�ŵ�oldvale���
		if (!ufoTable.checkEvent(event)) return;
		ufoTable.fireEvent(event);
		
		fillExcute(area, fillTo, isSequenceFill, isFillFormat);
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = new PluginActionDescriptor(getName());
		desc.setExtensionPoints(XPOINT.MENU,XPOINT.POPUPMENU);
		desc.setGroupPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("uiuforep0000500"),"insertAndFill"});
		return desc;
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
		case FillCmd.FillToUp:
			for (int dCol = 0; dCol < area.getWidth(); dCol++) {
				firPos = CellPosition.getInstance(endRow, startCol + dCol);
				format.setPosChange(-1, 0);
				fillExcuteOnePiece(area, firPos, format,isFillFormat);
			}
			break;
		case FillCmd.FillToDown:
			for (int dCol = 0; dCol < area.getWidth(); dCol++) {
				firPos = CellPosition.getInstance(startRow, startCol + dCol);
				format.setPosChange(1, 0);
				fillExcuteOnePiece(area, firPos, format,isFillFormat);
			}
			break;
		case FillCmd.FillToLeft:
			for (int dRow = 0; dRow < area.getHeigth(); dRow++) {
				firPos = CellPosition.getInstance(startRow + dRow, endCol);
				format.setPosChange(0, -1);
				fillExcuteOnePiece(area, firPos, format,isFillFormat);
			}
			break;
		case FillCmd.FillToRight:
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
        CellsModel cellsModel = getCellsModel();
        if(fillTo == FillCmd.FillToUp || fillTo == FillCmd.FillToDown){
            for (int i = startPos.getColumn(); i < endPos.getColumn(); i++) {
                CellPosition cellFirst = CellPosition.getInstance(startPos.getRow(),i);
                AreaPosition areaFirst = cellsModel.getCombinedCellArea(cellFirst);
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
                    AreaPosition areaTmp = cellsModel.getCombinedCellArea(cellTmp);
                    if(cellTmp.equals(areaTmp.getStart()) && 
                       areaTmp.getWidth() == width && 
                       areaTmp.getHeigth() == height){
                        areaPre = areaTmp;
                    }else{
                        return false;
                    }
                }
            }
        }else if(fillTo == FillCmd.FillToLeft || fillTo == FillCmd.FillToRight){
            for (int i = startPos.getRow(); i < endPos.getRow(); i++) {
                CellPosition cellFirst = CellPosition.getInstance(i,startPos.getColumn());
                AreaPosition areaFirst = cellsModel.getCombinedCellArea(cellFirst);
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
                    AreaPosition areaTmp = cellsModel.getCombinedCellArea(cellTmp);
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
	protected void fillExcuteOnePiece(AreaPosition area,CellPosition firPos,FillPluginFormat format, boolean isFillFormat){
		CellsModel cellsModel = getCellsModel();
		UFOTable table = getTable();
		int preNullNum = 0;//��һ������ǰ�Ŀյ�Ԫ����
		int postNullNum = 0;//��һ�����ݺ�Ŀյ�Ԫ����
		String firValue = "";
		String secValue = "";
		CellPosition curPos = firPos;
		//���ҵ�һ���ǿյ�Ԫ��
		while(curPos != null){
			if(!area.contain(curPos)){
				preNullNum = 0;
				break;
				}
			Cell tmpCell = cellsModel.getCell(curPos.getRow(),curPos.getColumn());
			firValue = tmpCell==null||tmpCell.getValue()==null?"":tmpCell.getValue().toString();
			if(!firValue.equals(""))break;
			curPos = format.getNextCellPos(curPos);
			preNullNum++;
		}
		curPos = format.getNextCellPos(curPos);
		//���ҵڶ����ǿյ�Ԫ��
		while(curPos != null){
			if(!area.contain(curPos)){
				postNullNum = preNullNum;//���û�еڶ����ǿյ�Ԫ�����Ƶ�һ���ǿյ�Ԫǰ�Ŀո�����
				break;
			}
			Cell tmpCell = cellsModel.getCell(curPos.getRow(),curPos.getColumn());
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
		while(curPos != null && area.contain(curPos)){
			i = i % (preNullNum+postNullNum+2);
			if(i == preNullNum || i == preNullNum+postNullNum+1){
//				m_cm.setCellValueByAuth(curPos.getRow(),curPos.getColumn(),curValue);
				table.simulateKeyBoardInput(curPos.getRow(),curPos.getColumn(),curValue);
				curValue = format.getNextValue(curValue);
			}else{
//				m_cm.setCellValueByAuth(curPos.getRow(),curPos.getColumn(),"");
				table.simulateKeyBoardInput(curPos.getRow(),curPos.getColumn(),"");
			}
			if(isFillFormat){
				Format firFormat = (Format) (cellsModel.getCellFormat(firPos)==null?null:cellsModel.getCellFormat(firPos).clone());
				cellsModel.setCellFormatByAuth(curPos.getRow(),curPos.getColumn(),firFormat);
			}
			curPos = format.getNextCellPos(curPos);
			
			i++;
		}
	}
	
	private CellsModel getCellsModel(){
		return plugin.getHandler().getCellsModel();
	}
	
	private UFOTable getTable(){
		return plugin.getHandler().getTable();
	}
}
