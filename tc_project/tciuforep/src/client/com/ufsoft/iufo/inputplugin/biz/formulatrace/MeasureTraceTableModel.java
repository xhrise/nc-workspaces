package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import javax.swing.table.AbstractTableModel;

import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.vo.iufo.data.MeasureTraceVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.unit.UnitInfoVO;
import com.ufsoft.report.util.MultiLang;

public class MeasureTraceTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private MeasureTraceVO[] m_tracevos = null;
	private int nColumnCount = 0;
	private String[] columnnames;
	private KeyVO[]	keyvos = null;
	/**
	 * @i18n report00104=ֵ
	 */
	public MeasureTraceTableModel(MeasureTraceVO[] tvos){
		
		this.m_tracevos = tvos;
		
	    String measpk = tvos[0].getMeasurePK();
	    String keycombpk = IUFOUICacheManager.getSingleton().getMeasureCache().getKeyCombPk(measpk);
	    KeyGroupVO kg = IUFOUICacheManager.getSingleton().getKeyGroupCache().getByPK(keycombpk);
	    KeyVO[] kvos = kg.getKeys();
	    nColumnCount = kvos.length  + 1;
	    columnnames = new String[nColumnCount];
	    columnnames[0] = MultiLang.getString("report00104");
	    
	    for (int i = 0; i < kvos.length; i++) {
			columnnames[i + 1] = kvos[i].getName();
		}
	    
	    this.keyvos = kvos;
	    
	    
	} 
	
    public String getColumnName(int column) {
//		int nColumnCount = getColumnCount();
//		if(nColumnCount <=0 ){
//			return null;
//		}
    	if(column <0 || column >= nColumnCount){
			return null;
		}
//		MeasureTraceVO curFormulaTraceValueItem =  this.m_tracevos[0];
//		if(column > 0 && column <= nColumnCount-1){
////			return curFormulaTraceValueItem.getKeyNames()[column];
//			return "Key" + (column);
//			
//		}else if(column == 0){
//			return "ֵ";
//		}
		
		return columnnames[column];
    }
    
	public int getColumnCount() {		
		return nColumnCount;
	}

	public int getRowCount() {
		if(m_tracevos!=null){
			return m_tracevos.length;
		}
		return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex <0 || rowIndex >= m_tracevos.length){
			return null;
		}
		int nColumnCount = getColumnCount();
		MeasureTraceVO tvo =  m_tracevos[rowIndex];
		
		if(columnIndex == 0){
			return tvo.getValue();
		} else  if(columnIndex > 0 && columnIndex <= nColumnCount-1){
			KeyVO kvo = keyvos[columnIndex - 1];
			String val = tvo.getKeyvalues()[columnIndex - 1];
			
            if(kvo.isPrivate()){
            	
            }
            
			if(kvo.getKeywordPK().equals(KeyVO.CORP_PK) || kvo.getKeywordPK().equals(KeyVO.DIC_CORP_PK)){
				UnitInfoVO unit = IUFOUICacheManager.getSingleton().getUnitCache().getUnitInfoByPK(val);
				if(unit != null){
					val = unit.getUnitName();
				}
			}
				
			return val;
		}
		
		return null;
	}

}
 