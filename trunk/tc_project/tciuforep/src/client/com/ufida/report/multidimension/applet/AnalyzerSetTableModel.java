/*
 * Created on 2005-7-19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.multidimension.applet;

import javax.swing.table.DefaultTableModel;

import nc.us.bi.integration.dimension.DimensionSrv;
import nc.vo.bi.integration.dimension.DimensionVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.multidimension.model.AnalyzerSet;
import com.ufida.report.multidimension.model.IAnalyzerSet;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author ll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AnalyzerSetTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @i18n miufopublic289=启用
	 */
	private static final String COLNAME_ENABLED = StringResource.getStringResource("miufopublic289");

	/**
	 * @i18n miufo1001390=类型
	 */
	private static final String COLNAME_TYPE = StringResource.getStringResource("miufo1001390");
	
	/**
	 * @i18n miufopublic476=位置
	 */
	private static final String COLNAME_POS = StringResource.getStringResource("miufopublic476");

	/**
	 * @i18n miufo1003134=名称
	 */
	private static final String COLNAME_NAME = StringResource.getStringResource("miufo1003134");

	/**
	 * @i18n mbimulti00024=是否有效
	 */
	private static final String COLNAME_VALID = StringResource.getStringResource("mbimulti00024");

	private AnalyzerSet m_model = null;

	private String[] m_colNames = new String[] { COLNAME_ENABLED, COLNAME_TYPE, COLNAME_POS, COLNAME_NAME, COLNAME_VALID };

	public AnalyzerSetTableModel(AnalyzerSet newSet) {
		super();
		m_model = newSet;
		initColNames();

	}

	private void initColNames() {
		setColumnIdentifiers(m_colNames);
	}

	public Object getValueAt(int row, int column) {
		IAnalyzerSet rowVO = getSetModel().getAnalyzer(row);
		if (rowVO != null) {
			switch (column) {
			case 0:
				return new Boolean(rowVO.isEnabled());
			case 1:
				return getTypeName(rowVO.getAnalyzerType());
			case 2:
				return getPosName(rowVO);
			case 3:
				return rowVO.getName();
			case 4:
				return new Boolean(rowVO.isValid());

			default:
				break;
			}
		}
		return null;
	}

	public void setValueAt(Object aValue, int row, int column) {
		if (column == 0) {
			IAnalyzerSet rowVO = getSetModel().getAnalyzer(row);
			if (rowVO != null) {
				rowVO.setEnabled(!rowVO.isEnabled());
				fireTableCellUpdated(row,column);
			}
		} /*else if (column == 3) {
			IAnalyzerSet rowVO = getSetModel().getAnalyzer(row);
			if (rowVO != null) {
				rowVO.setValid(((Boolean) aValue).booleanValue());
			}
		}*/

	}

	private String getTypeName(int analyzerType) {
		switch (analyzerType) {
		case IAnalyzerSet.TYPE_FILTER:
			return StringResource.getStringResource("miufo1001594");
		case IAnalyzerSet.TYPE_FORMULAR:
			return StringResource.getStringResource("miufo1000033");
		case IAnalyzerSet.TYPE_HIDDEN:
			return StringResource.getStringResource("ubimultidim0024");
		case IAnalyzerSet.TYPE_LIMITROWS:
			return StringResource.getStringResource("ubimultidim0034");
		case IAnalyzerSet.TYPE_SORT:
			return StringResource.getStringResource("miufo1001595");

		default:
			return null;
		}
	}
	/**
	 * @i18n miufo1000794=行
	 * @i18n miufo1000795=列
	 */
	private String getPosName(IAnalyzerSet rowVO) {
		
		if(rowVO.getAnalyzerType()==IAnalyzerSet.TYPE_FORMULAR
				&& rowVO.getDimensionID()!=null){
			try{
				DimensionVO dimVO=DimensionSrv.getInstance().getDimByID(rowVO.getDimensionID());
				if(dimVO!=null){
					return dimVO.getDimname();
				}
			}catch(Exception e){
				AppDebug.debug(e);
			}
		}
		String strReturn=null;
		int combinedType=rowVO.getCombineType();
		switch (combinedType) {
			case IMultiDimConst.COMBINE_ROW:
				strReturn= StringResource.getStringResource("miufo1000794");
				break;
			case IMultiDimConst.COMBINE_COLUMN:
				strReturn= StringResource.getStringResource("miufo1000795");
				break;
		}
		return strReturn;
	}

	public boolean isCellEditable(int row, int column) {
		if (column == 0)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int columnIndex) {
		if (columnIndex == 0 || columnIndex == 4)
			return Boolean.class;
		return String.class;
	}

	public int getRowCount() {
		return getSetModel().getRowCount();
	}

	private AnalyzerSet getSetModel() {
		if (m_model == null)
			m_model = new AnalyzerSet();
		return m_model;
	}

} 