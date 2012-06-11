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
 * 系统预制插件功能点：设置单元属性
 * 
 * @author caijie
 * @since 3.1
 */
public class SetCellAttrExt extends AbsActionExt{//,IPopupMenuExt {
    /** 已废弃*/
	private JMenuItem m_mainItem,m_popupItem;
    private UfoReport m_report;//报表工具
    /** 区域类型：区域*/
    public static int UPDATE_AREA_FORMAT = 0; 
    /** 区域类型：行*/
    public static int UPDATE_ROW_FORMAT = 1; 
    /** 区域类型：列*/
    public static int UPDATE_COLUMN_FORMAT = 2; 
    /** 区域类型：全表*/
    public static int UPDATE_TABLE_FORMAT = 3; 
    /**
     * 获取单元属性对话框所需要的IufoFormat的垂直属性时，由于每个单元都有各自的边框属性，
     * 需要通过每个单元的左边框和右边框来合成单元属性
     * 当多个连续单元格在一起时，标记那些共享的右边框
     */
      public static final int MIDDLE_RIGHT = 53;
    /**
     * 获取单元属性对话框所需要的IufoFormat的垂直属性时，由于每个单元都有各自的边框属性，
     * 需要通过每个单元的左边框和右边框来合成单元属性
     * 当多个连续单元格在一起时，标记那些共享的左边框
     */
    public static final int  MIDDLE_LEFT = 52; 
    /**
     * 获取单元属性对话框所需要的IufoFormat的水平属性时，由于每个单元都有各自的边框属性，
     * 需要通过每个单元的上边框和下边框来合成单元属性
     * 当多个连续单元格在一起时，标记那些共享的底边框
     */
    public static final int  MIDDLE_BOTTOM = 51; 
    /**
     * 获取单元属性对话框所需要的IufoFormat的水平属性时，由于每个单元都有各自的边框属性，
     * 需要通过每个单元的上边框和下边框来合成单元属性
     * 当多个连续单元格在一起时，标记那些共享的上边框
     */
    public static final int  MIDDLE_TOP = 50; 
//    private int m_AreaType = TableConstant.UNDEFINED; //区域类型:UPDATE_AREA_FORMAT/UPDATE_ROW_FORMAT/UPDATE_COLUMN_FORMAT/UPDATE_TABLE_FORMAT
    /**选中区域中的单元格数组，不保含选中行列以及整表下的单元。类型CellPosition*/
	private CellPosition[] m_selectedCells = null;
	/**选中区域的所有单元和选择行列的全部含有格式信息的单元，如果选择了整表，则包含整表全部有格式信息的单元	类型CellPosition */
	private CellPosition[] m_dealCells = null;
	/** 要设置属性的行	*/
	private int[] m_selectedRows = null;
	/**要设置属性的列 */
	private int[] m_selectedCols = null;
	
//	private CellsModel m_cellsModel;
	/** 是否选择全表*/
	private boolean m_isSelectedAllTable = false;
	/** 有条件格式的单元列表 add by guogang 2007-7-2 */
	Hashtable<CellPosition,Object> conditionList = new Hashtable<CellPosition,Object>();
    /**
     * 构造函数  
     */
    public SetCellAttrExt(UfoReport rep) {
        if (rep == null) {
            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000824"));//非法的报表工具参数 
        }
        m_report = rep;        
    }
    /**
     * 获取所操作单元所在的表的数据模型
     * @return CellsModel
     */
    protected CellsModel getCellsModel() {
        return m_report.getCellsModel();
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getName()
     */
    public String getName() {

        return MultiLang.getString("uiuforep0000870");//单元属性
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#getHint()
     */
    public String getHint() {

        return MultiLang.getString("uiuforep0000871");//设置单元属性
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
     * 重新构造getParams()方法，抽离公共的动作，以便通过覆盖该方法使其同时适合对对话框和下拉菜单的处理
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

				params[8] = dlg.getPropertyExtended();// 用于子类的扩展属性
				return params;
			}
			return null;

		} catch (Exception e) {
			AppDebug.debug(e);
			IUFOLogger.getLogger(this).fatal(
					MultiLang.getString("uiuforep0000872") + e.toString());// 设置单元属性失败
			UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000872"),
					getReport(), e);// 设置单元属性失败
		}

