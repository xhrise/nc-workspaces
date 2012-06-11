package com.ufsoft.table.exarea;

import java.awt.Component;
import java.util.ArrayList;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.IAreaAtt;
import com.ufsoft.table.SelectedArea;
import com.ufsoft.report.util.MultiLang;

public class ExAreaCombineExt  extends AbsExAreaExt {
	
	public ExAreaCombineExt(ExAreaPlugin plugin) {
		super(plugin);
	}

	public boolean isEnabled(Component focusComp) {
		ArrayList<ExAreaCell> combEx = getCombineExAreas();
		if(combEx.size() < 2){
			return false;
		}
		return true;
//		return getPlugIn().getExAreaModel().check(null, selAnchorCell);
		 
	}

	private ArrayList<ExAreaCell> getCombineExAreas() {
		AreaPosition selArea = getPlugIn().getCellsModel().getSelectModel()
				.getSelectedArea();
		ArrayList<ExAreaCell> combEx = new ArrayList<ExAreaCell>();
		ExAreaCell[] exs = getPlugIn().getExAreaModel().getExAreaCells();
		for (ExAreaCell e : exs) {
			if (e.getArea().intersection(selArea)) {
				combEx.add(e);
			}
		}
		return combEx;
	}
	
	/**
	 * @i18n miufo00149=�ϲ�����չ����
	 */
	@Override
	public String getDesName() {
		return MultiLang.getString("miufo00149");
	}

	/**
	 * @i18n miufo00150=���ϲ���������������չ�����ϲ���Ԫ�����ص�
	 * @i18n miufo00082=������ʾ
	 */
	@Override
	public void excuteImpl(UfoReport report) {
		ArrayList<ExAreaCell> listCombEx = getCombineExAreas();
		if(listCombEx.size() < 2){
			return;
		}
		
		if(!checkExAreaType(listCombEx)){
			UfoPublic.showErrorDialog(getPlugIn().getReport(), "�ֶκ�ͼ�������ܺϲ�", "��չ����ϲ�");
			return;
		}
					
		int r1 = 10000, c1 = 10000, r2 = 0, c2 = 0;
		for(ExAreaCell cell: listCombEx){
			r1 = Math.min(cell.getArea().getStart().getRow(), r1);
			c1 = Math.min(cell.getArea().getStart().getColumn(), c1);
			
			r2 = Math.max(cell.getArea().getEnd().getRow(), r2);
			c2 = Math.max(cell.getArea().getEnd().getColumn(), c2);
		}
		
		AreaPosition combArea = AreaPosition.getInstance(CellPosition.getInstance(r1, c1), CellPosition.getInstance(r2, c2));

    	ArrayList<IAreaAtt> areas = getPlugIn().getCellsModel().getAreaDatas();//�ϲ�����Ϳ���չ����
		for(IAreaAtt aa: areas){
			if(aa instanceof SelectedArea)
				continue;
			
			//�޽�����ϵ
			if(!aa.getArea().intersection(combArea)){
				continue;
			}

			if(listCombEx.contains(aa)){
				continue;
			}
			
			//���������ϲ���������ڱ���չ����
			if(aa instanceof CombinedCell && combArea.contain(aa.getArea())){
				continue;
			}
			
			//�������������ĺϲ������Լ���������չ����������ҵ�����
			UfoPublic.showErrorDialog(getPlugIn().getReport(), MultiLang.getString("miufo00150"), MultiLang.getString("miufo00082"));
			return;
			 
		}
		
        ExAreaCombineDialog dlg = new ExAreaCombineDialog(getPlugIn().getReport(), combArea, listCombEx);
        dlg.show();
        int nR = dlg.getResult();
        if(nR != UfoDialog.ID_OK){
        	return;
        }
        
        ExAreaCell combineCell = dlg.getSelected();
        
        listCombEx.remove(combineCell);
        
        ArrayList<IExData> models = new ArrayList<IExData>();
        for(ExAreaCell cell: listCombEx){
        	if(cell.getModel() != null){
        		models.add(cell.getModel());
        	}
        }
        if(models.size() > 0){
	        String error = combineCell.fireUIEvent(ExAreaModelListener.COMBINE, combineCell, (IExData[])models.toArray(new IExData[models.size()]));
	        if(error != null && error.length() > 1){
				UfoPublic.showErrorDialog(getPlugIn().getReport(), error, MultiLang.getString("miufo00082"));
				return;
	        }

        }
		combineCell.setArea(combArea);
		getPlugIn().getExAreaModel().removeExArea(listCombEx.toArray(new ExAreaCell[0]));
		
		getPlugIn().getReport().updateUI();
	 
	}
	
	/**
	 * add by wangyga ��չ����ϲ�ʱ��У�����ͣ��ֶκ�ͼ���ܹ�����һ����չ������
	 * @param listCombEx
	 * @return
	 */
	private boolean checkExAreaType(ArrayList<ExAreaCell> listCombEx) {
		if (listCombEx == null || listCombEx.size() == 0) {
			return true;
		}

		int iExAreaType = ExAreaCell.EX_TYPE_NONE;
		boolean isHas = false;
		for (ExAreaCell exArea : listCombEx) {
			int iType = exArea.getExAreaType();
			if ((iType == ExAreaCell.EX_TYPE_SAMPLE
					|| iType == ExAreaCell.EX_TYPE_CHART) && !isHas){
				iExAreaType = iType;
				isHas = true;
			}
				
			if (iExAreaType != ExAreaCell.EX_TYPE_NONE
					&& iType != ExAreaCell.EX_TYPE_NONE
					&& iExAreaType != iType)
				return false;
		}
		return true;
	}
	
}
 