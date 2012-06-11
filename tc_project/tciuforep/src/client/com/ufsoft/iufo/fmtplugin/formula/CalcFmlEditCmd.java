package com.ufsoft.iufo.fmtplugin.formula;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.key.AbsEditorAction;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.table.CellsModel;

/**
 * ��Ԫ��ʽ Description:��Ԫ��ʽCommand.
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
//		// @edit by wangyga at 2009-3-12,����09:37:00		
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
//		// �ж���ѡ��Ķ��幫ʽ�������Ƿ����(���������ܶ��幫ʽ)
//		IArea[] selAreas = m_cm.getSelectModel().getSelectedAreas();
//		IArea selArea = selAreas != null && selAreas.length > 0 ? selAreas[0]
//				: null;
//		DynAreaCell[] dynCells = getDynAreaModel().getDynAreaCellByArea(selArea);						
//		if (dynCells != null && dynCells.length > 0) {
//			if (dynCells.length == 1 && dynCells[0].getArea().contain(selArea)) {// ��̬����
//				CellPosition selPos = m_cm.getSelectModel().getAnchorCell();
//				MeasureVO mvo = getMeasureModel().getMeasureVOByPos(selPos);
//						
//			} else {
//				String strMessage = StringResource
//						.getStringResource("miufo1000976"); // "���������ܶ��幫ʽ"
//				JOptionPane.showMessageDialog(m_rpt, strMessage, StringResource
//						.getStringResource("miufopublic384"),
//						JOptionPane.OK_OPTION); // "��ʾ��Ϣ"
//				return;
//			}
//		}
//
//		// 2.���ѡ�������й�ʽ������ݹ�ʽ��Ӧ��������ѡ������
//		IArea fmlArea = getFormulaModel().getRelatedFmlArea(selArea, true);
//		if (fmlArea == null) {
//			fmlArea = getFormulaModel().getRelatedFmlArea(selArea, false);
//		}
//		if (fmlArea != null && !fmlArea.isCell()) {
//			selArea = fmlArea;
//			m_cm.getSelectModel().setSelectedArea((AreaPosition) fmlArea);
//		}
//
//		// 3.���ѡ�������Ӧ��ʽ��Ϣ
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
//		// 4.�ж����ӹ�ʽ�������Ƿ����
//		if (context.isCreateUnit() == false) {
//			if (publicCellFormula == null					
//					&& getMeasureModel().getMeasureVOByArea(
//							selArea) == null) {
//				JOptionPane.showMessageDialog(m_rpt, StringResource
//						.getStringResource("miufo1001712")); // "��ֻ�����޸����й�ʽ,������\n����ȡָ��ĵ�Ԫ���幫ʽ��"
//				return;
//			}
//		}
//
//		// for HR formula start
//		// ������Ҫ���hr��ʽ�������֣�����ֻ������Ԫ��ʽ�����ݼȿ�
//
//		// 5.����Ƿ�ʽ�򵼿ɱ༭��Ϣ���ҵ�ǰ����Ϣֻ��hr��Ч
//		boolean bIsEditable = getFormulaHandler().hasSpecialEditTypeFunc(
//				publicCellFormula);
//
//		// /for HR formula end
//
//		// 6.������ʽ�༭ڲ�����������ӵĹ�ʽ��Ϣ��
//		String strFormula = null;
//		String strOldFormula = null;
//		UfoSimpleObject func = null;
//		UfoSimpleObject module = null;
//		int currentTab = 0;
//		int currentFuncTab = 0;// add by wangyga 2008-7-31 ��ǰѡ�й�ʽ�����ҳǩ
//
//		// �ж��Ƿ���ӱ��
//		boolean bSpreadSheet = false;
//		// ��ǹ�ʽ���Ƿ��۵�,���true��Ҫ���¼���������Ĺ�ʽ����
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
//			// ���Ի���ʽ������
//			// modify by ljhua 2005-3-17 �޸Ĺ�ʽ����Ӧ�������ݿ��ʽ�Ĺ�ʽ����
//
//			// 6.1��ù����û���ʽ�Ĺ�ʽ����
//			String publicFml = getFormulaHandler().getPublicUserDefFmlContent(
//					selArea);
//
//			if (isFold) {// ���ȴ�������������������ʽ
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
//				if (isWizrd) {// �϶��޸��˹�ʽ
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
//					// ���й�ʽ���������
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
//					// ׼�����ݼ�������ݽṹ
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
//					// ���ݼ��������б����
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
//				// �õ���Ԫ��ʽ
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
	 * �õ���ʽ�����򵼷��صĹ�ʽ����
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
//	 * ����GETDATA�����������ݼ���������NULL.
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
//	 * add by ����� 2008-7-2 ��ӵ�Ԫ��Ĺ�ʽ��Ԫ�Ƿ�����༭����
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
//	 * ��ʾ�򵼶Ի���
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
//	 * ���ݼ�������������ࣨ�б�
//	 */
//	public static AbstractWizardListPanel createDataSetFuncWizard(
//			DataSetFuncDesignObject dsdo, UfoCalcEnv calcEnv, UfoReport report) {
//		AbstractWizardListPanel listPn = new DataSetFuncDesignWizardListPn(
//				dsdo, calcEnv, report);
//		return listPn;
//	}

}
