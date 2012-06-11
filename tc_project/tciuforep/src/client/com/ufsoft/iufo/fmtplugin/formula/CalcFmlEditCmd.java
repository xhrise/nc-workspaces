package com.ufsoft.iufo.fmtplugin.formula;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.key.AbsEditorAction;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellsModel;

/**
 * 单元公式 Description:单元公式Command.
 * 
 * @author zzl
 * @version 5.0
 */
public class CalcFmlEditCmd extends UfoCommand implements IUfoContextKey {
	private UfoReport m_rpt = null;

	private CellsModel m_cm = null;

	public CalcFmlEditCmd() {
	}

//	private UfoFmlExecutor getFormulaHandler() {
//		return getFormulaModel().getUfoFmlExecutor();
//	}
//
//	private FormulaModel getFormulaModel() {
//		return FormulaModel.getInstance(m_rpt.getCellsModel());
//	}
//
//	private DynAreaModel getDynAreaModel(){
//		return DynAreaModel.getInstance(getCellsModel());
//	}
//	
//	private MeasureModel getMeasureModel(){
//		return MeasureModel.getInstance(getCellsModel());
//	}
//	
//	private String getDynPKByFmlArea(IArea fmlArea) {		
//		// @edit by wangyga at 2009-3-12,上午09:37:00		
//		DynAreaCell[] dynCellsTemp = getDynAreaModel().getDynAreaCellByArea(fmlArea);
//		String strDynPK = null;
//		if (dynCellsTemp != null && dynCellsTemp.length > 0)
//			strDynPK = dynCellsTemp[0].getDynAreaVO().getDynamicAreaPK();
//		return strDynPK;
//	}

