/*
 * �������� 2006-2-13
 *
 */
package nc.ui.bi.integration.dimension;

import java.util.ArrayList;
import java.util.List;

import nc.pub.iufo.exception.CommonException;
import nc.ui.bi.web.reference.DimensionRefRefAction;
import nc.ui.iufo.resmng.common.ResWebEnvKit;
import nc.ui.iufo.resmng.common.ResWebParam;
import nc.ui.iufo.resmng.uitemplate.IResTreeObjForm;
import nc.ui.iufo.resmng.uitemplate.ResEditObjAction;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.util.iufo.pub.IDMaker;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.integration.dimension.DimMemberSrv;
import nc.vo.bi.integration.dimension.DimMemberVO;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.integration.dimension.DimensionException;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.integration.dimension.IDimension;
import nc.vo.iufo.pub.InputvalueCheck;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResOperException;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.pub.ValueObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebChoice;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebRadioGroup;
import com.ufida.web.comp.WebTextField;
import com.ufida.web.comp.WebTextRef;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.Script;
import com.ufsoft.iufo.resource.StringResource;

/**
 * �༭ά�ȵ�Action��
 * 
 */
public class DimensionEditAction extends ResEditObjAction {

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getSaveActionName()
	 */
	protected String getSaveActionName() {
		return DimensionEditAction.class.getName();
	}

	public ActionForward onDimTypeChanged(ActionForm actionForm) {
		String dimType = getRequestParameter("dimType");
		String createType = null;
		String refDimID = null;
		String str = getCreatePanel(ResWebEnvKit.isModify(this), dimType,
				createType, refDimID).toString();
		ajax(str);
		return null;
	}

