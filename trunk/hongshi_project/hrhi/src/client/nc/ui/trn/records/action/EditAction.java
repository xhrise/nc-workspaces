package nc.ui.trn.records.action;

import nc.ui.hr.frame.FrameUI;
import nc.ui.hr.frame.util.BillPanelUtils;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.smtm.pub.CommonValue;
import nc.ui.trn.records.ListRefUtils;
import nc.ui.trn.records.StafTransRdsDataModel;
import nc.ui.trn.records.StafTransRdsStateReg;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;

public class EditAction extends RdsBaseAction {

	private int selrow = -1;

	private SuperVO selvalue = null;

	public EditAction(FrameUI frameUI1) {
		super(frameUI1);
	}

	@Override
	public void execute() throws Exception {

		if (strTabCode != null && strTabCode.trim().length() > 0)// �в���������ò�����Ӧҳǩ
		{
			getMainPnl().setMainPanelEnabled(true);
			getMainPnl().editLine(strTabCode);
		}
		getParentUI().setCurrentState(StafTransRdsStateReg.SUBLIST_EDITING);

		if (CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)
				|| CommonValue.HI_PSNDOC_PART.equals(strTabCode)) {
			ctlCompIsEnable();
			setDeptPower(false);
		}

		// �κ�ʱ��ְ��ز��ܱ༭
		getMainPnl().setCellEditable(strTabCode, "pk_detytype", selrow, false);

