/*
 * Created on 2005-6-30
 */
package com.ufida.report.multidimension.applet;

import java.awt.Component;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.itf.iufo.freequery.IMember;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;

import com.ufida.bi.base.BIException;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.multidimension.model.DataDrillSet;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.MultiDimemsionModel;
import com.ufida.report.multidimension.model.MultiDimensionUtil;
import com.ufida.report.multidimension.model.MultiReportSrvUtil;
import com.ufida.report.multidimension.model.SelDimMemberVO;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.multidimension.model.SelDimensionVO;
import com.ufida.report.rep.applet.BIReportApplet;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.ToggleMenuUIDes;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsEvent;
import com.ufsoft.table.ForbidedOprException;

/**
 * @author ll
 * 
 * ��ά��������ȡ���Ҽ��˵��ļ������ɺʹ���
 */
public class DimensionPopupMenuDes {

	/** ��ȡ���ݵ���ʾ��ʽ��׷�ӻ��滻�� */
	private static class DrillOptionExt extends AbsDimActionExt {
		/**
		 * @param plugin
		 */
		public DrillOptionExt(DimensionPlugin plugin) {
			super(plugin);
		}

		private class DrillOptionCommand extends UfoCommand {
			public void execute(Object[] params) {
				DataDrillSet dModel = getPlugIn().getModel().getDateDrillSet();
				int newOption, oldOption = dModel.getDrillOption();
				if (oldOption == IMultiDimConst.DRILL_OPTION_APPEND) {
					newOption = IMultiDimConst.DRILL_OPTION_NEW;
				} else {
					newOption = IMultiDimConst.DRILL_OPTION_APPEND;
				}
				dModel.setDrillOption(newOption);
			}
		}

		public UfoCommand getCommand() {
			return new DrillOptionCommand();
		}

		public Object[] getParams(UfoReport container) {
			return null;
		}

		public ActionUIDes[] getUIDesArr() {
			ToggleMenuUIDes desc = new ToggleMenuUIDes();
			desc.setName(StringResource.getStringResource("ubimultidim0050"));
			desc.setPopup(true);
			// desc.setCheckBox(true);

			return new ActionUIDes[] { desc };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.table.CellsModelListener#cellsChanged(com.ufsoft.table.CellsEvent)
		 */
		public void cellsChanged(CellsEvent event) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.table.Examination#isSupport(int,
		 *      java.util.EventObject)
		 */
		public String isSupport(int source, EventObject e) throws ForbidedOprException {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isEnabled(Component focusComp) {
			boolean superEnabled = super.isEnabled(focusComp);

			return superEnabled && (getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_INPUT);
		}

	}

	/** ������ȡ��4�ַ�ʽ */
	private static class DrillTypeExt extends AbsDimActionExt {
		private class DrillTypeCommand extends UfoCommand {
			public void execute(Object[] params) {
				DataDrillSet dModel = getPlugIn().getModel().getDateDrillSet();
				dModel.setDrillType(m_drillType);
			}
		}

		private int m_drillType = IMultiDimConst.DATA_DRILLNEXT;

		public DrillTypeExt(DimensionPlugin plugin, int drillType) {
			super(plugin);
			m_drillType = drillType;
		}

		public boolean isEnabled(Component focusComp) {
			boolean superEnabled = super.isEnabled(focusComp);

			return superEnabled && (getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_INPUT);
		}

		public UfoCommand getCommand() {
			return new DrillTypeCommand();
		}

		public Object[] getParams(UfoReport container) {
			return null;
		}

		public ActionUIDes[] getUIDesArr() {
			ToggleMenuUIDes desc = new ToggleMenuUIDes();
			desc.setName(getMenuName(m_drillType));
			desc.setPaths(new String[] { StringResource.getStringResource("ubimultidim0041") });
			desc.setPopup(true);
			desc.setButtonGroup(StringResource.getStringResource("ubimultidim0041"));// ����ButtonGroup

			return new ActionUIDes[] { desc };
		}

	}

	/** ������ȡ����������4�ֲ�����������ȡҪ����ȡ���ͣ� */
	private static class DrillDetailExt extends AbsDimActionExt {

		private class DrillDetailCommand extends UfoCommand {
			public void execute(Object[] params) {
				UfoReport report = (UfoReport) params[0];
				int drilltype = m_drill;
				if (m_drill == DATA_DRILL)
					drilltype = getPlugIn().getModel().getDateDrillSet().getDrillType();

				// TODO Ӧ�ô����ȥ��ͷ����
				CellPosition pos = report.getCellsModel().getSelectModel().getSelectedArea().getStart();

				execDrill(report, getPlugIn(), pos, drilltype);

			}
		}

		private int m_drill = -1;

		public DrillDetailExt(DimensionPlugin plugin, int drill_type) {
			super(plugin);
			m_drill = drill_type;
		}

		public boolean isEnabled(Component focusComp) {
			boolean superEnabled = super.isEnabled(focusComp);

			return superEnabled && (getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_INPUT);
		}

		public UfoCommand getCommand() {
			return new DrillDetailCommand();
		}

