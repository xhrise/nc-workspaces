package nc.ui.trade.bill;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.internal.compiler.ast.IfStatement;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.bsdelegate.DefaultBusinessSplit;
import nc.ui.trade.bsdelegate.IBusinessSplit;
import nc.ui.trade.buffer.RecordNotFoundExcetption;
import nc.ui.trade.businessaction.BdBusinessAction;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.handler.EventHandler;
import nc.ui.trade.pub.BillDirectPrint;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.trade.pub.ReportTreeTableModelAdapter;
import nc.ui.trade.query.INormalQuery;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.IPFConfigInfo;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;

/**
 * ƽ̨���ݵİ�ť����ί������ �������ڣ�(2004-1-9 10:33:09)
 * 
 * @author�����ھ�
 */
public abstract class BillEventHandler extends EventHandler {

	private static final String staticACTION = "BOACTION";

	private static final String staticASS = "BOASS";

	/**
	 * BillButtonController ������ע�⡣
	 * 
	 * @param billUI
	 *            nc.ui.trade.pub.AbstractBillUI
	 * @param control
	 *            nc.ui.trade.controller.IControllerBase
	 */
	public BillEventHandler(AbstractBillUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	/**
	 * ҵ���ִ�ж����� �������ڣ�(2004-1-8 12:55:35)
	 * 
	 * nc.ui.pub.ButtonObject
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void afterOnBoAction(int intBtn, AggregatedValueObject billVo)
			throws java.lang.Exception {
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-8 12:55:35)
	 * 
	 * @param bo
	 *            nc.ui.pub.ButtonObject
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void afterOnBoAss(ButtonObject bo) throws java.lang.Exception {
	}

	/**
	 * ҵ���ִ�ж���ǰ�� �������ڣ�(2004-1-8 12:55:35)
	 * 
	 * nc.ui.pub.ButtonObject
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void beforeOnBoAction(int intBtn, AggregatedValueObject billVo)
			throws java.lang.Exception {
		if (billVo instanceof HYBillVO)
			((HYBillVO) billVo).setM_billField(getBillField());
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-8 12:55:35)
	 * 
	 * @param bo
	 *            nc.ui.pub.ButtonObject
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void beforeOnBoAss(ButtonObject bo) throws java.lang.Exception {
	}

	/**
	 * ҵ�����Ͷ���ִ��ǰ���¼����� �������Ըö���ִ��ǰ���п��ƣ���������ظ÷��� �������ڣ�(2004-1-6 21:01:32)
	 * 
	 * @param billUI
	 *            nc.ui.trade.pub.AbstractBillUI int
	 */
	protected void busiTypeBefore(AbstractBillUI billUI, ButtonObject bo)
			throws Exception {
	}

	/**
	 * ����ӱ������� �������ڣ�(2004-2-25 19:59:34)
	 * 
	 * @param vos
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	private void clearChildPk(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		if (vos == null || vos.length == 0)
			return;
		for (int i = 0; i < vos.length; i++) {
			vos[i].setPrimaryKey(null);
		}
	}

	/**
	 * ���ఴť���¼����� �������ڣ�(2004-2-25 21:04:27)
	 * 
	 * @param intBtn
	 *            int
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void complexOnButton(int intBtn, ButtonObject bo)
			throws java.lang.Exception {
		switch (intBtn) {
		case IBillButton.Busitype: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000060")/*
													 * @res "��ʼѡ��ҵ�����ͣ���ȴ�......"
													 */);
			onBoBusiType(bo);
			break;
		}
		case IBillButton.Add: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000061")/*
													 * @res "��ʼ�������ӵ��ݣ���ȴ�......"
													 */);
			onBoBusiTypeAdd(bo, null);
			break;
		}
		case IBillButton.Action: {
			onBoAction(bo);
			break;
		}
		case IBillButton.Ass: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000062")/*
													 * @res
													 * "��ʼ�Ե��ݵĸ������в�������ȴ�......"
													 */);
			onBoAss(bo);
			break;
		}
		case IBillButton.Line: {
			onBoLine(bo);
			break;
		}
		case IBillButton.File: {
			onBoFile(bo);
		}
		case IBillButton.Brow: {
			onBoBrow(bo);
			break;
		}
		case IBillButton.NodeKey: {
			onBoNodekey(bo);
			break;
		}
		case IBillButton.Refbill: {
			ButtonVO btnVo = (ButtonVO) bo.getData();
			onBoElse(btnVo.getBtnNo());
			break;
		}
		default: {
			if(bo.getData() != null && bo.getData() instanceof ButtonVO) {
				ButtonVO btnVo = (ButtonVO) bo.getData();
				onBoElse(btnVo.getBtnNo());
			}
			break;
		}
		}
	}

	/**
	 * ���е��ݶ����Ĵ���, ������е��ݶ���������Ҫ���ظ÷��� �������ڣ�(2004-1-3 18:13:36)
	 */
	protected IBusinessController createBusinessAction() {
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new BdBusinessAction(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}

	}

	/**
	 * ��ÿ�Ƭģ��İ�װ�ࡣ �������ڣ�(2004-1-6 22:29:36)
	 * 
	 * @return nc.ui.pub.bill.BillCardPanel
	 */
	protected abstract BillCardPanelWrapper getBillCardPanelWrapper();

	/**
	 * ��ó����ֶΡ� �������ڣ�(2004-1-7 8:44:06)
	 * 
	 * @return nc.ui.trade.buffer.BillUIBuffer
	 */
	protected final BusinessDelegator getBusiDelegator() {
		return getBillUI().getBusiDelegator();
	}

	/**
	 * ��ʼ��������ƽ̨ע���ִ�ж�����ť �������ڣ�(2002-12-23 10:11:27)
	 */
	public final void initActionButton() {
		try {
			ButtonObject boAction = getButtonManager().getButton(
					IBillButton.Action);
			// ҵ������
			if (getBillUI().isBusinessType().booleanValue()) {
				ButtonObject boBusitype = getButtonManager().getButton(
						IBillButton.Busitype);
				ButtonObject boAdd = getButtonManager().getButton(
						IBillButton.Add);
				
				ButtonObject boSelectBusitype = null;
				
				if(boBusitype != null) {
					getBusiDelegator().retBusinessBtn(boBusitype,
							_getCorp().getPrimaryKey(),
							getUIController().getBillType());
					if (boBusitype.getChildButtonGroup() != null
							&& boBusitype.getChildButtonGroup().length > 0) {

						boBusitype.getChildButtonGroup()[0].setSelected(true);
						boBusitype.setCheckboxGroup(true);

						ButtonObject bo = boBusitype.getChildButtonGroup()[0];
						// ����ҵ������
						BusitypeVO vo = (BusitypeVO) bo.getData();
						// ҵ����������
						getBillUI().setBusinessType(vo.getPrimaryKey());
						// ҵ�����ʹ���
						getBillUI().setBusicode(vo.getBusicode());

						boSelectBusitype = boBusitype.getChildButtonGroup()[0];
						
						// ����ִ�а�ť(�뵥��״̬�޹أ�
						if (boAction != null)
							getBusiDelegator().retElseBtn(boAction,
									getUIController().getBillType(), staticACTION);
						initAssButton(boSelectBusitype);
					}
				}
				// ����
				if (boAdd != null)
					getBusiDelegator().retAddBtn(boAdd,
							_getCorp().getPrimaryKey(),
							getUIController().getBillType(),
							boSelectBusitype);

//				if (boBusitype != null && boBusitype.getChildButtonGroup() != null
//						&& boBusitype.getChildButtonGroup().length > 0) {
//
//					boBusitype.getChildButtonGroup()[0].setSelected(true);
//					boBusitype.setCheckboxGroup(true);
//
//					// ����
//					if (boAdd != null)
//						getBusiDelegator().retAddBtn(boAdd,
//								_getCorp().getPrimaryKey(),
//								getUIController().getBillType(),
//								boBusitype.getChildButtonGroup()[0]);
//
//					ButtonObject bo = boBusitype.getChildButtonGroup()[0];
//					// ����ҵ������
//					BusitypeVO vo = (BusitypeVO) bo.getData();
//					// ҵ����������
//					getBillUI().setBusinessType(vo.getPrimaryKey());
//					// ҵ�����ʹ���
//					getBillUI().setBusicode(vo.getBusicode());
//
//					// ����ִ�а�ť(�뵥��״̬�޹أ�
//					if (boAction != null)
//						getBusiDelegator().retElseBtn(boAction,
//								getUIController().getBillType(), staticACTION);
//					initAssButton(boBusitype.getChildButtonGroup()[0]);
//				} else
//					System.out.println("û�г�ʼ��ҵ������!");
			} else {
				// ����ִ�а�ť(�뵥��״̬�޹أ�
				if (boAction != null)
					getBusiDelegator().retElseBtn(boAction,
							getUIController().getBillType(), staticACTION);
			}
			// ����ִ�а�ť
			if (boAction != null) {
				getButtonManager().setActionButtonVO(
						getBillUI().isSaveAndCommitTogether());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000063")/* @res "��ʼ��ҵ��ִ�а�ť����" */);
		}
	}

	/**
	 * ��ʼ��������ƽ̨��ע��ĵ��ݶ���
	 */
	private void initAssButton(ButtonObject bo) throws Exception {
		ButtonObject boAss = getButtonManager().getButton(IBillButton.Ass);
		if (boAss == null)
			return;
		getBusiDelegator().retElseBtn(boAss, getUIController().getBillType(),
				staticASS);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-7 10:53:11)
	 * 
	 * @return boolean
	 */
	public boolean isAdding() {
		return getBillUI().getBillOperate() == IBillOperate.OP_ADD
				|| getBillUI().getBillOperate() == IBillOperate.OP_REFADD;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-7 10:53:11)
	 * 
	 * @return boolean
	 */
	public boolean isEditing() {
		return getBillUI().getBillOperate() == IBillOperate.OP_EDIT;
	}

	/**
	 * ���յ��ݽ����Ƶ��� �������ڣ�(2003-6-12 10:01:28)
	 */
	public void onBillRef() throws Exception {
		ButtonObject btn = getButtonManager().getButton(IBillButton.Refbill);
		btn.setTag(getBillUI().getRefBillType() + ":");
		onBoBusiTypeAdd(btn, null);
	}

	/**
	 * ��ťm_boAction���ʱִ�еĶ���,���б�Ҫ���븲��. ����ִ�ж�������
	 */
	private final void onBoAction(ButtonObject bo) throws Exception {
		// getBillUI().showHintMessage(
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
		// "UPPuifactory-000179")/*
		// * @res "��ʼִ�в���,��ȴ�..."
		// */);
		ButtonVO btnVo = (ButtonVO) bo.getData();
		if (btnVo == null)
			return;
		switch (btnVo.getBtnNo()) {
		case IBillButton.Audit: {
			onBoAudit();
			break;
		}
		case IBillButton.CancelAudit: {
			onBoCancelAudit();
			break;
		}
		case IBillButton.Commit: {
			onBoCommit();
			break;
		}
		case IBillButton.Del: {
			onBoDel();
			break;
		}
		default: {
			onBoActionElse(bo);
			break;
		}
		}

		// getBillUI().showHintMessage(
		// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
		// "UPPuifactory-000180")/*
		// * @res "ִ�����."
		// */
		// );
	}

	/**
	 * Action��ť���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	public void onBoActionElse(ButtonObject bo) throws Exception {
		// �������
		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		int intBtn = 0;
		if (bo.getData() != null)
			intBtn = ((ButtonVO) bo.getData()).getBtnNo();
		beforeOnBoAction(intBtn, modelVo);
		// *******************
		Object retObj = getBusinessAction().processAction(bo.getTag(), modelVo,
				getUIController().getBillType(),
				getBillUI()._getDate().toString(), getBillUI().getUserObject());

		if (PfUtilClient.isSuccess()) {
			if (retObj instanceof AggregatedValueObject) {
				AggregatedValueObject retVo = (AggregatedValueObject) retObj;
				afterOnBoAction(intBtn, retVo);
				CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
				if (childVos == null)
					modelVo.setParentVO(retVo.getParentVO());
				else
					modelVo = retVo;
			}
			// �����б�
			getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		}
	}

	/**
	 * �������ӵĴ��� �������ڣ�(2002-12-23 12:43:15)
	 */
	public void onBoAdd(ButtonObject bo) throws Exception {
		// ����Ϊ��������
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
	}

	/**
	 * ��ťm_boAction���ʱִ�еĶ���,���б�Ҫ���븲��. ����ִ�ж�������
	 */
	private final void onBoAss(ButtonObject bo) throws Exception {
		beforeOnBoAss(bo);
		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		Object ret = getBusinessAction().processAction(bo.getTag(), modelVo,
				getUIController().getBillType(),
				getBillUI()._getDate().toString(), getBillUI().getUserObject());
		if (ret != null && ret instanceof AggregatedValueObject) {
			AggregatedValueObject vo = (AggregatedValueObject) ret;
			// ����״̬
			modelVo.getParentVO().setAttributeValue(
					getBillField().getField_BillStatus(),
					vo.getParentVO().getAttributeValue(
							getBillField().getField_BillStatus()));
			// ����ʱ���
			modelVo.getParentVO().setAttributeValue("ts",
					vo.getParentVO().getAttributeValue("ts"));

			getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		}
		afterOnBoAss(bo);
	}

	/**
	 * ��ťm_boAssign���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoAssign() throws Exception {
	}

	/**
	 * ��ťm_boAudit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	public void onBoAudit() throws Exception {
		// �������
		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		setCheckManAndDate(modelVo);
		// ���״̬һ�����˳�
		if (checkVOStatus(modelVo, new int[] { IBillStatus.CHECKPASS })) {
			System.out.println("��Ч����괦�����");
			return;
		}
		beforeOnBoAction(IBillButton.Audit, modelVo);
		// *******************
		AggregatedValueObject retVo = (AggregatedValueObject) getBusinessAction()
				.approve(modelVo, getUIController().getBillType(),
						getBillUI()._getDate().toString(),
						getBillUI().getUserObject());

		if (PfUtilClient.isSuccess()) {

			afterOnBoAction(IBillButton.Audit, retVo);
			CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
			if (childVos == null)
				modelVo.setParentVO(retVo.getParentVO());
			else
				modelVo = retVo;
			// �����б�
			getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		}

	}

	/**
	 * ���ڵ��ӱ�Ľ��洦���������ش˷���������ݣ�ͬʱ���л������� ���� getBufferData().clear();
	 * getBufferData().adddVOToBuffer(aVo); //����б�setListHeadData(queryVos);
	 * //���õ��ݲ���״̬ getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	 */
	protected void onBoBodyQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForBodyQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		doBodyQuery(strWhere.toString());

	}

	/**
	 * @param strWhere
	 * @throws Exception
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected void doBodyQuery(String strWhere) throws Exception,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		SuperVO[] queryVos = getBusiDelegator().queryByCondition(
				Class.forName(getUIController().getBillVoName()[2]),
				strWhere == null ? "" : strWhere);

		getBufferData().clear();

		AggregatedValueObject vo = (AggregatedValueObject) Class.forName(
				getUIController().getBillVoName()[0]).newInstance();
		vo.setChildrenVO(queryVos);
		getBufferData().addVOToBuffer(vo);

		updateBuffer();
	}

	/**
	 * @param strWhere
	 * @param b
	 * @return
	 */
	protected boolean askForBodyQueryCondition(StringBuffer sqlWhereBuf)
			throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null)
			strWhere = "1=1";

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		if (getUIController().getBodyCondition() != null)
			strWhere = strWhere + " and "
					+ getUIController().getBodyCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	}

	/**
	 * ����������� �������ڣ�(2004-1-7 8:57:02)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void onBoBrow(ButtonObject bo) throws java.lang.Exception {
		int intBtn = Integer.parseInt(bo.getTag());
		// ����ִ��ǰ����
		buttonActionBefore(getBillUI(), intBtn);
		switch (intBtn) {
		case IBillButton.First: {
			getBufferData().first();
			break;
		}
		case IBillButton.Prev: {
			getBufferData().prev();
			break;
		}
		case IBillButton.Next: {
			getBufferData().next();
			break;
		}
		case IBillButton.Last: {
			getBufferData().last();
			break;
		}
		}
		// ����ִ�к���
		buttonActionAfter(getBillUI(), intBtn);
		getBillUI().showHintMessage(
				nc.ui.ml.NCLangRes.getInstance()
						.getStrByID(
								"uifactory",
								"UPPuifactory-000503",
								null,
								new String[] { nc.vo.format.Format
										.indexFormat(getBufferData()
												.getCurrentRow() + 1) })/*
																		 * @res
																		 * "ת����:" +
																		 * getBufferData().getCurrentRow() +
																		 * "ҳ���)"
																		 */
		);

	}

	/**
	 * ��ťm_boBusiType���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	private final void onBoBusiType(ButtonObject bo) throws Exception {
		// ִ��ǰ����
		busiTypeBefore(getBillUI(), bo);
		bo.setSelected(true);
		// ����ҵ������
		BusitypeVO vo = (BusitypeVO) bo.getData();
		// �������Ӱ�ť
		getBusiDelegator()
				.retAddBtn(getButtonManager().getButton(IBillButton.Add),
						_getCorp().getPrimaryKey(),
						getUIController().getBillType(), bo);
		// //����ִ�а�ť(�뵥��״̬�޹أ�
		getBusiDelegator().retElseBtn(
				getButtonManager().getButton(IBillButton.Action),
				getUIController().getBillType(), staticACTION);

		getButtonManager().setActionButtonVO(
				getBillUI().isSaveAndCommitTogether());

		String oldtype = getBillUI().getBusinessType();
		String newtype = vo.getPrimaryKey();
		String oldcode = getBillUI().getBusicode();
		String newcode = vo.getBusicode();

		// ҵ����������
		getBillUI().setBusinessType(newtype);
		// ҵ�����ʹ���
		getBillUI().setBusicode(newcode);

		// ����ˢ��UI
		getBillUI().initUI();
		// ���UI����
		getBillUI().getBufferData().clear();
		getBillUI().getBufferData().setCurrentRow(-1);

		getBillUI().updateButtonUI();

		if (m_bbl != null) {
			BusiTypeChangeEvent e = new BusiTypeChangeEvent(this, oldtype,
					newtype, oldcode, newcode);
			m_bbl.busiTypeChange(e);
		}
	}

	/**
	 * ��ťm_boAdd���ʱִ�еĶ���,���б�Ҫ���븲��.
	 * 
	 * @param bo
	 *            ��Դ���ݵ�ƽ̨���ɰ�ť
	 * @param sourceBillId
	 *            �ο���Դ����Id
	 */
	public final void onBoRefAdd(String strRefBillType, String sourceBillId)
			throws Exception {

		onBoBusiTypeAdd(getBusiDelegator().getRefButton(
				getButtonManager().getButton(IBillButton.Add), strRefBillType),
				sourceBillId);
	}

	/**
	 * ��ťm_boAdd���ʱִ�еĶ���,���б�Ҫ���븲��.
	 * 
	 * @param bo
	 *            ��Դ���ݵ�ƽ̨���ɰ�ť
	 * @param sourceBillId
	 *            �ο���Դ����Id
	 */
	private final void onBoBusiTypeAdd(ButtonObject bo, String sourceBillId)
			throws Exception {
		getBusiDelegator().childButtonClicked(bo, _getCorp().getPrimaryKey(),
				getBillUI()._getModuleCode(), _getOperator(),
				getUIController().getBillType(), getBillUI(),
				getBillUI().getUserObject(), sourceBillId);
		if (nc.ui.pub.pf.PfUtilClient.makeFlag) {
			// ���õ���״̬
			getBillUI().setCardUIState();
			// ����
			getBillUI().setBillOperate(IBillOperate.OP_ADD);
		} else {
			if (PfUtilClient.isCloseOK()) {
				if (m_bbl != null) {
					String tmpString = bo.getTag();
					int findIndex = tmpString.indexOf(":");
					String newtype = tmpString.substring(0, findIndex);
					RefBillTypeChangeEvent e = new RefBillTypeChangeEvent(this,
							null, newtype);
					m_bbl.refBillTypeChange(e);
				}
				if (isDataChange())
					setRefData(PfUtilClient.getRetVos());
				else
					setRefData(PfUtilClient.getRetOldVos());
			}
		}
	}

	/**
	 * ��ťm_boCancel���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCancel() throws Exception {

		if (getBufferData().isVOBufferEmpty()) {
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		} else {
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			
            if(getBufferData().getCurrentRow()==-1){
               getBufferData().setCurrentRow(0);
            }else{
               getBufferData().setCurrentRow(getBufferData().getCurrentRow());
            }			
			
		}

	}

	/**
	 * ��ťm_boCancelAudit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCancelAudit() throws Exception {
		// �������
		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		// ���뷴�������ڡ�������
		setCheckManAndDate(modelVo);
		// ���״̬һ�����˳�
		if (checkVOStatus(modelVo, new int[] { IBillStatus.FREE })) {
			System.out.println("��Ч����괦�����");
			return;
		}
		beforeOnBoAction(IBillButton.CancelAudit, modelVo);
		// *******************
		AggregatedValueObject retVo = (AggregatedValueObject) getBusinessAction()
				.unapprove(modelVo, getUIController().getBillType(),
						getBillUI()._getDate().toString(),
						getBillUI().getUserObject());

		if (PfUtilClient.isSuccess()) {
			afterOnBoAction(IBillButton.CancelAudit, retVo);
			CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
			if (childVos == null)
				modelVo.setParentVO(retVo.getParentVO());
			else
				modelVo = retVo;

			Integer intState = (Integer) modelVo.getParentVO()
					.getAttributeValue(getBillField().getField_BillStatus());
			if (intState.intValue() == IBillStatus.FREE) {
				modelVo.getParentVO().setAttributeValue(
						getBillField().getField_CheckMan(), null);
				modelVo.getParentVO().setAttributeValue(
						getBillField().getField_CheckDate(), null);
			}
			// �����б�����
			getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		}
	}

	/**
	 * ��ťm_boCard���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCard() throws Exception {

		// setPageButtonState();
	}

	/**
	 * ��ťm_boCommit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCommit() throws Exception {
		// �������
		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		// ��ʱ�������Ƶ���Ϊ��ǰ�����ˣ�
		modelVo.getParentVO().setAttributeValue(
				getBillField().getField_Operator(), getBillUI()._getOperator());
		beforeOnBoAction(IBillButton.Commit, modelVo);
		String strTime = ClientEnvironment.getServerTime()
				.toString();
		ArrayList retList = getBusinessAction().commit(modelVo,
				getUIController().getBillType(),
				getBillUI()._getDate().toString() + strTime.substring(10),
				getBillUI().getUserObject());

		if (PfUtilClient.isSuccess()) {
			Object o = retList.get(1);
			if (o instanceof AggregatedValueObject) {
				AggregatedValueObject retVo = (AggregatedValueObject) o;
				afterOnBoAction(IBillButton.Commit, retVo);
				CircularlyAccessibleValueObject[] childVos = getChildVO(retVo);
				if (childVos == null)
					modelVo.setParentVO(retVo.getParentVO());
				else
					modelVo = retVo;
			}
			getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		}
	}

	/**
	 * ��ťm_boCopy���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoCopy() throws Exception {
		// �������
		AggregatedValueObject copyVo = getBufferData().getCurrentVOClone();
		// ����������մ���
		copyVo.getParentVO().setPrimaryKey(null);
		if (copyVo instanceof IExAggVO) {
			clearChildPk(((IExAggVO) copyVo).getAllChildrenVO());
		} else {
			clearChildPk(copyVo.getChildrenVO());
		}
		// ����Ϊ��������
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
		// ���õ��ݺ�
		String noField = getBillUI().getBillField().getField_BillNo();
		BillItem noitem = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem(noField);
		if (noitem != null)
			copyVo.getParentVO().setAttributeValue(noField,
					noitem.getValueObject());
		// ���ý�������
		getBillUI().setCardUIData(copyVo);
	}

	/**
	 * ��ťm_boDel���ʱִ�еĶ���,���б�Ҫ���븲��. ���ݵ����ϴ���
	 */
	protected void onBoDel() throws Exception {
		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		// �����ֶγ���
		if (modelVo instanceof HYBillVO)
			((HYBillVO) modelVo).setM_billField(getBillField());

		AggregatedValueObject delVo = (AggregatedValueObject) getBusinessAction()
				.delete(modelVo, getUIController().getBillType(),
						getBillUI()._getDate().toString(),
						getBillUI().getUserObject());
		if (PfUtilClient.isSuccess()) {
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			// ����״̬
			modelVo.getParentVO().setAttributeValue(
					getBillField().getField_BillStatus(),
					new Integer(IBillStatus.DELETE));
			modelVo.getParentVO().setAttributeValue("ts",
					delVo.getParentVO().getAttributeValue("ts"));
			getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		}

	}

	/**
	 * ��ťm_boDel���ʱִ�еĶ���,���б�Ҫ���븲��. ������ɾ������
	 */
	protected void onBoDelete() throws Exception {
		// ����û�����ݻ��������ݵ���û��ѡ���κ���
		if (getBufferData().getCurrentVO() == null)
			return;

		if (MessageDialog.showOkCancelDlg(getBillUI(),
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000064")/* @res "����ɾ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000065")/* @res "�Ƿ�ȷ��ɾ���û�������?" */
				, MessageDialog.ID_CANCEL) != UIDialog.ID_OK)
			return;

		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		getBusinessAction().delete(modelVo, getUIController().getBillType(),
				getBillUI()._getDate().toString(), getBillUI().getUserObject());
		if (PfUtilClient.isSuccess()) {
			// �����������
			getBillUI().removeListHeadData(getBufferData().getCurrentRow());
			if (getUIController() instanceof ISingleController) {
				ISingleController sctl = (ISingleController) getUIController();
				if (!sctl.isSingleDetail())
					getBufferData().removeCurrentRow();
			} else {
				getBufferData().removeCurrentRow();
			}

		}
		if (getBufferData().getVOBufferSize() == 0)
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		else
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	}

	/**
	 * ��ťm_boEdit���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoEdit() throws Exception {
		// ����û�����ݻ��������ݵ���û��ѡ���κ���
		if (getBufferData().getCurrentVO() == null)
			return;
		String strTime = getBillUI()._getServerTime().toString();
		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		Object o = getBusinessAction()
				.edit(
						modelVo,
						getUIController().getBillType(),
						getBillUI()._getDate().toString()
								+ strTime.substring(10), null);

		if (o instanceof AggregatedValueObject) {
			AggregatedValueObject retVo = (AggregatedValueObject) o;
			if (retVo.getChildrenVO() == null)
				modelVo.setParentVO(retVo.getParentVO());
			else
				modelVo = retVo;
			getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		}

		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
	}

	/**
	 * ������ť�������¼���������(�����)
	 */
	protected void onBoElse(int intBtn) throws Exception {
	}

	/**
	 * �в������� �������ڣ�(2004-1-7 8:57:02)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void onBoLine(ButtonObject bo) throws java.lang.Exception {
		int intBtn = -1 ;//Integer.parseInt(bo.getTag());

		if(bo.getData() != null && bo.getData() instanceof ButtonVO) {
			ButtonVO btnVo = (ButtonVO) bo.getData();
			intBtn = btnVo.getBtnNo();
		}else {
			intBtn = Integer.parseInt(bo.getTag());
		}
		
		// ����ִ��ǰ����
		buttonActionBefore(getBillUI(), intBtn);

		getBillUI().showHintMessage(bo.getName());
		
		switch (intBtn) {
		case IBillButton.AddLine: {
			onBoLineAdd();
			break;
		}
		case IBillButton.DelLine: {
			onBoLineDel();
			break;
		}
		case IBillButton.CopyLine: {
			onBoLineCopy();
			break;
		}
		case IBillButton.InsLine: {
			onBoLineIns();
			break;
		}
		case IBillButton.PasteLine: {
			onBoLinePaste();
			break;
		}
		case IBillButton.PasteLinetoTail: {
			onBoLinePasteToTail();
			break;
		}
		default:
			onBoElse(intBtn);
			break;
		}

		// ����ִ�к���
		buttonActionAfter(getBillUI(), intBtn);
	}

	/**
	 * ��ťm_boLineAdd���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLineAdd() throws Exception {
		// ������֮ǰ���õĳ��󷽷�
		getBillCardPanelWrapper().addLine();
		// int rows =
		// getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
		// getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectionModel().setSelectionInterval(rows-1,rows-1);
		postProcessOfAddNewLine();
	}

	/**
	 * 
	 */
	protected void postProcessOfAddNewLine() {
		try {
			// CircularlyAccessibleValueObject addedVO = null;
			// try
			// {
			// addedVO = (CircularlyAccessibleValueObject)
			// Class.forName(getUIController().getBillVoName()[2]).newInstance();
			// }
			// catch (Exception e)
			// {
			// e.printStackTrace();
			// throw new RuntimeException("creating bodyvo instance fails");
			//
			// }
			CircularlyAccessibleValueObject vo = processNewBodyVO(getBillCardPanelWrapper()
					.getSelectedBodyVOs()[0]);
			// CircularlyAccessibleValueObject vo = processNewBodyVO(addedVO);
			int row = getBillCardPanelWrapper().getBillCardPanel()
					.getBillTable().getSelectedRow();
			if (row == -1)//
				row = getBillCardPanelWrapper().getBillCardPanel()
						.getBillModel().getRowCount() - 1;
			if (row < 0)
				throw new RuntimeException("cann't get selected row");
			if (vo != null)
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.setBodyRowVO(vo, row);
		} catch (NullPointerException e) {
			System.out.println("�������л�ɾ�к�û�л�ȡ����ѡ���VO");
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("�������л�ɾ�к�û�л�ȡ����ѡ���VO");
			e.printStackTrace();
		}
	}

	/**
	 * ��ťm_boLineCopy���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLineCopy() throws Exception {
		getBillCardPanelWrapper().copySelectedLines();
	}

	/**
	 * ��ťm_boLineDel���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLineDel() throws Exception {
		getBillCardPanelWrapper().deleteSelectedLines();

	}

	/**
	 * ��ťm_boLineIns���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLineIns() throws Exception {
		getBillCardPanelWrapper().insertLine();
		if (getBillCardPanelWrapper().getBillCardPanel().getRowCount() > 0)
			postProcessOfAddNewLine();

	}

	/**
	 * �ķ��������кͲ��к󱻵��ã������ڸ÷�����Ϊ������������һЩĬ��ֵ
	 * 
	 * @param newBodyVO
	 * @return TODO
	 */
	protected CircularlyAccessibleValueObject processNewBodyVO(
			CircularlyAccessibleValueObject newBodyVO) {
		return newBodyVO;

	}

	/**
	 * ��ťm_boLinePaste���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoLinePaste() throws Exception {
		// ճ��ǰ�Ը��Ƶ�VO��Щҵ����صĴ���
		processCopyedBodyVOsBeforePaste(getBillCardPanelWrapper()
				.getCopyedBodyVOs());
		getBillCardPanelWrapper().pasteLines();

	}

	/**
	 * ճ�������е���β
	 */
	protected void onBoLinePasteToTail() throws Exception {
		// ճ��ǰ�Ը��Ƶ�VO��Щҵ����صĴ���
		processCopyedBodyVOsBeforePaste(getBillCardPanelWrapper()
				.getCopyedBodyVOs());
		getBillCardPanelWrapper().pasteLinesToTail();

	}

	/**
	 * ��ťm_boPrint���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoPrint() throws Exception {

		nc.ui.pub.print.IDataSource dataSource = new CardPanelPRTS(getBillUI()
				._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel());
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
				dataSource);
		print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
				._getModuleCode(), getBillUI()._getOperator(), getBillUI()
				.getBusinessType(), getBillUI().getNodeKey());
		if (print.selectTemplate() == 1)
			print.preview();

	}

	/**
	 * ��ťm_boDirectPrint���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoDirectPrint() throws Exception {

		BillModel billmodel = getBillCardPanelWrapper().getBillCardPanel()
				.getBillModel();
		if (billmodel == null)
			return;
		if (billmodel instanceof ReportTreeTableModelAdapter) {
			((ReportTreeTableModelAdapter) billmodel).setPrinting(true);
		}

		BillDirectPrint print = new BillDirectPrint(getBillCardPanelWrapper()
				.getBillCardPanel(), getBillCardPanelWrapper()
				.getBillCardPanel().getTitle());
		print.onPrint();

		if (billmodel instanceof ReportTreeTableModelAdapter) {
			((ReportTreeTableModelAdapter) billmodel).setPrinting(false);
		}
	}

	/**
	 * ���ڵ��ӱ�Ľ��洦���������ش˷���������ݣ�ͬʱ���л������� ���� getBufferData().clear();
	 * getBufferData().adddVOToBuffer(aVo); //����б�setListHeadData(queryVos);
	 * //���õ��ݲ���״̬ getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	 */
	protected void onBoQuery() throws Exception {

		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}

	/**
	 * �������������strWhere��ѯ���������
	 * 
	 * @param strWhere
	 * @return �鵽����������
	 * @throws Exception
	 * @throws ClassNotFoundException
	 */
	protected SuperVO[] queryHeadVOs(String strWhere) throws Exception,
			ClassNotFoundException {
		SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(
				Class.forName(getUIController().getBillVoName()[1]),
				getUIController().getBillType(), strWhere.toString());
		return queryVos;
	}

	/**
	 * ������ѯ�Ի������û�ѯ�ʲ�ѯ������ ����û��ڶԻ������ˡ�ȷ��������ô����true,���򷵻�false
	 * ��ѯ����ͨ�������StringBuffer���ظ�������
	 * 
	 * @param sqlWhereBuf
	 *            �����ѯ������StringBuffer
	 * @return �û�ѡȷ������true���򷵻�false
	 */
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
			throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null || strWhere.trim().length()==0)
			strWhere = "1=1";

		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// ҵ�����ͱ���
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// ҵ������
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		if (getHeadCondition() != null)
			strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	}

	/**
	 * ��ָ����VO���� <I>resultVOs </I>ȥ����BillUIBuffer.����������Ȱ�Buffer��ԭ�е�������ա�
	 * ���ָ��resultVOsΪ��Buffer�����������CurrentRow������Ϊ-1 ����CurrentRow����Ϊ��0��
	 * 
	 * @throws Exception
	 */
	final protected void updateBuffer() throws Exception // ��ʱ�ĳ�final,ȷ��Ŀǰ��û���˼̳й���
	{

		if (getBufferData().getVOBufferSize() != 0) {

			getBillUI().setListHeadData(
					getBufferData().getAllHeadVOsFromBuffer());
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			getBufferData().setCurrentRow(0);
		} else {
			getBillUI().setListHeadData(null);
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBufferData().setCurrentRow(-1);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000066")/* @res "û�в鵽�κ���������������!" */);
		}
	}

	/*
	 * �������ݵ�BillUIBuffer ������ӵ������������Ҫ�����򸲸Ǹ÷���
	 */
	protected void addDataToBuffer(SuperVO[] queryVos) throws Exception {
		if (queryVos == null) {
			getBufferData().clear();
			return;
		}
		for (int i = 0; i < queryVos.length; i++) {
			AggregatedValueObject aVo = (AggregatedValueObject) Class.forName(
					getUIController().getBillVoName()[0]).newInstance();
			aVo.setParentVO(queryVos[i]);
			getBufferData().addVOToBuffer(aVo);
		}
	}

	/**
	 * ��ťm_boRefresh���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoRefresh() throws Exception {
		// ��������
		try {
			getBufferData().refresh();
		} catch (RecordNotFoundExcetption e)// �����ǰ�ļ�¼��ɾ���˽��׳�����쳣
		{
			if (getBillUI().showYesNoMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000185")/*
													 * @res
													 * "��ǰ�ĵ����Ѿ���ɾ���ˣ��Ƿ���Ҫ�ڽ����ϱ����������?"
													 */) != MessageDialog.ID_YES) {
				getBufferData().removeCurrentRow();

			}

		}

	}

	/**
	 * ��ťm_boReturn���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoReturn() throws Exception {
	}

	/**
	 * ��ťm_boSave���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	protected void onBoSave() throws Exception {
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// �����������
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// �ж��Ƿ��д�������
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			if (getBillUI().isSaveAndCommitTogether())
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			else

				// write to database
				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
		}

		// �������ݻָ�����
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			} else {
				getBufferData().addVOsToBuffer(
						new AggregatedValueObject[] { billVO });
				nCurrentRow = getBufferData().getVOBufferSize() - 1;
			}
		}

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
		}
		
		setAddNewOperate(isAdding(), billVO);

		// ���ñ����״̬
		setSaveOperateState();
		
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
	}

	/**
	 * ҵ�������
	 */
	protected void onBusinessException(BusinessException e) {
		MessageDialog.showErrorDlg(getBillUI(), null, e.getMessage());
		Logger.error(e.getMessage(), e);

	}

	/**
	 * Button���¼���Ӧ���� �������ڣ�(2004-1-6 17:20:57)
	 * 
	 * @param bo
	 *            nc.ui.pub.ButtonObject �쳣˵����
	 */
	public void onButton(ButtonObject bo) {
		if (getBillUI().getBillOperate() == IBillOperate.OP_ADD
				|| getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			if (getBillCardPanelWrapper() != null)
				getBillCardPanelWrapper().getBillCardPanel().stopEditing();

		}
		try {
			ButtonObject parentBtn = bo.getParent();

			if (parentBtn != null && Integer.parseInt(parentBtn.getTag()) < 100) {
				int intParentBtn = Integer.parseInt(parentBtn.getTag());
				complexOnButton(intParentBtn, bo);
			} else {
				if (bo.getTag() == null)
					System.out.println("������ť��������TAG,TAG>100������.....");
				int intBtn = Integer.parseInt(bo.getTag());
				if (intBtn > 100)// ��Ŵ���100��Ϊ���Զ��尴ť
					onBoElse(intBtn);
				else
					// ������Ϊ��Ԥ�ð�ť
					simpleOnButton(intBtn, bo);
			}
//		} catch (BusinessException ex) {
//			onBusinessException(ex);
//		} catch (SQLException ex) {
//			getBillUI().showErrorMessage(ex.getMessage());
		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * �������ε��ݽ��в𵥵Ĵ��� �������ڣ�(2003-6-14 14:45:54)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject[]
	 * @param refVos
	 *            nc.vo.pub.AggregatedValueObject[]
	 */
	protected AggregatedValueObject[] onSplitBillVos(
			AggregatedValueObject[] refVos) throws Exception {
		return null;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-8 16:33:05)
	 * 
	 * @param vos
	 *            nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	protected void processCopyedBodyVOsBeforePaste(
			CircularlyAccessibleValueObject[] vos) {
		if (vos == null)
			return;

		for (int i = 0; i < vos.length; i++) {
			vos[i].setAttributeValue(getUIController().getPkField(), null);
			vos[i].setAttributeValue(getUIController().getChildPkField(), null);
		}
	}

	/**
	 * ���ӵ��ݵĴ������vos==null,��Ϊ�������� elseΪ���մ����ص���ѡ�񵥾�VO���� �������ڣ�(2003-1-6 10:31:53)
	 * 
	 * nc.vo.pub.AggregatedValueObject
	 */
	protected AggregatedValueObject refVOChange(AggregatedValueObject[] vos)
			throws Exception {
		return vos[0];
	}

	private void setCheckManAndDate(AggregatedValueObject vo) throws Exception {
		// �����������ڡ�������
		vo.getParentVO().setAttributeValue(getBillField().getField_CheckDate(),
				getBillUI()._getDate());
		vo.getParentVO().setAttributeValue(getBillField().getField_CheckMan(),
				getBillUI()._getOperator());
	}

	/**
	 * ���ఴť���¼����� �������ڣ�(2004-2-25 21:04:27)
	 * 
	 * @param intBtn
	 *            int
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void simpleOnButton(int intBtn, ButtonObject bo)
			throws java.lang.Exception {
		buttonActionBefore(getBillUI(), intBtn);
		switch (intBtn) {
		case IBillButton.Add: {
			if (!getBillUI().isBusinessType().booleanValue()) {
				getBillUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"uifactory", "UPPuifactory-000061")/*
																	 * @res
																	 * "��ʼ�������ӵ��ݣ���ȴ�......"
																	 */);
				onBoAdd(bo);
				// ����ִ�к���
				buttonActionAfter(getBillUI(), intBtn);
			}
			break;
		}
		case IBillButton.Edit: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000067")/*
													 * @res "��ʼ���б༭���ݣ���ȴ�......"
													 */);
			onBoEdit();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			break;
		}
		case IBillButton.Del: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000068")/*
													 * @res "��ʼ�������ϵ��ݣ���ȴ�......"
													 */);
			onBoDel();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000069")/* @res "�������ϲ�������" */
			);
			break;
		}
		case IBillButton.Delete: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000070")/*
													 * @res "��ʼ���е���ɾ������ȴ�......"
													 */);
			onBoDelete();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")/* @res "����ɾ�����" */
			);
			break;
		}
		case IBillButton.Query: {
			getBillUI().showHintMessage(bo.getName());

			if (super.getUIController() instanceof ISingleController) {
				ISingleController strl = (ISingleController) super
						.getUIController();
				if (strl.isSingleDetail())
					onBoBodyQuery();
				else
					onBoQuery();
			} else
				onBoQuery();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);

			break;
		}
		case IBillButton.Save: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000072")/*
													 * @res "��ʼ���е��ݱ��棬��ȴ�......"
													 */);
			onBoSave();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000073")/* @res "���ݱ����������" */
			);
			break;
		}
		case IBillButton.Cancel: {
			onBoCancel();
			// �����ʾ״̬����ʾ��Ϣ
			getBillUI().showHintMessage("");
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			break;
		}
		case IBillButton.Print: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000074")/*
													 * @res "��ʼ���д�ӡ���ݣ���ȴ�......"
													 */);
			onBoPrint();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000075")/* @res "���ݴ�ӡ��������" */
			);
			break;
		}
		case IBillButton.DirectPrint: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000074")/*
													 * @res "��ʼ���д�ӡ���ݣ���ȴ�......"
													 */);
			onBoDirectPrint();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000075")/* @res "���ݴ�ӡ��������" */
			);
			break;
		}

		case IBillButton.Return: {
			onBoReturn();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			break;
		}
		case IBillButton.Card: {
			onBoCard();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			break;
		}
		case IBillButton.Refresh: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000076")/*
													 * @res "��ʼ����ˢ�µ��ݣ���ȴ�......"
													 */);
			onBoRefresh();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000077")/* @res "����ˢ�²�������" */
			);

			break;
		}
		case IBillButton.Refbill: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000078")/*
													 * @res "��ʼ���в��յ��ݣ���ȴ�......"
													 */);
			onBillRef();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000079")/* @res "���ݲ��ղ�������" */
			);
			break;
		}
		case IBillButton.Copy: {
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000080")/*
													 * @res "��ʼ�������ݸ��ƣ���ȴ�......"
													 */);
			onBoCopy();
			// ����ִ�к���
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000081")/* @res "���ݸ��Ʋ�������" */
			);
			break;
		}
		case IBillButton.Audit: {
			// ��Ϊ�������ʾ�����������Թ淶������ɾ������
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res "��ʼִ�в���,��ȴ�..."
			// */);
			onBoAudit();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res "ִ�����. "
			// */
			// );
			break;
		}
		case IBillButton.CancelAudit: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res ��ʼִ�в���, ��ȴ�...
			// */
			// );
			onBoCancelAudit();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res ִ�����.
			// */
			// );
			break;
		}
		case IBillButton.Commit: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res ��ʼִ�в���, ��ȴ�...
			// */
			// );
			onBoCommit();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res ִ�����.
			// */
			// );
			break;
		}
		case IBillButton.SelAll: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res ��ʼִ�в���, ��ȴ�...
			// */
			// );

			onBoSelAll();

			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res ִ�����.
			// */
			// );
			break;
		}
		case IBillButton.SelNone: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res ��ʼִ�в���, ��ȴ�...
			// */
			// );
			onBoSelNone();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res ִ�����.
			// */
			// );
			break;
		}
		case IBillButton.ImportBill: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res ��ʼִ�в���, ��ȴ�...
			// */
			// );
			onBoImport();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res ִ�����.
			// */
			// );
			break;
		}
		case IBillButton.ExportBill: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res ��ʼִ�в���, ��ȴ�...
			// */
			// );
			onBoExport();
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res ִ�����.
			// */
			// );
			break;
		}
		case IBillButton.ApproveInfo: {
			onBoApproveInfo();
			break;
		}
		default: {
			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000179")/*
			// * @res ��ʼִ�в���, ��ȴ�...
			// */
			// );

			onBoActionElse(bo);

			// getBillUI().showHintMessage(
			// nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
			// "UPPuifactory-000180")/*
			// * @res ִ�����.
			// */
			// );
			break;
		}
		}

	}

	public void onBoApproveInfo() throws Exception {
		String strBilltype = getBillField().getField_Billtype();
		String billType = (String) getBufferData().getCurrentVO().getParentVO()
				.getAttributeValue(strBilltype);
		String billId = (String) getBufferData().getCurrentVO().getParentVO()
				.getPrimaryKey();

		// XXX:leijun+�������ʵ����û�е����������ԣ���ȡUI�������еĵ�������
		if (StringUtil.isEmptyWithTrim(billType))
			billType = getUIController().getBillType();

		FlowStateDlg dlg = new FlowStateDlg(getBillUI().getParent(), billType,
				IPFConfigInfo.STATEBUSINESSTYPE, billId);
		dlg.setVisible(true);

	}

	/**
	 * ���ݵ���
	 * 
	 * @throws Exception
	 */
	protected void onBoExport() throws Exception {
		// int ncount = getBillUI().getBufferData().getVOBufferSize();
		// AggregatedValueObject[] billVOs = new HYBillVO[ncount];
		// for (int i = 0; i < ncount; i++)
		// {
		// billVOs[i] = getBillUI().getBufferData().getVOByRowNo(i);
		// }
		// IBillExport billExport = getBillExportTool();
		// SwapInfoVO swapinfo = getSwapInfo();
		// JFileChooser chooser = new JFileChooser();
		// BillDataFileFilter filter = new BillDataFileFilter();
		// chooser.setFileFilter(filter);
		// int returnVal = chooser.showSaveDialog(getBillUI());
		// if (returnVal == JFileChooser.APPROVE_OPTION)
		// {
		// billExport.exportBill(billVOs, swapinfo, chooser.getSelectedFile()
		// .getPath());
		// }
	}

	/**
	 * ȡ�ý�����Ϣ����
	 * 
	 * @return
	 */
	// protected SwapInfoVO getSwapInfo()
	// {
	// SwapInfoVO swapinfo = new SwapInfoVO();
	// swapinfo.setAccount(ClientEnvironment.getInstance().getAccount()
	// .getAccountCode());
	// swapinfo.setBilltype(getUIController().getBillType());
	// swapinfo.setFilename("");
	// swapinfo.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());
	// swapinfo.setReceiver("");
	// swapinfo.setSender("NC");
	// swapinfo.setIsExchange("Y");
	// swapinfo.setReplace("Y");
	// return swapinfo;
	// }
	/**
	 * ȡ�õ��ݵ�������,Ĭ��ΪVOToXML
	 * 
	 * @return
	 */
	// protected IBillExport getBillExportTool()
	// {
	// return new VOToXML();
	// }
	/**
	 * ���ݵ���
	 * 
	 * @throws Exception
	 */
	protected void onBoImport() throws Exception {
		// JFileChooser chooser = new JFileChooser();
		// BillDataFileFilter filter = new BillDataFileFilter();
		// chooser.setFileFilter(filter);
		// int returnVal = chooser.showOpenDialog(getBillUI());
		// if (returnVal == JFileChooser.APPROVE_OPTION)
		// {
		// //���뵥��
		// String filename = chooser.getSelectedFile().getPath();
		// IBillImport importBill = new XMLToVO();
		// Object[] result = importBill.importBill(filename, ClientEnvironment
		// .getInstance().getAccount().getAccountCode(),
		// getUIController().getBillType());
		// //�ŵ�������
		// if (result == null || result.length == 0)
		// {
		// throw new BusinessException("û�еõ��κε�������ݣ�");
		// }
		// //����Ǿۺ�VO
		// if (result[0] instanceof AggregatedValueObject)
		// {
		// AggregatedValueObject[] billvos = new
		// AggregatedValueObject[result.length];
		// for (int i = 0; i < result.length; i++)
		// {
		// billvos[i] = (AggregatedValueObject) result[i];
		// }
		// addAggDataToUI(billvos);
		// }
		// //����ǵ�������
		// else if (result[0] instanceof SuperVO)
		// {
		// SuperVO[] billvos = new SuperVO[result.length];
		// for (int i = 0; i < result.length; i++)
		// {
		// billvos[i] = (SuperVO) result[i];
		// }
		// updateBuffer();
		// }
		// else if (result[0] instanceof ValueObject)
		// {
		// throw new BusinessException("��ʱ��֧��ValueObject���ݵ��룡");
		// }
		// }
	}

	private IBillBusiListener m_bbl = null;

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-8 12:55:35)
	 * 
	 * nc.ui.pub.ButtonObject �쳣˵����
	 */
	protected void addBillBusiListener(IBillBusiListener bbl) {
		m_bbl = bbl;
	}

	/**
	 * ���м�鵥��״̬�Ƿ�һ��,���״̬���ڵ�ǰָ��״̬�����return true����ȡ���� �������ڣ�(2004-6-11 17:33:18)
	 * 
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected boolean checkVOStatus(AggregatedValueObject vo, int[] intStatus)
			throws java.lang.Exception {
		if (vo == null || vo.getParentVO() == null)
			return true;
		Integer intState = (Integer) vo.getParentVO().getAttributeValue(
				getBillField().getField_BillStatus());
		if (intState == null)
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("uifactory", "UPPuifactory-000082")/*
																	 * @res
																	 * "����ģ��:����״̬vbillstatus�Ĵ������!"
																	 */);
		int intCurrentState = intState.intValue();
		for (int i = 0; i < intStatus.length; i++) {
			if (intStatus[i] == intCurrentState)
				return true;
		}
		return false;
	}

	/**
	 * ʵ����������в�ֵ���VO�Ĵ���, ������в�ֵ��ݴ�����Ҫ���ظ÷��� �������ڣ�(2004-1-3 18:13:36)
	 */
	protected IBusinessSplit createBusinessSplit() {
		return new DefaultBusinessSplit();
	}

	/**
	 * �ڲ�ѯ����ʱʹ�ã��Ƿ��Ƿ�ҵ�����ͱ������ҵ������pk Ĭ��Ϊfalse ��ʾʹ��ҵ������pk�� ����������������д
	 * �������ڣ�(2004-5-26 14:08:42)
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getBillIsUseBusiCode() {
		return new UFBoolean(false);
	}

	/**
	 * ����ӱ����ݡ� �������ڣ�(2004-3-11 17:44:14)
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
	 */
	private CircularlyAccessibleValueObject[] getChildVO(
			AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject[] childVos = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}

	/**
	 * �����Ӧ�Ĺ̶���ѯ������ �������ڣ�(2002-12-27 16:58:18)
	 * 
	 * @return java.lang.String
	 */
	protected String getHeadCondition() {
		// ��˾
		if (getBillCardPanelWrapper() != null)
			if (getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					getBillField().getField_Corp()) != null)
				return getBillField().getField_Corp() + "='"
						+ getBillUI()._getCorp().getPrimaryKey() + "'";
		return null;
	}

	/**
	 * ��ʼ����NodeKey��ť �������ڣ�(2002-12-23 10:11:27)
	 */
	public final void initNodeKeyButton() {
		try {
			ButtonObject boNodeKey = getButtonManager().getButton(
					IBillButton.NodeKey);
			// NodeKey����ģ��
			if (boNodeKey != null) {
				if (boNodeKey.getChildButtonGroup() != null
						&& boNodeKey.getChildButtonGroup().length > 0) {
					ButtonObject bo = boNodeKey.getChildButtonGroup()[0];
					bo.setSelected(true);

					// NodeKey
					getBillUI().setNodeKey(bo.getTag());
				} else
					System.out.println("û�г�ʼ��NodeKey����!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getBillUI().showHintMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000083")/*
													 * @res "��ʼ��ҵ��NodeKey��ť����"
													 */);
		}
	}

	/**
	 * ���в��յ���ʱ�Ƿ���Ҫ���ݽ����� �������ڣ�(2004-4-5 19:12:29)
	 * 
	 * @return boolean
	 */
	protected boolean isDataChange() {
		return true;
	}

	/**
	 * ���յ��ݽ����Ƶ��� �������ڣ�(2003-6-12 10:01:28)
	 */
	protected void onBillRef(int intBtn, String refBilltype) throws Exception {
		ButtonObject btn = getButtonManager().getButton(intBtn);
		btn.setTag(refBilltype + ":");
		onBoBusiTypeAdd(btn, null);
	}

	/**
	 * ��ťm_boNodekey���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
	private final void onBoNodekey(ButtonObject bo) throws Exception {
		bo.setSelected(true);
		// ����NodeKey
		getBillUI().setNodeKey(bo.getTag());

		// ����ˢ��UI
		getBillUI().initUI();
		// ��ղ�ѯģ��
		setQueryUI(null);
		// ���UI����
		getBillUI().getBufferData().clear();
		getBillUI().getBufferData().setCurrentRow(-1);

		getBillUI().updateButtonUI();
	}

	/**
	 * ��ťm_boSelAll���ʱִ�еĶ���, ���б�Ҫ���븲��.
	 */
	protected void onBoSelAll() throws Exception {
		return;
	}

	/**
	 * ��ťm_boSelNone���ʱִ�еĶ���, ���б�Ҫ���븲��.
	 */
	protected void onBoSelNone() throws Exception {
		return;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-1-8 12:55:35)
	 * 
	 * nc.ui.pub.ButtonObject �쳣˵����
	 */
	protected void removeBillBusiListener() {
		m_bbl = null;
	}

	/**
	 * ���������Ĳ������� �������ڣ�(2004-4-1 10:11:58)
	 */
	protected void setAddNewOperate(boolean isAdding,
			AggregatedValueObject billVO) throws Exception {
		
	}

	/**
	 * ���ý������ݣ���ִ��������ظ÷����� �������ڣ�(2004-4-1 8:20:25)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void setRefData(AggregatedValueObject[] vos)
			throws java.lang.Exception {
		// ���õ���״̬
		getBillUI().setCardUIState();

		AggregatedValueObject vo = refVOChange(vos);
		if (vo == null)
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("uifactory", "UPPuifactory-000084")/*
																	 * @res
																	 * "δѡ����յ���"
																	 */);
		// ����Ϊ��������
		getBillUI().setBillOperate(IBillOperate.OP_REFADD);
		// ������
		getBillCardPanelWrapper().setCardData(vo);
	}

	/**
	 * ���ñ�������״̬�� �������ڣ�(2004-4-1 10:11:58)
	 */
	protected void setSaveOperateState() throws Exception {
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	}

	/**
	 * ����Buffer�е�TS����ǰ����VO�� �������ڣ�(2004-5-14 18:04:59)
	 * 
	 * @param setVo
	 *            nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void setTSFormBufferToVO(AggregatedValueObject setVo)
			throws java.lang.Exception {
		if (setVo == null)
			return;
		AggregatedValueObject vo = getBufferData().getCurrentVO();
		if (vo == null)
			return;
		if (getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {

			if (vo.getParentVO() != null && setVo.getParentVO() != null)
				setVo.getParentVO().setAttributeValue("ts",
						vo.getParentVO().getAttributeValue("ts"));
			// SuperVO[] changedvos = (SuperVO[]) setVo.getChildrenVO();
			SuperVO[] changedvos = (SuperVO[]) getChildVO(setVo);

			if (changedvos != null && changedvos.length != 0) {
				// �����������е��ֱ�����
				HashMap bufferedVOMap = null;
				// SuperVO[] bufferedVOs = (SuperVO[]) vo.getChildrenVO();
				SuperVO[] bufferedVOs = (SuperVO[]) getChildVO(vo);
				if (bufferedVOs != null && bufferedVOs.length != 0) {
					bufferedVOMap = Hashlize.hashlizeObjects(bufferedVOs,
							new VOHashPrimaryKeyAdapter());
					for (int i = 0; i < changedvos.length; i++) {
						if (changedvos[i].getPrimaryKey() != null) {
							ArrayList bufferedAl = (ArrayList) bufferedVOMap
									.get(changedvos[i].getPrimaryKey());

							if (bufferedAl != null) {
								SuperVO bufferedVO = (SuperVO) bufferedAl
										.get(0);
								changedvos[i].setAttributeValue("ts",
										bufferedVO.getAttributeValue("ts"));
							}
						}
					}
				}
			}
		}
	}

	public IBillBusiListener getBillBusiListener() {
		return m_bbl;
	}
}