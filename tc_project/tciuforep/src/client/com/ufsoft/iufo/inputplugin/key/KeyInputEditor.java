package com.ufsoft.iufo.inputplugin.key;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.text.JTextComponent;

import nc.pub.iufo.accperiod.IUFODefaultNCAccSchemeUtil;
import nc.pub.iufo.cache.TaskCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.pub.date.UFODate;
import nc.vo.iufo.task.TaskVO;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.editor.RepDataDoubleEditor;
import com.ufsoft.iufo.inputplugin.editor.RepDataStringEditor;
import com.ufsoft.iufo.inputplugin.inputcore.AbsCodeRefEditor;
import com.ufsoft.iufo.inputplugin.inputcore.AccPeriodRefComp;
import com.ufsoft.iufo.inputplugin.inputcore.AccPeriodYearRefComp;
import com.ufsoft.iufo.inputplugin.inputcore.IUFOTimeRefComp;
import com.ufsoft.iufo.inputplugin.inputcore.RefInfo;
import com.ufsoft.iufo.inputplugin.inputcore.StringEditor;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.AbstractRefEditor;
import com.ufsoft.table.re.IRefComp;
import com.ufsoft.table.re.ReadOnlyEditor;
import com.ufsoft.table.re.RefTextField;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.TimeRefComp;

/**
 * 关键字输入编辑器，非动态区关键字不允许编辑。
 * 
 * @author zzl 2005-6-22
 */
public class KeyInputEditor implements SheetCellEditor, IUfoContextKey {
	
	private class TimeEditor extends AbstractRefEditor {
		private KeyFmt m_editintKeyFmt;

		private String strAccPeriodType = null;
		
		private CellsModel cellsModel = null;

		TimeEditor() {
			super();
			editorComponent = new RefTextField();
			
			delegate = new EditorDelegate() {
				private static final long serialVersionUID = 1L;

				public void setValue(Object newValue) {
					((JTextField)editorComponent).setText((newValue != null) ? newValue.toString() : "");
				}

				public Object getCellEditorValue() {
					return ((JTextField)editorComponent).getText();
				}
				
				public void actionPerformed(ActionEvent e) {
					//编辑事件结束的时候会移除编辑组件
					CellsPane cp=null;
					if(editorComponent.getParent() instanceof CellsPane){
						cp=(CellsPane)editorComponent.getParent();
					}
					TimeEditor.this.stopCellEditing();
					
					RepDataEditor editor=RepDataDoubleEditor.getRepDataEditor(cp);
		            if(editor!=null)
		            	editor.processEnterAction(false);
		            else if (cp!=null)
		            	cp.getSelectionModel().setAnchorCell((CellPosition) cp.getSelectionModel().getAnchorCell().getMoveArea(1,0));
				}
			};
			((JTextField)editorComponent).addActionListener(delegate);
			m_refTextField=(RefTextField)getComponent();
		}

