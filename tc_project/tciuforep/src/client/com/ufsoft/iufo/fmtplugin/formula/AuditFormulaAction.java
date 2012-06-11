package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.event.ActionEvent;
import java.util.Vector;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.AbstractRepPluginAction;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * 审核公式组件
 */
public class AuditFormulaAction extends AbstractRepPluginAction implements IUfoContextKey{

	@Override
	public void execute(ActionEvent e) {
		if(!isEnabled()){
			return;
		}
		IContext context = getCurrentView().getContext();
		FormulaModel formulaModel = FormulaModel.getInstance(getCellsModel());
		try {
			Vector vecComplexChecks = formulaModel.getComplexCheckFml();
			Vector vecSimpleChecks = formulaModel.getSimpleCheckFml();

			int iReturnTab = CheckFmlMngDlg.SIMPLE_CHECK_TAB_INDEX;
			UfoFmlExecutor fmlExecutor = (UfoFmlExecutor)formulaModel.getUfoFmlExecutor();
			
			for (;;) {
				CheckFmlMngDlg checkMngDlg = new CheckFmlMngDlg(getCellsPane(),getCellsModel(),
						vecComplexChecks, vecSimpleChecks, fmlExecutor, iReturnTab, context);
				checkMngDlg.setVisible(true);
				int mngRS = checkMngDlg.getResult();
				if (mngRS == UfoDialog.ID_OK) {
					formulaModel.setComplexCheckFml(checkMngDlg
							.getComplexChecks());
					formulaModel.setSimpleCheckFml(checkMngDlg
							.getSimpleChecks());
					break;
				} else if (mngRS == UfoDialog.ID_CANCEL) {
					break;
				} else if ((mngRS == CheckFmlMngDlg.ID_ADD_COMPLEX)
						|| (mngRS == CheckFmlMngDlg.ID_UPDATE_COMPLEX)) {
					iReturnTab = CheckFmlMngDlg.COMPLEX_CHECK_TAB_INDEX;
					vecComplexChecks = checkMngDlg.getComplexChecks();
					vecSimpleChecks = checkMngDlg.getSimpleChecks();

					String strName = "";// 审核名称
					String formula = ""; // 审核公式内容
					UfoSimpleObject func = null;
					UfoSimpleObject module = null;

					RepCheckVO checkVO = null;
					int nIndex = 0;

					if (mngRS == CheckFmlMngDlg.ID_UPDATE_COMPLEX) {
						checkVO = checkMngDlg.getSelectedVO();
						nIndex = checkMngDlg.getSelectedIndex();
					}
					boolean bReturnFromWizard = false;
					for (;;) {
						ComplexCheckFmlDlg defDlg = null;
						if (mngRS == CheckFmlMngDlg.ID_ADD_COMPLEX) {
							defDlg = new ComplexCheckFmlDlg(getCellsPane(),getCellsModel());
						} else if (mngRS == CheckFmlMngDlg.ID_UPDATE_COMPLEX) {
							defDlg = new ComplexCheckFmlDlg(getCellsPane(),getCellsModel(),checkVO);
						}
						if (bReturnFromWizard) {
							defDlg.setCheckName(strName);
							defDlg.setUserCheckFormula(formula);
							defDlg.setSelectedFunc(module, func);
						}
						defDlg.setVisible(true);
						int defRS = defDlg.getResult();
						if ((mngRS == CheckFmlMngDlg.ID_ADD_COMPLEX)
								&& (defRS == UfoDialog.ID_OK) /*
																 * || defRS ==
																 * CheckFmlDefDlg.ID_SAVEAS
																 */) {
							defDlg.getRepCheckVO().setID(
									RepCheckVO.getValidID());
							vecComplexChecks.add(defDlg.getRepCheckVO());
							break;
						} else if (defRS == ComplexCheckFmlDlg.ID_WIZARDFORMULA) {
							strName = defDlg.getCheckName();
							formula = defDlg.getUserCheckFormula();
							func = defDlg.getSelectedFunc();
							module = defDlg.getSeletedModule();

							int iFormulaCaretPos = defDlg.getFormulaCaretPos();

							String unitID = null;
							boolean isPrivate = context.getAttribute(PRIVATE) == null ? false
									: Boolean.parseBoolean(context
											.getAttribute(PRIVATE).toString());
							if (isPrivate) {
								unitID = (String) context
										.getAttribute(CUR_UNIT_ID);
							}
							FunctionReferDlg dlgCellFuncWizard = new FunctionReferDlg(getCellsPane(), context, func,fmlExecutor.getFuncListInst());
                            dlgCellFuncWizard.setFmlExecutor(fmlExecutor);
							dlgCellFuncWizard.setModal(false);
//							report.setActivatingComponent(dlgCellFuncWizard);

							dlgCellFuncWizard.show();
							if (dlgCellFuncWizard.getResult() == UfoDialog.ID_OK) {
								String strWizard = dlgCellFuncWizard
										.getCellFunc();
								StringBuffer buf = new StringBuffer();
								if (iFormulaCaretPos < formula.length()
										&& iFormulaCaretPos >= 0) {
									buf.append(formula.substring(0,
											iFormulaCaretPos));
									buf.append(strWizard);
									buf.append(formula
											.substring(iFormulaCaretPos));
								} else {
									buf.append(formula);
									buf.append(strWizard);
								}
								formula = buf.toString();

							}
							bReturnFromWizard = true;
//							report.setActivatingComponent(null);

						} else if (defRS == UfoDialog.ID_CANCEL) {
							break;
						} else if ((mngRS == CheckFmlMngDlg.ID_UPDATE_COMPLEX)
								&& (defRS == ComplexCheckFmlDlg.ID_OK)) {
							RepCheckVO repCheck = (RepCheckVO) vecComplexChecks
									.get(nIndex);
							if (repCheck != null)
								defDlg.getRepCheckVO().setID(repCheck.getID());
							vecComplexChecks.setElementAt(defDlg
									.getRepCheckVO(), nIndex);
							break;
						}

					}
				}
			}

		} catch (Exception ex) {
			AppDebug.debug(ex);
		}
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource.getStringResource("miufo1001692"), FormulaPlugin.GROUP);
		descriptor.setName(StringResource.getStringResource("miufo1001132"));//"审核公式"
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU});
		descriptor.setShowDialog(true);
		return descriptor;
	}

}
