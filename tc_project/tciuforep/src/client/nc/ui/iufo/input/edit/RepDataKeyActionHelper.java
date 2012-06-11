package nc.ui.iufo.input.edit;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.CellEditor;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.KeyStroke;

import nc.ui.iufo.pub.UfoPublic;

import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.ufobiz.data.InputDirConstant;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.CombinedCell;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.TablePane;
import com.ufsoft.table.header.HeaderModel;
import com.ufsoft.table.re.BorderPlayRender;

public class RepDataKeyActionHelper {
	public static void registerKeyAction(RepDataEditor editor){
		CellsPane cellsPane=editor.getCellsPane();
		createActionMap(cellsPane.getActionMap(),editor);
		//������JTAble�ж���ļ����¼�.
		getInputMap(cellsPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT),JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}
	
	public static void doNavigate(CellsPane table,int x,int y,boolean bFilterNoWriteCell){
		SelectModel selModel = table.getSelectionModel();
		CellsModel dataModel = table.getDataModel();
		int maxRow=dataModel.getRowNum();
		int maxCol=dataModel.getColNum();
		
		//�õ���ǰ��ê���λ��
		CellPosition anchorCell = selModel.getRealAnchorCell();
		if (anchorCell == null)
			return;
		int anchorRow = anchorCell.getRow();
		int anchorColumn = anchorCell.getColumn();
		//�����Ԫ��ǰ״̬�Ǳ༭,���ұ༭û�н���,����Ӧ��Щ�¼�.
		if (table.isEditing()
				&& !table.getTablePane().getCellEditor().stopCellEditing()) {
			return;
		}
		/**
		 * �ƶ��Ƿ񵽴����ޱ��ģ�����λ��? �����,��Ҫ�ж��Ƿ���Ե���ģ�͵Ĵ�С;
		 * �ƶ���Ҫ�ж��Ƿ��ƶ�������ͼ�ı�Ե?�����,��Ҫ�ƶ���ͼ��λ��;
		 */
		String strAnchorDynPK=getDynAreaPKByCell(dataModel,anchorCell);
		int newRow, newCol;
		while (true){
			newRow = anchorRow + y;
			newCol = anchorColumn + x;
			if (newRow==anchorCell.getRow() && newCol==anchorCell.getColumn()){
				return;
			}
			//�����жϳ�ʼ����ĵ�Ԫ�Ƿ���һ����ϵ�Ԫ,�����,��Ҫ���ݵ�ǰ�����ķ���������Anchor��λ��.
			CombinedCell cc = dataModel.getCombinedAreaModel().belongToCombinedCell(CellPosition.getInstance(anchorRow,anchorColumn));//.belongToCombinedCell(anchorRow, anchorColumn);
			if (cc != null) {
				CellPosition endCell = cc.getArea().getEnd();
				CellPosition startCell=cc.getArea().getStart();
				if (y > 0 && newRow <= endCell.getRow()) {
					newRow = endCell.getRow() + 1;
				} else if (x > 0 && newCol <= endCell.getColumn()) {
					newCol = endCell.getColumn() + 1;
				} else if (y < 0 && newRow >= startCell.getRow()) {
					newRow = startCell.getRow()-1;
				} else if (x < 0 && newCol >= startCell.getColumn()) {
					newCol = startCell.getColumn()-1;
				}
			}
			if (y!=0){
				boolean bAdjusted=false;
				if (newRow>=maxRow){
					newRow=0;
					newCol++;
					bAdjusted=true;
				}
				if (newRow<0){
					newRow=maxRow-1;
					newCol--;
					bAdjusted=true;
				}
				if (newCol>=maxCol){
					newCol=0;
					if (bAdjusted==false)
						newRow++;
				}
				if (newCol<0){
					newCol=maxCol-1;
					if (bAdjusted==false)					
						newRow--;
				}
			}else{
				boolean bAdjusted=false;
				if (newCol>=maxCol){
					newCol=0;
					newRow++;
					bAdjusted=true;
				}
				if (newCol<0){
					newCol=maxCol-1;
					newRow--;
					bAdjusted=true;
				}
				
				if (newRow>=maxRow){
					newRow=0;
					if (bAdjusted==false)
						newCol++;
				}
				if (newRow<0){
					newRow=maxRow-1;
					if (bAdjusted==false)
						newCol--;
				}
			}
			
			String strNewDynPK=getDynAreaPKByCell(dataModel,CellPosition.getInstance(newRow,newCol));
			if (bFilterNoWriteCell==false || dataModel.getCellsAuth()==null)
				break;
			
			if (!UfoPublic.strIsEqual(strAnchorDynPK, strNewDynPK) || !dataModel.getCellsAuth().isWritable(newRow, newCol)){
				anchorRow=newRow;
				anchorColumn=newCol;
			}else
				break;
		}
		
		if(newRow >= 0 && newCol >= 0){
		    selModel.clear();
		    selModel.setAnchorCell(newRow, newCol);
		    if (bFilterNoWriteCell){
				MouseEvent event=new MouseEvent(table,0,0,0,0,0,2,false,0);
				table.editCellAt(newRow,newCol,event);
				table.requestFocus();
		    }
		}
		table.moveViewToDisplayRect(selModel.getAnchorCell(), true);
	}
	
