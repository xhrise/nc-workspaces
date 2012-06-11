/*
 * �������� 2006-4-10
 *
 */
package com.ufsoft.iufo.fmtplugin.formula;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import nc.vo.iufo.unit.UnitInfoVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;

/**
 * @author ljhua
 * ��ʽ����
 */
public class FormulaTransCmd extends UfoCommand implements IUfoContextKey{

	/* ���� Javadoc��
	 * @see com.ufsoft.report.command.UfoCommand#execute(java.lang.Object[])
	 */
	public void execute(Object[] params) {
		UfoReport report = (UfoReport) params[0];
		UfoContextVO contextVO = (UfoContextVO) report.getContextVo();
		FormulaModel formulaModel = FormulaModel.getInstance(report.getCellsModel());
		
		String strCurUnitPK   = contextVO == null ? null : (String)contextVO.getAttribute(CUR_UNIT_ID);
		String strCurUnitCode = contextVO == null ? null : (String)contextVO.getAttribute(CUR_UNIT_CODE);
		
		if (strCurUnitPK == null)
			return;
		try {
			while (true) {
				//�õ�¼�����ֵ
				FormulaTransDlg fmlTransDlg = new FormulaTransDlg();
				fmlTransDlg.showModal();
				
				String strSourceCode = null;
				if(fmlTransDlg.getResult() == UfoDialog.ID_OK)
					strSourceCode = fmlTransDlg.getUnitCode();
				else {
		            break;
				}
//				String strSourceCode = JOptionPane.showInputDialog(report,
//						StringResource.getStringResource("miufo1001716"),
//						StringResource.getStringResource("miufo1001717"),
//						JOptionPane.PLAIN_MESSAGE);
				
				if (strSourceCode == null || strSourceCode.trim().length() == 0) {
					break;
				}
				
				//ִ�е��������Ա����ݼ��,�Լ�����Ϊ�Լ�����
				if (strSourceCode.equalsIgnoreCase(strCurUnitCode)) {
					return;
				}
				//����ĵ�λ������,���ߵ���ĵ�λ���ڵ�ǰ��������.
				UnitInfoVO unitInfo = nc.ui.iufo.unit.UnitMngBO_Client.findUnitByCode(strSourceCode);
				if (unitInfo == null) {
					JOptionPane.showMessageDialog(report, StringResource
							.getStringResource("miufo1001718"), StringResource
							.getStringResource("miufopublic384"),
							JOptionPane.WARNING_MESSAGE);
					break;
				}
				String strSourceUnitPK = unitInfo.getPK();
				//������������,����Ŀ��ı����а�����ʽ,��ʾ�ò�����ɾ��ԭ���Ĺ�ʽ
				//�õ�Ҫ����ĵ�λ�Ĺ�ʽ��Ϣ
//				FormulaDefPlugin formulaPI = (FormulaDefPlugin) report
//						.getPluginManager().getPlugin(
//								FormulaDefPlugin.class.getName());

				Hashtable hashSrcUnit = formulaModel
						.getUnitPersonalFormula(strSourceUnitPK);

				if (hashSrcUnit == null || hashSrcUnit.size() == 0) {
					JOptionPane.showMessageDialog(report, StringResource
							.getStringResource("miufo1001719"), StringResource
							.getStringResource("miufopublic384"),
							JOptionPane.WARNING_MESSAGE);
					continue;
				} else {
					int nRs = JOptionPane.showConfirmDialog(report,
							StringResource.getStringResource("miufo1001720"),
							StringResource.getStringResource("miufopublic384"),
							JOptionPane.YES_NO_OPTION);
					if (nRs != JOptionPane.YES_OPTION) { //����ִ�С�
						break;
					}
				}
				//��Դ��ʽ����
				
				formulaModel.copyPersonalFormula(strSourceUnitPK, strCurUnitPK);
				formulaModel.getUfoFmlExecutor().reInitformulaLet();
				break;
			}
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			JOptionPane.showMessageDialog(report, e.getLocalizedMessage(),
					StringResource.getStringResource("miufo1000109"),
					JOptionPane.WARNING_MESSAGE); //"�쳣��Ϣ"
		}

	}

}
