package com.ufsoft.iufo.fmtplugin.formula;

import javax.swing.JOptionPane;

import com.ufida.dataset.IContext;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.key.AbsEditorAction;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IArea;

/**
 * ��ʽ�༭������ִ����
 * 
 * @author zhaopq
 * @created at 2009-4-17,����11:05:46
 * @since v56
 */
public class FormulaActionHandler extends AbsEditorAction implements
		IUfoContextKey {

	FormulaActionHandler(CellsPane cellsPane) {
		super(cellsPane);
	}

	@Override
	public void execute(Object[] params) {
		IContext context = getContextVo();

		// �ж���ѡ��Ķ��幫ʽ�������Ƿ����(���������ܶ��幫ʽ)
		IArea[] selAreas = getCellsModel().getSelectModel().getSelectedAreas();
		IArea selArea = selAreas != null && selAreas.length > 0 ? selAreas[0]
				: null;
		DynAreaCell[] dynCells = DynAreaModel.getInstance(getCellsModel())
				.getDynAreaCellByArea(selArea);
		if (dynCells != null && dynCells.length > 0) {
			if (dynCells.length == 1 && dynCells[0].getArea().contain(selArea)) {// ��̬����
				// �������ɴ˶δ���û�ã�����ע�͡�����ϸ���Ժ��ٳ���ȥ��
				// CellPosition selPos = getCellsModel().getSelectModel()
				// .getAnchorCell();
				// MeasureModel.getInstance(getCellsModel())
				// .getMeasureVOByPos(selPos);

			} else {
				String strMessage = StringResource
						.getStringResource("miufo1000976"); // "���������ܶ��幫ʽ"
				JOptionPane.showMessageDialog(getParent(), strMessage,
						StringResource.getStringResource("miufopublic384"),
						JOptionPane.OK_OPTION); // "��ʾ��Ϣ"
				return;
			}
		}

		// 2.���ѡ�������й�ʽ������ݹ�ʽ��Ӧ��������ѡ������
		IArea fmlArea = getFormulaModel().getRelatedFmlArea(selArea, true);
		if (fmlArea == null) {
			fmlArea = getFormulaModel().getRelatedFmlArea(selArea, false);
		}
		if (fmlArea != null && !fmlArea.isCell()) {
			selArea = fmlArea;
//			getCellsModel().getSelectModel().setSelectedArea(
//					(AreaPosition) fmlArea);
		}

		FormulaVO publicCellFormula = fmlArea == null ? null
				: getFormulaModel().getPublicDirectFml(fmlArea);

		// 4.�ж����ӹ�ʽ�������Ƿ����
		if (IufoFormulalUtil.isCreateUnit(context) == false) {
			if (publicCellFormula == null
					&& MeasureModel.getInstance(getCellsModel())
							.getMeasureVOByArea(selArea) == null) {
				JOptionPane.showMessageDialog(getParent(), StringResource
						.getStringResource("miufo1001712")); // "��ֻ�����޸����й�ʽ,������\n����ȡָ��ĵ�Ԫ���幫ʽ��"
				return;
			}
		}

		// for HR formula start
		// ������Ҫ���hr��ʽ�������֣�����ֻ������Ԫ��ʽ�����ݼȿ�
		// 5.����Ƿ�ʽ�򵼿ɱ༭��Ϣ���ҵ�ǰ����Ϣֻ��hr��Ч
		boolean editable = getFmlExecutor().hasSpecialEditTypeFunc(
				publicCellFormula);
		// /for HR formula end

		getFmlExecutor().setAnaRep(
				(Boolean)context.getAttribute(ANA_REP));

		UfoFmlEditDlg fmlEditDlg = new UfoFmlEditDlg(getCellsPane(), context,
				getFmlExecutor(), editable);
		fmlEditDlg.setArea(selAreas);
		fmlEditDlg.loadFormula(selArea);
		fmlEditDlg.show();

	}

	@Override
	public Object[] getParams() {
		return null;
	}

	private UfoFmlExecutor getFmlExecutor() {
		return getFormulaModel().getUfoFmlExecutor();
	}

	private FormulaModel getFormulaModel() {
		return FormulaModel.getInstance(getCellsModel());
	}
}
