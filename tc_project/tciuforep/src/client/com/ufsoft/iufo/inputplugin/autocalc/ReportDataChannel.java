package com.ufsoft.iufo.inputplugin.autocalc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.data.DataSet;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.util.UfoPublic1;
import com.ufsoft.script.base.UfoDouble;
import com.ufsoft.script.base.UfoInteger;
import com.ufsoft.script.base.UfoString;
import com.ufsoft.script.base.UfoVal;
import com.ufsoft.script.datachannel.ITableData;
import com.ufsoft.script.datachannel.IUFODynAreaDataParam;
import com.ufsoft.script.exception.CmdException;
import com.ufsoft.script.exception.UfoValueException;
import com.ufsoft.script.expression.AreaExprCalcEnv;
import com.ufsoft.script.expression.UfoExpr;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.re.IDName;

/**
 * @author ask
 * ʵ��ITableData�ӿڵı�������ͨ����
 * 
 */
public class ReportDataChannel implements ITableData {
	private static Logger logger = Logger.getLogger(ReportDataChannel.class);
	
	/**
	 * ���ؼ�������ģ��
	 */
	private CellsModel m_CellsModel;
	/**
	 * ��¼�����ʾ�Ķ�̬���ݼ��ϡ�һ���������ʾ�����̬���ݼ��ϣ��������������Dataset�ı�ʶ��Value��Dataset��
	 */
	private Hashtable m_DataSets;
	/**
	 * ������������ָ��Ķ�̬���ݼ��ϡ������������Ϣֻ���Զ�Ӧ1��ؼ��ֵ�ֵ���������������ֻ����1����¼��
	 */
	private DataSet m_TableDataset;
	
	/**
	 * ��̬�����ݷ���������
	 */
	private IUFODynAreaDataParam m_objDynAreaCalcParam = null;
	
	/**
	 * ���캯��
	 * 
	 * @param model
	 *            ���ؼ�������ģ��
	 */
	public ReportDataChannel(CellsModel model) {
		super();
		m_CellsModel = model;
	}
	
	/* 
	 * �õ�ĳ�������������Ϣ������������UfoVal�����顣ֻ�е�Cell��ʵ����������UfoValʱ�ŷ��أ����򷵻�NULL
	 * (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getAreaData(com.ufsoft.table.IArea)
	 */
	public UfoVal[] getAreaData(IArea a) {
		a = getRealArea(a);
		
		//��ʽ�����ڶ�̬���ڣ�ֻ���㱾������
		boolean isInDynArea = isInDynArea(a.getStart());
		if(isInDynArea && this.getDynAreaCalcParam() != null){
			a = getRealAreaAfterDataExtend(a);
		} 
		
		//��������������Ϣ
		AreaPosition area = (AreaPosition) a;
		ArrayList posList  = m_CellsModel.seperateArea(area);
		UfoVal[] values = null;
		if(posList!=null){
			values = new UfoVal[posList.size()];
			for (int i = 0; i < values.length; i++) {
				Object pos = posList.get(i);
				CellPosition cp = null;
				if(pos instanceof CellPosition){
					cp = (CellPosition)pos;
				}
				else if(pos instanceof AreaPosition){
					cp = ((AreaPosition)pos).getStart();
				}
				
				Cell cell = m_CellsModel.getCell(cp);
				IufoFormat format=(IufoFormat)m_CellsModel.getRealFormat(cp);
				values[i] = getCellValue(cell,true,format);
				if(values[i]==null){
                	if(format!=null && format.getCellType() == TableConstant.CELLTYPE_NUMBER){
                		values[i]=UfoVal.NULLVAL;
                	}else
                		values[i]=UfoVal.NULLSTR;
                }	
			}
		}
		return values;
		
	}

