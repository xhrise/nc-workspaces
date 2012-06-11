package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;

import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureRefDlg;
import com.ufsoft.iufo.fmtplugin.measure.MeasureRefRightPanelSample;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;

public class MultiSelMeasureRefRightPanelSample extends MeasureRefRightPanelSample{

	private static final long serialVersionUID = 1L;
	private String m_strReportID = null;
	public MultiSelMeasureRefRightPanelSample(MeasureRefDlg parentDlg, boolean isContainsCurrentReport, KeyGroupVO currentKeyGroupVO, ArrayList excludeMeasuresList, boolean bIncludeRefMeas) {
		super(parentDlg, isContainsCurrentReport, currentKeyGroupVO, excludeMeasuresList, bIncludeRefMeas);
		setLayout(new BorderLayout());
	}
	public MeasureVO[] getSelMeasureVOs() {
		return getSelectedMeasureVOsByPos();
	}
	@Override
	protected void changeReportImpl(ReportVO reportVO, boolean bIncludeRefMeas) {
		super.changeReportImpl(reportVO, bIncludeRefMeas);
		m_strReportID = reportVO.getReportPK();
	}
	@Override
	public void setSelMeasureVOs(MeasureVO[] selMeasures) {
		if(selMeasures == null || selMeasures.length == 0)
			return;
		if(getComponentCount() == 0)
			return;
		Component component = getComponent(0);
		if(!(component instanceof UFOTable)){
			return;
		}
		UFOTable table = (UFOTable) component;
		CellsModel cellsModel = table.getCellsModel();
		MeasureModel measureModel = MeasureModel.getInstance(cellsModel);
		CellPosition pos = measureModel.getMeasurePosByPK(selMeasures[0].getCode());
		cellsModel.getSelectModel().setAnchorCell(pos);
	}
	public MeasureVO[] getSelectedMeasureVOsByPos() {
		Component component = getComponent(0);
		if(!(component instanceof UFOTable)){
			return null;
		}
		UFOTable table = (UFOTable) component;
		CellsModel cellsModel = table.getCellsModel();
		MeasureModel measureModel = MeasureModel.getInstance(cellsModel);
		CellPosition[] selPos = cellsModel.getSelectModel().getSelectedCells();
		if(selPos == null || selPos.length == 0)
			return null;
		MeasureVO[] matchingVOs = getMatchingMeasureVOs(getCurReportVO(), isIncludeRefMeasures());
		ArrayList<MeasureVO> al_measureVO = new ArrayList<MeasureVO>();
		for (int i = 0; i < selPos.length; i++) {
			MeasureVO selVO = measureModel.getMeasureVOByPos(selPos[i]);
			if(!Arrays.asList(matchingVOs).contains(selVO)){
				continue;
			}
			MeasureVO meas = filterMeasureVO(selVO);
			if(meas != null){
				meas.setSelReportPK(m_strReportID);
				al_measureVO.add(meas);
			}
		}
		return (al_measureVO.size()==0)?null:al_measureVO.toArray(new MeasureVO[0]);
	}
	protected boolean isAutoClose() {
		return false;
	}
}
