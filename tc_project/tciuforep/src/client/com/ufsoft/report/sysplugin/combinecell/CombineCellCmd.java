/*
 * Created on 2004-12-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufsoft.report.sysplugin.combinecell;

import java.util.ArrayList;
import java.util.Iterator;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.CombineCellDlg;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.IAreaAtt;
import com.ufsoft.table.TableDataModelException;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.IVerify.VerifyType;

/**
 * ��ϵ�Ԫ���õĲ���
 * @author zzl
 * @version 5.0
 * Create on 2004-12-1
 */
public class CombineCellCmd extends UfoCommand {
	
	protected UfoReport m_rep;
	protected CellsModel m_cm;

	public void execute(Object[] params) {
		m_rep = (UfoReport)params[0];
		m_cm = m_rep.getCellsModel();
		AreaPosition selArea = m_cm.getSelectModel().getSelectedArea();

		CombineCellDlg dialog = new CombineCellDlg(selArea.toString(),m_rep.getFrame());
		dialog.setVisible(true);
		if (dialog.getResult() == CombineCellDlg.ID_COMALL){//�������
		   doCombineCell(selArea,new AreaPosition[]{selArea});
		}else if (dialog.getResult() == CombineCellDlg.ID_CANCEL){
		    return;
		}
		if (dialog.getResult() == CombineCellDlg.ID_COMCOL){//�������
		    AreaPosition[] colAreas = new AreaPosition[selArea.getWidth()];
		    int startRow = selArea.getStart().getRow();
		    int startCol = selArea.getStart().getColumn();
		    for(int i=0;i<selArea.getWidth();i++){
		        colAreas[i] = AreaPosition.getInstance(startRow,startCol+i,1,selArea.getHeigth());
		    }
		    doCombineCell(selArea,colAreas);
		}
		if (dialog.getResult() == CombineCellDlg.ID_COMROW){//�������
		    AreaPosition[] rowAreas = new AreaPosition[selArea.getHeigth()];
		    int startRow = selArea.getStart().getRow();
		    int startCol = selArea.getStart().getColumn();
		    for(int i=0;i<selArea.getHeigth();i++){
		        rowAreas[i] = AreaPosition.getInstance(startRow+i,startCol,selArea.getWidth(),1);
		    }
		    doCombineCell(selArea,rowAreas);
		}	
		if (dialog.getResult() == CombineCellDlg.ID_COMREMOVE){//ɾ����ϵ�Ԫ	
		    delCombineCell(selArea, m_rep.getTable());
		}
 
	}
	