	/**
	  * ��ȡָ����Ԫ������
	  * @param c ��Ԫ����
	  * @param processDigital �Ƿ������λ��������Ԫ����Ϊ��ֵ���͵�ʱ����Ч��
	  * @return ��Ԫ����
	  */
	private UfoVal getCellValue(Cell c,boolean processDigital,IufoFormat format){
		try {
			if(c!=null&&c.getValue()!=null){
            	UfoVal value = null;
            	if(c.getValue() instanceof String && ((String)c.getValue()).trim().equals("")==false){
            		value = UfoString.getInstance(c.getValue());
            	}else if(c.getValue() instanceof Double){
            		value = UfoDouble.getInstance(c.getValue());
            	}else if(c.getValue() instanceof IDName){
            		value = UfoString.getInstance(((IDName)c.getValue()).getID());
            	}else if(c.getValue() instanceof UfoVal){
            		value = (UfoVal) c.getValue();
            	}else if(c.getValue() instanceof Integer){
            		value=UfoInteger.getInstance(c.getValue());
            	}
            	
            	if(value!=null && value instanceof UfoDouble && processDigital && format!=null){
                    UfoDouble doubleValue = (UfoDouble)value;
                    //����ʽ�����õ���λλ��
                    int digital = format.getDecimalDigits();
                    if(digital == TableConstant.UNDEFINED){
                    	digital = DefaultSetting.DEFAULT_DECIMALDIGITS;
                    }
                    if(format.isHasPercent()==TableConstant.TRUE)
                    	digital += 2;
                    double dValue = UfoPublic1.roundDouble(doubleValue.doubleValue(),digital);                
                    value = UfoDouble.getInstance(dValue);              
                }
                return value;
            }
		} catch (UfoValueException e) {
			AppDebug.debug(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getAreaDirection(com.ufsoft.table.IArea)
	 */
	public int[] getAreaDirection(IArea a) {
		return null;
	}

	//###################################################################################################
	/**
     * Ϊ��������ͳ�����Ӷ�̬��֧��(���ͳ�������ڶ�̬������ͳ�Ƹ�������չ������)
     * @param a
     * @param staticType
     * @return UfoVal[]
     */
    public UfoVal[] getAreaDataForStatics(IArea a, int staticType) {
    	//�������״̬ʱ�����ڴ���aΪ��ʽ�����cellsModel��������չ��������������⡣
    	ArrayList<CellPosition> list  = getSeperateCellPos(a);

    	ArrayList<UfoVal> values = new ArrayList<UfoVal>();
    	for (CellPosition cellPos: list) {
    		IArea area = getRealArea(cellPos);
    		cellPos = area.getStart();
    		boolean isInDynArea = isInDynArea(cellPos);
    		if(isInDynArea){//Ҫ����������ڶ�̬����
    			if(this.getDynAreaCalcParam() == null){//��̬�����㻷��Ϊ�գ���ʾ��ʽ�����������ϣ���ʱҪ������չ�������
    				DynAreaCell dynAreaCell = DynAreaOperUtil.getDynAreaCellByPos(m_CellsModel, cellPos);
    				ArrayList<CellPosition> cellPosList = getExtDataAreaByFmtArea(cellPos, dynAreaCell);
    				getExtDataByArea(values, cellPosList, staticType);
    			} else{//��ʽ�����ڶ�̬���ڣ�ֻ���㱾������
    				cellPos = getRealAreaAfterDataExtend(AreaPosition.getInstance(cellPos, cellPos)).getStart();
    				values.add(getAreaDataForStatic(staticType, cellPos));
    			}
    		}else{//������������
    			values.add(getAreaDataForStatic(staticType, cellPos));
    		} 
    	}        
    	return values.toArray(new UfoVal[0]);         
    }
	
    /**
	 * ��������ģ��ʵ��λ�õõ���չ�������
	 * @param values
	 * @param cellPosList
	 */
    private void getExtDataByArea(ArrayList<UfoVal> values, ArrayList<CellPosition> cellPosList, int staticType) {
    	for(CellPosition cellPos : cellPosList){
    		values.add(getAreaDataForStatic(staticType, cellPos));
    	}
	}
    /**
	 * ��������ģ��ʵ��λ�õõ���չ���ʵ������
	 * @param staticType
	 * @param values
	 * @param cellPos
	 */
    private UfoVal getAreaDataForStatic(int staticType, CellPosition cellPos) {
    	Cell c = m_CellsModel.getCell(cellPos);
    	if(staticType == STATIC_NONSAMPLE_CELL){//�Ա�����Ԫ�����ض�Ӧ��ֵΪNull;�ԷǱ�����Ԫ�����ض�Ӧ��ֵΪ1
    		if(c != null && c.getFormat() != null && ((IufoFormat)c.getFormat()).getCellType() != TableConstant.CELLTYPE_SAMPLE){
    			return UfoDouble.getInstance(1);
    		}else{
    			return null;
    		}
    	} else{
    		if(c != null && c.getFormat() != null && ((IufoFormat)c.getFormat()).getCellType() == TableConstant.CELLTYPE_NUMBER){
    			if(c.getValue() != null){
    				if (c.getValue() instanceof UfoDouble)
    					return (UfoVal) c.getValue();
    				else if (c.getValue() instanceof Double)
    					return UfoDouble.getInstance(((Double)c.getValue()).doubleValue());
    				else
    					return UfoVal.NULLVAL;
    			}else{
    				return UfoVal.NULLVAL;
    			}
    		}else{
    			return null;
    		}
    	}
    }
	/**
	 * ��������ģ�͸�ʽλ�õõ���չ���ʵ������
	 * @param cellPos
	 * @param dynAreaCell
	 * @return
	 */
	private ArrayList<CellPosition> getExtDataAreaByFmtArea(CellPosition cellPos, DynAreaCell dynAreaCell) {
		ArrayList<CellPosition> cellPosList = new ArrayList<CellPosition>();
		AreaPosition area = dynAreaCell.getArea();
		int dRow = dynAreaCell.getDirection()==DynAreaVO.DIRECTION_ROW ? dynAreaCell.getHeaderCount() : 0;
        int dCol = dynAreaCell.getDirection()==DynAreaVO.DIRECTION_ROW ? 0 : dynAreaCell.getHeaderCount();
		while(area.contain(cellPos.getRow(), cellPos.getColumn())){
			cellPosList.add(cellPos);
			cellPos = cellPos.getMoveArea(dRow, dCol);
		}
		return cellPosList;
	}
	//###################################################################################################
	
	/* (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getAreaDataForStatics(com.ufsoft.table.IArea, int)
	 * �ɰ汾��ʱ��������
	 */
	public UfoVal[] getAreaDataForStatics1(IArea a, int staticType) {
		AreaPosition area = (AreaPosition) a;
		ArrayList posList  = m_CellsModel.seperateArea(area);
		UfoVal[] values = new UfoVal[posList.size()];			
		if(posList!=null){
			UfoDouble d = UfoDouble.getInstance(1);
			for (int i = 0; i < values.length; i++) {
				Format fmt = null;
				Object pos = posList.get(i);
				Cell c = null;
				CellPosition cp = null;
				if(pos instanceof CellPosition){
					cp = (CellPosition)pos;
				}
				else if(pos instanceof AreaPosition){
					cp = ((AreaPosition)pos).getStart();
				}
				c = m_CellsModel.getCell(cp);
				UfoVal val = null;
				fmt = m_CellsModel.getRealFormat(cp);
				if(staticType==ITableData.STATIC_NONSAMPLE_CELL){
					if(fmt==null||fmt.getCellType()==TableConstant.CELLTYPE_SAMPLE){
						val = null;
					}
					else {
						val = d;
					}
				}else if(staticType==ITableData.STATIC_VALUE_CELL){
					//����ֵ��Ԫ,������Ӧֵ,���û��ֵ���򷵻�NULLVAL;�����������͵ĵ�Ԫ������null
					if(fmt!=null&&fmt.getCellType()==TableConstant.CELLTYPE_NUMBER){
						try {
							if (c!=null && c.getValue() instanceof Double){
								val = (UfoVal)(UfoDouble.getInstance(c.getValue()));
							}
						} catch (UfoValueException e) {
							logger.error(e);//@devTools                             AppDebug.debug(e);
						}
						if(val==null){
							val = UfoVal.NULLVAL;
						}
					}
				}
				values[i] = val;
				if(c!=null&&c.getValue() instanceof UfoVal){
					values[i] = (UfoVal)c.getValue();
				}	
			}
		}
		return values;
	}

	/* ����ĳ����������ݡ� �������Ϊ�գ�ԭ�����ݷǿ���ΪUfoValue�����ԭ��Cell�е�Value��
	 * 
	 * (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#setAreaData(com.ufsoft.table.IArea, java.lang.Object[])
	 */
	public void setAreaData(IArea a, Object[] val){
		if(a==null || val==null){
			return ;
		}
		
		a = getRealArea(a);
		
		ArrayList<CellPosition> cellsPos = null;
		cellsPos = getSeperateCellPos(a);
		for(int i=0,size=cellsPos.size(); i<size ;i++){
        	CellPosition cPos =cellsPos.get(i);
            Object value = i<val.length?val[i]:val[val.length-1];   
              
            Format cellFormat=m_CellsModel.getRealFormat(cPos);
            if(value!=null){
	            //�ж��Ƿ���Ը��ĵ�Ԫ������
	            boolean bCellSampleType=isCellSampleType(cellFormat);
	            if(bCellSampleType==true){
	            	int iValueType=getValueType(value);
	            	if(iValueType!=TableConstant.UNDEFINED){
	            		//���ĵ�Ԫ����������
	            		m_CellsModel.getCellFormatIfNullNew(cPos).setCellType(iValueType);
	            	}
	            }
            }
            Object convertedValue = getValidValue(cPos,cellFormat,value);
            cPos = (CellPosition)getRealAreaAfterDataExtend(cPos);
            m_CellsModel.setCellValue(cPos.getRow(),cPos.getColumn(),convertedValue);
            
        }		
	}
	
	/**
     * ���ܣ����Ϊδ���塢�������ͣ�����Ҫ���ݼ���ֵ���õ�Ԫ����������
     * @param format
     * @param value
     * @return
     */
    private boolean isCellSampleType(Format format){
    	if(format==null 
        		|| format.getCellType()==TableConstant.UNDEFINED
        		|| format.getCellType()==TableConstant.CELLTYPE_SAMPLE
        		){
            return true;
        }
        return false;
    }

    /**
     * ���ܣ����ݼ�����������Ч���������͡�
     * @param value
     * @return
     */
    private int getValueType(Object value){
    	int iValueType=TableConstant.UNDEFINED;
    	if(value!=null){
    		if(value instanceof Double || value instanceof Integer)
    			iValueType=TableConstant.CELLTYPE_NUMBER;
    		else if(value instanceof String)
    			iValueType=TableConstant.CELLTYPE_STRING;
    	}
    	return iValueType;
    }

    /**
     * ���ܣ����ݵ�Ԫ��ʽ���ظõ�Ԫ����Ч��ֵ��
     * @param area
     * @param format
     * @param value
     * @return
     */
    private Object getValidValue(IArea area,Format format,Object value){
    	if(format==null || value==null)
    		return value;

    	Object retValue=value;
    	if (format.getCellType()==TableConstant.CELLTYPE_NUMBER) {
    		if( value instanceof Double ){
    			if (((Double)value).isInfinite()) {
    				retValue = null;
    			} else if (((Double)value).isNaN()) {
    				retValue=null;
    			}else if(Math.abs(((Double)value).doubleValue()) <= 1.0e-20){
    				retValue = new Double("0");
	             }else{
	            	 int digits=((IufoFormat)format).getDecimalDigits();
	            	 if (digits == TableConstant.UNDEFINED)
	         			digits = DefaultSetting.DEFAULT_DECIMALDIGITS;
	            	 // @edit by wangyga at 2009-3-10,����02:10:02 ��������ʱ�ȴ���ٷֺ�
	            	 if(((IufoFormat)format).isHasPercent()==TableConstant.TRUE)
	                	 digits += 2;
	            	 retValue=new Double(UfoPublic1.roundDouble(((Double)retValue).doubleValue(), digits));
	             }
    		}
    		else if (value instanceof Integer){
    			retValue=Double.valueOf(value.toString());
    		}
    	}
    	return retValue;
    }
    
	/* (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getSplitAreaNames(com.ufsoft.table.IArea)
	 */
	public String[] getSplitAreaNames(IArea objArea) {
		ArrayList list = m_CellsModel.seperateArea((AreaPosition) objArea);
		String[] returnValue = new String[list.size()];
		for (int i = 0; i < returnValue.length; i++) {
			AreaPosition area = (AreaPosition) list.get(i);
			returnValue[i] = area.toString();
		}
		return returnValue;
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getMaxRow()
	 */
	public int getMaxRow() {
		return m_CellsModel.getMaxRow();
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getMaxCol()
	 */
	public int getMaxCol() {
		return m_CellsModel.getMaxCol();
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getID()
	 */
	public String getID() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getDataset(java.lang.String)
	 */
	public DataSet getDataset(String strDatasetId) {
		if (strDatasetId == null) {
			return m_TableDataset;
		} else {
			return (DataSet) m_DataSets.get(strDatasetId);
		}
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getDatasByMeta(java.lang.String, java.lang.String)
	 */
	public Hashtable getDatasByMeta(String strDatasetId, String metaDataID) {
		DataSet ds = getDataset(strDatasetId);
		Hashtable result = null;
		if (ds != null) {
			IMetaData MetaKey = ds.getKey();
			IMetaData metaValue = ds.getMetaData(metaDataID);
			Object[] keys = ds.getMetaDataValues(MetaKey);
			Object[] values = ds.getMetaDataValues(metaValue);
			if (keys != null && values != null && keys.length == values.length) {
				result = new Hashtable();
				for (int i = 0; i < values.length; i++) {
					result.put(keys[i], values[i]);
				}
			}
		}
		return result;
	}

	/* ����������ڣ�ĳ��Ԫ���ݶ�Ӧ����ֵ.���紫��ָ��pk����ô�ָ���������ڵ���ֵ.
	 * (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getMainDataByMeta(java.lang.String)
	 */
	public UfoVal getMainDataByMeta(String metaDataID) {
//		�����������ݼ��ϡ�
		if(m_TableDataset!=null){
			IMetaData meta = m_TableDataset.getMetaData(metaDataID);
			Object[] values = m_TableDataset.getMetaDataValues(meta);
			if(values!=null&&values.length>0&&values[0]instanceof UfoVal){
				return (UfoVal)values[0];
			}
		}
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#getKeyMetaID(java.lang.String)
	 */
	public String getKeyMetaID(String strDatasetId) {
		DataSet ds = getDataset(strDatasetId);
		String result = null;
		if (ds != null) {
			IMetaData MetaKey = ds.getKey();
			if(MetaKey!=null){
				result = MetaKey.getID();
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.script.datachannel.ITableData#hasFormulaInArea(com.ufsoft.table.IArea)
	 */
	public boolean hasFormulaInArea(IArea a) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
     * ���ݵ�ǰ�������������㶯̬������������չ���ȡ��λ��
     * @param area
     * @return
     */
    public IArea getRealAreaAfterDataExtend(IArea area){
    	if(m_objDynAreaCalcParam == null)
    		return area;
    	
    	boolean direction = m_objDynAreaCalcParam.getDirection() == Header.ROW;
    	int dRow = direction ? 
    				(m_objDynAreaCalcParam.getStepRow() * m_objDynAreaCalcParam.getUnitDataRowIndex()) : 
    				 m_objDynAreaCalcParam.getStepRow();
    	int dCol = direction ? 
    				 m_objDynAreaCalcParam.getStepCol() : 
    				(m_objDynAreaCalcParam.getStepCol() * m_objDynAreaCalcParam.getUnitDataRowIndex());
    	return area.getMoveArea(dRow, dCol);
    }
    
	/**
	 * ���ض�̬�����ݼ��������
	 * @return IUFODynAreaDataParam
	 */
	public IUFODynAreaDataParam getDynAreaCalcParam() {
		return m_objDynAreaCalcParam;
	}
	/**
	 * ���ö�̬�����ݼ��������
	 * @param dynAreaDataParam 
	 */
	public void setDynAreaCalcParam(IUFODynAreaDataParam dynAreaDataParam) {
		m_objDynAreaCalcParam = dynAreaDataParam;
		
	}
	/**
	 * �����̬�����ݼ��������
	 */
	public void removeDynAreaCalcParam(){
		m_objDynAreaCalcParam = null;
	}
	
	/**
     * ͨ�������ö�Ӧ��Ԫ���ϡ��������е���ϵ�Ԫֻ����һ������׵�Ԫ��
     * 						�����������ĳ��ϵ�Ԫһ���֣�Ҳֻ����һ������׵�Ԫ
     * @param area
     * @return
     */
    private ArrayList<CellPosition> getSeperateCellPos(IArea area){
    	CellPosition startCell = area.getStart();
		CellPosition endCell = area.getEnd();
		ArrayList<CellPosition> list = new ArrayList<CellPosition>();
		int startRow = startCell.getRow();
		int startCol = startCell.getColumn();
		int endRow = endCell.getRow();
		int endCol = endCell.getColumn();
		for (int row = startRow; row <= endRow; row++) {
			for (int col = startCol; col <= endCol; col++) {
				CellPosition cellPos = CellPosition.getInstance(row, col);
				AreaPosition areaPos = m_CellsModel.getCombinedCellArea(cellPos);
				if (areaPos!=null && list.contains(areaPos.getStart())==false) {
					list.add(areaPos.getStart());
				}
			}
		}
		return list;
    }
    
	/**
     * ���ܣ����ݳ�ʼ����õ���չ���ʵ������
     * @param oriArea
     * @return AreaPosition
     */
    private IArea getRealArea(IArea oriArea){
    	return DynAreaOperUtil.getRealArea(oriArea, m_CellsModel);
    }
    
    /**
	 * ���ָ��λ���Ƿ��ڶ�̬��
	 * @param oriPos ��Ԫ���ʽλ��
	 * @return
	 */
	public boolean isInDynArea(CellPosition oriPos){
		IArea realArea = DynAreaCell.getRealArea(oriPos, m_CellsModel);
		return DynAreaOperUtil.isInDynArea(m_CellsModel, realArea.getStart());
	}

	public UfoVal[] getAreaDataCondValForStatics(IArea area, UfoExpr acond, AreaExprCalcEnv env) 
		throws CmdException {
		// @edit by wangyga at 2009-8-18,����08:58:31 ����������֧�ֶ�̬��ȡ��
		ArrayList<CellPosition> list  = getSeperateCellPos(area);		
		ArrayList<UfoVal> values = new ArrayList<UfoVal>();
		for (CellPosition cellPos: list) {
			IArea realArea = DynAreaOperUtil.getRealArea(cellPos, m_CellsModel);
			//�����ʽ�ڶ�̬���У������ö�̬�����㻷��
			if(DynAreaOperUtil.isInDynArea(m_CellsModel, realArea.getStart())){
				DynAreaCell dynAreaCell = DynAreaOperUtil.getDynAreaCellByPos(m_CellsModel, realArea.getStart());
				AreaPosition[] unitAreas = dynAreaCell.getUnitAreas();
				int nUnitAreas = unitAreas.length;
				int stepRow = -1, stepCol = -1;
				ReportDataChannel dataChannel = (ReportDataChannel)env.getDataChannel();
				for(int i = 0; i < nUnitAreas; i++){
					stepRow = dynAreaCell.getDynAreaVO().isRowDirection() ? 
							dynAreaCell.getDynAreaVO().getOriArea().getHeigth() : 0;
					stepCol = dynAreaCell.getDynAreaVO().isRowDirection() ?
							0 : dynAreaCell.getDynAreaVO().getOriArea().getWidth();
					dataChannel.setDynAreaCalcParam(new IUFODynAreaDataParam(stepRow, stepCol, i, null, dynAreaCell.getDirection(), null));
					UfoVal[] condval = acond.calcExpr(env, 0, -1);
					dataChannel.removeDynAreaCalcParam();
	            	values.addAll(Arrays.asList(condval));
				}
				
			}else{//������������
	    		UfoVal[] condval = acond.calcExpr(env, 0, -1);
	    		values.addAll(Arrays.asList(condval));
	    	}
		}
		return values.toArray(new UfoVal[0]);
	}
}
