package com.ufsoft.iufo.fmtplugin.measure;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;

import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.key.KeyDefPlugin;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.insertdelete.DeleteInsertDialog;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.EditParameter;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.event.HeaderEvent;
import com.ufsoft.table.event.HeaderModelListener;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.re.CellRenderAndEditor;

/**
 * ָ������
 * @author wupeng
 * 2004-9-30
 */
public class MeasureDefPlugIn extends AbstractPlugIn implements HeaderModelListener,UserActionListner {
	/**��ʽ̬��ָ����չ�����Ƿ���ʾ*/
	private static boolean m_bMeasRendererVisible = true;
	public static boolean isMeasRendererVisible() {
		return m_bMeasRendererVisible;
	}
	public static void setMeasRendererVisible(boolean measRendererVisible) {
		m_bMeasRendererVisible = measRendererVisible;
	}
	
	static{
		CellRenderAndEditor.getInstance().registExtSheetRenderer(new MeasureDefRender());
		CellRenderAndEditor.getInstance().registExtSheetEditor(new MeasureDefEditor());
	}
	
	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
		getReport().getEventManager().addListener(this);
//		getReport().getTable().getCells().registExtSheetRenderer(new MeasureDefRenderer());
//		getReport().getTable().getCells().registExtSheetEditor(new MeasureDefEditor());
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	public IPluginDescriptor createDescriptor() {
		return new MeasureDescriptor(this);
	}



	/* (non-Javadoc)
	 * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
	 */
	public void userActionPerformed(UserUIEvent e) {		
		//�����¼���
	    switch (e.getEventType()){
	    	case UserUIEvent.PASTE:
	    	    processPasteEvent(e);
	    	    break;	 
	    	case UserUIEvent.DELETECELL:
	    		processDeleteCellEvent(e);
	    		break;
	    	case UserUIEvent.INSERTCELL:
	    		processInsertCellEvent(e);
	    		break;
	    }		
	}
	private void processInsertCellEvent(UserUIEvent e) {
		int insertType = (Integer)e.getOldValue();
		AreaPosition aimArea = (AreaPosition) e.getNewValue();
		boolean hor = insertType == DeleteInsertDialog.CELL_MOVE_RIGHT;
		AreaPosition extendDirArea = FormulaModel.getExtendDirectionArea(false, getCellsModel(), aimArea, hor);
		int dRow = hor ? 0 : aimArea.getHeigth();
		int dCol = hor ? aimArea.getWidth() : 0;
		getMeasureModel().moveMeasureImpl(extendDirArea,dRow,dCol);
	}


	private void processDeleteCellEvent(UserUIEvent e) {
		int delType = (Integer)e.getOldValue();
		AreaPosition aimArea = (AreaPosition) e.getNewValue();
		boolean hor = delType == DeleteInsertDialog.CELL_MOVE_LEFT;
		AreaPosition extendDirArea = FormulaModel.getExtendDirectionArea(false, getCellsModel(), aimArea, hor);
		int dRow = hor ? 0 : -aimArea.getHeigth();
		int dCol = hor ? -aimArea.getWidth() : 0;
		getMeasureModel().moveMeasureImpl(extendDirArea,dRow,dCol);
	}

	/**
	 * ����ճ���¼�.modify by ����� 2008-6-4 ͳһ�����Ӧճ���¼�ʱ����ò����ķ�ʽ
	 * ָ�괦������ճ��value,������������ָ�괦��valueֵ.
     * @param cellss
     */
    private void processPasteEvent(UserUIEvent e) {
    	Object object = e.getNewValue();
		EditParameter parameter = null;
		if(object instanceof EditParameter){
			parameter = (EditParameter)object;
		}
		AreaPosition areaSrc = parameter.getCopyArea();
		Cell[][] cells = getCellsModel().getCells(areaSrc);
        //�õ���ǰѡ�б�ҳ�Ľ��㵥Ԫ��
        CellPosition target = this.getCellsModel().getSelectModel().getAnchorCell();
        int rowStart = target.getRow();
        int colStart = target.getColumn();
        if (cells != null) {
            for (int i = 0; i < cells.length; i++) {
                Cell[] cLine = cells[i];
                if (cLine != null) {
                    for (int j = 0, colPos = colStart; j < cLine.length; j++, colPos++) {
                       CellPosition cellPos = CellPosition.getInstance(rowStart + i, colPos);
                       Cell cell = this.getCellsModel().getCell(cellPos);
                        if(cell != null && cell.getValue() != null && this.getMeasureModel().getMeasureVOByPos(cellPos)!= null){                           
                           cell.setValue(null);
                       }
                    }
                }
            }
        }      
    }