	private static String getDynAreaPKByCell(CellsModel dataModel,CellPosition cell){
		DynAreaModel dynModel=DynAreaModel.getInstance(dataModel);
		DynAreaCell[] dynAreas=dynModel.getDynAreaCellByArea(cell);
		if (dynAreas==null || dynAreas.length<=0)
			return null;
		
		return dynAreas[0].getDynAreaPK();
	}
	
	private static ActionMap createActionMap(ActionMap map,RepDataEditor editor) {
		//�������Ҽ���֧��
		map.put("selectNextColumn", new NavigationalAction(1, 0));
		map.put("selectPreviousColumn", new NavigationalAction(-1, 0));
		map.put("selectNextRow", new NavigationalAction(0, 1));
		map.put("selectPreviousRow", new NavigationalAction(0, -1));

		//Pageup,Pagedown�Ĵ���
		map.put("Pageup", new PagingAction(false));
		map.put("Pagedown", new PagingAction(true));
		//�س������ƶ�
		map.put("nextCell", new EnterAction(editor,false));
		map.put("tabCell", new EnterAction(editor,false));
		map.put("shiftTabCell", new EnterAction(editor,true));
		map.put("shiftNextCell", new EnterAction(editor,true));

		//Home,End,Ctrl+Home,Ctrl+End
		map.put("Home", new HomeEndAction(false, true));
		map.put("End", new HomeEndAction(false, false));
		map.put("CtrlHome", new HomeEndAction(true, true));
		map.put("CtrlEnd", new HomeEndAction(true, false));

		//��ĳ��ѡ�е�Ԫ������ĸ��������,��ʼ�༭.
		map.put("cancel", new CancelEditingAction());
		map.put("startEditing", new StartEditingAction());
		
		map.put("shiftRight",new ShiftDirAction(InputDirConstant.DIR_RIGHT));
		map.put("shiftLeft",new ShiftDirAction(InputDirConstant.DIR_LEFT));
		map.put("shiftDown",new ShiftDirAction(InputDirConstant.DIR_DOWN));
		map.put("shiftUp",new ShiftDirAction(InputDirConstant.DIR_UP));

		return map;
	}
	
	
	
