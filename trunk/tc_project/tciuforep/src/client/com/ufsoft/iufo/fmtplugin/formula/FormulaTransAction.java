package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.event.ActionEvent;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import nc.vo.iufo.unit.UnitInfoVO;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * 公式套用组件
 */
public class FormulaTransAction extends AbsIUFORptDesignerPluginAction{

	@Override
	public void execute(ActionEvent e) {
		if(!isEnabled()){
			return;
		}
		IContext context = getCurrentView().getContext();
		FormulaModel formulaModel = FormulaModel.getInstance(getCellsModel());

		String strCurUnitPK = (String) context.getAttribute(CUR_UNIT_ID);
		String strCurUnitCode =(String) context.getAttribute(CUR_UNIT_CODE);

		if (strCurUnitPK == null)
			return;
		try {
			while (true) {
				// 得到录入的数值
				FormulaTransDlg fmlTransDlg = new FormulaTransDlg();
				fmlTransDlg.showModal();

				String strSourceCode = null;
				if (fmlTransDlg.getResult() == UfoDialog.ID_OK)
					strSourceCode = fmlTransDlg.getUnitCode();
				else {
					break;
				}
				if (strSourceCode == null || strSourceCode.trim().length() == 0) {
					break;
				}

				// 执行导入操作人员的身份检查,自己无需为自己导入
				if (strSourceCode.equalsIgnoreCase(strCurUnitCode)) {
					return;
				}
				// 导入的单位不存在,或者导入的单位不在当前服务器中.
				UnitInfoVO unitInfo = nc.ui.iufo.unit.UnitMngBO_Client
						.findUnitByCode(strSourceCode);
				if (unitInfo == null) {
					JOptionPane.showMessageDialog(getCellsPane(), StringResource
							.getStringResource("miufo1001718"), StringResource
							.getStringResource("miufopublic384"),
							JOptionPane.WARNING_MESSAGE);
					break;
				}
				String strSourceUnitPK = unitInfo.getPK();
				// 如果导入过程中,发现目标的报表中包含公式,提示该操作将删除原来的公式
				// 得到要导入的单位的公式信息
				// FormulaDefPlugin formulaPI = (FormulaDefPlugin) report
				// .getPluginManager().getPlugin(
				// FormulaDefPlugin.class.getName());

				Hashtable hashSrcUnit = formulaModel
						.getUnitPersonalFormula(strSourceUnitPK);

				if (hashSrcUnit == null || hashSrcUnit.size() == 0) {
					JOptionPane.showMessageDialog(getCellsPane(), StringResource
							.getStringResource("miufo1001719"), StringResource
							.getStringResource("miufopublic384"),
							JOptionPane.WARNING_MESSAGE);
					continue;
				} else {
					int nRs = JOptionPane.showConfirmDialog(getCellsPane(),
							StringResource.getStringResource("miufo1001720"),
							StringResource.getStringResource("miufopublic384"),
							JOptionPane.YES_NO_OPTION);
					if (nRs != JOptionPane.YES_OPTION) { // 继续执行。
						break;
					}
				}
				// 将源公式设入

				formulaModel.copyPersonalFormula(strSourceUnitPK, strCurUnitPK);
				formulaModel.getUfoFmlExecutor().reInitformulaLet();
				break;
			}
		} catch (Exception ex) {
			AppDebug.debug(e);
			JOptionPane.showMessageDialog(getCellsPane(), ex.getLocalizedMessage(),
					StringResource.getStringResource("miufo1000109"),
					JOptionPane.WARNING_MESSAGE); // "异常信息"
		}

	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource.getStringResource("miufo1001692"), FormulaPlugin.GROUP);
		descriptor.setName(StringResource.getStringResource("miufo1001691"));//"套用公式"
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU});
		descriptor.setShowDialog(true);
		return descriptor;
	}

}
