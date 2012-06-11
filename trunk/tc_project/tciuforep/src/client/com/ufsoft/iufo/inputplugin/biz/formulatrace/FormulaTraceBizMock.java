package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import com.ufida.dataset.Context;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedData;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaParsedDataItem;
import com.ufsoft.iuforeport.tableinput.applet.IFormulaTraceValueItem;
import com.ufsoft.report.ContextVO;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;
/**
 *   客户端的公式追踪业务接口实现类 's Mock
 * @author liulp
 *
 */
public class FormulaTraceBizMock implements IFormulaTraceBiz{

	private static IFormulaTraceBiz s_iFormulaTraceBiz = new FormulaTraceBizMock();
	
	protected static CellPosition S_CELL_TEST1 = CellPosition.getInstance(0, 1);
	protected static CellPosition S_CELL_TEST2 = CellPosition.getInstance(1, 1);
	//B3=A3+(A4-A5)*(A6/A7)
	protected static CellPosition S_CELL_TEST_B3 = CellPosition.getInstance(2, 1);
	//B4=MSELECT('A3')
	protected static CellPosition S_CELL_TEST_B4 = CellPosition.getInstance(3, 1);
	//B5=MSELECT('A3','月',-1)
	protected static CellPosition S_CELL_TEST_B5 = CellPosition.getInstance(4, 1);
	//B6=MSELECT('A3',,-1)
	protected static CellPosition S_CELL_TEST_B6 = CellPosition.getInstance(5, 1);
	//B7:B9=MSELECTA(A7:A9)
//	protected static CellPosition S_CELL_TEST_B7TOB9 = CellPosition.getInstance(1, 1);//	TODO
	//A7=PTOTAL(A3:A6)
	protected static CellPosition S_CELL_TEST_A7 = CellPosition.getInstance(6, 0);
	
	private FormulaTraceBizMock(){
	}
	public static IFormulaTraceBiz getInstance(){
		return s_iFormulaTraceBiz;
	}
	
	public boolean existFormula(CellsModel cellModel, CellPosition cell) {
		if(cell == null){
			return false;
		}
		if(cell.equals(S_CELL_TEST1) || cell.equals(S_CELL_TEST2)
				|| cell.equals(S_CELL_TEST_B3)
				|| cell.equals(S_CELL_TEST_B4)
				|| cell.equals(S_CELL_TEST_B5)
				|| cell.equals(S_CELL_TEST_B6)
				|| cell.equals(S_CELL_TEST_A7)
				){
			return true;
		}
		return false;
	}