		return null;
	}
    /**
	 * 获取所要操作的单元的类型和位置
	 * modify by guogang 2007-6-1 从getParams()中抽离的方法，以便在重载getParams时调用
	 * @param params
	 * @return
	 */
    public Object[] getCellParams(Object[] params) {
		int paramsIndex = 1;// 参数标记
		SelectModel selectModel = getCellsModel().getSelectModel();
		m_selectedCells = getSelectCells();
		m_selectedRows = selectModel.getSelectedRow();
		m_selectedCols = selectModel.getSelectedCol();

		// 行或列或区域, 均在getSelectedCells中处理了. liuyy.
		m_dealCells = m_selectedCells;

		// 注意参数传入的顺序，由于选择行或者列时报表选择模型中会自动以行首或列头单元作为锚点而成为选择单元
		// 所以当行（或列）与单元混合设置时会存在格式覆盖问题，所以要先设置单元的，后设置行（或列）

		/** ****************设置行的格式************************** */
		if ((m_selectedRows != null) && (m_selectedRows.length > 0)) {
			params[paramsIndex++] = new Character('r');
			params[paramsIndex++] = m_selectedRows;
		}

		/** ****************设置列的格式************************** */
		else if ((m_selectedCols != null) && (m_selectedCols.length > 0)) {
			params[paramsIndex++] = new Character('c');
			params[paramsIndex++] = m_selectedCols;
		}

		/** ****************设置整表的格式************************** */
		else if (selectModel.isSelectAll()) {
			params[paramsIndex++] = new Character('t');

		}

		/** ****************设置区域的格式************************** */
		else if ((m_selectedCells != null) && (m_selectedCells.length > 0)) {
			params[paramsIndex++] = new Character('a');
			params[paramsIndex++] = m_selectedCells;
		}
		return params;
	}
    /**
     * 获取单元属性设置对话框
     * @param report 操作所在的报表
     * @param format 操作前的单元属性
     * @param isTypeLocked  选取单元是否为类型锁定
     * @param bSingleSelected 选取单元是否为单个单元
     * @return
     */
    protected CellPropertyDialog getCellPropertyDialog(UfoReport report, Format format, boolean isTypeLocked, boolean bSingleSelected){
    	CellPropertyDialog dlg = new CellPropertyDialog(report, format, isTypeLocked, bSingleSelected);  // computeFormat()
    	return dlg;
    }

    /**
	 * @return 返回UfoReport。
	 */
    public UfoReport getReport() {
        return m_report;
    }    
   