		public Object[] getParams(UfoReport container) {
			return new Object[] { container };
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes desc = new ActionUIDes();
			desc.setName(getMenuName(m_drill));
			desc.setPopup(true);

			return new ActionUIDes[] { desc };
		}

	}

	/** ִ�б���͸ */
	private static class DrillThroughExt extends AbsDimActionExt {
		/**
		 * @param plugin
		 */
		public DrillThroughExt(DimensionPlugin plugin) {
			super(plugin);
		}

		private class DrillThroughCommand extends UfoCommand {
			public void execute(Object[] params) {
				UfoReport report = (UfoReport) params[0];

				// TODO Ӧ�ô����ȥ��ͷ����
				CellPosition pos = report.getCellsModel().getSelectModel().getSelectedArea().getStart();
				try{
				BaseReportModel newModel = MultiDimensionUtil.getDrillThoughModel(getPlugIn().getModel(), pos, false);
				BIReportApplet.drillThrough(newModel, report, getPlugIn().getOperationState());
				}catch(BIException ex){
					AppDebug.debug(ex);
					JOptionPane.showMessageDialog(report, ex.getMessage());
				}

			}
		}

		public UfoCommand getCommand() {
			return new DrillThroughCommand();
		}

		public Object[] getParams(UfoReport container) {
			return new Object[] { container };
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes desc = new ActionUIDes();
			desc.setName(StringResource.getStringResource("ubimultidim0052"));
			desc.setPopup(true);

			return new ActionUIDes[] { desc };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.table.CellsModelListener#cellsChanged(com.ufsoft.table.CellsEvent)
		 */
		public void cellsChanged(CellsEvent event) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ufsoft.table.Examination#isSupport(int,
		 *      java.util.EventObject)
		 */
		public String isSupport(int source, EventObject e) throws ForbidedOprException {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isEnabled(Component focusComp) {
			boolean superEnabled = super.isEnabled(focusComp);

			return superEnabled && (getPlugIn().getReport().getOperationState() == UfoReport.OPERATION_INPUT);
		}

	}

	public static final int DATA_DRILL = 99;// ��ȡ�˵���ı�ʶ��ִ�в���ʱ��Ҫת����ģ���е���ȡ��������

	public static String getMenuName(int drilltype) {
		switch (drilltype) {
		case IMultiDimConst.DATA_DRILLNEXT:
			return StringResource.getStringResource("ubimultidim0042");
		case IMultiDimConst.DATA_DRILLDESCENDANT:
			return StringResource.getStringResource("ubimultidim0043");
		case IMultiDimConst.DATA_DRILLBUTTOM:
			return StringResource.getStringResource("ubimultidim0044");
		case IMultiDimConst.DATA_DRILLSAME_GENERATION:
			return StringResource.getStringResource("ubimultidim0045");
		case IMultiDimConst.DATA_DRILLUP:
			return StringResource.getStringResource("ubimultidim0046");
		case IMultiDimConst.DATA_DRILLTOP:
			return StringResource.getStringResource("ubimultidim0047");
		case IMultiDimConst.DATA_DRILLOTHER:
			return StringResource.getStringResource("ubimultidim0048");

		case DATA_DRILL:
			return StringResource.getStringResource("ubimultidim0040");

		default:
			return null;
		}
	}

	public static IExtension[] getPopupMenuExts(DimensionPlugin plugin) {
		// if (plugin.getInitState() == UfoReport.OPERATION_FORMAT)
		// return new IExtension[0];
		//
		return new IExtension[] { new DrillOptionExt(plugin), new DrillTypeExt(plugin, IMultiDimConst.DATA_DRILLNEXT),
				new DrillTypeExt(plugin, IMultiDimConst.DATA_DRILLDESCENDANT),
				new DrillTypeExt(plugin, IMultiDimConst.DATA_DRILLBUTTOM),
				new DrillTypeExt(plugin, IMultiDimConst.DATA_DRILLSAME_GENERATION),

				new DrillDetailExt(plugin, DATA_DRILL), new DrillDetailExt(plugin, IMultiDimConst.DATA_DRILLUP),
				new DrillDetailExt(plugin, IMultiDimConst.DATA_DRILLTOP),
				new DrillDetailExt(plugin, IMultiDimConst.DATA_DRILLOTHER), new DrillThroughExt(plugin) };

	}

	/** ִ����ȡ������ֱ�Ӹı��άģ�Ͳ�ִ�в�ѯ */
	static void execDrill(UfoReport report, DimensionPlugin plugin, CellPosition pos, int drilltype) {

		IMember selMember = MultiDimensionUtil.getSelectedDimMember(plugin.getModel(), pos);

		String msg = null;
		if (selMember == null || selMember.getDimID() == null) { // ��ѡ��ά�ȳ�Ա������ȡ
			msg = StringResource.getStringResource("mbimultidim0001");
		} else {
			BIContextVO context = (BIContextVO) report.getContextVo();
			String userID = context.getCurUserID();
			String reportID = context.getReportPK();

			// ��ȡ������ά�Ƚ������⴦��
			if (drilltype == IMultiDimConst.DATA_DRILLOTHER) {
				msg = processDrillOther(report, plugin.getModel(), selMember);
			} else {
				msg = MultiDimensionUtil.processDataDrill(plugin.getModel(), selMember, drilltype, reportID, userID);
			}
		}
		if (msg != null) {
			MessageDialog.showHintDlg(report, null, msg);
			return;
		}
		// plugin.getModel().createMultiDimCellsModel(report.getCellsModel());

	}

