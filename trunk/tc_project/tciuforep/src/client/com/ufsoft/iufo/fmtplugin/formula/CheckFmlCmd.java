package com.ufsoft.iufo.fmtplugin.formula;
import java.util.Vector;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;

public class CheckFmlCmd extends UfoCommand implements IUfoContextKey{

    public void execute(Object[] params) {
        UfoReport report = (UfoReport) params[0];
        UfoContextVO contextVO = (UfoContextVO) report.getContextVo();
        FormulaModel formulaModel = FormulaModel.getInstance(report.getCellsModel());
//        FormulaDefPlugin formulaPI = (FormulaDefPlugin) report.getPluginManager().getPlugin(FormulaDefPlugin.class.getName());
//        FormulaModel formulaModel = formulaPI.getFormulaModel();
        try{ 
        	Vector vecComplexChecks=formulaModel.getComplexCheckFml();
        	Vector vecSimpleChecks=formulaModel.getSimpleCheckFml();
        	
        	int iReturnTab=CheckFmlMngDlg.SIMPLE_CHECK_TAB_INDEX;

            for(; ; ){
                CheckFmlMngDlg checkMngDlg = new CheckFmlMngDlg(report,report.getCellsModel(),vecComplexChecks, vecSimpleChecks,formulaModel.getUfoFmlExecutor(),iReturnTab,contextVO);
                checkMngDlg.setVisible(true);
                int mngRS = checkMngDlg.getResult();
                if(mngRS == UfoDialog.ID_OK){
                    formulaModel.setComplexCheckFml(checkMngDlg.getComplexChecks());
                    formulaModel.setSimpleCheckFml(checkMngDlg.getSimpleChecks());
//                    formulaPI.setDirty(true);
                    break;
                } else if(mngRS == UfoDialog.ID_CANCEL){
                    break;
                } else if( (mngRS == CheckFmlMngDlg.ID_ADD_COMPLEX) || (mngRS == CheckFmlMngDlg.ID_UPDATE_COMPLEX)){
                	iReturnTab=CheckFmlMngDlg.COMPLEX_CHECK_TAB_INDEX;
                    vecComplexChecks = checkMngDlg.getComplexChecks();
                    vecSimpleChecks=checkMngDlg.getSimpleChecks();
                    
                    String strName = "";//审核名称
                    String formula = ""; //审核公式内容
                    UfoSimpleObject func=null;
                    UfoSimpleObject module=null;
                    
//                    String message = ""; //审核公式消息
//                    String condition = ""; //审核公式条件 `
//                    String condIfElse = ""; //审核公式 if else
//                    int messageLevel = 0; //审核公式信息级别
                    RepCheckVO  checkVO = null;
                    int nIndex = 0;
                    
                    if(mngRS == CheckFmlMngDlg.ID_UPDATE_COMPLEX){
                        checkVO = checkMngDlg.getSelectedVO();
                         nIndex = checkMngDlg.getSelectedIndex();                       
                    }
                    boolean bReturnFromWizard = false;
                    for(; ; ){
                    	ComplexCheckFmlDlg defDlg=null;
//                        CheckFmlDefDlg defDlg = null;
                        if(mngRS == CheckFmlMngDlg.ID_ADD_COMPLEX){
//                            defDlg = new CheckFmlDefDlg(report);
                        	defDlg=new ComplexCheckFmlDlg(report,report.getCellsModel());
                        } else if(mngRS == CheckFmlMngDlg.ID_UPDATE_COMPLEX){
//                            defDlg = new CheckFmlDefDlg(report, checkVO);
                            defDlg=new ComplexCheckFmlDlg(report,report.getCellsModel(),checkVO);
                        }
                        if( bReturnFromWizard ){
                            defDlg.setCheckName(strName);
                            defDlg.setUserCheckFormula(formula);
                            defDlg.setSelectedFunc(module,func);
                            
//                            defDlg.setCheckCondition(condition);
//                            defDlg.setCheckMessage(message);
//                            defDlg.setMessageLevel(messageLevel);
//                            defDlg.setCheckIfElse(condIfElse);
                        }
                        defDlg.setVisible(true);
                        int defRS = defDlg.getResult();
                        if( (mngRS == CheckFmlMngDlg.ID_ADD_COMPLEX) && (defRS == UfoDialog.ID_OK) /*||
                           defRS == CheckFmlDefDlg.ID_SAVEAS*/){
                        	defDlg.getRepCheckVO().setID(RepCheckVO.getValidID());
                            vecComplexChecks.add(defDlg.getRepCheckVO());
                            break;
                        } else if(defRS == ComplexCheckFmlDlg.ID_WIZARDFORMULA){
                            strName = defDlg.getCheckName();
                            formula = defDlg.getUserCheckFormula();
                            func=defDlg.getSelectedFunc();
                            module=defDlg.getSeletedModule();
                            
                            int iFormulaCaretPos=defDlg.getFormulaCaretPos();
                            
//                            condition = defDlg.getCheckConditon();
//                            message = defDlg.getCheckMessage();
//                            condIfElse = defDlg.getCheckIfElse();
//                            messageLevel = defDlg.getMessageLevel();

                            String unitID = null;
                            boolean isPrivate = contextVO.getAttribute(PRIVATE) == null ? false : Boolean.parseBoolean(contextVO.getAttribute(PRIVATE).toString());
                            if(isPrivate){
                                unitID = (String)contextVO.getAttribute(CUR_UNIT_ID);
                            }
                            FunctionReferDlg dlgCellFuncWizard = new FunctionReferDlg(report.getTable().getCells(),report.getContextVo(),func);
                            
//                            dlgCellFuncWizard.setContextVO(contextVO);
                            dlgCellFuncWizard.setModal(false);
                            report.setActivatingComponent(dlgCellFuncWizard);

                            dlgCellFuncWizard.show();
                            if(dlgCellFuncWizard.getResult() == UfoDialog.ID_OK){
                                String strWizard = dlgCellFuncWizard.getCellFunc();
                                StringBuffer buf=new StringBuffer();
                                if(iFormulaCaretPos<formula.length() && iFormulaCaretPos>=0){
                                	buf.append(formula.substring(0,iFormulaCaretPos));
                                	buf.append(strWizard);
                                	buf.append(formula.substring(iFormulaCaretPos));
                                }else{
                                	buf.append(formula);
                                	buf.append(strWizard);
                                }
                                formula =buf.toString();

                            }
                            bReturnFromWizard = true;
                            report.setActivatingComponent(null);

                        } else if(defRS == UfoDialog.ID_CANCEL){
                            break;
                        } else if( (mngRS == CheckFmlMngDlg.ID_UPDATE_COMPLEX) && (defRS == ComplexCheckFmlDlg.ID_OK)){
                        	RepCheckVO repCheck=(RepCheckVO)vecComplexChecks.get(nIndex);
                        	if (repCheck!=null)
                        		defDlg.getRepCheckVO().setID(repCheck.getID());
                            vecComplexChecks.setElementAt(defDlg.getRepCheckVO(), nIndex);
                            break;
                        }

                    }
                }
            }

        } catch(Exception e){
AppDebug.debug(e);//@devTools             AppDebug.debug(e);
        }
    }

}