	public FormulaParsedData parseFormula(Context contextVO,CellsModel cellModel, CellPosition cell) {
		FormulaParsedData formulaParsedData = new FormulaParsedData();
		formulaParsedData.setTracePos(cell);
		if(cell != null && cell.equals(S_CELL_TEST1)){
			formulaParsedData.setFormulaDisContent("strFormulaDisContent cellTest11111111"
					+"2222oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
					+"3333oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
					+"4444oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
//					+"5555oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
//					+"6666oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
//					+"7777oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo3"
					+"8888oooooooooooooo4444444444444444444444444444");
			FormulaParsedDataItem[] formulaParsedDataItems = new FormulaParsedDataItem[6];
			formulaParsedData.setFormulaParsedItems(formulaParsedDataItems);
			//1,有值，不需计算；不可追踪，
			//2,有值，不需计算；可追踪，本表，位置有，单值
			//3,有值，不需计算；可追踪，他表，无，单值
			//4,有值，不需计算；可追踪，他表，无，多值来源追踪
			//5,无值，需计算；可追踪，他表，无，单值
			//6,无值，需计算；可追踪，他表，无，多值来源追踪
			//begin
			//1,有值，不需计算；不可追踪，
			int nIndex = 0;
			formulaParsedDataItems[nIndex] = new FormulaParsedDataItem();
			formulaParsedDataItems[nIndex].setDisContent("strDisContent Item " + (nIndex+1));			
			formulaParsedDataItems[nIndex].setFormulaValue(new Double(111.11*(nIndex+1)));
			formulaParsedDataItems[nIndex].setNeedToCal(false);			
			formulaParsedDataItems[nIndex].setCanTrace(false);
			formulaParsedDataItems[nIndex].setTraceSelf(false);
			formulaParsedDataItems[nIndex].setTracedPos(null);
			formulaParsedDataItems[nIndex].setTraceMultiValues(false);
			//2,有值，不需计算；可追踪，本表，位置有，单值
			nIndex++;
			formulaParsedDataItems[nIndex] = new FormulaParsedDataItem();
			formulaParsedDataItems[nIndex].setDisContent("strDisContent Item " + (nIndex+1));			
			formulaParsedDataItems[nIndex].setFormulaValue(new Double(111.11*(nIndex+1)));
			formulaParsedDataItems[nIndex].setNeedToCal(false);			
			formulaParsedDataItems[nIndex].setCanTrace(true);
			formulaParsedDataItems[nIndex].setTraceSelf(true);
			formulaParsedDataItems[nIndex].setTracedPos(new IArea[]{CellPosition.getInstance(5, 5)});
			formulaParsedDataItems[nIndex].setTraceMultiValues(false);
			//3,有值，不需计算；可追踪，他表，无，单值
			nIndex++;
			formulaParsedDataItems[nIndex] = new FormulaParsedDataItem();
			formulaParsedDataItems[nIndex].setDisContent("strDisContent Item " + (nIndex+1));			
			formulaParsedDataItems[nIndex].setFormulaValue(new Double(111.11*(nIndex+1)));
			formulaParsedDataItems[nIndex].setNeedToCal(false);			
			formulaParsedDataItems[nIndex].setCanTrace(true);
			formulaParsedDataItems[nIndex].setTraceSelf(false);
			formulaParsedDataItems[nIndex].setTracedPos(null);
			formulaParsedDataItems[nIndex].setTraceMultiValues(false);
			//4,有值，不需计算；可追踪，他表，无，多值来源追踪
			nIndex++;
			formulaParsedDataItems[nIndex] = new FormulaParsedDataItem();
			formulaParsedDataItems[nIndex].setDisContent("strDisContent Item " + (nIndex+1));			
			formulaParsedDataItems[nIndex].setFormulaValue(new Double(111.11*(nIndex+1)));
			formulaParsedDataItems[nIndex].setNeedToCal(false);			
			formulaParsedDataItems[nIndex].setCanTrace(true);
			formulaParsedDataItems[nIndex].setTraceSelf(false);
			formulaParsedDataItems[nIndex].setTracedPos(null);
			formulaParsedDataItems[nIndex].setTraceMultiValues(true);
			//5,无值，需计算；可追踪，他表，无，单值
			nIndex++;
			formulaParsedDataItems[nIndex] = new FormulaParsedDataItem();
			formulaParsedDataItems[nIndex].setDisContent("strDisContent Item " + (nIndex+1));			
			formulaParsedDataItems[nIndex].setFormulaValue(null);
			formulaParsedDataItems[nIndex].setNeedToCal(true);			
			formulaParsedDataItems[nIndex].setCanTrace(true);
			formulaParsedDataItems[nIndex].setTraceSelf(false);
			formulaParsedDataItems[nIndex].setTracedPos(null);
			formulaParsedDataItems[nIndex].setTraceMultiValues(true);
			//6,无值，需计算；可追踪，他表，无，多值来源追踪
			nIndex++;
			formulaParsedDataItems[nIndex] = new FormulaParsedDataItem();
			formulaParsedDataItems[nIndex].setDisContent("strDisContent Item " + (nIndex+1));			
			formulaParsedDataItems[nIndex].setFormulaValue(null);
			formulaParsedDataItems[nIndex].setNeedToCal(true);			
			formulaParsedDataItems[nIndex].setCanTrace(true);
			formulaParsedDataItems[nIndex].setTraceSelf(false);
			formulaParsedDataItems[nIndex].setTracedPos(null);
			formulaParsedDataItems[nIndex].setTraceMultiValues(true);
		}else if(cell != null && cell.equals(S_CELL_TEST2)){
				formulaParsedData.setFormulaDisContent("strFormulaDisContent cellTest22222222");
				formulaParsedData.setFormulaParsedItems(null);
		}
		return formulaParsedData;
	}
	public IFormulaTraceValueItem calFormulaTraceValueItem(ContextVO contextVO,CellsModel cellModel, IFormulaParsedDataItem formulaParsedDataItem,CellPosition cell) {
		IFormulaTraceValueItem[] formulaTraceValueItems = FormulaTraceBizLinkMock.getMultiValuesMock(1,true);
		return formulaTraceValueItems[0];
	}

}