	public void execute(Object[] params) {
		
		AbsEditorAction editorAction = new FormulaActionHandler(((UfoReport) params[0]).getTable().getCells());
		editorAction.execute(null); 
		
//		m_rpt = (UfoReport) params[0];
//		m_cm = getCellsModel();
//		UfoContextVO context = (UfoContextVO) m_rpt.getContextVo();
//		
//		// 判断所选择的定义公式的区域是否合理(交叉区域不能定义公式)
//		IArea[] selAreas = m_cm.getSelectModel().getSelectedAreas();
//		IArea selArea = selAreas != null && selAreas.length > 0 ? selAreas[0]
//				: null;
//		DynAreaCell[] dynCells = getDynAreaModel().getDynAreaCellByArea(selArea);						
//		if (dynCells != null && dynCells.length > 0) {
//			if (dynCells.length == 1 && dynCells[0].getArea().contain(selArea)) {// 动态区内
//				CellPosition selPos = m_cm.getSelectModel().getAnchorCell();
//				MeasureVO mvo = getMeasureModel().getMeasureVOByPos(selPos);
//						
//			} else {
//				String strMessage = StringResource
//						.getStringResource("miufo1000976"); // "交叉区域不能定义公式"
//				JOptionPane.showMessageDialog(m_rpt, strMessage, StringResource
//						.getStringResource("miufopublic384"),
//						JOptionPane.OK_OPTION); // "提示信息"
//				return;
//			}
//		}
//
//		// 2.如果选择区域含有公式，则根据公式对应区域修正选择区域。
//		IArea fmlArea = getFormulaModel().getRelatedFmlArea(selArea, true);
//		if (fmlArea == null) {
//			fmlArea = getFormulaModel().getRelatedFmlArea(selArea, false);
//		}
//		if (fmlArea != null && !fmlArea.isCell()) {
//			selArea = fmlArea;
//			m_cm.getSelectModel().setSelectedArea((AreaPosition) fmlArea);
//		}
//
//		// 3.获得选择区域对应公式信息
//		FormulaVO publicCellFormula = fmlArea == null ? null
//				: getFormulaModel().getPublicDirectFml(fmlArea);
//		FormulaVO personCellFormula = fmlArea == null ? null
//				: getFormulaModel().getPersonalDirectFml(fmlArea);
//		FormulaVO totalFormula = fmlArea == null ? null : getFormulaModel()
//				.getDirectFml(fmlArea, false);
//
//		String strDynPK = getDynPKByFmlArea(fmlArea);
//		String strPubCellFormula = publicCellFormula == null ? null
//				: getFormulaHandler().getUserDefFmlContent(publicCellFormula,
//						fmlArea, strDynPK);
//		String strPerCellFormula = personCellFormula == null ? null
//				: getFormulaHandler().getUserDefFmlContent(personCellFormula,
//						fmlArea, strDynPK);
//		String strTotalFormula = totalFormula == null ? null
//				: getFormulaHandler().getUserDefFmlContent(totalFormula,
//						fmlArea, strDynPK);
//
//		// 4.判断增加公式的区域是否合理
//		if (context.isCreateUnit() == false) {
//			if (publicCellFormula == null					
//					&& getMeasureModel().getMeasureVOByArea(
//							selArea) == null) {
//				JOptionPane.showMessageDialog(m_rpt, StringResource
//						.getStringResource("miufo1001712")); // "您只允许修改现有公式,或者在\n已提取指标的单元定义公式！"
//				return;
//			}
//		}
//
//		// for HR formula start
//		// 由于主要针对hr公式进行区分，所以只解析单元公式的内容既可
//
//		// 5.获得是否公式向导可编辑信息。且当前此信息只对hr有效
//		boolean bIsEditable = getFormulaHandler().hasSpecialEditTypeFunc(
//				publicCellFormula);
//
//		// /for HR formula end
//
//		// 6.弹出公式编辑诓，并处理增加的公式信息。
//		String strFormula = null;
//		String strOldFormula = null;
//		UfoSimpleObject func = null;
//		UfoSimpleObject module = null;
//		int currentTab = 0;
//		int currentFuncTab = 0;// add by wangyga 2008-7-31 当前选中公式分类的页签
//
//		// 判断是否电子表格
//		boolean bSpreadSheet = false;
//		// 标记公式向导是否折叠,如果true需要重新加载新区域的公式内容
//		boolean isFold = false;
//		boolean isWizrd = false;
//		if (params.length >= 2 && params[1] != null
//				&& params[1] instanceof Boolean) {
//			bSpreadSheet = ((Boolean) params[1]).booleanValue();
//		}
//		
//		UfoFmlEditDlgParams dlgParams = UfoFmlEditDlgParams(cellsModel);
//		for (int i = 0;; i++) {
//			UfoFmlExecutor ufoFmlExecutor = getFormulaHandler();
//			boolean isAnaRep = context.getAttribute(ANA_REP) == null ? false
//					: Boolean.parseBoolean(context.getAttribute(ANA_REP)
//							.toString());
//
//			ufoFmlExecutor.setAnaRep(isAnaRep);
//			UfoFmlEditDlg fmlEditDlg = new UfoFmlEditDlg(m_rpt, ufoFmlExecutor,
//					bIsEditable, isAnaRep, bSpreadSheet);
//			fmlEditDlg.setArea(selAreas);
//			if (i > 0) {
//				fmlEditDlg.setCurrentFormulaTabPanel(currentTab);
//			}
//			fmlEditDlg.setCurrentFuncTabPanel(currentFuncTab);
//			fmlEditDlg.setSelectedFunc(module, func);
//
//			// 个性化公式待处理。
//			// modify by ljhua 2005-3-17 修改公式向导中应返回数据库格式的公式内容
//
//			// 6.1获得公有用户格式的公式内容
//			String publicFml = getFormulaHandler().getPublicUserDefFmlContent(
//					selArea);
//
//			if (isFold) {// 如果却换了区域需加载新区域公式
//				fmlArea = getFormulaModel().getRelatedFmlArea(selArea, true);
//				if (fmlArea == null) {
//					fmlArea = getFormulaModel().getRelatedFmlArea(selArea,
//							false);
//				}
//				if (fmlArea != null && !fmlArea.isCell()) {
//					selArea = fmlArea;
//					m_cm.getSelectModel().setSelectedArea(
//							(AreaPosition) fmlArea);
//				}
//
//				publicCellFormula = fmlArea == null ? null : getFormulaModel()
//						.getPublicDirectFml(selArea);
//				personCellFormula = fmlArea == null ? null : getFormulaModel()
//						.getPersonalDirectFml(selArea);
//				totalFormula = selArea == null ? null : getFormulaModel()
//						.getDirectFml(selArea, false);
//
//				strDynPK = getDynPKByFmlArea(selArea);
//				strPubCellFormula = publicCellFormula == null ? null
//						: getFormulaHandler().getUserDefFmlContent(
//								publicCellFormula, selArea, strDynPK);
//				strPerCellFormula = personCellFormula == null ? null
//						: getFormulaHandler().getUserDefFmlContent(
//								personCellFormula, selArea, strDynPK);
//				strTotalFormula = totalFormula == null ? null
//						: getFormulaHandler().getUserDefFmlContent(
//								totalFormula, selArea, strDynPK);
//			}
//
//			if (strPubCellFormula != null
//					&& strPubCellFormula.trim().length() > 0) {
//				if (isWizrd) {// 肯定修改了公式
//					((FormulaEditor) fmlEditDlg.getPublicFormulaTextArea())
//							.setOldValue(strOldFormula);
//				} else {
//					((FormulaEditor) fmlEditDlg.getPublicFormulaTextArea())
//							.setOldValue(strPubCellFormula);
//				}
//				fmlEditDlg.getPublicFormulaTextArea().setText(strPubCellFormula);
//			}
//			if (strPerCellFormula != null
//					&& strPerCellFormula.trim().length() > 0) {
//				if (isWizrd) {
//					((FormulaEditor) fmlEditDlg.getPersonalFormulaTextArea())
//							.setOldValue(strOldFormula);
//				} else {
//					((FormulaEditor) fmlEditDlg.getPersonalFormulaTextArea())
//							.setOldValue(strPerCellFormula);
//				}
//				fmlEditDlg.getPersonalFormulaTextArea().setText(strPerCellFormula);
//			}
//			if (strTotalFormula != null
//					&& strTotalFormula.trim().trim().length() > 0) {
//				if (isWizrd) {
//					((FormulaEditor) fmlEditDlg.getTotalFormulaTextArea())
//							.setOldValue(strOldFormula);
//				} else {
//					((FormulaEditor) fmlEditDlg.getTotalFormulaTextArea())
//							.setOldValue(strTotalFormula);
//				}
//				fmlEditDlg.getTotalFormulaTextArea().setText(strTotalFormula);
//			}
//			fmlEditDlg.setModal(true);
//			fmlEditDlg.show();
//			int nRs = fmlEditDlg.getResult();
//			if (nRs == UfoDialog.ID_OK) {
//
//				if (fmlEditDlg.hasCheckedFormula()) {
//					dealFormulaCellEdit(selArea, fmlEditDlg
//							.getFormulaEditType());
//					// 如有公式，清除表样
//					m_cm.clearArea(UFOTable.CELL_CONTENT,
//							new IArea[] { selArea });
//				}
//
//				break;
//
//			} else if (nRs == UfoDialog.ID_CANCEL) {
//				break;
//			} else if (nRs == CommonFmlEditDlg.ID_EDIT) {
//
//				IFuncEditTypeSpecial funedit = getFormulaHandler()
//						.getEditTypeDriver();
//				int funNameIndex = 0;
//				;
//				String funName = null;
//				int lastIndex = 0;
//				String partam = null;
//				if (strPubCellFormula != null) {
//					funNameIndex = strPubCellFormula.indexOf("(");
//					funName = strPubCellFormula.substring(0, funNameIndex);
//					lastIndex = strPubCellFormula.lastIndexOf(")");
//					partam = strPubCellFormula.substring(funNameIndex + 1,
//							lastIndex);
//				} else if (strPerCellFormula != null) {
//					funNameIndex = strPerCellFormula.indexOf("(");
//					funName = strPerCellFormula.substring(0, funNameIndex);
//					lastIndex = strPerCellFormula.lastIndexOf(")");
//					partam = strPerCellFormula.substring(funNameIndex + 1,
//							lastIndex);
//				}
//				String contents = null;
//				try {
//					if (funedit instanceof IFuncEditTypeSpecial2)
//						contents = ((IFuncEditTypeSpecial2) funedit).dealFunc(
//								fmlEditDlg, funName, partam, false);
//					else
//						contents = funedit.dealFunc(funName, partam, false);
//				} catch (UfoParseException ue) {
//					AppDebug.debug(ue);
//				}
//				if (contents != null && contents.length() > 0) {
//					strFormula = funName + "(" + contents + ")";
//				}
//
//			} else if (nRs == CommonFmlEditDlg.ID_WIZARD) {
//				strFormula = fmlEditDlg.getFormulaFromCurrentFmlEditArea();
//				func = fmlEditDlg.getSelectedFunc();
//				module = fmlEditDlg.getSelectedCategory();
//
//				currentTab = fmlEditDlg.getCurrentFormulaTabPanel();
//				currentFuncTab = fmlEditDlg.getCurrentFuncTabPanel();
//
//				String strWizard = null;
//				if (func.getName().equals(DataSetFuncDriver.GETDATA)) {
//					// 准备数据集设计数据结构
//					DataSetFuncDesignObject dsdo = new DataSetFuncDesignObject();
//					DataSetFunc objDataSetFunc = parseEditedDataSetFormula(
//							selArea, strFormula);
//					dsdo.setEditedDataSetFunc(objDataSetFunc);
//					dsdo
//							.setStatus(objDataSetFunc == null ? DataSetDesignObject.STATUS_CREATE
//									: DataSetDesignObject.STATUS_UPDATE);
//
//					Context ufoContext = ContextFactory.createContext(m_rpt);
//
//					dsdo.setContext(ufoContext);
//					// 数据集函数向导列表面板
//					AbstractWizardListPanel listPn = createDataSetFuncWizard(
//							dsdo, getFormulaHandler().getCalcEnv(), m_rpt);
//					UIDialog dlg = getWizard(listPn, null);
//
//					m_rpt.setActivatingComponent(dlg);
//
//					dlg.showModal();
//					dlg.destroy();
//					if (dlg.getResult() == UIDialog.ID_OK) {
//						isWizrd = true;
//						strWizard = dsdo.toString();
//					}
//					isFold = false;
//				} else {
//					FunctionReferDlg dlgCellFuncWizard = new FunctionReferDlg(
//							m_rpt, func);
//					dlgCellFuncWizard.setContextVO(context);
//					dlgCellFuncWizard.setModal(false);
//					dlgCellFuncWizard.setArea(selArea);
//
//					m_rpt.setActivatingComponent(dlgCellFuncWizard);
//
//					dlgCellFuncWizard.show();
//					if (dlgCellFuncWizard.getResult() == UfoDialog.ID_OK) {
//						isWizrd = true;
//						strWizard = dlgCellFuncWizard.getCellFunc();
//					}
//				}
//
//				// 得到单元公式
//				strOldFormula = strFormula;
//				strFormula = getFmlEditStr(strFormula, fmlEditDlg, strWizard);
//				fmlEditDlg.getCurrentFmlEditArea().setText(strFormula);
//				m_rpt.setActivatingComponent(null);
//				isFold = false;
//			} else if (nRs == CommonFmlEditDlg.ID_FOLD) {// add by wangyga
//				// 2008-9-11
//				FuncAreaSelectDlg areaSelectDlg = new FuncAreaSelectDlg(m_rpt);
//				areaSelectDlg.setModal(false);
//				m_rpt.setActivatingComponent(areaSelectDlg);
//
//				areaSelectDlg.show();
//				currentTab = fmlEditDlg.getCurrentFormulaTabPanel();
//				currentFuncTab = fmlEditDlg.getCurrentFuncTabPanel();
//				strFormula = fmlEditDlg.getFormulaFromCurrentFmlEditArea();
//
//				if (areaSelectDlg.getResult() == UfoDialog.ID_OK) {
//					String strSelectArea = areaSelectDlg.getSelectArea();
//					if (strSelectArea != null
//							&& strSelectArea.trim().length() > 0) {
//						isFold = true;
//						selArea = AreaPosition.getInstance(strSelectArea);
//						selAreas = new AreaPosition[] { (AreaPosition) selArea };
//					}
//				}
//			}
//
//			else {
//				break;
//			}
//			strPubCellFormula = fmlEditDlg.getRetCellFormulaForPublic();
//			strPerCellFormula = fmlEditDlg.getRetCellFormulaForPerson();
//			strTotalFormula = fmlEditDlg.getRetTotalFormula();
//		}
	}