	/**
	 * ִ�и��½ڵ����
	 * 
	 * @param selResTreeObj
	 * @throws ResOperException
	 */
	protected void doUpdateObj(IResTreeObject selResTreeObj)
			throws ResOperException {
		super.doUpdateObj(selResTreeObj);

		// ���³�Ա���ڵ�
		DimensionVO dimVO = (DimensionVO) selResTreeObj.getSrcVO();
		// ���������,�򲻸��¸���Ա
		if (dimVO.getReferDim() == null) {
			DimMemberSrv memSrv = new DimMemberSrv(dimVO);
			DimMemberVO root = memSrv.getRoot();
			root.setMemname("_" + dimVO.getDimname());
			root.setMemcode("_" + dimVO.getDimcode());
			memSrv.update(new DimMemberVO[] { root });
		}
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getLabelObjName()
	 */
	protected String getLabelObjName() {
		return null;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getLabelObjNote()
	 */
	protected String getLabelObjNote() {
		return null;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#isEditable(nc.vo.iufo.resmng.uitemplate.IResTreeObject)
	 */
	protected boolean isEditable(IResTreeObject resTreeObj) {
		return true;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#doGetUpdateVO(nc.ui.iufo.resmng.uitemplate.IResTreeObjForm,
	 *      nc.vo.pub.ValueObject)
	 */
	protected ValueObject doGetUpdateVO(IResTreeObjForm resTreeObjForm,
			ValueObject srcVO) {
		DimensionVO dimVO;
		try {
			dimVO = (DimensionVO) DimensionSrv.getInstance().getDimByID(
					((DimensionVO) srcVO).getDimID());
		} catch (Exception e) {
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
			throw new DimensionException(e.getMessage());
		}

		if (dimVO == null) {
			throw new DimensionException(DimensionException.ERR_NO_DIMDEF);
		}
		dimVO = (DimensionVO) dimVO.clone();
		convertForm2VO((DimensionForm) resTreeObjForm, dimVO);
		return dimVO;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#doGetNewVO(java.lang.String,
	 *      nc.ui.iufo.resmng.uitemplate.IResTreeObjForm,
	 *      nc.ui.iufo.resmng.common.ResWebParam)
	 */
	protected ValueObject doGetNewVO(String strParentVOPK,
			IResTreeObjForm resTreeObjForm, ResWebParam resWebParam) {
		DimensionVO dimVO = new DimensionVO();
		dimVO.setDimID(IDMaker.makeID(20));
		dimVO.setOwnerid(this.getCurUserInfo().getID());
		// �½�ά��
		convertForm2VO((DimensionForm) resTreeObjForm, dimVO);
		dimVO.setPk_folderid(strParentVOPK);
		dimVO.setOwnerid(getCurUserInfo().getID());
		return dimVO;
	}

	/**
	 * �õ�Form���� ˵���� ��д���෽��
	 * 
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getFormName()
	 */
	public String getFormName() {
		return DimensionForm.class.getName();
	}

	/**
	 * �õ��½����޸�����Ľ������� ˵���� ��д���෽��
	 * 
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#getExecuteUI()
	 */
	protected String getExecuteUI() {
		return DimensionEditDlg.class.getName();
	}

	/**
	 * ��д���෽��
	 * 
	 * @param form
	 * @param selResTreeObj
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#initFormValue(ActionForm,
	 *      IResTreeObject)
	 */
	protected void initFormValue(ActionForm actionForm,
			IResTreeObject selResTreeObj) {
		super.initFormValue(actionForm, selResTreeObj);
		DimensionForm form = (DimensionForm) actionForm;
		form.setModifyUI(ResWebEnvKit.isModify(this));
		String dimType = String.valueOf(IDimension.STANDARD_DIMENSION_TYPE);
		String createType = DimensionEditDlg.RD_NEW_VALUE;
		String refDimID = "";
		// ��ʾtextField��ֵ
		if (selResTreeObj != null && ResWebEnvKit.isModify(this)
				&& selResTreeObj.getSrcVO() != null) {
			DimensionVO dimVO = (DimensionVO) selResTreeObj.getSrcVO();

			form.setDimName(dimVO.getDimname());
			form.setDimCode(dimVO.getDimcode());
			form.setDimType(dimVO.getDimensionType().toString());
			form.setDimNote(dimVO.getNote());
			
			dimType = dimVO.getDimensionType().toString();
			if (dimVO.getReferDim() != null
					&& dimVO.getReferDim().trim().length() > 0) {
				createType = DimensionEditDlg.RD_REFER_VALUE;
				refDimID = dimVO.getReferDim();
			}
		}

		form.setCreateTypePanel(getCreatePanel(ResWebEnvKit.isModify(this),
				dimType, createType, refDimID));
	}

	private WebPanel getCreatePanel(boolean isModify, String dimType,
			String createType, String refDimID) {
		WebPanel m_createPanel = new WebPanel();
		WebGridLayout subLayout = new WebGridLayout(4, 3);
		m_createPanel.setLayout(subLayout);

		WebChoice m_cmbType = new WebChoice();
		m_cmbType.setID("dimType");
		m_cmbType.setName("dimType");
		m_cmbType.setDisabled(isModify);
		m_cmbType.setOnChange("onDimTypeChanged();");
		WebPanel pane = new WebPanel(1, 2);
		pane.add(new WebLabel(DimensionEditDlg.LBL_DIMTYPE), new Area(1, 1, 1,
				1));
		pane.add(m_cmbType, new Area(1, 2, 1, 1));
		m_createPanel.add(pane, new Area(1, 1, 1, 1));

		WebLabel lbl_choice = new WebLabel();
		m_createPanel.add(lbl_choice, new Area(2, 1, 1, 1));

		WebRadioGroup m_rdCreateType = new WebRadioGroup();
		m_rdCreateType.setID("createType");
		m_rdCreateType.setName("createType");
		m_rdCreateType.setDisabled(isModify);
		m_rdCreateType.setOnClick("onDimCreateTypeChanged();");
		m_createPanel.add(m_rdCreateType, new Area(3, 1, 2, 1));

		WebTextRef m_cmbRefer = new WebTextRef();
		m_cmbRefer.setID("refDimID");
		m_cmbRefer.setName("refDimID");
		m_cmbRefer.setReadonly(true);
		m_cmbRefer.setDisabled(true);
		Script spt = new Script();
		spt.addFuncLine("textRef_setEnable('" + refDimID + "', false);");

		m_cmbType
				.setItems(new String[][] {
						new String[] {
								String
										.valueOf(IDimension.STANDARD_DIMENSION_TYPE),
								StringResource
										.getStringResource(DimRescource.DIM_COMM_ID) },
						new String[] {
								String.valueOf(IDimension.TIME_DIMENSION_TYPE),
								StringResource
										.getStringResource(DimRescource.DIM_TIME_ID) } });
		m_cmbType.setValue(dimType);

		lbl_choice.setValue(DimensionEditDlg.LBL_CREATE_MODE);
		m_rdCreateType.setItems(new String[][] {
				new String[] { DimensionEditDlg.RD_NEW_VALUE,
						DimensionEditDlg.RD_NEW },
				new String[] { DimensionEditDlg.RD_REFER_VALUE,
						DimensionEditDlg.RD_REFER } });
		m_rdCreateType
				.setValue(createType == null ? DimensionEditDlg.RD_NEW_VALUE
						: createType);

		ActionForward refActionForward = new ActionForward(
				DimensionRefRefAction.class.getName(), "");
		refActionForward.addParameter(
				DimensionRefRefAction.PARAM_REFER_DIM_TYPE, dimType);
		m_cmbRefer.setActionForward(refActionForward);
		m_cmbRefer.setValue(refDimID);
		try {
			if ((refDimID != null) && (refDimID.trim().length() != 0)) {
				DimensionVO dim = DimensionSrv.getInstance().getDimByID(
						refDimID);
				if ((dim != null)
						&& (m_cmbRefer.getRefFld() instanceof WebTextField)) {
					((WebTextField) m_cmbRefer.getRefFld()).setValue(dim
							.getDimname());
				}
			}
		} catch (Exception e) {
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
		}

		m_createPanel.add(m_cmbRefer, new Area(4, 2, 1, 1));

		// m_createPanel.add(spt, new Area(4, 3, 1, 1));

		return m_createPanel;
	}

	private void convertForm2VO(DimensionForm form, DimensionVO dimVO) {
		dimVO.setDimname(form.getDimName());
		dimVO.setDimcode(form.getDimCode());
		dimVO.setNote(form.getDimNote());

		if (!isModify(form)) {
			dimVO.setDimensionType(Integer.valueOf(form.getDimType()));
			if (DimensionEditDlg.RD_REFER_VALUE.equalsIgnoreCase(form
					.getCreateType())
					&& (form.getRefDimID() != null)
					&& (form.getRefDimID().trim().length() != 0)) {
				dimVO.setReferDim(form.getRefDimID());
			}
		}

	}

	/**
	 * FormֵУ��
	 * 
	 * @param actionForm
	 * @return ֵУ��ʧ�ܵ���ʾ��Ϣ����
	 * @i18n miufo1003134=����
	 * @i18n miufo1001012=����
	 * @i18n mbidim00009=��ѡ�����õ�ά��
	 * @i18n mbidim00010=�����ظ�
	 * @i18n mbidim00011=��ǰĿ¼�������ظ�
	 */
	@SuppressWarnings("unchecked")
	public String[] validate(ActionForm actionForm) {
		if (actionForm == null) {
			return null;
		}
		UserInfoVO userInfo = getCurUserInfo();
		if (userInfo == null) {
			throw new CommonException("miufopublic390"); // ������ʱ�������µ�½��
		}

		List listStr = new ArrayList();
		DimensionForm form = (DimensionForm) actionForm;

		if (!InputvalueCheck.isValidName(form.getDimName())) { // У��ά������
			listStr.add(StringResource.getStringResource("miufo1003134")
					+ StringResource.getStringResource("miufo1003460")); // "���ڷǷ��ַ�"
		}
		if (!InputvalueCheck.isValidName(form.getDimCode())) {// У��ά�ȱ���
			listStr.add(StringResource.getStringResource("miufo1001012")
					+ StringResource.getStringResource("miufo1003460")); // "���ڷǷ��ַ�"
		}

		if (isModify(actionForm) == false) {
			if (form.getCreateType().equals(DimensionEditDlg.RD_REFER_VALUE)) {
				if (form.getRefDimID() == null || form.getRefDimID().trim().length() == 0)
					listStr
							.add(StringResource
									.getStringResource("mbidim00009"));
			}
			try {
				if (DimensionSrv.getInstance().getDimByCode(form.getDimCode()) != null) {
					listStr
							.add(StringResource
									.getStringResource("mbidim00010")); // "�����ظ�"
				}
				//#web�齨�Ż��ύ�������ܱ�֤��validate�������getTableSelectID��getTreeSelectIDһֱ��ֵ;����ҵ��Form�ﴫ�ݹ�����ֵ
				//modified by liulp:2007-12-03
		        String strDirPK = ResMngToolKit.getVOIDByTreeObjectID(form.getBizTreeSelectedID());
				if (DimensionSrv.getInstance().getDimByName(
						strDirPK, form.getDimName()) != null) {
					listStr
							.add(StringResource
									.getStringResource("mbidim00011"));
				}
			} catch (Exception e) {
			}
		}
		if (listStr.size() > 0) {
			String[] strReturns = new String[listStr.size()];
			listStr.toArray(strReturns);
			return strReturns;
		}
		return null;
	}

	/**
	 * ���ظ���ķ���������ʱ��ά�ȵ�������������г�Ա���� (non-Javadoc)
	 * 
	 * @see nc.ui.iufo.resmng.uitemplate.ResEditObjAction#actSave(com.ufida.web.action.ActionForm)
	 */
	protected ActionForward getSaveActForward(ActionForm actionForm) {
		ActionForward superFwd = super.getSaveActForward(actionForm);
		DimensionForm form = (DimensionForm) actionForm;
		if (isModify(actionForm))
			return superFwd;

		if ((form.getRefDimID() != null)
				&& (form.getRefDimID().trim().length() != 0))
			return superFwd;
		if (form.getDimType() != null
				&& form.getDimType()
						.equals("" + IDimension.TIME_DIMENSION_TYPE)) {
			ActionForward fwd = new ActionForward(CalendarDesignAction.class
					.getName(), CalendarDesignAction.METHOD_CALENDAR_DESIGN);
			/** TODO */
			IResTreeObjForm resForm = changetoResTreeObjForm(actionForm);
			fwd.addParameter(CalendarDesignAction.KEY_DIM_ID, ResMngToolKit
					.getVOIDByTreeObjectID(resForm.getID()));

			return fwd;
		} else
			return superFwd;
	}

}
