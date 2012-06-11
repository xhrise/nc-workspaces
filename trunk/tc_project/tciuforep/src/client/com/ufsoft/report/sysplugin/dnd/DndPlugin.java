package com.ufsoft.report.sysplugin.dnd;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.event.MouseInputAdapter;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;
/**
 * 使表格支持拖拽，用于UFBI设计表格时。
 * 1.UFBI应注册其可拖拽类型(当一个单元格中有多种扩展格式时，取其最早注册的可拖拽类型)。
 * 2.鼠标放到单元格左上方时，可以开始拖动。
 * @author zzl 2005-4-21
 */
public class DndPlugin extends AbstractPlugIn {
    /**null表示不支持拖拽，""表示拖拽value，其他值表示指定的扩展格式。默认值是""*/
    private String m_dndDataType = "";
    private CellPosition m_dndPos;
    private boolean isInDndArea;
	private Cursor DND_CURSOR = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
	private Cursor REPORT_CURSOR;
    
	private MouseInputAdapter mia = new MouseInputAdapter(){    
	    
	    /**
	     * 当前参数Point是否在选择区的左上角点附近。
	     * @param p
	     * @return
	     */
		private boolean isInDndArea(Point p){
		    CellsPane cellsPane = getFocusCellsPane();
			if(cellsPane == null){
				return false;
			}
		    int row = getCellsPane().rowAtPoint(p);
		    int col = getCellsPane().columnAtPoint(p);
		    
			Rectangle rect = getCellsPane().getCellRect(CellPosition.getInstance(row,col), true);
			Point startPoint = new Point(rect.x, rect.y);
			if(startPoint.distance(p) < 5){
			    return true;
			}
			return false;
		}

        /**
		 * 得到当前焦点组件所在的CellsPane。
		 * @return CellsPane
		 */
		private CellsPane getFocusCellsPane(){		
			Component focusCom = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
			while(true){
				if(focusCom == null){
					return null;
				}
				if(focusCom instanceof CellsPane){
					return (CellsPane)focusCom;
				}
				focusCom = focusCom.getParent();			
			}
		}
		public void mouseMoved(MouseEvent e){
		    if(isInDndArea(e.getPoint())){
		        isInDndArea = true;
		        getReport().setCursor(DND_CURSOR);	
		    }else{
		        isInDndArea = false;
		        getReport().setCursor(REPORT_CURSOR);
		    }
		}		
	};

    

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup() {
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#store()
     */
    public void store() {
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#setReport(com.ufsoft.report.UfoReport)
     */
    public void setReport(UfoReport report) {
        super.setReport(report);
        REPORT_CURSOR = getReport().getCursor(); 
		getCellsPane().addMouseListener(mia);
		getCellsPane().addMouseMotionListener(mia);
		DndHandler.enableDndDrag(getCellsPane(),new CellsPaneDndAdapter(),DnDConstants.ACTION_MOVE);
		DndHandler.enableDndDrop(getCellsPane(),new CellsPaneDndAdapter());
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
     */
    public boolean isDirty() {
        return false;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
     */
    public String[] getSupportData() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
     */
    public SheetCellEditor getDataEditor(String extFmtName) {
        return null;
    }

    /*
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void actionPerform(UserUIEvent e) {      
    }

    /*
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
        return null;
    }
    
	private SelectModel getSelectModel() {
        return getCellsModel().getSelectModel();
    }
	private CellsPane getCellsPane(){
	    return getReport().getTable().getCells();
	}
	class CellsPaneDndAdapter implements IDndAdapter{

        /*
         * @see dnd.IDndAdapter#getSourceObject()
         */
        public Object getSourceObject() {
            if(isInDndArea){
                m_dndPos = getSelectModel().getAnchorCell();
                getSelectModel().setSelectedArea(AreaPosition.getInstance(m_dndPos,m_dndPos));
                return getDndData(m_dndPos);
            }
            return null;
        }

        /*
         * @see dnd.IDndAdapter#removeSourceNode()
         */
        public void removeSourceNode() {
            setDndData(m_dndPos.getRow(),m_dndPos.getColumn(),null);
        }

        /*
         * @see dnd.IDndAdapter#insertObject(java.awt.Point, java.lang.Object)
         */
        public boolean insertObject(Point ap, Object obj) {
            int row = getCellsPane().rowAtPoint(ap);
            int col = getCellsPane().columnAtPoint(ap);
            if(m_dndPos != null && m_dndPos.getRow() == row && m_dndPos.getColumn() == col){
                //拖放回了原位置。
                return false;
            }
            setDndData(row,col,obj);
            return true;
        }
        private void setDndData(int row,int col,Object obj){
            if(m_dndDataType == null){
            }else if(m_dndDataType.equals("")){
                getCellsModel().setCellValue(row,col,obj);
            }else{
                getCellsModel().setBsFormat(CellPosition.getInstance(row,col),m_dndDataType,obj);
            }
        }
        private Object getDndData(CellPosition pos){
            if(m_dndDataType == null){
                return null;
            } else {
				Cell cell = getCellsModel().getCell(pos);
				if(cell == null){
					return null;
				}
				if(m_dndDataType.equals("")){
				    return cell.getValue();
				}else{
				    return cell.getExtFmt(m_dndDataType);
				}
			}
        }
	}
    /**
     * @param m_dndDataType 要设置的 m_dndDataType。
     */
    public void setDndDataType(String dndDataType) {
        m_dndDataType = dndDataType;
    }
}
