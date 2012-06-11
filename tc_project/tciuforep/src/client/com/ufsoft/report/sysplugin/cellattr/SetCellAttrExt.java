package com.ufsoft.report.sysplugin.cellattr;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.IUFOLogger;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.IVerify.VerifyType;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;

/**
 * ϵͳԤ�Ʋ�����ܵ㣺���õ�Ԫ����
 * 
 * @author caijie
 * @since 3.1
 */
public class SetCellAttrExt extends AbsActionExt{//,IPopupMenuExt {
    /** �ѷ���*/
	private JMenuItem m_mainItem,m_popupItem;
    private UfoReport m_report;//������
    /** �������ͣ�����*/
    public static int UPDATE_AREA_FORMAT = 0; 
    /** �������ͣ���*/
    public static int UPDATE_ROW_FORMAT = 1; 
    /** �������ͣ���*/
    public static int UPDATE_COLUMN_FORMAT = 2; 
    /** �������ͣ�ȫ��*/
    public static int UPDATE_TABLE_FORMAT = 3; 
    /**
     * ��ȡ��Ԫ���ԶԻ�������Ҫ��IufoFormat�Ĵ�ֱ����ʱ������ÿ����Ԫ���и��Եı߿����ԣ�
     * ��Ҫͨ��ÿ����Ԫ����߿���ұ߿����ϳɵ�Ԫ����
     * �����������Ԫ����һ��ʱ�������Щ������ұ߿�
     */
      public static final int MIDDLE_RIGHT = 53;
    /**
     * ��ȡ��Ԫ���ԶԻ�������Ҫ��IufoFormat�Ĵ�ֱ����ʱ������ÿ����Ԫ���и��Եı߿����ԣ�
     * ��Ҫͨ��ÿ����Ԫ����߿���ұ߿����ϳɵ�Ԫ����
     * �����������Ԫ����һ��ʱ�������Щ�������߿�
     */
    public static final int  MIDDLE_LEFT = 52; 
    /**
     * ��ȡ��Ԫ���ԶԻ�������Ҫ��IufoFormat��ˮƽ����ʱ������ÿ����Ԫ���и��Եı߿����ԣ�
     * ��Ҫͨ��ÿ����Ԫ���ϱ߿���±߿����ϳɵ�Ԫ����
     * �����������Ԫ����һ��ʱ�������Щ����ĵױ߿�
     */
    public static final int  MIDDLE_BOTTOM = 51; 
    /**
     * ��ȡ��Ԫ���ԶԻ�������Ҫ��IufoFormat��ˮƽ����ʱ������ÿ����Ԫ���и��Եı߿����ԣ�
     * ��Ҫͨ��ÿ����Ԫ���ϱ߿���±߿����ϳɵ�Ԫ����
     * �����������Ԫ����һ��ʱ�������Щ������ϱ߿�
     */
    public static final int  MIDDLE_TOP = 50; 
//    private int m_AreaType = TableConstant.UNDEFINED; //��������:UPDATE_AREA_FORMAT/UPDATE_ROW_FORMAT/UPDATE_COLUMN_FORMAT/UPDATE_TABLE_FORMAT
    /**ѡ�������еĵ�Ԫ�����飬������ѡ�������Լ������µĵ�Ԫ������CellPosition*/
	private CellPosition[] m_selectedCells = null;
	/**ѡ����������е�Ԫ��ѡ�����е�ȫ�����и�ʽ��Ϣ�ĵ�Ԫ�����ѡ�����������������ȫ���и�ʽ��Ϣ�ĵ�Ԫ	����CellPosition */
	private CellPosition[] m_dealCells = null;
	/** Ҫ�������Ե���	*/
	private int[] m_selectedRows = null;
	/**Ҫ�������Ե��� */
	private int[] m_selectedCols = null;
	
//	private CellsModel m_cellsModel;
	/** �Ƿ�ѡ��ȫ��*/
	private boolean m_isSelectedAllTable = false;
	/** ��������ʽ�ĵ�Ԫ�б� add by guogang 2007-7-2 */
	Hashtable<CellPosition,Object> conditionList = new Hashtable<CellPosition,Object>();
    /**
     * ���캯��  
     */
    public SetCellAttrExt(UfoReport rep) {
        if (rep == null) {
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000824"));//�Ƿ��ı����߲��� 
        }
        m_report = rep;        
    }
    /**
     * ��ȡ��������Ԫ���ڵı������ģ��
     * @return CellsModel
     */
    protected CellsModel getCellsModel() {
        return m_report.getCellsModel();
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getName()
     */
    public String getName() {

        return MultiLang.getString("uiuforep0000870");//��Ԫ����
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getHint()
     */
    public String getHint() {

        return MultiLang.getString("uiuforep0000871");//���õ�Ԫ����
    }

 

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
     */
    public String getImageFile() {

        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getAccelerator()
     */
    public KeyStroke getAccelerator() {

    	return null;
    }

//
//    /*
//     * @see com.ufsoft.report.plugin.ICommandExt#getRMenuPos()
//     */
//    public int getRMenuPos() {
//
//        return UFOTable.POPUP_CELLSPANEL;
//    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {
        
        return new SetCellAttrCmd(this.getReport());
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
     * modify by guogang 2007-6-1 
     * ���¹���getParams()���������빫���Ķ������Ա�ͨ�����Ǹ÷���ʹ��ͬʱ�ʺ϶ԶԻ���������˵��Ĵ���
     */
    public Object[] getParams(UfoReport container) {
		Object[] params = new Object[8 + 1];
		try {
			// computeSelCellPosition(selectModel);
			params=getCellParams(params);
			m_selectedCells = getSelectCells();
			boolean bSingleSelected = false;
			if (m_selectedCells == null || m_selectedCells.length < 2) {
				bSingleSelected = true;
			}

			Format format = computeFormat();
			CellPropertyDialog dlg = getCellPropertyDialog(getReport(), format,
					isTypeLocked(), bSingleSelected);
			dlg.setVisible(true);
			if (dlg.getResult() == UfoDialog.ID_OK) {
				params[0] = dlg.getPropertyCache();

				params[8] = dlg.getPropertyExtended();// �����������չ����
				return params;
			}
			return null;

		} catch (Exception e) {
			AppDebug.debug(e);
			IUFOLogger.getLogger(this).fatal(
					MultiLang.getString("uiuforep0000872") + e.toString());// ���õ�Ԫ����ʧ��
			UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000872"),
					getReport(), e);// ���õ�Ԫ����ʧ��
		}

		return null;
	}
    /**
	 * ��ȡ��Ҫ�����ĵ�Ԫ�����ͺ�λ��
	 * modify by guogang 2007-6-1 ��getParams()�г���ķ������Ա�������getParamsʱ����
	 * @param params
	 * @return
	 */
    public Object[] getCellParams(Object[] params) {
		int paramsIndex = 1;// �������
		SelectModel selectModel = getCellsModel().getSelectModel();
		m_selectedCells = getSelectCells();
		m_selectedRows = selectModel.getSelectedRow();
		m_selectedCols = selectModel.getSelectedCol();

		// �л��л�����, ����getSelectedCells�д�����. liuyy.
		m_dealCells = m_selectedCells;

		// ע����������˳������ѡ���л�����ʱ����ѡ��ģ���л��Զ������׻���ͷ��Ԫ��Ϊê�����Ϊѡ��Ԫ
		// ���Ե��У����У��뵥Ԫ�������ʱ����ڸ�ʽ�������⣬����Ҫ�����õ�Ԫ�ģ��������У����У�

		/** ****************�����еĸ�ʽ************************** */
		if ((m_selectedRows != null) && (m_selectedRows.length > 0)) {
			params[paramsIndex++] = new Character('r');
			params[paramsIndex++] = m_selectedRows;
		}

		/** ****************�����еĸ�ʽ************************** */
		else if ((m_selectedCols != null) && (m_selectedCols.length > 0)) {
			params[paramsIndex++] = new Character('c');
			params[paramsIndex++] = m_selectedCols;
		}

		/** ****************��������ĸ�ʽ************************** */
		else if (selectModel.isSelectAll()) {
			params[paramsIndex++] = new Character('t');

		}

		/** ****************��������ĸ�ʽ************************** */
		else if ((m_selectedCells != null) && (m_selectedCells.length > 0)) {
			params[paramsIndex++] = new Character('a');
			params[paramsIndex++] = m_selectedCells;
		}
		return params;
	}
    /**
     * ��ȡ��Ԫ�������öԻ���
     * @param report �������ڵı���
     * @param format ����ǰ�ĵ�Ԫ����
     * @param isTypeLocked  ѡȡ��Ԫ�Ƿ�Ϊ��������
     * @param bSingleSelected ѡȡ��Ԫ�Ƿ�Ϊ������Ԫ
     * @return
     */
    protected CellPropertyDialog getCellPropertyDialog(UfoReport report, Format format, boolean isTypeLocked, boolean bSingleSelected){
    	CellPropertyDialog dlg = new CellPropertyDialog(report, format, isTypeLocked, bSingleSelected);  // computeFormat()
    	return dlg;
    }

    /**
	 * @return ����UfoReport��
	 */
    public UfoReport getReport() {
        return m_report;
    }    
   
//    /**
//	 * �������ѡ��ʽ�µ���ѡ��ȫ����Ԫ��
//	 * ��������У������������е�Ԫ��
//	 * ��������У������������е�Ԫ��
//	 * ���ѡ��������������������е�Ԫ��
//	 */
//    @Deprecated 
//	private void computeSelCellPosition(SelectModel selectModel) {
//	    ArrayList<CellPosition> tmp = new ArrayList<CellPosition>();
//		/** ******ȷ�����ͣ������С��С�����****** */	
//	    if(selectModel.isSelectAll()){//ѡ��������	     
//	        m_isSelectedAllTable = true;
//			for (int i = 0; i < getCellsModel().getRowNum(); i++) 
//				for (int j = 0; j < getCellsModel().getColNum(); j++) 
////				    if(m_tableModel.getFormat(CellPosition.getInstance(i, j)) != null)
//				        tmp.add(CellPosition.getInstance(i, j));				
//				   	
//	    }else{
//	        m_isSelectedAllTable = false;
//	        //����Ԫ
//	         m_selectedCells = AreaCommonOpr.getCellsPositions(new AreaPosition[] {selectModel.getSelectedArea()});
//	        if(m_selectedCells != null){
//	            for(int i = 0; i < m_selectedCells.length; i++){
//	                tmp.add(m_selectedCells[i]);
//	            }
//	        }
//	   //--------------------ѡ��ģ�͵�getSelectedCell�Ѿ��ĳɷ��ذ��������������еĵ�Ԫ�����´������-------------     
//////	        ѡ���е����е�Ԫ
////	        m_selectedRows = selectModel.getSelectedRow();
////	        if(m_selectedRows != null){
////	            for (int i = 0; i < m_selectedRows.length; i++) 	        
////	                for (int j = 0; j < this.getCellsModel().getColNum(); j++) 
////	                    tmp.add(CellPosition.getInstance(m_selectedRows[i], j));
////	            
////	        }
////	        //ѡ���е����е�Ԫ
////	        m_selectedCols = selectModel.getSelectedCol();
////	        if(m_selectedCols != null){
////	            for (int i = 0; i < m_selectedCols.length; i++) 
////					for (int j = 0; j < getCellsModel().getRowNum(); j++) 
////					    tmp.add(CellPosition.getInstance(j,m_selectedCols[i]));
////	        }
//	         
//	       
//	    }	
//	    
//	    m_dealCells = tmp.toArray(new CellPosition[tmp.size()]);
////	    if(m_dealCells == null){
////	    	
////	    }
////	    if(tmp.isEmpty()){
////	        m_dealCells = null;
////	    }else{
////	        m_dealCells = new CellPosition[tmp.size()];
////	        for(int i = 0; i < tmp.size(); i++){
////	            m_dealCells[i] = (CellPosition) tmp.get(i);
////	        }
////	    }
//	    
//	}
	
	/**
	 * ������߿���Ϣʱ,�ӵ�Ԫ��������ѡ������Ҫ�����ĵ�Ԫ���ϡ� 
	 * @return com.ufsoft.m_tableModel.CellPosition[] ��Ҫ�����ĵ�Ԫ
	 * @param nType
	 */
	private CellPosition[] getAimCells(int direction) { 
//		Vector<CellPosition> tmp = new Vector<CellPosition>();
		CellPosition[] m_dealCells = getSelectCells();
		switch (direction) {
			case Format.TOPLINE ://�ϱ��ߺ���ɫ:�������������Ϸ��ĵ�Ԫ��	
				return getCellsModel().filterCells(m_dealCells, PropertyType.TLType);

			case Format.BOTTOMLINE ://�׶��ߺ���ɫ:�������������·��ĵ�Ԫ��		
				return getCellsModel().filterCells(m_dealCells, PropertyType.BLType);

			case Format.LEFTLINE : //����ߺ���ɫ:��������������ߵĵ�Ԫ��		
				return getCellsModel().filterCells(m_dealCells, PropertyType.LLType);

			case Format.RIGHTLINE : //�Ҷ��ߺ���ɫ:�������������ұߵĵ�Ԫ��	
				return getCellsModel().filterCells(m_dealCells, PropertyType.RLType);

			case Format.HORLINE ://ˮƽ�ߺ���ɫ:���������г�ȥ���ϱߺ����±ߵĵ�Ԫ��
				return getCellsModel().filterCells(m_dealCells, PropertyType.HLType);

			case Format.VERLINE ://��ֱ�ߺ���ɫ:���������г�ȥ���Ҷ˺�����ĵ�Ԫ��
				return getCellsModel().filterCells(m_dealCells, PropertyType.VLType);
				
//			case Format.DIAGONAL_LINE ://�Խ��ߺ���ɫ:�����жԽ��ߵĵ�Ԫ��
//				return getCellsModel().filterCells(m_dealCells, PropertyType.DLType);				

//			case SetCellAttrExt.MIDDLE_RIGHT ://��Ԫ������ұ߿�:���������г�ȥ���Ҷ˵�Ԫ�����൥Ԫ				
//			    for (int i = 0; i < m_dealCells.length; i++) {
//					if (isInLastCol(m_dealCells[i])) {
//						
//					} else if (!isInSelCellPos(CellPosition.getInstance(m_dealCells[i].getRow(), m_dealCells[i].getColumn() + 1))) {
//						
//					}else{
//					    tmp.add(m_dealCells[i]);
//					}
//				}
//				return (CellPosition[]) tmp.toArray(new CellPosition[0]);
//			case SetCellAttrExt.MIDDLE_LEFT://��Ԫ�������߿�:���������г�ȥ����˵�Ԫ�����൥Ԫ	
//				return getCellsModel().filterCells(m_dealCells, PropertyType.VLType);
//			    for (int i = 0; i < m_dealCells.length; i++) {
//					if (isInFirstCol(m_dealCells[i])) {
//						
//					} else if (!isInSelCellPos(CellPosition.getInstance(m_dealCells[i].getRow(), m_dealCells[i].getColumn() - 1))) {
//						
//					}else{
//					    tmp.add(m_dealCells[i]);
//					}
//				}
//				return (CellPosition[]) tmp.toArray(new CellPosition[0]);	
//			case SetCellAttrExt.MIDDLE_TOP ://��Ԫ������ϱ߿�:���������г�ȥ���϶˵�Ԫ�����൥Ԫ				
//			    for (int i = 0; i < m_dealCells.length; i++) {
//					if (isInFirstRow(m_dealCells[i])) {
//						
//					} else if (!isInSelCellPos(CellPosition.getInstance(
//					        m_dealCells[i].getRow() - 1, m_dealCells[i]
//									.getColumn()))) {
//						
//					}else{
//					    tmp.add(m_dealCells[i]);
//					}
//				}
//				return (CellPosition[]) tmp.toArray(new CellPosition[0]);
//			case SetCellAttrExt.MIDDLE_BOTTOM://��Ԫ������±߿�:���������г�ȥ���϶˵�Ԫ�����൥Ԫ				
//			    for (int i = 0; i < m_dealCells.length; i++) {
//					if (isInLastRow(m_dealCells[i])) {
//						
//					} else if (!isInSelCellPos(CellPosition.getInstance(
//							m_dealCells[i].getRow() + 1, m_dealCells[i]
//									.getColumn()))) {
//						
//					}else{
//					    tmp.add(m_dealCells[i]);
//					}
//				}
//				return (CellPosition[]) tmp.toArray(new CellPosition[0]);		
			default : //�Ǳ߿���Ϣʱ������������
				return m_dealCells;
		}
	}
		
	/**
	 * ��ȡѡ������������� caijie 2004-11-4
	 * 
	 * @param direction
	 *            ��Ԫ��ı߿���
	 * @see PropertyType
	 * @return int[]
	 */
	private int[] getShowLineFromCells(int direction) {
		CellPosition[] cellPositions;
		int[] lineProperty = {TableConstant.UNDEFINED, TableConstant.UNDEFINED};		
		switch (direction) {
			//      ȷ���ϱ߿�����,����getAimCells()�������ص�cells������ֻ������Щ���Ϸ��ĵ�Ԫ��,
			//��ֱ��ȡcells�����е��ϱ��߾Ϳ���
			case Format.TOPLINE : //ȷ���ϱ߿�����
			    cellPositions = getAimCells(Format.TOPLINE);
				for (int i = 0; i < cellPositions.length; i++) {				    
					int[] values = getShowFormat(cellPositions[i],Format.TOPLINE);					
					if (i == 0) { //��һ��ѭ���ó�ֵ
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //��������ʾ���
						break;
					}
				}
				break;

			//          ȷ���±߿�����,����getAimCells()�������ص�cells������ֻ������Щ���·��ĵ�Ԫ��,
			//��ֱ��ȡcells�����е��±��߾Ϳ���
			case Format.BOTTOMLINE : //ȷ���±߿�����
			    cellPositions = getAimCells(Format.BOTTOMLINE);
				for (int i = 0; i < cellPositions.length; i++) {
				    int[] values = getShowFormat(cellPositions[i],Format.BOTTOMLINE);
					if (i == 0) {
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //��������ʾ���
						break;
					}
				}
				break;

			//          ȷ����߿�����,����getAimCells()�������ص�cells������ֻ������Щ���󷽵ĵ�Ԫ��,
			//��ֱ��ȡcells�����е�����߾Ϳ���
			case Format.LEFTLINE : //ȷ����߿�����
			    cellPositions = getAimCells(Format.LEFTLINE);
				for (int i = 0; i < cellPositions.length; i++) {
				    int[] values = getShowFormat(cellPositions[i],Format.LEFTLINE);
					if (i == 0) {
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //��������ʾ���
						break;
					}
				}
				break;

			//          ȷ���ұ߿�����,����getAimCells()�������ص�cells������ֻ������Щ���ҷ��ĵ�Ԫ��,
			//��ֱ��ȡcells�����е��ұ��߾Ϳ���
			case Format.RIGHTLINE : //ȷ���ұ߿�����
			    cellPositions = getAimCells(Format.RIGHTLINE);
				for (int i = 0; i < cellPositions.length; i++) {
				    int[] values = getShowFormat(cellPositions[i],Format.RIGHTLINE);
					if (i == 0) {
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //��������ʾ���
						break;
					}
				}
			
				break;
			//ȷ����߿�����,����getAimCells()�������ص�cells������ֻ������Щ�м��еĵĵ�Ԫ��,
			//��ֱ��ȡcells�����е����±���
			case Format.HORLINE :
			    cellPositions = getAimCells(Format.HORLINE);
				for (int i = 0; i < cellPositions.length; i++) {
				    int[] values = getShowFormat(cellPositions[i],Format.TOPLINE);
					if (i == 0) {
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //��������ʾ���
						break;
					}
				}
	//			����ϱ��߶�һ�£����������±����Ƿ�Ҳһ��
				if(lineProperty[0] != 7 || lineProperty[1] != 10264988){
//				    cells = getAimCells(SetCellAttrExt.MIDDLE_BOTTOM);
				    for (int i = 0; i < cellPositions.length; i++) {
					    int[] values = getShowFormat(cellPositions[i],Format.BOTTOMLINE);
						 if (lineProperty[0] != values[0]|| lineProperty[1] != values[1]) {
							lineProperty[0] = 7;
							lineProperty[1] = 10264988; //��������ʾ���
							break;
						}
					}  
				}
				break;
				
			//          ȷ�����߿�����,����getAimCells()�������ص�cells������ֻ������Щ�м��еĵ�Ԫ��,
			//��ֱ��ȡcells�����е������ұ���
			case Format.VERLINE : //ȷ�����߿�����
			    cellPositions = getAimCells(Format.VERLINE);
				for (int i = 0; i < cellPositions.length; i++) {
				    int[] values = getShowFormat(cellPositions[i],Format.RIGHTLINE);
					if (i == 0) {
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //��������ʾ���
						break;
					}
				}
//			����ұ��߶�һ�£���������������Ƿ�Ҳһ��
				if(lineProperty[0] != 7 || lineProperty[1] != 10264988){
	//			    cells = getAimCells(SetCellAttrExt.MIDDLE_LEFT);
				    for (int i = 0; i < cellPositions.length; i++) {
					    int[] values = getShowFormat(cellPositions[i],Format.LEFTLINE);
						 if (lineProperty[0] != values[0]|| lineProperty[1] != values[1]) {
							lineProperty[0] = 7;
							lineProperty[1] = 10264988; //��������ʾ���
							break;
						}
					}  
				}
				break;
				
				//  ȷ���Խ�������,����getAimCells()�������ص�cells������ֻ�����жԽ��ߵĵ�Ԫ��,
				//��ֱ��ȡcells�����еĶԽ��߾Ϳ��� 
				case Format.DIAGONAL_LINE : 
				case Format.DIAGONAL2_LINE : 		
//				    cellPositions = m_dealCells;//getAimCells(Format.DIAGONAL_LINE);
					cellPositions = getSelectCells();
					for (int i = 0; i < cellPositions.length; i++) {
					    int[] values = getShowFormat(cellPositions[i], direction);
						if (i == 0) {
							lineProperty = values;
						} else if (lineProperty[0] != values[0]
								|| lineProperty[1] != values[1]) {
							lineProperty[0] = 7;
							lineProperty[1] = 10264988; //��������ʾ���
							break;
						}
					}
				
					break;
					
			default :
				throw new IllegalArgumentException(MultiLang.getString("uiuforep0000778"));//�������Ͳ�ͬ
		}
		return lineProperty;
	}

	

	/**
	 * �ж�һ����Ԫλ���Ƿ�����ѡ������������ �������ڣ�(2004-5-19 13:28:09)
	 * 
	 * @param cell
	 * @return boolean
	 */
	public boolean isInSelCellPos(CellPosition cell) {
	    if (m_isSelectedAllTable) {
			return true;
		} 
	    m_selectedCells = getSelectCells();
		for (int i = 0; i < m_selectedCells.length; i++) 
			if (m_selectedCells[i].compareTo(cell) == 0) 
				return true;


		if (isInSelectedRows(cell.getRow()) && cell.getColumn() >= 0
				&& cell.getColumn() < getCellsModel().getColNum()) 
			return true;
	

		if (isInSelectedCols(cell.getColumn()) && cell.getRow() >= 0
				&& cell.getRow() < getCellsModel().getRowNum()) 
			return true;
		
		return false;
	}

	/**
	 * �ж�һ������λ���Ƿ�����ѡ���������� �������ڣ�(2004-5-19 13:28:09)
	 */
	private boolean isInSelectedRows(int index) {
	    if(m_selectedRows == null) 
	        return false;
		for (int i = 0; i < m_selectedRows.length; i++) {
			if (index == m_selectedRows[i]) {
				return true;
			}
		}
		return false;
	}
	/**
	 * �ж�һ������λ���Ƿ�����ѡ���������� �������ڣ�(2004-5-19 13:28:09)
	 */
	private boolean isInSelectedCols(int index) {
	    if(m_selectedCols == null) 
	        return false;
		for (int i = 0; i < m_selectedCols.length; i++) {
			if (index == m_selectedCols[i]) {
				return true;
			}
		}
		return false;
	}
	/**
	 * ��Ԫλ���Ƿ�������
	 * @param cellPos
	 * @return
	 */
	private boolean isInFirstRow(CellPosition cellPos) {
		return cellPos.getRow() == 0;
	}
	/**
	 * ��Ԫλ���Ƿ���ĩ��
	 * @param cellPos
	 * @return
	 */
	private boolean isInFirstCol(CellPosition cellPos) {
		return cellPos.getColumn() == 0;
	}
	/**
	 * ��Ԫλ���Ƿ��������
	 * @param cellPos
	 * @return
	 */
	private boolean isInLastRow(CellPosition cellPos) {
		return cellPos.getRow() == getCellsModel().getRowNum() - 1;
	}
	/**
	 * ��Ԫλ���Ƿ��������
	 * @param cellPos
	 * @return
	 */
	private boolean isInLastCol(CellPosition cellPos) {
		return cellPos.getColumn() == getCellsModel().getColNum() - 1;
	}

//	/**
//	 * �ж�ѡ��������ָ��͹ؼ��ֵ����� -1:��ָ��͹ؼ���;0:����ֵ��;1:���ַ���;2:����ֵ��Ҳ���ַ��͡� �������ڣ�(2004-5-19
//	 * 13:28:09)
//	 * 
//	 * @return int
//	 */
//	private int getMeasureAndKeyTypeInSel() {
//		boolean hasDigit = false;
//		boolean hasChar = false;
//		//�ж�ָ������
//		java.util.Hashtable measureHT = m_tableModel.getMeasureTable();
//		Enumeration measureEnum = measureHT.keys();
//		while (measureEnum.hasMoreElements()) {
//			CellPosition cell = (CellPosition) measureEnum.nextElement();
//			if (isInSelCellPos(cell)) {
//				MeasureVO vo = (MeasureVO) measureHT.get(cell);
//				if (vo.getType() == MeasureVO.TYPE_NUMBER) {
//					hasDigit = true;
//				} else { //�ַ��ͱ������Ͷ���Ϊ���ַ���
//					hasChar = true;
//				}
//				if (hasDigit && hasChar) {
//					return 2;
//				}
//			}
//		}
//		//�жϹؼ������͡�
//		for (int i = 0; i < m_dealCells.length; i++) {
//			if (m_tableModel.getKeyWordAt(m_dealCells[i]) != null) {
//				//�ؼ��ֵ��������Ͷ���Ϊ���ַ����ͣ��ַ����͡��������͡�ʱ������
//				hasChar = true;
//				break; //��ȷ�����ַ��ͣ����ü�������ȥ
//			}
//		}
//		if (hasDigit && hasChar) {
//			return 2;
//		} else if (hasChar) {
//			return 1;
//		} else if (hasDigit) {
//			return 0;
//		} else {
//			return -1;
//		}
//	}

	/**
	 * ����ָ��λ�õ�Ԫ����ʾ��ʽ
	 * ����ǰ��Ԫ�ı߿�����û�ж��壬���������ڵ�Ԫ�ı߿����ԣ��ӽ����Ͽ���ǰ��Ԫ�ƺ��б߿�����
	 * �÷��������ڵ�Ԫȡ���߿�����ֵ�������Ԫ����û�ж���ñ߿�ֵ���������ڵ�Ԫ�ı߿�����ֵΪ��Ϊ����ֵ
	 */
	private int[] getShowFormat(CellPosition cellPos, int direction){	    
	    Format cellFormat = getCellsModel().getRealFormat(cellPos);
	    if(cellFormat == null) 
	        cellFormat = new IufoFormat();
	    int[] result = new int[2];
	    result[0] = cellFormat.getLineType(direction);
	    
//	    
	    Color lineColor = cellFormat.getLineColor(direction);
	    if(lineColor != null){
	    	result[1] = lineColor.getRGB();
	    } else {
	    	 result[1] = TableConstant.UNDEFINED;
	    }
	    
	    return result;
	    
//	    
//	    if(cellFormat.getLineColor(direction) != null){
//	        result[1] = cellFormat.getLineColor(direction).getRGB();
//	    }else{
//	        result[1] = TableConstant.UNDEFINED;
//	    }
//	     
//	    CellPosition pos;
//	    Format tmpFormat;	  
//	    switch(direction){
//	    case Format.TOPLINE://�ϱ߿�
//	        if(cellPos.getRow() != 0) {
//	 	       if(cellFormat.getLineType(Format.TOPLINE) == TableConstant.UNDEFINED
//	 		            || cellFormat.getLineColor(Format.TOPLINE) ==  null){
//	 	           pos = CellPosition.getInstance(cellPos.getRow() -1, cellPos.getColumn());
//	 	           tmpFormat = getCellsModel().getRealFormat(pos);	   
//	 	           if(tmpFormat != null){
//	 	              result[0] = tmpFormat.getLineType(Format.BOTTOMLINE);
//                        if (tmpFormat.getLineColor(Format.BOTTOMLINE) != null) {
//                            result[1] = tmpFormat.getLineColor(Format.BOTTOMLINE)
//                                    .getRGB();
//                        } else {
//                            result[1] = TableConstant.UNDEFINED;
//                        }		 	          
//	 	           }	 	           
//	 	       }	            
//	 	    }
//	        break;
//	    case Format.LEFTLINE://��߿�
//	        if(cellPos.getColumn() != 0) {
//	 	       if(cellFormat.getLineType(Format.LEFTLINE) == TableConstant.UNDEFINED
//	 		            || cellFormat.getLineColor(Format.LEFTLINE) ==  null){
//	 	           pos = CellPosition.getInstance(cellPos.getRow(), cellPos.getColumn() - 1);
//	 	           tmpFormat = getCellsModel().getRealFormat(pos);	
//	 	          if (tmpFormat != null) {
//                        result[0] = tmpFormat.getLineType(Format.RIGHTLINE);
//                        if (tmpFormat.getLineColor(Format.RIGHTLINE) != null) {
//                            result[1] = tmpFormat.getLineColor(Format.RIGHTLINE)
//                                    .getRGB();
//                        } else {
//                            result[1] = TableConstant.UNDEFINED;
//                        }	
//                    }
//	 		       
//	 	       }	            
//	 		 }
//	        break;
//	    case Format.BOTTOMLINE://�±߿�
//	        if(!isInLastRow(cellPos)) {
//			       if(cellFormat.getLineType(Format.BOTTOMLINE) == TableConstant.UNDEFINED
//				            || cellFormat.getLineColor(Format.BOTTOMLINE) ==  null){
//			           pos = CellPosition.getInstance(cellPos.getRow() + 1, cellPos.getColumn());
//			           tmpFormat = getCellsModel().getRealFormat(pos);	   
//			           if(tmpFormat != null){
//			               result[0] = tmpFormat.getLineType(Format.TOPLINE);
//			               if (tmpFormat.getLineColor(Format.TOPLINE) != null) {
//	                            result[1] = tmpFormat.getLineColor(Format.TOPLINE)
//	                                    .getRGB();
//	                        } else {
//	                            result[1] = TableConstant.UNDEFINED;
//	                        }		 
//			           }			           		       
//			       }	            
//			    }
//	        break;
//	    case Format.RIGHTLINE://�ұ߿�
//	        if(!isInLastCol(cellPos)) {
//			       if(cellFormat.getLineType(Format.RIGHTLINE) == TableConstant.UNDEFINED
//				            || cellFormat.getLineColor(Format.RIGHTLINE) ==  null){
//			           pos = CellPosition.getInstance(cellPos.getRow(), cellPos.getColumn() + 1);
//			           tmpFormat = getCellsModel().getRealFormat(pos);	 
//			           if(tmpFormat != null){
//			               result[0] = tmpFormat.getLineType(Format.LEFTLINE);
//			               if (tmpFormat.getLineColor(Format.LEFTLINE) != null) {
//	                            result[1] = tmpFormat.getLineColor(Format.LEFTLINE)
//	                                    .getRGB();
//	                        } else {
//	                            result[1] = TableConstant.UNDEFINED;
//	                        }	
//			           }			           			       
//			       }	            
//			    }
//	        break;
//	    }
//	    
//	    return result;    
	
	}

	/**
	 * ��CellsModel�л�ȡÿ����Ԫ��getRealFormat(),����ÿ����Ԫ��Format��
	 * �������������Ԫ��IufoFormat��һ��,�򷵻صĶ�ӦֵΪTableConstant.DIFFERENT
	 * �����и�ʽ���и�ʽ�����ʽ�ĺϳɣ���ͨ�����������ĵ�Ԫ��getShowFormat�ĺϳ���ʵ��
	 * 
	 */
    public Format computeFormat() {
        Format result = new IufoFormat(); 
        //modify by wangyga 2008-9-2 ������д������getSelectCells()
        CellPosition[] m_dealCells = getSelectCells();
        
	    if(m_dealCells == null || m_dealCells.length == 0)
	        return null;
	    	    
	    ArrayList<Format> list = new ArrayList<Format>();
	    Format tmp = null;
       //modify by guogang 2007-7-2
	    
	    Object conditionTmp=null;
	    for(int i = 0; i < m_dealCells.length; i++){
	        tmp = getCellsModel().getRealFormat(m_dealCells[i]); //getRealFormat  liuyy
	        if(tmp != null) {
	        	list.add(tmp);
	        	if(tmp.isCondition()){
	        		conditionTmp=getCellsModel().getBsFormat(m_dealCells[i],"ConditionFormat");
	        		if(conditionTmp!=null)
	        			conditionList.put(m_dealCells[i], conditionTmp);
	        	}
	        }
	    }
	    //modify end
	    if(list.isEmpty())
	        return result;
	  
	    Format[] allCellFormats = (Format[]) list.toArray(new Format[0]);
	    
	    result = (Format) allCellFormats[0].clone();
	    result.combine(allCellFormats, IufoFormat.DIFFERENT_METHOD);
	    int[] lineProp = {TableConstant.UNDEFINED, TableConstant.UNDEFINED};
	    //ȷ���߿�����	    
	    for(int i = 0; i < 8; i++){	      
	        lineProp = getShowLineFromCells(i);
	        result.setLineType(i, lineProp[0]);
	        if(lineProp[1] ==  TableConstant.UNDEFINED){
	            result.setLineColor(i, null);
	        }else{
	            result.setLineColor(i,new Color(lineProp[1]));
	        }
	       
	    }
	    return result;
    }
    
    private boolean isTypeLocked(){
    	// @edit by wangyga at 2009-3-18,����02:25:00 ����̬������ı䵥Ԫ��������
    	if(m_report.getOperationState() == UfoReport.OPERATION_INPUT){
    		return true;
    	}
    	CellPosition [] m_dealCells = getSelectCells();
	    if(m_dealCells == null || m_dealCells.length == 0)
	        return false;	    
	    for(int i = 0; i < m_dealCells.length; i++){
	    	if(getCellsModel().isVerify(m_dealCells[i], VerifyType.TYPE_LOCKED,true)){
	    		return true;
	    	}
	    }
	    return false;
    }

	/**
	 * �ɱ�������д
	 * @return
	 */
	protected CellPosition[] getSelectCells(){
		return getReport().getCellsModel().getSelectModel().getSelectedCells();
	}
		
    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
     */
    public String[] getPath() {
        return null;
    }


    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#getMainMenuItem()
     */
    public JMenuItem getMainMenuItem() {
        return m_mainItem;
    }

    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#setMainMenuItem(javax.swing.JMenuItem)
     */
    public void setMainMenuItem(JMenuItem item) {
        m_mainItem = item;        
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return isVisiable(focusComp);
    }

    /*
     * @see com.ufsoft.report.plugin.IPopupMenuExt#getPopupMenuItem()
     */
    public JMenuItem getPopupMenuItem() {
        return m_popupItem;
    }

    /*
     * @see com.ufsoft.report.plugin.IPopupMenuExt#setPopupMenuItem(javax.swing.JMenuItem)
     */
    public void setPopupMenuItem(JMenuItem item) {
        m_popupItem = item;        
    }

    /*
     * @see com.ufsoft.report.plugin.IPopupMenuExt#isVisiable(java.awt.Component)
     */
    public boolean isVisiable(Component focusComp) {
    	// @edit by wangyga at 2009-2-26,����02:05:01
//    	return StateUtil.isCellsPane(m_report, focusComp);  
    	return true;  
    }

    /*
     * @see com.ufsoft.report.plugin.IActionExt#getUIDes()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(MultiLang.getString("uiuforep0000870"));
        // @edit by wangyga at 2008-12-30,����08:56:11
        uiDes1.setImageFile("reportcore/cellformat.png");
        uiDes1.setPaths(new String[]{MultiLang.getString("format")});
        uiDes1.setGroup("IUFOMainFormat");
        uiDes1.setShowDialog(true);
        ActionUIDes uiDes2 = (ActionUIDes)uiDes1.clone();
        uiDes2.setPaths(new String[]{});
        uiDes2.setPopup(true);
        uiDes2.setGroup("IUFOMainFormat");
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
}