package com.ufsoft.iufo.inputplugin.biz.formulatrace;

import javax.swing.JOptionPane;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedData;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;

/**
 * 公式追踪菜单的Cmd类
 * 
 * @author liulp
 * 
 */
public class FormulaTraceCmd extends AbsIufoBizCmd {

	protected FormulaTraceCmd(UfoReport ufoReport) {
		super(ufoReport);
	}

	@Override
	protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
		if (params == null || params.length <= 0 || params[0] == null) {
			String strAlert = MultiLangInput.getString("miufotableinput0004");// 请选择一个单元格
			JOptionPane.showMessageDialog(ufoReport, strAlert);
			return;
		}
		//read self menu seleted:if true, to read window open,if open,to trace
		String strMenuName = FormulaTraceBizUtil.doGetStrFormulaTrace();//"公式追踪"
		String[] strParantMenuNames = new String[]{FormulaTraceBizUtil.doGetStrData()};//"数据"
		boolean isTraceMenuSelected = FormulaTraceBizUtil.isMenuSelected(strMenuName,strParantMenuNames,ufoReport);
		
		FormulaTraceNavPanel panel = FormulaTraceBizUtil.getFormulaTraceNavPanel(getUfoReport());
		if(!isTraceMenuSelected){
			if(panel!=null&&panel.getCurTracedPos()!=null){
				panel.setCurTracedPos(null);
			}
			return;
		}

		FormulaParsedData formulaParedData = null;
    	CellPosition cellPos=ufoReport.getCellsModel().getSelectModel().getAnchorCell();
    	IFormulaTraceBiz formulaTraceBiz = FormulaTraceBizHelper.getFormulaTraceBiz();
    	boolean bExistFormula = formulaTraceBiz.existFormula(ufoReport.getCellsModel(), cellPos);
		if(bExistFormula){
			//make sure:show formula trace window
			strParantMenuNames = new String[]{MultiLang.getString("window"),MultiLang.getString("panelManager")};
			boolean isTraceWinSelected = FormulaTraceBizUtil.isMenuSelected(strMenuName,strParantMenuNames,ufoReport);
			if(!isTraceWinSelected){
				FormulaTraceBizUtil.setMenuSelected(strMenuName,strParantMenuNames,ufoReport);
			}		
			// parse the formula
			formulaParedData = formulaTraceBiz.parseFormula(
					ufoReport.getContextVo(),ufoReport.getCellsModel(), (CellPosition) params[0]);
		}
		// set formula parsed data items
		
		if(panel != null){
			panel.setFormulaParedData(formulaParedData);
		}
		
//		FormulaTraceNavTestDlg testDlg = new FormulaTraceNavTestDlg(ufoReport,formulaParedData);
//		testDlg.show();
		// disply the paredData on the formula navigation panel
	}
	
}