//    /**
//	 * 计算各种选择方式下的所选的全部单元格。
//	 * 如果包含行，则计算该行所有单元格
//	 * 如果包含列，则计算该列所有单元格
//	 * 如果选则了整表，则计算整表所有单元格
//	 */
//    @Deprecated 
//	private void computeSelCellPosition(SelectModel selectModel) {
//	    ArrayList<CellPosition> tmp = new ArrayList<CellPosition>();
//		/** ******确定类型：区域、行、列、整表****** */	
//	    if(selectModel.isSelectAll()){//选择了整表	     
//	        m_isSelectedAllTable = true;
//			for (int i = 0; i < getCellsModel().getRowNum(); i++) 
//				for (int j = 0; j < getCellsModel().getColNum(); j++) 
////				    if(m_tableModel.getFormat(CellPosition.getInstance(i, j)) != null)
//				        tmp.add(CellPosition.getInstance(i, j));				
//				   	
//	    }else{
//	        m_isSelectedAllTable = false;
//	        //区域单元
//	         m_selectedCells = AreaCommonOpr.getCellsPositions(new AreaPosition[] {selectModel.getSelectedArea()});
//	        if(m_selectedCells != null){
//	            for(int i = 0; i < m_selectedCells.length; i++){
//	                tmp.add(m_selectedCells[i]);
//	            }
//	        }
//	   //--------------------选择模型的getSelectedCell已经改成返回包含所有整表整行的单元，以下代码多余-------------     
//////	        选择行的所有单元
////	        m_selectedRows = selectModel.getSelectedRow();
////	        if(m_selectedRows != null){
////	            for (int i = 0; i < m_selectedRows.length; i++) 	        
////	                for (int j = 0; j < this.getCellsModel().getColNum(); j++) 
////	                    tmp.add(CellPosition.getInstance(m_selectedRows[i], j));
////	            
////	        }
////	        //选择列的所有单元
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
	 * 当加入边框信息时,从单元数组中挑选出所需要操作的单元集合。 
	 * @return com.ufsoft.m_tableModel.CellPosition[] 需要操作的单元
	 * @param nType
	 */
	private CellPosition[] getAimCells(int direction) { 
//		Vector<CellPosition> tmp = new Vector<CellPosition>();
		CellPosition[] m_dealCells = getSelectCells();
		switch (direction) {
			case Format.TOPLINE ://上边线和颜色:返回数组中最上方的单元格	
				return getCellsModel().filterCells(m_dealCells, PropertyType.TLType);

			case Format.BOTTOMLINE ://底端线和颜色:返回数组中最下方的单元格		
				return getCellsModel().filterCells(m_dealCells, PropertyType.BLType);

			case Format.LEFTLINE : //左端线和颜色:返回数组中最左边的单元格		
				return getCellsModel().filterCells(m_dealCells, PropertyType.LLType);

			case Format.RIGHTLINE : //右端线和颜色:返回数组中最右边的单元格	
				return getCellsModel().filterCells(m_dealCells, PropertyType.RLType);

			case Format.HORLINE ://水平线和颜色:返回数组中除去最上边和最下边的单元格
				return getCellsModel().filterCells(m_dealCells, PropertyType.HLType);

			case Format.VERLINE ://垂直线和颜色:发挥数组中除去最右端和最左的单元格
				return getCellsModel().filterCells(m_dealCells, PropertyType.VLType);
				
//			case Format.DIAGONAL_LINE ://对角线和颜色:返回有对角线的单元格
//				return getCellsModel().filterCells(m_dealCells, PropertyType.DLType);				

//			case SetCellAttrExt.MIDDLE_RIGHT ://单元共享的右边框:返回数组中除去最右端单元的其余单元				
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
//			case SetCellAttrExt.MIDDLE_LEFT://单元共享的左边框:返回数组中除去最左端单元的其余单元	
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
//			case SetCellAttrExt.MIDDLE_TOP ://单元共享的上边框:返回数组中除去最上端单元的其余单元				
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
//			case SetCellAttrExt.MIDDLE_BOTTOM://单元共享的下边框:返回数组中除去最上端单元的其余单元				
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
			default : //非边框信息时返回整个区域
				return m_dealCells;
		}
	}
		
	/**
	 * 获取选定区域的线属性 caijie 2004-11-4
	 * 
	 * @param direction
	 *            单元格的边框线
	 * @see PropertyType
	 * @return int[]
	 */
	private int[] getShowLineFromCells(int direction) {
		CellPosition[] cellPositions;
		int[] lineProperty = {TableConstant.UNDEFINED, TableConstant.UNDEFINED};		
		switch (direction) {
			//      确定上边框类型,由于getAimCells()方法返回的cells数组中只包含那些最上方的单元格,
			//故直接取cells数组中的上边线就可以
			case Format.TOPLINE : //确定上边框类型
			    cellPositions = getAimCells(Format.TOPLINE);
				for (int i = 0; i < cellPositions.length; i++) {				    
					int[] values = getShowFormat(cellPositions[i],Format.TOPLINE);					
					if (i == 0) { //第一次循环置初值
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //交集的显示结果
						break;
					}
				}
				break;

			//          确定下边框类型,由于getAimCells()方法返回的cells数组中只包含那些最下方的单元格,
			//故直接取cells数组中的下边线就可以
			case Format.BOTTOMLINE : //确定下边框类型
			    cellPositions = getAimCells(Format.BOTTOMLINE);
				for (int i = 0; i < cellPositions.length; i++) {
				    int[] values = getShowFormat(cellPositions[i],Format.BOTTOMLINE);
					if (i == 0) {
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //交集的显示结果
						break;
					}
				}
				break;

			//          确定左边框类型,由于getAimCells()方法返回的cells数组中只包含那些最左方的单元格,
			//故直接取cells数组中的左边线就可以
			case Format.LEFTLINE : //确定左边框类型
			    cellPositions = getAimCells(Format.LEFTLINE);
				for (int i = 0; i < cellPositions.length; i++) {
				    int[] values = getShowFormat(cellPositions[i],Format.LEFTLINE);
					if (i == 0) {
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //交集的显示结果
						break;
					}
				}
				break;

			//          确定右边框类型,由于getAimCells()方法返回的cells数组中只包含那些最右方的单元格,
			//故直接取cells数组中的右边线就可以
			case Format.RIGHTLINE : //确定右边框类型
			    cellPositions = getAimCells(Format.RIGHTLINE);
				for (int i = 0; i < cellPositions.length; i++) {
				    int[] values = getShowFormat(cellPositions[i],Format.RIGHTLINE);
					if (i == 0) {
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //交集的显示结果
						break;
					}
				}
			
				break;
			//确定横边框类型,由于getAimCells()方法返回的cells数组中只包含那些中间行的的单元格,
			//故直接取cells数组中的上下边线
			case Format.HORLINE :
			    cellPositions = getAimCells(Format.HORLINE);
				for (int i = 0; i < cellPositions.length; i++) {
				    int[] values = getShowFormat(cellPositions[i],Format.TOPLINE);
					if (i == 0) {
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //交集的显示结果
						break;
					}
				}
	//			如果上边线都一致，则继续检查下边线是否也一致
				if(lineProperty[0] != 7 || lineProperty[1] != 10264988){
//				    cells = getAimCells(SetCellAttrExt.MIDDLE_BOTTOM);
				    for (int i = 0; i < cellPositions.length; i++) {
					    int[] values = getShowFormat(cellPositions[i],Format.BOTTOMLINE);
						 if (lineProperty[0] != values[0]|| lineProperty[1] != values[1]) {
							lineProperty[0] = 7;
							lineProperty[1] = 10264988; //交集的显示结果
							break;
						}
					}  
				}
				break;
				
			//          确定竖边框类型,由于getAimCells()方法返回的cells数组中只包含那些中间列的单元格,
			//故直接取cells数组中的左右右边线
			case Format.VERLINE : //确定竖边框类型
			    cellPositions = getAimCells(Format.VERLINE);
				for (int i = 0; i < cellPositions.length; i++) {
				    int[] values = getShowFormat(cellPositions[i],Format.RIGHTLINE);
					if (i == 0) {
						lineProperty = values;
					} else if (lineProperty[0] != values[0]
							|| lineProperty[1] != values[1]) {
						lineProperty[0] = 7;
						lineProperty[1] = 10264988; //交集的显示结果
						break;
					}
				}
//			如果右边线都一致，则继续检查左边线是否也一致
				if(lineProperty[0] != 7 || lineProperty[1] != 10264988){
	//			    cells = getAimCells(SetCellAttrExt.MIDDLE_LEFT);
				    for (int i = 0; i < cellPositions.length; i++) {
					    int[] values = getShowFormat(cellPositions[i],Format.LEFTLINE);
						 if (lineProperty[0] != values[0]|| lineProperty[1] != values[1]) {
							lineProperty[0] = 7;
							lineProperty[1] = 10264988; //交集的显示结果
							break;
						}
					}  
				}
				break;
				
				//  确定对角线类型,由于getAimCells()方法返回的cells数组中只包含有对角线的单元格,
				//故直接取cells数组中的对角线就可以 
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
							lineProperty[1] = 10264988; //交集的显示结果
							break;
						}
					}
				
					break;
					
			default :
				throw new IllegalArgumentException(MultiLang.getString("uiuforep0000778"));//方向类型不同
		}
		return lineProperty;
	}

	

	/**
	 * 判断一个单元位置是否在所选定的区域里里 创建日期：(2004-5-19 13:28:09)
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
	 * 判断一个行列位置是否在所选定的行列里 创建日期：(2004-5-19 13:28:09)
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
	 * 判断一个行列位置是否在所选定的行列里 创建日期：(2004-5-19 13:28:09)
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
	 * 单元位置是否是首行
	 * @param cellPos
	 * @return
	 */
	private boolean isInFirstRow(CellPosition cellPos) {
		return cellPos.getRow() == 0;
	}
	/**
	 * 单元位置是否是末行
	 * @param cellPos
	 * @return
	 */
	private boolean isInFirstCol(CellPosition cellPos) {
		return cellPos.getColumn() == 0;
	}
	/**
	 * 单元位置是否是最后行
	 * @param cellPos
	 * @return
	 */
	private boolean isInLastRow(CellPosition cellPos) {
		return cellPos.getRow() == getCellsModel().getRowNum() - 1;
	}
	/**
	 * 单元位置是否是最后列
	 * @param cellPos
	 * @return
	 */
	private boolean isInLastCol(CellPosition cellPos) {
		return cellPos.getColumn() == getCellsModel().getColNum() - 1;
	}