		if (CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)
				|| CommonValue.HI_PSNDOC_PART.equals(strTabCode)) {
			//�޸���ְ����ְ��¼�����²���
			ListRefUtils.refreshCorpInfoRef(selvalue, strTabCode, getDataMdl().getPk_corp(), getCurBillModel());
		}
		//������Ա������
		if (CommonValue.HI_PSNDOC_DEPTCHG.equals(strTabCode)) {			
			refreshPsnclRef();
		}
		afterExecute();
	}
	private void afterExecute(){
		getDataMdl().setEditType(StafTransRdsDataModel.UPDATE);
	}
	/**
	 * ������ְ�Ӽ���Ϣ����
	 */
	protected void refreshPsnclRef() {
		String refitemkey ="pk_psncl" + BillPanelUtils.REF_SHOW_NAME;
		UIRefPane refPane = (UIRefPane) getMainPnl().getBillListPanel()
				.getBodyBillModel(strTabCode).getItemByKey(refitemkey)
				.getComponent();
		// �Ƿ�Ϊ��Ƹ��Ա
		Object blobj = getMainPnl().getHeadSelectedData().getAttributeValue("isreturn");

		boolean bl = (blobj == null || blobj.toString().length() < 1) ? false
				: UFBoolean.valueOf(blobj.toString()).booleanValue();
		// ������ΧΪ��Ƹ�����ˣ���Ϊ��Ƹ��Ա
		bl = bl|| (getDataMdl().getPsnclscope() == nc.vo.hi.pub.CommonValue.PSNCLSCOPE_DISMISS
				|| getDataMdl().getPsnclscope() == nc.vo.hi.pub.CommonValue.PSNCLSCOPE_RETIRE);
		if (bl) {
			refPane.setWhereString("( bd_psncl.psnclscope = "
					+ nc.vo.hi.pub.CommonValue.PSNCLSCOPE_WORK
					+ ") and (bd_psncl.pk_corp='" + getDataMdl().getPk_corp()
					+ "' or bd_psncl.pk_corp='"
					+ nc.vo.hi.pub.CommonValue.GROUPCODE + "')");
		} else {
			refPane.setWhereString("( bd_psncl.psnclscope = "
					+ nc.vo.hi.pub.CommonValue.PSNCLSCOPE_WORK
					+ " or  bd_psncl.psnclscope = "
					+ nc.vo.hi.pub.CommonValue.PSNCLSCOPE_OTHER
					+ ") and (bd_psncl.pk_corp='" + getDataMdl().getPk_corp()
					+ "' or bd_psncl.pk_corp='"
					+ nc.vo.hi.pub.CommonValue.GROUPCODE + "')");
		}
		refPane.getRefModel().reloadData();
	}
	/**
	 * �赱ǰ�����в���PK=NULL�������������Ӧֵ����Ϊ����
	 */
	private void resetRefPK() {
		BillItem[] bodyItems = getMainPnl().getBodyBillModel(strTabCode).getBodyItems();
		for (int i = 0; i < bodyItems.length; i++) {
			if (bodyItems[i].getDataType() == BillItem.UFREF) {
				((UIRefPane) bodyItems[i].getComponent()).setPK(null);
				String key = "&" + bodyItems[i].getKey();
				if (key != null) {
					Object pk = getMainPnl().getBodyBillModel(strTabCode).getValueAt(
							selrow, key);
					((UIRefPane) bodyItems[i].getComponent()).setPK(pk);
				}
			}
		}
	}
	/**
	 * �������ϵ���ֵ��֯���ṹ�С� �������ڣ�(2003-3-27 20:21:10)
	 *
	 * @return nc.vo.pub.ValueObject[]
	 */
	public void setDeptPower(boolean useDeptPower) {
		//0726 �޸�
		String itemkey = "pk_deptdoc"+BillPanelUtils.REF_SHOW_NAME;
		if (getMainPnl().getBodyBillModel(strTabCode).getItemByKey(itemkey) != null) {
			((UIRefPane) getMainPnl().getBodyBillModel(strTabCode).getItemByKey(itemkey)
					.getComponent()).getRefModel()
					.setUseDataPower(useDeptPower);
		}
	}
	/**
	 * ��������Ŀؼ��Ƿ��ñ༭�� �������ڣ�(2002-3-30 12:58:53)
	 * @param isEnable
	 *            boolean
	 */
	public void ctlCompIsEnable() {
		BillModel myMode = getMainPnl().getBodyBillModel(strTabCode);
		BillItem[] bodyItems = (BillItem[]) myMode.getBodyItems();

		String job = null;
		String duty = null;

		if (myMode.getBodyColByKey("pk_postdoc") != -1) {
			Object objJob = myMode.getValueAt(selrow,
					"pk_postdoc");
			if (objJob != null) {
				job = objJob.toString();
			}
		}
		if (myMode.getBodyColByKey("pk_om_duty") != -1) {
			Object objDuty = myMode.getValueAt(selrow,
					"pk_om_duty");
			if (objDuty != null) {
				duty = objDuty.toString();
			}
		}
		for (int i = 0; i < bodyItems.length; i++) {
			if (job != null) {
				if (bodyItems[i].getKey().equals("pk_postdoc")) {
					myMode.setCellEditable(selrow, "pk_postdoc", true);
				} else if (bodyItems[i].getKey().equals("pk_jobserial")) {
					myMode.setCellEditable(selrow, "pk_jobserial", false);
				} else if (bodyItems[i].getKey().equals("pk_jobrank")) {
					myMode.setCellEditable(selrow, "pk_jobrank", false);
				} else if (bodyItems[i].getKey().equals("pk_om_duty")) {
					myMode.setCellEditable(selrow, "pk_om_duty", true);
				}
			} else {
				if (bodyItems[i].getKey().equals("pk_postdoc")) {
					myMode.setCellEditable(selrow, "pk_postdoc", true);
				} else if (bodyItems[i].getKey().equals("pk_jobserial")) {
					myMode.setCellEditable(selrow, "pk_jobserial", true);
				} else if (bodyItems[i].getKey().equals("pk_jobrank")) {
					myMode.setCellEditable(selrow, "pk_jobrank", true);
				} else if (bodyItems[i].getKey().equals("pk_om_duty")) {
					myMode.setCellEditable(selrow, "pk_om_duty", true);
				}

			}

			if (duty != null) {
				if (bodyItems[i].getKey().equals("pk_detytype")) {
					myMode.setCellEditable(selrow, "pk_detytype", false);
				}
			} else {
				if (bodyItems[i].getKey().equals("pk_detytype")) {
					myMode.setCellEditable(selrow, "pk_detytype", true);
				}
			}

		}
	}
	@Override
	public boolean validate() throws ValidationException {

		boolean result = super.validate();
		if(!result){
			return result;
		}
		selrow = getMainPnl().getBodySelectedRow(strTabCode);

		if (selrow == -1) {
			throw new ValidationException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("60090713","UPP60090713-000211")/*@res "��ѡ���У�Ȼ���ٵ��޸ģ�"*/);
		}

		selvalue = getMainPnl().getBodySelectedDatas(strTabCode)[0];

		return result;
	}

}