	/**
	 * ��������ʱ�¼���Ӧ֮��Ĺ�ϵ.
	 * 
	 * @param condition int
	 * @return InputMap
	 */
	private static InputMap getInputMap(InputMap keyMap,int condition) {
		if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
			//�������������
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),"selectPreviousColumn");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),"selectNextColumn");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),"selectPreviousRow");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),"selectNextRow");
			//�س����ƶ�����һ����Ԫ���س���һ��Ҫ�Ӳ���true.��Ϊ�༭״̬ʱ,��press�¼����༭������.
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),"nextCell");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0),"tabCell");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK),"shiftTabCell");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_MASK),"shiftNextCell");
			//Esc��Ctrl��A��F2
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0),"startEditing");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), "Home");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "End");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,KeyEvent.CTRL_MASK), "CtrlHome");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END,KeyEvent.CTRL_MASK), "CtrlEnd");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0),"Pageup");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.SHIFT_MASK),"shiftRight");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.SHIFT_MASK),"shiftLeft");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.SHIFT_MASK),"shiftDown");
			keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.SHIFT_MASK),"shiftUp");
 
			return keyMap;
		}
		return null;
	}
	
	//**************************��꣬���̶�����Ӧ���¼���Ӧ�Ĵ���********************//
	
	private static class ShiftDirAction extends AbstractAction{
		private static final long serialVersionUID = 5193067479943184903L;
		private int iDirection=InputDirConstant.DIR_DOWN;
		
		protected ShiftDirAction(int iDirection){
			this.iDirection=iDirection;
		}
		
		private int[] getValidMaxMinRC(CombinedCell[] combCells,AreaPosition anchorArea,AreaPosition selectArea,boolean bRow){
			if (bRow){
				int iMaxRow=anchorArea.getEnd().getRow();
				int iMinRow=anchorArea.getStart().getRow();
				
				if (combCells!=null){
					for (int i=0;i<combCells.length;i++){
						if (combCells[i].getArea().intersection(selectArea)){
							if ((anchorArea.getStart().getRow()>=combCells[i].getArea().getStart().getRow()
									&& anchorArea.getStart().getRow()<=combCells[i].getArea().getEnd().getRow()) ||
									(anchorArea.getEnd().getRow()>=combCells[i].getArea().getStart().getRow()
											&& anchorArea.getEnd().getRow()<=combCells[i].getArea().getEnd().getRow())){
								if (iMaxRow<combCells[i].getArea().getEnd().getRow())
									iMaxRow=combCells[i].getArea().getEnd().getRow();
								if (iMinRow>combCells[i].getArea().getStart().getRow())
									iMinRow=combCells[i].getArea().getStart().getRow();
							}
						}
					}
				}
	
				return new int[]{iMaxRow,iMinRow};
			}else{
				int iMaxCol=anchorArea.getEnd().getColumn();
				int iMinCol=anchorArea.getStart().getColumn();
				
				if (combCells!=null){
					for (int i=0;i<combCells.length;i++){
						if (combCells[i].getArea().intersection(selectArea)){
							if ((anchorArea.getStart().getColumn()>=combCells[i].getArea().getStart().getColumn()
									&& anchorArea.getStart().getColumn()<=combCells[i].getArea().getEnd().getColumn()) ||
									(anchorArea.getEnd().getColumn()>=combCells[i].getArea().getStart().getColumn()
											&& anchorArea.getEnd().getColumn()<=combCells[i].getArea().getEnd().getColumn())){
								if (iMaxCol<combCells[i].getArea().getEnd().getColumn())
									iMaxCol=combCells[i].getArea().getEnd().getColumn();
								if (iMinCol>combCells[i].getArea().getStart().getColumn())
									iMinCol=combCells[i].getArea().getStart().getColumn();
							}
						}
					}
				}
				
				return new int[]{iMaxCol,iMinCol};
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			CellsModel dataModel=table.getDataModel();
			SelectModel selectModel=dataModel.getSelectModel();
			selectModel.getRealAnchorCell();
			CellPosition anchorCell=selectModel.getAnchorCell();
			AreaPosition selectArea=selectModel.getSelectedArea();
			AreaPosition newSelectArea=selectArea;
			
			int iMaxAnchorRow=anchorCell.getRow();
			int iMaxAnchorCol=anchorCell.getColumn();
			int iMinAnchorRow=anchorCell.getRow();
			int iMinAnchorCol=anchorCell.getColumn();
			CombinedCell cc = dataModel.getCombinedAreaModel().belongToCombinedCell(anchorCell);
			CombinedCell[] combCells=dataModel.getCombinedAreaModel().getCombineCells();
			if (cc != null) {
				CellPosition endCell = cc.getArea().getEnd();
				iMaxAnchorRow=endCell.getRow();
				iMaxAnchorCol=endCell.getColumn();
			}
			AreaPosition anchorArea=AreaPosition.getInstance(iMinAnchorRow, iMinAnchorCol, iMaxAnchorRow-iMinAnchorRow+1, iMaxAnchorCol-iMinAnchorCol+1);
			
			while (true){
				try{
					if (iDirection==InputDirConstant.DIR_DOWN || iDirection==InputDirConstant.DIR_UP){
						int[] iMaxMinRows=getValidMaxMinRC(combCells,anchorArea,newSelectArea,true);
						iMaxAnchorRow=iMaxMinRows[0];
						iMinAnchorRow=iMaxMinRows[1];
						int iAddRow=iDirection==InputDirConstant.DIR_DOWN?1:-1;
						if (iMinAnchorRow<newSelectArea.getStart().getRow() || (iMinAnchorRow==newSelectArea.getStart().getRow() && iMaxAnchorRow<=newSelectArea.getEnd().getRow()))
							newSelectArea=newSelectArea.getInstanceByNewEnd(CellPosition.getInstance(newSelectArea.getEnd().getRow()+iAddRow,newSelectArea.getEnd().getColumn()));
						else 
							newSelectArea=newSelectArea.getInstanceByNewStart(CellPosition.getInstance(newSelectArea.getStart().getRow()+iAddRow,newSelectArea.getStart().getColumn()));
					}else{
						int[] iMaxMinCols=getValidMaxMinRC(combCells,anchorArea,newSelectArea,false);
						iMaxAnchorCol=iMaxMinCols[0];
						iMinAnchorCol=iMaxMinCols[1];
						int iAddCol=iDirection==InputDirConstant.DIR_RIGHT?1:-1;
						if (iMinAnchorCol<newSelectArea.getStart().getColumn() || (iMinAnchorCol==newSelectArea.getStart().getColumn() && iMaxAnchorCol<=newSelectArea.getEnd().getColumn()))
							newSelectArea=newSelectArea.getInstanceByNewEnd(CellPosition.getInstance(newSelectArea.getEnd().getRow(),newSelectArea.getEnd().getColumn()+iAddCol));
						else
							newSelectArea=newSelectArea.getInstanceByNewStart(CellPosition.getInstance(newSelectArea.getStart().getRow(),newSelectArea.getStart().getColumn()+iAddCol));
					}
				}catch(Exception te){
					break;
				}
				
				AreaPosition newSelectArea1=selectModel.fitCombinedCell(newSelectArea);
				if (newSelectArea1.equals(selectArea)==false){
					newSelectArea=newSelectArea1;
					break;
				}
			}
			selectModel.setSelectedArea(newSelectArea);
		}
	}

	/**
	 * <p>
	 * Title:������Ӧ�������ҵ��ƶ��¼�
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2003
	 * </p>
	 * <p>
	 * Company: UFSOFT
	 * </p>
	 * 
	 * @author wupeng
	 * @version 1.0.0.1
	 */
	private static class NavigationalAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		/** ˮƽ�ƶ��� */
		protected int dx;

		/** ��ֱ�ƶ��� */
		protected int dy;

		/**
		 * @param dx
		 *            ˮƽ�ƶ���Ԫ������.
		 * @param dy
		 *            ��ֱ�ƶ��ĵ�Ԫ������.
		 */
		protected NavigationalAction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
		/**
		 * �����ƶ���
		 * 
		 * @param x
		 * @param y
		 */
		protected void setOffset(int x, int y) {
			this.dx = x;
			this.dy = y;
		}
		
        /**
         * ��������ʱ�Ĵ�����
         */
		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			doNavigate(table,dx,dy,false);
		}
	}
	
	public static class EnterAction extends NavigationalAction{
		private static final long serialVersionUID = -231101730471811402L;
		
		private RepDataEditor editor=null;
		private boolean bShift=false;
		
		EnterAction(RepDataEditor editor,boolean bShift){
			super(-1,-1);
			this.editor=editor;
			this.bShift=bShift;
		}
		
        /**
         * ��������ʱ�Ĵ�����
         */
		public void actionPerformed(ActionEvent e) {
			if (editor==null)
				return;
			
			editor.processEnterAction(bShift);
		}
	}

	/** ����Pageup��Pagedown����Ӧ�� */
	private static class PagingAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private boolean forwards;
		private PagingAction(boolean forwards) {
			this.forwards = forwards;
		}
		/**
         * ��������ʱ�Ĵ�����
         */
		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			//���ȼ��������ͼ��λ�ã�Ȼ����㽹���ƶ������Ǹ�λ�á�
			CellsModel dataModel = table.getDataModel();
			SelectModel selModel = dataModel.getSelectModel();
			CellPosition anchorCell = selModel.getAnchorCell();
			HeaderModel rowModel = dataModel.getRowHeaderModel();
			HeaderModel colModel=dataModel.getColumnHeaderModel();

			JViewport view = (JViewport) table.getParent();
			Component comp=view;
			TablePane tablePane=null;
			while (comp!=null ){
				comp=comp.getParent();
				if (comp!=null && comp instanceof TablePane){
					tablePane=(TablePane)comp;
					break;
				}
			}
			
			
			Point cellPoint = view.getViewPosition();
			Dimension extDim = view.getExtentSize();
			int anchorRow;
			int iDiffRow=anchorCell.getRow()-rowModel.getIndexByPosition((int)(cellPoint.getY()));
			//ֻ�ǿ��Ǵ�ֱ����ķ�ҳ��
			if (forwards) { //���·�ҳ
				int iDownRow=rowModel.getIndexByPosition((int)(cellPoint.getY()+extDim.height));
				if (rowModel.getPosition(iDownRow+1)>cellPoint.getY()+extDim.height)
					iDownRow--;
				
				anchorRow=iDownRow+1+iDiffRow;
				if (anchorRow>=dataModel.getRowNum())
					anchorRow = dataModel.getRowNum()-1;
				
				if (iDownRow<=anchorRow)
					cellPoint.y=rowModel.getPosition(iDownRow+1);
			} else {		
				int iUpRow=rowModel.getIndexByPosition((int)(cellPoint.getY()));//-extDim.height));
				if (rowModel.getPosition(iUpRow)<cellPoint.getY())
					iUpRow++;
				
				int iNextY=rowModel.getPosition(iUpRow)-extDim.height;
				
				if (tablePane.isFreezing()){
					if (view==tablePane.getRightDownView() || view==tablePane.getDownView()){
						if (iNextY<tablePane.getSeperateY())
							iNextY=tablePane.getSeperateY();
					}
				}
				
				iUpRow=rowModel.getIndexByPosition(iNextY);
				if (iUpRow<0)
					iUpRow=0;
				if(rowModel.getPosition(iUpRow)<iNextY)
					iUpRow++;
				
				anchorRow=iUpRow+iDiffRow;
				
				if (iUpRow<=anchorRow)
					cellPoint.y=rowModel.getPosition(iUpRow);
			}
			
			if(view.getViewPosition().equals(cellPoint)==false){				
				if (tablePane.isFreezing()){
					if (cellPoint.getY()>=tablePane.getSeperateY()){
						if (cellPoint.getX()>=tablePane.getSeperateX()){
							if (tablePane.getRightDownView()!=null && colModel.getPosition(anchorCell.getColumn())>=tablePane.getSeperateY())
								tablePane.getRightDownView().getView().requestFocus();
							else if (tablePane.getDownView()!=null && colModel.getPosition(anchorCell.getColumn())<tablePane.getSeperateY())
								tablePane.getDownView().getView().requestFocus();
						}else if (tablePane.getDownView()!=null){
							tablePane.getDownView().getView().requestFocus();
						}
					}
				}
				
				view.setViewPosition(cellPoint);
				
				if (anchorRow != anchorCell.getRow()) {
					selModel.setAnchorCell(anchorRow, anchorCell.getColumn());
				}
				
				// @edit by wangyga at 2009-3-4,����07:09:36
				table.paginalData();
			}
		}
	}

	/**
	 * �༭ȡ������.��ӦEsc�������,ɾ����ǰ�ı༭��.
	 */
	private static class CancelEditingAction extends AbstractAction {
	 
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			table.removeEditor();
			BorderPlayRender.stopPlay(table);//ֹͣ������add by wangyga
		}
	}

	/**
	 * ��ʼ�༭�Ķ���.��ӦEnter��,�����㸳��༭����
	 */
	private static class StartEditingAction extends AbstractAction {
	 
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			CellsPane table = (CellsPane) e.getSource();
			if (!table.hasFocus()) { //��鵱ǰ�Ľ����Ƿ��ڱ༭���֮��
				CellEditor cellEditor = table.getTablePane().getCellEditor();
				if (cellEditor != null && !cellEditor.stopCellEditing()) {
					return;
				}
				table.requestFocus();
				return;
			}
			SelectModel sm = table.getSelectionModel();
			int row = sm.getAnchorCell().getRow();
			int col = sm.getAnchorCell().getColumn();
			//�õ�ѡ��Ľ���
			Component editorComp = table.getTablePane().getEditorComp();
			if (editorComp == null) {
				table.editCellAt(row, col);
				editorComp = table.getTablePane().getEditorComp();
			}
			if (editorComp != null) {
				editorComp.requestFocus();
			}
		}
	}

	/**
	 * ���¼���Ҫ��ӦHome��End�Լ����ǵ����.
	 */
	private static class HomeEndAction extends NavigationalAction {
		private static final long serialVersionUID = 8501573489339806403L;
		
		private boolean ctrlPressed, home;

		/**
		 * 
		 * @param bCtrlPress
		 *            ctrl�Ƿ���.
		 * @param bHome
		 *            ��home����end.trueΪHome.
		 */
		protected HomeEndAction(boolean bCtrlPress, boolean bHome) {
			super(0, 0);
			ctrlPressed = bCtrlPress;
			home = bHome;
		}
		/**
         * ��������ʱ�Ĵ�����
         */
		public void actionPerformed(ActionEvent e) {
			//���ȱ仯����,Ȼ��任��ͼ.
			CellsPane cells = (CellsPane) e.getSource();
			SelectModel selModel = cells.getDataModel().getSelectModel();
			int anchorRow = selModel.getAnchorCell().getRow();
			int anchorCol = selModel.getAnchorCell().getColumn();
			int xOffset = 0, yOffset = 0;
			if (home) {
				xOffset = 0 - anchorCol;
				if (ctrlPressed) {
					yOffset = 0 - anchorRow;
				}
			} else {
				HeaderModel rowModel = cells.getDataModel().getRowHeaderModel();
				xOffset = cells.getDataModel().getColNum() - 1 - anchorCol;
				if (ctrlPressed) {
					yOffset = cells.getDataModel().getRowNum() - 1 - anchorRow;
				}
			}
			setOffset(xOffset, yOffset);
			super.actionPerformed(e);
		}
	}
}