	/**
	 *�ж������Ƿ���Խ�����ϵ�Ԫ�Ĳ���. 
	 *���֧���������ļ���ʽ���ݵ�undo����,����Ϳ���ֱ��ִ��,���������������������,�ٻ��˼���.
	 *�Ͳ���ȫ�����ͨ������ִ����.
	 * @param area
	 * @return boolean
	 */
	protected boolean isCanCombineCell(AreaPosition area){
		if(area.isCell()){
			UfoPublic.sendErrorMessage(MultiLang.getString("miufo1001828"),m_rep,null);  //"������Ԫ�������û�ȡ�����"
			return false;
		}
	    if(area.getWidth()*area.getHeigth() > 300){
			UfoPublic.sendErrorMessage(MultiLang.getString("miufo1000741"),m_rep,null);  //"��ϵ�Ԫ�������Ԫ����Ϊ300"
			return false;
	    }	 
        //�����ϵ�Ԫ֮����������ָ����߹ؼ��֣���������ϡ�
		ArrayList list =  m_cm.getSeperateCellPos(area);
		for(int i=1;i<list.size();i++)	{
		    CellPosition pos = (CellPosition)list.get(i);
		    Cell cell = m_cm.getCell(pos);
		    if(cell != null && m_cm.isVerify(pos,VerifyType.UNSUPPORT_COMBINED,true)){
		        UfoPublic.sendErrorMessage(MultiLang.getString("miufo1001835"),m_rep,null);  //"�����ϵ�Ԫ֮����������ָ����߹ؼ��֣����������"
		        return false;
		    }
		}
		//���ע�����Ƿ���������ϲ�����
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.COMBINECELL, area, null);
		if (!m_rep.getTable().checkEvent(event)) {
			return false;
		}
		return true;
	}
	

	/**
	 * ɾ���ض��������������͵�Ԫ.�ض�������Դ�����ϵ�Ԫ����.
	 * @param area
	 */
	public static void delCombineCell(AreaPosition area, UFOTable ufoTable){
	    CellsModel cellsModel = ufoTable.getCellsModel();
		CombinedAreaModel crm = CombinedAreaModel.getInstance(cellsModel);
		CombinedCell[] ccs = crm.getCombineCells(area);
	     
        for (CombinedCell cc: ccs) {
        	
        	UserUIEvent event = new UserUIEvent(ufoTable, UserUIEvent.UNCOMBINECELL,
    				cc.getArea(), null);
        	
    		//����Ƿ��������.
    		if (!ufoTable.checkEvent(event)) {
    			continue;
    		}
    		crm.removeCombinedCell(cc);
    		
    		ufoTable.fireEvent(event);
        	 
        }
     
	}
	
	/**
	 * �ϲ���Ԫ.������ѡ������ĵ�Ԫ.
	 * 
	 * @param area
	 * @throws TableDataModelException
	 */
	public void combineCell(AreaPosition area,UFOTable ufoTable) throws TableDataModelException {
		if (area == null || area.isCell()) {
			return;
		}
		UserUIEvent event = new UserUIEvent(this, UserUIEvent.COMBINECELL,
				area, null);
		//����Ƿ��������.
		if (!ufoTable.checkEvent(event)) {
			return;
		}
		//�����������ģ�Ͳ���ǰ,��ģ�Ͳ�������Ԥ����.
		ufoTable.fireEvent(event);
		//ģ�Ͳ���.
		CellsModel cm = m_rep.getCellsModel();
		ArrayList areaDatas = cm.getAreaDatas();
		if (areaDatas != null) {
			Iterator iter = areaDatas.iterator();
			ArrayList listRemove = new ArrayList();
			//�����������Ƿ񽻲棬�Ƿ����ɾɵ�����
			while (iter.hasNext()) {
				IAreaAtt att = (IAreaAtt) iter.next();
				if (att instanceof CombinedCell) {
					//��������ཻ���ǲ��ຬ���׳��쳣��
					if (att.getArea().intersection(area)) {
						if (area.contain(att.getArea())) { //��������������򣬼�¼��Ҫɾ���ľ�����
							listRemove.add(att);
						} else {
							throw new TableDataModelException();
						}
					}
				}
			}
			areaDatas.removeAll(listRemove);
		}
		//�õ���ǰ�ϲ���Ԫ�׵�Ԫ�����ݡ�
		cm.combineCell(area);
	}

	/**
	 * add by ����� 2008-6-4 ���Ŀ������
	 * 
	 * @param AreaPosition
	 *            areaSrc��ԭ����,AreaPosition areaCombine��ԭ�������������,CellPosition
	 *            target��Ŀ������ê��
	 * @return AreaPosition
	 */
	public static AreaPosition getDestAreaPos(AreaPosition areaSrc,
			AreaPosition areaCombine, CellPosition target) {
		if (areaSrc == null || areaCombine == null || target == null) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1000496"));// �������������Ϊ��
		}
		int iStartRowSrc = areaSrc.getStart().getRow();
		int iStartColumnSrc = areaSrc.getStart().getColumn();
		int iCombineStartRow = areaCombine.getStart().getRow();
		int iCombineStartColumn = areaCombine.getStart().getColumn();
		int iOffRow = iCombineStartRow - iStartRowSrc;
		int iOffColumn = iCombineStartColumn - iStartColumnSrc;
		int iAreaWidth = areaCombine.getWidth();
		int iAreaHeight = areaCombine.getHeigth();
		AreaPosition areaPos = AreaPosition.getInstance(target.getRow()
				+ iOffRow, target.getColumn() + iOffColumn, iAreaWidth,
				iAreaHeight);
		return areaPos;
	}
	
	/**ִ�кϲ��Ķ���.
	 * @param selArea ����ѡ������.
	 * @param areas ��Ҫ�ϲ���С����.
	 */
	public boolean doCombineCell(AreaPosition selArea,AreaPosition[] areas){
	    //�����ж��Ƿ����ִ��.
	    for(int i=0;i<areas.length;i++){
	        if(!isCanCombineCell(areas[i])){
	            return false;
	        }
	    }
	    //��ʼִ�в���.
	    delCombineCell(selArea, m_rep.getTable());
	    try {
            for (int i = 0; i < areas.length; i++) {
                combineCell(areas[i],m_rep.getTable());
            }
//        } catch (TableDataModelException e) {
//            //����������ں�����ϵ�Ԫ.��Ӧ�׳�����.
//            AppDebug.debug(e);
        } finally {
        	CombinedAreaModel.getInstance(m_rep.getCellsModel()).clearCache();
        	
        }
        return true;
	}
	/**
	 * modify by ����� 2008-4-21 �ṩ���ⲿ�ľ�̬����,���ڰ���ҵ���жϵĵ�Ԫ��ϲ�.
	 * @param area
	 * @param cm
	 */
	public static void combineCell(AreaPosition area,UfoReport m_rep){
	    CombineCellCmd cmd = new CombineCellCmd();
	    cmd.m_cm = m_rep.getCellsModel();
	    cmd.m_rep = m_rep;
	    cmd.doCombineCell(area,new AreaPosition[]{area});
	}
}