		@Override
		protected IRefComp getRefComp(CellsPane table, Object value, int row,
				int col) {
			cellsModel = table.getDataModel();
			CellPosition cellPos = CellPosition.getInstance(row, col);
			m_editintKeyFmt = (KeyFmt) cellsModel.getBsFormat(cellPos,
					KeyFmt.EXT_FMT_KEYINPUT);
			int iTimeType = m_editintKeyFmt.getType();
			if (iTimeType == KeyFmt.TYPE_TIME) {// 自然日期
				return new IUFOTimeRefComp(m_editintKeyFmt.getKeyPK(),null, (RefTextField) getComponent());
			} else if (iTimeType == KeyFmt.TYPE_ACC) {
				String strAccPreiodPk = null;// 会计期间PK
				strAccPeriodType = m_editintKeyFmt.getKeyPK();
				IContext contextVo = (IContext)table.getContext();
				MeasurePubDataVO mPubDataVo = null;
				RepDataEditor editor=RepDataDoubleEditor.getRepDataEditor(table);
				if (editor!=null){
					mPubDataVo=editor.getPubData();
					String strTaskPk = editor.getRepDataParam().getTaskPK();
					TaskCache taskCache = IUFOUICacheManager.getSingleton().getTaskCache();
					TaskVO taskVO = taskCache.getTaskVO(strTaskPk);
					strAccPreiodPk = taskVO.getAccPeriodScheme();
				}else if (contextVo != null) {
					Object tableInputTransObj = contextVo
							.getAttribute(TABLE_INPUT_TRANS_OBJ);
					TableInputTransObj inputTransObj = tableInputTransObj != null
							&& (tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj) tableInputTransObj
							: null;

					Object pubDataObj = contextVo
							.getAttribute(MEASURE_PUB_DATA_VO);
					mPubDataVo = pubDataObj != null
							&& (pubDataObj instanceof MeasurePubDataVO) ? (MeasurePubDataVO) pubDataObj
							: null;

					String strTaskPk = inputTransObj.getRepDataParam()
							.getTaskPK();
					TaskCache taskCache = IUFOUICacheManager.getSingleton()
							.getTaskCache();
					TaskVO taskVO = taskCache.getTaskVO(strTaskPk);
					strAccPreiodPk = taskVO.getAccPeriodScheme();
				}
				if (strAccPreiodPk == null
						|| strAccPreiodPk.trim().length() == 0) {
					strAccPreiodPk = IUFODefaultNCAccSchemeUtil.getInstance()
							.getIUFODefaultNCAccScheme();
				}
				String strTextValue = value != null ? value.toString() : null;
				String strCurPeroidYear = null;
				if (mPubDataVo != null
						&& mPubDataVo.getKeyGroup().getTTimeProp().equals(
								UFODate.NONE_PERIOD) == false) {
					strCurPeroidYear = mPubDataVo.getTTimeKeyValue();
				}
				if (KeyVO.ACC_YEAR_PK.equals(strAccPeriodType)) {// 会计年
					return new AccPeriodYearRefComp(strAccPreiodPk,
							strAccPeriodType, strTextValue);
				} else {// 会计季度和会计月
					return new AccPeriodRefComp(strAccPreiodPk,
							strAccPeriodType, strTextValue, strCurPeroidYear);
				}
			}
			return null;
		}

		public Object getCellEditorValue() {
			String inputValue = (String) super.getCellEditorValue();
			if (KeyVO.ACC_YEAR_PK.equals(strAccPeriodType)
					|| KeyVO.ACC_MONTH_PK.equals(strAccPeriodType)
					|| KeyVO.ACC_SEASON_PK.equals(strAccPeriodType)) {
				return inputValue;
			}
			if (inputValue == null || inputValue.trim().equals("")) {
				return inputValue;
			}

			Object[] mainTableTimeKey = getMainTableTimeKey();
			String mainTableTimePeriod = mainTableTimeKey[0] == null ? null
					: ((KeyFmt) mainTableTimeKey[0]).getTimePeriod();
			String subTableTimePeriod = m_editintKeyFmt.getTimePeriod();
			String mainTableTimeValue = (String) mainTableTimeKey[1];

			String validValue = getValidTimeValue(mainTableTimePeriod,
					subTableTimePeriod, mainTableTimeValue, inputValue);
			return validValue;
		}
		