	/**
	 * 得到公式定义向导返回的公式内容
	 * 
	 * @param strFormula
	 * @param fmlEditDlg
	 * @param strFuncWizard
	 * @return
	 */
//	private String getFmlEditStr(String strFormula, UfoFmlEditDlg fmlEditDlg,
//			String strFuncWizard) {
//		if (strFuncWizard != null && strFuncWizard.trim().length() > 0) {
//			int nCaretPos = fmlEditDlg.getCaretPos();
//			if (nCaretPos < 0 || nCaretPos >= strFormula.length()) {
//				strFormula += strFuncWizard;
//			} else if (nCaretPos == 0) {
//				strFormula = strFuncWizard + strFormula;
//			} else {
//				String strTmp = strFormula;
//				strFormula = strFormula.substring(0, nCaretPos) + strFuncWizard;
//				strFormula += strTmp.substring(nCaretPos, strTmp.length());
//			}
//		}
//		return strFormula;
//
//	}
//
//	/**
//	 * 解析GETDATA函数，非数据集函数返回NULL.
//	 * 
//	 * @param fmlArea
//	 * @param formula
//	 * @return
//	 */
//	public DataSetFunc parseEditedDataSetFormula(IArea fmlArea, String formula) {
//		if (fmlArea == null || formula == null || formula.trim().length() == 0
//				|| !formula.trim().startsWith("GETDATA")) {
//			return null;
//		}
//
//		try {
//			UfoCmdLet objLet = getFormulaHandler().getFormulaExecutor()
//					.parseUserDefFormula(fmlArea, formula);
//			UfoExpr[] allExpr = objLet.getExprs();
//			if (allExpr != null
//					&& allExpr.length == 1
//					&& allExpr[0].getElementLength() == 1
//					&& allExpr[0].getElementObjByIndex(0) instanceof DataSetFunc) {
//				return (DataSetFunc) allExpr[0].getElementObjByIndex(0);
//			}
//		} catch (Exception e) {
//			AppDebug.debug(e);
//		}
//
//		return null;
//	}
//
//	/**
//	 * add by 王宇光 2008-7-2 添加单元格的公式单元是否允许编辑控制
//	 * 
//	 * @see com.ufsoft.iufo.report.propertyoperate.PropertyOperate
//	 */
//	private void dealFormulaCellEdit(IArea area, int iCellEdit) {
//		if (area == null)
//			return;
//		CellsModel cellsModel = m_rpt.getCellsModel();
//		ArrayList<CellPosition> cellPosList = cellsModel
//				.getSeperateCellPos(area);
//		if (cellPosList == null || cellPosList.size() == 0)
//			return;
//		int iLength = cellPosList.size();
//		for (int i = 0; i < iLength; i++) {
//			CellPosition cellPos = cellPosList.get(i);
//			if (iCellEdit == FormulaCellEditUtil.FORMULA_EDIT_NO_CONTROL) {
//				FormulaCellEditUtil.removeExtFormulaEditType(cellsModel,
//						cellPos);
//			}
//			FormulaCellEditUtil.setExtFormulaEditType(cellsModel, cellPos,
//					String.valueOf(iCellEdit));
//
//		}
//	}
//
//	/**
//	 * 显示向导对话框
//	 */
//	private UIDialog getWizard(AbstractWizardListPanel listPn, Dimension dim) {
//		AbstractWizardTabPn tabPn = listPn.getWizardTabPn();
//		WizardContainerDlg dlg = new WizardContainerDlg(m_rpt);
//		if (dim != null) {
//			dlg.setSize(dim);
//		}
//		dlg.setListPn(listPn);
//		dlg.setTabPn(tabPn);
//		return dlg;
//	}
//
//	private CellsModel getCellsModel(){
//		return m_rpt.getCellsModel();
//	}
//	
//	/**
//	 * 数据集函数设计向导主类（列表）
//	 */
//	public static AbstractWizardListPanel createDataSetFuncWizard(
//			DataSetFuncDesignObject dsdo, UfoCalcEnv calcEnv, UfoReport report) {
//		AbstractWizardListPanel listPn = new DataSetFuncDesignWizardListPn(
//				dsdo, calcEnv, report);
//		return listPn;
//	}

}
