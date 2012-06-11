package com.ufsoft.report.sysplugin.edit;

import java.awt.Component;
import java.awt.datatransfer.Clipboard;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.KeyStroke;

import nc.vo.pub.lang.UFDouble;

import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic1;
import com.ufsoft.script.base.UfoDouble;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellSelection;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CombinedAreaModel;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.BorderPlayRender;

/**
 * <pre>
 * </pre>
 * 
 * �༭��������У�����
 * 
 * @author �����
 * @version Create on 2008-4-14
 */
public class EditExt extends AbsActionExt {
	
	/** ���а�����:0:ȫ����1:���ݣ�2:��ʽ */
	private int iClipType = 0;
	/** �༭����: 2:���У�3:������4����ʽˢ */
	private int iEditType = 0;
	/** ������ */
	private UfoReport report;
	private UFOTable table;
	
	private static final String GROUP_EDIT = "edit";

	public EditExt(UfoReport report, int editType, int clipType) {
		super();
		this.report = report;
		this.table = report.getTable();
		this.iClipType = clipType;
		this.iEditType = editType;
	}

	public static EditExt getInstance(UfoReport report, int editType,
			int clipType) {
		return new EditExt(report, editType, clipType);
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				if(!isEnabled(null))//��ֹ��ݼ�
					return;
				BorderPlayRender.controlPlay(report.getTable().getCells());// ��������
				copy(getClipType(), getEditType());
			}
		};
	}

	public Object[] getParams(UfoReport container) {
		return new Object[] { container };
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
		ArrayList<ActionUIDes> actionDesList = new ArrayList<ActionUIDes>();
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(getName(iClipType));
		uiDes.setPaths(new String[] { MultiLang.getString(GROUP_EDIT),
				getPath(iEditType) });
		uiDes.setGroup(MultiLang.getString(GROUP_EDIT));
		if (iEditType == EditParameter.CUT && iClipType == EditParameter.CELL_ALL) {
			uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					KeyEvent.CTRL_MASK));
			
			ActionUIDes uiDes1 = new ActionUIDes();
	        uiDes1.setName(MultiLang.getString("miufo1000654"));
	        uiDes1.setImageFile("reportcore/cut.gif");
	        uiDes1.setToolBar(true);
	        uiDes1.setGroup(MultiLang.getString(GROUP_EDIT));
	        uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					KeyEvent.CTRL_MASK));
	        actionDesList.add(uiDes1);
	        
	        ActionUIDes uiDes2 = new ActionUIDes();
	        uiDes2.setName(MultiLang.getString("miufo1000654"));
	        uiDes2.setImageFile("reportcore/cut.gif");
	        uiDes2.setPopup(true);
	        uiDes2.setGroup(MultiLang.getString(GROUP_EDIT));
	        uiDes2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_MASK));
	        actionDesList.add(uiDes2);
		}
		if (iEditType == EditParameter.COPY && iClipType == EditParameter.CELL_ALL) {
			uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					KeyEvent.CTRL_MASK));
			
			ActionUIDes uiDes1 = new ActionUIDes();
			uiDes1.setName(MultiLang.getString("miufo1000653"));
			uiDes1.setImageFile("reportcore/copy.gif");
			uiDes1.setToolBar(true);
	        uiDes1.setGroup(MultiLang.getString(GROUP_EDIT));
	        uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					KeyEvent.CTRL_MASK));
	        actionDesList.add(uiDes1);
	        
	        ActionUIDes uiDes2 = new ActionUIDes();
	        uiDes2.setName(MultiLang.getString("miufo1000653"));
	        uiDes2.setImageFile("reportcore/copy.gif");
	        uiDes2.setPopup(true);
	        uiDes2.setGroup(MultiLang.getString(GROUP_EDIT));
	        uiDes2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK));
	        actionDesList.add(uiDes2);

		}
		actionDesList.add(uiDes);
		return actionDesList.toArray(new ActionUIDes[0]);
	}

	/**
	 * ��ò˵�����
	 * 
	 * @param clipType:���а�����
	 * @return String
	 */
	private String getName(int clipType) {
		String strName = null;
		switch (clipType) {
		case EditParameter.CELL_ALL:
			strName = MultiLang.getString("miufo1000362");
			break;
		case EditParameter.CELL_CONTENT:
			strName = MultiLang.getString("uiuforep0001001");
			break;
		case EditParameter.CELL_FORMAT:
			strName = MultiLang.getString("uiuforep0001002");
			break;
		}
		return strName;

	}

	/**
	 * ���·��
	 * 
	 * @param editType:�༭����
	 * @return String
	 */
	private String getPath(int editType) {
		String strPath = null;
		switch (editType) {
		case EditParameter.CUT:
			strPath = MultiLang.getString("miufo1000654");
			break;
		case EditParameter.COPY:
			strPath = MultiLang.getString("miufo1000653");
			break;
		}
		return strPath;
	}

	public void copy(int clipType, int editType) {
		copy(clipType, editType, clipType == UFOTable.CELL_ALL
				|| clipType == UFOTable.CELL_CONTENT);
	}

	/**
	 * �¼��Ƿ���ע�����ɷ���
	 * 
	 * @param clipType:ȫ���������ݣ���ʽ��editType�����У����ƣ���ʽˢ ��dispatch
	 *            ��cut���õ�ʱ��Ϊfalse
	 */
	private void copy(int clipType, int editType, boolean dispatch) {
		
		//liuyy+�� ��ѡ���ܡ�
		AreaPosition[] areas = getSelectedAreas();		
		
		AreaPosition area = null;
		if(areas!=null&&areas.length > 0){
			area = areas[0];
		}
		
		if(area == null){
			return;
		}
		
		String strExcelContent = getExcelContent(area);
		Cell[][] selectedCells = AbsChoosePaste.getCells(area, getCellsModel());
		
		EditParameter editParam = new EditParameter(clipType, editType, area,selectedCells);
		editParam.setAreaInfo(EditParameter.COMBINED_CELL, getCombinedAreaModel().getCombineCells(area));//������ϵ�Ԫ�����ڱ����֮�临��
		
		//��װ�༭����
		CellSelection sel = new CellSelection(editParam,strExcelContent);
				
		// ��װ�����¼�.
		UserUIEvent event = new UserUIEvent(this, editType, sel,
				editParam);
		UFOTable table = report.getTable();
		// �������¼�.
		if (dispatch) {//��ʽˢʱ����Ҫ�ɷ��¼�
			if(table.checkEvent(event)){
				table.fireEvent(event);
			}else{
				BorderPlayRender.stopPlay(table.getCells());
				return;
			}			
		}

		Clipboard m_Clipboard = table.getClipboard();// ��ü��а�
		if (m_Clipboard == null) {
			return;
		}
		
		m_Clipboard.setContents(sel, null);
	}

	/**
	 * ����������д�������Լ���ѡ������
	 * @return
	 */
	protected AreaPosition[] getSelectedAreas(){
		return getCellsModel().getSelectModel().getSelectedAreas();
	}
	
	protected CellsModel getCellsModel(){
		return report.getCellsModel();
	}
	
	private CombinedAreaModel getCombinedAreaModel(){
		return CombinedAreaModel.getInstance(getCellsModel());
	}
	
	/**
	 * ���л��߸���ת��Ϊexcel�ܹ�ʶ��ĸ�ʽ
	 * @param area
	 * @return
	 */
	private String getExcelContent(AreaPosition area){
		CellsModel cellsModel = report.getCellsModel();
		Cell[][] cells = cellsModel.getCells(area);
		StringBuffer strContent = new StringBuffer();
		if (cells != null) {
			for (int i = 0; i < cells.length; i++) {
				Cell[] cLine = cells[i];
				if (cLine != null) {
					for (int j = 0; j < cLine.length; j++) {
						if (cLine[j] != null) {
							Object value = cLine[j].getValue();
							double dbVal=0;
							if (cLine[j].getFormat() instanceof IufoFormat && value!=null && (value instanceof Double || value instanceof UfoDouble || value instanceof UFDouble)){
								try{
									if (value instanceof Double)
										dbVal=((Double)value).doubleValue();
									else if (value instanceof UfoDouble)
										dbVal=((UfoDouble)value).doubleValue();
									else
										dbVal=((UFDouble)value).doubleValue();
								}catch(Exception e){
									dbVal=0;
								}
							}
							if (dbVal!=0){
								int iDecimal=((IufoFormat)cLine[j].getFormat()).getDecimalDigits();
								if (iDecimal<0)
									iDecimal=2;
								strContent.append(UfoPublic1.doubleToString(dbVal,iDecimal));
							}else
								strContent.append(value == null ? "" : value.toString());

							if (j != cLine.length - 1) {
								strContent.append("\t");
							}
						}
					}
				}

				if (i != cells.length - 1) {
					strContent.append("\n");
				}
			}
		}
		return strContent.toString();
	}

	/*
	 * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
	 */
	public String[] getPath() {
		return new String[] { MultiLang.getString("miufo1000653") };
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
	 */
	public String getImageFile() {
		return "copy.gif";
	}

	// ���ݣ���ʽ��ȫ��
	private int getClipType() {
		return iClipType;
	}

	// ���У����ƣ���ʽˢ
	private int getEditType() {
		return iEditType;
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
	 */
	public boolean isEnabled(Component focusComp) {
//		return StateUtil.isAnchorEditable(table.getCellsModel());// StateUtil.isCellsPane(null,focusComp);
		return StateUtil.isAreaSel(report,focusComp);
	}
}