//	/**
//	 * 判断选定区域里指标和关键字的类型 -1:无指标和关键字;0:有数值型;1:有字符型;2:有数值型也有字符型。 创建日期：(2004-5-19
//	 * 13:28:09)
//	 * 
//	 * @return int
//	 */
//	private int getMeasureAndKeyTypeInSel() {
//		boolean hasDigit = false;
//		boolean hasChar = false;
//		//判断指标类型
//		java.util.Hashtable measureHT = m_tableModel.getMeasureTable();
//		Enumeration measureEnum = measureHT.keys();
//		while (measureEnum.hasMoreElements()) {
//			CellPosition cell = (CellPosition) measureEnum.nextElement();
//			if (isInSelCellPos(cell)) {
//				MeasureVO vo = (MeasureVO) measureHT.get(cell);
//				if (vo.getType() == MeasureVO.TYPE_NUMBER) {
//					hasDigit = true;
//				} else { //字符和编码类型都认为是字符。
//					hasChar = true;
//				}
//				if (hasDigit && hasChar) {
//					return 2;
//				}
//			}
//		}
//		//判断关键字类型。
//		for (int i = 0; i < m_dealCells.length; i++) {
//			if (m_tableModel.getKeyWordAt(m_dealCells[i]) != null) {
//				//关键字的三种类型都认为是字符类型：字符类型、参照类型、时间类型
//				hasChar = true;
//				break; //已确定有字符型，不用继续找下去
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
	 * 返回指定位置单元的显示格式
	 * 当当前单元的边框属性没有定义，而由于相邻单元的边框属性，从界面上看当前单元似乎有边框属性
	 * 该方法从相邻单元取出边框属性值，如果单元本身没有定义该边框值，则以相邻单元的边框属性值为作为返回值
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
//	    case Format.TOPLINE://上边框
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
//	    case Format.LEFTLINE://左边框
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
//	    case Format.BOTTOMLINE://下边框
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
//	    case Format.RIGHTLINE://右边框
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
	 * 从CellsModel中获取每个单元的getRealFormat(),复合每个单元的Format，
	 * 如果存在两个单元的IufoFormat不一致,则返回的对应值为TableConstant.DIFFERENT
	 * 对于行格式、列格式、表格式的合成，将通过其所包含的单元的getShowFormat的合成来实现
	 * 
	 */
    public Format computeFormat() {
        Format result = new IufoFormat(); 
        //modify by wangyga 2008-9-2 便于重写方法：getSelectCells()
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
	    //确定边框属性	    
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
    	// @edit by wangyga at 2009-3-18,下午02:25:00 数据态不允许改变单元数据类型
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
	 * 可被子类重写
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
    	// @edit by wangyga at 2009-2-26,下午02:05:01
//    	return StateUtil.isCellsPane(m_report, focusComp);  
    	return true;  
    }

    /*
     * @see com.ufsoft.report.plugin.IActionExt#getUIDes()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(MultiLang.getString("uiuforep0000870"));
        // @edit by wangyga at 2008-12-30,上午08:56:11
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