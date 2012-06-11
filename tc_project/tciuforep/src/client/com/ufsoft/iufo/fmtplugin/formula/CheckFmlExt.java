package com.ufsoft.iufo.fmtplugin.formula;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;

public class CheckFmlExt extends AbsActionExt{

        /**
         * <code>descriptor</code> 的注释
         */
        private final FormulaDescriptor descriptor;

        /**
         * @param descriptor
         */
        CheckFmlExt(FormulaDescriptor descriptor) {
            this.descriptor = descriptor;
        }

        /*
         * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
         */
        public UfoCommand getCommand() {
            return new CheckFmlCmd();
        }

        /*
         * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
         */
        public Object[] getParams(UfoReport container) {
            return new Object[]{descriptor.getPlugin().getReport()};
//            try{                
//                ContextVO context = this.descriptor.getPlugin().getReport().getContextVo();
//     
//                //报表中存储的公式信息
//                Vector vecCheckFormulas = ((FormulaPlugin)this.descriptor.getPlugin()).getCheckFormula();
//                if(vecCheckFormulas == null){
//                    vecCheckFormulas = new Vector();
//                }     
//                for(; ; ){
//                    CheckFmlMngDlg checkMngDlg = new CheckFmlMngDlg(this.descriptor.getPlugin().getReport(), vecCheckFormulas, ((FormulaPlugin)this.descriptor.getPlugin()).getCheckExecutor());
//                    checkMngDlg.show();
//                    int mngRS = checkMngDlg.getResult();
//                    if(mngRS == UfoDialog.ID_OK){
//                        ((FormulaPlugin)this.descriptor.getPlugin()).setCheckFormula(checkMngDlg.getVecChecks());
//                        ((FormulaPlugin)this.descriptor.getPlugin()).setDirty(true);
//                        break;
//                    } else if(mngRS == UfoDialog.ID_CANCEL){
//                        break;
//                    } else if( (mngRS == CheckFmlMngDlg.ID_ADD) || (mngRS == CheckFmlMngDlg.ID_UPDATE)){
//                        vecCheckFormulas = checkMngDlg.getVecChecks();
//
//                        String strName = "";//审核名称
//                        String formula = ""; //审核公式内容
//                        String message = ""; //审核公式消息
//                        String condition = ""; //审核公式条件
//                        String condIfElse = ""; //审核公式 if else
//                        int messageLevel = 0; //审核公式信息级别
//                        RepCheckVO  checkVO = null;
//                        int nIndex = 0;
//                        
//                        if(mngRS == CheckFmlMngDlg.ID_UPDATE){
//                            checkVO = checkMngDlg.getSelectedVO();
//                            nIndex = checkMngDlg.getSelectedIndex();
//                        }
//                        for(; ; ){
//                            CheckFmlDefDlg defDlg = null;
//                            if(mngRS == CheckFmlMngDlg.ID_ADD){
//                                defDlg = new CheckFmlDefDlg((FormulaPlugin)this.descriptor.getPlugin());
//                            } else if(mngRS == CheckFmlMngDlg.ID_UPDATE){
//                                defDlg = new CheckFmlDefDlg(((FormulaPlugin)this.descriptor.getPlugin()), checkVO);
//
//                            }
//                            defDlg.setCheckFms(vecCheckFormulas);
//                            
//                 //           defDlg.setUserCheckFormula(formula);
//                            defDlg.setCheckCondition(condition);
//                            defDlg.setCheckMessage(message);
//                            defDlg.setMessageLevel(messageLevel);
//                            defDlg.setCheckIfElse(condIfElse);
//                            
//                            defDlg.show();
//                            int defRS = defDlg.getResult();
//                            if( (mngRS == CheckFmlMngDlg.ID_ADD) && (defRS == UfoDialog.ID_OK) ||
//                               defRS == CheckFmlDefDlg.ID_SAVEAS){
//                                vecCheckFormulas.add(defDlg.getRepCheckVO());
//                                break;
//                            } else if( (defRS == CheckFmlDefDlg.ID_WIZARDCOND) || (defRS == CheckFmlDefDlg.ID_WIZARDFORMULA)){
//                            	
//                                formula = defDlg.getUserCheckFormula();
//                                condition = defDlg.getCheckConditon();
//                                message = defDlg.getCheckMessage();
//                                condIfElse = defDlg.getCheckIfElse();
//                                messageLevel = defDlg.getMessageLevel();
//
////                                String unitID = null;
////                                UfoDocument udDoc = m_pnlView.getDocument();
////                                if(isPrivateRep){
////                                    unitID = udDoc.getContextEO().getCurUnitId();
////                                }
//                                CalcFmlDefWizardDlg dlgCellFuncWizard = new CalcFmlDefWizardDlg(this.descriptor.getPlugin().getReport());
////                                dlgCellFuncWizard.setUnitID(unitID);
//                                dlgCellFuncWizard.setContextVO(context);
//                                dlgCellFuncWizard.setModal(false);
//                                this.descriptor.getPlugin().getReport().setActivatingComponent(dlgCellFuncWizard);
//
//                                dlgCellFuncWizard.show();
//                                if(dlgCellFuncWizard.getResult() == UfoDialog.ID_OK){
//                                    String strWizard = dlgCellFuncWizard.getCellFunc();
//                                    if(defRS == CheckFmlDefDlg.ID_WIZARDFORMULA){
//                                        formula += strWizard;
//                                    } else{
//                                        condition += strWizard;
//
//                                    }
//                                }
//                                this.descriptor.getPlugin().getReport().setActivatingComponent(null);
//
//                            } else if(defRS == UfoDialog.ID_CANCEL){
//                                break;
//                            } else if( (mngRS == CheckFmlMngDlg.ID_UPDATE) && (defRS == CheckFmlDefDlg.ID_OK)){
//                                vecCheckFormulas.setElementAt(defDlg.getRepCheckVO(), nIndex);
//                                break;
//                            }
//
//                        }
//                    }
//                }
//
//            } catch(Exception e){
//                AppDebug.debug(e);
//            }
//            return null;
        }

        public ActionUIDes[] getUIDesArr() {
            ActionUIDes uiDes = new ActionUIDes();
            uiDes.setName(StringResource.getStringResource("miufo1001132"));
            uiDes.setPaths(new String[]{StringResource.getStringResource("miufo1001692"),
//                    StringResource.getStringResource("miufo1001615")
                    });
            uiDes.setGroup("formulaDefExt");
            return new ActionUIDes[]{uiDes};
        }	
	}