		 private Object[] getMainTableTimeKey(){
		        Hashtable htKeyFmt = cellsModel.getBsFormats(KeyFmt.EXT_FMT_KEYINPUT);
		        String mainTableTimeValue = null;
		        KeyFmt mainTableTimeKey = null;
		        Enumeration enumeration = htKeyFmt.keys();
		        DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel);
		        //找主表时间关键字
		        while (enumeration.hasMoreElements()) {
		            CellPosition cellPos = (CellPosition) enumeration.nextElement();
		            if(!dynAreaModel.isInDynArea(cellPos)
		               && ((KeyFmt)htKeyFmt.get(cellPos)).getType() == KeyFmt.TYPE_TIME){
		                Object value = cellsModel.getCellValue(cellPos);
		                mainTableTimeValue = value==null?null:value.toString();
		                mainTableTimeKey = (KeyFmt)htKeyFmt.get(cellPos);
		                break;
		            }            
		        }
		        return new Object[]{mainTableTimeKey,mainTableTimeValue};
		    }
	}
	
	private class CodeRefEditor extends AbsCodeRefEditor{
		CodeRefEditor(){
			super();
			editorComponent = new RefTextField();
			
			delegate = new EditorDelegate() {
				private static final long serialVersionUID = 1L;

				public void setValue(Object newValue) {
					((RefTextField)editorComponent).setText((newValue != null) ? newValue.toString() : "");
				}

				public Object getCellEditorValue() {
					return ((RefTextField)editorComponent).getText();
				}
				
				public void actionPerformed(ActionEvent e) {
					//编辑事件结束的时候会移除编辑组件
					CellsPane cp=null;
					if(editorComponent.getParent() instanceof CellsPane){
						cp=(CellsPane)editorComponent.getParent();
					}
					CodeRefEditor.this.stopCellEditing();
					
					RepDataEditor editor=RepDataDoubleEditor.getRepDataEditor(cp);
		            if(editor!=null)
		            	editor.processEnterAction(false);
		            else if (cp!=null)
		            	cp.getSelectionModel().setAnchorCell((CellPosition) cp.getSelectionModel().getAnchorCell().getMoveArea(1,0));
				}
			};
			((RefTextField)editorComponent).addActionListener(delegate);
			 m_refTextField = (RefTextField) getComponent();
		}
		
		protected RefInfo getRefInfo(CellsPane table, int row, int col) {
			KeyFmt fmt = (KeyFmt) table.getDataModel()
					.getBsFormat(CellPosition.getInstance(row, col),
							KeyFmt.EXT_FMT_KEYINPUT);
			IContext context =  table.getContext();
			String strOrgPK = context.getAttribute(ORG_PK) == null ? null
					: (String) context.getAttribute(ORG_PK);

			if (fmt.getType() == KeyFmt.TYPE_DIC_UNIT) {
				RefInfo refInfo = new RefInfo(fmt.getType());
				refInfo.setOrgPK(strOrgPK);
				return refInfo;
			} else if (fmt.getType() == KeyFmt.TYPE_UNIT) {
				RefInfo info = new RefInfo(fmt.getType());
				info.setOrgPK(strOrgPK);
				String curUnitCode = (String) context.getAttribute(CUR_UNIT_CODE);
				info.setCurUnitCode(curUnitCode);
				return info;
			} else {
				return new RefInfo(fmt.getRefCodePK());
			}
		}
	}
	
	private SheetCellEditor proxy = new StringEditor();

	/**
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param row
	 * @param column
	 * @return
	 * @see com.ufsoft.table.re.SheetCellEditor#getTableCellEditorComponent(com.ufsoft.table.CellsPane,
	 *      java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(CellsPane table, Object value,
			boolean isSelected, int row, int column) {
		// Viewer view = (Viewer)SwingUtilities.getAncestorOfClass(Viewer.class,
		// table);
		// 非动态区域不能编辑
		if (!isInDynArea(row, column,table.getDataModel())) {
			proxy = new ReadOnlyEditor(
					new com.ufsoft.table.beans.UFOLabel());
		} else {
			KeyFmt fmt = (KeyFmt) table.getDataModel()
					.getBsFormat(CellPosition.getInstance(row, column),
							KeyFmt.EXT_FMT_KEYINPUT);
			if (fmt != null) {
				if (fmt.isRowNumKey()) {
					RowNumKeyEditor rowNumKeyEditor = new RowNumKeyEditor();
					rowNumKeyEditor.setCharLength(fmt.getCharLength());
					proxy = rowNumKeyEditor;
				} else if (fmt.getType() == KeyFmt.TYPE_CHAR) {
					StringEditor strEditor = new RepDataStringEditor();
					strEditor.setCharLength(fmt.getCharLength());
					proxy = strEditor;
				} else if (fmt.getType() == KeyFmt.TYPE_TIME
						|| fmt.getType() == KeyFmt.TYPE_ACC) {
					proxy =  new TimeEditor();
				} else if (fmt.getType() == KeyFmt.TYPE_UNIT
						|| fmt.getType() == KeyFmt.TYPE_DIC_UNIT
						|| fmt.getType() == KeyFmt.TYPE_CODE) {
					proxy = new CodeRefEditor();
				} else {
					throw new IllegalArgumentException();
				}
			}
		}
		final Component comp = proxy.getTableCellEditorComponent(table, value,
				isSelected, row, column);
		if (comp instanceof JTextComponent) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					((JTextComponent) comp).selectAll();
				}
			});
		}
		return comp;
	}

	public boolean isInDynArea(int row, int col,CellsModel cm) {
		return !(getDynAreaCell(CellPosition.getInstance(row, col),cm) == null);
	}

	private DynAreaCell getDynAreaCell(CellPosition pos,CellsModel cm) {
		DynAreaCell[] dynCells = getDynAreaCells(cm);
		for (int i = 0; i < dynCells.length; i++) {
			AreaPosition area = dynCells[i].getArea();
			if (area.contain(pos)) {
				return dynCells[i];
			}
		}
		return null;
	}

	/**
	 * 得到所有动态区域。
	 * 
	 * @return DynAreaCell[]
	 */
	public DynAreaCell[] getDynAreaCells(CellsModel cm) {
		// 暂时不做缓存，效率优化时再考虑。
		ArrayList list = new ArrayList();
		Iterator iter = cm.getAreaDatas().iterator();
		while (iter.hasNext()) {
			Object element = iter.next();
			if (element instanceof DynAreaCell) {
				list.add(element);
			}
		}
		return (DynAreaCell[]) list.toArray(new DynAreaCell[0]);
	}

	/**
	 * @return
	 * @see javax.swing.CellEditor#stopCellEditing()
	 */
	public boolean stopCellEditing() {
		return proxy.stopCellEditing();
	}

	/**
	 * @return
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return proxy.getCellEditorValue();
	}

	/**
	 * @see com.ufsoft.table.re.SheetCellEditor#getEditorPRI()
	 */
	public int getEditorPRI() {
		return 1;
	}

	/**
	 * @param anEvent
	 * @return
	 * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
	 */
	public boolean isCellEditable(EventObject anEvent) {
		return proxy.isCellEditable(anEvent);
	}

	/**
	 * @param anEvent
	 * @return
	 * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		return proxy.shouldSelectCell(anEvent);
	}

	/**
	 * 
	 * @see javax.swing.CellEditor#cancelCellEditing()
	 */
	public void cancelCellEditing() {
		proxy.cancelCellEditing();
	}

	/**
	 * @param l
	 * @see javax.swing.CellEditor#addCellEditorListener(javax.swing.event.CellEditorListener)
	 */
	public void addCellEditorListener(CellEditorListener l) {
		proxy.addCellEditorListener(l);

	}

	/**
	 * @param l
	 * @see javax.swing.CellEditor#removeCellEditorListener(javax.swing.event.CellEditorListener)
	 */
	public void removeCellEditorListener(CellEditorListener l) {
		proxy.removeCellEditorListener(l);
	}

	/**
	 * 根据主表和子表的时间类型，以及主表时间值和子表输入的时间值，得到修正后的子表时间值。
	 * 
	 * @param mainTableTimePeriod
	 * @param subTableTimePeriod
	 * @param mainTableTimeValue
	 * @param subTableInputVlaue
	 * @return String
	 */
	public String getValidTimeValue(String mainTableTimePeriod,
			String subTableTimePeriod, String mainTableTimeValue,
			String subTableInputVlaue) {
		// 根据主表时间区间，修正子表输入值为主表区间内。
		if (mainTableTimePeriod != null && subTableTimePeriod != null) {
			if (mainTableTimeValue != null) {
				String[] min_max = getMin_MaxValue(mainTableTimePeriod,
						mainTableTimeValue);
				subTableInputVlaue = getRuleTime(subTableInputVlaue);
				if (subTableInputVlaue.compareTo(min_max[0]) < 0) {
					subTableInputVlaue = min_max[0];
				} else if (subTableInputVlaue.compareTo(min_max[1]) > 0) {
					subTableInputVlaue = min_max[1];
				}
			} else {
				subTableInputVlaue = getRuleTime(subTableInputVlaue);
			}
		}
		// 根据子表时间区间，修正子表输入值为区间最后一天。
		if (subTableTimePeriod != null && subTableInputVlaue != null) {
			subTableInputVlaue = getMin_MaxValue(subTableTimePeriod,
					subTableInputVlaue)[1];
		}
		return subTableInputVlaue;

	}

	private String[] getMin_MaxValue(String timePeriod, String timeValue) {

		try {
			DateFormat.getDateInstance(DateFormat.DEFAULT,
					Locale.SIMPLIFIED_CHINESE).parse(timeValue);
		} catch (ParseException e) {
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
			throw new RuntimeException();
		}

		try {
			String start = TimeKeyUtil.getStartDay(timeValue, timePeriod);
			String end = TimeKeyUtil.getEndDay(timeValue, timePeriod);
			return new String[] { start, end };
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(date);
		// String minValue = null, maxValue = null;
		// if(timePeriod.equals(KeyFmt.YEAR_PERIOD)){
		// minValue = calendar.get(Calendar.YEAR) +"-01-01";
		// maxValue = calendar.get(Calendar.YEAR) +"-12-31";
		// }else if(timePeriod.equals(KeyFmt.HALFYEAR_PERIOD)){
		// if(calendar.get(Calendar.MONTH) < 6){//上半年
		// minValue = calendar.get(Calendar.YEAR) +"-01-01";
		// maxValue = calendar.get(Calendar.YEAR) +"-06-30";
		// }else{//下半年
		// minValue = calendar.get(Calendar.YEAR) +"-07-01";
		// maxValue = calendar.get(Calendar.YEAR) +"-12-31";
		// }
		// }else if(timePeriod.equals(KeyFmt.SEASON_PERIOD)){
		// if(calendar.get(Calendar.MONTH) < 3){//一季度
		// minValue = calendar.get(Calendar.YEAR) +"-01-01";
		// maxValue = calendar.get(Calendar.YEAR) +"-03-31";
		// }else if(calendar.get(Calendar.MONTH) < 6){//二季度
		// minValue = calendar.get(Calendar.YEAR) +"-04-01";
		// maxValue = calendar.get(Calendar.YEAR) +"-06-30";
		// }else if(calendar.get(Calendar.MONTH) < 9){//三季度
		// minValue = calendar.get(Calendar.YEAR) +"-07-01";
		// maxValue = calendar.get(Calendar.YEAR) +"-09-30";
		// }else{//四季度
		// minValue = calendar.get(Calendar.YEAR) +"-10-01";
		// maxValue = calendar.get(Calendar.YEAR) +"-12-31";
		// }
		// }else if(timePeriod.equals(KeyFmt.MONTH_PERIOD)){
		// minValue = calendar.get(Calendar.YEAR) +"-"+getMonth(calendar)+"-01";
		// maxValue = calendar.get(Calendar.YEAR)
		// +"-"+getMonth(calendar)+"-"+calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// }else if(timePeriod.equals(KeyFmt.DAY_PERIOD)){
		// minValue = calendar.get(Calendar.YEAR)
		// +"-"+getMonth(calendar)+"-"+getDay(calendar);
		// maxValue = minValue;
		// }else{
		// JOptionPane.showMessageDialog(m_report,"暂不支持周和旬的处理,请清空数据退出！");
		// throw new RuntimeException("暂不支持周和旬的处理！");
		// }
	}

	/**
	 * 处理加1，和一位数补足成2位数。
	 * 
	 * @return String
	 */
	private static String getMonth(Calendar calendar) {
		int month = calendar.get(Calendar.MONTH) + 1;
		String strMonth = "" + month;
		if (strMonth.length() < 2) {
			strMonth = "0" + strMonth;
		}
		return strMonth;
	}

	/**
	 * 一位数补足成2位数
	 * 
	 * @param calendar
	 * @return String
	 */
	private static String getDay(Calendar calendar) {
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String strDay = "" + day;
		if (strDay.length() < 2) {
			strDay = "0" + strDay;
		}
		return strDay;
	}

	/**
	 * 转换2005-111-01成2014-03-01
	 * 
	 * @param str
	 * @return String
	 */
	private static String getRuleTime(String str) {
		try {
			Date date = DateFormat.getDateInstance(DateFormat.DEFAULT,
					Locale.SIMPLIFIED_CHINESE).parse(str);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			str = calendar.get(Calendar.YEAR) + "-" + getMonth(calendar) + "-"
					+ getDay(calendar);
		} catch (ParseException e) {
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
			throw new RuntimeException();
		}
		return str;
	}

	public boolean isEnabled(CellsModel cellsModel, CellPosition cellPos) {
		return true;
	}
}