    /**
     * @return Returns the measureModel.
     */
    public MeasureModel getMeasureModel() {
        return CellsModelOperator.getMeasureModel(getCellsModel());
    }
    private DynAreaModel getDynAreaModel(){
    	return DynAreaModel.getInstance(getCellsModel());
    }

    
    public void headerCountChanged(HeaderEvent e) {	
		getMeasureModel().headerCountChanged(e);
		
		//modify chxw 2007-04-25 �ڸ�ʽ״̬�£�������������Ԫ��ʱ���������Ϊ��������
		if(e.isHeaderAdd()){
			int index = e.getStartIndex();
			AreaPosition area = null;
			if(e.isRow())
				area = AreaPosition.getInstance(index, 0, getCellsModel().getColNum(), 1);
			else
				area = AreaPosition.getInstance(0, index, 1, getCellsModel().getRowNum());
			
			Cell[][] cells = getCellsModel().getCells(area);
			resetCellDefaultFormat(cells);
		}
	}

    /**
     * �ڸ�ʽ״̬�£�������������Ԫ��ʱ���������Ϊ��������
     * @param cells
     */
    private void resetCellDefaultFormat(Cell[][] cells){
		if (cells != null) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null) {
					for (int j = 0; j < cells[i].length; j++) {
						Cell cell = cells[i][j];
						if (cell != null && cell.getFormat() != null)
							cell.getFormat().setCellType(TableConstant.CELLTYPE_SAMPLE);
					}
				}
			}
		}

	}
    
	public void headerPropertyChanged(PropertyChangeEvent e) {		
	}

	/**
	 * @i18n uiiufofmt00061=ɾ���������к���ָ���ؼ��֣�������ɾ����
//	 * modify by guogang ɾ����ʱ����ָ�������ƣ�ֻҪ��̬����������,,,,  liuyy+
	 */
	public String isSupport(int source, EventObject e) throws ForbidedOprException {
        if(e == null) return null;//modify by wangyga ָ�겻֧�ָ��ƻ����
        if(e instanceof UserUIEvent){
        	UserUIEvent ee = (UserUIEvent) e;
    		if (ee.getEventType() == UserUIEvent.COPY || ee.getEventType() == UserUIEvent.CUT) {
//    			isSupportCopy(ee);
    		}
        }
        		
        AreaPosition toDelArea = KeyDefPlugin.getDeleteAreaByEvent(e,getCellsModel());
        if(toDelArea != null && getMeasureModel().getMeasureVOByArea(toDelArea).size() != 0){
        	throw new ForbidedOprException(StringResource.getStringResource("uiiufofmt00061"));
        }
        
        return null;        
	}   

	/**
	 * add by wangyga 2008-7-17 
	 * @param e
	 * @return
	 * @throws ForbidedOprException 
	 * @i18n miufo00606=�Բ���ָ�겻֧�ָ��ƻ��߼��в���
	 */
	private String isSupportCopy(UserUIEvent e) throws ForbidedOprException{
		if (e == null) {
			throw new IllegalArgumentException(StringResource
					.getStringResource("miufo1000496"));// �������������Ϊ��
		}
		Object object = e.getNewValue();
		if (object instanceof EditParameter) {
			EditParameter editParameter = (EditParameter) object;
			if(editParameter == null) return null;
			
			AreaPosition copyArea = editParameter.getCopyArea();
			MeasureModel measureModel = getMeasureModel();
			CellPosition[] position = copyArea.split();
			for (CellPosition pos : position) {
				if(measureModel.hasMeasure(pos)){
					throw new ForbidedOprException(StringResource.getStringResource("miufo00606"));
				}
			}
			
		}

		return null;
	}
	
	public int getPriority(){
		  return HeaderModelListener.MAX_PRIORITY;
	}
	
}
 