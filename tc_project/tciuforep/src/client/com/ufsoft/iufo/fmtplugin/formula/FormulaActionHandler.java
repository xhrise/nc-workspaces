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
 * 公式编辑动作的执行者
 * 
 * @author zhaopq
 * @created at 2009-4-17,上午11:05:46
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

		// 判断所选择的定义公式的区域是否合理(交叉区域不能定义公式)
		IArea[] selAreas = getCellsModel().getSelectModel().getSelectedAreas();
		IArea selArea = selAreas != null && selAreas.length > 0 ? selAreas[0]
				: null;
		DynAreaCell[] dynCells = DynAreaModel.getInstance(getCellsModel())
				.getDynAreaCellByArea(selArea);
		if (dynCells != null && dynCells.length > 0) {
			if (dynCells.length == 1 && dynCells[0].getArea().contain(selArea)) {// 动态区内
				// 初步怀疑此段代码没用，所以注释。等详细测试后再彻底去掉
				// CellPosition selPos = getCellsModel().getSelectModel()
				// .getAnchorCell();
				// MeasureModel.getInstance(getCellsModel())
				// .getMeasureVOByPos(selPos);

			} else {
				String strMessage = StringResource
						.getStringResource("miufo1000976"); // "交叉区域不能定义公式"
				JOptionPane.showMessageDialog(getParent(), strMessage,
						StringResource.getStringResource("miufopublic384"),
						JOptionPane.OK_OPTION); // "提示信息"
				return;
			}
		}

		// 2.如果选择区域含有公式，则根据公式对应区域修正选择区域。
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

		// 4.判断增加公式的区域是否合理
		if (IufoFormulalUtil.isCreateUnit(context) == false) {
			if (publicCellFormula == null
					&& MeasureModel.getInstance(getCellsModel())
							.getMeasureVOByArea(selArea) == null) {
				JOptionPane.showMessageDialog(getParent(), StringResource
						.getStringResource("miufo1001712")); // "您只允许修改现有公式,或者在\n已提取指标的单元定义公式！"
				return;
			}
		}

		// for HR formula start
		// 由于主要针对hr公式进行区分，所以只解析单元公式的内容既可
		// 5.获得是否公式向导可编辑信息。且当前此信息只对hr有效
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