	// /** ִ�б���͸��ֱ�Ӹı��άģ�Ͳ�ִ�в�ѯ */
	// static void execDrillThrough(UfoReport report, DimensionPlugin plugin,
	// CellPosition pos) {
	//
	// MultiDimemsionModel newModel =
	// MultiDimensionUtil.getDrillThoughModel(plugin.getModel(), pos);
	//
	// if (newModel != null) {
	// newModel.setCellsModel(plugin.getModel().getCellsModel());
	//
	// plugin.setModel(newModel);
	// newModel.setSelDimModel(newModel.getSelDimModel());// ���ڷ�����Ϣ
	// plugin.setOperationState(report.getOperationState());
	//
	// }
	// }

	/** ��ȡ������ά�ȵķ�֧����ֱ�Ӹı��άģ�Ͳ�ִ�в�ѯ */
	private static String processDrillOther(UfoReport report, MultiDimemsionModel multiDimModel, IMember selMember) {
		String userID = ((BIContextVO) report.getContextVo()).getCurUserID();

		/** ����ִ����ȡ��ά�ȳ�Աλ�ã���ţ� */
		SelDimModel dimModel = multiDimModel.getSelDimModel();
		String pk_dimVO = selMember == null ? null : selMember.getDimID();

		SelDimensionVO[] unselDims = dimModel.getSelDimVOs(IMultiDimConst.POS_UNSEL);
		if (unselDims == null || unselDims.length == 0)
			return StringResource.getStringResource("mbimultidim0004");// û�д�ѡά�ȿɹ���ȡ

		SelDimensionVO mainDim = unselDims[0];
		if (unselDims.length > 1) {
			// TODO �������д�ѡά�ȣ����û�ѡ��
		}
		// �����û��ѡ���Ա��������Աѡ���
		if (mainDim.getSelMembers() == null) {
			IMember[] allMembers = (IMember[]) MultiReportSrvUtil.getAllMember(mainDim.getDimDef(), userID);
			SelMemberDialog memberDlg = new SelMemberDialog(report);
			memberDlg.setParams(allMembers, null);

			if (memberDlg.showModal() == UIDialog.ID_OK) {
				SelDimMemberVO[] selmembers = memberDlg.getSelMembers();
				mainDim.setSelMembers(selmembers);
			}
		}
		if (mainDim.getSelMembers() == null)
			return null;

		SelDimensionVO[] columnDims = dimModel.getSelDimVOs(IMultiDimConst.POS_COLUMN);
		SelDimensionVO[] rowDims = dimModel.getSelDimVOs(IMultiDimConst.POS_ROW);

		SelDimensionVO selDim = null;
		boolean isDrillRowDim = false;// ��ȡ�����У�/�У�ά��
		int dimIndex = -1; // ��ȡ��ά�����

		// ��������ά�ȵ�λ��
		for (int i = 0; i < rowDims.length; i++) {
			if (rowDims[i].getDimDef().getPrimaryKey().equals(pk_dimVO)) {
				isDrillRowDim = true;
				dimIndex = i;
				selDim = rowDims[i];
				break;
			}
		}
		if (selDim == null) {
			for (int i = 0; i < columnDims.length; i++) {
				if (columnDims[i].getDimDef().getPrimaryKey().equals(pk_dimVO)) {
					dimIndex = i;
					selDim = columnDims[i];
					break;
				}
			}
		}

		// �����µ�ά��
		SelDimensionVO[] dims = isDrillRowDim ? rowDims : columnDims;
		Vector<SelDimensionVO> vec = new Vector<SelDimensionVO>();
		for (int i = 0; i < dims.length; i++) {
			vec.addElement(dims[i]);
		}
		vec.add(dimIndex + 1, mainDim);
		dims = new SelDimensionVO[vec.size()];
		vec.copyInto(dims);
		dimModel.setSelDimVOs(isDrillRowDim ? IMultiDimConst.POS_ROW : IMultiDimConst.POS_COLUMN, dims);

		// �Ӵ�ѡά����ȥ��
		vec.clear();
		for (int i = 0; i < unselDims.length; i++) {
			if (!unselDims[i].getDimDef().getPrimaryKey().equals(mainDim.getDimDef().getPrimaryKey()))
				vec.addElement(unselDims[i]);
		}
		unselDims = new SelDimensionVO[vec.size()];
		vec.copyInto(unselDims);
		dimModel.setSelDimVOs(IMultiDimConst.POS_UNSEL, unselDims);

		// ����ִ�в�ѯ
		multiDimModel.setOperationState(multiDimModel.getOperationState(), ((BIContextVO) report.getContextVo())
				.getReportPK(), userID);

		return null;
	}

}