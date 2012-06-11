package com.ufida.report.multidimension.applet;

import java.util.Hashtable;

import nc.itf.iufo.freequery.IMember;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.multidimension.model.DimMemberCombinationVO;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.MultiDimensionUtil;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.multidimension.model.SelDimensionVO;

public class AnalyzerCheckService {

	private SelDimModel m_dimModel = null;

	private Hashtable<DimMemberCombinationVO, String> m_rowHeaders = null;

	private Hashtable<DimMemberCombinationVO, String> m_colHeaders = null;

	public AnalyzerCheckService(SelDimModel selDimModel) {
		super();
		m_dimModel = selDimModel;
	}

	private Hashtable<DimMemberCombinationVO, String> getRowHeaders() {
		if (m_rowHeaders == null) {
			m_rowHeaders = new Hashtable<DimMemberCombinationVO, String>();
			DimMemberCombinationVO[] rowHeaders = MultiDimensionUtil.getAllCombination(m_dimModel
					.getSelDimVOs(IMultiDimConst.POS_ROW));
			for (int i = 0; i < rowHeaders.length; i++) {
				m_rowHeaders.put(rowHeaders[i], "1");
			}
		}
		return m_rowHeaders;
	}

	private Hashtable<DimMemberCombinationVO, String> getColHeaders() {
		if (m_colHeaders == null) {
			m_colHeaders = new Hashtable<DimMemberCombinationVO, String>();
			DimMemberCombinationVO[] rowHeaders = MultiDimensionUtil.getAllCombination(m_dimModel
					.getSelDimVOs(IMultiDimConst.POS_COLUMN));
			for (int i = 0; i < rowHeaders.length; i++) {
				m_colHeaders.put(rowHeaders[i], "1");
			}
		}
		return m_colHeaders;
	}

	public boolean isValidAnalyzer(boolean isRow, boolean isCombined, Object analyzerKey) {
		try {
			if (isCombined) {
				DimMemberCombinationVO keyVO = (DimMemberCombinationVO) analyzerKey;
				return checkCombinationVO(isRow, keyVO);
			} else {
				IMember mem = (IMember) analyzerKey;
				return checkDimMember(isRow, mem);
			}
		} catch (ClassCastException ex) {
			AppDebug.debug(ex);
			return false;
		}
		
	}

	private boolean checkCombinationVO(boolean isRow, DimMemberCombinationVO keyVO) {
		if (isRow) {
			if (getRowHeaders().containsKey(keyVO))
				return true;
		} else {
			if (getColHeaders().containsKey(keyVO))
				return true;
		}
		return false;
	}

	private boolean checkDimMember(boolean isRow, IMember keyVO) {
		SelDimensionVO[] dims = m_dimModel.getSelDimVOs((isRow)?IMultiDimConst.POS_ROW:IMultiDimConst.POS_COLUMN);
		String keyDimPK = keyVO.getDimID();
		for (int i = 0; i < dims.length; i++) {
			if(dims[i].getDimDef().getDimID().equals(keyDimPK)){
				IMember[] allMems = dims[i].getAllMembers();
				for (int j = 0; j < allMems.length; j++) {
					if(allMems[j].getMemberID().equals(keyVO.getMemberID()))
						return true;
				}
				return false;
			}
		}
		return false;
	}

}
